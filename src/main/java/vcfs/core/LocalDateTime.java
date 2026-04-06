package vcfs.core;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * VCFS Simulated Time Wrapper.
 *
 * Wraps java.time.LocalDateTime to provide a clean, VCFS-specific
 * time API, used as the atomic unit of time across the entire system.
 *
 * Instances are IMMUTABLE — every arithmetic operation returns a NEW object.
 *
 * Used by:
 *   - SystemTimer (stores 'now')
 *   - CareerFair (compares boundaries for phase transitions)
 *   - Reservation (scheduledStart / scheduledEnd)
 *   - Offer (startTime / endTime)
 *   - MeetingSession, AttendanceRecord (join/leave times)
 *
 * Implemented by: Zaid — VCFS-001
 */
public class LocalDateTime {

    // Internal Java time object — private so nothing couples to java.time directly
    private final java.time.LocalDateTime inner;

    // Shared formatter for all UI display output
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    /**
     * Create a LocalDateTime from explicit date/time components.
     *
     * Example:
     *   new LocalDateTime(2026, 4, 10, 9, 0)  →  "2026-04-10 09:00"
     *
     * @param year   4-digit year
     * @param month  Month 1–12
     * @param day    Day of month 1–31
     * @param hour   Hour 0–23
     * @param minute Minute 0–59
     */
    public LocalDateTime(int year, int month, int day, int hour, int minute) {
        this.inner = java.time.LocalDateTime.of(year, month, day, hour, minute);
    }

    /**
     * Private constructor used internally by arithmetic methods.
     * External code never accesses java.time.LocalDateTime directly.
     */
    private LocalDateTime(java.time.LocalDateTime inner) {
        this.inner = inner;
    }

    // =========================================================
    // TIME ARITHMETIC
    // =========================================================

    /**
     * Return a NEW LocalDateTime advanced by the given number of minutes.
     * Does NOT modify this object (immutable).
     *
     * Called by: SystemTimer.stepMinutes()
     * Called by: parseAvailabilityIntoOffers() cursor loop
     *
     * @param mins Minutes to advance (must be positive)
     * @return     New LocalDateTime representing (this + mins minutes)
     */
    public LocalDateTime plusMinutes(long mins) {
        return new LocalDateTime(this.inner.plusMinutes(mins));
    }

    /**
     * Calculate how many minutes separate this time from another (future) time.
     * Returns a positive number when 'other' is in the future relative to this.
     *
     * Called by: Lobby Gatekeeper — how many minutes until session start?
     * Called by: MeetingSession — total attended duration calculation
     *
     * Example: 09:00.minutesUntil(09:30) → 30L
     *
     * @param other The later time
     * @return Long minutes between this and other
     */
    public long minutesUntil(LocalDateTime other) {
        return Duration.between(this.inner, other.inner).toMinutes();
    }

    // =========================================================
    // COMPARISON METHODS
    // =========================================================

    /**
     * True if this time is strictly BEFORE other.
     * Used in CareerFair phase boundary checks.
     *
     * 09:00.isBefore(09:30) → true
     * 09:30.isBefore(09:00) → false
     * 09:00.isBefore(09:00) → false  (equal is NOT before)
     */
    public boolean isBefore(LocalDateTime other) {
        return this.inner.isBefore(other.inner);
    }

    /**
     * True if this time is strictly AFTER other.
     *
     * 09:30.isAfter(09:00) → true
     * 09:00.isAfter(09:30) → false
     */
    public boolean isAfter(LocalDateTime other) {
        return this.inner.isAfter(other.inner);
    }

    /**
     * True if this time is the exact same instant as other.
     * Used for >= boundary checks: isEqual(boundary) || isAfter(boundary)
     *
     * 09:00.isEqual(09:00) → true
     * 09:00.isEqual(09:01) → false
     */
    public boolean isEqual(LocalDateTime other) {
        return this.inner.isEqual(other.inner);
    }

    /**
     * True if this time is before or exactly equal to other.
     * Equivalent to: isBefore(other) || isEqual(other)
     *
     * Convenience method for range checks in the state machine.
     */
    public boolean isBeforeOrEqual(LocalDateTime other) {
        return isBefore(other) || isEqual(other);
    }

    /**
     * True if this time is after or exactly equal to other.
     * Equivalent to: isAfter(other) || isEqual(other)
     *
     * Convenience method for >= boundary transitions.
     */
    public boolean isAfterOrEqual(LocalDateTime other) {
        return isAfter(other) || isEqual(other);
    }

    // =========================================================
    // DISPLAY & EQUALITY
    // =========================================================

    /**
     * Human-readable string representation for Swing UI clock labels
     * and AuditEntry log messages.
     *
     * Output format: "2026-04-10 09:30"
     */
    @Override
    public String toString() {
        return this.inner.format(DISPLAY_FORMAT);
    }

    /**
     * Value-based equality — two LocalDateTime objects are equal
     * if they represent the same instant in time.
     *
     * Required for HashMap keys in the MatchEngine score map.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LocalDateTime)) return false;
        LocalDateTime other = (LocalDateTime) obj;
        return this.inner.equals(other.inner);
    }

    @Override
    public int hashCode() {
        return this.inner.hashCode();
    }
}
