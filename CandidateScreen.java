package vcfs.views.candidate;

import vcfs.controllers.CandidateController;
import vcfs.core.CareerFairSystem;
import vcfs.core.LocalDateTime;
import vcfs.core.SystemTimer;
import vcfs.models.booking.Offer;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;
import vcfs.models.enums.FairPhase;
import vcfs.models.enums.ReservationState;
import vcfs.models.structure.Booth;
import vcfs.models.structure.Organization;
import vcfs.models.users.Candidate;
import vcfs.models.users.Recruiter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * CandidateScreen — MVC View for all Candidate interactions.
 *
 * Simplified to match the plain Swing style used by LoginFrame / AdminScreen.
 * All actions route through CandidateController.
 * Lobby gatekeeper cross-references SystemTimer (read-only).
 *
 * Assigned to: MJAMishkat
 * Tickets: VCFS-013 (Registration), VCFS-014 (Manual/Auto Booking), VCFS-016
 * (Lobby)
 */
public class CandidateScreen extends JFrame implements PropertyChangeListener {

    // =========================================================
    // MVC WIRING
    // =========================================================
    private final CareerFairSystem system;

    /**
     * Controller is created AFTER registration (not at frame-construction time).
     * This lets us pass the candidate's email into its constructor, scoping
     * this instance to exactly one user — the session-management strategy
     * required by the brief when no persistent database is available.
     */
    private CandidateController controller;

    // =========================================================
    // SESSION STATE
    // =========================================================
    private Candidate loggedInCandidate = null;

    // =========================================================
    // CARD LAYOUT — switches between screens
    // =========================================================
    private CardLayout cardLayout;
    private JPanel cardContainer;

    private static final String CARD_REGISTER = "REGISTER";
    private static final String CARD_MAIN = "MAIN";
    private static final String CARD_LOBBY = "LOBBY";

    // =========================================================
    // REGISTRATION SCREEN COMPONENTS
    // =========================================================
    private JTextField regNameField;
    private JTextField regEmailField;
    private JTextField regCvField;
    private JTextField regTagsField;
    private JLabel regStatusLabel;

    // =========================================================
    // MAIN SCREEN COMPONENTS
    // =========================================================
    private JLabel welcomeLabel;
    private JLabel phaseLabel;
    private JLabel clockLabel;
    private JTabbedPane mainTabs;

    // Browse Tab
    private DefaultListModel<String> boothListModel;
    private JList<String> boothList;
    private DefaultListModel<String> offersListModel;
    private JList<String> offersList;
    private JLabel browseStatusLabel;

    // Manual Booking Tab
    private DefaultListModel<String> manualOfferListModel;
    private JList<String> manualOfferList;
    private DefaultListModel<String> manualSlotListModel;
    private JList<String> manualSlotList;
    private JLabel manualStatusLabel;

    // Auto-Book Tab
    private JTextField autoTagsField;
    private JTextField autoOrgsField;
    private JSpinner autoMaxSpinner;
    private JTextArea autoProposalArea;
    private JLabel autoStatusLabel;
    private List<Reservation> lastProposals = new ArrayList<>();

    // Schedule Tab
    private JTextArea scheduleArea;
    private JLabel scheduleStatusLabel;
    private List<Reservation> cachedSchedule = new ArrayList<>();

    // =========================================================
    // LOBBY SCREEN COMPONENTS
    // =========================================================
    private JLabel lobbyTitleLabel;
    private JLabel lobbyCountdownLabel;
    private JLabel lobbyStatusLabel;
    private javax.swing.Timer lobbySwingTimer;
    private Reservation pendingLobbyReservation = null;

    // =========================================================
    // INTERNAL DATA CACHE
    // =========================================================
    private List<Offer> cachedOffers = new ArrayList<>();
    private List<Booth> cachedBooths = new ArrayList<>();

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public CandidateScreen(CareerFairSystem system) {
        this.system = system;
        this.controller = null; // Created after registration — see onLoginSuccess()

        // Register as SystemTimer Observer for header clock + Lobby Gatekeeper
        SystemTimer.getInstance().addPropertyChangeListener(this);

        buildFrame();
        showCard(CARD_REGISTER);
        setVisible(true);
    }

    // =========================================================
    // FRAME SETUP
    // =========================================================

    private void buildFrame() {
        setTitle("VCFS - Candidate Portal");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header bar — plain label row
        add(buildHeaderBar(), BorderLayout.NORTH);

        // Card container
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.add(buildRegistrationPanel(), CARD_REGISTER);
        cardContainer.add(buildMainPanel(), CARD_MAIN);
        cardContainer.add(buildLobbyPanel(), CARD_LOBBY);
        add(cardContainer, BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER BAR
    // =========================================================

    private JPanel buildHeaderBar() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        header.setBorder(BorderFactory.createEtchedBorder());

        header.add(new JLabel("VCFS - Candidate Portal"));
        header.add(new JLabel(" | "));

        clockLabel = new JLabel("Clock: --:--");
        phaseLabel = new JLabel("Phase: DORMANT");
        welcomeLabel = new JLabel("Not logged in");

        header.add(clockLabel);
        header.add(new JLabel("|"));
        header.add(phaseLabel);
        header.add(new JLabel("|"));
        header.add(welcomeLabel);
        return header;
    }

    // =========================================================
    // REGISTRATION PANEL (VCFS-013)
    // =========================================================

    private JPanel buildRegistrationPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0 — title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        outer.add(new JLabel("Candidate Registration", JLabel.CENTER), gbc);
        gbc.gridwidth = 1;

        // Row 1 — Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        outer.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        regNameField = new JTextField(18);
        outer.add(regNameField, gbc);

        // Row 2 — Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        outer.add(new JLabel("Email Address:"), gbc);
        gbc.gridx = 1;
        regEmailField = new JTextField(18);
        outer.add(regEmailField, gbc);

        // Row 3 — CV Summary
        gbc.gridx = 0;
        gbc.gridy = 3;
        outer.add(new JLabel("CV Summary:"), gbc);
        gbc.gridx = 1;
        regCvField = new JTextField(18);
        outer.add(regCvField, gbc);

        // Row 4 — Tags
        gbc.gridx = 0;
        gbc.gridy = 4;
        outer.add(new JLabel("Interest Tags:"), gbc);
        gbc.gridx = 1;
        regTagsField = new JTextField(18);
        regTagsField.setToolTipText("e.g. Java, AI, Cloud (comma-separated)");
        outer.add(regTagsField, gbc);

        // Row 5 — Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton registerBtn = new JButton("Register");
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> handleRegister());
        outer.add(registerBtn, gbc);

        // Row 6 — Status
        gbc.gridy = 6;
        regStatusLabel = new JLabel(" ");
        outer.add(regStatusLabel, gbc);

        return outer;
    }

    // =========================================================
    // MAIN PANEL with tabs
    // =========================================================

    private JPanel buildMainPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());

        mainTabs = new JTabbedPane();
        mainTabs.addTab("Booths & Offers", buildBrowseTab());
        mainTabs.addTab("Manual Booking", buildManualBookTab());
        mainTabs.addTab("Auto-Book", buildAutoBookTab());
        mainTabs.addTab("My Schedule", buildScheduleTab());

        wrapper.add(mainTabs, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Browse Tab ─────────────────────────────────────────

    private JPanel buildBrowseTab() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Left: booth list
        JPanel leftPanel = new JPanel(new BorderLayout(0, 4));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Participating Booths"));
        leftPanel.setPreferredSize(new Dimension(200, 0));

        boothListModel = new DefaultListModel<>();
        boothList = new JList<>(boothListModel);
        boothList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boothList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                refreshOffersForSelectedBooth();
        });

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshBoothList());

        leftPanel.add(new JScrollPane(boothList), BorderLayout.CENTER);
        leftPanel.add(refreshBtn, BorderLayout.SOUTH);

        // Right: offers list
        JPanel rightPanel = new JPanel(new BorderLayout(0, 4));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Offers in Selected Booth"));

        offersListModel = new DefaultListModel<>();
        offersList = new JList<>(offersListModel);

        browseStatusLabel = new JLabel("Select a booth to see offers.");

        rightPanel.add(new JScrollPane(offersList), BorderLayout.CENTER);
        rightPanel.add(browseStatusLabel, BorderLayout.SOUTH);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);
        return panel;
    }

    // ── Manual Booking Tab ─────────────────────────────────

    private JPanel buildManualBookTab() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Left: offer list
        JPanel leftPanel = new JPanel(new BorderLayout(0, 4));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Offers"));
        leftPanel.setPreferredSize(new Dimension(260, 0));

        manualOfferListModel = new DefaultListModel<>();
        manualOfferList = new JList<>(manualOfferListModel);
        manualOfferList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        manualOfferList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                refreshSlotsForOffer();
        });

        JButton loadBtn = new JButton("Load Offers");
        loadBtn.addActionListener(e -> loadManualOffers());

        leftPanel.add(new JScrollPane(manualOfferList), BorderLayout.CENTER);
        leftPanel.add(loadBtn, BorderLayout.SOUTH);

        // Right: slot list + reserve button
        JPanel rightPanel = new JPanel(new BorderLayout(0, 6));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Select a Time Slot"));

        manualSlotListModel = new DefaultListModel<>();
        manualSlotList = new JList<>(manualSlotListModel);
        manualSlotList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton reserveBtn = new JButton("Reserve");
        reserveBtn.addActionListener(e -> handleManualBook());
        manualStatusLabel = new JLabel(" ");
        bottomRow.add(reserveBtn);
        bottomRow.add(manualStatusLabel);

        rightPanel.add(new JScrollPane(manualSlotList), BorderLayout.CENTER);
        rightPanel.add(bottomRow, BorderLayout.SOUTH);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);
        return panel;
    }

    // ── Auto-Book Tab ──────────────────────────────────────

    private JPanel buildAutoBookTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Form section
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Auto-Book Request"));

        autoTagsField = new JTextField();
        autoTagsField.setToolTipText("e.g. Java, Machine Learning");
        autoOrgsField = new JTextField();
        autoOrgsField.setToolTipText("e.g. Google, Amazon (or leave blank)");
        autoMaxSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));

        formPanel.add(new JLabel("Desired Tags:"));
        formPanel.add(autoTagsField);
        formPanel.add(new JLabel("Preferred Organisations:"));
        formPanel.add(autoOrgsField);
        formPanel.add(new JLabel("Max Appointments:"));
        formPanel.add(autoMaxSpinner);

        JButton autoBtn = new JButton("Find Best Matches");
        autoBtn.addActionListener(e -> handleAutoBook());
        formPanel.add(new JLabel());
        formPanel.add(autoBtn);

        // Proposal section
        JPanel proposalPanel = new JPanel(new BorderLayout(0, 4));
        proposalPanel.setBorder(BorderFactory.createTitledBorder("Proposed Schedule"));

        autoProposalArea = new JTextArea(8, 0);
        autoProposalArea.setEditable(false);
        autoProposalArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel proposalBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton acceptAllBtn = new JButton("Accept All");
        JButton rejectAllBtn = new JButton("Reject All");
        autoStatusLabel = new JLabel(" ");
        acceptAllBtn.addActionListener(e -> handleAcceptAll());
        rejectAllBtn.addActionListener(e -> handleRejectAll());
        proposalBtns.add(acceptAllBtn);
        proposalBtns.add(rejectAllBtn);
        proposalBtns.add(autoStatusLabel);

        proposalPanel.add(new JScrollPane(autoProposalArea), BorderLayout.CENTER);
        proposalPanel.add(proposalBtns, BorderLayout.SOUTH);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(proposalPanel, BorderLayout.CENTER);
        return panel;
    }

    // ── Schedule Tab ───────────────────────────────────────

    private JPanel buildScheduleTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("My Confirmed Schedule"));

        scheduleArea = new JTextArea();
        scheduleArea.setEditable(false);
        scheduleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshScheduleTab());
        JButton joinBtn = new JButton("Join Selected");
        joinBtn.addActionListener(e -> handleJoinFromSchedule());
        scheduleStatusLabel = new JLabel("Select a line then click Join.");
        footer.add(refreshBtn);
        footer.add(joinBtn);
        footer.add(scheduleStatusLabel);

        panel.add(new JScrollPane(scheduleArea), BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    // =========================================================
    // LOBBY PANEL (VCFS-016)
    // =========================================================

    private JPanel buildLobbyPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createTitledBorder("Waiting in Lobby"));

        lobbyTitleLabel = new JLabel("Waiting for your session...");
        lobbyTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lobbyStatusLabel = new JLabel("Your session has not started yet.");
        lobbyStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countdownHdr = new JLabel("Session starts in:");
        countdownHdr.setAlignmentX(Component.CENTER_ALIGNMENT);

        lobbyCountdownLabel = new JLabel("--:--");
        lobbyCountdownLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        lobbyCountdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton leaveBtn = new JButton("Leave Lobby");
        leaveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaveBtn.addActionListener(e -> leaveLobby());

        card.add(Box.createVerticalStrut(12));
        card.add(lobbyTitleLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(lobbyStatusLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(countdownHdr);
        card.add(Box.createVerticalStrut(4));
        card.add(lobbyCountdownLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(leaveBtn);
        card.add(Box.createVerticalStrut(12));

        outer.add(card, gbc);
        return outer;
    }

    // =========================================================
    // REGISTRATION HANDLER (VCFS-013)
    // =========================================================

    private void handleRegister() {
        String name = regNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String cv = regCvField.getText().trim();
        String tags = regTagsField.getText().trim();

        if (name.isEmpty()) {
            regStatusLabel.setText("Please enter your full name.");
            return;
        }
        if (email.isEmpty() || !email.contains("@")) {
            regStatusLabel.setText("Please enter a valid email address.");
            return;
        }

        try {
            Candidate candidate = system.registerCandidate(name, email, cv, tags);
            loggedInCandidate = candidate;
            onLoginSuccess();
        } catch (IllegalStateException duplicateEmail) {
            regStatusLabel.setText("Email already registered.");
        } catch (Exception ex) {
            regStatusLabel.setText("Registration failed: " + ex.getMessage());
        }
    }

    private void onLoginSuccess() {
        // Instantiate the controller NOW that we have the candidate's email.
        // Each CandidateScreen instance gets its own controller scoped to one email
        // address,
        // so six concurrent windows never share or cross-contaminate each other's
        // state.
        this.controller = new CandidateController(system, loggedInCandidate.email);

        welcomeLabel.setText("Logged in: " + loggedInCandidate.displayName);
        refreshBoothList();
        loadManualOffers();
        refreshScheduleTab();
        showCard(CARD_MAIN);
    }

    // =========================================================
    // BROWSE — refresh booth & offer lists
    // =========================================================

    private void refreshBoothList() {
        SwingUtilities.invokeLater(() -> {
            boothListModel.clear();
            cachedBooths.clear();

            List<Organization> organizations = system.getOrganizations();
            if (organizations.isEmpty()) {
                browseStatusLabel.setText("No booths available yet.");
                return;
            }

            for (Organization org : organizations) {
                if (org.booths == null)
                    continue;
                for (Booth booth : org.booths) {
                    cachedBooths.add(booth);
                    boothListModel.addElement(org.name + " - " + booth.title);
                }
            }
            browseStatusLabel.setText(cachedBooths.size() + " booth(s) found.");
        });
    }

    private void refreshOffersForSelectedBooth() {
        int idx = boothList.getSelectedIndex();
        if (idx < 0 || idx >= cachedBooths.size())
            return;

        Booth booth = cachedBooths.get(idx);
        offersListModel.clear();
        cachedOffers.clear();

        if (booth.recruiters == null)
            return;

        for (Recruiter recruiter : booth.recruiters) {
            if (recruiter.offers == null)
                continue;
            for (Offer offer : recruiter.offers) {
                cachedOffers.add(offer);
                offersListModel.addElement(String.format("[%s] %s - %s (%d min)",
                        offer.startTime != null ? offer.startTime.toString() : "TBD",
                        offer.title,
                        recruiter.displayName,
                        offer.durationMins));
            }
        }
        browseStatusLabel.setText(cachedOffers.size() + " offer(s) in this booth.");
    }

    // =========================================================
    // MANUAL BOOKING (VCFS-014)
    // =========================================================

    private void loadManualOffers() {
        SwingUtilities.invokeLater(() -> {
            manualOfferListModel.clear();
            cachedOffers.clear();

            List<Organization> organizations = system.getOrganizations();
            for (Organization org : organizations) {
                if (org.booths == null)
                    continue;
                for (Booth booth : org.booths) {
                    if (booth.recruiters == null)
                        continue;
                    for (Recruiter recruiter : booth.recruiters) {
                        if (recruiter.offers == null)
                            continue;
                        for (Offer offer : recruiter.offers) {
                            cachedOffers.add(offer);
                            manualOfferListModel.addElement(String.format("[%s] %s - %s (%d min)",
                                    offer.startTime != null ? offer.startTime.toString() : "TBD",
                                    offer.title,
                                    recruiter.displayName,
                                    offer.durationMins));
                        }
                    }
                }
            }

            if (cachedOffers.isEmpty()) {
                manualStatusLabel.setText("No offers available yet.");
            } else {
                manualStatusLabel.setText("Select an offer, then choose a time slot.");
            }
        });
    }

    private void refreshSlotsForOffer() {
        int idx = manualOfferList.getSelectedIndex();
        manualSlotListModel.clear();
        if (idx < 0 || idx >= cachedOffers.size())
            return;

        Offer selected = cachedOffers.get(idx);
        if (selected.startTime != null) {
            manualSlotListModel.addElement(
                    selected.startTime + " -> " + selected.endTime + " (" + selected.durationMins + " min)");
        } else {
            manualSlotListModel.addElement("No time slot assigned yet.");
        }
    }

    private void handleManualBook() {
        if (loggedInCandidate == null) {
            manualStatusLabel.setText("You must be logged in to book.");
            return;
        }
        int offerIdx = manualOfferList.getSelectedIndex();
        if (offerIdx < 0 || offerIdx >= cachedOffers.size()) {
            manualStatusLabel.setText("Please select an offer first.");
            return;
        }
        if (manualSlotList.getSelectedIndex() < 0) {
            manualStatusLabel.setText("Please select a time slot.");
            return;
        }

        Offer chosenOffer = cachedOffers.get(offerIdx);
        try {
            Reservation reservation = controller.onManualBookOffer(loggedInCandidate, chosenOffer);
            manualStatusLabel.setText("Reserved: " + reservation.offer.title
                    + " at " + reservation.scheduledStart);
            refreshScheduleTab();
        } catch (Exception ex) {
            manualStatusLabel.setText("Booking failed: " + ex.getMessage());
        }
    }

    // =========================================================
    // AUTO-BOOKING (VCFS-015)
    // =========================================================

    private void handleAutoBook() {
        if (loggedInCandidate == null) {
            autoStatusLabel.setText("You must be logged in.");
            return;
        }
        String tags = autoTagsField.getText().trim();
        String orgs = autoOrgsField.getText().trim();
        int maxAppt = (Integer) autoMaxSpinner.getValue();

        if (tags.isEmpty()) {
            autoStatusLabel.setText("Please enter at least one desired tag.");
            return;
        }

        autoStatusLabel.setText("Searching...");
        autoProposalArea.setText("");
        lastProposals.clear();

        SwingWorker<List<Reservation>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Reservation> doInBackground() {
                Request req = system.createRequest(loggedInCandidate, tags, orgs, maxAppt);
                return controller.onAutoBook(loggedInCandidate, req);
            }

            @Override
            protected void done() {
                try {
                    List<Reservation> proposed = get();
                    lastProposals.addAll(proposed);
                    if (proposed.isEmpty()) {
                        autoStatusLabel.setText("No matching offers found.");
                        autoProposalArea.setText("No results for the given tags.");
                    } else {
                        autoStatusLabel.setText(proposed.size() + " match(es) — accept or reject below.");
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < proposed.size(); i++) {
                            Reservation r = proposed.get(i);
                            sb.append(String.format("[%d] %s  |  %s -> %s  |  Tags: %s%n",
                                    i + 1,
                                    r.offer != null ? r.offer.title : "?",
                                    r.scheduledStart != null ? r.scheduledStart : "?",
                                    r.scheduledEnd != null ? r.scheduledEnd : "?",
                                    r.offer != null && r.offer.topicTags != null ? r.offer.topicTags : "—"));
                        }
                        autoProposalArea.setText(sb.toString());
                    }
                } catch (Exception ex) {
                    autoStatusLabel.setText("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void handleAcceptAll() {
        if (lastProposals.isEmpty()) {
            autoStatusLabel.setText("No proposals to accept.");
            return;
        }
        for (Reservation r : lastProposals) {
            r.state = ReservationState.CONFIRMED;
        }
        autoStatusLabel.setText(lastProposals.size() + " reservation(s) confirmed.");
        refreshScheduleTab();
    }

    private void handleRejectAll() {
        if (lastProposals.isEmpty()) {
            autoStatusLabel.setText("No proposals to reject.");
            return;
        }
        for (Reservation r : lastProposals) {
            r.cancel("Rejected by candidate");
            if (loggedInCandidate.reservations != null)
                loggedInCandidate.reservations.remove(r);
        }
        lastProposals.clear();
        autoProposalArea.setText("");
        autoStatusLabel.setText("All proposals rejected.");
        refreshScheduleTab();
    }

    // =========================================================
    // SCHEDULE TAB — refresh
    // =========================================================

    private void refreshScheduleTab() {
        SwingUtilities.invokeLater(() -> {
            cachedSchedule.clear();
            if (loggedInCandidate == null || loggedInCandidate.reservations == null) {
                scheduleArea.setText("No reservations yet.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Reservation r : loggedInCandidate.reservations) {
                if (r.offer == null)
                    continue;
                cachedSchedule.add(r);
                sb.append(String.format("[%d] %s  |  Recruiter: %s  |  %s -> %s  |  Status: %s%n",
                        i++,
                        r.offer.title,
                        r.offer.publisher != null ? r.offer.publisher.displayName : "—",
                        r.scheduledStart != null ? r.scheduledStart : "—",
                        r.scheduledEnd != null ? r.scheduledEnd : "—",
                        r.state != null ? r.state.name() : "—"));
            }
            scheduleArea.setText(sb.length() > 0 ? sb.toString() : "No reservations yet.");
        });
    }

    // =========================================================
    // LOBBY GATEKEEPER (VCFS-016)
    // =========================================================

    /**
     * "Join Selected" — reads the line number the user typed, finds the
     * reservation,
     * then enforces the time gate.
     */
    private void handleJoinFromSchedule() {
        if (loggedInCandidate == null)
            return;

        String input = JOptionPane.showInputDialog(this,
                "Enter reservation number to join (see My Schedule tab):",
                "Join Session", JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.isBlank())
            return;

        int idx;
        try {
            idx = Integer.parseInt(input.trim()) - 1;
        } catch (NumberFormatException ex) {
            scheduleStatusLabel.setText("Invalid number entered.");
            return;
        }

        if (idx < 0 || idx >= cachedSchedule.size()) {
            scheduleStatusLabel.setText("No reservation with that number.");
            return;
        }

        Reservation reservation = cachedSchedule.get(idx);

        // ── LOBBY GATEKEEPER ───────────────────────────────────
        if (system.getCurrentFairPhase() != FairPhase.FAIR_LIVE) {
            scheduleStatusLabel.setText("You can only join sessions during FAIR_LIVE.");
            return;
        }
        if (reservation.scheduledStart == null) {
            scheduleStatusLabel.setText("Session has no scheduled start time.");
            return;
        }
        if (reservation.state == ReservationState.CANCELLED) {
            scheduleStatusLabel.setText("This reservation has been cancelled.");
            return;
        }

        LocalDateTime now = SystemTimer.getInstance().getNow();
        long minutesUntilStart = now.minutesUntil(reservation.scheduledStart);

        if (minutesUntilStart > 0) {
            pendingLobbyReservation = reservation;
            enterLobby(reservation, minutesUntilStart);
        } else {
            proceedToJoinSession(reservation);
        }
    }

    private void enterLobby(Reservation reservation, long minsRemaining) {
        String offerTitle = reservation.offer != null ? reservation.offer.title : "your session";
        lobbyTitleLabel.setText("Waiting for: " + offerTitle);
        lobbyStatusLabel.setText("Scheduled at: " + reservation.scheduledStart
                + "  |  Recruiter: " + (reservation.offer != null && reservation.offer.publisher != null
                        ? reservation.offer.publisher.displayName
                        : "—"));

        updateLobbyCountdown(minsRemaining);
        showCard(CARD_LOBBY);

        if (lobbySwingTimer != null && lobbySwingTimer.isRunning()) {
            lobbySwingTimer.stop();
        }

        lobbySwingTimer = new javax.swing.Timer(1000, e -> {
            LocalDateTime currentNow = SystemTimer.getInstance().getNow();
            long remaining = currentNow.minutesUntil(reservation.scheduledStart);
            if (remaining <= 0) {
                lobbySwingTimer.stop();
                proceedToJoinSession(reservation);
            } else {
                updateLobbyCountdown(remaining);
            }
        });
        lobbySwingTimer.start();
    }

    private void updateLobbyCountdown(long minutesRemaining) {
        long hrs = minutesRemaining / 60;
        long mins = minutesRemaining % 60;
        String display = hrs > 0
                ? String.format("%02d:%02d", hrs, mins)
                : String.format("%02d:00", mins);
        lobbyCountdownLabel.setText(display);
    }

    private void proceedToJoinSession(Reservation reservation) {
        if (lobbySwingTimer != null)
            lobbySwingTimer.stop();

        try {
            controller.onJoinSession(loggedInCandidate, reservation.offer.title);
        } catch (UnsupportedOperationException stub) {
            // Stub — UI still proceeds
        }

        showCard(CARD_MAIN);
        mainTabs.setSelectedIndex(3);

        JOptionPane.showMessageDialog(this,
                "You have joined: " + (reservation.offer != null ? reservation.offer.title : "the session"),
                "Session Joined", JOptionPane.INFORMATION_MESSAGE);

        refreshScheduleTab();
        pendingLobbyReservation = null;
    }

    private void leaveLobby() {
        if (lobbySwingTimer != null)
            lobbySwingTimer.stop();
        pendingLobbyReservation = null;
        showCard(CARD_MAIN);
    }

    // =========================================================
    // OBSERVER — SystemTimer ticks
    // =========================================================

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"time".equals(evt.getPropertyName()))
            return;

        SwingUtilities.invokeLater(() -> {
            LocalDateTime now = SystemTimer.getInstance().getNow();
            clockLabel.setText("Clock: " + now.toString());

            FairPhase phase = system.getCurrentFairPhase();
            switch (phase) {
                case DORMANT -> phaseLabel.setText("Phase: DORMANT");
                case PREPARING -> phaseLabel.setText("Phase: PREPARING");
                case BOOKINGS_OPEN -> phaseLabel.setText("Phase: BOOKINGS OPEN");
                case BOOKINGS_CLOSED -> phaseLabel.setText("Phase: BOOKINGS CLOSED");
                case FAIR_LIVE -> phaseLabel.setText("Phase: FAIR LIVE");
            }
        });
    }

    // =========================================================
    // CARD SWITCH
    // =========================================================

    private void showCard(String name) {
        cardLayout.show(cardContainer, name);
    }
}
