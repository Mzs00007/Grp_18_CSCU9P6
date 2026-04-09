package vcfs.views.candidate;

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
import vcfs.core.UIEnhancementUtils;
import vcfs.core.SessionManager;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Request;
import vcfs.models.booking.Lobby;
import vcfs.models.booking.Reservation;
import vcfs.models.booking.Offer;
import vcfs.models.users.Candidate;

public class CandidateScreen extends JFrame implements CandidateView, PropertyChangeListener {


    private CandidateController controller;
    private DefaultTableModel scheduleTableModel;
    private DefaultTableModel availableOffersTableModel;  // For refreshing from PropertyChangeListener
    private DefaultTableModel roomTableModel;  // For refreshing virtual room from PropertyChangeListener

    public CandidateScreen() {
        setTitle("VCFS - Candidate Portal - Virtual Career Fair System");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(900, 700));
        setLayout(new BorderLayout(0, 0));
        
        // Set background with modern gradient-like color
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ((JPanel) getContentPane()).setBackground(new Color(237, 242, 247));
        
        controller = new CandidateController(this);

        // CRITICAL FIX: Set the currently logged-in candidate on the controller
        Candidate currentCandidate = UserSession.getInstance().getCurrentCandidate();
        if (currentCandidate != null) {
            controller.setCurrentCandidate(currentCandidate);
        }

        // ===== HEADER PANEL - Modern Design =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));  // Professional blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel titleLabel = new JLabel("🎓 CANDIDATE PORTAL");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        String candidateName = (currentCandidate != null) ? currentCandidate.getDisplayName() : "Candidate";
        JLabel userLabel = new JLabel("👤 " + candidateName + " • Find & book interviews • Real-time updates");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(230, 240, 250));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(userLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Right side: Back + Logout buttons
        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerButtonPanel.setBackground(new Color(41, 128, 185));
        
        JButton backButton = new JButton("↶ BACK");
        backButton.setBackground(new Color(244, 208, 63));
        backButton.setForeground(new Color(80, 80, 80));
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(100, 38));
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
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(100, 38));
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

        // P2 UI UPGRADE: ZAID (mzs00007) - Add scrollable page with collapsible sections
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(237, 242, 247));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Add collapsible sections
        mainContentPanel.add(createCollapsibleStatusDashboard());
        mainContentPanel.add(Box.createVerticalGlue());
        
        // Wrap in scrollable panel
        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // ===== ADD CENTER CONTENT WITH COLLAPSIBLE DEMO NOTES =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // ===== ADD COLLAPSIBLE DEMO NOTES (BOTTOM) =====
        String[] candidateNotesTitles = {
            "STEP 1: BROWSE JOB OFFERS",
            "STEP 2: REQUEST INTERVIEW",
            "STEP 3: VIEW SLOT AVAILABILITY",
            "STEP 4: AUTO-BOOKING",
            "DEMO WORKFLOW"
        };
        
        String[] candidateNotesContent = {
            "• Click 'Browse Offers' tab\n" +
            "• See all live job offers from recruiters\n" +
            "• Each offer shows: Company, Job title, Requirements\n" +
            "• Filter by company/skills using search fields\n" +
            "• Offers update in REAL-TIME as recruiters publish them",
            
            "• Click 'Request Interview' tab\n" +
            "• Select a job offer from the list\n" +
            "• Pick your preferred interview time slots\n" +
            "• Submit request - notification goes to recruiter\n" +
            "• Track request status: PENDING → CONFIRMED",
            
            "• Click 'My Schedule' tab\n" +
            "• See all available interview slots\n" +
            "• Shows recruiter's name, company, time windows\n" +
            "• Green = available | Red = booked\n" +
            "• Book directly if preferred slot is open",
            
            "• If recruiter set AUTO-BOOKING: Candidate auto-enrolled\n" +
            "• System assigns best matching slot automatically\n" +
            "• Confirmation email sent instantly\n" +
            "• Meeting link and time appear in 'My Schedule'\n" +
            "• Reduces manual back-and-forth",
            
            "□ Start from Admin: Setup organizations, recruiters, booths\n" +
            "□ Switch to Recruiter: Publish 2-3 job offers\n" +
            "□ Switch to Candidate: Refresh - see new offers appear\n" +
            "□ Candidate: Browse offers, filter, view companies\n" +
            "□ Candidate: Request interview for preferred slot\n" +
            "□ Recruiter: Check port for incoming request\n" +
            "□ Recruiter: Approve and schedule meeting\n" +
            "□ Candidate: Confirm and see virtual room link\n" +
            "□ Admin: Verify all activities in Audit Log"
        };
        
        vcfs.views.shared.CollapsibleDemoNotesPanel candidateNotesPanel = new vcfs.views.shared.CollapsibleDemoNotesPanel(
            "Candidate Portal", candidateNotesTitles, candidateNotesContent
        );
        centerPanel.add(candidateNotesPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // ===== REGISTER AS OBSERVER =====
        // CandidateScreen receives property change events from CareerFairSystem
        // This enables real-time updates when offers are published or system state changes
        CareerFairSystem.getInstance().addPropertyChangeListener(this);
        
        // TRACK PORTAL ACCESS: Record when candidate enters portal
        Candidate candidate = UserSession.getInstance().getCurrentCandidate();
        if (candidate != null) {
            SessionManager.getInstance().recordActivity(candidate.getDisplayName(), "Candidate",
                "PORTAL_ACCESSED", "Entered candidate portal");
        }
        
        setVisible(true);
    }

    @Override
    public void displayError(String message) {
        UIEnhancementUtils.showError(this, "Error", message);
    }

    @Override
    public void displayMessage(String message) {
        // FIX: Use NON-MODAL toast notification - no stacking, auto-closing
        SwingUtilities.invokeLater(() -> {
            JFrame toastFrame = new JFrame();
            toastFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            toastFrame.setUndecorated(true);
            toastFrame.setOpacity(0.95f);
            toastFrame.setSize(500, 120);
            toastFrame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            panel.setBackground(new Color(240, 248, 255));
            
            JLabel icon = new JLabel("✓");
            icon.setFont(new Font("Arial", Font.BOLD, 20));
            icon.setForeground(new Color(46, 204, 113));
            panel.add(icon, BorderLayout.WEST);
            
            JLabel msgLabel = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
            msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            msgLabel.setForeground(new Color(40, 40, 40));
            panel.add(msgLabel, BorderLayout.CENTER);
            
            toastFrame.add(panel);
            toastFrame.setVisible(true);
            
            // Auto-close after 2.5 seconds
            new Timer(2500, e -> toastFrame.dispose()).start();
        });
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
        Logger.log(LogLevel.INFO, "[CandidateScreen] displaySchedule called - received " + 
            (schedule != null ? schedule.size() : 0) + " sessions");
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
        Logger.log(LogLevel.INFO, "[CandidateScreen] Schedule table now has " + scheduleTableModel.getRowCount() + " rows");
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

    // P2 UI UPGRADE: ZAID (mzs00007) - Collapsible status dashboard panel
    private JPanel createCollapsibleStatusDashboard() {
        JPanel collapsiblePanel = new JPanel();
        collapsiblePanel.setLayout(new BorderLayout());
        collapsiblePanel.setBackground(new Color(240, 245, 250));
        collapsiblePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JButton toggleBtn = new JButton("▼ STATUS & WORKFLOW GUIDE");
        toggleBtn.setHorizontalAlignment(SwingConstants.LEFT);
        toggleBtn.setBackground(new Color(76, 175, 80));
        toggleBtn.setForeground(Color.WHITE);
        toggleBtn.setFont(new Font("Arial", Font.BOLD, 12));
        toggleBtn.setFocusPainted(false);
        toggleBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.setPreferredSize(new Dimension(0, 35));
        
        JPanel contentPanel = createStatusDashboard();
        contentPanel.setVisible(true);
        
        toggleBtn.addActionListener(e -> {
            contentPanel.setVisible(!contentPanel.isVisible());
            toggleBtn.setText(contentPanel.isVisible() ? "▼ STATUS & WORKFLOW GUIDE" : "▶ STATUS & WORKFLOW GUIDE");
            collapsiblePanel.revalidate();
            collapsiblePanel.repaint();
        });
        
        collapsiblePanel.add(toggleBtn, BorderLayout.NORTH);
        collapsiblePanel.add(contentPanel, BorderLayout.CENTER);
        
        return collapsiblePanel;
    }
    
    // P2 UI UPGRADE: ZAID (mzs00007) - Collapsible tabbed content panel
    private JPanel createCollapsibleTabbedContent() {
        JPanel collapsiblePanel = new JPanel();
        collapsiblePanel.setLayout(new BorderLayout());
        collapsiblePanel.setBackground(new Color(237, 242, 247));
        collapsiblePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JButton toggleBtn = new JButton("▼ INTERVIEW MANAGEMENT");
        toggleBtn.setHorizontalAlignment(SwingConstants.LEFT);
        toggleBtn.setBackground(new Color(52, 152, 219));
        toggleBtn.setForeground(Color.WHITE);
        toggleBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        toggleBtn.setFocusPainted(false);
        toggleBtn.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.setPreferredSize(new Dimension(0, 40));
        
        JPanel contentHolder = new JPanel(new BorderLayout());
        contentHolder.setBackground(Color.WHITE);
        contentHolder.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        
        // Create tabs panel with modern styling
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Color.WHITE);
        tabs.setTabPlacement(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        // ===== TAB 1: Browse Available Offers =====
        JPanel browseOffersPanel = new JPanel(new BorderLayout());
        browseOffersPanel.setBackground(Color.WHITE);
        browseOffersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel browseTitle = new JLabel("📊 All Available Interview Offers");
        browseTitle.setFont(new Font("Arial", Font.BOLD, 14));
        browseOffersPanel.add(browseTitle, BorderLayout.NORTH);
        
        this.availableOffersTableModel = new DefaultTableModel(
            new String[]{"Offer Title", "Recruiter", "Duration (min)", "Topics", "Spaces Available", "Capacity"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable availableOffersTable = new JTable(this.availableOffersTableModel);
        availableOffersTable.setFont(new Font("Arial", Font.PLAIN, 11));
        availableOffersTable.setRowHeight(25);
        availableOffersTable.setBackground(Color.WHITE);
        availableOffersTable.setGridColor(new Color(220, 220, 220));
        availableOffersTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        // P2 FIX: Set proper column widths to display all data
        availableOffersTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Offer Title
        availableOffersTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Recruiter
        availableOffersTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Duration
        availableOffersTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Topics
        availableOffersTable.getColumnModel().getColumn(4).setPreferredWidth(130); // Spaces Available
        availableOffersTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Capacity
        
        JScrollPane browseScroll = new JScrollPane(availableOffersTable,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        browseScroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        browseScroll.getVerticalScrollBar().setUnitIncrement(5);
        browseOffersPanel.add(browseScroll, BorderLayout.CENTER);
        
        JButton refreshBrowseBtn = new JButton("🔄 Refresh Offers");
        refreshBrowseBtn.setBackground(new Color(52, 152, 219));
        refreshBrowseBtn.setForeground(Color.WHITE);
        refreshBrowseBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshBrowseBtn.setFocusPainted(false);
        refreshBrowseBtn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        refreshBrowseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBrowseBtn.setPreferredSize(new Dimension(150, 38));
        
        // BOOKING FIX P1: Book offer button with row selection required
        JButton bookOfferBtn = new JButton("✓ Book Selected Offer");
        bookOfferBtn.setBackground(new Color(46, 204, 113));
        bookOfferBtn.setForeground(Color.WHITE);
        bookOfferBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        bookOfferBtn.setFocusPainted(false);
        bookOfferBtn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        bookOfferBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookOfferBtn.setPreferredSize(new Dimension(160, 38));
        bookOfferBtn.setEnabled(false);
        
        // Track selected row to enable/disable book button
        availableOffersTable.getSelectionModel().addListSelectionListener(e -> {
            bookOfferBtn.setEnabled(availableOffersTable.getSelectedRow() >= 0);
        });
        
        // Book offer button action
        bookOfferBtn.addActionListener(e -> {
            int selectedRow = availableOffersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(CandidateScreen.this, 
                    "Please select an offer to book.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Get offer title from selected row
                String offerTitle = (String) availableOffersTableModel.getValueAt(selectedRow, 0);
                String recruiterName = (String) availableOffersTableModel.getValueAt(selectedRow, 1);
                
                // Find matching offer in system
                vcfs.models.booking.Offer selectedOffer = null;
                for (vcfs.models.booking.Offer offer : CareerFairSystem.getInstance().getAllOffers()) {
                    if (offer.getTitle().equals(offerTitle) && 
                        ((offer.getPublisher() != null && offer.getPublisher().getDisplayName().equals(recruiterName)) ||
                         recruiterName.equals("Unknown"))) {
                        selectedOffer = offer;
                        break;
                    }
                }
                
                if (selectedOffer == null) {
                    JOptionPane.showMessageDialog(CandidateScreen.this, 
                        "Selected offer not found. Please refresh and try again.", 
                        "Offer Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Book the offer via controller
                controller.bookOffer(selectedOffer);
                
                // Refresh the table and schedule
                refreshBrowseBtn.doClick();
                controller.viewMeetingSchedule();
                
            } catch (Exception ex) {
                Logger.log(LogLevel.ERROR, "[CandidateScreen] Error booking offer", ex);
                JOptionPane.showMessageDialog(CandidateScreen.this, 
                    "Error booking offer: " + ex.getMessage(), 
                    "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
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
        
        JPanel browseBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        browseBtnPanel.setBackground(new Color(245, 248, 250));        
        browseBtnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 235, 240)));
        browseBtnPanel.add(refreshBrowseBtn);
        browseBtnPanel.add(bookOfferBtn);
        browseOffersPanel.add(browseBtnPanel, BorderLayout.SOUTH);
        
        refreshBrowseBtn.doClick();
        tabs.addTab("🏢 Browse Offers", browseOffersPanel);

        // ===== TAB 2: Search & Book Offers =====
        JPanel searchOffersPanel = new JPanel(new BorderLayout(0, 12)); 
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
                controller.viewMeetingSchedule();
            } catch (NumberFormatException ex) {
                displayError("Max appointments must be a valid number.");
            } catch (IllegalArgumentException ex) {
                displayError(ex.getMessage());
            }
        });

        tabs.addTab("🔍 Step 1: Search & Book", searchOffersPanel);

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
        scheduleTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        // P2 FIX: Set proper column widths for schedule display
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(250); // Session Title
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Start Time
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Duration
        scheduleTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
        
        JScrollPane tableScroll = new JScrollPane(scheduleTable,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
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

        // ===== TAB 4: Virtual Meeting Rooms =====
        JPanel virtualRoomPanel = new JPanel(new BorderLayout(10, 10));
        virtualRoomPanel.setBackground(Color.WHITE);
        virtualRoomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel roomHeaderLabel = new JLabel("🎥 Your Scheduled Meeting Rooms");
        roomHeaderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        virtualRoomPanel.add(roomHeaderLabel, BorderLayout.NORTH);
        
        // Meeting rooms table - Enhanced with more data
        this.roomTableModel = new DefaultTableModel(
            new String[]{"Interview", "Recruiter", "Email", "Duration", "Company", "Scheduled Time", "Status", "Join"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable roomTable = new JTable(this.roomTableModel);
        roomTable.setRowHeight(30);
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(140);  // Interview
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Recruiter
        roomTable.getColumnModel().getColumn(2).setPreferredWidth(160);  // Email
        roomTable.getColumnModel().getColumn(3).setPreferredWidth(80);   // Duration
        roomTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // Company
        roomTable.getColumnModel().getColumn(5).setPreferredWidth(140);  // Scheduled Time
        roomTable.getColumnModel().getColumn(6).setPreferredWidth(90);   // Status
        roomTable.getColumnModel().getColumn(7).setPreferredWidth(100);  // Join
        
        // Populate with candidate's meeting rooms
        try {
            Candidate currentCandidate = UserSession.getInstance().getCurrentCandidate();
            if (currentCandidate != null && currentCandidate.getReservations() != null) {
                int roomNumber = 1;
                for (Reservation reservation : currentCandidate.getReservations()) {
                    if (reservation != null && reservation.getOffer() != null) {
                        Offer offer = reservation.getOffer();
                        String interviewTitle = offer.getTitle() != null ? offer.getTitle() : "Interview";
                        String recruiterName = offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "Unknown";
                        String recruiterEmail = offer.getPublisher() != null && offer.getPublisher().getEmail() != null ? 
                            offer.getPublisher().getEmail() : "N/A";
                        int duration = offer.getDurationMins();
                        String company = "Company";
                        if (offer.getPublisher() != null && offer.getPublisher().getBooth() != null && 
                            offer.getPublisher().getBooth().getOrganization() != null) {
                            String orgName = offer.getPublisher().getBooth().getOrganization().getName();
                            company = orgName != null ? orgName : "Company";
                        }
                        String scheduledTime = reservation.getScheduledStart() != null ? 
                            reservation.getScheduledStart().toString() : "TBD";
                        String status = reservation.getStatus() != null ? 
                            reservation.getStatus() : "PENDING";
                        
                        roomTableModel.addRow(new Object[]{
                            interviewTitle,
                            recruiterName,
                            recruiterEmail,
                            duration,
                            company,
                            scheduledTime,
                            status,
                            "🎥 Join Room " + roomNumber
                        });
                        roomNumber++;
                    }
                }
            }
        } catch (Exception e) {
            Logger.log(LogLevel.WARNING, "[CandidateScreen] Error loading meeting rooms: " + e.getMessage());
        }
        
        JScrollPane roomScrollPane = new JScrollPane(roomTable);
        virtualRoomPanel.add(roomScrollPane, BorderLayout.CENTER);
        
        // Handle Join button clicks on the virtual room table
        roomTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = roomTable.rowAtPoint(e.getPoint());
                int col = roomTable.columnAtPoint(e.getPoint());
                
                // Only handle clicks on the "Join" column (column 7 - now the last column)
                if (row >= 0 && col == 7) {
                    String interviewTitle = (String) roomTableModel.getValueAt(row, 0);
                    String recruiterName = (String) roomTableModel.getValueAt(row, 1);
                    String scheduledTime = (String) roomTableModel.getValueAt(row, 5);
                    String status = (String) roomTableModel.getValueAt(row, 6);
                    
                    Logger.log(LogLevel.INFO, "[CandidateScreen] JOIN CLICKED for: " + interviewTitle + " with " + recruiterName);
                    
                    // Show join confirmation dialog
                    int response = JOptionPane.showConfirmDialog(CandidateScreen.this,
                        "Join \'" + interviewTitle + "\' interview with " + recruiterName + "?\n\n" +
                        "Time: " + scheduledTime + "\n" +
                        "Status: " + status,
                        "Join Virtual Interview Room",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    if (response == JOptionPane.YES_OPTION) {
                        // Log the join action
                        Logger.log(LogLevel.INFO, "[INTERVIEW] Candidate joining: " + interviewTitle);
                        SessionManager.getInstance().recordActivity(
                            (controller != null && controller.getCurrentCandidate() != null) ? 
                                controller.getCurrentCandidate().getDisplayName() : "Unknown",
                            "Candidate", "INTERVIEW_JOINED", "Joined interview: " + interviewTitle);
                        
                        // Show virtual room opening
                        JOptionPane.showMessageDialog(CandidateScreen.this,
                            "🎥 Opening virtual interview room...\n" +
                            "Interview: " + interviewTitle + "\n" +
                            "Recruiter: " + recruiterName + "\n\n" +
                            "Connection established! ✅\n" +
                            "(In a real system, this would connect to video conference)",
                            "Virtual Room Connected",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        Logger.log(LogLevel.INFO, "[CandidateScreen] Successfully joined virtual room");
                    }
                }
            }
        });
        
        // Button panel
        JPanel roomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        roomButtonPanel.setBackground(Color.WHITE);
        
        JButton refreshRoomsBtn = new JButton("🔄 Refresh Rooms");
        refreshRoomsBtn.setBackground(new Color(76, 175, 80));
        refreshRoomsBtn.setForeground(Color.WHITE);
        refreshRoomsBtn.setFont(new Font("Arial", Font.BOLD, 11));
        refreshRoomsBtn.setFocusPainted(false);
        refreshRoomsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshRoomsBtn.addActionListener(e -> {
            roomTableModel.setRowCount(0);
            try {
                Candidate currentCandidate = UserSession.getInstance().getCurrentCandidate();
                if (currentCandidate != null && currentCandidate.getReservations() != null) {
                    int roomNumber = 1;
                    for (Reservation reservation : currentCandidate.getReservations()) {
                        if (reservation != null && reservation.getOffer() != null) {
                            Offer offer = reservation.getOffer();
                            String interviewTitle = offer.getTitle() != null ? offer.getTitle() : "Interview";
                            String recruiterName = offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "Unknown";
                            String scheduledTime = reservation.getScheduledStart() != null ? 
                                reservation.getScheduledStart().toString() : "TBD";
                            String status = reservation.getStatus() != null ? 
                                reservation.getStatus() : "PENDING";
                            
                            roomTableModel.addRow(new Object[]{
                                interviewTitle,
                                recruiterName,
                                scheduledTime,
                                status,
                                "🎥 Join Room " + roomNumber
                            });
                            roomNumber++;
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.log(LogLevel.WARNING, "[CandidateScreen] Error refreshing meeting rooms: " + ex.getMessage());
            }
        });
        roomButtonPanel.add(refreshRoomsBtn);
        
        virtualRoomPanel.add(roomButtonPanel, BorderLayout.SOUTH);

        tabs.addTab("🚪 Step 3: Join Virtual Room", virtualRoomPanel);
        
        contentHolder.add(tabs, BorderLayout.CENTER);
        
        toggleBtn.addActionListener(e -> {
            contentHolder.setVisible(!contentHolder.isVisible());
            toggleBtn.setText(contentHolder.isVisible() ? "▼ INTERVIEW MANAGEMENT" : "▶ INTERVIEW MANAGEMENT");
            collapsiblePanel.revalidate();
            collapsiblePanel.repaint();
        });
        
        collapsiblePanel.add(toggleBtn, BorderLayout.NORTH);
        collapsiblePanel.add(contentHolder, BorderLayout.CENTER);
        
        return collapsiblePanel;
    }
    
    // P2 UI UPGRADE: ZAID (mzs00007) - Helper method for status dashboard content
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
            "STEP 1️⃣  SEARCH & BOOK INTERVIEW OFFERS\n" +
            "    → Go to 'Browse Offers' tab to see available interviews\n" +
            "    → See all recruitment opportunities from participating companies\n" +
            "    → Enter your job interest tags (e.g., Software, Data Science, AI/ML)\n" +
            "    → View matching offers from recruiters in relevant fields\n" +
            "    → Click 'Auto-Book Best Matches' to let system match you with recruiters\n" +
            "    → System books you into recruiter's first available time slot\n" +
            "    → Check Booking Confirmation dialog that appears\n\n" +
            "STEP 2️⃣  VIEW YOUR SCHEDULED INTERVIEW MEETINGS\n" +
            "    → Go to 'My Schedule' tab\n" +
            "    → See all confirmed interview times with recruiters\n" +
            "    → Date, time, recruiter name, company, and position shown\n" +
            "    → Write personal prep notes for each interview\n" +
            "    → Cancel if needed (if your schedule conflicts)\n\n" +
            "STEP 3️⃣  JOIN VIRTUAL RECRUITER MEETING\n" +
            "    → Go to 'Virtual Room' tab when interview time approaches\n" +
            "    → See 'Join' button for confirmed interviews\n" +
            "    → When meeting starts, click 'Join' to enter video conference\n" +
            "    → Conduct your interview with recruiter\n\n" +
            "STEP 4️⃣  VIEW INTERVIEW OUTCOMES (After fair ends)\n" +
            "    → After admin closes recruiting fair\n" +
            "    → View interview feedback and results in 'Results' tab\n" +
            "    → See which recruiters advanced your application\n\n" +
            "🔄 DEMO SYNC VERIFICATION:\n" +
            "    ① First publish offers from Recruiter Portal\n" +
            "    ② Switch to Candidate Portal (this screen)\n" +
            "    ③ Offers appear in Browse Offers tab (system synced!)\n" +
            "    ④ Click Auto-Book → creates booking\n" +
            "    ⑤ Switch back to Admin Console\n" +
            "    ⑥ Check 'Bookings' table - shows your new booking\n" +
            "    ⑦ Check Audit Log - shows 'Booking Created' event\n\n" +
            "💡 SYNC DEMONSTRATION:\n" +
            "    • Offers published by recruiters appear here INSTANTLY\n" +
            "    • Your bookings are recorded in Admin system IMMEDIATELY\n" +
            "    • All three portals see changes at the same time\n" +
            "    • SessionManager tracks every action\n" +
            "✓ This shows real-time multi-user coordination across all portals!"
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
        availableOffersTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        // P2 FIX: Set proper column widths to display all data
        availableOffersTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Offer Title
        availableOffersTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Recruiter
        availableOffersTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Duration
        availableOffersTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Topics
        availableOffersTable.getColumnModel().getColumn(4).setPreferredWidth(130); // Spaces Available
        availableOffersTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Capacity
        
        // Add responsive scrolling (horizontal only for tables)
        JScrollPane browseScroll = new JScrollPane(availableOffersTable,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
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
        
        // BOOKING FIX P1: Book offer button with row selection required
        JButton bookOfferBtn2 = new JButton("📆 Book Selected Offer");
        bookOfferBtn2.setBackground(new Color(76, 175, 80));
        bookOfferBtn2.setForeground(Color.WHITE);
        bookOfferBtn2.setFont(new Font("Arial", Font.BOLD, 11));
        bookOfferBtn2.setFocusPainted(false);
        bookOfferBtn2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookOfferBtn2.setPreferredSize(new Dimension(180, 35));
        bookOfferBtn2.setEnabled(false); // Disabled until row selected
        
        // Track selected row to enable/disable book button
        availableOffersTable.getSelectionModel().addListSelectionListener(e -> {
            bookOfferBtn2.setEnabled(availableOffersTable.getSelectedRow() >= 0);
        });
        
        // Book offer button action
        bookOfferBtn2.addActionListener(e -> {
            int selectedRow = availableOffersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(CandidateScreen.this, 
                    "Please select an offer to book.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Get offer title from selected row
                String offerTitle = (String) availableOffersTableModel.getValueAt(selectedRow, 0);
                String recruiterName = (String) availableOffersTableModel.getValueAt(selectedRow, 1);
                
                // Find matching offer in system
                vcfs.models.booking.Offer selectedOffer = null;
                for (vcfs.models.booking.Offer offer : CareerFairSystem.getInstance().getAllOffers()) {
                    if (offer.getTitle().equals(offerTitle) && 
                        ((offer.getPublisher() != null && offer.getPublisher().getDisplayName().equals(recruiterName)) ||
                         recruiterName.equals("Unknown"))) {
                        selectedOffer = offer;
                        break;
                    }
                }
                
                if (selectedOffer == null) {
                    JOptionPane.showMessageDialog(CandidateScreen.this, 
                        "Selected offer not found. Please refresh and try again.", 
                        "Offer Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Book the offer via controller
                controller.bookOffer(selectedOffer);
                
                // Refresh the table and schedule
                refreshBrowseBtn.doClick();
                controller.viewMeetingSchedule();
                
            } catch (Exception ex) {
                Logger.log(LogLevel.ERROR, "[CandidateScreen] Error booking offer", ex);
                JOptionPane.showMessageDialog(CandidateScreen.this, 
                    "Error booking offer: " + ex.getMessage(), 
                    "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
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
        browseBtnPanel.add(bookOfferBtn2);
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
        } else if ("reset".equals(prop)) {
            Logger.log(LogLevel.INFO, "[CandidateScreen] System reset - clearing all displays");
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        } else if ("timeline".equals(prop)) {
            Logger.log(LogLevel.INFO, "[CandidateScreen] Timeline updated - refreshing display");
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        }
    }

    @Override
    public void displaySessions(List<MeetingSession> sessions) {
        if (scheduleTableModel == null) {
            Logger.log(LogLevel.WARNING, "[CandidateScreen] Schedule table model not initialized");
            return;
        }
        
        scheduleTableModel.setRowCount(0);
        
        // DEBUG: What did the controller send us?
        Logger.log(LogLevel.INFO, "[CandidateScreen] displaySessions() RECEIVED " + 
            (sessions != null ? sessions.size() : "NULL") + " sessions from controller");
        
        // CRITICAL FIX: ALWAYS get fresh candidate from UserSession to ensure sync
        Candidate currentCandidate = vcfs.core.UserSession.getInstance().getCurrentCandidate();
        Logger.log(LogLevel.INFO, "[CandidateScreen] displaySessions() - Got candidate: " + 
            (currentCandidate != null ? currentCandidate.getDisplayName() : "NULL") + 
            " with " + (currentCandidate != null ? currentCandidate.getReservations().size() : "?") + " reservations");
        
        int rowCount = 0;
        
        // Display candidate's actual reservations (these are synced from UserSession)
        if (currentCandidate != null && currentCandidate.getReservations() != null) {
            Logger.log(LogLevel.INFO, "[CandidateScreen] Looping through " + currentCandidate.getReservations().size() + " reservations...");
            
            for (vcfs.models.booking.Reservation res : currentCandidate.getReservations()) {
                if (res != null) {
                    // Get offer title
                    String title = (res.getOffer() != null) ? res.getOffer().getTitle() : "Unknown Offer";
                    if (res.getOffer() != null && res.getOffer().getPublisher() != null) {
                        title += " with " + res.getOffer().getPublisher().getDisplayName();
                    }
                    
                    // Get scheduled time
                    String startTime = (res.getScheduledStart() != null) 
                        ? res.getScheduledStart().toString() 
                        : "TBD";
                    
                    // Get duration
                    int duration = (res.getOffer() != null) 
                        ? res.getOffer().getDurationMins() 
                        : 0;
                    
                    // Get status
                    String status = (res.getStatus() != null) ? res.getStatus() : "CONFIRMED";
                    
                    scheduleTableModel.addRow(new Object[]{
                        title,
                        startTime,
                        duration,
                        status
                    });
                    rowCount++;
                    Logger.log(LogLevel.INFO, "[CandidateScreen] Added 1 row: " + title);
                }
            }
            Logger.log(LogLevel.INFO, "[CandidateScreen] Added " + rowCount + " reservation rows to schedule");
        } else {
            Logger.log(LogLevel.WARNING, "[CandidateScreen] PROBLEM: currentCandidate is NULL or getReservations() is NULL!");
        }
        
        // Also add sessions if any
        if (sessions != null) {
            Logger.log(LogLevel.INFO, "[CandidateScreen] Also processing " + sessions.size() + " sessions...");
            
            for (MeetingSession session : sessions) {
                String title = (session != null) ? session.getTitle() : "Unknown Session";
                String startTime = (session != null && session.getReservation() != null && session.getReservation().getScheduledStart() != null) 
                                    ? session.getReservation().getScheduledStart().toString() 
                                    : "TBD";
                int duration = (session != null && session.getReservation() != null && session.getReservation().getOffer() != null)
                                ? session.getReservation().getOffer().getDurationMins()
                                : 0;
                String status = (session != null && session.getReservation() != null) 
                                ? session.getReservation().getStatus().toString() 
                                : "PENDING";
                
                // Only add if not already in table
                boolean exists = false;
                for (int i = 0; i < scheduleTableModel.getRowCount(); i++) {
                    if (title.equals(scheduleTableModel.getValueAt(i, 0))) {
                        exists = true;
                        break;
                    }
                }
                
                if (!exists) {
                    scheduleTableModel.addRow(new Object[]{
                        title,
                        startTime,
                        duration,
                        status
                    });
                    rowCount++;
                    Logger.log(LogLevel.INFO, "[CandidateScreen] Added 1 session row: " + title);
                }
            }
        }
        
        Logger.log(LogLevel.INFO, "[CandidateScreen] FINAL RESULT: Schedule table now has " + scheduleTableModel.getRowCount() + " total rows");
    }

    /**
     * Refresh the schedule table with candidate's latest reservations.
     * Called immediately after booking to update the My Schedule tab.
     * CRITICAL: Directly updates scheduleTableModel from UserSession candidate.
     */
    public void refreshScheduleTable() {
        if (scheduleTableModel == null) {
            Logger.log(LogLevel.ERROR, "[CandidateScreen] Schedule table model not initialized!");
            return;
        }
        
        Logger.log(LogLevel.INFO, "[CandidateScreen] refreshScheduleTable() CALLED");
        
        // ALWAYS get fresh candidate from UserSession (synced after booking)
        Candidate currentCandidate = vcfs.core.UserSession.getInstance().getCurrentCandidate();
        
        Logger.log(LogLevel.INFO, "[CandidateScreen] Got candidate: " + 
            (currentCandidate != null ? currentCandidate.getDisplayName() : "NULL"));
        
        // Clear existing schedule data
        scheduleTableModel.setRowCount(0);
        Logger.log(LogLevel.INFO, "[CandidateScreen] Cleared schedule table");
        
        int rowCount = 0;
        
        if (currentCandidate != null && currentCandidate.getReservations() != null) {
            Logger.log(LogLevel.INFO, "[CandidateScreen] Processing " + currentCandidate.getReservations().size() + " reservations...");
            
            for (vcfs.models.booking.Reservation reservation : currentCandidate.getReservations()) {
                if (reservation != null && reservation.getOffer() != null) {
                    String title = reservation.getOffer().getTitle() + " with " + 
                        (reservation.getOffer().getPublisher() != null ? 
                            reservation.getOffer().getPublisher().getDisplayName() : "Unknown");
                    String startTime = (reservation.getScheduledStart() != null) 
                        ? reservation.getScheduledStart().toString() : "TBD";
                    int duration = reservation.getOffer().getDurationMins();
                    String status = (reservation.getStatus() != null) ? reservation.getStatus() : "CONFIRMED";
                    
                    scheduleTableModel.addRow(new Object[]{title, startTime, duration, status});
                    rowCount++;
                    Logger.log(LogLevel.INFO, "[CandidateScreen] Added reservation: " + title);
                }
            }
        } else {
            Logger.log(LogLevel.WARNING, "[CandidateScreen] No candidate or reservations!");
        }
        
        Logger.log(LogLevel.INFO, "[CandidateScreen] Schedule table now has " + rowCount + " rows");
    }

    /**
     * Refresh the virtual room table with candidate's latest reservations.
     * Called automatically after booking to update the Join Virtual Room tab.
     * CRITICAL: Always fetches fresh candidate from UserSession to ensure sync.
     */
    public void refreshVirtualRoomTable() {
        Logger.log(LogLevel.INFO, "[CandidateScreen] refreshVirtualRoomTable() CALLED");
        
        if (roomTableModel == null) {
            Logger.log(LogLevel.ERROR, "[CandidateScreen] CRITICAL: Room table model is NULL - not initialized!");
            return;
        }

        try {
            // CRITICAL FIX: ALWAYS get fresh candidate from UserSession
            // This ensures virtual room table shows current bookings from backend
            Candidate currentCandidate = vcfs.core.UserSession.getInstance().getCurrentCandidate();
            Logger.log(LogLevel.INFO, "[CandidateScreen] Got candidate from UserSession: " + 
                (currentCandidate != null ? currentCandidate.getDisplayName() : "NULL"));
            
            if (currentCandidate == null) {
                Logger.log(LogLevel.ERROR, "[CandidateScreen] PROBLEM: currentCandidate is NULL!");
                return;
            }
            
            java.util.Collection<Reservation> reservations = currentCandidate.getReservations();
            Logger.log(LogLevel.INFO, "[CandidateScreen] Reservations list: " + 
                (reservations != null ? reservations.size() : "NULL") + " items");
            
            Logger.log(LogLevel.INFO, "[CandidateScreen] Clearing virtual room table...");
            roomTableModel.setRowCount(0);
            Logger.log(LogLevel.INFO, "[CandidateScreen] Table cleared. Current row count: " + roomTableModel.getRowCount());
            
            if (reservations != null && !reservations.isEmpty()) {
                Logger.log(LogLevel.INFO, "[CandidateScreen] Starting to add " + reservations.size() + " reservations to virtual room...");
                
                int roomNumber = 1;
                for (Reservation reservation : reservations) {
                    Logger.log(LogLevel.INFO, "[CandidateScreen] Processing reservation #" + roomNumber);
                    
                    if (reservation == null) {
                        Logger.log(LogLevel.WARNING, "[CandidateScreen] Reservation is NULL - skipping");
                        continue;
                    }
                    
                    Offer offer = reservation.getOffer();
                    if (offer == null) {
                        Logger.log(LogLevel.WARNING, "[CandidateScreen] Reservation has NULL offer - skipping");
                        continue;
                    }
                    
                    String interviewTitle = offer.getTitle() != null ? offer.getTitle() : "Interview";
                    String recruiterName = offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "Unknown";
                    String recruiterEmail = offer.getPublisher() != null && offer.getPublisher().getEmail() != null ? 
                        offer.getPublisher().getEmail() : "N/A";
                    int duration = offer.getDurationMins();
                    String company = "Company";
                    if (offer.getPublisher() != null && offer.getPublisher().getBooth() != null && 
                        offer.getPublisher().getBooth().getOrganization() != null) {
                        String orgName = offer.getPublisher().getBooth().getOrganization().getName();
                        company = orgName != null ? orgName : "Company";
                    }
                    String scheduledTime = reservation.getScheduledStart() != null ? 
                        reservation.getScheduledStart().toString() : "TBD";
                    String status = reservation.getStatus() != null ? 
                        reservation.getStatus() : "PENDING";
                    
                    Logger.log(LogLevel.INFO, "[CandidateScreen] Adding row: " + interviewTitle + " | " + recruiterName);
                    
                    roomTableModel.addRow(new Object[]{
                        interviewTitle,
                        recruiterName,
                        recruiterEmail,
                        duration,
                        company,
                        scheduledTime,
                        status,
                        "🎥 Join Room " + roomNumber
                    });
                    
                    Logger.log(LogLevel.INFO, "[CandidateScreen] Row added. Table now has " + roomTableModel.getRowCount() + " rows");
                    roomNumber++;
                }
            } else {
                Logger.log(LogLevel.WARNING, "[CandidateScreen] No reservations to display in virtual room");
            }
            
            Logger.log(LogLevel.INFO, "[CandidateScreen] FINAL: Virtual room table now has " + roomTableModel.getRowCount() + " rows");
            
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateScreen] EXCEPTION refreshing virtual room table: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
}


