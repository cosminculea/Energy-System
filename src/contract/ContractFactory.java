package contract;

import constants.Constants;
import entities.player.Player;

public final class ContractFactory {

    /**
     * private constructor (utility class)
     */

    private ContractFactory() { }

    /**
     * @param distributor the distributor who offers the energy to the consumer
     * @param consumer the consumer who requires energy from the distributor
     * @param type the type of the contract (for consumer or distributor)
     * @return ContractConsumer or ContractDistributor, depending on the type given by parameter,
     * or null otherwise
     */

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
