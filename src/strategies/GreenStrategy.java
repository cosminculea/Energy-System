package strategies;

import constants.Constants;
import entities.Producer;

import java.util.*;

public final class GreenStrategy implements Strategy {
    /**
     * list of all producers in the system
     */

    private final List<Producer> producers;


    /**
     * constructor
     */

    public GreenStrategy(final List<Producer> producers) {
        this.producers = new ArrayList<>(producers);
    }

    public List<Producer> applyStrategy() {
        producers.sort((producer1, producer2) -> {
            if (producer1.getEnergyType().isRenewable() &&
                    producer2.getEnergyType().isRenewable()) {
                if (Double.compare(producer1.getPriceKW(), producer2.getPriceKW()) == 0) {
                    if (producer2.getEnergyPerDistributor()
                            == producer1.getEnergyPerDistributor()) {
                        return producer1.getId() - producer2.getId();
                    } else {
                        return Integer.compare(producer2.getEnergyPerDistributor(),
                                producer1.getEnergyPerDistributor());
                    }
                } else {
                    return Double.compare(producer1.getPriceKW(), producer2.getPriceKW());
                }
            }

            if (producer2.getEnergyType().isRenewable()) {
                return 1;
            } else {
                return -1;
            }
        });

        return producers;
    }

    @Override
    public String getType() {
        return Constants.GREEN;
    }

}
