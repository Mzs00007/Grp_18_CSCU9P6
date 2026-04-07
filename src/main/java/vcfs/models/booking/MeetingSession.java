package vcfs.models.booking;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.util.*;
import vcfs.core.LocalDateTime;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.structure.VirtualRoom;
import vcfs.models.audit.AttendanceRecord;
import vcfs.models.enums.MeetingState;
import vcfs.models.enums.AttendanceOutcome;
import vcfs.models.users.Candidate;

/**
 * Per-reservation runtime session; uses Booth's VirtualRoom and owns lobby/attendance records.
 */
public class MeetingSession {

	private VirtualRoom room;
	private Lobby lobby;
	private Collection<AttendanceRecord> attendanceRecords;
	private Reservation reservation;
	private MeetingState state;
	private String title;

	/**
	 * Create an empty MeetingSession.
	 */
	public MeetingSession() {
		this.room = null;
		this.lobby = new Lobby();
		this.attendanceRecords = new ArrayList<>();
		this.reservation = null;
		this.state = MeetingState.WAITING;
		this.title = "Meeting Session";
	}

	/**
	 * Get the virtual room for this session.
	 */
	public VirtualRoom getRoom() {
		return room;
	}

	/**
	 * Set the virtual room for this session.
	 * @param room The virtual room
	 */
	public void setRoom(VirtualRoom room) {
		this.room = room;
	}

	/**
	 * Get the lobby for this session.
	 */
	public Lobby getLobby() {
		return lobby;
	}

	/**
	 * Get all attendance records.
	 */
	public Collection<AttendanceRecord> getAttendanceRecords() {
		return Collections.unmodifiableCollection(attendanceRecords);
	}

	/**
	 * Get the reservation for this session.
	 */
	public Reservation getReservation() {
		return reservation;
	}

	/**
	 * Set the reservation for this session.
	 * @param reservation The reservation
	 */
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	/**
	 * Get the session state.
	 */
	public MeetingState getState() {
		return state;
	}

	/**
	 * Set the session state.
	 * @param state The meeting state
	 */
	public void setState(MeetingState state) {
		if (state == null) {
			throw new IllegalArgumentException("Meeting state cannot be null");
		}
		this.state = state;
	}

	/**
	 * Start the session.
	 * @param now The current time
	 */
	public void start(LocalDateTime now) {
		if (state != MeetingState.WAITING) {
			Logger.log(LogLevel.WARNING, "Attempted to start session that is not waiting");
			return;
		}
		state = MeetingState.IN_PROGRESS;
		Logger.log(LogLevel.INFO, "MeetingSession started at " + now);
	}

	/**
	 * End the session and finalise records.
	 * @param now The current time
	 */
	public void end(LocalDateTime now) {
		if (state != MeetingState.IN_PROGRESS) {
			Logger.log(LogLevel.WARNING, "Attempted to end session that is not in progress");
			return;
		}
		state = MeetingState.ENDED;
		Logger.log(LogLevel.INFO, "MeetingSession ended at " + now);
	}

	/**
	 * Create/update attendance record when a candidate joins.
	 * @param candidate The candidate joining
	 * @param now The join time
	 * @return The attendance record
	 */
	public AttendanceRecord recordJoin(Candidate candidate, LocalDateTime now) {
		if (candidate == null) {
			throw new IllegalArgumentException("Candidate cannot be null");
		}
		AttendanceRecord record = new AttendanceRecord();
		record.session = this;
		record.joinTime = now;
		attendanceRecords.add(record);
		Logger.log(LogLevel.INFO, "Join recorded for " + candidate.getDisplayName());
		return record;
	}

	/**
	 * Update attendance record when a candidate leaves.
	 * @param candidate The candidate leaving
	 * @param now The leave time
	 */
	public void recordLeave(Candidate candidate, LocalDateTime now) {
		if (candidate == null) {
			throw new IllegalArgumentException("Candidate cannot be null");
		}
		for (AttendanceRecord record : attendanceRecords) {
			if (record.joinTime != null && record.leaveTime == null) {
				record.leaveTime = now;
				Logger.log(LogLevel.INFO, "Leave recorded for " + candidate.getDisplayName());
				return;
			}
		}
	}

	/**
	 * Set session outcome.
	 * @param outcome The outcome
	 */
	public void setOutcome(AttendanceOutcome outcome) {
		if (outcome == null) {
			throw new IllegalArgumentException("Outcome cannot be null");
		}
		for (AttendanceRecord record : attendanceRecords) {
			record.outcome = outcome;
		}
		Logger.log(LogLevel.INFO, "Session outcome set to: " + outcome);
	}

	@Override
	public String toString() {
		return "MeetingSession{" +
				"state=" + state +
				", records=" + attendanceRecords.size() +
				'}';
	}

	/**
	 * Get the title of this meeting session.
	 * @return The session title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of this meeting session.
	 * @param title The session title
	 */
	public void setTitle(String title) {
		this.title = title != null ? title : "Meeting Session";
	}

}



