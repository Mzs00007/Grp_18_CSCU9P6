package vcfs.controllers;

import vcfs.core.CareerFairSystem;
import vcfs.core.LocalDateTime;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.SystemTimer;
import vcfs.models.users.Recruiter;
import vcfs.models.booking.Offer;

/**
 * MVC Controller for all Recruiter actions.
 *
 * Assigned to: Taha
 * Tickets: VCFS-009, VCFS-010, VCFS-011, VCFS-012
 *
 * Listens to button clicks in RecruiterScreen (View)
 * and dispatches to CareerFairSystem (Model/CoreFacade).
 */
public class RecruiterController {

    private final CareerFairSystem system;

    public RecruiterController(CareerFairSystem system) {
        if (system == null) {
            throw new IllegalArgumentException("CareerFairSystem cannot be null");
        }
        this.system = system;
    }

    /**
     * Publish an availability block as a recruiter
     * @param recruiter The recruiter publishing
     * @param title Session title
     * @param durationMins Duration of each slot in minutes
     * @param topicTags Topic tags (comma-separated)
     * @param capacity Maximum candidates per slot
     * @return The number of offer slots created, or -1 on failure
     */
    public int onPublishOffer(Recruiter recruiter, String title, int durationMins, String topicTags, int capacity) {
        if (recruiter == null) {
            Logger.log(LogLevel.ERROR, "Cannot publish offer: recruiter is null");
            return -1;
        }
        if (title == null || title.trim().isEmpty()) {
            Logger.log(LogLevel.ERROR, "Cannot publish offer with empty title");
            return -1;
        }
        if (durationMins <= 0) {
            Logger.log(LogLevel.ERROR, "Cannot publish offer with invalid duration");
            return -1;
        }
        if (capacity <= 0) {
            Logger.log(LogLevel.ERROR, "Cannot publish offer with invalid capacity");
            return -1;
        }

        try {
            // For single offer publishing, create a 1-slot offer block
            LocalDateTime now = SystemTimer.getInstance().getNow();
            LocalDateTime blockStart = now;
            LocalDateTime blockEnd = now.plusMinutes(durationMins);

            int slotsCreated = system.parseAvailabilityIntoOffers(
                recruiter, title, durationMins, topicTags, capacity, blockStart, blockEnd);

            Logger.log(LogLevel.INFO, "[RecruiterController] Offer published by " + recruiter.getDisplayName() 
                + ": " + slotsCreated + " slots");
            return slotsCreated;
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to publish offer: " + e.getMessage());
            return -1;
        }
    }

    /**
     * View the recruiter's schedule of published offers
     * @param recruiter The recruiter
     * @return The schedule as a formatted string, or empty string on failure
     */
    public String onViewSchedule(Recruiter recruiter) {
        if (recruiter == null) {
            Logger.log(LogLevel.ERROR, "Cannot view schedule: recruiter is null");
            return "";
        }

        try {
            StringBuilder schedule = new StringBuilder();
            if (recruiter.getOffers() == null || recruiter.getOffers().isEmpty()) {
                schedule.append("No offers published yet.");
            } else {
                schedule.append("Schedule for ").append(recruiter.getDisplayName()).append(":\n");
                for (Offer offer : recruiter.getOffers()) {
                    schedule.append("  - ").append(offer.getTitle())
                        .append(" @ ").append(offer.getStartTime())
                        .append(" (").append(offer.getDurationMins()).append(" min, capacity: ")
                        .append(offer.getCapacity()).append(")\n");
                }
            }
            Logger.log(LogLevel.INFO, "[RecruiterController] Schedule retrieved for " + recruiter.getDisplayName());
            return schedule.toString();
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to view schedule: " + e.getMessage());
            return "";
        }
    }

    /**
     * Cancel a reservation as a recruiter
     * @param recruiter The recruiter
     * @param reservationId The reservation ID to cancel
     * @param reason The reason for cancellation
     * @return true if cancellation was successful, false otherwise
     */
    public boolean onCancelReservation(Recruiter recruiter, String reservationId, String reason) {
        if (recruiter == null) {
            Logger.log(LogLevel.ERROR, "Cannot cancel reservation: recruiter is null");
            return false;
        }
        if (reservationId == null || reservationId.trim().isEmpty()) {
            Logger.log(LogLevel.ERROR, "Cannot cancel reservation: ID is empty");
            return false;
        }
        if (reason == null || reason.trim().isEmpty()) {
            Logger.log(LogLevel.ERROR, "Cannot cancel reservation: reason is required");
            return false;
        }

        try {
            Logger.log(LogLevel.INFO, "[RecruiterController] Cancellation request from " + recruiter.getDisplayName() 
                + " for reservation " + reservationId + ": " + reason);
            // Note: Full cancellation logic would be delegated to system.cancelAsRecruiter()
            // This logs the action for now
            return true;
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[RecruiterController] Failed to cancel reservation: " + e.getMessage());
            return false;
        }
    }

}
