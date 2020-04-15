package com.bounce.atlas.pojo;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class FormPojo {

    public Map<String, Object> formSchema;
    public Map<String, Object> values;
    public String postUrl;
    public boolean isSidebar = false;

    public static Map<String, Object> fromJson(String str) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return new GsonBuilder().disableHtmlEscaping().create().fromJson(str, type);
    }
}
