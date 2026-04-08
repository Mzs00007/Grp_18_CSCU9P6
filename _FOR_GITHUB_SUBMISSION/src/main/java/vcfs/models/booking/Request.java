package vcfs.models.booking;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.models.users.Candidate;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Candidate booking preferences used for manual browsing and auto-booking.
 */
public class Request {

	private String id;
	private Candidate requester;
	private String desiredTags;
	private String preferredOrgs;
	private int maxAppointments;

	/**
	 * Create an empty Request with valid default values.
	 */
	public Request() {
		this.requester = null;
		this.desiredTags = "";
		this.preferredOrgs = "";
		this.maxAppointments = 1;  // FIXED: Default to 1 instead of invalid 0
	}

	/**
	 * Get the requester (candidate).
	 */
	public Candidate getRequester() {
		return requester;
	}

	/**
	 * Get the candidate (alias for getRequester).
	 */
	public Candidate getCandidate() {
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
	 * Set the candidate (alias for setRequester).
	 * @param candidate The candidate
	 */
	public void setCandidate(Candidate candidate) {
		this.requester = candidate;
	}

	/**
	 * Get the request ID.
	 * @return The unique request ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the request ID.
	 * @param id The unique request ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the desired tags.
	 */
	public String getDesiredTags() {
		return desiredTags;
	}

	/**
	 * Set the desired tags.
	 * @param desiredTags The tags (can be null or empty)
	 */
	public void setDesiredTags(String desiredTags) {
		this.desiredTags = desiredTags;
	}

	/**
	 * Get the preferred organizations.
	 */
	public String getPreferredOrgs() {
		return preferredOrgs;
	}

	/**
	 * Get the preferred organizations (alias for getPreferredOrgs).
	 */
	public String getPreferredOrganizations() {
		return preferredOrgs;
	}

	/**
	 * Set the preferred organizations.
	 * @param preferredOrgs The organizations (can be empty string or null for no preference)
	 */
	public void setPreferredOrgs(String preferredOrgs) {
		this.preferredOrgs = preferredOrgs;
	}

	/**
	 * Set the preferred organizations (alias for setPreferredOrgs).
	 * @param preferredOrganizations The organizations (can be empty string or null for no preference)
	 */
	public void setPreferredOrganizations(String preferredOrganizations) {
		this.preferredOrgs = preferredOrganizations;
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



