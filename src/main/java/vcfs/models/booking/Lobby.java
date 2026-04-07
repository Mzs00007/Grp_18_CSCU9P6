package vcfs.models.booking;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.util.*;
import vcfs.models.users.Candidate;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Per-session waiting area for candidates who attempt to join early.
 */
public class Lobby {

	private MeetingSession session;
	private List<Candidate> waitingQueue;

	/**
	 * Create an empty Lobby.
	 */
	public Lobby() {
		this.session = null;
		this.waitingQueue = new ArrayList<>();
	}

	/**
	 * Get the meeting session for this lobby.
	 */
	public MeetingSession getSession() {
		return session;
	}

	/**
	 * Set the meeting session for this lobby.
	 * @param session The session
	 */
	public void setSession(MeetingSession session) {
		this.session = session;
	}

	/**
	 * Get the waiting queue (read-only).
	 */
	public List<Candidate> getWaitingQueue() {
		return Collections.unmodifiableList(waitingQueue);
	}

	/**
	 * Add an early-arriving candidate to the lobby.
	 * @param candidate The candidate to add
	 * @throws IllegalArgumentException if candidate is null
	 */
	public void add(Candidate candidate) {
		if (candidate == null) {
			throw new IllegalArgumentException("Cannot add null candidate.");
		}
		if (!waitingQueue.contains(candidate)) {
			waitingQueue.add(candidate);
			Logger.log(LogLevel.INFO, "Candidate joined queue: " + candidate.getDisplayName() 
				+ " (position: " + waitingQueue.size() + ")");
		}
	}

	/**
	 * Remove a candidate from the lobby queue.
	 * @param candidate The candidate to remove
	 */
	public void remove(Candidate candidate) {
		if (candidate != null) {
			boolean removed = waitingQueue.remove(candidate);
			if (removed) {
				Logger.log(LogLevel.INFO, "Candidate left queue: " + candidate.getDisplayName());
			}
		}
	}

	/**
	 * Get a formatted string of waiting candidates.
	 */
	public String listWaiting() {
		if (waitingQueue.isEmpty()) {
			return "[Lobby] No candidates waiting.";
		}
		StringBuilder sb = new StringBuilder("[Lobby] Waiting candidates (" + waitingQueue.size() + "):\n");
		for (int i = 0; i < waitingQueue.size(); i++) {
			Candidate c = waitingQueue.get(i);
			sb.append("  ").append(i + 1).append(". ")
				.append(c.getDisplayName()).append(" (").append(c.getEmail()).append(")\n");
		}
		return sb.toString();
	}

	/**
	 * Get all meeting sessions (returns single session if set).
	 * @return List of meeting sessions
	 */
	public List<MeetingSession> getMeetingSessions() {
		List<MeetingSession> sessions = new ArrayList<>();
		if (session != null) {
			sessions.add(session);
		}
		return sessions;
	}

	/**
	 * Get available sessions (sessions not full).
	 * @return List of available sessions
	 */
	public List<MeetingSession> getAvailableSessions() {
		List<MeetingSession> available = new ArrayList<>();
		if (session != null && session.getLobby().getWaitingQueue().size() < 10) {
			available.add(session);
		}
		return available;
	}

}



