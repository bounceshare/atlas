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
        // TODO return legit data and remove dummy data
        FormPojo formPojo = new FormPojo();
        String formSchemaStr = "{\"engineNumber\":{\"title\":\"Engine Number\",\"type\":\"string\"}," +
                "\"chassisNumber\":{\"title\":\"Chassis Number\",\"type\":\"string\"}," +
                "\"registrationCertificate\":{\"title\":\"Registration Certificate\",\"type\":\"string\"}," +
                "\"registrationValidTill\":{\"title\":\"Registration Valid Till\",\"type\":\"datetime-local\"}," +
                "\"insurance\":{\"title\":\"Insurance\",\"type\":\"string\"}," +
                "\"insuranceValidFrom\":{\"title\":\"Insurance Valid From\",\"type\":\"datetime-local\"}," +
                "\"insuranceValidTill\":{\"title\":\"Insurance Valid Till\",\"type\":\"datetime-local\"}," +
                "\"emissionCertificate\":{\"title\":\"Emission Certificate\",\"type\":\"string\"}," +
                "\"feeClearance\":{\"title\":\"Fee Clearance\",\"type\":\"string\"}," +
                "\"invoice\":{\"title\":\"Invoice\",\"type\":\"string\"}}";
        formPojo.formSchema = FormPojo.fromJson(formSchemaStr);
        formPojo.values = FormPojo.fromJson("{\"engineNumber\":\"123312213123\",\"chassisNumber\":\"5432332\"," +
                "\"registrationCertificate\":\"http://tinyurl.com/y8zbst2y\"," +
                "\"registrationValidTill\":\"2030-07-17T00:00\",\"insurance\":\"http://tinyurl.com/yahh76go\"," +
                "\"insuranceValidFrom\":\"2019-07-20T00:00\",\"insuranceValidTill\":\"2021-07-20T00:00\"," +
                "\"emissionCertificate\":\"http://tinyurl.com/ycw3s4lt\",\"feeClearance\":\"http://tinyurl" +
                ".com/yauqpzrp\",\"invoice\":\"http://tinyurl.com/ybk3dqow\"}");
        formPojo.postUrl = "/apis/test/drawnObjs";

        return formPojo;
    }
}
