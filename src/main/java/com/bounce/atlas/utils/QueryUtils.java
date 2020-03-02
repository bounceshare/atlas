package com.bounce.atlas.utils;

import com.bounce.atlas.pojo.PointPojo;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.dbmodels.public_.enums.BikeStatus;
import com.bounce.utils.dbmodels.public_.tables.Bike;
import com.bounce.utils.dbmodels.public_.tables.Booking;
import com.bounce.utils.dbmodels.public_.tables.Hub;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.HubRecord;
import com.google.common.collect.Lists;
import org.apache.http.util.TextUtils;
import org.jooq.Record;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryUtils {

    public static List<BikeRecord> getBikes(double lat, double lon, int limit, int radius) {
        return getBikes(lat, lon, limit, radius, null);
    }

    public static List<BikeRecord> getBikes(double lat, double lon, int limit, int radius, BikeStatus status) {
        try {
            String sql = null;
            if(status == null) {
                sql = "SELECT  * FROM bike WHERE ST_DWithin(CAST(ST_MakePoint(bike.lon, bike.lat) AS geography(GEOMETRY,-1)), " +
                                "CAST(ST_MakePoint(" + lon + "," + lat + ") AS geography(GEOMETRY,-1)), " + radius +
                                ") " + "limit " + limit;
            } else {
                sql = "SELECT  * FROM bike WHERE ST_DWithin(CAST(ST_MakePoint(bike.lon, bike.lat) AS geography(GEOMETRY,-1)), " +
                        "CAST(ST_MakePoint(" + lon + "," + lat + ") AS geography(GEOMETRY,-1)), " + radius +
                        ") AND status = '" + status.getLiteral() + "' limit " + limit;
            }

            List<BikeRecord> bikes = DatabaseConnector.getDb().getReadDbConnector().fetch(sql).into(Bike.BIKE);
            return bikes;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return Lists.newArrayList();
    }

    public static List<HubRecord> getHubs(double lat, double lon, int limit, int radius) {
        try {
            String sql = "SELECT * FROM hub WHERE ST_DWithin(CAST(ST_MakePoint(hub.lon, hub.lat) AS geography(GEOMETRY,-1)), " +
                    "CAST(ST_MakePoint(" + lon + "," + lat + ") AS geography(GEOMETRY,-1)), " + radius +
                    ") " + "limit " + limit;

            List<HubRecord> hubs = DatabaseConnector.getDb().getReadDbConnector().fetch(sql).into(Hub.HUB);
            return hubs;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return Lists.newArrayList();
    }

    public static List<BikeRecord> getBikes(String searchQuery) {
        try {
            boolean isBikeId = false;
            if (!TextUtils.isEmpty(searchQuery)) {
                try {
                    int num = Integer.parseInt(searchQuery);
                    if (num > 9999) {
                        isBikeId = true;
                    }
                } catch (NumberFormatException e) {
                }
            }
            List<BikeRecord> bikes;
            if (isBikeId) {
                bikes = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Bike.BIKE)
                        .where(Bike.BIKE.ID.eq(Integer.parseInt(searchQuery))).fetch();
            } else {
                bikes = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Bike.BIKE)
                        .where(Bike.BIKE.LICENSE_PLATE.contains(searchQuery)).fetch();
            }
            return bikes;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return Lists.newArrayList();
    }

    public static BikeRecord getBike(int bikeId) {
        Result<Record> records =
                DatabaseConnector.getDb().getConnector().select().from(Bike.BIKE).where(Bike.BIKE.ID.eq(bikeId))
                        .limit(1).fetch();

        if (records != null && records.size() > 0) {
            return (BikeRecord) records.get(0);
        }
        return null;
    }

    public static List<Map<String, Object>> getParkingAround(double lat, double lon, int radius) {
        List<Map<String, Object>> parkingData = new ArrayList<>();
        try {
            String sql = "SELECT ST_AsText(ST_ExteriorRing(fence)) as fence, name, category_name, negative, priority \n" +
                    "FROM public.parking \n" +
                    "LEFT JOIN public.parking_category on parking.category_id = parking_category.id\n" +
                    "WHERE ST_DWithin(fence::geography, ST_MakePoint(" + lon + "," + lat + ")::geography," + radius + ")\n" +
                    "    and active and verified";
            Result<Record> records = DatabaseConnector.getDb().getConnector().fetch(sql);
            for (Record record : records) {
                Map<String, Object> parkingMap = new HashMap<>();
                parkingMap.put("polygon", PointPojo.getPointsFromSqlLineString(record.get("fence").toString()));
                parkingMap.put("name", record.get("name"));
                parkingMap.put("category", record.get("category_name"));
                parkingMap.put("negative", record.get("negative"));
                parkingData.add(parkingMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return parkingData;
    }

    public static BookingRecord getLatestBooking(BikeRecord bike) {
        Result<Record> records = DatabaseConnector.getDb().getConnector().select().from(Booking.BOOKING)
                .where(Booking.BOOKING.BIKE_ID.eq(bike.getId()))
                .orderBy(Booking.BOOKING.CREATED_ON.desc()).limit(20).fetch();

        if (records != null && records.size() > 0) {
            return (BookingRecord) records.get(0);
        }
        return null;
    }

}
