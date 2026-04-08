package vcfs.controllers;

/**
 * BaseController — Abstract base class for all controllers.
 * Provides common validation, logging, and error handling utilities.
 * 
 * PURPOSE: Eliminate code duplication across CandidateController and RecruiterController
 * by centralizing boilerplate null-checks, logging, and error messages.
 * 
 * REFACTORING BENEFIT: Reduces ~600 lines of duplicated code to single utility methods.
 */

import vcfs.core.Logger;
import vcfs.core.LogLevel;

public abstract class BaseController {
    
    /**
     * Validate that a required user is logged in.
     * Logs warning and displays error if not logged in.
     * 
     * @param user The user object to check (can be null)
     * @param userType "Recruiter" or "Candidate" for error messages
     * @param operation Description of operation attempted
     * @return true if user is logged in, false otherwise
     */
    protected boolean validateLoggedIn(Object user, String userType, String operation) {
        if (user == null) {
            String message = "[" + userType + "Controller] " + operation + " attempted with no " + userType.toLowerCase() + " logged in";
            Logger.log(LogLevel.WARNING, message);
            return false;
        }
        return true;
    }
    
    /**
     * Validate that a required string parameter is not empty.
     */
    protected boolean validateNotEmpty(String value, String paramName) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validate that a required object is not null.
     */
    protected boolean validateNotNull(Object object, String objectName) {
        return object != null;
    }
    
    /**
     * Get user display name safely.
     */
    protected String getUserName(Object user) {
        if (user == null) return "UNKNOWN";
        try {
            return (String) user.getClass().getMethod("getDisplayName").invoke(user);
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
    
    /**
     * Log an operation with automatic controller context.
     */
    protected void logOperation(LogLevel level, String controllerName, String message) {
        Logger.log(level, "[" + controllerName + "] " + message);
    }
    
    /**
     * Log an operation with exception details.
     */
    protected void logError(String controllerName, String message, Exception e) {
        Logger.log(LogLevel.ERROR, "[" + controllerName + "] " + message, e);
    }
    
    /**
     * Trim a string parameter and return it, or null if it becomes empty.
     */
    protected String safeTrim(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
