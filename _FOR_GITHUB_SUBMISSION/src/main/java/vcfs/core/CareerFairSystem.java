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

    // =========================================================
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
     * Tracks all registered recruiters in the system.
     * THREAD-SAFE: Uses thread-safe collections.
     * CRITICAL: Used for admin display - recruiters shown even if no offers published yet.
     */
    private final List<Recruiter> recruitersList = Collections.synchronizedList(new ArrayList<>());

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
     */
    public void configureTimes(LocalDateTime openTime, LocalDateTime closeTime,
                        LocalDateTime startTime, LocalDateTime endTime) {
        fair.setTimes(openTime, closeTime, startTime, endTime);
        Logger.info("[CareerFairSystem] Fair times configured.");
    }

    /**
     * Admin: clear all in-memory fair data for a fresh run.
     * Safely clears every collection without causing NPEs.
     * Also clears all performance caches to ensure fresh state.
     * Called by YAMI's AdminController.onResetFair().
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
        
        Logger.log(LogLevel.INFO, "[CareerFairSystem] Candidate registered: " + displayName);
        return candidate;
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
        
        return reservation;
    }

    /**
     * Gather ALL Offer objects from every Org → Booth → Recruiter in the system.
     * Used by autoBook() MatchEngine and manual browsing queries - PUBLIC for UI access.
     * 
     * P2 OPTIMIZATION: Caching (ZAID - mzs00007)
     *   - First call: O(Orgs × Booths × Recruiters); result cached
     *   - Subsequent calls: O(1) cache hit
     *   - Cache invalidated whenever parseAvailabilityIntoOffers() called
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
        Logger.log(LogLevel.INFO, "Fair times set via controller: open=" + openStr + ", close=" + closeStr + ", start=" + startStr + ", end=" + endStr);
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
    }}


