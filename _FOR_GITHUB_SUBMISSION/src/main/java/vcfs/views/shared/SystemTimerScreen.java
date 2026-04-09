package vcfs.views.shared;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import vcfs.core.LocalDateTime;
import vcfs.core.Logger;
import vcfs.core.LogLevel;
import vcfs.core.SystemTimer;

/**
 * System Timer Screen - Admin interface for time control.
 *
 * Responsibilities:
 * - Display current simulated time
 * - Provide buttons to step time forward by minutes
 * - Jump to a specific time for scenario testing
 *
 * Implementation by: Zaid
 */
public class SystemTimerScreen {


	/**
	 * STEP 1.3A: Advance simulated time by minutes.
	 * Delegates to SystemTimer.stepMinutes() which broadcasts to all listeners.
	 * 
	 * @param mins Number of minutes to advance (must be positive)
	 * @throws IllegalArgumentException if mins is not positive
	 */
	public void stepMinutes(int mins) {
		if (mins <= 0) {
			Logger.log(LogLevel.WARNING, "[SystemTimerScreen] Rejected stepMinutes: " + mins + " is not positive");
			throw new IllegalArgumentException("Minutes must be positive. Got: " + mins);
		}
		SystemTimer.getInstance().stepMinutes(mins);
		Logger.log(LogLevel.INFO, "[SystemTimerScreen] Advanced by " + mins + " minutes");
	}

	/**
	 * STEP 1.3B: Jump simulated time to a specific timestamp.
	 * Delegates to SystemTimer.jumpTo() which broadcasts to all listeners.
	 * 
	 * @param time Target LocalDateTime (must not be null and must be valid)
	 * @throws IllegalArgumentException if time is null
	 */
	public void jumpTo(LocalDateTime time) {
		if (time == null) {
			Logger.log(LogLevel.WARNING, "[SystemTimerScreen] Rejected jumpTo: time is null");
			throw new IllegalArgumentException("Target time cannot be null");
		}
		SystemTimer.getInstance().jumpTo(time);
		Logger.log(LogLevel.INFO, "[SystemTimerScreen] Jumped to " + time);
	}

}


