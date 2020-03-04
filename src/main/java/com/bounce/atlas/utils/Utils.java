package com.bounce.atlas.utils;

import com.bounce.utils.BounceUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    // for reference - 2020-02-28T13:24:51.225+05:30
    public static long convertHawkeyeTimstamp(String time) {
        try {
            time = time.substring(0, time.length() - 6);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = format.parse(time);
            DateTime dateTime = new DateTime(date.getTime()).plusHours(5).plusMinutes(30);
            return dateTime.toInstant().getMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return -1;
    }

    public static long convertSQlTimestamp(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format.parse(time);
            DateTime dateTime = new DateTime(date.getTime()).plusHours(5).plusMinutes(30);
            return dateTime.toInstant().getMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("Time : " + new Date(convertHawkeyeTimstamp("2020-02-28T13:24:51.225+05:30")));
    }

}
