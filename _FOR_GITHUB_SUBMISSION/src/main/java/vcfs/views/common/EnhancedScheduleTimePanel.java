package vcfs.views.common;

/**
 * Enhanced Schedule Time Panel with Demo Values
 * 
 * PURPOSE:
 * - Improved UI for time slot selection
 * - Shows demo values below each field for reference
 * - Better layout for new users
 * - Clear visual hierarchy
 * 
 * FEATURES:
 * - 3 time slots (can be reused or extended)
 * - Date selection dropdown
 * - Time selection dropdown
 * - Duration selection dropdown
 * - Demo value hints for each field
 * - Formatted, professional design
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class EnhancedScheduleTimePanel extends JPanel {
    
    private JLabel statusLabel;
    private JComboBox<String>[] dateSelectors;
    private JComboBox<String>[] timeSelectors;
    private JComboBox<String>[] durationSelectors;
    
    public EnhancedScheduleTimePanel() {
        initializePanel();
    }
    
    /**
     * Initialize the complete time settings panel with enhanced UI
     */
    private void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        addHeader();
        add(Box.createVerticalStrut(5));
        
        // Instructions
        addInstructions();
        add(Box.createVerticalStrut(20));
        
        // Time slots
        dateSelectors = new JComboBox[3];
        timeSelectors = new JComboBox[3];
        durationSelectors = new JComboBox[3];
        
        for (int i = 0; i < 3; i++) {
            addTimeSlot(i + 1);
            if (i < 2) add(Box.createVerticalStrut(15));
        }
        
        add(Box.createVerticalStrut(20));
        
        // Demo reference box
        addDemoReference();
        
        add(Box.createVerticalGlue());
    }
    
    /**
     * Add header section
     */
    private void addHeader() {
        JLabel header = new JLabel("📅 Preferred Meeting Times");
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(new Color(0, 51, 102));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(header);
    }
    
    /**
     * Add instructions section
     */
    private void addInstructions() {
        JLabel instructions = new JLabel(
            "<html>Please select three preferred time slots for your interviews. " +
            "You can adjust times if recruiters propose different schedules.</html>");
        instructions.setFont(new Font("Arial", Font.PLAIN, 12));
        instructions.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructions.setForeground(new Color(80, 80, 80));
        instructions.setMaximumSize(new Dimension(600, 50));
        add(instructions);
    }
    
    /**
     * Add individual time slot with demo values
     */
    private void addTimeSlot(int slotNumber) {
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new BoxLayout(slotPanel, BoxLayout.Y_AXIS));
        slotPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        slotPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        slotPanel.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2, true));
        slotPanel.setBackground(new Color(240, 248, 255));
        
        // Slot header
        JLabel slotLabel = new JLabel("Preferred Time Slot " + slotNumber);
        slotLabel.setFont(new Font("Arial", Font.BOLD, 13));
        slotLabel.setForeground(new Color(0, 51, 102));
        slotLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        slotPanel.add(Box.createVerticalStrut(5));
        slotPanel.add(slotLabel);
        slotPanel.add(Box.createVerticalStrut(8));
        
        // Date, Time, Duration row
        JPanel fieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        fieldsRow.setBackground(new Color(240, 248, 255));
        fieldsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Date dropdown
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldsRow.add(dateLabel);
        
        JComboBox<String> dateCombo = new JComboBox<>(getDates());
        dateCombo.setPreferredSize(new Dimension(140, 30));
        dateCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        dateSelectors[slotNumber - 1] = dateCombo;
        fieldsRow.add(dateCombo);
        
        // Time dropdown
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldsRow.add(timeLabel);
        
        JComboBox<String> timeCombo = new JComboBox<>(getTimes());
        timeCombo.setPreferredSize(new Dimension(95, 30));
        timeCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        timeSelectors[slotNumber - 1] = timeCombo;
        fieldsRow.add(timeCombo);
        
        // Duration dropdown
        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldsRow.add(durationLabel);
        
        JComboBox<String> durationCombo = new JComboBox<>(getDurations());
        durationCombo.setPreferredSize(new Dimension(100, 30));
        durationCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        durationSelectors[slotNumber - 1] = durationCombo;
        fieldsRow.add(durationCombo);
        
        slotPanel.add(fieldsRow);
        
        // Demo values hint
        JLabel demoValues = new JLabel(getDemoValueHint(slotNumber));
        demoValues.setFont(new Font("Arial", Font.ITALIC, 10));
        demoValues.setForeground(new Color(100, 100, 150));
        demoValues.setAlignmentX(Component.LEFT_ALIGNMENT);
        slotPanel.add(Box.createVerticalStrut(5));
        slotPanel.add(demoValues);
        slotPanel.add(Box.createVerticalStrut(5));
        
        add(slotPanel);
    }
    
    /**
     * Add demo reference box at bottom
     */
    private void addDemoReference() {
        JPanel demoRef = new JPanel();
        demoRef.setLayout(new BoxLayout(demoRef, BoxLayout.Y_AXIS));
        demoRef.setBackground(new Color(255, 250, 205));
        demoRef.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 2));
        demoRef.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 200, 0), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        demoRef.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        demoRef.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel demoTitle = new JLabel("💡 Example Time Slots:");
        demoTitle.setFont(new Font("Arial", Font.BOLD, 12));
        demoTitle.setForeground(new Color(180, 100, 0));
        demoRef.add(demoTitle);
        
        String[] demoExamples = {
            "   Slot 1: Monday, 09:00 AM, 30 min",
            "   Slot 2: Tuesday, 02:00 PM, 1 hour",
            "   Slot 3: Wednesday, 10:00 AM, 45 min"
        };
        
        for (String example : demoExamples) {
            JLabel demoLine = new JLabel(example);
            demoLine.setFont(new Font("Arial", Font.PLAIN, 10));
            demoLine.setForeground(new Color(100, 100, 100));
            demoRef.add(demoLine);
        }
        
        add(demoRef);
    }
    
    /**
     * Get available dates (next 7 days)
     */
    private String[] getDates() {
        return new String[]{
            "Monday, 14 April",
            "Tuesday, 15 April",
            "Wednesday, 16 April",
            "Thursday, 17 April",
            "Friday, 18 April"
        };
    }
    
    /**
     * Get available times (business hours)
     */
    private String[] getTimes() {
        return new String[]{
            "09:00 AM",
            "10:00 AM",
            "11:00 AM",
            "02:00 PM",
            "03:00 PM",
            "04:00 PM"
        };
    }
    
    /**
     * Get available durations
     */
    private String[] getDurations() {
        return new String[]{
            "30 minutes",
            "45 minutes",
            "1 hour"
        };
    }
    
    /**
     * Get demo hint for specific slot
     */
    private String getDemoValueHint(int slotNumber) {
        switch (slotNumber) {
            case 1:
                return "   📌 Demo: 'Monday, 14 April' | '09:00 AM' | '30 minutes' — Standard morning slot";
            case 2:
                return "   📌 Demo: 'Tuesday, 15 April' | '02:00 PM' | '1 hour' — Afternoon with buffer";
            case 3:
                return "   📌 Demo: 'Wednesday, 16 April' | '10:00 AM' | '45 minutes' — Mid-week flexibility";
            default:
                return "";
        }
    }
    
    /**
     * Get selected times from all slots
     */
    public String[][] getSelectedTimes() {
        String[][] selected = new String[3][3];
        for (int i = 0; i < 3; i++) {
            selected[i][0] = dateSelectors[i] != null ? (String) dateSelectors[i].getSelectedItem() : "";
            selected[i][1] = timeSelectors[i] != null ? (String) timeSelectors[i].getSelectedItem() : "";
            selected[i][2] = durationSelectors[i] != null ? (String) durationSelectors[i].getSelectedItem() : "";
        }
        return selected;
    }
    
    /**
     * Show times in readable format
     */
    public String getFormattedTimes() {
        StringBuilder sb = new StringBuilder("Preferred Meeting Times:\n");
        String[][] times = getSelectedTimes();
        for (int i = 0; i < 3; i++) {
            sb.append((i + 1)).append(". ")
              .append(times[i][0]).append(" at ")
              .append(times[i][1]).append(" (")
              .append(times[i][2]).append(")\n");
        }
        return sb.toString();
    }
}
