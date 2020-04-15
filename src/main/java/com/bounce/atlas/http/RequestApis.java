package com.bounce.atlas.http;

import com.bounce.atlas.http.handlers.*;
import com.bounce.atlas.utils.GoogleAuth;
import com.bounce.utils.Log;
import com.bounce.utils.status.Status;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/apis/")
public class RequestApis {

    @Context
    private HttpServletRequest httpRequest;
    @Context
    private HttpServletResponse httpResponse;

    private static Logger logger = Log.getLogger(RequestApis.class.getCanonicalName());
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @POST
    @Path("/authTest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void authTest(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/authTest");
        asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess())).build());
    }

    @POST
    @Path("/listing")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Deprecated
    @GoogleAuth
    public void listing(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/listing");
        BikeListingApi bikeListingApi = new BikeListingApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeListingApi.onRequest();
    }

    @POST
    @Path("/search/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void layerSearch(String inputString, @Suspended final AsyncResponse asyncResponse, @PathParam("path") String path) {
        logger.info("/apis/search");
        LayersApi layersApi = new LayersApi(inputString, asyncResponse, httpRequest, httpResponse, path);
        layersApi.onRequest();
    }

    @POST
    @Path("/bike/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bikeSearch(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/bike/listing");
        BikeSearchApi bikeSearchApi = new BikeSearchApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeSearchApi.onRequest();
    }

    @POST
    @Path("/bike/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bikeEvents(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/bike/events");
        BikeEventsApi bikeSearchApi = new BikeEventsApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeSearchApi.onRequest();
    }

    @POST
    @Path("/bike/deviceData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bikeDeviceData(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/bike/deviceData");
        BikeDeviceDataApi
                bikeDeviceDataApi = new BikeDeviceDataApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeDeviceDataApi.onRequest();
    }

    @POST
    @Path("/bike/fetchAssets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void fetchBikeAssets(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/bike/fetchAssets");
        FetchBikeAssetApi
                bikeAssetApi = new FetchBikeAssetApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeAssetApi.onRequest();
    }

    @POST
    @Path("/booking/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bookingEvents(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/booking/events");
        BookingEventsApi bookingEventsApi = new BookingEventsApi(inputString, asyncResponse, httpRequest, httpResponse);
        bookingEventsApi.onRequest();
    }

    @POST
    @Path("/booking/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void bookingSearch(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/booking/search");
        BookingSearchApi bookingSearchApi = new BookingSearchApi(inputString, asyncResponse, httpRequest, httpResponse);
        bookingSearchApi.onRequest();
    }

    @POST
    @Path("/tracking/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void trackingSearch(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/tracking/search");
        TrackingSearchApi trackingSearchApi = new TrackingSearchApi(inputString, asyncResponse, httpRequest, httpResponse);
        trackingSearchApi.onRequest();
    }

    @POST
    @Path("/corona/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void coronaSearch(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/corona/search");
        CoronaSearchApi coronaSearchApi = new CoronaSearchApi(inputString, asyncResponse, httpRequest, httpResponse);
        coronaSearchApi.onRequest();
    }

    @POST
    @Path("/corona/testcentres")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void coronaTestCentres(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/corona/testcentres");
        CoronaTestCentresSearchApi coronaTestCentresSearchApi = new CoronaTestCentresSearchApi(inputString, asyncResponse, httpRequest, httpResponse);
        coronaTestCentresSearchApi.onRequest();
    }

    @POST
    @Path("/test/drawnObjs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void testDrawnObjs(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/test/drawnObjs Data : " + inputString);
        Map<Object, Object> response = Maps.newLinkedHashMap();
        asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess(response))).build());
    }

}
