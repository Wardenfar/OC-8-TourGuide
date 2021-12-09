package tourguide.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.app.tracker.Tracker;
import tourguide.app.user.User;
import tourguide.common.helper.InternalTestHelper;
import tourguide.common.helper.RwLockList;
import tourguide.common.model.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TourGuideService {

    @Autowired
    GpsService gpsService;

    @Autowired
    PricerService pricerService;

    @Autowired
    RewardsService rewardsService;

    private final Tracker tracker;

    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
    private final Map<String, User> internalUserMap = new HashMap<>();
    boolean testMode = true;

    ExecutorService executor = Executors.newFixedThreadPool(32);

    public TourGuideService() {
        if (testMode) {
            log.info("TestMode enabled");
            log.debug("Initializing users");
            initializeInternalUsers();
            log.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        executor.submit(tracker);
        addShutDownHook();
    }

    public List<NearByAttractionModel> getNearByAttractions(User user) {
        VisitedLocationModel userLoc = gpsService.getUserLocation(user.getId());
        List<AttractionModel> attractions = getNearByAttractions(userLoc);
        List<NearByAttractionModel> result = new ArrayList<>();
        for (AttractionModel attraction : attractions) {
            NearByAttractionModel nba = new NearByAttractionModel();
            nba.setAttractionName(attraction.getName());
            nba.setAttractionLat(attraction.getLatitude());
            nba.setAttractionLon(attraction.getLongitude());
            nba.setUserLat(userLoc.getLatitude());
            nba.setUserLon(userLoc.getLongitude());
            nba.setDistance(rewardsService.getDistance(attraction, userLoc));
            nba.setRewardPoints(rewardsService.getRewardPoints(attraction, user));
            result.add(nba);
        }
        return result;
    }

    public List<UserRewardModel> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public VisitedLocationModel getUserLocation(User user) {
        RwLockList.RwListGuard<VisitedLocationModel> guard = user.getVisitedLocations().read();
        int size = guard.inner().size();
        guard.release();

        return (size > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
    }

    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(internalUserMap.values());
    }

    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    public List<ProviderModel> getTripDeals(User user) {
        int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(UserRewardModel::getRewardPoints).sum();
        List<ProviderModel> providers = pricerService.price(
                user.getId(),
                cumulativeRewardPoints,
                user.getUserPreferences()
        );
        user.setTripDeals(providers);
        return providers;
    }

    public void trackMultipleUserLocation(List<User> users) {
        List<Future<VisitedLocationModel>> futures = new ArrayList<>();
        for (User u : users) {
            Future<VisitedLocationModel> future = executor.submit(() -> trackUserLocation(u));
            futures.add(future);
        }
        while (true) {
            long rest = futures.stream().filter(f -> !f.isDone()).count();
            if (rest == 0) {
                break;
            } else {
                System.out.println("Remaining to track : " + rest);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public VisitedLocationModel trackUserLocation(User user) {
        VisitedLocationModel visitedLocation = gpsService.getUserLocation(user.getId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user, gpsService.getAttractions());
        return visitedLocation;
    }

    public List<AttractionModel> getNearByAttractions(VisitedLocationModel userLoc) {
        return gpsService.getAttractions().stream().sorted((a, b) -> {
            double da = rewardsService.getDistance(a, userLoc);
            double db = rewardsService.getDistance(b, userLoc);
            return Double.compare(da, db);
        }).limit(5).collect(Collectors.toList());
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            tracker.stopTracking();
            executor.shutdownNow();
        }));
    }

    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        log.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        RwLockList.RwListGuard<VisitedLocationModel> guard = user.getVisitedLocations().write();
        IntStream.range(0, 3).forEach(i -> {
            VisitedLocationModel l = new VisitedLocationModel();
            l.setLatitude(generateRandomLatitude());
            l.setLongitude(generateRandomLongitude());
            l.setTimeVisited(getRandomTime());
            l.setUserId(user.getId());
            guard.inner().add(l);
        });
        guard.release();
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    public void stopTracker() {
        tracker.stopTracking();
    }
}
