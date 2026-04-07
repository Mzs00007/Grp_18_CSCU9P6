package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.util.List;
import vcfs.models.booking.MeetingSession;

public interface RecruiterView {
    void displayError(String message);
    void displayMessage(String message);
    void displaySessions(List<MeetingSession> sessions);
}


