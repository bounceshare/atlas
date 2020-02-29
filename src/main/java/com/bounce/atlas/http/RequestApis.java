package com.bounce.atlas.http;

import com.bounce.atlas.http.handlers.BikeListingApi;
import com.bounce.atlas.http.handlers.BikeSearchApi;
import com.bounce.atlas.http.handlers.LayersApi;
import com.bounce.utils.Log;
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

@Path("/apis/")
public class RequestApis {

    @Context
    private HttpServletRequest httpRequest;
    @Context
    private HttpServletResponse httpResponse;

    private static Logger logger = Log.getLogger(RequestApis.class.getCanonicalName());
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @POST
    @Path("/listing")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void listing(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/listing");
        BikeListingApi bikeListingApi = new BikeListingApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeListingApi.onRequest();
    }

    @POST
    @Path("/bike/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void bikeSearch(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/bike/listing");
        BikeSearchApi bikeSearchApi = new BikeSearchApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeSearchApi.onRequest();
    }

    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void search(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/apis/search");
        LayersApi layersApi = new LayersApi(inputString, asyncResponse, httpRequest, httpResponse);
        layersApi.onRequest();
    }

}
