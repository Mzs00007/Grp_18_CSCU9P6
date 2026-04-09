package vcfs.views.common;

import vcfs.core.Logger;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Enhanced Schedule Time Selection Panel V2
 * Comprehensive time slot selection with extensive UI guidance and demo value examples
 * 
 * Purpose: Allow candidates to specify 3 preferred meeting times with:
 * - Large, spacious layout for readability
 * - Color-coded time slots
 * - Multiple demo value hints per slot
 * - Pattern selection buttons (quick presets)
 * - Comprehensive tips and best practices
 * - Formatted examples section
 * - Visual hierarchy with sections
 */
public class EnhancedScheduleTimePanel_V2 extends JPanel {
    private JComboBox<String>[] dateSelectors;
    private JComboBox<String>[] timeSelectors;
    private JComboBox<String>[] durationSelectors;
    private JLabel[] demoHints;
    private JButton[] patternButtons;
    
    private static final Color SLOT_1_COLOR = new Color(66, 135, 245);      // Blue
    private static final Color SLOT_2_COLOR = new Color(76, 175, 80);       // Green
    private static final Color SLOT_3_COLOR = new Color(255, 152, 0);       // Orange
    private static final Color DEMO_HINT_COLOR = new Color(100, 100, 100);
    private static final Color TIPS_BG = new Color(245, 250, 255);
    private static final Color EXAMPLE_BG = new Color(240, 248, 245);

    public EnhancedScheduleTimePanel_V2() {
        initializePanel();
    }

    /**
     * Initialize the entire panel with all components
     * Layout: Header | Instructions | 3 Time Slots | Pattern Buttons | Tips | Examples | Reference
     */
    private void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Title Header
        addHeader();
        
        // 2. Subtitle with instructions
        addSubtitle();
        
        // 3. Three time slot sections
        dateSelectors = new JComboBox[3];
        timeSelectors = new JComboBox[3];
        durationSelectors = new JComboBox[3];
        demoHints = new JLabel[3];
        
        for (int i = 0; i < 3; i++) {
            addTimeSlot(i);
            addVerticalGap(15);
        }
        
        // 4. Pattern quick-select buttons
        addPatternButtons();
        addVerticalGap(20);
        
        // 5. Tips and best practices
        addTipsSection();
        addVerticalGap(20);
        
        // 6. Formatted examples
        addExamplesSection();
        addVerticalGap(20);
        
        // 7. Reference box
        addReferenceBox();
        
        // 8. Bottom spacer
        add(Box.createVerticalGlue());
    }

    /**
     * Add main title header
     */
    private void addHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("📅 Your Preferred Meeting Times");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        
        JLabel subtitle = new JLabel("Select 3 time slots when you're available for interviews");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(90, 90, 90));
        
        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitle);
        
        add(headerPanel);
        addVerticalGap(20);
    }

    /**
     * Add informational subtitle
     */
    private void addSubtitle() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 150, 200), 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JTextArea info = new JTextArea(
            "💡 Why 3 slots? Recruiters will try to match your availability. " +
            "Space them out across different days and times for better scheduling flexibility."
        );
        info.setFont(new Font("Arial", Font.PLAIN, 12));
        info.setForeground(new Color(60, 60, 120));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
        info.setBackground(new Color(240, 248, 255));
        info.setBorder(null);
        
        infoPanel.add(info);
        add(infoPanel);
        addVerticalGap(20);
    }

    /**
     * Add a single time slot with all controls
     * Slot numbers: 0, 1, 2 (displayed as 1st, 2nd, 3rd)
     */
    private void addTimeSlot(int slotNumber) {
        Color slotColor = slotNumber == 0 ? SLOT_1_COLOR : 
                         slotNumber == 1 ? SLOT_2_COLOR : SLOT_3_COLOR;
        
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new BoxLayout(slotPanel, BoxLayout.Y_AXIS));
        slotPanel.setBackground(Color.WHITE);
        slotPanel.setBorder(new CompoundBorder(
            new LineBorder(slotColor, 3),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Slot header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel slotLabel = new JLabel(
            "🎯 Slot " + (slotNumber + 1) + 
            (slotNumber == 0 ? " (Morning preferred)" : 
             slotNumber == 1 ? " (Mid-day preferred)" : 
             slotNumber == 2 ? " (Alternative time)" : "")
        );
        slotLabel.setFont(new Font("Arial", Font.BOLD, 14));
        slotLabel.setForeground(slotColor);
        
        headerPanel.add(slotLabel);
        slotPanel.add(headerPanel);
        slotPanel.add(Box.createVerticalStrut(10));
        
        // Date selector
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(new JLabel("📆 Date:"));
        dateSelectors[slotNumber] = createDateCombo();
        datePanel.add(dateSelectors[slotNumber]);
        JLabel dateDemoHint = new JLabel(" (e.g., Monday, 2026-03-31)");
        dateDemoHint.setFont(new Font("Arial", Font.ITALIC, 11));
        dateDemoHint.setForeground(DEMO_HINT_COLOR);
        datePanel.add(dateDemoHint);
        slotPanel.add(datePanel);
        slotPanel.add(Box.createVerticalStrut(8));
        
        // Time selector
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.setBackground(Color.WHITE);
        timePanel.add(new JLabel("🕐 Time:"));
        timeSelectors[slotNumber] = createTimeCombo();
        timePanel.add(timeSelectors[slotNumber]);
        JLabel timeDemoHint = new JLabel(" (e.g., 09:00 AM - morning is typically better)");
        timeDemoHint.setFont(new Font("Arial", Font.ITALIC, 11));
        timeDemoHint.setForeground(DEMO_HINT_COLOR);
        timePanel.add(timeDemoHint);
        slotPanel.add(timePanel);
        slotPanel.add(Box.createVerticalStrut(8));
        
        // Duration selector
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        durationPanel.setBackground(Color.WHITE);
        durationPanel.add(new JLabel("⏱️  Duration:"));
        durationSelectors[slotNumber] = createDurationCombo();
        durationPanel.add(durationSelectors[slotNumber]);
        JLabel durationDemoHint = new JLabel(" (e.g., 45 minutes - standard interview length)");
        durationDemoHint.setFont(new Font("Arial", Font.ITALIC, 11));
        durationDemoHint.setForeground(DEMO_HINT_COLOR);
        durationPanel.add(durationDemoHint);
        slotPanel.add(durationPanel);
        slotPanel.add(Box.createVerticalStrut(10));
        
        // Demo value reference with color background
        JPanel demoRefPanel = new JPanel();
        demoRefPanel.setLayout(new BoxLayout(demoRefPanel, BoxLayout.Y_AXIS));
        demoRefPanel.setBackground(new Color(240, 245, 250));
        demoRefPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        demoHints[slotNumber] = new JLabel();
        demoHints[slotNumber].setFont(new Font("Arial", Font.ITALIC, 11));
        demoHints[slotNumber].setForeground(new Color(80, 120, 160));
        updateDemoHint(slotNumber);
        demoRefPanel.add(demoHints[slotNumber]);
        
        slotPanel.add(demoRefPanel);
        
        add(slotPanel);
    }

    /**
     * Update demo hint for a specific slot based on typical scenario
     */
    private void updateDemoHint(int slotNumber) {
        String hint = "";
        if (slotNumber == 0) {
            hint = "📌 Demo 1: Monday, 09:00 AM, 45 min | Tuesday, 10:30 AM, 1 hour | Wednesday, 09:00 AM, 30 min";
        } else if (slotNumber == 1) {
            hint = "📌 Demo 2: Different days - good coverage! E.g., Thursday, 02:00 PM, 45 min | Friday, 10:00 AM, 1 hour";
        } else {
            hint = "📌 Demo 3: Alternative scenario - Friday, 03:00 PM, 45 min | Monday, 02:30 PM, 1 hour";
        }
        demoHints[slotNumber].setText(hint);
    }

    /**
     * Add pattern quick-select buttons
     * Allows users to quickly fill in common patterns
     */
    private void addPatternButtons() {
        JPanel patternPanel = new JPanel();
        patternPanel.setLayout(new BoxLayout(patternPanel, BoxLayout.Y_AXIS));
        patternPanel.setBackground(new Color(255, 250, 240));
        patternPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 152, 0), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel patternLabel = new JLabel("⚡ Quick Patterns - Click to Auto-Fill:");
        patternLabel.setFont(new Font("Arial", Font.BOLD, 12));
        patternPanel.add(patternLabel);
        patternPanel.add(Box.createVerticalStrut(10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(new Color(255, 250, 240));
        
        patternButtons = new JButton[4];
        String[] patternNames = {
            "🌅 Early Bird (9-11am)",
            "☀️ Mid-Day (1-3pm)",
            "🕐 Evening (3-5pm)",
            "🔄 Flexible (Mix times)"
        };
        
        for (int i = 0; i < patternNames.length; i++) {
            final int patternIndex = i;
            patternButtons[i] = new JButton(patternNames[i]);
            patternButtons[i].setFont(new Font("Arial", Font.PLAIN, 11));
            patternButtons[i].setBackground(new Color(255, 200, 100));
            patternButtons[i].setForeground(Color.BLACK);
            patternButtons[i].setBorder(new RoundBorder(5));
            patternButtons[i].addActionListener(e -> applyPattern(patternIndex));
            buttonPanel.add(patternButtons[i]);
        }
        
        patternPanel.add(buttonPanel);
        add(patternPanel);
    }

    /**
     * Add tips and best practices section
     */
    private void addTipsSection() {
        JPanel tipsPanel = new JPanel();
        tipsPanel.setLayout(new BoxLayout(tipsPanel, BoxLayout.Y_AXIS));
        tipsPanel.setBackground(TIPS_BG);
        tipsPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(100, 150, 200), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tipsTitle = new JLabel("💡 Tips for Better Scheduling:");
        tipsTitle.setFont(new Font("Arial", Font.BOLD, 12));
        tipsPanel.add(tipsTitle);
        tipsPanel.add(Box.createVerticalStrut(8));
        
        String[] tips = {
            "1️⃣ Morning slots (9-11am) are generally more available for recruiters",
            "2️⃣ Space slots 2-3 hours apart for recruiter scheduling flexibility",
            "3️⃣ Include at least one morning slot and one afternoon slot",
            "4️⃣ 45 minutes is the standard interview length - use that as default",
            "5️⃣ Provide slots across 2-3 different days for maximum availability",
            "6️⃣ Be realistic - choose times when you're alert and well-rested",
            "7️⃣ You can be flexible! Recruiters will see ALL your slots and pick best fit"
        };
        
        for (String tip : tips) {
            JLabel tipLabel = new JLabel(tip);
            tipLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            tipLabel.setForeground(new Color(40, 40, 100));
            tipsPanel.add(tipLabel);
            tipsPanel.add(Box.createVerticalStrut(5));
        }
        
        add(tipsPanel);
    }

    /**
     * Add formatted examples section
     * Shows 3-4 different realistic example schedules
     */
    private void addExamplesSection() {
        JPanel examplesPanel = new JPanel();
        examplesPanel.setLayout(new BoxLayout(examplesPanel, BoxLayout.Y_AXIS));
        examplesPanel.setBackground(EXAMPLE_BG);
        examplesPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(76, 175, 80), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel examplesTitle = new JLabel("📋 Example Schedules:");
        examplesTitle.setFont(new Font("Arial", Font.BOLD, 12));
        examplesPanel.add(examplesTitle);
        examplesPanel.add(Box.createVerticalStrut(10));
        
        // Example 1: Early Bird
        addExampleBlock(examplesPanel, "Example 1: Early Bird",
            "• Slot 1: Monday, 09:00 AM, 45 min",
            "• Slot 2: Tuesday, 10:30 AM, 1 hour",
            "• Slot 3: Wednesday, 09:00 AM, 45 min");
        
        examplesPanel.add(Box.createVerticalStrut(10));
        
        // Example 2: Flexible Schedule
        addExampleBlock(examplesPanel, "Example 2: Flexible Schedule",
            "• Slot 1: Monday, 02:00 PM, 1 hour",
            "• Slot 2: Wednesday, 03:00 PM, 45 min",
            "• Slot 3: Thursday, 10:00 AM, 30 min");
        
        examplesPanel.add(Box.createVerticalStrut(10));
        
        // Example 3: Spread Out (Best Practice)
        addExampleBlock(examplesPanel, "Example 3: Spread Out (★ RECOMMENDED)",
            "• Slot 1: Monday, 09:30 AM, 45 min",
            "• Slot 2: Wednesday, 02:00 PM, 1 hour",
            "• Slot 3: Friday, 10:00 AM, 45 min");
        
        add(examplesPanel);
    }

    /**
     * Helper to add a single example block
     */
    private void addExampleBlock(JPanel parent, String title, String... slots) {
        JPanel blockPanel = new JPanel();
        blockPanel.setLayout(new BoxLayout(blockPanel, BoxLayout.Y_AXIS));
        blockPanel.setBackground(new Color(255, 255, 255));
        blockPanel.setBorder(new LineBorder(new Color(150, 150, 150), 1));
        blockPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(150, 150, 150), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(new Color(40, 100, 40));
        blockPanel.add(titleLabel);
        blockPanel.add(Box.createVerticalStrut(5));
        
        for (String slot : slots) {
            JLabel slotLabel = new JLabel(slot);
            slotLabel.setFont(new Font("Courier", Font.PLAIN, 10));
            slotLabel.setForeground(new Color(60, 60, 60));
            blockPanel.add(slotLabel);
        }
        
        parent.add(blockPanel);
    }

    /**
     * Add reference box with all important info
     */
    private void addReferenceBox() {
        JPanel refPanel = new JPanel();
        refPanel.setLayout(new BoxLayout(refPanel, BoxLayout.Y_AXIS));
        refPanel.setBackground(new Color(245, 245, 245));
        refPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(100, 100, 100), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel refTitle = new JLabel("📚 Reference Information:");
        refTitle.setFont(new Font("Arial", Font.BOLD, 11));
        refPanel.add(refTitle);
        refPanel.add(Box.createVerticalStrut(8));
        
        String[] reference = {
            "✓ Interview Duration: Typically 30-60 minutes (45 min is standard)",
            "✓ Best Times: Mornings 9-11am are more available than afternoons",
            "✓ Spacing: Recruiters appreciate 2+ hour gaps between slots",
            "✓ Dates: Spread across 2-3 days if possible for flexibility",
            "✓ Feedback: Your selections will be ranked and matched with recruiters",
            "✓ Changes: You can update your time slots until the deadline"
        };
        
        for (String ref : reference) {
            JLabel refLabel = new JLabel(ref);
            refLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            refLabel.setForeground(new Color(80, 80, 80));
            refPanel.add(refLabel);
            refPanel.add(Box.createVerticalStrut(4));
        }
        
        add(refPanel);
    }

    /**
     * Apply a quick pattern to fill in time slots
     */
    private void applyPattern(int patternIndex) {
        if (patternIndex == 0) { // Early Bird
            dateSelectors[0].setSelectedIndex(0);
            timeSelectors[0].setSelectedItem("09:00 AM");
            durationSelectors[0].setSelectedItem("45 min");
            
            dateSelectors[1].setSelectedIndex(1);
            timeSelectors[1].setSelectedItem("10:30 AM");
            durationSelectors[1].setSelectedItem("1 hour");
            
            dateSelectors[2].setSelectedIndex(2);
            timeSelectors[2].setSelectedItem("09:00 AM");
            durationSelectors[2].setSelectedItem("45 min");
        } else if (patternIndex == 1) { // Mid-Day
            dateSelectors[0].setSelectedIndex(0);
            timeSelectors[0].setSelectedItem("01:00 PM");
            durationSelectors[0].setSelectedItem("45 min");
            
            dateSelectors[1].setSelectedIndex(1);
            timeSelectors[1].setSelectedItem("02:00 PM");
            durationSelectors[1].setSelectedItem("1 hour");
            
            dateSelectors[2].setSelectedIndex(2);
            timeSelectors[2].setSelectedItem("01:30 PM");
            durationSelectors[2].setSelectedItem("45 min");
        } else if (patternIndex == 2) { // Evening
            dateSelectors[0].setSelectedIndex(0);
            timeSelectors[0].setSelectedItem("03:00 PM");
            durationSelectors[0].setSelectedItem("45 min");
            
            dateSelectors[1].setSelectedIndex(1);
            timeSelectors[1].setSelectedItem("04:00 PM");
            durationSelectors[1].setSelectedItem("1 hour");
            
            dateSelectors[2].setSelectedIndex(2);
            timeSelectors[2].setSelectedItem("03:30 PM");
            durationSelectors[2].setSelectedItem("45 min");
        } else { // Flexible Mix
            dateSelectors[0].setSelectedIndex(0);
            timeSelectors[0].setSelectedItem("09:30 AM");
            durationSelectors[0].setSelectedItem("45 min");
            
            dateSelectors[1].setSelectedIndex(1);
            timeSelectors[1].setSelectedItem("02:00 PM");
            durationSelectors[1].setSelectedItem("1 hour");
            
            dateSelectors[2].setSelectedIndex(2);
            timeSelectors[2].setSelectedItem("10:00 AM");
            durationSelectors[2].setSelectedItem("45 min");
        }
        
        Logger.info("[TimePanel] Applied pattern: " + patternIndex);
    }

    /**
     * Create date dropdown with multiple date options
     */
    private JComboBox<String> createDateCombo() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String[] dates = {
            "Monday, 2026-03-30",
            "Tuesday, 2026-03-31",
            "Wednesday, 2026-04-01",
            "Thursday, 2026-04-02",
            "Friday, 2026-04-03",
            "Monday, 2026-04-06",
            "Tuesday, 2026-04-07"
        };
        for (String date : dates) {
            model.addElement(date);
        }
        JComboBox<String> combo = new JComboBox<>(model);
        combo.setFont(new Font("Arial", Font.PLAIN, 11));
        combo.setPreferredSize(new Dimension(200, 25));
        return combo;
    }

    /**
     * Create time dropdown with hourly intervals
     */
    private JComboBox<String> createTimeCombo() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String[] times = {
            "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
            "12:00 PM", "12:30 PM",
            "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM", "05:00 PM"
        };
        for (String time : times) {
            model.addElement(time);
        }
        JComboBox<String> combo = new JComboBox<>(model);
        combo.setFont(new Font("Arial", Font.PLAIN, 11));
        combo.setPreferredSize(new Dimension(150, 25));
        return combo;
    }

    /**
     * Create duration dropdown
     */
    private JComboBox<String> createDurationCombo() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String[] durations = {
            "30 min",
            "45 min",
            "1 hour",
            "1.5 hours",
            "2 hours"
        };
        for (String duration : durations) {
            model.addElement(duration);
        }
        JComboBox<String> combo = new JComboBox<>(model);
        combo.setFont(new Font("Arial", Font.PLAIN, 11));
        combo.setPreferredSize(new Dimension(130, 25));
        return combo;
    }

    /**
     * Helper: Add vertical gap
     */
    private void addVerticalGap(int pixels) {
        add(Box.createVerticalStrut(pixels));
    }

    /**
     * Get all selected time slots as array
     * @return 2D array: [slot][{date, time, duration}]
     */
    public String[][] getSelectedTimes() {
        String[][] result = new String[3][3];
        for (int i = 0; i < 3; i++) {
            result[i][0] = (String) dateSelectors[i].getSelectedItem();
            result[i][1] = (String) timeSelectors[i].getSelectedItem();
            result[i][2] = (String) durationSelectors[i].getSelectedItem();
        }
        return result;
    }

    /**
     * Get formatted time string for display
     */
    public String getFormattedTimes() {
        StringBuilder sb = new StringBuilder();
        String[][] times = getSelectedTimes();
        
        for (int i = 0; i < 3; i++) {
            sb.append(String.format("Slot %d: %s at %s (%s)\n", 
                i + 1, times[i][0], times[i][1], times[i][2]));
        }
        
        return sb.toString();
    }

    /**
     * RoundBorder helper for buttons
     */
    private static class RoundBorder extends AbstractBorder {
        private int radius;

        public RoundBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(200, 100, 50));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 5, 5);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }
}
