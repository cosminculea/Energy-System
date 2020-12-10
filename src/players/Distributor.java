package players;

import contract.Contract;
import input.DistributorInput;

import java.util.ArrayList;

public final class Distributor implements Player {
    private int id;
    private int contractLength;
    private int budget;
    private int infrastructureCost;
    private int productionCost;
    private int currentPriceContract;
    private ArrayList<Contract> contracts;

    public Distributor(DistributorInput distributorInput) {
        this.id = distributorInput.getId();
        this.contractLength = distributorInput.getContractLength();
        this.budget = distributorInput.getInitialBudget();
        this.infrastructureCost = distributorInput.getInitialInfrastructureCost();
        this.productionCost =  distributorInput.getInitialProductionCost();
        contracts = new ArrayList<>();

        int profit = (int) Math.round(Math.floor(0.2 * productionCost));
        currentPriceContract =  infrastructureCost + productionCost + profit;
    }

    @Override
    public boolean isBankrupt() {
        return budget < infrastructureCost + productionCost * contracts.size();
    }

    @Override
    public void signContract(Contract contract) {
        contracts.add(contract);
    }

    @Override
    public void payDebts() {
        budget = budget - infrastructureCost - productionCost * contracts.size();
    }

    @Override
    public void receiveMoney() {
        for (Contract contract : contracts) {
            if (!contract.getCounterpart().isBankrupt()) {
                budget = budget + contract.getPrice();
            }
        }
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

    public ArrayList<Contract> getContracts() {
        return contracts;
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
