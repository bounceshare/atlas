package com.bounce.atlas.http.handlers;

import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.Bike;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;
import org.jooq.exception.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
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

            List<BikeRecord> bikes = QueryUtils.getBikes(searchQuery);
            List<Map<String, Object>> bikeMap = Lists.newArrayList();
            for(BikeRecord bike : bikes) {
                bikeMap.add(bike.intoMap());
            }

            Map<Object, Object> response = Maps.newHashMap();
            response.put("bikes", bikeMap);

            sendSuccessResponse(asyncResponse, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
