
import input.Input;
import input.InputLoader;
import input.Writer;
import simulation.Simulation;


public class Main {

    public static void main(String[] args) throws Exception {

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
