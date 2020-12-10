package contract;

import players.Distributor;
import players.Player;

public final class ContractConsumer implements Contract {
    private final Player distributor;
    private int price;
    private int remainedContractMonths;

    public ContractConsumer (final Player distributor) {
        this.distributor = distributor;
        this.price = ((Distributor) distributor).getCurrentPriceContract();
        this.remainedContractMonths = ((Distributor) distributor).getContractLength();
    }

    public Player getCounterpart() {
        return distributor;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean contractExpired() {
        return remainedContractMonths == 0;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public void decreaseMonths() {
        remainedContractMonths--;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
