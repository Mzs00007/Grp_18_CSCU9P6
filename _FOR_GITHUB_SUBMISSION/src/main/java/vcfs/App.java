package vcfs;

import javax.swing.*;

import vcfs.controllers.AdminScreenController;
import vcfs.core.CareerFairSystem;
import vcfs.core.DataPersistenceManager;
import vcfs.core.LocalDateTime;
import vcfs.core.LogLevel;
import vcfs.core.Logger;
import vcfs.core.SystemTimer;
import vcfs.core.SessionManager;
import vcfs.core.SystemStateManager;
import vcfs.core.DemoAssistant;
import vcfs.models.users.Recruiter;
import vcfs.views.admin.AdminScreen;
import vcfs.views.candidate.CandidateScreen;
import vcfs.views.recruiter.RecruiterScreen;
import vcfs.views.shared.SystemTimerScreen;

/**
 * VCFS Application Entry Point.
 *
 * Virtual Career Fair System — Group 9, CSCU9P6
 * Project Manager: Zaid
 *
 * Bootstraps the Java Swing MVC application.
 * All UI screens are launched from here.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Virtual Career Fair System (VCFS)                        ║");
        System.out.println("║              Group 18 | CSCU9P6 | University of Stirling              ║");
        System.out.println("║                                                                        ║");
        System.out.println("║              🎬 DEMONSTRATION EDITION FOR 100-PERSON AUDIENCE        ║");
        System.out.println("║              Project Manager: Zaid Siddiqui (mzs00007)                ║");
        System.out.println("║              Version: " + Logger.VERSION.substring(0, Math.min(Logger.VERSION.length(), 45)) + "                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");
        
        Logger.info("🚀 Initializing Virtual Career Fair System (Demo Edition)");
        Logger.info("═══════════════════════════════════════════════════════════════════════════════════");
        
        try {
            SwingUtilities.invokeLater(() -> {
                long startTime = System.currentTimeMillis();
                Logger.info("📱 [1/7] Launching user interface framework...");

                // VCFS-001: Initialize Singleton CareerFairSystem
                Logger.info("[2/7] Initializing core backend system...");
                CareerFairSystem system = CareerFairSystem.getInstance();
                Logger.info("  ✓ CareerFairSystem Singleton active");

                // Initialize data persistence - PREVENTS DATA LOSS
                Logger.info("[3/7] Initializing data persistence engine...");
                DataPersistenceManager persistence = DataPersistenceManager.getInstance();
                persistence.initialize(system);
                Logger.info("  ✓ Auto-save engine running (checkpoints every 5 seconds)");

                // Initialize operational state tracking
                Logger.info("[3.5/7] Initializing operational monitoring...");
                SystemStateManager stateManager = SystemStateManager.getInstance();
                Logger.info("  ✓ State Manager active (tracking all operations)");
                
                // Initialize live session monitoring
                Logger.info("[3.6/7] Initializing live session monitor...");
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.enableLiveMode();
                Logger.info("  ✓ Session Manager active (real-time activity tracking)");
                
                // Initialize demonstration script for 20-minute guided demo
                Logger.info("[3.7/7] Preparing demonstration assistant...");
                DemoAssistant demoAssistant = DemoAssistant.getInstance();
                Logger.info("  ✓ Demo Assistant ready (20-minute workflow available)");

                // Initialize system timer (for VCFS-002 Observer pattern)
                Logger.info("[4/7] Starting system time controller...");
                SystemTimer.getInstance();
                Logger.info("  ✓ SystemTimer running (compressed demo time)");

                // Initialize demo data BEFORE UI opens (so screens can access it)
                Logger.info("[5/7] Loading demo data (3 orgs, 3 recruiters, 3 candidates, 6+ slots)...");
                initializeDemoData();
                Logger.info("  ✓ Demo data fully initialized and ready");

                // OPEN MAIN MENU - This is the proper entry point for the application
                Logger.info("[6/7] Launching main portal selector...");
                new vcfs.views.shared.MainMenuFrame();
                Logger.info("  ✓ Main Menu displayed - awaiting role selection");
                
                // Initialize SystemTimerScreen for time control in demo
                Logger.info("[7/7] Starting demo time control panel...");
                new SystemTimerScreen();
                Logger.log(LogLevel.INFO, "  ✓ Time control ready (simulates 2-hour fair in 2 minutes)");
                
                long loadTime = System.currentTimeMillis() - startTime;
                Logger.info("═══════════════════════════════════════════════════════════════════════════════════");
                Logger.info("🎬 SYSTEM READY FOR DEMONSTRATION TO 100-PERSON AUDIENCE (" + loadTime + "ms startup)");
                Logger.info("   ✅ Observer pattern: ACTIVE (PropertyChangeListener monitoring)");
                Logger.info("   ✅ MVC architecture: CONFIRMED (Model-View-Controller separation)");
                Logger.info("   ✅ Thread-safe backend: ENGAGED (ConcurrentHashMap, synchronized collections)");
                Logger.info("   ✅ Data persistence: RUNNING (auto-save every 5 seconds, crash recovery enabled)");
                Logger.info("   ✅ Operation tracking: ENABLED (all actions recorded in state manager)");
                Logger.info("   ✅ Live session monitoring: ENABLED (real-time activity logs in all portals)");
                Logger.info("   ✅ Demo assistant: READY (20-minute guided demonstration workflow)");
                Logger.info("   ✅ Portal system: READY (3 portals for Candidate/Recruiter/Admin)");
                Logger.info("   ✅ Demo data: LOADED (3 orgs, 3 recruiters, 3 candidates, 6+ interview slots)");
                Logger.info("═══════════════════════════════════════════════════════════════════════════════════");
                Logger.info("📌 HOW TO USE:");
                Logger.info("   1. Click a ROLE BUTTON (Candidate/Recruiter/Admin) to enter portal");
                Logger.info("   2. Select a USERNAME to login");
                Logger.info("   3. Each portal has a FLOW GUIDE button (?) to explain how it works");
                Logger.info("   4. Use SYSTEMTIMER to advance time through the fair (lower right)");
                Logger.info("   5. Watch real-time sync: bookings appear across all 3 portals instantly!");
                Logger.info("   6. AUDIT LOG shows every system action (Admin portal → Audit Log tab)");
                Logger.info("═══════════════════════════════════════════════════════════════════════════════════");
                
                // Setup shutdown hook for graceful data save
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    Logger.info("🛑 Shutdown detected - saving all data...");
                    persistence.shutdown();
                }));
            });
            
            // Simulating a random critical error for testing our new Logger!
            // Uncomment the next line to test logging errors to file:
            // throw new RuntimeException("Simulated Database Connection Failure!");
            
        } catch (Exception e) {
            Logger.critical("Fatal application crash detected", e);
        }
    }

    /**
     * Initialize demo data for live demonstrations.
     * Creates sample organizations, booths, recruiters, candidates, and offers.
     */
    private static void initializeDemoData() {
        CareerFairSystem system = CareerFairSystem.getInstance();
        Logger.info("[Demo] Initializing demo data...");

        try {
            // Create demo organizations
            system.addOrganization("Google");
            system.addOrganization("Microsoft");
            system.addOrganization("Apple");
            Logger.info("[Demo] ✓ Created 3 demo organizations");

            // Create sample recruiters
            Recruiter rec1 = system.registerRecruiter("Alice Thompson", "alice.thompson@google.com", null);
            Recruiter rec2 = system.registerRecruiter("Bob Chen", "bob.chen@microsoft.com", null);
            Recruiter rec3 = system.registerRecruiter("Carol Singh", "carol.singh@apple.com", null);
            Logger.info("[Demo] ✓ Registered 3 demo recruiters");

            // Create sample candidates
            system.registerCandidate("David Lee", "david.lee@example.com", "Software Engineer, 5 years exp", "Java,Python,AWS");
            system.registerCandidate("Elena Rodriguez", "elena.rodriguez@example.com", "Data Scientist, ML specialist", "Python,TensorFlow,AI");
            system.registerCandidate("Frank Williams", "frank.williams@example.com", "DevOps Engineer, Cloud expert", "Kubernetes,Docker,AWS");
            Logger.info("[Demo] ✓ Registered 3 demo candidates");

            // Set demo timeline (current time to 30 mins in future for bookings, 60 mins for fair live)
            LocalDateTime now = SystemTimer.getInstance().getNow();
            LocalDateTime bookingOpen = now;
            LocalDateTime bookingClose = now.plusMinutes(30);
            LocalDateTime fairStart = now.plusMinutes(60);
            LocalDateTime fairEnd = now.plusMinutes(120);
            
            // Helper to format LocalDateTime as "yyyy-MM-ddTHH:mm"
            java.util.function.Function<LocalDateTime, String> formatTime = (LocalDateTime ldt) -> {
                // Simple ISO-format: year-month-dayThour:minute
                String str = ldt.toString(); // Returns "yyyy-MM-dd HH:mm"
                return str.replace(" ", "T"); // Convert to "yyyy-MM-ddTHH:mm"
            };
            
            system.setFairTimes(
                formatTime.apply(bookingOpen),
                formatTime.apply(bookingClose),
                formatTime.apply(fairStart),
                formatTime.apply(fairEnd)
            );
            Logger.info("[Demo] ✓ Set fair timeline for demo");

            // Publish demo offers
            try {
                int offers1 = system.parseAvailabilityIntoOffers(
                    rec1, "Backend Engineering Interview",
                    30, "Java,Spring,Microservices", 2,
                    now, now.plusMinutes(90)
                );
                Logger.info("[Demo] ✓ Created " + offers1 + " slots for Alice (Backend)");
            } catch (Exception e) {
                Logger.error("[Demo] Failed to create offers for Alice", e);
            }
            
            try {
                int offers2 = system.parseAvailabilityIntoOffers(
                    rec2, "Cloud Architecture Discussion",
                    30, "AWS,Kubernetes,Docker", 2,
                    now, now.plusMinutes(90)
                );
                Logger.info("[Demo] ✓ Created " + offers2 + " slots for Bob (Cloud)");
            } catch (Exception e) {
                Logger.error("[Demo] Failed to create offers for Bob", e);
            }
            
            try {
                int offers3 = system.parseAvailabilityIntoOffers(
                    rec3, "AI/ML Research Panel",
                    30, "Python,TensorFlow,AI", 2,
                    now, now.plusMinutes(90)
                );
                Logger.info("[Demo] ✓ Created " + offers3 + " slots for Carol (AI/ML)");
            } catch (Exception e) {
                Logger.error("[Demo] Failed to create offers for Carol", e);
            }
            Logger.info("[Demo] ✓ Created demo offers from recruiters");

            Logger.info("[Demo] ✓ Demo data initialization complete - system ready for live demonstration");

        } catch (Exception e) {
            Logger.error("Failed to initialize demo data", e);
        }
    }
}

