package tourguide.rewards;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardsService {
    RewardCentral rewardCentral;

    public RewardsService() {
        rewardCentral = new RewardCentral();
    }

    public int getRewardPointsForAttraction(UUID attractionId, UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }
}
