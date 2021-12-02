package tourguide.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public
class NearByAttractionModel {
    
    String attractionName;
    double attractionLat;
    double attractionLon;
    double userLat;
    double userLon;
    double distance;
    int rewardPoints;

}
