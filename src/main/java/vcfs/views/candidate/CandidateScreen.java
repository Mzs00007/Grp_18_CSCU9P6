package vcfs.views.candidate;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import vcfs.controllers.CandidateController;
import vcfs.core.CareerFairSystem;
import vcfs.models.booking.MeetingSession;
import vcfs.models.booking.Request;
import vcfs.models.booking.Lobby;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CandidateScreen extends JFrame implements CandidateView, PropertyChangeListener {

    private CandidateController controller;
    private DefaultTableModel scheduleTableModel;

    public CandidateScreen() {
        setTitle("VCFS - Candidate Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        controller = new CandidateController(this);

        JTabbedPane tabs = new JTabbedPane();

        // TAB 1: Search Offers (Auto-Book)
        JPanel searchOffersPanel = new JPanel(new BorderLayout());
        searchOffersPanel.setBorder(BorderFactory.createTitledBorder("Auto-Book Meeting Request"));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Desired Tags (comma separated):"));
        JTextField tagsField = new JTextField();
        formPanel.add(tagsField);
        
        formPanel.add(new JLabel("Max Appointments:"));
        JTextField maxApptField = new JTextField("3");
        formPanel.add(maxApptField);
        
        JButton autoBookBtn = new JButton("Submit Auto-Book Request");
        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(autoBookBtn);
        
        searchOffersPanel.add(formPanel, BorderLayout.NORTH);
        
        autoBookBtn.addActionListener(e -> {
            try {
                String tags = tagsField.getText().trim();
                int maxAppts = Integer.parseInt(maxApptField.getText().trim());
                
                Request req = new Request();
                req.setDesiredTags(tags);
                req.setMaxAppointments(maxAppts);
                req.setPreferredOrgs("Any"); // Default to avoid exception
                
                controller.submitMeetingRequest(req);
            } catch (NumberFormatException ex) {
                displayError("Max appointments must be a valid number.");
            } catch (IllegalArgumentException ex) {
                displayError(ex.getMessage());
            }
        });

        tabs.add("Search Offers", searchOffersPanel);

        // TAB 2: My Schedule (Confirmed Reservations)
        JPanel mySchedulePanel = new JPanel(new BorderLayout());
        mySchedulePanel.setBorder(BorderFactory.createTitledBorder("Confirmed Reservations"));
        
        String[] columns = {"Session Title", "Start Time", "Duration (mins)"};
        scheduleTableModel = new DefaultTableModel(columns, 0);
        JTable scheduleTable = new JTable(scheduleTableModel);
        JScrollPane tableScroll = new JScrollPane(scheduleTable);
        mySchedulePanel.add(tableScroll, BorderLayout.CENTER);
        
        JButton refreshScheduleBtn = new JButton("Refresh Schedule");
        refreshScheduleBtn.addActionListener(e -> controller.viewMeetingSchedule());
        mySchedulePanel.add(refreshScheduleBtn, BorderLayout.SOUTH);

        tabs.add("My Schedule", mySchedulePanel);

        // TAB 3: Lobby Waiting Room
        JPanel lobbyPanel = new JPanel(new BorderLayout());
        lobbyPanel.setBorder(BorderFactory.createTitledBorder("Lobby Waiting Room"));
        
        JLabel lobbyStatus = new JLabel("Waiting to join a virtual room...");
        lobbyStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lobbyStatus.setFont(new Font("SansSerif", Font.BOLD, 16));
        lobbyPanel.add(lobbyStatus, BorderLayout.CENTER);
        
        JButton checkLobbyBtn = new JButton("Check Available Lobbies");
        checkLobbyBtn.addActionListener(e -> controller.viewAvailableLobbies());
        lobbyPanel.add(checkLobbyBtn, BorderLayout.SOUTH);

        tabs.add("Lobby", lobbyPanel);

        add(tabs);
        
        // ===== REGISTER AS OBSERVER =====
        // CandidateScreen receives property change events from CareerFairSystem
        // This enables real-time updates when offers are published or system state changes
        CareerFairSystem.getInstance().addPropertyChangeListener(this);
        
        setVisible(true);
    }

    @Override
    public void displayError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void displayLobbies(List<Lobby> lobbies) {
        StringBuilder sb = new StringBuilder("Available Lobbies:\n");
        for (Lobby l : lobbies) {
            String title = l.getSession() != null ? l.getSession().getTitle() : "Unknown Session";
            sb.append("- ").append(title).append("\n");
        }
        displayMessage(sb.toString());
    }

    @Override
    public void displayLobbyDetails(Lobby lobby) {
        String title = lobby.getSession() != null ? lobby.getSession().getTitle() : "Unknown Session";
        displayMessage("Lobby Details: " + title);
    }

    @Override
    public void displaySchedule(List<MeetingSession> schedule) {
        scheduleTableModel.setRowCount(0); // Clear table
        if (schedule != null) {
            for (MeetingSession session : schedule) {
                String startTime = (session.getReservation() != null && session.getReservation().getScheduledStart() != null) 
                                    ? session.getReservation().getScheduledStart().toString() : "TBD";
                int duration = (session.getReservation() != null && session.getReservation().getOffer() != null) 
                                ? session.getReservation().getOffer().getDurationMins() : 0;
                scheduleTableModel.addRow(new Object[]{
                    session.getTitle(),
                    startTime,
                    duration
                });
            }
        }
    }

    @Override
    public void displayRequestHistory(List<Request> requests) {
        // Not specifically required by TODOs, but needed for interface
    }

    /**
     * Observer callback - receives property change events from CareerFairSystem
     * Triggered when offers, organizations, or other system state changes
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Candidates primarily care about "offers" changes
        if ("offers".equals(evt.getPropertyName())) {
            // In a full implementation, would refresh available offers table
            // For now, just log the event
            System.out.println("[CandidateScreen] Offers updated: " + evt.getNewValue());
        }
    }
}


