package entities;

import input.ProducerInput;

public class Producer implements Entity {

    /**
     * the id of the producer
     */

    final int id;

    /**
     * the type of energy the producer generates
     */

    final String energyType;

    /**
     * the maximum number of distributors a producer can have
     */

    final int maxDistributors;

    /**
     * the price of the the energy per KW
     */

    final double priceKW;

    /**
     * how much energy measured in KW a producer can give to one distributor
     */

    final int energyPerDistributor;

    public Producer(ProducerInput producerInput) {
        id = producerInput.getId();
        energyType = producerInput.getEnergyType();
        maxDistributors = producerInput.getMaxDistributors();
        priceKW = producerInput.getPriceKW();
        energyPerDistributor = producerInput.getEnergyPerDistributor();
    }


    @Override
    public int getId() {
        return 0;
    }
}
