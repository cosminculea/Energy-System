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

    /**
     * the file from which the data is read
     */

    private final FileReader inputPath;

    /**
     * constructor which initialise the file
     */

    public InputLoader(final String inputPath) throws FileNotFoundException {
        this.inputPath = new FileReader(inputPath);
    }

    /**
     * load all data from input using JSON
     * @return input object containing all entities and monthly updates
     * @throws IOException if the reading failed
     */

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
            JSONArray producers = (JSONArray) initialData.get(Constants.PRODUCERS);

            loadConsumers(input, consumers);
            loadDistributors(input, distributors);
            loadProducers(input, producers);
            loadMonthlyUpdates(input, monthlyUpdates);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return input;
    }

    /**
     * loads all consumers given by the input
     */

    void loadConsumers(final Input input, final JSONArray consumers) {
        List<ConsumerInput> consumersInput = input.getInitialData().getConsumers();

        for (Object consumer : consumers) {
            JSONObject consumerJSON = (JSONObject) consumer;
            consumersInput.add(new ConsumerInput(consumerJSON.get(Constants.ID).toString(),
                    consumerJSON.get(Constants.INITIAL_BUDGET).toString(),
                    consumerJSON.get(Constants.MONTHLY_INCOME).toString()));
        }
    }

    /**
     * loads all distributors given by the input
     */

    void loadDistributors(final Input input, final JSONArray distributors) {
        List<DistributorInput> distributorsInput = input.getInitialData().getDistributors();

        for (Object distributor : distributors) {
            JSONObject distributorJSON = (JSONObject) distributor;
            distributorsInput.add(new DistributorInput(
                    distributorJSON.get(Constants.ID).toString(),
                    distributorJSON.get(Constants.CONTRACT_LENGTH).toString(),
                    distributorJSON.get(Constants.INITIAL_BUDGET).toString(),
                    distributorJSON.get(Constants.INITIAL_INFRASTRUCTURE_COST).toString(),
                    distributorJSON.get(Constants.ENERGY_NEEDED_KW).toString(),
                    distributorJSON.get(Constants.PRODUCER_STRATEGY).toString()));
        }
    }

    void loadProducers(final Input input, final JSONArray producers) {
        List<ProducerInput> producersInput = input.getInitialData().getProducers();

        for (Object producer : producers) {
            JSONObject producerJSON = (JSONObject) producer;
            producersInput.add(new ProducerInput(
                    producerJSON.get(Constants.ID).toString(),
                    producerJSON.get(Constants.ENERGY_TYPE).toString(),
                    producerJSON.get(Constants.MAX_DISTRIBUTORS).toString(),
                    producerJSON.get(Constants.PRICE_KW).toString(),
                    producerJSON.get(Constants.ENERGY_PER_DISTRIBUTOR).toString()));
        }
    }

    /**
     * loads all the updates from input represented by months (rounds of the game)
     */

    void loadMonthlyUpdates(final Input input, final JSONArray monthlyUpdates) {
        List<ActionInput> monthlyUpdatesInput = input.getMonthlyUpdates();


        for (Object monthlyUpdate : monthlyUpdates) {
            JSONObject monthlyUpdateJSON = (JSONObject) monthlyUpdate;
            JSONArray newConsumers = (JSONArray) monthlyUpdateJSON.get(Constants.NEW_CONSUMERS);
            JSONArray distributorsChanges =
                    (JSONArray) monthlyUpdateJSON.get(Constants.DISTRIBUTOR_CHANGES);
            JSONArray producersChanges =
                    (JSONArray) monthlyUpdateJSON.get(Constants.PRODUCER_CHANGES);

            ActionInput actionInput = new ActionInput();
            List<ConsumerInput> newConsumersInput = actionInput.getNewConsumers();
            List<DistributorChange> distributorsChangesInput = actionInput.getDistributorsChanges();
            List<ProducerChange> producersChangesInput = actionInput.getProducersChanges();

            for (Object newConsumer : newConsumers) {
                JSONObject newConsumerJSON = (JSONObject) newConsumer;

                newConsumersInput.add(new ConsumerInput(
                        newConsumerJSON.get(Constants.ID).toString(),
                        newConsumerJSON.get(Constants.INITIAL_BUDGET).toString(),
                        newConsumerJSON.get(Constants.MONTHLY_INCOME).toString()));
            }

            for (Object distributorChange : distributorsChanges) {
                JSONObject distributorChangeJSON = (JSONObject) distributorChange;

                distributorsChangesInput.add(new DistributorChange(
                        distributorChangeJSON.get(Constants.ID).toString(),
                        distributorChangeJSON.get(Constants.INFRASTRUCTURE_COST).toString()));

            }

            for (Object producerChange : producersChanges) {
                JSONObject producerChangeJSON = (JSONObject) producerChange;

                producersChangesInput.add(new ProducerChange(
                        producerChangeJSON.get(Constants.ID).toString(),
                        producerChangeJSON.get(Constants.ENERGY_PER_DISTRIBUTOR).toString()));

            }

            actionInput.setNewConsumers(newConsumersInput);
            actionInput.setDistributorsChanges(distributorsChangesInput);
            actionInput.setProducersChanges(producersChangesInput);

            monthlyUpdatesInput.add(actionInput);
        }
    }
}
