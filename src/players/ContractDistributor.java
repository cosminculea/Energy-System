package players;

public final class ContractDistributor implements Contract {
    private Player consumer;
    private int price;
    private int remainedContractMonths;

    public ContractDistributor(Player consumer, Player distributor) {
        this.consumer = consumer;
        this.price = ((Distributor) distributor).getCurrentPriceContract();
        this.remainedContractMonths = ((Distributor) distributor).getContractLength();
    }

    public Player getConsumer() {
        return consumer;
    }

    public int getPrice() {
        return price;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }
}
