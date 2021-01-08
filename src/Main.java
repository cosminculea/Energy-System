
import input.Input;
import input.InputLoader;
import output.Writer;
import simulation.Simulation;

import java.io.IOException;


public final class Main {

    private Main() { }

    /**
     * Main method which loads all the data from the input, sets all the rounds for the simulation
     * game represented by the monthly updates, sets all players for the rounds , and the let the
     * simulation play. All data which needs to be written is offered by the simulation. In the end,
     * when the writing is proceeded all data in simulation is erased.
     * @param args first argument is the name of the input file and the second argument is the name
     *             of the output file
     * @throws IOException if the opening of the input/output files failed
     */

    public static void main(final String[] args) throws IOException {

        String file = args[0];
        InputLoader inputLoader = new InputLoader(file);
        Input input = inputLoader.loadData();

        Simulation simulation = Simulation.getSimulation();

        simulation.setRounds(input.getMonthlyUpdates());
        simulation.setPlayers(input.getInitialData(), input.getMonthlyUpdates().size());
        simulation.play();

        Writer writer = new Writer(args[1]);
        writer.writeInFile(simulation.getDistributors(),
                simulation.getConsumers(),
                simulation.getProducers());

        simulation.clearGame();
    }
}
