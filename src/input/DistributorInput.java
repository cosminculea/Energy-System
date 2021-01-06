package input;

public final class DistributorInput {

    /**
     * the distributor's id
     */

    private int id;

    /**
     * the length of every contract
     */

    private int contractLength;

    /**
     * the initial budget given by the input
     */

    private int initialBudget;

    /**
     * the initial infrastructure cost of the distributor given by the input
     */

    private int initialInfrastructureCost;

    /**
     * the energy needed by the distributor from the producers in KW
     */

    private int energyNeededKW;

    /**
     * the strategy after which the distributor chooses his producers
     */

    private String producerStrategy;

    /**
     * constructor which initialise the fields with the inputs given as strings, transforming them
     * to integers
     */

    public DistributorInput(final String id,
                            final String contractLength,
                            final String initialBudget,
                            final String initialInfrastructureCost,
                            final String energyNeededKW,
                            final String producerStrategy) {
        this.id = Integer.parseInt(id);
        this.contractLength = Integer.parseInt(contractLength);
        this.initialBudget = Integer.parseInt(initialBudget);
        this.initialInfrastructureCost = Integer.parseInt(initialInfrastructureCost);
        this.energyNeededKW = Integer.parseInt(energyNeededKW);
        this.producerStrategy = producerStrategy;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final int initialBudget) {
        this.initialBudget = initialBudget;
    }

    public int getInitialInfrastructureCost() {
        return initialInfrastructureCost;
    }

    public void setInitialInfrastructureCost(final int initialInfrastructureCost) {
        this.initialInfrastructureCost = initialInfrastructureCost;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(int energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(String producerStrategy) {
        this.producerStrategy = producerStrategy;
    }
}
