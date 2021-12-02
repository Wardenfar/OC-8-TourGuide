package tourguide.pricer;

import tourguide.common.model.ProviderModel;
import tripPricer.Provider;

public class Converter {

    public static ProviderModel convert(Provider provider) {
        ProviderModel model = new ProviderModel();
        model.setName(provider.name);
        model.setTripId(provider.tripId);
        model.setPrice(provider.price);
        return model;
    }

}
