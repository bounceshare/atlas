package com.bounce.atlas.utils;

import com.bounce.atlas.pojo.*;
import com.bounce.utils.BounceUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import freemarker.template.*;
import org.apache.commons.io.IOUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ContentUtils {

    public static final int VERSION = 1;

    public static Gson gson = new Gson();
    private static ConfigPojo config = null;

    public static String getContent(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try {
            InputStream inputStream = ContentUtils.class.getClassLoader().getResourceAsStream(filename);
            if (inputStream != null) {
                content.append(IOUtils.toString(inputStream));
            } else {
                throw new IOException("File not found");
            }
        } catch (Exception e) {
            BounceUtils.logError(e);
            e.printStackTrace();
        }
        return content.toString();
    }

    public static InputStream getContentAsStream(String filename) throws IOException {
        InputStream content = null;
        try {
            content = ContentUtils.class.getClassLoader().getResourceAsStream(filename);
            if (content == null) {
                throw new IOException("Resource not found");
            }
        } catch (Exception e) {
            BounceUtils.logError(e);
            e.printStackTrace();
        }
        return content;
    }

    public static String getFreemarkerString(String filename, Map<String, Object> templateData) {
        StringBuilder content = new StringBuilder();

        try {
            Version version = new Version(2, 3, 20);
            DefaultObjectWrapper defaultObjectWrapper = new DefaultObjectWrapperBuilder(version).build();
            Configuration cfg = new Configuration(version);
            cfg.setClassForTemplateLoading(ContentUtils.class, "/");
            cfg.setIncompatibleImprovements(version);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setObjectWrapper(defaultObjectWrapper);
            cfg.setLocale(Locale.US);

            StringWriter stringWriter = new StringWriter();

            Template template = cfg.getTemplate(filename);
            template.process(templateData, stringWriter);

            content.append(stringWriter.toString());
        } catch (Exception e) {
            BounceUtils.logError(e);
            e.printStackTrace();
        }

        return content.toString();
    }

    public static void addMarkersToFreemarkerObj(List<MarkerPojo> markers, Map<String, Object> data) {
        data.put("markers", data);
        data.put("markersString", gson.toJson(markers));
        data.put("panOut", "true");
    }

    public static void addFencesToFreemarkerObj(List<FencePojo> markers, Map<String, Object> data) {
        data.put("fences", data);
        data.put("fenceString", gson.toJson(markers));
        data.put("panOut", "true");
    }

    public static void addCirclesToFreemarkerObj(List<CirclePojo> markers, Map<String, Object> data) {
        data.put("circles", data);
        data.put("circleString", gson.toJson(markers));
        data.put("panOut", "true");
    }

    public static void addPathsToFreemarkerObj(List<PathPojo> markers, Map<String, Object> data) {
        data.put("paths", data);
        data.put("pathString", gson.toJson(markers));
        data.put("panOut", "true");
    }

    public static Map<String, Object> getDefaultFreemarkerObj(String page, boolean isAuth) {
        Map<String, Object> data = Maps.newHashMap();

        ConfigPojo config = getConfig();

        data.put("title", config.getTitle());
        data.put("page", page);
        data.put("favicon", config.getFavicon());
        data.put("logo", config.getLogo());

        data.put("tabs", getRootPages(isAuth));
        data.put("nestedTabs", getNestedPages(isAuth));
        if(isAuth) {
            data.put("auth", isAuth);
        }

        return data;
    }

    public static void updateConfigPojo(String configJson) throws Exception {
        ConfigPojo configPojo = new Gson().fromJson(configJson, ConfigPojo.class);
        Utils.redisSet("atlas.config", gson.toJson(configPojo));
        config = configPojo;
    }

    public static ConfigPojo getConfig() {
        try {
            try {
                if (config == null) {
                    String configString = Utils.redisGet("atlas.config", getContent("sample_config.json"));
                    ConfigPojo configPojo = gson.fromJson(configString, ConfigPojo.class);
                    config = configPojo;
                    return configPojo;
                } else {
                    return config;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ConfigPojo configPojo = new Gson().fromJson(getContent("sample_config.json"), ConfigPojo.class);
            return configPojo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<ConfigPojo.Page> getRootPages(boolean isAuth) {
        List<ConfigPojo.Page> pages = Lists.newArrayList();
        for (ConfigPojo.Page item : getConfig().getTabs()) {
            if (item.getPages() == null || item.getPages().size() < 1) {
                if(item.isAuth()) {
                    if(isAuth) {
                        item.setPageId(item.getPage());
                        pages.add(item);
                    }
                } else {
                    item.setPageId(item.getPage());
                    pages.add(item);
                }
            }
        }

        return pages;
    }

    private static Map<String, List<ConfigPojo.Page>> getNestedPages(boolean isAuth) {
        Map<String, List<ConfigPojo.Page>> map = Maps.newHashMap();
        for (ConfigPojo.Page tab : getConfig().getTabs()) {
            if (tab.getPages() != null && tab.getPages().size() > 0) {
                for (ConfigPojo.Page page : tab.getPages()) {
                    if(page.isAuth()) {
                        if(isAuth) {
                            page.setPageId(tab.getTabName());
                            List<ConfigPojo.Page> pages = map.get(tab.getTabName());
                            if (pages == null) {
                                pages = Lists.newArrayList();
                            }
                            pages.add(page);
                            map.put(tab.getTabName(), pages);
                        }
                    } else {
                        page.setPageId(tab.getTabName());
                        List<ConfigPojo.Page> pages = map.get(tab.getTabName());
                        if (pages == null) {
                            pages = Lists.newArrayList();
                        }
                        pages.add(page);
                        map.put(tab.getTabName(), pages);
                    }
                }
            }
        }

        return map;
    }

    public static ConfigPojo.Page getPage(String path, boolean isAuth) {
        ConfigPojo.Page page = null;
        for (ConfigPojo.Page item : getConfig().getTabs()) {
            if (item.getPages() != null && item.getPages().size() > 0) {
                for (ConfigPojo.Page subItem : item.getPages()) {
                    if (subItem.getPath().equals(path)) {
                        if(subItem.isAuth()) {
                            if(isAuth) {
                                page = subItem;
                                page.setPageId(item.getTabName());
                            }
                        } else {
                            page = subItem;
                            page.setPageId(item.getTabName());
                        }
                    }
                }
            } else {
                if (item.getPath().equals(path)) {
                    if(item.isAuth()) {
                        if(isAuth) {
                            page = item;
                            page.setPageId(page.getPage());
                        }
                    } else {
                        page = item;
                        page.setPageId(page.getPage());
                    }
                }
            }
        }

        return page;
    }

}
