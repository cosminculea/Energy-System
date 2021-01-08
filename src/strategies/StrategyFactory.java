package strategies;


import entities.Producer;

import java.util.List;
import java.util.Map;

public final class StrategyFactory {

    public static Strategy getStrategy(final String type,
                                       final List<Producer> producers) {

        EnergyChoiceStrategyType energyType = EnergyChoiceStrategyType.valueOf(type);

        if (energyType.equals(EnergyChoiceStrategyType.GREEN)) {
            return new GreenStrategy(producers);
        }

        if (energyType.equals(EnergyChoiceStrategyType.QUANTITY)) {
            return new QuantityStrategy(producers);
        }

        if (energyType.equals(EnergyChoiceStrategyType.PRICE)) {
            return new PriceStrategy(producers);
        }

        return null;
    }
}
