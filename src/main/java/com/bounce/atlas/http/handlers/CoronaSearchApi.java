package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.pojo.PointPojo;
import com.bounce.atlas.utils.ContentUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.RestGenericRequest;
import com.bounce.utils.apis.BaseApiHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CoronaSearchApi extends BaseApiHandler {

    private static Map<String, String> mapCoords;

    static {
        updateMapCoords();
    }

    public CoronaSearchApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                           HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        Map<Object, Object> response = Maps.newHashMap();
        List<MarkerPojo> markers = Lists.newArrayList();
        try {
            super.onRequest();

            String worldDataResponse =
                    RestGenericRequest.httpGet("https://coronavirus-19-api.herokuapp.com/countries", new JSONObject("{}"), null);
            JSONArray worldData = new JSONArray(worldDataResponse);
            for (int i = 0; i < worldData.length(); i++) {
                MarkerPojo marker = getMarkerPojoForCountry(worldData.getJSONObject(i));
                if(marker!=null) {
                    markers.add(marker);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        response.put("markers", markers);
        response.put("autoRefresh", false);

        sendSuccessResponse(asyncResponse, response);
    }

    private MarkerPojo getMarkerPojoForCountry(JSONObject jsonObject) {
        MarkerPojo markerPojo = new MarkerPojo();

        String location = mapCoords.get(jsonObject.optString("country"));
        if(TextUtils.isEmpty(location)) {
            logger.info("Location not found for country : " + jsonObject.optString("country"));
            return null;
        }
        markerPojo.location = new PointPojo(Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1]));
        markerPojo.legend = jsonObject.optInt("cases") + "";
        markerPojo.title =jsonObject.optString("country");
        markerPojo.subtext = "Total Cases : " + jsonObject.optString("cases");
        markerPojo.iconUrl = "/resources/icons/marker_red.png";
        markerPojo.data = new Gson().fromJson(jsonObject.toString(), HashMap.class);
        markerPojo.data.remove("cases");
        markerPojo.data.remove("country");
        markerPojo.data = markerPojo.data.keySet().stream()
                .collect(Collectors.toMap(key -> StringUtils.capitalize(key), key -> markerPojo.data.get(key)));
        return markerPojo;
    }

    public static void main(String[] args) {
        updateMapCoords();
        System.out.println("MapCoords : " + gson.toJson(mapCoords));
    }

    private static void updateMapCoords() {
        try {
            String countriesString = ContentUtils.getContent("countries.json");
            JSONArray jsonArray = new JSONArray(countriesString);
            mapCoords = Maps.newHashMap();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mapCoords.put(jsonObject.optString("name"), jsonObject.optString("location"));
            }
        } catch (Exception e) {
            BounceUtils.logError(e);
            e.printStackTrace();
        }
    }
}
