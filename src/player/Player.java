package player;

import contract.Contract;

public interface Player {

    /**
     * @return true or false depending on the player's state, whether he is bankrupt (not in the
     * game anymore) or he is not bankrupt (still in the game)
     */

    boolean isBankrupt();

    /**
     * verify if the player is on the verge of being bankrupt and if he is, sets isBankrupt as true
     */

    void verifyBankruptcy();

    /**
     * add (replace) the contract of the player
     * @param contract the contract between two players
     */

    void signContract(Contract contract);

    /**
     * pay the debts the player has
     */

    void payDebts();

    /**
     * receive a sum to the budget
     * @param sum sum to be added to the player's budget
     */

    void receiveMoney(int sum);

    /**
     * @return true if the player has at least one contract signed, and false otherwise
     */

    boolean hasContract();

    /**
     * remove all contracts the player has (this can happen when the player is bankrupt)
     */

    void terminateContracts();

    /**
     * @return the player's id
     */

    int getId();

    /**
     * @return the player's budget
     */

    int getBudget();
}
