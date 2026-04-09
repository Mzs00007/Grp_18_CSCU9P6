package vcfs.views.shared;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import vcfs.core.UIEnhancementUtils;
import vcfs.core.PortalFlowGuide;

/**
 * ENHANCED MAIN MENU - Clear role selection with portal explanations
 * 
 * Shows 3 main portals with exact description of what each does
 * No confusion about the system flow
 */
public class EnhancedMainMenu extends JFrame {

    
    public EnhancedMainMenu() {
        setTitle("VCFS - Role Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(UIEnhancementUtils.LIGHT_BACKGROUND);
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        JPanel headerPanel = UIEnhancementUtils.createHeaderPanel(
            "🎓 SELECT YOUR ROLE",
            "Choose a portal to enter the Virtual Career Fair System",
            UIEnhancementUtils.PRIMARY_BLUE
        );
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Portal selection cards
        JPanel portalPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        portalPanel.setBackground(UIEnhancementUtils.LIGHT_BACKGROUND);
        
        // Candidate Portal Card
        portalPanel.add(createPortalCard(
            "🎓 CANDIDATE PORTAL",
            "Search for Interviews",
            "Browse available interview slots from recruiters\n" +
            "Book interviews that match your skills\n" +
            "Manage your interview schedule\n" +
            "Track your applications",
            UIEnhancementUtils.PRIMARY_GREEN,
            "candidate"
        ));
        
        // Recruiter Portal Card
        portalPanel.add(createPortalCard(
            "💼 RECRUITER PORTAL",
            "Interview Candidates",
            "Publish your available interview slots\n" +
            "See candidate bookings in real-time\n" +
            "Manage your interview schedule\n" +
            "Conduct interviews in virtual rooms",
            UIEnhancementUtils.PRIMARY_ORANGE,
            "recruiter"
        ));
        
        // Admin Portal Card
        portalPanel.add(createPortalCard(
            "🔐 ADMIN PORTAL",
            "Manage the System",
            "Create organizations and booths\n" +
            "Register recruiters and candidates\n" +
            "Monitor all system activity\n" +
            "View compliance audit trail",
            UIEnhancementUtils.PRIMARY_BLUE,
            "admin"
        ));
        
        mainPanel.add(portalPanel, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        infoPanel.setBackground(new Color(227, 242, 253));
        infoPanel.setBorder(BorderFactory.createLineBorder(UIEnhancementUtils.LINK_BLUE, 1, true));
        
        JLabel infoLabel = new JLabel(
            "<html><b>💡 TIP:</b> Each portal shows a flow guide button (?) to explain exactly how it works</html>"
        );
        infoLabel.setFont(UIEnhancementUtils.NORMAL_FONT);
        infoPanel.add(infoLabel);
        
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createPortalCard(String title, String subtitle, String description,
                                     Color headerColor, String portalType) {
        JPanel card = UIEnhancementUtils.createCardPanel();
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header with button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIEnhancementUtils.CARD_WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIEnhancementUtils.HEADER_FONT);
        titleLabel.setForeground(headerColor);
        
        JButton helpBtn = UIEnhancementUtils.createHelpButton("Learn how this portal works");
        helpBtn.addActionListener(e -> PortalFlowGuide.showPortalGuide(this, portalType));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(helpBtn, BorderLayout.EAST);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(UIEnhancementUtils.NORMAL_FONT);
        subtitleLabel.setForeground(headerColor);
        
        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(UIEnhancementUtils.NORMAL_FONT);
        descArea.setBackground(UIEnhancementUtils.LIGHT_BACKGROUND);
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Enter button
        JButton enterBtn = UIEnhancementUtils.createStyledButton(
            "ENTER " + portalType.toUpperCase() + " PORTAL →",
            headerColor,
            Color.WHITE
        );
        enterBtn.setFont(UIEnhancementUtils.HEADER_FONT);
        enterBtn.addActionListener(e -> {
            dispose();
            enterPortal(portalType);
        });
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(subtitleLabel, BorderLayout.PAGE_START);
        card.add(new JScrollPane(descArea), BorderLayout.CENTER);
        card.add(enterBtn, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void enterPortal(String portal) {
        dispose();
        
        // Use the existing system portals (they require user selection first)
        switch(portal.toLowerCase()) {
            case "candidate":
                UIEnhancementUtils.showInfo(null, "Next Step", 
                    "You'll now select a candidate to login as.\n" +
                    "Choose from: David Lee, Elena Rodriguez, or Frank Williams");
                new vcfs.views.candidate.CandidateScreen();
                break;
            case "recruiter":
                UIEnhancementUtils.showInfo(null, "Next Step", 
                    "You'll now select a recruiter to login as.\n" +
                    "Choose from: Alice Thompson, Bob Chen, or Carol Singh");
                new vcfs.views.recruiter.RecruiterScreen();
                break;
            case "admin":
                UIEnhancementUtils.showInfo(null, "Next Step", 
                    "Entering Administrator Portal.\n" +
                    "You have full system access.");
                new vcfs.views.admin.AdminScreen(new vcfs.controllers.AdminScreenController());
                break;
        }
    }
}
