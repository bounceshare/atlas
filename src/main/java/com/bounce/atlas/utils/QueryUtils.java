package com.bounce.atlas.utils;

import com.bounce.atlas.pojo.*;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.Pair;
import com.bounce.utils.dbmodels.public_.enums.BikeStatus;
import com.bounce.utils.dbmodels.public_.enums.BookingStatus;
import com.bounce.utils.dbmodels.public_.tables.Bike;
import com.bounce.utils.dbmodels.public_.tables.Booking;
import com.bounce.utils.dbmodels.public_.tables.Hub;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.HubRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;
import org.jooq.Record;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryUtils {

    public static List<BikeRecord> getBikes(double lat, double lon, int limit, int radius) {
        return getBikes(lat, lon, limit, radius, null);
    }

    public static List<BikeRecord> getBikes(double lat, double lon, int limit, int radius, BikeStatus status) {
        try {
            String sql = null;
            if(status == null) {
                sql = "SELECT  * FROM bike WHERE ST_DWithin(CAST(ST_MakePoint(bike.lon, bike.lat) AS geography(GEOMETRY,-1)), " +
                                "CAST(ST_MakePoint(" + lon + "," + lat + ") AS geography(GEOMETRY,-1)), " + radius +
                                ") " + "limit " + limit;
            } else {
                sql = "SELECT  * FROM bike WHERE ST_DWithin(CAST(ST_MakePoint(bike.lon, bike.lat) AS geography(GEOMETRY,-1)), " +
                        "CAST(ST_MakePoint(" + lon + "," + lat + ") AS geography(GEOMETRY,-1)), " + radius +
                        ") AND status = '" + status.getLiteral() + "' limit " + limit;
            }

            List<BikeRecord> bikes = DatabaseConnector.getDb().getReadDbConnector().fetch(sql).into(Bike.BIKE);
            return bikes;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return Lists.newArrayList();
    }

    public static List<HubRecord> getHubs(double lat, double lon, int limit, int radius) {
        try {
            String sql = "SELECT * FROM hub WHERE ST_DWithin(CAST(ST_MakePoint(hub.lon, hub.lat) AS geography(GEOMETRY,-1)), " +
                    "CAST(ST_MakePoint(" + lon + "," + lat + ") AS geography(GEOMETRY,-1)), " + radius +
                    ") " + "limit " + limit;

            List<HubRecord> hubs = DatabaseConnector.getDb().getReadDbConnector().fetch(sql).into(Hub.HUB);
            return hubs;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return Lists.newArrayList();
    }

    public static List<BikeRecord> getBikes(String searchQuery) {
        try {
            boolean isBikeId = false;
            if (!TextUtils.isEmpty(searchQuery)) {
                try {
                    int num = Integer.parseInt(searchQuery);
                    if (num > 9999) {
                        isBikeId = true;
                    }
                } catch (NumberFormatException e) {
                }
            }
            List<BikeRecord> bikes;
            if (isBikeId) {
                bikes = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Bike.BIKE)
                        .where(Bike.BIKE.ID.eq(Integer.parseInt(searchQuery))).fetch();
            } else {
                bikes = DatabaseConnector.getDb().getReadDbConnector().selectFrom(Bike.BIKE)
                        .where(Bike.BIKE.LICENSE_PLATE.contains(searchQuery)).fetch();
            }
            return bikes;
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return Lists.newArrayList();
    }

    public static BikeRecord getBike(int bikeId) {
        Result<Record> records =
                DatabaseConnector.getDb().getConnector().select().from(Bike.BIKE).where(Bike.BIKE.ID.eq(bikeId))
                        .limit(1).fetch();

        if (records != null && records.size() > 0) {
            return (BikeRecord) records.get(0);
        }
        return null;
    }

    public static List<MarkerPojo> getBookingMarkers(List<Pair<BikeRecord, BookingRecord>> bookings) {
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
                marker.data.put("Timeline", "<b><a href='#' onclick='showTimeline(\"/apis/bike/events\", " + bike.getId() +");'>Events</a></b>");
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

    public static List<CirclePojo> getHubsAsMarkers(List<HubRecord> hubs) {
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

    public static List<FencePojo> getParkingFences(double lat, double lon, int radius) {
        List<FencePojo> fences = Lists.newArrayList();
        List<Map<String, Object>> parkingList = getParkingAround(lat, lon, radius);
        for(Map<String, Object> parkingMap : parkingList) {
            Map<String, Object> data = Maps.newHashMap();
            data.put("Name", parkingMap.get("name"));
            data.put("Category", parkingMap.get("category"));
            FencePojo fence = FencePojo.createDefaultFence((List<PointPojo>)parkingMap.get("polygon"), data);
            if(! (boolean)parkingMap.get("negative")) {
                fence.fillColor = "#a2cff5";
            }
            fences.add(fence);
        }
        return fences;
    }

    private static List<Map<String, Object>> getParkingAround(double lat, double lon, int radius) {
        List<Map<String, Object>> parkingData = new ArrayList<>();
        try {
            String sql = "SELECT ST_AsText(ST_ExteriorRing(fence)) as fence, name, category_name, negative, priority \n" +
                    "FROM public.parking \n" +
                    "LEFT JOIN public.parking_category on parking.category_id = parking_category.id\n" +
                    "WHERE ST_DWithin(fence::geography, ST_MakePoint(" + lon + "," + lat + ")::geography," + radius + ")\n" +
                    "    and enabled and active and verified";
            Result<Record> records = DatabaseConnector.getDb().getConnector().fetch(sql);
            for (Record record : records) {
                Map<String, Object> parkingMap = new HashMap<>();
                parkingMap.put("polygon", getPointsFromSqlLineString(record.get("fence").toString()));
                parkingMap.put("name", record.get("name"));
                parkingMap.put("category", record.get("category_name"));
                parkingMap.put("negative", record.get("negative"));
                parkingData.add(parkingMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BounceUtils.logError(e);
        }
        return parkingData;
    }

    private static List<PointPojo> getPointsFromSqlLineString(String lineString) {
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

    public static BookingRecord getLatestBooking(BikeRecord bike) {
        Result<Record> records = DatabaseConnector.getDb().getConnector().select().from(Booking.BOOKING)
                .where(Booking.BOOKING.BIKE_ID.eq(bike.getId()))
                .orderBy(Booking.BOOKING.CREATED_ON.desc()).limit(20).fetch();

        if (records != null && records.size() > 0) {
            return (BookingRecord) records.get(0);
        }
        return null;
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
                startPoint.subtext = booking.getTripStartTime().toString();

                MarkerPojo endPoint = new MarkerPojo();
                endPoint.location = new PointPojo(booking.getActEndPointLat(), booking.getActEndPointLon());
                endPoint.iconUrl = "/resources/icons/marker_blue.png";
                endPoint.title = "Actual End Point";
                endPoint.subtext = booking.getTripEndTime().toString();

                PathPojo pathPojo = new PathPojo();
                pathPojo.points = Lists.newArrayList();
                pathPojo.points.add(startPoint.location);
                pathPojo.points.add(endPoint.location);
                pathPojo.data = Maps.newHashMap();
                pathPojo.data.put("Booking Id", booking.getId());

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
                endPoint.subtext = bike.getLocUpdatedTime().toString();

                PathPojo pathPojo = new PathPojo();
                pathPojo.points = Lists.newArrayList();
                pathPojo.points.add(startPoint.location);
                pathPojo.points.add(endPoint.location);
                pathPojo.data = Maps.newHashMap();
                pathPojo.data.put("Booking Id", booking.getId());

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
                startPoint.subtext = booking.getTripStartTime().toString();


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
