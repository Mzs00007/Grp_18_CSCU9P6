package vcfs.controllers;

import vcfs.core.CareerFairSystem;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.users.Candidate;
import vcfs.models.users.CandidateProfile;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;

/**
 * MVC Controller for all Candidate actions.
 *
 * Assigned to: MJAMishkat
 * Tickets: VCFS-013, VCFS-014, VCFS-015, VCFS-016
 *
 * Receives events from CandidateScreen (View) and
 * delegates to CareerFairSystem (Model/CoreFacade).
 * Contains NO business logic — only routing and validation.
 */
public class CandidateController {

    private final CareerFairSystem system;

    public CandidateController(CareerFairSystem system) {
        if (system == null) {
            throw new IllegalArgumentException("CareerFairSystem cannot be null");
        }
        this.system = system;
    }

    /**
     * Register a new candidate with their basic profile information
     * @param displayName Candidate's display name
     * @param email Candidate's unique email
     * @param cvSummary CV summary text
     * @param tags Interest tags (comma-separated)
     * @return The created Candidate, or null on failure
     */
    public Candidate onRegister(String displayName, String email, String cvSummary, String tags) {
        if (displayName == null || displayName.trim().isEmpty()) {
            Logger.log(LogLevel.ERROR, "Cannot register candidate with empty display name");
            return null;
        }
        if (email == null || email.trim().isEmpty()) {
            Logger.log(LogLevel.ERROR, "Cannot register candidate with empty email");
            return null;
        }

        try {
            Candidate candidate = system.registerCandidate(displayName, email, cvSummary, tags);
            if (candidate != null) {
                CandidateProfile profile = new CandidateProfile();
                profile.setCvSummary(cvSummary != null ? cvSummary : "");
                profile.setInterestTags(tags != null ? tags : "");
                Logger.log(LogLevel.INFO, "[CandidateController] Candidate registered: " + displayName);
            }
            return candidate;
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to register candidate: " + e.getMessage());
            return null;
        }
    }

    /**
     * Manually book a candidate to a specific offer at a specific time
     * @param candidate The candidate to book
     * @param offerId The offer ID/index
     * @return The created Reservation, or null on failure
     */
    public Reservation onManualBook(Candidate candidate, String offerId) {
        if (candidate == null) {
            Logger.log(LogLevel.ERROR, "Cannot book: candidate is null");
            return null;
        }
        if (offerId == null || offerId.trim().isEmpty()) {
            Logger.log(LogLevel.ERROR, "Cannot book: offer ID is empty");
            return null;
        }

        try {
            Logger.log(LogLevel.INFO, "[CandidateController] Manual booking for " + candidate.getDisplayName() 
                + " to offer " + offerId);
            // Note: Full implementation would require looking up offer by ID
            // For now, this logs the action
            return null;
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to perform manual booking: " + e.getMessage());
            return null;
        }
    }

    /**
     * Trigger auto-booking based on candidate preferences
     * @param candidate The candidate requesting auto-booking
     * @param request The candidate's booking request with preferences
     * @return The created Reservation if successful, null otherwise
     */
    public Reservation onAutoBook(Candidate candidate, Request request) {
        if (candidate == null) {
            Logger.log(LogLevel.ERROR, "Cannot auto-book: candidate is null");
            return null;
        }
        if (request == null) {
            Logger.log(LogLevel.ERROR, "Cannot auto-book: request is null");
            return null;
        }

        try {
            Reservation reservation = system.autoBook(candidate, request);
            if (reservation != null) {
                Logger.log(LogLevel.INFO, "[CandidateController] Auto-booking successful for " 
                    + candidate.getDisplayName());
            } else {
                Logger.log(LogLevel.WARNING, "[CandidateController] Auto-booking failed - no matching offers");
            }
            return reservation;
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to perform auto-booking: " + e.getMessage());
            return null;
        }
    }

    /**
     * Candidate joins a booked session via the session lobby
     * @param candidate The candidate joining
     * @param reservationId The reservation ID
     */
    public void onJoinSession(Candidate candidate, String reservationId) {
        if (candidate == null) {
            Logger.log(LogLevel.ERROR, "Cannot join session: candidate is null");
            return;
        }
        if (reservationId == null || reservationId.trim().isEmpty()) {
            Logger.log(LogLevel.ERROR, "Cannot join session: reservation ID is empty");
            return;
        }

        try {
            Logger.log(LogLevel.INFO, "[CandidateController] Candidate " + candidate.getDisplayName() 
                + " joining session: " + reservationId);
            // Note: Full implementation would involve Lobby Gatekeeper logic
            // For now, this logs the action
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateController] Failed to join session: " + e.getMessage());
        }
    }

}
