package vcfs.models.audit;

import vcfs.core.CareerFair;
import vcfs.core.LocalDateTime;

/**
 * Audit trail item recording significant events (phase changes, bookings, cancellations, outcomes).
 */
public class AuditEntry {

	private CareerFair fair;
	private LocalDateTime timestamp;
	private String eventType;

	/**
	 * Create an audit entry.
	 * @param fair The career fair
	 * @param timestamp The time of the event
	 * @param eventType The type of event
	 */
	public AuditEntry(CareerFair fair, LocalDateTime timestamp, String eventType) {
		setFair(fair);
		setTimestamp(timestamp);
		setEventType(eventType);
	}

	/**
	 * Get the career fair.
	 */
	public CareerFair getFair() {
		return fair;
	}

	/**
	 * Set the career fair.
	 * @param fair The fair
	 */
	public void setFair(CareerFair fair) {
		this.fair = fair;
	}

	/**
	 * Get the timestamp.
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Set the timestamp.
	 * @param timestamp The time
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Get the event type.
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Set the event type.
	 * @param eventType The type
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * Format the entry for display/export.
	 */
	@Override
	public String toString() {
		return String.format("[%s] %s | %s", 
			timestamp != null ? timestamp.toString() : "unknown",
			eventType != null ? eventType : "NO_EVENT",
			fair != null ? fair.name : "NO_FAIR");
	}

}

