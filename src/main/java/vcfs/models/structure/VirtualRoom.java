package vcfs.models.structure;

import java.util.*;
import vcfs.models.booking.MeetingSession;
import vcfs.models.enums.RoomState;
import vcfs.models.users.Candidate;

/**
 * Booth's persistent meeting room resource; reused across many MeetingSessions.
 */
public class VirtualRoom {

	public Booth booth;
	public Collection<MeetingSession> sessions;
	public RoomState state;

	/**
	 * Open the room for access (CLOSED â†’ OPEN).
	 */
	public void open() {
		// TODO - implement VirtualRoom.open
		throw new UnsupportedOperationException();
	}

	/**
	 * Close the room (clear occupants; set CLOSED).
	 */
	public void close() {
		// TODO - implement VirtualRoom.close
		throw new UnsupportedOperationException();
	}

	/**
	 * Admit a candidate to the room.
	 * @param candidate
	 */
	public void enter(Candidate candidate) {
		// TODO - implement VirtualRoom.enter
		throw new UnsupportedOperationException();
	}

	/**
	 * Remove a candidate from the room; may change state if empty.
	 * @param candidate
	 */
	public void leave(Candidate candidate) {
		// TODO - implement VirtualRoom.leave
		throw new UnsupportedOperationException();
	}

}

