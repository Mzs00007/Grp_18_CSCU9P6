package vcfs.controllers;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vcfs.core.CareerFairSystem;
import vcfs.models.booking.Request;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Lobby;
import vcfs.models.users.Candidate;
import vcfs.views.candidate.CandidateView;

public class CandidateController {
    private final CandidateView view;
    private final CareerFairSystem system;
    private Candidate currentCandidate;

    public CandidateController(CandidateView view, CareerFairSystem system) {
        this.view = view;
        this.system = system;
    }

    public void setCurrentCandidate(Candidate candidate) {
        this.currentCandidate = candidate;
    }

    public void submitMeetingRequest(Request request) {
        if (currentCandidate == null) {
            view.displayError("No candidate logged in.");
            return;
        }
        
        if (request == null) {
            view.displayError("Request cannot be null.");
            return;
        }
        
        currentCandidate.submitRequest(request);
        view.displayMessage("Meeting request submitted successfully.");
    }

    public void viewAvailableLobbies() {
        List<Lobby> lobbies = system.getAllLobbies();
        view.displayLobbies(lobbies);
    }

    public void viewLobbyInfo(String lobbyId) {
        if (lobbyId == null || lobbyId.isEmpty()) {
            view.displayError("Lobby ID cannot be empty.");
            return;
        }
        
        Optional<Lobby> lobby = system.getLobby(lobbyId);
        if (lobby.isEmpty()) {
            view.displayError("Lobby not found: " + lobbyId);
            return;
        }
        
        view.displayLobbyDetails(lobby.get());
    }

    public void viewMeetingSchedule() {
        if (currentCandidate == null) {
            view.displayError("No candidate logged in.");
            return;
        }
        
        List<MeetingSession> schedule = currentCandidate.getMeetingSchedule();
        view.displaySchedule(schedule);
    }

    public void cancelMeetingRequest(String requestId) {
        if (currentCandidate == null) {
            view.displayError("No candidate logged in.");
            return;
        }
        
        if (requestId == null || requestId.isEmpty()) {
            view.displayError("Request ID cannot be empty.");
            return;
        }
        
        currentCandidate.cancelRequest(requestId);
        view.displayMessage("Meeting request cancelled: " + requestId);
    }

    public void viewRequestHistory() {
        if (currentCandidate == null) {
            view.displayError("No candidate logged in.");
            return;
        }
        
        List<Request> history = currentCandidate.getRequestHistory();
        view.displayRequestHistory(history);
    }

    public void updateProfile(String phone, String email) {
        if (currentCandidate == null) {
            view.displayError("No candidate logged in.");
            return;
        }
        
        if (phone == null || phone.isEmpty() || email == null || email.isEmpty()) {
            view.displayError("Phone and email cannot be empty.");
            return;
        }
        
        currentCandidate.setPhoneNumber(phone);
        currentCandidate.setEmail(email);
        view.displayMessage("Profile updated successfully.");
    }

    public List<MeetingSession> getAvailableSessions(String lobbyId) {
        if (lobbyId == null || lobbyId.isEmpty()) {
            return new ArrayList<>();
        }
        
        Optional<Lobby> lobby = system.getLobby(lobbyId);
        if (lobby.isEmpty()) {
            return new ArrayList<>();
        }
        
        return lobby.get().getAvailableSessions();
    }

    public Candidate getCurrentCandidate() {
        return currentCandidate;
    }
}


