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

    public MultiPortalDemoWindow() {
        setTitle("VCFS - Demo Mode: Launch All Portals Separately");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
        
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
        // Footer with instructions
        // ================================================================
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(66, 66, 66));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel footerLabel = new JLabel(
            "💡 Tip: Open multiple portals in separate windows and arrange them side-by-side for a full demo"
        );
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        footerLabel.setForeground(new Color(150, 200, 150));
        
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        
        Logger.log(LogLevel.INFO, "[MultiPortalDemoWindow] Demo mode window ready");
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
