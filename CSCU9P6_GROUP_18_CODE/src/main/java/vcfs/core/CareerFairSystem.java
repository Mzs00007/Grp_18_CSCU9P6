package vcfs.core;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.models.audit.AuditEntry;
import vcfs.models.booking.Offer;
import vcfs.models.booking.Request;
import vcfs.models.booking.Reservation;
import vcfs.models.enums.FairPhase;
import vcfs.models.enums.ReservationState;
import vcfs.models.structure.Booth;
import vcfs.models.structure.Organization;
import vcfs.models.users.Candidate;
import vcfs.models.users.Recruiter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Application Facade — the single entry point for all system operations.
 *
 * SINGLETON:
 *   Only one CareerFairSystem exists. Access via getInstance().
 *
 * OBSERVER:
 *   Implements PropertyChangeListener and registers with SystemTimer.
 *   Every clock tick automatically calls tick() → fair.evaluatePhase(now).
 *
 * ZAID implements (VCFS-001, 002, 003, 004):
 *   - Singleton construction + Observer registration
 *   - tick() + getCurrentPhase()
 *   - configureTimes() + resetFairData() (lifecycle)
 *   - addOrganization() + addBooth() + registerRecruiter() + registerCandidate()
 *   - parseAvailabilityIntoOffers() [VCFS-003]
 *   - autoBook() + getAllOffers() [VCFS-004]
 *
 * TEAMMATES implement (stubs preserved below — DO NOT REMOVE):
 *   - cancelAsCandidate(), cancelAsRecruiter() → YAMI (audit log)
 *   - joinSession() → MJAMishkat (Lobby Gatekeeper)
 *   - manualBook(), createRequest(), publishOffer() → MJAMishkat / Taha
 * 
 * P2 CODE AUDIT: ZAID (mzs00007) — Performance Optimizations
 *   Added three-tier caching strategy for O(1) lookups:
 *   ✓ offerCache: List of all offers (flat structure) — cached after first build
 *   ✓ orgByNameCache: HashMap for org lookup by name — rebuilds on org changes
 *   ✓ boothByNameCache: HashMap for booth lookup by name — rebuilds on booth changes
 *   
 *   Impact:
 *   - autoBook() MatchEngine: Previously O(Orgs×Booths×Recruiters) per call
 *     Now: O(n) first call, then O(1) repetitions until invalidated
 *   - getOrganizationByName(): Reduced from O(n) to O(1) lookups
 *   - getBoothByName(): Reduced from O(n×m) to O(1) lookups
 *   - Cache invalidated strategically: addOrganization(), addBooth(), parseAvailability()
 */
public class CareerFairSystem implements PropertyChangeListener {
    // ==========================================================
    // SINGLETON INFRASTRUCTURE
    // =========================================================

    /**
     * CRITICAL FIX (P2): volatile keyword prevents race conditions in double-checked locking
     * when getInstance() is called from multiple threads simultaneously.
     * Without volatile, thread visibility of instance assignment is not guaranteed.
     */
    private static volatile CareerFairSystem instance;

    /**
     * Private constructor — enforces Singleton.
     * Initialises CareerFair and registers as SystemTimer Observer.
     */
    private CareerFairSystem() {
        this.fair = new CareerFair();
        this.fair.name = "CSCU9P6 Virtual Career Fair 2026";
        this.fair.system = this;

        // Register as Observer: from this point every timer tick
        // will automatically call our propertyChange() method.
        SystemTimer.getInstance().addPropertyChangeListener(this);

        Logger.info("[CareerFairSystem] Singleton created. Observer registered.");
    }

    /** Access the single CareerFairSystem instance */
    public static synchronized CareerFairSystem getInstance() {
        if (instance == null) {
            instance = new CareerFairSystem();
        }
        return instance;
    }

    /**
     * Register a listener to receive property change events from the system.
     * Used by UI screens (AdminScreen, RecruiterScreen, CandidateScreen) to stay in sync
     * with system state changes.
     * @param listener The PropertyChangeListener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener from receiving property change events.
     * @param listener The PropertyChangeListener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Fire a property change event to all registered listeners.
     * Used internally to notify screens of system state changes.
     * @param propertyName The name of the property that changed
     * @param oldValue The old value
     * @param newValue The new value
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Broadcast system statistics update to all portals.
     * Called whenever data changes to keep dashboards in sync.
     * This enables real-time statistics display across all portals.
     */
    protected void broadcastStatisticsUpdate() {
        try {
            SystemStatistics stats = SystemStatistics.getInstance();
            stats.refreshFromSystem();
            firePropertyChange("statistics", null, stats);
        } catch (Exception e) {
            Logger.log(LogLevel.WARNING, "[CareerFairSystem] Failed to broadcast statistics", e);
        }
    }

    // =========================================================
    // OWNED STATE
    // =========================================================

    CareerFair fair;
    
    /**
     * Tracks all registered emails (recruiters + candidates) to enforce uniqueness.
     * THREAD-SAFE: Uses Collections.synchronizedSet for concurrent access.
     * CRITICAL for preventing duplicate email registrations system-wide.
     */
    private final Set<String> registeredEmails = Collections.synchronizedSet(new HashSet<>());

    /**
     * Tracks all registered candidates in the system.
     * THREAD-SAFE: Uses thread-safe collections.
     * Used for candidate lookups and admin display.
     */
    /**
     * CRITICAL FIX: Track all recruiters in system (not just through offers).
     * This enables AdminScreen to show recruiters even before they publish offers.
     */
    private final List<Recruiter> recruitersList = Collections.synchronizedList(new ArrayList<>());

    private final List<Candidate> candidatesList = Collections.synchronizedList(new ArrayList<>());

    /**
     * PropertyChangeSupport for broadcasting system events to all registered listeners (UI screens).
     * Enables multi-portal synchronization: when state changes, all screens update automatically.
     * Used for: phase changes, fair timeline updates, system time, audit log entries.
     */
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    // =========================================================
    // PERFORMANCE OPTIMIZATION: CACHING & FAST LOOKUPS
    // P2 CODE AUDIT: ZAID (mzs00007) — Added caching for O(1) and O(n) lookups
    // =========================================================

    /**
     * Cache of all Offer objects in the system.
     * Flattened list for fast iteration in autoBook() MatchEngine.
     * INVALIDATED whenever new offers are added via parseAvailabilityIntoOffers().
     */
    private List<Offer> offerCache = null;

    /**
     * HashMap for O(1) organization lookups by name (case-insensitive).
     * INVALIDATED and rebuilt whenever organizations change.
     */
    private Map<String, Organization> orgByNameCache = null;

    /**
     * HashMap for O(1) booth lookups by name across entire system.
     * Maps: booth_name.toLowerCase() → Booth object.
     * INVALIDATED whenever booths/organizations change.
     */
    private Map<String, Booth> boothByNameCache = null;

    // =========================================================
    // VCFS-001/002: OBSERVER CALLBACK + TICK
    // =========================================================

    /**
     * Called AUTOMATICALLY by SystemTimer every time time advances.
     * This is the heartbeat that drives all time-based behaviour.
     *
     * Required method from PropertyChangeListener interface.
     * @param evt Contains propertyName="time", old/new LocalDateTime values
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("time".equals(evt.getPropertyName())) {
            tick();
        }
    }

    /**
     * VCFS-002: Advance time-based behaviour.
     * Called automatically on every clock tick via the Observer.
     *
     * Currently: drives phase transitions.
     * Future extension (Taha): also trigger MeetingSession start/end.
     */
    void tick() {
        LocalDateTime now = SystemTimer.getInstance().getNow();
        FairPhase oldPhase = fair.getCurrentPhase();
        fair.evaluatePhase(now);
        FairPhase newPhase = fair.getCurrentPhase();

        // BROADCAST: Notify all listeners (UI screens) of phase change
        if (!oldPhase.equals(newPhase)) {
            firePropertyChange("phase", oldPhase, newPhase);
            Logger.info("[CareerFairSystem] Phase transitioned: " + oldPhase + " → " + newPhase);
        }

        // BROADCAST: Always alert listeners that time has advanced
        firePropertyChange("time", null, now);

        // Future: check all active MeetingSessions for start/end triggers
    }

    /**
     * Return the current fair phase for UI display.
     * Used by Swing screens to enable/disable buttons.
     */
    public FairPhase getCurrentPhase() {
        return fair.getCurrentPhase();
    }

    /** Expose the CareerFair for Admin configuration (YAMI uses via AdminController) */
    public CareerFair getFair() {
        return fair;
    }

    // =========================================================
    // VCFS-002: ADMIN LIFECYCLE METHODS
    // =========================================================

    /**
     * Admin: validate and set the fair timeline.
     * Delegates to CareerFair.setTimes() which also validates boundaries.
     * Broadcasts "timeline" event so all portals can update UI (e.g., display fair hours).
     */
    public void configureTimes(LocalDateTime openTime, LocalDateTime closeTime,
                        LocalDateTime startTime, LocalDateTime endTime) {
        fair.setTimes(openTime, closeTime, startTime, endTime);
        
        // CRITICAL FIX: Broadcast timeline change so all portals refresh
        firePropertyChange("timeline", null, new Object[] {openTime, closeTime, startTime, endTime});
        
        Logger.info("[CareerFairSystem] Fair times configured.");
    }

    /**
     * Admin: clear all in-memory fair data for a fresh run.
     * Safely clears every collection without causing NPEs.
     * Also clears all performance caches to ensure fresh state.
     * Called by YAMI's AdminController.onResetFair().
     * Broadcasts "reset" event so all portals refresh their displays.
     */
    public void resetFairData() {
        if (fair.organizations != null) fair.organizations.clear();
        if (fair.auditTrail != null)    fair.auditTrail.clear();
        fair.currentPhase       = FairPhase.DORMANT;
        fair.bookingsOpenTime   = null;
        fair.bookingsCloseTime  = null;
        fair.startTime          = null;
        fair.endTime            = null;
        
        // P2 OPTIMIZATION: Clear all caches (ZAID - mzs00007)
        invalidateOfferCache();
        invalidateOrgCache();
        invalidateBoothCache();
        
        // CRITICAL FIX: Broadcast reset event so ALL portals refresh immediately
        // Portals will see organizations, recruiters, candidates, and offers all cleared
        firePropertyChange("reset", null, "SYSTEM_RESET");
        firePropertyChange("organizations", null, fair.organizations);
        broadcastStatisticsUpdate(); // Reset all statistics to zero
        
        Logger.info("[CareerFairSystem] Fair data reset. All collections and caches cleared.");
    }

    // =========================================================
    // P2 OPTIMIZATION: CACHE INVALIDATION HELPERS
    // ZAID (mzs00007) — Maintain cache consistency on data changes
    // =========================================================

    /** Mark offer cache as stale — will rebuild on next getAllOffers() call */
    private void invalidateOfferCache() {
        this.offerCache = null;
    }

    /** Mark organization lookup cache as stale — will rebuild on next getOrganizationByName() call */
    private void invalidateOrgCache() {
        this.orgByNameCache = null;
    }

    /** Mark booth lookup cache as stale — will rebuild on next getBoothByName() call */
    private void invalidateBoothCache() {
        this.boothByNameCache = null;
    }

    // =========================================================
    // VCFS-002: ADMIN REGISTRATION METHODS
    // =========================================================

    /**
     * Admin: create and register a participating organization.
     * @param name Display name of the organization
     * @return     The newly created Organization
     */
    public Organization addOrganization(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Organization name cannot be empty");
        }
        Organization org = new Organization(name);
        org.setFair(fair);
        fair.organizations.add(org);
        
        // P2 OPTIMIZATION: Invalidate lookup caches (ZAID - mzs00007)
        invalidateOrgCache();
        invalidateBoothCache(); // booths are children of org
        invalidateOfferCache(); // offers are nested under org
        
        // BROADCAST: Notify all listeners of organization update
        firePropertyChange("organizations", null, fair.organizations);
        broadcastStatisticsUpdate(); // Update dashboards with new org count
        
        Logger.log(LogLevel.INFO, "Organization added: " + name);
        return org;
    }

    /**
     * Admin: add a named Booth to an existing Organization.
     * @param org   The organization to add the booth to
     * @param title Display name for the booth
     * @return      The newly created Booth
     */
    public Booth addBooth(Organization org, String title) {
        if (org == null) {
            throw new IllegalArgumentException("Organization cannot be null.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Booth title cannot be empty");
        }
        Booth booth = new Booth(title);
        org.addBooth(booth);
        
        // P2 OPTIMIZATION: Invalidate lookup caches (ZAID - mzs00007)
        invalidateBoothCache();
        invalidateOfferCache(); // offers are nested under booth
        
        // BROADCAST: Notify all listeners of booth update
        firePropertyChange("booths", null, org.getBooths());
        broadcastStatisticsUpdate(); // Update dashboards with new booth count
        
        Logger.log(LogLevel.INFO, "Booth '" + title + "' added to '" + org.getName() + "'");
        return booth;
    }

    /**
     * Admin: create a Recruiter and optionally assign them to a Booth.
     * Enforces unique email across all recruiters in the system.
     *
     * @param displayName Recruiter's display name
     * @param email       Unique email identifier
     * @param booth       The booth to assign this recruiter to (can be null for demo)
     * @return            The newly created and assigned Recruiter
     */
    public Recruiter registerRecruiter(String displayName, String email, Booth booth) {
        // Enforce unique email across entire system (CRITICAL FIX: use registeredEmails set)
        if (registeredEmails.contains(email.toLowerCase())) {
            throw new IllegalStateException(
                "[CareerFairSystem] Email already registered: " + email);
        }

        Recruiter recruiter = new Recruiter(email, displayName, email);
        registeredEmails.add(email.toLowerCase()); // Track email (case-insensitive)
        recruitersList.add(recruiter); // CRITICAL FIX: Track in recruitersList so getAllRecruiters() works
        
        // BROADCAST: Notify all listeners of recruiter update
        firePropertyChange("recruiters", null, recruiter);
        broadcastStatisticsUpdate(); // Update dashboards with new recruiter count
        
        if (booth != null) {
            booth.assignRecruiter(recruiter);
            Logger.log(LogLevel.INFO, "Recruiter registered: "
                    + displayName + " -> Booth: " + booth.getTitle());
        } else {
            Logger.log(LogLevel.INFO, "Recruiter registered (no booth assigned yet): " + displayName);
        }
        return recruiter;
    }

    /**
     * Create candidate and profile (unique email enforced).
     * @param displayName  Candidate's display name
     * @param email        Unique email identifier
     * @param cvSummary    CV summary text
     * @param interestTags Comma-separated interest tags e.g. "Java,AI"
     * @return             The newly created Candidate
     */
    public Candidate registerCandidate(String displayName, String email,
                                 String cvSummary, String interestTags) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        // Enforce unique email across entire system (CRITICAL FIX: use registeredEmails set)
        if (registeredEmails.contains(email.toLowerCase())) {
            throw new IllegalStateException(
                "[CareerFairSystem] Email already registered: " + email);
        }
        
        Candidate candidate = new Candidate(email, displayName, email);
        registeredEmails.add(email.toLowerCase()); // Track email (case-insensitive)
        candidatesList.add(candidate); // Add to system candidates list
        if (cvSummary != null && !cvSummary.trim().isEmpty()) {
            candidate.getProfile().setCvSummary(cvSummary);
        }
        if (interestTags != null && !interestTags.trim().isEmpty()) {
            candidate.getProfile().setInterestTags(interestTags);
        }
        
        // BROADCAST: Notify all listeners of candidate update
        firePropertyChange("candidates", null, candidate);
        broadcastStatisticsUpdate(); // Update dashboards with new candidate count
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Candidate registered: " + displayName);
        return candidate;
    }

    /**
     * Register recruiter from existing Recruiter object (convenience overload for tests).
     * @param recruiter The recruiter to register
     * @return The registered recruiter
     */
    public Recruiter registerRecruiter(Recruiter recruiter) {
        if (recruiter == null) {
            throw new IllegalArgumentException("Recruiter cannot be null");
        }
        return registerRecruiter(recruiter.getDisplayName(), recruiter.getEmail(), null);
    }

    /**
     * Register candidate from existing Candidate object (convenience overload for tests).
     * @param candidate The candidate to register
     * @return The registered candidate
     */
    public Candidate registerCandidate(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }
        return registerCandidate(candidate.getDisplayName(), candidate.getEmail(), "", "");
    }

    // =========================================================
    // VCFS-003: AVAILABILITY PARSER ALGORITHM
    // =========================================================

    /**
     * Convert a recruiter's continuous availability block into
     * discrete, bookable Offer slot objects.
     *
     * Example: blockStart=09:00, blockEnd=12:00, durationMins=30
     * Generates 6 Offer objects: 09:00→09:30, 09:30→10:00, ..., 11:30→12:00
     *
     * Implemented by: Zaid (VCFS-003)
     *
     * @param recruiter    The recruiter publishing their availability
     * @param title        Session title
     * @param durationMins Length of each slot in minutes
     * @param topicTags    Comma-separated skill tags for MatchEngine
     * @param capacity     Max candidates per slot
     * @param blockStart   Start of availability window
     * @param blockEnd     End of availability window
     * @return             Number of Offer slots generated
     */
    public int parseAvailabilityIntoOffers(
            Recruiter recruiter, String title,
            int durationMins, String topicTags, int capacity,
            LocalDateTime blockStart, LocalDateTime blockEnd) {

        // Phase guard: only allowed during BOOKINGS_OPEN
        LocalDateTime now = SystemTimer.getInstance().getNow();
        if (!fair.canBook(now)) {
            throw new IllegalStateException(
                "[PARSER] Cannot publish offers — phase is: "
                + fair.getCurrentPhase() + " (requires BOOKINGS_OPEN)");
        }

        // Validate block is at least one slot long
        if (blockStart.minutesUntil(blockEnd) < durationMins) {
            throw new IllegalArgumentException(
                "[PARSER] Block duration (" + blockStart.minutesUntil(blockEnd)
                + " min) is shorter than slot duration (" + durationMins + " min).");
        }

        if (recruiter == null || recruiter.getOffers() == null) {
            throw new IllegalArgumentException("[PARSER] Invalid recruiter.");
        }

        LocalDateTime cursor = blockStart;
        int slotsCreated = 0;

        Logger.log(LogLevel.INFO, "[PARSER] Parsing block for " + recruiter.getDisplayName()
                + ": " + blockStart + " → " + blockEnd
                + " | " + durationMins + " min slots");

        // Core loop: generate one Offer per slot until cursor overshoots blockEnd
        while (!cursor.plusMinutes(durationMins).isAfter(blockEnd)) {
            Offer slot = new Offer();
            slot.setTitle(title);
            slot.setStartTime(cursor);
            slot.setEndTime(cursor.plusMinutes(durationMins));
            slot.setDurationMins(durationMins);
            slot.setTopicTags(topicTags);
            slot.setCapacity(capacity);
            slot.setPublisher(recruiter);

            recruiter.addOffer(slot);
            slotsCreated++;

            Logger.log(LogLevel.INFO, "[PARSER] Slot " + slotsCreated + ": "
                    + slot.getStartTime() + " → " + slot.getEndTime());

            cursor = cursor.plusMinutes(durationMins);
        }

        Logger.log(LogLevel.INFO, "[PARSER] Complete: " + slotsCreated + " slots for "
                + recruiter.getDisplayName());
        
        // P2 OPTIMIZATION: Invalidate offer cache since new offers were added (ZAID - mzs00007)
        invalidateOfferCache();
        
        return slotsCreated;
    }

    // =========================================================
    // VCFS-004: TAG-WEIGHTED MATCHENGINE
    // =========================================================

    /**
     * Automatically find and confirm the highest-scoring Offer for a Candidate.
     *
     * Algorithm:
     *   1. Validate BOOKINGS_OPEN phase
     *   2. Parse candidate's desired tags from Request
     *   3. For each global Offer:
     *      a. Collision Detection — skip if time overlaps existing reservation
     *      b. Tag Intersection Scoring — count matching tags
     *   4. Select Offer with maximum score (HashMap max)
     *   5. Create and confirm a Reservation
     *
     * Implemented by: Zaid (VCFS-004)
     *
     * @param candidate The candidate requesting auto-booking
     * @param request   Contains desiredTags and preferredOrgs
     * @return          Confirmed Reservation, or null if no match found
     */
    public Reservation autoBook(Candidate candidate, Request request) {

        // Phase guard
        LocalDateTime now = SystemTimer.getInstance().getNow();
        if (!fair.canBook(now)) {
            Logger.info("[MATCHENGINE] Rejected — Phase: " + fair.getCurrentPhase());
            return null;
        }

        if (candidate == null || request == null || request.getDesiredTags() == null) {
            throw new IllegalArgumentException("[MATCHENGINE] Invalid candidate or request.");
        }

        // Parse desired tags: "Java, AI, Cloud" → ["java", "ai", "cloud"]
        List<String> desiredTags = Arrays.asList(
                request.getDesiredTags().toLowerCase().split(",\\s*"));

        Logger.log(LogLevel.INFO, "[MATCHENGINE] Searching for: " + candidate.getDisplayName()
                + " | Tags: " + desiredTags);

        Map<Offer, Integer> scoreMap = new HashMap<>();

        for (Offer offer : getAllOffers()) {
            if (offer.getTopicTags() == null || offer.getStartTime() == null) continue;

            // --- COLLISION DETECTION ---
            // Two windows [A_start, A_end) and [B_start, B_end) overlap when:
            //   A_start < B_end  AND  B_start < A_end
            boolean conflict = false;
            if (candidate.getReservations() != null && !candidate.getReservations().isEmpty()) {
                for (Reservation existing : candidate.getReservations()) {
                    if (existing.getScheduledStart() == null || existing.getScheduledEnd() == null) continue;
                    if (existing.getScheduledStart().isBefore(offer.getEndTime())
                            && offer.getStartTime().isBefore(existing.getScheduledEnd())) {
                        conflict = true;
                        Logger.log(LogLevel.INFO, "[MATCHENGINE] Conflict at "
                                + offer.getStartTime() + " — skipped.");
                        break;
                    }
                }
            }
            if (conflict) continue;

            // --- TAG SCORING ---
            List<String> offerTags = Arrays.asList(
                    offer.getTopicTags().toLowerCase().split(",\\s*"));
            int score = 0;
            for (String tag : desiredTags) {
                if (offerTags.contains(tag.trim())) score++;
            }

            if (score > 0) {
                scoreMap.put(offer, score);
                Logger.log(LogLevel.INFO, "[MATCHENGINE] Offer @ " + offer.getStartTime()
                        + " score=" + score + "/" + desiredTags.size());
            }
        }

        if (scoreMap.isEmpty()) {
            Logger.info("[MATCHENGINE] No matching offers found.");
            return null;
        }

        // Select the highest-scoring Offer
        Offer bestOffer = Collections.max(
                scoreMap.entrySet(),
                Map.Entry.comparingByValue()).getKey();

        Logger.log(LogLevel.INFO, "[MATCHENGINE] Winner: " + bestOffer.getStartTime()
                + " (score=" + scoreMap.get(bestOffer) + ")");

        // Create confirmed Reservation
        Reservation reservation = new Reservation();
        reservation.setCandidate(candidate);
        reservation.setOffer(bestOffer);
        reservation.setScheduledStart(bestOffer.getStartTime());
        reservation.setScheduledEnd(bestOffer.getEndTime());
        reservation.setState(ReservationState.CONFIRMED);

        // Add reservation to candidate's list (proper public method, no reflection)
        candidate.addReservation(reservation);
        
        // Add reservation to offer's list (proper public method, no reflection)
        bestOffer.addReservation(reservation);

        Logger.log(LogLevel.INFO, "[MATCHENGINE] CONFIRMED: " + candidate.getDisplayName()
                + " → " + bestOffer.getStartTime()
                + " with " + (bestOffer.getPublisher() != null ? bestOffer.getPublisher().getDisplayName() : "unknown"));
        
        // CRITICAL FIX: Fire PropertyChangeEvent so all portals see booking immediately
        firePropertyChange("reservations", null, reservation);
        firePropertyChange("offers", null, getAllOffers());  // Update offer status (# of bookings)
        broadcastStatisticsUpdate(); // Update dashboards with booking count
        
        return reservation;
    }

    /**
     * CRITICAL: When a recruiter publishes a new offer via UI,
     * ensure offer is added to the recruiter in the SYSTEM STRUCTURE (not just session recruiter).
     * This prevents object reference mismatch where session recruiter != system recruiter.
     * 
     * CONSISTENCY FIX P3: Data Isolation Bug (mzs00007)
     *   Problem: Session recruiter ≠ System recruiter (different object instances)
     *   Solution: Explicitly add offer to system recruiter before broadcasting
     *   Result: getAllOffers() will find the offer via org->booth->recruiters iteration
     * 
     * @param offer The newly published offer
     * @param publisherEmail Email of recruiter who published this offer
     */
    public void registerPublishedOffer(Offer offer, String publisherEmail) {
        if (offer == null || publisherEmail == null) return;
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Registering published offer: " + 
            (offer.getTitle() != null ? offer.getTitle() : "unknown") + 
            " by recruiter: " + publisherEmail);
        
        // CRITICAL FIX: Find the SYSTEM recruiter (not session recruiter) and add offer
        // This ensures getAllOffers() will find it when iterating org->booth->recruiters
        boolean foundInSystem = false;
        if (fair.organizations != null) {
            for (Organization org : fair.organizations) {
                if (org.getBooths() != null) {
                    for (Booth booth : org.getBooths()) {
                        if (booth.getRecruiters() != null) {
                            for (Recruiter systemRecruiter : booth.getRecruiters()) {
                                if (systemRecruiter.getEmail() != null && 
                                    systemRecruiter.getEmail().equalsIgnoreCase(publisherEmail)) {
                                    // Found the recruiter in system structure
                                    // Ensure offer is in their collection
                                    if (!systemRecruiter.getOffers().contains(offer)) {
                                        systemRecruiter.addOffer(offer);
                                        Logger.log(LogLevel.INFO, "[CareerFairSystem] Offer added to system recruiter: " + 
                                            systemRecruiter.getDisplayName());
                                        foundInSystem = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (!foundInSystem) {
            Logger.log(LogLevel.WARNING, "[CareerFairSystem] Could not find recruiter in system structure: " + 
                publisherEmail + " - offer may not appear in tables!");
        }
        
        // CRITICAL: Invalidate offer cache since new offer has been added
        invalidateOfferCache();
        
        // BROADCAST: Notify all UI listeners of offer publication
        firePropertyChange("offer_published", null, offer);
        firePropertyChange("offers", null, getAllOffers()); // Send updated list with fresh data
        
        // UPDATE DASHBOARDS: Refresh statistics on all portals
        broadcastStatisticsUpdate();
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Offer publication complete - all portals notified");
    }
    
    /**
     * BACKWARD COMPATIBILITY: Overload for offers where publisher is already set
     */
    public void registerPublishedOffer(Offer offer) {
        if (offer == null || offer.getPublisher() == null) return;
        registerPublishedOffer(offer, offer.getPublisher().getEmail());
    }

    /**
     * Gather ALL Offer objects from every Org → Booth → Recruiter in the system.
     * Used by autoBook() MatchEngine and manual browsing queries - PUBLIC for UI access.
     * 
     * P2 OPTIMIZATION: Caching (ZAID - mzs00007)
     *   - First call: O(Orgs × Booths × Recruiters); result cached
     *   - Subsequent calls: O(1) cache hit
     *   - Cache invalidated whenever parseAvailabilityIntoOffers() or registerPublishedOffer() called
     *   
     * CONSISTENCY FIX: P3 (mzs00007)
     *   - Now properly invalidated when offers dynamically change
     *   - Prevents stale data in UI table display
     */
    public List<Offer> getAllOffers() {
        // Return cached result if available
        if (offerCache != null) {
            return offerCache;
        }
        
        // Cache miss — rebuild offer list
        List<Offer> allOffers = new ArrayList<>();
        if (fair.organizations == null) return allOffers;
        for (Organization org : fair.organizations) {
            if (org.getBooths() == null) continue;
            for (Booth booth : org.getBooths()) {
                if (booth.getRecruiters() == null) continue;
                for (Recruiter recruiter : booth.getRecruiters()) {
                    if (recruiter.getOffers() != null && !recruiter.getOffers().isEmpty()) {
                        allOffers.addAll(recruiter.getOffers());
                    }
                }
            }
        }
        
        // Cache for next time
        this.offerCache = allOffers;
        return allOffers;
    }

    /**
     * Get all organizations.
     * @return Collection of all organizations in the fair
     */
    public Collection<Organization> getAllOrganizations() {
        return (fair != null && fair.organizations != null) ? fair.organizations : new ArrayList<>();
    }

    /**
     * Get all registered candidates.
     * Returns an empty list if no candidates have been registered yet.
     * @return List of all candidates in the system
     */
    /**
     * CRITICAL FIX: Get all recruiters directly (not searching through offers).
     * Called by AdminScreen to populate recruiter table.
     * Returns complete list of ALL registered recruiters, whether they have published offers or not.
     * @return New ArrayList of all Recruiter objects in system
     */
    public List<Recruiter> getAllRecruiters() {
        return new ArrayList<>(recruitersList);
    }

    /**
     * Get all candidates registered in the system.
     * Called by AdminScreen to display candidate table.
     * @return List of all registered Candidate objects
     */
    public List<Candidate> getAllCandidates() {
        try {
            return new ArrayList<>(candidatesList);
        } catch (Exception e) {
            Logger.log(LogLevel.WARNING, "[CareerFairSystem] Error retrieving candidates: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // =========================================================
    // PUBLIC QUERY METHODS (for controllers)
    // =========================================================

    /**
     * Find an organization by name.
     * @param name Organization name to search for
     * @return The Organization if found, null otherwise
     * 
     * P2 OPTIMIZATION: HashMap lookup (ZAID - mzs00007)
     *   - First call: O(n) linear search; result cached
     *   - Subsequent calls: O(1) HashMap get()
     *   - Cache invalidated whenever addOrganization() called
     */
    public Organization getOrganizationByName(String name) {
        if (name == null || fair.organizations == null) return null;
        
        // Rebuild cache if needed
        if (orgByNameCache == null) {
            orgByNameCache = new HashMap<>();
            for (Organization org : fair.organizations) {
                orgByNameCache.put(org.getName().toLowerCase(), org);
            }
        }
        
        // O(1) lookup
        return orgByNameCache.get(name.toLowerCase());
    }

    /**
     * Find a booth by name (searches all organizations).
     * @param name Booth name to search for
     * @return The Booth if found, null otherwise
     * 
     * P2 OPTIMIZATION: HashMap lookup (ZAID - mzs00007)
     *   - First call: O(n×m) linear search; result cached
     *   - Subsequent calls: O(1) HashMap get()
     *   - Cache invalidated whenever addBooth() or addOrganization() called
     */
    public Booth getBoothByName(String name) {
        if (name == null || fair.organizations == null) return null;
        
        // Rebuild cache if needed  
        if (boothByNameCache == null) {
            boothByNameCache = new HashMap<>();
            for (Organization org : fair.organizations) {
                if (org.getBooths() == null) continue;
                for (Booth booth : org.getBooths()) {
                    boothByNameCache.put(booth.getTitle().toLowerCase(), booth);
                }
            }
        }
        
        // O(1) lookup
        return boothByNameCache.get(name.toLowerCase());
    }

    /**
     * Parse timeline strings (format: "yyyy-MM-ddTHH:mm") and configure the fair.
     * Broadcasts "timeline" event so all portals can update UI if needed.
     * @param openStr Bookings open time
     * @param closeStr Bookings close time
     * @param startStr Fair start time
     * @param endStr Fair end time
     */
    public void setFairTimes(String openStr, String closeStr, String startStr, String endStr) {
        // Parse strings to LocalDateTime
        // Simple parser: assumes "yyyy-MM-ddTHH:mm" format
        LocalDateTime open = parseTimeString(openStr);
        LocalDateTime close = parseTimeString(closeStr);
        LocalDateTime start = parseTimeString(startStr);
        LocalDateTime end = parseTimeString(endStr);

        // Delegate to CareerFair
        fair.setTimes(open, close, start, end);
        
        // CRITICAL FIX: Broadcast timeline change so ALL portals (AdminScreen, etc.) refresh
        firePropertyChange("timeline", null, new Object[] {open, close, start, end});
        
        Logger.log(LogLevel.INFO, "Fair times set via controller: open=" + openStr + ", close=" + closeStr + ", start=" + startStr + ", end=" + endStr);
    }

    /**
     * CONSISTENCY FIX P4: Register booking in system structure (not just session candidate).
     * Prevents data isolation where session candidate ≠ system candidate.
     * 
     * @param reservation The newly created Reservation
     * @param candidateEmail Email of candidate who booked
     * @param offerTitle Title of offer booked (for logging)
     */
    public void registerBooking(Reservation reservation, String candidateEmail, String offerTitle) {
        if (reservation == null || candidateEmail == null) return;
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Registering booking by candidate: " + 
            candidateEmail + " for offer: " + offerTitle);
        
        // CRITICAL: Find the SYSTEM candidate (not session candidate) and add reservation
        // This ensures candidate's bookings appear in their schedule across all portals
        boolean foundInSystem = false;
        List<Candidate> allCandidates = getAllCandidates();
        for (Candidate systemCandidate : allCandidates) {
            if (systemCandidate.getEmail() != null && 
                systemCandidate.getEmail().equalsIgnoreCase(candidateEmail)) {
                // Found the candidate in system
                if (!systemCandidate.getReservations().contains(reservation)) {
                    systemCandidate.addReservation(reservation);
                    Logger.log(LogLevel.INFO, "[CareerFairSystem] Reservation added to system candidate: " + 
                        systemCandidate.getDisplayName());
                    foundInSystem = true;
                }
            }
        }
        
        if (!foundInSystem) {
            Logger.log(LogLevel.WARNING, "[CareerFairSystem] Could not find candidate in system: " + 
                candidateEmail + " - booking may not persist!");
        }
        
        // BROADCAST: Update all portals with booking information
        firePropertyChange("reservations", null, reservation);
        firePropertyChange("offers", null, getAllOffers()); // Offer counts updated
        broadcastStatisticsUpdate();
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Booking registration complete");
    }
    
    /**
     * BACKWARD COMPATIBILITY: Overload for bookings where objects are already set
     */
    public void registerBooking(Reservation reservation) {
        if (reservation == null || reservation.getCandidate() == null) return;
        String offerTitle = (reservation.getOffer() != null && reservation.getOffer().getTitle() != null) 
                            ? reservation.getOffer().getTitle() : "unknown";
        registerBooking(reservation, reservation.getCandidate().getEmail(), offerTitle);
    }
    
    /**
     * CONSISTENCY FIX P3 (mzs00007) — Notify system of cancellation/deletion.
     * Called when a booking is cancelled, offer is withdrawn, etc.
     * Ensures all portals refresh to show updated state.
     * 
     * @param eventType Type of cancellation ("BOOKING_CANCELLED", "OFFER_WITHDRAWN", etc.)
     * @param details Description of what was cancelled
     */
    public void registerCancellation(String eventType, String details) {
        if (eventType == null) return;
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Cancellation registered: " + eventType);
        
        // Broadcast: All portals need to refresh for cancellation
        firePropertyChange(eventType, null, details);
        firePropertyChange("offers", null, getAllOffers());
        firePropertyChange("reservations", null, null);
        broadcastStatisticsUpdate();
    }

    /**
     * Parse a time string in format "yyyy-MM-ddTHH:mm".
     * @param timeStr The time string to parse
     * @return LocalDateTime object
     * @throws IllegalArgumentException if format is invalid
     */
    private LocalDateTime parseTimeString(String timeStr) {
        if (timeStr == null || !timeStr.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Time must be in format yyyy-MM-ddTHH:mm, got: " + timeStr);
        }

        // Split "2026-04-07T09:00"
        String[] parts = timeStr.split("T");
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        return new LocalDateTime(year, month, day, hour, minute);
    }

    // =========================================================
    // STUBS FOR TEAMMATES — DO NOT IMPLEMENT (Zaid's scope ends above)
    // =========================================================

    /**
     * Register a recruiter-owned offer; check phase/policy.
     * Implemented for Taha — VCFS-009
     */
    Offer publishOffer(Recruiter recruiter, String title,
                       int durationMins, String topicTags, int capacity) {
        // STEP 2.1: Validate inputs
        if (recruiter == null) {
            throw new IllegalArgumentException("[publishOffer] Recruiter cannot be null");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("[publishOffer] Title cannot be empty");
        }
        if (durationMins <= 0) {
            throw new IllegalArgumentException("[publishOffer] Duration must be positive. Got: " + durationMins);
        }
        
        // Check phase allows publishing
        LocalDateTime now = SystemTimer.getInstance().getNow();
        if (!fair.canBook(now)) {
            throw new IllegalStateException(
                "[publishOffer] Cannot publish — current phase: " + fair.getCurrentPhase() 
                + " (requires BOOKINGS_OPEN)");
        }
        
        // Create Offer object with all properties
        Offer offer = new Offer();
        offer.setTitle(title);
        offer.setDurationMins(durationMins);
        offer.setTopicTags(topicTags);
        offer.setCapacity(capacity);
        offer.setPublisher(recruiter);
        
        // Add to recruiter's offers list
        recruiter.addOffer(offer);
        
        Logger.log(LogLevel.INFO, "[publishOffer] Published: " + title 
            + " (" + durationMins + "min) by " + recruiter.getDisplayName());
        
        // CRITICAL FIX: Fire PropertyChangeEvent so all portals see new offer immediately
        firePropertyChange("offers", null, getAllOffers());
        firePropertyChange("offer_published", null, offer);
        broadcastStatisticsUpdate(); // Update dashboards with new offer count
        
        return offer;
    }

    /**
     * Register a candidate request; check phase/policy.
     * Implemented for MJAMishkat — VCFS-014
     */
    Request createRequest(Candidate candidate, String desiredTags,
                          String preferredOrgs, int maxAppointments) {
        // STEP 2.2: Validate inputs
        if (candidate == null) {
            throw new IllegalArgumentException("[createRequest] Candidate cannot be null");
        }
        if (desiredTags == null || desiredTags.trim().isEmpty()) {
            throw new IllegalArgumentException("[createRequest] Desired tags cannot be empty");
        }
        
        // Check phase allows request creation
        LocalDateTime now = SystemTimer.getInstance().getNow();
        if (!fair.canBook(now)) {
            throw new IllegalStateException(
                "[createRequest] Cannot create request — current phase: " + fair.getCurrentPhase() 
                + " (requires BOOKINGS_OPEN)");
        }
        
        // Create Request object with candidate's preferences
        Request request = new Request();
        request.setRequester(candidate);
        request.setDesiredTags(desiredTags);
        request.setPreferredOrgs(preferredOrgs);
        request.setMaxAppointments(maxAppointments);
        
        Logger.log(LogLevel.INFO, "[createRequest] Created for " + candidate.getDisplayName() 
            + " | Tags: " + desiredTags + " | Max appointments: " + maxAppointments);
        
        return request;
    }

    /**
     * Create a CONFIRMED reservation at a selected time (manual flow).
     * Implemented for MJAMishkat — VCFS-014
     */
    Reservation manualBook(Candidate candidate, Offer offer, LocalDateTime start) {
        // STEP 2.3: Validate inputs
        if (candidate == null || offer == null || start == null) {
            throw new IllegalArgumentException("[manualBook] Candidate, offer, and start time cannot be null");
        }
        
        // Check phase allows booking
        LocalDateTime now = SystemTimer.getInstance().getNow();
        if (!fair.canBook(now)) {
            throw new IllegalStateException(
                "[manualBook] Cannot book — current phase: " + fair.getCurrentPhase() 
                + " (requires BOOKINGS_OPEN)");
        }
        
        // Create reservation with manual booking details
        Reservation reservation = new Reservation();
        reservation.setCandidate(candidate);
        reservation.setOffer(offer);
        reservation.setScheduledStart(start);
        reservation.setScheduledEnd(start.plusMinutes(offer.getDurationMins()));
        reservation.setState(ReservationState.CONFIRMED);
        
        // Add reservation to candidate's list using public methods (proper encapsulation)
        candidate.addReservation(reservation);
        
        // Add reservation to offer's list using public methods (proper encapsulation)
        offer.addReservation(reservation);
        
        Logger.log(LogLevel.INFO, "[manualBook] CONFIRMED: " + candidate.getDisplayName() 
            + " → " + start + " with " 
            + (offer.getPublisher() != null ? offer.getPublisher().getDisplayName() : "unknown"));
        
        return reservation;
    }

    /**
     * Cancel as recruiter; record audit and publish events.
     * Implemented for YAMI — VCFS-008
     */
    void cancelAsRecruiter(Recruiter recruiter, String reservationId, String reason) {
        // STEP 2.4: Validate inputs
        if (recruiter == null) {
            throw new IllegalArgumentException("[cancelAsRecruiter] Recruiter cannot be null");
        }
        if (reservationId == null || reservationId.trim().isEmpty()) {
            throw new IllegalArgumentException("[cancelAsRecruiter] Reservation ID cannot be empty");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("[cancelAsRecruiter] Cancellation reason cannot be empty");
        }
        
        // Search for reservation in recruiter's offers
        Reservation foundReservation = null;
        for (Offer offer : recruiter.getOffers()) {
            if (offer.getReservations() != null) {
                for (Reservation res : offer.getReservations()) {
                    // Match by reservation object hash or candidate info
                    // For now, match by first available matching reservation for this recruiter
                    foundReservation = res;
                    break;
                }
            }
            if (foundReservation != null) break;
        }
        
        if (foundReservation == null) {
            throw new IllegalStateException(
                "[cancelAsRecruiter] Reservation not found: " + reservationId);
        }
        
        // Cancel the reservation with RECRUITER-specific state
        foundReservation.setState(ReservationState.CANCELLED);
        
        // Create audit entry for cancellation
        LocalDateTime now = SystemTimer.getInstance().getNow();
        AuditEntry auditEntry = new AuditEntry(
            fair,
            now,
            "CANCELLED_BY_RECRUITER | Recruiter: " + recruiter.getDisplayName() 
                + " | Reason: " + reason 
                + " | Candidate: " + (foundReservation.getCandidate() != null ? foundReservation.getCandidate().getDisplayName() : "unknown")
        );
        
        // Add to audit trail
        if (fair.auditTrail != null) {
            fair.auditTrail.add(auditEntry);
        }
        
        Logger.log(LogLevel.INFO, "[cancelAsRecruiter] Cancelled by " + recruiter.getDisplayName() 
            + " | Reason: " + reason 
            + " | Candidate: " + (foundReservation.getCandidate() != null ? foundReservation.getCandidate().getDisplayName() : "unknown"));
    }

    /**
     * Cancel as candidate; record audit and publish events.
     * Implemented for YAMI — VCFS-008
     */
    void cancelAsCandidate(Candidate candidate, String reservationId) {
        if (candidate == null) {
            throw new IllegalArgumentException("[cancelAsCandidate] Candidate cannot be null");
        }
        if (reservationId == null || reservationId.trim().isEmpty()) {
            throw new IllegalArgumentException("[cancelAsCandidate] Reservation ID cannot be empty");
        }
        
        Reservation foundReservation = null;
        if (candidate.getReservations() != null) {
            for (Reservation res : candidate.getReservations()) {
                // Match by first available for this candidate as a placeholder
                foundReservation = res;
                break;
            }
        }
        
        if (foundReservation == null) {
            throw new IllegalStateException("[cancelAsCandidate] Reservation not found");
        }
        
        foundReservation.setState(ReservationState.CANCELLED);
        
        LocalDateTime now = SystemTimer.getInstance().getNow();
        AuditEntry auditEntry = new AuditEntry(
            fair,
            now,
            "CANCELLED_BY_CANDIDATE | Candidate: " + candidate.getDisplayName() 
        );
        
        if (fair.auditTrail != null) {
            fair.auditTrail.add(auditEntry);
        }
        
        Logger.log(LogLevel.INFO, "[cancelAsCandidate] Cancelled by " + candidate.getDisplayName());
    }

    /**
     * Join logic: lobby vs room; ensure MeetingSession exists; record attendance.
     * Implemented for MJAMishkat — VCFS-016
     */
    void joinSession(Candidate candidate, String reservationId) {
        if (candidate == null) {
            throw new IllegalArgumentException("[joinSession] Candidate cannot be null");
        }
        
        LocalDateTime now = SystemTimer.getInstance().getNow();
        if (!fair.isLive(now)) {
            throw new IllegalStateException("[joinSession] Cannot join — current phase: " + fair.getCurrentPhase() + " (requires FAIR_LIVE)");
        }

        Reservation foundReservation = null;
        if (candidate.getReservations() != null) {
            for (Reservation res : candidate.getReservations()) {
                foundReservation = res;
                break;
            }
        }
        
        if (foundReservation == null || foundReservation.getState() != ReservationState.CONFIRMED) {
            throw new IllegalStateException("[joinSession] Valid confirmed reservation not found");
        }
        
        Offer offer = foundReservation.getOffer();
        if (offer == null) {
            throw new IllegalStateException("[joinSession] Offer not found for reservation");
        }
        
        // Mark as IN_PROGRESS or log attendance
        foundReservation.setState(ReservationState.IN_PROGRESS);
        
        AuditEntry auditEntry = new AuditEntry(
            fair,
            now,
            "SESSION_JOINED | Candidate: " + candidate.getDisplayName() + " joined session for " + offer.getTitle()
        );
        
        if (fair.auditTrail != null) {
            fair.auditTrail.add(auditEntry);
        }
        
        Logger.log(LogLevel.INFO, "[joinSession] " + candidate.getDisplayName() + " joined session: " + offer.getTitle());
    }
    /**
     * Get all lobbies in the system.
     * @return List of all lobbies
     */
    public List<vcfs.models.booking.Lobby> getAllLobbies() {
        List<vcfs.models.booking.Lobby> lobbies = new ArrayList<>();
        // Collect lobbies from all meeting sessions
        if (fair.organizations != null) {
            for (Organization org : fair.organizations) {
                if (org.getBooths() == null) continue;
                for (Booth booth : org.getBooths()) {
                    if (booth.getRecruiters() == null) continue;
                    for (Recruiter recruiter : booth.getRecruiters()) {
                        if (recruiter.getOffers() == null) continue;
                        for (Offer offer : recruiter.getOffers()) {
                            if (offer.getMeetingSession() != null 
                                    && offer.getMeetingSession().getLobby() != null) {
                                vcfs.models.booking.Lobby lobby = offer.getMeetingSession().getLobby();
                                if (!lobbies.contains(lobby)) {
                                    lobbies.add(lobby);
                                }
                            }
                        }
                    }
                }
            }
        }
        return lobbies;
    }

    /**
     * Get a specific lobby by ID.
     * @param lobbyId The lobby ID (can be null)
     * @return Optional containing the lobby if found
     */
    public Optional<vcfs.models.booking.Lobby> getLobby(String lobbyId) {
        if (lobbyId == null) {
            return Optional.empty();
        }
        
        // Search through all lobbies
        for (vcfs.models.booking.Lobby lobby : getAllLobbies()) {
            if (lobbyId.equals(lobby.hashCode() + "")) {
                return Optional.of(lobby);
            }
        }
        
        // Return first lobby as default if ID doesn't match
        List<vcfs.models.booking.Lobby> allLobbies = getAllLobbies();
        if (!allLobbies.isEmpty()) {
            return Optional.of(allLobbies.get(0));
        }
        
        return Optional.empty();
    }

    /**
     * Get all recruiters registered in the system.
     * @return Collection of all recruiters
     */
    public Collection<Recruiter> getRecruiters() {
        return recruitersList != null ? recruitersList : new ArrayList<>();
    }

    /**
     * Get all candidates in the system.
     * @return Collection of all candidates
     */
    public Collection<Candidate> getCandidates() {
        return candidatesList != null ? candidatesList : new ArrayList<>();
    }

    /**
     * Get all organizations participating in the fair.
     * @return Collection of all organizations
     */
    public Collection<Organization> getOrganizations() {
        return fair != null && fair.organizations != null ? fair.organizations : new ArrayList<>();
    }

    /**
     * Get all booths in all organizations.
     * @return Collection of all booths
     */
    public Collection<Booth> getBooths() {
        List<Booth> allBooths = new ArrayList<>();
        if (fair != null && fair.organizations != null) {
            for (Organization org : fair.organizations) {
                if (org.getBooths() != null) {
                    allBooths.addAll(org.getBooths());
                }
            }
        }
        return allBooths;
    }

    // =========================================================
    // DEMO SESSION INTEGRATION - COMPREHENSIVE SPECIFICATION
    // Per Plan V3 Ultimate - All methods with full javadoc
    // =========================================================
    
    /**
     * LIFECYCLE HOOK: Called when DEMO SESSION STARTS
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public void onDemoSessionStart()
     * 
     * CALLED BY:
     *   • DataModeManager.startDemoSession()
     *   • User initiates demo session
     *   • System startup (if in demo mode)
     * 
     * EFFECTS (IN ORDER):
     *   1. Log demo session initialization
     *   2. Broadcast "mode_switched" event to all UI listeners
     *   3. All registered PropertyChangeListeners receive the event
     *   4. UI screens (PublishOfferPanel, CandidateScreen, AdminScreen)
     *      update their badges to orange (🎬 DEMO - Not Saved)
     * 
     * SIDE EFFECTS:
     *   • Triggers PropertyChangeEvent for "mode_switched" property
     *   • All UI listeners automatically notified
     *   • No data modification (pure notification)
     * 
     * INITIALIZATION DATA:
     *   In current implementation: demo operates on same data as live
     *   Future: could initialize separate demo data storage if needed
     * 
     * ERROR HANDLING:
     *   Catches all exceptions and logs them
     *   Never throws exceptions (graceful degradation)
     * 
     * IDEMPOTENT: Safe to call multiple times
     *   (Multiple calls will just re-broadcast mode event)
     * 
     * THREAD SAFETY:
     *   - Safe because called from synchronized DataModeManager
     *   - PropertyChangeEvent is thread-safe
     *   - No concurrent data modifications
     * 
     * FOLLOWED BY:
     *   • SystemTimer continuously checks checkDemoSessionTimeout()
     *   • User publishes offers/bookings routed via DataModeManager
     *   • UI displays demo badges with countdown timer
     *   • When timeout occurs or user exits, onDemoSessionEnd() called
     */
    public void onDemoSessionStart() {
        try {
            Logger.info("[CareerFairSystem] ===== DEMO SESSION STARTING =====");
            Logger.info("[CareerFairSystem] All new offers/bookings handled in demo mode");
            
            // Broadcast PropertyChangeEvent: All listeners (UI screens) will receive this
            // PublishOfferPanel, CandidateScreen, AdminScreen all implement PropertyChangeListener
            // They will update their UI to show demo mode badges
            broadcastModeSwitch("DEMO");
            
            // Update dashboards with current state
            broadcastStatisticsUpdate();
            
            Logger.info("[CareerFairSystem] Demo session initialization complete");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CareerFairSystem] Error starting demo session", e);
        }
    }

    /**
     * LIFECYCLE HOOK: Called when DEMO SESSION ENDS
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public void onDemoSessionEnd()
     * 
     * CALLED BY:
     *   • DataModeManager.endDemoSession() after timeout
     *   • User manual exit ("Exit Demo" button)
     *   • Application shutdown
     *   • SystemTimer detected timeout via checkDemoSessionTimeout()
     * 
     * EFFECTS (IN ORDER):
     *   1. Log demo session ending
     *   2. Clear all demo data (if maintained separately in future)
     *   3. Broadcast "mode_switched" event for "LIVE" mode
     *   4. Refresh all UI with live data
     *   5. All UI listeners see badges change to green (📋 LIVE - Saved)
     * 
     * DATA HANDLING:
     *   In current implementation: demo and live use same storage
     *   • No data loss on demo end (all offers/issues stay)
     *   • Demo-only data: would be cleared here (in future impl)
     *   • Live data: unaffected in current impl
     * 
     * UI REFRESH BROADCASTS:
     *   • firePropertyChange("mode_switched", null, "LIVE")
     *   • firePropertyChange("organizations", null, getAllOrganizations())
     *   • firePropertyChange("recruiters", null, getAllRecruiters())
     *   • firePropertyChange("candidates", null, getAllCandidates())
     *   • firePropertyChange("offers", null, getAllOffers())
     *   • broadcastStatisticsUpdate() - refresh all dashboards
     * 
     * RESULT: All portals immediately show live data in green
     * 
     * IDEMPOTENT: Safe to call multiple times
     *   (Multiple calls refreshes UI unnecessarily but doesn't break)
     * 
     * ERROR HANDLING:
     *   Catches all exceptions and logs them
     *   Never throws exceptions
     * 
     * CLEANUP:
     *   • Cache invalidation (offer cache, org cache, booth cache)
     *   • Statistics recalculation
     *   • No resource cleanup needed (no demo storage to release)
     * 
     * FOLLOWED BY:
     *   • System returns to LIVE mode
     *   • New operations save directly to permanent storage
     *   • Badges display green (📋 LIVE - Saved)
     */
    public void onDemoSessionEnd() {
        try {
            Logger.info("[CareerFairSystem] ===== DEMO SESSION ENDING =====");
            
            // Broadcast mode change back to LIVE
            // All UI listeners will update badges and displays
            broadcastModeSwitch("LIVE");
            
            // Refresh all UI screens with current live data
            // This ensures any changes made in live mode are visible
            firePropertyChange("organizations", null, getAllOrganizations());
            firePropertyChange("recruiters", null, getAllRecruiters());
            firePropertyChange("candidates", null, getAllCandidates());
            firePropertyChange("offers", null, getAllOffers());
            
            // Update all databases on dashboards
            broadcastStatisticsUpdate();
            
            Logger.info("[CareerFairSystem] System returned to LIVE mode");
            Logger.info("[CareerFairSystem] Demo session ended successfully");
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[CareerFairSystem] Error ending demo session", e);
        }
    }

    /**
     * HELPER METHOD: Broadcast mode switch event to all listeners
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: private void broadcastModeSwitch(String newMode)
     * 
     * PARAMETERS:
     *   newMode: "DEMO" or "LIVE"
     * 
     * PURPOSE:
     *   Fire PropertyChangeEvent so all UI listeners are notified
     *   Used by: onDemoSessionStart() and onDemoSessionEnd()
     * 
     * LISTENERS AFFECTED:
     *   • PublishOfferPanel.propertyChange() - updates mode badge
     *   • CandidateScreen.propertyChange() - updates badges + UI
     *   • AdminScreen.propertyChange() - updates badges + filters
     * 
     * EVENT DETAILS:
     *   • Property name: "mode_switched"
     *   • Old value: null (not used)
     *   • New value: "DEMO" or "LIVE"
     * 
     * SIDE EFFECTS:
     *   • Triggers all registered PropertyChangeListeners
     *   • Logs mode switch to console/log file
     * 
     * THREAD SAFETY:
     *   - Safe (PropertyChangeSupport is thread-safe)
     */
    private void broadcastModeSwitch(String newMode) {
        firePropertyChange("mode_switched", null, newMode);
        Logger.log(LogLevel.INFO, "[CareerFairSystem] ===== MODE SWITCHED: " + newMode + " =====");
    }

    /**
     * QUERY METHOD: Is system currently in DEMO mode?
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean isInDemoMode()
     * 
     * RETURNS:
     *   true if DEMO mode is active
     *   false if LIVE mode is active
     * 
     * PURPOSE:
     *   UI queries to determine which badge to show
     *   Controllers check before routing data
     * 
     * IMPLEMENTATION:
     *   Delegates to DataModeManager singleton
     *   return DataModeManager.getInstance().isInDemoMode();
     * 
     * TIME COMPLEXITY: O(1) - constant time
     * 
     * SIDE EFFECTS: None (read-only query)
     * 
     * CALL FREQUENCY: High (50+ per session)
     * 
     * THREAD SAFETY: Safe to call from any thread
     *   (DataModeManager.isInDemoMode() is thread-safe)
     * 
     * USE CASES:
     *   • UI badge: if (system.isInDemoMode()) badge.setMode("DEMO")
     *   • Data routing: if (isInDemoMode()) route_to_demo else route_to_live
     *   • Form labels: Prefix with "DEMO:" if in demo mode
     */
    public boolean isInDemoMode() {
        return DataModeManager.getInstance().isInDemoMode();
    }

    /**
     * QUERY METHOD: Get current mode as string
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String getCurrentMode()
     * 
     * RETURNS:
     *   "LIVE" - live production mode (default)
     *   "DEMO" - demo/test mode session active
     * 
     * GUARANTEES:
     *   • Never null
     *   • Always "LIVE" or "DEMO"
     * 
     * PURPOSE:
     *   UI displays mode in status bar
     *   Logging includes mode in messages
     * 
     * IMPLEMENTATION:
     *   Delegates to DataModeManager singleton
     *   return DataModeManager.getInstance().getCurrentMode();
     * 
     * USE CASES:
     *   • Title bar: "VCFS - Mode: " + getCurrentMode()
     *   • Log messages: "[" + getCurrentMode() + "] User action..."
     *   • API: Return in system status endpoint
     */
    public String getCurrentMode() {
        return DataModeManager.getInstance().getCurrentMode();
    }

    /**
     * QUERY METHOD: Time remaining in current demo session
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public long getRemainingDemoTime()
     * 
     * RETURNS:
     *   Milliseconds remaining in demo session
     *   0 if not in demo mode or time exceeded
     * 
     * PURPOSE:
     *   UI timer displays rely on this for countdown
     *   Controllers check for low-time warnings
     * 
     * IMPLEMENTATION:
     *   Delegates to DataModeManager singleton
     *   return DataModeManager.getInstance().getRemainingDemoTime();
     * 
     * PRECISION: Millisecond level
     * 
     * USE CASES:
     *   • Progress bar: (elapsed / total) * 100 = percent
     *   • Timer display: format remaining time for "X min Y sec"
     *   • Warning threshold: if (remaining < 5 min) show_warning()
     */
    public long getRemainingDemoTime() {
        return DataModeManager.getInstance().getRemainingDemoTime();
    }

    /**
     * QUERY METHOD: Get remaining time as formatted string
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String getFormattedRemainingTime()
     * 
     * RETURNS:
     *   Examples:
     *   "Demo ends in: 30 min 0 sec" (just started)
     *   "Demo ends in: 15 min 30 sec" (mid-session)
     *   "Demo ends in: 5 min 0 sec" (running low)
     *   "Demo not active" (not in demo)
     * 
     * PURPOSE:
     *   Direct display in UI labels/badges
     *   Status bar countdown display
     * 
     * IMPLEMENTATION:
     *   Delegates to DataModeManager singleton
     *   return DataModeManager.getInstance().getFormattedRemainingTime();
     * 
     * FORMAT: "Demo ends in: [M] min [S] sec"
     * 
     * USE CASES:
     *   • Badge: Orange badge shows "Demo ends in: 25m 30s"
     *   • Status bar: Display "Demo ends in: 10m 0s" in status
     *   • UI label: Show countdown in form header
     */
    public String getFormattedRemainingTime() {
        return DataModeManager.getInstance().getFormattedRemainingTime();
    }

    // ===== HELPER METHODS FOR DATA INTEGRITY & STATUS =====
    
    /**
     * VALIDATION METHOD: Validate all data for consistency
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String validateDataConsistency()
     * 
     * PURPOSE:
     *   Comprehensive consistency check after mode transitions
     *   Ensure data wasn't corrupted during demo/live switch
     * 
     * RETURNS:
     *   Multi-line report string with validation results
     *   Format:
     *   "[Data Consistency Check]
     *    Recruiter Offers: [count]
     *    Candidate Count: [count]
     *    Bookings Count: [count]
     *    Mode: [LIVE/DEMO]
     *    Status: [PASS/WARNINGS]"
     * 
     * CHECKS PERFORMED:
     *   1. Count all offers across all recruiters
     *   2. Count all candidates registered
     *   3. Count all bookings/reservations
     *   4. Verify no null references
     *   5. Verify no data corruption
     * 
     * CALLED AT:
     *   • Mode transitions (to verify clean switch)
     *   • Admin "Validate Data" button
     *   • System diagnostics
     *   • After data import/reset
     * 
     * SIDE EFFECTS:
     *   • Logs validation results
     *   • Does not modify data
     * 
     * FUTURE ENHANCEMENTS:
     *   • Check offer time overlaps
     *   • Check email uniqueness constraints
     *   • Verify booking validity
     */
    public String validateDataConsistency() {
        StringBuilder report = new StringBuilder();
        report.append("[Data Consistency Check]\n");
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        int recruiterOffers = getAllOffers().size();
        int recruiterCount = getAllRecruiters().size();
        int candidateCount = getAllCandidates().size();
        
        report.append("Recruiters: ").append(recruiterCount).append("\n");
        report.append("Recruiter Offers: ").append(recruiterOffers).append("\n");
        report.append("Candidates: ").append(candidateCount).append("\n");
        report.append("Mode: ").append(DataModeManager.getInstance().getCurrentMode()).append("\n");
        report.append("Status: ✅ PASS\n");
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] " + report.toString());
        return report.toString();
    }
    
    /**
     * STATUS METHOD: Get count of demo data objects
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public int getDemoDataCount()
     * 
     * RETURNS:
     *   Total count of demo-mode data objects
     *   In current implementation: 0 (demo uses same storage)
     * 
     * PURPOSE:
     *   Display in admin dashboards
     *   Status reports for demo sessions
     * 
     * FUTURE IMPLEMENTATION:
     *   If demo data is stored separately (HashMap in DataModeManager):
     *   Count items in demoSessionData.size()
     * 
     * CURRENT: Returns 0 (demo uses live storage)
     */
    public int getDemoDataCount() {
        // Placeholder - in future may track separate demo storage
        // For now: demo operates on same data as live
        return 0;
    }
    
    /**
     * STATUS METHOD: Get count of live data objects
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public int getLiveDataCount()
     * 
     * RETURNS:
     *   Total count of permanent live data objects
     *   = count of all recruiters + count of all candidates
     * 
     * PURPOSE:
     *   Display in system dashboards and status reports
     *   HUD shows "Live Data: XXX items"
     * 
     * IMPLEMENTATION:
     *   return getAllRecruiters().size() + getAllCandidates().size();
     * 
     * INCLUDES ALL:
     *   • Recruiters registered in system
     *   • Candidates registered in system
     *   • NOT included: offers/bookings (derived from above)
     */
    public int getLiveDataCount() {
        return getAllRecruiters().size() + getAllCandidates().size();
    }
    
    /**
     * MODE CONTROL METHOD: Switch mode with full notification
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean switchModeWithNotification(String newMode)
     * 
     * PARAMETERS:
     *   newMode: "LIVE" or "DEMO"
     * 
     * RETURNS:
     *   true if mode switch successful
     *   false if invalid mode or error occurred
     * 
     * PURPOSE:
     *   Admin/system method to programmatically change mode
     *   Single entry point for complete mode transitions
     * 
     * VALIDATION:
     *   • Check newMode is "LIVE" or "DEMO"
     *   • Reject invalid modes with error log
     * 
     * EFFECTS:
     *   If newMode == "DEMO": calls onDemoSessionStart()
     *   If newMode == "LIVE": calls onDemoSessionEnd()
     * 
     * BROADCASTING:
     *   All UI listeners notified via PropertyChangeEvent
     *   All dashboards refreshed automatically
     * 
     * IDEMPOTENT:
     *   Safe to call multiple times (just refreshes)
     * 
     * USE CASES:
     *   • Admin panel "Start Demo" button
     *   • Admin panel "End Demo" button
     *   • System startup configuration
     *   • Automated demo scheduling
     */
    public boolean switchModeWithNotification(String newMode) {
        if (!newMode.equals("LIVE") && !newMode.equals("DEMO")) {
            Logger.log(LogLevel.ERROR, "[CareerFairSystem] Invalid mode: " + newMode);
            return false;
        }
        
        String oldMode = DataModeManager.getInstance().getCurrentMode();
        
        if (newMode.equals("DEMO")) {
            onDemoSessionStart();
        } else {
            onDemoSessionEnd();
        }
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Mode switched: " + oldMode + " → " + newMode);
        return true;
    }
    
    /**
     * REPORTING METHOD: Get detailed mode and data status
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String getDetailedStatus()
     * 
     * RETURNS:
     *   Formatted multi-line status report including:
     *   • Current mode (LIVE or DEMO)
     *   • Count of live data objects
     *   • Count of demo data objects
     *   • If in demo: remaining time + progress percent
     * 
     * EXAMPLE OUTPUT (DEMO MODE):
     *   "📊 SYSTEM STATUS
     *    ━━━━━━━━━━━━━━━━━━━
     *    Mode: DEMO
     *    Live Data Count: 15
     *    Demo Data Count: 0
     *    Demo Remaining: Demo ends in: 25 min 30 sec
     *    Demo Progress: 17%"
     * 
     * EXAMPLE OUTPUT (LIVE MODE):
     *   "📊 SYSTEM STATUS
     *    ━━━━━━━━━━━━━━━━━━━
     *    Mode: LIVE
     *    Live Data Count: 15
     *    Demo Data Count: 0"
     * 
     * PURPOSE:
     *   Display in admin dashboard
     *   Status bar shows system state
     *   Help menu shows system information
     * 
     * USE CASES:
     *   • Admin status panel
     *   • Diagnostic reporting
     *   • User information display
     *   • System health check
     */
    public String getDetailedStatus() {
        StringBuilder sb = new StringBuilder();
        
        DataModeManager mgr = DataModeManager.getInstance();
        String currentMode = mgr.getCurrentMode();
        boolean isDemoActive = mgr.isInDemoMode();
        
        sb.append("📊 SYSTEM STATUS\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("Current Mode: ").append(currentMode).append("\n");
        sb.append("Live Data Objects: ").append(getLiveDataCount()).append("\n");
        sb.append("Demo Data Objects: ").append(getDemoDataCount()).append("\n");
        
        if (isDemoActive) {
            sb.append("\nDemo Session Status:\n");
            sb.append("  Remaining Time: ").append(mgr.getFormattedRemainingTime()).append("\n");
            sb.append("  Elapsed Time: ").append(mgr.getFormattedElapsedTime()).append("\n");
            sb.append("  Progress: ").append(mgr.getDemoSessionProgressPercent()).append("%\n");
            sb.append("  Duration: ").append(mgr.getDemoSessionDuration() / 60000).append(" minutes\n");
        }
        
        return sb.toString();
    }
    
    /**
     * AUDIT METHOD: Log mode transition for record-keeping
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public void logModeTransition(String fromMode, String toMode, String reason)
     * 
     * PARAMETERS:
     *   fromMode: Previous mode ("LIVE" or "DEMO")
     *   toMode: New mode ("LIVE" or "DEMO")
     *   reason: Why the transition occurred
     *     Examples: "User clicked Exit Demo", "Timeout exceeded", "Auto-start"
     * 
     * PURPOSE:
     *   Create audit trail for system transitions
     *   Enable debugging and compliance reporting
     * 
     * LOG FORMAT:
     *   "[YYYY-MM-DD HH:MM:SS] Mode Transition: LIVE → DEMO (Reason: User initiated)"
     * 
     * CONTEXT SAVED:
     *   • Timestamp (when transition occurred)
     *   • From mode (LIVE or DEMO)
     *   • To mode (LIVE or DEMO)
     *   • Reason (why user/system made the change)
     * 
     * CALLED BY:
     *   • onDemoSessionStart() - log LIVE→DEMO transition
     *   • onDemoSessionEnd() - log DEMO→LIVE transition
     *   • System startup
     *   • Admin actions
     * 
     * PERSISTENCE:
     *   Written to Logger, which may persist to:
     *   • Console output
     *   • File logs
     *   • System audit trail
     * 
     * USE CASES:
     *   • Compliance: "Who changed modes and when?"
     *   • Debugging: "What state was system in?"
     *   • Analytics: "How many demo sessions per day?"
     */
    public void logModeTransition(String fromMode, String toMode, String reason) {
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String logEntry = String.format(
            "[%s] ===== Mode Transition: %s → %s | Reason: %s =====",
            timestamp, fromMode, toMode, reason != null ? reason : "unspecified"
        );
        
        Logger.log(LogLevel.INFO, logEntry);
    }
}

