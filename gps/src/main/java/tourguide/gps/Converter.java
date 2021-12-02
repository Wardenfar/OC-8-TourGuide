package tourguide.gps;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.LocationModel;
import tourguide.common.model.VisitedLocationModel;

public class Converter {

    // VisitedLocation to VisitedLocationModel
    public static VisitedLocationModel toModel(VisitedLocation visitedLocation) {
        VisitedLocationModel model = new VisitedLocationModel();
        model.setUserId(visitedLocation.userId);
        model.setLatitude(visitedLocation.location.latitude);
        model.setLongitude(visitedLocation.location.longitude);
        model.setTimeVisited(visitedLocation.timeVisited);
        return model;
    }

    // Attraction to Attraction Model
    public static AttractionModel toModel(Attraction attraction) {
        AttractionModel model = new AttractionModel();
        model.setName(attraction.attractionName);
        model.setCity(attraction.city);
        model.setState(attraction.state);
        model.setId(attraction.attractionId);
        model.setLatitude(attraction.latitude);
        model.setLongitude(attraction.longitude);
        return model;
    }

    // Location to LocationModel
    public static LocationModel toModel(Location location) {
        LocationModel model = new LocationModel();
        model.setLatitude(location.latitude);
        model.setLongitude(location.longitude);
        return model;
    }
}
