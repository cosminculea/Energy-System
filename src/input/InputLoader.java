package input;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import constants.Constants;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public final class InputLoader {
    private final FileReader inputPath;

    public InputLoader(final String inputPath) throws FileNotFoundException {
        this.inputPath = new FileReader(inputPath);
    }

    public Input loadData() throws IOException {
        JSONParser jsonParser = new JSONParser();
        Input input = new Input();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(inputPath);

            String numberOfTurns = jsonObject.get(Constants.NUMBER_OF_TURNS).toString();
            JSONObject initialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);
            JSONArray monthlyUpdates = (JSONArray)  jsonObject.get(Constants.MONTHLY_UPDATES);

            input.setNumberOfTurns(Integer.parseInt(numberOfTurns));

            JSONArray consumers = (JSONArray) initialData.get(Constants.CONSUMERS);
            JSONArray distributors = (JSONArray) initialData.get(Constants.DISTRIBUTORS);

            List<ConsumerInput> consumersInput = input.getInitialData().getConsumers();
            List<DistributorInput> distributorsInput = input.getInitialData().getDistributors();
            List<ActionInput> monthlyUpdatesInput = input.getMonthlyUpdates();

            for (Object consumer : consumers) {
                JSONObject consumerJSON = (JSONObject) consumer;
                consumersInput.add(new ConsumerInput(consumerJSON.get(Constants.ID).toString(),
                        consumerJSON.get(Constants.INITIAL_BUDGET).toString(),
                        consumerJSON.get(Constants.MONTHLY_INCOME).toString()));
            }

            for (Object distributor : distributors) {
                JSONObject distributorJSON = (JSONObject) distributor;
                distributorsInput.add(new DistributorInput(
                        distributorJSON.get(Constants.ID).toString(),
                        distributorJSON.get(Constants.CONTRACT_LENGTH).toString(),
                        distributorJSON.get(Constants.INITIAL_BUDGET).toString(),
                        distributorJSON.get(Constants.INITIAL_INFRASTRUCTURE_COST).toString(),
                        distributorJSON.get(Constants.INITIAL_PRODUCTION_COST).toString()));
            }

            for (Object monthlyUpdate : monthlyUpdates) {
                JSONObject monthlyUpdateJSON = (JSONObject) monthlyUpdate;
                JSONArray newConsumers = (JSONArray) monthlyUpdateJSON.get(Constants.NEW_CONSUMERS);
                JSONArray costsChanges = (JSONArray) monthlyUpdateJSON.get(Constants.COSTS_CHANGES);

                ActionInput actionInput = new ActionInput();
                List<ConsumerInput> newConsumersInput = actionInput.getNewConsumers();
                List<CostsChangesInput> costsChangesInput = actionInput.getCostsChanges();

                for (Object newConsumer : newConsumers) {
                    JSONObject newConsumerJSON = (JSONObject) newConsumer;

                    newConsumersInput.add(new ConsumerInput(
                            newConsumerJSON.get(Constants.ID).toString(),
                            newConsumerJSON.get(Constants.INITIAL_BUDGET).toString(),
                            newConsumerJSON.get(Constants.MONTHLY_INCOME).toString()));
                }

                for (Object costsChange : costsChanges) {
                    JSONObject costsChangeJSON = (JSONObject) costsChange;

                    costsChangesInput.add(new CostsChangesInput(
                            costsChangeJSON.get(Constants.ID).toString(),
                            costsChangeJSON.get(Constants.INFRASTRUCTURE_COST).toString(),
                            costsChangeJSON.get(Constants.PRODUCTION_COST).toString()));

                }

                actionInput.setNewConsumers(newConsumersInput);
                actionInput.setCostsChanges(costsChangesInput);

                monthlyUpdatesInput.add(actionInput);
            }




        } catch (ParseException e) {
            e.printStackTrace();
        }

        return input;
    }

}
