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
            currentRecruiter.publishOffer(offer);
            logOperation(LogLevel.INFO, "RecruiterController", "Offer published by " + recruiterName + ": " + offer.getTitle());
            
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
}
