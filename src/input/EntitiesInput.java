package input;

import java.util.ArrayList;
import java.util.List;

public final class EntitiesInput {

    /**
     * list of all consumers given by the input
     */

    private List<ConsumerInput> consumers;

    /**
     * list of all distributors given by the input
     */

    private List<DistributorInput> distributors;

    /**
     * constructor which initialise the lists
     */

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
