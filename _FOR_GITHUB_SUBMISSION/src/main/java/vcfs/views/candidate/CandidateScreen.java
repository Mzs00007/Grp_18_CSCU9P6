package vcfs.views.candidate;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import vcfs.controllers.CandidateController;
import vcfs.core.CareerFairSystem;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.UserSession;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Request;
import vcfs.models.booking.Lobby;
import vcfs.models.users.Candidate;

public class CandidateScreen extends JFrame implements CandidateView, PropertyChangeListener {

    private CandidateController controller;
    private DefaultTableModel scheduleTableModel;
    private DefaultTableModel availableOffersTableModel;  // For refreshing from PropertyChangeListener

    public CandidateScreen() {
        setTitle("VCFS - Candidate Portal");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        setLayout(new BorderLayout(10, 10));
        
        // Set background
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ((JPanel) getContentPane()).setBackground(new Color(245, 248, 250));
        
        controller = new CandidateController(this);

        // CRITICAL FIX: Set the currently logged-in candidate on the controller
        Candidate currentCandidate = UserSession.getInstance().getCurrentCandidate();
        if (currentCandidate != null) {
            controller.setCurrentCandidate(currentCandidate);
        }

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🎓 CANDIDATE PORTAL - Find & Book Interviews");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        String candidateName = (currentCandidate != null) ? currentCandidate.getDisplayName() : "Candidate";
        JLabel userLabel = new JLabel("👤 Welcome, " + candidateName + " | Browse offers from recruiters and schedule your interviews");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(new Color(225, 245, 225));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(76, 175, 80));
        titlePanel.add(titleLabel);
        titlePanel.add(userLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Right side: Back + Logout buttons
        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerButtonPanel.setBackground(new Color(76, 175, 80));
        
        JButton backButton = new JButton("↶ BACK");
        backButton.setBackground(new Color(255, 152, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 11));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createRaisedBevelBorder());
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Return to Main Menu?", "Confirm Navigation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                vcfs.core.Logger.log(vcfs.core.LogLevel.INFO, "[CandidateScreen] Back to Main Menu");
                vcfs.core.UserSession.getInstance().logout();
                new vcfs.views.shared.MainMenuFrame();
                dispose();
            }
        });
        headerButtonPanel.add(backButton);
        
        JButton logoutButton = new JButton("🚪 Logout");
        logoutButton.setBackground(new Color(244, 67, 54));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 11));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createRaisedBevelBorder());
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> {
            vcfs.core.Logger.log(vcfs.core.LogLevel.INFO, "[CandidateScreen] Logout clicked");
            vcfs.core.UserSession.getInstance().logout();
            vcfs.core.UIHelpers.showInfoDialog(CandidateScreen.this, "Logged Out", "You have been logged out successfully.");
            new vcfs.views.shared.MainMenuFrame();
            dispose();
        });
        headerButtonPanel.add(logoutButton);
        
        headerPanel.add(headerButtonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // P2 UI UPGRADE: ZAID (mzs00007) - Add status dashboard with workflow guidance
        JPanel dashboardPanel = createStatusDashboard();
        add(dashboardPanel, BorderLayout.CENTER);
        
        // ===== REGISTER AS OBSERVER =====
        // CandidateScreen receives property change events from CareerFairSystem
        // This enables real-time updates when offers are published or system state changes
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
    public void displayLobbies(List<Lobby> lobbies) {
        StringBuilder sb = new StringBuilder("Available Lobbies:\n");
        for (Lobby l : lobbies) {
            String title = l.getSession() != null ? l.getSession().getTitle() : "Unknown Session";
            sb.append("- ").append(title).append("\n");
        }
        displayMessage(sb.toString());
    }

    @Override
    public void displayLobbyDetails(Lobby lobby) {
        String title = lobby.getSession() != null ? lobby.getSession().getTitle() : "Unknown Session";
        displayMessage("Lobby Details: " + title);
    }

    @Override
    public void displaySchedule(List<MeetingSession> schedule) {
        scheduleTableModel.setRowCount(0); // Clear table
        if (schedule != null) {
            for (MeetingSession session : schedule) {
                String startTime = (session.getReservation() != null && session.getReservation().getScheduledStart() != null) 
                                    ? session.getReservation().getScheduledStart().toString() : "TBD";
                int duration = (session.getReservation() != null && session.getReservation().getOffer() != null) 
                                ? session.getReservation().getOffer().getDurationMins() : 0;
                scheduleTableModel.addRow(new Object[]{
                    session.getTitle(),
                    startTime,
                    duration
                });
            }
        }
    }

    @Override
    public void displayRequestHistory(List<Request> requests) {
        StringBuilder sb = new StringBuilder("📋 Booking Request History:\n\n");
        if (requests == null || requests.isEmpty()) {
            sb.append("No booking requests found.");
        } else {
            for (Request req : requests) {
                sb.append("• Request ID: ").append(req.getId()).append("\n");
                sb.append("  Desired Tags: ").append(req.getDesiredTags()).append("\n");
                sb.append("  Preferred Orgs: ").append(req.getPreferredOrgs()).append("\n");
                sb.append("  Max Appointments: ").append(req.getMaxAppointments()).append("\n");
                sb.append("\n");
            }
        }
        displayMessage(sb.toString());
    }

    // P2 UI UPGRADE: ZAID (mzs00007) - Status dashboard with workflow guidance
    private JPanel createStatusDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 248, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== STATUS BAR =====
        JPanel statusBarPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statusBarPanel.setBackground(new Color(76, 175, 80));
        statusBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel fairPhaseLabel = new JLabel("📍 Fair Status: ACTIVE");
        fairPhaseLabel.setFont(new Font("Arial", Font.BOLD, 11));
        fairPhaseLabel.setForeground(Color.WHITE);
        statusBarPanel.add(fairPhaseLabel);

        JLabel bookingStatusLabel = new JLabel("📚 Your Bookings: READY");
        bookingStatusLabel.setFont(new Font("Arial", Font.BOLD, 11));
        bookingStatusLabel.setForeground(Color.WHITE);
        statusBarPanel.add(bookingStatusLabel);

        JLabel systemStatusLabel = new JLabel("✅ System: OPERATIONAL");
        systemStatusLabel.setFont(new Font("Arial", Font.BOLD, 11));
        systemStatusLabel.setForeground(Color.WHITE);
        statusBarPanel.add(systemStatusLabel);

        mainPanel.add(statusBarPanel, BorderLayout.NORTH);

        // ===== COMPREHENSIVE WORKFLOW GUIDANCE PANEL =====
        JPanel guidancePanel = new JPanel(new BorderLayout(10, 10));
        guidancePanel.setBackground(new Color(255, 250, 230));
        guidancePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 3),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel guidanceTitle = new JLabel("🚀 YOUR INTERVIEW BOOKING WORKFLOW - 4 Simple Steps:");
        guidanceTitle.setFont(new Font("Arial", Font.BOLD, 13));
        guidanceTitle.setForeground(new Color(200, 120, 0));
        guidancePanel.add(guidanceTitle, BorderLayout.NORTH);

        JTextArea guidanceText = new JTextArea();
        guidanceText.setText(
            "STEP 1️⃣  SEARCH & BOOK OFFERS\n" +
            "    → Go to 'Browse Offers' tab\n" +
            "    → See all available recruitment interviews from companies\n" +
            "    → Enter your interest subject tags (e.g., Software, Data Science, Finance)\n" +
            "    → Click 'Auto-Book Best Matches' to get matched with recruiters\n" +
            "    → System automatically books you into recruiter's available slots\n\n" +
            "STEP 2️⃣  VIEW YOUR SCHEDULED MEETINGS\n" +
            "    → Go to 'My Schedule' tab\n" +
            "    → See all confirmed interview times with recruiters\n" +
            "    → Write personal notes and prepare for each meeting\n" +
            "    → Check recruiter details and interview duration\n\n" +
            "STEP 3️⃣  JOIN VIRTUAL INTERVIEW ROOM\n" +
            "    → Go to 'Virtual Room' tab\n" +
            "    → When meeting time arrives, click 'Join' button\n" +
            "    → Connect with recruiter via video conference\n" +
            "    → Conduct your interview - have a great conversation!\n\n" +
            "STEP 4️⃣  VIEW INTERVIEW RESULTS (After recruiting fair ends)\n" +
            "    → Wait for fair to close - Admin will record attendance\n" +
            "    → See interview outcomes and feedback\n" +
            "    → Track which opportunities moved forward\n\n" +
            "💡 TIPS: Upload a compelling CV and skills in your profile first!\n" +
            "✓ System syncs instantly - after you book, check Admin Console to see updates!"
        );
        guidanceText.setFont(new Font("Arial", Font.PLAIN, 11));
        guidanceText.setEditable(false);
        guidanceText.setLineWrap(true);
        guidanceText.setWrapStyleWord(true);
        guidanceText.setBackground(new Color(255, 250, 230));
        guidanceText.setForeground(new Color(40, 40, 40));
        guidancePanel.add(guidanceText, BorderLayout.CENTER);

        mainPanel.add(guidancePanel, BorderLayout.CENTER);

        // ===== TABBED CONTENT PANEL =====
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Color.WHITE);

        // ===== TAB 1: Browse Available Offers =====
        JPanel browseOffersPanel = new JPanel(new BorderLayout());
        browseOffersPanel.setBackground(Color.WHITE);
        browseOffersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel browseTitle = new JLabel("📊 All Available Interview Offers");
        browseTitle.setFont(new Font("Arial", Font.BOLD, 14));
        browseOffersPanel.add(browseTitle, BorderLayout.NORTH);
        
        // Create table for available offers
        this.availableOffersTableModel = new DefaultTableModel(
            new String[]{"Offer Title", "Recruiter", "Duration (min)", "Topics", "Spaces Available", "Capacity"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable availableOffersTable = new JTable(this.availableOffersTableModel);
        availableOffersTable.setFont(new Font("Arial", Font.PLAIN, 11));
        availableOffersTable.setRowHeight(25);
        availableOffersTable.setBackground(Color.WHITE);
        availableOffersTable.setGridColor(new Color(220, 220, 220));
        availableOffersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Add responsive scrolling (vertical AND horizontal)
        JScrollPane browseScroll = new JScrollPane(availableOffersTable,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        browseScroll.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        browseOffersPanel.add(browseScroll, BorderLayout.CENTER);
        
        JButton refreshBrowseBtn = new JButton("🔄 Refresh Available Offers");
        refreshBrowseBtn.setBackground(new Color(33, 150, 243));
        refreshBrowseBtn.setForeground(Color.WHITE);
        refreshBrowseBtn.setFont(new Font("Arial", Font.BOLD, 11));
        refreshBrowseBtn.setFocusPainted(false);
        refreshBrowseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBrowseBtn.setPreferredSize(new Dimension(200, 35));
        
        refreshBrowseBtn.addActionListener(e -> {
            this.availableOffersTableModel.setRowCount(0);
            try {
                for (vcfs.models.booking.Offer offer : CareerFairSystem.getInstance().getAllOffers()) {
                    int booked = (offer.getReservations() != null) ? offer.getReservations().size() : 0;
                    int available = Math.max(0, offer.getCapacity() - booked);
                    String recruiterName = (offer.getPublisher() != null) ? offer.getPublisher().getDisplayName() : "Unknown";
                    this.availableOffersTableModel.addRow(new Object[]{
                        offer.getTitle(),
                        recruiterName,
                        offer.getDurationMins(),
                        offer.getTopicTags().isEmpty() ? "-" : offer.getTopicTags(),
                        available,
                        offer.getCapacity()
                    });
                }
                if (this.availableOffersTableModel.getRowCount() == 0) {
                    this.availableOffersTableModel.addRow(new Object[]{"(No offers available yet)", "-", "-", "-", "0", "0"});
                }
            } catch (Exception ex) { 
                Logger.log(LogLevel.ERROR, "[CandidateScreen] Error loading offers", ex); 
            }
        });
        
        JPanel browseBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        browseBtnPanel.setBackground(Color.WHITE);
        browseBtnPanel.add(refreshBrowseBtn);
        browseOffersPanel.add(browseBtnPanel, BorderLayout.SOUTH);
        
        // Load initial data
        refreshBrowseBtn.doClick();
        
        tabs.addTab("🏢 Browse Offers", browseOffersPanel);

        // ===== TAB 2: Search & Book Offers =====
        JPanel searchOffersPanel = new JPanel(new BorderLayout(10, 10));
        searchOffersPanel.setBackground(Color.WHITE);
        searchOffersPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 200, 100), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel tagsPanel = new JPanel(new BorderLayout(10, 0));
        tagsPanel.setBackground(Color.WHITE);
        tagsPanel.add(new JLabel("🏷️  Desired Tags (comma separated):"), BorderLayout.NORTH);
        JTextField tagsField = new JTextField();
        tagsField.setFont(new Font("Arial", Font.PLAIN, 12));
        tagsField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        tagsField.setPreferredSize(new Dimension(0, 30));
        tagsPanel.add(tagsField, BorderLayout.CENTER);
        formPanel.add(tagsPanel);
        
        JPanel apptPanel = new JPanel(new BorderLayout(10, 0));
        apptPanel.setBackground(Color.WHITE);
        apptPanel.add(new JLabel("📅 Max Appointments:"), BorderLayout.NORTH);
        JTextField maxApptField = new JTextField("3");
        maxApptField.setFont(new Font("Arial", Font.PLAIN, 12));
        maxApptField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        maxApptField.setPreferredSize(new Dimension(0, 30));
        apptPanel.add(maxApptField, BorderLayout.CENTER);
        formPanel.add(apptPanel);
        
        JButton autoBookBtn = new JButton("🚀 Submit Auto-Book Request");
        autoBookBtn.setBackground(new Color(76, 175, 80));
        autoBookBtn.setForeground(Color.WHITE);
        autoBookBtn.setFont(new Font("Arial", Font.BOLD, 12));
        autoBookBtn.setFocusPainted(false);
        autoBookBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        autoBookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        autoBookBtn.setPreferredSize(new Dimension(0, 35));
        formPanel.add(autoBookBtn);
        
        searchOffersPanel.add(formPanel, BorderLayout.NORTH);
        
        autoBookBtn.addActionListener(e -> {
            try {
                String tags = tagsField.getText().trim();
                int maxAppts = Integer.parseInt(maxApptField.getText().trim());
                controller.submitAutoBookRequest(tags, maxAppts);
                // Auto-refresh schedule after booking
                controller.viewMeetingSchedule();
            } catch (NumberFormatException ex) {
                displayError("Max appointments must be a valid number.");
            } catch (IllegalArgumentException ex) {
                displayError(ex.getMessage());
            }
        });

        tabs.addTab("� Step 1: Search & Book", searchOffersPanel);

        // ===== TAB 3: My Schedule =====
        JPanel mySchedulePanel = new JPanel(new BorderLayout(10, 10));
        mySchedulePanel.setBackground(Color.WHITE);
        mySchedulePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        mySchedulePanel.add(new JLabel("📋 Your Confirmed Reservations"), BorderLayout.NORTH);
        
        String[] columns = {"Session Title", "Start Time", "Duration (mins)", "Status"};
        scheduleTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable scheduleTable = new JTable(scheduleTableModel);
        scheduleTable.setBackground(Color.WHITE);
        scheduleTable.setGridColor(new Color(200, 200, 200));
        scheduleTable.setRowHeight(25);
        scheduleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scheduleTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Add responsive scrolling (vertical AND horizontal)
        JScrollPane tableScroll = new JScrollPane(scheduleTable,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        mySchedulePanel.add(tableScroll, BorderLayout.CENTER);
        
        JButton refreshScheduleBtn = new JButton("🔄 Refresh Schedule");
        refreshScheduleBtn.setBackground(new Color(33, 150, 243));
        refreshScheduleBtn.setForeground(Color.WHITE);
        refreshScheduleBtn.setFont(new Font("Arial", Font.BOLD, 11));
        refreshScheduleBtn.setFocusPainted(false);
        refreshScheduleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshScheduleBtn.setPreferredSize(new Dimension(150, 35));
        refreshScheduleBtn.addActionListener(e -> controller.viewMeetingSchedule());
        mySchedulePanel.add(refreshScheduleBtn, BorderLayout.SOUTH);

        tabs.addTab("📅 Step 2: My Schedule", mySchedulePanel);

        // ===== TAB 4: Lobby Waiting Room =====
        JPanel lobbyPanel = new JPanel(new BorderLayout(10, 10));
        lobbyPanel.setBackground(Color.WHITE);
        lobbyPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lobbyStatusLabel = new JLabel("Virtual Room Status");
        lobbyStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lobbyPanel.add(lobbyStatusLabel, BorderLayout.NORTH);
        
        JLabel lobbyStatus = new JLabel("⏳ Waiting to join a virtual room...");
        lobbyStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lobbyStatus.setFont(new Font("SansSerif", Font.BOLD, 16));
        lobbyStatus.setForeground(new Color(100, 100, 100));
        lobbyPanel.add(lobbyStatus, BorderLayout.CENTER);
        
        JButton checkLobbyBtn = new JButton("🚪 Check Available Lobbies");
        checkLobbyBtn.setBackground(new Color(33, 150, 243));
        checkLobbyBtn.setForeground(Color.WHITE);
        checkLobbyBtn.setFont(new Font("Arial", Font.BOLD, 11));
        checkLobbyBtn.setFocusPainted(false);
        checkLobbyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkLobbyBtn.setPreferredSize(new Dimension(200, 35));
        checkLobbyBtn.addActionListener(e -> controller.viewAvailableLobbies());
        lobbyPanel.add(checkLobbyBtn, BorderLayout.SOUTH);

        tabs.addTab("🚪 Step 3: Join Virtual Room", lobbyPanel);

        contentPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Refresh the display with latest data.
     * Called in multi-portal demo mode to keep portals in sync.
     */
    public void refreshDisplay() {
        Logger.log(LogLevel.INFO, "[CandidateScreen] Refreshing display");
        try {
            // Reload candidate's meeting schedule and available lobbies
            refreshAvailableOffers();
            controller.viewMeetingSchedule();
            controller.viewAvailableLobbies();
            
            // Log current state
            Candidate curr = UserSession.getInstance().getCurrentCandidate();
            if (curr != null) {
                Logger.log(LogLevel.INFO, "[CandidateScreen] Current candidate: " + curr.getDisplayName());
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateScreen] Error refreshing", e);
        }
    }

    /**
     * Refresh the available offers table with latest system data.
     * Called when PropertyChangeListener detects "offers" change.
     */
    private void refreshAvailableOffers() {
        if (this.availableOffersTableModel == null) {
            Logger.log(LogLevel.WARNING, "[CandidateScreen] Available offers table model not initialized");
            return;
        }
        
        try {
            this.availableOffersTableModel.setRowCount(0);
            for (vcfs.models.booking.Offer offer : CareerFairSystem.getInstance().getAllOffers()) {
                int booked = (offer.getReservations() != null) ? offer.getReservations().size() : 0;
                int available = Math.max(0, offer.getCapacity() - booked);
                String recruiterName = (offer.getPublisher() != null) ? offer.getPublisher().getDisplayName() : "Unknown";
                this.availableOffersTableModel.addRow(new Object[]{
                    offer.getTitle(),
                    recruiterName,
                    offer.getDurationMins(),
                    offer.getTopicTags().isEmpty() ? "-" : offer.getTopicTags(),
                    available,
                    offer.getCapacity()
                });
            }
            if (this.availableOffersTableModel.getRowCount() == 0) {
                this.availableOffersTableModel.addRow(new Object[]{"(No offers available yet)", "-", "-", "-", "0", "0"});
            }
            Logger.log(LogLevel.INFO, "[CandidateScreen] Available offers refreshed: " + this.availableOffersTableModel.getRowCount() + " offers");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateScreen] Error refreshing available offers", e);
        }
    }

    /**
     * Observer callback - receives system notifications from CareerFairSystem.
     * Updates candidate screen when phase changes, candidates are added, or time advances.
     * 
     * CRITICAL: All Swing UI updates must happen on the Event Dispatch Thread (EDT)
     * PropertyChangeSupport fires events on its own thread, so we must use
     * SwingUtilities.invokeLater() to marshal calls back to EDT
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if ("phase".equals(prop)) {
            Logger.log(LogLevel.INFO, "[CandidateScreen] Phase changed: " + evt.getNewValue());
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        } else if ("candidates".equals(prop) || "offers".equals(prop)) {
            Logger.log(LogLevel.INFO, "[CandidateScreen] System data updated: " + prop);
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        }
    }
}


