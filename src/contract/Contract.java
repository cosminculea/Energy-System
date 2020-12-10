package contract;

import players.Player;

public interface Contract {
    Player getCounterpart();
    int getPrice();
    boolean contractExpired();
    int getRemainedContractMonths();
    void decreaseMonths();
}
