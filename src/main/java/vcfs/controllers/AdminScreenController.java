package vcfs.controllers;

/**
 * Admin Controller for managing fair setup and administration
 */
public class AdminScreenController {

    public void createOrganization(String name) {
        System.out.println("[AdminScreenController] Creating organization: " + name);
    }

    public void createBooth(String name, String orgName) {
        System.out.println("[AdminScreenController] Creating booth: " + name + " in org: " + orgName);
    }

    public void assignRecruiter(String recruiterName, String boothName) {
        System.out.println("[AdminScreenController] Assigning recruiter: " + recruiterName + " to booth: " + boothName);
    }

    public void setTimeline(String openStr, String closeStr, String startStr, String endStr) {
        System.out.println("[AdminScreenController] Setting timeline: open=" + openStr + ", close=" + closeStr + ", start=" + startStr + ", end=" + endStr);
    }
}
