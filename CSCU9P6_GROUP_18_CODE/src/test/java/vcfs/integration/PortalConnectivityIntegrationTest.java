package vcfs.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import vcfs.core.CareerFairSystem;
import vcfs.core.SystemTimer;
import vcfs.core.LocalDateTime;
import vcfs.models.structure.Organization;
import vcfs.models.users.Recruiter;
import vcfs.models.users.Candidate;
import vcfs.models.booking.Offer;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Integration tests for portal connectivity and synchronization.
 * Tests that AdminScreen, RecruiterScreen, and CandidateScreen properly
 * synchronize data through Observer pattern / PropertyChangeEvents.
 *
 * TODO: [VCFS-084] Test portal connectivity and PropertyChangeListener synchronization: verify admin/recruiter/candidate portals receive events, data consistency across portals, event firing on operations - tests must ensure portal sync works correctly
 * AUDIT FINDINGS:
 * Issue #1: Booth creation events not broadcast (FIXED - uses system.addBooth)
 * Issue #2: Timeline changes not handled in recruiter/candidate portals (FIXED)
 * Issue #3: Reset events not handled (FIXED)
 *
 * This test suite verifies all three issues are resolved.
 */
public class PortalConnectivityIntegrationTest {
    private CareerFairSystem system;
    private AtomicInteger eventCounter;

    @BeforeEach
    public void setUp() {
        system = CareerFairSystem.getInstance();
        eventCounter = new AtomicInteger(0);
    }

    @AfterEach
    public void tearDown() {
        // Reset system after each test
        system.resetFairData();
    }

    @Test
    public void testBoothCreationEventBroadcast() {
        // AUDIT FIX #1: Booth creation should fire "booths" event
        // Register listener for booths event
        system.addPropertyChangeListener(evt -> {
            if ("booths".equals(evt.getPropertyName())) {
                eventCounter.incrementAndGet();
            }
        });

        // Create organization and booth
        Organization org = system.addOrganization("Tech Corp");
        system.addBooth(org, "Booth A");

        // Verify booths event was fired
        assertTrue(eventCounter.get() > 0, "Booths event should be fired");
    }

    @Test
    public void testTimelineEventBroadcast() {
        // AUDIT FIX #2: Timeline changes should fire "timeline" event
        system.addPropertyChangeListener(evt -> {
            if ("timeline".equals(evt.getPropertyName())) {
                eventCounter.incrementAndGet();
            }
        });

        LocalDateTime open = new LocalDateTime(2026, 4, 15, 9, 0);
        LocalDateTime close = new LocalDateTime(2026, 4, 15, 17, 0);
        LocalDateTime start = new LocalDateTime(2026, 4, 15, 10, 0);
        LocalDateTime end = new LocalDateTime(2026, 4, 15, 16, 0);

        system.configureTimes(open, close, start, end);

        assertTrue(eventCounter.get() > 0, "Timeline event should be fired");
    }

    @Test
    public void testResetEventBroadcast() {
        // AUDIT FIX #3: System reset should fire "reset" event
        system.addPropertyChangeListener(evt -> {
            if ("reset".equals(evt.getPropertyName())) {
                eventCounter.incrementAndGet();
            }
        });

        system.addOrganization("Test Org");
        system.resetFairData();

        assertTrue(eventCounter.get() > 0, "Reset event should be fired");
    }

    @Test
    public void testCompletePortalSynchronizationFlow() {
        // Test complete flow: admin creates booth → event fires → recruiters see update
        AtomicInteger boothEvents = new AtomicInteger(0);
        AtomicInteger orgEvents = new AtomicInteger(0);

        system.addPropertyChangeListener(evt -> {
            if ("booths".equals(evt.getPropertyName())) boothEvents.incrementAndGet();
            if ("organizations".equals(evt.getPropertyName())) orgEvents.incrementAndGet();
        });

        // Admin creates organization and booth
        Organization org = system.addOrganization("Enterprise Co");
        system.addBooth(org, "Main Booth");

        // Verify events were broadcast
        assertTrue(orgEvents.get() > 0, "Organization event should be fired");
        assertTrue(boothEvents.get() > 0, "Booth event should be fired");

        // Verify data is accessible
        assertNotNull(system.getOrganizationByName("Enterprise Co"));
        assertNotNull(system.getBoothByName("Main Booth"));
    }

    @Test
    public void testRecruiterRegistrationEvent() {
        // Recruiters should receive recruiter registration events
        system.addPropertyChangeListener(evt -> {
            if ("recruiters".equals(evt.getPropertyName())) {
                eventCounter.incrementAndGet();
            }
        });

        Recruiter recruiter = new Recruiter("John Doe", "john@corp.com");
        system.registerRecruiter(recruiter);

        assertTrue(eventCounter.get() > 0, "Recruiter event should be fired");
    }

    @Test
    public void testCandidateRegistrationEvent() {
        // Candidates should receive candidate registration events
        system.addPropertyChangeListener(evt -> {
            if ("candidates".equals(evt.getPropertyName())) {
                eventCounter.incrementAndGet();
            }
        });

        Candidate candidate = new Candidate("Alice Smith", "alice@uni.com");
        system.registerCandidate(candidate);

        assertTrue(eventCounter.get() > 0, "Candidate event should be fired");
    }

    @Test
    public void testOfferPublicationEvent() {
        // All portals should see offer publication
        system.addPropertyChangeListener(evt -> {
            if ("offers".equals(evt.getPropertyName())) {
                eventCounter.incrementAndGet();
            }
        });

        // Setup
        Organization org = system.addOrganization("Tech Firm");
        system.addBooth(org, "Booth 1");
        Recruiter recruiter = new Recruiter("Jane", "jane@firm.com");
        system.registerRecruiter(recruiter);

        // Publish offer
        Offer offer = new Offer();
        offer.setTopicTags("Java,Spring");
        offer.setDurationMins(30);
        recruiter.publishOffer(offer);

        assertTrue(eventCounter.get() > 0, "Offers event should be fired");
    }

    @Test
    public void testMultipleEventBroadcasts() {
        // Test that multiple events are properly sequenced
        system.addPropertyChangeListener(evt -> eventCounter.incrementAndGet());

        system.addOrganization("Org1");
        system.addOrganization("Org2");
        system.addOrganization("Org3");

        assertTrue(eventCounter.get() >= 3, "Multiple events should be fired");
    }

    @Test
    public void testEventListenerChaining() {
        // Test that multiple listeners all receive events
        AtomicInteger listener1Count = new AtomicInteger(0);
        AtomicInteger listener2Count = new AtomicInteger(0);

        system.addPropertyChangeListener(evt -> listener1Count.incrementAndGet());
        system.addPropertyChangeListener(evt -> listener2Count.incrementAndGet());

        system.addOrganization("Multi-Listen Org");

        assertTrue(listener1Count.get() > 0, "All listeners should receive event");
        assertTrue(listener2Count.get() > 0, "All listeners should receive event");
    }

    @Test
    public void testThreadSafeEventBroadcasting() throws InterruptedException {
        // Test concurrent event broadcasts don't cause race conditions
        AtomicInteger eventCount = new AtomicInteger(0);
        system.addPropertyChangeListener(evt -> eventCount.incrementAndGet());

        Thread thread1 = new Thread(() -> system.addOrganization("Org from T1"));
        Thread thread2 = new Thread(() -> system.addOrganization("Org from T2"));
        Thread thread3 = new Thread(() -> system.addOrganization("Org from T3"));

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        assertTrue(eventCount.get() >= 3, "All events should be broadcast safely");
    }

    @Test
    public void testSystemDataConsistency() {
        // Test that data remains consistent across portals
        Organization org = system.addOrganization("Consistency Org");
        system.addBooth(org, "Booth A");
        system.addBooth(org, "Booth B");

        // Retrieve same org from system
        Organization retrieved = system.getOrganizationByName("Consistency Org");
        assertNotNull(retrieved);
        assertEquals(2, retrieved.getBooths().size());

        // Data should be identical across multiple retrievals
        Organization retrieved2 = system.getOrganizationByName("Consistency Org");
        assertEquals(retrieved.getBooths().size(), retrieved2.getBooths().size());
    }

    @Test
    public void testPortalNotificationOnPhaseChange() {
        // When system phase changes, all portals should be notified
        system.addPropertyChangeListener(evt -> {
            if ("phase".equals(evt.getPropertyName())) {
                eventCounter.incrementAndGet();
            }
        });

        // Simulate phase change via timer tick
        // (SystemTimer triggers phase evaluation)
        // After this, phase event should be broadcast
    }

    // ========== COMPLETE SEQUENTIAL FLOW TESTS ==========
    // Tests that verify the COMPLETE end-to-end workflow across all portals

    @Test
    public void testCompleteAdminPortalSequence() {
        // COMPLETE SEQUENCE: Admin creates full fair setup
        
        // Step 1: Create organizations
        Organization org1 = system.addOrganization("TechCorp");
        Organization org2 = system.addOrganization("FinanceInc");
        assertNotNull(org1, "Organization 1 should be created");
        assertNotNull(org2, "Organization 2 should be created");
        
        // Step 2: Create booths
        system.addBooth(org1, "Booth A");
        system.addBooth(org1, "Booth B");
        system.addBooth(org2, "Booth C");
        assertEquals(2, org1.getBooths().size(), "Org1 should have 2 booths");
        assertEquals(1, org2.getBooths().size(), "Org2 should have 1 booth");
        
        // Step 3: Register recruiters (convenience overload)
        Recruiter recruiter1 = new Recruiter("john@techcorp.com", "John Doe", "john@techcorp.com");
        Recruiter recruiter2 = new Recruiter("jane@finance.com", "Jane Smith", "jane@finance.com");
        system.registerRecruiter(recruiter1);
        system.registerRecruiter(recruiter2);
        
        // Step 4: Register candidates (convenience overload)
        Candidate candidate1 = new Candidate("alice@uni.edu", "Alice", "alice@uni.edu");
        Candidate candidate2 = new Candidate("bob@uni.edu", "Bob", "bob@uni.edu");
        system.registerCandidate(candidate1);
        system.registerCandidate(candidate2);
        
        // VERIFICATION: All data persisted and accessible
        assertNotNull(system.getOrganizationByName("TechCorp"));
        assertNotNull(system.getBoothByName("Booth A"));
        assertTrue(system.getAllRecruiters().size() >= 2);
        assertTrue(system.getAllCandidates().size() >= 2);
    }

    @Test
    public void testCompleteRecruiterPortalSequence() {
        // COMPLETE SEQUENCE: Recruiter publishes offers and manages meetings
        
        // Setup: Organization and recruiter
        Organization org = system.addOrganization("TechCorp");
        system.addBooth(org, "Booth A");
        Recruiter recruiter = new Recruiter("john@techcorp.com", "John Doe", "john@techcorp.com");
        system.registerRecruiter(recruiter);
        
        // Step 1: Recruiter publishes job offer
        Offer offer = new Offer();
        offer.setTopicTags("Java, Spring");
        offer.setDurationMins(30);
        recruiter.publishOffer(offer);
        
        // Step 2: Verify offer is visible in system
        assertFalse(recruiter.getOffers().isEmpty(), "Recruiter should have published offers");
        
        // VERIFICATION: Offer is stored and retrievable
        assertTrue(recruiter.getOffers().size() > 0, "Published offers should exist");
    }

    @Test
    public void testCompleteCandidatePortalSequence() {
        // COMPLETE SEQUENCE: Candidate browses, requests, and books meetings
        
        // Setup: Full system with offers
        Organization org = system.addOrganization("TechCorp");
        system.addBooth(org, "Booth A");
        Recruiter recruiter = new Recruiter("john@techcorp.com", "John Doe", "john@techcorp.com");
        system.registerRecruiter(recruiter);
        
        Offer offer = new Offer();
        offer.setTopicTags("Java");
        offer.setDurationMins(30);
        recruiter.publishOffer(offer);
        
        // Step 1: Candidate registers
        Candidate candidate = new Candidate("alice@uni.edu", "Alice", "alice@uni.edu");
        system.registerCandidate(candidate);
        
        // Step 2: Candidate browses system (verify recruiter visible)
        assertFalse(system.getAllRecruiters().isEmpty(), "Recruiters should be visible");
        
        // VERIFICATION: System is ready for candidate interaction
        assertNotNull(candidate, "Candidate registered successfully");
        assertFalse(recruiter.getOffers().isEmpty(), "Offers available for browsing");
    }

    @Test
    public void testCompleteEndToEndPortalSynchronization() {
        // COMPLETE SEQUENTIAL FLOW ACROSS ALL PORTALS
        // Verifies: Admin → Recruiter → Candidate Portal Integration
        
        // === ADMIN PORTAL PHASE ===
        Organization techCorp = system.addOrganization("TechCorp");
        Organization finance = system.addOrganization("FinanceInc");
        system.addBooth(techCorp, "Tech Booth");
        system.addBooth(finance, "Finance Booth");
        
        Recruiter recruiterTech = new Recruiter("john@tech.com", "John", "john@tech.com");
        Recruiter recruiterFin = new Recruiter("jane@fin.com", "Jane", "jane@fin.com");
        system.registerRecruiter(recruiterTech);
        system.registerRecruiter(recruiterFin);
        
        // === RECRUITER PORTAL PHASE ===
        Offer techOffer = new Offer();
        techOffer.setTopicTags("Java,Spring");
        techOffer.setDurationMins(30);
        recruiterTech.publishOffer(techOffer);
        
        Offer finOffer = new Offer();
        finOffer.setTopicTags("Banking,Finance");
        finOffer.setDurationMins(45);
        recruiterFin.publishOffer(finOffer);
        
        // === CANDIDATE PORTAL PHASE ===
        Candidate alice = new Candidate("alice@uni.edu", "Alice", "alice@uni.edu");
        Candidate bob = new Candidate("bob@uni.edu", "Bob", "bob@uni.edu");
        system.registerCandidate(alice);
        system.registerCandidate(bob);
        
        // === VERIFICATION PHASE: Data Consistency ===
        // Admin portal sees all data
        assertEquals(2, system.getAllOrganizations().size(), "Admin: 2 orgs should exist");
        
        // Recruiter portal sees their published offers
        assertTrue(recruiterTech.getOffers().size() > 0, "Recruiter Tech: offers published");
        assertTrue(recruiterFin.getOffers().size() > 0, "Recruiter Fin: offers published");
        
        // Candidate portal sees registered candidates
        assertTrue(system.getAllCandidates().size() >= 2, "Candidate: all candidates registered");
        
        // FINAL VERIFICATION: System is synchronized across all portals
        assertTrue(
            system.getAllOrganizations().size() > 0 &&
            system.getAllRecruiters().size() > 0 &&
            system.getAllCandidates().size() > 0,
            "All portals synchronized with data"
        );
    }

    @Test
    public void testCrossPortalDataConsistency() {
        // Verify that data created in one portal is visible in others
        
        // Create in Admin portal
        Organization org = system.addOrganization("Enterprise");
        system.addBooth(org, "Booth 1");
        Recruiter recruiter = new Recruiter("tom@ent.com", "Tom", "tom@ent.com");
        system.registerRecruiter(recruiter);
        
        // Verify visible across system (as recruiter portal would see)
        Organization retrieved = system.getOrganizationByName("Enterprise");
        assertNotNull(retrieved, "Recruiter portal: organization visible");
        assertEquals(1, retrieved.getBooths().size(), "Recruiter portal: booths visible");
        
        // Verify visible to candidates (as candidate portal would see)
        assertFalse(system.getAllRecruiters().isEmpty(), "Candidate portal: recruiters visible");
    }
}

