package input;

import java.util.ArrayList;
import java.util.List;

public final class ActionInput {
    private List<ConsumerInput> newConsumers;
    private List<CostsChangesInput> costsChanges;

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
