package com.bounce.atlas.pojo;

import com.bounce.utils.BounceUtils;
import com.google.common.collect.Lists;

import java.util.List;

public class PointPojo {

    public Double lat;
    public Double lon;

    public PointPojo(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static List<PointPojo> getPointsFromSqlLineString(String lineString) {
        //LINESTRING(77.5942394 12.9347669,77.5939417 12.9337579,77.5946445 12.9335827,77.5949368 12.9345264,77.5942394 12.9347669,77.5942394 12.9347669)
        List<PointPojo> pointPojos = Lists.newArrayList();
        lineString = lineString.replace("LINESTRING", "");
        lineString = lineString.replace("(", "");
        lineString = lineString.replace(")", "");

        String[] splits = lineString.split(",");
        for(String split : splits) {
            try {
                PointPojo pointPojo = new PointPojo(Double.parseDouble(split.split(" ")[1]), Double.parseDouble(split.split(" ")[0]));
                pointPojos.add(pointPojo);
            } catch (Exception e) {
                BounceUtils.logError(e);
                e.printStackTrace();
            }
        }
        return pointPojos;
    }
}
