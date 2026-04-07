package vcfs.models.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.core.LocalDateTime;
import vcfs.models.enums.ReservationState;
import vcfs.models.users.Candidate;

@DisplayName("Reservation - State Machine and Time Checks")
public class ReservationTest {

    private Reservation res;
    private Candidate candidate;
    private Offer offer;

    @BeforeEach
    void setUp() {
        res = new Reservation();
        candidate = new Candidate("C1", "bob@test.com", "Bob");
        offer = new Offer();
        offer.setTitle("Java Dev");
        
        res.setCandidate(candidate);
        res.setOffer(offer);
        res.setScheduledStart(new LocalDateTime(2026, 4, 8, 10, 0));
        res.setScheduledEnd(new LocalDateTime(2026, 4, 8, 10, 30));
    }

    @Test
    @DisplayName("isActive evaluates to true within bounds for CONFIRMED")
    void testIsActive_WithinBounds_Confirmed() {
        res.setState(ReservationState.CONFIRMED);
        
        // Exactly at start
        assertTrue(res.isActive(new LocalDateTime(2026, 4, 8, 10, 0)));
        // In the middle
        assertTrue(res.isActive(new LocalDateTime(2026, 4, 8, 10, 15)));
        // Exactly at end
        assertTrue(res.isActive(new LocalDateTime(2026, 4, 8, 10, 30)));
    }

    @Test
    @DisplayName("isActive evaluates to true within bounds for IN_PROGRESS")
    void testIsActive_WithinBounds_InProgress() {
        res.setState(ReservationState.IN_PROGRESS);
        assertTrue(res.isActive(new LocalDateTime(2026, 4, 8, 10, 15)));
    }

    @Test
    @DisplayName("isActive evaluates to false outside bounds")
    void testIsActive_OutsideBounds() {
        res.setState(ReservationState.CONFIRMED);
        
        // Before start
        assertFalse(res.isActive(new LocalDateTime(2026, 4, 8, 9, 59)));
        // After end
        assertFalse(res.isActive(new LocalDateTime(2026, 4, 8, 10, 31)));
    }

    @Test
    @DisplayName("isActive evaluates to false for wrong states")
    void testIsActive_WrongStates() {
        LocalDateTime now = new LocalDateTime(2026, 4, 8, 10, 15);
        
        res.setState(ReservationState.CANCELLED);
        assertFalse(res.isActive(now));
        
        res.setState(ReservationState.COMPLETED);
        assertFalse(res.isActive(now));
        
        res.setState(ReservationState.EXPIRED);
        assertFalse(res.isActive(now));
    }

    @Test
    @DisplayName("isActive evaluates to false if dates are null")
    void testIsActive_NullDates() {
        res.setState(ReservationState.CONFIRMED);
        LocalDateTime now = new LocalDateTime(2026, 4, 8, 10, 15);
        
        res.setScheduledStart(null);
        assertFalse(res.isActive(now));
        
        res.setScheduledStart(new LocalDateTime(2026, 4, 8, 10, 0));
        res.setScheduledEnd(null);
        assertFalse(res.isActive(now));
        
        res.setScheduledEnd(new LocalDateTime(2026, 4, 8, 10, 30));
        assertFalse(res.isActive(null));
    }

    @Test
    @DisplayName("cancel changes state to CANCELLED and logs reason")
    void testCancel_ValidReason() {
        res.setState(ReservationState.CONFIRMED);
        res.cancel("Candidate no-show");
        
        assertEquals(ReservationState.CANCELLED, res.getState());
    }

    @Test
    @DisplayName("cancel throws exception for empty or null reason")
    void testCancel_InvalidReason() {
        assertThrows(IllegalArgumentException.class, () -> res.cancel(null));
        assertThrows(IllegalArgumentException.class, () -> res.cancel(""));
        assertThrows(IllegalArgumentException.class, () -> res.cancel("   "));
    }
}
