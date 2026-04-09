package vcfs.core;

import vcfs.models.enums.FairPhase;
import java.util.*;

public class SystemStatistics extends ViewModel {
    private static final long serialVersionUID = 1L;
    private static SystemStatistics instance;
    
    private int totalOrganizations;
    private int totalBooths;
    private int totalRecruiters;
    private int totalCandidates;
    private int totalOffers;
    private int totalBookings;
    private FairPhase currentPhase;
    private String systemStatus;
    private List<String> recentEvents;
    
    private SystemStatistics() {
        super();
        this.recentEvents = new ArrayList<>();
        this.systemStatus = "Initializing...";
        this.currentPhase = FairPhase.DORMANT;
    }

    /**
     * Public constructor for creating SystemStatistics with a specific CareerFair.
     * This is used for testing and creating independent statistics instances.
     * 
     * @param fair The CareerFair instance to track statistics for
     */
    public SystemStatistics(CareerFair fair) {
        super();
        this.recentEvents = new ArrayList<>();
        this.systemStatus = "Initialized for: " + (fair != null ? fair.name : "null");
        this.currentPhase = FairPhase.DORMANT;
        if (fair != null) {
            this.totalOrganizations = fair.organizations != null ? fair.organizations.size() : 0;
            this.totalBooths = 0;
            this.totalRecruiters = 0;
            this.totalCandidates = 0;
            this.totalOffers = 0;
            this.totalBookings = 0;
        }
    }
    
    public static synchronized SystemStatistics getInstance() {
        if (instance == null) {
            instance = new SystemStatistics();
        }
        return instance;
    }
    
    public void setTotalOrganizations(int count) {
        int old = this.totalOrganizations;
        this.totalOrganizations = count;
        firePropertyChange("totalOrganizations", old, count);
    }
    
    public void setTotalBooths(int count) {
        int old = this.totalBooths;
        this.totalBooths = count;
        firePropertyChange("totalBooths", old, count);
    }
    
    public void setTotalRecruiters(int count) {
        int old = this.totalRecruiters;
        this.totalRecruiters = count;
        firePropertyChange("totalRecruiters", old, count);
    }
    
    public void setTotalCandidates(int count) {
        int old = this.totalCandidates;
        this.totalCandidates = count;
        firePropertyChange("totalCandidates", old, count);
    }
    
    public void setTotalOffers(int count) {
        int old = this.totalOffers;
        this.totalOffers = count;
        firePropertyChange("totalOffers", old, count);
    }
    
    public void setTotalBookings(int count) {
        int old = this.totalBookings;
        this.totalBookings = count;
        firePropertyChange("totalBookings", old, count);
    }
    
    public void setCurrentPhase(FairPhase phase) {
        FairPhase old = this.currentPhase;
        this.currentPhase = phase;
        firePropertyChange("currentPhase", old, phase);
        addRecentEvent("Phase changed to: " + phase);
    }
    
    public void setSystemStatus(String status) {
        String old = this.systemStatus;
        this.systemStatus = status;
        firePropertyChange("systemStatus", old, status);
    }
    
    public void addRecentEvent(String event) {
        String timestampedEvent = String.format("[%s] %s", 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")),
            event);
        recentEvents.add(0, timestampedEvent);
        if (recentEvents.size() > 50) {
            recentEvents.subList(50, recentEvents.size()).clear();
        }
        firePropertyChange("recentEvents", null, recentEvents);
    }
    
    public void refreshFromSystem() {
        CareerFairSystem system = CareerFairSystem.getInstance();
        setTotalOrganizations(system.getFair().getOrganizations().size());
        int boothCount = 0;
        for (vcfs.models.structure.Organization org : system.getFair().getOrganizations()) {
            boothCount += org.getBooths().size();
        }
        setTotalBooths(boothCount);
        setTotalRecruiters(system.getAllRecruiters().size());
        setTotalCandidates(system.getAllCandidates().size());
        setTotalOffers(system.getAllOffers().size());
        int bookingCount = 0;
        for (vcfs.models.booking.Offer offer : system.getAllOffers()) {
            bookingCount += offer.getReservations().size();
        }
        setTotalBookings(bookingCount);
        setCurrentPhase(system.getCurrentPhase());
        addRecentEvent("Statistics refreshed");
    }
    
    public int getTotalOrganizations() { return totalOrganizations; }
    public int getTotalBooths() { return totalBooths; }
    public int getTotalRecruiters() { return totalRecruiters; }
    public int getTotalCandidates() { return totalCandidates; }
    public int getTotalOffers() { return totalOffers; }
    public int getTotalBookings() { return totalBookings; }
    public FairPhase getCurrentPhase() { return currentPhase; }
    public String getSystemStatus() { return systemStatus; }
    public List<String> getRecentEvents() { return new ArrayList<>(recentEvents); }
    
    /**
     * Refresh statistics from the system (convenience method for tests).
     */
    public void refresh() {
        refreshFromSystem();
    }
    
    /**
     * Get all statistics as a map (convenience method for tests).
     * @return Map of statistics with keys: totalOrganizations, totalBooths, etc.
     */
    public java.util.Map<String, Object> getStatisticsMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("totalOrganizations", totalOrganizations);
        map.put("totalBooths", totalBooths);
        map.put("totalRecruiters", totalRecruiters);
        map.put("totalCandidates", totalCandidates);
        map.put("totalOffers", totalOffers);
        map.put("totalBookings", totalBookings);
        map.put("currentPhase", currentPhase);
        map.put("systemStatus", systemStatus);
        map.put("recentEvents", new ArrayList<>(recentEvents));
        return map;
    }
}
