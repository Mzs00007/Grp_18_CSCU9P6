package vcfs.models.booking;

import java.util.*;
import vcfs.models.users.Candidate;

/**
 * Per-session waiting area for candidates who attempt to join early.
 */
public class Lobby {

	public MeetingSession session;
	private List<Candidate> waitingQueue;

	public Lobby() {
		this.waitingQueue = new ArrayList<>();
	}

	/**
	 * Add an early-arriving candidate to the lobby.
	 * @param candidate
	 */
	public void add(Candidate candidate) {
		if (candidate == null) {
			throw new IllegalArgumentException("[Lobby] Cannot add null candidate.");
		}
		if (!waitingQueue.contains(candidate)) {
			waitingQueue.add(candidate);
			System.out.println("[Lobby] Candidate joined queue: " + candidate.displayName 
				+ " (position: " + waitingQueue.size() + ")");
		}
	}

	/**
	 * Remove a candidate from the lobby queue.
	 * @param candidate
	 */
	public void remove(Candidate candidate) {
		if (candidate != null) {
			boolean removed = waitingQueue.remove(candidate);
			if (removed) {
				System.out.println("[Lobby] Candidate left queue: " + candidate.displayName);
			}
		}
	}

	public String listWaiting() {
		if (waitingQueue.isEmpty()) {
			return "[Lobby] No candidates waiting.";
		}
		StringBuilder sb = new StringBuilder("[Lobby] Waiting candidates (" + waitingQueue.size() + "):\n");
		for (int i = 0; i < waitingQueue.size(); i++) {
			Candidate c = waitingQueue.get(i);
			sb.append("  ").append(i + 1).append(". ")
				.append(c.displayName).append(" (").append(c.email).append(")\n");
		}
		return sb.toString();
	}

}

