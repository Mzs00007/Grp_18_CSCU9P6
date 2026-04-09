package vcfs.views.candidate;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.util.List;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Request;
import vcfs.models.booking.Lobby;

public interface CandidateView {
    void displayError(String message);;
    void displayMessage(String message);
    void displayLobbies(List<Lobby> lobbies);
    void displayLobbyDetails(Lobby lobby);
    void displaySchedule(List<MeetingSession> schedule);
    void displayRequestHistory(List<Request> requests);
    void displaySessions(List<MeetingSession> sessions);
}


