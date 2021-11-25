package tourGuide.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;


@Getter
@Setter
@NoArgsConstructor
public class UserPreferences {

    private final CurrencyUnit currency = Monetary.getCurrency("USD");
    private int attractionProximity = Integer.MAX_VALUE;
    private Money lowerPricePoint = Money.of(0, currency);
    private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);
    private int tripDuration = 1;
    private int ticketQuantity = 1;
    private int numberOfAdults = 1;
    private int numberOfChildren = 0;

}
