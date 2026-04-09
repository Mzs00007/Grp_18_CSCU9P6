package vcfs.models.users;

/**
 * Candidate profile data used for auto-booking (string data is sufficient, file uploading of CVs is not required).
 */
public class CandidateProfile {


	private Candidate candidate;
	private String cvSummary;
	private String interestTags;
	private String phoneNumber;
	private String skills;
	private String preferences;
	private String cv;

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
	 * Set the CV summary with validation.
	 * @param cvSummary The CV summary text (max 1000 characters)
	 * @throws IllegalArgumentException if summary exceeds maximum length
	 */
	public void setCvSummary(String cvSummary) {
		if (cvSummary == null || cvSummary.trim().isEmpty()) {
			this.cvSummary = "";
		} else {
			String trimmed = cvSummary.trim();
			if (trimmed.length() > 1000) {
				throw new IllegalArgumentException("CV Summary cannot exceed 1000 characters (got: " + trimmed.length() + ")");
			}
			this.cvSummary = trimmed;
		}
	}

	/**
	 * Get the interest tags.
	 */
	public String getInterestTags() {
		return interestTags;
	}

	/**
	 * Set the interest tags with validation.
	 * @param interestTags Comma-separated interest tags (max 200 characters)
	 * @throws IllegalArgumentException if tags exceed maximum length
	 */
	public void setInterestTags(String interestTags) {
		if (interestTags == null || interestTags.trim().isEmpty()) {
			this.interestTags = "";
		} else {
			String trimmed = interestTags.trim();
			if (trimmed.length() > 200) {
				throw new IllegalArgumentException("Interest tags cannot exceed 200 characters (got: " + trimmed.length() + ")");
			}
			this.interestTags = trimmed;
		}
	}

	/**
	 * Get the phone number.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Set the phone number.
	 * @param phoneNumber The phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSkills() {
        return skills;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public void setCV(String cv) {
        this.cv = cv;
    }

    public Object getCV() {
        return cv;
    }

    public Object getPreferences() {
        return preferences;
    }

}



