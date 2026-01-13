import org.junit.jupiter.api.Test;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminTest {

    @Test
    void testPrintInfoLogsMessage() {
        // Arrange: Create the object and a way to intercept logs
        Admin admin = new Admin();

        // We get the same logger instance used in the Admin class
        Logger adminLogger = Logger.getLogger(Admin.class.getName());

        // Create a custom handler to capture the log record
        List<LogRecord> logs = new ArrayList<>();
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                logs.add(record);
            }
            @Override public void flush() {}
            @Override public void close() throws SecurityException {}
        };
        adminLogger.addHandler(handler);

        // Act: Call the method
        admin.printInfo();

        // Assert: Check if the message was captured
        boolean found = logs.stream()
                .anyMatch(record -> record.getMessage().contains("The project is running."));

        assertTrue(found, "The logger should have recorded 'The project is running.'");

        // Clean up
        adminLogger.removeHandler(handler);
    }
}