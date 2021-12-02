package tourguide.rewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RewardsController {

    @Autowired
    RewardsService rewardsService;

    @GetMapping("rewardPoints/attraction/{attractionId}/user/{userId}")
    public int getRewardPointsForAttraction(@PathVariable String attractionId, @PathVariable String userId) {
        UUID attractionUUID = UUID.fromString(attractionId);
        UUID userUUID = UUID.fromString(userId);
        return rewardsService.getRewardPointsForAttraction(attractionUUID, userUUID);
    }

}
