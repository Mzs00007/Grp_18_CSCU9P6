package vcfs.views.admin;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager & Lead Developer)
 */

import java.util.List;
import vcfs.models.structure.Organization;
import vcfs.models.structure.Booth;
import vcfs.models.users.Recruiter;
import vcfs.core.LocalDateTime;

public interface AdminView {
    void displayError(String message);
    void displayMessage(String message);
    void displayOrganizations(List<Organization> organizations);
    void displayBooths(List<Booth> booths);
    void displayRecruiters(List<Recruiter> recruiters);
    void updateTimeline(LocalDateTime bookingOpen, LocalDateTime bookingClose, 
                       LocalDateTime fairStart, LocalDateTime fairEnd);
    void refreshAuditLog(String auditContent);
}
