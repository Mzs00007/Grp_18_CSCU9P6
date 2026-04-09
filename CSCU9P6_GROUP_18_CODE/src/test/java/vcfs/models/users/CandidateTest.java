package vcfs.models.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Comprehensive JUnit tests for Candidate class.
 * Tests candidate-specific model functionality including inheritance from User.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("Candidate - Candidate User Model with Profile Management")
public class CandidateTest {


    private Candidate candidate;
    private CandidateProfile profile;
    private static final String VALID_ID = "cand001";
    private static final String VALID_NAME = "Alice Johnson";
    private static final String VALID_EMAIL = "alice@example.com";

    @BeforeEach
    void setUp() {
        candidate = new Candidate(VALID_ID, VALID_NAME, VALID_EMAIL);
        profile = new CandidateProfile();
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Candidate constructor initializes with valid parameters")
        void testConstructorValidParameters() {
            assertNotNull(candidate);
            assertEquals(VALID_ID, candidate.getId());
            assertEquals(VALID_NAME, candidate.getDisplayName());
            assertEquals(VALID_EMAIL, candidate.getEmail());
            assertTrue(candidate instanceof User);
        }

        @Test
        @DisplayName("Candidate constructor throws exception for null ID")
        void testConstructorNullId() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate(null, VALID_NAME, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("Candidate constructor throws exception for null email")
        void testConstructorNullEmail() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate(VALID_ID, VALID_NAME, null);
            });
        }
    }

    // ========== INHERITANCE & USER INTERFACE ==========

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Candidate is instanceof User")
        void testInstanceOfUser() {
            assertTrue(candidate instanceof User);
        }

        @Test
        @DisplayName("User methods work on Candidate")
        void testUserMethodsOnCandidate() {
            candidate.setId("newCandidateId");
            candidate.setDisplayName("New Name");
            candidate.setEmail("newemail@test.com");
            
            assertEquals("newCandidateId", candidate.getId());
            assertEquals("New Name", candidate.getDisplayName());
            assertEquals("newemail@test.com", candidate.getEmail());
        }

        @Test
        @DisplayName("Null validation inherited from User")
        void testNullValidationInherited() {
            assertThrows(IllegalArgumentException.class, () -> {
                candidate.setId(null);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                candidate.setDisplayName(null);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                candidate.setEmail(null);
            });
        }
    }

    // ========== PROFILE MANAGEMENT ==========

    @Nested
    @DisplayName("Profile Management Tests")
    class ProfileManagementTests {

        @Test
        @DisplayName("Candidate can associate with profile")
        void testAssociateProfile() {
            candidate.setProfile(profile);
            assertEquals(profile, candidate.getProfile());
        }

        @Test
        @DisplayName("Candidate profile can be set to null")
        void testSetProfileNull() {
            candidate.setProfile(null);
            assertNull(candidate.getProfile());
        }

        @Test
        @DisplayName("Profile can be updated after initial assignment")
        void testUpdateProfile() {
            CandidateProfile profile1 = new CandidateProfile();
            CandidateProfile profile2 = new CandidateProfile();
            
            candidate.setProfile(profile1);
            assertEquals(profile1, candidate.getProfile());
            
            candidate.setProfile(profile2);
            assertEquals(profile2, candidate.getProfile());
        }
    }

    // ========== STATE & IDENTIFICATION ==========

    @Nested
    @DisplayName("State and Identification Tests")
    class StateAndIdentificationTests {

        @Test
        @DisplayName("Candidate maintains unique identity")
        void testUniqueIdentity() {
            Candidate candidate2 = new Candidate("cand002", "Bob Smith", "bob@example.com");
            
            assertNotEquals(candidate.getId(), candidate2.getId());
            assertNotEquals(candidate.getDisplayName(), candidate2.getDisplayName());
            assertNotEquals(candidate.getEmail(), candidate2.getEmail());
        }

        @Test
        @DisplayName("Candidate ID can be updated")
        void testUpdateCandidateId() {
            String newId = "cand_updated";
            candidate.setId(newId);
            assertEquals(newId, candidate.getId());
        }

        @Test
        @DisplayName("Different candidates have independent states")
        void testIndependentStates() {
            Candidate candidate2 = new Candidate("cand002", "Bob Smith", "bob@example.com");
            CandidateProfile profile2 = new CandidateProfile();
            
            candidate.setProfile(profile);
            candidate2.setProfile(profile2);
            
            assertEquals(profile, candidate.getProfile());
            assertEquals(profile2, candidate2.getProfile());
            assertNotEquals(candidate.getProfile(), candidate2.getProfile());
        }
    }

    // ========== EDGE CASES & BOUNDARY CONDITIONS ==========

    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCasesAndBoundaryConditions {

        @Test
        @DisplayName("Candidate with special characters in name")
        void testSpecialCharactersInName() {
            Candidate testCandidate = new Candidate("cand003", "José García-López", "jose@example.com");
            assertEquals("José García-López", testCandidate.getDisplayName());
        }

        @Test
        @DisplayName("Candidate with very long email address")
        void testLongEmailAddress() {
            String longEmail = "very.long.email.address.with.many.characters@subdomain.example.co.uk";
            candidate.setEmail(longEmail);
            assertEquals(longEmail, candidate.getEmail());
        }

        @Test
        @DisplayName("Candidate with numeric ID")
        void testNumericId() {
            Candidate testCandidate = new Candidate("12345", "Test User", "test@example.com");
            assertEquals("12345", testCandidate.getId());
        }

        @Test
        @DisplayName("Candidate with single character display name")
        void testSingleCharacterName() {
            candidate.setDisplayName("A");
            assertEquals("A", candidate.getDisplayName());
        }
    }
}
