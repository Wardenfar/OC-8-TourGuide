package tourguide.app.tracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import tourguide.app.service.TourGuideService;
import tourguide.app.user.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Tracker extends Thread {

    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);

    private final TourGuideService tourGuideService;
    private boolean stop = false;

    public Tracker(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
    }

    @Override
    public void run() {

        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                log.debug("Tracker stopping");
                break;
            }

            StopWatch stopWatch = new StopWatch();

            List<User> users = tourGuideService.getAllUsers();
            log.debug("Begin Tracker. Tracking " + users.size() + " users.");
            stopWatch.start();

            tourGuideService.trackMultipleUserLocation(users);

            stopWatch.stop();
            log.debug("Tracker Time Elapsed: " + stopWatch.getTotalTimeSeconds() + " seconds.");
            try {
                log.debug("Tracker sleeping");
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
