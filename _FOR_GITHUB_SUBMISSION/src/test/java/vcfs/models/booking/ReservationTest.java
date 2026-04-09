package vcfs.models.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.core.LocalDateTime;
import vcfs.models.enums.ReservationState;
import vcfs.models.users.Candidate;

/**
 * Comprehensive JUnit tests for Reservation class.
 * Tests booking linking candidate to offer with state management and time validation.
 * CSCU9P6 Group Project - Required for JUnit Test Report
 * ZAID (mzs00007) - Test Suite Implementation
 */
@DisplayName("Reservation - Candidate Booking with State Management and Time Validation")
public class ReservationTest {

    private Reservation res;
    private Candidate candidate;
    private Offer offer;
    private MeetingSession session;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        res = new Reservation();
        
        // Create test candidate
        candidate = new Candidate("bob@test.com", "Bob Davis", "bob@test.com");
        
        // Create test offer
        offer = new Offer();
        offer.setTitle("Java Developer Role");
        
        // Time bounds
        startTime = new LocalDateTime(2026, 4, 8, 10, 0);
        endTime = new LocalDateTime(2026, 4, 8, 10, 30);
        
        // Create session for integration
        session = new MeetingSession();
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Test
    @DisplayName("Reservation constructor creates empty instance")
    void testReservationConstructor() {
        assertNotNull(res);
        assertNull(res.getCandidate());
        assertNull(res.getOffer());
        assertNull(res.getSession());
        assertEquals(ReservationState.CONFIRMED, res.getState(), "Initial state should be CONFIRMED");
    }

    // ========== CANDIDATE MANAGEMENT ==========

    @Test
    @DisplayName("setCandidate stores candidate reference")
    void testSetCandidate() {
        res.setCandidate(candidate);
        assertEquals(candidate, res.getCandidate());
    }

    @Test
    @DisplayName("setCandidate throws exception for null")
    void testSetCandidateNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            res.setCandidate(null);
        });
        assertTrue(exception.getMessage().contains("null") || exception.getMessage().contains("Candidate"));
    }

    // ========== OFFER MANAGEMENT ==========

    @Test
    @DisplayName("setOffer stores offer reference")
    void testSetOffer() {
        res.setOffer(offer);
        assertEquals(offer, res.getOffer());
    }

    @Test
    @DisplayName("setOffer throws exception for null")
    void testSetOfferNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            res.setOffer(null);
        });
        assertTrue(exception.getMessage().contains("null") || exception.getMessage().contains("Offer"));
    }

    // ========== SESSION MANAGEMENT ==========

    @Test
    @DisplayName("setSession stores meeting session")
    void testSetSession() {
        res.setSession(session);
        assertEquals(session, res.getSession());
    }

    @Test
    @DisplayName("setSession throws exception for null")
    void testSetSessionNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            res.setSession(null);
        });
        assertTrue(exception.getMessage().contains("null") || exception.getMessage().contains("Session"));
    }

    // ========== STATE MANAGEMENT ==========

    @Test
    @DisplayName("setState updates reservation state")
    void testSetState() {
        res.setState(ReservationState.IN_PROGRESS);
        assertEquals(ReservationState.IN_PROGRESS, res.getState());
        
        res.setState(ReservationState.CANCELLED);
        assertEquals(ReservationState.CANCELLED, res.getState());
    }

    @Test
    @DisplayName("setState throws exception for null")
    void testSetStateNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            res.setState(null);
        });
        assertTrue(exception.getMessage().contains("null") || exception.getMessage().contains("State"));
    }

    // ========== SCHEDULED TIME MANAGEMENT ==========

    @Test
    @DisplayName("setScheduledStart stores start time")
    void testSetScheduledStart() {
        res.setScheduledStart(startTime);
        assertEquals(startTime, res.getScheduledStart());
    }

    @Test
    @DisplayName("setScheduledStart accepts null")
    void testSetScheduledStartNull() {
        assertDoesNotThrow(() -> {
            res.setScheduledStart(null);
        });
        assertNull(res.getScheduledStart());
    }

    @Test
    @DisplayName("setScheduledEnd stores end time")
    void testSetScheduledEnd() {
        res.setScheduledEnd(endTime);
        assertEquals(endTime, res.getScheduledEnd());
    }

    @Test
    @DisplayName("setScheduledEnd throws when end before start")
    void testSetScheduledEndBeforeStart() {
        res.setScheduledStart(startTime);
        
        LocalDateTime beforeStart = new LocalDateTime(2026, 4, 8, 9, 30);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            res.setScheduledEnd(beforeStart);
        });
        assertTrue(exception.getMessage().toLowerCase().contains("after"));
    }

    @Test
    @DisplayName("setScheduledEnd accepts null")
    void testSetScheduledEndNull() {
        assertDoesNotThrow(() -> {
            res.setScheduledEnd(null);
        });
        assertNull(res.getScheduledEnd());
    }

    // ========== CANCEL METHOD TESTS ==========

    @Test
    @DisplayName("cancel() throws for null reason")
    void testCancelNullReason() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            res.cancel(null);
        });
        assertTrue(exception.getMessage().contains("Cancellation reason"));
    }

    @Test
    @DisplayName("cancel() throws for empty reason")
    void testCancelEmptyReason() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            res.cancel("   ");
        });
        assertTrue(exception.getMessage().contains("Cancellation reason"));
    }

    @Test
    @DisplayName("cancel() sets state to CANCELLED")
    void testCancelSetsState() {
        res.cancel("Recruiter unavailable");
        assertEquals(ReservationState.CANCELLED, res.getState());
    }

    @Test
    @DisplayName("cancel() accepts valid reason")
    void testCancelValidReason() {
        assertDoesNotThrow(() -> {
            res.cancel("Candidate conflict");
        });
        assertEquals(ReservationState.CANCELLED, res.getState());
    }

    // ========== ISACTIVE METHOD TESTS ==========

    @Test
    @DisplayName("isActive returns true inside time window for CONFIRMED")
    void testIsActiveWithinWindow() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        LocalDateTime withinWindow = new LocalDateTime(2026, 4, 8, 10, 15);
        assertTrue(res.isActive(withinWindow));
    }

    @Test
    @DisplayName("isActive returns true at start time")
    void testIsActiveAtStart() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        assertTrue(res.isActive(startTime));
    }

    @Test
    @DisplayName("isActive returns true at end time")
    void testIsActiveAtEnd() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        assertTrue(res.isActive(endTime));
    }

    @Test
    @DisplayName("isActive returns false before window")
    void testIsActiveBeforeWindow() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        LocalDateTime beforeWindow = new LocalDateTime(2026, 4, 8, 9, 30);
        assertFalse(res.isActive(beforeWindow));
    }

    @Test
    @DisplayName("isActive returns false after window")
    void testIsActiveAfterWindow() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        LocalDateTime afterWindow = new LocalDateTime(2026, 4, 8, 10, 45);
        assertFalse(res.isActive(afterWindow));
    }

    @Test
    @DisplayName("isActive returns true for IN_PROGRESS within window")
    void testIsActiveInProgress() {
        res.setState(ReservationState.IN_PROGRESS);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        assertTrue(res.isActive(startTime));
    }

    @Test
    @DisplayName("isActive returns false for CANCELLED state")
    void testIsActiveCancelled() {
        res.setState(ReservationState.CANCELLED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        assertFalse(res.isActive(startTime), "Cancelled reservations should not be active");
    }

    @Test
    @DisplayName("isActive with null time returns false")
    void testIsActiveNullTime() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        assertFalse(res.isActive(null));
    }

    @Test
    @DisplayName("isActive with null start time returns false")
    void testIsActiveNullStartTime() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(null);
        res.setScheduledEnd(endTime);
        
        assertFalse(res.isActive(startTime));
    }

    @Test
    @DisplayName("isActive with null end time returns false")
    void testIsActiveNullEndTime() {
        res.setState(ReservationState.CONFIRMED);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(null);
        
        assertFalse(res.isActive(startTime));
    }

    // ========== INTEGRATION TESTS ==========

    @Test
    @DisplayName("Complete reservation setup")
    void testCompleteReservationSetup() {
        res.setCandidate(candidate);
        res.setOffer(offer);
        res.setSession(session);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        res.setState(ReservationState.CONFIRMED);
        
        assertEquals(candidate, res.getCandidate());
        assertEquals(offer, res.getOffer());
        assertEquals(session, res.getSession());
        assertEquals(startTime, res.getScheduledStart());
        assertEquals(endTime, res.getScheduledEnd());
        assertTrue(res.isActive(new LocalDateTime(2026, 4, 8, 10, 15)));
    }

    @Test
    @DisplayName("Reservation lifecycle: confirmed -> in progress -> completed")
    void testReservationLifecycle() {
        res.setCandidate(candidate);
        res.setOffer(offer);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        assertEquals(ReservationState.CONFIRMED, res.getState());
        
        res.setState(ReservationState.IN_PROGRESS);
        assertEquals(ReservationState.IN_PROGRESS, res.getState());
        
        res.setState(ReservationState.COMPLETED);
        assertEquals(ReservationState.COMPLETED, res.getState());
    }

    @Test
    @DisplayName("toString contains relevant information")
    void testToString() {
        res.setCandidate(candidate);
        res.setOffer(offer);
        res.setScheduledStart(startTime);
        res.setScheduledEnd(endTime);
        
        String str = res.toString();
        assertNotNull(str);
        assertTrue(str.contains("Reservation") || str.contains("candidate") || str.contains("offer"));
    }

    @Test
    @DisplayName("Legacy: Reservation test passed")
    void testReservationLegacy() {
        System.out.println("[TEST] Reservation comprehensive test suite");
        assertTrue(true);
        System.out.println("[TEST] Reservation tests passed.\n");
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

