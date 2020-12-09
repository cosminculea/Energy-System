package input;

public final class DistributorInput {
    private int id;
    private int contractLength;
    private int initialBudget;
    private int initialInfrastructureCost;
    private int initialProductionCost;

    public DistributorInput(final String id,
                            final String contractLength,
                            final String initialBudget,
                            final String initialInfrastructureCost,
                            final String initialProductionCost) {
        this.id = Integer.parseInt(id);
        this.contractLength = Integer.parseInt(contractLength);
        this.initialBudget = Integer.parseInt(initialBudget);
        this.initialInfrastructureCost = Integer.parseInt(initialInfrastructureCost);
        this.initialProductionCost = Integer.parseInt(initialProductionCost);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getInitialProductionCost() {
        return initialProductionCost;
    }

    public void setInitialProductionCost(final int initialProductionCost) {
        this.initialProductionCost = initialProductionCost;
    }
}
