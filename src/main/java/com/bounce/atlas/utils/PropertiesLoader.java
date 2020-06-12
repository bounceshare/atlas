package com.bounce.atlas.utils;

import com.google.common.collect.Maps;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    private static Map<String, String> map;

    public static String getProperty(String key) {
        if(map == null || map.size() < 1) {
            try {
                String env = System.getProperty("env", "prod");
                String configPath = "config.ini";
                if(env.equals("local")) {
                    configPath = "config-local.ini";
                }
                InputStream inputStream = new FileInputStream(configPath);
                Properties prop = new Properties();
                prop.load(inputStream);
                map = Maps.newLinkedHashMap();
                for(String propKey : prop.stringPropertyNames()) {
                    map.put(propKey, prop.getProperty(propKey));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Utils.logError(e);
            }
        }
        return map.get(key);
    }

}
