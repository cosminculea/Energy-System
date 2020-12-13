package contract;

import players.Distributor;
import players.Player;

public final class ContractDistributor implements Contract {

    /**
     * the consumer whom the distributor gives energy to
     */

    private final Player consumer;

    /**
     * price of the contract
     */

    private final int price;

    /**
     * the numbers of month remained until the end of the contract
     */

    private int remainedContractMonths;

    /**
     * constructor based on the distributor requirements
     * @param distributor the possessor of the contract
     * @param consumer the counterpart
     */

    public ContractDistributor(final Player consumer, final Player distributor) {
        this.consumer = consumer;
        this.price = ((Distributor) distributor).getCurrentPriceContract();
        this.remainedContractMonths = ((Distributor) distributor).getContractLength();
    }

    @Override
    public boolean contractExpired() {
        return remainedContractMonths == 0;
    }

    @Override
    public void decreaseMonths() {
        remainedContractMonths--;
    }

    @Override
    public Player getCounterpart() {
        return consumer;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }
}
