package output;

import constants.Constants;
import contract.Contract;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import entities.player.Distributor;
import entities.player.Player;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Writer {

    /**
     * the file in which the data is written
     */

    private final FileWriter output;

    /**
     * constructor which initialise the file writer
     * @param fileName the name of the film as string
     * @throws IOException if the opening of the file failed
     */

    public Writer(final String fileName) throws IOException {
        output = new FileWriter(fileName);
    }

    /**
     * write all the necessary data of the consumers and distributors
     * @param distributors list of all distributors in the system
     * @param consumers list of all consumer in the system
     * @throws IOException if the writing has failed
     */

    @SuppressWarnings("unchecked")
    public void writeInFile(final ArrayList<Player> distributors,
                            final ArrayList<Player> consumers) throws IOException {

        JSONObject object = new JSONObject();
        JSONArray consumersJSON = new JSONArray();
        JSONArray distributorsJSON = new JSONArray();

        for (Player consumer : consumers) {
            JSONObject consumerJSON = new JSONObject();
            consumerJSON.put(Constants.ID, consumer.getId());
            consumerJSON.put(Constants.IS_BANKRUPT, consumer.isBankrupt());
            consumerJSON.put(Constants.BUDGET, consumer.getBudget());
            consumersJSON.add(consumerJSON);
        }

        for (Player distributor : distributors) {
            JSONObject distributorJSON = new JSONObject();
            distributorJSON.put(Constants.ID, distributor.getId());
            distributorJSON.put(Constants.IS_BANKRUPT, distributor.isBankrupt());
            distributorJSON.put(Constants.BUDGET, distributor.getBudget());

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

        object.put(Constants.CONSUMERS, consumersJSON);
        object.put(Constants.DISTRIBUTORS, distributorsJSON);

        output.write(object.toJSONString());
        output.flush();
    }
}
