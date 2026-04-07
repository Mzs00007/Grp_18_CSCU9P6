package vcfs.core;

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
import java.util.*;

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
 */
public class CareerFairSystem implements PropertyChangeListener {

    // =========================================================
    // SINGLETON INFRASTRUCTURE
    // =========================================================

    private static CareerFairSystem instance;

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

        System.out.println("[CareerFairSystem] Singleton created. Observer registered.");
    }

    /** Access the single CareerFairSystem instance */
    public static synchronized CareerFairSystem getInstance() {
        if (instance == null) {
            instance = new CareerFairSystem();
        }
        return instance;
    }

    // =========================================================
    // OWNED STATE
    // =========================================================

    CareerFair fair;

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
        fair.evaluatePhase(now);
        // Future: check all active MeetingSessions for start/end triggers
    }

    /**
     * Return the current fair phase for UI display.
     * Used by Swing screens to enable/disable buttons.
     */
    FairPhase getCurrentPhase() {
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
    void configureTimes(LocalDateTime openTime, LocalDateTime closeTime,
                        LocalDateTime startTime, LocalDateTime endTime) {
        fair.setTimes(openTime, closeTime, startTime, endTime);
        System.out.println("[CareerFairSystem] Fair times configured.");
    }

    /**
     * Admin: clear all in-memory fair data for a fresh run.
     * Safely clears every collection without causing NPEs.
     * Called by YAMI's AdminController.onResetFair().
     */
    void resetFairData() {
        if (fair.organizations != null) fair.organizations.clear();
        if (fair.auditTrail != null)    fair.auditTrail.clear();
        fair.currentPhase       = FairPhase.DORMANT;
        fair.bookingsOpenTime   = null;
        fair.bookingsCloseTime  = null;
        fair.startTime          = null;
        fair.endTime            = null;
        System.out.println("[CareerFairSystem] Fair data reset. All collections cleared.");
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
        Logger.log(LogLevel.INFO, "Booth '" + title + "' added to '" + org.getName() + "'");
        return booth;
    }

    /**
     * Admin: create a Recruiter and assign them to a Booth.
     * Enforces unique email across all recruiters in the system.
     *
     * @param displayName Recruiter's display name
     * @param email       Unique email identifier
     * @param booth       The booth to assign this recruiter to
     * @return            The newly created and assigned Recruiter
     */
    public Recruiter registerRecruiter(String displayName, String email, Booth booth) {
        if (booth == null) {
            throw new IllegalArgumentException("Booth cannot be null.");
        }

        // Enforce unique email across entire system
        for (Offer o : getAllOffers()) {
            if (o.getPublisher() != null && email.equalsIgnoreCase(o.getPublisher().getEmail())) {
                throw new IllegalStateException(
                    "[CareerFairSystem] Recruiter email already exists: " + email);
            }
        }

        Recruiter recruiter = new Recruiter(email, displayName, email);
        booth.assignRecruiter(recruiter);
        Logger.log(LogLevel.INFO, "Recruiter registered: "
                + displayName + " -> Booth: " + booth.getTitle());
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
        
        Candidate candidate = new Candidate(email, displayName, email);
        if (cvSummary != null && !cvSummary.trim().isEmpty()) {
            candidate.getProfile().setCvSummary(cvSummary);
        }
        if (interestTags != null && !interestTags.trim().isEmpty()) {
            candidate.getProfile().setInterestTags(interestTags);
        }
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
            System.out.println("[MATCHENGINE] Rejected — Phase: " + fair.getCurrentPhase());
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
                        System.out.println("[MATCHENGINE] Conflict at "
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
                System.out.println("[MATCHENGINE] Offer @ " + offer.getStartTime()
                        + " score=" + score + "/" + desiredTags.size());
            }
        }

        if (scoreMap.isEmpty()) {
            System.out.println("[MATCHENGINE] No matching offers found.");
            return null;
        }

        // Select the highest-scoring Offer
        Offer bestOffer = Collections.max(
                scoreMap.entrySet(),
                Map.Entry.comparingByValue()).getKey();

        System.out.println("[MATCHENGINE] Winner: " + bestOffer.getStartTime()
                + " (score=" + scoreMap.get(bestOffer) + ")");

        // Create confirmed Reservation
        Reservation reservation = new Reservation();
        reservation.setCandidate(candidate);
        reservation.setOffer(bestOffer);
        reservation.setScheduledStart(bestOffer.getStartTime());
        reservation.setScheduledEnd(bestOffer.getEndTime());
        reservation.setState(ReservationState.CONFIRMED);

        if (candidate.getReservations() != null) {
            // Add to existing collection
            ((java.util.ArrayList<Reservation>) candidate.getReservations()).add(reservation);
        }
        if (bestOffer.getReservations() != null) {
            ((java.util.ArrayList<Reservation>) bestOffer.getReservations()).add(reservation);
        }

        System.out.println("[MATCHENGINE] CONFIRMED: " + candidate.getDisplayName()
                + " → " + bestOffer.getStartTime()
                + " with " + (bestOffer.getPublisher() != null ? bestOffer.getPublisher().getDisplayName() : "unknown"));
        return reservation;
    }

    /**
     * Gather ALL Offer objects from every Org → Booth → Recruiter in the system.
     * Used by autoBook() MatchEngine and manual browsing queries.
     */
    private List<Offer> getAllOffers() {
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
        return allOffers;
    }

    // =========================================================
    // PUBLIC QUERY METHODS (for controllers)
    // =========================================================

    /**
     * Find an organization by name.
     * @param name Organization name to search for
     * @return The Organization if found, null otherwise
     */
    public Organization getOrganizationByName(String name) {
        if (name == null || fair.organizations == null) return null;
        for (Organization org : fair.organizations) {
            if (name.equals(org.getName())) {
                return org;
            }
        }
        return null;
    }

    /**
     * Find a booth by name (searches all organizations).
     * @param name Booth name to search for
     * @return The Booth if found, null otherwise
     */
    public Booth getBoothByName(String name) {
        if (name == null || fair.organizations == null) return null;
        for (Organization org : fair.organizations) {
            if (org.getBooths() == null) continue;
            for (Booth booth : org.getBooths()) {
                if (name.equals(booth.getTitle())) {
                    return booth;
                }
            }
        }
        return null;
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
     * TODO (Taha — VCFS-009): implement publishOffer
     */
    Offer publishOffer(Recruiter recruiter, String title,
                       int durationMins, String topicTags, int capacity) {
        // TODO - implement CareerFairSystem.publishOffer
        throw new UnsupportedOperationException();
    }

    /**
     * Register a candidate request; check phase/policy.
     * TODO (MJAMishkat — VCFS-014): implement createRequest
     */
    Request createRequest(Candidate candidate, String desiredTags,
                          String preferredOrgs, int maxAppointments) {
        // TODO - implement CareerFairSystem.createRequest
        throw new UnsupportedOperationException();
    }

    /**
     * Create a CONFIRMED reservation at a selected time (manual flow).
     * TODO (MJAMishkat — VCFS-014): implement manualBook
     */
    Reservation manualBook(Candidate candidate, Offer offer, LocalDateTime start) {
        // TODO - implement CareerFairSystem.manualBook
        throw new UnsupportedOperationException();
    }

    /**
     * Cancel as recruiter; record audit and publish events.
     * TODO (YAMI — VCFS-008): implement cancelAsRecruiter
     */
    void cancelAsRecruiter(Recruiter recruiter, String reservationId, String reason) {
        // TODO - implement CareerFairSystem.cancelAsRecruiter
        throw new UnsupportedOperationException();
    }

    /**
     * Cancel as candidate; record audit and publish events.
     * TODO (YAMI — VCFS-008): implement cancelAsCandidate
     */
    void cancelAsCandidate(Candidate candidate, String reservationId) {
        // TODO - implement CareerFairSystem.cancelAsCandidate
        throw new UnsupportedOperationException();
    }

    /**
     * Join logic: lobby vs room; ensure MeetingSession exists; record attendance.
     * TODO (MJAMishkat — VCFS-016): implement joinSession
     */
    void joinSession(Candidate candidate, String reservationId) {
        // TODO - implement CareerFairSystem.joinSession
        throw new UnsupportedOperationException();
    }
}
