package com.bounce.atlas.http;

import com.bounce.atlas.http.handlers.BikeListingApi;
import com.bounce.utils.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/bikes/")
public class BikeApis {

    @Context
    private HttpServletRequest httpRequest;
    @Context
    private HttpServletResponse httpResponse;

    private static Logger logger = Log.getLogger(BikeApis.class.getCanonicalName());
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @POST
    @Path("/listing")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public void healthPost(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/bikes/listing");
        BikeListingApi bikeListingApi = new BikeListingApi(inputString, asyncResponse, httpRequest, httpResponse);
        bikeListingApi.onRequest();
    }

}
