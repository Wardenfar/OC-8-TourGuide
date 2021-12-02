package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.helper.RwLockList;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private final int defaultProximityBuffer = 10;
    private final int attractionProximityRange = 200;
    private final GpsUtil gpsUtil;
    private final RewardCentral rewardsCentral;
    private int proximityBuffer = defaultProximityBuffer;

    public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public void calculateRewards(User user) {
        RwLockList.RwListGuard<VisitedLocation> guard = user.getVisitedLocations().read();

        List<Attraction> attractions = gpsUtil.getAttractions();
        for (VisitedLocation visitedLocation : guard.inner()) {
            for (Attraction attraction : attractions) {
                if (nearAttraction(visitedLocation, attraction)) {
                    if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                    }
                }
            }
        }

        guard.release();
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return !(getDistance(attraction, location) > attractionProximityRange);
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
    }

    public int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
