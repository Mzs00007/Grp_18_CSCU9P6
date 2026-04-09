package vcfs.views.recruiter;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.controllers.RecruiterController;
import vcfs.core.UIHelpers;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.CareerFairSystem;
import vcfs.core.UserSession;
import vcfs.models.booking.Offer;
import vcfs.models.users.Recruiter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Publish Offer Panel - Recruiter interface for creating and publishing offer slots.
 *
 * Responsibilities:
 * - Display table of recruiter's existing published offers
 * - Display form for offer creation (title, duration, tags, capacity)
 * - Allow recruiter to set availability blocks
 * - Submit offer to CareerFairSystem
 *
 * Original implementation by: Taha
 * Adapted to skeleton by: Zaid - Now implements PropertyChangeListener for real-time updates
 */
public class PublishOfferPanel extends JPanel implements PropertyChangeListener {



    private RecruiterController controller;
    private JTextField titleField;
    private JTextField durationField;
    private JTextField tagsField;
    private JTextField capacityField;
    private JButton publishButton;
    private javax.swing.table.DefaultTableModel offersTableModel;

    public PublishOfferPanel(RecruiterController controller) {
        this.controller = controller;
        
        // Register as listener for system updates
        CareerFairSystem.getInstance().addPropertyChangeListener(this);
        
        // Main layout with BorderLayout
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================================
        // HEADER
        // ============================================
        JPanel headerPanel = UIHelpers.createHeaderPanel(
            "Publish Interview Offer",
            "Create and publish available interview time slots"
        );
        add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // MAIN CONTENT (Tabbed between view existing & create new)
        // ============================================
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
        tabbedPane.setBackground(Color.WHITE);

        // TAB 1: View Existing Offers
        JPanel viewOffersPanel = createViewOffersTab();
        tabbedPane.addTab("📋 My Published Offers", viewOffersPanel);

        // TAB 2: Create New Offer
        JPanel createOfferPanel = createCreateOfferTab();
        tabbedPane.addTab("➕ Publish New Offer", createOfferPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Create tab showing recruiter's existing published offers
     */
    private JPanel createViewOffersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with mode badge
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("📊 Your Published Offers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Add mode badge
        String modeLabel = CareerFairSystem.getInstance().isInDemoMode() 
            ? "🎬 DEMO MODE - Not Saved" 
            : "📋 LIVE - Saved";
        Color badgeColor = CareerFairSystem.getInstance().isInDemoMode() 
            ? new Color(255, 152, 0)  // Orange for demo
            : new Color(76, 175, 80); // Green for live
        
        JLabel modeBadge = new JLabel(modeLabel);
        modeBadge.setFont(new Font("Arial", Font.BOLD, 11));
        modeBadge.setForeground(Color.WHITE);
        modeBadge.setBackground(badgeColor);
        modeBadge.setOpaque(true);
        modeBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        modeBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(badgeColor, 1, true),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        headerPanel.add(modeBadge, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Table to display offers
        offersTableModel = new javax.swing.table.DefaultTableModel(
            new String[]{"Offer Title", "Duration (min)", "Topics/Tags", "Capacity", "Bookings", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable offersTable = new JTable(offersTableModel);
        offersTable.setFont(new Font("Arial", Font.PLAIN, 11));
        offersTable.setRowHeight(25);
        offersTable.setBackground(Color.WHITE);
        offersTable.setGridColor(new Color(220, 220, 220));
        offersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Add responsive scrolling (vertical AND horizontal)
        JScrollPane scrollPane = new JScrollPane(offersTable,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Refresh button
        JButton refreshBtn = new JButton("🔄 Refresh My Offers");
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 11));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(180, 35));
        
        refreshBtn.addActionListener(e -> refreshOffersTable());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(refreshBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshOffersTable();
        return panel;
    }

    /**
     * CONSISTENCY FIX: Refresh the offers table with current recruiter's offers.
     * P3 (mzs00007) — Enhanced with error resilience and logging.
     * 
     * CRITICAL: This method MUST be called:
     *   1. On initial panel load
     *   2. After publishing a new offer
     *   3. When PropertyChangeEvent fires from CareerFairSystem
     *   4. When user clicks "Refresh" button
     * 
     * Ensures table always shows accurate, up-to-date offer data.
     */
    private void refreshOffersTable() {
        offersTableModel.setRowCount(0); // Clear existing rows
        try {
            Recruiter currentRecruiter = UserSession.getInstance().getCurrentRecruiter();
            if (currentRecruiter == null) {
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] No current recruiter in session");
                offersTableModel.addRow(new Object[]{"ERROR: No recruiter logged in", "-", "-", "-", "-", "-"});
                return;
            }

            // CRITICAL FIX: Get fresh offer list from system (not cached stale data)
            List<Offer> allOffers = CareerFairSystem.getInstance().getAllOffers();
            
            if (allOffers == null || allOffers.isEmpty()) {
                Logger.log(LogLevel.DEBUG, "[PublishOfferPanel] No offers in system yet");
                offersTableModel.addRow(new Object[]{"(No offers published yet)", "-", "-", "-", "-", "-"});
                return;
            }
            
            // Filter offers for current recruiter only
            int offersDisplayed = 0;
            for (Offer offer : allOffers) {
                if (offer == null) continue; // Skip null offers
                if (offer.getPublisher() == null) continue; // Skip offers without publisher
                
                try {
                    // Match current recruiter by email (case-insensitive)
                    String publisherEmail = offer.getPublisher().getEmail();
                    String recruiterEmail = currentRecruiter.getEmail();
                    
                    if (publisherEmail != null && recruiterEmail != null &&
                        publisherEmail.equalsIgnoreCase(recruiterEmail)) {
                        
                        // Build offer row with safe null-checks
                        int bookings = (offer.getReservations() != null) ? offer.getReservations().size() : 0;
                        int capacity = offer.getCapacity() > 0 ? offer.getCapacity() : 1;
                        String status = (bookings >= capacity) ? "FULL" : "OPEN";
                        String title = offer.getTitle() != null ? offer.getTitle() : "(Untitled)";
                        int duration = offer.getDurationMins() > 0 ? offer.getDurationMins() : 0;
                        String tags = (offer.getTopicTags() != null && !offer.getTopicTags().isEmpty()) 
                                        ? offer.getTopicTags() : "-";
                        
                        offersTableModel.addRow(new Object[]{
                            title,
                            duration,
                            tags,
                            capacity,
                            bookings,
                            status
                        });
                        offersDisplayed++;
                    }
                } catch (Exception rowEx) {
                    Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Error processing offer row", rowEx);
                    // Continue to next offer instead of crashing
                }
            }

            // Show placeholder if no offers from this recruiter
            if (offersDisplayed == 0) {
                offersTableModel.addRow(new Object[]{"(No offers published yet)", "-", "-", "-", "-", "-"});
                Logger.log(LogLevel.INFO, "[PublishOfferPanel] Recruiter " + currentRecruiter.getDisplayName() + 
                    " has no published offers");
            } else {
                Logger.log(LogLevel.INFO, "[PublishOfferPanel] Displayed " + offersDisplayed + " offers for " + 
                    currentRecruiter.getDisplayName());
            }
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[PublishOfferPanel] Critical error refreshing offers table", e);
            offersTableModel.setRowCount(0);
            offersTableModel.addRow(new Object[]{"ERROR: " + e.getMessage(), "-", "-", "-", "-", "-"});
        }
    }

    /**
     * Create tab for publishing a new offer
     */
    private JPanel createCreateOfferTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // ============================================
        // FORM PANEL
        // ============================================
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Title field
        JLabel titleLabel = new JLabel("Interview Title *");
        titleLabel.setFont(UIHelpers.FONT_LABEL);
        formPanel.add(titleLabel);
        
        titleField = new JTextField(25);
        titleField.setPreferredSize(new Dimension(400, 30));
        formPanel.add(titleField);
        
        JLabel titleHelpLabel = UIHelpers.createHelpLabel("e.g., 'Software Engineer Interview' or 'Technical Assessment'");
        formPanel.add(titleHelpLabel);
        formPanel.add(Box.createVerticalStrut(15));

        // Duration field
        JLabel durationLabel = new JLabel("Duration (minutes) *");
        durationLabel.setFont(UIHelpers.FONT_LABEL);
        formPanel.add(durationLabel);
        
        durationField = new JTextField("30");
        durationField.setPreferredSize(new Dimension(100, 30));
        formPanel.add(durationField);
        
        JLabel durationHelpLabel = UIHelpers.createHelpLabel("How long is each interview slot? (e.g., 30, 45, 60 minutes)");
        formPanel.add(durationHelpLabel);
        formPanel.add(Box.createVerticalStrut(15));

        // Tags field
        JLabel tagsLabel = new JLabel("Topics/Tags");
        tagsLabel.setFont(UIHelpers.FONT_LABEL);
        formPanel.add(tagsLabel);
        
        tagsField = new JTextField(25);
        tagsField.setPreferredSize(new Dimension(400, 30));
        formPanel.add(tagsField);
        
        
        JLabel tagsHelpLabel = UIHelpers.createHelpLabel("Comma-separated skills (e.g., 'Java, Spring Boot, Database Design')");
        formPanel.add(tagsHelpLabel);
        formPanel.add(Box.createVerticalStrut(15));

        // Capacity field
        JLabel capacityLabel = new JLabel("Capacity");
        capacityLabel.setFont(UIHelpers.FONT_LABEL);
        formPanel.add(capacityLabel);
        
        capacityField = new JTextField("1");
        capacityField.setPreferredSize(new Dimension(100, 30));
        formPanel.add(capacityField);
        
        JLabel capacityHelpLabel = UIHelpers.createHelpLabel("Number of candidates per slot (typically 1 for 1-to-1 interviews)");
        formPanel.add(capacityHelpLabel);
        formPanel.add(Box.createVerticalStrut(25));

        // Publish button
        publishButton = new JButton("📤 Publish Offer");
        UIHelpers.stylePrimaryButton(publishButton);
        publishButton.setPreferredSize(new Dimension(150, 40));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(publishButton);
        
        formPanel.add(buttonPanel);

        // Wrap form in scrollpane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        publishButton.addActionListener(e -> publishOffer());
        
        return panel;
    }

    private void publishOffer() {
        try {
            String title = titleField.getText().trim();
            String durationText = durationField.getText().trim();
            String tagsText = tagsField.getText().trim();
            String capacityText = capacityField.getText().trim();

            // Validate inputs with detailed error messages
            if (title.isEmpty()) {
                UIHelpers.showErrorDialog(this, "Validation Error", "Offer title cannot be empty.");
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Rejected: empty title");
                return;
            }

            if (durationText.isEmpty()) {
                UIHelpers.showErrorDialog(this, "Validation Error", "Duration must be specified.");
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Rejected: empty duration");
                return;
            }

            if (capacityText.isEmpty()) {
                UIHelpers.showErrorDialog(this, "Validation Error", "Capacity must be specified.");
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Rejected: empty capacity");
                return;
            }

            // Parse and validate numeric values
            int duration;
            int capacity;
            
            try {
                duration = Integer.parseInt(durationText);
            } catch (NumberFormatException e) {
                UIHelpers.showErrorDialog(this, "Input Error", "Duration must be a valid number.");
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Invalid duration: " + durationText);
                return;
            }
            
            try {
                capacity = Integer.parseInt(capacityText);
            } catch (NumberFormatException e) {
                UIHelpers.showErrorDialog(this, "Input Error", "Capacity must be a valid number.");
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Invalid capacity: " + capacityText);
                return;
            }

            if (duration <= 0) {
                UIHelpers.showErrorDialog(this, "Validation Error", "Duration must be positive (> 0).");
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Rejected: negative duration " + duration);
                return;
            }

            if (capacity <= 0) {
                UIHelpers.showErrorDialog(this, "Validation Error", "Capacity must be positive (> 0).");
                Logger.log(LogLevel.WARNING, "[PublishOfferPanel] Rejected: negative capacity " + capacity);
                return;
            }

            // Create Offer instance with all properties
            Offer offer = new Offer();
            offer.setTitle(title);
            offer.setDurationMins(duration);
            offer.setCapacity(capacity);
            offer.setTopicTags(tagsText.isEmpty() ? "" : tagsText);

            Logger.log(LogLevel.INFO, "[PublishOfferPanel] Publishing offer: " + title + 
                " (" + duration + "min, cap=" + capacity + ")");

            // Publish via controller (which now correctly notifies system)
            controller.publishOffer(offer);

            // Success message
            UIHelpers.showSuccessDialog(this, "Success", 
                "Offer '" + title + "' published successfully!");

            // CRITICAL FIX: Explicit refresh ensures table updates immediately
            // This works in conjunction with PropertyChangeListener notification
            // If system notification is delayed, manual refresh guarantees UI consistency
            javax.swing.SwingUtilities.invokeLater(() -> {
                Logger.log(LogLevel.DEBUG, "[PublishOfferPanel] Executing deferred table refresh");
                refreshOffersTable();
            });

            // Clear form for next entry
            titleField.setText("");
            durationField.setText("30");
            tagsField.setText("");
            capacityField.setText("1");
            
            Logger.log(LogLevel.INFO, "[PublishOfferPanel] Offer '" + title + "' successfully added to table");
        } catch (NumberFormatException ex) {
            UIHelpers.showErrorDialog(this, "Input Error", 
                "Duration and Capacity must be valid numbers.");
            Logger.log(LogLevel.ERROR, "[PublishOfferPanel] Number format error: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            UIHelpers.showErrorDialog(this, "Error", 
                "Error publishing offer: " + ex.getMessage());
            Logger.log(LogLevel.ERROR, "[PublishOfferPanel] Unexpected error: " + ex.getMessage(), ex);
        }
    }

    /**
     * PropertyChangeListener - Updates table when offers change in system
     * Also handles mode switches between LIVE and DEMO
     * 
     * CRITICAL: All Swing UI updates must happen on the Event Dispatch Thread (EDT)
     * PropertyChangeSupport fires events on its own thread, so we must use
     * SwingUtilities.invokeLater() to marshal calls back to EDT
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if ("offers".equals(prop) || "offer_published".equals(prop)) {
            Logger.log(LogLevel.INFO, "[PublishOfferPanel] Offers updated - refreshing table");
            javax.swing.SwingUtilities.invokeLater(() -> refreshOffersTable());
        } else if ("mode_switched".equals(prop)) {
            // Mode has changed between LIVE and DEMO
            Logger.log(LogLevel.INFO, "[PublishOfferPanel] Mode switched to: " + evt.getNewValue());
            javax.swing.SwingUtilities.invokeLater(() -> {
                refreshOffersTable(); // Refresh data
                // Note: Header will auto-update when createViewOffersTab is called
            });
        }
    }
}

