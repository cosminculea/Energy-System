package strategies;


import player.Producer;

import java.util.List;


public final class StrategyFactory {

    /**
     * the only instance of this class
     */

    private static final StrategyFactory FACTORY = new StrategyFactory();

    /**
     * private constructor -> Singleton Pattern
     */

    private StrategyFactory() { }

    /**
     * - method for factory pattern which creates and returns a strategy for a distributor depending
     * on the type given
     * @param type type of strategy (GREEN, PRICE, QUANTITY)
     * @param producers list of all producers in the system used for strategies constructors
     * @return strategy for distributor
     */

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

    /**
     * @return the only instance of this class
     */

    public static StrategyFactory getFACTORY() {
        return FACTORY;
    }
}
