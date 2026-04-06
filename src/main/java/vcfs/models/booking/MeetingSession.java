package vcfs.models.booking;

import java.util.*;
import vcfs.core.LocalDateTime;
import vcfs.models.structure.VirtualRoom;
import vcfs.models.audit.AttendanceRecord;
import vcfs.models.enums.MeetingState;
import vcfs.models.enums.AttendanceOutcome;
import vcfs.models.users.Candidate;

/**
 * Per-reservation runtime session; uses Booth's VirtualRoom and owns lobby/attendance records.
 */
public class MeetingSession {

	public VirtualRoom room;
	public Lobby lobby;
	public Collection<AttendanceRecord> attendanceRecords;
	public Reservation reservation;
	public MeetingState state;

	/**
	 * Start the session (WAITING â†’ IN_PROGRESS).
	 * @param now
	 */
	public void start(LocalDateTime now) {
		// TODO - implement MeetingSession.start
		throw new UnsupportedOperationException();
	}

	/**
	 * End the session (IN_PROGRESS â†’ ENDED) and finalise records.
	 * @param now
	 */
	public void end(LocalDateTime now) {
		// TODO - implement MeetingSession.end
		throw new UnsupportedOperationException();
	}

	/**
	 * Create/update attendance record when a candidate joins.
	 * @param candidate
	 * @param now
	 */
	public AttendanceRecord recordJoin(Candidate candidate, LocalDateTime now) {
		// TODO - implement MeetingSession.recordJoin
		throw new UnsupportedOperationException();
	}

	/**
	 * Update attendance record when a candidate leaves.
	 * @param candidate
	 * @param now
	 */
	public void recordLeave(Candidate candidate, LocalDateTime now) {
		// TODO - implement MeetingSession.recordLeave
		throw new UnsupportedOperationException();
	}

	/**
	 * Set session outcome (attended / no-show / ended-early).
	 * @param outcome
	 */
	public void setOutcome(AttendanceOutcome outcome) {
		// TODO - implement MeetingSession.setOutcome
		throw new UnsupportedOperationException();
	}

}

