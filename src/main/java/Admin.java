import java.util.logging.Logger;

public class Admin {
    Logger logger = Logger.getLogger(getClass().getName());

    public Admin(){

    }

    public void printInfo(){
        logger.info("The project is running.");
    }
}
