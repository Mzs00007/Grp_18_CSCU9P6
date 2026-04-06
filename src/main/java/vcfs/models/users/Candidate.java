package vcfs.models.users;

import java.util.*;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;

/**
 * Candidate who registers, creates Requests, books Reservations and joins sessions.
 */
public class Candidate extends User {

	public CandidateProfile profile;
	public Collection<Request> requests;
	public Collection<Reservation> reservations;

	/**
	 * Create booking request/preferences.
	 * @param desiredTags
	 * @param preferredOrgs
	 * @param maxAppointments
	 */
	public Request createRequest(String desiredTags, String preferredOrgs, int maxAppointments) {
		if (this.requests == null) {
			this.requests = new ArrayList<>();
		}
		Request request = new Request();
		request.requester = this;
		request.desiredTags = desiredTags;
		request.preferredOrgs = preferredOrgs;
		request.maxAppointments = maxAppointments;
		this.requests.add(request);
		System.out.println("[Candidate] Request created by " + this.displayName);
		return request;
	}

	/**
	 * 
	 * @param reservationId
	 */
	public void cancelMyReservation(String reservationId) {
		if (reservations == null) return;
		reservations.removeIf(r -> {
			if (r != null && r.offer != null && reservationId.equals(r.offer.title)) {
				r.cancel("Cancelled by candidate");
				return true;
			}
			return false;
		});
	}

	public String viewMySchedule() {
		if (reservations == null || reservations.isEmpty()) {
			return "[Candidate] " + this.displayName + " has no reservations.";
		}
		StringBuilder sb = new StringBuilder("[Candidate] " + this.displayName + "'s Schedule:\n");
		for (Reservation r : reservations) {
			if (r.offer != null) {
				sb.append("  - ").append(r.offer.title)
					.append(" @ ").append(r.scheduledStart != null ? r.scheduledStart : "unknown")
					.append(" (").append(r.state).append(")\n");
			}
		}
		return sb.toString();
	}

}

