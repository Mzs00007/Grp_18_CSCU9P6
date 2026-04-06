package vcfs.models.audit;

import vcfs.core.LocalDateTime;
import vcfs.models.booking.MeetingSession;
import vcfs.models.enums.AttendanceOutcome;

/**
 * Attendance log for a single participant in a MeetingSession.
 */
public class AttendanceRecord {

	public MeetingSession session;
	public LocalDateTime joinTime;
	public LocalDateTime leaveTime;
	public AttendanceOutcome outcome;

	/**
	 * Finalise the record with leave time and outcome.
	 * @param leaveTime
	 * @param outcome
	 */
	public void close(LocalDateTime leaveTime, AttendanceOutcome outcome) {
		this.leaveTime = leaveTime;
		this.outcome = outcome;
		System.out.println("[AttendanceRecord] Closed: outcome=" + outcome 
			+ " leave=" + (leaveTime != null ? leaveTime.toString() : "unknown"));
	}

}

