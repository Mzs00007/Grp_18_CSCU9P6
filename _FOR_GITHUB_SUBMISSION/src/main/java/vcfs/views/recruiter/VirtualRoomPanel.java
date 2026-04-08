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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Virtual Room Panel - Recruiter interface for conducting virtual interviews.
 *
 * PURPOSE:
 *   Provide recruiter interface to join virtual meeting rooms and conduct
 *   interviews with candidates. Track attendance and record outcomes.
 *
 * COMPONENTS:
 *   ✓ Header Panel — "Virtual Meeting Room" title with description
 *   ✓ Dual-View Card Layout:
 *      - Waiting View: Instructions for joining a session
 *      - Active Room View: Session status and attendance tracking
 *   ✓ Attendance Panel — Mark attendance (ATTENDED or NO_SHOW)
 *
 * FEATURES:
 *   ✓ Professional header with color-coded styling
 *   ✓ Dual-mode interface (waiting/active via CardLayout)
 *   ✓ Session status display with timer information
 *   ✓ Color-coded buttons:
 *      - Green (Mark ATTENDED) = success/completion
 *      - Red (Mark NO_SHOW) = error/rejection
 *      - Blue (Join/End) = primary actions
 *   ✓ Comprehensive logging for all state changes
 *   ✓ User-friendly dialogs for feedback
 *
 * WORKFLOW:
 *   1. Initial state: Waiting view with instructions
 *   2. User clicks "Join Selected Session"
 *   3. Transition to active room view
 *   4. Conduct interview
 *   5. Mark attendance (ATTENDED or NO_SHOW)
 *   6. Click "End Session & Return" to go back to waiting view
 *
 * Original implementation by: Taha
 * Enhanced by: Zaid
 */
public class VirtualRoomPanel extends JPanel {

    private RecruiterController controller;
    private JPanel cards;
    private CardLayout cardLayout;
    private String currentSessionId;
    private JLabel statusLabel;
    private JLabel timerLabel;

    public VirtualRoomPanel(RecruiterController controller) {
        this.controller = controller;
        this.currentSessionId = null;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================================
        // HEADER
        // ============================================
        JPanel headerPanel = UIHelpers.createHeaderPanel(
            "Virtual Meeting Room",
            "Join virtual sessions to conduct interviews with candidates"
        );
        add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // CARD LAYOUT VIEW
        // ============================================
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // ============== CARD 1: WAITING/SELECTION VIEW ==============
        JPanel waitingView = new JPanel(new BorderLayout(10, 10));
        waitingView.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        waitingView.setBackground(Color.WHITE);

        JLabel instructionLabel = new JLabel("Session Selection & Preparation");
        instructionLabel.setFont(UIHelpers.FONT_TITLE);
        waitingView.add(instructionLabel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Color.WHITE);

        JLabel helpLabel1 = new JLabel("1. Select a session from the Schedule tab");
        JLabel helpLabel2 = new JLabel("2. Ensure you are prepared for the interview");
        JLabel helpLabel3 = new JLabel("3. Click 'Join' below when ready to start");

        for (JLabel label : new JLabel[]{helpLabel1, helpLabel2, helpLabel3}) {
            label.setFont(UIHelpers.FONT_LABEL);
            label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            messagePanel.add(label);
        }

        JScrollPane messageScroll = new JScrollPane(messagePanel);
        messageScroll.setBorder(null);
        waitingView.add(messageScroll, BorderLayout.CENTER);

        JPanel joinButtonPanel = new JPanel();
        joinButtonPanel.setBackground(Color.WHITE);
        JButton joinRoomBtn = new JButton("Join Selected Session");
        UIHelpers.stylePrimaryButton(joinRoomBtn);

        joinButtonPanel.add(joinRoomBtn);
        waitingView.add(joinButtonPanel, BorderLayout.SOUTH);

        // ============== CARD 2: ACTIVE ROOM VIEW ==============
        JPanel activeRoomView = new JPanel(new BorderLayout(10, 10));
        activeRoomView.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        activeRoomView.setBackground(Color.WHITE);

        JLabel activeRoomHeader = new JLabel("Virtual Meeting Room - ACTIVE SESSION");
        activeRoomHeader.setFont(UIHelpers.FONT_TITLE);
        activeRoomHeader.setForeground(UIHelpers.COLOR_PRIMARY);
        activeRoomView.add(activeRoomHeader, BorderLayout.NORTH);

        JPanel sessionStatusPanel = new JPanel();
        sessionStatusPanel.setLayout(new BoxLayout(sessionStatusPanel, BoxLayout.Y_AXIS));
        sessionStatusPanel.setBackground(new Color(240, 248, 255));
        sessionStatusPanel.setBorder(BorderFactory.createLineBorder(UIHelpers.COLOR_PRIMARY, 2));

        statusLabel = new JLabel("Status: Interview in progress...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        sessionStatusPanel.add(Box.createVerticalStrut(10));
        sessionStatusPanel.add(statusLabel);
        sessionStatusPanel.add(Box.createVerticalStrut(10));

        timerLabel = new JLabel("Session Timer: --:-- remaining");
        timerLabel.setFont(UIHelpers.FONT_LABEL);
        sessionStatusPanel.add(timerLabel);
        sessionStatusPanel.add(Box.createVerticalStrut(10));

        activeRoomView.add(sessionStatusPanel, BorderLayout.CENTER);

        // ============== ATTENDANCE OUTCOME PANEL ==============
        JPanel outcomePanel = new JPanel();
        outcomePanel.setLayout(new BoxLayout(outcomePanel, BoxLayout.Y_AXIS));
        outcomePanel.setBorder(BorderFactory.createTitledBorder("Interview Outcome"));
        outcomePanel.setBackground(Color.WHITE);

        JLabel outcomeLabel = UIHelpers.createHelpLabel(
            "Mark attendance status below to record the candidate's participation"
        );
        outcomePanel.add(outcomeLabel);
        outcomePanel.add(Box.createVerticalStrut(10));

        JPanel attendanceButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        attendanceButtonPanel.setBackground(Color.WHITE);

        JButton attendedBtn = new JButton("Mark ATTENDED");
        UIHelpers.styleSuccessButton(attendedBtn);

        JButton noShowBtn = new JButton("Mark NO_SHOW");
        UIHelpers.styleErrorButton(noShowBtn);

        JButton endSessionBtn = new JButton("End Session & Return");
        UIHelpers.styleSecondaryButton(endSessionBtn);

        attendanceButtonPanel.add(attendedBtn);
        attendanceButtonPanel.add(Box.createHorizontalStrut(5));
        attendanceButtonPanel.add(noShowBtn);
        attendanceButtonPanel.add(Box.createHorizontalStrut(15));
        attendanceButtonPanel.add(endSessionBtn);

        outcomePanel.add(attendanceButtonPanel);
        activeRoomView.add(outcomePanel, BorderLayout.SOUTH);

        // ============================================
        // ADD CARDS AND SET DEFAULT
        // ============================================
        cards.add(waitingView, "WAITING");
        cards.add(activeRoomView, "ACTIVE_ROOM");
        add(cards, BorderLayout.CENTER);
        cardLayout.show(cards, "WAITING");

        // ============================================
        // ACTION LISTENERS
        // ============================================
        joinRoomBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[VirtualRoomPanel] Join room clicked");
            // Generate unique session ID for this meeting
            currentSessionId = "session-" + System.currentTimeMillis();
            statusLabel.setText("Status: Interview in progress with Session ID: " + currentSessionId);
            timerLabel.setText("Session Timer: 30:00 remaining");
            cardLayout.show(cards, "ACTIVE_ROOM");
            UIHelpers.showSuccessDialog(VirtualRoomPanel.this, "Session Started", 
                "Virtual room session has started. You are now connected.\nSession ID: " + currentSessionId);
        });

        attendedBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[VirtualRoomPanel] Marking attendance: ATTENDED for Session: " + currentSessionId);
            if (currentSessionId != null) {
                this.controller.updateOfferStatus(currentSessionId, "ATTENDED");
                UIHelpers.showSuccessDialog(VirtualRoomPanel.this, "Status Updated", 
                    "Candidate marked as ATTENDED.\nSession ID: " + currentSessionId);
            } else {
                UIHelpers.showErrorDialog(VirtualRoomPanel.this, "Error", 
                    "No active session. Please join a session first.");
            }
        });

        noShowBtn.addActionListener(e -> {
            Logger.log(LogLevel.WARNING, "[VirtualRoomPanel] Marking attendance: NO_SHOW for Session: " + currentSessionId);
            if (currentSessionId != null) {
                this.controller.updateOfferStatus(currentSessionId, "NO_SHOW");
                UIHelpers.showWarningDialog(VirtualRoomPanel.this, "Status Updated", 
                    "Candidate marked as NO_SHOW. This will affect their profile.\nSession ID: " + currentSessionId);
            } else {
                UIHelpers.showErrorDialog(VirtualRoomPanel.this, "Error", 
                    "No active session. Please join a session first.");
            }
        });

        endSessionBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[VirtualRoomPanel] Ending session and returning to waiting");
            currentSessionId = null;
            statusLabel.setText("Status: Interview in progress...");
            timerLabel.setText("Session Timer: --:-- remaining");
            cardLayout.show(cards, "WAITING");
            UIHelpers.showInfoDialog(VirtualRoomPanel.this, "Session Ended", 
                "Virtual room session has ended. Records have been saved.");
        });
    }
}


