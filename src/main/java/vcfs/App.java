package vcfs;

import vcfs.core.CareerFairSystem;
import vcfs.core.SystemTimer;
import vcfs.views.admin.AdminScreen;
import vcfs.views.candidate.CandidateScreen;
import vcfs.views.recruiter.RecruiterScreen;
import vcfs.views.shared.SystemTimerScreen;

import javax.swing.*;

import vcfs.core.Logger;
import vcfs.core.LogLevel;

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
                
                System.out.println("========================================");
                System.out.println(" Virtual Career Fair System (VCFS)");
                System.out.println(" Group 9 — CSCU9P6");
                System.out.println(" Project Manager: Zaid");
                System.out.println("========================================");

                // TODO (Zaid): Initialise Singleton CareerFairSystem
                // CareerFairSystem system = CareerFairSystem.getInstance();

                // TODO (YAMI): Open the Administrator Screen
                // new AdminScreen(system).setVisible(true);

                // TODO (MJAMishkat): Open the Candidate Screen
                // new CandidateScreen(system).setVisible(true);

                // TODO (Taha): Open the Recruiter Screen
                // new RecruiterScreen(system).setVisible(true);
            });
            
            // Simulating a random critical error for testing our new Logger!
            // Uncomment the next line to test logging errors to file:
            // throw new RuntimeException("Simulated Database Connection Failure!");
            
        } catch (Exception e) {
            Logger.critical("Fatal application crash detected", e);
        }
    }
}
