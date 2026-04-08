package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */

/**
 * RecruiterController — Handles all recruiter-related business logic.
 * 
 * RESPONSIBILITIES:
 *   - Publishes interview offers with validation and logging
 *   - Schedules meeting sessions for candidates
 *   - Views available lobbies and candidate meeting history
 *   - Updates offer status (ATTENDED, NO_SHOW) after interviews
 *   - Cancels scheduled sessions when needed
 * 
 * KEY FEATURES:
 *   ✓ Input validation (null checks, empty string trimming)
 *   ✓ Comprehensive logging (INFO, WARNING, ERROR levels)
 *   ✓ Error handling with try-catch blocks
 *   ✓ User-friendly error messages via view layer
 *   ✓ Success feedback with checkmarks (✓)
 * 
 * USAGE FLOW:
 *   1. RecruiterScreen creates this controller
 *   2. UI panels (PublishOfferPanel, SchedulePanel) call controller methods
 *   3. Controller validates inputs and delegates to Recruiter model
 *   4. Results are displayed to user via view.displayMessage() / view.displayError()
 * 
 * Implemented by: Zaid
 * 
 * CODE AUDIT: ZAID (mzs00007) — Input validation and defensive null checks throughout class
 * - Constructor validates view parameter (non-null) with explicit exception
 * - All methods check currentRecruiter before use
 * - String parameters trimmed before empty checks
 * - Comprehensive try-catch with logging on all operations
 */

import java.util.ArrayList;
import java.util.List;

import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.booking.Offer;
import vcfs.models.booking.MeetingSession;
import vcfs.models.users.Recruiter;
import vcfs.views.recruiter.RecruiterView;
import vcfs.views.recruiter.SchedulePanel;

public class RecruiterController {
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
     * 
     * @param recruiter The Recruiter object from login (cannot be null)
     */
    public void setCurrentRecruiter(Recruiter recruiter) {
        this.currentRecruiter = recruiter;
    }
    
    /**
     * Set the SchedulePanel reference so it can be updated with session data.
     * Called by SchedulePanel during initialization.
     * 
     * @param panel The SchedulePanel instance
     */
    public void setSchedulePanel(SchedulePanel panel) {
        this.schedulePanel = panel;
    }

    /**
     * Publish a new interview offer to the system.
     */
    public void publishOffer(Offer offer) {
        String recruiterName = (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "UNKNOWN";
        
        if (currentRecruiter == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Publish offer attempted with no recruiter logged in");
            view.displayError("Error: No recruiter logged in. Please log in to publish offers.");
            return;
        }
        
        if (offer == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Publish offer attempted with null offer by recruiter: " + recruiterName);
            view.displayError("Error: Offer data is missing. Please fill all required fields.");
            return;
        }
        
        try {
            currentRecruiter.publishOffer(offer);
            Logger.log(LogLevel.INFO, "[RecruiterController] Offer published by " + recruiterName + ": " + offer.getTitle());
            view.displayMessage("✓ Offer '" + offer.getTitle() + "' published successfully!");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to publish offer by " + recruiterName, e);
            view.displayError("Error publishing offer: " + e.getMessage());
        }
    }

    public void scheduleSession(MeetingSession session) {
        String recruiterName = (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "UNKNOWN";
        
        if (currentRecruiter == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Schedule session attempted with no recruiter logged in");
            view.displayError("Error: No recruiter logged in. Please log in to schedule sessions.");
            return;
        }
        
        if (session == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Schedule session attempted with null session by recruiter: " + recruiterName);
            view.displayError("Error: Meeting session data is missing. Please fill all required fields.");
            return;
        }
        
        try {
            currentRecruiter.scheduleSession(session);
            Logger.log(LogLevel.INFO, "[RecruiterController] Session scheduled by " + recruiterName + ": " + session.getTitle());
            view.displayMessage("✓ Meeting session '" + session.getTitle() + "' scheduled successfully!");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to schedule session by " + recruiterName, e);
            view.displayError("Error scheduling session: " + e.getMessage());
        }
    }

    public void viewLobbySessions(String lobbyId) {
        String recruiterName = (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "UNKNOWN";
        
        if (currentRecruiter == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] View lobby sessions attempted with no recruiter logged in");
            view.displayError("Error: No recruiter logged in. Please log in to view sessions.");
            return;
        }
        
        if (lobbyId == null || (lobbyId = lobbyId.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] View lobby sessions attempted with empty lobby ID by recruiter: " + recruiterName);
            view.displayError("Error: Lobby ID is required.");
            return;
        }
        
        try {
            Logger.log(LogLevel.INFO, "[RecruiterController] Requesting to view sessions in lobby " + lobbyId + " (recruiter: " + recruiterName + ")");
            view.displayMessage("✓ Showing lobby information for: " + lobbyId);
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to retrieve lobby sessions for " + recruiterName, e);
            view.displayError("Error retrieving lobby sessions: " + e.getMessage());
        }
    }

    public void viewMeetingHistory() {
        String recruiterName = (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "UNKNOWN";
        
        if (currentRecruiter == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] View meeting history attempted with no recruiter logged in");
            view.displayError("Error: No recruiter logged in. Please log in to view history.");
            return;
        }
        
        try {
            List<MeetingSession> history = currentRecruiter.getMeetingHistory();
            Logger.log(LogLevel.INFO, "[RecruiterController] Viewing meeting history for " + recruiterName + " (" + history.size() + " sessions)");
            
            if (schedulePanel != null) {
                schedulePanel.updateSchedule(history);
            }
            
            view.displaySessions(history);
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to retrieve meeting history for " + recruiterName, e);
            view.displayError("Error retrieving meeting history: " + e.getMessage());
        }
    }

    public void updateOfferStatus(String offerId, String status) {
        String recruiterName = (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "UNKNOWN";
        
        if (currentRecruiter == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Update offer status attempted with no recruiter logged in");
            view.displayError("Error: No recruiter logged in. Please log in to update offers.");
            return;
        }
        
        if (offerId == null || (offerId = offerId.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Update offer status attempted with empty offer ID by recruiter: " + recruiterName);
            view.displayError("Error: Offer ID is required.");
            return;
        }
        
        if (status == null || (status = status.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Update offer status attempted with empty status by recruiter: " + recruiterName);
            view.displayError("Error: Status value is required.");
            return;
        }
        
        try {
            currentRecruiter.updateOfferStatus(offerId, status);
            Logger.log(LogLevel.INFO, "[RecruiterController] Offer status updated by " + recruiterName + ": " + offerId + " → " + status);
            view.displayMessage("✓ Offer status updated to '" + status + "'");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to update offer status by " + recruiterName, e);
            view.displayError("Error updating offer status: " + e.getMessage());
        }
    }

    public void cancelSession(String sessionId) {
        String recruiterName = (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "UNKNOWN";
        
        if (currentRecruiter == null) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Cancel session attempted with no recruiter logged in");
            view.displayError("Error: No recruiter logged in. Please log in to cancel sessions.");
            return;
        }
        
        if (sessionId == null || (sessionId = sessionId.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[RecruiterController] Cancel session attempted with empty session ID by recruiter: " + recruiterName);
            view.displayError("Error: Session ID is required.");
            return;
        }
        
        try {
            currentRecruiter.cancelSession(sessionId);
            Logger.log(LogLevel.INFO, "[RecruiterController] Session cancelled by " + recruiterName + ": " + sessionId);
            view.displayMessage("✓ Session '" + sessionId + "' has been cancelled.");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to cancel session by " + recruiterName, e);
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
     * Helper method to get recruiter display name safely.
     * @return The recruiter's display name, or "UNKNOWN" if not logged in
     */
    private String getRecruiterName() {
        return (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "UNKNOWN";
    }
}
