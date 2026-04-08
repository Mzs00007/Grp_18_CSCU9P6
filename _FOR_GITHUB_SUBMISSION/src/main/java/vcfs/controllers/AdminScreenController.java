package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.core.*;
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
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to create organization: " + name, e);
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
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to create booth: " + boothName, e);
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
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to assign recruiter: " + recruiterName, e);
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
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreenController] Failed to set fair timeline", e);
            throw e;
        }
    }

    /**
     * Admin: Reset the system data
     */
    public void resetFair() {
        CareerFairSystem.getInstance().resetFairData();
    }
}


