package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.*;

import java.util.List;
import vcfs.controllers.RecruiterController;
import vcfs.core.CareerFairSystem;
import vcfs.models.booking.MeetingSession;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Recruiter Dashboard - Main UI for recruiter operations.
 *
 * Original implementation by: Taha (CodeByTaha18)
 * Adapted to skeleton by: Zaid
 *
 * Responsibilities:
 * - Display tabs for recruiter functions: Publish Offer, Schedule, Virtual Room
 * - Coordinate with RecruiterController for business logic
 * - Receive PropertyChangeListener notifications for real-time updates
 */
public class RecruiterScreen extends JFrame implements RecruiterView, PropertyChangeListener {

    private RecruiterController controller;

    public RecruiterScreen() {
        setTitle("VCFS - Recruiter Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controller = new RecruiterController(this, CareerFairSystem.getInstance());

        JTabbedPane tabs = new JTabbedPane();

        PublishOfferPanel publishPanel = new PublishOfferPanel(controller);
        tabs.add("Publish Offer", publishPanel);
        
        SchedulePanel schedulePanel = new SchedulePanel(controller);
        tabs.add("Schedule", schedulePanel);
        
        VirtualRoomPanel virtualRoomPanel = new VirtualRoomPanel(controller);
        tabs.add("Virtual Room", virtualRoomPanel);

        add(tabs);
        
        // ===== REGISTER AS OBSERVER =====
        // RecruiterScreen receives property change events from CareerFairSystem
        // This enables real-time updates when candidates book slots or system state changes
        CareerFairSystem.getInstance().addPropertyChangeListener(this);
        
        setVisible(true);
    }

    @Override
    public void displayError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void displaySessions(List<MeetingSession> sessions) {
        // To be handled by panels
    }

    /**
     * Observer callback - receives property change events from CareerFairSystem
     * Triggered when candidates book slots, organizations change, or system state updates
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Recruiters care about events related to their availability and bookings
        if ("time".equals(evt.getPropertyName()) || "phase".equals(evt.getPropertyName())) {
            // System time or phase changed - may affect availability display
            System.out.println("[RecruiterScreen] System event: " + evt.getPropertyName() + " = " + evt.getNewValue());
        }
    }
}


