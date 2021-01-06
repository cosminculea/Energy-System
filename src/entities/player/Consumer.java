package entities.player;

import contract.Contract;
import input.ConsumerInput;


public final class Consumer implements Player {
    private static final double OVERDUE = 2.2;

    /**
     * the consumer's id
     */

    private final int id;

    /**
     * teh consumer's budget
     */

    private int budget;

    /**
     * the consumer monthly income which is added every month
     */

    private final int monthlyIncome;

    /**
     * keep track of the overdue payments of the consumer
     */

    private boolean hasOverduePayments;

    /**
     * keep track of the consumer state as an active player of the game
     */

    private boolean isBankrupt;

    /**
     * the current contract of the consumer
     */

    private Contract contract;

    /**
     * constructor which initialise the consumer's fields with data from the input (initial state)
     * @param consumerInput data from the input
     */

    public Consumer(final ConsumerInput consumerInput) {
        id = consumerInput.getId();
        budget = consumerInput.getInitialBudget();
        monthlyIncome = consumerInput.getMonthlyIncome();
        hasOverduePayments = false;
        isBankrupt = false;
    }

    /**
     * @return the consumer state
     */

    @Override
    public boolean isBankrupt() {
        return isBankrupt;
    }

    /**
     * sets isBankrupt true if the consumer has and overdue payments and he is not able to pay
     * all of them
     */

    @Override
    public void verifyBankruptcy() {
        if (!isBankrupt) {
            if (hasOverduePayments
                    && budget < (int) Math.round(Math.floor(OVERDUE * contract.getPrice()))) {
                isBankrupt = true;
            }
        }
    }

    /**
     * - sets hasOverduePayments in case the consumer had an overdue payment and the distributor
     * has become bankrupt or the contract has expired.
     * - replace the current contract with other contract
     */

    @Override
    public void signContract(final Contract newContract) {
        hasOverduePayments = false;
        contract = newContract;
    }

    /**
     * - if the consumer has overdue payments he pays all of them to his distributor (the budget is
     * reduced and the distributor receives money)
     * - otherwise it verifies if the consumer can pay the current payment and makes the transaction
     * between players, and if he cannot the hasOverduePayments is set true
     */

    @Override
    public void payDebts() {
        if (hasOverduePayments) {
            budget = budget - (int) Math.round(Math.floor(OVERDUE * contract.getPrice()));
            contract.getCounterpart()
                    .receiveMoney((int) Math.round(Math.floor(OVERDUE * contract.getPrice())));
            contract.decreaseMonths();
            hasOverduePayments = false;
        } else {
            if (budget >= contract.getPrice()) {
                budget = budget - contract.getPrice();
                contract.getCounterpart().receiveMoney(contract.getPrice());
                contract.decreaseMonths();
            } else {
                hasOverduePayments = true;
            }
        }
    }

    /**
     * add the salary to the budget
     * @param sum sum to be added to the player's budget (meaning its salary)
     */

    @Override
    public void receiveMoney(final int sum) {
        budget = budget + monthlyIncome;
    }

    /**
     * @return true if the contract is not null, and false otherwise
     */
    public boolean hasContract() {
        return contract != null;
    }

    /**
     * the method removes the contract from the customer and also from the distributor
     * - if this method is called because the customer is bankrupt then the distributor's budget
     * is reduced with the production cost for the energy supplied this month
     */

    @Override
    public void terminateContracts() {
        Distributor counterpart = (Distributor) contract.getCounterpart();
        counterpart.getContracts().remove(id);

        if (isBankrupt) {
            counterpart.setBudget(counterpart.getBudget() - counterpart.getProductionCost());
        }

        contract = null;
    }

    public int getId() {
        return id;
    }

    public int getBudget() {
        return budget;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public Contract getContract() {
        return contract;
    }
}
