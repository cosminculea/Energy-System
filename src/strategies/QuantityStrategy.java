package strategies;

import player.Producer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class QuantityStrategy implements Strategy {

    /**
     * list of all producers in the system
     */

    private final List<Producer> producers;

    /**
     * constructor
     * @param producers all producers in the system
     */

    public QuantityStrategy(final List<Producer> producers) {
        this.producers = new ArrayList<>(producers);
    }

    /**
     * - apply the strategy by sorting the producers after quantity
     * @return list of producers sorted after the QUANTITY strategy priorities
     */

    @Override
    public List<Producer> applyStrategy() {
        producers.sort(Comparator.comparingInt(Producer::getEnergyPerDistributor)
                                .reversed()
                                .thenComparing(Producer::getId));

        return producers;
    }

    @Override
    public String getType() {
        return EnergyChoiceStrategyType.QUANTITY.getLabel();
    }
}
