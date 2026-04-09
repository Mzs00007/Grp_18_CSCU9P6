package vcfs.views.shared;

import javax.swing.*;
import java.awt.*;

import vcfs.core.CareerFairSystem;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.UserSession;
import vcfs.views.admin.AdminScreen;
import vcfs.controllers.AdminScreenController;

/**
 * Admin Login Frame - Login UI for administrators.
 *
 * Allows administrators to:
 * - Enter username (typically "admin")
 * - Enter password (hardcoded for demo: "admin123")
 * - Access Admin Console
 *
 * Implementation by: Zaid
 */
public class AdminLoginFrame extends JFrame {


    public AdminLoginFrame() {
        setTitle("VCFS - Admin Login");
        setSize(500, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Set background
        ((JPanel) getContentPane()).setBackground(new Color(245, 248, 250));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(25, 70, 130));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🔐 Administrator Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Secure Access - Admin Only");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(180, 200, 220));
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
        JLabel instructionLabel = new JLabel("📝 Enter administrator credentials:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        instructionLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(instructionLabel);
        formPanel.add(Box.createVerticalStrut(15));

        // Username field
        JLabel userLabel = new JLabel("👤 Username");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(userLabel);
        
        JTextField usernameInput = new JTextField(20);
        usernameInput.setText("admin");
        usernameInput.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameInput.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        usernameInput.setPreferredSize(new Dimension(0, 35));
        formPanel.add(usernameInput);
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
        formPanel.add(Box.createVerticalStrut(10));

        // Help text
        JLabel helpLabel = new JLabel("💡 Demo: admin / admin123");
        helpLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        helpLabel.setForeground(new Color(150, 150, 150));
        formPanel.add(helpLabel);
        formPanel.add(Box.createVerticalStrut(15));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 248, 250));

        JButton loginButton = new JButton("🚀 Sign In");
        loginButton.setBackground(new Color(25, 70, 130));
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
        loginButton.addActionListener(e -> performAdminLogin(usernameInput, passwordInput, this));
        passwordInput.addActionListener(e -> performAdminLogin(usernameInput, passwordInput, this));

        backButton.addActionListener(e -> {
            new MainMenuFrame();
            dispose();
        });

        setVisible(true);
    }

    private void performAdminLogin(JTextField usernameField, JPasswordField passwordField, JFrame frame) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your username", "Required Field", JOptionPane.WARNING_MESSAGE);
            Logger.log(LogLevel.WARNING, "[AdminLoginFrame] Rejected: empty username");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your password", "Required Field", JOptionPane.WARNING_MESSAGE);
            Logger.log(LogLevel.WARNING, "[AdminLoginFrame] Rejected: empty password");
            return;
        }

        if (!username.equals("admin") || !password.equals("admin123")) {
            JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            Logger.log(LogLevel.WARNING, "[AdminLoginFrame] Invalid credentials for user: " + username);
            usernameField.setText("admin");
            passwordField.setText("");
            return;
        }

        UserSession.getInstance().setCurrentAdmin(username);
        UserSession.getInstance().setCurrentRole(UserSession.UserRole.ADMIN);
        Logger.log(LogLevel.INFO, "[AdminLoginFrame] Admin authenticated: " + username);

        JOptionPane.showMessageDialog(frame, "Login successful! Welcome, Admin", "Success", JOptionPane.INFORMATION_MESSAGE);

        try {
            AdminScreenController controller = new AdminScreenController();
            // AdminScreen now registers itself as a listener in its constructor
            AdminScreen adminScreen = new AdminScreen(controller);
            Logger.log(LogLevel.INFO, "[AdminLoginFrame] AdminScreen created and initialized");
        } catch (Exception ex) {
            Logger.log(LogLevel.ERROR, "[AdminLoginFrame] Failed to launch AdminScreen", ex);
            JOptionPane.showMessageDialog(frame, "Error launching admin portal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        ((JFrame) frame).dispose();
    }
}
