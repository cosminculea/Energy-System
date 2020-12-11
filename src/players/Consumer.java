package players;

import contract.Contract;
import input.ConsumerInput;


public final class Consumer implements Player {
    private final int id;
    private int budget;
    private final int monthlyIncome;
    private Contract contract;
    boolean hasOverduePayments;
    boolean isBankrupt;

    public Consumer(final ConsumerInput consumerInput) {
        this.id = consumerInput.getId();
        this.budget = consumerInput.getInitialBudget();
        this.monthlyIncome = consumerInput.getMonthlyIncome();
        hasOverduePayments = false;
        isBankrupt = false;
    }

    @Override
    public void signContract(Contract contract) {
        hasOverduePayments = false;
        this.contract = contract;
    }


    @Override
    public void verifyBankruptcy() {
        if (!isBankrupt) {
            if (hasOverduePayments &&
                    budget < (int) Math.round(Math.floor(2.2 * contract.getPrice()))) {
                isBankrupt = true;
            }
        }
    }

    @Override
    public boolean isBankrupt() {
        return isBankrupt;
    }

    @Override
    public void payDebts() {
        if (budget >= contract.getPrice()) {
            budget = budget - contract.getPrice();
            contract.getCounterpart().receiveMoney(contract.getPrice());
            contract.decreaseMonths();
        } else {
            if (!hasOverduePayments) {
                hasOverduePayments = true;
            } else {
                budget = budget - (int) Math.round(Math.floor(2.2 * contract.getPrice()));
                contract.getCounterpart()
                        .receiveMoney((int) Math.round(Math.floor(2.2 * contract.getPrice())));
                contract.decreaseMonths();
                hasOverduePayments = false;
            }
        }
    }

    @Override
    public void receiveMoney(final int sum) {
        budget = budget + monthlyIncome;
    }

    public int getId() {
        return id;
    }

    public int getBudget() {
        return budget;
    }

    public boolean hasContract() {
        return contract != null;
    }

    @Override
    public void closeContracts() {
        Distributor counterpart = (Distributor) contract.getCounterpart();
        counterpart.getContracts().remove(id);

        if (isBankrupt) {
            counterpart.setBudget(counterpart.getBudget() - counterpart.getProductionCost());
        }

        contract = null;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public Contract getContract() {
        return contract;
    }
}
