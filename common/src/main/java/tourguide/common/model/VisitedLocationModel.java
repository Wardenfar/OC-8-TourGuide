package tourguide.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitedLocationModel extends LocationModel {

    UUID userId;
    Date timeVisited;

    public VisitedLocationModel(UUID userId, LocationModel locationModel, Date timeVisited) {
        this.userId = userId;
        this.latitude = locationModel.getLatitude();
        this.longitude = locationModel.getLongitude();
        this.timeVisited = timeVisited;
    }

}
