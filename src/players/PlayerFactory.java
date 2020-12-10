package players;

import constants.Constants;
import input.ConsumerInput;
import input.DistributorInput;

public class PlayerFactory {

    private PlayerFactory() { }

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
