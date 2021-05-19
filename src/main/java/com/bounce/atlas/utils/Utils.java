package com.bounce.atlas.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class Utils {

    protected static Logger logger = Log.getLogger(Utils.class.getCanonicalName());

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
            Utils.logError(e);
        }
        return -1;
    }

    public static boolean isValidLatLong(String location) {
        try {
            double lat = Double.parseDouble(location.split(",")[0]);
            double lon = Double.parseDouble(location.split(",")[1]);
            if(lat < -90 || lat > 90 || lon < -180 || lon > 180) return false;
            return true;
        } catch (Exception e) {
            Utils.logError(e);
        }
        return false;
    }

    public static boolean isValidZoom(String zoom) {
        try {
            int intZoom = Integer.parseInt(zoom);
            return true;
        } catch (Exception e) {
            Utils.logError(e);
        }
        return false;
    }

    public static long convertHtmlInputTimestamp(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = format.parse(time);
            DateTime dateTime = new DateTime(date.getTime());
            return dateTime.toInstant().getMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            Utils.logError(e);
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
            Utils.logError(e);
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
            Utils.logError(e);
        }
        return -1;
    }

    public static void main(String[] args) {
//        System.out.println("Time : " + new Date(convertHtmlInputTimestamp("2020-04-17T00:59")));
        System.out.println("Timestamp : " + new Timestamp(System.currentTimeMillis()));
    }

    public static boolean isAuthEnabled() {
        boolean isAuthEnabled = false;
        try {
            String authEnabledStr = PropertiesLoader.getProperty("auth.enabled");
            if(!TextUtils.isEmpty(authEnabledStr)) {
                isAuthEnabled = Boolean.parseBoolean(authEnabledStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
        }
        return isAuthEnabled;
    }

    private static Pair<String, Integer> getRedisHostPortPair() {
        try {
            InputStream inputStream = new FileInputStream("config.ini");
            Properties prop = new Properties();
            prop.load(inputStream);

            String redisHost = PropertiesLoader.getProperty("redis.host");
            Integer redisPort = Integer.parseInt(PropertiesLoader.getProperty("redis.port"));

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

    public static String httpGetWithoutCertificateCheck(String iUrl, JSONObject jsonObject, Map<String, String> headers) throws Exception {
        StringBuffer newUrl = new StringBuffer(iUrl);
        newUrl.append("?");
        Iterator var4 = jsonObject.keySet().iterator();

        while(var4.hasNext()) {
            String key = (String)var4.next();
            newUrl.append(key);
            newUrl.append("=");
            newUrl.append(jsonObject.get(key).toString());
            newUrl.append("&");
        }

        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (x509CertChain, authType) -> true)
                .build();

        newUrl.deleteCharAt(newUrl.length() - 1);
        HttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext).build();
        HttpGet request = new HttpGet(newUrl.toString());
        if (headers != null && headers.size() > 0) {
            Iterator var6 = headers.entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var6.next();
                request.addHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

        request.addHeader("Content-Type", "application/json");
        HttpResponse response = httpClient.execute(request);
        if (response != null) {
            InputStream in = response.getEntity().getContent();
            String body = IOUtils.toString(in);
            logger.info("HTTP GET :: url : " + newUrl.toString() + " :: response : " + body);
            return body;
        } else {
            return null;
        }
    }

    public static void logError(Exception e) {
        if (e != null) {
            Logger logger = Log.getLogger("Exception");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            logger.error(stackTrace);
        }
    }

    public static String httpPost(String url, Object jsonObject, Map<String, String> headers) throws Exception {
        logger.info("url is : " + url);
        logger.info("json obj is : " + jsonObject.toString());
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            Iterator var5 = headers.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var5.next();
                request.addHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

        StringEntity params = new StringEntity(jsonObject.toString());
        request.addHeader("Content-Type", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        if (response != null) {
            InputStream in = response.getEntity().getContent();
            String body = IOUtils.toString(in);
            logger.info("HTTP POST :: url : " + url + " :: requestBody : " + jsonObject.toString() + " :: response : " + body);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("Bad Response from server");
            } else {
                return body;
            }
        } else {
            return null;
        }
    }

    public static String httpGet(String iUrl, JSONObject jsonObject, Map<String, String> headers) throws Exception {
        StringBuffer newUrl = new StringBuffer(iUrl);
        newUrl.append("?");
        Iterator var4 = jsonObject.keySet().iterator();

        while(var4.hasNext()) {
            String key = (String)var4.next();
            newUrl.append(key);
            newUrl.append("=");
            newUrl.append(jsonObject.get(key).toString());
            newUrl.append("&");
        }

        newUrl.deleteCharAt(newUrl.length() - 1);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(newUrl.toString());
        if (headers != null && headers.size() > 0) {
            Iterator var6 = headers.entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var6.next();
                request.addHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

        request.addHeader("Content-Type", "application/json");
        HttpResponse response = httpClient.execute(request);
        if (response != null) {
            InputStream in = response.getEntity().getContent();
            String body = IOUtils.toString(in);
            logger.info("HTTP GET :: url : " + newUrl.toString() + " :: response : " + body);
            return body;
        } else {
            return null;
        }
    }

}
