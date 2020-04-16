package com.bounce.atlas.utils;

import com.bounce.atlas.http.RecordApis;
import com.bounce.atlas.pojo.*;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.DatabaseConnector;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import freemarker.template.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.util.TextUtils;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Timestamp;
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

    public static ConfigPojo.Page getPageFromPagePath(String pagePath) {
        ConfigPojo.Page page = null;
        for (ConfigPojo.Page item : getConfig().getTabs()) {
            if (item.getPages() != null && item.getPages().size() > 0) {
                for (ConfigPojo.Page subItem : item.getPages()) {
                    if (subItem.getPath().equals(pagePath)) {
                        return subItem;
                    }
                }
            } else {
                if (item.getPageId().equals(pagePath)) {
                    return page;
                }
            }
        }

        return page;
    }

    public static List<List<String>> getDbRecords(ConfigPojo.Page page, String where, int limit) {
        String sql = "select * from " + page.getCrudConfig().getSchema() + "." + page.getCrudConfig().getTable();
        boolean isCustomQuery = false;
        if(TextUtils.isEmpty(where)) {
            sql += " limit " + limit;
            isCustomQuery = true;
        } else {
            sql += " where " + where;
        }
        List<List<String>> result = Lists.newLinkedList();

        try {
            Result<Record> records = DatabaseConnector.getDb()
                    .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                            page.getCrudConfig().getDbPassword())
                    .fetch(sql);

            if(records.size() > 0) {
                Record record = records.get(0);
                List<String> fieldList = Lists.newLinkedList();
                fieldList.add("");
                for(Field f : record.fields()) {
                    fieldList.add(f.getName());
                }
                result.add(fieldList);
            }
            for(Record record : records)  {
                List<String> recordList = Lists.newLinkedList();
                JSONObject buttonFunction = new JSONObject();
                if(page.getCrudConfig().isEditAllowed()) {
                    String pageId = page.getPageId();
                    int id = (int) record.get("id");
                    buttonFunction.put("edit", "\"" + pageId + "\"" + "," + id);
                } if(page.getCrudConfig().isDeleteAllowed()) {
                    String pageId = page.getPageId();
                    int id = (int) record.get("id");
                    buttonFunction.put("delete", "\"" + pageId + "\"" + "," + id);
                }
                recordList.add(buttonFunction.toString());
                for(Field f : record.fields()) {
                    Object obj = record.get(f.getName());
                    if(obj != null) {
                        recordList.add(obj.toString());
                    } else {
                        recordList.add("null");
                    }
                }
                result.add(recordList);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        if(isCustomQuery) {
            result = getDbRecords(page, null, 1);
            result.remove(1);
        }
        return result;
    }

    public static Map<String, Field> getColumns(ConfigPojo.Page page) {
        Map<String, Field> columns = Maps.newLinkedHashMap();

        String sql = "select * from " + page.getCrudConfig().getSchema() + "." + page.getCrudConfig().getTable() + " limit 1";
        Result<Record> records = DatabaseConnector.getDb()
                .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                        page.getCrudConfig().getDbPassword())
                .fetch(sql);

        if(records.size() > 0) {
            Record record = records.get(0);
            for(Field f : record.fields()) {
                columns.put(f.getName(), f);
            }
        }

        return columns;
    }

    public static Map<String, Object> getFormSchema(ConfigPojo.Page page) {
        Map<String, Object> formSchema = Maps.newLinkedHashMap();
        Map<String, Field> columns = getColumns(page);

        String infosql = "SELECT * FROM information_schema.columns WHERE table_schema = '" + page.getCrudConfig().getSchema() + "' AND table_name   = '" + page.getCrudConfig().getTable() + "';";
        Result<Record> columnRecords = DatabaseConnector.getDb()
                .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                        page.getCrudConfig().getDbPassword())
                .fetch(infosql);

        Map<String, Record> columnRecordMap = Maps.newLinkedHashMap();
        for(Record columnRecord : columnRecords) {
            columnRecordMap.put(columnRecord.get("column_name").toString(), columnRecord);
        }

        for(Map.Entry<String, Field> column : columns.entrySet()) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            map.put("title", column.getKey());
            if(column.getKey().equals("id")) {
                map.put("readonly", true);
            }
            if(columnRecordMap.get(column.getKey()).get("is_nullable").equals("NO")) {
                map.put("required", true);
            }
            switch (column.getValue().getDataType().getSQLDataType().getTypeName()) {
                case "varchar":
                    map.put("type", "string");
                    break;
                case "timestamp":
                    map.put("type", "datetime-local");
                    break;
                case "boolean":
                    map.put("type", "boolean");
                    break;
                case "integer":
                    map.put("type", "integer");
                    break;
                case "float":
                    map.put("type", "number");
                    break;
                case "other":
                    map.put("type", "string");
                    break;
                case "bigint":
                    map.put("type", "string");
                    break;
                default:
                    map.put("type", "string");
                    if(column.getValue().getDataType().isEnum()) {
                        try {
                            //mostly enum
                            String sql = "SELECT unnest(enum_range(NULL::" +
                                    column.getValue().getDataType().getSQLDataType().getTypeName() + "))::text";
                            Result<Record> records = DatabaseConnector.getDb()
                                    .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                                            page.getCrudConfig().getDbPassword()).fetch(sql);
                            List<String> enums = Lists.newLinkedList();
                            for (Record record : records) {
                                enums.add(record.getValue(0).toString());
                            }
                            map.put("enum", enums);
                        } catch (Exception e) {
                            BounceUtils.logError(e);
                            e.printStackTrace();
                        }
                    }

                    break;
            }
            formSchema.put(column.getKey(), map);
        }

        return formSchema;
    }

    public static Map<String, Object> getFormValues(ConfigPojo.Page page, int id) {
        Map<String, Object> formValues = Maps.newLinkedHashMap();

        String sql = "select * from " + page.getCrudConfig().getSchema() + "." + page.getCrudConfig().getTable() + " where id=" + id;
        Record record = DatabaseConnector.getDb()
                .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                        page.getCrudConfig().getDbPassword())
                .fetchOne(sql);

        for(Field field : record.fields()) {
            Object val = record.get(field.getName());
            if(field.getDataType().getSQLDataType().getTypeName().equals("timestamp") && val != null) {
                // convert timestamp to html time
                long timestamp = Timestamp.valueOf(val.toString()).getTime();
                val = Utils.toHtmlInputTimestamp(timestamp);

            }
            if(val != null) {
                formValues.put(field.getName(), val);
            }
        }
        return formValues;
    }

}
