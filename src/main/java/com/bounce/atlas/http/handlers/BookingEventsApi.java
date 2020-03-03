package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.BikeDetailsCard;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.BikeStatusLog;
import com.bounce.utils.dbmodels.public_.tables.Booking;
import com.bounce.utils.dbmodels.public_.tables.EndTripFeedback;
import com.bounce.utils.dbmodels.public_.tables.records.BikeStatusLogRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.EndTripFeedbackRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BookingEventsApi extends BaseApiHandler {

    public BookingEventsApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        List<BikeDetailsCard> bikeDetailsCards = Lists.newArrayList();
        try {
            super.onRequest();
            int bookingId = input.optInt("id");

            BookingRecord booking = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Booking.BOOKING)
                    .where(Booking.BOOKING.ID.eq(bookingId)).fetchAny();

            bikeDetailsCards.addAll(BikeDetailsCard.getCard(booking));
            List<EndTripFeedbackRecord> endTripFeedbacks =
                    DatabaseConnector.getDb().getReadDbConnector().selectFrom(EndTripFeedback.END_TRIP_FEEDBACK)
                            .where(EndTripFeedback.END_TRIP_FEEDBACK.BOOKING_ID.eq(booking.getId())).fetch();

            for (EndTripFeedbackRecord endTripFeedback : endTripFeedbacks) {
                bikeDetailsCards.add(BikeDetailsCard.getCard(endTripFeedback));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(bikeDetailsCards, new BikeDetailsCard.CardComparator());
        Map<Object, Object> response = Maps.newHashMap();
        response.put("events", bikeDetailsCards);

        sendSuccessResponse(asyncResponse, response);
    }

}