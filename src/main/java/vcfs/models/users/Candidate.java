package vcfs.models.users;

import java.util.*;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Candidate who registers, creates Requests, books Reservations and joins sessions.
 */
public class Candidate extends User {

	private CandidateProfile profile;
	private Collection<Request> requests;
	private Collection<Reservation> reservations;

	/**
	 * Create a Candidate with required user details.
	 * @param id Unique candidate identifier
	 * @param displayName Candidate's display name
	 * @param email Candidate's email address
	 */
	public Candidate(String id, String displayName, String email) {
		super(id, displayName, email);
		this.profile = new CandidateProfile();
		this.requests = new ArrayList<>();
		this.reservations = new ArrayList<>();
	}

	/**
	 * Get the candidate's profile.
	 */
	public CandidateProfile getProfile() {
		return profile;
	}

	/**
	 * Set the candidate's profile.
	 * @param profile The profile object (cannot be null)
	 * @throws IllegalArgumentException if profile is null
	 */
	public void setProfile(CandidateProfile profile) {
		if (profile == null) {
			throw new IllegalArgumentException("Profile cannot be null");
		}
		this.profile = profile;
	}

	/**
	 * Get all requests created by this candidate.
	 */
	public Collection<Request> getRequests() {
		return Collections.unmodifiableCollection(requests);
	}

	/**
	 * Get all reservations for this candidate.
	 */
	public Collection<Reservation> getReservations() {
		return Collections.unmodifiableCollection(reservations);
	}

	/**
	 * Create booking request/preferences.
	 * @param desiredTags Tags describing desired offers
	 * @param preferredOrgs Preferred organizations
	 * @param maxAppointments Maximum number of appointments
	 * @return The created Request
	 * @throws IllegalArgumentException if any parameter is invalid
	 */
	public Request createRequest(String desiredTags, String preferredOrgs, int maxAppointments) {
		if (desiredTags == null || desiredTags.trim().isEmpty()) {
			throw new IllegalArgumentException("Desired tags cannot be empty");
		}
		if (preferredOrgs == null || preferredOrgs.trim().isEmpty()) {
			throw new IllegalArgumentException("Preferred organizations cannot be empty");
		}
		if (maxAppointments <= 0) {
			throw new IllegalArgumentException("Max appointments must be positive");
		}

		Request request = new Request();
		request.setRequester(this);
		request.setDesiredTags(desiredTags);
		request.setPreferredOrgs(preferredOrgs);
		request.setMaxAppointments(maxAppointments);
		this.requests.add(request);
		
		Logger.log(LogLevel.INFO, "Request created by " + this.getDisplayName());
		return request;
	}

	/**
	 * Cancel a reservation by offer title.
	 * @param reservationId The offer title to cancel
	 */
	public void cancelMyReservation(String reservationId) {
		if (reservationId == null || reservationId.trim().isEmpty()) {
			throw new IllegalArgumentException("Reservation ID cannot be empty");
		}

		reservations.removeIf(r -> {
			if (r != null && r.getOffer() != null && reservationId.equals(r.getOffer().getTitle())) {
				r.cancel("Cancelled by candidate");
				return true;
			}
			return false;
		});
	}

	/**
	 * Get a formatted string of the candidate's schedule.
	 */
	public String viewMySchedule() {
		if (reservations.isEmpty()) {
			return "[Candidate] " + this.getDisplayName() + " has no reservations.";
		}
		StringBuilder sb = new StringBuilder("[Candidate] " + this.getDisplayName() + "'s Schedule:\n");
		for (Reservation r : reservations) {
			if (r.getOffer() != null) {
				sb.append("  - ").append(r.getOffer().getTitle())
					.append(" @ ").append(r.getScheduledStart() != null ? r.getScheduledStart() : "unknown")
					.append(" (").append(r.getState()).append(")\n");
			}
		}
		return sb.toString();
	}

}

