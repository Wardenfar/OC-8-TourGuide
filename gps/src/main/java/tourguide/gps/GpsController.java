package tourguide.gps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.VisitedLocationModel;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsController {

    @Autowired
    GpsService gpsService;

    // get user location
    @GetMapping("user/location/{userId}")
    public VisitedLocationModel getUserLocation(@PathVariable String userId) {
        return gpsService.getUserLocation(UUID.fromString(userId));
    }

    // get attractions
    @GetMapping("attractions")
    public List<AttractionModel> getAttractions() {
        return gpsService.getAttractions();
    }
}
