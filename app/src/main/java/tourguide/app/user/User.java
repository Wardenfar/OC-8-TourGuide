package tourguide.app.user;

import lombok.Getter;
import lombok.Setter;
import tourguide.common.helper.RwLockList;
import tourguide.common.model.ProviderModel;
import tourguide.common.model.UserPreferences;
import tourguide.common.model.UserRewardModel;
import tourguide.common.model.VisitedLocationModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User {
    private final UUID id;
    private final String userName;
    private final RwLockList<VisitedLocationModel> visitedLocations = new RwLockList<>(new ArrayList<>());
    private final List<UserRewardModel> userRewards = new ArrayList<>();
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private UserPreferences userPreferences = new UserPreferences();
    private List<ProviderModel> tripDeals = new ArrayList<>();

    public User(UUID id, String userName, String phoneNumber, String emailAddress) {
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public void addToVisitedLocations(VisitedLocationModel visitedLocation) {
        RwLockList.RwListGuard<VisitedLocationModel> guard = visitedLocations.write();
        guard.inner().add(visitedLocation);
        guard.release();
    }

    public RwLockList<VisitedLocationModel> getVisitedLocations() {
        return visitedLocations;
    }

    public void addUserReward(UserRewardModel userReward) {
        if (userRewards.stream().noneMatch(r -> r.getAttraction().getName().equals(userReward.getAttraction().getName()))) {
            userRewards.add(userReward);
        }
    }

    public VisitedLocationModel getLastVisitedLocation() {
        RwLockList.RwListGuard<VisitedLocationModel> guard = visitedLocations.read();
        if (guard.inner().isEmpty()) {
            guard.release();
            return null;
        } else {
            VisitedLocationModel result = guard.inner().get(guard.inner().size() - 1);
            guard.release();
            return result;
        }
    }
}
