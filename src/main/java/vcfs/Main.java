package vcfs;

import javax.swing.SwingUtilities;
import vcfs.views.shared.LoginFrame;

/**
 * VCFS Application Entry Point
 * 
 * Launches the system with initial login frame.
 * 
 * Original implementation by: Taha
 * Adapted to skeleton by: Zaid
 */
public class Main {

    public static void main(String[] args) {
        // Launch LoginFrame on EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
