package entities;

import constants.Constants;
import entities.player.Consumer;
import entities.player.Distributor;
import input.ConsumerInput;
import input.DistributorInput;
import entities.player.Player;

public final class EntityFactory {

    /**
     * private constructor (utility class)
     */

    private EntityFactory() { }

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
