package input;

import java.util.ArrayList;
import java.util.List;

public final class Input {

    /**
     * the number of turns given by the input
     */

    private int numberOfTurns;

    /**
     * all entities in the game
     */

    private EntitiesInput initialData;

    /**
     * list of all monthly updates
     */

    private List<ActionInput> monthlyUpdates;

    public Input() {
        initialData = new EntitiesInput();
        monthlyUpdates = new ArrayList<>();
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public EntitiesInput getInitialData() {
        return initialData;
    }

    public void setInitialData(final EntitiesInput initialData) {
        this.initialData = initialData;
    }

    public List<ActionInput> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final List<ActionInput> monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }


}
