package com.bounce.atlas.http;

import com.bounce.atlas.pojo.*;
import com.bounce.atlas.utils.AuthUtils;
import com.bounce.atlas.utils.FreemarkerUtils;
import com.bounce.atlas.utils.GoogleAuth;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.Log;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.status.Status;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;

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
import java.util.Enumeration;
import java.util.HashMap;
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

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "login");

        String content = FreemarkerUtils.getFreemarkerString("login.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void homeLayers(@Suspended final AsyncResponse asyncResponse, @QueryParam("loc") String location,
                           @QueryParam("layers") String layers) {
        logger.info("/home");

        Map<String, String> map = new HashMap<String, String>();

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "home");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }
        if (TextUtils.isEmpty(layers)) {
            layers = "bikes,parking";
        }

        data.put("location", location);
        data.put("layers", layers);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/layers")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void layers(@Suspended final AsyncResponse asyncResponse, @QueryParam("loc") String location, @QueryParam(
            "l") String layers) {
        logger.info("/home");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "layers");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }
        if (TextUtils.isEmpty(layers)) {
            layers = "bikes,parking";
        }

        data.put("location", location);
        data.put("layers", layers);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/bikes")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bikePage(@Suspended final AsyncResponse asyncResponse, @QueryParam("loc") String location) {
        logger.info("/bikes");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "bikes");
        data.put("searchPage", "true");
        data.put("searchUrl", "/apis/bike/search");
        data.put("searchText", "Bike Id or License Plate");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }

        data.put("location", location);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/bookings")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bookingPage(@Suspended final AsyncResponse asyncResponse, @QueryParam("loc") String location) {
        logger.info("/bookings");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "bookings");
        data.put("searchPage", "true");
        data.put("searchUrl", "/apis/booking/search");
        data.put("searchText", "Booking Id");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }

        data.put("location", location);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void test(@Suspended final AsyncResponse asyncResponse, @QueryParam("q") String location) {
        logger.info("/test");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "test");

        double lat = 12.9160463;
        double lon = 77.5967117;

        List<BikeRecord> bikes = QueryUtils.getBikes(lat, lon, 10000, 2500);
        List<MarkerPojo> markers = MarkerPojo.getBikesAsMarkers(bikes);

        List<FencePojo> fences = FencePojo.getParkingFences(lat, lon, 2500);
        List<CirclePojo> circles = Lists.newArrayList();
        List<PathPojo> paths = Lists.newArrayList();

        FreemarkerUtils.addMarkersToFreemarkerObj(markers, data);
        FreemarkerUtils.addFencesToFreemarkerObj(fences, data);
        FreemarkerUtils.addCirclesToFreemarkerObj(circles, data);
        FreemarkerUtils.addPathsToFreemarkerObj(paths, data);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

}
