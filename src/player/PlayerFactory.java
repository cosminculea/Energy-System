package player;

import constants.Constants;
import input.ConsumerInput;
import input.DistributorInput;
import input.ProducerInput;

public final class PlayerFactory {

    /**
     * private constructor (utility class)
     */

    private static final PlayerFactory FACTORY = new PlayerFactory();

    private PlayerFactory() { }

    /**
     * static method used as a factory for players
     * @param entityInput object for the constructor (ConsumerInput or DistributorInput)
     * @param type the player's type (consumer or distributor)
     * @return new object depending on the type given as a parameter
     */

    public Player getPlayer(final Object entityInput, final String type) {
        if (type.equals(Constants.CONSUMER)) {
            ConsumerInput consumerInput = (ConsumerInput) entityInput;
            return new Consumer(consumerInput);
        }

        if (type.equals(Constants.DISTRIBUTOR)) {
            DistributorInput distributorInput = (DistributorInput) entityInput;
            return new Distributor(distributorInput);
         }

        if (type.equals(Constants.PRODUCER)) {
            ProducerInput producerInput = (ProducerInput) entityInput;
            return new Producer(producerInput);
        }

        return null;
    }

    public static PlayerFactory getInstance() {
        return FACTORY;
    }
}
