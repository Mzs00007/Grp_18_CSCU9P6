package Recruitment;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        // Basic window setup
        setTitle("Recruiter Portal - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Keeps the window in the center of the screen

        // Using GridBagLayout for a clean, centered look
        JPanel mainContent = new JPanel(new GridBagLayout());
        GridBagConstraints layoutRules = new GridBagConstraints();
        layoutRules.insets = new Insets(10, 10, 10, 10); // Adds breathing room between components
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
        layoutRules.gridwidth = 2; // Make the button span across both columns
        JButton enterButton = new JButton("Login to Dashboard");
        enterButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Makes it look clickable
        mainContent.add(enterButton, layoutRules);

        add(mainContent);

        // Logic for transitioning to the main screen
        enterButton.addActionListener(e -> {
            // Usually, you would check userNameInput.getText() and passwordInput.getPassword() here
            new RecruiterScreen(); // Launch your main dashboard
            dispose(); // Close the login window
        });

        setVisible(true);
    }
}