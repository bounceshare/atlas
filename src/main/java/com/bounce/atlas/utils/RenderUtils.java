package com.bounce.atlas.utils;

import com.bounce.atlas.pojo.*;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.Log;
import com.bounce.utils.Pair;
import com.bounce.utils.dbmodels.public_.enums.BikeStatus;
import com.bounce.utils.dbmodels.public_.tables.records.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.*;

public class RenderUtils {

    protected static Logger logger = Log.getLogger(RenderUtils.class.getCanonicalName());

    public static List<CardPojo> getCard(BookingRecord booking) {
        CardPojo tripCreatedCard = new CardPojo();
        CardPojo tripStartCard = new CardPojo();
        CardPojo tripEndCard = new CardPojo();
        try {
            tripCreatedCard.header = "Booking : " + booking.getId() + " - " + "created";
            tripCreatedCard.time = booking.getCreatedOn().getTime();
            tripCreatedCard.timeString = new Date(booking.getCreatedOn().getTime()).toString();
            tripCreatedCard.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            tripCreatedCard.details = Maps.newHashMap();

            tripEndCard.time = booking.getCreatedOn().getTime();
            tripEndCard.timeString = new Date(booking.getCreatedOn().getTime()).toString();
            tripEndCard.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            tripEndCard.details = Maps.newHashMap();

            tripStartCard.header = "Booking : " + booking.getId() + " - " + "started";
            tripStartCard.time = booking.getCreatedOn().getTime();
            tripStartCard.timeString = new Date(booking.getCreatedOn().getTime()).toString();
            tripStartCard.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            tripStartCard.details = Maps.newHashMap();

            switch (booking.getStatus()) {
                case hold:
                    tripCreatedCard = null;
                    tripStartCard = null;
                    tripEndCard = null;
                    break;
                case reserved:
                    tripCreatedCard.color = Constants.Color.BOUNCE_RED;
                    tripCreatedCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripCreatedCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripCreatedCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripCreatedCard.details.put("User Id", booking.getUserId() + "");

                    tripEndCard = null;
                    tripStartCard = null;
                    break;
                case in_delivery:
                    tripCreatedCard = null;
                    tripStartCard = null;
                    tripEndCard = null;
                    break;
                case delivered:
                    tripCreatedCard = null;
                    tripStartCard = null;
                    tripEndCard = null;
                    break;
                case in_trip:
                    tripCreatedCard.color = Constants.Color.BOUNCE_RED;
                    tripCreatedCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripCreatedCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripCreatedCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripCreatedCard.details.put("User Id", booking.getUserId() + "");
                    tripCreatedCard.details.put("Trip Start Time", booking.getTripStartTime() + "");
                    tripCreatedCard.details.put("Actual Start Point", booking.getActStartPointLat() + "," + booking.getActStartPointLon());

                    tripStartCard.color = Constants.Color.BOUNCE_RED;
                    if(booking.getTripStartTime() != null) {
                        tripStartCard.time = booking.getTripStartTime().getTime();
                        tripStartCard.timeString = new Date(booking.getTripStartTime().getTime()).toString();
                    } else {
                        tripStartCard.time = booking.getCreatedOn().getTime() + (20 * 60 * 1000);
                        tripStartCard.timeString = new Date(booking.getCreatedOn().getTime() + (20 * 60 * 1000)).toString();
                    }
                    tripStartCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripStartCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripStartCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripStartCard.details.put("User Id", booking.getUserId() + "");
                    tripStartCard.details.put("Trip Start Time", booking.getTripStartTime() + "");
                    tripStartCard.details.put("Actual Start Point", booking.getActStartPointLat() + "," + booking.getActStartPointLon());

                    tripEndCard = null;
                    break;
                case completed:
                    tripCreatedCard.color = Constants.Color.BOUNCE_GREEN;
                    tripCreatedCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripCreatedCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripCreatedCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripCreatedCard.details.put("Actual Time", booking.getActualTime() + "");
                    tripCreatedCard.details.put("Actual Distance", booking.getActualDistance() + "");
                    tripCreatedCard.details.put("Actual Cost", booking.getActualCost() + "");
                    tripCreatedCard.details.put("User Id", booking.getUserId() + "");

                    tripEndCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripEndCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripEndCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripEndCard.details.put("Actual Time", booking.getActualTime() + "");
                    tripEndCard.details.put("Actual Distance", booking.getActualDistance() + "");
                    tripEndCard.details.put("Actual Cost", booking.getActualCost() + "");
                    tripEndCard.details.put("User Id", booking.getUserId() + "");
                    if(booking.getTripEndTime() != null) {
                        tripEndCard.time = booking.getTripEndTime().getTime();
                        tripEndCard.timeString = new Date(booking.getTripEndTime().getTime()).toString();
                    } else {
                        tripEndCard.time = booking.getUpdatedOn().getTime();
                        tripEndCard.timeString = new Date(booking.getUpdatedOn().getTime()).toString();
                    }
                    tripEndCard.header = "Booking : " + booking.getId() + " - " + booking.getStatus();

                    tripStartCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripStartCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripStartCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripStartCard.details.put("Actual Time", booking.getActualTime() + "");
                    tripStartCard.details.put("Actual Distance", booking.getActualDistance() + "");
                    tripStartCard.details.put("Actual Cost", booking.getActualCost() + "");
                    tripStartCard.details.put("User Id", booking.getUserId() + "");
                    if(booking.getTripStartTime() != null) {
                        tripStartCard.time = booking.getTripStartTime().getTime();
                        tripStartCard.timeString = new Date(booking.getTripStartTime().getTime()).toString();
                    } else {
                        tripStartCard.time = booking.getCreatedOn().getTime() + (20 * 60 * 1000);
                        tripStartCard.timeString = new Date(booking.getCreatedOn().getTime() + (20 * 60 * 1000)).toString();
                    }
                    break;
                case cancelled:
                    tripCreatedCard.color = Constants.Color.WARNING;
                    tripCreatedCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripCreatedCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripCreatedCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripCreatedCard.details.put("User Id", booking.getUserId() + "");
                    tripCreatedCard.body = "Cancelled because of " + booking.getCancellationReason();

                    tripEndCard.color = Constants.Color.WARNING;
                    tripEndCard.details.put("Est. Time", booking.getEstTime() + "");
                    tripEndCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    tripEndCard.details.put("Est. Cost", booking.getEstCost() + "");
                    tripEndCard.details.put("User Id", booking.getUserId() + "");
                    tripEndCard.body = "Cancelled because of " + booking.getCancellationReason();
                    tripEndCard.time = booking.getUpdatedOn().getTime();
                    tripEndCard.timeString = new Date(booking.getUpdatedOn().getTime()).toString();
                    tripEndCard.header = "Booking : " + booking.getId() + " - " + booking.getStatus();

                    tripStartCard = null;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        List<CardPojo> cards = Lists.newArrayList();
        cards.add(tripCreatedCard);
        if(tripEndCard != null) {
            cards.add(tripEndCard);
        }
        if(tripStartCard != null) {
            cards.add(tripStartCard);
        }
        return cards;
    }

    public static CardPojo getCard(EndTripFeedbackRecord endTripFeedback) {
        CardPojo card = new CardPojo();
        try {
            card.header = "EndTripFeedback - " + endTripFeedback.getBookingId();
            card.time = endTripFeedback.getCreatedOn().getTime();
            card.timeString = new Date(endTripFeedback.getCreatedOn().getTime()).toString();
            card.body = endTripFeedback.getPrimary();
            card.details = Maps.newHashMap();
            card.color = Constants.Color.INFO;

            card.details.put("Image", endTripFeedback.getImageLink());
            if(endTripFeedback.getData() != null) {
                card.details.put("Data", endTripFeedback.getData().toString());
            }
            card.details.put("Source", endTripFeedback.getSource());
            card.details.put("Secondary", endTripFeedback.getSecondary());
            if(endTripFeedback.getRating() != null) {
                card.details.put("Rating", endTripFeedback.getRating().toString());
            }
            card.details.put("Comments", endTripFeedback.getComments());
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return card;
    }

    public static CardPojo getCard(BikeStatusLogRecord bikeStatusLog) {
        CardPojo card = new CardPojo();
        try {
            card.header = "Bike State Change";
            card.time = bikeStatusLog.getCreatedOn().getTime();
            card.timeString = new Date(bikeStatusLog.getCreatedOn().getTime()).toString();
            card.body = bikeStatusLog.getPreviousStatus() + " to " + bikeStatusLog.getCurrentStatus() + " - " + bikeStatusLog.getReason();
            card.details = Maps.newHashMap();
            card.color = Constants.Color.INFO;

        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return card;
    }

    public static List<CardPojo> getCard(JSONObject task) {
        CardPojo creationCard = new CardPojo();
        CardPojo updationCard = new CardPojo();
        List<CardPojo> cards = Lists.newArrayList();
        try {
            creationCard.header = "Task Created";
            creationCard.body = task.optString("taskType") + " for " + task.optString("assetType");
            creationCard.color = Constants.Color.INFO;
            creationCard.timeString = new Date(Utils.convertHawkeyeTimstamp(task.optString("createdOn"))).toString();
            creationCard.time = Utils.convertHawkeyeTimstamp(task.optString("createdOn"));
            creationCard.details = Maps.newHashMap();
            creationCard.details.put("Task Lat", task.optDouble("taskLat") + "");
            creationCard.details.put("Task Lon", task.optDouble("taskLon") + "");
            creationCard.details.put("Team Id", task.optDouble("teamId") + "");
            creationCard.details.put("Task Id", task.optDouble("taskId") + "");
            creationCard.details.put("Comments", task.optDouble("comment") + "");

            updationCard.header = "Task " + task.optString("taskStatus");
            updationCard.body = task.optString("taskType") + " for " + task.optString("assetType");
            updationCard.color = Constants.Color.INFO;
            updationCard.timeString = new Date(Utils.convertHawkeyeTimstamp(task.optString("updatedOn"))).toString();
            updationCard.time = Utils.convertHawkeyeTimstamp(task.optString("updatedOn"));
            updationCard.details = Maps.newHashMap();
            updationCard.details.put("Task Lat", task.optDouble("taskLat") + "");
            updationCard.details.put("Task Lon", task.optDouble("taskLon") + "");
            updationCard.details.put("Team Id", task.optDouble("teamId") + "");
            updationCard.details.put("Task Id", task.optDouble("taskId") + "");
            updationCard.details.put("Comments", task.optDouble("comment") + "");

            cards.add(creationCard);
            cards.add(updationCard);

        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return cards;
    }

    public static CardPojo getCard(DeviceLatestDataRecord deviceLatestData) {
        CardPojo card = new CardPojo();

        try {
            card.header = "Latest Sensor Information";
            card.color = Constants.Color.INFO;
            card.time = System.currentTimeMillis();
            card.timeString = new Date().toString();
            card.details = Maps.newHashMap();
            Map<String, Object> deviceMap = deviceLatestData.intoMap();
            for(Map.Entry<String, Object> entry : deviceMap.entrySet()) {
                card.details.put(entry.getKey(), entry.getValue() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }

        return card;
    }

    public static class CardComparator implements Comparator<CardPojo> {

        @Override
        public int compare(CardPojo o1, CardPojo o2) {
            long time1 = o1.time;
            long time2 = o2.time;

            if (time1 == time2)
                return 0;
            else if (time1 > time2)
                return 1;
            else
                return -1;
        }
    }

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

    public static FencePojo createDefaultFence(List<PointPojo> points, Map<String, Object> data) {
        FencePojo fencePojo = new FencePojo();
        fencePojo.points = points;
        fencePojo.data = data;
        return fencePojo;
    }

    public static List<FencePojo> getParkingFences(double lat, double lon, int radius) {
        List<FencePojo> fences = Lists.newArrayList();
        List<Map<String, Object>> parkingList = QueryUtils.getParkingAround(lat, lon, radius);
        logger.info("Parking : " + parkingList);
        for(Map<String, Object> parkingMap : parkingList) {
            Map<String, Object> data = Maps.newHashMap();
            data.put("Name", parkingMap.get("name"));
            data.put("Category", parkingMap.get("category"));
            data.put("Bad Parking", parkingMap.get("negative"));
            FencePojo fence = createDefaultFence((List<PointPojo>)parkingMap.get("polygon"), data);
            if(! (boolean)parkingMap.get("negative")) {
                fence.fillColor = "#a2cff5";
                fence.color = "blue";
            }
            fences.add(fence);
        }
        return fences;
    }

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

                marker.data = Maps.newLinkedHashMap();
                marker.data.put("Location", (bikeData.get("lat")+ "," + bikeData.get("lon")));
                marker.data.put("Location Updated At", bikeData.get("loc_updated_time"));
                marker.data.put("Type", bikeData.get("type"));
                marker.data.put("Active", bikeData.get("active"));
                marker.data.put("Is Live", bikeData.get("is_live"));
                marker.data.put("Geo Id", bikeData.get("geo_id"));
                marker.data.put("Axcess Id", bikeData.get("axcess_id"));
                marker.data.put("Secondary GPS", (bikeData.get("sec_gps_lat")+ "," + bikeData.get("sec_gps_lon")));
                marker.data.put("Secondary GPS updated time", bikeData.get("sec_gps_updated_time"));
                marker.data.put("Timeline", "<b><a href='#' onclick='showTimeline(\"/apis/bike/events\", " + bike.getId() +", \"Bike Event Timeline - " + bike.getId() + "\", true);'>Events</a></b>");
                marker.data.put("Asset Data", "<b><a href='#' onclick='fetchForm(\"/apis/bike/fetchAssets\", " + bike.getId() +");'>Click Here</a></b>");
                marker.data.put("Sensor Information", "<b><a href='#' onclick='showTimeline(\"/apis/bike/deviceData\", " + bike.getId() +", \"Bike Sensor Values - " + bike.getId() + "\");'>Here</a></b>");
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

    public static MarkerPojo getMarkerPojo(Map<String, Object> tracking) {
        try {
            if(!tracking.containsKey("lat") || !tracking.containsKey("lng")) {
                return null;
            }
            MarkerPojo markerPojo = new MarkerPojo();
            markerPojo.location = new PointPojo((Double) tracking.get("lat"), (Double) tracking.get("lng"));
            markerPojo.iconUrl = "/resources/icons/dot.png";
            markerPojo.title = "Tracking Data";
            markerPojo.subtext = tracking.get("device_id").toString();
            markerPojo.data = Maps.newHashMap();
            markerPojo.data = tracking;
            return markerPojo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PathPojo createDefaultPath(List<PointPojo> points, Map<String, Object> data) {
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
