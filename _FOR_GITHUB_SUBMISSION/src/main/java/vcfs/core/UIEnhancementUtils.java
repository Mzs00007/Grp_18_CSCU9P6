package vcfs.core;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * UI ENHANCEMENT UTILITIES - Professional UI components for all portals
 * 
 * Provides:
 * - Consistent styling across all screens
 * - Info/Warning/Error/Success dialogs
 * - Flow guidance integration
 * - Professional icon support
 * - Responsive layouts
 */
public class UIEnhancementUtils {

    
    // Color scheme
    public static final Color PRIMARY_GREEN = new Color(76, 175, 80);
    public static final Color PRIMARY_BLUE = new Color(33, 150, 243);
    public static final Color PRIMARY_ORANGE = new Color(255, 152, 0);
    public static final Color ERROR_RED = new Color(244, 67, 54);
    public static final Color SUCCESS_GREEN = new Color(76, 175, 80);
    public static final Color WARNING_ORANGE = new Color(255, 152, 0);
    public static final Color LINK_BLUE = new Color(33, 150, 243);
    public static final Color LIGHT_BACKGROUND = new Color(245, 248, 250);
    public static final Color CARD_WHITE = new Color(255, 255, 255);
    public static final Color BORDER_GRAY = new Color(224, 224, 224);
    
    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    public static final Font MONO_FONT = new Font("Courier New", Font.PLAIN, 11);
    
    /**
     * Create a professional header panel
     */
    public static JPanel createHeaderPanel(String title, String subtitle, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout(10, 8));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(SMALL_FONT);
        subtitleLabel.setForeground(new Color(225, 245, 225));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(bgColor);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        panel.add(textPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Create an info panel with icon and message
     */
    public static JPanel createInfoPanel(String title, String message) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(227, 242, 253));
        panel.setBorder(BorderFactory.createLineBorder(LINK_BLUE, 2, true));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel("ℹ️");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(227, 242, 253));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(LINK_BLUE);
        
        JLabel msgLabel = new JLabel("<html>" + message + "</html>");
        msgLabel.setFont(NORMAL_FONT);
        msgLabel.setForeground(new Color(33, 33, 33));
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(msgLabel);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a success notification
     */
    public static void showSuccess(JFrame parent, String title, String message) {
        showNotification(parent, title, message, "✓", SUCCESS_GREEN, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Create an error notification
     */
    public static void showError(JFrame parent, String title, String message) {
        showNotification(parent, title, message, "✗", ERROR_RED, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Create a warning notification
     */
    public static void showWarning(JFrame parent, String title, String message) {
        showNotification(parent, title, message, "⚠", WARNING_ORANGE, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Create an info notification
     */
    public static void showInfo(JFrame parent, String title, String message) {
        showNotification(parent, title, message, "ℹ️", LINK_BLUE, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void showNotification(JFrame parent, String title, String message, 
                                         String icon, Color color, int type) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        
        JLabel msgLabel = new JLabel("<html><b>" + title + "</b><br/>" + message + "</html>");
        msgLabel.setFont(NORMAL_FONT);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(msgLabel, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(parent, panel, title, type);
    }
    
    /**
     * Create a professional button with styling
     */
    public static JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(NORMAL_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setMargin(new Insets(8, 16, 8, 16));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkerColor(bgColor));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Create a primary action button (green)
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY_GREEN, Color.WHITE);
    }
    
    /**
     * Create a secondary action button (blue)
     */
    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, PRIMARY_BLUE, Color.WHITE);
    }
    
    /**
     * Create a danger action button (red)
     */
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, ERROR_RED, Color.WHITE);
    }
    
    /**
     * Create a help/info button
     */
    public static JButton createHelpButton(String tooltip) {
        JButton btn = new JButton("?");
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(LINK_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(28, 28));
        btn.setToolTipText(tooltip);
        return btn;
    }
    
    private static Color darkerColor(Color color) {
        return new Color(
            Math.max(color.getRed() - 30, 0),
            Math.max(color.getGreen() - 30, 0),
            Math.max(color.getBlue() - 30, 0)
        );
    }
    
    /**
     * Create a titled section panel
     */
    public static JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(title));
        panel.setBackground(LIGHT_BACKGROUND);
        return panel;
    }
    
    /**
     * Create a card-style panel (white background with subtle shadow)
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_WHITE);
        panel.setBorder(BorderFactory.createLineBorder(BORDER_GRAY, 1, true));
        return panel;
    }
    
    /**
     * Show a confirmation dialog with custom buttons
     */
    public static int showConfirmDialog(JFrame parent, String title, String message,
                                        String... buttonLabels) {
        if (buttonLabels.length == 0) {
            return JOptionPane.showConfirmDialog(parent, message, title,
                JOptionPane.YES_NO_CANCEL_OPTION);
        }
        return JOptionPane.showOptionDialog(parent, message, title,
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, buttonLabels, buttonLabels[0]);
    }
}
