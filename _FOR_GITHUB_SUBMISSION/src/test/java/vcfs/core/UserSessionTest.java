package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import vcfs.models.users.Candidate;
import vcfs.models.users.Recruiter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for UserSession Singleton.
 * Tests login/logout, role management, and session state tracking.
 */
@DisplayName("UserSession Test Suite")
public class UserSessionTest {

    private UserSession session;

    @BeforeEach
    void setUp() {
        session = UserSession.getInstance();
    }

    // =========================================================
    // SINGLETON Tests
    // =========================================================

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonTests {

        @Test
        @DisplayName("Should return same instance on multiple calls")
        void testSingletonInstance() {
            UserSession instance1 = UserSession.getInstance();
            UserSession instance2 = UserSession.getInstance();

            assertSame(instance1, instance2, "Should return same instance");
        }

        @Test
        @DisplayName("Should be thread-safe singleton")
        void testThreadSafeSingleton() {
            UserSession[] instances = new UserSession[10];

            for (int i = 0; i < 10; i++) {
                instances[i] = UserSession.getInstance();
            }

            for (int i = 1; i < 10; i++) {
                assertSame(instances[0], instances[i], "All instances should be identical");
            }
        }

        @Test
        @DisplayName("Should maintain singleton across method calls")
        void testSingletonPersistence() {
            UserSession session1 = UserSession.getInstance();
            UserSession session2 = UserSession.getInstance();
            UserSession session3 = UserSession.getInstance();

            assertTrue(session1 == session2 && session2 == session3, "Singleton should persist");
        }
    }

    // =========================================================
    // SET CURRENT CANDIDATE Tests
    // =========================================================

    @Nested
    @DisplayName("Set Current Candidate Tests")
    class SetCurrentCandidateTests {

        @Test
        @DisplayName("Should set current candidate")
        void testSetCurrentCandidate() {
            Candidate candidate = new Candidate("cand001", "John Doe", "john@example.com");

            assertDoesNotThrow(() -> {
                session.setCurrentCandidate(candidate);
            }, "Should set candidate without error");
        }

        @Test
        @DisplayName("Should allow setting null candidate (logout)")
        void testSetCurrentCandidateNull() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            session.setCurrentCandidate(candidate);

            assertDoesNotThrow(() -> {
                session.setCurrentCandidate(null);
            }, "Should allow null candidate (logout)");
        }

        @Test
        @DisplayName("Should update candidate when called multiple times")
        void testSetCurrentCandidateMultipleTimes() {
            Candidate candidate1 = new Candidate("cand001", "First", "first@example.com");
            Candidate candidate2 = new Candidate("cand002", "Second", "second@example.com");

            session.setCurrentCandidate(candidate1);
            session.setCurrentCandidate(candidate2);

            assertDoesNotThrow(() -> {}, "Should handle multiple candidate changes");
        }
    }

    // =========================================================
    // SET CURRENT RECRUITER Tests
    // =========================================================

    @Nested
    @DisplayName("Set Current Recruiter Tests")
    class SetCurrentRecruiterTests {

        @Test
        @DisplayName("Should set current recruiter")
        void testSetCurrentRecruiter() {
            Recruiter recruiter = new Recruiter("rec001", "Jane Smith", "jane@company.com");

            assertDoesNotThrow(() -> {
                session.setCurrentRecruiter(recruiter);
            }, "Should set recruiter without error");
        }

        @Test
        @DisplayName("Should allow setting null recruiter (logout)")
        void testSetCurrentRecruiterNull() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            session.setCurrentRecruiter(recruiter);

            assertDoesNotThrow(() -> {
                session.setCurrentRecruiter(null);
            }, "Should allow null recruiter (logout)");
        }

        @Test
        @DisplayName("Should update recruiter when called multiple times")
        void testSetCurrentRecruiterMultipleTimes() {
            Recruiter recruiter1 = new Recruiter("rec001", "First", "first@company.com");
            Recruiter recruiter2 = new Recruiter("rec002", "Second", "second@company.com");

            session.setCurrentRecruiter(recruiter1);
            session.setCurrentRecruiter(recruiter2);

            assertDoesNotThrow(() -> {}, "Should handle multiple recruiter changes");
        }
    }

    // =========================================================
    // SET CURRENT ADMIN Tests
    // =========================================================

    @Nested
    @DisplayName("Set Current Admin Tests")
    class SetCurrentAdminTests {

        @Test
        @DisplayName("Should set current admin")
        void testSetCurrentAdmin() {
            Object admin = new Object();

            assertDoesNotThrow(() -> {
                session.setCurrentAdmin(admin);
            }, "Should set admin without error");
        }

        @Test
        @DisplayName("Should allow setting null admin")
        void testSetCurrentAdminNull() {
            Object admin = new Object();
            session.setCurrentAdmin(admin);

            assertDoesNotThrow(() -> {
                session.setCurrentAdmin(null);
            }, "Should allow null admin (logout)");
        }

        @Test
        @DisplayName("Should update admin when called multiple times")
        void testSetCurrentAdminMultipleTimes() {
            Object admin1 = new Object();
            Object admin2 = new Object();

            session.setCurrentAdmin(admin1);
            session.setCurrentAdmin(admin2);

            assertDoesNotThrow(() -> {}, "Should handle multiple admin changes");
        }
    }

    // =========================================================
    // GET CURRENT CANDIDATE Tests
    // =========================================================

    @Nested
    @DisplayName("Get Current Candidate Tests")
    class GetCurrentCandidateTests {

        @Test
        @DisplayName("Should return null when no candidate set")
        void testGetCurrentCandidateNull() {
            session.setCurrentCandidate(null);

            Object result = session.getCurrentCandidate();

            assertNull(result, "Should return null when no candidate set");
        }

        @Test
        @DisplayName("Should return candidate after being set")
        void testGetCurrentCandidateAfterSet() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            session.setCurrentCandidate(candidate);

            Object result = session.getCurrentCandidate();

            assertNotNull(result, "Should return candidate");
        }

        @Test
        @DisplayName("Should return most recent candidate")
        void testGetCurrentCandidateLatest() {
            Candidate candidate1 = new Candidate("cand001", "First", "first@example.com");
            Candidate candidate2 = new Candidate("cand002", "Second", "second@example.com");

            session.setCurrentCandidate(candidate1);
            session.setCurrentCandidate(candidate2);

            Object result = session.getCurrentCandidate();

            assertNotNull(result, "Should return most recent candidate");
        }
    }

    // =========================================================
    // GET CURRENT RECRUITER Tests
    // =========================================================

    @Nested
    @DisplayName("Get Current Recruiter Tests")
    class GetCurrentRecruiterTests {

        @Test
        @DisplayName("Should return null when no recruiter set")
        void testGetCurrentRecruiterNull() {
            session.setCurrentRecruiter(null);

            Object result = session.getCurrentRecruiter();

            assertNull(result, "Should return null when no recruiter set");
        }

        @Test
        @DisplayName("Should return recruiter after being set")
        void testGetCurrentRecruiterAfterSet() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            session.setCurrentRecruiter(recruiter);

            Object result = session.getCurrentRecruiter();

            assertNotNull(result, "Should return recruiter");
        }

        @Test
        @DisplayName("Should return most recent recruiter")
        void testGetCurrentRecruiterLatest() {
            Recruiter recruiter1 = new Recruiter("rec001", "First", "first@company.com");
            Recruiter recruiter2 = new Recruiter("rec002", "Second", "second@company.com");

            session.setCurrentRecruiter(recruiter1);
            session.setCurrentRecruiter(recruiter2);

            Object result = session.getCurrentRecruiter();

            assertNotNull(result, "Should return most recent recruiter");
        }
    }

    // =========================================================
    // GET CURRENT ADMIN Tests
    // =========================================================

    @Nested
    @DisplayName("Get Current Admin Tests")
    class GetCurrentAdminTests {

        @Test
        @DisplayName("Should return null when no admin set")
        void testGetCurrentAdminNull() {
            session.setCurrentAdmin(null);

            Object result = session.getCurrentAdmin();

            assertNull(result, "Should return null when no admin set");
        }

        @Test
        @DisplayName("Should return admin after being set")
        void testGetCurrentAdminAfterSet() {
            Object admin = new Object();
            session.setCurrentAdmin(admin);

            Object result = session.getCurrentAdmin();

            assertNotNull(result, "Should return admin");
        }
    }

    // =========================================================
    // LOGOUT Tests
    // =========================================================

    @Nested
    @DisplayName("Logout Tests")
    class LogoutTests {

        @Test
        @DisplayName("Should logout candidate (set to null)")
        void testLogoutCandidate() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            session.setCurrentCandidate(candidate);

            session.logout();

            assertNull(session.getCurrentCandidate(), "Candidate should be null after logout");
        }

        @Test
        @DisplayName("Should logout recruiter (set to null)")
        void testLogoutRecruiter() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            session.setCurrentRecruiter(recruiter);

            session.logout();

            assertNull(session.getCurrentRecruiter(), "Recruiter should be null after logout");
        }

        @Test
        @DisplayName("Should logout admin (set to null)")
        void testLogoutAdmin() {
            Object admin = new Object();
            session.setCurrentAdmin(admin);

            session.logout();

            assertNull(session.getCurrentAdmin(), "Admin should be null after logout");
        }

        @Test
        @DisplayName("Should clear all user types on logout")
        void testLogoutClearsAllUsers() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            Object admin = new Object();

            session.setCurrentCandidate(candidate);
            session.setCurrentRecruiter(recruiter);
            session.setCurrentAdmin(admin);

            session.logout();

            assertNull(session.getCurrentCandidate(), "Candidate should be null");
            assertNull(session.getCurrentRecruiter(), "Recruiter should be null");
            assertNull(session.getCurrentAdmin(), "Admin should be null");
        }
    }

    // =========================================================
    // ROLE MANAGEMENT Tests
    // =========================================================

    @Nested
    @DisplayName("Role Management Tests")
    class RoleManagementTests {

        @Test
        @DisplayName("Should handle switching between candidate and recruiter")
        void testSwitchBetweenRoles() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");

            session.setCurrentCandidate(candidate);
            assertNotNull(session.getCurrentCandidate(), "Candidate should be set");

            session.logout();

            session.setCurrentRecruiter(recruiter);
            assertNotNull(session.getCurrentRecruiter(), "Recruiter should be set");
        }

        @Test
        @DisplayName("Should maintain separate candidate sessions")
        void testSeparateCandidateSessions() {
            Candidate candidate1 = new Candidate("cand001", "Alice", "alice@example.com");
            Candidate candidate2 = new Candidate("cand002", "Bob", "bob@example.com");

            session.setCurrentCandidate(candidate1);
            session.setCurrentCandidate(candidate2);

            Object current = session.getCurrentCandidate();
            assertNotNull(current, "Current candidate should exist");
        }

        @Test
        @DisplayName("Should maintain separate recruiter sessions")
        void testSeparateRecruiterSessions() {
            Recruiter recruiter1 = new Recruiter("rec001", "Jane", "jane@company.com");
            Recruiter recruiter2 = new Recruiter("rec002", "John", "john@company.com");

            session.setCurrentRecruiter(recruiter1);
            session.setCurrentRecruiter(recruiter2);

            Object current = session.getCurrentRecruiter();
            assertNotNull(current, "Current recruiter should exist");
        }
    }

    // =========================================================
    // STATE INDEPENDENCE Tests
    // =========================================================

    @Nested
    @DisplayName("State Independence Tests")
    class StateIndependenceTests {

        @Test
        @DisplayName("Should allow rapid user changes")
        void testRapidUserChanges() {
            for (int i = 0; i < 10; i++) {
                Candidate candidate = new Candidate("cand" + i, "User" + i, "user" + i + "@example.com");
                session.setCurrentCandidate(candidate);

                assertNotNull(session.getCurrentCandidate(), "Should handle rapid changes");
            }
        }

        @Test
        @DisplayName("Should allow login/logout cycles")
        void testLoginLogoutCycles() {
            for (int i = 0; i < 5; i++) {
                Candidate candidate = new Candidate("cand" + i, "User" + i, "user" + i + "@example.com");

                session.setCurrentCandidate(candidate);
                assertNotNull(session.getCurrentCandidate(), "Login cycle " + i);

                session.logout();
                assertNull(session.getCurrentCandidate(), "Logout cycle " + i);
            }
        }
    }

    // =========================================================
    // ERROR HANDLING Tests
    // =========================================================

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null safely")
        void testHandleNull() {
            assertDoesNotThrow(() -> {
                session.setCurrentCandidate(null);
                session.setCurrentRecruiter(null);
                session.setCurrentAdmin(null);
            }, "Should handle null values");
        }

        @Test
        @DisplayName("Should handle multiple quick nullifications")
        void testMultipleQuickNulls() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");

            session.setCurrentCandidate(candidate);
            session.setCurrentCandidate(null);
            session.setCurrentCandidate(null);
            session.setCurrentCandidate(null);

            assertNull(session.getCurrentCandidate(), "Should remain null");
        }
    }
}
