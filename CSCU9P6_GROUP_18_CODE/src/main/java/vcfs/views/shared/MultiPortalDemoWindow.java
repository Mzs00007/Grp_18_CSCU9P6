package vcfs.views.shared;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Project Manager & Lead Developer: Zaid Siddiqui (mzs00007)
 */

import javax.swing.*;
import java.awt.*;
import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Multi-Portal Demo Window - Shows how to open all 3 portals.
 * 
 * FEATURES:
 *   ✓ Opens AdminLoginFrame
 *   ✓ Opens LoginFrame (Recruiter)
 *   ✓ Opens CandidateLoginFrame
 *   ✓ All 3 portals can run simultaneously
 *   ✓ Perfect for live demonstrations
 * 
 * USAGE:
 *   new MultiPortalDemoWindow();
 * 
 * Implemented by: Zaid
 */
public class MultiPortalDemoWindow extends JFrame {

    private JPanel demoNotesPanel;
    private JButton toggleNotesButton;
    private boolean notesExpanded = false;

    public MultiPortalDemoWindow() {
        setTitle("VCFS - Demo Mode: Launch All Portals Separately");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        
        Logger.log(LogLevel.INFO, "[MultiPortalDemoWindow] Launching demo mode - all 3 portals");

        // ================================================================
        // Create main content panel
        // ================================================================
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 248, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================================================================
        // Header
        // ================================================================
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🎯 VCFS - Multi-Portal Demo Mode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Click any button below to launch a portal independently");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(225, 245, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ================================================================
        // Content Panel - Launch Buttons
        // ================================================================
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 1, 0, 15));
        contentPanel.setBackground(new Color(245, 248, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // ADMIN BUTTON
        JButton adminBtn = createDemoButton(
            "🔐 Launch Admin Portal",
            "Manage organizations, booths, and fair timeline",
            new Color(244, 67, 54)
        );
        adminBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[MultiPortalDemoWindow] Launching Admin portal");
            new AdminLoginFrame();
        });

        // RECRUITER BUTTON
        JButton recruiterBtn = createDemoButton(
            "📤 Launch Recruiter Portal",
            "Publish offers and manage interview schedule",
            new Color(63, 81, 181)
        );
        recruiterBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[MultiPortalDemoWindow] Launching Recruiter portal");
            new LoginFrame();
        });

        // CANDIDATE BUTTON
        JButton candidateBtn = createDemoButton(
            "🎓 Launch Candidate Portal",
            "Search offers and book interview meetings",
            new Color(76, 175, 80)
        );
        candidateBtn.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[MultiPortalDemoWindow] Launching Candidate portal");
            new CandidateLoginFrame();
        });

        contentPanel.add(adminBtn);
        contentPanel.add(recruiterBtn);
        contentPanel.add(candidateBtn);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // ================================================================
        // Collapsible Demo Notes Section
        // ================================================================
        JPanel notesContainerPanel = new JPanel(new BorderLayout());
        notesContainerPanel.setBackground(new Color(245, 248, 250));
        notesContainerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Toggle Button with Arrow
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        togglePanel.setBackground(new Color(232, 245, 233));
        togglePanel.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));

        toggleNotesButton = new JButton("▼ Demo Notes (Instructor Only)");
        toggleNotesButton.setFont(new Font("Arial", Font.BOLD, 12));
        toggleNotesButton.setForeground(Color.WHITE);
        toggleNotesButton.setBackground(new Color(76, 175, 80));
        toggleNotesButton.setFocusPainted(false);
        toggleNotesButton.setBorderPainted(false);
        toggleNotesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleNotesButton.setOpaque(true);

        toggleNotesButton.addActionListener(e -> toggleDemoNotes());

        togglePanel.add(toggleNotesButton);
        notesContainerPanel.add(togglePanel, BorderLayout.NORTH);

        // Demo Notes Content (Collapsible)
        demoNotesPanel = new JPanel();
        demoNotesPanel.setLayout(new BoxLayout(demoNotesPanel, BoxLayout.Y_AXIS));
        demoNotesPanel.setBackground(new Color(240, 250, 240));
        demoNotesPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 230, 200), 1, true));
        demoNotesPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        addDemoNoteItem(demoNotesPanel, "1. Admin Portal", 
            "• Set Fair Timeline: Click 'Set Fair Timeline' button\n" +
            "• Create Organizations: Add sample organizations\n" +
            "• Create Booths: Assign booths to organizations\n" +
            "• Register Recruiters: Onboard recruiter users\n" +
            "• View Audit Log: Show system actions");

        addDemoNoteItem(demoNotesPanel, "2. Recruiter Portal",
            "• Publish Offers: Create job offers and set availability windows\n" +
            "• View Requests: See incoming candidate interview requests\n" +
            "• Manage Calendar: Show meeting schedule and confirmations\n" +
            "• Track Bookings: Display candidate booking history");

        addDemoNoteItem(demoNotesPanel, "3. Candidate Portal",
            "• Browse Offers: Show available job postings\n" +
            "• View Organizations: Display participating companies\n" +
            "• Request Interviews: Submit meeting requests to recruiters\n" +
            "• Check Bookings: Show confirmed interview appointments\n" +
            "• Auto-Booking: Demonstrate automatic slot assignment");

        addDemoNoteItem(demoNotesPanel, "4. System Features",
            "• Fair Timeline: DORMANT→PREPARING→BOOKINGS_OPEN→BOOKINGS_CLOSED→FAIR_LIVE→DORMANT\n" +
            "• Real-Time Clock: Show system time simulation\n" +
            "• Observer Pattern: Multi-portal synchronization\n" +
            "• Audit Logging: All actions recorded automatically");

        addDemoNoteItem(demoNotesPanel, "⚠️ Demo Tips",
            "• Use existing test data when available\n" +
            "• Arrange windows side-by-side for full demonstration\n" +
            "• Test state transitions between fair phases\n" +
            "• Show auto-booking feature with overlapping availability\n" +
            "• Don't close windows during transitions");

        // Scroll pane for notes
        JScrollPane notesScrollPane = new JScrollPane(demoNotesPanel);
        notesScrollPane.setVisible(false);
        notesScrollPane.setPreferredSize(new Dimension(800, 0));
        notesContainerPanel.add(notesScrollPane, BorderLayout.CENTER);

        mainPanel.add(notesContainerPanel, BorderLayout.SOUTH);

        // ================================================================
        // Footer with instructions
        // ================================================================
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(66, 66, 66));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel footerLabel = new JLabel(
            "💡 Tip: Open multiple portals in separate windows and arrange them side-by-side for a full demo"
        );
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        footerLabel.setForeground(new Color(150, 200, 150));
        
        footerPanel.add(footerLabel);

        // Create a wrapper to hold both notes container and footer
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.add(notesContainerPanel, BorderLayout.CENTER);
        bottomWrapper.add(footerPanel, BorderLayout.SOUTH);
        
        mainPanel.add(bottomWrapper, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        
        Logger.log(LogLevel.INFO, "[MultiPortalDemoWindow] Demo mode window ready");
    }

    /**
     * Add a demo note item to the notes panel.
     */
    private void addDemoNoteItem(JPanel panel, String title, String content) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(new Color(240, 250, 240));
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(new Color(27, 94, 32));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel contentLabel = new JLabel("<html>" + content.replace("\n", "<br/>") + "</html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        contentLabel.setForeground(new Color(50, 50, 50));
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        itemPanel.add(titleLabel);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(contentLabel);
        itemPanel.add(Box.createVerticalStrut(8));

        panel.add(itemPanel);
    }

    /**
     * Toggle demo notes visibility with smooth expand/collapse animation.
     */
    private void toggleDemoNotes() {
        JScrollPane scrollPane = null;
        
        // Find the scroll pane in the component tree
        Container parent = demoNotesPanel.getParent();
        while (parent != null) {
            if (parent instanceof JScrollPane) {
                scrollPane = (JScrollPane) parent;
                break;
            }
            parent = parent.getParent();
        }

        if (scrollPane != null) {
            notesExpanded = !notesExpanded;

            if (notesExpanded) {
                // Expand
                toggleNotesButton.setText("▲ Demo Notes (Instructor Only)");
                toggleNotesButton.setBackground(new Color(56, 142, 60));
                scrollPane.setVisible(true);
                scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), 280));
            } else {
                // Collapse
                toggleNotesButton.setText("▼ Demo Notes (Instructor Only)");
                toggleNotesButton.setBackground(new Color(76, 175, 80));
                scrollPane.setVisible(false);
                scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), 0));
            }

            // Revalidate and repaint the layout
            SwingUtilities.invokeLater(() -> {
                demoNotesPanel.revalidate();
                demoNotesPanel.repaint();
                this.revalidate();
                this.repaint();
            });
        }
    }

    /**
     * Create a styled demo button.
     * 
     * @param title Main button text
     * @param description Additional description
     * @param bgColor Background color
     * @return Styled JButton
     */
    private JButton createDemoButton(String title, String description, Color bgColor) {
        JButton button = new JButton("<html><b style='font-size:14px'>" + title + "</b><br/>" +
                                     "<font size='3' color='#ffffff'>" + description + "</font></html>");
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 90));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}
