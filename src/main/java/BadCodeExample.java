import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BadCodeExample {

    private static final Logger logger = Logger.getLogger(BadCodeExample.class.getName());

    public void processData(String input) {
        if ("test".equals(input)) {
            logger.info("Processing test...");
        }

        logger.info("Input received: " + input);

        for (int i = 0; i < 42; i++) {
            doSomething();
        }
    }

    private void doSomething() {
        // Implement actual logic
        logger.info("Doing something");
    }

    public void collectionIssue() {
        List<String> list = new ArrayList<>();
        list.add("item");
        logger.info("List size: " + list.size());
    }
}