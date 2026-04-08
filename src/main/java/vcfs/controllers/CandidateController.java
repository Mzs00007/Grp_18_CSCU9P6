package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */

/**
 * CandidateController — Handles all candidate-related business logic.
 * 
 * RESPONSIBILITIES:
 *   - Submit meeting requests to interact with recruiters
 *   - View available recruitment lobbies and sessions
 *   - Manage personal meeting schedule
 *   - Cancel meeting requests when needed
 *   - Update profile information (phone, email)
 *   - View request history for reference
 * 
 * KEY FEATURES:
 *   ✓ Input validation (null checks, empty string trimming)
 *   ✓ Comprehensive logging (INFO, WARNING, ERROR levels)
 *   ✓ Exception handling with try-catch blocks
 *   ✓ User-friendly error messages displayed via view
 *   ✓ Success feedback with checkmarks (✓)
 * 
 * USAGE FLOW:
 *   1. CandidateScreen creates this controller
 *   2. UI panels call controller methods (submitRequest, viewLobbies, etc.)
 *   3. Controller validates inputs and calls Candidate model methods
 *   4. Results displayed via view.displayMessage() / view.displayError()
 * 
 * Implemented by: Zaid
 * 
 * CODE AUDIT: ZAID (mzs00007) — Input validation and defensive null checks throughout class
 * - Constructor validates view parameter (non-null)
 * - All methods check currentCandidate before use
 * - String parameters trimmed before empty checks
 * - Comprehensive try-catch with logging on all operations
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.CareerFairSystem;
import vcfs.models.booking.Request;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Lobby;
import vcfs.models.users.Candidate;
import vcfs.views.candidate.CandidateView;

public class CandidateController {
    private final CandidateView view;
    private Candidate currentCandidate;

    /**
     * Constructor — Initialize controller with view reference.
     * 
     * @param view CandidateView instance for displaying results/errors
     * @throws IllegalArgumentException if view is null
     * 
     * CODE AUDIT: ZAID (mzs00007) — Added null-safety check to prevent NPE when view methods are called
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
     * 
     * @param candidate The Candidate object from login (cannot be null)
     */
    public void setCurrentCandidate(Candidate candidate) {
        this.currentCandidate = candidate;
    }

    /**
     * Submit a new meeting request to a specific recruiter.
     * 
     * VALIDATION:
     *   ✓ Checks candidate is logged in
     *   ✓ Checks request object is not null
     * 
     * WORKFLOW:
     *   1. Validate candidate logged in → if not, show error & return
     *   2. Validate request data exists → if not, show error & return
     *   3. Call candidate.submitRequest() to send request
     *   4. Show success dialog if successful
     *   5. Log action at INFO level
     *   6. Catch and handle any exceptions
     * 
     * @param request The Request object containing recruiter and details
     * @see vcfs.models.booking.Request
     */
    public void submitMeetingRequest(Request request) {
        String candidateName = (currentCandidate != null) ? currentCandidate.getDisplayName() : "UNKNOWN";
        
        if (currentCandidate == null) {
            Logger.log(LogLevel.WARNING, "[CandidateController] Submit meeting request attempted with no candidate logged in");
            view.displayError("Error: No candidate logged in. Please log in to submit requests.");
            return;
        }
        
        if (request == null) {
            Logger.log(LogLevel.WARNING, "[CandidateController] Submit meeting request attempted with null request by candidate: " + candidateName);
            view.displayError("Error: Meeting request data is missing. Please fill all required fields.");
            return;
        }
        
        try {
            currentCandidate.submitRequest(request);
            Logger.log(LogLevel.INFO, "[CandidateController] Meeting request submitted by " + candidateName);
            view.displayMessage("✓ Your meeting request has been submitted successfully!");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to submit meeting request by " + candidateName, e);
            view.displayError("Error submitting request: " + e.getMessage());
        }
    }

    /**
     * Submit an auto-book request from the UI with tags and max appointments.
     * Controller creates the Request object - NOT the view (MVC separation).
     * CRITICAL FIX: Proper separation of concerns - UI delegates to controller.
     * 
     * @param desiredTags Comma-separated tags the candidate is interested in
     * @param maxAppointments Maximum number of appointments to book
     * @throws IllegalArgumentException if tags/appointments are invalid
     */
    public void submitAutoBookRequest(String desiredTags, int maxAppointments) {
        if (desiredTags == null || desiredTags.trim().isEmpty()) {
            view.displayError("Error: Desired tags cannot be empty.");
            return;
        }
        if (maxAppointments <= 0) {
            view.displayError("Error: Max appointments must be greater than 0.");
            return;
        }
        
        // Controller creates the Request - NOT the view
        Request req = new Request();
        req.setDesiredTags(desiredTags.trim());
        req.setMaxAppointments(maxAppointments);
        req.setPreferredOrgs("Any");  // Default preference
        
        // Delegate to existing method (model update happens here)
        submitMeetingRequest(req);
    }

    public void viewAvailableLobbies() {
        try {
            // Query system for all lobbies where current candidate can join
            CareerFairSystem system = CareerFairSystem.getInstance();
            List<Lobby> lobbies = system.getAllLobbies();
            
            if (lobbies == null) {
                lobbies = new ArrayList<>();
            }
            
            Logger.log(LogLevel.INFO, "[CandidateController] Viewing available lobbies. Total: " + lobbies.size());
            view.displayMessage("✓ Available lobbies loaded. Total: " + lobbies.size());
            view.displayLobbies(lobbies);
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to retrieve available lobbies", e);
            view.displayError("Error retrieving lobbies: " + e.getMessage());
        }
    }

    public void viewLobbyInfo(String lobbyId) {
        if (lobbyId == null || (lobbyId = lobbyId.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[CandidateController] View lobby info attempted with empty lobby ID");
            view.displayError("Error: Lobby ID is required.");
            return;
        }
        
        try {
            Logger.log(LogLevel.INFO, "[CandidateController] Viewing lobby info for: " + lobbyId);
            view.displayMessage("✓ Loading lobby details for: " + lobbyId);
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to display lobby info", e);
            view.displayError("Error displaying lobby details: " + e.getMessage());
        }
    }

    public void viewMeetingSchedule() {
        String candidateName = (currentCandidate != null) ? currentCandidate.getDisplayName() : "UNKNOWN";
        
        if (currentCandidate == null) {
            Logger.log(LogLevel.WARNING, "[CandidateController] View meeting schedule attempted with no candidate logged in");
            view.displayError("Error: No candidate logged in. Please log in to view your schedule.");
            return;
        }
        
        try {
            List<MeetingSession> schedule = currentCandidate.getMeetingSchedule();
            Logger.log(LogLevel.INFO, "[CandidateController] Viewing meeting schedule for " + candidateName + " (" + schedule.size() + " sessions)");
            view.displaySchedule(schedule);
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to retrieve meeting schedule for " + candidateName, e);
            view.displayError("Error retrieving schedule: " + e.getMessage());
        }
    }

    public void cancelMeetingRequest(String requestId) {
        String candidateName = (currentCandidate != null) ? currentCandidate.getDisplayName() : "UNKNOWN";
        
        if (currentCandidate == null) {
            Logger.log(LogLevel.WARNING, "[CandidateController] Cancel meeting request attempted with no candidate logged in");
            view.displayError("Error: No candidate logged in. Please log in to cancel requests.");
            return;
        }
        
        if (requestId == null || (requestId = requestId.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[CandidateController] Cancel meeting request attempted with empty request ID by candidate: " + candidateName);
            view.displayError("Error: Request ID is required.");
            return;
        }
        
        try {
            currentCandidate.cancelRequest(requestId);
            Logger.log(LogLevel.INFO, "[CandidateController] Meeting request cancelled by " + candidateName + ": " + requestId);
            view.displayMessage("✓ Your meeting request has been cancelled.");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to cancel meeting request by " + candidateName, e);
            view.displayError("Error cancelling request: " + e.getMessage());
        }
    }

    public void viewRequestHistory() {
        String candidateName = (currentCandidate != null) ? currentCandidate.getDisplayName() : "UNKNOWN";
        
        if (currentCandidate == null) {
            Logger.log(LogLevel.WARNING, "[CandidateController] View request history attempted with no candidate logged in");
            view.displayError("Error: No candidate logged in. Please log in to view history.");
            return;
        }
        
        try {
            List<Request> history = currentCandidate.getRequestHistory();
            Logger.log(LogLevel.INFO, "[CandidateController] Viewing request history for " + candidateName + " (" + history.size() + " requests)");
            view.displayRequestHistory(history);
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to retrieve request history for " + candidateName, e);
            view.displayError("Error retrieving history: " + e.getMessage());
        }
    }

    public void updateProfile(String phone, String email) {
        String candidateName = (currentCandidate != null) ? currentCandidate.getDisplayName() : "UNKNOWN";
        
        if (currentCandidate == null) {
            Logger.log(LogLevel.WARNING, "[CandidateController] Update profile attempted with no candidate logged in");
            view.displayError("Error: No candidate logged in. Please log in to update profile.");
            return;
        }
        
        if (phone == null || (phone = phone.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[CandidateController] Update profile attempted with empty phone by candidate: " + candidateName);
            view.displayError("Error: Phone number is required.");
            return;
        }
        
        if (email == null || (email = email.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[CandidateController] Update profile attempted with empty email by candidate: " + candidateName);
            view.displayError("Error: Email address is required.");
            return;
        }
        
        try {
            currentCandidate.setPhoneNumber(phone);
            currentCandidate.setEmail(email);
            Logger.log(LogLevel.INFO, "[CandidateController] Profile updated by " + candidateName + " (email: " + email + ")");
            view.displayMessage("✓ Your profile has been updated successfully.");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to update profile for " + candidateName, e);
            view.displayError("Error updating profile: " + e.getMessage());
        }
    }

    public List<MeetingSession> getAvailableSessions(String lobbyId) {
        if (lobbyId == null || (lobbyId = lobbyId.trim()).isEmpty()) {
            return new ArrayList<>();
        }
        
        CareerFairSystem system = CareerFairSystem.getInstance();
        Optional<Lobby> lobby = system.getLobby(lobbyId);
        if (lobby.isEmpty()) {
            return new ArrayList<>();
        }
        
        return lobby.get().getAvailableSessions();
    }
}


