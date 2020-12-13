package input;

public final class CostsChangesInput {

    /**
     * id of the distributor who needs to change his costs
     */

    private int id;

    /**
     * the new infrastructure cost of the distributor
     */

    private int infrastructureCost;

    /**
     * the new production cost of the distributor
     */

    private int productionCost;

    /**
     * constructor which initialise the fields with the inputs given as strings, transforming them
     * to integers
     */

    public CostsChangesInput(final String id,
                             final String infrastructureCost,
                             final String productionCost) {
        this.id = Integer.parseInt(id);
        this.infrastructureCost = Integer.parseInt(infrastructureCost);
        this.productionCost = Integer.parseInt(productionCost);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(final int productionCost) {
        this.productionCost = productionCost;
    }


}
