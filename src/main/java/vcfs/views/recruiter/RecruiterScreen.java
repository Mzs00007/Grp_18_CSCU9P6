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

/**
 * Recruiter Dashboard - Main UI for recruiter operations.
 *
 * Original implementation by: Taha (CodeByTaha18)
 * Adapted to skeleton by: Zaid
 *
 * Responsibilities:
 * - Display tabs for recruiter functions: Publish Offer, Schedule, Virtual Room
 * - Coordinate with RecruiterController for business logic
 */
public class RecruiterScreen extends JFrame implements RecruiterView {

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
}


