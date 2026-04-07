package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.controllers.RecruiterController;
import vcfs.models.booking.Offer;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Publish Offer Panel - Recruiter interface for creating and publishing offer slots.
 *
 * Responsibilities:
 * - Display form for offer creation (title, duration, tags, capacity)
 * - Allow recruiter to set availability blocks
 * - Submit offer to CareerFairSystem
 *
 * Original implementation by: Taha
 * Adapted to skeleton by: Zaid
 */
public class PublishOfferPanel extends JPanel {

    private RecruiterController controller;
    private JTextField titleField;
    private JTextField durationField;
    private JTextField tagsField;
    private JTextField capacityField;
    private JButton publishButton;

    public PublishOfferPanel(RecruiterController controller) {
        this.controller = controller;
        // Panel setup
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Publish Offer"));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Offer Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Duration (minutes):"));
        durationField = new JTextField();
        formPanel.add(durationField);

        formPanel.add(new JLabel("Tags (comma separated):"));
        tagsField = new JTextField();
        formPanel.add(tagsField);

        formPanel.add(new JLabel("Capacity (1 for 1-to-1):"));
        capacityField = new JTextField("1");
        formPanel.add(capacityField);

        publishButton = new JButton("Publish Offer");
        formPanel.add(new JLabel("")); // spacer
        formPanel.add(publishButton);

        add(formPanel, BorderLayout.NORTH);

        publishButton.addActionListener(e -> publishOffer());
    }

    private void publishOffer() {
        try {
            String title = titleField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            String tagsText = tagsField.getText().trim();
            List<String> tags = Arrays.stream(tagsText.split(","))
                                      .map(String::trim)
                                      .filter(s -> !s.isEmpty())
                                      .collect(Collectors.toList());
            int capacity = Integer.parseInt(capacityField.getText().trim());

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create Offer instance
            Offer offer = new Offer();
            offer.setTitle(title);
            offer.setDurationMins(duration);
            offer.setCapacity(capacity);
            offer.setTopicTags(String.join(",", tags));

            // Publish via controller
            controller.publishOffer(offer);

            // Clear form
            titleField.setText("");
            durationField.setText("");
            tagsField.setText("");
            capacityField.setText("1");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duration and Capacity must be numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


