package tourguide.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tourguide.app.http.HttpClient;
import tourguide.common.model.ProviderModel;
import tourguide.common.model.UserPreferences;

import java.util.List;
import java.util.UUID;

@Service
public class PricerService {

    private static final String REMOTE = "http://localhost:8083/";

    public List<ProviderModel> price(UUID userId, int rewardPoints, UserPreferences preferences) {
        String url = REMOTE + "price/" + userId + "/" + rewardPoints;
        ProviderModel[] list = HttpClient.post(ProviderModel[].class, url, preferences);
        return List.of(list);
    }
}
