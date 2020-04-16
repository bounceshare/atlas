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
import org.jooq.Field;
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
    public void form(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/records/form");
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
                form.postUrl = "/records/edit";
            } else {
                form.values = ContentUtils.getFormValues(page, id);
                form.postUrl = "/records/create";
            }

            response.put("form", form);
            asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess(response))).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void editRecord(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/records/edit");
        try {
            logger.info("/edit : " + inputString);
            JSONObject input = new JSONObject(inputString);
            String pagePath = input.optJSONObject("data").optString("pagePath");
            ConfigPojo.Page page = ContentUtils.getPageFromPagePath(pagePath);

            Map<String, Object> editData = FormPojo.fromJson(input.optJSONObject("data").toString());

            int id = Double.valueOf((double)editData.get("id")).intValue();
            Map<String,Object> oldData = ContentUtils.getFormValues(page, id);
            editData.remove("pagePath");
            oldData.remove("pagePath");
            Map<String, Field> fieldMap = ContentUtils.getColumns(page);
            for(Map.Entry<String, Object> entry : oldData.entrySet()) {
                String columnName = entry.getKey();
                Object oldValue = entry.getValue();
                Object newValue = editData.get(columnName);
                Field field = fieldMap.get(columnName);

                switch (field.getDataType().getSQLDataType().getTypeName()) {
                    case "varchar":
                        // no change
                        break;
                    case "timestamp":
                        // no change needed
                        break;
                    case "boolean":
                        newValue = Boolean.parseBoolean(newValue.toString());
                        editData.put(columnName, newValue);
                        break;
                    case "integer":
                        newValue = Double.valueOf(newValue.toString()).intValue();
                        editData.put(columnName, newValue);
                        break;
                    case "float":
                        newValue = Double.parseDouble(newValue.toString());
                        editData.put(columnName, newValue);
                        break;
                    case "other":
                        break;
                    case "bigint":
                        String longStr = newValue.toString().replace(".0", "");
                        newValue = Long.parseLong(longStr);
                        editData.put(columnName, newValue);
                        break;
                }

                if(oldValue != null && newValue != null && oldValue.toString().equals(newValue.toString())) {
                    editData.remove(entry.getKey());
                } else {
                    logger.info(columnName + " :: oldValue : " + oldValue + " :: newValue : " + newValue);
                }
            }

            logger.info("Actual edited values : " + gson.toJson(editData));

            Map<Object, Object> response = Maps.newHashMap();
            asyncResponse.resume(Response.ok().entity(gson.toJson(Status.buildSuccess(response))).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
