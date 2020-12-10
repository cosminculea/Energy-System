package simulation;

import constants.Constants;
import contract.Contract;
import contract.ContractFactory;
import input.ActionInput;
import input.ConsumerInput;
import input.DistributorInput;
import input.EntitiesInput;
import players.Distributor;
import players.Player;
import players.PlayerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {

    private List<ActionInput> rounds = new ArrayList<>();
    private ArrayList<Player> distributors = new ArrayList<>();
    private ArrayList<Player> consumers = new ArrayList<>();

    private static Simulation simulation = new Simulation();

    private Simulation() { }

    public void play() {
        ArrayList<Player> distributorsInGame = new ArrayList<>(distributors);
        ArrayList<Player> consumersInGame = new ArrayList<>(consumers);

        for (ActionInput round : rounds) {

        }
    }

    public void setRounds(final List<ActionInput> rounds) {
        this.rounds = rounds;
    }

    public void setPlayers(final EntitiesInput initialData) {
        List<ConsumerInput> consumersInput = initialData.getConsumers();
        List<DistributorInput> distributorsInput = initialData.getDistributors();

        for (DistributorInput distributorInput : distributorsInput) {
            distributors.add(PlayerFactory.getPlayer(distributorInput, Constants.DISTRIBUTOR));
        }

        for (ConsumerInput consumerInput : consumersInput) {
            consumers.add(PlayerFactory.getPlayer(consumerInput, Constants.CONSUMER));
        }

        Player cheapestDistributor= findCheapestDistributor();

        for (Player consumer : consumers) {
            consumer.receiveMoney();

            Contract contractForConsumer = ContractFactory.getContract(cheapestDistributor,
                                                    consumer, Constants.CONSUMER);
            Contract contractForDistributor = ContractFactory.getContract(cheapestDistributor,
                                                    consumer, Constants.DISTRIBUTOR);
            consumer.signContract(contractForConsumer);
            cheapestDistributor.signContract(contractForDistributor);

            cheapestDistributor.receiveMoney();
        }

        cheapestDistributor.payDebts();
    }



    public Player findCheapestDistributor() {
        Distributor cheapestDistributor = (Distributor) distributors.get(0);

        for (Player player : distributors) {
            Distributor distributor = (Distributor) player;

            if (distributor.getCurrentPriceContract() <
                                            cheapestDistributor.getCurrentPriceContract()) {
                cheapestDistributor = distributor;
            }
        }

        return cheapestDistributor;
    }

    public static Simulation getSimulation() {
        return simulation;
    }

    public ArrayList<Player> getDistributors() {
        return distributors;
    }

    public ArrayList<Player> getConsumers() {
        return consumers;
    }
}
