package contract;

import player.Player;

public interface Contract {

    /**
     * @return true or false whether the contract has expired or not
     */

    boolean contractExpired();

    /**
     * mark the end of one month in the contract
     */

    void decreaseMonths();

    /**
     * @return the counterpart of the contract
     */

    Player getCounterpart();

    /**
     * @return the price set in the contract
     */

    int getPrice();


    /**
     * @return the number of months remained until the expiration of the contract
     *
     */

    int getRemainedContractMonths();
}
