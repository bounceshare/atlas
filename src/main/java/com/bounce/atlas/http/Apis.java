package com.bounce.atlas.http;

import com.bounce.atlas.pojo.*;
import com.bounce.atlas.utils.*;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Path("/")
public class Apis {

    @Context
    private HttpServletRequest httpRequest;
    @Context
    private HttpServletResponse httpResponse;

    private static Logger logger = Log.getLogger(Apis.class.getCanonicalName());
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void health(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/health");
        try {
            asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess(Maps.newLinkedHashMap()))).build());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
            asyncResponse.resume(Response.status(400).entity(gson.toJson(StatusPojo.buildFailure(400, "Error : " + e.getMessage()))).build());
        }
    }

    /**
     * WARNING
     *
     * @param asyncResponse
     */
    @POST
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void healthPost(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/health");
        logger.info("RequestParams : " + inputString);
        try {
            asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess(Maps.newLinkedHashMap()))).build());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
            asyncResponse.resume(Response.status(400).entity(gson.toJson(StatusPojo.buildFailure(400, "Error : " + e.getMessage()))).build());
        }
    }

    @GET
    @Path("/resources/{path:.+}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes({MediaType.APPLICATION_JSON})
    public InputStream resource(@PathParam("path") String path) {
        logger.info("/resource/" + path);
        try {
            return ContentUtils.getContentAsStream(path);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.logError(e);
        }
        return null;
    }

    @GET
    @Path("/files/{path:.+}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON})
    public String files(@PathParam("path") String path) {
        logger.info("/files/" + path);
        try {
            return ContentUtils.getContent(path);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.logError(e);
        }
        return "404.ftl";
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    public void login(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/login");

        Map<String, Object> data = ContentUtils.getDefaultFreemarkerObj("login", false, AuthUtils.getUserId(getToken()));

        String content = ContentUtils.getFreemarkerString("login.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/config")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void config(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/config");

        String userId = AuthUtils.getUserId(getToken());

        if(!AuthUtils.isAdmin(userId)) {
            logger.info("Unauthorized token : " + getToken());
            String redirectPath = "/404";
            UriBuilder builder =
                    UriBuilder.fromPath("").path(redirectPath);
            asyncResponse.resume(Response.temporaryRedirect(builder.build()).build());
            return;
        }

        Map<String, Object> data = ContentUtils.getDefaultFreemarkerObj("config", true, userId);
        data.put("config", gson.toJson(ContentUtils.getConfig()));

        String content = ContentUtils.getFreemarkerString("config.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/404")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    public void notFound404(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/404");

        Map<String, Object> data = ContentUtils.getDefaultFreemarkerObj("404", false, AuthUtils.getUserId(getToken()));
        data.put("config", gson.toJson(ContentUtils.getConfig()));

        String content = ContentUtils.getFreemarkerString("404.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @POST
    @Path("/config")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void configPost(String inputString, @Suspended final AsyncResponse asyncResponse) {
        try {

            String userId = AuthUtils.getUserId(getToken());

            if(!AuthUtils.isAdmin(userId)) {
                logger.info("Unauthorized token : " + getToken());
                asyncResponse.resume(Response.status(400).entity(gson.toJson(StatusPojo.buildFailure(401, "Couldn't update the config cause of permission issue"))).build());
                return;
            }

            logger.info("/config");
            JSONObject jsonObject = new JSONObject(inputString);
            String configData = jsonObject.optString("config");
            logger.info("Config To Update : " + configData);
            ContentUtils.updateConfigPojo(configData);
            asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess())).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        asyncResponse.resume(Response.status(400).entity(gson.toJson(StatusPojo.buildFailure(400, "Couldn't update the config"))).build());
    }

    @POST
    @Path("/test/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void testSearch(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/test/search");
        try {
            String sampleResponse = ContentUtils.getContent("sample_response.json");
            asyncResponse.resume(Response.ok().entity(sampleResponse).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/geofence/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void geofenceSearch(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/geofence/search");
        try {
            JSONObject jsonObject = new JSONObject(inputString);
            double lat = jsonObject.optDouble("lat", -1);
            double lon = jsonObject.optDouble("lon", -1);
            double radius = jsonObject.optDouble("radius", -1);
            String path = jsonObject.optString("path");

            Map<String, Object> response = Maps.newLinkedHashMap();
            ConfigPojo.Page page = ContentUtils.getPageFromPagePath(path);
            JSONArray jsonArray = ContentUtils.getGeoJsonRecords(page, lat, lon, radius);

            Type userListType = new TypeToken<List<Object>>(){}.getType();
            List<Object> geojsonObjects = gson.fromJson(jsonArray.toString(), userListType);

            response.put("geojson", geojsonObjects);

            asyncResponse.resume(Response.ok().entity(gson.toJson(response)).build());
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
        }
    }

    @GET
    @Path("/{path:.*}")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    public void search(@Suspended final AsyncResponse asyncResponse, @QueryParam("p") String location, @QueryParam("z")
            String zoom, @QueryParam("q") String query, @PathParam("path") String path) {

        logger.info("" + path);

        logger.info("ConfigJSON : " + ContentUtils.getConfig());

        path = "/" + path;
        boolean isAuth = AuthUtils.isAuth(getToken());

        ConfigPojo.Page page = ContentUtils.getPage(path, isAuth);
        if(page == null || !ContentUtils.isPageOpenForUser(page, AuthUtils.getUserId(getToken()))) {
            String redirectPath = "/login";
            if(httpRequest.getRequestURL().toString().contains("covid.bounceshare") || httpRequest.getRequestURI().contains("covid.bounce.bike")) {
                redirectPath = "/404";
            }
            if(isAuth) {
                redirectPath = "/404";
            }
            UriBuilder builder =
                    UriBuilder.fromPath("").path(redirectPath);
            asyncResponse.resume(Response.temporaryRedirect(builder.build()).build());
            return;
        }
        Map<String, Object> data = ContentUtils.getDefaultFreemarkerObj(page.getPage(), isAuth, AuthUtils.getUserId(getToken()));
        ConfigPojo config = ContentUtils.getConfig();
        if (TextUtils.isEmpty(location)) {
            location = config.getDefaultLocation();
            if(!TextUtils.isEmpty(page.getDefaultLocation())) {
                location = page.getDefaultLocation();
            }
        }
        if (TextUtils.isEmpty(zoom)) {
            zoom = config.getZoom() + "";
            if(page.getZoom() > 0) {
                zoom = page.getZoom() + "";
            }
        }

        data.put("location", location);
        data.put("query", query);
        data.put("zoom", zoom);

        data.put("page", page.getPageId());
        data.put("pagePath", page.getPath());
        data.put("autoRefresh", page.getAutoRefresh());
        data.put("searchUrl", page.getSearchUrl());
        data.put("searchPage", page.getSearchPage());
        data.put("searchText", page.getSearchText());
        data.put("help", page.getHelp());
        if(page.getEditControl() != null) {
            data.put("editFenceUrl", page.getEditControl().getEditFenceUrl());
            if(page.getEditControl().getEditFenceDataSchema() != null) {
                data.put("editFenceDataSchema", gson.toJson(page.getEditControl().getEditFenceDataSchema()));
            }
            data.put("isEditControlSupported", page.getEditControl().IsEditControlSupported() + "");
        }
        data.put("searchDataSchema", page.getSearchDataSchema());
        data.put("mapView", true);
        if(page.getCrudConfig() != null) {
            data.put("recordsDataString", gson.toJson(ContentUtils.getDbRecords(page, null, 100, page.getCrudConfig().getListExcludeFields())));
            data.put("recordsData", ContentUtils.getDbRecords(page, null, 100, page.getCrudConfig().getListExcludeFields()));
            data.put("isCreateAllowed", page.getCrudConfig().isCreateAllowed());
            data.put("mapView", false);
            data.put("tableView", true);
            data.put("queryBuilder", true);
            data.put("searchQueryBuilderFilters", gson.toJson(ContentUtils.getSearchFilters(page)));
        }
        if(page.getGeoJsonRecordConfig() != null) {
            double lat = Double.valueOf(location.split(",")[0]);
            double lon = Double.valueOf(location.split(",")[1]);
            data.put("geojson", ContentUtils.getGeoJsonRecords(page, lat, lon, 0).toString());
        }

        String content = ContentUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    private String getToken() {
        if(httpRequest.getCookies() != null && httpRequest.getCookies().length > 0) {
            for (Cookie cookie : httpRequest.getCookies()) {
                if (cookie.getName().equals("token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
