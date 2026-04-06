package vcfs.controllers;

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

    /**
     * UI Convenience Methods for AdminScreen
     */

    public void createOrganization(String name) {
        System.out.println("[AdminController] Creating organization: " + name);
    }

    public void createBooth(String name, String orgName) {
        System.out.println("[AdminController] Creating booth: " + name + " in org: " + orgName);
    }

    public void assignRecruiter(String recruiterName, String boothName) {
        System.out.println("[AdminController] Assigning recruiter: " + recruiterName + " to booth: " + boothName);
    }

    public void setTimeline(String openStr, String closeStr, String startStr, String endStr) {
        System.out.println("[AdminController] Setting timeline: open=" + openStr + ", close=" + closeStr + ", start=" + startStr + ", end=" + endStr);
    }
}
