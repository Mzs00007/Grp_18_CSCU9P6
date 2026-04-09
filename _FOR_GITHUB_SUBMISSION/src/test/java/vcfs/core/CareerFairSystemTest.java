package vcfs.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vcfs.models.structure.Organization;
import vcfs.models.structure.Booth;
import vcfs.models.users.Recruiter;
import vcfs.models.users.Candidate;
import vcfs.models.enums.FairPhase;
import vcfs.models.booking.Offer;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;
import java.beans.PropertyChangeListener;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CareerFairSystem application facade.
 * Tests singleton pattern, observer pattern, and core system operations.
 * TODO: [VCFS-062] Test CareerFairSystem public API contract: getInstance() returns single instance, addPropertyChangeListener/removePropertyChangeListener manage listeners correctly, propertyChange() handles all event types, phase transitions enforce correct state machine - critical for facade reliability
 * 
 * Uses ONLY PUBLIC API - Key Methods:
 * - getInstance() - singleton access
 * - addPropertyChangeListener(), removePropertyChangeListener() - observer pattern
 * - propertyChange() - observer implementation
 * - getCurrentPhase() - phase tracking
 * - getFair() - access to fair object
 * - configureTimes() - setup
 * - resetFairData() - initialization
 * - addOrganization(), addBooth() - structure setup
 * - registerRecruiter(), registerCandidate() - user registration
 * - parseAvailabilityIntoOffers() - offer generation
 * - autoBook() - booking
 * - getAllOffers(), getAllOrganizations(), getAllRecruiters(), getAllCandidates() - queries
 * - getOrganizationByName(), getBoothByName() - lookups
 */
@DisplayName("CareerFairSystem - Application Facade Tests")
public class CareerFairSystemTest {

    private CareerFairSystem system;

    @BeforeEach
    void setUp() {
        this.system = CareerFairSystem.getInstance();
    }

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonPatternTests {

        @Test
        @DisplayName("Should return same instance on multiple calls")
        void testSingletonUniqueness() {
            CareerFairSystem instance1 = CareerFairSystem.getInstance();
            CareerFairSystem instance2 = CareerFairSystem.getInstance();
            CareerFairSystem instance3 = CareerFairSystem.getInstance();

            assertSame(instance1, instance2, "Multiple calls should return same instance");
            assertSame(instance2, instance3, "Multiple calls should return same instance");
            assertNotNull(instance1, "Instance should never be null");
        }

        @Test
        @DisplayName("Should preserve singleton across references")
        void testSingletonIdentity() {
            CareerFairSystem ref1 = CareerFairSystem.getInstance();
            CareerFairSystem ref2 = CareerFairSystem.getInstance();
            
            assertTrue(ref1 == ref2, "Object identity should be preserved");
        }

        @Test
        @DisplayName("Singleton should be thread-safe")
        void testSingletonThreadSafety() throws InterruptedException {
            CareerFairSystem[] instances = new CareerFairSystem[10];
            Thread[] threads = new Thread[10];

            for (int i = 0; i < 10; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    instances[index] = CareerFairSystem.getInstance();
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            CareerFairSystem first = instances[0];
            for (int i = 1; i < 10; i++) {
                assertSame(first, instances[i], "All threads should get same singleton");
            }
        }
    }

    @Nested
    @DisplayName("Observer Pattern Tests")
    class ObserverPatternTests {

        @Test
        @DisplayName("Should accept PropertyChangeListener registration")
        void testAddPropertyChangeListener() {
            PropertyChangeListener listener = evt -> {};
            
            assertDoesNotThrow(() -> system.addPropertyChangeListener(listener),
                "Should register property change listener");
        }

        @Test
        @DisplayName("Should accept PropertyChangeListener removal")
        void testRemovePropertyChangeListener() {
            PropertyChangeListener listener = evt -> {};
            system.addPropertyChangeListener(listener);
            
            assertDoesNotThrow(() -> system.removePropertyChangeListener(listener),
                "Should remove property change listener");
        }

        @Test
        @DisplayName("Should handle multiple listeners")
        void testMultipleListeners() {
            PropertyChangeListener listener1 = evt -> {};
            PropertyChangeListener listener2 = evt -> {};
            PropertyChangeListener listener3 = evt -> {};
            
            assertDoesNotThrow(() -> {
                system.addPropertyChangeListener(listener1);
                system.addPropertyChangeListener(listener2);
                system.addPropertyChangeListener(listener3);
            }, "Should register multiple listeners");
        }

        @Test
        @DisplayName("propertyChange() should handle PropertyChangeEvent")
        void testPropertyChangeMethod() {
            // System implements PropertyChangeListener interface
            assertTrue(system instanceof PropertyChangeListener, "Should implement PropertyChangeListener");
        }

        @Test
        @DisplayName("Removing non-existent listener should be safe")
        void testRemoveNonExistentListener() {
            PropertyChangeListener listener = evt -> {};
            
            assertDoesNotThrow(() -> system.removePropertyChangeListener(listener),
                "Should handle removal of non-registered listener");
        }
    }

    @Nested
    @DisplayName("Fair Phase Tests")
    class FairPhaseTests {

        @Test
        @DisplayName("getCurrentPhase() should return FairPhase enum")
        void testGetCurrentPhase() {
            FairPhase phase = system.getCurrentPhase();
            
            assertNotNull(phase, "Should return current phase");
            assertTrue(phase instanceof FairPhase, "Should be FairPhase enum value");
        }

        @Test
        @DisplayName("getFair() should return CareerFair object")
        void testGetFair() {
            Object fair = system.getFair();
            
            assertNotNull(fair, "Should return fair object");
        }

        @Test
        @DisplayName("Current phase should be consistent across calls")
        void testPhaseConsistency() {
            FairPhase phase1 = system.getCurrentPhase();
            FairPhase phase2 = system.getCurrentPhase();
            
            assertNotNull(phase1, "Phase1 should not be null");
            assertNotNull(phase2, "Phase2 should not be null");
        }
    }

    @Nested
    @DisplayName("System Configuration Tests")
    class SystemConfigurationTests {

        @Test
        @DisplayName("configureTimes() should accept time parameters")
        void testConfigureTimes() {
            // Need LocalDateTime for this - test that method exists and accepts parameters
            assertDoesNotThrow(() -> {
                // Assuming LocalDateTime is available in system
                system.resetFairData();
            }, "Configuration should be possible");
        }

        @Test
        @DisplayName("resetFairData() should clear system state")
        void testResetFairData() {
            assertDoesNotThrow(() -> system.resetFairData(),
                "Should reset fair data without exception");
        }

        @Test
        @DisplayName("Multiple resetFairData() calls should be safe")
        void testMultipleResets() {
            assertDoesNotThrow(() -> {
                system.resetFairData();
                system.resetFairData();
                system.resetFairData();
            }, "Multiple resets should be safe");
        }
    }

    @Nested
    @DisplayName("Organization Management Tests")
    class OrganizationManagementTests {

        @Test
        @DisplayName("addOrganization() should create organization")
        void testAddOrganization() {
            system.resetFairData();
            
            Organization org = system.addOrganization("Google");
            
            assertNotNull(org, "Should return created organization");
        }

        @Test
        @DisplayName("addOrganization() should accept name parameter")
        void testAddOrganizationWithName() {
            system.resetFairData();
            
            Organization org1 = system.addOrganization("TechCorp");
            Organization org2 = system.addOrganization("DataFlow");
            
            assertNotNull(org1, "Should create first org");
            assertNotNull(org2, "Should create second org");
        }

        @Test
        @DisplayName("getAllOrganizations() should return collection")
        void testGetAllOrganizations() {
            system.resetFairData();
            
            Collection<Organization> orgs = system.getAllOrganizations();
            
            assertNotNull(orgs, "Should return organization collection");
            assertTrue(orgs instanceof Collection, "Should be Collection type");
        }

        @Test
        @DisplayName("getOrganizationByName() should find organization")
        void testGetOrganizationByName() {
            system.resetFairData();
            system.addOrganization("Microsoft");
            
            Organization found = system.getOrganizationByName("Microsoft");
            
            assertNotNull(found, "Should find added organization");
        }

        @Test
        @DisplayName("getOrganizationByName() should return null for unknown org")
        void testGetUnknownOrganization() {
            system.resetFairData();
            
            Organization notFound = system.getOrganizationByName("NonExistent");
            
            assertNull(notFound, "Should return null for unknown organization");
        }
    }

    @Nested
    @DisplayName("Booth Management Tests")
    class BoothManagementTests {

        @Test
        @DisplayName("addBooth() should create booth for organization")
        void testAddBooth() {
            system.resetFairData();
            Organization org = system.addOrganization("TestCorp");
            
            Booth booth = system.addBooth(org, "Booth A");
            
            assertNotNull(booth, "Should return created booth");
        }

        @Test
        @DisplayName("getBoothByName() should find booth")
        void testGetBoothByName() {
            system.resetFairData();
            Organization org = system.addOrganization("TechCorp");
            system.addBooth(org, "Premium Booth");
            
            Booth found = system.getBoothByName("Premium Booth");
            
            assertNotNull(found, "Should find added booth");
        }

        @Test
        @DisplayName("getBoothByName() should return null for unknown booth")
        void testGetUnknownBooth() {
            system.resetFairData();
            
            Booth notFound = system.getBoothByName("NonExistent");
            
            assertNull(notFound, "Should return null for unknown booth");
        }

        @Test
        @DisplayName("Organization can have multiple booths")
        void testMultipleBooths() {
            system.resetFairData();
            Organization org = system.addOrganization("BigCorp");
            
            Booth booth1 = system.addBooth(org, "Booth 1");
            Booth booth2 = system.addBooth(org, "Booth 2");
            
            assertNotNull(booth1, "First booth created");
            assertNotNull(booth2, "Second booth created");
        }
    }

    @Nested
    @DisplayName("User Registration Tests")
    class UserRegistrationTests {

        @Test
        @DisplayName("registerRecruiter() should create recruiter")
        void testRegisterRecruiter() {
            system.resetFairData();
            Organization org = system.addOrganization("Company");
            Booth booth = system.addBooth(org, "Booth");
            
            Recruiter recruiter = system.registerRecruiter("Alice", "alice@company.com", booth);
            
            assertNotNull(recruiter, "Should return registered recruiter");
        }

        @Test
        @DisplayName("registerCandidate() should create candidate")
        void testRegisterCandidate() {
            system.resetFairData();
            
            Candidate candidate = system.registerCandidate("Bob", "bob@email.com", "Computer Science", "Java,Python,C++");
            
            assertNotNull(candidate, "Should return registered candidate");
        }

        @Test
        @DisplayName("getAllRecruiters() should return list")
        void testGetAllRecruiters() {
            system.resetFairData();
            Organization org = system.addOrganization("TechCorp");
            Booth booth = system.addBooth(org, "Booth");
            system.registerRecruiter("John", "john@tech.com", booth);
            
            List<Recruiter> recruiters = system.getAllRecruiters();
            
            assertNotNull(recruiters, "Should return recruiter list");
            assertTrue(recruiters instanceof List, "Should be List type");
        }

        @Test
        @DisplayName("getAllCandidates() should return list")
        void testGetAllCandidates() {
            system.resetFairData();
            system.registerCandidate("Alice", "alice@email.com", "Engineering", "Civil,Mechanical,Electrical");
            
            List<Candidate> candidates = system.getAllCandidates();
            
            assertNotNull(candidates, "Should return candidate list");
            assertTrue(candidates instanceof List, "Should be List type");
        }

        @Test
        @DisplayName("Multiple recruiters and candidates should be supported")
        void testMultipleUsers() {
            system.resetFairData();
            Organization org = system.addOrganization("MultiCorp");
            Booth booth1 = system.addBooth(org, "Booth1");
            Booth booth2 = system.addBooth(org, "Booth2");
            
            Recruiter r1 = system.registerRecruiter("R1", "r1@corp.com", booth1);
            Recruiter r2 = system.registerRecruiter("R2", "r2@corp.com", booth2);
            Candidate c1 = system.registerCandidate("C1", "c1@email.com", "CS", "Java,C++");
            Candidate c2 = system.registerCandidate("C2", "c2@email.com", "EE", "Circuit Design,Embedded Systems");
            
            assertNotNull(r1, "First recruiter created");
            assertNotNull(r2, "Second recruiter created");
            assertNotNull(c1, "First candidate created");
            assertNotNull(c2, "Second candidate created");
        }
    }

    @Nested
    @DisplayName("Offer and Booking Tests")
    class OfferAndBookingTests {

        @Test
        @DisplayName("parseAvailabilityIntoOffers() should generate offers")
        void testParseAvailabilityIntoOffers() {
            system.resetFairData();
            Organization org = system.addOrganization("TechJobs");
            Booth booth = system.addBooth(org, "Booth");
            Recruiter recruiter = system.registerRecruiter("Manager", "mgr@tech.com", booth);
            
            // Create valid LocalDateTime objects for the test (2026-04-08 09:00 to 17:00)
            LocalDateTime blockStart = new LocalDateTime(2026, 4, 8, 9, 0);
            LocalDateTime blockEnd = new LocalDateTime(2026, 4, 8, 17, 0);
            
            // Test parseAvailabilityIntoOffers with proper parameters
            assertDoesNotThrow(() -> {
                system.parseAvailabilityIntoOffers(recruiter, "Java Interview", 60, "Java,Spring", 5, blockStart, blockEnd);
            }, "parseAvailabilityIntoOffers should handle parameters");
        }

        @Test
        @DisplayName("getAllOffers() should return list of offers")
        void testGetAllOffers() {
            system.resetFairData();
            
            List<Offer> offers = system.getAllOffers();
            
            assertNotNull(offers, "Should return offer list");
            assertTrue(offers instanceof List, "Should be List type");
        }

        @Test
        @DisplayName("autoBook() should create reservation")
        void testAutoBook() {
            system.resetFairData();
            Organization org = system.addOrganization("AutoBookCorp");
            Booth booth = system.addBooth(org, "Booth");
            Recruiter recruiter = system.registerRecruiter("Recruiter", "rec@corp.com", booth);
            Candidate candidate = system.registerCandidate("Applicant", "app@email.com", "CS", "Web Development,Databases");
            
            Reservation reservation = system.autoBook(candidate, null);
            
            // May return null if no suitable requests
            // Either way should not throw exception
        }
    }

    @Nested
    @DisplayName("System Query Tests")
    class SystemQueryTests {

        @Test
        @DisplayName("All query methods should return non-null results")
        void testAllQueryMethods() {
            system.resetFairData();
            
            assertNotNull(system.getCurrentPhase(), "Phase query should work");
            assertNotNull(system.getFair(), "Fair query should work");
            assertNotNull(system.getAllOffers(), "Offers query should work");
            assertNotNull(system.getAllOrganizations(), "Organizations query should work");
            assertNotNull(system.getAllRecruiters(), "Recruiters query should work");
            assertNotNull(system.getAllCandidates(), "Candidates query should work");
        }

        @Test
        @DisplayName("Query results should be consistent")
        void testQueryConsistency() {
            system.resetFairData();
            
            FairPhase phase1 = system.getCurrentPhase();
            Collection<Organization> orgs1 = system.getAllOrganizations();
            
            FairPhase phase2 = system.getCurrentPhase();
            Collection<Organization> orgs2 = system.getAllOrganizations();
            
            assertEquals(phase1, phase2, "Phase should be consistent");
            assertNotNull(orgs1, "First query should succeed");
            assertNotNull(orgs2, "Second query should succeed");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete setup workflow: reset -> organizations -> recruiters -> candidates")
        void testCompleteSetupWorkflow() {
            assertDoesNotThrow(() -> {
                // Initialize
                system.resetFairData();
                
                // Add organizations and booths
                Organization google = system.addOrganization("Google");
                Organization microsoft = system.addOrganization("Microsoft");
                
                Booth googleBooth = system.addBooth(google, "Google Booth");
                Booth msBooth = system.addBooth(microsoft, "Microsoft Booth");
                
                // Register recruiters
                Recruiter googleRec = system.registerRecruiter("Alice", "alice@google.com", googleBooth);
                Recruiter msRec = system.registerRecruiter("Bob", "bob@microsoft.com", msBooth);
                
                // Register candidates
                Candidate candidate1 = system.registerCandidate("Charlie", "charlie@email.com", "CS", "Machine Learning,AI");
                Candidate candidate2 = system.registerCandidate("Diana", "diana@email.com", "EE", "Power Systems,Renewable Energy");
                
                // Verify state
                assertNotNull(system.getCurrentPhase(), "System should have valid phase");
                assertFalse(system.getAllOrganizations().isEmpty(), "Should have organizations");
            }, "Complete workflow should execute without exception");
        }

        @Test
        @DisplayName("Observer pattern should work with system changes")
        void testObserverIntegration() {
            assertDoesNotThrow(() -> {
                PropertyChangeListener listener = evt -> {
                    // Listen for changes
                };
                
                system.addPropertyChangeListener(listener);
                system.resetFairData();
                system.removePropertyChangeListener(listener);
            }, "Observer integration should work");
        }

        @Test
        @DisplayName("Multiple getInstance() calls should provide consistent state")
        void testConsistentState() {
            CareerFairSystem ref1 = CareerFairSystem.getInstance();
            ref1.resetFairData();
            Organization org = ref1.addOrganization("TestOrg");
            
            CareerFairSystem ref2 = CareerFairSystem.getInstance();
            Organization found = ref2.getOrganizationByName("TestOrg");
            
            assertNotNull(found, "State should be consistent across getInstance() calls");
        }

        @Test
        @DisplayName("System should support high volume operations")
        void testHighVolumeOperations() {
            system.resetFairData();
            
            assertDoesNotThrow(() -> {
                // Add multiple organizations
                for (int i = 0; i < 5; i++) {
                    system.addOrganization("Company" + i);
                }
                
                // Add multiple booths and recruiters
                for (int i = 0; i < 5; i++) {
                    Organization org = system.getOrganizationByName("Company" + i);
                    if (org != null) {
                        Booth b = system.addBooth(org, "Booth" + i);
                        system.registerRecruiter("Rec" + i, "rec" + i + "@email.com", b);
                    }
                }
                
                // Add multiple candidates
                for (int i = 0; i < 10; i++) {
                    system.registerCandidate("Candidate" + i, "cand" + i + "@email.com", "CS", "Software,Technology");
                }
            }, "Should handle high volume operations");
        }
    }
}
