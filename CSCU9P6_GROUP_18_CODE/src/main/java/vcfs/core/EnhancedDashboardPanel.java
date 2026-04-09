package vcfs.core;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * EnhancedDashboardPanel - Unified statistics dashboard for all portals
 * 
 * PURPOSE:
 *   - Display real-time system statistics in a consistent format
 *   - Show metrics for organizations, booths, recruiters, candidates, offers, bookings
 *   - Auto-update when system state changes (via SystemStatistics)
 *   - Provide single, reusable component across AdminScreen, RecruiterScreen, CandidateScreen
 * 
 * FEATURES:
 *   - Real-time metric updates
 *   - Color-coded status indicators
 *   - Responsive grid layout
 *   - Professional styling with borders and spacing
 * 
 * USAGE:
 *   EnhancedDashboardPanel dashboard = new EnhancedDashboardPanel();
 *   panel.add(dashboard, BorderLayout.NORTH);
 * 
 * @author Zaid Siddiqui (mzs00007)
 */
public class EnhancedDashboardPanel extends JPanel implements PropertyChangeListener {

    
    private JLabel orgsLabel;
    private JLabel boothsLabel;
    private JLabel recruitersLabel;
    private JLabel candidatesLabel;
    private JLabel offersLabel;
    private JLabel bookingsLabel;
    private JLabel phaseLabel;
    private JLabel systemStatusLabel;
    private SystemStatistics stats;
    
    public EnhancedDashboardPanel() {
        setLayout(new GridLayout(2, 4, 15, 15));
        setBackground(new Color(240, 245, 250));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(25, 70, 130), 2),
            "System Statistics & Metrics",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 13),
            new Color(25, 70, 130)
        ));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Initialize labels
        orgsLabel = createMetricLabel("Organizations", "0", new Color(0, 150, 136));
        boothsLabel = createMetricLabel("Booths", "0", new Color(63, 81, 181));
        recruitersLabel = createMetricLabel("Recruiters", "0", new Color(244, 67, 54));
        candidatesLabel = createMetricLabel("Candidates", "0", new Color(76, 175, 80));
        offersLabel = createMetricLabel("Offers Published", "0", new Color(255, 152, 0));
        bookingsLabel = createMetricLabel("Bookings Made", "0", new Color(156, 39, 176));
        phaseLabel = createMetricLabel("Current Phase", "SETUP", new Color(230, 124, 115));
        systemStatusLabel = createMetricLabel("System Status", "Initializing", new Color(38, 198, 218));
        
        // Add to panel
        add(orgsLabel);
        add(boothsLabel);
        add(recruitersLabel);
        add(candidatesLabel);
        add(offersLabel);
        add(bookingsLabel);
        add(phaseLabel);
        add(systemStatusLabel);
        
        // Register for updates
        stats = SystemStatistics.getInstance();
        stats.addPropertyChangeListener(this);
    }
    
    /**
     * Create a styled metric label
     */
    private JLabel createMetricLabel(String title, String value, Color accentColor) {
        JLabel label = new JLabel();
        label.setLayout(new BorderLayout(5, 5));
        label.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        label.setBackground(new Color(255, 255, 255));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(accentColor);
        
        // Value label
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        label.add(titleLabel, BorderLayout.NORTH);
        label.add(valueLabel, BorderLayout.CENTER);
        
        return label;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        
        SwingUtilities.invokeLater(() -> {
            switch (prop) {
                case "totalOrganizations":
                    updateLabel(orgsLabel, (Integer) evt.getNewValue());
                    break;
                case "totalBooths":
                    updateLabel(boothsLabel, (Integer) evt.getNewValue());
                    break;
                case "totalRecruiters":
                    updateLabel(recruitersLabel, (Integer) evt.getNewValue());
                    break;
                case "totalCandidates":
                    updateLabel(candidatesLabel, (Integer) evt.getNewValue());
                    break;
                case "totalOffers":
                    updateLabel(offersLabel, (Integer) evt.getNewValue());
                    break;
                case "totalBookings":
                    updateLabel(bookingsLabel, (Integer) evt.getNewValue());
                    break;
                case "currentPhase":
                    systemStatusLabel.setText(evt.getNewValue().toString());
                    break;
                case "systemStatus":
                    systemStatusLabel.setText((String) evt.getNewValue());
                    break;
            }
            repaint();
        });
    }
    
    /**
     * Update a metric label with a new value
     */
    private void updateLabel(JLabel label, Integer value) {
        // Find the value label (the second component)
        if (label.getComponentCount() > 0) {
            Component[] components = label.getComponents();
            for (Component c : components) {
                if (c instanceof JLabel && ((JLabel) c).getFont().getSize() > 20) {
                    ((JLabel) c).setText(String.valueOf(value));
                    break;
                }
            }
        }
    }
}
