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
        setSize(600, 850);  // INCREASED HEIGHT FOR DEMO CREDENTIALS DISPLAY
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

        mainContent.add(formPanel);
        mainContent.add(Box.createVerticalStrut(10));
        
        // ===== DEMO CREDENTIALS DISPLAY PANEL =====
        JPanel demoPanel = new JPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        demoPanel.setBackground(new Color(200, 255, 200));
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel demoTitle = new JLabel("📋 DEMO LOGIN CREDENTIALS - For Testing & Demonstration");
        demoTitle.setFont(new Font("Arial", Font.BOLD, 11));
        demoTitle.setForeground(new Color(27, 94, 32));
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
        credentialText.append("🎓 CANDIDATE ACCOUNTS:\n");
        credentialText.append("  Email: alice@email.com        | Password: candidate123 | Name: Alice Brown\n");
        credentialText.append("  Email: bob@email.com          | Password: candidate456 | Name: Bob Wilson\n");
        credentialText.append("  Email: chloe@email.com        | Password: candidate789 | Name: Chloe Davis\n");
        credentialText.append("  Email: diana@email.com        | Password: candidate999 | Name: Diana Martinez\n");
        credentialText.append("  Email: ethan@email.com        | Password: candidate555 | Name: Ethan Taylor\n\n");
        credentialText.append("💡 Quick Tip: Fill in all three fields (Email, Password, Full Name) using any account above");
        
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
        loginButton.addActionListener(e -> performCandidateLogin(emailInput, passwordInput, displayInput, this));
        displayInput.addActionListener(e -> performCandidateLogin(emailInput, passwordInput, displayInput, this));

        backButton.addActionListener(e -> {
            new MainMenuFrame();
            dispose();
        });

        setVisible(true);
    }

    /**
     * DEMO CANDIDATE CREDENTIALS FOR TESTING
     * Fixed credentials hardcoded for quick testing without admin setup
     */
    private static final java.util.Map<String, String[]> CANDIDATE_DEMO_CREDS = 
        java.util.Map.ofEntries(
            java.util.Map.entry("alice@email.com", new String[]{"candidate123", "Alice Brown"}),
            java.util.Map.entry("bob@email.com", new String[]{"candidate456", "Bob Wilson"}),
            java.util.Map.entry("chloe@email.com", new String[]{"candidate789", "Chloe Davis"}),
            java.util.Map.entry("diana@email.com", new String[]{"candidate999", "Diana Martinez"}),
            java.util.Map.entry("ethan@email.com", new String[]{"candidate555", "Ethan Taylor"})
        );

    /**
     * Validate candidate credentials - DEMO CREDENTIALS OR REGISTERED CANDIDATES
     */
    private Candidate validateCandidateCredentials(String email, String password, String displayName) {
        // Check DEMO credentials first (for quick testing)
        if (CANDIDATE_DEMO_CREDS.containsKey(email.toLowerCase())) {
            String[] creds = CANDIDATE_DEMO_CREDS.get(email.toLowerCase());
            if (creds[0].equals(password)) {
                // Demo credential matched - find or create candidate
                Candidate cand = findCandidateByEmail(email);
                if (cand == null) {
                    // Create demo candidate if doesn't exist
                    try {
                        cand = CareerFairSystem.getInstance().registerCandidate(creds[1], email, "Demo candidate", "Software,Data");
                        Logger.log(LogLevel.INFO, "[CandidateLoginFrame] Demo candidate created: " + email);
                    } catch (Exception e) {
                        Logger.log(LogLevel.ERROR, "[CandidateLoginFrame] Failed to create demo candidate", e);
                        return null;
                    }
                }
                return cand;
            }
        }
        
        // Check existing candidates in system
        Candidate existing = findCandidateByEmail(email);
        if (existing != null) {
            // For existing candidates, password validation would go here
            // For now, accept any password for registered candidates (demo mode)
            return existing;
        }
        
        return null;
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
            // Validate credentials (demo or registered)
            Candidate candidate = validateCandidateCredentials(email, password, displayName);
            
            if (candidate == null) {
                int result = JOptionPane.showConfirmDialog(frame, 
                    "Candidate account not found.\n\nWould you like to create a new candidate account?\n" +
                    "(Email: " + email + ", Name: " + displayName + ")", 
                    "New Account?", 
                    JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION) {
                    // Sign up new candidate
                    performCandidateSignup(email, password, displayName, frame);
                    return;
                }
                Logger.log(LogLevel.WARNING, "[CandidateLoginFrame] Candidate not found: " + email);
                return;
            }
            
            UserSession.getInstance().setCurrentCandidate(candidate);
            UserSession.getInstance().setCurrentRole(UserSession.UserRole.CANDIDATE);
            Logger.log(LogLevel.INFO, "[CandidateLoginFrame] Candidate authenticated: " + email);
            
            JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + candidate.getDisplayName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            
            CandidateScreen candidateScreen = new CandidateScreen();
            CareerFairSystem.getInstance().addPropertyChangeListener(candidateScreen);
            Logger.log(LogLevel.INFO, "[CandidateLoginFrame] CandidateScreen registered as listener");
            
            frame.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.log(LogLevel.ERROR, "[CandidateLoginFrame] Error: " + ex.getMessage());
        }
    }
    
    /**
     * Sign up new candidate account
     */
    private void performCandidateSignup(String email, String password, String displayName, CandidateLoginFrame frame) {
        try {
            // Register new candidate in system
            Candidate candidate = CareerFairSystem.getInstance().registerCandidate(displayName, email, "New candidate profile", "Software,Data");
            
            JOptionPane.showMessageDialog(frame, 
                "✓ Account created successfully!\n\n" +
                "Email: " + email + "\n" +
                "Name: " + displayName + "\n\n" +
                "Logging you in now...", 
                "Welcome!", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Auto-login
            UserSession.getInstance().setCurrentCandidate(candidate);
            UserSession.getInstance().setCurrentRole(UserSession.UserRole.CANDIDATE);
            Logger.log(LogLevel.INFO, "[CandidateLoginFrame] New candidate account created and logged in: " + email);
            
            CandidateScreen candidateScreen = new CandidateScreen();
            CareerFairSystem.getInstance().addPropertyChangeListener(candidateScreen);
            
            frame.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Signup failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.log(LogLevel.ERROR, "[CandidateLoginFrame] Signup error: " + ex.getMessage());
        }
    }
    
    /**
     * Find existing candidate by email
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
