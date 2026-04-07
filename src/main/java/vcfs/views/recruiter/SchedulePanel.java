package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.controllers.RecruiterController;
import vcfs.models.booking.MeetingSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Schedule Panel - Recruiter interface for managing meeting schedule and time slots.
 *
 * Responsibilities:
 * - Display recruiter's published offers
 * - Show scheduled meetings and confirmations
 * - Allow recruiter to view booking calendar
 *
 * Original implementation by: Taha
 * Adapted to skeleton by: Zaid
 */
public class SchedulePanel extends JPanel {

    private RecruiterController controller;
    private DefaultListModel<String> listModel;
    private JList<String> scheduleList;

    public SchedulePanel(RecruiterController controller) {
        this.controller = controller;
        // Panel setup
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Schedule"));

        listModel = new DefaultListModel<>();
        scheduleList = new JList<>(listModel);

        JScrollPane scrollPane = new JScrollPane(scheduleList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh Schedule");
        JButton viewSessionBtn = new JButton("View Session");
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewSessionBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> this.controller.viewMeetingHistory());
    }

    public void updateSchedule(List<MeetingSession> sessions) {
        listModel.clear();
        for (MeetingSession session : sessions) {
            String startTime = (session.getReservation() != null && session.getReservation().getScheduledStart() != null) 
                                ? session.getReservation().getScheduledStart().toString() : "TBD";
            listModel.addElement(session.getTitle() + " - " + startTime);
        }
    }
}


