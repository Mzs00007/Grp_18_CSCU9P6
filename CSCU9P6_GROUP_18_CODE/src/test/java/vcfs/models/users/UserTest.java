package vcfs.models.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Comprehensive JUnit tests for abstract User class.
 * Tests base user model functionality including ID, display name, and email management.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 * 
 * This test class uses Candidate as a concrete implementation of User for testing.
 */
@DisplayName("User - Abstract Base User Model with Validation")
public class UserTest {

    private User user;
    private static final String VALID_ID = "user123";
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_EMAIL = "john@example.com";

    @BeforeEach
    void setUp() {
        // Create concrete instance via Candidate (User is abstract)
        user = new Candidate(VALID_ID, VALID_NAME, VALID_EMAIL);
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("User constructor initializes with valid parameters")
        void testConstructorValidParameters() {
            assertNotNull(user);
            assertEquals(VALID_ID, user.getId());
            assertEquals(VALID_NAME, user.getDisplayName());
            assertEquals(VALID_EMAIL, user.getEmail());
        }

        @Test
        @DisplayName("User constructor throws exception for null ID")
        void testConstructorNullId() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate(null, VALID_NAME, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("User constructor throws exception for empty ID")
        void testConstructorEmptyId() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate("", VALID_NAME, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("User constructor throws exception for whitespace-only ID")
        void testConstructorWhitespaceOnlyId() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate("   ", VALID_NAME, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("User constructor throws exception for null display name")
        void testConstructorNullDisplayName() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate(VALID_ID, null, VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("User constructor throws exception for empty display name")
        void testConstructorEmptyDisplayName() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate(VALID_ID, "", VALID_EMAIL);
            });
        }

        @Test
        @DisplayName("User constructor throws exception for null email")
        void testConstructorNullEmail() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate(VALID_ID, VALID_NAME, null);
            });
        }

        @Test
        @DisplayName("User constructor throws exception for empty email")
        void testConstructorEmptyEmail() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Candidate(VALID_ID, VALID_NAME, "");
            });
        }
    }

    // ========== ID MANAGEMENT ==========

    @Nested
    @DisplayName("ID Management Tests")
    class IdManagementTests {

        @Test
        @DisplayName("getId returns correct ID")
        void testGetId() {
            assertEquals(VALID_ID, user.getId());
        }

        @Test
        @DisplayName("setId updates ID successfully")
        void testSetIdValid() {
            String newId = "user456";
            user.setId(newId);
            assertEquals(newId, user.getId());
        }

        @Test
        @DisplayName("setId throws exception for null")
        void testSetIdNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                user.setId(null);
            });
        }

        @Test
        @DisplayName("setId throws exception for empty string")
        void testSetIdEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                user.setId("");
            });
        }

        @Test
        @DisplayName("setId throws exception for whitespace-only string")
        void testSetIdWhitespaceOnly() {
            assertThrows(IllegalArgumentException.class, () -> {
                user.setId("   \t\n");
            });
        }

        @Test
        @DisplayName("setId preserves original ID on exception")
        void testSetIdPreservesOnException() {
            String originalId = user.getId();
            assertThrows(IllegalArgumentException.class, () -> {
                user.setId(null);
            });
            assertEquals(originalId, user.getId());
        }
    }

    // ========== DISPLAY NAME MANAGEMENT ==========

    @Nested
    @DisplayName("Display Name Management Tests")
    class DisplayNameManagementTests {

        @Test
        @DisplayName("getDisplayName returns correct name")
        void testGetDisplayName() {
            assertEquals(VALID_NAME, user.getDisplayName());
        }

        @Test
        @DisplayName("setDisplayName updates name successfully")
        void testSetDisplayNameValid() {
            String newName = "Jane Doe";
            user.setDisplayName(newName);
            assertEquals(newName, user.getDisplayName());
        }

        @Test
        @DisplayName("setDisplayName throws exception for null")
        void testSetDisplayNameNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                user.setDisplayName(null);
            });
        }

        @Test
        @DisplayName("setDisplayName throws exception for empty string")
        void testSetDisplayNameEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                user.setDisplayName("");
            });
        }

        @Test
        @DisplayName("setDisplayName preserves original name on exception")
        void testSetDisplayNamePreservesOnException() {
            String originalName = user.getDisplayName();
            assertThrows(IllegalArgumentException.class, () -> {
                user.setDisplayName(null);
            });
            assertEquals(originalName, user.getDisplayName());
        }
    }

    // ========== EMAIL MANAGEMENT ==========

    @Nested
    @DisplayName("Email Management Tests")
    class EmailManagementTests {

        @Test
        @DisplayName("getEmail returns correct email")
        void testGetEmail() {
            assertEquals(VALID_EMAIL, user.getEmail());
        }

        @Test
        @DisplayName("setEmail updates email successfully")
        void testSetEmailValid() {
            String newEmail = "newemail@example.com";
            user.setEmail(newEmail);
            assertEquals(newEmail, user.getEmail());
        }

        @Test
        @DisplayName("setEmail throws exception for null")
        void testSetEmailNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                user.setEmail(null);
            });
        }

        @Test
        @DisplayName("setEmail throws exception for empty string")
        void testSetEmailEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                user.setEmail("");
            });
        }

        @Test
        @DisplayName("setEmail preserves original email on exception")
        void testSetEmailPreservesOnException() {
            String originalEmail = user.getEmail();
            assertThrows(IllegalArgumentException.class, () -> {
                user.setEmail(null);
            });
            assertEquals(originalEmail, user.getEmail());
        }
    }

    // ========== STATE IMMUTABILITY TESTS ==========

    @Nested
    @DisplayName("State Immutability Tests")
    class StateImmutabilityTests {

        @Test
        @DisplayName("Setting one field does not affect others")
        void testIndependentFieldUpdates() {
            String originalName = user.getDisplayName();
            String originalEmail = user.getEmail();
            
            user.setId("newId");
            
            assertEquals(originalName, user.getDisplayName());
            assertEquals(originalEmail, user.getEmail());
        }

        @Test
        @DisplayName("Multiple field updates work correctly")
        void testMultipleFieldUpdates() {
            user.setId("newId");
            user.setDisplayName("New Name");
            user.setEmail("newemail@test.com");
            
            assertEquals("newId", user.getId());
            assertEquals("New Name", user.getDisplayName());
            assertEquals("newemail@test.com", user.getEmail());
        }
    }
}

