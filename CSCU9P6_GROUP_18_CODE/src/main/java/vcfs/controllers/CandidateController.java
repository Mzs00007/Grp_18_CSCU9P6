package vcfs.controllers;

/**
 * CandidateController — Handles all candidate-related business logic.
 * REFACTORED to eliminate code duplication by extending BaseController.
 * 
 * RESPONSIBILITIES:
 *   - Submit meeting requests to recruiters
 *   - View available recruitment lobbies and sessions
 *   - Manage personal meeting schedule
 *   - Cancel meeting requests when needed
 *   - Update profile information (phone, email)
 *   - View request history for reference
 * 
 * KEY FEATURES:
 *   ✓ Uses BaseController helper methods to eliminate duplicated validation/logging
 *   ✓ Input validation (null checks via validateNotEmpty, validateNotNull)
 *   ✓ Comprehensive logging via logOperation/logError helpers
 *   ✓ Exception handling with try-catch blocks
 *   ✓ User-friendly error messages displayed via view layer
 *   ✓ Success feedback with checkmarks (✓)
 * 
 * REFACTORING REDUCTION: ~80 lines of duplicated code reduced to single method calls
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.CareerFairSystem;
import vcfs.core.SystemStateManager;
import vcfs.core.SessionManager;
import vcfs.models.booking.Request;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Lobby;
import vcfs.models.booking.Offer;
import vcfs.models.booking.Reservation;
import vcfs.models.users.Candidate;
import vcfs.views.candidate.CandidateView;

public class CandidateController extends BaseController {
    private final CandidateView view;
    private Candidate currentCandidate;

    /**
     * Constructor — Initialize controller with view reference.
     * 
     * @param view CandidateView instance for displaying results/errors
     * @throws IllegalArgumentException if view is null
     */
    public CandidateController(CandidateView view) {
        if (view == null) {
            throw new IllegalArgumentException("CandidateView cannot be null");
        }
        this.view = view;
    }

    /**
     * Set the currently active candidate for this session.
     * Called during login after authentication succeeds.
     */
    public void setCurrentCandidate(Candidate candidate) {
        this.currentCandidate = candidate;
    }

    public void submitMeetingRequest(Request request) {
        if (!validateLoggedIn(currentCandidate, "Candidate", "Submit meeting request")) {
            view.displayError("Error: No candidate logged in. Please log in to submit requests.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        if (!validateNotNull(request, "Request")) {
            logOperation(LogLevel.WARNING, "CandidateController", "Submit meeting request attempted with null request by candidate: " + candidateName);
            view.displayError("Error: Meeting request data is missing. Please fill all required fields.");
            return;
        }
        
        try {
            currentCandidate.submitRequest(request);
            logOperation(LogLevel.INFO, "CandidateController", "Meeting request submitted by " + candidateName);
            
            // CONSISTENCY FIX P4: Notify system so it registers in system structure
            // This ensures booking appears in all candidate portals immediately
            CareerFairSystem.getInstance().registerBooking(null, currentCandidate.getEmail(), 
                (request != null ? request.getDesiredTags() : "unknown request"));
            
            // RECORD OPERATION: Track this booking in system state manager
            SystemStateManager stateManager = SystemStateManager.getInstance();
            stateManager.recordStateChange("CANDIDATE_REQUEST", 
                candidateName + " submitted request for interview", true);
            
            // RECORD SESSION ACTIVITY: Track in session manager for live monitoring
            SessionManager sessionManager = SessionManager.getInstance();
            sessionManager.recordActivity(candidateName, "Candidate", "REQUEST_SUBMITTED", 
                "Submitted interview request - active booking");
            
            view.displayMessage("✓ Your meeting request has been submitted successfully!");
        } catch (Exception e) {
            logError("CandidateController", "Failed to submit meeting request by " + candidateName, e);
            
            // RECORD FAILURE: Track failed operation
            SystemStateManager.getInstance().recordStateChange("CANDIDATE_REQUEST_FAILED", 
                candidateName + " request submission failed: " + e.getMessage(), false);
            
            view.displayError("Error submitting request: " + e.getMessage());
        }
    }

    public void submitAutoBookRequest(String desiredTags, int maxAppointments) {
        if (!validateNotEmpty(desiredTags, "Desired tags")) {
            view.displayError("Error: Desired tags cannot be empty.");
            return;
        }
        if (maxAppointments <= 0) {
            view.displayError("Error: Max appointments must be greater than 0.");
            return;
        }
        
        if (!validateLoggedIn(currentCandidate, "Candidate", "Auto-book interview")) {
            view.displayError("Error: No candidate logged in. Please log in to book interviews.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        String[] tags = desiredTags.trim().toLowerCase().split(",");
        
        try {
            // CRITICAL FIX P3: Actually book matching offers instead of just creating a request
            CareerFairSystem system = CareerFairSystem.getInstance();
            List<Offer> allOffers = system.getAllOffers();
            
            int booked = 0;
            int maxToBook = maxAppointments;
            
            logOperation(LogLevel.INFO, "CandidateController", 
                "Auto-booking started for " + candidateName + " - tags: " + desiredTags + ", max: " + maxToBook);
            
            // Search for matching offers
            for (Offer offer : allOffers) {
                if (booked >= maxToBook) break;
                
                // Check if offer matches tags
                String offerTags = (offer.getTopicTags() != null) ? offer.getTopicTags().toLowerCase() : "";
                boolean matches = false;
                
                for (String tag : tags) {
                    tag = tag.trim();
                    if (!tag.isEmpty() && offerTags.contains(tag)) {
                        matches = true;
                        break;
                    }
                }
                
                // Check if offer has capacity
                int offeredBooked = (offer.getReservations() != null) ? offer.getReservations().size() : 0;
                int available = offer.getCapacity() - offeredBooked;
                
                if (matches && available > 0) {
                    // Create reservation
                    Reservation reservation = new Reservation();
                    reservation.setCandidate(currentCandidate);
                    reservation.setOffer(offer);
                    reservation.setStatus("CONFIRMED");
                    
                    // Create MeetingSession
                    MeetingSession session = new MeetingSession();
                    session.setTitle(offer.getTitle() + " with " + 
                        (offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "Recruiter"));
                    session.setReservation(reservation);
                    
                    reservation.setSession(session);
                    
                    // Add to offer's reservations
                    offer.addReservation(reservation);
                    
                    // Add to candidate's reservations so it appears in getMeetingSchedule()
                    currentCandidate.addReservation(reservation);
                    
                    // Register in system
                    system.registerBooking(reservation, currentCandidate.getEmail(), offer.getTitle());
                    
                    logOperation(LogLevel.INFO, "CandidateController", 
                        "[AUTO-BOOK] ✓ Booked: " + offer.getTitle() + " with " + 
                        (offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "Recruiter"));
                    
                    booked++;
                }
            }
            
            if (booked == 0) {
                logOperation(LogLevel.WARNING, "CandidateController", 
                    "Auto-book found NO matching offers for tags: " + desiredTags);
                view.displayMessage("✓ Auto-book request submitted.\nℹ️ No matching offers found for tags: " + desiredTags + 
                    "\nBookings may become available as recruiters publish more offers.");
                return;
            }
            
            // RECORD: Track successful auto-bookings
            SystemStateManager.getInstance().recordStateChange("AUTO_BOOKING", 
                candidateName + " auto-booked " + booked + " interviews", true);
            
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "AUTO_BOOKING_SUCCESS", "Auto-booked " + booked + " interviews for tags: " + desiredTags);
            
            view.displayMessage("✓ Success! Auto-booked " + booked + " interview(s)!\n" +
                "Check 'My Schedule' tab to see your confirmed bookings.\n" +
                "Interviews: " + booked + "/" + maxToBook + " slots filled");
            
            // SYNC FIX: Update UserSession candidate before refreshing UI
            vcfs.core.UserSession.getInstance().setCurrentCandidate(currentCandidate);
            
            // CRITICAL FIX P4: Force refresh of schedule table immediately
            // This ensures the candidate sees their bookings right away
            viewMeetingSchedule();
            view.refreshScheduleTable();  // NEW: Directly update schedule table model
            view.refreshVirtualRoomTable();
            
        } catch (Exception e) {
            logError("CandidateController", "Failed to auto-book interviews for " + candidateName, e);
            
            SystemStateManager.getInstance().recordStateChange("AUTO_BOOKING_FAILED", 
                candidateName + " auto-booking failed: " + e.getMessage(), false);
            
            view.displayError("Error during auto-booking: " + e.getMessage());
        }
    }

    /**
     * BOOKING FIX P1: Book a specific offer directly for the current candidate.
     * Called when candidate selects an offer and clicks "Book Now".
     * 
     * @param offer The offer to book
     * @throws IllegalArgumentException if offer is null or fully booked
     */
    public void bookOffer(Offer offer) {
        if (!validateLoggedIn(currentCandidate, "Candidate", "Book interview offer")) {
            view.displayError("Error: No candidate logged in. Please log in to book interviews.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        if (!validateNotNull(offer, "Offer")) {
            logOperation(LogLevel.WARNING, "CandidateController", "Book offer attempted with null offer by candidate: " + candidateName);
            view.displayError("Error: Offer data is missing.");
            return;
        }
        
        try {
            // Check if offer still has capacity
            int booked = (offer.getReservations() != null) ? offer.getReservations().size() : 0;
            int available = offer.getCapacity() - booked;
            
            if (available <= 0) {
                logOperation(LogLevel.WARNING, "CandidateController", 
                    "Book offer attempted but offer is FULL: " + offer.getTitle() + " (capacity: " + offer.getCapacity() + ")");
                view.displayError("Error: This interview slot is fully booked. Please select another offer.");
                return;
            }
            
            // Create reservation linking candidate to offer
            Reservation reservation = new Reservation();
            reservation.setCandidate(currentCandidate);
            reservation.setOffer(offer);
            reservation.setStatus("CONFIRMED");
            
            // CRITICAL FIX P2: Create MeetingSession for the reservation
            // This allows the reservation to show up in getMeetingSchedule()
            MeetingSession session = new MeetingSession();
            session.setTitle(offer.getTitle() + " with " + 
                (offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "Recruiter"));
            session.setReservation(reservation);
            reservation.setSession(session);
            
            // Add reservation to offer's list
            offer.addReservation(reservation);
            
            // CRITICAL FIX P2: Add reservation to candidate's personal collection
            // This ensures candidate.getReservations() returns the booking
            currentCandidate.addReservation(reservation);
            
            // SYNC FIX: Update UserSession candidate to reflect booking
            // This ensures UI always gets the same candidate reference with updated reservations
            vcfs.core.UserSession.getInstance().setCurrentCandidate(currentCandidate);
            
            // DEBUG LOGGING - Verify reservation was added AND synced to UserSession
            int reservationCount = currentCandidate.getReservations().size();
            logOperation(LogLevel.INFO, "CandidateController", 
                "[BOOKING DEBUG] Reservation added. Candidate now has " + reservationCount + " reservations.");
            
            logOperation(LogLevel.INFO, "CandidateController", 
                "Interview booking created by " + candidateName + " for offer: " + offer.getTitle());
            
            // CONSISTENCY FIX: Notify system so booking appears in all portals
            // This ensures all three portals see the booking immediately
            CareerFairSystem.getInstance().registerBooking(reservation, currentCandidate.getEmail(), 
                offer.getTitle());
            
            // RECORD OPERATION: Track this booking in system state manager
            SystemStateManager.getInstance().recordStateChange("CANDIDATE_BOOKING", 
                candidateName + " booked interview: " + offer.getTitle(), true);
            
            // RECORD SESSION ACTIVITY: Track in session manager for live monitoring
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "OFFER_BOOKED", "Booked interview: " + offer.getTitle() + " with " + 
                (offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "recruiter"));
            
            view.displayMessage("✓ You have successfully booked this interview!\n" +
                "Interview: " + offer.getTitle() + "\n" +
                "Recruiter: " + (offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "Unknown") + "\n" +
                "Duration: " + offer.getDurationMins() + " minutes\n" +
                "Check 'My Schedule' for details.");
            
            // CRITICAL FIX: Refresh schedule AND virtual room table immediately after booking
            logOperation(LogLevel.INFO, "CandidateController", "[BOOKING COMPLETE] About to refresh UI...");
            viewMeetingSchedule();
            logOperation(LogLevel.INFO, "CandidateController", "[BOOKING COMPLETE] Calling refreshScheduleTable()...");
            view.refreshScheduleTable();  // NEW: Directly update schedule table model
            logOperation(LogLevel.INFO, "CandidateController", "[BOOKING COMPLETE] About to refresh virtual room...");
            view.refreshVirtualRoomTable();
            logOperation(LogLevel.INFO, "CandidateController", "[BOOKING COMPLETE] UI refresh finished");
            
        } catch (Exception e) {
            logError("CandidateController", "Failed to book offer by " + candidateName, e);
            
            // RECORD FAILURE
            SystemStateManager.getInstance().recordStateChange("BOOKING_FAILED", 
                candidateName + " booking attempt failed: " + e.getMessage(), false);
            
            view.displayError("Error booking interview: " + e.getMessage());
        }
    }

    public void viewAvailableLobbies() {
        try {
            CareerFairSystem system = CareerFairSystem.getInstance();
            List<Lobby> lobbies = system.getAllLobbies();
            
            if (lobbies == null) {
                lobbies = new ArrayList<>();
            }
            
            logOperation(LogLevel.INFO, "CandidateController", "Viewing available lobbies. Total: " + lobbies.size());
            
            // RECORD SEARCH ACTIVITY: Track browsing/lobby searches
            SystemStateManager.getInstance().recordStateChange("LOBBY_BROWSED", 
                "Candidate browsed " + lobbies.size() + " available lobbies", true);
            
            view.displayMessage("✓ Available lobbies loaded. Total: " + lobbies.size());
            view.displayLobbies(lobbies);
        } catch (Exception e) {
            logError("CandidateController", "Failed to retrieve available lobbies", e);
            view.displayError("Error retrieving lobbies: " + e.getMessage());
        }
    }

    public void viewLobbyInfo(String lobbyId) {
        if (!validateNotEmpty(lobbyId, "Lobby ID")) {
            logOperation(LogLevel.WARNING, "CandidateController", "View lobby info attempted with empty lobby ID");
            view.displayError("Error: Lobby ID is required.");
            return;
        }
        
        try {
            String trimmedId = safeTrim(lobbyId);
            logOperation(LogLevel.INFO, "CandidateController", "Viewing lobby info for: " + trimmedId);
            view.displayMessage("✓ Loading lobby details for: " + trimmedId);
        } catch (Exception e) {
            logError("CandidateController", "Failed to display lobby info", e);
            view.displayError("Error displaying lobby details: " + e.getMessage());
        }
    }

    public void viewMeetingSchedule() {
        if (!validateLoggedIn(currentCandidate, "Candidate", "View meeting schedule")) {
            view.displayError("Error: No candidate logged in. Please log in to view your schedule.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        try {
            // DEBUG: Check what we have BEFORE getting fresh candidate
            logOperation(LogLevel.INFO, "CandidateController", 
                "[SCHEDULE DEBUG-1] currentCandidate.getReservations().size() = " + 
                (currentCandidate != null ? currentCandidate.getReservations().size() : "NULL CANDIDATE"));
            
            // CRITICAL FIX: Get fresh candidate from UserSession to ensure all reservations are visible
            Candidate freshCandidate = vcfs.core.UserSession.getInstance().getCurrentCandidate();
            
            // DEBUG: Check what we got from UserSession
            logOperation(LogLevel.INFO, "CandidateController", 
                "[SCHEDULE DEBUG-2] UserSession freshCandidate = " + 
                (freshCandidate != null ? "EXISTS (" + freshCandidate.getDisplayName() + ")" : "NULL"));
            
            if (freshCandidate == null) {
                logOperation(LogLevel.WARNING, "CandidateController", 
                    "[SCHEDULE DEBUG] UserSession returned NULL - using currentCandidate instead");
                freshCandidate = currentCandidate;
            }
            
            // DEBUG: Check reservation count from fresh candidate
            logOperation(LogLevel.INFO, "CandidateController", 
                "[SCHEDULE DEBUG-3] freshCandidate.getReservations().size() = " + 
                (freshCandidate != null ? freshCandidate.getReservations().size() : "NULL"));
            
            List<MeetingSession> schedule = freshCandidate.getMeetingSchedule();
            
            // DEBUG: Check what getMeetingSchedule() returned
            logOperation(LogLevel.INFO, "CandidateController", 
                "[SCHEDULE DEBUG-4] freshCandidate.getMeetingSchedule().size() = " + 
                (schedule != null ? schedule.size() : "NULL LIST"));
            
            logOperation(LogLevel.INFO, "CandidateController", "Viewing meeting schedule for " + candidateName + " (" + schedule.size() + " sessions)");
            
            // RECORD SESSION ACTIVITY: Track schedule views in real-time
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "SCHEDULE_VIEWED", "Accessed meeting schedule (" + schedule.size() + " sessions)");
            
            view.displaySchedule(schedule);
        } catch (Exception e) {
            logError("CandidateController", "Failed to retrieve meeting schedule for " + candidateName, e);
            view.displayError("Error retrieving schedule: " + e.getMessage());
        }
    }

    public void cancelMeetingRequest(String requestId) {
        if (!validateLoggedIn(currentCandidate, "Candidate", "Cancel meeting request")) {
            view.displayError("Error: No candidate logged in. Please log in to cancel requests.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        String finalRequestId = safeTrim(requestId);
        if (finalRequestId == null) {
            logOperation(LogLevel.WARNING, "CandidateController", "Cancel meeting request attempted with empty request ID by candidate: " + candidateName);
            view.displayError("Error: Request ID is required.");
            return;
        }
        
        try {
            currentCandidate.cancelRequest(finalRequestId);
            logOperation(LogLevel.INFO, "CandidateController", "Meeting request cancelled by " + candidateName + ": " + finalRequestId);
            
            // CONSISTENCY FIX P3: Notify system of cancellation so all portals refresh
            // This makes the offer "available" again for other candidates
            CareerFairSystem.getInstance().registerCancellation("BOOKING_CANCELLED",
                candidateName + " cancelled booking: " + finalRequestId);
            
            // RECORD CANCELLATION: Track booking cancellations
            SystemStateManager.getInstance().recordStateChange("BOOKING_CANCELLED", 
                candidateName + " cancelled meeting request: " + finalRequestId, true);
            
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "BOOKING_CANCELLED", "Cancelled interview request ID: " + finalRequestId);
            
            view.displayMessage("✓ Your meeting request has been cancelled.");
        } catch (Exception e) {
            logError("CandidateController", "Failed to cancel meeting request by " + candidateName, e);
            
            // RECORD CANCEL FAILURE
            SystemStateManager.getInstance().recordStateChange("CANCEL_FAILED", 
                candidateName + " failed to cancel request: " + e.getMessage(), false);
            
            view.displayError("Error cancelling request: " + e.getMessage());
        }
    }

    public void viewRequestHistory() {
        if (!validateLoggedIn(currentCandidate, "Candidate", "View request history")) {
            view.displayError("Error: No candidate logged in. Please log in to view history.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        try {
            List<Request> history = currentCandidate.getRequestHistory();
            logOperation(LogLevel.INFO, "CandidateController", "Viewing request history for " + candidateName + " (" + history.size() + " requests)");
            
            // RECORD SESSION ACTIVITY: Track history access
            SessionManager.getInstance().recordActivity(candidateName, "Candidate", 
                "HISTORY_ACCESSED", "Reviewed request history (" + history.size() + " requests)");
            
            view.displayRequestHistory(history);
        } catch (Exception e) {
            logError("CandidateController", "Failed to retrieve request history for " + candidateName, e);
            view.displayError("Error retrieving history: " + e.getMessage());
        }
    }

    public void updateProfile(String phone, String email) {
        if (!validateLoggedIn(currentCandidate, "Candidate", "Update profile")) {
            view.displayError("Error: No candidate logged in. Please log in to update profile.");
            return;
        }
        
        String candidateName = getUserName(currentCandidate);
        
        String finalPhone = safeTrim(phone);
        if (finalPhone == null) {
            logOperation(LogLevel.WARNING, "CandidateController", "Update profile attempted with empty phone by candidate: " + candidateName);
            view.displayError("Error: Phone number is required.");
            return;
        }
        
        String finalEmail = safeTrim(email);
        if (finalEmail == null) {
            logOperation(LogLevel.WARNING, "CandidateController", "Update profile attempted with empty email by candidate: " + candidateName);
            view.displayError("Error: Email address is required.");
            return;
        }
        
        try {
            currentCandidate.setPhoneNumber(finalPhone);
            currentCandidate.setEmail(finalEmail);
            logOperation(LogLevel.INFO, "CandidateController", "Profile updated by " + candidateName + " (email: " + finalEmail + ")");
            view.displayMessage("✓ Your profile has been updated successfully.");
        } catch (Exception e) {
            logError("CandidateController", "Failed to update profile for " + candidateName, e);
            view.displayError("Error updating profile: " + e.getMessage());
        }
    }

    public List<MeetingSession> getAvailableSessions(String lobbyId) {
        String finalId = safeTrim(lobbyId);
        if (finalId == null) {
            return new ArrayList<>();
        }
        
        CareerFairSystem system = CareerFairSystem.getInstance();
        Optional<Lobby> lobby = system.getLobby(finalId);
        if (lobby.isEmpty()) {
            return new ArrayList<>();
        }
        
        return lobby.get().getAvailableSessions();
    }

    public Candidate getCurrentCandidate() {
        return currentCandidate;
    }
}
