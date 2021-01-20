package player;

import contract.Contract;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import input.DistributorInput;

import observer.Observer;
import strategies.Strategy;


public final class Distributor implements ActivePlayer, Player, Observer {
    private static final double PROFIT_PERCENTAGE = 0.2;
    private static final int FACTOR = 10;

    /**
     * distributor's id
     */

    private final int id;

    /**
     * the number of months the contract with this distributor is available
     */

    private final int contractLength;

    /**
     * the budget of the distributor
     */

    private int budget;

    /**
     * the infrastructure cost which needs to be paid at the end of every month
     */

    private int infrastructureCost;

    /**
     * the production cost for every consumer which needs to be paid at the end of every month
     */

    private int productionCost;

    /**
     * the energy needed by the distributor from the producers in KW
     */

    private final int energyNeededKW;


    /**
     * the strategy after which the distributor chooses his producers
     */

    private Strategy producerStrategy;


    /**
     * current price of all contract signed on the current month
     */

    private int currentPriceContract;

    /**
     * all the contracts the distributor has signed, mapped by the input fo the consumer he has a
     * contract with
     */

    private final Map<Integer, Contract> contracts;

    /**
     * keep track of the distributor state as an active player of the game
     */

    private boolean isBankrupt = false;

    /**
     * list of all producers which gives energy to the distributor in the current month
     */

    private final List<Producer> currentProducers = new ArrayList<>();

    /**
     * flag which keeps track of the recalculation of the production cost
     */

    private boolean needToModify = false;

    /**
     * constructor which initialise the distributor's fields with data from the input(initial state)
     * @param distributorInput data from the input
     */


    public Distributor(final DistributorInput distributorInput) {
        id = distributorInput.getId();
        contractLength = distributorInput.getContractLength();
        budget = distributorInput.getInitialBudget();
        infrastructureCost = distributorInput.getInitialInfrastructureCost();
        energyNeededKW = distributorInput.getEnergyNeededKW();
        contracts = new LinkedHashMap<>();
    }

    /**
     * - applies the strategy this distributor has and chooses the minimum producers he needs to
     * cover the energyNeededKW
     */

    private void chooseProducers() {
        List<Producer> producers = producerStrategy.applyStrategy();
        int energySum = 0;

        for (Producer producer : producers) {
            if (producer.getNumberOfDistributors() != producer.getMaxDistributors()) {
                energySum += producer.getEnergyPerDistributor();
                producer.updateMonthlyDistributors(this);
                currentProducers.add(producer);
            }

            if (energySum > energyNeededKW) {
                break;
            }
        }
    }

    /**
     * - the distributor chooses their producers and production cost is calculated
     */

    public void calculateProductionCost() {
        chooseProducers();
        productionCost = 0;

        for (Producer producer : currentProducers) {
            productionCost += producer.getEnergyPerDistributor() * producer.getPriceKW();
        }

        productionCost = (int) Math.round(Math.floor((1.0 * productionCost / FACTOR)));
    }

    /**
     * - the price for the contract is calculated every month, according to the infrastructure and
     * production costs updated
     * - the price is calculated differently depending on the number of contracts the distributor
     * has
     */

    public void calculateNewContract() {

        int profit = (int) Math.round(Math.floor(PROFIT_PERCENTAGE * productionCost));

        if (hasContract()) {
            currentPriceContract =
                    (int) (Math.round(Math.floor((float) infrastructureCost / contracts.size())
                            + productionCost + profit));
        } else {
            currentPriceContract =  infrastructureCost + productionCost + profit;
        }
    }

    /**
     * @return the distributor's state
     */

    @Override
    public boolean isBankrupt() {
        return isBankrupt;
    }

    /**
     * sets isBankrupt true if the distributor does not have enough money to pay all the cost in the
     * current month
     */

    @Override
    public void verifyBankruptcy() {
        if (!isBankrupt) {
            if (budget < infrastructureCost + productionCost * contracts.size())  {
                isBankrupt = true;
            }
        }
    }

    /**
     * adds the contract to the map of contracts with the counterpart's (consumer's) id
     * @param contract the contract between this distributor and a consumer
     */

    @Override
    public void signContract(final Contract contract) {
        contracts.put(contract.getCounterpart().getId(), contract);
    }

    /**
     * - the budget is reduced by the infrastructure cost and the production cost for every contract
     * (consumer) he has
     * - the payments indicates the end of the month for all of the contracts, so for every contract
     * the number of months is decremented
     */

    @Override
    public void payDebts() {
        budget = budget - infrastructureCost - productionCost * contracts.size();

        for (Contract contract : contracts.values()) {
            contract.decreaseMonths();
        }
    }

    /**
     * - the distributor receives a certain sum from every consumer he has contract with, depending
     * on the price set in the contract
     * @param sum sum to be added to the distributor's budget
     */

    @Override
    public void receiveMoney(final int sum) {
        budget = budget + sum;
    }

    /**
     * @return ture if he has at least one contract signed with other consumers and false otherwise
     */

    @Override
    public boolean hasContract() {
        return contracts.size() != 0;
    }

    /**
     * the method removes all contracts the distributor has by clearing the map and also by removing
     * them from every consumer
     */

    @Override
    public void terminateContracts() {

        for (Contract contract : contracts.values()) {
            contract.getCounterpart().terminateContracts();
        }

        contracts.clear();
    }

    /**
     * sets the neeToModify variable as true which means that the current distributor must find
     * another producer
     */

    @Override
    public void update() {
        needToModify = true;
    }

    /**
     * removes this distributor from every producer's list
     */

    public void removeFromOldProducers() {
        for (Producer producer : currentProducers) {
            producer.removeObserver(this);
        }
    }

    /**
     * removes all producers this distributor has
     */

    public void resetCurrentProducers() {
        currentProducers.clear();
    }

    /**
     * @return the state of the distributor as far as the production cost is concerned
     */

    public boolean needToRecalculate() {
        return needToModify;
    }

    /**
     * resets distributor state as far as the production cost is concerned
     */

    public void resetNeedToRecalculate() {
        needToModify = false;
    }

    public int getBudget() {
        return budget;
    }

    public int getId() {
        return id;
    }


    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public void setProductionCost(final int productionCost) {
        this.productionCost = productionCost;
    }

    public void setProducerStrategy(Strategy producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public Strategy getProducerStrategy() {
        return producerStrategy;
    }

    public Map<Integer, Contract> getContracts() {
        return contracts;
    }

    public int getContractLength() {
        return contractLength;
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

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public List<Producer> getCurrentProducers() {
        return currentProducers;
    }
}
