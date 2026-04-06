package vcfs.views.recruiter;

import javax.swing.*;

/**
 * Recruiter Dashboard - Main UI for recruiter operations.
 *
 * Original implementation by: Taha (CodeByTaha18)
 * Adapted to skeleton by: Zaid
 *
 * Responsibilities:
 * - Display tabs for recruiter functions: Publish Offer, Schedule, Virtual Room
 * - Coordinate with RecruiterController for business logic
 */
public class RecruiterScreen extends JFrame {

    public RecruiterScreen() {
        setTitle("VCFS - Recruiter Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // TODO (Taha): Create stub panels - functionality pending
        // tabs.add("Publish Offer", new PublishOfferPanel());
        // tabs.add("Schedule", new SchedulePanel());
        // tabs.add("Virtual Room", new VirtualRoomPanel());

        // Temporary placeholder
        tabs.add("Placeholder", new JLabel("Recruiter Dashboard - Panels Coming Soon"));

        add(tabs);
        setVisible(true);
    }
}
