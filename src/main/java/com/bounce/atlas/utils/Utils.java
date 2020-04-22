package com.bounce.atlas.utils;

import com.amazonaws.util.IOUtils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.Log;
import com.bounce.utils.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
            BounceUtils.logError(e);
        }
        return -1;
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
        System.out.println("Time : " + new Date(convertSQlTimestamp("2020-03-04T22:00:00")));
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

}
