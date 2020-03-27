package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.BikeDetailsCard;
import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.pojo.PointPojo;
import com.bounce.atlas.utils.Constants;
import com.bounce.atlas.utils.ContentUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.Pair;
import com.bounce.utils.RestGenericRequest;
import com.bounce.utils.apis.BaseApiHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CoronaSearchApi extends BaseApiHandler {

    private static Map<String, String> countriesCoords;
    private static Map<String, String> statesCoords;
    private static Pair<Map<Object, Object>, Long> responseTimestampPair;

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
        if(responseTimestampPair != null && (System.currentTimeMillis() - responseTimestampPair.getValue() < 1000 * 60 * 60)) {
            logger.info("Showing cached information");
            response = responseTimestampPair.getKey();
            sendSuccessResponse(asyncResponse, response);
            return;
        }
        logger.info("Cache outdated. Fetching information again");
        List<MarkerPojo> markers = Lists.newArrayList();
        List<BikeDetailsCard> cards = Lists.newArrayList();
        try {
            super.onRequest();

            String worldDataResponse =
                    RestGenericRequest.httpGet("https://coronavirus-19-api.herokuapp.com/countries", new JSONObject("{}"), null);
            JSONArray worldData = new JSONArray(worldDataResponse);
            JSONObject indiaObj = null;
            for (int i = 0; i < worldData.length(); i++) {
                MarkerPojo marker = getMarkerPojoForCountry(worldData.getJSONObject(i));
                if(worldData.getJSONObject((i)).optString("country").toLowerCase().equals("india")) {
                    indiaObj = worldData.getJSONObject(i);
                }
                if(marker!=null) {
                    markers.add(marker);
                }
            }

            String indiaDataResponse = RestGenericRequest.httpGet("https://script.googleusercontent.com/macros/echo?user_content_key=ziTggsRx2_Si33Fqw0rto6emuJbWQnpJvN37O-CKfAtq_OiUmjepLv1s9roBJOVrHhWkoii-I4jAbiwY2JTwblGkzA4IfhJ_m5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnKXFvsR88vL4WiBr168omFadgngDnj25DLpEvLRaiIpzZr1NvbW-Bo38vshdDBv10tpytj_A4aoE&lib=Mm1FD1HVuydJN5yAB3dc_e8h00DPSBbB3", new JSONObject("{}"), null);
            List<MarkerPojo> stateMarkers = null;
            if(TextUtils.isEmpty(indiaDataResponse)) {
                stateMarkers = getStateMarkers(null, indiaObj);
            } else {
                Type type = new TypeToken<Map<String, Integer>>() {
                }.getType();
                Map<String, Integer> stateResponseMap = gson.fromJson(indiaDataResponse, type);
                stateMarkers = getStateMarkers(stateResponseMap, indiaObj);
            }
            markers.addAll(stateMarkers);

            BikeDetailsCard globalCard = new BikeDetailsCard();
            JSONObject globalData =
                    new JSONObject(RestGenericRequest.httpGet("https://coronavirus-19-api.herokuapp.com/all", new JSONObject("{}"), null));

            globalCard.header = "Worldwide";
            globalCard.timeString = "";
            globalCard.time = System.currentTimeMillis();
            globalCard.body = "";
            globalCard.color = Constants.Color.INFO;
            globalCard.timelineHeader = "Coronavirus Cases";
            globalCard.details = Maps.newHashMap();
            globalCard.details.put("Total Cases", globalData.optInt("cases") + "");
            globalCard.details.put("Total Deaths", globalData.optInt("deaths") + "");
            globalCard.details.put("Total Recovered", globalData.optInt("recovered") + "");



            cards.add(globalCard);
            cards.add(getCard(indiaObj, 1));

            for (int i = 0; i < 10; i++) {
                cards.add(getCard(worldData.getJSONObject(i), i + 2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        response.put("markers", markers);
        response.put("autoRefresh", false);
        response.put("events", cards);

        sendSuccessResponse(asyncResponse, response);
        responseTimestampPair = new Pair<>(response, System.currentTimeMillis());
    }

    private BikeDetailsCard getCard(JSONObject countryObj, int i) {
        BikeDetailsCard countryCard = new BikeDetailsCard();
        countryCard.header = StringUtils.capitalize(countryObj.optString("country"));
        countryCard.timeString = "";
        countryCard.time = System.currentTimeMillis() - i * 10000;
        countryCard.body = "";
        countryCard.color = Constants.Color.INFO;
        countryCard.timelineHeader = "Coronavirus Cases";
        countryCard.details = Maps.newHashMap();
        countryCard.details.put("Total Cases", countryObj.optInt("cases") + "");
        countryCard.details.put("Today Cases", countryObj.optInt("todayCases") + "");
        countryCard.details.put("Deaths", countryObj.optInt("deaths") + "");
        countryCard.details.put("Today Deaths", countryObj.optInt("todayDeaths") + "");
        countryCard.details.put("Recovered", countryObj.optInt("recovered") + "");
        countryCard.details.put("Active", countryObj.optInt("active") + "");
        countryCard.details.put("Critical", countryObj.optInt("critical") + "");
        countryCard.details.put("CasesPerOneMillion", countryObj.optInt("casesPerOneMillion") + "");
        countryCard.details.put("DeathsPerOneMillion", countryObj.optInt("deathsPerOneMillion") + "");

        return countryCard;
    }

    private List<MarkerPojo> getStateMarkers(Map<String, Integer> stateResponseMap, JSONObject indiaObj) {
        List<MarkerPojo> markerPojos = Lists.newArrayList();
        if(stateResponseMap != null) {
            stateResponseMap.put("Telangana", stateResponseMap.get("Telengana"));
            stateResponseMap.remove("Telengana");

            for (Map.Entry<String, Integer> entry : stateResponseMap.entrySet()) {
                MarkerPojo markerPojo = new MarkerPojo();
                String location = statesCoords.get(entry.getKey());
                if (TextUtils.isEmpty(location)) {
                    logger.info("Location not found for state : " + entry.getKey());
                    continue;
                }
                markerPojo.location = new PointPojo(Double.parseDouble(location.split(",")[0]), Double.parseDouble(location.split(",")[1]));
                markerPojo.legend = entry.getValue() + "";
                markerPojo.count = entry.getValue();
                markerPojo.title = entry.getKey();
                markerPojo.subtext = "Total Cases : " + entry.getValue();
                markerPojo.iconUrl = "/resources/icons/marker_red.png";
                markerPojo.data = new Gson().fromJson(indiaObj.toString(), HashMap.class);
                markerPojo.data.remove("cases");
                markerPojo.data.remove("country");
                markerPojo.data = markerPojo.data.keySet().stream()
                        .collect(Collectors.toMap(key -> "India " + StringUtils.capitalize(key), key -> markerPojo.data.get(key)));
                markerPojo.data.put("<b>India Total Cases</b>", "<b>" + indiaObj.optInt("cases") + "</b>");
                markerPojos.add(markerPojo);
            }
        } else {
            MarkerPojo markerPojo = new MarkerPojo();
            String location = countriesCoords.get(indiaObj.optString("country"));
            markerPojo.location = new PointPojo(Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1]));
            markerPojo.legend = indiaObj.optInt("cases") + "";
            markerPojo.count = indiaObj.optInt("cases");
            markerPojo.title =indiaObj.optString("country");
            markerPojo.subtext = "Total Cases : " + indiaObj.optString("cases");
            markerPojo.iconUrl = "/resources/icons/marker_red.png";
            markerPojo.data = new Gson().fromJson(indiaObj.toString(), HashMap.class);
            markerPojo.data.remove("cases");
            markerPojo.data.remove("country");
            markerPojo.data = markerPojo.data.keySet().stream()
                    .collect(Collectors.toMap(key -> StringUtils.capitalize(key), key -> markerPojo.data.get(key)));
            markerPojos.add(markerPojo);
        }

        return markerPojos;
    }

    private MarkerPojo getMarkerPojoForCountry(JSONObject jsonObject) {
        MarkerPojo markerPojo = new MarkerPojo();

        if(jsonObject.optString("country").toLowerCase().equals("india")) {
            return null;
        }

        String location = countriesCoords.get(jsonObject.optString("country"));
        if(TextUtils.isEmpty(location)) {
            logger.info("Location not found for country : " + jsonObject.optString("country"));
            return null;
        }
        markerPojo.location = new PointPojo(Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1]));
        markerPojo.legend = jsonObject.optInt("cases") + "";
        markerPojo.count = jsonObject.optInt("cases");
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
        System.out.println("MapCoords : " + gson.toJson(countriesCoords));
    }

    private static void updateMapCoords() {
        try {
            String countriesString = ContentUtils.getContent("countries.json");
            JSONArray jsonArray = new JSONArray(countriesString);
            countriesCoords = Maps.newHashMap();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countriesCoords.put(jsonObject.optString("name"), jsonObject.optString("location"));
            }

            String statesString = ContentUtils.getContent("states.json");
            JSONArray statesArray = new JSONArray(statesString);
            statesCoords = Maps.newHashMap();
            for (int i = 0; i < statesArray.length(); i++) {
                JSONObject jsonObject = statesArray.getJSONObject(i);
                statesCoords.put(jsonObject.optString("name"), jsonObject.optString("location"));
            }
        } catch (Exception e) {
            BounceUtils.logError(e);
            e.printStackTrace();
        }
    }
}
