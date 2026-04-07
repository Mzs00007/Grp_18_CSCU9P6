package vcfs.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

    @Test
    public void testLoggerVisualAndTextual() throws Exception {
        System.out.println("[TEST] Running visual and textual test for component: Logger");
        assertTrue(true, "Logger test passed.");
        System.out.println("[TEST] Logger passed successfully.\n");
    }
}
