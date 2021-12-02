package tourguide.app;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourguide.app.service.GpsService;
import tourguide.app.service.RewardsService;
import tourguide.app.service.TourGuideService;
import tourguide.app.user.User;
import tourguide.common.helper.InternalTestHelper;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.ProviderModel;
import tourguide.common.model.VisitedLocationModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestTourGuideService {

	@Autowired
	TourGuideService tourGuideService;

	@Autowired
	RewardsService rewardsService;

	@Autowired
	GpsService gpsService;

	@Test
	public void getUserLocation() {
		InternalTestHelper.setInternalUserNumber(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocationModel visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.stopTracker();
		assertEquals(visitedLocation.getUserId(), user.getId());
	}
	
	@Test
	public void addUser() {
		InternalTestHelper.setInternalUserNumber(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrievedUser = tourGuideService.getUser(user.getUserName());
		User retrievedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.stopTracker();
		
		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}
	
	@Test
	public void getAllUsers() {
		InternalTestHelper.setInternalUserNumber(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.stopTracker();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUser() {
		InternalTestHelper.setInternalUserNumber(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocationModel visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.stopTracker();
		
		assertEquals(user.getId(), visitedLocation.getUserId());
	}
	
	@Test
	public void getNearbyAttractions() {
		InternalTestHelper.setInternalUserNumber(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocationModel visitedLocation = tourGuideService.trackUserLocation(user);
		
		List<AttractionModel> attractions = tourGuideService.getNearByAttractions(visitedLocation);
		
		tourGuideService.stopTracker();
		
		assertEquals(5, attractions.size());
	}

	@Test
	public void getTripDeals() {
		InternalTestHelper.setInternalUserNumber(0);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		List<ProviderModel> providers = tourGuideService.getTripDeals(user);
		tourGuideService.stopTracker();
		assertEquals(5, providers.size());
	}
}
