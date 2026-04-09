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
import vcfs.models.booking.Reservation;
import vcfs.models.users.Candidate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Collection;

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
    private DefaultTableModel scheduleTableModel;
    private JTable scheduleTable;

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
        scheduleTableModel = new DefaultTableModel(
            new String[]{"Candidate", "Email", "Interview", "Status", "Scheduled Time", "Details"}, 0);
        scheduleTable = new JTable(scheduleTableModel);
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 12));
        scheduleTable.setRowHeight(25);
        
        // Set column widths
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(120);  // Candidate
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(160);  // Email
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Interview
        scheduleTable.getColumnModel().getColumn(3).setPreferredWidth(90);   // Status
        scheduleTable.getColumnModel().getColumn(4).setPreferredWidth(150);  // Scheduled Time
        scheduleTable.getColumnModel().getColumn(5).setPreferredWidth(80);   // Details

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
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
            refreshScheduleTable();
            UIHelpers.showInfoDialog(SchedulePanel.this, "Refreshed", "Schedule updated successfully.");
        });

        viewSessionBtn.addActionListener(e -> {
            int selectedRow = scheduleTable.getSelectedRow();
            if (selectedRow < 0) {
                UIHelpers.showWarningDialog(SchedulePanel.this, "Selection Required", 
                    "Please select a session to view details.");
                Logger.log(LogLevel.WARNING, "[SchedulePanel] View clicked with no selection");
                return;
            }
            String candidate = (String) scheduleTableModel.getValueAt(selectedRow, 0);
            String interview = (String) scheduleTableModel.getValueAt(selectedRow, 2);
            String time = (String) scheduleTableModel.getValueAt(selectedRow, 4);
            String status = (String) scheduleTableModel.getValueAt(selectedRow, 3);
            
            String details = "Candidate: " + candidate + "\n" +
                           "Interview: " + interview + "\n" +
                           "Time: " + time + "\n" +
                           "Status: " + status;
            
            Logger.log(LogLevel.INFO, "[SchedulePanel] View session details clicked for: " + candidate);
            UIHelpers.showInfoDialog(SchedulePanel.this, "Session Details", details);
        });
        
        // Load initial schedule
        loadSchedule();
    }
    
    private void loadSchedule() {
        try {
            if (controller != null) {
                Logger.log(LogLevel.INFO, "[SchedulePanel] Loading initial schedule");
                refreshScheduleTable();
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[SchedulePanel] Failed to load initial schedule", e);
        }
    }

    public void updateSchedule(List<MeetingSession> sessions) {
        scheduleTableModel.setRowCount(0);
        for (MeetingSession session : sessions) {
            String startTime = (session.getReservation() != null && session.getReservation().getScheduledStart() != null) 
                                ? session.getReservation().getScheduledStart().toString() : "TBD";
            String candidate = "Unknown";
            String email = "N/A";
            String status = "UNKNOWN";
            
            if (session.getReservation() != null && session.getReservation().getCandidate() != null) {
                candidate = session.getReservation().getCandidate().getDisplayName();
                email = session.getReservation().getCandidate().getEmail();
                status = session.getReservation().getStatus();
            }
            
            scheduleTableModel.addRow(new Object[]{
                candidate,
                email,
                session.getTitle(),
                status,
                startTime,
                "View"
            });
        }
    }
    
    public void refreshScheduleTable() {
        try {
            scheduleTableModel.setRowCount(0);
            
            if (controller != null && controller.getCurrentRecruiter() != null) {
                Logger.log(LogLevel.INFO, "[SchedulePanel] Refreshing schedule table for recruiter");
                
                // Get all offers from recruiter
                Collection<vcfs.models.booking.Offer> offers = controller.getCurrentRecruiter().getOffers();
                
                for (vcfs.models.booking.Offer offer : offers) {
                    // *** CRITICAL: Always query UserSession for fresh candidate data ***
                    if (offer.getReservations() != null) {
                        for (Reservation reservation : offer.getReservations()) {
                            Candidate candidate = reservation.getCandidate();
                            
                            if (candidate != null) {
                                String candidateName = candidate.getDisplayName() != null ? 
                                    candidate.getDisplayName() : "Unknown";
                                String email = candidate.getEmail() != null ? 
                                    candidate.getEmail() : "N/A";
                                String interviewTitle = offer.getTitle() != null ? 
                                    offer.getTitle() : "N/A";
                                String status = reservation.getStatus() != null ? 
                                    reservation.getStatus() : "PENDING";
                                String scheduledTime = reservation.getScheduledStart() != null ? 
                                    reservation.getScheduledStart().toString() : "TBD";
                                
                                scheduleTableModel.addRow(new Object[]{
                                    candidateName,
                                    email,
                                    interviewTitle,
                                    status,
                                    scheduledTime,
                                    "View"
                                });
                            }
                        }
                    }
                }
                Logger.log(LogLevel.INFO, "[SchedulePanel] Schedule table refreshed with " + 
                    scheduleTableModel.getRowCount() + " interviews");
            }
        } catch (Exception e) {
            Logger.log(LogLevel.WARNING, "[SchedulePanel] Error refreshing schedule: " + e.getMessage());
        }
    }
}


