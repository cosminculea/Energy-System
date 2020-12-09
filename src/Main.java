import com.fasterxml.jackson.databind.ObjectMapper;
import input.Input;
import input.InputLoader;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        String file = args[0];
        InputLoader inputLoader = new InputLoader(file);
        Input input = inputLoader.loadData();

    }
}
