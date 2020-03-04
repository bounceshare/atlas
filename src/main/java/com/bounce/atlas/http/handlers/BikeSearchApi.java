package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BikeSearchApi extends BaseApiHandler {

    public BikeSearchApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        try {
            super.onRequest();
            String searchQuery = input.optString("searchQuery");

            if(!TextUtils.isEmpty(searchQuery)) {
                List<BikeRecord> bikes = QueryUtils.getBikes(searchQuery);

                logger.info("Bikes : " + bikes);

                Map<Object, Object> response = Maps.newHashMap();
                response.put("markers", MarkerPojo.getBikesAsMarkers(bikes));

                sendSuccessResponse(asyncResponse, response);
            } else {
                Map<Object, Object> response = Maps.newHashMap();
                response.put("markers", new ArrayList<MarkerPojo>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
