package vcfs.controllers;

import vcfs.core.CareerFairSystem;
import vcfs.core.LocalDateTime;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.structure.Organization;
import vcfs.models.structure.Booth;
import vcfs.models.users.Recruiter;

/**
 * MVC Controller for all Administrator actions.
 *
 * Assigned to: YAMI
 * Tickets: VCFS-005, VCFS-006, VCFS-007, VCFS-008
 *
 * Bridges the AdminScreen (View) to system-level
 * operations: fair setup, time boundaries, and data lifecycle.
 */
public class AdminController {

	private final CareerFairSystem system;

	/**
	 * Constructor accepting CareerFairSystem dependency
	 * @param system The system facade
	 */
	public AdminController(CareerFairSystem system) {
		if (system == null) {
			throw new IllegalArgumentException("CareerFairSystem cannot be null");
		}
		this.system = system;
	}

	/**
	 * UI Convenience Methods for AdminScreen
	 */

	/**
	 * Create a new organization in the fair
	 * @param name The organization name
	 * @return The created Organization, or null on failure
	 */
	public Organization createOrganization(String name) {
		if (name == null || name.trim().isEmpty()) {
			Logger.log(LogLevel.ERROR, "Cannot create organization with empty name");
			return null;
		}
		try {
			Organization org = system.addOrganization(name);
			Logger.log(LogLevel.INFO, "[AdminController] Organization created: " + name);
			return org;
		} catch (Exception e) {
			Logger.log(LogLevel.ERROR, "[AdminController] Failed to create organization: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Create a booth within an organization
	 * @param boothName The booth name
	 * @param orgName The organization name
	 * @return The created Booth, or null on failure
	 */
	public Booth createBooth(String boothName, String orgName) {
		if (boothName == null || boothName.trim().isEmpty()) {
			Logger.log(LogLevel.ERROR, "Cannot create booth with empty name");
			return null;
		}
		if (orgName == null || orgName.trim().isEmpty()) {
			Logger.log(LogLevel.ERROR, "Cannot create booth in organization with empty name");
			return null;
		}

		try {
			// Find the organization
			Organization org = null;
			for (Organization o : system.getFair().getOrganizations()) {
				if (o.getName().equalsIgnoreCase(orgName)) {
					org = o;
					break;
				}
			}

			if (org == null) {
				Logger.log(LogLevel.ERROR, "Organization not found: " + orgName);
				return null;
			}

			Booth booth = system.addBooth(org, boothName);
			Logger.log(LogLevel.INFO, "[AdminController] Booth created: " + boothName + " in " + orgName);
			return booth;
		} catch (Exception e) {
			Logger.log(LogLevel.ERROR, "[AdminController] Failed to create booth: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Assign a recruiter to a booth
	 * @param recruiterName The recruiter display name
	 * @param recruiterEmail The recruiter email
	 * @param boothName The booth name
	 * @return The assigned Recruiter, or null on failure
	 */
	public Recruiter assignRecruiter(String recruiterName, String recruiterEmail, String boothName) {
		if (recruiterName == null || recruiterName.trim().isEmpty()) {
			Logger.log(LogLevel.ERROR, "Cannot assign recruiter with empty name");
			return null;
		}
		if (recruiterEmail == null || recruiterEmail.trim().isEmpty()) {
			Logger.log(LogLevel.ERROR, "Cannot assign recruiter with empty email");
			return null;
		}
		if (boothName == null || boothName.trim().isEmpty()) {
			Logger.log(LogLevel.ERROR, "Cannot assign recruiter to booth with empty name");
			return null;
		}

		try {
			// Find the booth
			Booth booth = null;
			for (Organization org : system.getFair().getOrganizations()) {
				for (Booth b : org.getBooths()) {
					if (b.getTitle().equalsIgnoreCase(boothName)) {
						booth = b;
						break;
					}
				}
				if (booth != null) break;
			}

			if (booth == null) {
				Logger.log(LogLevel.ERROR, "Booth not found: " + boothName);
				return null;
			}

			Recruiter recruiter = system.registerRecruiter(recruiterName, recruiterEmail, booth);
			Logger.log(LogLevel.INFO, "[AdminController] Recruiter assigned: " + recruiterName + " to booth: " + boothName);
			return recruiter;
		} catch (Exception e) {
			Logger.log(LogLevel.ERROR, "[AdminController] Failed to assign recruiter: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Set the fair timeline with opening/closing and start/end times
	 * @param openStr Opening time string (format: "HH:mm")
	 * @param closeStr Closing time string (format: "HH:mm")
	 * @param startStr Fair start time string (format: "HH:mm")
	 * @param endStr Fair end time string (format: "HH:mm")
	 */
	public void setTimeline(String openStr, String closeStr, String startStr, String endStr) {
		if (openStr == null || closeStr == null || startStr == null || endStr == null) {
			Logger.log(LogLevel.ERROR, "All timeline parameters must be provided");
			return;
		}

		try {
			LocalDateTime openTime = LocalDateTime.parse(openStr);
			LocalDateTime closeTime = LocalDateTime.parse(closeStr);
			LocalDateTime startTime = LocalDateTime.parse(startStr);
			LocalDateTime endTime = LocalDateTime.parse(endStr);

			system.getFair().setTimes(openTime, closeTime, startTime, endTime);
			Logger.log(LogLevel.INFO, "[AdminController] Timeline configured: open=" + openStr 
				+ ", close=" + closeStr + ", start=" + startStr + ", end=" + endStr);
		} catch (Exception e) {
			Logger.log(LogLevel.ERROR, "[AdminController] Failed to set timeline: " + e.getMessage());
		}
	}
}

