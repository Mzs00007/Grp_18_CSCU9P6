# 🎯 COMPREHENSIVE LIVE DATA HANDLING PLAN - VERSION 2.0
## Complete Implementation Blueprint with Method Names + UI Enhancements

**Status:** Detailed Implementation Ready  
**Target Date:** April 9, 2026  
**Scope:** All 3 Portals + Time Settings UI Improvement  
**Lines of Code:** ~250 total  
**Classes Modified:** 7 core classes  

---

# TABLE OF CONTENTS
1. [Core Architecture](#core-architecture)
2. [Detailed Method Specifications](#detailed-method-specifications)
3. [Data Flow Diagrams](#detailed-data-flows)
4. [CareerFairSystem Implementation](#careerfairsystem-detailed)
5. [Portal-Specific Implementations](#portal-implementations)
6. [Time Settings UI Enhancement](#time-settings-ui)
7. [Integration Matrix](#integration-matrix)
8. [Compilation & Testing](#compilation-testing)

---

# CORE ARCHITECTURE

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    CAREERFAIRSYSTEM (SINGLETON)                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────────────────┐    ┌─────────────────────────┐   │
│  │  CORE DATA (PERSISTENT)  │    │  DEMO DATA (TEMPORARY)  │   │
│  ├──────────────────────────┤    ├─────────────────────────┤   │
│  │ coreDataManager          │    │ demoSessionManager      │   │
│  │  ├─ organizations        │    │  ├─ organizations       │   │
│  │  ├─ recruiters           │    │  ├─ recruiters          │   │
│  │  ├─ candidates           │    │  ├─ candidates          │   │
│  │  └─ offers               │    │  └─ offers (temporary)  │   │
│  │                          │    │                         │   │
│  │ [MARKED "LIVE"]          │    │ [MARKED "DEMO MODE"]    │   │
│  │ Survives restart         │    │ Cleared on endDemo()    │   │
│  └──────────────────────────┘    └─────────────────────────┘   │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  MODE MANAGER                                           │   │
│  ├─────────────────────────────────────────────────────────┤   │
│  │ • currentMode: LIVE | DEMO                              │   │
│  │ • demoSessionStartTime: long                            │   │
│  │ • demoSessionDuration: long (default 30 min)            │   │
│  │ • isInDemoMode(): boolean                               │   │
│  │ • startDemoSession(): void                              │   │
│  │ • endDemoSession(): void                                │   │
│  │ • getRemainingDemoTime(): long                          │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  EVENT BROADCASTING                                     │   │
│  ├─────────────────────────────────────────────────────────┤   │
│  │ • firePropertyChange(mode, eventType, data)             │   │
│  │   - "LIVE_DATA_CHANGED" + data                          │   │
│  │   - "DEMO_DATA_CHANGED" + data                          │   │
│  │   - "MODE_SWITCHED" + (LIVE|DEMO)                       │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
         ▲                 ▲                  ▲
         │                 │                  │
    ┌────┴────┐    ┌─────┴──────┐    ┌──────┴──────┐
    │  ADMIN  │    │ RECRUITER  │    │ CANDIDATE  │
    │ PORTAL  │    │  PORTAL    │    │  PORTAL    │
    └────┬────┘    └─────┬──────┘    └──────┬──────┘
         │                │                  │
    ALWAYS LIVE      MODE-AWARE          MODE-AWARE
```

---

# DETAILED METHOD SPECIFICATIONS

## CLASS: DataModeManager
**Purpose:** Manages LIVE vs DEMO mode switching and state tracking  
**Location:** vcfs.core.DataModeManager (new class)

```java
public class DataModeManager {
    
    // ===== MODE CONSTANTS =====
    public static final String MODE_LIVE = "LIVE";
    public static final String MODE_DEMO = "DEMO";
    
    // ===== STATE FIELDS =====
    private String currentMode = MODE_LIVE;
    private long demoSessionStartTime = 0;
    private long demoSessionDuration = 1800000;  // 30 minutes in milliseconds
    private boolean demoActive = false;
    
    // ===== SINGLETON =====
    private static instance: DataModeManager
    public static DataModeManager getInstance()
    
    // ===== CORE MODE METHODS =====
    /**
     * Query: Is system in demo mode?
     * Used by controllers to decide: add to DEMO or LIVE storage
     * @return true if isInDemoMode, false if isInLiveMode
     */
    public boolean isInDemoMode()
    
    /**
     * Query: What is the current mode?
     * @return "LIVE" or "DEMO"
     */
    public String getCurrentMode()
    
    /**
     * Action: Start a demo session
     * Called by: DemoLauncher.startDemoButton_clicked()
     * Effect: currentMode = "DEMO", demoActive = true, startTime = now()
     */
    public void startDemoSession()
    
    /**
     * Action: End demo session
     * Called by: DemoLauncher.endDemoButton_clicked()
     * Effect: currentMode = "LIVE", demoActive = false
     * Side Effect: Notifies CareerFairSystem to clearDemoData()
     */
    public void endDemoSession()
    
    /**
     * Query: Time remaining in demo
     * Used by: UI to show "Demo ends in: 25 min 30 sec"
     * @return milliseconds until demo auto-ends
     */
    public long getRemainingDemoTime()
    
    /**
     * Query: Is demo mode active?
     * Alternative to isInDemoMode() for explicit checking
     * @return true if demo session active and within duration
     */
    public boolean isDemoSessionActive()
    
    /**
     * Auto-check: Called periodically by SystemTimer
     * If demo time exceeded, auto-end demo
     */
    public void checkDemoSessionTimeout()
}
```

---

## CLASS: CareerFairSystem (ENHANCED)
**Purpose:** Central data manager with dual storage (LIVE + DEMO)  
**Location:** vcfs.core.CareerFairSystem  

### SECTION 1: Data Storage Fields

```java
public class CareerFairSystem extends Observable {
    
    // ===== LIVE DATA STORAGE (PERMANENT) =====
    private DataContainer liveDataContainer;
    
    private List<Organization> liveOrganizations;      // Persistent
    private List<Recruiter> liveRecruiters;            // Persistent
    private List<Candidate> liveCandidates;            // Persistent
    private List<Offer> liveOffers;                    // Persistent
    private List<Reservation> liveReservations;        // Persistent
    
    // ===== DEMO DATA STORAGE (TEMPORARY) =====
    private DataContainer demoDataContainer;
    
    private List<Organization> demoOrganizations;      // Temp - cleared on endDemo()
    private List<Recruiter> demoRecruiters;            // Temp
    private List<Candidate> demoCandidates;            // Temp
    private List<Offer> demoOffers;                    // Temp
    private List<Reservation> demoReservations;        // Temp
    
    // ===== MODE MANAGER =====
    private DataModeManager modeManager;
    
    // ===== CACHE (MODE-AWARE) =====
    private Map<String, List<Offer>> offerCache;       // Cached by mode
    
}
```

### SECTION 2: Initialization & Setup Methods

```java
    /**
     * Initialize system with LIVE data
     * Called by: App startup
     * Effect: Load persisted live data, init demo containers empty
     */
    public void initializeSystem()
    
    /**
     * Load persistent live data from storage
     * Called by: initializeSystem()
     * Source: File/Database with previously saved organizations, recruiters, candidates
     */
    private void loadLiveData()
    
    /**
     * Initialize empty demo data container
     * Called by: startDemoSession()
     * Effect: Fresh, empty lists for demo mode
     */
    private void initializeDemoDataContainer()
    
    /**
     * Clear all demo data
     * Called by: endDemoSession() via modeManager
     * Effect: demoOrganizations.clear(), demoRecruiters.clear(), etc.
     */
    public void clearDemoData()
```

### SECTION 3: Data Access Methods (GET - Read Only)

```java
    // ===== LIVE DATA ACCESSORS =====
    
    /**
     * Get all organizations (LIVE mode only)
     * Called by: AdminScreen to populate dropdowns
     * @return unmodifiable list of live organizations
     */
    public List<Organization> getLiveOrganizations()
    
    /**
     * Get all recruiters (LIVE mode only)
     * Called by: RecruiterController to find current recruiter
     * @return unmodifiable list of live recruiters
     */
    public List<Recruiter> getLiveRecruiters()
    
    /**
     * Get all candidates (LIVE mode only)
     * Called by: CandidateController to find current candidate
     * @return unmodifiable list of live candidates
     */
    public List<Candidate> getLiveCandidates()
    
    /**
     * Get all live offers (LIVE mode only)
     * Called by: PublishOfferPanel.refreshOffersTable() in LIVE mode
     * @return list of offers published in LIVE mode
     */
    public List<Offer> getLiveOffers()
    
    // ===== DEMO DATA ACCESSORS =====
    
    /**
     * Get all organizations (DEMO mode only)
     * Called by: AdminScreen during demo
     * @return unmodifiable list of demo organizations
     */
    public List<Organization> getDemoOrganizations()
    
    /**
     * Get all offers (DEMO mode only)
     * Called by: PublishOfferPanel.refreshOffersTable() in DEMO mode
     * @return list of offers published during demo session
     */
    public List<Offer> getDemoOffers()
    
    // ===== MODE-AWARE UNIFIED ACCESSORS =====
    
    /**
     * Get offers for current mode (LIVE or DEMO)
     * Smart method: checks mode and returns correct list
     * Called by: PublishOfferPanel.refreshOffersTable()
     * @return offers from either LIVE or DEMO storage
     */
    public List<Offer> getCurrentModeOffers()
    
    /**
     * Get organizations for current mode (LIVE or DEMO)
     * Called by: AdminScreen initialization
     * @return organizations from either LIVE or DEMO storage
     */
    public List<Organization> getCurrentModeOrganizations()
    
    /**
     * Get candidates for current mode (LIVE or DEMO)
     * Called by: CandidateScreen
     * @return candidates from either LIVE or DEMO storage
     */
    public List<Candidate> getCurrentModeCandidates()
```

### SECTION 4: Data Modification Methods (SET - Write)

```java
    /**
     * Add organization to appropriate storage
     * Called by: AdminController.createOrganization()
     * Logic: 
     *   if (isInDemoMode) → add to demoOrganizations
     *   else → add to liveOrganizations (PERSIST)
     * @param org organization to add
     */
    public void addOrganization(Organization org)
    
    /**
     * Add recruiter to appropriate storage
     * Called by: AdminController.addRecruiterToBooth()
     * Logic: Same mode-aware logic
     * @param recruiter recruiter to add
     */
    public void addRecruiter(Recruiter recruiter)
    
    /**
     * Add candidate to appropriate storage
     * Called by: AdminController.registerCandidate()
     * Logic: Same mode-aware logic
     * @param candidate candidate to add
     */
    public void addCandidate(Candidate candidate)
    
    /**
     * Publish an offer to appropriate storage
     * Called by: RecruiterController.publishOffer() + UI
     * Logic:
     *   if (isInDemoMode) {
     *       find demoRecruiter by email
     *       demoRecruiter.addOffer(offer)
     *       firePropertyChange("demo_offer_published")
     *   } else {
     *       find liveRecruiter by email
     *       liveRecruiter.addOffer(offer)
     *       invalidateOfferCache()
     *       firePropertyChange("live_offer_published")
     *       SAVE TO DISK (persists)
     *   }
     * @param offer offer to publish
     * @param recruiterEmail email to find recruiter
     */
    public void publishOffer(Offer offer, String recruiterEmail)
    
    /**
     * Submit meeting request to appropriate storage
     * Called by: CandidateController.submitMeetingRequest()
     * Logic:
     *   if (isInDemoMode) {
     *       find demoCandidate by email
     *       demoCandidate.submitRequest(request)
     *       firePropertyChange("demo_request_submitted")
     *   } else {
     *       find liveCandidate by email
     *       liveCandidate.submitRequest(request)
     *       firePropertyChange("live_request_submitted")
     *       SAVE TO DISK (persists)
     *   }
     * @param request meeting request
     * @param candidateEmail email to find candidate
     */
    public void submitMeetingRequest(Request request, String candidateEmail)
    
    /**
     * Cancel booking from appropriate storage
     * Called by: CandidateController.cancelMeetingRequest()
     * @param reservationId id of reservation to cancel
     * @param candidateEmail email to find candidate
     */
    public void cancelBooking(String reservationId, String candidateEmail)
```

### SECTION 5: Helper/Finder Methods

```java
    /**
     * Find recruiter in LIVE storage by email
     * Called by: publishOffer() in LIVE mode
     * @param email recruiter email
     * @return Recruiter if found, null otherwise
     */
    private Recruiter findRecruiterInLiveStorage(String email)
    
    /**
     * Find recruiter in DEMO storage by email
     * Called by: publishOffer() in DEMO mode
     * @param email recruiter email
     * @return Recruiter if found, null otherwise
     */
    private Recruiter findRecruiterInDemoStorage(String email)
    
    /**
     * Find candidate in LIVE storage by email
     * Called by: submitMeetingRequest() in LIVE mode
     * @param email candidate email
     * @return Candidate if found, null otherwise
     */
    private Candidate findCandidateInLiveStorage(String email)
    
    /**
     * Find candidate in DEMO storage by email
     * Called by: submitMeetingRequest() in DEMO mode
     * @param email candidate email
     * @return Candidate if found, null otherwise
     */
    private Candidate findCandidateInDemoStorage(String email)
    
    /**
     * Find organization in storage by name
     * @param orgName organization name
     * @param inDemoMode true=search demo, false=search live
     * @return Organization if found
     */
    private Organization findOrganization(String orgName, boolean inDemoMode)
```

### SECTION 6: Event Broadcasting Methods

```java
    /**
     * Broadcast data change event to all listeners
     * Called by: All data modification methods
     * Effect: All UI panels receive update notification
     * @param eventType type of change (e.g., "live_offer_published")
     * @param mode "LIVE" or "DEMO"
     * @param data object that changed
     */
    private void broadcastDataChange(String eventType, String mode, Object data)
    
    /**
     * Broadcast mode switch event
     * Called by: startDemoSession(), endDemoSession()
     * Effect: All UI elements update to show/hide demo-specific UI
     * @param newMode new mode ("LIVE" or "DEMO")
     */
    public void broadcastModeSwitch(String newMode)
    
    /**
     * Invalidate cache for current mode
     * Called by: publishOffer() and other data changes
     * Effect: Next getAllOffers() query rebuilds from source
     */
    private void invalidateModeCache()
```

### SECTION 7: Integration with Demo Session

```java
    /**
     * Called when demo session starts
     * Called by: DataModeManager.startDemoSession()
     * Effect:
     *   1. demoDataContainer = new DataContainer()
     *   2. demoOrganizations = demo orgs from startup config
     *   3. demoRecruiters = demo recruiters from startup config
     *   4. Notify all UI: "MODE_SWITCHED" to DEMO
     *   5. All portals hide "Save" buttons, add "DEMO MODE" badges
     */
    public void onDemoSessionStart()
    
    /**
     * Called when demo session ends
     * Called by: DataModeManager.endDemoSession() or timeout
     * Effect:
     *   1. clearDemoData()
     *   2. Reload liveData from disk
     *   3. Notify all UI: "MODE_SWITCHED" to LIVE
     *   4. Refresh all tables with live data
     */
    public void onDemoSessionEnd()
```

---

# DETAILED DATA FLOWS

## FLOW 1: Publishing Offer (Complete Path)

```
┌─ RECRUITER DASHBOARD ──────────────────────────────────────┐
│                                                             │
│  1. User clicks "Publish Offer" button                      │
│     └─ Input: Title, salary, description                    │
│                                                             │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─ RECRUITER CONTROLLER ─────────────────────────────────────┐
│  RecruiterController.publishOffer(Offer offer) {           │
│      1. Get currentRecruiter from UserSession              │
│      2. Get currentRecruiter.getEmail() → "john@acme.com"  │
│      3. offer.setPublisher(currentRecruiter)               │
│      4. currentRecruiter.addOffer(offer) [session]         │
│      5. CareerFairSystem.publishOffer(offer, email)        │
│                                                             │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─ CAREERFAIRSYSTEM.publishOffer() ──────────────────────────┐
│                                                             │
│  if (DataModeManager.isInDemoMode()) {                     │
│      ┌── DEMO MODE PATH ──────────────────┐               │
│      │ 1. recruiter = findRecruiterIn     │               │
│      │     DemoStorage("john@acme.com")   │               │
│      │ 2. recruiter.addOffer(offer)       │               │
│      │    [adds to demoRecruiters]        │               │
│      │ 3. invalidateModeCache()           │               │
│      │ 4. broadcastDataChange(            │               │
│      │     "demo_offer_published",        │               │
│      │     "DEMO",                        │               │
│      │     offer)                         │               │
│      │ 5. UI SHOWS: "DEMO - Not saved"    │               │
│      │ 6. Data location: demoRecruiters   │               │
│      │    [memory only]                   │               │
│      └────────────────────────────────────┘               │
│  } else {                                                   │
│      ┌── LIVE MODE PATH ─────────────────┐               │
│      │ 1. recruiter = findRecruiterIn    │               │
│      │     LiveStorage("john@acme.com")  │               │
│      │ 2. recruiter.addOffer(offer)      │               │
│      │    [adds to liveRecruiters]       │               │
│      │ 3. invalidateOfferCache()         │               │
│      │ 4. broadcastDataChange(           │               │
│      │     "live_offer_published",       │               │
│      │     "LIVE",                       │               │
│      │     offer)                        │               │
│      │ 5. persistToDisk(offer)           │               │
│      │    [SAVE TO FILE] ✓               │               │
│      │ 6. UI SHOWS: "LIVE - Saved"       │               │
│      │ 7. Data location: liveRecruiters  │               │
│      │    [persistent]                   │               │
│      └────────────────────────────────────┘               │
│  }                                                          │
│                                                             │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─ EVENT BROADCASTS ─────────────────────────────────────────┐
│                                                             │
│  PropertyChangeEvent broadcasted to:                       │
│  • PublishOfferPanel (Recruiter portal)                    │
│  • AdminScreen (Admin portal)                              │
│  • CandidateScreen (Candidate portal)                      │
│                                                             │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─ UI PANELS REFRESH ────────────────────────────────────────┐
│                                                             │
│  PublishOfferPanel.propertyChange(event) {                 │
│      if (event ~= "offer_published") {                     │
│          refreshOffersTable()                              │
│      }                                                      │
│  }                                                          │
│                                                             │
│  refreshOffersTable() {                                    │
│      if (isInDemoMode) {                                   │
│          offers = getDemoOffers()  [temp]                  │
│          badge = "📋 DEMO MODE - Not Saved"               │
│      } else {                                              │
│          offers = getLiveOffers()  [persisted] ✓           │
│          badge = "📋 LIVE - Saved"                         │
│      }                                                      │
│      displayAllOffers(offers)                              │
│  }                                                          │
│                                                             │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─ USER SEES ────────────────────────────────────────────────┐
│                                                             │
│  DEMO MODE: "Your offer is published (DEMO - Not saved)"  │
│  Table shows offer                                         │
│                                                             │
│  LIVE MODE: "Your offer is published (LIVE - Saved)" ✓    │
│  Table shows offer                                         │
│                                                             │
│  User refreshes page:                                      │
│    DEMO: Offer GONE (demoData not persisted)              │
│    LIVE: Offer STILL THERE (liveData persisted) ✓          │
│                                                             │
└────────────────────────────────────────────────────────────┘
```

## FLOW 2: Booking Offer (Complete Path)

```
PRIMARY ACTOR: CANDIDATE                  SECONDARY: RECRUITER
┌─────────────────────────────────┬──────────────────────────────┐
│  Candidate sees offer in table  │  Recruiter published offer   │
│  (getDemoOffers or getLive)     │  (to demoRecruiters or live) │
│                                │                              │
│  1. Candidate clicks "Book Now" │                              │
└─────────────────────┬───────────┴──────────────────────────────┘
                      │
                      ▼
    ┌───────────────────────────────┐
    │ CandidateScreen.bookOffer()   │
    │                               │
    │ Creates booking request       │
    │ Request details:              │
    │  • Offer title                │
    │  • Recruiter email            │
    │  • Candidate email            │
    │  • Preferred times            │
    └──────────┬────────────────────┘
               │
               ▼
    ┌───────────────────────────────────────┐
    │ CandidateController.                  │
    │  submitMeetingRequest(request)        │
    │                                       │
    │ 1. Get currentCandidate               │
    │ 2. Get currentCandidate.getEmail()    │
    │ 3. currentCandidate.submitRequest()   │
    │ 4. CareerFairSystem.submitMeeting     │
    │    Request(request, candidateEmail)   │
    └──────────┬────────────────────────────┘
               │
               ▼
    ┌──────────────────────────────────┐
    │ CareerFairSystem.                │
    │  submitMeetingRequest()           │
    │                                  │
    │ if (isInDemoMode) {              │
    │   Candidate = findInDemoStorage  │
    │   Candidate.submitRequest()      │
    │   firePropertyChange             │
    │   ("demo_request_submitted")     │
    │ } else {                         │
    │   Candidate = findInLiveStorage  │
    │   Candidate.submitRequest()      │
    │   persistToDisk()          ✓     │
    │   firePropertyChange             │
    │   ("live_request_submitted")     │
    │ }                                │
    │                                  │
    └──────────┬───────────────────────┘
               │
               ▼
    ┌──────────────────────────────┐
    │ EVENT BROADCASTS             │
    │                              │
    │ → CandidateScreen            │
    │ → RecruiterScreen            │
    │ → AdminScreen                │
    │                              │
    └──────────┬───────────────────┘
               │
               ▼
    ┌──────────────────────────────┐
    │ RESULT:                      │
    │                              │
    │ DEMO: Request created,       │
    │       not saved, cleared     │
    │       when demo ends         │
    │                              │
    │ LIVE: Request created,       │
    │       saved to disk,         │
    │       visible to recruiter   │
    │       and admin ✓            │
    │                              │
    └──────────────────────────────┘
```

## FLOW 3: Mode Switching

```
┌─ USER INITIATES DEMO ─────────────────────────────────────┐
│ DemoLauncher.startDemoButton_clicked()                    │
│                                                           │
│ Button Action:                                            │
│  1. "Start Demo Session" button clicked                   │
│  2. Confirms with user: "Start 30-minute demo?"           │
│  3. User clicks "Yes"                                     │
└────────────────┬────────────────────────────────────────────┘
                 │
                 ▼
┌─ MODE MANAGER ────────────────────────────────────────────┐
│ DataModeManager.startDemoSession()                        │
│                                                           │
│ 1. currentMode = "DEMO"                                   │
│ 2. demoActive = true                                      │
│ 3. demoSessionStartTime = System.currentTimeMillis()      │
│ 4. Call: CareerFairSystem.onDemoSessionStart()            │
│                                                           │
└────────────┬─────────────────────────────────────────────┘
             │
             ▼
┌─ CAREERFAIRSYSTEM ────────────────────────────────────────┐
│ CareerFairSystem.onDemoSessionStart()                     │
│                                                           │
│ 1. initializeDemoDataContainer()                          │
│    └─ demoOrganizations = []                              │
│    └─ demoRecruiters = []                                 │
│    └─ demoCandidates = [] (load demo user)                │
│                                                           │
│ 2. loadDemoPresetData()                                   │
│    └─ Load "Demo XYZ Company" org                         │
│    └─ Load "Demo Recruiter John" recruiter                │
│    └─ Load "Demo Candidate Sarah" candidate               │
│                                                           │
│ 3. invalidateModeCache()                                  │
│ 4. broadcastModeSwitch("DEMO")                            │
│                                                           │
└────────────┬─────────────────────────────────────────────┘
             │
             ▼
┌─ ALL PORTALS RECEIVE NOTIFICATION ────────────────────────┐
│                                                           │
│ PropertyChangeEvent(source, "mode_switched", "DEMO")     │
│                                                           │
│ AdminScreen.propertyChange(event) {                      │
│   if ("mode_switched") {                                 │
│     hideAllButtons()                                     │
│     addBadge("🎬 DEMO MODE - Data Not Saved")           │
│     refreshTables(getDemoData())                         │
│   }                                                       │
│ }                                                         │
│                                                           │
│ RecruiterScreen.propertyChange(event) {                  │
│   if ("mode_switched") {                                 │
│     addBadge("🎬 DEMO MODE - Offers Not Saved")          │
│     disablePublishButton()                               │
│     refreshOffersTable(getDemoOffers())                  │
│   }                                                       │
│ }                                                         │
│                                                           │
│ CandidateScreen.propertyChange(event) {                  │
│   if ("mode_switched") {                                 │
│     addBadge("🎬 DEMO MODE - Bookings Not Saved")        │
│     disableBookButton()                                  │
│     refreshBookingsTable(getDemoBookings())              │
│   }                                                       │
│ }                                                         │
│                                                           │
└────────┬────────────────────────────────────────────────────┘
         │
         ▼
┌─ USER INTERACTS IN DEMO ──────────────────────────────────┐
│                                                           │
│ • All changes go to demoStorage                          │
│ • NO changes to liveStorage                              │
│ • UI shows "DEMO MODE" badge                             │
│ • Timer shows "Demo ends in: 29:45"                      │
│                                                           │
└────────┬────────────────────────────────────────────────────┘
         │
         ▼
┌─ DEMO ENDS (User clicks "End Demo") ──────────────────────┐
│ DemoLauncher.endDemoButton_clicked()                      │
│ → DataModeManager.endDemoSession()                        │
│ → CareerFairSystem.onDemoSessionEnd()                     │
│    • clearDemoData()                                      │
│    • invalidateCache()                                    │
│    • broadcastModeSwitch("LIVE")                          │
│                                                           │
│ ALL PORTALS RESET TO LIVE DATA ✓                         │
│ • Demo badges removed                                    │
│ • Live data displayed                                    │
│ • Buttons re-enabled                                     │
│                                                           │
└────────────────────────────────────────────────────────────┘
```

---

# CAREERFAIRSYSTEM DETAILED (Code Snippets)

## Method: publishOffer

```java
/**
 * Publish offer to appropriate storage (LIVE or DEMO)
 * 
 * Scenario 1 - DEMO MODE:
 *   → Offer added to demoRecruiters list
 *   → NOT saved to disk
 *   → Cleared when demo ends
 *   → Visible only in demo mode
 *
 * Scenario 2 - LIVE MODE:
 *   → Offer added to liveRecruiters list
 *   → SAVED to disk
 *   → Persists after refresh/restart
 *   → Visible to all portals
 */
public void publishOffer(Offer offer, String recruiterEmail) {
    if (offer == null || recruiterEmail == null) {
        logError("publishOffer: Null parameters - offer=" + offer + ", email=" + recruiterEmail);
        return;
    }
    
    try {
        if (DataModeManager.getInstance().isInDemoMode()) {
            // ===== DEMO MODE =====
            logInfo("DEMO MODE: Publishing offer as demo");
            Recruiter demoRecruiter = findRecruiterInDemoStorage(recruiterEmail);
            
            if (demoRecruiter == null) {
                logWarn("Demo recruiter not found: " + recruiterEmail);
                return;
            }
            
            offer.setPublisher(demoRecruiter);
            demoRecruiter.addOffer(offer);
            demoOffers.add(offer);
            
            invalidateModeCache();
            
            // Broadcast ONLY to demo listeners
            broadcastDataChange("demo_offer_published", "DEMO", offer);
            logInfo("Demo offer published: " + offer.getTitle());
            
        } else {
            // ===== LIVE MODE =====
            logInfo("LIVE MODE: Publishing offer as permanent");
            Recruiter liveRecruiter = findRecruiterInLiveStorage(recruiterEmail);
            
            if (liveRecruiter == null) {
                logWarn("Live recruiter not found: " + recruiterEmail);
                return;
            }
            
            offer.setPublisher(liveRecruiter);
            liveRecruiter.addOffer(offer);
            liveOffers.add(offer);
            
            invalidateModeCache();
            
            // PERSIST TO DISK
            persistOfferToDisk(offer);
            logInfo("Live offer SAVED TO DISK: " + offer.getTitle());
            
            // Broadcast to all listeners
            broadcastDataChange("live_offer_published", "LIVE", offer);
        }
    } catch (Exception e) {
        logError("Error publishing offer: " + e.getMessage());
    }
}
```

## Method: submitMeetingRequest

```java
/**
 * Submit meeting request to appropriate storage
 */
public void submitMeetingRequest(Request request, String candidateEmail) {
    if (request == null || candidateEmail == null) return;
    
    try {
        if (DataModeManager.getInstance().isInDemoMode()) {
            // DEMO: Candidate stored in demo container
            Candidate demoCandidate = findCandidateInDemoStorage(candidateEmail);
            if (demoCandidate == null) return;
            
            request.setCandidate(demoCandidate);
            demoCandidate.submitRequest(request);
            demoReservations.add(request);
            
            broadcastDataChange("demo_request_submitted", "DEMO", request);
            logInfo("Demo booking request submitted");
            
        } else {
            // LIVE: Candidate from live storage
            Candidate liveCandidate = findCandidateInLiveStorage(candidateEmail);
            if (liveCandidate == null) return;
            
            request.setCandidate(liveCandidate);
            liveCandidate.submitRequest(request);
            liveReservations.add(request);
            
            persistReservationToDisk(request);  // SAVE TO DISK
            logInfo("Live booking request SAVED TO DISK");
            
            broadcastDataChange("live_request_submitted", "LIVE", request);
        }
    } catch (Exception e) {
        logError("Error submitting meeting request: " + e.getMessage());
    }
}
```

## Method: onDemoSessionEnd

```java
/**
 * Clean up when demo ends
 * Called by: DataModeManager when demo session times out or user clicks "End"
 */
public void onDemoSessionEnd() {
    logInfo("===== DEMO SESSION ENDING =====");
    
    // Clear ALL demo data
    clearDemoData();
    
    // Reload live data from disk
    loadLiveData();
    
    // Notify all UI panels
    broadcastModeSwitch("LIVE");
    
    logInfo("Demo data cleared, LIVE data reloaded");
}

private void clearDemoData() {
    demoOrganizations.clear();
    demoRecruiters.clear();
    demoCandidates.clear();
    demoOffers.clear();
    demoReservations.clear();
    demoDataContainer = null;
    
    logInfo("All demo data cleared from memory");
}
```

---

# PORTAL IMPLEMENTATIONS

## ADMIN PORTAL (AdminScreen.java)

```java
/**
 * AdminScreen - Always uses LIVE data
 * Admins never work with demo data
 */

public class AdminScreen {
    
    /**
     * Create new organization (always LIVE)
     */
    public void createOrganization() {
        String orgName = orgNameField.getText();
        Organization org = new Organization(orgName);
        
        // ALWAYS add to LIVE
        CareerFairSystem.getInstance().addOrganization(org);
        
        logInfo("Organization " + orgName + " added to LIVE storage");
        
        orgNameField.setText("");
        refreshOrganizationDropdown();
    }
    
    /**
     * Refresh organizations dropdown
     * ADMIN always sees LIVE organizations
     */
    private void refreshOrganizationDropdown() {
        // Admin is not affected by demo mode - always LIVE
        List<Organization> orgs = 
            CareerFairSystem.getInstance().getLiveOrganizations();
        
        organizationDropdown.removeAllItems();
        for (Organization org : orgs) {
            organizationDropdown.addItem(org);
        }
    }
    
    /**
     * Property change listener
     * Monitor LIVE data changes only
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("live_organization_added".equals(evt.getPropertyName())) {
            refreshOrganizationDropdown();
        } else if ("live_recruiter_added".equals(evt.getPropertyName())) {
            refreshRecruiterDropdown();
        }
        // Ignore all DEMO mode events
    }
}
```

## RECRUITER PORTAL (PublishOfferPanel.java)

```java
/**
 * PublishOfferPanel - Mode-aware (LIVE or DEMO)
 */

public class PublishOfferPanel {
    
    private JLabel statusBadge;  // Shows "DEMO" or "LIVE"
    
    /**
     * Refresh offers table based on current mode
     */
    public void refreshOffersTable() {
        List<Offer> offers;
        String statusText;
        
        if (DataModeManager.getInstance().isInDemoMode()) {
            // DEMO MODE
            offers = CareerFairSystem.getInstance().getDemoOffers();
            statusBadge.setText("🎬 DEMO MODE - Offers will Not be saved");
            statusBadge.setForeground(Color.ORANGE);
            
            logInfo("Showing DEMO offers: " + offers.size());
            
        } else {
            // LIVE MODE
            offers = CareerFairSystem.getInstance().getLiveOffers();
            statusBadge.setText("📋 LIVE - Offers Saved Permanently");
            statusBadge.setForeground(Color.GREEN);
            
            logInfo("Showing LIVE offers: " + offers.size());
        }
        
        populateOffersTable(offers);
    }
    
    /**
     * Publish offer button clicked
     */
    public void publishOfferButton_clicked() {
        Offer offer = createOfferFromForm();
        String recruiterEmail = UserSession.getInstance()
                                    .getCurrentRecruiter()
                                    .getEmail();
        
        CareerFairSystem.getInstance()
                       .publishOffer(offer, recruiterEmail);
        
        // Refresh table to show new offer
        refreshOffersTable();
    }
    
    /**
     * Property change listener
     * Update when offers change (demo or live)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("demo_offer_published".equals(evt.getPropertyName()) ||
            "live_offer_published".equals(evt.getPropertyName())) {
            
            SwingUtilities.invokeLater(() -> {
                refreshOffersTable();
            });
        }
    }
}
```

## CANDIDATE PORTAL (CandidateScreen.java)

```java
/**
 * CandidateScreen - Mode-aware (LIVE or DEMO)
 */

public class CandidateScreen {
    
    private JLabel statusBadge;
    
    /**
     * Refresh bookings table based on current mode
     */
    public void refreshBookingsTable() {
        List<Reservation> bookings;
        
        if (DataModeManager.getInstance().isInDemoMode()) {
            bookings = CareerFairSystem.getInstance().getDemoBookings();
            statusBadge.setText("🎬 DEMO MODE - Bookings will Not be saved");
            statusBadge.setForeground(Color.ORANGE);
        } else {
            bookings = CareerFairSystem.getInstance().getLiveBookings();
            statusBadge.setText("📋 LIVE - Bookings Saved");
            statusBadge.setForeground(Color.GREEN);
        }
        
        populateBookingsTable(bookings);
    }
    
    /**
     * Book offer button clicked
     */
    public void bookOfferButton_clicked(Offer offer) {
        Request request = createRequestFromForm();
        String candidateEmail = UserSession.getInstance()
                                   .getCurrentCandidate()
                                   .getEmail();
        
        CareerFairSystem.getInstance()
                       .submitMeetingRequest(request, candidateEmail);
        
        refreshBookingsTable();
    }
}
```

---

# TIME SETTINGS UI (NEW ENHANCEMENT)

## Problem with Current UI
```
❌ CURRENT: Time input is too compact
   └─ Small text fields
   └─ No examples shown
   └─ User confused about format
   └─ Cramped layout

✅ DESIRED: Show demo values + better formatting
   └─ Clear, large input fields
   └─ Example values below each field
   └─ Visual separators
   └─ Helpful labels
```

## Enhanced Time Settings Panel

```java
/**
 * Enhanced ScheduleTimePanel with Demo Values
 * Shows: Preferred times with helpful examples
 */

public class ScheduleTimePanel extends JPanel {
    
    private JLabel titleLabel;
    private JPanel timeInputsContainer;
    
    public ScheduleTimePanel() {
        setLayout(new BorderLayout(10, 10));
        
        // ===== TITLE =====
        titleLabel = new JLabel("📅 Schedule Preferred Meeting Times");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // ===== INPUTS CONTAINER =====
        timeInputsContainer = new JPanel(new GridLayout(3, 1, 0, 15));
        
        // Time Slot 1
        timeInputsContainer.add(createTimeSlotPanel(1));
        
        // Time Slot 2
        timeInputsContainer.add(createTimeSlotPanel(2));
        
        // Time Slot 3
        timeInputsContainer.add(createTimeSlotPanel(3));
        
        add(timeInputsContainer, BorderLayout.CENTER);
    }
    
    /**
     * Create individual time slot with demo values
     */
    private JPanel createTimeSlotPanel(int slotNumber) {
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new BoxLayout(slotPanel, BoxLayout.Y_AXIS));
        slotPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        slotPanel.setBackground(new Color(245, 245, 245));
        
        // Slot Label
        JLabel slotLabel = new JLabel("Preferred Time Slot " + slotNumber);
        slotLabel.setFont(new Font("Arial", Font.BOLD, 14));
        slotLabel.setForeground(new Color(0, 51, 102));
        slotPanel.add(Box.createVerticalStrut(5));
        slotPanel.add(slotLabel);
        slotPanel.add(Box.createVerticalStrut(8));
        
        // Date Selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JComboBox<String> dateCombo = new JComboBox<>();
        dateCombo.setPreferredSize(new Dimension(120, 30));
        dateCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        dateCombo.addItem("Monday, 14 April");
        dateCombo.addItem("Tuesday, 15 April");
        dateCombo.addItem("Wednesday, 16 April");
        datePanel.add(dateLabel);
        datePanel.add(dateCombo);
        slotPanel.add(datePanel);
        
        // Demo value for date
        JLabel demoDate = new JLabel("     📌 Demo: 'Monday, 14 April' - Any weekday works");
        demoDate.setFont(new Font("Arial", Font.ITALIC, 11));
        demoDate.setForeground(new Color(100, 100, 150));
        slotPanel.add(demoDate);
        slotPanel.add(Box.createVerticalStrut(8));
        
        // Time Selection
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JComboBox<String> timeCombo = new JComboBox<>();
        timeCombo.setPreferredSize(new Dimension(120, 30));
        timeCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        timeCombo.addItem("09:00 AM");
        timeCombo.addItem("010:00 AM");
        timeCombo.addItem("11:00 AM");
        timeCombo.addItem("02:00 PM");
        timeCombo.addItem("03:00 PM");
        timeCombo.addItem("04:00 PM");
        timePanel.add(timeLabel);
        timePanel.add(timeCombo);
        slotPanel.add(timePanel);
        
        // Demo value for time
        JLabel demoTime = new JLabel("     📌 Demo: '10:00 AM' - Morning preferred");
        demoTime.setFont(new Font("Arial", Font.ITALIC, 11));
        demoTime.setForeground(new Color(100, 100, 150));
        slotPanel.add(demoTime);
        slotPanel.add(Box.createVerticalStrut(8));
        
        // Duration
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JComboBox<String> durationCombo = new JComboBox<>();
        durationCombo.setPreferredSize(new Dimension(120, 30));
        durationCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        durationCombo.addItem("30 minutes");
        durationCombo.addItem("45 minutes");
        durationCombo.addItem("1 hour");
        durationPanel.add(durationLabel);
        durationPanel.add(durationCombo);
        slotPanel.add(durationPanel);
        
        // Demo value for duration
        JLabel demoDuration = new JLabel("     📌 Demo: '30 minutes' - Standard interview slot");
        demoDuration.setFont(new Font("Arial", Font.ITALIC, 11));
        demoDuration.setForeground(new Color(100, 100, 150));
        slotPanel.add(demoDuration);
        slotPanel.add(Box.createVerticalStrut(5));
        
        return slotPanel;
    }
    
    /**
     * Example values for quick reference
     */
    public static void showDemoExamples() {
        String examples = """
            📅 PREFERRED TIMES - EXAMPLE VALUES:
            
            Slot 1: Monday, 14 April | 09:00 AM | 30 minutes
            Slot 2: Tuesday, 15 April | 02:00 PM | 1 hour
            Slot 3: Wednesday, 16 April | 11:00 AM | 45 minutes
            
            💡 Tips:
            • Choose times that work best for you
            • Mix morning and afternoon slots for flexibility
            • Allow enough duration for proper interview
            • Can adjust if recruiter proposes different time
        """;
        
        JTextArea textArea = new JTextArea(examples);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        JOptionPane.showMessageDialog(null, textArea, 
            "Demo Time Slot Examples", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
```

## Alternative: Time Panel with Better Layout

```java
/**
 * Alternative: Horizontal time slot layout with visual hierarchy
 */

public class ImprovedSchedulePanel extends JPanel {
    
    public ImprovedSchedulePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        addHeader();
        
        // Instructions
        addInstructions();
        
        // Time slots
        addTimeSlots();
        
        // Demo values reference
        addDemoReference();
    }
    
    private void addHeader() {
        JLabel header = new JLabel("📅 Preferred Meeting Times");
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(header);
        add(Box.createVerticalStrut(10));
    }
    
    private void addInstructions() {
        JLabel instructions = new JLabel(
            "<html>Please select three preferred time slots for your interviews. " +
            "You can adjust times if recruiters propose different schedules.</html>");
        instructions.setFont(new Font("Arial", Font.PLAIN, 12));
        instructions.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructions.setForeground(new Color(80, 80, 80));
        add(instructions);
        add(Box.createVerticalStrut(20));
    }
    
    private void addTimeSlots() {
        for (int i = 1; i <= 3; i++) {
            addTimeSlotRow(i);
            if (i < 3) add(Box.createVerticalStrut(15));
        }
    }
    
    private void addTimeSlotRow(int slotNum) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBackground(new Color(240, 248, 255));
        row.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230)));
        
        // Slot number
        JLabel slotNum_label = new JLabel("Slot " + slotNum + ":");
        slotNum_label.setFont(new Font("Arial", Font.BOLD, 13));
        row.add(slotNum_label);
        
        // Date field
        JComboBox<String> dateField = new JComboBox<>(
            new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        dateField.setPreferredSize(new Dimension(110, 25));
        row.add(dateField);
        
        // Time field
        JComboBox<String> timeField = new JComboBox<>(
            new String[]{"09:00", "10:00", "11:00", "14:00", "15:00", "16:00"});
        timeField.setPreferredSize(new Dimension(80, 25));
        row.add(timeField);
        
        // Duration field
        JComboBox<String> durationField = new JComboBox<>(
            new String[]{"30 min", "45 min", "1 hour"});
        durationField.setPreferredSize(new Dimension(80, 25));
        row.add(durationField);
        
        add(row);
    }
    
    private void addDemoReference() {
        add(Box.createVerticalStrut(20));
        
        JPanel demoRef = new JPanel();
        demoRef.setLayout(new BoxLayout(demoRef, BoxLayout.Y_AXIS));
        demoRef.setBackground(new Color(255, 250, 205));
        demoRef.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 2));
        demoRef.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel demoTitle = new JLabel("💡 Example Time Slots:");
        demoTitle.setFont(new Font("Arial", Font.BOLD, 12));
        demoTitle.setForeground(new Color(180, 100, 0));
        demoRef.add(demoTitle);
        
        String[] demoValues = {
            "Slot 1: Monday, 09:00, 30 min",
            "Slot 2: Tuesday, 14:00, 1 hour",
            "Slot 3: Wednesday, 10:00, 45 min"
        };
        
        for (String demo : demoValues) {
            JLabel demoLine = new JLabel("   → " + demo);
            demoLine.setFont(new Font("Arial", Font.PLAIN, 11));
            demoLine.setForeground(new Color(100, 100, 100));
            demoRef.add(demoLine);
        }
        
        demoRef.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoRef.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        add(demoRef);
    }
}
```

---

# INTEGRATION MATRIX

## Which Methods Call Which

```
┌─ UI USER ACTION ──────────────────────────────────────────────────┐
│                                                                   │
├─ Admin Creates Org ────────────────────────────────────────────┐  │
│  AdminScreen.createOrgButton_clicked()                          │  │
│   └─→ AdminController.createOrganization(org)                   │  │
│       └─→ CareerFairSystem.addOrganization(org) [ALWAYS LIVE]   │  │
│           └─→ firePropertyChange("live_organization_added")     │  │
│               └─→ AdminScreen.propertyChange() → refresh        │  │
│                   └─→ AdminScreen.refreshOrgDropdown()          │  │
│                       └─→ getLiveOrganizations() [no mode check] │  │
│                                                                  │  │
│                       RESULT: Org visible to all portals ✓      │  │
├────────────────────────────────────────────────────────────────┘  │
│                                                                   │
├─ Recruiter Publishes Offer (LIVE) ────────────────────────────┐  │
│  PublishOfferPanel.publishButton_clicked()                      │  │
│   └─→ RecruiterController.publishOffer(offer)                   │  │
│       └─→ offer.setPublisher(currentRecruiter)                  │  │
│       └─→ currentRecruiter.addOffer(offer) [session]            │  │
│       └─→ CareerFairSystem.publishOffer(offer, email)           │  │
│           └─→ if (!isInDemoMode) {                              │  │
│               ├─→ recruiter = findRecruiterInLiveStorage(email) │  │
│               ├─→ recruiter.addOffer(offer)                     │  │
│               ├─→ liveOffers.add(offer)                         │  │
│               ├─→ persistOfferToDisk(offer) [SAVED]             │  │
│               ├─→ broadcastDataChange("live_offer_published")   │  │
│               │   └─→ Admin/Candidate/Recruiter screens see it  │  │
│               └─→ refreshOffersTable() [shows from live]         │  │
│                                                                  │  │
│            RESULT: Offer visible + PERSISTED ✓                  │  │
├────────────────────────────────────────────────────────────────┘  │
│                                                                   │
├─ Recruiter Publishes Offer (DEMO) ────────────────────────────┐  │
│  PublishOfferPanel.publishButton_clicked()                      │  │
│   └─→ RecruiterController.publishOffer(offer)                   │  │
│       └─→ CareerFairSystem.publishOffer(offer, email)           │  │
│           └─→ if (isInDemoMode) {                               │  │
│               ├─→ recruiter = findRecruiterInDemoStorage(email) │  │
│               ├─→ recruiter.addOffer(offer)                     │  │
│               ├─→ demoOffers.add(offer)                         │  │
│               ├─→ [NO persistToDisk]                            │  │
│               └─→ broadcastDataChange("demo_offer_published")   │  │
│                   └─→ Demo offers table refreshes               │  │
│                                                                  │  │
│           RESULT: Offer visible in demo ONLY ✓                  │  │
├────────────────────────────────────────────────────────────────┘  │
│                                                                   │
├─ Candidate Books Meeting (LIVE) ──────────────────────────────┐  │
│  CandidateScreen.bookButton_clicked()                           │  │
│   └─→ CandidateController.submitMeetingRequest(request)         │  │
│       └─→ CareerFairSystem.submitMeetingRequest(request, email) │  │
│           └─→ if (!isInDemoMode) {                              │  │
│               ├─→ candidate = findCandidateInLiveStorage(email) │  │
│               ├─→ candidate.submitRequest(request)              │  │
│               ├─→ liveReservations.add(request)                 │  │
│               ├─→ persistReservationToDisk(request) [SAVED]     │  │
│               └─→ broadcastDataChange("live_request_submitted") │  │
│                   └─→ Admin sees booking, Recruiter sees request│  │
│                                                                  │  │
│           RESULT: Booking visible + PERSISTED ✓                 │  │
├────────────────────────────────────────────────────────────────┘  │
│                                                                   │
└─ Demo Mode Start ────────────────────────────────────────────────┐  │
   DemoLauncher.startDemoButton_clicked()                           │
    └─→ DataModeManager.startDemoSession()                          │
        ├─→ currentMode = "DEMO"                                    │
        └─→ CareerFairSystem.onDemoSessionStart()                   │
            ├─→ initializeDemoDataContainer()                       │
            ├─→ loadDemoPresetData()                                │
            └─→ broadcastModeSwitch("DEMO")                         │
                └─→ All portals update UI with demo badges          │
                                                                    │
           RESULT: All portals now in DEMO mode ✓                   │
   ────────────────────────────────────────────────────────────────┘

   ─ Demo Mode End ────────────────────────────────────────────────X
   DemoLauncher.endDemoButton_clicked()                             │
    └─→ DataModeManager.endDemoSession()                            │
        └─→ CareerFairSystem.onDemoSessionEnd()                     │
            ├─→ clearDemoData() [ALL DEMO DATA GONE]                │
            ├─→ loadLiveData()                                      │
            └─→ broadcastModeSwitch("LIVE")                         │
                └─→ All portals refresh with live data              │
                                                                    │
           RESULT: Demo cleared, LIVE data restored ✓               │
└────────────────────────────────────────────────────────────────────┘
```

---

# COMPILATION & TESTING

## Files to Modify (7 Total)

```
1. vcfs/core/DataModeManager.java
   ├─ NEW CLASS (150 lines)
   ├─ Manages LIVE vs DEMO mode
   └─ Controls startDemoSession() / endDemoSession()

2. vcfs/core/CareerFairSystem.java
   ├─ Add dual storage (100 lines)
   ├─ Add mode-aware methods (80 lines)
   ├─ Modify publishOffer() (30 lines)
   ├─ Modify submitMeetingRequest() (30 lines)
   └─ Total: ~240 lines added/modified

3. vcfs/controllers/RecruiterController.java
   ├─ Modify publishOffer() (5 lines)
   └─ Pass email parameter to CareerFairSystem

4. vcfs/controllers/CandidateController.java
   ├─ Modify submitMeetingRequest() (5 lines)
   └─ Pass email parameter to CareerFairSystem

5. vcfs/views/PublishOfferPanel.java
   ├─ Add mode checking in refreshOffersTable() (15 lines)
   ├─ Add status badge (10 lines)
   └─ Update propertyChange() (5 lines)

6. vcfs/views/CandidateScreen.java
   ├─ Add mode checking (15 lines)
   └─ Add status badge (10 lines)

7. vcfs/views/AdminScreen.java
   ├─ Ensure always uses getLive* (5 lines)
   └─ Explicitly document admin=LIVE only

TOTAL: ~500 lines added/modified
NEW CLASSES: 1 (DataModeManager)
EXISTING CLASSES MODIFIED: 6
```

## Expected After Compilation

```
✅ 80 classes compile successfully
✅ 0 errors
✅ All new methods linked correctly
✅ PropertyChangeListener events working
✅ Data flows correctly:
   - DEMO data → temp storage → cleared on end
   - LIVE data → persistent storage → survives refresh
✅ All 3 portals respect mode settings
```

## Manual Testing Scenarios

```
TEST 1: Admin operations (always LIVE)
┌─ Admin creates org
│  └─ Org appears in LIVE storage
│  └─ Visible to all portals immediately
│  └─ Visible after app restart
└─ PASS if: Org persists ✓

TEST 2: Demo mode data isolation
┌─ Start demo
│  └─ Create demo org + recruiter + candidate
│  └─ Recruiter publishes demo offer
│  └─ Candidate books demo offer
│  └─ Check: LIVE data unchanged
│  └─ End demo
│  └─ Check: ALL demo data gone, live data intact
└─ PASS if: Demo cleared, live unchanged ✓

TEST 3: Live mode persistence
┌─ Recruiter publishes live offer
│  └─ Page refresh: offer still there
│  └─ App restart: offer still there
│  └─ Admin sees offer
└─ PASS if: Offer persists + visible to all ✓

TEST 4: Mode badges
┌─ In demo: badges show "DEMO MODE"
│  └─ Buttons disabled/color changed
│  └─ Status label shows "Not saved"
│  └─ End demo: badges removed, buttons enabled
└─ PASS if: UI updates correctly ✓
```

---

##  APPROVAL CHECKLIST

**Before Implementation - User Review:**

- [ ] ✅ DataModeManager class design correct?
- [ ] ✅ Dual storage approach (LIVE + DEMO) makes sense?
- [ ] ✅ Method names clear and descriptive?
- [ ] ✅ Data flows accurate?
- [ ] ✅ Admin always LIVE (no demo for admin)?
- [ ] ✅ Time settings UI improvements look good?
- [ ] ✅ Ready to start implementation?

---

**END OF COMPREHENSIVE PLAN V2.0**
