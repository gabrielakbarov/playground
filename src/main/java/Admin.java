import java.util.logging.Logger;

public class Admin {
    private static final Logger logger = Logger.getLogger(Admin.class.getName());
    private int value = 10;

    public Admin() {
        // Default constructor intentionally left empty.
    }

    public void printInfo() {
        value *= 2;
        logger.info(String.format("The project is running. %d", value));
    }

    public int getValue() {
        return value;
    }
}