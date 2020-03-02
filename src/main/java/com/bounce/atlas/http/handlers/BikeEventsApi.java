package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.BikeDetailsCard;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.GlobalConfigUtils;
import com.bounce.utils.RealtimeData;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.tables.BikeStatusLog;
import com.bounce.utils.dbmodels.public_.tables.Booking;
import com.bounce.utils.dbmodels.public_.tables.EndTripFeedback;
import com.bounce.utils.dbmodels.public_.tables.records.BikeStatusLogRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.EndTripFeedbackRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

public class BikeEventsApi extends BaseApiHandler {

    public BikeEventsApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                         HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        List<BikeDetailsCard> bikeDetailsCards = Lists.newArrayList();
        try {
            super.onRequest();
            int bikeId = input.optInt("id");

            long from = input.optLong("from", System.currentTimeMillis());

            List<BookingRecord> bookings = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Booking.BOOKING)
                    .where(Booking.BOOKING.BIKE_ID.eq(bikeId))
                    .and(Booking.BOOKING.CREATED_ON.lessThan(new Timestamp(from))).orderBy(Booking.BOOKING.CREATED_ON.desc())
                    .limit(40).fetch();

            for (BookingRecord booking : bookings) {
                bikeDetailsCards.addAll(BikeDetailsCard.getCard(booking));
                List<EndTripFeedbackRecord> endTripFeedbacks =
                        DatabaseConnector.getDb().getReadDbConnector().selectFrom(EndTripFeedback.END_TRIP_FEEDBACK)
                                .where(EndTripFeedback.END_TRIP_FEEDBACK.BOOKING_ID.eq(booking.getId())).fetch();

                for (EndTripFeedbackRecord endTripFeedback : endTripFeedbacks) {
                    bikeDetailsCards.add(BikeDetailsCard.getCard(endTripFeedback));
                }
            }

            boolean fetchTillNow = true;
            if (input.has("from")) {
                fetchTillNow = false;
            }

            long bslFirstTime = 0;
            long bslLastTime = 0;
            if (fetchTillNow && bookings.size() > 1) {
                bslFirstTime = System.currentTimeMillis();
                bslLastTime = bookings.get(bookings.size() - 1).getCreatedOn().getTime();
            } else if (!fetchTillNow && bookings.size() > 1) {
                bslFirstTime = bookings.get(0).getCreatedOn().getTime();
                bslLastTime = bookings.get(bookings.size() - 1).getCreatedOn().getTime();
            } else if (fetchTillNow && bookings.size() < 1) {
                bslFirstTime = System.currentTimeMillis();
                bslLastTime = DateTime.now().minusDays(2).getMillis();
            } else if (!fetchTillNow && bookings.size() < 1) {
                bslFirstTime = from;
                bslLastTime = new DateTime(from).minusDays(2).getMillis();
            }

            List<BikeStatusLogRecord> bikeStatusLogRecords = null;
            if(GlobalConfigUtils.getGlobalConfigBoolean("ATLAS_USE_BSL_FROM_REDIS", true)) {
                bikeStatusLogRecords = getBsl(bikeId, bslFirstTime, bslLastTime);
            } else {
                bikeStatusLogRecords = DatabaseConnector.getDb().getReadDbConnector().selectFrom(BikeStatusLog.BIKE_STATUS_LOG)
                                .where(BikeStatusLog.BIKE_STATUS_LOG.BIKE_ID.eq(bikeId))
                                .and(BikeStatusLog.BIKE_STATUS_LOG.CREATED_ON.greaterThan(new Timestamp(bslLastTime)))
                                .and(BikeStatusLog.BIKE_STATUS_LOG.CREATED_ON.lessThan(new Timestamp(bslFirstTime))).fetch();
            }

            for(BikeStatusLogRecord bsl : bikeStatusLogRecords) {
                bikeDetailsCards.add(BikeDetailsCard.getCard(bsl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(bikeDetailsCards, new BikeDetailsCard.CardComparator());
        Map<Object, Object> response = Maps.newHashMap();
        response.put("events", bikeDetailsCards);

        sendSuccessResponse(asyncResponse, response);
    }

    private static List<BikeStatusLogRecord> getBsl(int bikeId, long startTime, long endTime) {
        Set<String> bikeStatusLogSet = new HashSet<>();
        List<BikeStatusLogRecord> bikeStatusLogRecords = Lists.newArrayList();
        try {
            try (Jedis jedis = RealtimeData.get().getJedisFactory().getTrackingJedisConnection(1)) {
                bikeStatusLogSet = jedis.zrevrangeByScore("bike_status_created_on_bike_id_" + bikeId,
                        endTime, startTime);
            } catch (Exception e) {
                logger.info("Exception while fetching BSL for bike " + bikeId);
                BounceUtils.logError(e);
                e.printStackTrace();
            }

            for(String bslString : bikeStatusLogSet) {
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> map = new Gson().fromJson(bslString, type);
                BikeStatusLogRecord record = new BikeStatusLogRecord();
                record.fromMap(map);

                bikeStatusLogRecords.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return bikeStatusLogRecords;
    }

}
