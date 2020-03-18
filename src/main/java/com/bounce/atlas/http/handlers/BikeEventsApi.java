package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.BikeDetailsCard;
import com.bounce.atlas.utils.Constants;
import com.bounce.atlas.utils.Utils;
import com.bounce.utils.*;
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
import org.apache.http.util.TextUtils;
import org.joda.time.DateTime;
import org.jooq.meta.derby.sys.Sys;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

            bikeDetailsCards = getCards(bikeId, from);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(bikeDetailsCards, new BikeDetailsCard.CardComparator());

        Map<Object, Object> response = Maps.newHashMap();
        response.put("events", bikeDetailsCards);

        sendSuccessResponse(asyncResponse, response);
    }

    public static List<BikeDetailsCard> getCards(int bikeId, long from) {
        List<BikeDetailsCard> bikeDetailsCards = Lists.newArrayList();
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
        if (System.currentTimeMillis() - from > 60 * 1000 * 60) {
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

        logger.info("bslFirstTime : " + bslFirstTime);
        logger.info("bslLastTime : " + bslLastTime);
        logger.info("bikeId : " + bikeId);

        List<BikeStatusLogRecord> bikeStatusLogRecords = null;
        if(GlobalConfigUtils.getGlobalConfigBoolean("ATLAS_USE_BSL_FROM_REDIS", true)) {
            bikeStatusLogRecords = getBsl(bikeId, bslLastTime, bslFirstTime);
        } else {
            bikeStatusLogRecords = DatabaseConnector.getDb().getReadDbConnector().selectFrom(BikeStatusLog.BIKE_STATUS_LOG)
                    .where(BikeStatusLog.BIKE_STATUS_LOG.BIKE_ID.eq(bikeId))
                    .and(BikeStatusLog.BIKE_STATUS_LOG.CREATED_ON.greaterThan(new Timestamp(bslLastTime)))
                    .and(BikeStatusLog.BIKE_STATUS_LOG.CREATED_ON.lessThan(new Timestamp(bslFirstTime))).fetch();
        }

        for(BikeStatusLogRecord bsl : bikeStatusLogRecords) {
            bikeDetailsCards.add(BikeDetailsCard.getCard(bsl));
        }

        Collections.sort(bikeDetailsCards, new BikeDetailsCard.CardComparator());

        JSONArray tasksArray = getTasks(bikeId);
        if(tasksArray.length() > 0) {
            for (int i = 0; i < tasksArray.length(); i++) {
                bikeDetailsCards.addAll(BikeDetailsCard.getCard(tasksArray.getJSONObject(i)));
            }
        }
        return bikeDetailsCards;
    }

    private static JSONArray getTasks(int bikeId) {
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray reqArray = new JSONArray();
            reqArray.put("" + bikeId);
            jsonObject.put("assetIds", reqArray);
            String response = RestGenericRequest.httpPost(ConfigData.getConfig().get(Constants.Config.HAWKEYE_URL), jsonObject, Maps.newHashMap());
            if(!TextUtils.isEmpty(response)) {
                JSONObject responseObj = new JSONObject(response);
                jsonArray = responseObj.optJSONObject("data").optJSONArray("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return jsonArray;
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

            logger.info("BSL records found : " + bikeStatusLogSet.size());

            for(String data : bikeStatusLogSet) {
                BikeStatusLogRecord record = new BikeStatusLogRecord();
                Type type = new TypeToken<Map<String, Object>>(){}.getType();

                JSONObject jsonObject = new JSONObject(data);
                long createdOn = jsonObject.optLong("created_on");
                long updatedOn = jsonObject.optLong("updated_on");

                Map<String, Object> map = new Gson().fromJson(data, type);

                map.remove("created_on");
                map.remove("updated_on");

                record.fromMap(map);
                record.setCreatedOn(new Timestamp(createdOn));
                record.setUpdatedOn(new Timestamp(updatedOn));

                bikeStatusLogRecords.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return bikeStatusLogRecords;
    }

    public static Date toTimestamp(String input) {
        //Sep 30, 2019 8:20:55 PM
        try{
            //Converting the input String to Date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
            return sdf.parse(input);
            //Displaying the date
        }catch(ParseException pe){
            BounceUtils.logError(pe);
            pe.printStackTrace();
        }
        return null;
    }

}
