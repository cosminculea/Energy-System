package input;

import constants.Constants;
import contract.Contract;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import players.Distributor;
import players.Player;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {
    FileWriter output;

    public Writer(final String fileName) throws IOException {
        output = new FileWriter(fileName);
    }

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
            for (Contract contract : ((Distributor) distributor).getContracts()) {
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
    }
}
