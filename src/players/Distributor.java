package players;

import contract.Contract;
import input.DistributorInput;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Distributor implements Player {
    private final int id;
    private final int contractLength;
    private int budget;
    private int infrastructureCost;
    private int productionCost;
    private int currentPriceContract;
    private final Map<Integer, Contract> contracts;
    private boolean isBankrupt = false;

    public Distributor(DistributorInput distributorInput) {
        id = distributorInput.getId();
        contractLength = distributorInput.getContractLength();
        budget = distributorInput.getInitialBudget();
        infrastructureCost = distributorInput.getInitialInfrastructureCost();
        productionCost =  distributorInput.getInitialProductionCost();
        contracts = new LinkedHashMap<>();

        int profit = (int )Math.round(Math.floor(0.2 * productionCost));
        currentPriceContract =  infrastructureCost + productionCost + profit;
    }

    @Override
    public void verifyBankruptcy() {
        if (!isBankrupt) {
            if (budget < infrastructureCost + productionCost * contracts.size())  {
                isBankrupt = true;
            }
        }
    }

    @Override
    public boolean isBankrupt() {
        return isBankrupt;
    }

    @Override
    public void signContract(Contract contract) {
        contracts.put(contract.getCounterpart().getId(),contract);
    }

    @Override
    public void payDebts() {
        budget = budget - infrastructureCost - productionCost * contracts.size();

        for (Contract contract : contracts.values()) {
            contract.decreaseMonths();
        }
    }

    @Override
    public void receiveMoney(final int sum) {
       budget = budget + sum;
    }

    @Override
    public boolean hasContract() {
        return contracts.size() != 0;
    }

    public void calculateNewContract() {
        int profit = (int) Math.round(Math.floor(0.2 * productionCost));

        if (hasContract()) {
            currentPriceContract = (int) (Math.round(Math.floor(infrastructureCost / contracts.size())
                                            + productionCost + profit));
        } else {
            currentPriceContract =  infrastructureCost + productionCost + profit;
        }

    }

    @Override
    public void closeContracts() {
        budget = budget - infrastructureCost - productionCost * contracts.size();

        for (Contract contract : contracts.values()) {
            contract.getCounterpart().closeContracts();
        }

        contracts.clear();
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

    public Map<Integer, Contract> getContracts() {
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

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
