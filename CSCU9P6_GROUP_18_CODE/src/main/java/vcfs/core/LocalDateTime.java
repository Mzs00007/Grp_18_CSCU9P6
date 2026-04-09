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

    /**
     * Parse a LocalDateTime from a time string (HH:mm format).
     * Parse a time string (HH:mm) using the CURRENT system date.
     * CRITICAL FIX: Uses SystemTimer's current date, not hardcoded 2026-01-01.
     * 
     * Example: If systeme says April 7, 2026:
     * LocalDateTime.parse("09:30") → LocalDateTime(2026, 4, 7, 9, 30)
     * 
     * @param timeStr Time string in "HH:mm" format
     * @return LocalDateTime object on current system date
     * @throws IllegalArgumentException if format is invalid
     */
    public static LocalDateTime parse(String timeStr) {
        if (timeStr == null || !timeStr.matches("\\d{1,2}:\\d{2}")) {
            throw new IllegalArgumentException("Time must be in HH:mm format");
        }
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid hour or minute values");
        }
        
        // Get current system date from SystemTimer (CRITICAL FIX)
        LocalDateTime now = SystemTimer.getInstance().getNow();
        return new LocalDateTime(now.inner.getYear(), now.inner.getMonthValue(), 
                                 now.inner.getDayOfMonth(), hour, minute);
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
     * Return a NEW LocalDateTime advanced by the given number of hours.
     * Does NOT modify this object (immutable).
     *
     * @param hours Hours to advance
     * @return New LocalDateTime representing (this + hours)
     */
    public LocalDateTime addHours(long hours) {
        return new LocalDateTime(this.inner.plusHours(hours));
    }

    /**
     * Return a NEW LocalDateTime advanced by the given number of days.
     * Does NOT modify this object (immutable).
     *
     * @param days Days to advance
     * @return New LocalDateTime representing (this + days)
     */
    public LocalDateTime addDays(long days) {
        return new LocalDateTime(this.inner.plusDays(days));
    }

    /**
     * Return a NEW LocalDateTime advanced by the given number of minutes.
     * Alias for plusMinutes for test compatibility.
     *
     * @param minutes Minutes to advance
     * @return New LocalDateTime representing (this + minutes)
     */
    public LocalDateTime addMinutes(long minutes) {
        return new LocalDateTime(this.inner.plusMinutes(minutes));
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
     * Format this LocalDateTime using the provided DateTimeFormatter.
     * @param formatter The formatter to use
     * @return Formatted string
     */
    public String format(DateTimeFormatter formatter) {
        return this.inner.format(formatter);
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

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        java.time.LocalDateTime javaDateTime = java.time.LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
        return new LocalDateTime(javaDateTime);
    }
}


