package entities;

import entities.player.Distributor;
import input.ProducerInput;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Observable;

public class Producer extends Observable implements Player {

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
     *
     */


    private final List<Distributor> currentDistributors = new ArrayList<>();


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

    public void updateMonthlyDistributors(Distributor distributor) {
        monthlyDistributorsEvidence
                .get(monthlyDistributorsEvidence.size() - 1).add(distributor.getId());

        currentDistributors.add(distributor);
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

    public void setMonthlyDistributorsEvidence(final List<List<Integer>>
                                                       monthlyDistributorsEvidence) {
        this.monthlyDistributorsEvidence = monthlyDistributorsEvidence;

        List<Integer> initialRound = new ArrayList<>();
        monthlyDistributorsEvidence.add(initialRound);
    }

    public List<List<Integer>> getMonthlyDistributorsEvidence() {
        return monthlyDistributorsEvidence;
    }

    public void updateCurrentDistributors(Player distributor) {
        currentDistributors.add((Distributor) distributor);
    }


    public List<Distributor> getCurrentDistributors() {
        return currentDistributors;
    }

    @Override
    public void notifyObservers() {
        for (Distributor distributor : currentDistributors) {
            distributor.update(this, true);
        }

        //currentDistributors.clear();
    }

    public void resetCurrentDistributors() {
        currentDistributors.clear();
    }

    @Override
    public int getId() {
        return id;
    }
}
