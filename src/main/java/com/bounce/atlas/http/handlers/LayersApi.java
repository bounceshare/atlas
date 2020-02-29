package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.CirclePojo;
import com.bounce.atlas.pojo.FencePojo;
import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.pojo.PathPojo;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
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

    public LayersApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                     HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest(){
        Map<String, Object> data = Maps.newHashMap();
        data.put("title", "Bounce Atlas");
        data.put("page", "home");

        List<MarkerPojo> markers = Lists.newArrayList();
        List<FencePojo> fences = Lists.newArrayList();
        List<CirclePojo> circles = Lists.newArrayList();
        List<PathPojo> paths = Lists.newArrayList();

        try {
            super.onRequest();

            double lat = input.optDouble("lat", 12.9160463);
            double lon = input.optDouble("lon", 77.5967117);

            int limit = input.optInt("limit", 10000);
            int radius = input.optInt("radius", 5000);
            String layers = input.optString("layers", "bikes,parking");


            data.put("location", "" + lat + "," + lon);
            data.put("layers", layers);

            List<String> layerList = Arrays.asList(layers.split(","));

            for(String layer : layerList) {
                switch (layer) {
                    case "bikes":
                        List<BikeRecord> bikes = QueryUtils.getBikes(lat, lon, 10000, radius);
                        List<MarkerPojo> bikeMarkers = QueryUtils.getBikesAsMarkers(bikes);
                        markers.addAll(bikeMarkers);
                        break;
                    case "parking":
                        fences = QueryUtils.getParkingFences(lat, lon, radius);
                        break;
                }
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

        sendSuccessResponse(asyncResponse, response);
    }
}
