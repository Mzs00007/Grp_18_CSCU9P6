package vcfs.models.users;

import java.util.*;
import vcfs.models.booking.Offer;
import vcfs.models.structure.Booth;

/**
 * Recruiter assigned to a Booth; publishes Offers and hosts sessions.
 */
public class Recruiter extends User {

	public Collection<Offer> offers;
	public Booth booth;

	/**
	 * Create a new offer owned by this recruiter (system registers it).
	 * @param title
	 * @param durationMins
	 * @param topicTags
	 * @param capacity
	 */
	public Offer publishOffer(String title, int durationMins, String topicTags, int capacity) {
		if (this.offers == null) {
			this.offers = new ArrayList<>();
		}
		Offer offer = new Offer();
		offer.title = title;
		offer.durationMins = durationMins;
		offer.topicTags = topicTags;
		offer.capacity = capacity;
		offer.publisher = this;
		offer.reservations = new ArrayList<>();
		this.offers.add(offer);
		System.out.println("[Recruiter] Offer published by " + this.displayName + ": " + title);
		return offer;
	}

	/**
	 * Request cancellation of a reservation as a recruiter (policy enforced by system).
	 * @param reservationId
	 * @param reason
	 */
	public void cancelReservation(String reservationId, String reason) {
		System.out.println("[Recruiter] Cancellation requested by " + this.displayName 
			+ ": " + reservationId + " (" + reason + ")");
	}

	/**
	 * Return a human-readable view of the recruiter's schedule.
	 */
	public String viewSchedule() {
		if (offers == null || offers.isEmpty()) {
			return "[Recruiter] " + this.displayName + " has no published offers.";
		}
		StringBuilder sb = new StringBuilder("[Recruiter] " + this.displayName + "'s Offers:\n");
		for (Offer o : offers) {
			sb.append("  - ").append(o.title)
				.append(" @ ").append(o.startTime != null ? o.startTime : "not scheduled")
				.append(" (").append(o.durationMins).append(" min, cap=").append(o.capacity).append(")\n");
		}
		return sb.toString();
	}

}

