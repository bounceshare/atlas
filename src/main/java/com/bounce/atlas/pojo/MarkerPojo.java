package com.bounce.atlas.pojo;

import com.bounce.utils.BounceUtils;
import com.bounce.utils.Pair;
import com.bounce.utils.dbmodels.public_.enums.BikeStatus;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MarkerPojo {

    public PointPojo location;
    public String iconUrl;
    public String title;
    public String subtext;
    public String cta;
    public Map<String, Object> data;

    public static List<MarkerPojo> getBikesAsMarkers(List<BikeRecord> bikes) {
        List<MarkerPojo> markers = new ArrayList<>();
        try {
            for (BikeRecord bike : bikes) {
                MarkerPojo marker = new MarkerPojo();
                switch (bike.getStatus()) {
                    case idle:
                        marker.iconUrl = "/resources/icons/marker_green.png";
                        break;
                    case busy:
                        marker.iconUrl = "/resources/icons/marker_blue.png";
                        break;
                    case oos:
                        marker.iconUrl = "/resources/icons/marker_red.png";
                        break;
                    default:
                        marker.iconUrl = "/resources/icons/marker_red.png";
                        break;
                }
                marker.cta = "/bikes/" + bike.getId();
                marker.title = "" + bike.getId();
                marker.subtext = bike.getLicensePlate();
                marker.location = new PointPojo(bike.getLat(), bike.getLon());

                Map<String, Object> bikeData = bike.intoMap();

                marker.data = Maps.newHashMap();
                marker.data.put("Location", (bikeData.get("lat")+ "," + bikeData.get("lon")));
                marker.data.put("Location Updated At", bikeData.get("loc_updated_time"));
                marker.data.put("Type", bikeData.get("type"));
                marker.data.put("Active", bikeData.get("active"));
                marker.data.put("Is Live", bikeData.get("is_live"));
                marker.data.put("Geo Id", bikeData.get("geo_id"));
                marker.data.put("Axcess Id", bikeData.get("axcess_id"));
                marker.data.put("Secondary GPS", (bikeData.get("sec_gps_lat")+ "," + bikeData.get("sec_gps_lon")));
                marker.data.put("Secondary GPS updated time", bikeData.get("sec_gps_updated_time"));
                marker.data.put("Timeline", "<b><a href='#' onclick='showTimeline(\"/apis/bike/events\", " + bike.getId() +", \"Bike Event Timeline - " + bike.getId() + "\");'>Events</a></b>");
                if(bike.getStatus() == BikeStatus.oos) {
                    marker.data.put("OOS Reason", bikeData.get("oos_reason"));
                }

                markers.add(marker);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return markers;
    }

    public static List<MarkerPojo> getBookingMarkers(List<com.bounce.utils.Pair<BikeRecord, BookingRecord>> bookings) {
        List<MarkerPojo> markers = new ArrayList<>();
        try {
            for(Pair<BikeRecord, BookingRecord> pair : bookings) {
                try {
                    MarkerPojo marker = new MarkerPojo();
                    BikeRecord bike = pair.getKey();
                    BookingRecord booking = pair.getValue();
                    marker.iconUrl = "/resources/icons/marker_blue.png";
                    marker.title = "" + booking.getId();
                    marker.subtext = "BikeId : " + bike.getId();
                    marker.location = new PointPojo(bike.getLat(), bike.getLon());


                    marker.data = Maps.newHashMap();
                    marker.data.put("Trip Created At", booking.getCreatedOn().toString());
                    if(booking.getTripStartTime() != null) {
                        marker.data.put("Trip Started At", booking.getTripStartTime().toString());
                    }
                    if(booking.getEstTime() != null) {
                        marker.data.put("Est. Time", booking.getEstTime());
                    }
                    if(booking.getEstDistance() != null) {
                        marker.data.put("Est. Distance", booking.getEstDistance());
                    }
                    if(booking.getEstCost() != null) {
                        marker.data.put("Est. Cost", booking.getEstCost());
                    }
                    if(booking.getUserId() != null) {
                        marker.data.put("User Id", booking.getUserId());
                    }
                    if(booking.getStatus() != null) {
                        marker.data.put("Status", booking.getStatus());
                    }

                    markers.add(marker);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return markers;
    }

}
