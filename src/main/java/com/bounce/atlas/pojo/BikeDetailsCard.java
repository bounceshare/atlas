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
        BikeDetailsCard startCard = new BikeDetailsCard();
        BikeDetailsCard endCard = new BikeDetailsCard();
        try {
            startCard.header = "Booking : " + booking.getId() + " - " + "started";
            startCard.time = booking.getCreatedOn().getTime();
            startCard.timeString = booking.getCreatedOn().toString();
            startCard.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            startCard.details = Maps.newHashMap();

            endCard.time = booking.getCreatedOn().getTime();
            endCard.timeString = booking.getCreatedOn().toString();
            endCard.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            endCard.details = Maps.newHashMap();

            switch (booking.getStatus()) {
                case hold:
                    break;
                case reserved:
                    startCard.color = Constants.Color.BOUNCE_RED;
                    startCard.details.put("Est. Time", booking.getEstTime() + "");
                    startCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    startCard.details.put("Est. Cost", booking.getEstCost() + "");
                    startCard.details.put("User Id", booking.getUserId() + "");

                    endCard = null;
                    break;
                case in_delivery:
                    break;
                case delivered:
                    break;
                case in_trip:
                    startCard.color = Constants.Color.BOUNCE_RED;
                    startCard.details.put("Est. Time", booking.getEstTime() + "");
                    startCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    startCard.details.put("Est. Cost", booking.getEstCost() + "");
                    startCard.details.put("User Id", booking.getUserId() + "");
                    startCard.details.put("Trip Start Time", booking.getTripStartTime() + "");
                    startCard.details.put("Actual Start Point", booking.getActStartPointLat() + "," + booking.getActStartPointLon());

                    endCard = null;
                    break;
                case completed:
                    startCard.color = Constants.Color.BOUNCE_GREEN;
                    startCard.details.put("Est. Time", booking.getEstTime() + "");
                    startCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    startCard.details.put("Est. Cost", booking.getEstCost() + "");
                    startCard.details.put("Actual Time", booking.getActualTime() + "");
                    startCard.details.put("Actual Distance", booking.getActualDistance() + "");
                    startCard.details.put("Actual Cost", booking.getActualCost() + "");
                    startCard.details.put("User Id", booking.getUserId() + "");

                    endCard.details.put("Est. Time", booking.getEstTime() + "");
                    endCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    endCard.details.put("Est. Cost", booking.getEstCost() + "");
                    endCard.details.put("Actual Time", booking.getActualTime() + "");
                    endCard.details.put("Actual Distance", booking.getActualDistance() + "");
                    endCard.details.put("Actual Cost", booking.getActualCost() + "");
                    endCard.details.put("User Id", booking.getUserId() + "");
                    endCard.time = booking.getTripEndTime().getTime();
                    endCard.timeString = booking.getTripEndTime().toString();
                    endCard.header = "Booking : " + booking.getId() + " - " + booking.getStatus();
                    break;
                case cancelled:
                    startCard.color = Constants.Color.WARNING;
                    startCard.details.put("Est. Time", booking.getEstTime() + "");
                    startCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    startCard.details.put("Est. Cost", booking.getEstCost() + "");
                    startCard.details.put("User Id", booking.getUserId() + "");
                    startCard.body = "Cancelled because of " + booking.getCancellationReason();

                    endCard.color = Constants.Color.WARNING;
                    endCard.details.put("Est. Time", booking.getEstTime() + "");
                    endCard.details.put("Est. Distance", booking.getEstDistance() + "");
                    endCard.details.put("Est. Cost", booking.getEstCost() + "");
                    endCard.details.put("User Id", booking.getUserId() + "");
                    endCard.body = "Cancelled because of " + booking.getCancellationReason();
                    endCard.time = booking.getUpdatedOn().getTime();
                    endCard.timeString = booking.getUpdatedOn().toString();
                    endCard.header = "Booking : " + booking.getId() + " - " + booking.getStatus();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        List<BikeDetailsCard> cards = Lists.newArrayList();
        cards.add(startCard);
        if(endCard != null) {
            cards.add(endCard);
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
