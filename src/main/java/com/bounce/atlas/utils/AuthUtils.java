package com.bounce.atlas.utils;

import com.nimbusds.jose.JWSObject;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthUtils {

    public static boolean isAuth(String token) {
        try {
            if(!TextUtils.isEmpty(token)) {
                JWSObject jwsObject = JWSObject.parse(token);
                JSONObject jsonObject = new JSONObject(jwsObject.getPayload().toJSONObject().toJSONString());
                String domain = jsonObject.optString("hd");
                String email_verified = jsonObject.optString("email_verified");
                if (!TextUtils.isEmpty(getDomain()) && domain.equals(getDomain()) && email_verified.equals("true")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUserId(String token) {
        try {
            if(!TextUtils.isEmpty(token)) {
                JWSObject jwsObject = JWSObject.parse(token);
                JSONObject jsonObject = new JSONObject(jwsObject.getPayload().toJSONObject().toJSONString());
                String domain = jsonObject.optString("hd");
                String email_verified = jsonObject.optString("email_verified");
                String email = jsonObject.optString("email");
                return email;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDomain() {
        return PropertiesLoader.getProperty("googleauth.domain");
    }

}
