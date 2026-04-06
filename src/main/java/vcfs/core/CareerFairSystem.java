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
    Organization addOrganization(String name) {
        Organization org = new Organization();
        org.name   = name;
        org.fair   = fair;
        org.booths = new ArrayList<>();
        fair.organizations.add(org);
        System.out.println("[CareerFairSystem] Organization added: " + name);
        return org;
    }

    /**
     * Admin: add a named Booth to an existing Organization.
     * @param org   The organization to add the booth to
     * @param title Display name for the booth
     * @return      The newly created Booth
     */
    Booth addBooth(Organization org, String title) {
        if (org == null) throw new IllegalArgumentException("Organization cannot be null.");
        Booth booth = new Booth();
        booth.title        = title;
        booth.organization = org;
        booth.recruiters   = new ArrayList<>();
        org.booths.add(booth);
        System.out.println("[CareerFairSystem] Booth '" + title + "' added to '" + org.name + "'");
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
    Recruiter registerRecruiter(String displayName, String email, Booth booth) {
        if (booth == null) throw new IllegalArgumentException("Booth cannot be null.");

        // Enforce unique email across entire system
        for (Offer o : getAllOffers()) {
            if (o.publisher != null && email.equalsIgnoreCase(o.publisher.email)) {
                throw new IllegalStateException(
                    "[CareerFairSystem] Recruiter email already exists: " + email);
            }
        }

        Recruiter recruiter = new Recruiter();
        recruiter.displayName = displayName;
        recruiter.email       = email;
        recruiter.booth       = booth;
        recruiter.offers      = new ArrayList<>();
        booth.assignRecruiter(recruiter);

        System.out.println("[CareerFairSystem] Recruiter registered: "
                + displayName + " → Booth: " + booth.title);
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
    Candidate registerCandidate(String displayName, String email,
                                 String cvSummary, String interestTags) {
        Candidate candidate = new Candidate();
        candidate.displayName = displayName;
        candidate.email       = email;
        candidate.reservations = new ArrayList<>();
        System.out.println("[CareerFairSystem] Candidate registered: " + displayName);
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

        if (recruiter == null || recruiter.offers == null) {
            throw new IllegalArgumentException("[PARSER] Invalid recruiter.");
        }

        LocalDateTime cursor = blockStart;
        int slotsCreated = 0;

        System.out.println("[PARSER] Parsing block for " + recruiter.displayName
                + ": " + blockStart + " → " + blockEnd
                + " | " + durationMins + " min slots");

        // Core loop: generate one Offer per slot until cursor overshoots blockEnd
        while (!cursor.plusMinutes(durationMins).isAfter(blockEnd)) {
            Offer slot = new Offer();
            slot.title        = title;
            slot.startTime    = cursor;
            slot.endTime      = cursor.plusMinutes(durationMins);
            slot.durationMins = durationMins;
            slot.topicTags    = topicTags;
            slot.capacity     = capacity;
            slot.publisher    = recruiter;
            slot.reservations = new ArrayList<>();

            recruiter.offers.add(slot);
            slotsCreated++;

            System.out.println("[PARSER] Slot " + slotsCreated + ": "
                    + slot.startTime + " → " + slot.endTime);

            cursor = cursor.plusMinutes(durationMins);
        }

        System.out.println("[PARSER] Complete: " + slotsCreated + " slots for "
                + recruiter.displayName);
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
    Reservation autoBook(Candidate candidate, Request request) {

        // Phase guard
        LocalDateTime now = SystemTimer.getInstance().getNow();
        if (!fair.canBook(now)) {
            System.out.println("[MATCHENGINE] Rejected — Phase: " + fair.getCurrentPhase());
            return null;
        }

        if (candidate == null || request == null || request.desiredTags == null) {
            throw new IllegalArgumentException("[MATCHENGINE] Invalid candidate or request.");
        }

        // Parse desired tags: "Java, AI, Cloud" → ["java", "ai", "cloud"]
        List<String> desiredTags = Arrays.asList(
                request.desiredTags.toLowerCase().split(",\\s*"));

        System.out.println("[MATCHENGINE] Searching for: " + candidate.displayName
                + " | Tags: " + desiredTags);

        Map<Offer, Integer> scoreMap = new HashMap<>();

        for (Offer offer : getAllOffers()) {
            if (offer.topicTags == null || offer.startTime == null) continue;

            // --- COLLISION DETECTION ---
            // Two windows [A_start, A_end) and [B_start, B_end) overlap when:
            //   A_start < B_end  AND  B_start < A_end
            boolean conflict = false;
            if (candidate.reservations != null) {
                for (Reservation existing : candidate.reservations) {
                    if (existing.scheduledStart == null || existing.scheduledEnd == null) continue;
                    if (existing.scheduledStart.isBefore(offer.endTime)
                            && offer.startTime.isBefore(existing.scheduledEnd)) {
                        conflict = true;
                        System.out.println("[MATCHENGINE] Conflict at "
                                + offer.startTime + " — skipped.");
                        break;
                    }
                }
            }
            if (conflict) continue;

            // --- TAG SCORING ---
            List<String> offerTags = Arrays.asList(
                    offer.topicTags.toLowerCase().split(",\\s*"));
            int score = 0;
            for (String tag : desiredTags) {
                if (offerTags.contains(tag.trim())) score++;
            }

            if (score > 0) {
                scoreMap.put(offer, score);
                System.out.println("[MATCHENGINE] Offer @ " + offer.startTime
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

        System.out.println("[MATCHENGINE] Winner: " + bestOffer.startTime
                + " (score=" + scoreMap.get(bestOffer) + ")");

        // Create confirmed Reservation
        Reservation reservation = new Reservation();
        reservation.candidate      = candidate;
        reservation.offer          = bestOffer;
        reservation.scheduledStart = bestOffer.startTime;
        reservation.scheduledEnd   = bestOffer.endTime;
        reservation.state          = ReservationState.CONFIRMED;

        if (candidate.reservations == null) candidate.reservations = new ArrayList<>();
        candidate.reservations.add(reservation);
        if (bestOffer.reservations == null) bestOffer.reservations = new ArrayList<>();
        bestOffer.reservations.add(reservation);

        System.out.println("[MATCHENGINE] CONFIRMED: " + candidate.displayName
                + " → " + bestOffer.startTime
                + " with " + (bestOffer.publisher != null ? bestOffer.publisher.displayName : "unknown"));
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
            if (org.booths == null) continue;
            for (Booth booth : org.booths) {
                if (booth.recruiters == null) continue;
                for (Recruiter recruiter : booth.recruiters) {
                    if (recruiter.offers != null) {
                        allOffers.addAll(recruiter.offers);
                    }
                }
            }
        }
        return allOffers;
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
