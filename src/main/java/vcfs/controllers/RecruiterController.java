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
import vcfs.models.booking.Offer;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Lobby;
import vcfs.models.users.Recruiter;
import vcfs.views.recruiter.RecruiterView;

public class RecruiterController {
    private final RecruiterView view;
    private final CareerFairSystem system;
    private Recruiter currentRecruiter;

    public RecruiterController(RecruiterView view, CareerFairSystem system) {
        this.view = view;
        this.system = system;
    }

    public void setCurrentRecruiter(Recruiter recruiter) {
        this.currentRecruiter = recruiter;
    }

    public void publishOffer(Offer offer) {
        if (currentRecruiter == null) {
            view.displayError("No recruiter logged in.");
            return;
        }
        
        if (offer == null) {
            view.displayError("Offer cannot be null.");
            return;
        }
        
        currentRecruiter.publishOffer(offer);
        view.displayMessage("Offer published successfully: " + offer.getTitle());
    }

    public void scheduleSession(MeetingSession session) {
        if (currentRecruiter == null) {
            view.displayError("No recruiter logged in.");
            return;
        }
        
        if (session == null) {
            view.displayError("Meeting session cannot be null.");
            return;
        }
        
        currentRecruiter.scheduleSession(session);
        view.displayMessage("Meeting session scheduled: " + session.getTitle());
    }

    public void viewLobbySessions(String lobbyId) {
        if (currentRecruiter == null) {
            view.displayError("No recruiter logged in.");
            return;
        }
        
        Optional<Lobby> lobby = system.getLobby(lobbyId);
        if (lobby.isEmpty()) {
            view.displayError("Lobby not found: " + lobbyId);
            return;
        }
        
        List<MeetingSession> sessions = lobby.get().getMeetingSessions();
        view.displaySessions(sessions);
    }

    public void viewMeetingHistory() {
        if (currentRecruiter == null) {
            view.displayError("No recruiter logged in.");
            return;
        }
        
        List<MeetingSession> history = currentRecruiter.getMeetingHistory();
        view.displaySessions(history);
    }

    public void updateOfferStatus(String offerId, String status) {
        if (currentRecruiter == null) {
            view.displayError("No recruiter logged in.");
            return;
        }
        
        if (offerId == null || offerId.isEmpty()) {
            view.displayError("Offer ID cannot be empty.");
            return;
        }
        
        currentRecruiter.updateOfferStatus(offerId, status);
        view.displayMessage("Offer status updated: " + status);
    }

    public void cancelSession(String sessionId) {
        if (currentRecruiter == null) {
            view.displayError("No recruiter logged in.");
            return;
        }
        
        if (sessionId == null || sessionId.isEmpty()) {
            view.displayError("Session ID cannot be empty.");
            return;
        }
        
        currentRecruiter.cancelSession(sessionId);
        view.displayMessage("Session cancelled: " + sessionId);
    }

    public List<Offer> getPublishedOffers() {
        if (currentRecruiter == null) {
            return new ArrayList<>();
        }
        
        return currentRecruiter.getPublishedOffers();
    }

    public Recruiter getCurrentRecruiter() {
        return currentRecruiter;
    }
}


