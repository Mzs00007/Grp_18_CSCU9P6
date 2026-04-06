package vcfs.models.booking;

import vcfs.models.users.Candidate;

/**
 * Candidate booking preferences used for manual browsing and auto-booking.
 */
public class Request {

	public Candidate requester;
	public String desiredTags;
	public String preferredOrgs;
	public int maxAppointments;

	/**
	 * Update booking preferences
	 * @param desiredTags
	 * @param preferredOrgs
	 * @param maxAppointments
	 */
	public void updatePreferences(String desiredTags, String preferredOrgs, int maxAppointments) {
		this.desiredTags = desiredTags;
		this.preferredOrgs = preferredOrgs;
		this.maxAppointments = maxAppointments;
		System.out.println("[Request] Preferences updated for " 
			+ (requester != null ? requester.displayName : "unknown"));
	}

}

