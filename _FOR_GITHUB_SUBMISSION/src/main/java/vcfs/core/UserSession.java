package vcfs.core;

import vcfs.models.users.Candidate;
import vcfs.models.users.Recruiter;

/**
 * User Session Manager — Singleton pattern for tracking logged-in user.
 *
 * SINGLETON:
 *   Only ONE UserSession instance exists. All UI components and controllers
 *   access the same session state via UserSession.getInstance().
 *
 * PURPOSE:
 *   Maintains the identity and context of the currently logged-in user.
 *   This allows the UI to know who is using the system and customize
 *   the experience accordingly (Admin console vs Recruiter dashboard vs Candidate portal).
 *
 * USAGE:
 *   // During login
 *   Recruiter recruiter = new Recruiter(...);
 *   UserSession.getInstance().setCurrentRecruiter(recruiter);
 *   UserSession.getInstance().setCurrentRole(UserRole.RECRUITER);
 *
 *   // In controllers/views
 *   Recruiter current = UserSession.getInstance().getCurrentRecruiter();
 *   if (current != null) { ... }
 *
 *   // On logout
 *   UserSession.getInstance().logout();
 *
 * Implemented by: Zaid
 */
public class UserSession {

    // =========================================================
    // SINGLETON INFRASTRUCTURE
    // =========================================================

    private static volatile UserSession instance;

    /**
     * Private constructor — enforces Singleton.
     * Initializes all user fields to null (logged out state).
     */
    private UserSession() {
        this.currentRecruiter = null;
        this.currentCandidate = null;
        this.currentAdmin = null;
        this.currentRole = null;
        Logger.info("[UserSession] Initialized. No user logged in.");
    }

    /**
     * Get the singleton UserSession instance.
     * Synchronized to prevent race conditions in multi-threaded scenarios.
     *
     * @return The single UserSession instance
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // =========================================================
    // USER ROLE ENUM
    // =========================================================

    /**
     * Enumeration of possible user roles in the system.
     */
    public enum UserRole {
        RECRUITER("Recruiter"),
        CANDIDATE("Candidate"),
        ADMIN("Administrator");

        private final String displayName;

        UserRole(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // =========================================================
    // STATE
    // =========================================================

    private Recruiter currentRecruiter;
    private Candidate currentCandidate;
    private String currentAdmin;  // Can be extended to Admin class later
    private UserRole currentRole;

    // =========================================================
    // RECRUITER METHODS
    // =========================================================

    /**
     * Set the currently logged-in recruiter.
     *
     * @param recruiter The recruiter object (can be null to clear)
     */
    public void setCurrentRecruiter(Recruiter recruiter) {
        this.currentRecruiter = recruiter;
        if (recruiter != null) {
            Logger.log(LogLevel.INFO, "[UserSession] Recruiter logged in: " + recruiter.getDisplayName());
        }
    }

    /**
     * Get the currently logged-in recruiter.
     *
     * @return The recruiter object, or null if not logged in as recruiter
     */
    public Recruiter getCurrentRecruiter() {
        return this.currentRecruiter;
    }

    // =========================================================
    // CANDIDATE METHODS
    // =========================================================

    /**
     * Set the currently logged-in candidate.
     *
     * @param candidate The candidate object (can be null to clear)
     */
    public void setCurrentCandidate(Candidate candidate) {
        this.currentCandidate = candidate;
        if (candidate != null) {
            Logger.log(LogLevel.INFO, "[UserSession] Candidate logged in: " + candidate.getDisplayName());
        }
    }

    /**
     * Get the currently logged-in candidate.
     *
     * @return The candidate object, or null if not logged in as candidate
     */
    public Candidate getCurrentCandidate() {
        return this.currentCandidate;
    }

    // =========================================================
    // ADMIN METHODS
    // =========================================================

    /**
     * Set the currently logged-in admin.
     *
     * @param adminName The admin user name (can be null to clear)
     */
    public void setCurrentAdmin(String adminName) {
        this.currentAdmin = adminName;
        if (adminName != null) {
            Logger.log(LogLevel.INFO, "[UserSession] Admin logged in: " + adminName);
        }
    }

    /**
     * Get the currently logged-in admin name.
     *
     * @return The admin name, or null if not logged in as admin
     */
    public String getCurrentAdmin() {
        return this.currentAdmin;
    }

    // =========================================================
    // ROLE MANAGEMENT
    // =========================================================

    /**
     * Set the current user role.
     *
     * @param role The UserRole (RECRUITER, CANDIDATE, or ADMIN)
     */
    public void setCurrentRole(UserRole role) {
        this.currentRole = role;
        if (role != null) {
            Logger.log(LogLevel.INFO, "[UserSession] Role set to: " + role.getDisplayName());
        }
    }

    /**
     * Get the current user role.
     *
     * @return The UserRole, or null if no user logged in
     */
    public UserRole getCurrentRole() {
        return this.currentRole;
    }

    /**
     * Check if user is logged in as a recruiter.
     *
     * @return true if currentRole is RECRUITER and recruiter is not null
     */
    public boolean isRecruiterLoggedIn() {
        return currentRole == UserRole.RECRUITER && currentRecruiter != null;
    }

    /**
     * Check if user is logged in as a candidate.
     *
     * @return true if currentRole is CANDIDATE and candidate is not null
     */
    public boolean isCandidateLoggedIn() {
        return currentRole == UserRole.CANDIDATE && currentCandidate != null;
    }

    /**
     * Check if user is logged in as admin.
     *
     * @return true if currentRole is ADMIN and admin is not null
     */
    public boolean isAdminLoggedIn() {
        return currentRole == UserRole.ADMIN && currentAdmin != null;
    }

    // =========================================================
    // GENERAL SESSION METHODS
    // =========================================================

    /**
     * Check if ANY user is currently logged in.
     *
     * @return true if a role is set and appropriate user is not null
     */
    public boolean isLoggedIn() {
        switch (currentRole) {
            case RECRUITER:
                return currentRecruiter != null;
            case CANDIDATE:
                return currentCandidate != null;
            case ADMIN:
                return currentAdmin != null;
            default:
                return false;
        }
    }

    /**
     * Get the display name of the currently logged-in user.
     * Returns appropriate identifier based on role.
     *
     * @return User display name, or "(Not logged in)" if nobody is logged in
     */
    public String getCurrentUserName() {
        if (currentRole == null) {
            return "(Not logged in)";
        }

        switch (currentRole) {
            case RECRUITER:
                return currentRecruiter != null ? currentRecruiter.getDisplayName() : "(Recruiter not set)";
            case CANDIDATE:
                return currentCandidate != null ? currentCandidate.getDisplayName() : "(Candidate not set)";
            case ADMIN:
                return currentAdmin != null ? currentAdmin : "(Admin not set)";
            default:
                return "(Unknown user)";
        }
    }

    /**
     * Logout the current user — clears all session data.
     * Called when user clicks "Log Out" button or session expires.
     */
    public void logout() {
        String userName = getCurrentUserName();
        this.currentRecruiter = null;
        this.currentCandidate = null;
        this.currentAdmin = null;
        this.currentRole = null;
        Logger.log(LogLevel.INFO, "[UserSession] User logged out: " + userName);
    }

    /**
     * Get session summary for debugging.
     * Returns a string describing the current session state.
     *
     * @return String representation of current session
     */
    @Override
    public String toString() {
        if (!isLoggedIn()) {
            return "[UserSession] Status: No user logged in";
        }

        return String.format("[UserSession] Role: %s | User: %s",
                currentRole != null ? currentRole.getDisplayName() : "None",
                getCurrentUserName());
    }
}
