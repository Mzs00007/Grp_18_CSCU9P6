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
            // Note: View displayError() is called by the concrete controller
            return false;
        }
        return true;
    }
    
    /**
     * Validate that a required string parameter is not empty.
     * @param value The string to validate
     * @param paramName Parameter name for error messages
     * @return true if valid (not null and not empty after trim), false otherwise
     */
    protected boolean validateNotEmpty(String value, String paramName) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validate that a required object is not null.
     * @param object The object to validate
     * @param objectName Name of object for error messages
     * @return true if object is not null, false otherwise
     */
    protected boolean validateNotNull(Object object, String objectName) {
        return object != null;
    }
    
    /**
     * Get user display name safely.
     * @param user The user object (can be null)
     * @return Display name if available, or "UNKNOWN"
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
     * @param level Log level (INFO, WARNING, ERROR)
     * @param controllerName "RecruiterController" or "CandidateController"
     * @param message Operation message
     */
    protected void logOperation(LogLevel level, String controllerName, String message) {
        Logger.log(level, "[" + controllerName + "] " + message);
    }
    
    /**
     * Log an operation with exception details.
     * @param controllerName "RecruiterController" or "CandidateController"
     * @param message Operation message
     * @param e Exception to log
     */
    protected void logError(String controllerName, String message, Exception e) {
        Logger.log(LogLevel.ERROR, "[" + controllerName + "] " + message, e);
    }
    
    /**
     * Trim a string parameter and return it, or null if it becomes empty.
     * @param value The string to trim
     * @return Trimmed string, or null if empty after trimming
     */
    protected String safeTrim(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
