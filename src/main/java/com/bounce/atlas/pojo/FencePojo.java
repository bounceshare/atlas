package com.bounce.atlas.pojo;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class FencePojo {

    public List<PointPojo> points;
    public String color = "red";
    public String fillColor = "#f03";
    public double fillOpacity = 0.5;
    public Map<String, Object> data;

    public static FencePojo createDefaultFence(List<PointPojo> points, Map<String, Object> data) {
        FencePojo fencePojo = new FencePojo();
        fencePojo.points = points;
        fencePojo.data = data;
        return fencePojo;
    }

}
