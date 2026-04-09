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
	 * Constructor with auto-generated ID.
	 * Generates an ID based on displayName and email.
	 * 
	 * @param displayName Candidate's display name
	 * @param email Candidate's email address
	 */
	public Candidate(String displayName, String email) {
		this("cand_" + System.nanoTime(), displayName, email);
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
	 * Add a reservation to this candidate's list.
	 * @param reservation The reservation to add (cannot be null)
	 * @throws IllegalArgumentException if reservation is null
	 */
	public void addReservation(Reservation reservation) {
		if (reservation == null) {
			throw new IllegalArgumentException("Reservation cannot be null");
		}
		this.reservations.add(reservation);
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
	 * @param offerTitle The offer title to cancel (searches through reservations)
	 */
	public void cancelMyReservation(String offerTitle) {
		if (offerTitle == null || offerTitle.trim().isEmpty()) {
			throw new IllegalArgumentException("Offer title cannot be empty");
		}

		reservations.removeIf(r -> {
			if (r != null && r.getOffer() != null && offerTitle.equals(r.getOffer().getTitle())) {
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
			if (r != null && r.getOffer() != null) {
				sb.append("  - ").append(r.getOffer().getTitle())
					.append(" @ ").append(r.getScheduledStart() != null ? r.getScheduledStart() : "unknown")
					.append(" (").append(r.getState()).append(")\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Submit a meeting request (controller method).
	 * @param request The request to submit
	 */
	public void submitRequest(Request request) {
		if (request != null && !requests.contains(request)) {
			this.requests.add(request);
		}
	}

	/**
	 * Get the candidate's meeting schedule as a list of meeting sessions.
	 * @return List of meeting sessions
	 */
	public List<vcfs.models.booking.MeetingSession> getMeetingSchedule() {
		List<vcfs.models.booking.MeetingSession> schedule = new ArrayList<>();
		for (Reservation r : reservations) {
			if (r.getSession() != null) {
				schedule.add(r.getSession());
			}
		}
		return schedule;
	}

	/**
	 * Cancel a request by ID (controller method).
	 * @param requestId The request ID to cancel
	 * @throws IllegalArgumentException if requestId is null or empty (prevents NPE)
	 */
	public void cancelRequest(String requestId) {
		if (requestId == null || requestId.trim().isEmpty()) {
			throw new IllegalArgumentException("Request ID cannot be empty");
		}
		requests.removeIf(r -> r != null && requestId.equals(r.getId()));
	}

	/**
	 * Get the request history.
	 * @return List of all requests
	 */
	public List<Request> getRequestHistory() {
		return new ArrayList<>(requests);
	}

	/**
	 * Set the candidate's phone number.
	 * @param phoneNumber The phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		if (this.profile != null) {
			this.profile.setPhoneNumber(phoneNumber);
		}
	}

	/**
	 * Override email setter.
	 * @param email The new email
	 */
	public void setEmail(String email) {
		super.setEmail(email);
	}

}



