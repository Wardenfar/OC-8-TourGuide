package tourguide.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourguide.app.service.TourGuideService;
import tourguide.app.user.User;
import tourguide.common.model.*;

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
    public VisitedLocationModel getLocation(@RequestParam String userName) {
        return tourGuideService.getUserLocation(getUser(userName));
    }

    @RequestMapping("/getNearbyAttractions")
    public List<NearByAttractionModel> getNearbyAttractions(@RequestParam String userName) {
        User user = getUser(userName);
        return tourGuideService.getNearByAttractions(user);
    }

    @RequestMapping("/getRewards")
    public List<UserRewardModel> getRewards(@RequestParam String userName) {
        return tourGuideService.getUserRewards(getUser(userName));
    }

    @RequestMapping("/getAllCurrentLocations")
    public HashMap<UUID, LocationModel> getAllCurrentLocations() {
        HashMap<UUID, LocationModel> result = new HashMap<>();
        for (User user : tourGuideService.getAllUsers()) {
            VisitedLocationModel visitedLocation = user.getLastVisitedLocation();
            if (visitedLocation != null) {
                result.put(user.getId(), visitedLocation);
            }
        }
        return result;
    }

    @RequestMapping("/getTripDeals")
    public List<ProviderModel> getTripDeals(@RequestParam String userName) {
        return tourGuideService.getTripDeals(getUser(userName));
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