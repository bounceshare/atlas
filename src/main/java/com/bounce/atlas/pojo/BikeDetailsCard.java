package com.bounce.atlas.pojo;

import com.bounce.atlas.utils.Constants;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.dbmodels.public_.tables.records.BikeStatusLogRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.EndTripFeedbackRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Comparator;
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
            tripCreatedCard.timeString = booking.getCreatedOn().toString();
            tripCreatedCard.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            tripCreatedCard.details = Maps.newHashMap();

            tripEndCard.time = booking.getCreatedOn().getTime();
            tripEndCard.timeString = booking.getCreatedOn().toString();
            tripEndCard.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            tripEndCard.details = Maps.newHashMap();

            tripStartCard.header = "Booking : " + booking.getId() + " - " + "started";
            tripStartCard.time = booking.getCreatedOn().getTime();
            tripStartCard.timeString = booking.getCreatedOn().toString();
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
                        tripStartCard.timeString = booking.getTripStartTime().toString();
                    } else {
                        tripStartCard.time = booking.getCreatedOn().getTime() + (20 * 60 * 1000);
                        tripStartCard.timeString = booking.getCreatedOn().toString() + (20 * 60 * 1000);
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
                        tripEndCard.timeString = booking.getTripEndTime().toString();
                    } else {
                        tripEndCard.time = booking.getUpdatedOn().getTime();
                        tripEndCard.timeString = booking.getUpdatedOn().toString();
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
                        tripStartCard.timeString = booking.getTripStartTime().toString();
                    } else {
                        tripStartCard.time = booking.getCreatedOn().getTime() + (20 * 60 * 1000);
                        tripStartCard.timeString = booking.getCreatedOn().toString() + (20 * 60 * 1000);
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
                    tripEndCard.timeString = booking.getUpdatedOn().toString();
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
            card.timeString = endTripFeedback.getCreatedOn().toString();
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
            card.timeString = bikeStatusLog.getCreatedOn().toString();
            card.body = bikeStatusLog.getPreviousStatus() + " to " + bikeStatusLog.getCurrentStatus() + " - " + bikeStatusLog.getReason();
            card.details = Maps.newHashMap();
            card.color = Constants.Color.INFO;

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
