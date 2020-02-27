package com.bounce.atlas.http;

import com.bounce.atlas.utils.FreemarkerUtils;
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
    @Path("/home")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    public void home(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/home");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Atlas");
        data.put("body", "Coming Soon : Atlas");

        String content = FreemarkerUtils.getFreemarkerString("home.ftl", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

}
