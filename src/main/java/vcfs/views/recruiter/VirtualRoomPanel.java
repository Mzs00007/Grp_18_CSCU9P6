package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.controllers.RecruiterController;

import javax.swing.*;
import java.awt.*;

/**
 * Virtual Room Panel - Recruiter interface for virtual meeting management.
 *
 * Responsibilities:
 * - Display active virtual meeting rooms
 * - Allow recruiter to join/manage meetings
 * - Show connected candidates
 *
 * Original implementation by: Taha
 * Adapted to skeleton by: Zaid
 */
public class VirtualRoomPanel extends JPanel {

    private RecruiterController controller;
    private JPanel cards;
    private CardLayout cardLayout;

    public VirtualRoomPanel(RecruiterController controller) {
        this.controller = controller;
        // Panel setup
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Virtual Room"));

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Card 1: Waiting / Selection View
        JPanel waitingView = new JPanel(new BorderLayout());
        JLabel placeholder = new JLabel("Select a session from the Schedule to join the Virtual Room.");
        placeholder.setHorizontalAlignment(JLabel.CENTER);
        waitingView.add(placeholder, BorderLayout.CENTER);
        
        JButton joinRoomBtn = new JButton("Join Selected Session (Demo)");
        joinRoomBtn.addActionListener(e -> cardLayout.show(cards, "ACTIVE_ROOM"));
        waitingView.add(joinRoomBtn, BorderLayout.SOUTH);

        // Card 2: Active Room View
        JPanel activeRoomView = new JPanel(new BorderLayout());
        JLabel roomLabel = new JLabel("Virtual Room: Active Session");
        roomLabel.setHorizontalAlignment(JLabel.CENTER);
        activeRoomView.add(roomLabel, BorderLayout.CENTER);

        JPanel outcomePanel = new JPanel();
        outcomePanel.setBorder(BorderFactory.createTitledBorder("Post-Session Attendance Outcome"));
        JButton attendedBtn = new JButton("Mark ATTENDED");
        JButton noShowBtn = new JButton("Mark NO_SHOW");
        JButton endSessionBtn = new JButton("End Session & Return");

        attendedBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Attendance marked: ATTENDED");
            this.controller.updateOfferStatus("demo-session", "ATTENDED");
        });
        noShowBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Attendance marked: NO_SHOW");
            this.controller.updateOfferStatus("demo-session", "NO_SHOW");
        });
        endSessionBtn.addActionListener(e -> cardLayout.show(cards, "WAITING"));

        outcomePanel.add(attendedBtn);
        outcomePanel.add(noShowBtn);
        outcomePanel.add(endSessionBtn);

        activeRoomView.add(outcomePanel, BorderLayout.SOUTH);

        cards.add(waitingView, "WAITING");
        cards.add(activeRoomView, "ACTIVE_ROOM");

        add(cards, BorderLayout.CENTER);
    }
}


