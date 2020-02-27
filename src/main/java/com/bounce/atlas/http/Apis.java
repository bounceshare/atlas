package com.bounce.atlas.http;

import com.bounce.atlas.utils.FreemarkerUtils;
import com.bounce.utils.BounceUtils;
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
import java.io.IOException;
import java.io.InputStream;
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
    @Path("/home")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    public void home(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/home");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "home");

        String content = FreemarkerUtils.getFreemarkerString("home.html", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

    @GET
    @Path("/bikes")
    @Produces(MediaType.TEXT_HTML)
    @Consumes({MediaType.APPLICATION_JSON})
    public void bikes(@Suspended final AsyncResponse asyncResponse) {
        logger.info("/bikes");

        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "bikes");

        String content = FreemarkerUtils.getFreemarkerString("home.html", data);
        asyncResponse.resume(Response.ok().entity(content).build());
    }

}
