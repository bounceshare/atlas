package com.bounce.atlas.http.handlers;

import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.Booking;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.List;
import java.util.Map;

public class BookingSearchApi extends BaseApiHandler {

    public BookingSearchApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        try {
            super.onRequest();
            String searchQuery = input.optString("searchQuery");

            BookingRecord booking = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Booking.BOOKING)
                    .where(Booking.BOOKING.ID.eq(Integer.parseInt(searchQuery))).fetchAny();

            Map<String, Object> markersPaths = QueryUtils.getMarkersAndPathForBooking(booking);
            Map<Object, Object> response = Maps.newHashMap();
            response.put("markers", markersPaths.get("markers"));
            response.put("paths", markersPaths.get("paths"));

            sendSuccessResponse(asyncResponse, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
