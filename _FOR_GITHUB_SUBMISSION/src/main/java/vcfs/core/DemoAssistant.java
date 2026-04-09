package vcfs.core;

import javax.swing.*;
import java.util.*;

/**
 * DEMO ASSISTANT - Guides presenter through scripted 20-minute presentation
 * 
 * Provides:
 * - Step-by-step guidance for presenter
 * - Automatic timing management
 * - Suggested actions at each stage
 * - Real-time tips and talking points
 * - Portal flow explanations
 */
public class DemoAssistant {

    
    private static DemoAssistant instance;
    private int currentStep = 0;
    private long demoStartTime = 0;
    private boolean demoActive = false;
    private final List<DemoStep> steps = new ArrayList<>();
    
    private DemoAssistant() {
        initializeDemoSteps();
    }
    
    public static synchronized DemoAssistant getInstance() {
        if (instance == null) {
            instance = new DemoAssistant();
        }
        return instance;
    }
    
    private void initializeDemoSteps() {
        // PART 1: Introduction (2 minutes)
        steps.add(new DemoStep("INTRODUCTION", 0, 120, 
            "Welcome & System Overview",
            "Explain what VCFS does\n" +
            "Show system architecture\n" +
            "Mention 3 portals and 4 demo roles"));
        
        // PART 2: Admin Setup (3 minutes)
        steps.add(new DemoStep("ADMIN_SETUP", 120, 300,
            "Administrator Portal Setup",
            "Click 'Administrator' button\n" +
            "Select role\n" +
            "Show: 3 organizations, 3 recruiters, 3 candidates\n" +
            "Explain audit log"));
        
        // PART 3: Recruiter Portal (4 minutes)
        steps.add(new DemoStep("RECRUITER_DEMO", 300, 540,
            "Recruiter Portal - Alice Thompson",
            "Login as Alice (Google Backend)\n" +
            "Show 6 available interview slots\n" +
            "Explain: automatic slot generation from availability\n" +
            "Show tags: Java, Spring, Microservices"));
        
        // PART 4: Candidate Search (3 minutes)
        steps.add(new DemoStep("CANDIDATE_SEARCH", 540, 720,
            "Candidate Portal - David Lee",
            "Login as David (Candidate)\n" +
            "Search for: 'java spring microservices'\n" +
            "Show results: Alice's 6 slots available\n" +
            "Explain search and matching algorithm"));
        
        // PART 5: Live Booking & Sync (2 minutes)
        steps.add(new DemoStep("LIVE_BOOKING", 720, 840,
            "Real-Time Booking & Synchronization",
            "David books Alice's 8:30am slot\n" +
            "Show success message\n" +
            "Advance time 30 minutes\n" +
            "Switch to Alice's screen - shows David's booking!\n" +
            "Explain: Observer pattern, PropertyChangeListener"));
        
        // PART 6: Architecture Explanation (2 minutes)
        steps.add(new DemoStep("ARCHITECTURE", 840, 960,
            "System Architecture Deep-Dive",
            "Show CandidateController code\n" +
            "Explain: Model-View-Controller\n" +
            "Show error handling and logging\n" +
            "Highlight: Thread-safe collections, Observer pattern"));
        
        // PART 7: Advanced Features (2 minutes)
        steps.add(new DemoStep("FEATURES", 960, 1080,
            "Advanced Features & Scalability",
            "Show data persistence features\n" +
            "Explain: Session management, audit trail\n" +
            "Discuss: How system scales to 1000s of users\n" +
            "Show state management architecture"));
        
        // PART 8: Q&A (5 minutes)
        steps.add(new DemoStep("QA", 1080, 1380,
            "Questions and Answers",
            "Open the floor for questions\n" +
            "Answer technical questions\n" +
            "Show code examples as needed\n" +
            "Discuss: Production deployment, scalability, security"));
        
        Logger.info("[DemoAssistant] Initialized with 8 demonstration steps (20 minutes total)");
    }
    
    /**
     * Start the demo timer
     */
    public void startDemo() {
        demoStartTime = System.currentTimeMillis();
        demoActive = true;
        currentStep = 0;
        Logger.info("[DemoAssistant] 🎬 DEMO STARTED - 20 minute presentation begins");
    }
    
    /**
     * Get current step and elapsed time
     */
    public String getCurrentStatus() {
        if (!demoActive) {
            return "Demo not started. Click 'START DEMO' to begin 20-minute presentation.";
        }
        
        long elapsedMs = System.currentTimeMillis() - demoStartTime;
        long elapsedSeconds = elapsedMs / 1000;
        int elapsedMinutes = (int)(elapsedSeconds / 60);
        int secondsInMinute = (int)(elapsedSeconds % 60);
        
        // Find current step
        currentStep = 0;
        for (int i = 0; i < steps.size(); i++) {
            if (elapsedSeconds >= steps.get(i).startSeconds) {
                currentStep = i;
            } else {
                break;
            }
        }
        
        DemoStep step = steps.get(Math.min(currentStep, steps.size() - 1));
        int timeInStepSeconds = (int)elapsedSeconds - step.startSeconds;
        int timeInStepMinutes = timeInStepSeconds / 60;
        int secondsRemaining = step.durationSeconds - timeInStepSeconds;
        
        return String.format(
            "📌 DEMO STEP %d/%d: %s\n" +
            "⏱️  Time: %d:%02d / 20:00\n" +
            "⏱️  Step: %d:%02d of %d:%02d remaining\n" +
            "\n%s",
            currentStep + 1, steps.size(), step.title,
            elapsedMinutes, secondsInMinute,
            timeInStepMinutes, timeInStepSeconds % 60,
            secondsRemaining / 60, secondsRemaining % 60,
            step.guidance
        );
    }
    
    /**
     * Get next step guidance
     */
    public String getNextStepGuidance() {
        if (currentStep + 1 < steps.size()) {
            DemoStep nextStep = steps.get(currentStep + 1);
            return "NEXT STEP: " + nextStep.title + "\n\n" + nextStep.guidance;
        }
        return "Demo Complete! Thank you for your attention.";
    }
    
    /**
     * Get a tip for current action
     */
    public String getTip(String action) {
        String tips = "";
        
        switch(action.toLowerCase()) {
            case "login":
                tips = "💡 Click on any role button (Admin/Recruiter/Candidate)\n" +
                       "    Then select a username from the dropdown";
                break;
            case "search":
                tips = "💡 Enter keywords separated by spaces\n" +
                       "    Example: 'java spring microservices'\n" +
                       "    System shows matching interview offers";
                break;
            case "book":
                tips = "💡 Click GREEN 'BOOK' button next to any offer\n" +
                       "    You'll see a ✓ confirmation message\n" +
                       "    Interview appears in 'My Schedule' tab";
                break;
            case "time":
                tips = "💡 Use SystemTimer screen (lower right)\n" +
                       "    Click 'Advance 30 Minutes' to skip ahead\n" +
                       "    All portals update automatically!";
                break;
            case "sync":
                tips = "💡 This is real-time Observer pattern!\n" +
                       "    PropertyChangeListener on all screens\n" +
                       "    Change in one portal → others update instantly";
                break;
            default:
                tips = "💡 Need help? Check the flow guide button (?) in each portal!";
        }
        
        return tips;
    }
    
    /**
     * Get all demo steps
     */
    public String getAllSteps() {
        StringBuilder sb = new StringBuilder("📋 20-MINUTE DEMO SEQUENCE:\n\n");
        for (int i = 0; i < steps.size(); i++) {
            DemoStep step = steps.get(i);
            sb.append(String.format("%d. %s (%d min)\n", 
                i + 1, step.title, (step.durationSeconds / 60)));
        }
        return sb.toString();
    }
    
    /**
     * End demo
     */
    public void endDemo() {
        demoActive = false;
        Logger.info("[DemoAssistant] Demo ended. Preview for next time available.");
    }
    
    /**
     * Inner class: Demo step definition
     */
    private static class DemoStep {
        String name;
        int startSeconds;
        int durationSeconds;
        String title;
        String guidance;
        
        DemoStep(String name, int startSeconds, int durationSeconds, String title, String guidance) {
            this.name = name;
            this.startSeconds = startSeconds;
            this.durationSeconds = durationSeconds - startSeconds;
            this.title = title;
            this.guidance = guidance;
        }
    }
}
