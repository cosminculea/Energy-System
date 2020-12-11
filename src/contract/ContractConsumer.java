package contract;

import players.Distributor;
import players.Player;

public final class ContractConsumer implements Contract {

    /**
     * the current distributor of the consumer
     */

    private final Player distributor;

    /**
     * the price of the contract (how much the consumer must give to the distributor)
     */

    private final int price;

    /**
     * the numbers of month remained until the end of the contract
     */

    private int remainedContractMonths;

    /**
     * constructor based on the distributor requirements
     * @param distributor the counterpart
     */

    public ContractConsumer(final Player distributor) {
        this.distributor = distributor;
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
        return distributor;
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
