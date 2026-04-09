package vcfs.views.shared;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Collapsible Demo Notes Panel - Reusable component for all portals
 * 
 * Project Manager & Lead Developer: Zaid Siddiqui (mzs00007)
 */

import javax.swing.*;
import java.awt.*;

/**
 * Provides a collapsible demo notes panel with up/down arrow toggle.
 * Can be reused across all portals (Admin, Recruiter, Candidate).
 * 
 * FEATURES:
 *   ✓ Collapsible/expandable with arrow toggle button
 *   ✓ Up arrow (▲) when expanded, Down arrow (▼) when collapsed
 *   ✓ Instructor-only content hidden by default
 *   ✓ Full-length expansion to show all notes
 *   ✓ Professional styling with green theme
 */
public class CollapsibleDemoNotesPanel extends JPanel {
    
    private JButton toggleButton;
    private JScrollPane contentScrollPane;
    private JPanel contentPanel;
    private boolean isExpanded = false;
    
    public CollapsibleDemoNotesPanel(String portalName, String[] notesTitles, String[] notesContent) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 248, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // ===== TOGGLE BUTTON WITH ARROW =====
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        togglePanel.setBackground(new Color(232, 245, 233));
        togglePanel.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));
        
        toggleButton = new JButton("▼ Demo Notes: " + portalName + " (Instructor Only)");
        toggleButton.setFont(new Font("Arial", Font.BOLD, 12));
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setBackground(new Color(76, 175, 80));
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.setOpaque(true);
        
        toggleButton.addActionListener(e -> toggleExpand());
        
        // Hover effect
        toggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toggleButton.setBackground(toggleButton.getBackground().darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isExpanded) {
                    toggleButton.setBackground(new Color(76, 175, 80));
                } else {
                    toggleButton.setBackground(new Color(56, 142, 60));
                }
            }
        });
        
        togglePanel.add(toggleButton);
        add(togglePanel, BorderLayout.NORTH);
        
        // ===== CONTENT PANEL (COLLAPSIBLE) =====
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 250, 240));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Add all note items
        if (notesTitles.length == notesContent.length) {
            for (int i = 0; i < notesTitles.length; i++) {
                addNoteItem(notesTitles[i], notesContent[i]);
            }
        }
        
        // Add to scroll pane
        contentScrollPane = new JScrollPane(contentPanel);
        contentScrollPane.setVisible(false);
        contentScrollPane.setPreferredSize(new Dimension(800, 0));
        add(contentScrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Add a single note item to the panel.
     */
    private void addNoteItem(String title, String content) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(new Color(240, 250, 240));
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(new Color(27, 94, 32));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel contentLabel = new JLabel("<html>" + content.replace("\n", "<br/>") + "</html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        contentLabel.setForeground(new Color(50, 50, 50));
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        itemPanel.add(titleLabel);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(contentLabel);
        itemPanel.add(Box.createVerticalStrut(8));
        
        contentPanel.add(itemPanel);
    }
    
    /**
     * Toggle the expansion state of the demo notes.
     */
    private void toggleExpand() {
        isExpanded = !isExpanded;
        
        if (isExpanded) {
            // Expand
            toggleButton.setText("▲ Demo Notes: Portal (Instructor Only)");
            toggleButton.setBackground(new Color(56, 142, 60));
            contentScrollPane.setVisible(true);
            contentScrollPane.setPreferredSize(new Dimension(getWidth(), 280));
        } else {
            // Collapse
            toggleButton.setText("▼ Demo Notes: Portal (Instructor Only)");
            toggleButton.setBackground(new Color(76, 175, 80));
            contentScrollPane.setVisible(false);
            contentScrollPane.setPreferredSize(new Dimension(getWidth(), 0));
        }
        
        // Revalidate and repaint
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
            getParent().revalidate();
            getParent().repaint();
        });
    }
}
