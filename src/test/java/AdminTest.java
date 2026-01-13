import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminTest {

    @Test
    void testPrintInfoLogsMessage() {
        Admin admin = new Admin();
        admin.printInfo();
        assertEquals(20, admin.getValue());
    }
}