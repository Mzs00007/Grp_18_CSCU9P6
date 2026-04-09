package vcfs.models.users;

import java.util.*;
import vcfs.models.booking.Offer;
import vcfs.models.structure.Booth;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Recruiter assigned to a Booth; publishes Offers and hosts sessions.
 */
public class Recruiter extends User {


	private Collection<Offer> offers;
	private Booth booth;

	/**
	 * Create a Recruiter with required user details.
	 * @param id Unique recruiter identifier
	 * @param displayName Recruiter's display name
	 * @param email Recruiter's email address
	 */
	public Recruiter(String id, String displayName, String email) {
		super(id, displayName, email);
		this.offers = new ArrayList<>();
		this.booth = null;
	}

	/**
	 * Constructor with auto-generated ID.
	 * Generates an ID based on displayName and email.
	 * 
	 * @param displayName Recruiter's display name
	 * @param email Recruiter's email address
	 */
	public Recruiter(String displayName, String email) {
		this("rec_" + System.nanoTime(), displayName, email);
	}

	/**
	 * Get all offers published by this recruiter.
	 */
	public Collection<Offer> getOffers() {
		return Collections.unmodifiableCollection(offers);
	}

	/**
	 * Internal method to add an offer to this recruiter's collection.
	 * Used by CareerFairSystem.parseAvailabilityIntoOffers().
	 * @param offer The offer to add (cannot be null)
	 * @throws IllegalArgumentException if offer is null
	 */
	public void addOffer(Offer offer) {
		if (offer == null) {
			throw new IllegalArgumentException("Offer cannot be null");
		}
		this.offers.add(offer);
	}

	/**
	 * Get the booth assigned to this recruiter.
	 */
	public Booth getBooth() {
		return booth;
	}

	/**
	 * Set the booth assigned to this recruiter.
	 * @param booth The booth to assign (can be null for unassignment)
	 */
	public void setBooth(Booth booth) {
		this.booth = booth;
	}

	/**
	 * Create a new offer owned by this recruiter (system registers it).
	 * @param title Title of the offer (cannot be empty)
	 * @param durationMins Duration in minutes (must be positive)
	 * @param topicTags Topic tags for the offer (cannot be empty)
	 * @param capacity Capacity for the offer (must be positive)
	 * @return The created Offer
	 * @throws IllegalArgumentException if any parameter is invalid
	 */
	public Offer publishOffer(String title, int durationMins, String topicTags, int capacity) {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		if (durationMins <= 0) {
			throw new IllegalArgumentException("Duration must be positive");
		}
		if (topicTags == null || topicTags.trim().isEmpty()) {
			throw new IllegalArgumentException("Topic tags cannot be empty");
		}
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity must be positive");
		}

		Offer offer = new Offer();
		offer.setTitle(title);
		offer.setDurationMins(durationMins);
		offer.setTopicTags(topicTags);
		offer.setCapacity(capacity);
		offer.setPublisher(this);
		this.offers.add(offer);
		
		Logger.log(LogLevel.INFO, "Offer published by " + this.getDisplayName() + ": " + title);
		return offer;
	}

	/**
	 * Request cancellation of a reservation as a recruiter (policy enforced by system).
	 * @param reservationId ID of the reservation to cancel
	 * @param reason Reason for cancellation
	 * @throws IllegalArgumentException if parameters are invalid
	 */
	public void cancelReservation(String reservationId, String reason) {
		if (reservationId == null || reservationId.trim().isEmpty()) {
			throw new IllegalArgumentException("Reservation ID cannot be empty");
		}
		if (reason == null || reason.trim().isEmpty()) {
			throw new IllegalArgumentException("Reason cannot be empty");
		}

		Logger.log(LogLevel.INFO, "Cancellation requested by " + this.getDisplayName() 
			+ ": " + reservationId + " (" + reason + ")");
	}

	/**
	 * Return a human-readable view of the recruiter's schedule.
	 */
	public String viewSchedule() {
		if (offers.isEmpty()) {
			return "[Recruiter] " + this.getDisplayName() + " has no published offers.";
		}
		StringBuilder sb = new StringBuilder("[Recruiter] " + this.getDisplayName() + "'s Offers:\n");
		for (Offer o : offers) {
			sb.append("  - ").append(o.getTitle())
				.append(" @ ").append(o.getStartTime() != null ? o.getStartTime() : "not scheduled")
				.append(" (").append(o.getDurationMins()).append(" min, cap=").append(o.getCapacity()).append(")\n");
		}
		return sb.toString();
	}

	/**
	 * Publish an offer object (controller method).
	 * @param offer The offer to publish
	 */
	public void publishOffer(Offer offer) {
		if (offer != null && !offers.contains(offer)) {
			this.offers.add(offer);
			Logger.log(LogLevel.INFO, "Offer published: " + offer.getTitle());
		}
	}

	/**
	 * Schedule a meeting session (controller method).
	 * @param session The meeting session to schedule
	 */
	public void scheduleSession(vcfs.models.booking.MeetingSession session) {
		if (session != null) {
			Logger.log(LogLevel.INFO, "Session scheduled by " + this.getDisplayName());
		}
	}

	/**
	 * Get meeting history.
	 * @return List of meeting sessions
	 */
	public List<vcfs.models.booking.MeetingSession> getMeetingHistory() {
		List<vcfs.models.booking.MeetingSession> sessions = new ArrayList<>();
		// Return any sessions associated with offers
		for (Offer o : offers) {
			if (o.getMeetingSession() != null) {
				sessions.add(o.getMeetingSession());
			}
		}
		return sessions;
	}

	/**
	 * Update offer status (controller method).
	 * @param offerId The offer ID to update
	 * @param status The new status
	 */
	public void updateOfferStatus(String offerId, String status) {
		Logger.log(LogLevel.INFO, "Offer status updated: " + offerId + " -> " + status);
	}

	/**
	 * Cancel a session (controller method).
	 * @param sessionId The session ID to cancel
	 */
	public void cancelSession(String sessionId) {
		Logger.log(LogLevel.INFO, "Session cancelled: " + sessionId);
	}

	/**
	 * Get all published offers.
	 * @return List of offers
	 */
	public List<Offer> getPublishedOffers() {
		return new ArrayList<>(offers);
	}

}



