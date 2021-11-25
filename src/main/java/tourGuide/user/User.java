package tourGuide.user;

import gpsUtil.location.VisitedLocation;
import lombok.Getter;
import lombok.Setter;
import tourGuide.helper.RwLockList;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User {
	private final UUID userId;
	private final String userName;
	private final RwLockList<VisitedLocation> visitedLocations = new RwLockList<>(new ArrayList<>());
	private final List<UserReward> userRewards = new ArrayList<>();
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();

	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		RwLockList.RwListGuard<VisitedLocation> guard = visitedLocations.write();
		guard.inner().add(visitedLocation);
		guard.release();
	}

	public RwLockList<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}

	public void addUserReward(UserReward userReward) {
		if (userRewards.stream().noneMatch(r -> r.attraction.attractionName.equals(userReward.attraction.attractionName))) {
			userRewards.add(userReward);
		}
	}

	public VisitedLocation getLastVisitedLocation() {
		RwLockList.RwListGuard<VisitedLocation> guard = visitedLocations.read();
		if (guard.inner().isEmpty()) {
			guard.release();
			return null;
		} else {
			VisitedLocation result = guard.inner().get(guard.inner().size() - 1);
			guard.release();
			return result;
		}
	}
}
