package vcfs.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import vcfs.core.LogLevel;
import vcfs.core.Logger;
import vcfs.models.users.Candidate;
import vcfs.models.users.Recruiter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for BaseController helper methods.
 * Tests validation, logging, and user extraction utilities.
 */
@DisplayName("BaseController Test Suite")
public class BaseControllerTest {

    private ConcreteController controller;

    /**
     * Concrete implementation of abstract BaseController for testing.
     */
    private static class ConcreteController extends BaseController {
        // Concrete implementation - enables testing of abstract class
    }

    @BeforeEach
    void setUp() {
        controller = new ConcreteController();
    }

    // =========================================================
    // VALIDATION: validateLoggedIn Tests
    // =========================================================

    @Nested
    @DisplayName("validateLoggedIn Tests")
    class ValidateLoggedInTests {

        @Test
        @DisplayName("Should return true when user is logged in (not null)")
        void testValidateLoggedInWithValidUser() {
            Candidate candidate = new Candidate("cand001", "John Doe", "john@example.com");

            boolean result = controller.validateLoggedIn(candidate, "Candidate", "Test Operation");

            assertTrue(result, "Should return true for non-null user");
        }

        @Test
        @DisplayName("Should return false when user is null (logged out)")
        void testValidateLoggedInWithNullUser() {
            boolean result = controller.validateLoggedIn(null, "Candidate", "Test Operation");

            assertFalse(result, "Should return false for null user");
        }

        @Test
        @DisplayName("Should validate recruiter login state")
        void testValidateLoggedInWithRecruiter() {
            Recruiter recruiter = new Recruiter("rec001", "Jane Smith", "jane@company.com");

            boolean result = controller.validateLoggedIn(recruiter, "Recruiter", "Submit Offer");

            assertTrue(result, "Should return true for recruiter login verification");
        }

        @Test
        @DisplayName("Should return false when admin is null")
        void testValidateLoggedInWithNullAdmin() {
            boolean result = controller.validateLoggedIn(null, "Admin", "Admin Operation");

            assertFalse(result, "Should return false when admin is not logged in");
        }

        @Test
        @DisplayName("Should handle multiple consecutive validations")
        void testValidateLoggedInMultipleChecks() {
            Candidate candidate = new Candidate("cand001", "Alice Brown", "alice@example.com");

            boolean firstCheck = controller.validateLoggedIn(candidate, "Candidate", "Operation 1");
            boolean secondCheck = controller.validateLoggedIn(candidate, "Candidate", "Operation 2");
            boolean thirdCheck = controller.validateLoggedIn(null, "Candidate", "Operation 3");

            assertTrue(firstCheck, "First check should pass");
            assertTrue(secondCheck, "Second check should pass");
            assertFalse(thirdCheck, "Third check should fail");
        }
    }

    // =========================================================
    // VALIDATION: validateNotNull Tests
    // =========================================================

    @Nested
    @DisplayName("validateNotNull Tests")
    class ValidateNotNullTests {

        @Test
        @DisplayName("Should return true when object is not null")
        void testValidateNotNullWithValidObject() {
            Object obj = new String("Valid object");

            boolean result = controller.validateNotNull(obj, "TestField");

            assertTrue(result, "Should return true for non-null object");
        }

        @Test
        @DisplayName("Should return false when object is null")
        void testValidateNotNullWithNull() {
            boolean result = controller.validateNotNull(null, "TestField");

            assertFalse(result, "Should return false for null object");
        }

        @Test
        @DisplayName("Should validate different object types")
        void testValidateNotNullWithDifferentTypes() {
            Object candidate = new Candidate("c001", "John", "john@example.com");
            Object recruiter = new Recruiter("r001", "Jane", "jane@company.com");
            Object request = new Integer(42);

            assertTrue(controller.validateNotNull(candidate, "Candidate"), "Should pass for Candidate");
            assertTrue(controller.validateNotNull(recruiter, "Recruiter"), "Should pass for Recruiter");
            assertTrue(controller.validateNotNull(request, "Request"), "Should pass for Integer");
        }

        @Test
        @DisplayName("Should return false for null String object field")
        void testValidateNotNullWithNullString() {
            String str = null;

            boolean result = controller.validateNotNull(str, "StringField");

            assertFalse(result, "Should return false for null string");
        }

        @Test
        @DisplayName("Should validate multiple nullable fields independently")
        void testValidateNotNullMultipleFields() {
            Object field1 = new Object();
            Object field2 = null;
            Object field3 = "populated";

            assertTrue(controller.validateNotNull(field1, "Field1"), "Field1 should be valid");
            assertFalse(controller.validateNotNull(field2, "Field2"), "Field2 should be invalid");
            assertTrue(controller.validateNotNull(field3, "Field3"), "Field3 should be valid");
        }
    }

    // =========================================================
    // VALIDATION: validateNotEmpty Tests
    // =========================================================

    @Nested
    @DisplayName("validateNotEmpty Tests")
    class ValidateNotEmptyTests {

        @Test
        @DisplayName("Should return true when string is not empty")
        void testValidateNotEmptyWithValidString() {
            boolean result = controller.validateNotEmpty("Valid String", "TestField");

            assertTrue(result, "Should return true for non-empty string");
        }

        @Test
        @DisplayName("Should return false when string is null")
        void testValidateNotEmptyWithNull() {
            boolean result = controller.validateNotEmpty(null, "TestField");

            assertFalse(result, "Should return false for null string");
        }

        @Test
        @DisplayName("Should return false when string is empty")
        void testValidateNotEmptyWithEmpty() {
            boolean result = controller.validateNotEmpty("", "TestField");

            assertFalse(result, "Should return false for empty string");
        }

        @Test
        @DisplayName("Should return false when string contains only whitespace")
        void testValidateNotEmptyWithWhitespace() {
            boolean result = controller.validateNotEmpty("   ", "TestField");

            assertFalse(result, "Should return false for whitespace-only string");
        }

        @Test
        @DisplayName("Should trim and validate correctly")
        void testValidateNotEmptyWithTrimmedString() {
            boolean result1 = controller.validateNotEmpty("  valid  ", "TestField");
            boolean result2 = controller.validateNotEmpty("  ", "TestField");

            assertTrue(result1, "Should accept trimmed content");
            assertFalse(result2, "Should reject whitespace after trimming");
        }

        @Test
        @DisplayName("Should validate multiple string fields")
        void testValidateNotEmptyMultipleStrings() {
            assertTrue(controller.validateNotEmpty("name", "Name"), "Name field should be valid");
            assertTrue(controller.validateNotEmpty("email@example.com", "Email"), "Email field should be valid");
            assertFalse(controller.validateNotEmpty("", "Phone"), "Empty phone should be invalid");
            assertFalse(controller.validateNotEmpty(null, "Address"), "Null address should be invalid");
        }
    }

    // =========================================================
    // STRING: safeTrim Tests
    // =========================================================

    @Nested
    @DisplayName("safeTrim Tests")
    class SafeTrimTests {

        @Test
        @DisplayName("Should trim whitespace from string")
        void testSafeTrimNormalString() {
            String result = controller.safeTrim("  hello world  ");

            assertEquals("hello world", result, "Should trim leading and trailing whitespace");
        }

        @Test
        @DisplayName("Should return empty string when input is null")
        void testSafeTrimNull() {
            String result = controller.safeTrim(null);

            assertEquals("", result, "Should return empty string for null input");
        }

        @Test
        @DisplayName("Should handle string with only whitespace")
        void testSafeTrimWhitespaceOnly() {
            String result = controller.safeTrim("     ");

            assertEquals("", result, "Should return empty string for whitespace-only input");
        }

        @Test
        @DisplayName("Should not remove internal spaces")
        void testSafeTrimInternalSpaces() {
            String result = controller.safeTrim("  hello  world  test  ");

            assertEquals("hello  world  test", result, "Should preserve internal spaces");
        }

        @Test
        @DisplayName("Should handle already trimmed string")
        void testSafeTrimAlreadyTrimmed() {
            String result = controller.safeTrim("hello");

            assertEquals("hello", result, "Should return unchanged string");
        }

        @Test
        @DisplayName("Should handle tabs and newlines")
        void testSafeTrimTabsAndNewlines() {
            String result = controller.safeTrim("\t  hello\n  ");

            assertEquals("hello", result, "Should trim tabs and newlines");
        }
    }

    // =========================================================
    // USER: getUserName Tests
    // =========================================================

    @Nested
    @DisplayName("getUserName Tests")
    class GetUserNameTests {

        @Test
        @DisplayName("Should extract display name from Candidate")
        void testGetUserNameFromCandidate() {
            Candidate candidate = new Candidate("cand001", "John Doe", "john@example.com");

            String result = controller.getUserName(candidate);

            assertEquals("John Doe", result, "Should extract candidate display name");
        }

        @Test
        @DisplayName("Should extract display name from Recruiter")
        void testGetUserNameFromRecruiter() {
            Recruiter recruiter = new Recruiter("rec001", "Jane Smith", "jane@company.com");

            String result = controller.getUserName(recruiter);

            assertEquals("Jane Smith", result, "Should extract recruiter display name");
        }

        @Test
        @DisplayName("Should return UNKNOWN for null user")
        void testGetUserNameNull() {
            String result = controller.getUserName(null);

            assertEquals("UNKNOWN", result, "Should return UNKNOWN for null user");
        }

        @Test
        @DisplayName("Should handle user with special characters in name")
        void testGetUserNameSpecialCharacters() {
            Candidate candidate = new Candidate("cand002", "José García-López", "jose@example.com");

            String result = controller.getUserName(candidate);

            assertEquals("José García-López", result, "Should handle special characters in name");
        }

        @Test
        @DisplayName("Should handle user with single name")
        void testGetUserNameSingleName() {
            Candidate candidate = new Candidate("cand003", "Madonna", "madonna@example.com");

            String result = controller.getUserName(candidate);

            assertEquals("Madonna", result, "Should handle single name");
        }

        @Test
        @DisplayName("Should extract name from user with long display name")
        void testGetUserNameLongName() {
            Candidate candidate = new Candidate("cand004", "Alexander James Christopher Thompson", "alex@example.com");

            String result = controller.getUserName(candidate);

            assertEquals("Alexander James Christopher Thompson", result, "Should handle long names");
        }
    }

    // =========================================================
    // LOGGING: logOperation Tests
    // =========================================================

    @Nested
    @DisplayName("logOperation Tests")
    class LogOperationTests {

        @Test
        @DisplayName("Should log operation with INFO level")
        void testLogOperationInfo() {
            assertDoesNotThrow(() -> {
                controller.logOperation(LogLevel.INFO, "TestController", "Test operation completed");
            }, "Should not throw exception logging INFO operation");
        }

        @Test
        @DisplayName("Should log operation with WARNING level")
        void testLogOperationWarning() {
            assertDoesNotThrow(() -> {
                controller.logOperation(LogLevel.WARNING, "TestController", "Warning: unusual operation");
            }, "Should not throw exception logging WARNING operation");
        }

        @Test
        @DisplayName("Should log operation with ERROR level")
        void testLogOperationError() {
            assertDoesNotThrow(() -> {
                controller.logOperation(LogLevel.ERROR, "TestController", "Error: operation failed");
            }, "Should not throw exception logging ERROR operation");
        }

        @Test
        @DisplayName("Should log operation with DEBUG level")
        void testLogOperationDebug() {
            assertDoesNotThrow(() -> {
                controller.logOperation(LogLevel.DEBUG, "TestController", "Debug: operation details");
            }, "Should not throw exception logging DEBUG operation");
        }

        @Test
        @DisplayName("Should handle empty message")
        void testLogOperationEmptyMessage() {
            assertDoesNotThrow(() -> {
                controller.logOperation(LogLevel.INFO, "TestController", "");
            }, "Should handle empty message");
        }

        @Test
        @DisplayName("Should handle null level gracefully")
        void testLogOperationNullLevel() {
            assertDoesNotThrow(() -> {
                controller.logOperation(null, "TestController", "Operation");
            }, "Should handle null log level");
        }
    }

    // =========================================================
    // LOGGING: logError Tests
    // =========================================================

    @Nested
    @DisplayName("logError Tests")
    class LogErrorTests {

        @Test
        @DisplayName("Should log error with exception")
        void testLogErrorWithException() {
            Exception testException = new Exception("Test error message");

            assertDoesNotThrow(() -> {
                controller.logError("TestController", "Operation failed", testException);
            }, "Should log error with exception");
        }

        @Test
        @DisplayName("Should log error with null exception")
        void testLogErrorNullException() {
            assertDoesNotThrow(() -> {
                controller.logError("TestController", "Operation failed", null);
            }, "Should handle null exception");
        }

        @Test
        @DisplayName("Should log error with custom exception message")
        void testLogErrorCustomMessage() {
            Exception testException = new IllegalArgumentException("Invalid argument provided");

            assertDoesNotThrow(() -> {
                controller.logError("AdminController", "Failed to create organization", testException);
            }, "Should log custom exception");
        }

        @Test
        @DisplayName("Should log error with nested exception")
        void testLogErrorNestedCause() {
            Exception cause = new RuntimeException("Root cause");
            Exception testException = new Exception("Wrapper exception", cause);

            assertDoesNotThrow(() -> {
                controller.logError("CandidateController", "Request processing failed", testException);
            }, "Should handle nested exceptions");
        }

        @Test
        @DisplayName("Should log multiple errors independently")
        void testLogErrorMultiple() {
            Exception error1 = new IllegalArgumentException("Error 1");
            Exception error2 = new RuntimeException("Error 2");

            assertDoesNotThrow(() -> {
                controller.logError("TestController", "First error", error1);
                controller.logError("TestController", "Second error", error2);
            }, "Should log multiple errors independently");
        }
    }

    // =========================================================
    // INTEGRATION Tests
    // =========================================================

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should perform complete validation workflow")
        void testCompleteValidationWorkflow() {
            Candidate candidate = new Candidate("cand001", "Test User", "test@example.com");
            String email = "test@example.com";

            boolean userChecked = controller.validateLoggedIn(candidate, "Candidate", "Email update");
            boolean emailValid = controller.validateNotEmpty(email, "Email");
            String userName = controller.getUserName(candidate);
            String emailTrimmed = controller.safeTrim("  " + email + "  ");

            assertTrue(userChecked, "User should be validated");
            assertTrue(emailValid, "Email should be valid");
            assertEquals("Test User", userName, "Username should be extracted");
            assertEquals(email, emailTrimmed, "Email should be trimmed");
        }

        @Test
        @DisplayName("Should handle failed validation with logging")
        void testFailedValidationWithLogging() {
            assertDoesNotThrow(() -> {
                boolean result = controller.validateLoggedIn(null, "Candidate", "Submit Request");
                assertFalse(result, "Validation should fail");
                controller.logOperation(LogLevel.WARNING, "CandidateController", "User not logged in");
            }, "Should handle failed validation and log");
        }

        @Test
        @DisplayName("Should validate and extract user info for display")
        void testValidateAndExtractUserInfo() {
            Candidate candidate = new Candidate("cand005", "Display Name Test", "display@test.com");

            if (controller.validateLoggedIn(candidate, "Candidate", "Test")) {
                String displayName = controller.getUserName(candidate);
                assertTrue(displayName.length() > 0, "Display name should be extracted");
                assertNotEquals("UNKNOWN", displayName, "Should not return UNKNOWN for valid user");
            }
        }
    }
}
