package com.bounce.atlas.utils;

import com.nimbusds.jose.JWSObject;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

public class AuthUtils {

    public static boolean isAuth(String token) {
        try {
            if(!TextUtils.isEmpty(token)) {
                JWSObject jwsObject = JWSObject.parse(token);
                JSONObject jsonObject = new JSONObject(jwsObject.getPayload().toJSONObject().toJSONString());
                String domain = jsonObject.optString("hd");
                String email_verified = jsonObject.optString("email_verified");
                if (domain.equals("bounceshare.com") && email_verified.equals("true")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
