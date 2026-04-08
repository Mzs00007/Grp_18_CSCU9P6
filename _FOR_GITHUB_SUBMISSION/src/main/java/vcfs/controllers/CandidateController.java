package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6  
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */

/**
 * CandidateController — Handles all candidate-related business logic.
 * REFACTORED to eliminate code duplication by extending BaseController.
 * 
 * RESPONSIBILITIES:
 *   - Submit meeting requests to recruiters
 *   - View available recruitment lobbies and sessions
 *   - Manage personal meeting schedule
 *   - Cancel meeting requests when needed
 *   - Update profile information (phone, email)
 *   - View request history for reference
 * 
 * KEY FEATURES:
 *   ✓ Uses BaseController helper methods to eliminate duplicated validation/logging
 *   ✓ Input validation (null checks via validateNotEmpty, validateNotNull)
 *   ✓ Comprehensive logging via logOperation/logError helpers
 *   ✓ Exception handling with try-catch blocks
 *   ✓ User-friendly error messages displayed via view layer
 *   ✓ Success feedback with checkmarks (✓)
 * 
 * REFACTORING REDUCTION: ~80 lines of duplicated code reduced to single method calls
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.CareerFairSystem;
import vcfs.core.SystemStateManager;
import vcfs.core.SessionManager;
import vcfs.models.booking.Request;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Lobby;
import vcfs.models.users.Candidate;
import vcfs.views.candidate.CandidateView;

public class CandidateController extends BaseController {
    private final CandidateView view;
    private Candidate currentCandidate;

    /**
     * Constructor — Initialize controller with view reference.
     * 
     * @param view CandidateView instance for displaying results/errors
     * @throws IllegalArgumentException if view is null
     */
    public CandidateController(CandidateView view) {
        if (view == null) {
            throw new IllegalArgumentException("CandidateView cannot be null");
        }
        this.view = view;
    }

    /**
     * Set the currently active candidate for this session.
     * Called during login after authentication succeeds.
     */
    public void setCurrentCandidate(Candidate candidate) {
        this.currentCandidate = candidate;
    }

    public void submitMeetingRequest(Request request) {
        if (!validateLoggedIn(currentCandidate, "Candidate", "Submit meeting request")) {
            view.displayError("Error: No candidate logged in. Please log in to submit requests.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        if (!validateNotNull(request, "Request")) {
            logOperation(LogLevel.WARNING, "CandidateController", "Submit meeting request attempted with null request by candidate: " + candidateName);
            view.displayError("Error: Meeting request data is missing. Please fill all required fields.");
            return;
        }
        
        try {
            currentCandidate.submitRequest(request);
            logOperation(LogLevel.INFO, "CandidateController", "Meeting request submitted by " + candidateName);
            
            // RECORD OPERATION: Track this booking in system state manager
            SystemStateManager stateManager = SystemStateManager.getInstance();
            stateManager.recordStateChange("CANDIDATE_REQUEST", 
                candidateName + " submitted request for interview", true);
            
            // RECORD SESSION ACTIVITY: Track in session manager for live monitoring
            SessionManager sessionManager = SessionManager.getInstance();
            sessionManager.recordActivity(candidateName, "Candidate", "REQUEST_SUBMITTED", 
                "Submitted interview request - active booking");
            
            view.displayMessage("✓ Your meeting request has been submitted successfully!");
        } catch (Exception e) {
            logError("CandidateController", "Failed to submit meeting request by " + candidateName, e);
            
            // RECORD FAILURE: Track failed operation
            SystemStateManager.getInstance().recordStateChange("CANDIDATE_REQUEST_FAILED", 
                candidateName + " request submission failed: " + e.getMessage(), false);
            
            view.displayError("Error submitting request: " + e.getMessage());
        }
    }

    public void submitAutoBookRequest(String desiredTags, int maxAppointments) {
        if (!validateNotEmpty(desiredTags, "Desired tags")) {
            view.displayError("Error: Desired tags cannot be empty.");
            return;
        }
        if (maxAppointments <= 0) {
            view.displayError("Error: Max appointments must be greater than 0.");
            return;
        }
        
        Request req = new Request();
        req.setDesiredTags(desiredTags.trim());
        req.setMaxAppointments(maxAppointments);
        req.setPreferredOrgs("Any");
        
        submitMeetingRequest(req);
    }

    public void viewAvailableLobbies() {
        try {
            CareerFairSystem system = CareerFairSystem.getInstance();
            List<Lobby> lobbies = system.getAllLobbies();
            
            if (lobbies == null) {
                lobbies = new ArrayList<>();
            }
            
            logOperation(LogLevel.INFO, "CandidateController", "Viewing available lobbies. Total: " + lobbies.size());
            
            // RECORD SEARCH ACTIVITY: Track browsing/lobby searches
            SystemStateManager.getInstance().recordStateChange("LOBBY_BROWSED", 
                "Candidate browsed " + lobbies.size() + " available lobbies", true);
            
            view.displayMessage("✓ Available lobbies loaded. Total: " + lobbies.size());
            view.displayLobbies(lobbies);
        } catch (Exception e) {
            logError("CandidateController", "Failed to retrieve available lobbies", e);
            view.displayError("Error retrieving lobbies: " + e.getMessage());
        }
    }

    public void viewLobbyInfo(String lobbyId) {
        if (!validateNotEmpty(lobbyId, "Lobby ID")) {
            logOperation(LogLevel.WARNING, "CandidateController", "View lobby info attempted with empty lobby ID");
            view.displayError("Error: Lobby ID is required.");
            return;
        }
        
        try {
            String trimmedId = safeTrim(lobbyId);
            logOperation(LogLevel.INFO, "CandidateController", "Viewing lobby info for: " + trimmedId);
            view.displayMessage("✓ Loading lobby details for: " + trimmedId);
        } catch (Exception e) {
            logError("CandidateController", "Failed to display lobby info", e);
            view.displayError("Error displaying lobby details: " + e.getMessage());
        }
    }

    public void viewMeetingSchedule() {
        if (!validateLoggedIn(currentCandidate, "Candidate", "View meeting schedule")) {
            view.displayError("Error: No candidate logged in. Please log in to view your schedule.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        try {
            List<MeetingSession> schedule = currentCandidate.getMeetingSchedule();
            logOperation(LogLevel.INFO, "CandidateController", "Viewing meeting schedule for " + candidateName + " (" + schedule.size() + " sessions)");
            
            // RECORD SESSION ACTIVITY: Track schedule views in real-time
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "SCHEDULE_VIEWED", "Accessed meeting schedule (" + schedule.size() + " sessions)");
            
            view.displaySchedule(schedule);
        } catch (Exception e) {
            logError("CandidateController", "Failed to retrieve meeting schedule for " + candidateName, e);
            view.displayError("Error retrieving schedule: " + e.getMessage());
        }
    }

    public void cancelMeetingRequest(String requestId) {
        if (!validateLoggedIn(currentCandidate, "Candidate", "Cancel meeting request")) {
            view.displayError("Error: No candidate logged in. Please log in to cancel requests.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        String finalRequestId = safeTrim(requestId);
        if (finalRequestId == null) {
            logOperation(LogLevel.WARNING, "CandidateController", "Cancel meeting request attempted with empty request ID by candidate: " + candidateName);
            view.displayError("Error: Request ID is required.");
            return;
        }
        
        try {
            currentCandidate.cancelRequest(finalRequestId);
            logOperation(LogLevel.INFO, "CandidateController", "Meeting request cancelled by " + candidateName + ": " + finalRequestId);
            
            // RECORD CANCELLATION: Track booking cancellations
            SystemStateManager.getInstance().recordStateChange("BOOKING_CANCELLED", 
                candidateName + " cancelled meeting request: " + finalRequestId, true);
            
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "BOOKING_CANCELLED", "Cancelled interview request ID: " + finalRequestId);
            
            view.displayMessage("✓ Your meeting request has been cancelled.");
        } catch (Exception e) {
            logError("CandidateController", "Failed to cancel meeting request by " + candidateName, e);
            
            // RECORD CANCEL FAILURE
            SystemStateManager.getInstance().recordStateChange("CANCEL_FAILED", 
                candidateName + " failed to cancel request: " + e.getMessage(), false);
            
            view.displayError("Error cancelling request: " + e.getMessage());
        }
    }

    public void viewRequestHistory() {
        if (!validateLoggedIn(currentCandidate, "Candidate", "View request history")) {
            view.displayError("Error: No candidate logged in. Please log in to view history.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        try {
            List<Request> history = currentCandidate.getRequestHistory();
            logOperation(LogLevel.INFO, "CandidateController", "Viewing request history for " + candidateName + " (" + history.size() + " requests)");
            
            // RECORD SESSION ACTIVITY: Track history access
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "HISTORY_ACCESSED", "Reviewed request history (" + history.size() + " requests)");
            
            view.displayRequestHistory(history);
        } catch (Exception e) {
            logError("CandidateController", "Failed to retrieve request history for " + candidateName, e);
            view.displayError("Error retrieving history: " + e.getMessage());
        }
    }

    public void updateProfile(String phone, String email) {
        if (!validateLoggedIn(currentCandidate, "Candidate", "Update profile")) {
            view.displayError("Error: No candidate logged in. Please log in to update profile.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        String finalPhone = safeTrim(phone);
        if (finalPhone == null) {
            logOperation(LogLevel.WARNING, "CandidateController", "Update profile attempted with empty phone by candidate: " + candidateName);
            view.displayError("Error: Phone number is required.");
            return;
        }
        
        String finalEmail = safeTrim(email);
        if (finalEmail == null) {
            logOperation(LogLevel.WARNING, "CandidateController", "Update profile attempted with empty email by candidate: " + candidateName);
            view.displayError("Error: Email address is required.");
            return;
        }
        
        try {
            currentCandidate.setPhoneNumber(finalPhone);
            currentCandidate.setEmail(finalEmail);
            logOperation(LogLevel.INFO, "CandidateController", "Profile updated by " + candidateName + " (email: " + finalEmail + ")");
            view.displayMessage("✓ Your profile has been updated successfully.");
        } catch (Exception e) {
            logError("CandidateController", "Failed to update profile for " + candidateName, e);
            view.displayError("Error updating profile: " + e.getMessage());
        }
    }

    public List<MeetingSession> getAvailableSessions(String lobbyId) {
        String finalId = safeTrim(lobbyId);
        if (finalId == null) {
            return new ArrayList<>();
        }
        
        CareerFairSystem system = CareerFairSystem.getInstance();
        Optional<Lobby> lobby = system.getLobby(finalId);
        if (lobby.isEmpty()) {
            return new ArrayList<>();
        }
        
        return lobby.get().getAvailableSessions();
    }

    public Candidate getCurrentCandidate() {
        return currentCandidate;
    }
}
