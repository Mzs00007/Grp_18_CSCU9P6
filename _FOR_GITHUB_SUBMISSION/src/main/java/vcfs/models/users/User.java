package vcfs.models.users;

/**
 * Abstract base for all users (Candidate and Recruiter).
 */
public abstract class User {


	private String id;
	private String displayName;
	private String email;

	/**
	 * Initialize a new User with required fields.
	 * @param id Unique user identifier (cannot be empty)
	 * @param displayName User's display name (cannot be empty)
	 * @param email User's email address (cannot be empty)
	 * @throws IllegalArgumentException if any parameter is null or empty
	 */
	protected User(String id, String displayName, String email) {
		this.id = id;
		this.displayName = displayName;
		this.email = email;
		validate();
	}

	/**
	 * Validate user fields on construction
	 */
	private void validate() {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("User ID cannot be empty");
		}
		if (displayName == null || displayName.trim().isEmpty()) {
			throw new IllegalArgumentException("Display name cannot be empty");
		}
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
	}
	public String getId() {
		return this.id;
	}

	/**
	 * Set the user id with validation.
	 * @param id Unique user identifier (cannot be empty)
	 * @throws IllegalArgumentException if id is null or empty
	 */
	public void setId(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("User ID cannot be empty");
		}
		this.id = id;
	}

	/**
	 * Return the user's display name.
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * Set the user's display name with validation.
	 * @param displayName User's display name (cannot be empty)
	 * @throws IllegalArgumentException if displayName is null or empty
	 */
	public void setDisplayName(String displayName) {
		if (displayName == null || displayName.trim().isEmpty()) {
			throw new IllegalArgumentException("Display name cannot be empty");
		}
		this.displayName = displayName;
	}

	/**
	 * Return the user's unique email identifier.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set the user's email with validation.
	 * @param email User's email address (cannot be empty)
	 * @throws IllegalArgumentException if email is null or empty
	 */
	public void setEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		this.email = email;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"id='" + id + '\'' +
				", displayName='" + displayName + '\'' +
				", email='" + email + '\'' +
				'}';
	}

}



