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
import vcfs.views.recruiter.RecruiterScreen;

/**
 * Login Frame - Initial UI for recruiter access.
 *
 * Original implementation by: Taha (CodeByTaha18)
 * Adapted to skeleton by: Zaid
 * 
 * Responsibilities:
 * - Display username/password input fields
 * - Authenticate recruiter (currently stubbed)
 * - Launch RecruiterScreen on successful login
 */
public class LoginFrame extends JFrame {

    public LoginFrame() {
        // Basic window setup
        setTitle("VCFS - Recruiter Portal Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Using GridBagLayout for clean, centered appearance
        JPanel mainContent = new JPanel(new GridBagLayout());
        GridBagConstraints layoutRules = new GridBagConstraints();
        layoutRules.insets = new Insets(10, 10, 10, 10);
        layoutRules.fill = GridBagConstraints.HORIZONTAL;

        // --- Username Row ---
        layoutRules.gridx = 0;
        layoutRules.gridy = 0;
        mainContent.add(new JLabel("Username:"), layoutRules);

        layoutRules.gridx = 1;
        JTextField userNameInput = new JTextField(15);
        mainContent.add(userNameInput, layoutRules);

        // --- Password Row ---
        layoutRules.gridx = 0;
        layoutRules.gridy = 1;
        mainContent.add(new JLabel("Password:"), layoutRules);

        layoutRules.gridx = 1;
        JPasswordField passwordInput = new JPasswordField(15);
        mainContent.add(passwordInput, layoutRules);

        // --- Action Row (Login Button) ---
        layoutRules.gridx = 0;
        layoutRules.gridy = 2;
        layoutRules.gridwidth = 2;
        JButton enterButton = new JButton("Login to Dashboard");
        enterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainContent.add(enterButton, layoutRules);

        add(mainContent);

        // Logic for transitioning to the main screen
        enterButton.addActionListener(e -> {
            // STEP 1.1: Implement actual authentication
            String username = userNameInput.getText().trim();
            String password = new String(passwordInput.getPassword()).trim();
            
            // Validation: Check username and password are not empty
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Username cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                );
                Logger.log(LogLevel.WARNING, "[LoginFrame] Rejected login: empty username");
                return;
            }
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Password cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                );
                Logger.log(LogLevel.WARNING, "[LoginFrame] Rejected login: empty password for user " + username);
                return;
            }
            
            // Authentication passed - log and launch recruiter dashboard
            Logger.log(LogLevel.INFO, "[LoginFrame] Recruiter authenticated: " + username);
            new RecruiterScreen();
            dispose();
        });

        setVisible(true);
    }
}


