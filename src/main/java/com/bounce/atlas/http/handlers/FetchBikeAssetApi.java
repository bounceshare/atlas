package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.FormPojo;
import com.bounce.utils.BikeUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.Bike;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.Map;

public class FetchBikeAssetApi extends BaseApiHandler {

    public FetchBikeAssetApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        try {
            super.onRequest();

            int bikeId = input.optInt("id");

            Map<Object, Object> response = Maps.newHashMap();
            response.put("form", getFormData(bikeId));

            sendSuccessResponse(asyncResponse, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FormPojo getFormData(int bikeId) {
        FormPojo formPojo = new FormPojo();
        String formSchemaStr = "{\"name\":{\"title\":\"Fence Name\",\"description\":\"Please specify a name to " +
                "identify this geo fence\",\"type\":\"string\"},\"gender\":{\"title\":\"Type\"," +
                "\"description\":\"Fence type\",\"type\":\"string\",\"enum\":[\"Red Zone\",\"Black Zone\",\"Blue " +
                "Zone\"]}}";
        formPojo.formSchema = FormPojo.fromJson(formSchemaStr);
        formPojo.values = Maps.newLinkedHashMap();
        formPojo.postUrl = "/apis/test/drawnObjs";

        return formPojo;
    }
}
