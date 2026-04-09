package vcfs.models.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Comprehensive JUnit tests for Recruiter class.
 * Tests recruiter-specific model functionality and user management.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("Recruiter - Recruiter User Model")
public class RecruiterTest {

    private Recruiter recruiter;
    private static final String VALID_ID = "rec001";
    private static final String VALID_NAME = "Bob Johnson";
    private static final String VALID_EMAIL = "bob@company.com";

    @BeforeEach
    void setUp() {
        recruiter = new Recruiter(VALID_ID, VALID_NAME, VALID_EMAIL);
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Recruiter constructor initializes with valid parameters")
        void testConstructorValidParameters() {
            assertNotNull(recruiter);
            assertEquals(VALID_ID, recruiter.getId());
            assertEquals(VALID_NAME, recruiter.getDisplayName());
            assertEquals(VALID_EMAIL, recruiter.getEmail());
            assertTrue(recruiter instanceof User);
        }

        @Test
        @DisplayName("Recruiter constructor throws exception for null ID")
        void testConstructorNullId() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Recruiter(null, VALID_NAME, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("Recruiter constructor throws exception for empty ID")
        void testConstructorEmptyId() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Recruiter("", VALID_NAME, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("Recruiter constructor throws exception for null display name")
        void testConstructorNullDisplayName() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Recruiter(VALID_ID, null, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("Recruiter constructor throws exception for null email")
        void testConstructorNullEmail() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Recruiter(VALID_ID, VALID_NAME, null);
            });
        }
    }

    // ========== INHERITANCE & USER INTERFACE ==========

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Recruiter is instanceof User")
        void testInstanceOfUser() {
            assertTrue(recruiter instanceof User);
        }

        @Test
        @DisplayName("User methods work on Recruiter")
        void testUserMethodsOnRecruiter() {
            recruiter.setId("rec_updated");
            recruiter.setDisplayName("Updated Recruiter");
            recruiter.setEmail("updated@company.com");
            
            assertEquals("rec_updated", recruiter.getId());
            assertEquals("Updated Recruiter", recruiter.getDisplayName());
            assertEquals("updated@company.com", recruiter.getEmail());
        }

        @Test
        @DisplayName("Null validation inherited from User")
        void testNullValidationInherited() {
            assertThrows(IllegalArgumentException.class, () -> {
                recruiter.setId(null);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                recruiter.setDisplayName(null);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                recruiter.setEmail(null);
            });
        }
    }

    // ========== IDENTIFIER MANAGEMENT ==========

    @Nested
    @DisplayName("Identifier Management Tests")
    class IdentifierManagementTests {

        @Test
        @DisplayName("Recruiter ID is retrievable")
        void testGetId() {
            assertEquals(VALID_ID, recruiter.getId());
        }

        @Test
        @DisplayName("Recruiter ID can be updated")
        void testSetId() {
            String newId = "rec_new";
            recruiter.setId(newId);
            assertEquals(newId, recruiter.getId());
        }

        @Test
        @DisplayName("Recruiter ID cannot be empty after update")
        void testSetIdEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                recruiter.setId("");
            });
        }
    }

    // ========== STATE & UNIQUENESS ==========

    @Nested
    @DisplayName("State and Uniqueness Tests")
    class StateAndUniquenessTests {

        @Test
        @DisplayName("Different recruiters maintain independent state")
        void testIndependentStates() {
            Recruiter recruiter2 = new Recruiter("rec002", "Alice Smith", "alice@company.com");
            
            recruiter.setDisplayName("Bob Updated");
            recruiter2.setDisplayName("Alice Updated");
            
            assertEquals("Bob Updated", recruiter.getDisplayName());
            assertEquals("Alice Updated", recruiter2.getDisplayName());
        }

        @Test
        @DisplayName("Recruiter maintains unique identity")
        void testUniqueIdentity() {
            Recruiter recruiter2 = new Recruiter("rec002", "Carol Davis", "carol@company.com");
            
            assertNotEquals(recruiter.getId(), recruiter2.getId());
            assertNotEquals(recruiter.getDisplayName(), recruiter2.getDisplayName());
            assertNotEquals(recruiter.getEmail(), recruiter2.getEmail());
        }

        @Test
        @DisplayName("Multiple field updates work correctly")
        void testMultipleFieldUpdates() {
            recruiter.setId("rec_updated");
            recruiter.setDisplayName("New Recruiter Name");
            recruiter.setEmail("new@company.com");
            
            assertEquals("rec_updated", recruiter.getId());
            assertEquals("New Recruiter Name", recruiter.getDisplayName());
            assertEquals("new@company.com", recruiter.getEmail());
        }
    }

    // ========== EMAIL MANAGEMENT ==========

    @Nested
    @DisplayName("Email Management Tests")
    class EmailManagementTests {

        @Test
        @DisplayName("Recruiter email is retrievable")
        void testGetEmail() {
            assertEquals(VALID_EMAIL, recruiter.getEmail());
        }

        @Test
        @DisplayName("Recruiter email can be updated")
        void testSetEmail() {
            String newEmail = "new@company.com";
            recruiter.setEmail(newEmail);
            assertEquals(newEmail, recruiter.getEmail());
        }

        @Test
        @DisplayName("Recruiter email validation rejects empty string")
        void testSetEmailEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                recruiter.setEmail("");
            });
        }
    }

    // ========== EDGE CASES ==========

    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCasesAndBoundaryConditions {

        @Test
        @DisplayName("Recruiter with corporate email domain")
        void testCorporateEmailDomain() {
            Recruiter corp = new Recruiter("rec003", "Corporate User", "user@largecorp.co.uk");
            assertEquals("user@largecorp.co.uk", corp.getEmail());
        }

        @Test
        @DisplayName("Recruiter with hyphenated name")
        void testHyphenatedName() {
            Recruiter hyphen = new Recruiter("rec004", "Jean-Pierre Dupont", "jp@company.com");
            assertEquals("Jean-Pierre Dupont", hyphen.getDisplayName());
        }

        @Test
        @DisplayName("Recruiter with numeric suffix in ID")
        void testNumericIdSuffix() {
            Recruiter numeric = new Recruiter("recruiter_2026_001", "Name", "email@test.com");
            assertEquals("recruiter_2026_001", numeric.getId());
        }
    }
}

