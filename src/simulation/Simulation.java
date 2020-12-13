package simulation;

import constants.Constants;
import contract.Contract;
import contract.ContractFactory;
import input.ActionInput;
import input.ConsumerInput;
import input.CostsChangesInput;
import input.EntitiesInput;
import players.Consumer;
import input.DistributorInput;
import players.Distributor;
import players.Player;
import java.util.Iterator;
import players.PlayerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Simulation {

    /**
     * list of rounds for the simulation given by the input represented by the changes of costs and
     * new consumers (list of months)
     */

    private List<ActionInput> rounds;

    /**
     * list of distributors in the system
     */

    private final ArrayList<Player> distributors = new ArrayList<>();

    /**
     * list of consumers in the system
     */

    private final ArrayList<Player> consumers = new ArrayList<>();

    /**
     * map of distributors (with their ids as keys) in the game who play an active role in the
     * simulation at one give time
     */

    private Map<Integer, Player> distributorsInGame;

    /**
     * map of consumers (with their ids as keys) in the game who play an active role in the
     * simulation at one give time
     */

    private Map<Integer, Player> consumersInGame;

    /**
     * the only object of the class in order to maintain a singleton factory
     */

    private static final Simulation SIMULATION = new Simulation();

    /**
     * private constructor which guarantee the class has a unique object (singleton pattern)
     */

    private Simulation() { }

    /**
     * method which runs the simulation between all players by iterating through all rounds (months)
     * and making all the necessary changes:
     *      - sets all players in the game with the information provided by the input and establish
     *    the initial relationship between them (round 0)
     *      - for every round: the monthly changes are updated, the new contracts for every
     *     distributor are calculated, all consumers receive salaries, find and sign a convenient
     *     contract (the cheapest). Moreover, the players have to pay their debts and are eliminated
     *     from the game if they do not have enough money. The simulation ends when whether the
     *     rounds are over or there are no distributors left in the game
     */

    public void play() {
        distributorsInGame = new HashMap<>();
        consumersInGame = new HashMap<>();

        setPlayersInGame();

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

    /**
     * - this method represents the round 0 of the simulation in which all players are added to the
     * game in their specific list, then every consumer finds and sign their first contract with
     * the cheapest distributor and all of the players, both consumers and distributors pay
     * their debts
     * - distributors must be verified because they can be bankrupt from their first payment, unlike
     * the consumers who can delay it
     */

    private void setPlayersInGame() {

        for (Player distributor : distributors) {
            distributorsInGame.put(distributor.getId(), distributor);
        }

        for (Player consumer : consumers) {
            consumersInGame.put(consumer.getId(), consumer);
        }

        Distributor cheapestDistributor = (Distributor) findCheapestDistributor();

        for (Player consumer : consumersInGame.values()) {
            consumer.receiveMoney(((Consumer) consumer).getMonthlyIncome());

            Contract contractForConsumer = ContractFactory.getContract(cheapestDistributor,
                    consumer, Constants.CONSUMER);
            Contract contractForDistributor = ContractFactory.getContract(cheapestDistributor,
                    consumer, Constants.DISTRIBUTOR);
            consumer.signContract(contractForConsumer);
            cheapestDistributor.signContract(contractForDistributor);

            consumer.payDebts();
        }

        for (Player distributor : distributorsInGame.values()) {
            distributor.verifyBankruptcy();
            distributor.payDebts();
        }

        distributorsInGame.values().removeIf(Player::isBankrupt);
    }

    /**
     * iterates through all the distributors in the game and finds the first distributor who has the
     * lowest current price for the contract
     * @return the cheapest distributor in the current state of the game
     */

    public Player findCheapestDistributor() {
        Iterator<Player> it = distributorsInGame.values().iterator();
        Distributor cheapestDistributor = (Distributor) it.next();

        while (it.hasNext()) {
            Distributor distributor = (Distributor) it.next();

            if (!distributor.isBankrupt()) {
                if (distributor.getCurrentPriceContract()
                        < cheapestDistributor.getCurrentPriceContract()) {
                    cheapestDistributor = distributor;
                }
            }
        }

        return cheapestDistributor;
    }

    /**
     * adds all the new consumers in both the data base and game
     * @param newConsumers list of new consumers given by the input for a round
     */

    private void resolveNewConsumers(final List<ConsumerInput> newConsumers) {
        for (ConsumerInput newConsumer : newConsumers) {
            Player consumerCreated = PlayerFactory.getPlayer(newConsumer, Constants.CONSUMER);
            consumers.add(consumerCreated);
            consumersInGame.put(consumerCreated.getId(), consumerCreated);
        }
    }

    /**
     * - updates the infrastructure cost and production cost for the distributors given by the input
     * with their ids
     * @param costsChanges list of costs changes given by the input for a round
     */

    private void resolveCostsChanges(final List<CostsChangesInput> costsChanges) {
        for (CostsChangesInput costChanges : costsChanges) {
            Distributor distributor = (Distributor) distributorsInGame.get(costChanges.getId());

            if (distributor == null) {
                continue;
            }

            distributor.setInfrastructureCost(costChanges.getInfrastructureCost());
            distributor.setProductionCost(costChanges.getProductionCost());
        }
    }

    /**
     * for every distributor in the game the contracts prices are updated
     */

    private void calculateNewContracts() {
        for (Player distributor : distributorsInGame.values()) {
            Distributor distributorInGame = (Distributor) distributor;
            distributorInGame.calculateNewContract();
        }
    }

    /**
     * every consumer in the game receives his salary (the salary is added to the budget)
     */

    private void receiveSalaries() {
        for (Player consumer : consumersInGame.values()) {
            Consumer consumerInGame = (Consumer) consumer;
            consumerInGame.receiveMoney(consumerInGame.getMonthlyIncome());
        }
    }

    /**
     * - every round the consumers who do not have a contract or have a contract but it is expired
     * (remainedContractMonths == 0), sign a contract with the cheapest distributor in the
     * current month
     * - if the consumer has an expired contract, he has to close the old contract first
     */

    private void findAndSignContracts() {
        Player cheapestDistributor = findCheapestDistributor();

        for (Player consumer : consumersInGame.values()) {
            if (!consumer.hasContract()
                    || (consumer.hasContract()
                        && ((Consumer) consumer).getContract().contractExpired())) {

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

    /**
     * - this method marks the end of one round for consumers, in which they have to pay de debts
     * they have
     * - for every consumer it verifies if he is bankrupt, and if the response is true his
     * contract is closed and he is eliminated from the game
     * - otherwise he pays the debts
     */

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

    /**
     * - this method marks the end of one round for distributors, in which they have to pay the
     * debts they have
     * - for every distributor it verifies if he is bankrupt and then he pays his debts, regardless
     * of his state of bankruptcy
     * - if the distributor is bankrupt after the verification he closes all the contract he has
     * with all of the consumers and is eliminated from the game
     */

    private void paymentDayDistributors() {

        for (Integer distributorId : distributorsInGame.keySet()) {
            Player distributor = distributorsInGame.get(distributorId);
            distributor.verifyBankruptcy();
            distributor.payDebts();

            if (distributor.isBankrupt()) {
                distributor.closeContracts();
            }
        }

        distributorsInGame.values().removeIf(Player::isBankrupt);
    }

    /**
     * @param rounds monthly updates given by the input
     */

    public void setRounds(final List<ActionInput> rounds) {
        this.rounds = rounds;
    }

    /**
     * every player given by the input is added to the data base of the simulation
     * @param initialData consumers and distributors given by the input
     */

    public void setPlayers(final EntitiesInput initialData) {
        List<ConsumerInput> consumersInput = initialData.getConsumers();
        List<DistributorInput> distributorsInput = initialData.getDistributors();

        for (DistributorInput distributorInput : distributorsInput) {
            distributors.add(PlayerFactory.getPlayer(distributorInput, Constants.DISTRIBUTOR));
        }

        for (ConsumerInput consumerInput : consumersInput) {
            consumers.add(PlayerFactory.getPlayer(consumerInput, Constants.CONSUMER));
        }
    }

    /**
     * clears all the lists with the players
     */

    public void clearGame() {
        distributorsInGame.clear();
        consumersInGame.clear();
        distributors.clear();
        consumers.clear();
    }

    /**
     * @return the unique object of the class (singleton pattern)
     */

    public static Simulation getSimulation() {
        return SIMULATION;
    }

    public ArrayList<Player> getDistributors() {
        return distributors;
    }

    public ArrayList<Player> getConsumers() {
        return consumers;
    }
}
