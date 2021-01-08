package strategies;

import constants.Constants;
import entities.Producer;

import java.util.*;

public final class PriceStrategy implements Strategy {

    /**
     * list of all producers in the system
     */

    private final List<Producer> producers;

    public PriceStrategy(final List<Producer> producers) {
        this.producers = new ArrayList<>(producers);
    }

    public List<Producer> applyStrategy() {
        producers.sort((producer1, producer2) -> {
            if (Double.compare(producer1.getPriceKW(), producer2.getPriceKW()) == 0) {
                if (producer2.getEnergyPerDistributor() == producer1.getEnergyPerDistributor()) {
                    return producer1.getId() - producer2.getId();
                } else {
                    return producer2.getEnergyPerDistributor() - producer1.getEnergyPerDistributor();
                }
            } else {
                return Double.compare(producer1.getPriceKW(), producer2.getPriceKW());
            }
        });

        return producers;
    }

    @Override
    public String getType() {
        return "PRICE";
    }
}
