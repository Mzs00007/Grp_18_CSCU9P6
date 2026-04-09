package vcfs.core;

import javax.swing.*;
import java.util.*;

/**
 * PORTAL FLOW GUIDE - Step-by-step guidance for each user role
 * 
 * Shows exactly what to do at each stage of the demo/system usage.
 * No confusion about how portals work.
 */
public class PortalFlowGuide {

    
    public static final class CandidateFlow {
        public static String getFlowDescription() {
            return "🎓 CANDIDATE FLOW - Book Your Interviews\n\n" +
                   "Step 1: YOU ARE HERE ✓\n" +
                   "  → You've logged in as a candidate\n" +
                   "  → You see available interview opportunities\n\n" +
                   "Step 2: SEARCH FOR OFFERS\n" +
                   "  → Enter skills you want to discuss (Java, AI, Cloud)\n" +
                   "  → System shows matching interview slots\n" +
                   "  → Each slot shows recruiter name, company, time\n\n" +
                   "Step 3: BOOK YOUR INTERVIEWS\n" +
                   "  → Click 'Book' on slots that interest you\n" +
                   "  → System confirms: 'Interview booked!'\n" +
                   "  → You can book up to 3 interviews\n\n" +
                   "Step 4: MANAGE YOUR SCHEDULE\n" +
                   "  → View 'My Schedule' tab\n" +
                   "  → See all your confirmed interviews\n" +
                   "  → Shows: Time | Recruiter | Company\n\n" +
                   "Step 5: WATCH FOR RECRUITER\n" +
                   "  → When your scheduled time arrives\n" +
                   "  → Recruiter enters the Virtual Room\n" +
                   "  → Interview begins (you can see recruiter's actions)\n\n" +
                   "NO STRESS - You control when to book!\n" +
                   "All times shown in demo clock (compressed for demo)";
        }
        
        public static String[] getTabs() {
            return new String[]{"Search Offers", "My Schedule", "Request History"};
        }
        
        public static String getStepAssistance(int step) {
            switch(step) {
                case 1: return "💡 LOOKING FOR OFFERS?\nEnter keywords like: java, spring, ai, cloud, frontend, backend";
                case 2: return "💡 READY TO BOOK?\nClick the green 'BOOK' button next to any interview that interests you";
                case 3: return "💡 CONFIRM BOOKING?\nLook for the ✓ success message and check your schedule to verify";
                case 4: return "💡 NO MORE SLOTS?\nCheck 'My Schedule' to see your confirmed interviews";
                default: return "How can we help? Check the tabs above!";
            }
        }
    }
    
    public static final class RecruiterFlow {
        public static String getFlowDescription() {
            return "💼 RECRUITER FLOW - Interview Candidates\n\n" +
                   "Step 1: YOU ARE HERE ✓\n" +
                   "  → You've logged in as a recruiter\n" +
                   "  → You represent your company (Google/Microsoft/Apple)\n\n" +
                   "Step 2: PUBLISH YOUR OFFERS\n" +
                   "  → Go to 'Publish Offers' tab\n" +
                   "  → System shows your available interview times\n" +
                   "  → You already have 6 slots (8:00am - 9:30am, in 30min intervals)\n" +
                   "  → These are ready for candidates to book\n\n" +
                   "Step 3: MANAGE YOUR SCHEDULE\n" +
                   "  → Go to 'Schedule' tab\n" +
                   "  → See all booked interviews\n" +
                   "  → Status shows: AVAILABLE | BOOKED | IN_PROGRESS | COMPLETED\n" +
                   "  → Red = slots with booked candidates (you'll interview them)\n\n" +
                   "Step 4: ENTER VIRTUAL ROOMS\n" +
                   "  → When interview time arrives\n" +
                   "  → Click 'Enter Virtual Room' for that interview\n" +
                   "  → Candidate appears in the room\n" +
                   "  → You can see their details\n\n" +
                   "Step 5: CONDUCT INTERVIEWS\n" +
                   "  → Ask questions, assess candidate\n" +
                   "  → Track time (30-minute slots)\n" +
                   "  → When time ends, candidate is automatically disconnected\n" +
                   "  → Next interview slot becomes active\n\n" +
                   "FLOW: Your slots auto-create from availability.\n" +
                   "Just watch candidates book your times!";
        }
        
        public static String[] getTabs() {
            return new String[]{"Publish Offers", "Schedule", "Virtual Room", "Interview History"};
        }
        
        public static String getStepAssistance(int step) {
            switch(step) {
                case 1: return "💡 YOUR OFFERS\nAlmost ready! Check 'Publish Offers' to verify availability and tags";
                case 2: return "💡 WATCH FOR BOOKINGS\nRefresh your schedule to see candidates booking your slots";
                case 3: return "💡 INTERVIEW TIME\nWhen a slot time arrives, click 'Enter Room' to start interviewing";
                case 4: return "💡 INTERVIEW IN PROGRESS\nConduct the interview - candidate details shown on screen";
                case 5: return "💡 INTERVIEW COMPLETE\nSlot auto-ends. Next interview slot activates automatically";
                default: return "Questions? Check your schedule or offer details!";
            }
        }
    }
    
    public static final class AdminFlow {
        public static String getFlowDescription() {
            return "🔐 ADMIN FLOW - Setup & Monitor Career Fair\n\n" +
                   "Step 1: YOU ARE HERE ✓\n" +
                   "  → You have full system access\n" +
                   "  → You can create orgs, manage recruiters, monitor everything\n\n" +
                   "Step 2: CREATE ORGANIZATIONS\n" +
                   "  → Add companies that will participate\n" +
                   "  → For demo: Google, Microsoft, Apple already exist\n" +
                   "  → Each org gets a booth at the fair\n\n" +
                   "Step 3: CREATE BOOTHS\n" +
                   "  → Each booth is where a recruiter sits\n" +
                   "  → Assign booth to organization\n" +
                   "  → Configure interviewer (recruiter) for that booth\n\n" +
                   "Step 4: ASSIGN RECRUITERS\n" +
                   "  → Recruiters are employees from each company\n" +
                   "  → Assign them to specific booths\n" +
                   "  → One recruiter per booth in this demo\n" +
                   "  → For demo: Alice (Google), Bob (Microsoft), Carol (Apple)\n\n" +
                   "Step 5: REGISTER CANDIDATES\n" +
                   "  → Candidates apply to the fair\n" +
                   "  → System tracks their skills and experience\n" +
                   "  → For demo: David, Elena, Frank pre-registered\n\n" +
                   "Step 6: MONITOR AUDIT LOG\n" +
                   "  → See EVERY action in the system\n" +
                   "  → Time | Action | User | Result\n" +
                   "  → Checks system is running correctly\n" +
                   "  → Proves all interviews logged (compliance!)\n\n" +
                   "POWER: You see everything. You control the timeline!";
        }
        
        public static String[] getTabs() {
            return new String[]{"Organizations", "Booths", "Recruiters", "Candidates", "Audit Log"};
        }
        
        public static String getStepAssistance(int step) {
            switch(step) {
                case 1: return "💡 ORGANIZATIONS\nAlready created for demo! You can add more companies as needed";
                case 2: return "💡 BOOTHS\nEach booth represents a recruiter's interview station";
                case 3: return "💡 RECRUITERS\nThey conduct interviews. Assign them to booths from this interface";
                case 4: return "💡 CANDIDATES\nView all applicants and their skills for matching with recruiters";
                case 5: return "💡 AUDIT LOG\nFull transaction history. Every booking, interview, and system event logged";
                default: return "Admin panel ready! Use tabs above to manage the career fair";
            }
        }
    }
    
    public static void showPortalGuide(JFrame parent, String portalType) {
        String guide = "";
        switch(portalType.toLowerCase()) {
            case "candidate":
                guide = CandidateFlow.getFlowDescription();
                break;
            case "recruiter":
                guide = RecruiterFlow.getFlowDescription();
                break;
            case "admin":
                guide = AdminFlow.getFlowDescription();
                break;
        }
        
        JTextArea textArea = new JTextArea(guide);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 11));
        textArea.setMargin(new javax.swing.border.EmptyBorder(10, 10, 10, 10).getBorderInsets(new JPanel()));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 500));
        
        JOptionPane.showMessageDialog(parent, scrollPane, 
            "📖 " + portalType.toUpperCase() + " PORTAL - HOW IT WORKS", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static String getTipForAction(String action) {
        Map<String, String> tips = new HashMap<>();
        tips.put("search", "💡 Enter tags separated by spaces: java python cloud ai");
        tips.put("book", "💡 Click GREEN button to book interview with recruiter");
        tips.put("schedule", "💡 Shows all your confirmed interviews with times");
        tips.put("room", "💡 Join virtual room when interview time arrives");
        tips.put("offer", "💡 Your availability is already split into bookable slots");
        tips.put("admin", "💡 You have full system access - create/view everything");
        tips.put("audit", "💡 Every system action is logged here for compliance");
        
        return tips.getOrDefault(action.toLowerCase(), "💡 Need help? Check the portal guide!");
    }
}
