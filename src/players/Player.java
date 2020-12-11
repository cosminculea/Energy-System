package players;

import contract.Contract;

public interface Player {
    boolean isBankrupt();
    void signContract(Contract contract);
    int getBudget();
    void payDebts();
    void receiveMoney(final int sum);
    int getId();
    boolean hasContract();
    void closeContracts();
}
