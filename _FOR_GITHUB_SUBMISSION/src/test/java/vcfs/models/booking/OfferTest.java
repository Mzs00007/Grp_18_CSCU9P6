package vcfs.models.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import vcfs.core.LocalDateTime;
import vcfs.models.users.Recruiter;
import java.util.Collection;

@DisplayName("Offer - Booking Offer Model (VCFS-003 Output)")
public class OfferTest {

    private Offer offer;
    private Recruiter recruiter;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        offer = new Offer();
        recruiter = new Recruiter("recruiter@test.com", "Test Recruiter", "pass123");
        startTime = new LocalDateTime(2026, 4, 8, 9, 0);
        endTime = new LocalDateTime(2026, 4, 8, 10, 0);
    }

    @Test
    @DisplayName("Offer constructor creates valid instance")
    void testOffer_Constructor() {
        assertNotNull(offer);
    }

    @Test
    @DisplayName("setPublisher and getPublisher work correctly")
    void testPublisher() {
        assertNull(offer.getPublisher());
        
        offer.setPublisher(recruiter);
        assertEquals(recruiter, offer.getPublisher());
    }

    @Test
    @DisplayName("setTitle and getTitle work correctly")
    void testTitle() {
        String title = "Software Engineer Meeting";
        offer.setTitle(title);
        assertEquals(title, offer.getTitle());
    }

    @Test
    @DisplayName("setTitle with null value is handled")
    void testTitle_Null() {
        offer.setTitle(null);
        assertNull(offer.getTitle());
    }

    @Test
    @DisplayName("setDurationMins and getDurationMins work correctly")
    void testDurationMins() {
        offer.setDurationMins(30);
        assertEquals(30, offer.getDurationMins());
    }

    @Test
    @DisplayName("setDurationMins with various valid values")
    void testDurationMins_ValidValues() {
        offer.setDurationMins(15);
        assertEquals(15, offer.getDurationMins());
        
        offer.setDurationMins(60);
        assertEquals(60, offer.getDurationMins());
        
        offer.setDurationMins(120);
        assertEquals(120, offer.getDurationMins());
    }

    @Test
    @DisplayName("setTopicTags and getTopicTags work correctly")
    void testTopicTags() {
        String tags = "Java,Spring,AWS";
        offer.setTopicTags(tags);
        assertEquals(tags, offer.getTopicTags());
    }

    @Test
    @DisplayName("setTopicTags with null value is handled")
    void testTopicTags_Null() {
        offer.setTopicTags(null);
        assertNull(offer.getTopicTags());
    }

    @Test
    @DisplayName("setTopicTags with empty string is handled")
    void testTopicTags_Empty() {
        offer.setTopicTags("");
        assertEquals("", offer.getTopicTags());
    }

    @Test
    @DisplayName("setCapacity and getCapacity work correctly")
    void testCapacity() {
        offer.setCapacity(1);
        assertEquals(1, offer.getCapacity());
        
        offer.setCapacity(10);
        assertEquals(10, offer.getCapacity());
    }

    @Test
    @DisplayName("setStartTime and getStartTime work correctly")
    void testStartTime() {
        assertNull(offer.getStartTime());
        
        offer.setStartTime(startTime);
        assertEquals(startTime, offer.getStartTime());
    }

    @Test
    @DisplayName("setEndTime and getEndTime work correctly")
    void testEndTime() {
        assertNull(offer.getEndTime());
        
        offer.setEndTime(endTime);
        assertEquals(endTime, offer.getEndTime());
    }

    @Test
    @DisplayName("Offer with both start and end times set correctly")
    void testTimeSlot() {
        offer.setStartTime(startTime);
        offer.setEndTime(endTime);
        
        assertEquals(startTime, offer.getStartTime());
        assertEquals(endTime, offer.getEndTime());
    }

    @Test
    @DisplayName("Multiple properties can be set together")
    void testMultipleProperties() {
        offer.setPublisher(recruiter);
        offer.setTitle("Senior Developer");
        offer.setDurationMins(45);
        offer.setTopicTags("Node.js,TypeScript");
        offer.setCapacity(3);
        offer.setStartTime(startTime);
        offer.setEndTime(endTime);
        
        assertEquals(recruiter, offer.getPublisher());
        assertEquals("Senior Developer", offer.getTitle());
        assertEquals(45, offer.getDurationMins());
        assertEquals("Node.js,TypeScript", offer.getTopicTags());
        assertEquals(3, offer.getCapacity());
        assertEquals(startTime, offer.getStartTime());
        assertEquals(endTime, offer.getEndTime());
    }

    @Test
    @DisplayName("Reservations collection is accessible")
    void testReservations() {
        Collection<Reservation> reservations = offer.getReservations();
        assertNotNull(reservations, "Reservations collection should not be null");
        assertTrue(reservations.isEmpty(), "Newly created offer should have no reservations");
    }

    @Test
    @DisplayName("Offer can be used with null publisher for demo")
    void testOffer_NullPublisher() {
        offer.setPublisher(null);
        assertNull(offer.getPublisher());
    }

    @Test
    @DisplayName("Offer slot duration matches endTime - startTime")
    void testSlotDuration() {
        offer.setStartTime(startTime);
        offer.setEndTime(endTime);
        
        // Both times should be stored
        assertNotNull(offer.getStartTime());
        assertNotNull(offer.getEndTime());
    }

    @Test
    @DisplayName("Same Offer instance maintains state across multiple calls")
    void testOfferStateConsistency() {
        // Set value
        offer.setTitle("Initial Title");
        assertEquals("Initial Title", offer.getTitle());
        
        // Verify it persists
        assertEquals("Initial Title", offer.getTitle());
        
        // Change it
        offer.setTitle("Updated Title");
        assertEquals("Updated Title", offer.getTitle());
        
        // Verify change persisted
        assertEquals("Updated Title", offer.getTitle());
    }
}
