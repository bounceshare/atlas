package com.bounce.atlas.pojo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class PathPojo {

    public List<PointPojo> points;
    public String color = "red";
    public int lineWeight = 6;
    public Map<String, Object> data;

    public static PathPojo createDefaultFence(List<PointPojo> points, Map<String, Object> data) {
        PathPojo fencePojo = new PathPojo();
        fencePojo.points = points;
        fencePojo.data = data;
        return fencePojo;
    }

    public static PathPojo getMarkerPojo(List<Map<String, Object>> trackings) {
        PathPojo pathPojo = new PathPojo();
        try {
            List<PointPojo> pointPojos = Lists.newArrayList();
            for(Map<String, Object> tracking : trackings) {
                if(!tracking.containsKey("lat") || !tracking.containsKey("lng")) {
                    continue;
                }
                pointPojos.add(new PointPojo((double) tracking.get("lat"), (double) tracking.get("lng")));
            }
            pathPojo.points = pointPojos;
            return pathPojo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathPojo;
    }

}
