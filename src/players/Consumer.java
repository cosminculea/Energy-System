package players;

import contract.Contract;
import input.ConsumerInput;


public final class Consumer implements Player {
    private final int id;
    private int budget;
    private final int monthlyIncome;
    private Contract contract;

    public Consumer(final ConsumerInput consumerInput) {
        this.id = consumerInput.getId();
        this.budget = consumerInput.getInitialBudget();
        this.monthlyIncome = consumerInput.getMonthlyIncome();
    }

    @Override
    public void signContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public boolean isBankrupt() {
        return budget < contract.getPrice();
    }

    @Override
    public void payDebts() {
        if (budget >= contract.getPrice()) {
            budget = budget - contract.getPrice();
            contract.decreaseMonths();
        }
    }

    @Override
    public void receiveMoney() {
        budget = budget + monthlyIncome;
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

}
