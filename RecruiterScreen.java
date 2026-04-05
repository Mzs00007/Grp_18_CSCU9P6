package Recruitment;

import javax.swing.*;

public class RecruiterScreen extends JFrame {

    public RecruiterScreen() {

        setTitle("Recruiter Dashboard");
        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        
        tabs.add("Publish Offer", new PublishOfferPanel());
        tabs.add("Schedule" , new SchedulePanel());
        tabs.add("Virtual Room", new VirtualRoomPanel());

        add(tabs);

        setVisible(true);
    }
}
