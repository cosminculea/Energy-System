package input;

import java.util.ArrayList;
import java.util.List;

public final class ActionInput {

    /**
     * list of new consumers in a month
     */

    private List<ConsumerInput> newConsumers;

    /**
     * list of costs changes in a month
     */

    private List<CostsChangesInput> costsChanges;

    /**
     * constructor which initialise the lists
     */

    public ActionInput() {
        newConsumers = new ArrayList<>();
        costsChanges = new ArrayList<>();
    }

    public List<ConsumerInput> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<ConsumerInput> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<CostsChangesInput> getCostsChanges() {
        return costsChanges;
    }

    public void setCostsChanges(final List<CostsChangesInput> costsChanges) {
        this.costsChanges = costsChanges;
    }

}
