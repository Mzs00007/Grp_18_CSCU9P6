package vcfs.views.shared;

import javax.swing.*;
import java.awt.*;

import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.CareerFairSystem;
import vcfs.core.UserSession;
import vcfs.models.users.Candidate;
import vcfs.views.candidate.CandidateScreen;

/**
 * Candidate Login Frame - Login UI for candidates.
 *
 * Allows candidates to:
 * - Enter email and password
 * - Create new account (for demo purposes accepts any email)
 * - Access Candidate Dashboard
 *
 * Implementation by: Zaid
 */
public class CandidateLoginFrame extends JFrame {

    public CandidateLoginFrame() {
        setTitle("VCFS - Candidate Login");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Set background
        ((JPanel) getContentPane()).setBackground(new Color(245, 248, 250));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🎓 Candidate Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Join Your Career Fair Journey");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(225, 245, 225));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 248, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Instruction
        JLabel instructionLabel = new JLabel("📝 Create your account or sign in:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        instructionLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(instructionLabel);
        formPanel.add(Box.createVerticalStrut(15));

        // Email field
        JLabel emailLabel = new JLabel("📧 Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(emailLabel);
        
        JTextField emailInput = new JTextField(20);
        emailInput.setFont(new Font("Arial", Font.PLAIN, 12));
        emailInput.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        emailInput.setPreferredSize(new Dimension(0, 35));
        formPanel.add(emailInput);
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
        formPanel.add(Box.createVerticalStrut(15));

        // Display Name field
        JLabel nameLabel = new JLabel("👤 Full Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel);
        
        JTextField displayInput = new JTextField(20);
        displayInput.setFont(new Font("Arial", Font.PLAIN, 12));
        displayInput.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        displayInput.setPreferredSize(new Dimension(0, 35));
        formPanel.add(displayInput);
        formPanel.add(Box.createVerticalStrut(20));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 248, 250));

        JButton loginButton = new JButton("🚀 Sign In");
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createRaisedBevelBorder());
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(150, 40));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 11));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(120, 35));

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.CENTER);

        // Allow press Enter to login
        loginButton.addActionListener(e -> performCandidateLogin(emailInput, passwordInput, displayInput, this));
        displayInput.addActionListener(e -> performCandidateLogin(emailInput, passwordInput, displayInput, this));

        backButton.addActionListener(e -> {
            new MainMenuFrame();
            dispose();
        });

        setVisible(true);
    }

    private void performCandidateLogin(JTextField emailField, JPasswordField passwordField, JTextField displayField, CandidateLoginFrame frame) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String displayName = displayField.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your email address", "Required Field", JOptionPane.WARNING_MESSAGE);
            Logger.log(LogLevel.WARNING, "[CandidateLoginFrame] Rejected: empty email");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your password", "Required Field", JOptionPane.WARNING_MESSAGE);
            Logger.log(LogLevel.WARNING, "[CandidateLoginFrame] Rejected: empty password");
            return;
        }

        if (displayName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your full name", "Required Field", JOptionPane.WARNING_MESSAGE);
            Logger.log(LogLevel.WARNING, "[CandidateLoginFrame] Rejected: empty display name");
            return;
        }

        try {
            // FIX: Find existing candidate by email instead of registering new one
            // This prevents duplicate candidates and login loops
            Candidate candidate = findCandidateByEmail(email);
            
            if (candidate == null) {
                JOptionPane.showMessageDialog(frame, "No candidate found with that email.\\nPlease check your email or register at the fair.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                Logger.log(LogLevel.WARNING, "[CandidateLoginFrame] Candidate not found: " + email);
                return;
            }
            
            UserSession.getInstance().setCurrentCandidate(candidate);
            UserSession.getInstance().setCurrentRole(UserSession.UserRole.CANDIDATE);
            Logger.log(LogLevel.INFO, "[CandidateLoginFrame] Candidate authenticated: " + email);
            
            JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + candidate.getDisplayName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            
            CandidateScreen candidateScreen = new CandidateScreen();
            // REGISTER: CandidateScreen as listener to system events
            CareerFairSystem.getInstance().addPropertyChangeListener(candidateScreen);
            Logger.log(LogLevel.INFO, "[CandidateLoginFrame] CandidateScreen registered as listener");
            
            frame.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.log(LogLevel.ERROR, "[CandidateLoginFrame] Error: " + ex.getMessage());
        }
    }
    
    /**
     * Find existing candidate by email
     * Searches through all registered candidates in the system
     */
    private Candidate findCandidateByEmail(String email) {
        try {
            java.lang.reflect.Method m = CareerFairSystem.getInstance().getClass().getMethod("getAllCandidates");
            java.util.List<?> candidates = (java.util.List<?>) m.invoke(CareerFairSystem.getInstance());
            if (candidates != null) {
                for (Object c : candidates) {
                    if (c instanceof Candidate) {
                        Candidate cand = (Candidate) c;
                        if (cand.getEmail().equalsIgnoreCase(email)) {
                            return cand;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CandidateLoginFrame] Error finding candidate", e);
        }
        return null;
    }
}
