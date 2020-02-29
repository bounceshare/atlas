package com.bounce.atlas.pojo;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class PathPojo {

    public List<PointPojo> points;
    public String color = "red";
    public Map<String, Object> data;

    public static PathPojo createDefaultFence(List<PointPojo> points, Map<String, Object> data) {
        PathPojo fencePojo = new PathPojo();
        fencePojo.points = points;
        fencePojo.data = data;
        return fencePojo;
    }

}
