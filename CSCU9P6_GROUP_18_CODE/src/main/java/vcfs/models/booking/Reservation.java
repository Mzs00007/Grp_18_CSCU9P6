package vcfs.models.booking;


import vcfs.core.LocalDateTime;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.enums.ReservationState;
import vcfs.models.users.Candidate;

/**
 * A booking linking a Candidate to an Offer at a specific time window.
 */
public class Reservation {

	private Candidate candidate;
	private Offer offer;
	private MeetingSession session;
	private ReservationState state;
	private LocalDateTime scheduledStart;
	private LocalDateTime scheduledEnd;
	private String status;

	/**
	 * Create an empty Reservation.
	 */
	public Reservation() {
		this.candidate = null;
		this.offer = null;
		this.session = null;
		this.state = ReservationState.CONFIRMED;  // Initialize with valid state instead of null (THREAD-SAFE)
		this.scheduledStart = null;
		this.scheduledEnd = null;
		this.status = "PENDING";
	}

	/**
	 * Cancel the reservation (reason recorded by policy).
	 * @param reason The cancellation reason
	 * @throws IllegalArgumentException if reason is invalid
	 */
	public void cancel(String reason) {
		if (reason == null || reason.trim().isEmpty()) {
			throw new IllegalArgumentException("Cancellation reason cannot be empty");
		}
		this.state = ReservationState.CANCELLED;
		Logger.log(LogLevel.INFO, "Cancelled for " 
			+ (candidate != null ? candidate.getDisplayName() : "unknown") 
			+ ": " + reason);
	}

	/**
	 * True iff reservation is active at time now.
	 * @param now The time to check
	 * @return True if active
	 */
	public boolean isActive(LocalDateTime now) {
		// Null-safe state check: state should never be null (initialized in constructor)
		if (state == null || (state != ReservationState.CONFIRMED && state != ReservationState.IN_PROGRESS)) {
			return false;
		}
		if (scheduledStart == null || scheduledEnd == null || now == null) {
			return false;
		}
		return now.isAfterOrEqual(scheduledStart) && now.isBeforeOrEqual(scheduledEnd);
	}

	public Candidate getCandidate() { return candidate; }
	public void setCandidate(Candidate candidate) { 
		if (candidate == null) throw new IllegalArgumentException("Candidate cannot be null");
		this.candidate = candidate; 
	}
	
	public Offer getOffer() { return offer; }
	public void setOffer(Offer offer) { 
		if (offer == null) throw new IllegalArgumentException("Offer cannot be null");
		this.offer = offer; 
	}
	
	public MeetingSession getSession() { return session; }
	public void setSession(MeetingSession session) { 
		if (session == null) throw new IllegalArgumentException("Session cannot be null");
		this.session = session; 
	}
	
	public ReservationState getState() { return state; }
	public void setState(ReservationState state) { 
		if (state == null) throw new IllegalArgumentException("State cannot be null");
		this.state = state; 
	}
	
	public LocalDateTime getScheduledStart() { return scheduledStart; }
	public void setScheduledStart(LocalDateTime scheduledStart) { 
		this.scheduledStart = scheduledStart; 
	}
	
	public LocalDateTime getScheduledEnd() { return scheduledEnd; }
	public void setScheduledEnd(LocalDateTime scheduledEnd) { 
		if (scheduledEnd != null && scheduledStart != null && scheduledEnd.isBefore(scheduledStart)) {
			throw new IllegalArgumentException("Scheduled end must be after scheduled start");
		}
		this.scheduledEnd = scheduledEnd; 
	}

	@Override
	public String toString() {
		return "Reservation{" +
				"candidate=" + (candidate != null ? candidate.getDisplayName() : "unknown") +
				", offer=" + (offer != null ? offer.getTitle() : "none") +
				", state=" + state +
				", scheduledStart=" + scheduledStart +
				", scheduledEnd=" + scheduledEnd +
				'}';
	}

public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

}



