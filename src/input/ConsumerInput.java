package input;

public final class ConsumerInput {

    /**
     * consumer's id
     */

    private int id;

    /**
     * initial budget of the consumer given in the input
     */

    private int initialBudget;

    /**
     * the monthly input of the income
     */

    private int monthlyIncome;

    /**
     * constructor which initialise the fields with the inputs given as strings, transforming them
     * to integers
     */

    public ConsumerInput(final String id, final String initialBudget, final String monthlyIncome) {
        this.id = Integer.parseInt(id);
        this.initialBudget = Integer.parseInt(initialBudget);
        this.monthlyIncome = Integer.parseInt(monthlyIncome);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final int initialBudget) {
        this.initialBudget = initialBudget;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
