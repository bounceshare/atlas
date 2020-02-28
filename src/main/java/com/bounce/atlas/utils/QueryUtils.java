package com.bounce.atlas.utils;

import com.bounce.utils.BounceUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.dbmodels.public_.tables.Bike;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.google.common.collect.Lists;
import org.apache.http.util.TextUtils;

import java.util.List;

public class QueryUtils {

    public static List<BikeRecord> getBikes(double lat, double lon, int limit) {
        try {
            String sql = "select * from bike WHERE bike.axcess_id IS NOT NULL AND bike.type != 'cycle'" +
                    " and axcess_id is not NULL " +
                    " order by ST_SetSRID(ST_MakePoint(lon, lat), 4326) <-> ST_SetSRID(ST_MakePoint(" + lon + "," +
                    lat + ") , 4326) " + "limit " + limit;

            List<BikeRecord> bikes = DatabaseConnector.getDb().getReadDbConnector().fetch(sql).into(Bike.BIKE);
            return bikes;
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

}
