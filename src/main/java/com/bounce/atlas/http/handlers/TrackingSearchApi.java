package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.pojo.PathPojo;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.atlas.utils.Utils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.Keys;
import com.bounce.utils.dbmodels.public_.tables.records.AxcessRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TrackingSearchApi extends BaseApiHandler {

    public TrackingSearchApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        Map<Object, Object> response = Maps.newHashMap();
        List<MarkerPojo> markers = Lists.newArrayList();
        List<PathPojo> paths = Lists.newArrayList();
        try {
            super.onRequest();

            String searchQuery = input.optString("searchQuery");

            if(TextUtils.isEmpty(searchQuery)) {
                response.put("markers", markers);
                response.put("paths", paths);

                sendSuccessResponse(asyncResponse, response);
                return;
            }

            String[] splits = searchQuery.split(" ");

            int bikeId = Integer.parseInt(splits[0]);

            long to = System.currentTimeMillis();
            long from = DateTime.now().minusHours(6).getMillis();

            if(splits.length > 1) {
                String max = splits[1];
                String min = splits[1];

                to = Utils.convertSQlTimestamp(max);
                from = Utils.convertSQlTimestamp(min);
            }

            BikeRecord bike = QueryUtils.getBike(bikeId);
            AxcessRecord axcess = bike.fetchParent(Keys.BIKE__BIKE_AXCESS_ID_FKEY);

            List<Map<String, Object>> trackings = QueryUtils.getTrackingRecords(axcess.getImei(), from, to);
            for(Map<String, Object> tracking : trackings) {
                MarkerPojo marker = MarkerPojo.getMarkerPojo(tracking);
                if(marker != null) {
                    markers.add(marker);
                }
            }

            PathPojo pojo = PathPojo.getMarkerPojo(trackings);
            pojo.data = Maps.newHashMap();
            pojo.data.put("From", new Date(from));
            pojo.data.put("To", new Date(to));
            if(pojo != null && pojo.points != null && pojo.points.size() > 0) {
                paths.add(pojo);
            }

            logger.info("TrackingSearchApi:: Bike : " + bike);



        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        response.put("markers", markers);
        response.put("paths", paths);

        sendSuccessResponse(asyncResponse, response);
    }

    public static Map<Object, Object> getTrackingRenderData(String imei, long from, long to) {
        Map<Object, Object> response = Maps.newHashMap();
        List<MarkerPojo> markers = Lists.newArrayList();
        List<PathPojo> paths = Lists.newArrayList();
        try {
            List<Map<String, Object>> trackings = QueryUtils.getTrackingRecords(imei, from, to);
            for(Map<String, Object> tracking : trackings) {
                MarkerPojo marker = MarkerPojo.getMarkerPojo(tracking);
                if(marker != null) {
                    markers.add(marker);
                }
            }

            PathPojo pojo = PathPojo.getMarkerPojo(trackings);
            pojo.data = Maps.newHashMap();
            pojo.data.put("From", new Date(from));
            pojo.data.put("To", new Date(to));
            if(pojo != null) {
                paths.add(pojo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("markers", markers);
        response.put("paths", paths);
        return response;
    }
}
