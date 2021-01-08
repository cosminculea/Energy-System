package entities.player;

import contract.Contract;
import entities.Player;
import entities.Producer;
import input.DistributorInput;

import java.util.*;

import strategies.Strategy;

public final class Distributor implements ActivePlayer, Player, Observer {
    public static final double PROFIT_PERCENTAGE = 0.2;

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

    private List<Producer> currentProducers = new ArrayList<>();

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
     *
     */

    public void calculateProductionCost() {

        List<Producer> producers = producerStrategy.applyStrategy();
        productionCost = 0;
        int energySum = 0;

        for (Producer producer : producers) {

            if (producer.getCurrentDistributors().size() == producer.getMaxDistributors()) {
                continue;
            }

            energySum += producer.getEnergyPerDistributor();
            productionCost += producer.getEnergyPerDistributor() * producer.getPriceKW();

            producer.updateMonthlyDistributors(this);
            currentProducers.add(producer);

            if (energySum > energyNeededKW) {
                break;
            }
        }

       productionCost = (int) Math.round(Math.floor((1.0 * productionCost / 10)));
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

        //calculateProductionCost();
        //int profit = (int) Math.round(Math.floor(PROFIT_PERCENTAGE * productionCost));
        //currentPriceContract =  infrastructureCost + productionCost + profit;
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

    @Override
    public void update(Observable producerObserved, Object arg) {
        needToModify = (boolean) arg;

//        for (Producer producer : currentProducers) {
//            if(!producer.equals(producerObserved)) {
//                producer.getCurrentDistributors().remove(this);
//            }
//        }

        //currentProducers.clear();
    }

    public void resetCurrentProducers() {
        currentProducers.clear();
    }

    public boolean isNeedToModify() {
        return needToModify;
    }

    public void resetNeedToModify() {
        needToModify = false;
    }

    public List<Producer> getCurrentProducers() {
        return currentProducers;
    }
}
