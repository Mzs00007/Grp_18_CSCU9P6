package vcfs.models.booking;

import vcfs.core.LocalDateTime;
import vcfs.models.users.Recruiter;
import java.util.*;

/**
 * A bookable appointment offering published by a recruiter. Availability is intentionally not modelled here; groups design availability representation.
 */
public class Offer {

	public Recruiter publisher;
	public Collection<Reservation> reservations;
	public String title;
	public int durationMins;
	public String topicTags;
	public int capacity;

	/**
	 * VCFS-003 (Zaid): Start time of this discrete availability slot.
	 * Set by parseAvailabilityIntoOffers() in CareerFairSystem.
	 */
	public LocalDateTime startTime;

	/**
	 * VCFS-003 (Zaid): End time of this discrete availability slot.
	 * Set by parseAvailabilityIntoOffers() in CareerFairSystem.
	 * Always equals startTime + durationMins.
	 */
	public LocalDateTime endTime;

	/**
	 * Update offer metadata (topic/tags, duration, capacity).
	 * @param title
	 * @param durationMins
	 * @param topicTags
	 * @param capacity
	 */
	public void updateDetails(String title, int durationMins, String topicTags, int capacity) {
		this.title = title;
		this.durationMins = durationMins;
		this.topicTags = topicTags;
		this.capacity = capacity;
		System.out.println("[Offer] Updated: " + title);
	}

	/**
	 * Return offer duration (minutes).
	 */
	public int getDurationMins() {
		return this.durationMins;
	}

}

