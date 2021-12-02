package tourguide.pricer;

import org.springframework.stereotype.Service;
import tourguide.common.model.ProviderModel;
import tourguide.common.model.UserPreferences;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PricerService {

    private static final String API_KEY = "api_key26223223";

    TripPricer tripPricer;

    public PricerService() {
        tripPricer = new TripPricer();
    }

    public List<ProviderModel> getPrice(UUID userId, int rewardsPoints, UserPreferences userPreferences) {
        List<Provider> list = tripPricer.getPrice(API_KEY, userId, userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren(), userPreferences.getTripDuration(), rewardsPoints);
        return list.stream().map(Converter::convert).collect(Collectors.toList());
    }

}
