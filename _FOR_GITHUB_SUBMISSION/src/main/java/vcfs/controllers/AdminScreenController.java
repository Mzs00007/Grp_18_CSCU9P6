package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.core.*;
import vcfs.core.SystemStateManager;
import vcfs.core.SessionManager;
import vcfs.models.users.Recruiter;
import vcfs.models.structure.Organization;
import vcfs.models.structure.Booth;

/**
 * Admin Controller for managing fair setup and administration.
 * Handles all admin UI actions and delegates to CareerFairSystem for business logic.
 * 
 * CODE AUDIT: ZAID (mzs00007) — Input validation and error handling
 * - Null checks on all string parameters before use
 * - Empty string validation after trim()
 * - OrganizationNotFound checks before booth operations
 * - Comprehensive logging at WARNING/ERROR levels
 */
public class AdminScreenController {

    /**
     * Create a new organization and add it to the system.
     * @param name Organization name (cannot be empty)
     * @throws IllegalArgumentException if name is null or empty
     */
    public void createOrganization(String name) {
        if (name == null || (name = name.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[AdminScreenController] Create organization attempted with empty name");
            throw new IllegalArgumentException("Organization name cannot be empty");
        }
        
        try {
            // Add organization to the system - CareerFairSystem handles creation
            CareerFairSystem system = CareerFairSystem.getInstance();
            system.addOrganization(name);
            
            // Log the action
            Logger.log(LogLevel.INFO, "[AdminScreenController] Organization created successfully: " + name);
            
            // RECORD OPERATION: Track organization creation
            SystemStateManager.getInstance().recordStateChange("ORG_CREATED",
                "Organization created: " + name, true);
            
            // RECORD SESSION ACTIVITY: For admin activity tracking
            SessionManager.getInstance().recordActivity("Admin", "Administrator",
                "ORG_CREATED", "Created organization: " + name);
                
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to create organization: " + name, e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("ORG_CREATE_FAILED",
                "Organization creation failed: " + name + " - " + e.getMessage(), false);
                
            throw e;
        }
    }

    /**
     * Create a new booth and add it to an organization.
     * @param boothName Booth name (cannot be empty)
     * @param orgName Organization name (must exist in system)
     * @throws IllegalArgumentException if parameters are invalid
     */
    public void createBooth(String boothName, String orgName) {
        if (boothName == null || (boothName = boothName.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[AdminScreenController] Create booth attempted with empty booth name");
            throw new IllegalArgumentException("Booth name cannot be empty");
        }
        if (orgName == null || (orgName = orgName.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[AdminScreenController] Create booth attempted with empty organization name");
            throw new IllegalArgumentException("Organization name cannot be empty");
        }
        
        try {
            // Get the system and organization
            CareerFairSystem system = CareerFairSystem.getInstance();
            Organization org = system.getOrganizationByName(orgName);
            
            if (org == null) {
                Logger.log(LogLevel.WARNING, "[AdminScreenController] Organization not found: " + orgName);
                throw new IllegalArgumentException("Organization not found: " + orgName);
            }
            
            // Create and add the booth
            Booth booth = new Booth(boothName);
            org.addBooth(booth);
            
            // Log the action
            Logger.log(LogLevel.INFO, "[AdminScreenController] Booth created successfully: " + boothName + " in " + orgName);
            
            // RECORD OPERATION: Track booth creation
            SystemStateManager.getInstance().recordStateChange("BOOTH_CREATED",
                "Booth created: " + boothName + " in organization: " + orgName, true);
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity("Admin", "Administrator",
                "BOOTH_CREATED", "Created booth: " + boothName + " under " + orgName);
                
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to create booth: " + boothName, e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("BOOTH_CREATE_FAILED",
                "Booth creation failed: " + boothName + " - " + e.getMessage(), false);
                
            throw e;
        }
    }

    /**
     * Assign a recruiter to a booth.
     * @param recruiterName Recruiter name (cannot be empty)
     * @param boothName Booth name (must exist in system)
     * @throws IllegalArgumentException if parameters are invalid
     */
    public void assignRecruiter(String recruiterName, String boothName) {
        if (recruiterName == null || (recruiterName = recruiterName.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[AdminScreenController] Assign recruiter attempted with empty recruiter name");
            throw new IllegalArgumentException("Recruiter name cannot be empty");
        }
        if (boothName == null || (boothName = boothName.trim()).isEmpty()) {
            Logger.log(LogLevel.WARNING, "[AdminScreenController] Assign recruiter attempted with empty booth name");
            throw new IllegalArgumentException("Booth name cannot be empty");
        }
        
        try {
            // Get the system and booth
            CareerFairSystem system = CareerFairSystem.getInstance();
            Booth booth = system.getBoothByName(boothName);
            
            if (booth == null) {
                Logger.log(LogLevel.WARNING, "[AdminScreenController] Booth not found: " + boothName);
                throw new IllegalArgumentException("Booth not found: " + boothName);
            }
            
            // Generate unique recruiter ID (R + timestamp + random) for global uniqueness
            String recruiterId = "R" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
            
            // Generate unique email using the unique recruiter ID (not just name, which could collide)
            String email = recruiterId.toLowerCase() + "@company.com";
            Recruiter recruiter = new Recruiter(recruiterId, recruiterName, email);
            
            // Assign to booth
            booth.assignRecruiter(recruiter);
            
            // Log the action
            Logger.log(LogLevel.INFO, "[AdminScreenController] Recruiter assigned successfully: " + recruiterName + " to booth " + boothName);
            
            // RECORD OPERATION: Track recruiter assignments
            SystemStateManager.getInstance().recordStateChange("RECRUITER_ASSIGNED",
                "Recruiter assigned: " + recruiterName + " to booth: " + boothName, true);
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity("Admin", "Administrator",
                "RECRUITER_ASSIGNED", "Assigned recruiter: " + recruiterName + " to booth: " + boothName);
                
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to assign recruiter: " + recruiterName, e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("RECRUITER_ASSIGN_FAILED",
                "Recruiter assignment failed: " + recruiterName + " - " + e.getMessage(), false);
                
            throw e;
        }
    }

    /**
     * Configure the fair timeline (when bookings open/close, when fair starts/ends).
     * @param openStr Bookings open time (format: "yyyy-MM-ddTHH:mm")
     * @param closeStr Bookings close time (format: "yyyy-MM-ddTHH:mm")
     * @param startStr Fair start time (format: "yyyy-MM-ddTHH:mm")
     * @param endStr Fair end time (format: "yyyy-MM-ddTHH:mm")
     * @throws IllegalArgumentException if parameters are invalid
     */
    public void setTimeline(String openStr, String closeStr, String startStr, String endStr) {
        if (openStr == null || closeStr == null || startStr == null || endStr == null) {
            Logger.log(LogLevel.WARNING, "[AdminScreenController] Set timeline attempted with null values");
            throw new IllegalArgumentException("All timeline values are required");
        }
        
        try {
            // Get the system and set timeline
            CareerFairSystem system = CareerFairSystem.getInstance();
            system.setFairTimes(openStr, closeStr, startStr, endStr);
            
            // Log the action
            Logger.log(LogLevel.INFO, "[AdminScreenController] Fair timeline configured successfully: open=" + openStr 
                + ", close=" + closeStr 
                + ", start=" + startStr 
                + ", end=" + endStr);
            
            // RECORD OPERATION: Track timeline configuration
            SystemStateManager.getInstance().recordStateChange("TIMELINE_SET",
                "Fair timeline configured - Open: " + openStr + ", Close: " + closeStr, true);
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity("Admin", "Administrator",
                "TIMELINE_CONFIGURED", "Set fair schedule times");
                
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to set fair timeline", e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("TIMELINE_SET_FAILED",
                "Timeline configuration failed: " + e.getMessage(), false);
                
            throw e;
        }
    }

    /**
     * Admin: Reset the system data
     */
    public void resetFair() {
        try {
            CareerFairSystem.getInstance().resetFairData();
            
            // RECORD OPERATION: Track fair reset
            SystemStateManager.getInstance().recordStateChange("FAIR_RESET",
                "System data reset by administrator", true);
            
            // RECORD SESSION ACTIVITY
            SessionManager.getInstance().recordActivity("Admin", "Administrator",
                "FAIR_RESET", "Reset all fair data");
            
            Logger.log(LogLevel.INFO, "[AdminScreenController] Fair data reset successfully");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to reset fair data", e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("FAIR_RESET_FAILED",
                "Fair reset failed: " + e.getMessage(), false);
                
            throw e;
        }
    }
}


