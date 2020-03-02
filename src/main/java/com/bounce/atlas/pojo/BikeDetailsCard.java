package com.bounce.atlas.pojo;

import com.bounce.atlas.utils.Constants;
import com.bounce.atlas.utils.Utils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.dbmodels.public_.tables.records.BikeStatusLogRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.DeviceLatestDataRecord;
import com.bounce.utils.dbmodels.public_.tables.records.EndTripFeedbackRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BikeDetailsCard {

    public String header;
    public long time;
    public String timeString;
    public String body;
    public String color = Constants.Color.BOUNCE_RED;
    public Map<String, String> details;

    public static List<BikeDetailsCard> getCard(BookingRecord booking) {
        BikeDetailsCard tripCreatedCard = new BikeDetailsCard();
        BikeDetailsCard tripStartCard = new BikeDetailsCard();
        BikeDetailsCard tripEndCard = new BikeDetailsCard();
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
        List<BikeDetailsCard> cards = Lists.newArrayList();
        cards.add(tripCreatedCard);
        if(tripEndCard != null) {
            cards.add(tripEndCard);
        }
        if(tripStartCard != null) {
            cards.add(tripStartCard);
        }
        return cards;
    }

    public static BikeDetailsCard getCard(EndTripFeedbackRecord endTripFeedback) {
        BikeDetailsCard card = new BikeDetailsCard();
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

    public static BikeDetailsCard getCard(BikeStatusLogRecord bikeStatusLog) {
        BikeDetailsCard card = new BikeDetailsCard();
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

    public static List<BikeDetailsCard> getCard(JSONObject task) {
        BikeDetailsCard creationCard = new BikeDetailsCard();
        BikeDetailsCard updationCard = new BikeDetailsCard();
        List<BikeDetailsCard> cards = Lists.newArrayList();
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

    public static BikeDetailsCard getCard(DeviceLatestDataRecord deviceLatestData) {
        BikeDetailsCard card = new BikeDetailsCard();

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

    public static class CardComparator implements Comparator<BikeDetailsCard> {

        @Override
        public int compare(BikeDetailsCard o1, BikeDetailsCard o2) {
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

}
