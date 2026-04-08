package vcfs.views.shared;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.*;
import java.awt.*;

import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.CareerFairSystem;
import vcfs.core.UserSession;
import vcfs.models.users.Recruiter;
import vcfs.views.recruiter.RecruiterScreen;

/**
 * Login Frame - Initial UI for recruiter access.
 *
 * Original implementation by: Taha (CodeByTaha18)
 * Adapted to skeleton by: Zaid
 * 
 * Responsibilities:
 * - Display username/password input fields
 * - Authenticate recruiter with credential validation
 * - Launch RecruiterScreen on successful login
 */
public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("VCFS - Recruiter Login");
        setSize(550, 750);  // INCREASED HEIGHT FOR DEMO CREDENTIALS DISPLAY
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Set background
        ((JPanel) getContentPane()).setBackground(new Color(245, 248, 250));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(63, 81, 181));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📤 Recruiter Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign In to Your Account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(225, 230, 245));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN SCROLLABLE PANEL FOR ALL CONTENT =====
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(245, 248, 250));
        
        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 248, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Instruction
        JLabel instructionLabel = new JLabel("📝 Enter your login credentials below:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        instructionLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(instructionLabel);
        formPanel.add(Box.createVerticalStrut(15));

        // Username field
        JLabel userLabel = new JLabel("👤 Username");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(userLabel);
        
        JTextField userNameInput = new JTextField(20);
        userNameInput.setFont(new Font("Arial", Font.PLAIN, 12));
        userNameInput.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        userNameInput.setPreferredSize(new Dimension(0, 35));
        formPanel.add(userNameInput);
        formPanel.add(Box.createVerticalStrut(15));

        // Password field
        JLabel passLabel = new JLabel("🔒 Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passLabel);
        
        JPasswordField passwordInput = new JPasswordField(20);
        passwordInput.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordInput.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        passwordInput.setPreferredSize(new Dimension(0, 35));
        formPanel.add(passwordInput);
        formPanel.add(Box.createVerticalStrut(20));

        // Button panel with Login and Back buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 248, 250));

        JButton enterButton = new JButton("🚀 Sign In");
        enterButton.setBackground(new Color(63, 81, 181));
        enterButton.setForeground(Color.WHITE);
        enterButton.setFont(new Font("Arial", Font.BOLD, 12));
        enterButton.setFocusPainted(false);
        enterButton.setBorder(BorderFactory.createRaisedBevelBorder());
        enterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enterButton.setPreferredSize(new Dimension(150, 40));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 11));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(120, 35));

        buttonPanel.add(enterButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel);

        mainContent.add(formPanel);
        mainContent.add(Box.createVerticalStrut(10));
        
        // ===== DEMO CREDENTIALS DISPLAY PANEL =====
        JPanel demoPanel = new JPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        demoPanel.setBackground(new Color(255, 250, 205));
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel demoTitle = new JLabel("📋 DEMO LOGIN CREDENTIALS - For Testing & Demonstration");
        demoTitle.setFont(new Font("Arial", Font.BOLD, 11));
        demoTitle.setForeground(new Color(255, 111, 0));
        demoPanel.add(demoTitle);
        demoPanel.add(Box.createVerticalStrut(8));
        
        // Demo credentials text area
        JTextArea demoCredentials = new JTextArea();
        demoCredentials.setFont(new Font("Courier New", Font.PLAIN, 10));
        demoCredentials.setEditable(false);
        demoCredentials.setOpaque(false);
        demoCredentials.setLineWrap(true);
        demoCredentials.setWrapStyleWord(true);
        
        StringBuilder credentialText = new StringBuilder();
        credentialText.append("👤 RECRUITER ACCOUNTS:\n");
        credentialText.append("  • Ahmed Hassan  →  recruiter123\n");
        credentialText.append("  • Mohamed Ali   →  recruiter456\n");
        credentialText.append("  • Fatima Khan   →  recruiter789\n");
        credentialText.append("  • David Smith   →  recruiter999\n");
        credentialText.append("  • Sarah Johnson →  recruiter555\n\n");
        credentialText.append("💡 Quick Tip: Use any username above with its corresponding password");
        
        demoCredentials.setText(credentialText.toString());
        demoPanel.add(demoCredentials);
        
        mainContent.add(demoPanel);
        mainContent.add(Box.createVerticalStrut(5));
        mainContent.add(Box.createVerticalGlue());
        
        // Add scrollable content area
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Allow press Enter to login
        enterButton.addActionListener(e -> performLogin(userNameInput, passwordInput, this));
        passwordInput.addActionListener(e -> performLogin(userNameInput, passwordInput, this));

        backButton.addActionListener(e -> {
            new MainMenuFrame();
            dispose();
        });

        setVisible(true);
    }
    
    /**
     * Find existing recruiter by username (display name)
     * Searches through all registered recruiters in the system
     */
    private Recruiter findRecruiterByUsername(String username) {
        try {
            for (vcfs.models.booking.Offer offer : CareerFairSystem.getInstance().getAllOffers()) {
                Recruiter rec = offer.getPublisher();
                if (rec != null && rec.getDisplayName().equalsIgnoreCase(username)) {
                    return rec;
                }
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[LoginFrame] Error finding recruiter", e);
        }
        return null;
    }

    private void performLogin(JTextField usernameField, JPasswordField passwordField, JFrame frame) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your username", "Required Field", JOptionPane.WARNING_MESSAGE);
            Logger.log(LogLevel.WARNING, "[LoginFrame] Rejected: empty username");
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your password", "Required Field", JOptionPane.WARNING_MESSAGE);
            Logger.log(LogLevel.WARNING, "[LoginFrame] Rejected: empty password");
            return;
        }
        
        try {
            // FIX: Find existing recruiter instead of registering new one
            // This prevents duplicate recruiters and login loops
            Recruiter recruiter = findRecruiterByUsername(username);
            
            if (recruiter == null) {
                JOptionPane.showMessageDialog(frame, "No recruiter found with that username.\nAvailable recruiters are registered in the system.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                Logger.log(LogLevel.WARNING, "[LoginFrame] Recruiter not found: " + username);
                return;
            }
            
            UserSession.getInstance().setCurrentRecruiter(recruiter);
            UserSession.getInstance().setCurrentRole(UserSession.UserRole.RECRUITER);
            
            JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + recruiter.getDisplayName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            Logger.log(LogLevel.INFO, "[LoginFrame] Recruiter authenticated: " + username);
            
            RecruiterScreen recruiterScreen = new RecruiterScreen();
            // REGISTER: RecruiterScreen as listener to system events
            CareerFairSystem.getInstance().addPropertyChangeListener(recruiterScreen);
            Logger.log(LogLevel.INFO, "[LoginFrame] RecruiterScreen registered as listener");
            
            ((JFrame) frame).dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.log(LogLevel.ERROR, "[LoginFrame] Error: " + ex.getMessage());
        }
    }
}


