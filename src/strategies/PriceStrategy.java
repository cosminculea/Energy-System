package strategies;

import player.Producer;

import java.util.ArrayList;
import java.util.List;

public final class PriceStrategy implements Strategy {

    /**
     * list of all producers in the system
     */

    private final List<Producer> producers;

    /**
     * constructor
     * @param producers list of all producers in the system
     */

    public PriceStrategy(final List<Producer> producers) {
        this.producers = new ArrayList<>(producers);
    }

    /**
     * - apply the strategy by sorting the producers after price, quantity and ids
     * @return list of producers sorted after the PRICE strategy priorities
     */

    public List<Producer> applyStrategy() {
        producers.sort(this::compareAfterStrategyPriorities);

        return producers;
    }

    /**
     * compare 2 producers after price, quantity and ids
     */

    private int compareAfterStrategyPriorities(final Producer producer1, final Producer producer2) {
        if (Double.compare(producer1.getPriceKW(), producer2.getPriceKW()) == 0) {
            if (producer2.getEnergyPerDistributor() == producer1.getEnergyPerDistributor()) {
                return producer1.getId() - producer2.getId();
            } else {
                return producer2.getEnergyPerDistributor() - producer1.getEnergyPerDistributor();
            }
        } else {
            return Double.compare(producer1.getPriceKW(), producer2.getPriceKW());
        }
    }

    @Override
    public String getType() {
        return EnergyChoiceStrategyType.PRICE.getLabel();
    }
}
