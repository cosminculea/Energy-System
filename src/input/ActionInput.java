package input;

import java.util.ArrayList;
import java.util.List;

public final class ActionInput {

    /**
     * list of new consumers in a month
     */

    private List<ConsumerInput> newConsumers;

    /**
     * list of all distributors changes in a month
     */

    private List<DistributorChange> distributorsChanges;

    /**
     * list of all producers changes in a month
     */

    private List<ProducerChange> producersChanges;

    /**
     * constructor which initialise the lists
     */

    public ActionInput() {
        newConsumers = new ArrayList<>();
        distributorsChanges = new ArrayList<>();
        producersChanges = new ArrayList<>();
    }

    public List<ConsumerInput> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<ConsumerInput> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<DistributorChange> getDistributorsChanges() {
        return distributorsChanges;
    }

    public void setDistributorsChanges(List<DistributorChange> distributorsChanges) {
        this.distributorsChanges = distributorsChanges;
    }

    public List<ProducerChange> getProducersChanges() {
        return producersChanges;
    }

    public void setProducersChanges(List<ProducerChange> producersChanges) {
        this.producersChanges = producersChanges;
    }
}
