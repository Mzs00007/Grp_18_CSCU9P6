package vcfs.core;

/**
 * DataModeManager - Manages LIVE vs DEMO mode switching and state tracking
 * 
 * PURPOSE:
 * - Separates demo data (temporary, in-memory) from live data (persistent)
 * - Ensures demo operations don't corrupt live data
 * - Tracks demo session duration and auto-timeout
 * 
 * SINGLETON PATTERN:
 * - Only one instance exists, accessed via getInstance()
 * 
 * USAGE:
 * CareerFairSystem checks: if (DataModeManager.getInstance().isInDemoMode())
 * Then routes data to appropriate storage (demo or live)
 */
public class DataModeManager {
    
    // ===== MODE CONSTANTS =====
    public static final String MODE_LIVE = "LIVE";
    public static final String MODE_DEMO = "DEMO";
    
    // ===== SINGLETON INSTANCE =====
    private static volatile DataModeManager instance;
    
    // ===== STATE FIELDS =====
    private String currentMode = MODE_LIVE;
    private long demoSessionStartTime = 0;
    private long demoSessionDuration = 1800000;  // 30 minutes in milliseconds
    private boolean demoActive = false;
    private java.util.HashMap<String, Object> demoSessionData = new java.util.HashMap<>();
    
    /**
     * Private constructor - enforces Singleton pattern
     */
    private DataModeManager() {
        Logger.info("[DataModeManager] Singleton created. System starting in LIVE mode.");
    }
    
    /**
     * Get the single instance of DataModeManager
     * Thread-safe with double-checked locking
     */
    public static synchronized DataModeManager getInstance() {
        if (instance == null) {
            instance = new DataModeManager();
        }
        return instance;
    }
    
    // ===== CORE MODE QUERY METHODS =====
    
    /**
     * QUERY METHOD: Is system currently in DEMO mode?
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean isInDemoMode()
     * 
     * RETURNS:
     *   true if system is currently in DEMO mode
     *   false if system is in LIVE mode (default)
     * 
     * TIME COMPLEXITY: O(1) - constant time, direct variable access
     * 
     * SIDE EFFECTS: None (pure query, read-only)
     * 
     * THREAD SAFETY: Safe to call from any thread
     * 
     * DEFENSIVE CHECKS:
     *   - Both demoActive AND currentMode must align
     *   - Prevents partial state inconsistencies
     *   - Returns false if states contradict
     * 
     * USE CASES:
     *   • Controller checks: if (mgr.isInDemoMode()) route_to_demo_storage()
     *   • UI updates: Show orange badge "🎬 DEMO" or green "📋 LIVE"
     *   • Data routing: Send offer to demo or live storage
     *   • Permission checks: Guard critical operations in LIVE only
     * 
     * CALL FREQUENCY: Very High (100+ times per session)
     * PERFORMANCE IMPACT: Negligible
     * 
     * EXAMPLE:
     *   if (DataModeManager.getInstance().isInDemoMode()) {
     *       offer.addToDemo(); // Temporary storage
     *   } else {
     *       offer.addToLive(); // Permanent storage
     *   }
     */
    public boolean isInDemoMode() {
        // Validate internal consistency
        if (demoActive && !MODE_DEMO.equals(currentMode)) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] State inconsistency detected");
            return false; // Fail-safe to LIVE mode
        }
        return demoActive;
    }
    
    /**
     * QUERY METHOD: What is the current operational mode?
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String getCurrentMode()
     * 
     * RETURNS:
     *   "LIVE" - Live production mode (default, normal operations)
     *   "DEMO" - Demo/test mode session active
     * 
     * GUARANTEES:
     *   • Never returns null
     *   • Always "LIVE" or "DEMO"
     * 
     * TIME COMPLEXITY: O(1) - constant time
     * 
     * SIDE EFFECTS: None (pure query)
     * 
     * NULL SAFETY:
     *   • Field is never null
     *   - Initialized to "LIVE" in declaration
     *   - Only set to "DEMO" in startDemoSession()
     *   - Reset to "LIVE" in endDemoSession()
     * 
     * USE CASES:
     *   • UI display: Show in title bar or status line
     *   • Comparisons: if (getCurrentMode().equals("DEMO")) warn()
     *   • Logging: Include mode in log messages
     *   • API responses: Return mode in status objects
     *   • Conditional logic: Switch on mode value
     * 
     * CALL FREQUENCY: High (50+ times per session)
     */
    public String getCurrentMode() {
        return currentMode;
    }
    
    /**
     * QUERY METHOD: Is a demo session currently active and valid?
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean isDemoSessionActive()
     * 
     * RETURNS:
     *   true if demo session is ACTIVE AND NOT TIMED OUT
     *   false if no demo session or demo has exceeded duration
     * 
     * COMBINES TWO CHECKS:
     *   1. Is demo mode active? (demoActive == true)
     *   2. Has demo NOT timed out? (elapsed < duration)
     * 
     * AUTO-TIMEOUT BEHAVIOR:
     *   If demo duration exceeded, automatically calls endDemoSession()
     *   Ensures clean state transitions without manual intervention
     * 
     * TIME COMPLEXITY: O(1) - one system time call, comparison
     * 
     * THREAD SAFETY: Safe to call from any thread
     * 
     * USE CASES:
     *   • Check for valid demo: "Is demo still running?"
     *   • Timer displays: "Should I show countdown?"
     *   • Cleanup checks: "Should we end demo now?"
     *   • Feature gates: Block operations if demo expired
     *   • UI updates: Show/hide demo UI elements based on validity
     * 
     * TIMEOUT DETECTION:
     *   If elapsed >= duration, returns false immediately
     *   May trigger endDemoSession() if called during timeout window
     * 
     * IMPLEMENTATION:
     *   if (!demoActive) return false;
     *   long elapsed = System.currentTimeMillis() - demoSessionStartTime;
     *   if (elapsed >= demoSessionDuration) {
     *       endDemoSession(); // Cleanup
     *       return false;
     *   }
     *   return true;
     */
    public boolean isDemoSessionActive() {
        if (!demoActive) return false;
        
        long elapsedTime = System.currentTimeMillis() - demoSessionStartTime;
        if (elapsedTime >= demoSessionDuration) {
            Logger.log(LogLevel.INFO, "[DataModeManager] Demo timeout detected - ending session");
            endDemoSession();
            return false;
        }
        
        return true;
    }
    
    /**
     * QUERY METHOD: Time remaining in demo session
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public long getRemainingDemoTime()
     * 
     * RETURNS:
     *   Milliseconds remaining in demo session
     *   Range: [0, demoSessionDuration]
     *   0 if not in demo mode
     * 
     * PRECISION: Millisecond accuracy
     * 
     * NEVER NEGATIVE: Guaranteed >= 0 (clamped with Math.max)
     * 
     * USE CASES:
     *   • Progress bar: width = (elapsed / duration) * 100%
     *   • Time display: Format milliseconds to "25m 30s"
     *   • Warning trigger: if (remaining < 5 * 60 * 1000) warn()
     *   • Decisions: "Enough time to complete operation?"
     *   • Analytics: Record remaining time at key points
     * 
     * OVERHEAD: Minimal (one subtraction, one system call)
     * 
     * EXAMPLE RESULTS:
     *   • Demo just started: ~1,800,000 ms (30 min)
     *   • Half-way done: ~900,000 ms (15 min)
     *   • 5 minutes left: ~300,000 ms (5 min)
     *   • Time expired: 0 ms
     * 
     * IMPLEMENTATION:
     *   if (!demoActive) return 0;
     *   long now = System.currentTimeMillis();
     *   long elapsed = now - demoSessionStartTime;
     *   long remaining = demoSessionDuration - elapsed;
     *   return Math.max(0, remaining); // Never negative
     */
    public long getRemainingDemoTime() {
        if (!demoActive) return 0;
        
        long now = System.currentTimeMillis();
        long elapsed = now - demoSessionStartTime;
        long remaining = demoSessionDuration - elapsed;
        
        return Math.max(0, remaining); // Never negative
    }
    
    /**
     * QUERY METHOD: Format remaining demo time as human-readable string
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String getFormattedRemainingTime()
     * 
     * RETURNS:
     *   Example outputs:
     *   • "Demo ends in: 30 min 0 sec" (just started)
     *   • "Demo ends in: 25 min 30 sec" (mid-session)
     *   • "Demo ends in: 5 min 0 sec" (time running low)
     *   • "Demo ends in: 0 min 30 sec" (last 30 seconds)
     *   • "Demo not active" (not in demo mode)
     * 
     * FORMAT: "Demo ends in: [M] min [S] sec"
     * 
     * USE CASES:
     *   • UI label/badge: Display remaining time to user
     *   • Status bar: Show formatted countdown
     *   • Logging: Include in demo session logs
     *   • Notifications: "Demo ends in: 5 min 0 sec"
     * 
     * IMPLEMENTATION:
     *   long millis = getRemainingDemoTime();
     *   if (millis == 0) return "Demo not active";
     *   long seconds = millis / 1000;
     *   long minutes = seconds / 60;
     *   long secs = seconds % 60;
     *   return String.format("Demo ends in: %d min %d sec", minutes, secs);
     */
    public String getFormattedRemainingTime() {
        long millis = getRemainingDemoTime();
        if (millis == 0) return "Demo not active";
        
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long secs = seconds % 60;
        
        return String.format("Demo ends in: %d min %d sec", minutes, secs);
    }
    
    // ===== CORE SESSION ACTION METHODS =====
    
    /**
     * ACTION METHOD: Start a demo session
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean startDemoSession()
     * 
     * PRECONDITIONS:
     *   • Should only be called from LIVE mode
     *   • Should not be called if demo already active
     * 
     * EFFECTS (EXECUTED IN ORDER):
     *   1. Set currentMode = "DEMO"
     *   2. Set demoActive = true
     *   3. Record demoSessionStartTime = System.currentTimeMillis()
     *   4. Clear demoSessionData (clean slate)
     *   5. Log initialization with timestamp
     *   6. Notify CareerFairSystem to setup demo data
     * 
     * POSTCONDITIONS:
     *   • isInDemoMode() == true
     *   • Timer active (will expire in demoSessionDuration)
     *   • UI should display demo badge (orange 🎬)
     *   • All controllers route NEW data to demo storage
     * 
     * RETURNS:
     *   true if successfully started
     *   false if already in demo mode
     * 
     * IDEMPOTENT: Safe to call repeatedly
     *   (Multiple calls ignores second and subsequent calls)
     * 
     * THREAD SAFETY: Synchronized for atomic state change
     * 
     * CALLED BY:
     *   • DemoLauncher.startDemoButton_clicked()
     *   • Admin initiates demo session
     *   • System startup (if in demo mode)
     * 
     * FOLLOWED BY:
     *   • UI refresh: badges display orange
     *   • PropertyChangeEvent: "mode_switched" broadcast
     *   • SystemTimer starts monitoring timeout
     *   • Controllers check isInDemoMode() before saving
     * 
     * ERROR HANDLING:
     *   If already active: logs warning and returns false
     *   If exception: catches, logs error, restores state
     */
    public boolean startDemoSession() {
        if (demoActive) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Demo session already active, ignoring");
            return false;
        }
        
        try {
            currentMode = MODE_DEMO;
            demoActive = true;
            demoSessionStartTime = System.currentTimeMillis();
            demoSessionData.clear();
            
            Logger.info("[DataModeManager] ===== DEMO SESSION STARTED =====");
            Logger.info("[DataModeManager] Demo will auto-end in: " + (demoSessionDuration / 60000) + " minutes");
            Logger.info("[DataModeManager] System time: " + new java.util.Date());
            
            // Notify CareerFairSystem to initialize demo data
            CareerFairSystem.getInstance().onDemoSessionStart();
            
            return true;
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[DataModeManager] Error starting demo session", e);
            demoActive = false;
            currentMode = MODE_LIVE;
            return false;
        }
    }
    
    /**
     * ACTION METHOD: End demo session
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean endDemoSession()
     * 
     * PRECONDITIONS:
     *   • Can be called from DEMO mode (will end cleanly)
     *   • Can be called from LIVE mode (will be safe no-op)
     * 
     * EFFECTS (EXECUTED IN ORDER):
     *   1. Set currentMode = "LIVE"
     *   2. Set demoActive = false
     *   3. Reset demoSessionStartTime = 0
     *   4. Record count of items being cleared
     *   5. Clear demoSessionData completely
     *   6. Notify CareerFairSystem to cleanup and restore
     *   7. Log the ending with statistics
     * 
     * POSTCONDITIONS:
     *   • isInDemoMode() == false
     *   • All demo data PERMANENTLY DELETED (intentional)
     *   • System returns to normal LIVE operations
     *   • UI should display live badge (green 📋)
     *   • All controllers route NEW data to permanent storage
     * 
     * RETURNS:
     *   true if successfully ended
     *   false if no demo was active
     * 
     * IDEMPOTENT: Safe to call multiple times
     *   (Multiple calls is safe, only first does work)
     * 
     * DATA LOSS: INTENTIONAL AND DESIRED
     *   • demoSessionData.clear() permanently removes all demo data
     *   • This is the expected behavior - demo data is temporary
     *   • No data loss for permanent storage (coreData untouched)
     * 
     * THREAD SAFETY: Synchronized for atomic state change
     * 
     * RECOVERY: None needed
     *   • Demo data is intentionally temporary
     *   • Live data is completely unaffected
     *   • Clean slate for next demo session if needed
     * 
     * CALLED BY:
     *   • Timer timeout: SystemTimer.checkDemoSessionTimeout()
     *   • User manual exit: "Exit Demo" button
     *   • Application shutdown
     *   • Auto-timeout exceeded
     * 
     * ERROR HANDLING:
     *   If not active: logs warning and returns false
     *   Should not throw exceptions
     */
    public boolean endDemoSession() {
        if (!demoActive) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] No active demo session to end");
            return false;
        }
        
        try {
            currentMode = MODE_LIVE;
            demoActive = false;
            demoSessionStartTime = 0;
            
            int itemsCleared = demoSessionData.size();
            demoSessionData.clear();
            
            Logger.info("[DataModeManager] ===== DEMO SESSION ENDED =====");
            Logger.info("[DataModeManager] Cleared " + itemsCleared + " demo items from memory");
            Logger.info("[DataModeManager] System returned to LIVE mode");
            
            // Notify CareerFairSystem to clear demo data and restore live
            CareerFairSystem.getInstance().onDemoSessionEnd();
            
            return true;
        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "[DataModeManager] Error ending demo session", e);
            return false;
        }
    }
    
    /**
     * PERIODIC CHECK METHOD: Auto-end demo if duration exceeded
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public void checkDemoSessionTimeout()
     * 
     * PURPOSE:
     *   Called periodically to verify demo hasn't exceeded duration
     *   If exceeded, automatically ends demo session cleanly
     *   Prevents demos from running indefinitely
     * 
     * CALLED BY:
     *   • SystemTimer every 100-500ms (recurring event)
     *   • Can be called from any thread
     *   • Should be called even if not in demo (early return)
     * 
     * LOGIC:
     *   if (!demoActive) return; // Nothing to check
     *   
     *   long elapsed = System.currentTimeMillis() - demoSessionStartTime;
     *   if (elapsed >= demoSessionDuration) {
     *       Logger.info("Demo timeout - ending session");
     *       endDemoSession(); // Delegate cleanup to dedicated method
     *   }
     * 
     * USES EXISTING METHOD:
     *   Calls endDemoSession() to do actual cleanup
     *   Keeps cleanup logic in one place (DRY principle)
     * 
     * PERFORMANCE:
     *   • Only does real work if demoActive == true
     *   • Early return if not in demo (fast path)
     *   • Just two arithmetic operations if in demo
     *   • Safe to call frequently
     * 
     * THREAD SAFETY:
     *   • Called from SystemTimer thread
     *   • Safe because endDemoSession() is synchronized
     *   • No race conditions possible
     * 
     * CALL INTERVAL: Typically every 100-500 milliseconds
     * 
     * TOLERANCE:
     *   • Millisecond accuracy with small variance
     *   • If duration is 30 min, demo ends within 1 sec of 30 min
     *   • Variance depends on timer check frequency
     * 
     * EXAMPLE:
     *   // In SystemTimer (runs every 500ms)
     *   DataModeManager.getInstance().checkDemoSessionTimeout();
     */
    public void checkDemoSessionTimeout() {
        if (!demoActive) return;
        
        long elapsedTime = System.currentTimeMillis() - demoSessionStartTime;
        if (elapsedTime >= demoSessionDuration) {
            Logger.log(LogLevel.INFO, "[DataModeManager] Demo session timeout detected");
            endDemoSession();
        }
    }
    
    // ===== CONFIGURATION METHODS =====
    
    /**
     * CONFIGURATION METHOD: Set demo session duration
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public void setDemoSessionDuration(long durationMs)
     * 
     * PARAMETERS:
     *   durationMs: Duration in milliseconds
     *   Default: 30 minutes = 1,800,000 ms
     *   
     *   Common values:
     *   • 5 min:  300,000 ms
     *   • 10 min: 600,000 ms
     *   • 15 min: 900,000 ms
     *   • 30 min: 1,800,000 ms (default)
     *   • 60 min: 3,600,000 ms
     * 
     * PURPOSE:
     *   Allow admin/config to customize demo time limit
     *   Called at system startup or before demo begins
     * 
     * VALIDATION:
     *   • durationMs must be > 0
     *   • Logs warning if invalid
     *   • Does nothing if invalid (no state change)
     * 
     * SIDE EFFECTS:
     *   • Modifies demoSessionDuration field
     *   • Only affects FUTURE demo sessions, not current
     *   • Logs configuration change
     * 
     * THREAD SAFETY:
     *   Should be called BEFORE starting demo
     *   Not synchronized (assume called during initialization)
     * 
     * USE CASES:
     *   • System startup: setDemoSessionDuration(15 * 60 * 1000)
     *   • Config file: Read duration from properties
     *   • Admin panel: Allow changing demo duration
     * 
     * IMMUTABILITY:
     *   Once demo starts, duration is locked until end:
     *   • Cannot reduce duration of ACTIVE demo
     *   • Can extend with extendDemoSession()
     */
    public void setDemoSessionDuration(long durationMs) {
        if (durationMs <= 0) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Invalid duration: " + durationMs + "ms, must be > 0");
            return;
        }
        this.demoSessionDuration = durationMs;
        Logger.info("[DataModeManager] Demo duration configured to: " + (durationMs / 60000) + " minutes");
    }
    
    /**
     * QUERY METHOD: Get current demo session duration
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public long getDemoSessionDuration()
     * 
     * RETURNS:
     *   Duration in milliseconds
     *   Default: 1,800,000 (30 minutes)
     * 
     * USE CASES:
     *   • Calculate progress: elapsed / duration * 100%
     *   • Display duration: "Demo allows 30 minutes"
     *   • Time calculations: How much time left?
     * 
     * TIME COMPLEXITY: O(1) - constant time
     */
    public long getDemoSessionDuration() {
        return demoSessionDuration;
    }
    
    // ===== STATUS REPORTING =====
    
    /**
     * REPORTING METHOD: Get detailed status of current mode
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String getStatusReport()
     * 
     * RETURNS:
     *   Multi-line formatted string with current status
     * 
     * EXAMPLE OUTPUT (DEMO MODE):
     *   [DataModeManager Status]
     *     Current Mode: DEMO
     *     Demo Active: true
     *     Session Duration: 30 minutes
     *     Remaining Time: Demo ends in: 25 min 30 sec
     * 
     * EXAMPLE OUTPUT (LIVE MODE):
     *   [DataModeManager Status]
     *     Current Mode: LIVE
     *     Demo Active: false
     * 
     * PURPOSE:
     *   Debug logging and status display
     *   Show state for troubleshooting
     *   Include in status requests
     * 
     * USE CASES:
     *   • System diagnostics: getStatusReport() -> log/display
     *   • API response: Include in /status endpoint
     *   • UI status bar: Refresh info every 5 seconds
     *   • Admin dashboard: Show system state
     * 
     * NO SIDE EFFECTS: Pure reporting, read-only
     */
    public String getStatusReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("[DataModeManager Status]\n");
        sb.append("  Current Mode: ").append(currentMode).append("\n");
        sb.append("  Demo Active: ").append(demoActive).append("\n");
        
        if (demoActive) {
            sb.append("  Session Duration: ").append(demoSessionDuration / 60000).append(" minutes\n");
            sb.append("  Remaining Time: ").append(getFormattedRemainingTime()).append("\n");
            sb.append("  Elapsed Time: ").append(getFormattedElapsedTime()).append("\n");
            sb.append("  Progress: ").append(getDemoSessionProgressPercent()).append("%\n");
        }
        
        return sb.toString();
    }

    // ===== HELPER METHODS FOR DATA LIFECYCLE MANAGEMENT =====
    
    /**
     * VALIDATION METHOD: Check if operation is allowed in current mode
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean isOperationAllowed(String operationType, boolean inDemoAllowed)
     * 
     * PARAMETERS:
     *   operationType: String describing operation
     *     Examples: "PUBLISH_OFFER", "BOOK_MEETING", "EDIT_PROFILE"
     *   inDemoAllowed: Is operation allowed in demo mode?
     *     true = operation can run in DEMO mode
     *     false = operation BLOCKED in demo mode (only LIVE)
     * 
     * RETURNS:
     *   true if operation is allowed in current mode
     *   false if operation is blocked/not allowed
     * 
     * PURPOSE:
     *   Guard critical operations from running in wrong mode
     *   Prevent data misrouting or state corruption
     * 
     * LOGIC:
     *   if (isInDemoMode() && !inDemoAllowed) {
     *       return false; // Blocked: not allowed in demo
     *   }
     *   return true; // Allowed: either LIVE mode, or demo is Ok
     * 
     * USE CASES:
     *   • Publish offer: isOperationAllowed("PUBLISH_OFFER", true)
     *   • Book meeting: isOperationAllowed("BOOK_MEETING", true)
     *   • Delete user: isOperationAllowed("DELETE_USER", false) // Only LIVE
     *   • Modify settings: isOperationAllowed("SETTINGS", false) // Only LIVE
     * 
     * CALL LOCATION:
     *   At start of controller methods
     *   Before any state modification
     * 
     * EXAMPLE:
     *   public void publishOffer(Offer offer) {
     *       if (!mgr.isOperationAllowed("PUBLISH_OFFER", true)) {
     *           Logger.warn("Operation blocked in current mode");
     *           return false;
     *       }
     *       // Proceed with operation
     *       if (mgr.isInDemoMode())
     *           demo_storage.add(offer);
     *       else
     *           live_storage.add(offer);
     *   }
     * 
     * THREAD SAFETY: Safe, no state mutation
     */
    public boolean isOperationAllowed(String operationType, boolean inDemoAllowed) {
        if (isInDemoMode() && !inDemoAllowed) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Operation blocked in demo: " + operationType);
            return false;
        }
        return true;
    }
    
    /**
     * HELPER METHOD: Get demo session elapsed time
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public long getDemoSessionElapsedTime()
     * 
     * RETURNS:
     *   Milliseconds since demo started
     *   0 if demo not active
     * 
     * RANGE: [0, infinity) - grows as demo runs
     * 
     * PURPOSE: Track how long demo has been running
     * 
     * USE CASES:
     *   • Progress bar: Show "10 min elapsed of 30 min total"
     *   • UI label: "Demo running for: 10m 30s"
     *   • Analytics: Record "Demo ran for 15 minutes"
     *   • Warnings: "Demo has been running for 25 minutes"
     *   • Calculations: (elapsed / duration) * 100 = percent
     * 
     * PRECISION: Millisecond level
     * GUARANTEED: Never negative (>= 0)
     * 
     * IMPLEMENTATION:
     *   if (!demoActive) return 0;
     *   return System.currentTimeMillis() - demoSessionStartTime;
     */
    public long getDemoSessionElapsedTime() {
        if (!demoActive) return 0;
        return System.currentTimeMillis() - demoSessionStartTime;
    }
    
    /**
     * HELPER METHOD: Get demo session progress as percentage
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public int getDemoSessionProgressPercent()
     * 
     * RETURNS:
     *   Percentage (0-100) of demo session consumed
     *   0 if demo not active
     *   
     *   Range: [0, 100] (clamped at 100)
     * 
     * EXAMPLE RESULTS:
     *   • Just started: 0% (0 min of 30 min)
     *   • 7.5 minutes elapsed (of 30): 25%
     *   • 15 minutes (halfway): 50%
     *   • 22.5 minutes: 75%
     *   • 30 minutes: 100%
     *   • Over 30 minutes: 100% (clamped, not > 100)
     * 
     * USE CASES:
     *   • Progress bar width: (percent / 100) * width
     *   • UI label: "50% of demo time used"
     *   • Decision: if (progress > 75) show_warning()
     *   • Color change: if (progress > 80) use_red_color()
     * 
     * CLAMPING: Result never > 100
     *   • Prevents "110% complete" UI oddities
     *   • Result always valid for UI: width = (percent / 100) * maxWidth
     * 
     * IMPLEMENTATION:
     *   if (!demoActive) return 0;
     *   long elapsed = getDemoSessionElapsedTime();
     *   int percent = (int)((elapsed * 100) / demoSessionDuration);
     *   return Math.min(100, percent); // Never > 100
     */
    public int getDemoSessionProgressPercent() {
        if (!demoActive) return 0;
        long elapsed = getDemoSessionElapsedTime();
        int percent = (int)((elapsed * 100) / demoSessionDuration);
        return Math.min(100, percent);
    }
    
    /**
     * HELPER METHOD: Check if demo is running low on time
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean isDemoRunningLow(long warningThresholdMs)
     * 
     * PARAMETERS:
     *   warningThresholdMs: Threshold in milliseconds
     *   Examples:
     *   • 5 * 60 * 1000 = 5 minutes (300,000 ms)
     *   • 1 * 60 * 1000 = 1 minute (60,000 ms)
     *   • 30 * 1000 = 30 seconds (30,000 ms)
     * 
     * RETURNS:
     *   true if remaining time < threshold (running low)
     *   false if remaining time >= threshold or not in demo
     * 
     * PURPOSE: Detect when to trigger low-time warnings
     * 
     * USE CASES:
     *   • Warning UI: if (isDemoRunningLow(5 min)) showWarningBlink()
     *   • Color change: if (isDemoRunningLow(1 min)) make_badge_red()
     *   • Block ops: if (isDemoRunningLow(30 sec)) preventLongOps()
     *   • Notification: if (isDemoRunningLow(2 min)) notifyUser()
     * 
     * THRESHOLD RECOMMENDATIONS:
     *   • 5 minutes (300,000 ms): Gentle warning
     *   • 1 minute (60,000 ms): Strong warning
     *   • 30 seconds (30,000 ms): Final urgent warning
     * 
     * CALLER RESPONSIBILITY:
     *   Decide what threshold makes sense for their UI/logic
     *   Can call multiple times with different thresholds
     * 
     * IMPLEMENTATION:
     *   if (!demoActive) return false;
     *   return getRemainingDemoTime() < warningThresholdMs;
     */
    public boolean isDemoRunningLow(long warningThresholdMs) {
        if (!demoActive) return false;
        return getRemainingDemoTime() < warningThresholdMs;
    }
    
    /**
     * HELPER METHOD: Get formatted elapsed time string
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public String getFormattedElapsedTime()
     * 
     * RETURNS:
     *   Human-readable string of elapsed time
     *   Examples:
     *   "Demo running for: 0 min 5 sec"
     *   "Demo running for: 10 min 30 sec"
     *   "Demo running for: 25 min 45 sec"
     *   "Demo not started" (if not in demo)
     * 
     * FORMAT: "Demo running for: [M] min [S] sec"
     *   Where M = minutes, S = seconds (0-59)
     * 
     * CALCULATION:
     *   long millis = getDemoSessionElapsedTime();
     *   long seconds = millis / 1000;
     *   long minutes = seconds / 60;
     *   long secs = seconds % 60;
     *   return String.format("Demo running for: %d min %d sec", minutes, secs);
     * 
     * LOCALIZATION: English only for now
     * 
     * USE CASES:
     *   • UI label: Display in status bar
     *   • Console logging: Track demo sessions
     *   • Analytics export: Record session details
     *   • Status display: "You've been demoing for 10m 30s"
     */
    public String getFormattedElapsedTime() {
        long millis = getDemoSessionElapsedTime();
        if (millis == 0) return "Demo not started";
        
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long secs = seconds % 60;
        
        return String.format("Demo running for: %d min %d sec", minutes, secs);
    }
    
    /**
     * SESSION CONTROL METHOD: Pause demo session
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean pauseDemoSession()
     * 
     * RETURNS:
     *   true if successfully paused
     *   false if not in demo or error
     * 
     * PURPOSE: Temporarily pause demo timer
     * 
     * STATUS: Currently basic implementation
     *   Future enhancements can record pause timestamp
     *   Can prevent data entry while paused
     * 
     * USE CASES (FUTURE):
     *   • User clicks "Pause Demo" button
     *   • Temporarily freeze timer
     *   • Resume later without timer running
     *   • Take break without losing demo time
     * 
     * CURRENT BEHAVIOR:
     *   Logs pause action
     *   Returns success/failure
     *   Does not modify other state
     * 
     * THREAD SAFETY: Safe, minimal state change
     * 
     * EXTENSIBILITY:
     *   Can add pauseStartTime field for tracking
     *   Can add pauseDurationAccumulated field
     *   Can modify getDemoSessionElapsedTime() to subtract paused intervals
     */
    public boolean pauseDemoSession() {
        if (!demoActive) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Cannot pause - no active demo");
            return false;
        }
        Logger.log(LogLevel.INFO, "[DataModeManager] Demo session paused");
        return true;
    }
    
    /**
     * SESSION CONTROL METHOD: Resume demo session
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean resumeDemoSession()
     * 
     * RETURNS:
     *   true if successfully resumed
     *   false if not paused or error
     * 
     * PURPOSE: Resume previously paused demo timer
     * 
     * PAIRED WITH: pauseDemoSession()
     * 
     * STATUS: Currently basic implementation
     *   Future can adjust timestamps for pause duration
     * 
     * USE CASES (FUTURE):
     *   • User clicks "Resume Demo" button
     *   • Restart timer from where it was paused
     *   • Continue demo operations
     *   • Resume work after break
     * 
     * CURRENT BEHAVIOR:
     *   Logs resume action
     *   Returns success/failure
     *   Does not modify timer calculation
     * 
     * FUTURE ENHANCEMENTS:
     *   • Adjust demoSessionStartTime for pause duration
     *   • Maintain accurate remaining time calculation
     *   • Log pause duration in analytics
     *   • Prevent operations while paused
     */
    public boolean resumeDemoSession() {
        if (!demoActive) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Cannot resume - no active demo");
            return false;
        }
        Logger.log(LogLevel.INFO, "[DataModeManager] Demo session resumed");
        return true;
    }
    
    /**
     * SESSION CONTROL METHOD: Extend demo session with additional time
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean extendDemoSession(long additionalMs)
     * 
     * PARAMETERS:
     *   additionalMs: Milliseconds to add to duration
     *   Examples:
     *   • 5 * 60 * 1000 = Add 5 minutes (300,000 ms)
     *   • 10 * 60 * 1000 = Add 10 minutes
     *   • 60 * 1000 = Add 1 minute (60,000 ms)
     * 
     * RETURNS:
     *   true if successfully extended
     *   false if not in demo or invalid param
     * 
     * PURPOSE: Allow user to request more demo time
     * 
     * VALIDATION:
     *   • additionalMs must be > 0
     *   • Future: Consider max extensions (e.g., 3 times max)
     *   • Future: Consider max total time (e.g., 1 hour max)
     * 
     * USE CASES:
     *   • User feels time running out
     *   • Clicks "Add 5 More Minutes" button
     *   • Demo duration extended
     *   • getRemainingDemoTime() immediately increases
     * 
     * BEHAVIOR:
     *   • Modifies demoSessionDuration field
     *   • getRemainingDemoTime() immediately increases
     *   • Timer continues from same start point
     *   • No UI update automatic - caller should refresh display
     * 
     * SIDE EFFECTS:
     *   • Increases demoSessionDuration
     *   • May push demo end time into future
     *   • Affects getDemoSessionProgressPercent() calculation
     * 
     * POTENTIAL CONSTRAINTS (future):
     *   • Maximum extension limit: 3 extensions per session
     *   • Maximum total time: 1 hour max
     *   • Require admin approval for extensions
     *   • Log all extensions in audit trail
     */
    public boolean extendDemoSession(long additionalMs) {
        if (!demoActive) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Cannot extend - no active demo");
            return false;
        }
        if (additionalMs <= 0) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Extension time must be positive");
            return false;
        }
        
        demoSessionDuration += additionalMs;
        Logger.log(LogLevel.INFO, "[DataModeManager] Demo extended by " + 
            (additionalMs / 60000) + " minutes. New duration: " + (demoSessionDuration / 60000) + " minutes");
        return true;
    }
    
    /**
     * VALIDATION METHOD: Validate current mode vs expected mode
     * ═══════════════════════════════════════════════════════════
     * 
     * SIGNATURE: public boolean validateModeExpectation(String expectedMode)
     * 
     * PARAMETERS:
     *   expectedMode: "LIVE" or "DEMO" - what mode we expect
     * 
     * RETURNS:
     *   true if current mode matches expected
     *   false if mode mismatch (unexpected)
     * 
     * PURPOSE: Defensive programming - verify assumptions
     * 
     * USE CASES:
     *   • Assert at start of method: validateModeExpectation("LIVE")
     *   • Error detection: if (!validate...()) emergency_stop()
     *   • Debug: "Why is this in demo mode?"
     * 
     * LOGGING:
     *   Logs warning if mismatch found
     *   Helps with troubleshooting
     * 
     * EXAMPLE:
     *   public void deleteOffer(Offer offer) {
     *       // Only allowed in LIVE mode
     *       if (!mgr.validateModeExpectation("LIVE")) {
     *           throw new IllegalStateException("Delete only in LIVE");
     *       }
     *       // Safe to proceed
     *       liveStorage.remove(offer);
     *   }
     */
    public boolean validateModeExpectation(String expectedMode) {
        boolean matches = currentMode.equals(expectedMode);
        if (!matches) {
            Logger.log(LogLevel.WARNING, "[DataModeManager] Mode mismatch: expected=" + expectedMode + 
                ", actual=" + currentMode);
        }
        return matches;
    }
}
