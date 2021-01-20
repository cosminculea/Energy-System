package input;

public final class DistributorChange {

    /**
     * the id of the distributor who needs to change his infrastructure cost
     */

    private int id;

    /**
     * the new infrastructure cost of the distributor
     */

    private int infrastructureCost;

    /**
     * constructor of the distributor change with Strings from the input
     */

    public DistributorChange(final String id, final String infrastructureCost) {
        this.id = Integer.parseInt(id);
        this.infrastructureCost = Integer.parseInt(infrastructureCost);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }
}
