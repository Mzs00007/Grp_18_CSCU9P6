package vcfs.controllers;

import vcfs.core.CareerFairSystem;
import vcfs.models.booking.Offer;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;
import vcfs.models.users.Candidate;
import java.util.ArrayList;
import java.util.List;

/**
 * MVC Controller for all Candidate actions.
 *
 * Assigned to: MJAMishkat
 * Tickets: VCFS-013, VCFS-014, VCFS-015, VCFS-016
 *
 * Receives events from CandidateScreen (View) and
 * delegates to CareerFairSystem (Model/CoreFacade).
 * Contains NO business logic — only routing.
 *
 * SESSION SCOPING:
 * candidateEmail is passed in at construction time (after registration).
 * This binds each controller instance to exactly one candidate, ensuring
 * that six simultaneous CandidateScreen windows never cross-contaminate
 * each other's data — even without a persistent database or session tokens.
 */
public class CandidateController {

    private final CareerFairSystem system;

    /**
     * The email that uniquely identifies the logged-in candidate for this
     * controller instance. Set once at construction; never changes.
     */
    private final String candidateEmail;

    /**
     * Primary constructor — used after the candidate has registered.
     *
     * @param system         The shared application facade (Model).
     * @param candidateEmail The email that acts as a session token for this instance.
     */
    public CandidateController(CareerFairSystem system, String candidateEmail) {
        if (candidateEmail == null || !candidateEmail.contains("@")) {
            throw new IllegalArgumentException("A valid candidate email is required to create a controller.");
        }
        this.system         = system;
        this.candidateEmail = candidateEmail;
    }

    /** Returns the email this controller instance is scoped to. */
    public String getCandidateEmail() {
        return candidateEmail;
    }

    // =========================================================
    // VCFS-013: REGISTRATION
    // =========================================================

    /**
     * Register a new candidate via the system facade.
     * Called from CandidateScreen registration form.
     *
     * @param displayName Candidate's full name
     * @param email       Unique email (acts as ID)
     * @param cvSummary   Optional CV text
     * @param tags        Comma-separated interest tags
     * @return            The created Candidate model object
     */
    public Candidate onRegister(String displayName, String email, String cvSummary, String tags) {
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Display name is required.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        return system.registerCandidate(displayName, email, cvSummary, tags);
    }

    // =========================================================
    // VCFS-014: MANUAL BOOKING
    // =========================================================

    /**
     * Create a confirmed reservation for the candidate at the specified offer/slot.
     * Routes through system.manualBook when fully wired.
     *
     * @param candidate The logged-in candidate
     * @param offer     The chosen Offer object
     * @return          The confirmed Reservation
     */
    public Reservation onManualBookOffer(Candidate candidate, Offer offer) {
        if (candidate == null) throw new IllegalArgumentException("Candidate is required.");
        if (offer == null) throw new IllegalArgumentException("Offer is required.");
        return system.manualBook(candidate, offer, offer.startTime);
    }

    /**
     * Legacy overload — resolves offer by title string (used by CandidateScreen stub).
     * Once fully wired, the view passes Offer objects directly.
     */
    public void onManualBook(Candidate candidate, String offerId) {
        throw new UnsupportedOperationException(
            "CandidateController.onManualBook(String) is a stub. "
            + "Use onManualBookOffer(Candidate, Offer) once wired.");
    }

    // =========================================================
    // VCFS-015: AUTO-BOOKING
    // =========================================================

    /**
     * Send the candidate's Request to the MatchEngine and return the best Reservation.
     * Delegates to system.autoBook (Zaid's algorithm — VCFS-004).
     *
     * @param candidate The logged-in candidate
     * @param request   Contains desiredTags, preferredOrgs, maxAppointments
     * @return          Confirmed Reservation list from MatchEngine
     */
    public List<Reservation> onAutoBook(Candidate candidate, Request request) {
        if (candidate == null) throw new IllegalArgumentException("Candidate is required.");
        if (request == null) throw new IllegalArgumentException("Request is required.");

        List<Reservation> results = new ArrayList<>();
        int limit = Math.max(1, request.maxAppointments);

        for (int i = 0; i < limit; i++) {
            Reservation reservation = system.autoBook(candidate, request);
            if (reservation == null) break;
            results.add(reservation);
        }
        return results;
    }

    // =========================================================
    // VCFS-016: JOIN SESSION (LOBBY GATEKEEPER)
    // =========================================================

    /**
     * Attempt to join a session.
     * The VIEW (CandidateScreen) already performs the time-gate check
     * before calling this method. By the time this is invoked, the
     * SystemTimer has confirmed the session start time has been reached.
     *
     * @param candidate     The logged-in candidate
     * @param reservationId The reservation identifier (offer title in current skeleton)
     */
    public void onJoinSession(Candidate candidate, String reservationId) {
        if (candidate == null) throw new IllegalArgumentException("Candidate is required.");
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("ReservationId is required.");
        }
        system.joinSession(candidate, reservationId);
    }

    // =========================================================
    // VCFS-014: CANCEL RESERVATION
    // =========================================================

    /**
     * Cancel a candidate's reservation by ID.
     *
     * @param candidate     The logged-in candidate
     * @param reservationId The reservation to cancel
     */
    public void onCancelReservation(Candidate candidate, String reservationId) {
        if (candidate == null) throw new IllegalArgumentException("Candidate is required.");
        // system.cancelAsCandidate(candidate, reservationId); // YAMI wires this
        candidate.cancelMyReservation(reservationId);
        System.out.println("[CandidateController] Cancelled: " + reservationId);
    }
}
