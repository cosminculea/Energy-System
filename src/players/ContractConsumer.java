package players;

public final class ContractConsumer implements Contract {
    private Player distributor;
    private int price;
    private int remainedContractMonths;

    public ContractConsumer (Player distributor) {
        this.distributor = distributor;
        this.price = ((Distributor) distributor).getCurrentPriceContract();
        this.remainedContractMonths = ((Distributor) distributor).getContractLength();
    }

    public Player getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public void setRemainedContractMonths(int remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }
}
