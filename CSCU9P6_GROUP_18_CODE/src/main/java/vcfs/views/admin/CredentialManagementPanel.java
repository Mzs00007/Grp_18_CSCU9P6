package vcfs.views.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Credential Management Panel - Admin can view and manage recruiter/candidate credentials.
 * 
 * Purpose: Centralized credential management for testing and demos
 * Features: View all users, reset passwords, view demo credentials
 */
public class CredentialManagementPanel extends JPanel {


    private JTabbedPane tabbedPane;
    private DefaultTableModel recruiterTableModel;
    private DefaultTableModel candidateTableModel;
    
    public CredentialManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 245, 250));
        JLabel headerLabel = new JLabel("🔐 Credential Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setForeground(new Color(25, 70, 130));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // ===== TABBED INTERFACE =====
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Tab 1: Recruiter Credentials
        JPanel recruiterTab = createRecruiterCredentialsTab();
        tabbedPane.addTab("👔 Recruiter Accounts", recruiterTab);
        
        // Tab 2: Candidate Credentials
        JPanel candidateTab = createCandidateCredentialsTab();
        tabbedPane.addTab("🎓 Candidate Accounts", candidateTab);
        
        // Tab 3: Demo Credentials Guide
        JPanel demoTab = createDemoCredentialsTab();
        tabbedPane.addTab("📋 Demo Login Guide", demoTab);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Create recruiter credentials management panel
     */
    private JPanel createRecruiterCredentialsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header instruction
        JPanel instrPanel = new JPanel();
        instrPanel.setBackground(new Color(240, 245, 250));
        JLabel instr = new JLabel("👇 All Registered Recruiter Accounts - Use these for Recruiter Portal Login:");
        instr.setFont(new Font("Arial", Font.PLAIN, 11));
        instr.setForeground(new Color(60, 60, 60));
        instrPanel.add(instr);
        panel.add(instrPanel, BorderLayout.NORTH);
        
        // Create table
        String[] columnNames = {"Username (Display Name)", "Default Password", "Status", "Quick Copy"};
        recruiterTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only "Quick Copy" column is editable
            }
        };
        
        // Add demo recruiter accounts
        recruiterTableModel.addRow(new Object[]{"Ahmed Hassan", "recruiter123", "✓ Active", "COPY"});
        recruiterTableModel.addRow(new Object[]{"Mohamed Ali", "recruiter456", "✓ Active", "COPY"});
        recruiterTableModel.addRow(new Object[]{"Fatima Khan", "recruiter789", "✓ Active", "COPY"});
        recruiterTableModel.addRow(new Object[]{"David Smith", "recruiter999", "✓ Active", "COPY"});
        recruiterTableModel.addRow(new Object[]{"Sarah Johnson", "recruiter555", "✓ Active", "COPY"});
        
        JTable table = new JTable(recruiterTableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(32);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Instructions panel
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBackground(new Color(255, 250, 205));
        helpPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 2));
        helpPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel helpTitle = new JLabel("💡 Testing Instructions:");
        helpTitle.setFont(new Font("Arial", Font.BOLD, 11));
        helpPanel.add(helpTitle);
        
        JTextArea helpText = new JTextArea(
            "1. For Recruiter Portal login:\n" +
            "   - Use any username from the 'Username' column above\n" +
            "   - Use the corresponding 'Default Password'\n" +
            "   - Example: Username=Ahmed Hassan, Password=recruiter123\n\n" +
            "2. All recruiter accounts are pre-configured with offers\n" +
            "3. Multiple recruiters can be logged in simultaneously for demo\n" +
            "4. Demo purposes only - reset via Admin Console if needed"
        );
        helpText.setFont(new Font("Arial", Font.PLAIN, 10));
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setOpaque(false);
        helpPanel.add(helpText);
        
        panel.add(helpPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create candidate credentials management panel
     */
    private JPanel createCandidateCredentialsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header instruction
        JPanel instrPanel = new JPanel();
        instrPanel.setBackground(new Color(240, 245, 250));
        JLabel instr = new JLabel("👇 All Registered Candidate Accounts - Use these for Candidate Portal Login:");
        instr.setFont(new Font("Arial", Font.PLAIN, 11));
        instr.setForeground(new Color(60, 60, 60));
        instrPanel.add(instr);
        panel.add(instrPanel, BorderLayout.NORTH);
        
        // Create table
        String[] columnNames = {"Email", "Display Name", "Default Password", "Status", "Quick Copy"};
        candidateTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only "Quick Copy" column is editable
            }
        };
        
        // Add demo candidate accounts
        candidateTableModel.addRow(new Object[]{"alice@email.com", "Alice Brown", "candidate123", "✓ Active", "COPY"});
        candidateTableModel.addRow(new Object[]{"bob@email.com", "Bob Wilson", "candidate456", "✓ Active", "COPY"});
        candidateTableModel.addRow(new Object[]{"chloe@email.com", "Chloe Davis", "candidate789", "✓ Active", "COPY"});
        candidateTableModel.addRow(new Object[]{"diana@email.com", "Diana Martinez", "candidate999", "✓ Active", "COPY"});
        candidateTableModel.addRow(new Object[]{"ethan@email.com", "Ethan Taylor", "candidate555", "✓ Active", "COPY"});
        
        JTable table = new JTable(candidateTableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(32);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Instructions panel
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBackground(new Color(200, 255, 200));
        helpPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel helpTitle = new JLabel("💡 Testing Instructions:");
        helpTitle.setFont(new Font("Arial", Font.BOLD, 11));
        helpPanel.add(helpTitle);
        
        JTextArea helpText = new JTextArea(
            "1. For Candidate Portal login:\n" +
            "   - Use any email from the 'Email' column above\n" +
            "   - Use the corresponding 'Default Password'\n" +
            "   - Enter the 'Display Name' as Full Name\n" +
            "   - Example: Email=alice@email.com, Password=candidate123, Name=Alice Brown\n\n" +
            "2. All candidate accounts are pre-loaded with bookings\n" +
            "3. Multiple candidates can be logged in simultaneously for demo\n" +
            "4. Demo purposes only - reset via Admin Console if needed"
        );
        helpText.setFont(new Font("Arial", Font.PLAIN, 10));
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setOpaque(false);
        helpPanel.add(helpText);
        
        panel.add(helpPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create demo credentials reference guide tab
     */
    private JPanel createDemoCredentialsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JTextArea guideText = new JTextArea();
        guideText.setFont(new Font("Courier New", Font.PLAIN, 11));
        guideText.setEditable(false);
        guideText.setLineWrap(true);
        guideText.setWrapStyleWord(true);
        guideText.setBackground(new Color(250, 250, 250));
        guideText.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        
        String[] recruiterCredentials = {
            "Ahmed Hassan / recruiter123",
            "Mohamed Ali / recruiter456",
            "Fatima Khan / recruiter789",
            "David Smith / recruiter999",
            "Sarah Johnson / recruiter555"
        };
        
        String[] candidateCredentials = {
            "alice@email.com / candidate123 (Alice Brown)",
            "bob@email.com / candidate456 (Bob Wilson)",
            "chloe@email.com / candidate789 (Chloe Davis)",
            "diana@email.com / candidate999 (Diana Martinez)",
            "ethan@email.com / candidate555 (Ethan Taylor)"
        };
        
        StringBuilder guide = new StringBuilder();
        guide.append("════════════════════════════════════════════════════════════\n");
        guide.append("             ADMIN CREDENTIAL MANAGEMENT GUIDE\n");
        guide.append("════════════════════════════════════════════════════════════\n\n");
        
        guide.append("📌 RECRUITER PORTAL LOGIN CREDENTIALS:\n");
        guide.append("─────────────────────────────────────\n");
        for (String cred : recruiterCredentials) {
            guide.append("  • ").append(cred).append("\n");
        }
        
        guide.append("\n📌 CANDIDATE PORTAL LOGIN CREDENTIALS:\n");
        guide.append("─────────────────────────────────────\n");
        for (String cred : candidateCredentials) {
            guide.append("  • ").append(cred).append("\n");
        }
        
        guide.append("\n\n📋 HOW TO USE FOR DEMO & TESTING:\n");
        guide.append("─────────────────────────────────\n");
        guide.append("1. Main Menu → Select role (Admin, Recruiter, or Candidate)\n");
        guide.append("2. For Recruiter Login:\n");
        guide.append("   - Username: Pick any from recruiter list\n");
        guide.append("   - Password: Use corresponding password\n");
        guide.append("3. For Candidate Login:\n");
        guide.append("   - Email: Pick any from candidate list\n");
        guide.append("   - Password: Use corresponding password\n");
        guide.append("   - Full Name: Use display name from list\n");
        guide.append("4. Multiple users can be logged in simultaneously\n\n");
        
        guide.append("🔄 IMPORTANT:\n");
        guide.append("─────────────\n");
        guide.append("• All credentials managed by Admin → Credentials tab\n");
        guide.append("• Password reset available (future enhancement)\n");
        guide.append("• Demo mode shows all 3 portals side-by-side\n");
        guide.append("• Each portal operates independently\n");
        guide.append("• Booking changes reflect across all sessions\n");
        guide.append("════════════════════════════════════════════════════════════\n");
        
        guideText.setText(guide.toString());
        guideText.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(guideText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}
