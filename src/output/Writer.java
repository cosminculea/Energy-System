package output;

import constants.Constants;
import contract.Contract;
import player.Producer;
import player.Distributor;
import player.ActivePlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Writer {

    /**
     * the file in which the data is written
     */

    private final FileWriter output;

    /**
     * constructor which initialise the file writer
     * @param fileName the name of the file as string
     * @throws IOException if the opening of the file failed
     */

    public Writer(final String fileName) throws IOException {
        output = new FileWriter(fileName);
    }

    /**
     * write all the necessary data of the consumers, distributors and producers
     * @param distributors list of all distributors in the system
     * @param consumers list of all consumer in the system
     * @param producers list of all producers in the system
     * @throws IOException if the writing has failed
     */

    @SuppressWarnings("unchecked")
    public void writeInFile(final ArrayList<ActivePlayer> distributors,
                            final ArrayList<ActivePlayer> consumers,
                            final List<Producer> producers) throws IOException {

        JSONObject object = new JSONObject();
        JSONArray consumersJSON = new JSONArray();
        JSONArray distributorsJSON = new JSONArray();
        JSONArray producersJSON = new JSONArray();

        for (ActivePlayer consumer : consumers) {
            JSONObject consumerJSON = new JSONObject();
            consumerJSON.put(Constants.ID, consumer.getId());
            consumerJSON.put(Constants.IS_BANKRUPT, consumer.isBankrupt());
            consumerJSON.put(Constants.BUDGET, consumer.getBudget());
            consumersJSON.add(consumerJSON);
        }

        for (ActivePlayer distributor : distributors) {
            JSONObject distributorJSON = new JSONObject();
            distributorJSON.put(Constants.ID, distributor.getId());
            distributorJSON.put(Constants.ENERGY_NEEDED_KW,
                    ((Distributor) distributor).getEnergyNeededKW());
            distributorJSON.put(Constants.CONTRACT_COST,
                    ((Distributor) distributor).getCurrentPriceContract());
            distributorJSON.put(Constants.BUDGET, distributor.getBudget());
            distributorJSON.put(Constants.PRODUCER_STRATEGY,
                    ((Distributor) distributor).getProducerStrategy().getType());
            distributorJSON.put(Constants.IS_BANKRUPT, distributor.isBankrupt());

            JSONArray contractsJSON = new JSONArray();
            Map<Integer, Contract> contracts = ((Distributor) distributor).getContracts();

            for (Integer id : contracts.keySet()) {
                Contract contract = contracts.get(id);
                JSONObject contractJSON = new JSONObject();
                contractJSON.put(Constants.CONSUMER_ID, contract.getCounterpart().getId());
                contractJSON.put(Constants.PRICE, contract.getPrice());
                contractJSON.put(Constants.REMAINED_CONTRACT_MONTHS,
                        contract.getRemainedContractMonths());

                contractsJSON.add(contractJSON);
            }

            distributorJSON.put(Constants.CONTRACTS, contractsJSON);
            distributorsJSON.add(distributorJSON);
        }


        for (Producer producer : producers) {
            JSONObject producerJSON = new JSONObject();
            producerJSON.put(Constants.ID, producer.getId());
            producerJSON.put(Constants.MAX_DISTRIBUTORS, producer.getMaxDistributors());
            producerJSON.put(Constants.PRICE_KW, producer.getPriceKW());
            producerJSON.put(Constants.ENERGY_TYPE, producer.getEnergyType().getLabel());
            producerJSON.put(Constants.ENERGY_PER_DISTRIBUTOR, producer.getEnergyPerDistributor());

            JSONArray monthlyStats = new JSONArray();

            for (int monthNumber = 1;
                 monthNumber < producer.getMonthlyDistributorsEvidence().size(); monthNumber++) {

                List<Integer> distributorsIds =
                        producer.getMonthlyDistributorsEvidence().get(monthNumber);
                JSONObject month = new JSONObject();
                JSONArray distributorsIdsJSON = new JSONArray();
                month.put(Constants.MONTH, monthNumber);

                distributorsIdsJSON.addAll(distributorsIds);

                month.put(Constants.DISTRIBUTORS_IDS, distributorsIdsJSON);
                monthlyStats.add(month);
            }

            producerJSON.put(Constants.MONTHLY_STATS, monthlyStats);
            producersJSON.add(producerJSON);
        }

        object.put(Constants.CONSUMERS, consumersJSON);
        object.put(Constants.DISTRIBUTORS, distributorsJSON);
        object.put(Constants.ENERGY_PRODUCERS, producersJSON);


        output.write(object.toJSONString());
        output.flush();
    }
}
