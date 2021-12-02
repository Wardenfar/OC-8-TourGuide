package tourguide.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPreferences {

    int attractionProximity = Integer.MAX_VALUE;
    double lowerPricePoint = 0.0;
    double highPricePoint = Double.MAX_VALUE;
    int tripDuration = 1;
    int ticketQuantity = 1;
    int numberOfAdults = 1;
    int numberOfChildren = 0;

}
