package com.bounce.atlas.http;

import com.bounce.atlas.http.handlers.TrackingSearchApi;
import com.bounce.atlas.pojo.*;
import com.bounce.atlas.utils.FreemarkerUtils;
import com.bounce.atlas.utils.GoogleAuth;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.Log;
import com.bounce.utils.dbmodels.public_.Keys;
import com.bounce.utils.dbmodels.public_.tables.Booking;
import com.bounce.utils.dbmodels.public_.tables.records.AxcessRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.status.Status;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bounce.atlas.http.handlers.BookingSearchApi.getMarkersAndPathForBooking;

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
        asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess())).build());
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
    public void healthPost(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/health");
        asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess())).build());
    }

    @GET
    @Path("/resources/{path:.+}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes({MediaType.APPLICATION_JSON})
    public InputStream resource(@PathParam("path") String path) {
        logger.info("/resource/" + path);
        try {
            return FreemarkerUtils.getContentAsStream(path);
        } catch (IOException e) {
            e.printStackTrace();
            BounceUtils.logError(e);
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
            return FreemarkerUtils.getContent(path);
        } catch (IOException e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return "404";
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    public void login(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/login");

        Map<String, Object> data = FreemarkerUtils.getDefaultFreemarkerObj("login");

        String content = FreemarkerUtils.getFreemarkerString("login.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/{path:.*}")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void search(@Suspended final AsyncResponse asyncResponse, @QueryParam("p") String location, @QueryParam("z")
            String zoom, @QueryParam("q") String query, @PathParam("path") String path) {

        logger.info("" + path);

        logger.info("ConfigJSON : " + FreemarkerUtils.getConfig());

        Map<String, Object> data = FreemarkerUtils.getDefaultFreemarkerObj("home");
        ConfigPojo config = FreemarkerUtils.getConfig();
        if (TextUtils.isEmpty(location)) {
            location = config.getDefaultLocation();
        }
        if (TextUtils.isEmpty(zoom)) {
            zoom = config.getZoom() + "";
        }

        data.put("location", location);
        data.put("query", query);
        data.put("zoom", zoom);

        path = "/" + path;

        ConfigPojo.Page page = FreemarkerUtils.getPage(path);
        if(page != null) {
            data.put("page", page.getPageId());
            data.put("autoRefresh", page.getAutoRefresh());
            data.put("searchUrl", page.getSearchUrl());
            data.put("searchPage", page.getSearchPage());
            data.put("searchText", page.getSearchText());
            data.put("help", page.getHelp());
        }

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

}
