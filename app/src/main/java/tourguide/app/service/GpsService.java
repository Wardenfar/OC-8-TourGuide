package tourguide.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import tourguide.app.http.HttpClient;
import tourguide.common.model.AttractionModel;
import tourguide.common.model.VisitedLocationModel;

import java.util.List;
import java.util.UUID;

@Service
public class GpsService {

    private static final String REMOTE = "http://localhost:8081/";

    public VisitedLocationModel getUserLocation(UUID userId) {
        String url = REMOTE + "user/location/" + userId.toString();
        return HttpClient.get(VisitedLocationModel.class, url);
    }

    public List<AttractionModel> getAttractions() {
        String url = REMOTE + "attractions";
        AttractionModel[] attractions = HttpClient.get(AttractionModel[].class, url);
        assert attractions != null;
        return List.of(attractions);
    }



}
