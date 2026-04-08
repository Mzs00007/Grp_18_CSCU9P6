package vcfs;

import javax.swing.*;

import vcfs.controllers.AdminScreenController;
import vcfs.core.CareerFairSystem;
import vcfs.core.LocalDateTime;
import vcfs.core.LogLevel;
import vcfs.core.Logger;
import vcfs.core.SystemTimer;
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
        Logger.info("Starting up Virtual Career Fair System...");
        
        try {
            SwingUtilities.invokeLater(() -> {
                Logger.info("Initializing UI threads...");
                Logger.info("========================================");
                Logger.info("Virtual Career Fair System (VCFS)");
                Logger.info("Group 18 — CSCU9P6");
                Logger.info("Project Manager: Zaid");
                Logger.info("========================================");

                // VCFS-001: Initialize Singleton CareerFairSystem
                CareerFairSystem.getInstance();
                Logger.info("✅ CareerFairSystem Singleton initialized");

                // Initialize system timer (for VCFS-002 Observer pattern)
                SystemTimer.getInstance();
                Logger.info("✅ SystemTimer initialized");

                // Initialize demo data BEFORE UI opens (so screens can access it)
                initializeDemoData();
                Logger.info("✅ Demo data initialized");

                // OPEN MAIN MENU - This is the proper entry point for the application
                // Users select their role (Admin, Recruiter, Candidate, or Demo Mode)
                new vcfs.views.shared.MainMenuFrame();
                Logger.info("✅ Main Menu opened - awaiting role selection");
                
                // Initialize SystemTimerScreen for time control in demo
                new SystemTimerScreen();
                Logger.log(LogLevel.INFO, "✅ SystemTimerScreen initialized for time control");
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

