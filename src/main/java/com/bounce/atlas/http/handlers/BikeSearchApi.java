package com.bounce.atlas.http.handlers;

import com.bounce.atlas.utils.QueryUtils;
import com.bounce.atlas.utils.RenderUtils;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.status.Status;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
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

            if(!TextUtils.isEmpty(searchQuery.trim())) {
                List<BikeRecord> bikes = QueryUtils.getBikes(searchQuery);

                logger.info("Bikes : " + bikes);

                Map<Object, Object> response = Maps.newHashMap();
                response.put("markers", RenderUtils.getBikesAsMarkers(bikes));
                if(bikes.size() == 1) {
                    response.put("events", BikeEventsApi.getCards(bikes.get(0).getId(), System.currentTimeMillis()));
                }

                sendSuccessResponse(asyncResponse, response);
            } else {
                asyncResponse.resume(Response.status(400).entity(gson.toJson(Status.buildFailure(400, "Looks like search field is empty"))).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
