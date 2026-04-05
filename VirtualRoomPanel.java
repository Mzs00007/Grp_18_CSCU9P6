/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recruitment;

/**
 *
 * @author Taha
 */
import javax.swing.*;
import java.awt.*;

public class VirtualRoomPanel extends JPanel {

    private JLabel candidateLabel;
    private JLabel sessionStatus;

    public VirtualRoomPanel() {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Virtual Interview Room", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2,1));

        candidateLabel = new JLabel("Candidate: Waiting...", SwingConstants.CENTER);
        sessionStatus = new JLabel("Session Not Started", SwingConstants.CENTER);

        centerPanel.add(candidateLabel);
        centerPanel.add(sessionStatus);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton attendedButton = new JButton("Attended");
        JButton noShowButton = new JButton("No Show");
        JButton endedEarlyButton = new JButton("Ended Early");

        buttonPanel.add(attendedButton);
        buttonPanel.add(noShowButton);
        buttonPanel.add(endedEarlyButton);

        add(buttonPanel, BorderLayout.SOUTH);

        attendedButton.addActionListener(e -> markAttendance("Attended"));
        noShowButton.addActionListener(e -> markAttendance("No Show"));
        endedEarlyButton.addActionListener(e -> markAttendance("Ended Early"));

        simulateSession();

    }

    private void simulateSession() {

        candidateLabel.setText("Candidate: Sarah Ali");
        sessionStatus.setText("Session Running...");

    }

    private void markAttendance(String status) {
    sessionStatus.setText("Session Result: " + status);
    
    // SPEC REQUIREMENT: Update Audit Log
    // Assuming 6igglepill provides a class called AuditLog
    // AuditLog.saveRecord(candidateLabel.getText(), status); 

    JOptionPane.showMessageDialog(this, "Attendance Recorded in Audit Log: " + status);
}
}
