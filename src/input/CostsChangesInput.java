package input;

public final class CostsChangesInput {
    private int id;
    private int infrastructureCost;
    private int productionCost;

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

    public void setId(int id) {
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
