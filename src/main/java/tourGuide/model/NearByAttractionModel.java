package tourGuide.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NearByAttractionModel {
    public String attractionName;
    public double attractionLat;
    public double attractionLon;
    public double userLat;
    public double userLon;
    public double distance;
    public int rewardPoints;
}
