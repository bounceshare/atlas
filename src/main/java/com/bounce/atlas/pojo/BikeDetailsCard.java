package com.bounce.atlas.pojo;

import com.bounce.atlas.utils.Constants;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.EndTripFeedbackRecord;
import com.google.common.collect.Maps;

import java.util.Comparator;
import java.util.Map;

public class BikeDetailsCard {

    public String header;
    public long time;
    public String timeString;
    public String body;
    public String color = Constants.Color.BOUNCE_RED;
    public Map<String, String> details;

    public static BikeDetailsCard getCard(BookingRecord booking) {
        BikeDetailsCard card = new BikeDetailsCard();
        try {
            card.header = "Booking : " + booking.getId() + " - " + booking.getStatus();
            card.time = booking.getCreatedOn().getTime();
            card.timeString = booking.getCreatedOn().toString();
            card.body = "From " + booking.getStartAddress() + " to " + booking.getEndAddress();
            card.details = Maps.newHashMap();

            switch (booking.getStatus()) {
                case hold:
                    break;
                case reserved:
                    card.color = Constants.Color.BOUNCE_RED;
                    card.details.put("Est. Time", booking.getEstTime() + "");
                    card.details.put("Est. Distance", booking.getEstDistance() + "");
                    card.details.put("Est. Cost", booking.getEstCost() + "");
                    card.details.put("User Id", booking.getUserId() + "");
                    break;
                case in_delivery:
                    break;
                case delivered:
                    break;
                case in_trip:
                    card.color = Constants.Color.BOUNCE_RED;
                    card.details.put("Est. Time", booking.getEstTime() + "");
                    card.details.put("Est. Distance", booking.getEstDistance() + "");
                    card.details.put("Est. Cost", booking.getEstCost() + "");
                    card.details.put("User Id", booking.getUserId() + "");
                    card.details.put("Trip Start Time", booking.getTripStartTime() + "");
                    card.details.put("Actual Start Point", booking.getActStartPointLat() + "," + booking.getActStartPointLon());
                    break;
                case completed:
                    card.color = Constants.Color.BOUNCE_GREEN;
                    card.details.put("Est. Time", booking.getEstTime() + "");
                    card.details.put("Est. Distance", booking.getEstDistance() + "");
                    card.details.put("Est. Cost", booking.getEstCost() + "");
                    card.details.put("Actual Time", booking.getActualTime() + "");
                    card.details.put("Actual Distance", booking.getActualDistance() + "");
                    card.details.put("Actual Cost", booking.getActualCost() + "");
                    card.details.put("User Id", booking.getUserId() + "");
                    break;
                case cancelled:
                    card.color = Constants.Color.WARNING;
                    card.details.put("Est. Time", booking.getEstTime() + "");
                    card.details.put("Est. Distance", booking.getEstDistance() + "");
                    card.details.put("Est. Cost", booking.getEstCost() + "");
                    card.details.put("User Id", booking.getUserId() + "");
                    card.body = "Cancelled because of " + booking.getCancellationReason();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return card;
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
