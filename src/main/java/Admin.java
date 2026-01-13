import java.util.logging.Logger;

public class Admin {
    Logger logger = Logger.getLogger(getClass().getName());
    int value = 10;

    public Admin(){

    }

    public void printInfo(){
        value *= 2;
        logger.info("The project is running." + value);
    }

    public int getValue(){
        return value;
    }
}
