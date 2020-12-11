package simulation;

import constants.Constants;
import contract.Contract;
import contract.ContractFactory;
import input.*;
import players.Consumer;
import players.Distributor;
import players.Player;
import players.PlayerFactory;

import java.util.*;

public class Simulation {

    private List<ActionInput> rounds;
    private final ArrayList<Player> distributors = new ArrayList<>();
    private final ArrayList<Player> consumers = new ArrayList<>();
    private Map<Integer, Player> distributorsInGame;
    private Map<Integer, Player> consumersInGame;

    private static final Simulation simulation = new Simulation();

    private Simulation() { }

    public void play() {
        distributorsInGame = new HashMap<>();
        consumersInGame = new HashMap<>();

        setPlayersInGame(distributorsInGame, consumersInGame);

        for (ActionInput round : rounds) {
            resolveNewConsumers(round.getNewConsumers());
            resolveCostsChanges(round.getCostsChanges());

            calculateNewContracts();
            receiveSalaries();
            findAndSignContracts();
            paymentDayConsumers();
            paymentDayDistributors();

            if (distributorsInGame.size() == 0) {
                break;
            }
        }
    }

    private void calculateNewContracts() {
        for (Integer id : distributorsInGame.keySet()) {
            Distributor distributorInGame = (Distributor) distributorsInGame.get(id);
            distributorInGame.calculateNewContract();
        }
    }

    private void receiveSalaries() {
        for (Integer id : consumersInGame.keySet()) {
            Consumer consumerInGame = (Consumer) consumersInGame.get(id);
            consumerInGame.receiveMoney(consumerInGame.getMonthlyIncome());
        }
    }

    private void findAndSignContracts() {
        Player cheapestDistributor= findCheapestDistributor();

        for (Player consumer : consumersInGame.values()) {
            if (!consumer.hasContract() ||
                    (consumer.hasContract() &&
                            ((Consumer) consumer).getContract().contractExpired())) {
                Contract contractForConsumer = ContractFactory.getContract(cheapestDistributor,
                        consumer, Constants.CONSUMER);
                Contract contractForDistributor = ContractFactory.getContract(cheapestDistributor,
                        consumer, Constants.DISTRIBUTOR);

                if (consumer.hasContract()) {
                    consumer.closeContracts();
                }

                consumer.signContract(contractForConsumer);
                cheapestDistributor.signContract(contractForDistributor);
            }
        }
    }

    private void paymentDayConsumers() {

        for (Integer consumerId : consumersInGame.keySet()) {
            Player consumer = consumersInGame.get(consumerId);
            consumer.verifyBankruptcy();

            if (consumer.isBankrupt()) {
                consumer.closeContracts();
            } else {
                consumer.payDebts();
            }
        }

        consumersInGame.values().removeIf(player -> !player.hasContract());
    }

    private void paymentDayDistributors() {

        for (Integer distributorId : distributorsInGame.keySet()) {
            Player distributor = distributorsInGame.get(distributorId);
            distributor.verifyBankruptcy();

            if (distributor.isBankrupt()) {
                distributor.closeContracts();
            } else {
                distributor.payDebts();
            }
        }

        distributorsInGame.values().removeIf(Player::isBankrupt);
    }

    private void resolveNewConsumers(final List<ConsumerInput> newConsumers) {
        for (ConsumerInput newConsumer : newConsumers) {
            Player consumerCreated = PlayerFactory.getPlayer(newConsumer, Constants.CONSUMER);
            consumers.add(consumerCreated);
            consumersInGame.put(consumerCreated.getId(), consumerCreated);
        }
    }

    private void resolveCostsChanges(final List<CostsChangesInput> costsChanges) {
        for (CostsChangesInput costChanges : costsChanges) {
            Player distributor = distributorsInGame.get(costChanges.getId());

            if (distributor == null) {
                continue;
            }

            ((Distributor) distributor).setInfrastructureCost(costChanges.getInfrastructureCost());
            ((Distributor) distributor).setProductionCost(costChanges.getProductionCost());
        }
    }

    private void setPlayersInGame(Map<Integer, Player> distributorsInGame,
                                  Map<Integer, Player> consumersInGame) {

        for (Player distributor : distributors) {
            distributor.verifyBankruptcy();

            if (!distributor.isBankrupt()) {
                distributorsInGame.put(distributor.getId(), distributor);
            }
        }

        for (Player consumer : consumers) {
            consumersInGame.put(consumer.getId(), consumer);
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

        Distributor cheapestDistributor = (Distributor) distributors.get(0);

        for (Player player : distributors) {
            Distributor distributor = (Distributor) player;

            if (!distributor.isBankrupt()) {
                if (distributor.getCurrentPriceContract() <
                        cheapestDistributor.getCurrentPriceContract()) {
                    cheapestDistributor = distributor;
                }
            }
        }

        for (Player consumer : consumers) {
            consumer.receiveMoney(((Consumer) consumer).getMonthlyIncome());

            Contract contractForConsumer = ContractFactory.getContract(cheapestDistributor,
                                                    consumer, Constants.CONSUMER);
            Contract contractForDistributor = ContractFactory.getContract(cheapestDistributor,
                                                    consumer, Constants.DISTRIBUTOR);
            consumer.signContract(contractForConsumer);
            cheapestDistributor.signContract(contractForDistributor);

            consumer.payDebts();
        }

        for (Player distributor : distributors) {
            distributor.verifyBankruptcy();
            distributor.payDebts();
        }

    }

    public Player findCheapestDistributor() {
        Iterator<Player> it = distributorsInGame.values().iterator();
        Distributor cheapestDistributor = (Distributor) it.next();

        while (it.hasNext()) {
            Distributor distributor = (Distributor) it.next();

            if (!distributor.isBankrupt()) {
                if (distributor.getCurrentPriceContract() <
                        cheapestDistributor.getCurrentPriceContract()) {
                    cheapestDistributor = distributor;
                }
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

    public void clearGame() {
        distributors.clear();
        consumers.clear();
    }
}
