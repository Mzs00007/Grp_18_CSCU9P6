package vcfs.models.audit;

import vcfs.core.CareerFair;
import vcfs.core.LocalDateTime;

/**
 * Audit trail item recording significant events (phase changes, bookings, cancellations, outcomes).
 */
public class AuditEntry {

	public CareerFair fair;
	public LocalDateTime timestamp;
	public String eventType;

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

