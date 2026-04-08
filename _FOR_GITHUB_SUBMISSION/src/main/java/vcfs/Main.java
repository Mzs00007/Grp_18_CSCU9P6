package vcfs;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import javax.swing.SwingUtilities;
import vcfs.views.shared.MainMenuFrame;

/**
 * VCFS Application Entry Point
 * 
 * Launches the system with role selection menu.
 * Users can choose Admin, Recruiter, or Candidate role.
 * 
 * Original implementation by: Taha
 * Adapted to skeleton by: Zaid
 */
public class Main {

    public static void main(String[] args) {
        // Launch MainMenuFrame on EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            new MainMenuFrame();
        });
    }
}


