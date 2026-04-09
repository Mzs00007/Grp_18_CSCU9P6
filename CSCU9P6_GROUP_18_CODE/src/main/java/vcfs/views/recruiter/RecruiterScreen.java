package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import vcfs.controllers.RecruiterController;
import vcfs.core.CareerFairSystem;
import vcfs.core.UIHelpers;
import vcfs.core.UIEnhancementUtils;
import vcfs.core.SessionManager;
import vcfs.core.UserSession;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.models.booking.MeetingSession;
import vcfs.models.users.Recruiter;

/**
 * Recruiter Dashboard - Main UI for recruiter operations.
 *
 * Original implementation by: Taha (CodeByTaha18)
 * Adapted to skeleton by: Zaid
 *
 * Responsibilities:
 * - Display tabs for recruiter functions: Publish Offer, Schedule, Virtual Room
 * - Coordinate with RecruiterController for business logic
 * - Listen to CareerFairSystem events for real-time updates
 */
public class RecruiterScreen extends JFrame implements RecruiterView, PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private RecruiterController controller;

    public RecruiterScreen() {
        setTitle("VCFS - Recruiter Portal");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        setLayout(new BorderLayout(10, 10));
        
        // Set background
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ((JPanel) getContentPane()).setBackground(new Color(245, 248, 250));

        controller = new RecruiterController(this);

        // CRITICAL FIX: Set the currently logged-in recruiter on the controller
        Recruiter currentRecruiter = UserSession.getInstance().getCurrentRecruiter();
        if (currentRecruiter != null) {
            controller.setCurrentRecruiter(currentRecruiter);
        }

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(63, 81, 181));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("📢 RECRUITER PORTAL - Publish Offers & Manage Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        String recruiterName = (currentRecruiter != null) ? currentRecruiter.getDisplayName() : "Recruiter";
        JLabel userLabel = new JLabel("👤 Welcome, " + recruiterName + " | You can publish offers, schedule meetings, and participate in virtual recruiting");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(new Color(225, 230, 245));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(63, 81, 181));
        titlePanel.add(titleLabel);
        titlePanel.add(userLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Right side: Back + Logout buttons
        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerButtonPanel.setBackground(new Color(63, 81, 181));
        
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
                Logger.log(LogLevel.INFO, "[RecruiterScreen] Back to Main Menu");
                UserSession.getInstance().logout();
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
            Logger.log(LogLevel.INFO, "[RecruiterScreen] Logout clicked");
            UserSession.getInstance().logout();
            UIHelpers.showInfoDialog(RecruiterScreen.this, "Logged Out", "You have been logged out successfully.");
            new vcfs.views.shared.MainMenuFrame();
            dispose();
        });
        headerButtonPanel.add(logoutButton);
        
        headerPanel.add(headerButtonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // ===== STATUS & GUIDANCE DASHBOARD WITH PAGE-LEVEL SCROLLER =====
        JPanel dashboardPanel = createStatusDashboard();
        
        // P2 FIX: Wrap dashboard in page-level scroller for content overflow
        JScrollPane pageScroller = new JScrollPane(dashboardPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pageScroller.getVerticalScrollBar().setUnitIncrement(16);
        
        // ===== ADD CENTER CONTENT WITH COLLAPSIBLE DEMO NOTES =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(pageScroller, BorderLayout.CENTER);
        
        // ===== ADD COLLAPSIBLE DEMO NOTES (BOTTOM) =====
        String[] recruiterNotesTitles = {
            "STEP 1: PUBLISH JOB OFFERS",
            "STEP 2: VIEW INTERVIEW REQUESTS",
            "STEP 3: SCHEDULE MEETINGS",
            "STEP 4: MANAGE VIRTUAL ROOMS",
            "DEMO WORKFLOW"
        };
        
        String[] recruiterNotesContent = {
            "• Click 'Publish Offer' tab at top\n" +
            "• Fill in job details: Title, description, requirements\n" +
            "• Set availability time window (when candidates can book)\n" +
            "• Click 'Publish' - Offer goes live immediately\n" +
            "• Offer appears in Candidate portal in real-time",
            
            "• Click 'My Requests' tab\n" +
            "• View all incoming candidate interview requests\n" +
            "• Each request shows: Candidate name, preferred time slots\n" +
            "• Status shows if request is 'PENDING', 'ACCEPTED', 'REJECTED'\n" +
            "• Requests auto-populate as candidates submit them",
            
            "• Click 'Schedule Meeting' tab\n" +
            "• Select a candidate from incoming requests\n" +
            "• Pick interview time slot (must be within your published availability)\n" +
            "• System generates virtual meeting room link automatically\n" +
            "• Confirmation sent to candidate immediately",
            
            "• Click 'Virtual Room' tab\n" +
            "• Shows all scheduled interviews with video meeting links\n" +
            "• Room ID and join link displayed\n" +
            "• Interview notes and timestamps recorded\n" +
            "• Can invite candidates to meetings from here",
            
            "□ Start by viewing demo admin setup (organizations, recruiters created)\n" +
            "□ In Recruiter portal: Create 2-3 job offers with overlapping times\n" +
            "□ Switch to Candidate Portal: Show offers appearing live\n" +
            "□ Candidate: Request interviews for available slots\n" +
            "□ Return to Recruiter: Show incoming requests appearing\n" +
            "□ Recruiter: Accept/schedule meetings with candidates\n" +
            "□ Show virtual room links and meeting confirmations\n" +
            "□ Return to Admin: Show all activities in Audit Log"
        };
        
        vcfs.views.shared.CollapsibleDemoNotesPanel recruiterNotesPanel = new vcfs.views.shared.CollapsibleDemoNotesPanel(
            "Recruiter Portal", recruiterNotesTitles, recruiterNotesContent
        );
        centerPanel.add(recruiterNotesPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // ===== REGISTER AS OBSERVER =====
        // RecruiterScreen receives property change events from CareerFairSystem
        // This enables real-time updates when candidates book slots or system state changes
        CareerFairSystem.getInstance().addPropertyChangeListener(this);
        
        // TRACK PORTAL ACCESS: Record when recruiter enters portal
        Recruiter recruiter = UserSession.getInstance().getCurrentRecruiter();
        if (recruiter != null) {
            SessionManager.getInstance().recordActivity(recruiter.getDisplayName(), "Recruiter",
                "PORTAL_ACCESSED", "Entered recruiter portal");
        }
        
        setVisible(true);

        Logger.log(LogLevel.INFO, "[RecruiterScreen] Launched for " + recruiterName);
    }

    /**
     * P2 UI UPGRADE: ZAID (mzs00007) — Status Dashboard with Workflow Guidance
     * Displays fair status, bookings info, and tabbed workflow for recruiter tasks
     */
    private JPanel createStatusDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 248, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== STATUS BAR (Top) =====
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statusBar.setBackground(new Color(33, 150, 243));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(13, 71, 161), 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Fair Phase Status  
        JLabel phaseLabel = new JLabel("📍 Fair Phase: BOOKINGS_OPEN");
        phaseLabel.setFont(new Font("Arial", Font.BOLD, 12));
        phaseLabel.setForeground(Color.WHITE);
        statusBar.add(phaseLabel);

        // Booking Status
        JLabel bookingLabel = new JLabel("📅 Bookings: OPEN");
        bookingLabel.setFont(new Font("Arial", Font.BOLD, 12));
        bookingLabel.setForeground(new Color(102, 255, 102));
        statusBar.add(bookingLabel);

        // System Status
        JLabel systemLabel = new JLabel("✓ System Status: READY");
        systemLabel.setFont(new Font("Arial", Font.BOLD, 12));
        systemLabel.setForeground(new Color(102, 255, 102));
        statusBar.add(systemLabel);

        mainPanel.add(statusBar, BorderLayout.NORTH);

        // ===== COMPREHENSIVE GUIDANCE PANEL (Workflow Instructions) =====
        JPanel guidancePanel = new JPanel(new BorderLayout());
        guidancePanel.setBackground(new Color(230, 245, 240));
        guidancePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80), 3),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel guidanceTitle = new JLabel("🚀 RECRUITER WORKFLOW - Complete These Steps in Order:");
        guidanceTitle.setFont(new Font("Arial", Font.BOLD, 13));
        guidanceTitle.setForeground(new Color(39, 174, 96));

        JTextArea guidanceText = new JTextArea(
            "STEP 1️⃣  PUBLISH RECRUITMENT OFFER\n" +
            "    → Go to 'Publish Offer' tab at the top\n" +
            "    → Enter job title (e.g., 'Senior Developer', 'Data Scientist')\n" +
            "    → Enter job description with requirements\n" +
            "    → Set interview duration (e.g., 30 mins, 60 mins)\n" +
            "    → Enter required skills (e.g., Java, Python, React)\n" +
            "    → Click 'Publish Offer' button\n" +
            "    → Your offer appears in Candidates' 'Browse Offers' tab immediately!\n" +
            "    → Also visible in Admin Console 'Offers' table\n\n" +
            "STEP 2️⃣  CONFIGURE YOUR AVAILABLE INTERVIEW SLOTS\n" +
            "    → Go to 'Manage Schedule' tab\n" +
            "    → Define time slots when you're available to interview\n" +
            "    → Candidates can book into these available slots\n" +
            "    → View incoming booking requests from candidates\n" +
            "    → Accept or decline interview requests as they arrive\n" +
            "    → System updates Admin Console LIVE with all booking changes\n\n" +
            "STEP 3️⃣  CONDUCT VIRTUAL INTERVIEWS\n" +
            "    → Go to 'Virtual Room' tab\n" +
            "    → See your confirmed interviews with candidates\n" +
            "    → When meeting time arrives, click 'Join' to start video call\n" +
            "    → Conduct interview with candidate\n" +
            "    → Provide feedback and interview results\n\n" +
            "🔄 MULTI-PORTAL SYNC DEMO:\n" +
            "    ① After publishing offer → Switch to Admin Console\n" +
            "    ② Check Admin 'Offers' tab - see your offer listed\n" +
            "    ③ Check Audit Log at bottom - see 'Offer Published' event\n" +
            "    ④ Then switch to Candidate Portal & show offer appears there\n" +
            "    ⑤ Have candidate book → Watch Admin log show 'Booking Created'\n\n" +
            "💡 KEY POINTS FOR DEMO:\n" +
            "    • Publish offer FIRST so candidates can search & book\n" +
            "    • Every action (publish, accept, decline) syncs INSTANTLY across all portals\n" +
            "    • Admin Console Audit Log shows all recruiter operations in real-time\n" +
            "    • All changes persist and are tracked by SystemStateManager\n" +
            "✓ This is how REAL-TIME multi-user system coordination works!"
        );
        guidanceText.setEditable(false);
        guidanceText.setFont(new Font("Arial", Font.PLAIN, 11));
        guidanceText.setBackground(new Color(245, 252, 250));
        guidanceText.setForeground(new Color(30, 30, 30));
        guidanceText.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        guidanceText.setLineWrap(true);
        guidanceText.setWrapStyleWord(true);

        guidancePanel.add(guidanceTitle, BorderLayout.NORTH);
        guidancePanel.add(guidanceText, BorderLayout.CENTER);

        // ===== TAB PANEL WITH ALL FUNCTIONS =====
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 248, 250));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Color.WHITE);
        tabs.setFont(new Font("Arial", Font.PLAIN, 12));

        PublishOfferPanel publishPanel = new PublishOfferPanel(controller);
        tabs.addTab("📤 Step 1: Publish Offer", publishPanel);
        
        SchedulePanel schedulePanel = new SchedulePanel(controller);
        tabs.addTab("📅 Step 2: Manage Schedule", schedulePanel);
        
        VirtualRoomPanel virtualRoomPanel = new VirtualRoomPanel(controller);
        tabs.addTab("🚪 Step 3: Virtual Room", virtualRoomPanel);

        contentPanel.add(guidancePanel, BorderLayout.NORTH);
        contentPanel.add(tabs, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    @Override
    public void displayError(String message) {
        UIEnhancementUtils.showError(this, "Error", message);
    }

    @Override
    public void displayMessage(String message) {
        UIEnhancementUtils.showSuccess(this, "Success", message);
    }

    @Override
    public void displaySessions(List<MeetingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            Logger.log(LogLevel.INFO, "[RecruiterScreen] No sessions to display");
            return;
        }
        StringBuilder sb = new StringBuilder("📋 Meeting Sessions:\n\n");
        for (MeetingSession session : sessions) {
            sb.append("• ").append(session.getTitle()).append("\n");
        }
        Logger.log(LogLevel.INFO, "[RecruiterScreen] Displaying " + sessions.size() + " sessions");
    }

    /**
     * Refresh the display with latest data.
     * Called when system state changes via PropertyChangeListener.
     * Updates recruiter's published offers and current fair phase.
     */
    public void refreshDisplay() {
        Logger.log(LogLevel.INFO, "[RecruiterScreen] Refreshing display");
        if (controller != null) {
            // Reload meeting history and available offers
            try {
                controller.viewMeetingHistory();
                
                // Update phase if needed
                Recruiter curr = UserSession.getInstance().getCurrentRecruiter();
                if (curr != null) {
                    Logger.log(LogLevel.INFO, "[RecruiterScreen] Current recruiter: " + curr.getDisplayName());
                }
            } catch (Exception e) {
                Logger.log(LogLevel.ERROR, "[RecruiterScreen] Error refreshing", e);
            }
        }
    }

    /**
     * Observer callback - receives system notifications from CareerFairSystem.
     * Updates recruiter screen when phase changes, recruiters are added, or time advances.
     * 
     * CRITICAL: All Swing UI updates must happen on the Event Dispatch Thread (EDT)
     * PropertyChangeSupport fires events on its own thread, so we must use
     * SwingUtilities.invokeLater() to marshal calls back to EDT
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if ("phase".equals(prop)) {
            Logger.log(LogLevel.INFO, "[RecruiterScreen] Phase changed: " + evt.getNewValue());
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        } else if ("recruiters".equals(prop) || "offers".equals(prop)) {
            Logger.log(LogLevel.INFO, "[RecruiterScreen] System data updated: " + prop);
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        } else if ("reset".equals(prop)) {
            Logger.log(LogLevel.INFO, "[RecruiterScreen] System reset - clearing all displays");
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        } else if ("timeline".equals(prop)) {
            Logger.log(LogLevel.INFO, "[RecruiterScreen] Timeline updated - refreshing display");
            javax.swing.SwingUtilities.invokeLater(() -> refreshDisplay());
        }
    }

    @Override
    public void displayOffers(List offers) {
        if (offers == null) offers = new java.util.ArrayList<>();
        
        Logger.log(LogLevel.INFO, "[RecruiterScreen] Displaying " + offers.size() + " offers");
        
        // Display count in a message
        String message = String.format("📊 Total Interview Offers: %d%n", offers.size());
        if (offers.isEmpty()) {
            message += "No offers published yet.";
        } else {
            java.util.List<String> offerSummaries = new java.util.ArrayList<>();
            for (Object offer : offers) {
                if (offer instanceof vcfs.models.booking.Offer) {
                    vcfs.models.booking.Offer o = (vcfs.models.booking.Offer) offer;
                    offerSummaries.add(String.format("%s (Capacity: %d)", o.getTitle(), o.getCapacity()));
                } else {
                    offerSummaries.add(offer.toString());
                }
            }
            message += java.lang.String.join("\n", offerSummaries);
        }
        
        Logger.log(LogLevel.INFO, "[RecruiterScreen] Offers presentation: " + message);
    }

    @Override
    public void displayRequests(List requests) {
        if (requests == null) requests = new java.util.ArrayList<>();
        
        Logger.log(LogLevel.INFO, "[RecruiterScreen] Displaying " + requests.size() + " booking requests");
        
        // Display count of candidate requests
        String message = String.format("📋 Total Booking Requests: %d%n", requests.size());
        if (requests.isEmpty()) {
            message += "No booking requests received yet.";
        } else {
            java.util.List<String> requestSummaries = new java.util.ArrayList<>();
            for (Object request : requests) {
                if (request instanceof vcfs.models.booking.Request) {
                    vcfs.models.booking.Request r = (vcfs.models.booking.Request) request;
                    requestSummaries.add(String.format("Request ID: %s (Max Appointments: %d)", 
                        r.getId(), r.getMaxAppointments()));
                } else {
                    requestSummaries.add(request.toString());
                }
            }
            message += java.lang.String.join("\n", requestSummaries);
        }
        
        Logger.log(LogLevel.INFO, "[RecruiterScreen] Requests presentation: " + message);
    }
}


