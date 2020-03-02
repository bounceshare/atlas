package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.MarkerPojo;
import com.bounce.atlas.pojo.PathPojo;
import com.bounce.atlas.pojo.PointPojo;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.enums.BookingStatus;
import com.bounce.utils.dbmodels.public_.tables.Bike;
import com.bounce.utils.dbmodels.public_.tables.Booking;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.List;
import java.util.Map;

public class BookingSearchApi extends BaseApiHandler {

    public BookingSearchApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        try {
            super.onRequest();
            String searchQuery = input.optString("searchQuery");

            BookingRecord booking = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Booking.BOOKING)
                    .where(Booking.BOOKING.ID.eq(Integer.parseInt(searchQuery))).fetchAny();

            logger.info("Booking : " + booking);

            Map<String, Object> markersPaths = getMarkersAndPathForBooking(booking);
            Map<Object, Object> response = Maps.newHashMap();
            response.put("markers", markersPaths.get("markers"));
            response.put("paths", markersPaths.get("paths"));

            sendSuccessResponse(asyncResponse, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getMarkersAndPathForBooking(BookingRecord booking) {
        Map<String, Object> markersAndPathMap = Maps.newHashMap();
        try {
            List<MarkerPojo> markers = Lists.newArrayList();
            List<PathPojo> paths = Lists.newArrayList();
            if(booking.getStatus() == BookingStatus.completed) {
                MarkerPojo startPoint = new MarkerPojo();
                startPoint.location = new PointPojo(booking.getActStartPointLat(), booking.getActEndPointLon());
                startPoint.title = "Actual Start Point";
                startPoint.iconUrl = "/resources/icons/marker_blue.png";
                if(booking.getTripStartTime() != null) {
                    startPoint.subtext = booking.getTripStartTime().toString();
                }

                MarkerPojo endPoint = new MarkerPojo();
                endPoint.location = new PointPojo(booking.getActEndPointLat(), booking.getActEndPointLon());
                endPoint.iconUrl = "/resources/icons/marker_blue.png";
                endPoint.title = "Actual End Point";
                if(booking.getTripEndTime() != null) {
                    endPoint.subtext = booking.getTripEndTime().toString();
                }

                PathPojo pathPojo = new PathPojo();
                pathPojo.points = Lists.newArrayList();
                pathPojo.points.add(startPoint.location);
                pathPojo.points.add(endPoint.location);
                pathPojo.data = Maps.newHashMap();
                pathPojo.data.put("Booking Id", booking.getId());
                pathPojo.data.put("Timeline", "<b><a href='#' onclick='showTimeline(\"/apis/booking/events\", " + booking.getId() +", \"Bike Event Timeline - " + booking.getId() + "\");'>Events</a></b>");

                pathPojo.data.put("Trip Created At", booking.getCreatedOn().toString());
                if(booking.getTripStartTime() != null) {
                    pathPojo.data.put("Trip Started At", booking.getTripStartTime().toString());
                }
                if(booking.getEstTime() != null) {
                    pathPojo.data.put("Est. Time", booking.getEstTime());
                }
                if(booking.getEstDistance() != null) {
                    pathPojo.data.put("Est. Distance", booking.getEstDistance());
                }
                if(booking.getEstCost() != null) {
                    pathPojo.data.put("Est. Cost", booking.getEstCost());
                }
                if(booking.getUserId() != null) {
                    pathPojo.data.put("User Id", booking.getUserId());
                }
                if(booking.getStatus() != null) {
                    pathPojo.data.put("Status", booking.getStatus());
                }

                markers.add(startPoint);
                markers.add(endPoint);
                paths.add(pathPojo);
            } else if(booking.getStatus() == BookingStatus.in_trip) {
                MarkerPojo startPoint = new MarkerPojo();
                startPoint.location = new PointPojo(booking.getStartPointLat(), booking.getStartPointLon());
                startPoint.title = "Start Point";
                startPoint.iconUrl = "/resources/icons/marker_blue.png";
                startPoint.subtext = booking.getTripStartTime().toString();

                BikeRecord bike = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Bike.BIKE).where(Bike.BIKE.ID.eq(booking.getBikeId())).fetchAny();

                MarkerPojo endPoint = new MarkerPojo();
                endPoint.location = new PointPojo(bike.getLat(), bike.getLon());
                endPoint.iconUrl = "/resources/icons/marker_green.png";
                endPoint.title = "Bike Location";
                if(bike.getLocUpdatedTime() != null) {
                    endPoint.subtext = bike.getLocUpdatedTime().toString();
                }

                PathPojo pathPojo = new PathPojo();
                pathPojo.points = Lists.newArrayList();
                pathPojo.points.add(startPoint.location);
                pathPojo.points.add(endPoint.location);
                pathPojo.data = Maps.newHashMap();
                pathPojo.data.put("Booking Id", booking.getId());
                pathPojo.data.put("Timeline", "<b><a href='#' onclick='showTimeline(\"/apis/booking/events\", " + booking.getId() +", \"Bike Event Timeline - " + booking.getId() + "\");'>Events</a></b>");

                pathPojo.data.put("Trip Created At", booking.getCreatedOn().toString());
                if(booking.getTripStartTime() != null) {
                    pathPojo.data.put("Trip Started At", booking.getTripStartTime().toString());
                }
                if(booking.getEstTime() != null) {
                    pathPojo.data.put("Est. Time", booking.getEstTime());
                }
                if(booking.getEstDistance() != null) {
                    pathPojo.data.put("Est. Distance", booking.getEstDistance());
                }
                if(booking.getEstCost() != null) {
                    pathPojo.data.put("Est. Cost", booking.getEstCost());
                }
                if(booking.getUserId() != null) {
                    pathPojo.data.put("User Id", booking.getUserId());
                }
                if(booking.getStatus() != null) {
                    pathPojo.data.put("Status", booking.getStatus());
                }

                markers.add(startPoint);
                markers.add(endPoint);
                paths.add(pathPojo);
            } else if(booking.getStatus() == BookingStatus.reserved || booking.getStatus() == BookingStatus.cancelled) {
                MarkerPojo startPoint = new MarkerPojo();
                startPoint.location = new PointPojo(booking.getStartPointLat(), booking.getStartPointLon());
                startPoint.title = "Start Point";
                startPoint.iconUrl = "/resources/icons/marker_blue.png";
                startPoint.subtext = booking.getCreatedOn().toString();


                MarkerPojo endPoint = new MarkerPojo();
                endPoint.location = new PointPojo(booking.getEndPointLat(), booking.getEndPointLon());
                endPoint.iconUrl = "/resources/icons/marker_green.png";
                endPoint.title = "Destination Selected";
                endPoint.subtext = booking.getStatus().getLiteral();

                PathPojo pathPojo = new PathPojo();
                pathPojo.points = Lists.newArrayList();
                pathPojo.points.add(startPoint.location);
                pathPojo.points.add(endPoint.location);
                pathPojo.data = Maps.newHashMap();
                pathPojo.data.put("Booking Id", booking.getId());
                pathPojo.data.put("Timeline", "<b><a href='#' onclick='showTimeline(\"/apis/booking/events\", " + booking.getId() +", \"Bike Event Timeline - " + booking.getId() + "\");'>Events</a></b>");

                pathPojo.data.put("Trip Created At", booking.getCreatedOn().toString());
                if(booking.getTripStartTime() != null) {
                    pathPojo.data.put("Trip Started At", booking.getTripStartTime().toString());
                }
                if(booking.getEstTime() != null) {
                    pathPojo.data.put("Est. Time", booking.getEstTime());
                }
                if(booking.getEstDistance() != null) {
                    pathPojo.data.put("Est. Distance", booking.getEstDistance());
                }
                if(booking.getEstCost() != null) {
                    pathPojo.data.put("Est. Cost", booking.getEstCost());
                }
                if(booking.getUserId() != null) {
                    pathPojo.data.put("User Id", booking.getUserId());
                }
                if(booking.getStatus() != null) {
                    pathPojo.data.put("Status", booking.getStatus());
                }

                markers.add(startPoint);
                markers.add(endPoint);
                paths.add(pathPojo);
            }
            markersAndPathMap.put("markers", markers);
            markersAndPathMap.put("paths", paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return markersAndPathMap;
    }
}
