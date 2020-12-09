package players;

import input.DistributorInput;

public final class Distributor implements Player {
    private int id;
    private int contractLength;
    private int budget;
    private int infrastructureCost;
    private int productionCost;
    private int currentPriceContract;
    private Contract contract;

    public Distributor(DistributorInput distributorInput) {
        this.id = distributorInput.getId();
        this.contractLength = distributorInput.getContractLength();
        this.budget = distributorInput.getInitialBudget();
        this.infrastructureCost = distributorInput.getInitialInfrastructureCost();
        this.productionCost =  distributorInput.getInitialProductionCost();
    }

    @Override
    public boolean isBankrupt() {
        return budget < 0;
    }

    public void signContract(Contract contract) {
        this.contract = contract;
    }

    public void setInfrastructureCost(int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public void setProductionCost(int productionCost) {
        this.productionCost = productionCost;
    }

    public int getId() {
        return id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public int getBudget() {
        return budget;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public int getCurrentPriceContract() {
        return currentPriceContract;
    }
}
