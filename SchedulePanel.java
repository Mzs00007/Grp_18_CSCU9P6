package Recruitment;

import javax.swing.*;
import java.awt.*;

public class SchedulePanel extends JPanel {
    private DefaultListModel<Booking> listModel; // Use the Booking class!
    private JList<Booking> bookingList;

    public SchedulePanel() {
        setLayout(new BorderLayout(10, 10));
        listModel = new DefaultListModel<>();
        bookingList = new JList<>(listModel);

        // Replace hardcoded strings with actual Booking objects
        listModel.addElement(new Booking("Jack", "09:00", "Graduate Chat"));
        
        add(new JScrollPane(bookingList), BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel Reservation");
        cancelButton.addActionListener(e -> {
            int selectedIndex = bookingList.getSelectedIndex();
            if (selectedIndex != -1) {
                Booking selected = listModel.get(selectedIndex);
                listModel.remove(selectedIndex);
                
                // SPEC REQUIREMENT: Trigger notification
                sendNotificationToCandidate(selected);
                
                JOptionPane.showMessageDialog(this, "Cancelled and Candidate Notified.");
            }
        });
        add(cancelButton, BorderLayout.SOUTH);
    }

    private void sendNotificationToCandidate(Booking b) {
        // This is where you connect to the notification system later
        System.out.println("Notification sent to candidate regarding: " + b.toString());
    }
}