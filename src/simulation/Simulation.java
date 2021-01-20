package simulation;

import constants.Constants;
import contract.Contract;
import contract.ContractFactory;
import player.PlayerFactory;
import player.Producer;
import player.Consumer;
import player.Distributor;
import player.ActivePlayer;
import input.ActionInput;
import input.ConsumerInput;
import input.DistributorChange;
import input.DistributorInput;
import input.EntitiesInput;
import input.Input;
import input.ProducerChange;
import input.ProducerInput;
import strategies.Strategy;
import strategies.StrategyFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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

    private final ArrayList<ActivePlayer> distributors = new ArrayList<>();

    /**
     * list of consumers in the system
     */

    private final ArrayList<ActivePlayer> consumers = new ArrayList<>();

    /**
     * list of producers in the system
     */

    private final List<Producer> producers = new ArrayList<>();

    /**
     * map of distributors (with their ids as keys) in the game who play an active role in the
     * simulation at one give time
     */

    private Map<Integer, ActivePlayer> distributorsInGame;

    /**
     * map of consumers (with their ids as keys) in the game who play an active role in the
     * simulation at one give time
     */

    private Map<Integer, ActivePlayer> consumersInGame;

    /**
     * private constructor which guarantee the class has a unique object (singleton pattern)
     */

    public Simulation(Input input) {
        setRounds(input.getMonthlyUpdates());
        setPlayers(input.getInitialData(), input.getMonthlyUpdates().size());
    }

    /**
     * method which runs the simulation between all players by iterating through all rounds (months)
     * and making all the necessary changes:
     *      - sets all players in the game with the information provided by the input and establish
     *    the initial relationship between them (round 0)
     *      - for every round: the monthly changes are updated, the new contracts for every
     *     distributor are calculated, all consumers receive salaries, find and sign a convenient
     *     contract (the cheapest). Moreover, the active players have to pay their debts and are
     *     eliminated from the game if they do not have enough money. The simulation ends when
     *     whether the rounds are over or there are no distributors left in the game
     *      - in addition, at the end of the month producers update their data and distributors
     *      are verified if they have to recalculate their production cost
     */

    public void play() {
        distributorsInGame = new HashMap<>();
        consumersInGame = new HashMap<>();

        setPlayersInGame();

        for (ActionInput round : rounds) {
            resolveNewConsumers(round.getNewConsumers());
            resolveDistributorsChanges(round.getDistributorsChanges());

            calculateNewContracts();
            receiveSalaries();
            findAndSignContracts();
            paymentDayConsumers();
            paymentDayDistributors();

            resolveProducersChanges(round.getProducersChanges());
            verifyRecalculationDistributors();

            if (distributorsInGame.size() == 0) {
                break;
            }
        }
    }

    /**
     * initialise a new month to every producer evidence
     */

    private void initialiseCurrentMonthEvidence() {
        for (Producer producer : producers) {
            producer.initialiseNewMonthEvidence();
        }
    }

    /**
     * at the end of the month every distributor is verified whether he needs to recalculate his
     * production cost or not
     * - if he does, he is removed from all his producers, finds another producers, calculate the
     * production cost and the needToRecalculate flag is reset to false
     * - if he does not, he is just added to the current month evidence
     */

    private void verifyRecalculationDistributors() {
       initialiseCurrentMonthEvidence();

        for (ActivePlayer player : distributors) {
            Distributor distributor = (Distributor) player;

            if (!distributor.isBankrupt() && distributor.needToRecalculate()) {
                distributor.removeFromOldProducers();
                distributor.resetCurrentProducers();
                distributor.calculateProductionCost();
                distributor.resetNeedToRecalculate();
            } else {
                if (!distributor.needToRecalculate()) {
                    for (Producer producer : distributor.getCurrentProducers()) {
                        producer.addToMonthEvidence(distributor);
                    }
                }
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

        for (ActivePlayer distributor : distributors) {
            distributorsInGame.put(distributor.getId(), distributor);
            ((Distributor) distributor).calculateProductionCost();
            ((Distributor) distributor).calculateNewContract();

        }

        for (ActivePlayer consumer : consumers) {
            consumersInGame.put(consumer.getId(), consumer);
        }

        Distributor cheapestDistributor = (Distributor) findCheapestDistributor();

        for (ActivePlayer consumer : consumersInGame.values()) {
            consumer.receiveMoney(((Consumer) consumer).getMonthlyIncome());

            Contract contractForConsumer = ContractFactory.getContract(cheapestDistributor,
                    consumer, Constants.CONSUMER);
            Contract contractForDistributor = ContractFactory.getContract(cheapestDistributor,
                    consumer, Constants.DISTRIBUTOR);
            consumer.signContract(contractForConsumer);
            cheapestDistributor.signContract(contractForDistributor);

            consumer.payDebts();
        }

        for (ActivePlayer distributor : distributorsInGame.values()) {
            distributor.verifyBankruptcy();
            distributor.payDebts();
        }

        distributorsInGame.values().removeIf(ActivePlayer::isBankrupt);
    }

    /**
     * iterates through all the distributors in the game and finds the first distributor who has the
     * lowest current price for the contract
     * @return the cheapest distributor in the current state of the game
     */

    public ActivePlayer findCheapestDistributor() {
        Iterator<ActivePlayer> it = distributorsInGame.values().iterator();
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
        PlayerFactory factory = PlayerFactory.getInstance();

        for (ConsumerInput newConsumer : newConsumers) {
            ActivePlayer consumerCreated =
                    (ActivePlayer) factory.getPlayer(newConsumer, Constants.CONSUMER);
            consumers.add(consumerCreated);
            consumersInGame.put(consumerCreated.getId(), consumerCreated);
        }
    }

    /**
     * set the new infrastructure costs for the distributors given by the input
     * @param distributorsChanges list of al the distributors who need to change the infrastructure
     *                            cost
     */

    private void resolveDistributorsChanges(final List<DistributorChange> distributorsChanges) {
        for (DistributorChange distributorChange : distributorsChanges) {
            Distributor distributor =
                    (Distributor) distributorsInGame.get(distributorChange.getId());

            if (distributor == null) {
                continue;
            }

            distributor.setInfrastructureCost(distributorChange.getInfrastructureCost());
        }
    }

    /**
     * - the method sets the new energy per distributor given by the input for producers and
     * notifies all the distributors for every producer that they need to recalculate their
     * production cost
     * @param producersChanges list of producers changes given by the input
     */

    private void resolveProducersChanges(final List<ProducerChange> producersChanges) {

        for (ProducerChange producerChange : producersChanges) {
            Producer producer = producers.get(producerChange.getId());

            if (producer == null) {
                continue;
            }

            producer.setEnergyPerDistributor(producerChange.getEnergyPerDistributor());
            producer.notifyObservers();
        }

    }

    /**
     * for every distributor in the game the contracts prices are updated
     */

    private void calculateNewContracts() {
        for (ActivePlayer distributor : distributorsInGame.values()) {
            Distributor distributorInGame = (Distributor) distributor;
            distributorInGame.calculateNewContract();
        }
    }

    /**
     * every consumer in the game receives his salary (the salary is added to the budget)
     */

    private void receiveSalaries() {
        for (ActivePlayer consumer : consumersInGame.values()) {
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
        ActivePlayer cheapestDistributor = findCheapestDistributor();

        for (ActivePlayer consumer : consumersInGame.values()) {
            if (!consumer.hasContract()
                    || (consumer.hasContract()
                        && ((Consumer) consumer).getContract().contractExpired())) {

                Contract contractForConsumer = ContractFactory.getContract(cheapestDistributor,
                        consumer, Constants.CONSUMER);
                Contract contractForDistributor = ContractFactory.getContract(cheapestDistributor,
                        consumer, Constants.DISTRIBUTOR);

                if (consumer.hasContract()) {
                    consumer.terminateContracts();
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
     * contract is terminated and he is eliminated from the game
     * - otherwise he pays the debts
     */

    private void paymentDayConsumers() {

        for (Integer consumerId : consumersInGame.keySet()) {
            ActivePlayer consumer = consumersInGame.get(consumerId);
            consumer.verifyBankruptcy();

            if (consumer.isBankrupt()) {
                consumer.terminateContracts();
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
     * - if the distributor is bankrupt after the verification he terminates all the contract he has
     * with all of the consumers and is eliminated from the game
     */

    private void paymentDayDistributors() {

        for (Integer distributorId : distributorsInGame.keySet()) {
            ActivePlayer distributor = distributorsInGame.get(distributorId);
            distributor.verifyBankruptcy();
            distributor.payDebts();

            if (distributor.isBankrupt()) {
                distributor.terminateContracts();
            }
        }

        distributorsInGame.values().removeIf(ActivePlayer::isBankrupt);
    }

    /**
     * @param rounds monthly updates given by the input
     */

    private void setRounds(final List<ActionInput> rounds) {
        this.rounds = rounds;
    }

    /**
     * every player given by the input is added to the data base of the simulation
     * - in addition, for distributors the strategy is created and set, and for producers the list
     * which keeps track of the monthly updates is initialised
     * @param initialData consumers and distributors given by the input
     */

    private void setPlayers(final EntitiesInput initialData, final int numberOfRounds) {
        List<ConsumerInput> consumersInput = initialData.getConsumers();
        List<DistributorInput> distributorsInput = initialData.getDistributors();
        List<ProducerInput> producersInput = initialData.getProducers();

        PlayerFactory playerFactory = PlayerFactory.getInstance();
        StrategyFactory strategyFactory = StrategyFactory.getInstance();

        for (ProducerInput producerInput : producersInput) {
            List<List<Integer>> monthlyDistributorsEvidence = new ArrayList<>(numberOfRounds);
            Producer newProducer = (Producer) playerFactory.getPlayer(producerInput,
                    Constants.PRODUCER);
            newProducer.setMonthlyDistributorsEvidence(monthlyDistributorsEvidence);
            producers.add(newProducer.getId(), newProducer);
        }

        producers.sort(Comparator.comparingInt(Producer::getId));

        for (DistributorInput distributorInput : distributorsInput) {
            ActivePlayer distributor = (ActivePlayer) playerFactory.getPlayer(distributorInput,
                    Constants.DISTRIBUTOR);

            Strategy strategy = strategyFactory.getStrategy(distributorInput.getProducerStrategy(),
                    producers);

            ((Distributor) distributor).setProducerStrategy(strategy);

            distributors.add(distributor);
        }

        for (ConsumerInput consumerInput : consumersInput) {
            consumers.add((ActivePlayer) playerFactory.getPlayer(consumerInput,
                    Constants.CONSUMER));
        }
    }

    public ArrayList<ActivePlayer> getDistributors() {
        return distributors;
    }

    public ArrayList<ActivePlayer> getConsumers() {
        return consumers;
    }

    public List<Producer> getProducers() {
        return producers;
    }
}
