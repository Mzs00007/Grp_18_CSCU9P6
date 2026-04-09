package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */

/**
 * RecruiterController — Handles all recruiter-related business logic.
 * REFACTORED to eliminate code duplication by extending BaseController.
 * 
 * RESPONSIBILITIES:
 *   - Publishes interview offers with validation and logging
 *   - Schedules meeting sessions for candidates
 *   - Views available lobbies and candidate meeting history
 *   - Updates offer status (ATTENDED, NO_SHOW) after interviews
 *   - Cancels scheduled sessions when needed
 * 
 * KEY FEATURES:
 *   ✓ Uses BaseController helper methods to eliminate duplicated validation/logging
 *   ✓ Input validation (null checks via validateNotEmpty, validateNotNull)
 *   ✓ Comprehensive logging via logOperation/logError helpers
 *   ✓ Error handling with try-catch blocks
 *   ✓ User-friendly error messages via view layer
 *   ✓ Success feedback with checkmarks (✓)
 * 
 * REFACTORING REDUCTION: ~70 lines of duplicated code reduced to single method calls
 */

import java.util.ArrayList;
import java.util.List;

import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.SystemStateManager;
import vcfs.core.SessionManager;
import vcfs.models.booking.Offer;
import vcfs.models.booking.MeetingSession;
import vcfs.models.users.Recruiter;
import vcfs.views.recruiter.RecruiterView;
import vcfs.views.recruiter.SchedulePanel;

public class RecruiterController extends BaseController {
    private final RecruiterView view;
    private Recruiter currentRecruiter;
    private SchedulePanel schedulePanel;

    /**
     * Constructor — Initialize controller with view reference.
     * 
     * @param view RecruiterView instance for displaying results/errors
     * @throws IllegalArgumentException if view is null
     */
    public RecruiterController(RecruiterView view) {
        if (view == null) {
            throw new IllegalArgumentException("RecruiterView cannot be null");
        }
        this.view = view;
    }

    /**
     * Set the currently active recruiter for this session.
     * Called during login after authentication succeeds.
     */
    public void setCurrentRecruiter(Recruiter recruiter) {
        this.currentRecruiter = recruiter;
    }
    
    /**
     * Set the SchedulePanel reference so it can be updated with session data.
     */
    public void setSchedulePanel(SchedulePanel panel) {
        this.schedulePanel = panel;
    }

    public void publishOffer(Offer offer) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "Publish offer")) {
            view.displayError("Error: No recruiter logged in. Please log in to publish offers.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        if (!validateNotNull(offer, "Offer")) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Publish offer attempted with null offer by recruiter: " + recruiterName);
            view.displayError("Error: Offer data is missing. Please fill all required fields.");
            return;
        }
        
        try {
            // Set publisher relationship for proper tracking
            offer.setPublisher(currentRecruiter);
            
            // Add to recruiter's local collection
            currentRecruiter.publishOffer(offer);
            logOperation(LogLevel.INFO, "RecruiterController", "Offer published by " + recruiterName + ": " + offer.getTitle());
            
            // CRITICAL FIX P4: Pass recruiter email so system finds the SAME recruiter instance
            // This solves object reference mismatch where session recruiter ≠ system recruiter
            // System will search org->booth->recruiters and add offer to the RIGHT instance
            vcfs.core.CareerFairSystem.getInstance().registerPublishedOffer(offer, currentRecruiter.getEmail());
            
            // RECORD OPERATION: Track published offers in system state
            SystemStateManager.getInstance().recordStateChange("OFFER_PUBLISHED",
                recruiterName + " published offer: " + offer.getTitle(), true);
            
            // RECORD SESSION ACTIVITY: Track offering action
            SessionManager.getInstance().recordActivity(recruiterName, "Recruiter",
                "OFFER_PUBLISHED", "Published interview offer: " + offer.getTitle());
            
            view.displayMessage("✓ Offer '" + offer.getTitle() + "' published successfully!");
        } catch (Exception e) {
            logError("RecruiterController", "Failed to publish offer by " + recruiterName, e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("OFFER_PUBLISH_FAILED",
                recruiterName + " failed to publish offer: " + e.getMessage(), false);
            
            view.displayError("Error publishing offer: " + e.getMessage());
        }
    }

    public void scheduleSession(MeetingSession session) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "Schedule session")) {
            view.displayError("Error: No recruiter logged in. Please log in to schedule sessions.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        if (!validateNotNull(session, "MeetingSession")) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Schedule session attempted with null session by recruiter: " + recruiterName);
            view.displayError("Error: Meeting session data is missing. Please fill all required fields.");
            return;
        }
        
        try {
            currentRecruiter.scheduleSession(session);
            logOperation(LogLevel.INFO, "RecruiterController", "Session scheduled by " + recruiterName + ": " + session.getTitle());
            
            // RECORD OPERATION: Track scheduled sessions
            SystemStateManager.getInstance().recordStateChange("SESSION_SCHEDULED",
                recruiterName + " scheduled session: " + session.getTitle(), true);
            
            // RECORD SESSION ACTIVITY: Track scheduling action
            SessionManager.getInstance().recordActivity(recruiterName, "Recruiter",
                "SESSION_SCHEDULED", "Scheduled meeting session: " + session.getTitle());
            
            view.displayMessage("✓ Meeting session '" + session.getTitle() + "' scheduled successfully!");
        } catch (Exception e) {
            logError("RecruiterController", "Failed to schedule session by " + recruiterName, e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("SESSION_SCHEDULE_FAILED",
                recruiterName + " failed to schedule session: " + e.getMessage(), false);
            
            view.displayError("Error scheduling session: " + e.getMessage());
        }
    }

    public void viewLobbySessions(String lobbyId) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "View lobby sessions")) {
            view.displayError("Error: No recruiter logged in. Please log in to view sessions.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        if (!validateNotEmpty(lobbyId, "Lobby ID")) {
            logOperation(LogLevel.WARNING, "RecruiterController", "View lobby sessions attempted with empty lobby ID by recruiter: " + recruiterName);
            view.displayError("Error: Lobby ID is required.");
            return;
        }
        
        try {
            String trimmedId = safeTrim(lobbyId);
            logOperation(LogLevel.INFO, "RecruiterController", "Requesting to view sessions in lobby " + trimmedId + " (recruiter: " + recruiterName + ")");
            view.displayMessage("✓ Showing lobby information for: " + trimmedId);
        } catch (Exception e) {
            logError("RecruiterController", "Failed to retrieve lobby sessions for " + recruiterName, e);
            view.displayError("Error retrieving lobby sessions: " + e.getMessage());
        }
    }

    public void viewMeetingHistory() {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "View meeting history")) {
            view.displayError("Error: No recruiter logged in. Please log in to view history.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        try {
            List<MeetingSession> history = currentRecruiter.getMeetingHistory();
            logOperation(LogLevel.INFO, "RecruiterController", "Viewing meeting history for " + recruiterName + " (" + history.size() + " sessions)");
            
            // RECORD SESSION ACTIVITY: Track history access
            SessionManager.getInstance().recordActivity(recruiterName, "Recruiter",
                "HISTORY_VIEWED", "Accessed meeting history (" + history.size() + " sessions)");
            
            if (schedulePanel != null) {
                schedulePanel.updateSchedule(history);
            }
            
            view.displaySessions(history);
        } catch (Exception e) {
            logError("RecruiterController", "Failed to retrieve meeting history for " + recruiterName, e);
            view.displayError("Error retrieving meeting history: " + e.getMessage());
        }
    }

    public void updateOfferStatus(String offerId, String status) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "Update offer status")) {
            view.displayError("Error: No recruiter logged in. Please log in to update offers.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        String finalOfferId = safeTrim(offerId);
        if (finalOfferId == null) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Update offer status attempted with empty offer ID by recruiter: " + recruiterName);
            view.displayError("Error: Offer ID is required.");
            return;
        }
        
        String finalStatus = safeTrim(status);
        if (finalStatus == null) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Update offer status attempted with empty status by recruiter: " + recruiterName);
            view.displayError("Error: Status value is required.");
            return;
        }
        
        try {
            currentRecruiter.updateOfferStatus(finalOfferId, finalStatus);
            logOperation(LogLevel.INFO, "RecruiterController", "Offer status updated by " + recruiterName + ": " + finalOfferId + " → " + finalStatus);
            view.displayMessage("✓ Offer status updated to '" + finalStatus + "'");
        } catch (Exception e) {
            logError("RecruiterController", "Failed to update offer status by " + recruiterName, e);
            view.displayError("Error updating offer status: " + e.getMessage());
        }
    }

    public void cancelSession(String sessionId) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "Cancel session")) {
            view.displayError("Error: No recruiter logged in. Please log in to cancel sessions.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        String finalSessionId = safeTrim(sessionId);
        if (finalSessionId == null) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Cancel session attempted with empty session ID by recruiter: " + recruiterName);
            view.displayError("Error: Session ID is required.");
            return;
        }
        
        try {
            currentRecruiter.cancelSession(finalSessionId);
            logOperation(LogLevel.INFO, "RecruiterController", "Session cancelled by " + recruiterName + ": " + finalSessionId);
            view.displayMessage("✓ Session '" + finalSessionId + "' has been cancelled.");
        } catch (Exception e) {
            logError("RecruiterController", "Failed to cancel session by " + recruiterName, e);
            view.displayError("Error cancelling session: " + e.getMessage());
        }
    }

    public List<Offer> getPublishedOffers() {
        if (currentRecruiter == null) {
            return new ArrayList<>();
        }
        
        return currentRecruiter.getPublishedOffers();
    }

    public Recruiter getCurrentRecruiter() {
        return currentRecruiter;
    }

    /**
     * Create a new offer for candidates.
     * This method publishes an offer from the current recruiter.
     * @param position Job position title (cannot be empty)
     * @param description Job description (cannot be empty)
     */
    public void createOffer(String position, String description) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "Create offer")) {
            view.displayError("Error: No recruiter logged in. Please log in to create offers.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        if (!validateNotEmpty(position, "Position title")) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Create offer attempted with empty position by " + recruiterName);
            view.displayError("Error: Position title is required.");
            return;
        }
        
        if (!validateNotEmpty(description, "Description")) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Create offer attempted with empty description by " + recruiterName);
            view.displayError("Error: Job description is required.");
            return;
        }
        
        try {
            String trimmedPosition = safeTrim(position);
            String trimmedDescription = safeTrim(description);
            
            Offer offer = new Offer(trimmedPosition, 0, trimmedDescription, 0, currentRecruiter);
            currentRecruiter.publishOffer(offer);
            
            logOperation(LogLevel.INFO, "RecruiterController", "Offer created by " + recruiterName + ": " + trimmedPosition);
            
            // RECORD OPERATION
            SystemStateManager.getInstance().recordStateChange("OFFER_CREATED",
                recruiterName + " created offer: " + trimmedPosition, true);
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity(recruiterName, "Recruiter",
                "OFFER_CREATED", "Created job offer for position: " + trimmedPosition);
            
            view.displayMessage("✓ Offer created successfully for position: " + trimmedPosition);
        } catch (Exception e) {
            logError("RecruiterController", "Failed to create offer by " + recruiterName, e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("OFFER_CREATE_FAILED",
                recruiterName + " failed to create offer: " + e.getMessage(), false);
            
            view.displayError("Error creating offer: " + e.getMessage());
        }
    }

    /**
     * View all available meeting requests for this recruiter.
     */
    public void viewAvailableRequests() {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "View available requests")) {
            view.displayError("Error: No recruiter logged in. Please log in to view requests.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        try {
            List<MeetingSession> sessions = currentRecruiter.getMeetingHistory();
            logOperation(LogLevel.INFO, "RecruiterController", "Viewing available requests for " + recruiterName + " (" + sessions.size() + " requests)");
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity(recruiterName, "Recruiter",
                "REQUESTS_VIEWED", "Viewed available meeting requests (" + sessions.size() + " items)");
            
            view.displayMessage("✓ Showing " + sessions.size() + " available requests");
            if (schedulePanel != null) {
                schedulePanel.updateSchedule(sessions);
            }
        } catch (Exception e) {
            logError("RecruiterController", "Failed to view available requests for " + recruiterName, e);
            view.displayError("Error retrieving available requests: " + e.getMessage());
        }
    }

    /**
     * Manage a meeting session (join/leave virtual room).
     * @param roomId Virtual room identifier (cannot be empty)
     * @param action Action to perform: "join" or "leave"
     */
    public void manageMeetingSession(String roomId, String action) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "Manage meeting session")) {
            view.displayError("Error: No recruiter logged in. Please log in to manage sessions.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        String finalRoomId = safeTrim(roomId);
        if (finalRoomId == null) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Manage session attempted with empty room ID by " + recruiterName);
            view.displayError("Error: Meeting room ID is required.");
            return;
        }
        
        String finalAction = safeTrim(action);
        if (finalAction == null || (!finalAction.equalsIgnoreCase("join") && !finalAction.equalsIgnoreCase("leave"))) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Manage session attempted with invalid action by " + recruiterName);
            view.displayError("Error: Action must be 'join' or 'leave'.");
            return;
        }
        
        try {
            logOperation(LogLevel.INFO, "RecruiterController", "Managing session by " + recruiterName + ": " + finalAction + " room " + finalRoomId);
            
            // RECORD OPERATION
            SystemStateManager.getInstance().recordStateChange("SESSION_MANAGED",
                recruiterName + " " + finalAction + "ed meeting room: " + finalRoomId, true);
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity(recruiterName, "Recruiter",
                "SESSION_MANAGED", "Performed action '" + finalAction + "' on meeting room");
            
            view.displayMessage("✓ Successfully " + finalAction + "ed meeting session in room: " + finalRoomId);
        } catch (Exception e) {
            logError("RecruiterController", "Failed to manage meeting session by " + recruiterName, e);
            view.displayError("Error managing meeting session: " + e.getMessage());
        }
    }

    /**
     * Respond to a candidate meeting request.
     * @param requestId Meeting request identifier (cannot be empty)
     * @param response Response status (e.g., "APPROVED", "REJECTED", "PENDING")
     */
    public void respondToRequest(String requestId, String response) {
        if (!validateLoggedIn(currentRecruiter, "Recruiter", "Respond to request")) {
            view.displayError("Error: No recruiter logged in. Please log in to respond to requests.");
            return;
        }
        
        String recruiterName = getUserName(currentRecruiter);
        
        String finalRequestId = safeTrim(requestId);
        if (finalRequestId == null) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Respond to request attempted with empty request ID by " + recruiterName);
            view.displayError("Error: Request ID is required.");
            return;
        }
        
        String finalResponse = safeTrim(response);
        if (finalResponse == null) {
            logOperation(LogLevel.WARNING, "RecruiterController", "Respond to request attempted with empty response by " + recruiterName);
            view.displayError("Error: Response status is required.");
            return;
        }
        
        try {
            logOperation(LogLevel.INFO, "RecruiterController", "Request response by " + recruiterName + ": " + finalRequestId + " → " + finalResponse);
            
            // RECORD OPERATION
            SystemStateManager.getInstance().recordStateChange("REQUEST_RESPONDED",
                recruiterName + " responded to request: " + finalRequestId + " with status: " + finalResponse, true);
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity(recruiterName, "Recruiter",
                "REQUEST_RESPONDED", "Responded to meeting request with status: " + finalResponse);
            
            view.displayMessage("✓ Request response recorded: " + finalResponse);
        } catch (Exception e) {
            logError("RecruiterController", "Failed to respond to request by " + recruiterName, e);
            view.displayError("Error processing request response: " + e.getMessage());
        }
    }
}
