package vcfs.controllers;

import vcfs.core.CareerFairSystem;
import vcfs.models.booking.Offer;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;
import vcfs.models.users.Candidate;
import java.util.ArrayList;
import java.util.List;

/**
 * Assigned to: MJAMishkat
 * Tickets: VCFS-013, VCFS-014, VCFS-015, VCFS-016
 *
 * 
 */
public class CandidateController {

    private final CareerFairSystem system;

    private final String candidateEmail;

    public CandidateController(CareerFairSystem system, String candidateEmail) {
        if (candidateEmail == null || !candidateEmail.contains("@")) {
            throw new IllegalArgumentException("A valid candidate email is required to create a controller.");
        }
        this.system         = system;
        this.candidateEmail = candidateEmail;
    }
    
    public String getCandidateEmail() {
        return candidateEmail;
    }

    // =========================================================
    // VCFS-013: REGISTRATION
    // =========================================================

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

    public Reservation onManualBookOffer(Candidate candidate, Offer offer) {
        if (candidate == null) throw new IllegalArgumentException("Candidate is required.");
        if (offer == null) throw new IllegalArgumentException("Offer is required.");
        return system.manualBook(candidate, offer, offer.startTime);
    }

    public void onManualBook(Candidate candidate, String offerId) {
        throw new UnsupportedOperationException(
            "CandidateController.onManualBook(String) is a stub. "
            + "Use onManualBookOffer(Candidate, Offer) once wired.");
    }

    // =========================================================
    // VCFS-015: AUTO-BOOKING
    // =========================================================

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

    public void onCancelReservation(Candidate candidate, String reservationId) {
        if (candidate == null) throw new IllegalArgumentException("Candidate is required.");
        // system.cancelAsCandidate(candidate, reservationId); // YAMI wires this
        candidate.cancelMyReservation(reservationId);
        System.out.println("[CandidateController] Cancelled: " + reservationId);
    }
}
