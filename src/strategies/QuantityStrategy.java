package strategies;

import constants.Constants;
import entities.Producer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class QuantityStrategy implements Strategy {

    /**
     * list of all producers in the system
     */

    private final List<Producer> producers;

    public QuantityStrategy(final List<Producer> producers) {
        this.producers = new ArrayList<>(producers);
    }

    @Override
    public List<Producer> applyStrategy() {
        producers.sort(Comparator.comparingInt(Producer::getId));
        producers.sort((producer1, producer2) ->
                producer2.getEnergyPerDistributor() - producer1.getEnergyPerDistributor());
        return producers;
    }

    @Override
    public String getType() {
        return Constants.QUANTITY;
    }
}
