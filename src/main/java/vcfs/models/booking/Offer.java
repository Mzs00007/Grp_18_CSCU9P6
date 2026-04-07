package vcfs.models.booking;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.core.LocalDateTime;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.users.Recruiter;
import java.util.*;

/**
 * A bookable appointment offering published by a recruiter. Availability is intentionally not modelled here; groups design availability representation.
 */
public class Offer {

	private Recruiter publisher;
	private Collection<Reservation> reservations;
	private String title;
	private int durationMins;
	private String topicTags;
	private int capacity;
	private MeetingSession meetingSession;

	/**
	 * VCFS-003 (Zaid): Start time of this discrete availability slot.
	 * Set by parseAvailabilityIntoOffers() in CareerFairSystem.
	 */
	private LocalDateTime startTime;

	/**
	 * VCFS-003 (Zaid): End time of this discrete availability slot.
	 * Set by parseAvailabilityIntoOffers() in CareerFairSystem.
	 * Always equals startTime + durationMins.
	 */
	private LocalDateTime endTime;

	/**
	 * Create an Offer with required details.
	 * @param title Title of the offer
	 * @param durationMins Duration in minutes
	 * @param topicTags Topic tags
	 * @param capacity Maximum capacity
	 * @param publisher The recruiter publishing this offer
	 * @throws IllegalArgumentException if any parameter is invalid
	 */
	public Offer(String title, int durationMins, String topicTags, int capacity, Recruiter publisher) {
		setTitle(title);
		setDurationMins(durationMins);
		setTopicTags(topicTags);
		setCapacity(capacity);
		setPublisher(publisher);
		this.reservations = new ArrayList<>();
	}

	/**
	 * Create an empty Offer (fields must be set via setters).
	 */
	public Offer() {
		this.reservations = new ArrayList<>();
	}

	/**
	 * Get the publisher of this offer.
	 */
	public Recruiter getPublisher() {
		return publisher;
	}

	/**
	 * Set the publisher of this offer.
	 * @param publisher The recruiter (cannot be null)
	 * @throws IllegalArgumentException if publisher is null
	 */
	public void setPublisher(Recruiter publisher) {
		if (publisher == null) {
			throw new IllegalArgumentException("Publisher cannot be null");
		}
		this.publisher = publisher;
	}

	/**
	 * Get all reservations for this offer.
	 */
	public Collection<Reservation> getReservations() {
		return Collections.unmodifiableCollection(reservations);
	}

	/**
	 * Get the title of this offer.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of this offer.
	 * @param title The title (cannot be empty)
	 * @throws IllegalArgumentException if title is invalid
	 */
	public void setTitle(String title) {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		this.title = title;
	}

	/**
	 * Get the duration in minutes.
	 */
	public int getDurationMins() {
		return durationMins;
	}

	/**
	 * Set the duration in minutes.
	 * @param durationMins The duration (must be positive)
	 * @throws IllegalArgumentException if duration is invalid
	 */
	public void setDurationMins(int durationMins) {
		if (durationMins <= 0) {
			throw new IllegalArgumentException("Duration must be positive");
		}
		this.durationMins = durationMins;
	}

	/**
	 * Get the topic tags for this offer.
	 */
	public String getTopicTags() {
		return topicTags;
	}

	/**
	 * Set the topic tags for this offer.
	 * @param topicTags The tags (cannot be empty)
	 * @throws IllegalArgumentException if tags are invalid
	 */
	public void setTopicTags(String topicTags) {
		if (topicTags == null || topicTags.trim().isEmpty()) {
			throw new IllegalArgumentException("Topic tags cannot be empty");
		}
		this.topicTags = topicTags;
	}

	/**
	 * Get the capacity of this offer.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Set the capacity of this offer.
	 * @param capacity The capacity (must be positive)
	 * @throws IllegalArgumentException if capacity is invalid
	 */
	public void setCapacity(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity must be positive");
		}
		this.capacity = capacity;
	}

	/**
	 * Get the start time of this offer.
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Set the start time of this offer.
	 * @param startTime The start time
	 */
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Get the end time of this offer.
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * Set the end time of this offer.
	 * @param endTime The end time
	 */
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	/**
	 * Get the meeting session for this offer.
	 * @return The meeting session, or null if not set
	 */
	public MeetingSession getMeetingSession() {
		return meetingSession;
	}

	/**
	 * Set the meeting session for this offer.
	 * @param session The meeting session
	 */
	public void setMeetingSession(MeetingSession session) {
		this.meetingSession = session;
	}

	/**
	 * Update offer metadata (title/tags, duration, capacity).
	 * @param title New title (cannot be empty)
	 * @param durationMins New duration (must be positive)
	 * @param topicTags New tags (cannot be empty)
	 * @param capacity New capacity (must be positive)
	 * @throws IllegalArgumentException if any parameter is invalid
	 */
	public void updateDetails(String title, int durationMins, String topicTags, int capacity) {
		setTitle(title);
		setDurationMins(durationMins);
		setTopicTags(topicTags);
		setCapacity(capacity);
		Logger.log(LogLevel.INFO, "Offer updated: " + title);
	}

	@Override
	public String toString() {
		return "Offer{" +
				"title='" + title + '\'' +
				", durationMins=" + durationMins +
				", capacity=" + capacity +
				", startTime=" + startTime +
				'}';
	}

}



