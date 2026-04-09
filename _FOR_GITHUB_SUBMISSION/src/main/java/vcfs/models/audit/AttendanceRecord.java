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


	private MeetingSession session;
	private vcfs.models.users.Candidate candidate;
	private LocalDateTime joinTime;
	private LocalDateTime leaveTime;
	private AttendanceOutcome outcome;

	/**
	 * Create an empty attendance record.
	 */
	public AttendanceRecord() {
		this.session = null;
		this.candidate = null;
		this.joinTime = null;
		this.leaveTime = null;
		this.outcome = null;
	}

	public MeetingSession getSession() {
		return session;
	}

	public void setSession(MeetingSession session) {
		this.session = session;
	}

	public vcfs.models.users.Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(vcfs.models.users.Candidate candidate) {
		this.candidate = candidate;
	}

	public LocalDateTime getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(LocalDateTime joinTime) {
		this.joinTime = joinTime;
	}

	public LocalDateTime getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(LocalDateTime leaveTime) {
		this.leaveTime = leaveTime;
	}

	public AttendanceOutcome getOutcome() {
		return outcome;
	}

	public void setOutcome(AttendanceOutcome outcome) {
		this.outcome = outcome;
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
