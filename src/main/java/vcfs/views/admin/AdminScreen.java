package vcfs.views.admin;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import vcfs.controllers.AdminScreenController;

/**
 * Administrator GUI Screen (Phase 4 - UI Integration).
 *
 * Responsibilities:
 * - Allow admin to create Organizations and Booths
 * - Allow admin to set the system timeline
 * - Display Audit Log via PropertyChangeListener
 *
 * NOTE: All business logic is in AdminScreenController. This class ONLY handles UI.
 *
 * Original implementation by: YAMI (leiyam)
 * Adapted to skeleton by: Zaid
 */
public class AdminScreen extends JFrame implements PropertyChangeListener {

    // ===== CONTROLLER =====
    private AdminScreenController controller;

    // ===== UI COMPONENTS =====
    private JTextField orgField;
    private JTextField boothField;
    private JTextField recruiterField;
    private JComboBox<String> orgDropdown;
    private JComboBox<String> boothDropdown;

    // Timeline fields
    private JTextField openField;
    private JTextField closeField;
    private JTextField startField;
    private JTextField endField;

    // Audit log display
    private JTextArea auditArea;

    /**
     * Constructor - build the UI and wire controllers
     * @param controller The AdminScreenController that handles all business logic
     */
    public AdminScreen(AdminScreenController controller) {
        this.controller = controller;

        // ===== FRAME SETUP =====
        setTitle("VCFS Administrator Screen");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // SECTION 1: CREATE ORGANIZATION
        JPanel orgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        orgPanel.setBorder(BorderFactory.createTitledBorder("1. Create Organization"));

        orgField = new JTextField(15);
        JButton createOrgBtn = new JButton("Create Organization");

        orgPanel.add(new JLabel("Organization Name:"));
        orgPanel.add(orgField);
        orgPanel.add(createOrgBtn);
        mainPanel.add(orgPanel);

        // SECTION 2: CREATE BOOTH
        JPanel boothPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boothPanel.setBorder(BorderFactory.createTitledBorder("2. Create Booth"));

        boothField = new JTextField(15);
        orgDropdown = new JComboBox<>();
        JButton createBoothBtn = new JButton("Create Booth");

        boothPanel.add(new JLabel("Booth Name:"));
        boothPanel.add(boothField);
        boothPanel.add(new JLabel("Organization:"));
        boothPanel.add(orgDropdown);
        boothPanel.add(createBoothBtn);
        mainPanel.add(boothPanel);

        // SECTION 3: ASSIGN RECRUITER
        JPanel recruiterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recruiterPanel.setBorder(BorderFactory.createTitledBorder("3. Assign Recruiter to Booth"));

        recruiterField = new JTextField(15);
        boothDropdown = new JComboBox<>();
        JButton assignRecruiterBtn = new JButton("Assign Recruiter");

        recruiterPanel.add(new JLabel("Recruiter Name:"));
        recruiterPanel.add(recruiterField);
        recruiterPanel.add(new JLabel("Booth:"));
        recruiterPanel.add(boothDropdown);
        recruiterPanel.add(assignRecruiterBtn);
        mainPanel.add(recruiterPanel);

        // SECTION 4: SET TIMELINE & RESET
        JPanel timePanel = new JPanel(new GridLayout(6, 2, 10, 10));
        timePanel.setBorder(BorderFactory.createTitledBorder("4. Configure Fair Timeline"));

        openField = new JTextField("2026-04-08T09:00");
        closeField = new JTextField("2026-04-08T12:00");
        startField = new JTextField("2026-04-08T13:00");
        endField = new JTextField("2026-04-08T17:00");
        
        JButton setTimelineBtn = new JButton("Set Timeline");
        JButton resetSystemBtn = new JButton("Reset System Data");
        resetSystemBtn.setForeground(Color.RED);

        timePanel.add(new JLabel("Bookings Open (YYYY-MM-DDTHH:MM):"));
        timePanel.add(openField);
        timePanel.add(new JLabel("Bookings Close:"));
        timePanel.add(closeField);
        timePanel.add(new JLabel("Fair Start:"));
        timePanel.add(startField);
        timePanel.add(new JLabel("Fair End:"));
        timePanel.add(endField);
        timePanel.add(resetSystemBtn);
        timePanel.add(setTimelineBtn);
        mainPanel.add(timePanel);

        // SECTION 5: AUDIT LOG (Observer Output)
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("System Audit Log"));

        auditArea = new JTextArea(12, 60);
        auditArea.setEditable(false);
        auditArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(auditArea);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== LAYOUT =====
        add(mainPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        // ===== BUTTON HANDLERS =====

        createOrgBtn.addActionListener(e -> {
            String name = orgField.getText().trim();
            if (name.isEmpty()) {
                auditArea.append("[ERROR] Organization name cannot be empty.\n");
                return;
            }
            try {
                this.controller.createOrganization(name);
                orgDropdown.addItem(name);
                orgField.setText("");
                auditArea.append("[✓] Organization created: " + name + "\n");
            } catch (Exception ex) {
                auditArea.append("[ERROR] " + ex.getMessage() + "\n");
            }
        });

        createBoothBtn.addActionListener(e -> {
            String boothName = boothField.getText().trim();
            Object selectedOrg = orgDropdown.getSelectedItem();
            if (selectedOrg == null || boothName.isEmpty()) {
                auditArea.append("[ERROR] Booth name and organization required.\n");
                return;
            }
            try {
                this.controller.createBooth(boothName, selectedOrg.toString());
                boothDropdown.addItem(boothName);
                boothField.setText("");
                auditArea.append("[✓] Booth created: " + boothName + "\n");
            } catch (Exception ex) {
                auditArea.append("[ERROR] " + ex.getMessage() + "\n");
            }
        });

        assignRecruiterBtn.addActionListener(e -> {
            String recruiterName = recruiterField.getText().trim();
            Object selectedBooth = boothDropdown.getSelectedItem();
            if (selectedBooth == null || recruiterName.isEmpty()) {
                auditArea.append("[ERROR] Recruiter name and booth required.\n");
                return;
            }
            try {
                this.controller.assignRecruiter(recruiterName, selectedBooth.toString());
                recruiterField.setText("");
                auditArea.append("[✓] Recruiter assigned: " + recruiterName + "\n");
            } catch (Exception ex) {
                auditArea.append("[ERROR] " + ex.getMessage() + "\n");
            }
        });

        setTimelineBtn.addActionListener(e -> {
            try {
                this.controller.setTimeline(
                    openField.getText(),
                    closeField.getText(),
                    startField.getText(),
                    endField.getText()
                );
                auditArea.append("[✓] Timeline configured successfully.\n");
            } catch (Exception ex) {
                auditArea.append("[ERROR] Timeline error: " + ex.getMessage() + "\n");
            }
        });

        resetSystemBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to reset all fair data? This cannot be undone.", 
                "Confirm Reset", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    this.controller.resetFair();
                    orgDropdown.removeAllItems();
                    boothDropdown.removeAllItems();
                    auditArea.setText("");
                    auditArea.append("[✓] System Data Reset Successfully.\n");
                } catch (Exception ex) {
                    auditArea.append("[ERROR] Reset failed: " + ex.getMessage() + "\n");
                }
            }
        });

        setVisible(true);
    }

    /**
     * Observer callback - receives system notifications
     * Called when CareerFairSystem broadcasts PropertyChange events
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("auditLog".equals(evt.getPropertyName()) || "time".equals(evt.getPropertyName())) {
            auditArea.append("[SYSTEM EVENT] " + evt.getPropertyName() + ": " + evt.getNewValue() + "\n");
            // Auto-scroll to bottom
            auditArea.setCaretPosition(auditArea.getDocument().getLength());
        }
    }
}


