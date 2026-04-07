package vcfs.models.booking;

import vcfs.models.users.Candidate;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Candidate booking preferences used for manual browsing and auto-booking.
 */
public class Request {

	private Candidate requester;
	private String desiredTags;
	private String preferredOrgs;
	private int maxAppointments;

	/**
	 * Create an empty Request.
	 */
	public Request() {
		this.requester = null;
		this.desiredTags = "";
		this.preferredOrgs = "";
		this.maxAppointments = 0;
	}

	/**
	 * Get the requester (candidate).
	 */
	public Candidate getRequester() {
		return requester;
	}

	/**
	 * Set the requester (candidate).
	 * @param requester The candidate
	 */
	public void setRequester(Candidate requester) {
		this.requester = requester;
	}

	/**
	 * Get the desired tags.
	 */
	public String getDesiredTags() {
		return desiredTags;
	}

	/**
	 * Set the desired tags.
	 * @param desiredTags The tags (cannot be empty)
	 * @throws IllegalArgumentException if tags are invalid
	 */
	public void setDesiredTags(String desiredTags) {
		if (desiredTags == null || desiredTags.trim().isEmpty()) {
			throw new IllegalArgumentException("Desired tags cannot be empty");
		}
		this.desiredTags = desiredTags;
	}

	/**
	 * Get the preferred organizations.
	 */
	public String getPreferredOrgs() {
		return preferredOrgs;
	}

	/**
	 * Set the preferred organizations.
	 * @param preferredOrgs The organizations (cannot be empty)
	 * @throws IllegalArgumentException if orgs are invalid
	 */
	public void setPreferredOrgs(String preferredOrgs) {
		if (preferredOrgs == null || preferredOrgs.trim().isEmpty()) {
			throw new IllegalArgumentException("Preferred organizations cannot be empty");
		}
		this.preferredOrgs = preferredOrgs;
	}

	/**
	 * Get the maximum appointments.
	 */
	public int getMaxAppointments() {
		return maxAppointments;
	}

	/**
	 * Set the maximum appointments.
	 * @param maxAppointments The max (must be positive)
	 * @throws IllegalArgumentException if invalid
	 */
	public void setMaxAppointments(int maxAppointments) {
		if (maxAppointments <= 0) {
			throw new IllegalArgumentException("Max appointments must be positive");
		}
		this.maxAppointments = maxAppointments;
	}

	/**
	 * Update booking preferences.
	 * @param desiredTags The desired tags (cannot be empty)
	 * @param preferredOrgs The preferred organizations (cannot be empty)
	 * @param maxAppointments The maximum appointments (must be positive)
	 * @throws IllegalArgumentException if any parameter is invalid
	 */
	public void updatePreferences(String desiredTags, String preferredOrgs, int maxAppointments) {
		setDesiredTags(desiredTags);
		setPreferredOrgs(preferredOrgs);
		setMaxAppointments(maxAppointments);
		Logger.log(LogLevel.INFO, "Preferences updated for " 
			+ (requester != null ? requester.getDisplayName() : "unknown"));
	}

	@Override
	public String toString() {
		return "Request{" +
				"requester=" + (requester != null ? requester.getDisplayName() : "unknown") +
				", desiredTags='" + desiredTags + '\'' +
				", preferredOrgs='" + preferredOrgs + '\'' +
				", maxAppointments=" + maxAppointments +
				'}';
	}

}

