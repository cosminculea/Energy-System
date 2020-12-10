package contract;

import constants.Constants;
import players.Player;

public class ContractFactory {
    private ContractFactory() { }

    public static Contract getContract(final Player distributor,
                                     final Player consumer,
                                     final String type) {
        if (type.equals(Constants.CONSUMER)) {
            return new ContractConsumer(distributor);
        }

        if (type.equals(Constants.DISTRIBUTOR)) {
            return new ContractDistributor(consumer, distributor);
        }

        return null;
    }
}
