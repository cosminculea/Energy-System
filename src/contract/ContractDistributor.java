package contract;

import players.Distributor;
import players.Player;

public final class ContractDistributor implements Contract {
    private final Player consumer;
    private final int price;
    private int remainedContractMonths;

    public ContractDistributor(final Player consumer, final Player distributor) {
        this.consumer = consumer;
        this.price = ((Distributor) distributor).getCurrentPriceContract();
        this.remainedContractMonths = ((Distributor) distributor).getContractLength();
    }

    public Player getCounterpart() {
        return consumer;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean contractExpired() {
        return remainedContractMonths == 0;
    }

    @Override
    public void decreaseMonths() {
        remainedContractMonths--;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }


}
