package com.bounce.atlas.pojo;

import com.bounce.atlas.utils.QueryUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

    public static List<FencePojo> getParkingFences(double lat, double lon, int radius) {
        List<FencePojo> fences = Lists.newArrayList();
        List<Map<String, Object>> parkingList = QueryUtils.getParkingAround(lat, lon, radius);
        for(Map<String, Object> parkingMap : parkingList) {
            Map<String, Object> data = Maps.newHashMap();
            data.put("Name", parkingMap.get("name"));
            data.put("Category", parkingMap.get("category"));
            data.put("Bad Parking", parkingMap.get("negative"));
            FencePojo fence = FencePojo.createDefaultFence((List<PointPojo>)parkingMap.get("polygon"), data);
            if(! (boolean)parkingMap.get("negative")) {
                fence.fillColor = "#a2cff5";
                fence.color = "blue";
            }
            fences.add(fence);
        }
        return fences;
    }

}
