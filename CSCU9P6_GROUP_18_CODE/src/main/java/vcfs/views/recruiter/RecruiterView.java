package vcfs.views.recruiter;


import java.util.List;
import vcfs.models.booking.MeetingSession;

public interface RecruiterView {

    void displayError(String message);
    void displayMessage(String message);
    void displaySessions(List<MeetingSession> sessions);
    void displayOffers(List<Object> offers);
    void displayRequests(List<Object> requests);
}


