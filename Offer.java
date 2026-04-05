package Recruitment;


import java.time.LocalTime;

public class Offer {
    private String name;
    private int duration;
    private int capacity;
    private String startTime; // Continuous block start
    private String endTime;   // Continuous block end

    public Offer(String name, int duration, int capacity, String startTime, String endTime) {
        this.name = name;
        this.duration = duration;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Zaid's engine will need these getters to "chop up" the time
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public int getDuration() { return duration; }

    @Override
    public String toString() {
        return name + " (" + duration + "m) | " + startTime + "-" + endTime + " | Cap: " + capacity;
    }
}