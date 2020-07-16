package com.bounce.atlas.http;

import com.bounce.atlas.pojo.ConfigPojo;
import com.bounce.atlas.pojo.FormPojo;
import com.bounce.atlas.pojo.StatusPojo;
import com.bounce.atlas.utils.*;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
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
import java.sql.Timestamp;
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
            if(TextUtils.isEmpty(where)) {
                where += page.getCrudConfig().getWhereCondition();
            } else {
                where += " and " + page.getCrudConfig().getWhereCondition();
            }
            List<List<String>> obj = ContentUtils.getDbRecords(page, where, 100, page.getCrudConfig().getListExcludeFields());
            Map<Object, Object> response = Maps.newHashMap();
            response.put("records", obj);

            asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess(response))).build());
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
            String primaryKeyVal = null;
            if(input.has("primaryKeyVal")) {
                primaryKeyVal = input.opt("primaryKeyVal").toString();
            }
            Map<Object, Object> response = Maps.newHashMap();

            ConfigPojo.Page page = ContentUtils.getPageFromPagePath(pagePath);
            FormPojo form = new FormPojo();
            form.formSchema = ContentUtils.getFormSchema(page);
            if(!TextUtils.isEmpty(primaryKeyVal)) {
                form.values = ContentUtils.getFormValues(page, primaryKeyVal, page.getCrudConfig().getEditExcludeFields());
                form.postUrl = "/records/edit";
            } else {
                form.values = ContentUtils.getFormValues(page, primaryKeyVal, page.getCrudConfig().getEditExcludeFields());
                form.postUrl = "/records/create";
            }

            response.put("form", form);
            asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess(response))).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void createRecord(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/records/create");
        try {
            JSONObject input = new JSONObject(inputString);
            String pagePath = input.optJSONObject("data").optString("pagePath");
            ConfigPojo.Page page = ContentUtils.getPageFromPagePath(pagePath);
            Map<String, Object> createData = FormPojo.fromJson(input.optJSONObject("data").toString());
            Map<String, Field> fieldMap = ContentUtils.getColumns(page);
            createData.remove("pagePath");
            String insertStatement = "";
            String columnStatement = "";
            String valuesStatement = "";

            for(Map.Entry<String, Object> entry : createData.entrySet()) {
                String column = entry.getKey();
                if(page.getCrudConfig().getCreateExcludeFields().contains(column)) {
                    continue;
                }
                Field field = fieldMap.get(column);
                Object val = entry.getValue();
                if(TextUtils.isEmpty(val.toString())) {
                    continue;
                }
                switch (field.getDataType().getSQLDataType().getTypeName()) {
                    case "varchar":
                        break;
                    case "timestamp":
                        long timestamp = Utils.convertHtmlInputTimestamp(val.toString());
                        val = new Timestamp(timestamp).toString();
                        break;
                    case "boolean":
                        val = Boolean.parseBoolean(val.toString());
                        createData.put(column, val);
                        break;
                    case "integer":
                        val = Double.valueOf(val.toString()).intValue();
                        createData.put(column, val);
                        break;
                    case "float":
                        val = Double.parseDouble(val.toString());
                        createData.put(column, val);
                        break;
                    case "other":
                        break;
                    case "bigint":
                        String longStr = val.toString().replace(".0", "");
                        val = Long.parseLong(longStr);
                        createData.put(column, val);
                        break;
                }
                if(!TextUtils.isEmpty(val.toString()))  {
                    columnStatement += column + ",";
                    valuesStatement += "'" + val + "'" + ",";
                }

            }

            if(!TextUtils.isEmpty(page.getCrudConfig().getWhereCondition())) {
                String[] conditions = page.getCrudConfig().getWhereCondition().split("AND");
                for(String condition : conditions) {
                    String colName = condition.split("=")[0].trim();
                    String val = condition.split("=")[1].trim();

                    columnStatement += colName + ",";
                    valuesStatement += "'" + val + "'" + ",";
                }

            }

            columnStatement = columnStatement.substring(0, columnStatement.length() -1);
            valuesStatement = valuesStatement.substring(0, valuesStatement.length() -1);

            insertStatement = "INSERT INTO " + page.getCrudConfig().getSchema() + "." + page.getCrudConfig().getTable() + " ( " + columnStatement + " ) " + "VALUES ( "  + valuesStatement + " ) RETURNING id";
            logger.info("Insert statement : " + insertStatement);

            Result result = DatabaseConnector.getDb()
                    .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                            page.getCrudConfig().getDbPassword()).fetch(insertStatement);

            if(result.size() > 0) {
                Map<Object, Object> response = Maps.newHashMap();
                asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess(response))).build());
            } else {
                asyncResponse.resume(Response.status(500).entity(gson.toJson(StatusPojo.buildFailure(500,  "Error  : No records deleted"))));
                return;
            }

            if(!TextUtils.isEmpty(page.getCrudConfig().getCallbackUrl())) {
                ContentUtils.postRecordCallback(result.getValues("id").get(0) + "", "create", page.getCrudConfig().getCallbackUrl(), page.getCrudConfig().getCallbackHeaders());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
            asyncResponse.resume(Response.status(500).entity(gson.toJson(StatusPojo.buildFailure(500,  "Error  : " + e.getMessage()))));
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

            String primaryKeyVal = input.optJSONObject("data").opt(ContentUtils.getPrimaryKey(page)).toString();
            Map<String,Object> oldData = ContentUtils.getFormValues(page, primaryKeyVal, page.getCrudConfig().getEditExcludeFields());
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

            String updateStatement = "";
            for(Map.Entry<String, Object> entry : editData.entrySet()) {
                String column = entry.getKey();
                Field field = fieldMap.get(column);
                Object val = entry.getValue();
                switch (field.getDataType().getSQLDataType().getTypeName()) {
                    case "varchar":
                        break;
                    case "timestamp":
                        long timestamp = Utils.convertHtmlInputTimestamp(val.toString());
                        val = new Timestamp(timestamp).toString();
                        break;
                    case "boolean":
                        break;
                    case "integer":
                        break;
                    case "float":
                        break;
                    case "other":
                        break;
                    case "bigint":
                        break;
                }
                updateStatement += column + " = '" + val + "',";
            }

            updateStatement = updateStatement.substring(0, updateStatement.length() -1);
            updateStatement = "update " + page.getCrudConfig().getSchema() + "." + page.getCrudConfig().getTable() +
                    " SET " + updateStatement + " WHERE " + ContentUtils.getPrimaryKey(page) + " = " + primaryKeyVal;
            logger.info("Update statement : " + updateStatement);

            DatabaseConnector.getDb()
                    .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                            page.getCrudConfig().getDbPassword()).fetch(updateStatement);


            Map<Object, Object> response = Maps.newHashMap();
            asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess(response))).build());
            if(!TextUtils.isEmpty(page.getCrudConfig().getCallbackUrl())) {
                ContentUtils.postRecordCallback(primaryKeyVal, "edit", page.getCrudConfig().getCallbackUrl(), page.getCrudConfig().getCallbackHeaders());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
            asyncResponse.resume(Response.status(500).entity(gson.toJson(StatusPojo.buildFailure(500,  "Error  : " + e.getMessage()))));
        }

    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @GoogleAuth
    public void deleteRecord(String inputString, @Suspended final AsyncResponse asyncResponse) {
        logger.info("/records/delete");
        try {
            JSONObject input = new JSONObject(inputString);
            String pagePath = input.optString("pagePath");
            String primaryKeyVal = null;
            if(input.has("primaryKeyVal")) {
                primaryKeyVal = input.opt("primaryKeyVal").toString();
            }
            ConfigPojo.Page page = ContentUtils.getPageFromPagePath(pagePath);
            Map<String,Object> oldData = ContentUtils.getFormValues(page, primaryKeyVal, page.getCrudConfig().getEditExcludeFields());
            if(oldData.size() < 1) {
                asyncResponse.resume(Response.status(500).entity(gson.toJson(StatusPojo.buildFailure(500,  "Error  : No record found to delete"))));
                return;
            }

            String deleteStatement = "";

            deleteStatement = "delete from " + page.getCrudConfig().getSchema() + "." + page.getCrudConfig().getTable() +
                    " WHERE " + ContentUtils.getPrimaryKey(page) + " = " + primaryKeyVal;
            logger.info("Delete statement : " + deleteStatement);

            Result result = DatabaseConnector.getDb()
                    .getConnector(page.getCrudConfig().getJdbcUrl(), page.getCrudConfig().getDbUsername(),
                            page.getCrudConfig().getDbPassword()).fetch(deleteStatement);

            if(result.size() > 0) {
                Map<Object, Object> response = Maps.newHashMap();
                asyncResponse.resume(Response.ok().entity(gson.toJson(StatusPojo.buildSuccess(response))).build());
            } else {
                asyncResponse.resume(Response.status(500).entity(gson.toJson(StatusPojo.buildFailure(500,  "Error  : No records deleted"))));
                return;
            }

            if(!TextUtils.isEmpty(page.getCrudConfig().getCallbackUrl())) {
                ContentUtils.postRecordCallback(primaryKeyVal, "delete", page.getCrudConfig().getCallbackUrl(), page.getCrudConfig().getCallbackHeaders());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
            asyncResponse.resume(Response.status(500).entity(gson.toJson(StatusPojo.buildFailure(500,  "Error  : " + e.getMessage()))));
        }
    }

}
