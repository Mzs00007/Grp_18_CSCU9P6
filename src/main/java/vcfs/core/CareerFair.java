package vcfs.core;

import vcfs.models.audit.AuditEntry;
import vcfs.models.enums.FairPhase;
import vcfs.models.structure.Organization;

import java.util.*;

/**
 * Aggregate root for a single career-fair run.
 * Stores the timeline, owns the phase state machine, and
 * provides boolean guards for all system operations.
 *
 * Relationships:
 *   - Owns a Collection of Organizations (each has Booths → Recruiters → Offers)
 *   - Owns an AuditTrail for logging significant events
 *   - Referenced by CareerFairSystem (the system facade)
 *
 * Phase Lifecycle:
 *   DORMANT → PREPARING → BOOKINGS_OPEN → BOOKINGS_CLOSED → FAIR_LIVE → DORMANT
 *
 * Implemented by: Zaid — VCFS-002
 */
public class CareerFair {

    // =========================================================
    // FIELDS (preserving the original skeleton's declarations)
    // =========================================================

    /** All participating organizations — traversed by MatchEngine */
    Collection<Organization> organizations = new ArrayList<>();

    /** Audit log — populated by AuditEntry factory methods (YAMI wires the UI) */
    Collection<AuditEntry> auditTrail = new ArrayList<>();

    /** Back-reference to the owning system facade */
    CareerFairSystem system;

    /** Display name of this fair instance */
    public String name;

    /**
     * Current operational phase.
     * Starts DORMANT — transitions only after Admin calls setTimes().
     */
    FairPhase currentPhase = FairPhase.DORMANT;

    // =========================================================
    // THE 4 TIME BOUNDARY PILLARS
    // =========================================================

    /**
     * When the booking window opens.
     * At/after this moment: phase transitions to BOOKINGS_OPEN.
     */
    LocalDateTime bookingsOpenTime;

    /**
     * When the booking window closes.
     * At/after this moment: phase transitions to BOOKINGS_CLOSED.
     */
    LocalDateTime bookingsCloseTime;

    /**
     * When the live fair begins (virtual rooms open).
     * At/after this moment: phase transitions to FAIR_LIVE.
     */
    LocalDateTime startTime;

    /**
     * When the fair is completely over.
     * After this moment: phase returns to DORMANT.
     */
    LocalDateTime endTime;

    // =========================================================
    // VCFS-002: ADMIN TIME CONFIGURATION
    // (YAMI calls this via AdminController.onSetFairTimes)
    // =========================================================

    /**
     * Configure the fair's four time boundaries.
     *
     * Validates boundaries are in strict chronological order.
     * Rejects misconfigured input early (fail-fast) to prevent
     * the state machine from producing nonsensical phase results.
     *
     * After this is called, every SystemTimer tick will begin
     * transitioning the phase correctly via evaluatePhase().
     *
     * Chronological contract:
     *   openTime < closeTime < startTime < endTime
     *
     * @param openTime  When booking window opens
     * @param closeTime When booking window closes
     * @param startTime When the live fair begins
     * @param endTime   When the fair ends completely
     */
    void setTimes(LocalDateTime openTime, LocalDateTime closeTime,
                  LocalDateTime startTime, LocalDateTime endTime) {

        // === Validate chronological ordering ===
        if (!openTime.isBefore(closeTime)) {
            throw new IllegalArgumentException(
                    "[CareerFair] Bookings open time must be BEFORE close time.\n"
                    + "  Got: " + openTime + " → " + closeTime);
        }
        if (!closeTime.isBefore(startTime)) {
            throw new IllegalArgumentException(
                    "[CareerFair] Bookings close time must be BEFORE fair start time.\n"
                    + "  Got: " + closeTime + " → " + startTime);
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "[CareerFair] Fair start time must be BEFORE fair end time.\n"
                    + "  Got: " + startTime + " → " + endTime);
        }

        // === Store the validated boundaries ===
        this.bookingsOpenTime  = openTime;
        this.bookingsCloseTime = closeTime;
        this.startTime         = startTime;
        this.endTime           = endTime;

        // Immediately transition out of DORMANT now that times are configured
        this.currentPhase = FairPhase.PREPARING;

        System.out.println("[CareerFair] '" + name + "' timeline configured:");
        System.out.println("  Bookings Open  : " + this.bookingsOpenTime);
        System.out.println("  Bookings Close : " + this.bookingsCloseTime);
        System.out.println("  Fair Start     : " + this.startTime);
        System.out.println("  Fair End       : " + this.endTime);
        System.out.println("  Phase set to   : " + this.currentPhase);
    }

    // =========================================================
    // VCFS-002: THE CORE STATE MACHINE EVALUATOR
    // =========================================================

    /**
     * Evaluate and update the current phase based on the simulated time.
     *
     * This is called AUTOMATICALLY on every SystemTimer tick via:
     *   SystemTimer fires "time" event
     *     → CareerFairSystem.propertyChange(evt)
     *       → CareerFairSystem.tick()
     *         → THIS METHOD
     *
     * The if/else cascade checks 'now' against the 4 boundaries in order.
     *
     * Phase Timeline:
     * ┌──────────────────────────────────────────────────────────────┐
     * │ DORMANT │ PREPARING │ BOOKINGS_OPEN │ CLOSED │ FAIR_LIVE │...│
     * └──────────────────────────────────────────────────────────────┘
     *           ▲            ▲               ▲        ▲           ▲
     *        setTimes   bookingsOpen    bookingsClose  start      end
     *
     * @param now The current simulated LocalDateTime from SystemTimer
     */
    void evaluatePhase(LocalDateTime now) {

        // Guard: if times haven't been configured by Admin yet, stay DORMANT
        if (bookingsOpenTime == null) {
            currentPhase = FairPhase.DORMANT;
            return;
        }

        // Capture previous phase for transition logging
        FairPhase previous = currentPhase;

        if (now.isBefore(bookingsOpenTime)) {
            // Times configured but booking window not yet open
            currentPhase = FairPhase.PREPARING;

        } else if (now.isAfterOrEqual(bookingsOpenTime)
                && now.isBefore(bookingsCloseTime)) {
            // Inside the open booking window
            currentPhase = FairPhase.BOOKINGS_OPEN;

        } else if (now.isAfterOrEqual(bookingsCloseTime)
                && now.isBefore(startTime)) {
            // Booking window closed; fair hasn't gone live yet
            currentPhase = FairPhase.BOOKINGS_CLOSED;

        } else if (now.isAfterOrEqual(startTime)
                && now.isBeforeOrEqual(endTime)) {
            // The fair is actively running — virtual rooms are open
            currentPhase = FairPhase.FAIR_LIVE;

        } else {
            // Past the end time — fair is over
            currentPhase = FairPhase.DORMANT;
        }

        // Log only actual transitions (avoids console spam on repeated ticks)
        if (previous != currentPhase) {
            System.out.println("[CareerFair] ⚡ Phase: "
                    + previous + " ──► " + currentPhase
                    + "  (at " + now + ")");
        }
    }

    // =========================================================
    // BOOLEAN GUARD METHODS
    // Called by CareerFairSystem before every system action
    // =========================================================

    /**
     * True iff currentPhase exactly matches the provided phase.
     * O(1) enum equality — no re-evaluation of boundaries.
     *
     * Used for specific phase queries throughout the system.
     * @param phase The FairPhase to test against
     */
    boolean isInPhase(FairPhase phase) {
        return this.currentPhase == phase;
    }

    /**
     * True ONLY during the BOOKINGS_OPEN phase.
     *
     * Enforces the rule: Candidates can ONLY create bookings when the
     * booking window is actively open. Called before every booking action:
     *   - autoBook()
     *   - manualBook()
     *   - createRequest()
     *   - parseAvailabilityIntoOffers()
     *
     * Re-evaluates the phase before checking to guarantee freshness.
     *
     * @param now The current simulated time from SystemTimer
     */
    boolean canBook(LocalDateTime now) {
        evaluatePhase(now);
        return isInPhase(FairPhase.BOOKINGS_OPEN);
    }

    /**
     * True ONLY during the FAIR_LIVE phase.
     *
     * Enforces the rule: Candidates can ONLY enter virtual rooms when
     * the fair is actively live. Called by the Lobby Gatekeeper
     * (MJAMishkat's joinSession implementation) before VirtualRoom entry.
     *
     * Re-evaluates the phase before checking to guarantee freshness.
     *
     * @param now The current simulated time from SystemTimer
     */
    boolean isLive(LocalDateTime now) {
        evaluatePhase(now);
        return isInPhase(FairPhase.FAIR_LIVE);
    }

    /**
     * Return the current phase without re-evaluating boundaries.
     * Used by CareerFairSystem.getCurrentPhase() for UI display.
     */
    FairPhase getCurrentPhase() {
        return currentPhase;
    }
}
