package vcfs.models.users;

/**
 * Candidate profile data used for auto-booking (string data is sufficient, file uploading of CVs is not required).
 */
public class CandidateProfile {

	public Candidate candidate;
	public String cvSummary;
	public String interestTags;

	/**
	 * Update the candidate's profile fields.
	 * @param cvSummary
	 * @param interestTags
	 */
	public void updateProfile(String cvSummary, String interestTags) {
		// TODO - implement CandidateProfile.updateProfile
		throw new UnsupportedOperationException();
	}

}

