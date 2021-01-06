package player;

import constants.Constants;
import input.ConsumerInput;
import input.DistributorInput;

public final class PlayerFactory {

    /**
     * private constructor (utility class)
     */

    private PlayerFactory() { }

    /**
     * static method used as a factory for players
     * @param playerInput object for the constructor (ConsumerInput or DistributorInput)
     * @param type the player's type (consumer or distributor)
     * @return new object depending on the type given as a parameter
     */

    public static Player getPlayer(final Object playerInput, final String type) {
        if (type.equals(Constants.CONSUMER)) {
            ConsumerInput consumerInput = (ConsumerInput) playerInput;
            return new Consumer(consumerInput);
        }

        if (type.equals(Constants.DISTRIBUTOR)) {
            DistributorInput distributorInput = (DistributorInput) playerInput;
            return new Distributor(distributorInput);
         }

        return null;
    }
}
