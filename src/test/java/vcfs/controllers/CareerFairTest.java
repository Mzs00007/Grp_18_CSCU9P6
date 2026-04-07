package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.models.enums.FairPhase;

@DisplayName("CareerFair - State Machine and Phase Transitions (VCFS-002)")
public class CareerFairTest {

    private CareerFair fair;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        fair = new CareerFair();
        fair.name = "Test Fair";
        
        openTime = new LocalDateTime(2026, 4, 8, 10, 0);
        closeTime = new LocalDateTime(2026, 4, 8, 12, 0);
        startTime = new LocalDateTime(2026, 4, 8, 13, 0);
        endTime = new LocalDateTime(2026, 4, 8, 17, 0);
    }

    @Test
    @DisplayName("Initial state is DORMANT")
    void testInitialState() {
        assertEquals(FairPhase.DORMANT, fair.getCurrentPhase());
    }

    @Test
    @DisplayName("setTimes validates chronological ordering correctly")
    void testSetTimes_Validation() {
        // Valid configuration
        assertDoesNotThrow(() -> fair.setTimes(openTime, closeTime, startTime, endTime));
        assertEquals(FairPhase.PREPARING, fair.getCurrentPhase());

        // Invalid: open after close
        assertThrows(IllegalArgumentException.class, 
            () -> fair.setTimes(closeTime, openTime, startTime, endTime));

        // Invalid: close after start
        assertThrows(IllegalArgumentException.class, 
            () -> fair.setTimes(openTime, startTime, closeTime, endTime));

        // Invalid: start after end
        assertThrows(IllegalArgumentException.class, 
            () -> fair.setTimes(openTime, closeTime, endTime, startTime));
    }

    @Test
    @DisplayName("evaluatePhase transitions correctly through all phases")
    void testEvaluatePhase_FullLifecycle() {
        fair.setTimes(openTime, closeTime, startTime, endTime);

        // Before bookings open -> PREPARING
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 9, 59));
        assertEquals(FairPhase.PREPARING, fair.getCurrentPhase());

        // At bookings open -> BOOKINGS_OPEN
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 10, 0));
        assertEquals(FairPhase.BOOKINGS_OPEN, fair.getCurrentPhase());

        // Inside bookings open -> BOOKINGS_OPEN
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 11, 0));
        assertEquals(FairPhase.BOOKINGS_OPEN, fair.getCurrentPhase());

        // At bookings close -> BOOKINGS_CLOSED
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 12, 0));
        assertEquals(FairPhase.BOOKINGS_CLOSED, fair.getCurrentPhase());

        // Inside bookings closed -> BOOKINGS_CLOSED
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 12, 30));
        assertEquals(FairPhase.BOOKINGS_CLOSED, fair.getCurrentPhase());

        // At fair start -> FAIR_LIVE
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 13, 0));
        assertEquals(FairPhase.FAIR_LIVE, fair.getCurrentPhase());

        // Inside fair -> FAIR_LIVE
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 15, 0));
        assertEquals(FairPhase.FAIR_LIVE, fair.getCurrentPhase());

        // Exactly at fair end -> FAIR_LIVE (inclusive bound)
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 17, 0));
        assertEquals(FairPhase.FAIR_LIVE, fair.getCurrentPhase());

        // After fair end -> DORMANT
        fair.evaluatePhase(new LocalDateTime(2026, 4, 8, 17, 1));
        assertEquals(FairPhase.DORMANT, fair.getCurrentPhase());
    }

    @Test
    @DisplayName("canBook is only true during BOOKINGS_OPEN")
    void testCanBook() {
        fair.setTimes(openTime, closeTime, startTime, endTime);

        // PREPARING
        assertFalse(fair.canBook(new LocalDateTime(2026, 4, 8, 9, 0)));
        
        // BOOKINGS_OPEN
        assertTrue(fair.canBook(new LocalDateTime(2026, 4, 8, 10, 0)));
        assertTrue(fair.canBook(new LocalDateTime(2026, 4, 8, 11, 0)));
        
        // BOOKINGS_CLOSED
        assertFalse(fair.canBook(new LocalDateTime(2026, 4, 8, 12, 0)));
        
        // FAIR_LIVE
        assertFalse(fair.canBook(new LocalDateTime(2026, 4, 8, 13, 0)));
        
        // DORMANT
        assertFalse(fair.canBook(new LocalDateTime(2026, 4, 8, 18, 0)));
    }

    @Test
    @DisplayName("isLive is only true during FAIR_LIVE")
    void testIsLive() {
        fair.setTimes(openTime, closeTime, startTime, endTime);

        // PREPARING
        assertFalse(fair.isLive(new LocalDateTime(2026, 4, 8, 9, 0)));
        
        // BOOKINGS_OPEN
        assertFalse(fair.isLive(new LocalDateTime(2026, 4, 8, 11, 0)));
        
        // BOOKINGS_CLOSED
        assertFalse(fair.isLive(new LocalDateTime(2026, 4, 8, 12, 0)));
        
        // FAIR_LIVE
        assertTrue(fair.isLive(new LocalDateTime(2026, 4, 8, 13, 0)));
        assertTrue(fair.isLive(new LocalDateTime(2026, 4, 8, 17, 0)));
        
        // DORMANT
        assertFalse(fair.isLive(new LocalDateTime(2026, 4, 8, 18, 0)));
    }
}
