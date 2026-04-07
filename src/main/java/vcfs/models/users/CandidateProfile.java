package vcfs.models.users;

/**
 * Candidate profile data used for auto-booking (string data is sufficient, file uploading of CVs is not required).
 */
public class CandidateProfile {

	private Candidate candidate;
	private String cvSummary;
	private String interestTags;

	/**
	 * Create an empty profile.
	 */
	public CandidateProfile() {
		this.candidate = null;
		this.cvSummary = "";
		this.interestTags = "";
	}

	/**
	 * Get the candidate this profile belongs to.
	 */
	public Candidate getCandidate() {
		return candidate;
	}

	/**
	 * Set the candidate this profile belongs to.
	 * @param candidate The candidate
	 */
	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	/**
	 * Get the CV summary.
	 */
	public String getCvSummary() {
		return cvSummary;
	}

	/**
	 * Set the CV summary.
	 * @param cvSummary The CV summary text
	 */
	public void setCvSummary(String cvSummary) {
		if (cvSummary == null) {
			this.cvSummary = "";
		} else {
			this.cvSummary = cvSummary;
		}
	}

	/**
	 * Get the interest tags.
	 */
	public String getInterestTags() {
		return interestTags;
	}

	/**
	 * Set the interest tags.
	 * @param interestTags The interest tags
	 */
	public void setInterestTags(String interestTags) {
		if (interestTags == null) {
			this.interestTags = "";
		} else {
			this.interestTags = interestTags;
		}
	}

	/**
	 * Update the candidate's profile fields.
	 * @param cvSummary The CV summary
	 * @param interestTags The interest tags
	 */
	public void updateProfile(String cvSummary, String interestTags) {
		setCvSummary(cvSummary);
		setInterestTags(interestTags);
	}

	@Override
	public String toString() {
		return "CandidateProfile{" +
				"cvSummary='" + cvSummary + '\'' +
				", interestTags='" + interestTags + '\'' +
				'}';
	}

}

