package tourguide.gps;

import com.sun.org.apache.bcel.internal.generic.LXOR;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.VisitedLocationModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GpsService {

    GpsUtil gpsUtil;

    public GpsService() {
        gpsUtil = new GpsUtil();
    }

    public VisitedLocationModel getUserLocation(UUID userId) {
        return Converter.toModel(gpsUtil.getUserLocation(userId));
    }

    // List attractions
    public List<AttractionModel> getAttractions() {
        List<Attraction> list = gpsUtil.getAttractions();
        return list.stream().map(Converter::toModel).collect(Collectors.toList());
    }
}
