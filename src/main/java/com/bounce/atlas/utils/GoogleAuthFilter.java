package com.bounce.atlas.utils;

import com.bounce.utils.Log;
import com.bounce.utils.requestfilters.DocStatus;
import com.bounce.utils.requestfilters.Secured;
import com.bounce.utils.status.LegacyStatus;
import org.apache.log4j.Logger;

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
}
