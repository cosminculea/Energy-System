package player;

import input.ProducerInput;
import observer.Observable;
import observer.Observer;
import strategies.EnergyType;

import java.util.ArrayList;
import java.util.List;

public final class Producer implements Player, Observable {

    /**
     * the id of the producer
     */

    private final int id;

    /**
     * the type of energy the producer generates
     */

    private EnergyType energyType;

    /**
     * the maximum number of distributors a producer can have
     */

    private int maxDistributors;

    /**
     * the price of the the energy per KW
     */

    private double priceKW;

    /**
     * how much energy measured in KW a producer can give to one distributor
     */

    private int energyPerDistributor;

    /**
     * list of all monthly updates with the distributors ids
     */

    private List<List<Integer>> monthlyDistributorsEvidence;

    /**
     * list of distributors in the current month
     */

    private final List<Observer> currentDistributors = new ArrayList<>();


    /**
     * constructor with information given by the input
     * @param producerInput producers information given by the input
     */

    public Producer(final ProducerInput producerInput) {
        id = producerInput.getId();
        energyType = EnergyType.valueOf(producerInput.getEnergyType());
        maxDistributors = producerInput.getMaxDistributors();
        priceKW = producerInput.getPriceKW();
        energyPerDistributor = producerInput.getEnergyPerDistributor();
    }

    /**
     * notify all current distributors
     */

    @Override
    public void notifyObservers() {
        for (Observer distributor : currentDistributors) {
            distributor.update();
        }
    }

    /**
     * add a distributor to the list of observers
     * @param observer the distributor who needs to be added
     */

    @Override
    public void addObserver(final Observer observer) {
        currentDistributors.add(observer);
    }

    /**
     * remove a distributor
     * @param distributor the distributor who needs to be removed
     */

    @Override
    public void removeObserver(final Observer distributor) {
        currentDistributors.remove(distributor);
    }

    /**
     * adds a new empty list in the months evidence
     */

    public void initialiseNewMonthEvidence() {
        monthlyDistributorsEvidence.add(new ArrayList<>());
    }

    /**
     * clears the list of current distributors
     */

    public void resetCurrentDistributors() {
        currentDistributors.clear();
    }

    /**
     * - add a new distributor to the list of current distributors and keeps evidence of him in the
     * current month
     */

    public void updateMonthlyDistributors(Distributor distributor) {
        addObserver(distributor);
        addToMonthEvidence(distributor);
    }

    /**
     * add a new distributor to the evidence of the current month
     */

    public void addToMonthEvidence(Distributor distributor) {
        monthlyDistributorsEvidence
                .get(monthlyDistributorsEvidence.size() - 1).add(distributor.getId());
    }

    /**
     * sets a new list of evidences for every month and initialises the first month (round 0)
     */

    public void setMonthlyDistributorsEvidence(final List<List<Integer>>
                                                       monthlyDistributorsEvidence) {
        this.monthlyDistributorsEvidence = monthlyDistributorsEvidence;

        initialiseNewMonthEvidence();
    }

    /**
     * @return the number of current distributors
     */

    public int getNumberOfDistributors() {
        return currentDistributors.size();
    }


    /**
     * @return id of the distributor
     */

    @Override
    public int getId() {
        return id;
    }


    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(final EnergyType energyType) {
        this.energyType = energyType;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(final int maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(final double priceKW) {
        this.priceKW = priceKW;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public List<List<Integer>> getMonthlyDistributorsEvidence() {
        return monthlyDistributorsEvidence;
    }
}
