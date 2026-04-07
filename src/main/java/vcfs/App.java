package vcfs;

import javax.swing.*;

import vcfs.controllers.AdminScreenController;
import vcfs.core.CareerFairSystem;
import vcfs.core.LogLevel;
import vcfs.core.Logger;
import vcfs.core.SystemTimer;
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
                
                System.out.println("========================================");
                System.out.println(" Virtual Career Fair System (VCFS)");
                System.out.println(" Group 9 — CSCU9P6");
                System.out.println(" Project Manager: Zaid");
                System.out.println("========================================");

                // VCFS-001: Initialize Singleton CareerFairSystem
                CareerFairSystem.getInstance();
                Logger.info("✅ CareerFairSystem Singleton initialized");

                // Initialize system timer (for VCFS-002 Observer pattern)
                SystemTimer.getInstance();
                Logger.info("✅ SystemTimer initialized");

                // Create Admin Controller
                AdminScreenController adminController = new AdminScreenController();
                Logger.info("✅ AdminScreenController created");

                // Open Administrator Screen - Primary interface for fair setup
                AdminScreen adminScreen = new AdminScreen(adminController);
                adminScreen.setVisible(true);
                Logger.info("✅ Admin Screen opened");

                // Open additional screens for demonstration
                RecruiterScreen recruiterScreen = new RecruiterScreen();
                recruiterScreen.setVisible(true);
                Logger.log(LogLevel.INFO, "✅ Recruiter Screen opened");
                
                CandidateScreen candidateScreen = new CandidateScreen();
                candidateScreen.setVisible(true);
                Logger.log(LogLevel.INFO, "✅ Candidate Screen opened");
                
                new SystemTimerScreen(); // Just instantiate to initialize
                Logger.log(LogLevel.INFO, "✅ SystemTimerScreen initialized for time control");
            });
            
            // Simulating a random critical error for testing our new Logger!
            // Uncomment the next line to test logging errors to file:
            // throw new RuntimeException("Simulated Database Connection Failure!");
            
        } catch (Exception e) {
            Logger.critical("Fatal application crash detected", e);
        }
    }
}

