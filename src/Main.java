
import input.Input;
import input.InputLoader;
import input.Writer;
import simulation.Simulation;


public final class Main {

    private Main() { }

    /**
     * Main method which loads all the data from the input, sets all the rounds for the simulation
     * game represented by the monthly updates, sets all players for the rounds (meaning that the
     * round 0 is done during this set), and the let the simulation play. All data which needs to be
     * written is offered by the simulation. In the end, when the writing is proceeded all data in
     * simulation is erased.
     * @param args first argument is the name of the input file and the second argument is the name
     *             of the output file
     * @throws Exception if the opening of the input file failed
     */

    public static void main(final String[] args) throws Exception {

        String file = args[0];
        InputLoader inputLoader = new InputLoader(file);
        Input input = inputLoader.loadData();

        Simulation simulation = Simulation.getSimulation();

        simulation.setRounds(input.getMonthlyUpdates());
        simulation.setPlayers(input.getInitialData());
        simulation.play();

        Writer writer = new Writer(args[1]);
        writer.writeInFile(simulation.getDistributors(), simulation.getConsumers());

        simulation.clearGame();

    }
}
