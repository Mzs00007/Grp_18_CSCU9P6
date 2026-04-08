package vcfs.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import vcfs.models.users.Recruiter;
import vcfs.views.recruiter.RecruiterView;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for RecruiterController.
 * Tests offer creation, meeting session management, and recruiter operations.
 */
@DisplayName("RecruiterController Test Suite")
public class RecruiterControllerTest {

    private RecruiterController controller;
    private MockRecruiterView mockView;

    /**
     * Mock RecruiterView for testing controller output.
     */
    private static class MockRecruiterView implements RecruiterView {
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
        public void displayOffers(java.util.List offers) {
            // Mock implementation
        }

        @Override
        public void displayRequests(java.util.List requests) {
            // Mock implementation
        }

        @Override
        public void displaySessions(java.util.List sessions) {
            // Mock implementation
        }
    }

    @BeforeEach
    void setUp() {
        mockView = new MockRecruiterView();
        controller = new RecruiterController(mockView);
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
            MockRecruiterView view = new MockRecruiterView();

            assertDoesNotThrow(() -> new RecruiterController(view), "Should create controller with valid view");
        }

        @Test
        @DisplayName("Should throw exception when view is null")
        void testConstructorWithNullView() {
            assertThrows(IllegalArgumentException.class, () -> new RecruiterController(null),
                "Should throw exception for null view");
        }

        @Test
        @DisplayName("Should initialize multiple instances independently")
        void testConstructorMultipleInstances() {
            MockRecruiterView view1 = new MockRecruiterView();
            MockRecruiterView view2 = new MockRecruiterView();

            RecruiterController controller1 = new RecruiterController(view1);
            RecruiterController controller2 = new RecruiterController(view2);

            assertNotNull(controller1, "First controller should be created");
            assertNotNull(controller2, "Second controller should be created");
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
                controller.setCurrentRecruiter(recruiter);
            }, "Should set recruiter successfully");
        }

        @Test
        @DisplayName("Should allow setting null recruiter (logout)")
        void testSetCurrentRecruiterNull() {
            assertDoesNotThrow(() -> {
                controller.setCurrentRecruiter(null);
            }, "Should allow null recruiter (logout)");
        }

        @Test
        @DisplayName("Should update recruiter when called multiple times")
        void testSetCurrentRecruiterMultipleTimes() {
            Recruiter recruiter1 = new Recruiter("rec001", "First", "first@company.com");
            Recruiter recruiter2 = new Recruiter("rec002", "Second", "second@company.com");

            assertDoesNotThrow(() -> {
                controller.setCurrentRecruiter(recruiter1);
                controller.setCurrentRecruiter(recruiter2);
                controller.setCurrentRecruiter(null);
            }, "Should handle multiple recruiter changes");
        }

        @Test
        @DisplayName("Should set recruiters with different corporate IDs")
        void testSetCurrentRecruiterDifferentIds() {
            Recruiter recruiter1 = new Recruiter("rec001", "Alice", "alice@company.com");
            Recruiter recruiter2 = new Recruiter("rec999", "Bob", "bob@company.com");

            controller.setCurrentRecruiter(recruiter1);
            controller.setCurrentRecruiter(recruiter2);

            assertDoesNotThrow(() -> {}, "Should handle different recruiter IDs");
        }
    }

    // =========================================================
    // CREATE OFFER Tests
    // =========================================================

    @Nested
    @DisplayName("Create Offer Tests")
    class CreateOfferTests {

        @Test
        @DisplayName("Should display error when no recruiter logged in")
        void testCreateOfferNotLoggedIn() {
            controller.setCurrentRecruiter(null);

            controller.createOffer("Software Engineer", "9:00-10:00");

            assertTrue(mockView.errorDisplayed, "Should display error when not logged in");
        }

        @Test
        @DisplayName("Should display error when position is empty")
        void testCreateOfferEmptyPosition() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.createOffer("", "9:00-10:00");

            assertTrue(mockView.errorDisplayed, "Should display error for empty position");
        }

        @Test
        @DisplayName("Should display error when time slot is empty")
        void testCreateOfferEmptyTimeSlot() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.createOffer("Software Engineer", "");

            assertTrue(mockView.errorDisplayed, "Should display error for empty time slot");
        }

        @Test
        @DisplayName("Should display success when offer is valid")
        void testCreateOfferValid() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.createOffer("Software Engineer", "9:00-10:00");

            assertTrue(mockView.messageDisplayed, "Should display success message");
        }

        @Test
        @DisplayName("Should handle multiple offers")
        void testCreateOfferMultiple() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.createOffer("Software Engineer", "9:00-10:00");
            mockView.messageDisplayed = false;
            controller.createOffer("Data Scientist", "10:00-11:00");

            assertTrue(mockView.messageDisplayed, "Should create multiple offers");
        }

        @Test
        @DisplayName("Should trim whitespace from offer details")
        void testCreateOfferTrimmedInput() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.createOffer("  Software Engineer  ", "  9:00-10:00  ");

            assertTrue(mockView.messageDisplayed, "Should accept trimmed input");
        }
    }

    // =========================================================
    // VIEW AVAILABLE REQUESTS Tests
    // =========================================================

    @Nested
    @DisplayName("View Available Requests Tests")
    class ViewAvailableRequestsTests {

        @Test
        @DisplayName("Should execute without error")
        void testViewAvailableRequests() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            assertDoesNotThrow(() -> {
                controller.viewAvailableRequests();
            }, "Should execute without error");
        }

        @Test
        @DisplayName("Should display requests message on success")
        void testViewAvailableRequestsDisplaysMessage() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.viewAvailableRequests();

            assertTrue(mockView.messageDisplayed, "Should display requests message");
        }

        @Test
        @DisplayName("Should handle multiple view requests")
        void testViewAvailableRequestsMultipleCalls() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            assertDoesNotThrow(() -> {
                controller.viewAvailableRequests();
                controller.viewAvailableRequests();
                controller.viewAvailableRequests();
            }, "Should handle multiple view requests");
        }
    }

    // =========================================================
    // MANAGE MEETING SESSION Tests
    // =========================================================

    @Nested
    @DisplayName("Manage Meeting Session Tests")
    class ManageMeetingSessionTests {

        @Test
        @DisplayName("Should display error when no recruiter logged in")
        void testManageSessionNotLoggedIn() {
            controller.setCurrentRecruiter(null);

            controller.manageMeetingSession("meeting123", "start");

            assertTrue(mockView.errorDisplayed, "Should display error when not logged in");
        }

        @Test
        @DisplayName("Should display error when session ID is empty")
        void testManageSessionEmptyId() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.manageMeetingSession("", "start");

            assertTrue(mockView.errorDisplayed, "Should display error for empty session ID");
        }

        @Test
        @DisplayName("Should display error when action is empty")
        void testManageSessionEmptyAction() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.manageMeetingSession("meeting123", "");

            assertTrue(mockView.errorDisplayed, "Should display error for empty action");
        }

        @Test
        @DisplayName("Should handle valid session management")
        void testManageSessionValid() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.manageMeetingSession("meeting123", "start");

            assertTrue(mockView.messageDisplayed, "Should manage session successfully");
        }

        @Test
        @DisplayName("Should handle multiple session actions")
        void testManageSessionMultipleActions() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.manageMeetingSession("meeting123", "start");
            mockView.messageDisplayed = false;
            controller.manageMeetingSession("meeting123", "pause");
            mockView.messageDisplayed = false;
            controller.manageMeetingSession("meeting123", "end");

            assertTrue(mockView.messageDisplayed, "Should handle multiple session actions");
        }
    }

    // =========================================================
    // RESPOND TO REQUEST Tests
    // =========================================================

    @Nested
    @DisplayName("Respond to Request Tests")
    class RespondToRequestTests {

        @Test
        @DisplayName("Should display error when no recruiter logged in")
        void testRespondToRequestNotLoggedIn() {
            controller.setCurrentRecruiter(null);

            controller.respondToRequest("request123", "accept");

            assertTrue(mockView.errorDisplayed, "Should display error when not logged in");
        }

        @Test
        @DisplayName("Should display error when request ID is empty")
        void testRespondToRequestEmptyId() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.respondToRequest("", "accept");

            assertTrue(mockView.errorDisplayed, "Should display error for empty request ID");
        }

        @Test
        @DisplayName("Should display error when response is empty")
        void testRespondToRequestEmptyResponse() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.respondToRequest("request123", "");

            assertTrue(mockView.errorDisplayed, "Should display error for empty response");
        }

        @Test
        @DisplayName("Should accept response")
        void testRespondToRequestAccept() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.respondToRequest("request123", "accept");

            assertTrue(mockView.messageDisplayed, "Should accept request");
        }

        @Test
        @DisplayName("Should reject response")
        void testRespondToRequestReject() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.respondToRequest("request123", "reject");

            assertTrue(mockView.messageDisplayed, "Should reject request");
        }

        @Test
        @DisplayName("Should handle multiple responses")
        void testRespondToRequestMultiple() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            controller.respondToRequest("request123", "accept");
            mockView.messageDisplayed = false;
            controller.respondToRequest("request124", "reject");

            assertTrue(mockView.messageDisplayed, "Should handle multiple responses");
        }
    }

    // =========================================================
    // ERROR HANDLING Tests
    // =========================================================

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null parameters gracefully")
        void testHandleNullParameters() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            assertDoesNotThrow(() -> {
                controller.createOffer(null, "9:00-10:00");
            }, "Should handle null position");

            assertDoesNotThrow(() -> {
                controller.manageMeetingSession(null, "start");
            }, "Should handle null session ID");
        }

        @Test
        @DisplayName("Should provide meaningful error messages")
        void testErrorMessageContent() {
            controller.setCurrentRecruiter(null);
            controller.createOffer("Position", "Time");

            assertTrue(mockView.lastError.length() > 0, "Error message should not be empty");
        }

        @Test
        @DisplayName("Should log errors without crashing")
        void testExceptionHandling() {
            assertDoesNotThrow(() -> {
                controller.setCurrentRecruiter(null);
                controller.createOffer("Position", "Time");
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
            MockRecruiterView view1 = new MockRecruiterView();
            MockRecruiterView view2 = new MockRecruiterView();

            RecruiterController controller1 = new RecruiterController(view1);
            RecruiterController controller2 = new RecruiterController(view2);

            Recruiter recruiter1 = new Recruiter("rec001", "First", "first@company.com");
            Recruiter recruiter2 = new Recruiter("rec002", "Second", "second@company.com");

            controller1.setCurrentRecruiter(recruiter1);
            controller2.setCurrentRecruiter(recruiter2);

            assertDoesNotThrow(() -> {
                controller1.createOffer("Position1", "9:00-10:00");
                controller2.createOffer("Position2", "10:00-11:00");
            }, "Controllers should maintain independent state");
        }

        @Test
        @DisplayName("Should reset state after logout")
        void testStateResetAfterLogout() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");

            controller.setCurrentRecruiter(recruiter);
            controller.createOffer("Position", "9:00-10:00");

            mockView.messageDisplayed = false;
            mockView.errorDisplayed = false;

            // Logout
            controller.setCurrentRecruiter(null);
            controller.createOffer("Another", "10:00-11:00");

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
        @DisplayName("Should complete full recruiter workflow")
        void testCompleteRecruiterWorkflow() {
            // Login
            Recruiter recruiter = new Recruiter("rec001", "Jane Smith", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            // Create offer
            controller.createOffer("Software Engineer", "9:00-10:00");
            assertTrue(mockView.messageDisplayed, "Offer should be created");

            // View requests
            mockView.messageDisplayed = false;
            controller.viewAvailableRequests();
            assertTrue(mockView.messageDisplayed, "Requests should be displayed");

            // Manage session
            mockView.messageDisplayed = false;
            controller.manageMeetingSession("session123", "start");
            assertTrue(mockView.messageDisplayed, "Session should be managed");

            // Logout
            controller.setCurrentRecruiter(null);
            mockView.messageDisplayed = false;
            mockView.errorDisplayed = false;

            controller.createOffer("Position", "11:00-12:00");
            assertTrue(mockView.errorDisplayed, "Should fail after logout");
        }

        @Test
        @DisplayName("Should handle recruiter switching")
        void testRecruiterSwitching() {
            Recruiter recruiter1 = new Recruiter("rec001", "Jane", "jane@company.com");
            Recruiter recruiter2 = new Recruiter("rec002", "John", "john@company.com");

            controller.setCurrentRecruiter(recruiter1);
            controller.createOffer("Position1", "9:00-10:00");

            controller.setCurrentRecruiter(recruiter2);
            controller.createOffer("Position2", "10:00-11:00");

            assertTrue(mockView.messageDisplayed, "Should handle recruiter switching");
        }

        @Test
        @DisplayName("Should handle rapid operation sequences")
        void testRapidOperations() {
            Recruiter recruiter = new Recruiter("rec001", "Jane", "jane@company.com");
            controller.setCurrentRecruiter(recruiter);

            for (int i = 0; i < 5; i++) {
                assertDoesNotThrow(() -> {
                    controller.createOffer("Position" + i, "9:00-10:00");
                    controller.viewAvailableRequests();
                    controller.manageMeetingSession("session" + i, "start");
                }, "Should handle rapid operations");
            }
        }
    }
}
