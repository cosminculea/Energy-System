package input;

public final class ProducerInput {

    /**
     * the id of the producer
     */

    private final int id;

    /**
     * the type of energy the producer generates
     */

    private final String energyType;

    /**
     * the maximum number of distributors a producer can have
     */

    private final int maxDistributors;

    /**
     * the price of the the energy per KW
     */

    private final double priceKW;

    /**
     * how much energy measured in KW a producer can give to one distributor
     */

    private final int energyPerDistributor;

    public ProducerInput(final String id,
                         final String energyType,
                         final String maxDistributors,
                         final String priceKW,
                         final String energyPerDistributor) {
        this.id = Integer.parseInt(id);
        this.energyType = energyType;
        this.maxDistributors = Integer.parseInt(maxDistributors);
        this.priceKW = Double.parseDouble(priceKW);
        this.energyPerDistributor = Integer.parseInt(energyPerDistributor);
    }

    public int getId() {
        return id;
    }

    public String getEnergyType() {
        return energyType;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }
}
