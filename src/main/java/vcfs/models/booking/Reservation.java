package vcfs.models.booking;

import vcfs.core.LocalDateTime;
import vcfs.models.enums.ReservationState;
import vcfs.models.users.Candidate;

/**
 * A booking linking a Candidate to an Offer at a specific time window.
 */
public class Reservation {

	public Candidate candidate;
	public Offer offer;
	public MeetingSession session;
	public ReservationState state;
	public LocalDateTime scheduledStart;
	public LocalDateTime scheduledEnd;

	/**
	 * Cancel the reservation (reason recorded by policy).
	 * @param reason
	 */
	public void cancel(String reason) {
		this.state = ReservationState.CANCELLED;
		System.out.println("[Reservation] Cancelled for " 
			+ (candidate != null ? candidate.displayName : "unknown") 
			+ ": " + reason);
	}

	/**
	 * True iff reservation is active at time now.
	 * @param now
	 */
	public boolean isActive(LocalDateTime now) {
		if (state != ReservationState.CONFIRMED && state != ReservationState.IN_PROGRESS) {
			return false;
		}
		if (scheduledStart == null || scheduledEnd == null || now == null) {
			return false;
		}
		return now.isAfterOrEqual(scheduledStart) && now.isBeforeOrEqual(scheduledEnd);
	}

}

