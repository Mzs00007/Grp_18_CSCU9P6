package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.models.users.Candidate;
import vcfs.models.users.Recruiter;


/**
 * Comprehensive JUnit tests for SessionManager class.
 * Tests user session management including login, logout, and session tracking.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("SessionManager - User Session Management")
public class SessionManagerTest {

    private Candidate candidate;
    private Recruiter recruiter;

    @BeforeEach
    void setUp() {
        candidate = new Candidate("cand001", "Test Candidate", "cand@test.com");
        recruiter = new Recruiter("rec001", "Test Recruiter", "rec@test.com");
        // Clear any existing sessions before each test
        SessionManager.clear();
    }

    // ========== SESSION CREATION & MANAGEMENT ==========

    @Nested
    @DisplayName("Session Creation and Management Tests")
    class SessionCreationAndManagementTests {

        @Test
        @DisplayName("Create candidate session")
        void testCreateCandidateSession() {
            SessionManager.setCurrentCandidate(candidate);
            assertEquals(candidate, SessionManager.getCurrentCandidate());
        }

        @Test
        @DisplayName("Create recruiter session")
        void testCreateRecruiterSession() {
            SessionManager.setCurrentRecruiter(recruiter);
            assertEquals(recruiter, SessionManager.getCurrentRecruiter());
        }

        @Test
        @DisplayName("Candidate and recruiter sessions are independent")
        void testIndependentSessions() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentRecruiter(recruiter);
            
            assertEquals(candidate, SessionManager.getCurrentCandidate());
            assertEquals(recruiter, SessionManager.getCurrentRecruiter());
        }
    }

    // ========== SESSION CLEARING ==========

    @Nested
    @DisplayName("Session Clearing Tests")
    class SessionClearingTests {

        @Test
        @DisplayName("Clear all sessions")
        void testClearAllSessions() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentRecruiter(recruiter);
            
            SessionManager.clear();
            
            assertNull(SessionManager.getCurrentCandidate());
            assertNull(SessionManager.getCurrentRecruiter());
        }

        @Test
        @DisplayName("Clear candidate session only")
        void testClearCandidateSession() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentRecruiter(recruiter);
            
            SessionManager.setCurrentCandidate(null);
            
            assertNull(SessionManager.getCurrentCandidate());
            assertEquals(recruiter, SessionManager.getCurrentRecruiter());
        }

        @Test
        @DisplayName("Clear recruiter session only")
        void testClearRecruiterSession() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentRecruiter(recruiter);
            
            SessionManager.setCurrentRecruiter(null);
            
            assertEquals(candidate, SessionManager.getCurrentCandidate());
            assertNull(SessionManager.getCurrentRecruiter());
        }
    }

    // ========== SESSION UPDATES ==========

    @Nested
    @DisplayName("Session Update Tests")
    class SessionUpdateTests {

        @Test
        @DisplayName("Update candidate session")
        void testUpdateCandidateSession() {
            Candidate candidate1 = new Candidate("cand1", "Candidate 1", "cand1@test.com");
            Candidate candidate2 = new Candidate("cand2", "Candidate 2", "cand2@test.com");
            
            SessionManager.setCurrentCandidate(candidate1);
            assertEquals(candidate1, SessionManager.getCurrentCandidate());
            
            SessionManager.setCurrentCandidate(candidate2);
            assertEquals(candidate2, SessionManager.getCurrentCandidate());
        }

        @Test
        @DisplayName("Update recruiter session")
        void testUpdateRecruiterSession() {
            Recruiter recruiter1 = new Recruiter("rec1", "Recruiter 1", "rec1@test.com");
            Recruiter recruiter2 = new Recruiter("rec2", "Recruiter 2", "rec2@test.com");
            
            SessionManager.setCurrentRecruiter(recruiter1);
            assertEquals(recruiter1, SessionManager.getCurrentRecruiter());
            
            SessionManager.setCurrentRecruiter(recruiter2);
            assertEquals(recruiter2, SessionManager.getCurrentRecruiter());
        }

        @Test
        @DisplayName("Replace active session")
        void testReplaceActiveSession() {
            SessionManager.setCurrentCandidate(candidate);
            Candidate newCandidate = new Candidate("new_cand", "New Candidate", "new@test.com");
            
            SessionManager.setCurrentCandidate(newCandidate);
            
            assertEquals(newCandidate, SessionManager.getCurrentCandidate());
            assertNotEquals(candidate, SessionManager.getCurrentCandidate());
        }
    }

    // ========== SESSION QUERIES ==========

    @Nested
    @DisplayName("Session Query Tests")
    class SessionQueryTests {

        @Test
        @DisplayName("Get candidate session when active")
        void testGetCandidateSessionWhenActive() {
            SessionManager.setCurrentCandidate(candidate);
            Candidate retrieved = (Candidate) SessionManager.getCurrentCandidate();
            assertNotNull(retrieved);
            assertEquals(candidate.getId(), retrieved.getId());
        }

        @Test
        @DisplayName("Get candidate session when inactive returns null")
        void testGetCandidateSessionWhenInactive() {
            assertNull(SessionManager.getCurrentCandidate());
        }

        @Test
        @DisplayName("Get recruiter session when active")
        void testGetRecruiterSessionWhenActive() {
            SessionManager.setCurrentRecruiter(recruiter);
            Recruiter retrieved = (Recruiter) SessionManager.getCurrentRecruiter();
            assertNotNull(retrieved);
            assertEquals(recruiter.getId(), retrieved.getId());
        }

        @Test
        @DisplayName("Get recruiter session when inactive returns null")
        void testGetRecruiterSessionWhenInactive() {
            assertNull(SessionManager.getCurrentRecruiter());
        }
    }

    // ========== SESSION VALIDITY ==========

    @Nested
    @DisplayName("Session Validity Tests")
    class SessionValidityTests {

        @Test
        @DisplayName("Check if candidate session is active")
        void testCandidateSessionActive() {
            SessionManager.setCurrentCandidate(candidate);
            assertTrue(SessionManager.isCandidateLoggedIn());
        }

        @Test
        @DisplayName("Check if candidate session is inactive")
        void testCandidateSessionInactive() {
            assertFalse(SessionManager.isCandidateLoggedIn());
        }

        @Test
        @DisplayName("Check if recruiter session is active")
        void testRecruiterSessionActive() {
            SessionManager.setCurrentRecruiter(recruiter);
            assertTrue(SessionManager.isRecruiterLoggedIn());
        }

        @Test
        @DisplayName("Check if recruiter session is inactive")
        void testRecruiterSessionInactive() {
            assertFalse(SessionManager.isRecruiterLoggedIn());
        }

        @Test
        @DisplayName("Both sessions can be active simultaneously")
        void testBothSessionsActive() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentRecruiter(recruiter);
            
            assertTrue(SessionManager.isCandidateLoggedIn());
            assertTrue(SessionManager.isRecruiterLoggedIn());
        }
    }

    // ========== SESSION LOGOUT ==========

    @Nested
    @DisplayName("Session Logout Tests")
    class SessionLogoutTests {

        @Test
        @DisplayName("Logout candidate")
        void testLogoutCandidate() {
            SessionManager.setCurrentCandidate(candidate);
            assertTrue(SessionManager.isCandidateLoggedIn());
            
            SessionManager.setCurrentCandidate(null);
            assertFalse(SessionManager.isCandidateLoggedIn());
        }

        @Test
        @DisplayName("Logout recruiter")
        void testLogoutRecruiter() {
            SessionManager.setCurrentRecruiter(recruiter);
            assertTrue(SessionManager.isRecruiterLoggedIn());
            
            SessionManager.setCurrentRecruiter(null);
            assertFalse(SessionManager.isRecruiterLoggedIn());
        }

        @Test
        @DisplayName("Logout candidate does not affect recruiter")
        void testLogoutCandidateIndependent() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentRecruiter(recruiter);
            
            SessionManager.setCurrentCandidate(null);
            
            assertFalse(SessionManager.isCandidateLoggedIn());
            assertTrue(SessionManager.isRecruiterLoggedIn());
        }

        @Test
        @DisplayName("Logout recruiter does not affect candidate")
        void testLogoutRecruiterIndependent() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentRecruiter(recruiter);
            
            SessionManager.setCurrentRecruiter(null);
            
            assertTrue(SessionManager.isCandidateLoggedIn());
            assertFalse(SessionManager.isRecruiterLoggedIn());
        }
    }

    // ========== MULTIPLE SESSION TRANSITIONS ==========

    @Nested
    @DisplayName("Multiple Session Transition Tests")
    class MultipleSessionTransitionTests {

        @Test
        @DisplayName("Multiple candidate logins and logouts")
        void testMultipleCandidateTransitions() {
            Candidate cand1 = new Candidate("cand1", "Candidate 1", "cand1@test.com");
            Candidate cand2 = new Candidate("cand2", "Candidate 2", "cand2@test.com");
            
            SessionManager.setCurrentCandidate(cand1);
            assertEquals(cand1, SessionManager.getCurrentCandidate());
            
            SessionManager.setCurrentCandidate(null);
            assertNull(SessionManager.getCurrentCandidate());
            
            SessionManager.setCurrentCandidate(cand2);
            assertEquals(cand2, SessionManager.getCurrentCandidate());
        }

        @Test
        @DisplayName("Multiple recruiter logins and logouts")
        void testMultipleRecruiterTransitions() {
            Recruiter rec1 = new Recruiter("rec1", "Recruiter 1", "rec1@test.com");
            Recruiter rec2 = new Recruiter("rec2", "Recruiter 2", "rec2@test.com");
            
            SessionManager.setCurrentRecruiter(rec1);
            assertEquals(rec1, SessionManager.getCurrentRecruiter());
            
            SessionManager.setCurrentRecruiter(null);
            assertNull(SessionManager.getCurrentRecruiter());
            
            SessionManager.setCurrentRecruiter(rec2);
            assertEquals(rec2, SessionManager.getCurrentRecruiter());
        }
    }

    // ========== SESSION WORKFLOW ==========

    @Nested
    @DisplayName("Session Workflow Tests")
    class SessionWorkflowTests {

        @Test
        @DisplayName("Complete candidate login-logout workflow")
        void testCompleteCandidateWorkflow() {
            // Initially no session
            assertFalse(SessionManager.isCandidateLoggedIn());
            
            // Candidate logs in
            SessionManager.setCurrentCandidate(candidate);
            assertTrue(SessionManager.isCandidateLoggedIn());
            assertEquals(candidate, SessionManager.getCurrentCandidate());
            
            // Candidate performs actions
            Candidate retrieved = (Candidate) SessionManager.getCurrentCandidate();
            assertNotNull(retrieved);
            
            // Candidate logs out
            SessionManager.setCurrentCandidate(null);
            assertFalse(SessionManager.isCandidateLoggedIn());
        }

        @Test
        @DisplayName("Complete recruiter login-logout workflow")
        void testCompleteRecruiterWorkflow() {
            // Initially no session
            assertFalse(SessionManager.isRecruiterLoggedIn());
            
            // Recruiter logs in
            SessionManager.setCurrentRecruiter(recruiter);
            assertTrue(SessionManager.isRecruiterLoggedIn());
            assertEquals(recruiter, SessionManager.getCurrentRecruiter());
            
            // Recruiter performs actions
            Recruiter retrieved = (Recruiter) SessionManager.getCurrentRecruiter();
            assertNotNull(retrieved);
            
            // Recruiter logs out
            SessionManager.setCurrentRecruiter(null);
            assertFalse(SessionManager.isRecruiterLoggedIn());
        }

        @Test
        @DisplayName("Candidate and recruiter can switch sessions")
        void testSessionSwitching() {
            SessionManager.setCurrentCandidate(candidate);
            assertTrue(SessionManager.isCandidateLoggedIn());
            
            // Switch roles
            SessionManager.setCurrentCandidate(null);
            SessionManager.setCurrentRecruiter(recruiter);
            
            assertFalse(SessionManager.isCandidateLoggedIn());
            assertTrue(SessionManager.isRecruiterLoggedIn());
        }
    }

    // ========== EDGE CASES ==========

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesAndBoundaryTests {

        @Test
        @DisplayName("Set same candidate twice")
        void testSetSameCandidateTwice() {
            SessionManager.setCurrentCandidate(candidate);
            SessionManager.setCurrentCandidate(candidate);
            assertEquals(candidate, SessionManager.getCurrentCandidate());
        }

        @Test
        @DisplayName("Set same recruiter twice")
        void testSetSameRecruiterTwice() {
            SessionManager.setCurrentRecruiter(recruiter);
            SessionManager.setCurrentRecruiter(recruiter);
            assertEquals(recruiter, SessionManager.getCurrentRecruiter());
        }

        @Test
        @DisplayName("Set null when already null")
        void testSetNullWhenAlreadyNull() {
            assertNull(SessionManager.getCurrentCandidate());
            SessionManager.setCurrentCandidate(null);
            assertNull(SessionManager.getCurrentCandidate());
        }

        @Test
        @DisplayName("Rapid session changes")
        void testRapidSessionChanges() {
            for (int i = 0; i < 10; i++) {
                Candidate temp = new Candidate("cand" + i, "Candidate " + i, "cand" + i + "@test.com");
                SessionManager.setCurrentCandidate(temp);
                assertEquals(temp, SessionManager.getCurrentCandidate());
            }
        }
    }
}

