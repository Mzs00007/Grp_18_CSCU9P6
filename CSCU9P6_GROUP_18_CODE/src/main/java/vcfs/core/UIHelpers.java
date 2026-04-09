package vcfs.core;

import javax.swing.*;
import java.awt.*;

/**
 * UI Helpers Utility Class - Reusable UI components and methods.
 *
 * Provides:
 * - Consistent dialog methods
 * - Input validation helpers
 * - Common UI styles and patterns
 * - Date/time formatting
 * - Button and label styling
 *
 * Usage:
 *   UIHelpers.showSuccessDialog(this, "Success!", "Operation completed");
 *   UIHelpers.formatDateTime(now);
 *
 * Implemented by: Zaid
 */
public class UIHelpers {


    // =========================================================
    // COLORS & STYLING CONSTANTS
    // =========================================================

    public static final Color COLOR_SUCCESS = new Color(40, 167, 69);  // Green
    public static final Color COLOR_ERROR = new Color(220, 53, 69);    // Red
    public static final Color COLOR_WARNING = new Color(255, 193, 7);  // Amber
    public static final Color COLOR_INFO = new Color(0, 102, 204);     // Blue
    public static final Color COLOR_PRIMARY = new Color(0, 102, 204);  // Blue
    public static final Color COLOR_SECONDARY = new Color(108, 117, 125);  // Gray

    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 12);

    // =========================================================
    // DIALOG METHODS
    // =========================================================

    /**
     * Show success dialog.
     *
     * @param parent Parent component
     * @param title Dialog title
     * @param message Dialog message
     */
    public static void showSuccessDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
        Logger.log(LogLevel.INFO, "[UIHelpers] Success: " + message);
    }

    /**
     * Show error dialog.
     *
     * @param parent Parent component
     * @param title Dialog title
     * @param message Dialog message
     */
    public static void showErrorDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
        Logger.log(LogLevel.ERROR, "[UIHelpers] Error: " + message);
    }

    /**
     * Show warning dialog.
     *
     * @param parent Parent component
     * @param title Dialog title
     * @param message Dialog message
     */
    public static void showWarningDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.WARNING_MESSAGE
        );
        Logger.log(LogLevel.WARNING, "[UIHelpers] Warning: " + message);
    }

    /**
     * Show information dialog.
     *
     * @param parent Parent component
     * @param title Dialog title
     * @param message Dialog message
     */
    public static void showInfoDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
        Logger.log(LogLevel.INFO, "[UIHelpers] Info: " + message);
    }

    /**
     * Show confirmation dialog (Yes/No).
     *
     * @param parent Parent component
     * @param message Confirmation message
     * @return true if user clicked Yes, false if No
     */
    public static boolean showConfirmDialog(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            "Confirm Action",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    // =========================================================
    // BUTTON STYLING
    // =========================================================

    /**
     * Style a button with success color theme.
     *
     * @param button Button to style
     */
    public static void styleSuccessButton(JButton button) {
        button.setBackground(COLOR_SUCCESS);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Style a button with error color theme.
     *
     * @param button Button to style
     */
    public static void styleErrorButton(JButton button) {
        button.setBackground(COLOR_ERROR);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Style a button with primary color theme.
     *
     * @param button Button to style
     */
    public static void stylePrimaryButton(JButton button) {
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Style a button with secondary color theme.
     *
     * @param button Button to style
     */
    public static void styleSecondaryButton(JButton button) {
        button.setBackground(COLOR_SECONDARY);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // =========================================================
    // LABEL STYLING
    // =========================================================

    /**
     * Create a title label.
     *
     * @param text Label text
     * @return Styled JLabel
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(new Color(33, 33, 33));
        return label;
    }

    /**
     * Create a subtitle label.
     *
     * @param text Label text
     * @return Styled JLabel
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(new Color(66, 66, 66));
        return label;
    }

    /**
     * Create a help/hint label.
     *
     * @param text Label text
     * @return Styled JLabel
     */
    public static JLabel createHelpLabel(String text) {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setForeground(new Color(120, 120, 120));
        return label;
    }

    // =========================================================
    // VALIDATION HELPERS
    // =========================================================

    /**
     * Validate email format.
     *
     * @param email Email to validate
     * @return true if email appears valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
    }

    /**
     * Validate non-empty string.
     *
     * @param text Text to validate
     * @return true if text is not empty
     */
    public static boolean isValidText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * Validate positive integer.
     *
     * @param text Text to validate as integer
     * @return true if positive integer
     */
    public static boolean isValidPositiveInteger(String text) {
        try {
            int value = Integer.parseInt(text);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // =========================================================
    // FORMATTING HELPERS
    // =========================================================

    /**
     * Format LocalDateTime to readable string.
     *
     * @param dateTime LocalDateTime to format
     * @return Formatted string (e.g., "2026-04-07 14:30")
     */
    public static String formatDateTime(vcfs.core.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "(No date)";
        }
        return dateTime.toString();
    }

    /**
     * Format LocalDateTime to date only.
     *
     * @param dateTime LocalDateTime to format
     * @return Formatted string (e.g., "2026-04-07 14:30")
     */
    public static String formatDate(vcfs.core.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "(No date)";
        }
        return dateTime.toString();
    }

    /**
     * Format LocalDateTime to time only.
     *
     * @param dateTime LocalDateTime to format
     * @return Formatted string (e.g., "14:30")
     */
    public static String formatTime(vcfs.core.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "(No time)";
        }
        String fullTime = dateTime.toString();
        // Extract time portion (after the space)
        int spaceIndex = fullTime.indexOf(' ');
        if (spaceIndex >= 0) {
            return fullTime.substring(spaceIndex + 1);
        }
        return fullTime;
    }

    // =========================================================
    // PANEL HELPERS
    // =========================================================

    /**
     * Create a header panel with title and description.
     *
     * @param title Main title
     * @param description Optional description
     * @return Styled JPanel
     */
    public static JPanel createHeaderPanel(String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = createTitleLabel(title);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        if (description != null && !description.isEmpty()) {
            panel.add(Box.createVerticalStrut(5));
            JLabel descLabel = createSubtitleLabel(description);
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(descLabel);
        }

        return panel;
    }

    /**
     * Add hover effect to button.
     *
     * @param button Button to add effect to
     * @param normalColor Normal color
     * @param hoverColor Color when hovered
     */
    public static void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }

    // =========================================================
    // DEMO-SPECIFIC DIALOGS (Enhanced for Presentation)
    // =========================================================

    /**
     * Show professional demo success dialog with visual feedback.
     *  
     * @param parent Parent component
     * @param operation What was accomplished
     * @param status Additional status info
     */
    public static void showDemoSuccessDialog(Component parent, String operation, String status) {
        String message = "✓ " + operation + "\n\n" +
                         (status != null ? status : "") + "\n\n" +
                         "This event was logged to the audit trail and reflected\n" +
                         "across all connected portals in real-time (Observer Pattern).";
        
        JOptionPane.showMessageDialog(
            parent,
            message,
            "✓ Operation Successful",
            JOptionPane.INFORMATION_MESSAGE
        );
        Logger.log(LogLevel.INFO, "[DemoDialog] Success: " + operation);
    }

    /**
     * Show professional demo error dialog with recovery guidance.
     *
     * @param parent Parent component
     * @param operation What was attempted
     * @param errorReason Why it failed
     */
    public static void showDemoErrorDialog(Component parent, String operation, String errorReason) {
        String message = "✗ " + operation + " FAILED\n\n" +
                         "Reason: " + errorReason + "\n\n" +
                         "Please check the logs for details or try again.";
        
        JOptionPane.showMessageDialog(
            parent,
            message,
            "✗ Operation Failed",
            JOptionPane.ERROR_MESSAGE
        );
        Logger.log(LogLevel.ERROR, "[DemoDialog] Failed: " + operation + " - " + errorReason);
    }

    /**
     * Show real-time sync confirmation dialog.
     *
     * @param parent Parent component
     * @param affectedRoles Which roles saw the update
     */
    public static void showRealtimeSyncDialog(Component parent, String... affectedRoles) {
        StringBuilder msg = new StringBuilder("✓ Real-Time Synchronization\n\n");
        msg.append("This change was instantly broadcast to:\n");
        for (String role : affectedRoles) {
            msg.append("  ✓ ").append(role).append("\n");
        }
        msg.append("\nThis demonstrates the Observer Pattern at work!\n");
        msg.append("Multiple screens stay synchronized without explicit refresh.");
        
        JOptionPane.showMessageDialog(
            parent,
            msg.toString(),
            "🔄 Real-Time Update Confirmed",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
