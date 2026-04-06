package vcfs.models.users;

/**
 * Abstract base for all users (Candidate and Recruiter).
 */
public abstract class User {

	public String id;
	public String displayName;
	public String email;

	/**
	 * Return the internal user id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Return the user's unique email identifier.
	 */
	public String getEmail() {
		return this.email;
	}

}

