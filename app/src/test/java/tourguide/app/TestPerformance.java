package tourguide.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import tourguide.app.service.GpsService;
import tourguide.app.service.RewardsService;
import tourguide.app.service.TourGuideService;
import tourguide.app.user.User;
import tourguide.common.helper.InternalTestHelper;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.VisitedLocationModel;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestPerformance {

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    RewardsService rewardsService;

    @Autowired
    GpsService gpsService;

    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */

    @Test
    public void highVolumeTrackLocation() {
        InternalTestHelper.setInternalUserNumber(100);

        List<User> allUsers = tourGuideService.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        tourGuideService.trackMultipleUserLocation(allUsers);

        stopWatch.stop();
        tourGuideService.stopTracker();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTotalTimeMillis()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTotalTimeMillis()));
    }

    @Test
    public void highVolumeGetRewards() {
        InternalTestHelper.setInternalUserNumber(100);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<AttractionModel> allAttractions = gpsService.getAttractions();
        AttractionModel attraction = allAttractions.get(0);
        List<User> allUsers = tourGuideService.getAllUsers();

        allUsers.forEach(u -> {
            VisitedLocationModel l = new VisitedLocationModel();
            l.setUserId(u.getId());
            l.setLatitude(attraction.getLatitude());
            l.setLongitude(attraction.getLongitude());
            l.setTimeVisited(new Date());
            u.addToVisitedLocations(l);
        });

        allUsers.forEach(u -> rewardsService.calculateRewards(u, allAttractions));

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }
        stopWatch.stop();
        tourGuideService.stopTracker();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTotalTimeMillis()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTotalTimeMillis()));
    }

}
