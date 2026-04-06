package vcfs.controllers;

import vcfs.core.CareerFairSystem;
import vcfs.models.users.Candidate;
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
 * Contains NO business logic — only routing.
 */
public class CandidateController {

    private final CareerFairSystem system;

    public CandidateController(CareerFairSystem system) {
        this.system = system;
    }

    // TODO (MJAMishkat - VCFS-013): Wire registration form submission
    public void onRegister(String displayName, String email, String cvSummary, String tags) {
        // system.registerCandidate(displayName, email, cvSummary, tags);
        throw new UnsupportedOperationException("CandidateController.onRegister not yet implemented");
    }

    // TODO (MJAMishkat - VCFS-014): Manual booking action
    public void onManualBook(Candidate candidate, String offerId) {
        // system.manualBook(candidate, offerId);
        throw new UnsupportedOperationException("CandidateController.onManualBook not yet implemented");
    }

    // TODO (MJAMishkat - VCFS-015): Auto-booking action — calls Zaid's MatchEngine
    public Reservation onAutoBook(Candidate candidate, Request request) {
        // return system.autoBook(candidate, request);
        throw new UnsupportedOperationException("CandidateController.onAutoBook not yet implemented");
    }

    // TODO (MJAMishkat - VCFS-016): Join session — routed via Lobby Gatekeeper
    public void onJoinSession(Candidate candidate, String reservationId) {
        // system.joinSession(candidate, reservationId);
        throw new UnsupportedOperationException("CandidateController.onJoinSession not yet implemented");
    }

}
