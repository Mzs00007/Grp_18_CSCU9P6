package vcfs.models.structure;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.util.*;
import vcfs.models.booking.MeetingSession;
import vcfs.models.enums.RoomState;
import vcfs.models.users.Candidate;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Booth's persistent meeting room resource; reused across many MeetingSessions.
 */
public class VirtualRoom {

	private Booth booth;
	private Collection<MeetingSession> sessions;
	private Collection<Candidate> occupants;
	private RoomState state;

	/**
	 * Create a VirtualRoom for a booth.
	 * @param booth The booth that owns this room (cannot be null)
	 * @throws IllegalArgumentException if booth is null
	 */
	public VirtualRoom(Booth booth) {
		setBooth(booth);
		this.sessions = new ArrayList<>();
		this.occupants = new ArrayList<>();
		this.state = RoomState.CLOSED;
	}

	/**
	 * Get the booth that owns this room.
	 */
	public Booth getBooth() {
		return booth;
	}

	/**
	 * Set the booth that owns this room.
	 * @param booth The booth (cannot be null)
	 * @throws IllegalArgumentException if booth is null
	 */
	public void setBooth(Booth booth) {
		if (booth == null) {
			throw new IllegalArgumentException("Booth cannot be null");
		}
		this.booth = booth;
	}

	/**
	 * Get all meeting sessions in this room.
	 */
	public Collection<MeetingSession> getSessions() {
		return Collections.unmodifiableCollection(sessions);
	}

	/**
	 * Get all candidates currently in this room.
	 */
	public Collection<Candidate> getOccupants() {
		return Collections.unmodifiableCollection(occupants);
	}

	/**
	 * Get the room state.
	 */
	public RoomState getState() {
		return state;
	}

	/**
	 * Set the room state.
	 * @param state The room state
	 */
	public void setState(RoomState state) {
		if (state == null) {
			throw new IllegalArgumentException("Room state cannot be null");
		}
		this.state = state;
	}

	/**
	 * Open the room for access.
	 */
	public void open() {
		state = RoomState.OPEN;
		Logger.log(LogLevel.INFO, "VirtualRoom opened for booth: " + booth.getTitle());
	}

	/**
	 * Close the room and clear occupants.
	 */
	public void close() {
		state = RoomState.CLOSED;
		occupants.clear();
		Logger.log(LogLevel.INFO, "VirtualRoom closed for booth: " + booth.getTitle());
	}

	/**
	 * Admit a candidate to the room.
	 * @param candidate The candidate entering
	 */
	public void enter(Candidate candidate) {
		if (candidate == null) {
			throw new IllegalArgumentException("Candidate cannot be null");
		}
		if (state != RoomState.OPEN) {
			Logger.log(LogLevel.WARNING, "Attempted to enter room that is not open");
			return;
		}
		occupants.add(candidate);
		Logger.log(LogLevel.INFO, "Candidate " + candidate.getDisplayName() + " entered VirtualRoom");
	}

	/**
	 * Remove a candidate from the room.
	 * @param candidate The candidate leaving
	 */
	public void leave(Candidate candidate) {
		if (candidate == null) {
			throw new IllegalArgumentException("Candidate cannot be null");
		}
		occupants.remove(candidate);
		Logger.log(LogLevel.INFO, "Candidate " + candidate.getDisplayName() + " left VirtualRoom");
	}

	@Override
	public String toString() {
		return "VirtualRoom{" +
				"booth=" + booth.getTitle() +
				", state=" + state +
				", occupants=" + occupants.size() +
				'}';
	}

}



