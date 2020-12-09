package players;

import input.ConsumerInput;


public final class Consumer implements Player {
    private int id;
    private int budget;
    private int monthlyIncome;
    private Contract contract;

    public Consumer(ConsumerInput consumerInput) {
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
        if (budget < 0) {
            return true;
        }

        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBudget() {
        return budget;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

}
