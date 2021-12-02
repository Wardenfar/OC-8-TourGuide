package tourguide.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRewardModel {

    VisitedLocationModel visitedLocation;
    AttractionModel attraction;
    int rewardPoints;

}
