package tourguide.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourguide.app.service.GpsService;
import tourguide.app.service.RewardsService;
import tourguide.app.service.TourGuideService;
import tourguide.app.user.User;
import tourguide.common.helper.InternalTestHelper;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.UserRewardModel;
import tourguide.common.model.VisitedLocationModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestRewardsService {

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    RewardsService rewardsService;

    @Autowired
    GpsService gpsService;

    @Test
    public void userGetRewards() {
        InternalTestHelper.setInternalUserNumber(0);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        AttractionModel attraction = gpsService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocationModel(user.getId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserRewardModel> userRewards = user.getUserRewards();
        tourGuideService.stopTracker();
		assertEquals(1, userRewards.size());
    }

    @Test
    public void isWithinAttractionProximity() {
        AttractionModel attraction = gpsService.getAttractions().get(0);
        assertTrue(rewardsService.getDistance(attraction, attraction) < 1);
    }

    @Test
    public void nearAllAttractions() {
        InternalTestHelper.setInternalUserNumber(1);

		List<AttractionModel> attractions = gpsService.getAttractions();
        rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0), attractions);
        List<UserRewardModel> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
        tourGuideService.stopTracker();

        assertEquals(attractions.size(), userRewards.size());
    }

}
