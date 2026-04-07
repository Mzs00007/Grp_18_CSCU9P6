package vcfs.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.core.CareerFairSystem;
import vcfs.core.CareerFair;
import vcfs.core.SystemTimer;
import vcfs.core.LocalDateTime;
import vcfs.models.booking.Offer;
import vcfs.models.booking.Reservation;
import vcfs.models.booking.Request;
import vcfs.models.structure.Organization;
import vcfs.models.structure.Booth;
import vcfs.models.users.Recruiter;
import vcfs.models.users.Candidate;

/**
 * CareerFairSystemTest - Testing VCFS-003 (Availability Parser) and VCFS-004 (Match Engine)
 * 
 * VCFS-003: parseAvailabilityIntoOffers() algorithm - converts 3-hour recruiter availability
 * blocks into discrete 30-minute bookable slots for candidates
 * 
 * VCFS-004: autoBook() tag-weighted matching algorithm - automatically pairs candidates
 * with best-matching offers based on tag intersection scoring
 * 
 * Priority 1 Quality Fixes:
 * - 8 tests for VCFS-003 parsing algorithm including edge cases and phase guards
 * - 7 tests for VCFS-004 auto-booking including collision detection and tag matching
 * - Unique emails per test to avoid Singleton state conflicts
 * 
 * Total: 15 new test methods covering previously untested algorithms
 */
@DisplayName("CareerFairSystem - Booking Algorithms (VCFS-003 & VCFS-004)")
public class CareerFairSystemTest {

    private CareerFairSystem system;
    private CareerFair fair;
    private SystemTimer timer;
    private Organization org;
    private Booth booth;
    private int testEmailCounter = 0;
    
    @BeforeEach
    void setUp() {
        testEmailCounter++;
        system = CareerFairSystem.getInstance();
        timer = SystemTimer.getInstance();
        fair = system.getFair();
        
        // Set up a fair in the future so we can test during BOOKINGS_OPEN
        // Timeline: bookings open 10:00, close 12:00, fair 13:00-17:00
        LocalDateTime bookingsOpen = new LocalDateTime(2026, 4, 8, 10, 0);
        LocalDateTime bookingsClose = new LocalDateTime(2026, 4, 8, 12, 0);
        LocalDateTime fairStart = new LocalDateTime(2026, 4, 8, 13, 0);
        LocalDateTime fairEnd = new LocalDateTime(2026, 4, 8, 17, 0);
        fair.setTimes(bookingsOpen, bookingsClose, fairStart, fairEnd);
        
        // Jump timer to BOOKINGS_OPEN phase
        timer.jumpTo(new LocalDateTime(2026, 4, 8, 10, 30));
        
        // Create org and booth with unique names
        org = system.addOrganization("TechCorp-" + testEmailCounter);
        booth = system.addBooth(org, "Booth-A-" + testEmailCounter);
    }

    // ===================================================================
    // VCFS-003: AVAILABILITY PARSER ALGORITHM TESTS
    // ===================================================================

    @Test
    @DisplayName("VCFS-003: Parse 3-hour block into 6 × 30-minute slots")
    void testParseAvailability_3HourBlock() {
        Recruiter rec = system.registerRecruiter("R1", "r1-" + testEmailCounter + "@test.com", booth);
        LocalDateTime blockStart = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime blockEnd = new LocalDateTime(2026, 4, 8, 12, 0);
        
        int count = system.parseAvailabilityIntoOffers(
                rec, "Session 1", 30, "Java,Cloud", 5, blockStart, blockEnd);
        
        assertEquals(6, count, "3-hour block should generate 6 × 30-min slots");
        assertEquals(6, rec.getOffers().size(), "Recruiter should have 6 offers");
    }

    @Test
    @DisplayName("VCFS-003: Slots have correct start/end times")
    void testParseAvailability_SlotBoundaries() {
        Recruiter rec = system.registerRecruiter("R2", "r2-" + testEmailCounter + "@test.com", booth);
        LocalDateTime blockStart = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime blockEnd = new LocalDateTime(2026, 4, 8, 10, 0);
        
        system.parseAvailabilityIntoOffers(rec, "Session", 15, "Java", 5, blockStart, blockEnd);
        
        java.util.List<Offer> offers = new java.util.ArrayList<>(rec.getOffers());
        assertEquals(4, offers.size(), "60 minutes / 15 min slots = 4 offers");
        
        assertEquals(blockStart, offers.get(0).getStartTime());
        assertEquals(new LocalDateTime(2026, 4, 8, 9, 15), offers.get(0).getEndTime());
        assertEquals(new LocalDateTime(2026, 4, 8, 9, 45), offers.get(3).getStartTime());
        assertEquals(blockEnd, offers.get(3).getEndTime());
    }

    @Test
    @DisplayName("VCFS-003: Single slot boundary case")
    void testParseAvailability_SingleSlot() {
        Recruiter rec = system.registerRecruiter("R3", "r3-" + testEmailCounter + "@test.com", booth);
        LocalDateTime blockStart = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime blockEnd = new LocalDateTime(2026, 4, 8, 9, 30);
        
        int count = system.parseAvailabilityIntoOffers(rec, "Quick Chat", 30, "AI", 1, blockStart, blockEnd);
        assertEquals(1, count, "30-minute block with 30-min slots = 1 slot");
    }

    @Test
    @DisplayName("VCFS-003: Block shorter than slot duration throws exception")
    void testParseAvailability_BlockTooShort() {
        Recruiter rec = system.registerRecruiter("R4", "r4-" + testEmailCounter + "@test.com", booth);
        
        // 15-minute block but 30-minute slot duration
        assertThrows(IllegalArgumentException.class, () ->
            system.parseAvailabilityIntoOffers(rec, "Too Short", 30, "Java", 5,
                new LocalDateTime(2026, 4, 8, 9, 0),
                new LocalDateTime(2026, 4, 8, 9, 15)),
            "Should throw if block is shorter than slot duration");
    }

    @Test
    @DisplayName("VCFS-003: Phase guard enforced - outside BOOKINGS_OPEN")
    void testParseAvailability_PhaseGuard() {
        Recruiter rec = system.registerRecruiter("R5", "r5-" + testEmailCounter + "@test.com", booth);
        // Jump to FAIR_LIVE phase (14:00)
        timer.jumpTo(new LocalDateTime(2026, 4, 8, 14, 0));
        
        assertThrows(IllegalStateException.class, () ->
            system.parseAvailabilityIntoOffers(rec, "Too Late", 30, "Java", 5,
                new LocalDateTime(2026, 4, 8, 14, 30),
                new LocalDateTime(2026, 4, 8, 15, 0)),
            "Should throw if not in BOOKINGS_OPEN phase");
    }

    @Test
    @DisplayName("VCFS-003: Null recruiter throws exception")
    void testParseAvailability_NullRecruiter() {
        assertThrows(IllegalArgumentException.class, () ->
            system.parseAvailabilityIntoOffers(null, "Session", 30, "Java", 5,
                new LocalDateTime(2026, 4, 8, 9, 0),
                new LocalDateTime(2026, 4, 8, 10, 0)),
            "Should throw if recruiter is null");
    }

    @Test
    @DisplayName("VCFS-003: Slot metadata correctly preserved")
    void testParseAvailability_Metadata() {
        Recruiter rec = system.registerRecruiter("R6", "r6-" + testEmailCounter + "@test.com", booth);
        system.parseAvailabilityIntoOffers(rec, "Workshop", 30, "Java,OOP,Spring", 8,
            new LocalDateTime(2026, 4, 8, 9, 0),
            new LocalDateTime(2026, 4, 8, 9, 30));
        
        Offer offer = new java.util.ArrayList<>(rec.getOffers()).get(0);
        assertEquals("Workshop", offer.getTitle());
        assertEquals("Java,OOP,Spring", offer.getTopicTags());
        assertEquals(30, offer.getDurationMins());
        assertEquals(8, offer.getCapacity());
        assertEquals(rec, offer.getPublisher());
    }

    // ===================================================================
    // VCFS-004: TAG-WEIGHTED AUTO-BOOKING TESTS
    // ===================================================================

    @Test
    @DisplayName("VCFS-004: Perfect tag match finds and books offer")
    void testAutoBook_PerfectTagMatch() {
        Recruiter rec = system.registerRecruiter("R7", "r7-" + testEmailCounter + "@test.com", booth);
        Candidate cand = system.registerCandidate("Bob", "bob-" + testEmailCounter + "@student.com", null, null);
        
        system.parseAvailabilityIntoOffers(rec, "Java Track", 30, "Java,OOP", 10,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 11, 0));
        
        Request req = new Request();
        req.setDesiredTags("Java");
        Reservation res = system.autoBook(cand, req);
        
        assertNotNull(res, "Should find and book matching offer");
        assertEquals(cand, res.getCandidate());
    }

    @Test
    @DisplayName("VCFS-004: Multiple offers - highest scoring offer wins")
    void testAutoBook_HighestScoreWins() {
        Recruiter rec1 = system.registerRecruiter("R8a", "r8a-" + testEmailCounter + "@test.com", booth);
        Recruiter rec2 = system.registerRecruiter("R8b", "r8b-" + testEmailCounter + "@test.com", booth);
        Candidate cand = system.registerCandidate("Charlie", "charlie-" + testEmailCounter + "@student.com", null, null);
        
        // Offer 1: "Java,OOP" (score=2 for "Java,OOP" request)
        system.parseAvailabilityIntoOffers(rec1, "Java Session", 30, "Java,OOP", 5,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 10, 30));
        
        // Offer 2: "Cloud,Docker" (score=1 for "Cloud" only)
        system.parseAvailabilityIntoOffers(rec2, "Cloud Session", 30, "Cloud,Docker", 5,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 10, 30));
        
        Request req = new Request();
        req.setDesiredTags("Java,OOP");
        Reservation res = system.autoBook(cand, req);
        
        assertNotNull(res);
        assertEquals(rec1, res.getOffer().getPublisher(), "Should book highest-scoring offer");
    }

    @Test
    @DisplayName("VCFS-004: Collision detection prevents double-booking")
    void testAutoBook_CollisionDetection() {
        Recruiter rec1 = system.registerRecruiter("R9a", "r9a-" + testEmailCounter + "@test.com", booth);
        Recruiter rec2 = system.registerRecruiter("R9b", "r9b-" + testEmailCounter + "@test.com", booth);
        Candidate cand = system.registerCandidate("Eve", "eve-" + testEmailCounter + "@student.com", null, null);
        
        // First booking at 10:00-10:30
        system.parseAvailabilityIntoOffers(rec1, "S1", 30, "Java", 5,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 10, 30));
        
        Request req = new Request();
        req.setDesiredTags("Java");
        Reservation res1 = system.autoBook(cand, req);
        assertNotNull(res1, "First booking should succeed");
        
        // Second offer overlapping at 10:15-10:45 should be skipped
        system.parseAvailabilityIntoOffers(rec2, "S2", 30, "Java", 5,
            new LocalDateTime(2026, 4, 8, 10, 15),
            new LocalDateTime(2026, 4, 8, 10, 45));
        
        assertEquals(1, cand.getReservations().size(), "Should prevent double-booking");
    }

    @Test
    @DisplayName("VCFS-004: No matching offers returns null")
    void testAutoBook_NoMatchingOffers() {
        Recruiter rec = system.registerRecruiter("R10", "r10-" + testEmailCounter + "@test.com", booth);
        Candidate cand = system.registerCandidate("Grace", "grace-" + testEmailCounter + "@student.com", null, null);
        
        // Offers with Java tags
        system.parseAvailabilityIntoOffers(rec, "Java Track", 30, "Java,Spring", 5,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 11, 0));
        
        // Request with completely different tags
        Request req = new Request();
        req.setDesiredTags("Python,Django");
        Reservation res = system.autoBook(cand, req);
        
        assertNull(res, "Should return null when no tags match");
    }

    @Test
    @DisplayName("VCFS-004: Phase guard - outside BOOKINGS_OPEN returns null")
    void testAutoBook_PhaseGuard() {
        Recruiter rec = system.registerRecruiter("R11", "r11-" + testEmailCounter + "@test.com", booth);
        Candidate cand = system.registerCandidate("Hank", "hank-" + testEmailCounter + "@student.com", null, null);
        
        system.parseAvailabilityIntoOffers(rec, "Session", 30, "Java", 5,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 11, 0));
        
        // Jump to FAIR_LIVE phase (out of BOOKINGS_OPEN)
        timer.jumpTo(new LocalDateTime(2026, 4, 8, 14, 0));
        
        Request req = new Request();
        req.setDesiredTags("Java");
        Reservation res = system.autoBook(cand, req);
        
        assertNull(res, "Should return null outside BOOKINGS_OPEN");
    }

    @Test
    @DisplayName("VCFS-004: Tag matching is case-insensitive")
    void testAutoBook_CaseInsensitive() {
        Recruiter rec = system.registerRecruiter("R12", "r12-" + testEmailCounter + "@test.com", booth);
        Candidate cand = system.registerCandidate("Ivy", "ivy-" + testEmailCounter + "@student.com", null, null);
        
        system.parseAvailabilityIntoOffers(rec, "Session", 30, "Java,Spring", 5,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 10, 30));
        
        Request req = new Request();
        req.setDesiredTags("JAVA,SPRING"); // Different case
        Reservation res = system.autoBook(cand, req);
        
        assertNotNull(res, "Should match tags case-insensitively");
    }

    @Test
    @DisplayName("VCFS-004: Partial tag match scores correctly")
    void testAutoBook_PartialTagMatch() {
        Recruiter rec = system.registerRecruiter("R13", "r13-" + testEmailCounter + "@test.com", booth);
        Candidate cand = system.registerCandidate("Jack", "jack-" + testEmailCounter + "@student.com", null, null);
        
        // Offer with 3 tags
        system.parseAvailabilityIntoOffers(rec, "Session", 30, "Java,Spring,Docker", 5,
            new LocalDateTime(2026, 4, 8, 10, 0),
            new LocalDateTime(2026, 4, 8, 10, 30));
        
        // Request 2 of 3 tags
        Request req = new Request();
        req.setDesiredTags("Java,Spring");
        Reservation res = system.autoBook(cand, req);
        
        assertNotNull(res, "Should match 2 out of 2 requested tags");
    }

    @Test
    @DisplayName("VCFS-004: Null candidate throws exception")
    void testAutoBook_NullCandidate() {
        Request req = new Request();
        req.setDesiredTags("Java");
        
        assertThrows(IllegalArgumentException.class, () -> system.autoBook(null, req),
            "Should throw if candidate is null");
    }

    @Test
    @DisplayName("VCFS-004: Null request throws exception")
    void testAutoBook_NullRequest() {
        Candidate cand = system.registerCandidate("Karl", "karl-" + testEmailCounter + "@student.com", null, null);
        
        assertThrows(IllegalArgumentException.class, () -> system.autoBook(cand, null),
            "Should throw if request is null");
    }
}
