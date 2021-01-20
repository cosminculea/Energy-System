package strategies;

import player.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class GreenStrategy implements Strategy {

    /**
     * list of all producers in the system
     */

    private final List<Producer> producers;


    /**
     * constructor
     * @param producers list of all producers in the system
     */

    public GreenStrategy(final List<Producer> producers) {
        this.producers = new ArrayList<>(producers);
    }

    /**
     * - apply the strategy by sorting the producers after price, quantity and ids while
     * prioritising producers that have renewable type of energy
     * @return list of producers sorted after the GREEN strategy priorities
     */

    public List<Producer> applyStrategy() {
        List<Producer> producersRenewable = producers.stream()
                .filter(producer -> producer.getEnergyType().isRenewable())
                .sorted(this::compareAfterStrategyPriorities)
                .collect(Collectors.toList());


        List<Producer> producersNotRenewable = producers.stream()
                .filter(producer -> !producer.getEnergyType().isRenewable())
                .sorted(this::compareAfterStrategyPriorities)
                .collect(Collectors.toList());

        producersRenewable.addAll(producersNotRenewable);

        return producersRenewable;
    }

    /**
     * compare 2 producers after price, quantity and ids
     */

    private int compareAfterStrategyPriorities(final Producer producer1, final Producer producer2) {
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

    @Override
    public String getType() {
        return EnergyChoiceStrategyType.GREEN.getLabel();
    }

}
