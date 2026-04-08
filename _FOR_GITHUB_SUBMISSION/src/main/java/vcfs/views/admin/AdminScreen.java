package vcfs.views.admin;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import vcfs.controllers.AdminScreenController;
import vcfs.core.CareerFairSystem;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.enums.FairPhase;

/**
 * Administrator GUI Screen (Phase 4 - UI Integration).
 *
 * Responsibilities:
 * - Allow admin to create Organizations and Booths
 * - Allow admin to set the system timeline
 * - Display Audit Log via PropertyChangeListener
 *
 * NOTE: All business logic is in AdminScreenController. This class ONLY handles UI.
 *
 * Original implementation by: YAMI (leiyam)
 * Adapted to skeleton by: Zaid
 */
public class AdminScreen extends JFrame implements PropertyChangeListener {

    // ===== CONTROLLER =====
    private AdminScreenController controller;

    // ===== UI COMPONENTS =====
    private JTextField orgField;
    private JTextField boothField;
    private JTextField recruiterField;
    private JComboBox<String> orgDropdown;
    private JComboBox<String> boothDropdown;

    // Timeline fields — Replaced with Calendar & Time Pickers
    private JSpinner openDateSpinner;
    private JSpinner openTimeSpinner;
    private JSpinner closeTimeSpinner;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;

    // Audit log display
    private JTextArea auditArea;
    
    // ===== TABLE MODELS FOR AUTO-REFRESH =====
    private javax.swing.table.DefaultTableModel organizationsTableModel;
    private javax.swing.table.DefaultTableModel recruitersTableModel;
    private javax.swing.table.DefaultTableModel candidatesTableModel;
    private javax.swing.table.DefaultTableModel offersTableModel;
    
    // ===== DASHBOARD METRICS =====
    private JLabel phaseIndicatorLabel;
    private JLabel orgCountLabel;
    private JLabel recruiterCountLabel;
    private JLabel candidateCountLabel;
    private JLabel timelineStatusLabel;

    /**
     * Constructor - build the UI and wire controllers
     * @param controller The AdminScreenController that handles all business logic
     */
    public AdminScreen(AdminScreenController controller) {
        this.controller = controller;

        // ===== FRAME SETUP =====
        setTitle("VCFS - Administrator Console");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(900, 600));
        setLayout(new BorderLayout(10, 10));
        
        // Set modern look and feel
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ((JPanel) getContentPane()).setBackground(new Color(240, 245, 250));

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 70, 130));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Administrator Console");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Manage Fair Configuration & Setup");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(25, 70, 130));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Add BACK button to header right side
        JButton backBtn = createStyledButton("↶ BACK", new Color(255, 152, 0));
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Return to Main Menu? Any unsaved changes will be lost.",
                "Confirm Navigation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new vcfs.views.shared.MainMenuFrame();
                dispose();
            }
        });
        
        JPanel headerButtonPanel = new JPanel();
        headerButtonPanel.setBackground(new Color(25, 70, 130));
        headerButtonPanel.add(backBtn);
        headerPanel.add(headerButtonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // ===== TABBED INTERFACE =====
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
        tabbedPane.setBackground(new Color(240, 245, 250));

        // ===== TAB 1: MANAGEMENT (Forms) =====
        JPanel managementTab = createManagementTab();
        tabbedPane.addTab("⚙️ Setup & Configuration", managementTab);

        // ===== TAB 2: DATA VIEW - ORGANIZATIONS =====
        JPanel organizationsTab = createOrganizationsTab();
        tabbedPane.addTab("🏢 Organizations", organizationsTab);

        // ===== TAB 3: DATA VIEW - RECRUITERS =====
        JPanel recruitersTab = createRecruitersTab();
        tabbedPane.addTab("👔 Recruiters", recruitersTab);

        // ===== TAB 4: DATA VIEW - CANDIDATES =====
        JPanel candidatesTab = createCandidatesTab();
        tabbedPane.addTab("👨 Candidates", candidatesTab);

        // ===== TAB 5: DATA VIEW - OFFERS =====
        JPanel offersTab = createOffersTab();
        tabbedPane.addTab("📋 Offers", offersTab);

        // ===== TAB 6: AUDIT LOG =====
        JPanel auditTab = createAuditLogTab();
        tabbedPane.addTab("📊 Audit Log & System Events", auditTab);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Observer callback - receives system notifications
     * Called when CareerFairSystem broadcasts PropertyChange events
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        
        // Log time and audit events
        if ("auditLog".equals(prop) || "time".equals(prop)) {
            auditArea.append("[SYSTEM EVENT] " + prop + ": " + evt.getNewValue() + "\n");
            // Auto-scroll to bottom
            auditArea.setCaretPosition(auditArea.getDocument().getLength());
        }
        
        // CRITICAL FIX: Refresh UI when data changes
        // This ensures all portals see updates from other portals immediately
        if ("organizations".equals(prop)) {
            Logger.log(LogLevel.INFO, "[AdminScreen] Organizations updated - refreshing dropdowns");
            refreshOrganizationDropdown();
        }
        if ("booths".equals(prop)) {
            Logger.log(LogLevel.INFO, "[AdminScreen] Booths updated - refreshing booth dropdown");
            refreshBoothDropdown();
        }
        if ("recruiters".equals(prop) || "candidates".equals(prop) || "offers".equals(prop)) {
            Logger.log(LogLevel.INFO, "[AdminScreen] System data updated - refreshing display");
            refreshDisplay();
        }
    }

    /**
     * Refresh the display with latest system data.
     * Called when system state changes via PropertyChangeListener.
     * Updates all dashboard metrics with current organization, recruiter, candidate counts.
     */
    public void refreshDisplay() {
        Logger.log(LogLevel.INFO, "[AdminScreen] Refreshing display with latest data");
        
        try {
            CareerFairSystem system = CareerFairSystem.getInstance();
            
            // Update phase indicator with color coding
            FairPhase phase = system.getCurrentPhase();
            String phaseText = "🎯 Fair Phase: " + phase.name();
            java.awt.Color phaseColor = switch (phase) {
                case DORMANT -> new Color(200, 50, 50); // Red
                case PREPARING -> new Color(255, 152, 0); // Orange
                case BOOKINGS_OPEN -> new Color(76, 175, 80); // Green
                case BOOKINGS_CLOSED -> new Color(255, 193, 7); // Amber
                case FAIR_LIVE -> new Color(63, 81, 181); // Blue
            };
            phaseIndicatorLabel.setText(phaseText);
            phaseIndicatorLabel.setForeground(phaseColor);
            
            // Update entity counts from system
            int orgCount = system.getAllOrganizations().size();
            orgCountLabel.setText("🏢 Organizations: " + orgCount);
            
            // Get all offers as proxy for recruiter count (offers come from recruiters)
            java.util.List<vcfs.models.booking.Offer> allOffers = system.getAllOffers();
            java.util.Set<String> uniqueRecruiters = new java.util.HashSet<>();
            if (allOffers != null) {
                for (vcfs.models.booking.Offer offer : allOffers) {
                    if (offer.getPublisher() != null) {
                        uniqueRecruiters.add(offer.getPublisher().getEmail());
                    }
                }
            }
            recruiterCountLabel.setText("👔 Recruiters: " + uniqueRecruiters.size());
            
            // Try to count candidates - might be stored in system
            int candidateCount = 0;
            try {
                java.util.List<?> candidates = (java.util.List<?>) system.getClass()
                    .getMethod("getAllCandidates").invoke(system);
                if (candidates != null) candidateCount = candidates.size();
            } catch (Exception e) {
                // Candidates might not have a getter - that's ok
                candidateCount = 0;
            }
            candidateCountLabel.setText("👨 Candidates: " + candidateCount);
            
            // AUTO-REFRESH ALL TABLES
            refreshOrganizationsTable();
            refreshRecruitersTable();
            refreshCandidatesTable();
            refreshOffersTable();
            Logger.log(LogLevel.INFO, "[AdminScreen] All tables refreshed");
            
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreen] Error refreshing display", e);
        }
    }

    /**
     * Helper method: Create a styled section panel with title and background
     */
    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(new Color(25, 70, 130));
        
        return panel;
    }

    /**
     * Helper method: Create a styled button with color
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 35));
        return btn;
    }

    /**
     * Helper method: Create a timestamp input field with validation
     */
    private JTextField createTimestampField(String defaultValue) {
        JTextField field = new JTextField(defaultValue, 25);
        field.setFont(new Font("Monospaced", Font.PLAIN, 11));
        field.setBackground(new Color(250, 250, 250));
        field.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        return field;
    }

    /**
     * Helper method: Create a styled label with consistent formatting
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    // =========================================================
    // P2 UI UPGRADE: CALENDAR & TIME PICKER COMPONENTS
    // ZAID (mzs00007) — Easy date/time selection with visual components
    // =========================================================

    /**
     * Create a time picker panel with calendar date + time spinner.
     * Uses JSpinner for both date and time selection.
     * 
     * @param label Display label for this time picker
     * @param calendar Initial calendar value
     * @param defaultHour Default hour (0-23)
     * @param defaultMin Default minute (0-59)
     * @return Panel containing calendar icon button + date/time spinners
     */
    private JPanel CreateTimePickerPanel(String label, Calendar calendar, int defaultHour, int defaultMin) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Label with emoji
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 11));
        labelComponent.setPreferredSize(new Dimension(180, 30));
        panel.add(labelComponent);

        // === DATE PICKER (Calendar Icon Button + Display) ===
        Date initialDate = calendar.getTime();
        SpinnerDateModel dateModel = new SpinnerDateModel(initialDate, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(120, 30));
        dateSpinner.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        
        // Add calendar icon button next to date spinner
        JButton calendarBtn = new JButton("📅");
        calendarBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        calendarBtn.setPreferredSize(new Dimension(40, 30));
        calendarBtn.setBackground(new Color(33, 150, 243));
        calendarBtn.setForeground(Color.WHITE);
        calendarBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        calendarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        calendarBtn.addActionListener(e -> {
            // Show a simple date picker dialog
            JSpinner tempSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor editor = new JSpinner.DateEditor(tempSpinner, "yyyy-MM-dd");
            tempSpinner.setEditor(editor);
            
            int result = JOptionPane.showConfirmDialog(panel, tempSpinner, 
                "Select Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                dateSpinner.setValue(tempSpinner.getValue());
            }
        });
        
        panel.add(calendarBtn);
        panel.add(dateSpinner);

        // === TIME PICKER (Hour:Minute Spinners) ===
        SpinnerDateModel timeModel = new SpinnerDateModel();
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.set(Calendar.HOUR_OF_DAY, defaultHour);
        timeCalendar.set(Calendar.MINUTE, defaultMin);
        timeCalendar.set(Calendar.SECOND, 0);
        timeModel.setValue(timeCalendar.getTime());
        
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setPreferredSize(new Dimension(80, 30));
        timeSpinner.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        
        JButton timeBtn = new JButton("🕐");
        timeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        timeBtn.setPreferredSize(new Dimension(40, 30));
        timeBtn.setBackground(new Color(76, 175, 80));
        timeBtn.setForeground(Color.WHITE);
        timeBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        timeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(timeBtn);
        panel.add(timeSpinner);

        // Store spinners as client properties for later retrieval
        panel.putClientProperty("dateSpinner", dateSpinner);
        panel.putClientProperty("timeSpinner", timeSpinner);

        return panel;
    }

    /**
     * Format Date objects from spinners into YYYY-MM-DDTHH:MM format.
     * Combines the date from dateSpinner with time from timeSpinner.
     * 
     * @param dateValue Date object from date spinner
     * @param timeValue Date object from time spinner
     * @return Formatted string in YYYY-MM-DDTHH:MM format
     */
    private String formatDatetimeFromSpinners(Date dateValue, Date timeValue) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(dateValue);
        
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(timeValue);
        
        // Combine: date from dateCal + time from timeCal
        int year = dateCal.get(Calendar.YEAR);
        int month = dateCal.get(Calendar.MONTH) + 1;
        int day = dateCal.get(Calendar.DAY_OF_MONTH);
        int hour = timeCal.get(Calendar.HOUR_OF_DAY);
        int min = timeCal.get(Calendar.MINUTE);
        
        return String.format("%04d-%02d-%02dT%02d:%02d", year, month, day, hour, min);
    }

    // =========================================================
    // P3 UI ENHANCEMENT: STATUS DASHBOARD & SELF-EXPLANATORY UI
    // ZAID (mzs00007) — Admin features + visual guidance + metrics
    // =========================================================

    /**
     * Create a status dashboard panel showing:
     * - Current fair phase
     * - Organization count
     * - Recruiter count  
     * - Timeline status
     * - Quick action buttons to switch roles
     * 
     * Makes the UI self-explanatory by showing system state at a glance.
     */
    private JPanel createStatusDashboard() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));
        dashboardPanel.setBackground(new Color(230, 245, 255));
        dashboardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // ===== LEFT SECTION: METRICS =====
        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 15, 10));
        metricsPanel.setBackground(new Color(230, 245, 255));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Phase Indicator
        phaseIndicatorLabel = new JLabel("🎯 Fair Phase: DORMANT");
        phaseIndicatorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        phaseIndicatorLabel.setForeground(new Color(200, 50, 50));
        metricsPanel.add(phaseIndicatorLabel);

        // Organization Count
        orgCountLabel = new JLabel("🏢 Organizations: 0");
        orgCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        orgCountLabel.setForeground(new Color(33, 133, 200));
        metricsPanel.add(orgCountLabel);

        // Recruiter Count
        recruiterCountLabel = new JLabel("👔 Recruiters: 0");
        recruiterCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        recruiterCountLabel.setForeground(new Color(76, 175, 80));
        metricsPanel.add(recruiterCountLabel);

        // Candidate Count
        candidateCountLabel = new JLabel("👨 Candidates: 0");
        candidateCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        candidateCountLabel.setForeground(new Color(156, 39, 176));
        metricsPanel.add(candidateCountLabel);

        dashboardPanel.add(metricsPanel, BorderLayout.WEST);

        // ===== CENTER SECTION: WORKFLOW GUIDANCE =====
        JPanel guidancePanel = new JPanel();
        guidancePanel.setLayout(new BoxLayout(guidancePanel, BoxLayout.Y_AXIS));
        guidancePanel.setBackground(new Color(230, 245, 255));

        JLabel workflowTitle = new JLabel("📋 ADMIN SETUP WORKFLOW:");
        workflowTitle.setFont(new Font("Arial", Font.BOLD, 11));
        workflowTitle.setForeground(new Color(25, 70, 130));
        guidancePanel.add(workflowTitle);

        JLabel step1 = new JLabel("Step 1️⃣  Create Organizations");
        step1.setFont(new Font("Arial", Font.PLAIN, 10));
        guidancePanel.add(step1);

        JLabel step2 = new JLabel("Step 2️⃣  Add Booths to Organizations");
        step2.setFont(new Font("Arial", Font.PLAIN, 10));
        guidancePanel.add(step2);

        JLabel step3 = new JLabel("Step 3️⃣  Assign Recruiters to Booths");
        step3.setFont(new Font("Arial", Font.PLAIN, 10));
        guidancePanel.add(step3);

        JLabel step4 = new JLabel("Step 4️⃣  Configure Timeline (Calendar Dates/Times)");
        step4.setFont(new Font("Arial", Font.PLAIN, 10));
        guidancePanel.add(step4);

        JLabel step5 = new JLabel("Step 5️⃣  Monitor System via Audit Log Below");
        step5.setFont(new Font("Arial", Font.PLAIN, 10));
        guidancePanel.add(step5);

        dashboardPanel.add(guidancePanel, BorderLayout.CENTER);

        // ===== RIGHT SECTION: QUICK NAVIGATION =====
        JPanel navPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        navPanel.setBackground(new Color(230, 245, 255));

        JButton viewRecruiterPortalBtn = createStyledButton("👔 View Recruiter Portal", new Color(63, 81, 181));
        viewRecruiterPortalBtn.setPreferredSize(new Dimension(150, 35));
        viewRecruiterPortalBtn.setToolTipText("Switch to Recruiter Portal to manage offers and interviews");
        viewRecruiterPortalBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[AdminScreen] Navigating to Recruiter Portal");
            new vcfs.views.recruiter.RecruiterScreen();
            dispose();
        });
        navPanel.add(viewRecruiterPortalBtn);

        JButton viewCandidatePortalBtn = createStyledButton("👨 View Candidate Portal", new Color(156, 39, 176));
        viewCandidatePortalBtn.setPreferredSize(new Dimension(150, 35));
        viewCandidatePortalBtn.setToolTipText("Switch to Candidate Portal to monitor registrations");
        viewCandidatePortalBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[AdminScreen] Navigating to Candidate Portal");
            new vcfs.views.candidate.CandidateScreen();
            dispose();
        });
        navPanel.add(viewCandidatePortalBtn);

        dashboardPanel.add(navPanel, BorderLayout.EAST);

        // Timeline Status
        timelineStatusLabel = new JLabel("⏰ Timeline: Not Configured");
        timelineStatusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timelineStatusLabel.setForeground(new Color(200, 100, 0));
        dashboardPanel.add(timelineStatusLabel, BorderLayout.SOUTH);

        return dashboardPanel;
    }

    /**
     * Refresh organization dropdown from system data
     */
    private void refreshOrganizationDropdown() {
        if (orgDropdown == null) return;
        orgDropdown.removeAllItems();
        try {
            for (vcfs.models.structure.Organization org : CareerFairSystem.getInstance().getAllOrganizations()) {
                orgDropdown.addItem(org.getName());
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreen] Error loading organizations to dropdown", e);
        }
    }

    /**
     * Refresh booth dropdown based on selected organization
     */
    private void refreshBoothDropdown() {
        if (boothDropdown == null || orgDropdown == null) return;
        boothDropdown.removeAllItems();
        Object selectedOrg = orgDropdown.getSelectedItem();
        if (selectedOrg == null) return;
        
        try {
            vcfs.models.structure.Organization org = CareerFairSystem.getInstance().getOrganizationByName(selectedOrg.toString());
            if (org != null) {
                for (vcfs.models.structure.Booth booth : org.getBooths()) {
                    boothDropdown.addItem(booth.getTitle());
                }
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[AdminScreen] Error loading booths to dropdown", e);
        }
    }

    // ===== NEW TAB CREATION METHODS =====

    private JPanel createManagementTab() {
        JPanel tab = new JPanel();
        tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
        tab.setBackground(new Color(240, 245, 250));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== GETTING STARTED GUIDANCE PANEL =====
        JPanel gettingStartedPanel = new JPanel(new BorderLayout());
        gettingStartedPanel.setBackground(new Color(255, 240, 200));
        gettingStartedPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 165, 0), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel gettingStartedTitle = new JLabel("🚀 GETTING STARTED - Complete This Setup:");
        gettingStartedTitle.setFont(new Font("Arial", Font.BOLD, 13));
        gettingStartedTitle.setForeground(new Color(200, 100, 0));
        
        JPanel guidanceTextPanel = new JPanel();
        guidanceTextPanel.setLayout(new BoxLayout(guidanceTextPanel, BoxLayout.Y_AXIS));
        guidanceTextPanel.setBackground(new Color(255, 240, 200));
        guidanceTextPanel.add(gettingStartedTitle);
        
        String[] steps = {
            "1️⃣  CREATE ORGANIZATIONS - Set up company booths that will participate",
            "2️⃣  ADD BOOTHS - Create booth spaces within each organization",
            "3️⃣  ASSIGN RECRUITERS - Link recruiters to booths with email addresses",
            "4️⃣  CONFIGURE TIMELINE - Set fair opening/closing and event start/end times",
            "5️⃣  SWITCH TO OTHER TABS - View Organizations, Recruiters, Candidates, and Offers",
            "6️⃣  MONITOR AUDIT LOG - Track all system events in real-time"
        };
        
        for (String step : steps) {
            JLabel stepLabel = new JLabel(step);
            stepLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            stepLabel.setForeground(new Color(100, 50, 0));
            guidanceTextPanel.add(stepLabel);
            guidanceTextPanel.add(Box.createVerticalStrut(2));
        }
        
        gettingStartedPanel.add(guidanceTextPanel, BorderLayout.WEST);
        tab.add(gettingStartedPanel);
        tab.add(Box.createVerticalStrut(10));

        // Dashboard at top
        JPanel dashboardPanel = createStatusDashboard();
        tab.add(dashboardPanel);
        tab.add(Box.createVerticalStrut(10));

        // Scroll panel for forms
        JPanel formsPanel = new JPanel();
        formsPanel.setLayout(new BoxLayout(formsPanel, BoxLayout.Y_AXIS));
        formsPanel.setBackground(Color.WHITE);
        formsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Section 1: Organization Management
        JPanel orgSection = createSectionPanel("1️⃣  CREATE ORGANIZATION");
        JLabel orgDescLabel = new JLabel("💡 Add participating companies to the fair. Each org will have booths.");
        orgDescLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        orgDescLabel.setForeground(new Color(100, 100, 100));
        orgField = new JTextField(30);
        JButton createOrgBtn = createStyledButton("✓ Create", new Color(76, 175, 80));
        JPanel orgInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        orgInputPanel.setBackground(Color.WHITE);
        orgInputPanel.add(new JLabel("Organization Name:"));
        orgInputPanel.add(orgField);
        orgInputPanel.add(createOrgBtn);
        orgSection.setLayout(new BorderLayout(5, 5));
        orgSection.add(orgDescLabel, BorderLayout.NORTH);
        orgSection.add(orgInputPanel, BorderLayout.CENTER);
        formsPanel.add(orgSection);
        formsPanel.add(Box.createVerticalStrut(10));

        // Section 2: Booth Management
        JPanel boothSection = createSectionPanel("2️⃣  CREATE BOOTH");
        JLabel boothDescLabel = new JLabel("💡 Create booth spaces within each organization for recruiters to operate.");
        boothDescLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        boothDescLabel.setForeground(new Color(100, 100, 100));
        boothField = new JTextField(15);
        orgDropdown = new JComboBox<>();
        JButton createBoothBtn = createStyledButton("✓ Create", new Color(76, 175, 80));
        JPanel boothInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        boothInputPanel.setBackground(Color.WHITE);
        boothInputPanel.add(new JLabel("Booth Name:"));
        boothInputPanel.add(boothField);
        boothInputPanel.add(new JLabel("  Organization:"));
        boothInputPanel.add(orgDropdown);
        boothInputPanel.add(createBoothBtn);
        boothSection.setLayout(new BorderLayout(5, 5));
        boothSection.add(boothDescLabel, BorderLayout.NORTH);
        boothSection.add(boothInputPanel, BorderLayout.CENTER);
        formsPanel.add(boothSection);
        formsPanel.add(Box.createVerticalStrut(10));

        // Section 3: Recruiter Assignment
        JPanel recruiterSection = createSectionPanel("3️⃣  ASSIGN RECRUITER");
        JLabel recruiterDescLabel = new JLabel("💡 Assign recruiters (by email) to specific booth spaces. This email will be used for recruiter login.");
        recruiterDescLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        recruiterDescLabel.setForeground(new Color(100, 100, 100));
        recruiterField = new JTextField(15);
        boothDropdown = new JComboBox<>();
        JButton assignRecruiterBtn = createStyledButton("✓ Assign", new Color(33, 150, 243));
        JPanel recruiterInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        recruiterInputPanel.setBackground(Color.WHITE);
        recruiterInputPanel.add(new JLabel("Recruiter Email:"));
        recruiterInputPanel.add(recruiterField);
        recruiterInputPanel.add(new JLabel("  Booth:"));
        recruiterInputPanel.add(boothDropdown);
        recruiterInputPanel.add(assignRecruiterBtn);
        recruiterSection.setLayout(new BorderLayout(5, 5));
        recruiterSection.add(recruiterDescLabel, BorderLayout.NORTH);
        recruiterSection.add(recruiterInputPanel, BorderLayout.CENTER);
        formsPanel.add(recruiterSection);
        formsPanel.add(Box.createVerticalStrut(10));
        
        // Initialize dropdowns with system data
        refreshOrganizationDropdown();
        
        // When organization selection changes, refresh booth dropdown
        orgDropdown.addItemListener(e -> refreshBoothDropdown());

        // Section 4: Timeline
        JPanel timelineSection = createSectionPanel("4️⃣  CONFIGURE FAIR TIMELINE");
        JLabel timelineDescLabel = new JLabel("💡 Set when the fair booking window opens/closes and when the actual fair events run (start/end times).");
        timelineDescLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        timelineDescLabel.setForeground(new Color(100, 100, 100));
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.APRIL, 8, 9, 0);
        openDateSpinner = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH));
        openTimeSpinner = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        closeTimeSpinner = new JSpinner(new SpinnerNumberModel(12, 0, 23, 1));
        startTimeSpinner = new JSpinner(new SpinnerNumberModel(13, 0, 23, 1));
        endTimeSpinner = new JSpinner(new SpinnerNumberModel(17, 0, 23, 1));
        
        JPanel timelineInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        timelineInputPanel.setBackground(Color.WHITE);
        timelineInputPanel.add(new JLabel("Booking Opens:"));
        timelineInputPanel.add(openDateSpinner);
        timelineInputPanel.add(openTimeSpinner);
        timelineInputPanel.add(new JLabel("  Closes:"));
        timelineInputPanel.add(closeTimeSpinner);
        
        JLabel fairStartLabel = new JLabel("Fair Runs:  ");
        fairStartLabel.setFont(new Font("Arial", Font.BOLD, 11));
        JPanel fairRunPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        fairRunPanel.setBackground(Color.WHITE);
        fairRunPanel.add(fairStartLabel);
        fairRunPanel.add(new JLabel("Start Time:"));
        fairRunPanel.add(startTimeSpinner);
        fairRunPanel.add(new JLabel("  End Time:"));
        fairRunPanel.add(endTimeSpinner);
        
        JButton setTimelineBtn = createStyledButton("✓ Set Timeline", new Color(76, 175, 80));
        JButton resetBtn = createStyledButton("⟲ Reset System", new Color(244, 67, 54));
        JPanel timelineBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        timelineBtnPanel.setBackground(Color.WHITE);
        timelineBtnPanel.add(setTimelineBtn);
        timelineBtnPanel.add(resetBtn);
        
        timelineSection.setLayout(new BorderLayout(5, 5));
        JPanel timelineAllPanel = new JPanel();
        timelineAllPanel.setLayout(new BoxLayout(timelineAllPanel, BoxLayout.Y_AXIS));
        timelineAllPanel.setBackground(Color.WHITE);
        timelineAllPanel.add(timelineInputPanel);
        timelineAllPanel.add(fairRunPanel);
        timelineAllPanel.add(timelineBtnPanel);
        timelineSection.add(timelineDescLabel, BorderLayout.NORTH);
        timelineSection.add(timelineAllPanel, BorderLayout.CENTER);
        formsPanel.add(timelineSection);

        JScrollPane scroll = new JScrollPane(formsPanel);
        scroll.setBorder(null);
        tab.add(scroll);

        // Button handlers
        createOrgBtn.addActionListener(e -> {
            String name = orgField.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter organization name"); return; }
            try { 
                controller.createOrganization(name); 
                refreshOrganizationDropdown();
                orgField.setText("");
                JOptionPane.showMessageDialog(this, "✓ Organization created!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        createBoothBtn.addActionListener(e -> {
            String booth = boothField.getText().trim();
            Object org = orgDropdown.getSelectedItem();
            if (booth.isEmpty() || org == null) { JOptionPane.showMessageDialog(this, "Please fill all fields"); return; }
            try {
                controller.createBooth(booth, org.toString());
                refreshBoothDropdown();
                boothField.setText("");
                JOptionPane.showMessageDialog(this, "✓ Booth created!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        assignRecruiterBtn.addActionListener(e -> {
            String recruiter = recruiterField.getText().trim();
            Object booth = boothDropdown.getSelectedItem();
            if (recruiter.isEmpty() || booth == null) { JOptionPane.showMessageDialog(this, "Please fill all fields"); return; }
            try {
                controller.assignRecruiter(recruiter, booth.toString());
                recruiterField.setText("");
                JOptionPane.showMessageDialog(this, "✓ Recruiter assigned!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        setTimelineBtn.addActionListener(e -> {
            try {
                String openStr = String.format("2026-04-08T%02d:00", ((Number)openTimeSpinner.getValue()).intValue());
                String closeStr = String.format("2026-04-08T%02d:00", ((Number)closeTimeSpinner.getValue()).intValue());
                String startStr = String.format("2026-04-08T%02d:00", ((Number)startTimeSpinner.getValue()).intValue());
                String endStr = String.format("2026-04-08T%02d:00", ((Number)endTimeSpinner.getValue()).intValue());
                controller.setTimeline(openStr, closeStr, startStr, endStr);
                JOptionPane.showMessageDialog(this, "✓ Timeline set!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        resetBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Reset all system data?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try { controller.resetFair(); orgDropdown.removeAllItems(); boothDropdown.removeAllItems(); JOptionPane.showMessageDialog(this, "✓ System reset!"); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        return tab;
    }

    private JPanel createOrganizationsTab() {
        JPanel tab = new JPanel(new BorderLayout());
        tab.setBackground(new Color(240, 245, 250));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with guidance
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 245, 250));
        
        JLabel titleLabel = new JLabel("🏢 ALL ORGANIZATIONS PARTICIPATING IN FAIR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel descLabel = new JLabel("Create organizations in the Setup tab. Each organization can have multiple booths. Watch the Booths count increase as you add them.");
        descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        descLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 245, 250));
        titlePanel.add(titleLabel);
        titlePanel.add(descLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        tab.add(headerPanel, BorderLayout.NORTH);

        this.organizationsTableModel = new javax.swing.table.DefaultTableModel(
            new String[]{"Organization Name", "Booths", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(this.organizationsTableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JButton refreshBtn = new JButton("🔄 Refresh Data");
        refreshBtn.addActionListener(e -> refreshOrganizationsTable());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 245, 250));
        btnPanel.add(refreshBtn);
        tab.add(btnPanel, BorderLayout.SOUTH);

        // Add responsive scrolling (vertical AND horizontal)
        JScrollPane scrollPane = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tab.add(scrollPane, BorderLayout.CENTER);
        
        // Load data initially
        refreshBtn.doClick();
        
        return tab;
    }
    
    private void refreshOrganizationsTable() {
        if (this.organizationsTableModel == null) return;
        this.organizationsTableModel.setRowCount(0);
        try {
            for (vcfs.models.structure.Organization org : CareerFairSystem.getInstance().getAllOrganizations()) {
                this.organizationsTableModel.addRow(new Object[]{org.getName(), org.getBooths().size(), "Active"});
            }
        } catch (Exception ex) { Logger.log(LogLevel.ERROR, "Error loading orgs", ex); }
    }

    private JPanel createRecruitersTab() {
        JPanel tab = new JPanel(new BorderLayout());
        tab.setBackground(new Color(240, 245, 250));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with guidance
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 245, 250));
        
        JLabel titleLabel = new JLabel("👔 ALL RECRUITERS REGISTERED IN SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel descLabel = new JLabel("You must assign recruiters in the Setup tab FIRST. This table shows all registered recruiters and their published offers.");
        descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        descLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 245, 250));
        titlePanel.add(titleLabel);
        titlePanel.add(descLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        tab.add(headerPanel, BorderLayout.NORTH);

        this.recruitersTableModel = new javax.swing.table.DefaultTableModel(
            new String[]{"Recruiter Name", "Email", "Organization", "Offers Published"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(this.recruitersTableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JButton refreshBtn = new JButton("🔄 Refresh Data");
        refreshBtn.addActionListener(e -> refreshRecruitersTable());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 245, 250));
        btnPanel.add(refreshBtn);
        tab.add(btnPanel, BorderLayout.SOUTH);

        // Add responsive scrolling (vertical AND horizontal)
        JScrollPane scrollPane = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tab.add(scrollPane, BorderLayout.CENTER);
        refreshBtn.doClick();
        return tab;
    }
    
    private void refreshRecruitersTable() {
        if (this.recruitersTableModel == null) return;
        this.recruitersTableModel.setRowCount(0);
        try {
            // CRITICAL FIX: Get recruiters directly from system, not from offers
            // This ensures ALL recruiters are shown, even if they haven't published offers yet
            java.util.List<vcfs.models.users.Recruiter> allRecruiters = 
                CareerFairSystem.getInstance().getAllRecruiters();
            
            if (allRecruiters.isEmpty()) {
                this.recruitersTableModel.addRow(new Object[]{"(No recruiters yet)", "-", "-", "0"});
                return;
            }
            
            // Display each recruiter with their offer count
            for (vcfs.models.users.Recruiter rec : allRecruiters) {
                int offerCount = 0;
                for (vcfs.models.booking.Offer o : CareerFairSystem.getInstance().getAllOffers()) {
                    if (o.getPublisher() != null && o.getPublisher().getEmail().equals(rec.getEmail())) 
                        offerCount++;
                }
                this.recruitersTableModel.addRow(new Object[]{
                    rec.getDisplayName(), 
                    rec.getEmail(), 
                    "N/A", 
                    offerCount
                });
            }
        } catch (Exception ex) { 
            Logger.log(LogLevel.ERROR, "Error loading recruiters", ex); 
        }
    }

    private JPanel createCandidatesTab() {
        JPanel tab = new JPanel(new BorderLayout());
        tab.setBackground(new Color(240, 245, 250));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with guidance
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 245, 250));
        
        JLabel titleLabel = new JLabel("👨 ALL REGISTERED CANDIDATES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel descLabel = new JLabel("Candidates register through the Candidate Portal. They can search for offers published by recruiters and book meetings.");
        descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        descLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 245, 250));
        titlePanel.add(titleLabel);
        titlePanel.add(descLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        tab.add(headerPanel, BorderLayout.NORTH);

        this.candidatesTableModel = new javax.swing.table.DefaultTableModel(
            new String[]{"Candidate Name", "Email", "Bio", "Skills"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(this.candidatesTableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JButton refreshBtn = new JButton("🔄 Refresh Data");
        refreshBtn.addActionListener(e -> refreshCandidatesTable());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 245, 250));
        btnPanel.add(refreshBtn);
        tab.add(btnPanel, BorderLayout.SOUTH);

        // Add responsive scrolling (vertical AND horizontal)
        JScrollPane scrollPane = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tab.add(scrollPane, BorderLayout.CENTER);
        refreshBtn.doClick();
        return tab;
    }
    
    private void refreshCandidatesTable() {
        if (this.candidatesTableModel == null) return;
        this.candidatesTableModel.setRowCount(0);
        try {
            java.lang.reflect.Method m = CareerFairSystem.getInstance().getClass().getMethod("getAllCandidates");
            java.util.List<?> candidates = (java.util.List<?>) m.invoke(CareerFairSystem.getInstance());
            if (candidates != null) {
                for (Object c : candidates) {
                    if (c instanceof vcfs.models.users.Candidate) {
                        vcfs.models.users.Candidate cand = (vcfs.models.users.Candidate) c;
                        String skills = (cand.getProfile() != null) ? cand.getProfile().getInterestTags() : "N/A";
                        String bio = (cand.getProfile() != null) ? cand.getProfile().getCvSummary() : "N/A";
                        this.candidatesTableModel.addRow(new Object[]{cand.getDisplayName(), cand.getEmail(), bio, skills});
                    }
                }
            }
            if (this.candidatesTableModel.getRowCount() == 0) {
                this.candidatesTableModel.addRow(new Object[]{"(No candidates yet)", "-", "-", "-"});
            }
        } catch (Exception ex) { Logger.log(LogLevel.ERROR, "Error loading candidates", ex); }
    }

    private JPanel createOffersTab() {
        JPanel tab = new JPanel(new BorderLayout());
        tab.setBackground(new Color(240, 245, 250));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with guidance
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 245, 250));
        
        JLabel titleLabel = new JLabel("📋 ALL PUBLISHED OFFERS - CANDIDATES WILL SEE THESE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel descLabel = new JLabel("Offers are created by recruiters in the Recruiter Portal. Watch this list populate as recruiters publish opportunities.");
        descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        descLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 245, 250));
        titlePanel.add(titleLabel);
        titlePanel.add(descLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        tab.add(headerPanel, BorderLayout.NORTH);

        this.offersTableModel = new javax.swing.table.DefaultTableModel(
            new String[]{"Offer Title", "Recruiter", "Duration", "Tags", "Bookings", "Capacity"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(this.offersTableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JButton refreshBtn = new JButton("🔄 Refresh Data");
        refreshBtn.addActionListener(e -> refreshOffersTable());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 245, 250));
        btnPanel.add(refreshBtn);
        tab.add(btnPanel, BorderLayout.SOUTH);

        // Add responsive scrolling (vertical AND horizontal)
        JScrollPane scrollPane = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tab.add(scrollPane, BorderLayout.CENTER);
        refreshBtn.doClick();
        return tab;
    }
    
    private void refreshOffersTable() {
        if (this.offersTableModel == null) return;
        this.offersTableModel.setRowCount(0);
        try {
            for (vcfs.models.booking.Offer offer : CareerFairSystem.getInstance().getAllOffers()) {
                String recruiter = (offer.getPublisher() != null) ? offer.getPublisher().getDisplayName() : "Unknown";
                int booked = (offer.getReservations() != null) ? offer.getReservations().size() : 0;
                this.offersTableModel.addRow(new Object[]{
                    offer.getTitle(),
                    recruiter,
                    "30 min",
                    offer.getTopicTags(),
                    booked,
                    offer.getCapacity()
                });
            }
            if (this.offersTableModel.getRowCount() == 0) {
                this.offersTableModel.addRow(new Object[]{"(No offers yet)", "-", "-", "-", "0", "0"});
            }
        } catch (Exception ex) { Logger.log(LogLevel.ERROR, "Error loading offers", ex); }
    }

    private JPanel createAuditLogTab() {
        JPanel tab = new JPanel(new BorderLayout());
        tab.setBackground(new Color(240, 245, 250));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("📊 System Audit Log & Events");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tab.add(titleLabel, BorderLayout.NORTH);

        auditArea = new JTextArea(20, 80);
        auditArea.setEditable(false);
        auditArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        auditArea.setBackground(new Color(30, 30, 30));
        auditArea.setForeground(new Color(0, 255, 0));
        auditArea.setText("[2026-04-08 08:00:00] System initialized\n");
        auditArea.append("[2026-04-08 08:00:00] Admin logged in\n");
        auditArea.append("[2026-04-08 08:00:00] Demo data loaded\n");

        JScrollPane scroll = new JScrollPane(auditArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        tab.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 245, 250));
        JButton clearBtn = new JButton("🗑️ Clear Log");
        JButton exportBtn = new JButton("💾 Export");
        clearBtn.addActionListener(e -> auditArea.setText(""));
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Export feature coming soon"));
        btnPanel.add(clearBtn);
        btnPanel.add(exportBtn);
        tab.add(btnPanel, BorderLayout.SOUTH);

        return tab;
    }
}