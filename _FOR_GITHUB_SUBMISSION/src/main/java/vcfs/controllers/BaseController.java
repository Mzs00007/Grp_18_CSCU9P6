package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * BaseController - Shared helper methods for controller refactoring
 * 
 * PURPOSE:
 *   Eliminate 70+ lines of duplicated validation/logging code across
 *   CandidateController and RecruiterController by centralizing common patterns.
 * 
 * METHODS PROVIDED:
 *   - validateLoggedIn(Object, String, String): Check if user is logged in
 *   - validateNotNull(Object, String): Check if object is not null
 *   - validateNotEmpty(String, String): Check if string is not empty
 *   - safeTrim(String): Safely trim a string, returning empty string if null
 *   - getUserName(Object): Get display name from user object safely
 *   - logOperation(LogLevel, String, String): Log standard operations
 *   - logError(String, String, Exception): Log errors with exceptions
 * 
 * REFACTORING BENEFIT: ~150 lines of duplicated validation/logging reduced
 *   to clean, single method calls in both controller classes.
 */

import vcfs.core.Logger;
import vcfs.core.LogLevel;

public abstract class BaseController {

    /**
     * Validate that a user object is not null (i.e., someone is logged in).
     * 
     * @param user The user object to check (Candidate, Recruiter, Admin)
     * @param userType Name of the user type for logging (e.g., "Recruiter")
     * @param operation Name of the operation being attempted for logging
     * @return true if user is logged in, false otherwise
     */
    protected boolean validateLoggedIn(Object user, String userType, String operation) {
        if (user == null) {
            Logger.log(LogLevel.WARNING, "[" + userType + "Controller] " + operation + " attempted with no " + userType.toLowerCase() + " logged in");
            return false;
        }
        return true;
    }

    /**
     * Validate that an object is not null.
     * 
     * @param obj The object to check
     * @param fieldName Name of the field for error messages
     * @return true if object is not null, false otherwise
     */
    protected boolean validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            Logger.log(LogLevel.WARNING, fieldName + " cannot be null");
            return false;
        }
        return true;
    }

    /**
     * Validate that a string is not empty or null.
     * 
     * @param str The string to check
     * @param fieldName Name of the field for error messages
     * @return true if string is not empty, false otherwise
     */
    protected boolean validateNotEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            Logger.log(LogLevel.WARNING, fieldName + " cannot be empty or null");
            return false;
        }
        return true;
    }

    /**
     * Safely trim a string, returning empty string if input is null.
     * 
     * @param str The string to trim
     * @return Trimmed string, or empty string if null
     */
    protected String safeTrim(String str) {
        return (str == null) ? "" : str.trim();
    }

    /**
     * Get the display name of a user object.
     * Uses reflection to call getDisplayName() if available.
     * 
     * @param user The user object (Candidate, Recruiter, Admin)
     * @return Display name, or "UNKNOWN" if not available
     */
    protected String getUserName(Object user) {
        if (user == null) {
            return "UNKNOWN";
        }
        
        try {
            // Try to call getDisplayName() via reflection
            java.lang.reflect.Method method = user.getClass().getMethod("getDisplayName");
            Object result = method.invoke(user);
            return (result != null) ? result.toString() : "UNKNOWN";
        } catch (Exception e) {
            // If reflection fails, try getName()
            try {
                java.lang.reflect.Method method = user.getClass().getMethod("getName");
                Object result = method.invoke(user);
                return (result != null) ? result.toString() : "UNKNOWN";
            } catch (Exception e2) {
                return "UNKNOWN";
            }
        }
    }

    /**
     * Log a standard operation at the specified log level.
     * 
     * @param level The log level (INFO, WARNING, ERROR)
     * @param sourceClass The controller class name
     * @param message The message to log
     */
    protected void logOperation(LogLevel level, String sourceClass, String message) {
        Logger.log(level, "[" + sourceClass + "] " + message);
    }

    /**
     * Log an error with exception details.
     * 
     * @param sourceClass The controller class name
     * @param message The error message
     * @param exception The exception that occurred
     */
    protected void logError(String sourceClass, String message, Exception exception) {
        Logger.log(LogLevel.ERROR, "[" + sourceClass + "] " + message, exception);
    }
}
