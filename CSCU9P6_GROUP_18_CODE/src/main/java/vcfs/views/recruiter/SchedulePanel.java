package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.controllers.RecruiterController;
import vcfs.core.UIHelpers;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.booking.MeetingSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Schedule Panel - Recruiter interface for viewing and managing scheduled meetings.
 *
 * PURPOSE:
 *   Display all scheduled interview sessions for the current recruiter.
 *   Provide ability to refresh schedule and view session details.
 *
 * COMPONENTS:
 *   ✓ Header Panel — "Interview Schedule" title with professional styling
 *   ✓ List Panel — Scrollable JList displaying all sessions
 *   ✓ Button Panel —"Refresh Schedule" and "View Session Details" buttons
 *
 * FEATURES:
 *   ✓ Professional header with title and description
 *   ✓ Scrollable session list with proper formatting
 *   ✓ Color-coded buttons (primary for refresh, secondary for view)
 *   ✓ Input validation (requires session selection)
 *   ✓ Comprehensive logging for debugging
 *   ✓ User-friendly error/info messages via UIHelpers
 *
 * WORKFLOW:
 *   1. Display all sessions from controller.viewMeetingHistory()
 *   2. User clicks "Refresh Schedule" to reload sessions
 *   3. User selects session and clicks "View Session Details"
 *   4. Selected session details shown in dialog
 *
 * Original implementation by: Taha
 * Enhanced by: Zaid
 */
public class SchedulePanel extends JPanel {


    private RecruiterController controller;
    private DefaultListModel<String> listModel;
    private JList<String> scheduleList;

    public SchedulePanel(RecruiterController controller) {
        this.controller = controller;
        
        // Register this panel with controller for data updates
        if (controller != null) {
            controller.setSchedulePanel(this);
        }
        
        // Main layout with BorderLayout
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================================
        // HEADER
        // ============================================
        JPanel headerPanel = UIHelpers.createHeaderPanel(
            "Interview Schedule",
            "View all your scheduled interviews and meeting sessions"
        );
        add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // LIST PANEL
        // ============================================
        listModel = new DefaultListModel<>();
        scheduleList = new JList<>(listModel);
        scheduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleList.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(scheduleList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // ============================================
        // BUTTON PANEL
        // ============================================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton refreshBtn = new JButton("Refresh Schedule");
        UIHelpers.stylePrimaryButton(refreshBtn);
        
        JButton viewSessionBtn = new JButton("View Session Details");
        UIHelpers.styleSecondaryButton(viewSessionBtn);
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewSessionBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // ============================================
        // ACTION LISTENERS
        // ============================================
        refreshBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[SchedulePanel] Refresh clicked");
            this.controller.viewMeetingHistory();
            UIHelpers.showInfoDialog(SchedulePanel.this, "Refreshed", "Schedule updated successfully.");
        });

        viewSessionBtn.addActionListener(e -> {
            int selectedIndex = scheduleList.getSelectedIndex();
            if (selectedIndex < 0) {
                UIHelpers.showWarningDialog(SchedulePanel.this, "Selection Required", 
                    "Please select a session to view details.");
                Logger.log(LogLevel.WARNING, "[SchedulePanel] View clicked with no selection");
                return;
            }
            Logger.log(LogLevel.INFO, "[SchedulePanel] View session details clicked for index: " + selectedIndex);
            UIHelpers.showInfoDialog(SchedulePanel.this, "Session Details", 
                "Session: " + scheduleList.getSelectedValue());
        });
        
        // Load initial schedule
        loadSchedule();
    }
    
    private void loadSchedule() {
        try {
            if (controller != null) {
                this.controller.viewMeetingHistory();
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[SchedulePanel] Failed to load initial schedule", e);
        }
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


