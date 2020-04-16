package com.bounce.atlas.http;

import com.bounce.atlas.pojo.ConfigPojo;
import com.bounce.atlas.pojo.FormPojo;
import com.bounce.atlas.utils.ContentUtils;
import com.bounce.atlas.utils.GoogleAuth;
import com.bounce.utils.Log;
import com.bounce.utils.status.Status;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;

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
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/records")
public class RecordApis {

    @Context
    private HttpServletRequest httpRequest;
    @Context
    private HttpServletResponse httpResponse;

    private static Logger logger = Log.getLogger(RecordApis.class.getCanonicalName());
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void searchRecords(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/records/search");
        try {
            JSONObject input = new JSONObject(inputString);
            String pagePath = input.optString("pagePath");
            String where = input.optString("where");

            ConfigPojo.Page page = ContentUtils.getPageFromPagePath(pagePath);
            List<List<String>> obj = ContentUtils.getDbRecords(page, where, 100);
            Map<Object, Object> response = Maps.newHashMap();
            response.put("records", obj);

            asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess(response))).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/form")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void editRecord(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/records/search");
        try {
            JSONObject input = new JSONObject(inputString);
            String pagePath = input.optString("pagePath");
            int id = input.optInt("id", -1);
            Map<Object, Object> response = Maps.newHashMap();

            ConfigPojo.Page page = ContentUtils.getPageFromPagePath(pagePath);
            FormPojo form = new FormPojo();
            form.formSchema = ContentUtils.getFormSchema(page);
            if(id >= 0) {
                form.values = ContentUtils.getFormValues(page, id);
            }
            form.postUrl = "/apis/test/drawnObjs";

            response.put("form", form);
            asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess(response))).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
