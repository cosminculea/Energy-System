package input;

import java.util.ArrayList;
import java.util.List;

public final class EntitiesInput {
    private List<ConsumerInput> consumers;
    private List<DistributorInput> distributors;

    public EntitiesInput() {
        consumers = new ArrayList<>();
        distributors = new ArrayList<>();
    }

    public List<ConsumerInput> getConsumers() {
        return consumers;
    }

    public void setConsumers(final List<ConsumerInput> consumers) {
        this.consumers = consumers;
    }

    public List<DistributorInput> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<DistributorInput> distributors) {
        this.distributors = distributors;
    }


}
