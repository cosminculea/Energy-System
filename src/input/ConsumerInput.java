package input;

public final class ConsumerInput {
    private int id;
    private int initialBudget;
    private int monthlyIncome;

    public ConsumerInput(final String id, final String initialBudget, final String monthlyIncome) {
        this.id = Integer.parseInt(id);
        this.initialBudget = Integer.parseInt(initialBudget);
        this.monthlyIncome = Integer.parseInt(monthlyIncome);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(int initialBudget) {
        this.initialBudget = initialBudget;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

}
