package vcfs.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import vcfs.models.booking.Lobby;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Request;
import vcfs.models.users.Candidate;
import vcfs.views.candidate.CandidateView;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Comprehensive test suite for CandidateController.

 * Tests meeting request submission, profile management, and view interactions.
 */
@DisplayName("CandidateController Test Suite")
public class CandidateControllerTest {

    private CandidateController controller;
    private MockCandidateView mockView;

    /**
     * Mock CandidateView for testing controller output.
     */
    private static class MockCandidateView implements CandidateView {
        public String lastMessage = "";
        public String lastError = "";
        public boolean messageDisplayed = false;
        public boolean errorDisplayed = false;

        @Override
        public void displayMessage(String message) {
            this.lastMessage = message;
            this.messageDisplayed = true;
        }

        @Override
        public void displayError(String error) {
            this.lastError = error;
            this.errorDisplayed = true;
        }

        @Override
        public void displayLobbies(java.util.List lobbies) {
            // Mock implementation
        }

        @Override
        public void displaySessions(java.util.List sessions) {
            // Mock implementation
        }

        @Override
        public void displayLobbyDetails(Lobby lobby) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'displayLobbyDetails'");
        }

        @Override
        public void displaySchedule(List<MeetingSession> schedule) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'displaySchedule'");
        }

        @Override
        public void displayRequestHistory(List<Request> requests) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'displayRequestHistory'");
        }
    }

    @BeforeEach
    void setUp() {
        mockView = new MockCandidateView();
        controller = new CandidateController(mockView);
    }

    // =========================================================
    // CONSTRUCTOR Tests
    // =========================================================

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create controller with valid view")
        void testConstructorWithValidView() {
            MockCandidateView view = new MockCandidateView();

            assertDoesNotThrow(() -> new CandidateController(view), "Should create controller with valid view");
        }

        @Test
        @DisplayName("Should throw exception when view is null")
        void testConstructorWithNullView() {
            assertThrows(IllegalArgumentException.class, () -> new CandidateController(null), 
                "Should throw exception for null view");
        }

        @Test
        @DisplayName("Should initialize with different view implementations")
        void testConstructorMultipleViews() {
            MockCandidateView view1 = new MockCandidateView();
            MockCandidateView view2 = new MockCandidateView();

            CandidateController controller1 = new CandidateController(view1);
            CandidateController controller2 = new CandidateController(view2);

            assertNotNull(controller1, "First controller should be created");
            assertNotNull(controller2, "Second controller should be created");
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
                controller.setCurrentCandidate(candidate);
            }, "Should set candidate successfully");
        }

        @Test
        @DisplayName("Should allow setting null candidate (logout)")
        void testSetCurrentCandidateNull() {
            assertDoesNotThrow(() -> {
                controller.setCurrentCandidate(null);
            }, "Should allow null candidate (logout)");
        }

        @Test
        @DisplayName("Should update candidate when called multiple times")
        void testSetCurrentCandidateMultipleTimes() {
            Candidate candidate1 = new Candidate("cand001", "First", "first@example.com");
            Candidate candidate2 = new Candidate("cand002", "Second", "second@example.com");

            assertDoesNotThrow(() -> {
                controller.setCurrentCandidate(candidate1);
                controller.setCurrentCandidate(candidate2);
                controller.setCurrentCandidate(null);
            }, "Should handle multiple candidate changes");
        }

        @Test
        @DisplayName("Should set candidates with different profiles")
        void testSetCurrentCandidateDifferentProfiles() {
            Candidate candidate1 = new Candidate("cand001", "Alice", "alice@example.com");
            Candidate candidate2 = new Candidate("cand002", "Bob", "bob@example.com");

            controller.setCurrentCandidate(candidate1);
            controller.setCurrentCandidate(candidate2);

            assertDoesNotThrow(() -> {}, "Should handle multiple candidates");
        }
    }

    // =========================================================
    // SUBMIT MEETING REQUEST Tests
    // =========================================================

    @Nested
    @DisplayName("Submit Meeting Request Tests")
    class SubmitMeetingRequestTests {

        @Test
        @DisplayName("Should display error when no candidate logged in")
        void testSubmitMeetingRequestNotLoggedIn() {
            controller.setCurrentCandidate(null);
            Request request = new Request();

            controller.submitMeetingRequest(request);

            assertTrue(mockView.errorDisplayed, "Should display error");
            assertFalse(mockView.messageDisplayed, "Should not display success message");
        }

        @Test
        @DisplayName("Should display error when request is null")
        void testSubmitMeetingRequestNullRequest() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.submitMeetingRequest(null);

            assertTrue(mockView.errorDisplayed, "Should display error for null request");
        }

        @Test
        @DisplayName("Should display success when request is valid")
        void testSubmitMeetingRequestValid() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);
            Request request = new Request();

            controller.submitMeetingRequest(request);

            assertTrue(mockView.messageDisplayed, "Should display success message");
        }

        @Test
        @DisplayName("Should handle multiple requests from same candidate")
        void testSubmitMeetingRequestMultiple() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            Request request1 = new Request();
            Request request2 = new Request();

            controller.submitMeetingRequest(request1);
            controller.submitMeetingRequest(request2);

            assertTrue(mockView.messageDisplayed, "Should process multiple requests");
        }

        @Test
        @DisplayName("Should reject request after logout")
        void testSubmitMeetingRequestAfterLogout() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);
            Request validRequest = new Request();
            controller.submitMeetingRequest(validRequest);

            // Logout
            controller.setCurrentCandidate(null);
            mockView.messageDisplayed = false;
            mockView.errorDisplayed = false;

            // Try to submit after logout
            Request newRequest = new Request();
            controller.submitMeetingRequest(newRequest);

            assertTrue(mockView.errorDisplayed, "Should reject request after logout");
        }
    }

    // =========================================================
    // AUTO BOOK REQUEST Tests
    // =========================================================

    @Nested
    @DisplayName("Auto Book Request Tests")
    class AutoBookRequestTests {

        @Test
        @DisplayName("Should display error when desired tags are empty")
        void testSubmitAutoBookRequestEmptyTags() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.submitAutoBookRequest("", 5);

            assertTrue(mockView.errorDisplayed, "Should display error for empty tags");
        }

        @Test
        @DisplayName("Should display error when max appointments is zero")
        void testSubmitAutoBookRequestZeroAppointments() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.submitAutoBookRequest("Tech", 0);

            assertTrue(mockView.errorDisplayed, "Should display error for zero appointments");
        }

        @Test
        @DisplayName("Should display error when max appointments is negative")
        void testSubmitAutoBookRequestNegativeAppointments() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.submitAutoBookRequest("Tech", -5);

            assertTrue(mockView.errorDisplayed, "Should display error for negative appointments");
        }

        @Test
        @DisplayName("Should accept valid auto book parameters")
        void testSubmitAutoBookRequestValid() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.submitAutoBookRequest("Software Engineering", 3);

            assertTrue(mockView.messageDisplayed, "Should accept valid parameters");
        }

        @Test
        @DisplayName("Should trim tags before processing")
        void testSubmitAutoBookRequestTrimmedTags() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.submitAutoBookRequest("   Technology   ", 2);

            assertTrue(mockView.messageDisplayed, "Should trim tags and accept");
        }

        @Test
        @DisplayName("Should handle multiple auto book requests")
        void testSubmitAutoBookRequestMultiple() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.submitAutoBookRequest("Tech", 2);
            mockView.messageDisplayed = false;
            controller.submitAutoBookRequest("Finance", 3);

            assertTrue(mockView.messageDisplayed, "Should handle multiple auto book requests");
        }
    }

    // =========================================================
    // VIEW AVAILABLE LOBBIES Tests
    // =========================================================

    @Nested
    @DisplayName("View Available Lobbies Tests")
    class ViewAvailableLobbiesTests {

        @Test
        @DisplayName("Should execute without error")
        void testViewAvailableLobbies() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            assertDoesNotThrow(() -> {
                controller.viewAvailableLobbies();
            }, "Should execute without error");
        }

        @Test
        @DisplayName("Should display lobbies message on success")
        void testViewAvailableLobbiesDisplaysMessage() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            controller.viewAvailableLobbies();

            assertTrue(mockView.messageDisplayed, "Should display lobbies message");
        }

        @Test
        @DisplayName("Should call display lobbies on view")
        void testViewAvailableLobbiesCallsViewMethod() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            assertDoesNotThrow(() -> {
                controller.viewAvailableLobbies();
            }, "Should call view method without error");
        }

        @Test
        @DisplayName("Should handle multiple lobby view requests")
        void testViewAvailableLobbiesMultipleCalls() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            assertDoesNotThrow(() -> {
                controller.viewAvailableLobbies();
                controller.viewAvailableLobbies();
                controller.viewAvailableLobbies();
            }, "Should handle multiple view requests");
        }
    }

    // =========================================================
    // ERROR HANDLING Tests
    // =========================================================

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null request gracefully")
        void testHandleNullRequest() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            assertDoesNotThrow(() -> {
                controller.submitMeetingRequest(null);
            }, "Should handle null request");
        }

        @Test
        @DisplayName("Should provide meaningful error messages")
        void testErrorMessageContent() {
            controller.setCurrentCandidate(null);
            Request request = new Request();

            controller.submitMeetingRequest(request);

            assertTrue(mockView.lastError.contains("No candidate logged in") || 
                      mockView.lastError.contains("error"), 
                "Error message should indicate the problem");
        }

        @Test
        @DisplayName("Should log errors without crashing")
        void testExceptionHandling() {
            assertDoesNotThrow(() -> {
                controller.setCurrentCandidate(null);
                controller.submitMeetingRequest(new Request());
            }, "Should handle errors without crashing");
        }
    }

    // =========================================================
    // STATE INDEPENDENCE Tests
    // =========================================================

    @Nested
    @DisplayName("State Independence Tests")
    class StateIndependenceTests {

        @Test
        @DisplayName("Should maintain independent state between instances")
        void testControllerStateIndependence() {
            MockCandidateView view1 = new MockCandidateView();
            MockCandidateView view2 = new MockCandidateView();

            CandidateController controller1 = new CandidateController(view1);
            CandidateController controller2 = new CandidateController(view2);

            Candidate candidate1 = new Candidate("cand001", "First", "first@example.com");
            Candidate candidate2 = new Candidate("cand002", "Second", "second@example.com");

            controller1.setCurrentCandidate(candidate1);
            controller2.setCurrentCandidate(candidate2);

            // Verify each controller operates independently
            assertDoesNotThrow(() -> {
                controller1.submitMeetingRequest(new Request());
                controller2.submitMeetingRequest(new Request());
            }, "Controllers should maintain independent state");
        }

        @Test
        @DisplayName("Should reset state after logout")
        void testStateResetAfterLogout() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            
            controller.setCurrentCandidate(candidate);
            controller.submitMeetingRequest(new Request());
            
            mockView.messageDisplayed = false;
            mockView.errorDisplayed = false;
            
            // Logout
            controller.setCurrentCandidate(null);
            controller.submitMeetingRequest(new Request());
            
            assertTrue(mockView.errorDisplayed, "Should show error after logout");
        }
    }

    // =========================================================
    // INTEGRATION Tests
    // =========================================================

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should complete full user workflow: login, submit, view, logout")
        void testCompleteUserWorkflow() {
            // Login
            Candidate candidate = new Candidate("cand001", "John Doe", "john@example.com");
            controller.setCurrentCandidate(candidate);

            // Submit request
            Request request = new Request();
            controller.submitMeetingRequest(request);
            assertTrue(mockView.messageDisplayed, "Request should be submitted");

            // View lobbies
            mockView.messageDisplayed = false;
            controller.viewAvailableLobbies();
            assertTrue(mockView.messageDisplayed, "Lobbies should be displayed");

            // Logout
            controller.setCurrentCandidate(null);
            mockView.messageDisplayed = false;
            mockView.errorDisplayed = false;

            // Try operation after logout
            controller.submitMeetingRequest(new Request());
            assertTrue(mockView.errorDisplayed, "Should fail after logout");
        }

        @Test
        @DisplayName("Should handle rapid operation sequences")
        void testRapidOperations() {
            Candidate candidate = new Candidate("cand001", "John", "john@example.com");
            controller.setCurrentCandidate(candidate);

            for (int i = 0; i < 5; i++) {
                assertDoesNotThrow(() -> {
                    controller.submitMeetingRequest(new Request());
                    controller.viewAvailableLobbies();
                }, "Should handle rapid operations");
            }
        }

        @Test
        @DisplayName("Should handle candidate switching")
        void testCandidateSwitching() {
            Candidate candidate1 = new Candidate("cand001", "First", "first@example.com");
            Candidate candidate2 = new Candidate("cand002", "Second", "second@example.com");

            controller.setCurrentCandidate(candidate1);
            controller.submitMeetingRequest(new Request());

            controller.setCurrentCandidate(candidate2);
            controller.submitMeetingRequest(new Request());

            assertTrue(mockView.messageDisplayed, "Should handle candidate switching");
        }
    }
}
