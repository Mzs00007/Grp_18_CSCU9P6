package vcfs.models.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.core.CareerFair;
import vcfs.core.LocalDateTime;


/**
 * Comprehensive JUnit tests for AuditEntry class.
 * Tests audit trail entries recording significant system events.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("AuditEntry - System Audit Trail Entry")
public class AuditEntryTest {

    private AuditEntry auditEntry;
    private CareerFair careerFair;
    private LocalDateTime timestamp;
    private static final String VALID_EVENT_TYPE = "BOOKING_CREATED";

    @BeforeEach
    void setUp() {
        careerFair = new CareerFair("Career Fair 2026", "Tech Career Fair");
        timestamp = new LocalDateTime(2026, 4, 10, 14, 30);
        auditEntry = new AuditEntry(careerFair, timestamp, VALID_EVENT_TYPE);
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("AuditEntry constructor initializes with valid parameters")
        void testConstructorValidParameters() {
            assertNotNull(auditEntry);
            assertEquals(careerFair, auditEntry.getFair());
            assertEquals(timestamp, auditEntry.getTimestamp());
            assertEquals(VALID_EVENT_TYPE, auditEntry.getEventType());
        }

        @Test
        @DisplayName("AuditEntry constructor with different event types")
        void testConstructorDifferentEventTypes() {
            String[] eventTypes = {"PHASE_CHANGE", "BOOKING_CREATED", "BOOKING_CANCELLED", 
                                   "USER_LOGIN", "OFFER_PUBLISHED"};
            
            for (String eventType : eventTypes) {
                AuditEntry entry = new AuditEntry(careerFair, timestamp, eventType);
                assertEquals(eventType, entry.getEventType());
            }
        }
    }

    // ========== CAREER FAIR MANAGEMENT ==========

    @Nested
    @DisplayName("Career Fair Management Tests")
    class CareerFairManagementTests {

        @Test
        @DisplayName("getFair returns career fair")
        void testGetFair() {
            assertEquals(careerFair, auditEntry.getFair());
        }

        @Test
        @DisplayName("setFair updates career fair")
        void testSetFair() {
            CareerFair newFair = new CareerFair("Career Fair 2027", "Future Fair");
            auditEntry.setFair(newFair);
            assertEquals(newFair, auditEntry.getFair());
        }

        @Test
        @DisplayName("setFair accepts different career fairs")
        void testSetDifferentFairs() {
            CareerFair fair1 = new CareerFair("Fair 1", "Description 1");
            CareerFair fair2 = new CareerFair("Fair 2", "Description 2");
            
            auditEntry.setFair(fair1);
            assertEquals(fair1, auditEntry.getFair());
            
            auditEntry.setFair(fair2);
            assertEquals(fair2, auditEntry.getFair());
        }
    }

    // ========== TIMESTAMP MANAGEMENT ==========

    @Nested
    @DisplayName("Timestamp Management Tests")
    class TimestampManagementTests {

        @Test
        @DisplayName("getTimestamp returns correct timestamp")
        void testGetTimestamp() {
            assertEquals(timestamp, auditEntry.getTimestamp());
        }

        @Test
        @DisplayName("setTimestamp updates timestamp")
        void testSetTimestamp() {
            LocalDateTime newTimestamp = new LocalDateTime(2026, 5, 15, 10, 0);
            auditEntry.setTimestamp(newTimestamp);
            assertEquals(newTimestamp, auditEntry.getTimestamp());
        }

        @Test
        @DisplayName("setTimestamp with future timestamp")
        void testSetTimestampFuture() {
            LocalDateTime futureTimestamp = new LocalDateTime(2027, 1, 1, 0, 0);
            auditEntry.setTimestamp(futureTimestamp);
            assertEquals(futureTimestamp, auditEntry.getTimestamp());
        }

        @Test
        @DisplayName("setTimestamp with past timestamp")
        void testSetTimestampPast() {
            LocalDateTime pastTimestamp = new LocalDateTime(2025, 1, 1, 0, 0);
            auditEntry.setTimestamp(pastTimestamp);
            assertEquals(pastTimestamp, auditEntry.getTimestamp());
        }

        @Test
        @DisplayName("Multiple timestamp updates")
        void testMultipleTimestampUpdates() {
            LocalDateTime time1 = new LocalDateTime(2026, 4, 10, 10, 0);
            LocalDateTime time2 = new LocalDateTime(2026, 4, 10, 15, 0);
            LocalDateTime time3 = new LocalDateTime(2026, 4, 10, 20, 0);
            
            auditEntry.setTimestamp(time1);
            assertEquals(time1, auditEntry.getTimestamp());
            
            auditEntry.setTimestamp(time2);
            assertEquals(time2, auditEntry.getTimestamp());
            
            auditEntry.setTimestamp(time3);
            assertEquals(time3, auditEntry.getTimestamp());
        }
    }

    // ========== EVENT TYPE MANAGEMENT ==========

    @Nested
    @DisplayName("Event Type Management Tests")
    class EventTypeManagementTests {

        @Test
        @DisplayName("getEventType returns correct event type")
        void testGetEventType() {
            assertEquals(VALID_EVENT_TYPE, auditEntry.getEventType());
        }

        @Test
        @DisplayName("setEventType updates event type")
        void testSetEventType() {
            String newEventType = "BOOKING_CANCELLED";
            auditEntry.setEventType(newEventType);
            assertEquals(newEventType, auditEntry.getEventType());
        }

        @Test
        @DisplayName("setEventType with various event types")
        void testSetEventTypeVariousTypes() {
            String[] eventTypes = {"PHASE_CHANGE", "USER_LOGIN", "OFFER_PUBLISHED", 
                                   "RESERVATION_CONFIRMED", "SYSTEM_ERROR"};
            
            for (String eventType : eventTypes) {
                auditEntry.setEventType(eventType);
                assertEquals(eventType, auditEntry.getEventType());
            }
        }

        @Test
        @DisplayName("setEventType accepts lowercase")
        void testSetEventTypeLowercase() {
            auditEntry.setEventType("booking_created");
            assertEquals("booking_created", auditEntry.getEventType());
        }

        @Test
        @DisplayName("setEventType accepts empty string")
        void testSetEventTypeEmpty() {
            auditEntry.setEventType("");
            assertEquals("", auditEntry.getEventType());
        }
    }

    // ========== INDEPENDENT FIELD UPDATES ==========

    @Nested
    @DisplayName("Independent Field Updates Tests")
    class IndependentFieldUpdatesTests {

        @Test
        @DisplayName("Updating fair does not affect timestamp or event type")
        void testUpdateFairIndependent() {
            LocalDateTime originalTimestamp = auditEntry.getTimestamp();
            String originalEventType = auditEntry.getEventType();
            
            CareerFair newFair = new CareerFair("New Fair", "Description");
            auditEntry.setFair(newFair);
            
            assertEquals(originalTimestamp, auditEntry.getTimestamp());
            assertEquals(originalEventType, auditEntry.getEventType());
        }

        @Test
        @DisplayName("Updating timestamp does not affect fair or event type")
        void testUpdateTimestampIndependent() {
            CareerFair originalFair = auditEntry.getFair();
            String originalEventType = auditEntry.getEventType();
            
            LocalDateTime newTimestamp = new LocalDateTime(2026, 5, 1, 0, 0);
            auditEntry.setTimestamp(newTimestamp);
            
            assertEquals(originalFair, auditEntry.getFair());
            assertEquals(originalEventType, auditEntry.getEventType());
        }

        @Test
        @DisplayName("Updating event type does not affect fair or timestamp")
        void testUpdateEventTypeIndependent() {
            CareerFair originalFair = auditEntry.getFair();
            LocalDateTime originalTimestamp = auditEntry.getTimestamp();
            
            auditEntry.setEventType("NEW_EVENT");
            
            assertEquals(originalFair, auditEntry.getFair());
            assertEquals(originalTimestamp, auditEntry.getTimestamp());
        }
    }

    // ========== COMPLETE AUDIT ENTRY LIFECYCLE ==========

    @Nested
    @DisplayName("Complete Audit Entry Lifecycle Tests")
    class AuditEntryLifecycleTests {

        @Test
        @DisplayName("Create and update audit entry")
        void testCreateAndUpdateAuditEntry() {
            CareerFair fair1 = new CareerFair("Fair 1", "Desc 1");
            LocalDateTime time1 = new LocalDateTime(2026, 4, 10, 10, 0);
            
            AuditEntry entry = new AuditEntry(fair1, time1, "EVENT_TYPE_1");
            
            assertEquals(fair1, entry.getFair());
            assertEquals(time1, entry.getTimestamp());
            assertEquals("EVENT_TYPE_1", entry.getEventType());
            
            // Update all fields
            CareerFair fair2 = new CareerFair("Fair 2", "Desc 2");
            LocalDateTime time2 = new LocalDateTime(2026, 4, 10, 15, 0);
            
            entry.setFair(fair2);
            entry.setTimestamp(time2);
            entry.setEventType("EVENT_TYPE_2");
            
            assertEquals(fair2, entry.getFair());
            assertEquals(time2, entry.getTimestamp());
            assertEquals("EVENT_TYPE_2", entry.getEventType());
        }

        @Test
        @DisplayName("Multiple audit entries track different events")
        void testMultipleAuditEntries() {
            CareerFair fair = new CareerFair("Fair", "Description");
            
            AuditEntry entry1 = new AuditEntry(fair, new LocalDateTime(2026, 4, 10, 10, 0),
                                              "BOOKING_CREATED");
            AuditEntry entry2 = new AuditEntry(fair, new LocalDateTime(2026, 4, 10, 11, 0),
                                              "PHASE_CHANGED");
            AuditEntry entry3 = new AuditEntry(fair, new LocalDateTime(2026, 4, 10, 12, 0),
                                              "BOOKING_CANCELLED");
            
            assertEquals("BOOKING_CREATED", entry1.getEventType());
            assertEquals("PHASE_CHANGED", entry2.getEventType());
            assertEquals("BOOKING_CANCELLED", entry3.getEventType());
        }
    }

    // ========== EDGE CASES ==========

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("AuditEntry with special characters in event type")
        void testSpecialCharactersInEventType() {
            auditEntry.setEventType("EVENT-TYPE_2026/SPECIAL");
            assertEquals("EVENT-TYPE_2026/SPECIAL", auditEntry.getEventType());
        }

        @Test
        @DisplayName("AuditEntry with very long event type")
        void testVeryLongEventType() {
            String longEventType = "VERY_LONG_EVENT_TYPE_WITH_MANY_UNDERSCORES_AND_CHARACTERS_" +
                                  "THAT_DESCRIBES_A_COMPLEX_SYSTEM_EVENT_HAPPENING_AT_RUNTIME";
            auditEntry.setEventType(longEventType);
            assertEquals(longEventType, auditEntry.getEventType());
        }

        @Test
        @DisplayName("AuditEntry with numeric event type")
        void testNumericEventType() {
            auditEntry.setEventType("EVENT_001");
            assertEquals("EVENT_001", auditEntry.getEventType());
        }
    }
}
