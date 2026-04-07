package vcfs.models.audit;

import vcfs.core.LocalDateTime;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
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
	 * Create an empty attendance record.
	 */
	public AttendanceRecord() {
		this.session = null;
		this.joinTime = null;
		this.leaveTime = null;
		this.outcome = null;
	}

	/**
	 * Finalise the record with leave time and outcome.
	 * @param leaveTime The time the participant left
	 * @param outcome The attendance outcome
	 * @throws IllegalArgumentException if outcome is null
	 */
	public void close(LocalDateTime leaveTime, AttendanceOutcome outcome) {
		if (outcome == null) {
			throw new IllegalArgumentException("Outcome cannot be null");
		}
		this.leaveTime = leaveTime;
		this.outcome = outcome;
		Logger.log(LogLevel.INFO, "Record closed: outcome=" + outcome 
			+ " leave=" + (leaveTime != null ? leaveTime.toString() : "unknown"));
	}

	@Override
	public String toString() {
		return "AttendanceRecord{" +
				"joinTime=" + joinTime +
				", leaveTime=" + leaveTime +
				", outcome=" + outcome +
				'}';
	}

}

