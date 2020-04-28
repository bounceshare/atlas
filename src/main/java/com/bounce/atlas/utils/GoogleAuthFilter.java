package com.bounce.atlas.utils;

import com.bounce.atlas.pojo.StatusPojo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Provider
@Priority(1)
public class GoogleAuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;
    private static Logger logger = Log.getLogger(GoogleAuthFilter.class.getCanonicalName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method resourceMethod = resourceInfo.getResourceMethod();
        boolean authSupported = authSupported(resourceMethod);

        if(!authSupported) {
            return;
        }

        if(requestContext.getMethod().equals("POST")) {
            String token = getTokenFromPostRequest(requestContext);
            if(!AuthUtils.isAuth(token)) {
                logger.info("Unauthorized token : " + token);
                requestContext.abortWith(LegacyStatus
                        .error(401, "Not Authorized"));
            }
        } else {
            String token = getTokenFromGetRequest(requestContext);
            if(!AuthUtils.isAuth(token)) {
                logger.info("Unauthorized token : " + token);
                UriBuilder builder =
                        UriBuilder.fromPath(requestContext.getUriInfo().getBaseUriBuilder().toString()).path("/login");
                requestContext.abortWith(Response.temporaryRedirect(builder.build()).build());
            }
        }
    }

    private String getTokenFromPostRequest(ContainerRequestContext requestContext) {
        return requestContext.getHeaders() != null && requestContext.getHeaders().size() > 0 ? (String)requestContext.getHeaders().getFirst("token") : null;
    }

    private String getTokenFromGetRequest(ContainerRequestContext requestContext) {
        try {
            String value = requestContext.getHeaders() != null && requestContext.getHeaders().size() > 0 ? (String)requestContext.getHeaders().getFirst("Cookie") : null;
            return requestContext.getCookies().get("token").getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private boolean authSupported(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return false;
        } else {
            GoogleAuth annotation = annotatedElement.getAnnotation(GoogleAuth.class);
            if (annotation == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static class LegacyStatus {

        private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        public Map<Object, Object> data = new HashMap<>();
        public int status;
        public String msg;

        public LegacyStatus(Map<Object, Object> data, int status, String msg) {
            this.data = data;
            this.status = status;
            this.msg = msg;
        }

        public static Response error(int status, String msg) {
            LegacyStatus legacyStatus = new LegacyStatus(new HashMap<Object, Object>(), status, msg);
            return Response.status(status).entity(gson.toJson(legacyStatus)).build();
        }

        public static Response error(int status, String msg, Map<Object, Object> response) {
            LegacyStatus legacyStatus = new LegacyStatus(response, status, msg);
            return Response.status(status).entity(gson.toJson(legacyStatus)).build();
        }

        public static Response success(Map<Object, Object> data) {
            String msg = "Success";
            if(data == null) {
                msg = "No data found";
            }
            LegacyStatus legacyStatus = new LegacyStatus(data, 200, msg);
            String retVal = new GsonBuilder().serializeNulls().create().toJson(legacyStatus).toString();
            return Response.status(200).entity(retVal).build();
        }

        public static Response actualResponseSuccessHack(Map<Object, Object> data) {
            String msg = "Success";
            if(data == null) {
                msg = "No data found";
            }
            LegacyStatus legacyStatus = new LegacyStatus(data, 200, msg);
            String retVal = new GsonBuilder().serializeNulls().create().toJson(legacyStatus).toString();
            JSONObject responseObj = new JSONObject();
            JSONObject result = new JSONObject(retVal);
            responseObj.put("msg", "Success");
            responseObj.put("result", result);
            return Response.status(200).entity(responseObj.toString()).build();
        }
    }

}
