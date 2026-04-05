package Recruitment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PublishOfferPanel extends JPanel {

    private JTextField nameField;
    private JTextField durationField;
    private JTextField capacityField;
    private JTextField startField;
    private JTextField endField;

    private DefaultListModel<Offer> offerListModel;

    public PublishOfferPanel() {

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel nameLabel = new JLabel("Offer Name:");
        nameField = new JTextField();

        JLabel durationLabel = new JLabel("Duration (minutes):");
        durationField = new JTextField();

        JLabel capacityLabel = new JLabel("Candidate Capacity:");
        capacityField = new JTextField();

        JLabel startLabel = new JLabel("Start Time:");
        startField = new JTextField();

        JLabel endLabel = new JLabel("End Time:");
        endField = new JTextField();

        JButton publishButton = new JButton("Publish Offer");

        formPanel.add(nameLabel);
        formPanel.add(nameField);

        formPanel.add(durationLabel);
        formPanel.add(durationField);

        formPanel.add(capacityLabel);
        formPanel.add(capacityField);

        formPanel.add(startLabel);
        formPanel.add(startField);

        formPanel.add(endLabel);
        formPanel.add(endField);

        formPanel.add(new JLabel());
        formPanel.add(publishButton);

        add(formPanel, BorderLayout.NORTH);

        offerListModel = new DefaultListModel<>();
        JList<Offer> offerList = new JList<>(offerListModel);

        add(new JScrollPane(offerList), BorderLayout.CENTER);

        publishButton.addActionListener(e -> publishOffer());

    }

    private void publishOffer() {

        try {

            String name = nameField.getText();
            int duration = Integer.parseInt(durationField.getText());
            int capacity = Integer.parseInt(capacityField.getText());
            String start = startField.getText();
            String end = endField.getText();

            Offer offer = new Offer(name, duration, capacity, start, end);
            offerListModel.addElement(offer);

            JOptionPane.showMessageDialog(this, "Offer Published Successfully");

            nameField.setText("");
            durationField.setText("");
            capacityField.setText("");
            startField.setText("");
            endField.setText("");

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Please enter valid data");

        }

    }
}
