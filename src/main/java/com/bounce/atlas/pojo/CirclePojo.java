package com.bounce.atlas.pojo;

import com.bounce.utils.BounceUtils;
import com.bounce.utils.dbmodels.public_.tables.records.HubRecord;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
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

    public static List<CirclePojo> getHubsAsCircles(List<HubRecord> hubs) {
        List<CirclePojo> circles = new ArrayList<>();
        try {
            for (HubRecord hub : hubs) {
                CirclePojo circle = new CirclePojo();
                circle.location = new PointPojo(hub.getLat(), hub.getLon());
                circle.color = "blue";
                circle.fillColor = "#a2cff5";

                circle.data = Maps.newHashMap();
                circle.data.put("Location", (hub.getLat()+ "," + hub.getLon()));
                circle.data.put("Created On", hub.getCreatedOn().toString());
                circle.data.put("Name", hub.getName());
                circle.data.put("Address", hub.getAddress());
                circle.data.put("Active", hub.getActive());
                circle.data.put("Capacity", hub.getCapacity());
                if(!TextUtils.isEmpty(hub.getImageUrl())) {
                    circle.data.put("Image",
                            "<br/><img src='" + hub.getImageUrl() + "' alt='Hub Image' width='200' height='200'>");
                }
                circle.data.put("Contact Number", hub.getContactNumber());
                circle.data.put("Is Reliable", hub.getIsReliable());
                circle.data.put("Is Online", hub.getIsOnline());
                circle.data.put("Rating", hub.getRating());
                circle.data.put("Rating Booking Count", hub.getRatedBookingCount());

                circles.add(circle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return circles;
    }

}
