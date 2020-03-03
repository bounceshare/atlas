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
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void homeLayers(@Suspended final AsyncResponse asyncResponse, @QueryParam("p") String location,
                           @QueryParam("z") String zoom, @QueryParam("q") String query) {
        logger.info("/home");

        Map<String, Object> data = FreemarkerUtils.getDefaultFreemarkerObj("home");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }
        if (TextUtils.isEmpty(zoom)) {
            zoom = 17 + "";
        }
        if (TextUtils.isEmpty(query)) {
            query = "bikes";
        }

        data.put("location", location);
        data.put("query", query);
        data.put("zoom", zoom);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/layers")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void layers(@Suspended final AsyncResponse asyncResponse, @QueryParam("p") String location,
                       @QueryParam("z") String zoom, @QueryParam("q") String query) {
        logger.info("/home");

        Map<String, Object> data = FreemarkerUtils.getDefaultFreemarkerObj("layers");
        data.put("searchPage", "true");
        data.put("searchUrl", "/apis/search");
        data.put("searchText", "SQL Where Query");
        data.put("autoRefresh", "true");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }
        if (TextUtils.isEmpty(zoom)) {
            zoom = 17 + "";
        }

        data.put("location", location);
        data.put("query", query);
        data.put("zoom", zoom);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/bikes")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bikePage(@Suspended final AsyncResponse asyncResponse, @QueryParam("p") String location,
                         @QueryParam("z") String zoom, @QueryParam("q") String searchTerm) {
        logger.info("/bikes");

        Map<String, Object> data = FreemarkerUtils.getDefaultFreemarkerObj("bikes");
        data.put("searchPage", "true");
        data.put("searchUrl", "/apis/bike/search");
        data.put("searchText", "Bike Id or License Plate");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }
        if (TextUtils.isEmpty(zoom)) {
            zoom = 17 + "";
        }

        if(!TextUtils.isEmpty(searchTerm)) {
            List<BikeRecord> bikes = QueryUtils.getBikes(searchTerm);
            if(bikes != null && bikes.size() > 0) {
                FreemarkerUtils.addMarkersToFreemarkerObj(MarkerPojo.getBikesAsMarkers(bikes), data);
            }
        }

        data.put("location", location);
        data.put("zoom", zoom);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/bookings")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bookingPage(@Suspended final AsyncResponse asyncResponse, @QueryParam("p") String location,
                            @QueryParam("z") String zoom, @QueryParam("q") String bookingId) {
        logger.info("/bookings");

        Map<String, Object> data = FreemarkerUtils.getDefaultFreemarkerObj("bookings");
        data.put("searchPage", "true");
        data.put("searchUrl", "/apis/booking/search");
        data.put("searchText", "Booking Id");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }
        if (TextUtils.isEmpty(zoom)) {
            zoom = 17 + "";
        }

        if(!TextUtils.isEmpty(bookingId)) {
            BookingRecord booking = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Booking.BOOKING)
                    .where(Booking.BOOKING.ID.eq(Integer.parseInt(bookingId))).fetchAny();
            Map<String, Object> markersPaths = getMarkersAndPathForBooking(booking);
            if(markersPaths != null && markersPaths.size() > 0) {
                FreemarkerUtils.addMarkersToFreemarkerObj((List<MarkerPojo>) markersPaths.get("markers"), data);
                FreemarkerUtils.addPathsToFreemarkerObj((List<PathPojo>) markersPaths.get("paths"), data);
            }
        }

        data.put("location", location);
        data.put("zoom", zoom);

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/tracking")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void trackingPage(@Suspended final AsyncResponse asyncResponse, @QueryParam("p") String location,
                             @QueryParam("z") String zoom, @QueryParam("q") String bikeId) {
        logger.info("/tracking");

        Map<String, Object> data = FreemarkerUtils.getDefaultFreemarkerObj("tracking");
        data.put("searchPage", "true");
        data.put("searchUrl", "/apis/tracking/search");
        data.put("searchText", "Bike Id");

        if (TextUtils.isEmpty(location)) {
            location = "12.9160463,77.5967117";
        }
        if (TextUtils.isEmpty(zoom)) {
            zoom = 17 + "";
        }
        data.put("location", location);
        data.put("zoom", zoom);

        String imei = null;

        String query = bikeId;
        if(!TextUtils.isEmpty(query)) {
            BikeRecord bike = QueryUtils.getBike(Integer.parseInt(query));
            if (bike != null) {
                AxcessRecord axcessRecord = bike.fetchParent(Keys.BIKE__BIKE_AXCESS_ID_FKEY);
                imei = axcessRecord.getImei();
            }
        }

        if (!TextUtils.isEmpty(imei)) {
//            if(TextUtils.isEmpty(hours) && !TextUtils.isEmpty(hours)) {
//                hours = "24";
//            }
//            if(TextUtils.isEmpty(mins)) {
//                mins = "0";
//            }
//            int numHours = Integer.parseInt(hours);
//            int numMins = Integer.parseInt(mins);
            Map<Object, Object> response = TrackingSearchApi.getTrackingRenderData(imei,
                    new DateTime().minusHours(24).minusMinutes(0).getMillis(), System.currentTimeMillis());
            FreemarkerUtils.addMarkersToFreemarkerObj((List<MarkerPojo>) response.get("markers"), data);
            FreemarkerUtils.addPathsToFreemarkerObj((List<PathPojo>) response.get("paths"), data);
        }

        String content = FreemarkerUtils.getFreemarkerString("index.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

}
