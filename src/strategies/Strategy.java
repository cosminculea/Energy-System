package strategies;

import player.Producer;

import java.util.List;

public interface Strategy {

    /**
     * apply the strategy depending on the type by sorting the producers
     * @return list of producers sorted
     */

    List<Producer> applyStrategy();

    /**
     * @return the type of strategy
     */

    String getType();
}
