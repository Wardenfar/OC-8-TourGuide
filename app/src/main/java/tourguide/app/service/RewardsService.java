package tourguide.app.service;

import org.springframework.stereotype.Service;
import tourguide.app.http.HttpClient;
import tourguide.app.user.User;
import tourguide.common.helper.RwLockList;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.LocationModel;
import tourguide.common.model.UserRewardModel;
import tourguide.common.model.VisitedLocationModel;

import java.util.List;
import java.util.UUID;

@Service
public class RewardsService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private static final int DEFAULT_PROXIMITY_BUFFER = 10;

    private static final String REMOTE = "http://localhost:8082/";

    public int getRewardPointsForAttraction(UUID attractionId, UUID userId) {
       String url = REMOTE + "rewardPoints/attraction/" + attractionId + "/user/" + userId;
       return HttpClient.get(int.class, url);
    }

    public void calculateRewards(User user, List<AttractionModel> allAttractions) {
        RwLockList.RwListGuard<VisitedLocationModel> guard = user.getVisitedLocations().read();

        for (VisitedLocationModel visitedLocation : guard.inner()) {
            for (AttractionModel attraction : allAttractions) {
                if (nearAttraction(visitedLocation, attraction)) {
                    if (user.getUserRewards().stream().noneMatch(r -> r.getAttraction().getName().equals(attraction.getName()))) {
                        user.addUserReward(new UserRewardModel(visitedLocation, attraction, getRewardPoints(attraction, user)));
                    }
                }
            }
        }

        guard.release();
    }

    private boolean nearAttraction(VisitedLocationModel visitedLocation, AttractionModel attraction) {
        return !(getDistance(attraction, visitedLocation) > DEFAULT_PROXIMITY_BUFFER);
    }

    public int getRewardPoints(AttractionModel attraction, User user) {
        return getRewardPointsForAttraction(attraction.getId(), user.getId());
    }

    public double getDistance(LocationModel loc1, LocationModel loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }

}
