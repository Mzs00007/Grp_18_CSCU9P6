package vcfs.views.shared;

import javax.swing.*;
import java.awt.*;

import vcfs.core.Logger;
import vcfs.core.LogLevel;

/**
 * Main Menu Frame - Role Selection for VCFS System.
 *
 * Initial screen allowing users to select their role:
 * - Administrator (ADMIN)
 * - Recruiter (RECRUITER)
 * - Candidate (CANDIDATE)
 *
 * Each role opens a different login flow and dashboard.
 *
 * Implemented by: Zaid
 */
public class MainMenuFrame extends JFrame {


    public MainMenuFrame() {
        // Basic window setup
        setTitle("VCFS - Virtual Career Fair System");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(600, 500));
        setLayout(new BorderLayout(10, 10));
        
        // Set background
        ((JPanel) getContentPane()).setBackground(new Color(245, 248, 250));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ========================================
        // HEADER - Title and Description
        // ========================================
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🎯 VCFS - Virtual Career Fair");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Select Your Role to Continue");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(225, 245, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // ========================================
        // CONTENT PANEL
        // ========================================
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 248, 250));

        // Instruction label
        JLabel instructionLabel = new JLabel("Choose your role below to access your portal:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setForeground(new Color(80, 80, 80));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        contentPanel.add(instructionLabel);

        // ========================================
        // DEMO MODE BUTTON (Top)
        // ========================================
        JButton demoModeButton = createRoleButton(
            "⚡ DEMO MODE - All 3 Portals Side-by-Side",
            "Perfect for live demonstrations - see Admin, Recruiter & Candidate portals simultaneously",
            new Color(255, 152, 0)
        );
        demoModeButton.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[MainMenuFrame] Demo mode activated");
            new MultiPortalDemoWindow();
            dispose();
        });
        contentPanel.add(demoModeButton);
        contentPanel.add(Box.createVerticalStrut(20));

        // ========================================
        // BUTTON PANEL - Three Role Selection Buttons
        // ========================================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 15));
        buttonPanel.setBackground(new Color(245, 248, 250));

        // Admin Button
        JButton adminButton = createRoleButton(
            "🔐 Administrator",
            "Manage fair, organizations, and timeline",
            new Color(244, 67, 54)
        );
        adminButton.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[MainMenuFrame] Admin role selected");
            new AdminLoginFrame();
            dispose();
        });

        // Recruiter Button
        JButton recruiterButton = createRoleButton(
            "📤 Recruiter",
            "Publish offers and manage interviews",
            new Color(63, 81, 181)
        );
        recruiterButton.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[MainMenuFrame] Recruiter role selected");
            new LoginFrame();
            dispose();
        });

        // Candidate Button
        JButton candidateButton = createRoleButton(
            "🎓 Candidate",
            "Search offers and book interviews",
            new Color(76, 175, 80)
        );
        candidateButton.addActionListener(e -> {
            Logger.log(LogLevel.INFO, "[MainMenuFrame] Candidate role selected");
            new CandidateLoginFrame();
            dispose();
        });

        buttonPanel.add(adminButton);
        buttonPanel.add(recruiterButton);
        buttonPanel.add(candidateButton);
        contentPanel.add(buttonPanel);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);

        Logger.log(LogLevel.INFO, "[MainMenuFrame] Launched - awaiting role selection");
    }

    /**
     * Helper method to create a styled role selection button.
     *
     * @param title Main button text
     * @param description Additional description text
     * @param bgColor Background color for button
     * @return Styled JButton
     */
    private JButton createRoleButton(String title, String description, Color bgColor) {
        JButton button = new JButton("<html><b>" + title + "</b><br/><font size='3'>" + description + "</font></html>");
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(400, 80));
        button.setOpaque(true);
        button.setBorderPainted(false);

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
