/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.admin;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author leiyam
 * 
 * AdministratorScreen
 * 
 * This is the MAIN GUI used by the admin during the Preparing state.
 * 
 * Responsibilities:
 * - Allow admin to create Organizations, Booths, and Recruiters
 * - Allow admin to set the system timeline
 * - Display Audit Log (listening to system events via Observer pattern)
 * 
 * NOTE:
 * - This class does NOT contain business logic
 * - All logic is handled by AdminController
 */
public class AdministratorScreen extends JFrame implements Observer {

    // ===== CONTROLLER =====
    // Acts as the bridge between UI and Model
    private AdminController controller;

    // ===== UI COMPONENTS =====

    // Input fields
    private JTextField orgField;
    private JTextField boothField;
    private JTextField recruiterField;

    // Dropdowns (populated dynamically)
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
     * Constructor
     * 
     * @param controller The AdminController handling logic
     */
    public AdministratorScreen(AdminController controller) {
        this.controller = controller;

        // ===== FRAME SETTINGS =====
        setTitle("Administrator Screen");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== MAIN PANEL (Top Sections) =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // SECTION 1: CREATE ORGANIZATION
        JPanel orgPanel = new JPanel(new FlowLayout());
        orgPanel.setBorder(BorderFactory.createTitledBorder("Create Organization"));

        orgField = new JTextField(10);
        JButton createOrgBtn = new JButton("Create");

        orgPanel.add(new JLabel("Org Name:"));
        orgPanel.add(orgField);
        orgPanel.add(createOrgBtn);

        mainPanel.add(orgPanel);

        // SECTION 2: CREATE BOOTH
        JPanel boothPanel = new JPanel(new FlowLayout());
        boothPanel.setBorder(BorderFactory.createTitledBorder("Create Booth"));

        boothField = new JTextField(10);
        orgDropdown = new JComboBox<>(); // populated after org creation
        JButton createBoothBtn = new JButton("Create Booth");

        boothPanel.add(new JLabel("Booth Name:"));
        boothPanel.add(boothField);
        boothPanel.add(new JLabel("Organization:"));
        boothPanel.add(orgDropdown);
        boothPanel.add(createBoothBtn);

        mainPanel.add(boothPanel);

        // SECTION 3: ASSIGN RECRUITER
        JPanel recruiterPanel = new JPanel(new FlowLayout());
        recruiterPanel.setBorder(BorderFactory.createTitledBorder("Assign Recruiter"));

        recruiterField = new JTextField(10);
        boothDropdown = new JComboBox<>(); // populated after booth creation
        JButton assignRecruiterBtn = new JButton("Assign");

        recruiterPanel.add(new JLabel("Recruiter Name:"));
        recruiterPanel.add(recruiterField);
        recruiterPanel.add(new JLabel("Booth:"));
        recruiterPanel.add(boothDropdown);
        recruiterPanel.add(assignRecruiterBtn);

        mainPanel.add(recruiterPanel);

        // SECTION 4: SET TIMELINE
        JPanel timePanel = new JPanel(new GridLayout(5, 2));
        timePanel.setBorder(BorderFactory.createTitledBorder("Set Timeline"));

        openField = new JTextField();
        closeField = new JTextField();
        startField = new JTextField();
        endField = new JTextField();

        JButton setTimelineBtn = new JButton("Set Timeline");

        timePanel.add(new JLabel("Bookings Open (yyyy-MM-ddTHH:mm):"));
        timePanel.add(openField);

        timePanel.add(new JLabel("Bookings Close:"));
        timePanel.add(closeField);

        timePanel.add(new JLabel("Start Time:"));
        timePanel.add(startField);

        timePanel.add(new JLabel("End Time:"));
        timePanel.add(endField);

        timePanel.add(new JLabel()); // empty space
        timePanel.add(setTimelineBtn);

        mainPanel.add(timePanel);

        // SECTION 5: AUDIT LOG (Observer Output)
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Audit Log"));

        auditArea = new JTextArea(10, 50);
        auditArea.setEditable(false); // user cannot edit log

        JScrollPane scrollPane = new JScrollPane(auditArea);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== ADD PANELS TO FRAME =====
        add(mainPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        // BUTTON ACTIONS (UI → Controller)

        /**
         * Create Organization
         * - Sends input to controller
         * - Updates dropdown for Booth creation
         */
        createOrgBtn.addActionListener(e -> {
            String name = orgField.getText();

            controller.createOrganization(name);

            // Update UI
            orgDropdown.addItem(name);

            auditArea.append("Created Organization: " + name + "\n");
        });

        /**
         * Create Booth
         * - Requires selected Organization
         */
        createBoothBtn.addActionListener(e -> {
            String boothName = boothField.getText();
            String orgName = (String) orgDropdown.getSelectedItem();

            controller.createBooth(boothName, orgName);

            // Update UI
            boothDropdown.addItem(boothName);

            auditArea.append("Created Booth: " + boothName + " under " + orgName + "\n");
        });

        /**
         * Assign Recruiter
         */
        assignRecruiterBtn.addActionListener(e -> {
            String recruiterName = recruiterField.getText();
            String boothName = (String) boothDropdown.getSelectedItem();

            controller.assignRecruiter(recruiterName, boothName);

            auditArea.append("Assigned Recruiter: " + recruiterName + " to " + boothName + "\n");
        });

        /**
         * Set Timeline
         * 
         * NOTE:
         * - Format must match LocalDateTime.parse()
         * - Example: 2026-04-05T14:00
         */
        setTimelineBtn.addActionListener(e -> {
            controller.setTimeline(
                openField.getText(),
                closeField.getText(),
                startField.getText(),
                endField.getText()
            );

            auditArea.append("Timeline configured.\n");
        });
    }

    // OBSERVER METHOD (Audit Log Listener)

    /**
     * This method is called by the Model whenever an event occurs.
     * 
     * Example events:
     * - Candidate books a slot
     * - Recruiter cancels meeting
     * 
     * This updates the Audit Log in real-time.
     */
    @Override
    public void update(String message) {
        auditArea.append(message + "\n");
    }
}