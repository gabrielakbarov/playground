import java.util.ArrayList;
import java.util.List;

public class BadCodeExample {

    // 1. Vulnerability: Hardcoded password (RSPEC-6437)
    private String adminPassword = "password123!";

    // 2. Code Smell: Unused private field (RSPEC-1068)
    private int ignoredCounter = 0;

    public void processData(String input) {
        // 3. Bug: Possible NullPointerException (RSPEC-2259)
        if (input.equals("test")) {
            System.out.println("Processing test...");
        }

        // 4. Code Smell: Using System.out instead of a Logger (RSPEC-106)
        System.out.println("Input received: " + input);

        // 5. Code Smell: Magic Number (RSPEC-109)
        for (int i = 0; i < 42; i++) {
            doSomething();
        }
    }

    private void doSomething() {
        // 6. Bug: Infinite Loop or Empty Statement
        // 7. Code Smell: TODO left in code (RSPEC-1135)
        // TODO: Implement actual logic
    }

    public void collectionIssue() {
        // 8. Code Smell: Raw types should not be used (RSPEC-3740)
        List list = new ArrayList();
        list.add("item");

        // 9. Bug: Creating an object just to throw it away (RSPEC-1848)
        new String("Unnecessary object");
    }
}