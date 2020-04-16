package com.bounce.atlas.utils;

import com.bounce.utils.BounceUtils;
import com.bounce.utils.Pair;
import org.apache.http.util.TextUtils;
import org.joda.time.DateTime;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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

    public static long convertHtmlInputTimestamp(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = format.parse(time);
            DateTime dateTime = new DateTime(date.getTime());
            return dateTime.toInstant().getMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return -1;
    }

    public static String toHtmlInputTimestamp(long time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String date = format.format(new Date(time));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return null;
    }

    public static long convertSQlTimestamp(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format.parse(time);
            DateTime dateTime = new DateTime(date.getTime());
            return dateTime.toInstant().getMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("Time : " + new Date(convertHtmlInputTimestamp("2020-04-17T00:59")));
    }

    private static Pair<String, Integer> getRedisHostPortPair() {
        try {
            InputStream inputStream = new FileInputStream("config.ini");
            Properties prop = new Properties();
            prop.load(inputStream);

            String redisHost = prop.getProperty("redis.host");
            Integer redisPort = Integer.parseInt(prop.getProperty("redis.port"));

            return new Pair<>(redisHost, redisPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>("127.0.0.1", 6379);
    }

    public static void redisSet(String key, String data) {
        Pair<String, Integer> redisHostPort = getRedisHostPortPair();
        Jedis jedis = new Jedis(redisHostPort.getKey(), redisHostPort.getValue());
        jedis.set(key, data);
        jedis.close();
    }

    public static String redisGet(String key) {
        Pair<String, Integer> redisHostPort = getRedisHostPortPair();
        Jedis jedis = new Jedis(redisHostPort.getKey(), redisHostPort.getValue());
        String response = jedis.get(key);
        jedis.close();
        return response;
    }

    public static String redisGet(String key, String defaultString) {
        Pair<String, Integer> redisHostPort = getRedisHostPortPair();
        Jedis jedis = new Jedis(redisHostPort.getKey(), redisHostPort.getValue());
        String data = jedis.get(key);
        if(TextUtils.isEmpty(data)) {
            return defaultString;
        }
        return data;
    }

}
