package vcfs.models.booking;

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
		if (durationMins <= 0) {
			throw new IllegalArgumentException("Duration must be positive");
		}
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity must be positive");
		}
		this.title = title;
		this.durationMins = durationMins;
		this.topicTags = topicTags;
		this.capacity = capacity;
		this.publisher = publisher;
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
	 * @param publisher The recruiter (can be null)
	 */
	public void setPublisher(Recruiter publisher) {
		this.publisher = publisher;
	}

	/**
	 * Get all reservations for this offer.
	 */
	public Collection<Reservation> getReservations() {
		return Collections.unmodifiableCollection(reservations);
	}

	/**
	 * Add a reservation to this offer's list.
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
	 * Get the title of this offer.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of this offer.
	 * @param title The title (can be null or empty)
	 */
	public void setTitle(String title) {
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
	 * @param topicTags The tags (can be null or empty)
	 */
	public void setTopicTags(String topicTags) {
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
	 * @param startTime The start time (cannot be null)
	 * @throws IllegalArgumentException if startTime is null
	 */
	public void setStartTime(LocalDateTime startTime) {
		if (startTime == null) throw new IllegalArgumentException("Start time cannot be null");
		if (endTime != null && startTime.isAfter(endTime)) {
			throw new IllegalArgumentException("Start time must be before end time");
		}
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
	 * @param endTime The end time (cannot be null)
	 * @throws IllegalArgumentException if endTime is null or before startTime
	 */
	public void setEndTime(LocalDateTime endTime) {
		if (endTime == null) throw new IllegalArgumentException("End time cannot be null");
		if (startTime != null && endTime.isBefore(startTime)) {
			throw new IllegalArgumentException("End time must be after start time");
		}
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



