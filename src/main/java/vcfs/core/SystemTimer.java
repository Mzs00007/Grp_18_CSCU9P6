package vcfs.core;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * VCFS Simulated Clock — Singleton + Observer Pattern.
 *
 * SINGLETON:
 *   Only ONE SystemTimer instance exists for the entire application lifetime.
 *   All classes access it via SystemTimer.getInstance().
 *   This guarantees a single shared source of truth for simulated time.
 *
 * OBSERVER (PropertyChangeSupport):
 *   When time advances (stepMinutes or jumpTo), a "time" PropertyChangeEvent
 *   is broadcast to ALL registered listeners simultaneously.
 *
 *   Registered listeners at startup:
 *     - CareerFairSystem : automatically re-evaluates FairPhase on every tick
 *     - SystemTimerScreen: updates the clock JLabel in the Swing UI
 *
 * Implemented by: Zaid — VCFS-001
 */
public class SystemTimer {

    // =========================================================
    // SINGLETON INFRASTRUCTURE
    // =========================================================

    /** The single permitted instance across the whole application */
    private static SystemTimer instance;

    /**
     * PRIVATE constructor — enforces Singleton.
     * Nobody outside this class can write 'new SystemTimer()'.
     * Initialises the clock to a default simulation start time.
     */
    private SystemTimer() {
        // Default start: April 1 2026 at 08:00
        // Admin can call jumpTo() to set a specific starting point from the UI
        this.now = new LocalDateTime(2026, 4, 1, 8, 0);
        System.out.println("[SystemTimer] Initialised at: " + this.now);
    }

    /**
     * The ONLY way to obtain the SystemTimer.
     *
     * 'synchronized' prevents a race condition:
     * Without it, two threads could both see instance==null at the same time
     * and each create their own timer, breaking the Singleton guarantee.
     *
     * Usage from anywhere in the project:
     *   SystemTimer.getInstance().stepMinutes(30);
     *   SystemTimer.getInstance().getNow();
     */
    public static synchronized SystemTimer getInstance() {
        if (instance == null) {
            instance = new SystemTimer();
        }
        return instance;
    }

    // =========================================================
    // STATE
    // =========================================================

    /** The current simulated time — never null after construction */
    LocalDateTime now;

    // =========================================================
    // OBSERVER INFRASTRUCTURE
    // =========================================================

    /**
     * Java's built-in Observer engine from java.beans.
     * 'this' = SystemTimer is the event source object.
     * No external libraries required — ships with core Java SE.
     */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Register any class as a time-change listener.
     *
     * Called at app startup by:
     *   CareerFairSystem constructor: timer.addPropertyChangeListener(this)
     *   SystemTimerScreen constructor: timer.addPropertyChangeListener(this)
     *
     * After registering, the listener's propertyChange(evt) method is
     * called automatically whenever firePropertyChange() runs.
     *
     * @param listener Any class implementing java.beans.PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
        System.out.println("[SystemTimer] Listener registered: "
                + listener.getClass().getSimpleName());
    }

    // =========================================================
    // CORE METHODS
    // =========================================================

    /**
     * Return the current simulated time (read-only).
     * Callers cannot modify time via this method.
     */
    public LocalDateTime getNow() {
        return this.now;
    }

    /**
     * VCFS-001: Advance simulated time by the given number of minutes.
     *
     * After advancing, broadcasts a "time" PropertyChangeEvent to ALL
     * registered listeners so they can react automatically.
     *
     * Called by: AdminController.onStepTime(mins) when Admin clicks
     * the "Advance Time" button in the Swing UI.
     *
     * @param mins Positive number of minutes to advance
     * @throws IllegalArgumentException if mins is not positive
     */
    public void stepMinutes(int mins) {
        if (mins <= 0) {
            throw new IllegalArgumentException(
                    "[SystemTimer] stepMinutes requires a positive value. Got: " + mins);
        }
        LocalDateTime oldTime = this.now;
        this.now = this.now.plusMinutes(mins);

        System.out.println("[SystemTimer] +" + mins + "min  →  "
                + oldTime + " ──► " + this.now);

        // Broadcast: all registered listeners receive this automatically
        support.firePropertyChange("time", oldTime, this.now);
    }

    /**
     * VCFS-001: Jump the simulation directly to a specific point in time.
     *
     * Used when Admin wants to fast-forward to a specific scenario
     * (e.g. jump to fair start time) without stepping minute-by-minute.
     *
     * Also broadcasts the "time" PropertyChangeEvent so all listeners
     * (CareerFairSystem, SystemTimerScreen) respond immediately.
     *
     * @param time The target LocalDateTime to jump to (must not be null)
     * @throws IllegalArgumentException if time is null
     */
    public void jumpTo(LocalDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException(
                    "[SystemTimer] Cannot jump to null time.");
        }
        LocalDateTime oldTime = this.now;
        this.now = time;

        System.out.println("[SystemTimer] JUMP  →  " + this.now);

        // Broadcast to all registered listeners
        support.firePropertyChange("time", oldTime, this.now);
    }
}


