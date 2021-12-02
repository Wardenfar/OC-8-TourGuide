package tourguide.pricer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourguide.common.model.ProviderModel;
import tourguide.common.model.UserPreferences;

import java.util.List;
import java.util.UUID;

@RestController
public class PricerController {

    @Autowired
    PricerService pricerService;

    @PostMapping("/price/{attractionUUID}/{rewardPoints}")
    public List<ProviderModel> price(@PathVariable String attractionUUID, @PathVariable String rewardPoints, @RequestBody UserPreferences pref) {
        return pricerService.getPrice(
                UUID.fromString(attractionUUID),
                Integer.parseInt(rewardPoints),
                pref);
    }
}
