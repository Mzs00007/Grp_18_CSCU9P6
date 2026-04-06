package vcfs.controllers;

import vcfs.core.CareerFairSystem;
import vcfs.models.users.Recruiter;

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
        this.system = system;
    }

    // TODO (Taha - VCFS-009): Publish a new Offer Availability Block
    public void onPublishOffer(Recruiter recruiter, String title, int durationMins, String topicTags, int capacity) {
        // system.publishOffer(recruiter, title, durationMins, topicTags, capacity);
        throw new UnsupportedOperationException("RecruiterController.onPublishOffer not yet implemented");
    }

    // TODO (Taha - VCFS-010): Refresh schedule JList
    public void onViewSchedule(Recruiter recruiter) {
        // String schedule = recruiter.viewSchedule();
        throw new UnsupportedOperationException("RecruiterController.onViewSchedule not yet implemented");
    }

    // TODO (Taha - VCFS-012): Cancel a reservation as a recruiter
    public void onCancelReservation(Recruiter recruiter, String reservationId, String reason) {
        // system.cancelAsRecruiter(recruiter, reservationId, reason);
        throw new UnsupportedOperationException("RecruiterController.onCancelReservation not yet implemented");
    }

}
