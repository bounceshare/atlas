package com.bounce.atlas.pojo;

import java.util.List;
import java.util.Map;

public class CirclePojo {

    public PointPojo location;
    public String color = "red";
    public String fillColor = "#f03";
    public double radius = 20;
    public double fillOpacity = 0.5;
    public Map<String, Object> data;

    public static CirclePojo createDefaultCircle(PointPojo location, Map<String, Object> data) {
        CirclePojo circlePojo = new CirclePojo();
        circlePojo.location = location;
        circlePojo.data = data;
        return circlePojo;
    }

}
