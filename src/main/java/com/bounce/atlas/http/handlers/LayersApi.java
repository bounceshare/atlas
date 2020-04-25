package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.CirclePojo;
import com.bounce.atlas.pojo.FencePojo;
import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.pojo.PathPojo;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.atlas.utils.RenderUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.Pair;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.enums.BikeStatus;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.HubRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LayersApi extends BaseApiHandler {

    private String path;

    public LayersApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                     HttpServletResponse httpResponse, String path) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
        this.path = path;
    }

    @Override
    public void onRequest(){
        try {
            super.onRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<MarkerPojo> markers = Lists.newArrayList();
        List<FencePojo> fences = Lists.newArrayList();
        List<CirclePojo> circles = Lists.newArrayList();
        List<PathPojo> paths = Lists.newArrayList();

        double lat = input.optDouble("lat", 12.9160463);
        double lon = input.optDouble("lon", 77.5967117);
        int radius = input.optInt("radius", 5000);
        String searchQuery = input.optString("searchQuery", null);

        if(TextUtils.isEmpty(path)) {
            path = "bikes";
        }

        try {
            List<String> layerList = Arrays.asList(searchQuery.split(","));
            switch (path) {
                case "bikes":
                    List<BikeRecord> bikes = QueryUtils.getBikes(lat, lon, 20000, radius, null, searchQuery);
                    List<MarkerPojo> bikeMarkers = RenderUtils.getBikesAsMarkers(bikes);
                    markers.addAll(bikeMarkers);
                    break;
                case "parking":
                    fences = RenderUtils.getParkingFences(lat, lon, radius);
                    break;
                case "idle":
                    List<BikeRecord> idleBikes = QueryUtils.getBikes(lat, lon, 10000, radius, BikeStatus.idle, searchQuery);
                    markers.addAll(RenderUtils.getBikesAsMarkers(idleBikes));
                    break;
                case "busy":
                    List<BikeRecord> busyBikes = QueryUtils.getBikes(lat, lon, 10000, radius, BikeStatus.busy, searchQuery);
                    markers.addAll(RenderUtils.getBikesAsMarkers(busyBikes));
                    break;
                case "oos":
                    List<BikeRecord> oosBikes = QueryUtils.getBikes(lat, lon, 10000, radius, BikeStatus.oos, searchQuery);
                    markers.addAll(RenderUtils.getBikesAsMarkers(oosBikes));
                    break;
                case "bookings":
                    List<BikeRecord> inTripBikes = QueryUtils.getBikes(lat, lon, 1000, radius, BikeStatus.busy, searchQuery);
                    List<Pair<BikeRecord, BookingRecord>> pairs = Lists.newArrayList();
                    for(BikeRecord bike : inTripBikes) {
                        BookingRecord booking = QueryUtils.getLatestBooking(bike);
                        pairs.add(new Pair<>(bike, booking));
                    }
                    markers.addAll(RenderUtils.getBookingMarkers(pairs));
                    break;
                case "hubs":
                    List<HubRecord> hubs = QueryUtils.getHubs(lat, lon, 100, radius, searchQuery);
                    circles.addAll(RenderUtils.getHubsAsCircles(hubs));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        Map<Object, Object> response = Maps.newHashMap();
        response.put("markers", markers);
        response.put("fences", fences);
        response.put("circles", circles);
        response.put("paths", paths);
        if(TextUtils.isEmpty(searchQuery)) {
            response.put("autoRefresh", true);
        } else {
            response.put("autoRefresh", false);
        }

        sendSuccessResponse(asyncResponse, response);
    }
}
