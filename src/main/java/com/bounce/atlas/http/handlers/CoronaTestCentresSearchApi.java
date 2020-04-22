package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.BikeDetailsCard;
import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.pojo.PointPojo;
import com.bounce.atlas.utils.Constants;
import com.bounce.atlas.utils.Utils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.Pair;
import com.bounce.utils.RestGenericRequest;
import com.bounce.utils.apis.BaseApiHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CoronaTestCentresSearchApi extends BaseApiHandler {

    private static Pair<Map<Object, Object>, Long> responseTimestampPair;

    public CoronaTestCentresSearchApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest(){
        try {
            super.onRequest();

            Map<Object, Object> response = Maps.newHashMap();
            if(responseTimestampPair != null && (System.currentTimeMillis() - responseTimestampPair.getValue() < 1000 * 60 * 60)) {
                logger.info("Showing cached information");
                response = responseTimestampPair.getKey();
                sendSuccessResponse(asyncResponse, response);
                return;
            }
            logger.info("Cache outdated. Fetching information again");
            List<MarkerPojo> markers = Lists.newArrayList();
            try {
                super.onRequest();

                String testCentresStr =
                        Utils.httpGetWithoutCertificateCheck("https://covid.icmr.org.in/index.php/testing-facilities?option=com_hotspots&view=jsonv4&task=gethotspots&hs-language=en-GB&page=1&per_page=500&total_pages=1&total_entries=158&cat=&level=2&ne=61.979994%2C150.880238&sw=-78.331766%2C-13.299449&c=-24.767312%2C68.790395&fs=0&offset=0&format=raw", new JSONObject("{}"), null);

                JSONArray testCentres = new JSONObject(testCentresStr).optJSONArray("items");
                for (int i = 0; i < testCentres.length(); i++) {
                    MarkerPojo marker = getMarkerForTestCentre(testCentres.optJSONObject(i));
                    if(marker != null) {
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
            responseTimestampPair = new Pair<>(response, System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
    }

    private MarkerPojo getMarkerForTestCentre(JSONObject jsonObject) {
        MarkerPojo markerPojo = new MarkerPojo();
        markerPojo.location = new PointPojo(jsonObject.optDouble("lat"), jsonObject.optDouble("lng"));
        String title = jsonObject.optString("title");
        markerPojo.title = title;
        markerPojo.legend = title.split(",")[0];
        markerPojo.subtext = "";

        markerPojo.data = Maps.newLinkedHashMap();
        if(jsonObject.optInt("catid") == 16) {
            markerPojo.iconUrl = "/resources/icons/marker_green.png";
            markerPojo.data.put("Type", "Private");
        } else {
            markerPojo.iconUrl = "/resources/icons/marker_blue.png";
            markerPojo.data.put("Type", "Public");
        }
        markerPojo.data.put("Description", jsonObject.optString("description"));
        markerPojo.data.put("Address", jsonObject.optString("address"));
        markerPojo.data.put("City", jsonObject.optString("city"));
        markerPojo.data.put("Date", jsonObject.optString("date"));
        markerPojo.data.put("More", "<a href='https://covid.icmr.org.in" + jsonObject.optString("readmore") + "' target='_blank'>Click Here</a>");

        return markerPojo;
    }
}
