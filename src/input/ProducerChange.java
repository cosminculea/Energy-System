package input;

public final class ProducerChange {

    /**
     * the id of the producer who needs to change his energy per distributor
     */

    private int id;

    /**
     * the new energy per distributor
     */

    private int energyPerDistributor;

    /**
     * constructor of the producer change with Strings from the input
     */

    public ProducerChange(final String id, final String energyPerDistributor) {
        this.id = Integer.parseInt(id);
        this.energyPerDistributor = Integer.parseInt(energyPerDistributor);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }
}
