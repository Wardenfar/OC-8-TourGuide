package tourGuide;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.model.NearByAttractionModel;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class TourGuideController {

    @Autowired
    TourGuideService tourGuideService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        User user = getUser(userName);
        VisitedLocation userLoc = tourGuideService.getUserLocation(user);
        List<Attraction> attractions = tourGuideService.getNearByAttractions(userLoc);
        List<NearByAttractionModel> result = new ArrayList<>();
        for (Attraction attraction : attractions) {
            NearByAttractionModel nba = new NearByAttractionModel();
            nba.attractionName = attraction.attractionName;
            nba.attractionLat = attraction.latitude;
            nba.attractionLon = attraction.longitude;
            nba.userLat = userLoc.location.latitude;
            nba.userLon = userLoc.location.longitude;
            nba.distance = tourGuideService.rewardsService.getDistance(userLoc.location, attraction);
            nba.rewardPoints = tourGuideService.rewardsService.getRewardPoints(attraction, user);
            result.add(nba);
        }
        return JsonStream.serialize(result);
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        HashMap<UUID, Location> result = new HashMap<>();
        for (User user : tourGuideService.getAllUsers()) {
            VisitedLocation visitedLocation = user.getLastVisitedLocation();
            if (visitedLocation != null) {
                result.put(user.getUserId(), visitedLocation.location);
            }
        }

        return JsonStream.serialize(result);
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }

    @PostMapping("/update/preferences/{userName}")
    public void updateUserPreferences(@PathVariable String userName, @RequestParam UserPreferences preferences) {
        User user = getUser(userName);
        user.setUserPreferences(preferences);
    }
}