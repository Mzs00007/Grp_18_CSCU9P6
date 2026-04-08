# 🏛️ VCFS COMPLETE ARCHITECTURE & ALGORITHM REFERENCE
**Synthesized from All Documentation**

---

## TABLE OF CONTENTS
1. System Architecture (MVC Pattern)
2. Core Algorithms (VCFS-001 through 004)
3. Class Dependency Map
4. System State Machine
5. Data Flow Diagrams
6. Algorithm Pseudocode with Examples

---

## PART 1: SYSTEM ARCHITECTURE

### 1.1 The Three Layers of MVC

```
┌────────────────────────────────────────────────────────────────┐
│                    USER INTERFACE LAYER (View)                │
│          ┌─────────────┬─────────────┬──────────────┐          │
│          │   Admin     │  Recruiter  │  Candidate   │          │
│          │   Screen    │   Screen    │   Screen     │          │
│          └──────┬──────┴──────┬──────┴──────┬───────┘          │
└─────────────────┼─────────────┼─────────────┼─────────────────┘
                  │             │             │
                CALLS        CALLS         CALLS
                  │             │             │
┌─────────────────┼─────────────┼─────────────┼─────────────────┐
│          BUSINESS LOGIC LAYER (Controller)                   │
│          ┌─────────────┬─────────────┬──────────────┐          │
│          │   Admin     │  Recruiter  │  Candidate   │          │
│          │  Controller │ Controller  │ Controller   │          │
│          └──────┬──────┴──────┬──────┴──────┬───────┘          │
└─────────────────┼─────────────┼─────────────┼─────────────────┘
                  │             │             │
              MUTATES        MUTATES       QUERIES
                  │             │             │
┌─────────────────┼─────────────┼─────────────┼─────────────────┐
│            DATA LAYER (Model)                                │
│     ┌────────────────────────────────────────────────┐       │
│     │    CareerFairSystem (Singleton Facade)        │       │
│     │    • Organizations, Booths, Offers            │       │
│     │    • Reservations, Meeting Sessions           │       │
│     │    • Candidates, Recruiters, Audit Log        │       │
│     │    • State Machine (Dormant→Preparing→...     │       │
│     │    • Algorithms (booking, collision detect)   │       │
│     └────────────────────────────────────────────────┘       │
│                                                              │
│     Supporting Models: Candidate, Recruiter, Offer,         │
│                       Reservation, MeetingSession, etc.      │
└──────────────────────────────────────────────────────────────┘
```

### 1.2 Why MVC?

**Problem**: Business logic mixed with UI → Hard to test, hard to change UI
**Solution**: Separation of concerns

```
BEFORE (No MVC):
  AdminButton.onClick() {
    try {
      Database.addOrganization(name);  ← Business logic IN UI
      refreshList();
    } catch(Exception e) {
      JOptionPane.showErrorDialog(...); ← UI in business logic
    }
  }
  PROBLEM: Can't test without GUI, can't change DB without changing UI

AFTER (MVC):
  AdminButton.onClick() {
    adminController.createOrganization(name);  ← Just delegates
  }
  
  AdminController.createOrganization(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name required");
    }
    Organization org = new Organization(name);
    system.addOrganization(org);
  }
  BENEFIT: Controller is pure business logic, testable without GUI
```

### 1.3 Package Structure

```
vcfs/
├── core/                           ← System-wide services
│   ├── CareerFairSystem.java       ← Singleton facade (orchestrates everything)
│   ├── CareerFair.java             ← State machine (Dormant, Preparing, BookingsOpen, etc.)
│   ├── SystemTimer.java            ← Observable time (fires events on tick)
│   ├── Logger.java                 ← Centralized logging
│   ├── LogLevel.java               ← Enum: INFO, WARNING, ERROR
│   └── LocalDateTime.java          ← Custom date/time (no java.time dependency)
│
├── models/                         ← Pure data (NO UI code)
│   ├── users/
│   │   ├── Candidate.java          ← Email, name, preferences, requests
│   │   ├── Recruiter.java          ← Email, booth assignment, offers
│   │   └── Admin.java              ← System administrator
│   ├── booking/
│   │   ├── Offer.java              ← Bookable slot (30 min, offering recruiter, tags)
│   │   ├── Request.java            ← Candidate's search criteria (tags, cap, orgs)
│   │   ├── Reservation.java        ← Confirmed booking (Candidate + Offer + dates)
│   │   ├── MeetingSession.java     ← Active session (attendees, join/leave times)
│   │   ├── Lobby.java              ← Waiting room (who's waiting for booth)
│   │   └── VirtualRoom.java        ← Session container (manages lobby + active)
│   ├── structure/
│   │   ├── Organization.java       ← Company
│   │   └── Booth.java              ← Recruiter's station (has name + recruiter)
│   ├── audit/
│   │   ├── AttendanceRecord.java   ← Attendance outcome (ATTENDED, NO_SHOW, LEFT_EARLY)
│   │   └── AuditEntry.java         ← System event log entry
│   └── enums/                       ← Enumerations
│       ├── FairState.java          ← DORMANT, PREPARING, BOOKINGS_OPEN, etc.
│       ├── UserRole.java           ← ADMIN, RECRUITER, CANDIDATE
│       └── AttendanceStatus.java   ← ATTENDED, NO_SHOW, LEFT_EARLY
│
├── controllers/                    ← Business logic (processes user actions)
│   ├── AdminScreenController.java  ← Handles admin actions
│   ├── RecruiterController.java    ← Handles recruiter actions
│   ├── CandidateController.java    ← Handles candidate actions
│   └── TestClass.java              ← Test/debug helper
│
└── views/                          ← User interfaces (display data, capture input)
    ├── admin/
    │   └── AdministratorScreen.java ← Admin UI
    ├── recruiter/
    │   ├── RecruiterScreen.java     ← Recruiter dashboard
    │   ├── PublishOfferPanel.java   ← Form to create offer
    │   └── SchedulePanel.java       ← Shows recruiter's appointments
    ├── candidate/
    │   ├── CandidateScreen.java     ← Candidate dashboard
    │   └── BrowseOffersPanel.java   ← Browse & book interface
    └── shared/
        ├── VirtualRoomPanel.java    ← Virtual meeting interface
        ├── UIHelpers.java           ← Colors, fonts, dialogs (reusable)
        └── LoginFrame.java          ← Login screen
```

---

## PART 2: CORE ALGORITHMS (VCFS-001 through 004)

### 2.1 VCFS-001: Singleton Pattern

**What**: Only ONE instance of CareerFairSystem can exist  
**Why**: Global state must be shared across all user screens  
**How**: Private constructor + static getInstance()

```java
public class CareerFairSystem {
    // Step 1: Create static instance variable (null initially)
    private static CareerFairSystem instance = null;
    
    // Step 2: Make constructor PRIVATE (prevents new CareerFairSystem())
    private CareerFairSystem() {
        this.organizations = new ArrayList<>();
        this.candidates = new ArrayList<>();
        // ... initialize all state
    }
    
    // Step 3: Provide synchronized static getter
    public static synchronized CareerFairSystem getInstance() {
        if (instance == null) {
            instance = new CareerFairSystem();  // Created only once
        }
        return instance;  // Always returns same object
    }
}
```

**Usage**:
```java
// Anywhere in code:
CareerFairSystem system = CareerFairSystem.getInstance();
// ↑ Always the same object (same memory reference)
// ↑ Multiple calls return identical instance, no duplication
```

**Why Synchronized?**: If two threads call getInstance() simultaneously, without synchronized, two instances could be created. Synchronized ensures only one wins the race.

---

### 2.2 VCFS-002: Observer Pattern + Tick Mechanism

**What**: SystemTimer fires events → CareerFairSystem listens → State changes trigger  
**Why**: Fair needs to move from BookingsOpen → BookingsClosed → FairLive automatically  
**How**: Observer pattern + PropertyChangeEvent

```
┌─────────────────────┐
│   SystemTimer       │  Fires: propertyChange(PropertyChangeEvent)
│  (Observable)       │         • property = "tick"
│  • tick()           │         • oldValue = current time
│  • setTime()        │         • newValue = new time
└────────┬────────────┘
         │ propertyChange event
         │ (every 5 minutes or when manually advanced)
         │
┌────────▼────────────────────────┐
│ CareerFairSystem               │
│ (Observer, listens for events) │
│ • propertyChange(event) {      │
│     LocalDateTime now = (LocalDateTime)event.getNewValue();
│     fair.checkStateTransition(now);  ← Check if time to change state
│   }                            │
└────────┬──────────────────────┘
         │ Checks and updates state
         │
┌────────▼───────────────────────────┐
│ CareerFair.checkStateTransition()  │
│ • Is now >= BookingsOpenTime?      │
│   → Set state to BOOKINGS_OPEN     │
│ • Is now >= BookingsCloseTime?     │
│   → Set state to BOOKINGS_CLOSED   │
│ • Is now >= FairStartTime?         │
│   → Set state to FAIR_LIVE         │
└────────────────────────────────────┘
```

**Code Example**:

```java
// SystemTimer fires event (every 5 minutes)
public void tick() {
    LocalDateTime newTime = currentTime.plusMinutes(5);
    // oldValue = previous time, newValue = new time
    firePropertyChange("tick", previousTime, newTime);
}

// CareerFairSystem listens
@Override
public void propertyChange(PropertyChangeEvent evt) {
    if ("tick".equals(evt.getPropertyName())) {
        LocalDateTime newTime = (LocalDateTime) evt.getNewValue();
        fair.checkStateTransition(newTime);
    }
}

// Fair updates state based on timeline
public void checkStateTransition(LocalDateTime now) {
    if (now.isAfterOrEqual(bookingsOpenTime) && state == PREPARING) {
        this.state = BOOKINGS_OPEN;
        Logger.log(INFO, "Fair moved to BOOKINGS_OPEN");
    }
    if (now.isAfterOrEqual(fairStartTime) && state == BOOKINGS_CLOSED) {
        this.state = FAIR_LIVE;
        Logger.log(INFO, "Fair moved to FAIR_LIVE - booths now open");
    }
}
```

**Timeline Example**:
```
Time: April 7, 08:00 → Fair in DORMANT state
      ↓
Time: April 8, 10:00 (bookingsOpenTime) → Tick fires → State → BOOKINGS_OPEN
      ↓
Time: April 8, 14:00 (bookingsCloseTime) → Tick fires → State → BOOKINGS_CLOSED
      ↓
Time: April 8, 15:00 (fairStartTime) → Tick fires → State → FAIR_LIVE
      ↓
Time: April 8, 20:00 (fairEndTime) → Tick fires → State → DORMANT
```

---

### 2.3 VCFS-003: Offer Slot Generation Algorithm

**Problem**: Recruiter says "I'm available 09:00-12:00"  
**Solution**: Split that into six 30-minute bookable slots  
**Algorithm**: "Sliding window" over the availability block

```
INPUT: Availability Block "09:00-12:00" (3 hours)
       Offer Duration: 30 minutes
       Tags: ["Java", "Backend"]

PROCESS:
  Slot 1: 09:00 - 09:30  ← Start at 09:00, add 30 min, create Offer
  Slot 2: 09:30 - 10:00  ← Start at 09:30, add 30 min, create Offer
  Slot 3: 10:00 - 10:30  ← Start at 10:00, add 30 min, create Offer
  Slot 4: 10:30 - 11:00  ← Start at 10:30, add 30 min, create Offer
  Slot 5: 11:00 - 11:30  ← Start at 11:00, add 30 min, create Offer
  Slot 6: 11:30 - 12:00  ← Start at 11:30, add 30 min, create Offer
                               ↑ Stop here (12:00 is exactly end time)

OUTPUT: List<Offer> with 6 offers, each 30 min, all tagged ["Java", "Backend"]
```

**Pseudocode**:

```
Algorithm PARSE_AVAILABILITY_INTO_OFFERS(block, duration, tags)
  
  Result = empty list
  CurrentStart = block.startTime
  
  WHILE CurrentStart + duration <= block.endTime:
    CurrentEnd = CurrentStart + duration
    NewOffer = createOffer(CurrentStart, CurrentEnd, tags)
    Result.add(NewOffer)
    CurrentStart = CurrentEnd  ← Move to next slot's start time
  END WHILE
  
  RETURN Result
```

**Java Implementation** (from CareerFairSystem):

```java
public List<Offer> parseAvailabilityIntoOffers(
        AvailabilityBlock block, 
        int offerDuration, 
        List<String> tags) {
    
    List<Offer> offers = new ArrayList<>();
    LocalDateTime currentStart = block.getStartTime();
    LocalDateTime blockEnd = block.getEndTime();
    
    // While we can fit another offer in the remaining time
    while (currentStart.plusMinutes(offerDuration).isBeforeOrEqual(blockEnd)) {
        LocalDateTime currentEnd = currentStart.plusMinutes(offerDuration);
        
        // Create new Offer for this time slot
        Offer offer = new Offer(
            currentStart, 
            currentEnd, 
            block.getRecruiter(), 
            tags
        );
        offers.add(offer);
        
        // Move to next slot's start time
        currentStart = currentEnd;
    }
    
    Logger.log(INFO, "Generated " + offers.size() + " offers from block");
    return offers;
}
```

**Edge Cases Handled**:
- What if block.startTime = 09:00 and endTime = 09:20 (20 min)?
  → No offers generated (block too short for one 30-min offer)
- What if block.startTime = 09:00 and endTime = 09:30 (exactly 30 min)?
  → One offer generated (09:00-09:30)
- What if block.startTime = 09:00 and endTime = 09:31 (31 min)?
  → One offer generated (09:00-09:30), last minute wasted

---

### 2.4 VCFS-004: Auto-Booking Algorithm (Tag-Weighted Matching)

**Problem**: Candidate says "I want Java and Backend experience"  
**Problem 2**: Available times are 09:00, 11:00, and 14:00, all with same tag  
**Solution**: Score each offer by tag overlap, pick highest score (avoiding conflicts)

```
INPUT:
  Request: tags = ["Java", "Backend", "Microservices"]
  Available Offers:
    A: Time=09:00-09:30, Recruiter=Google, tags=["Java", "Backend"]    ← score=2
    B: Time=11:00-11:30, Recruiter=Microsoft, tags=["Backend"]        ← score=1
    C: Time=14:00-14:30, Recruiter=Meta, tags=["Java"]                ← score=1
  Already Booked: None

MATCHING PROCESS:
  Score Offer A: intersection(["Java","Backend","Microservices"], 
                              ["Java","Backend"]) = ["Java","Backend"]
                 Score = 2 (matches 2 of candidate's desired tags)
  
  Score Offer B: intersection = ["Backend"] → Score = 1
  Score Offer C: intersection = ["Java"] → Score = 1
  
  PICK OFFER A (highest score = 2)
  NO COLLISION (candidate not booked at 09:00)
  
  CREATE RESERVATION:
    Candidate → Offer A
    Time → 09:00-09:30
    Status → CONFIRMED

OUTPUT: Reservation object booked for Offer A
```

**Collision Detection Matrix**:

```
Candidate's Existing Bookings: [09:00-09:30, 11:30-12:00]

Checking Offer at Time 10:00-10:30:
  09:00-09:30    |  10:00-10:30    ?  NO COLLISION
  └─────┘           └─────┘
  Offer 1         Offer being checked
  ↑ ends at 9:30, checked offer starts at 10:00 (no overlap)

Checking Offer at Time 09:15-09:45:
  09:00-09:30    |  09:15-09:45    ?  COLLISION!
  └─────┘        |  └─────┘
             Overlap from 09:15-09:30 → REJECT

Checking Offer at Time 11:30-12:00:
  11:30-12:00    |  11:30-12:00    ?  COLLISION (exactly same time)
  └─────┘           └─────┘
         ↑ Both have SAME time range → REJECT

Collision Logic:
  A and B collide IF: A.start < B.end AND B.start < A.end
```

**Pseudocode**:

```
Algorithm AUTO_BOOK(candidate, requestedTags, capacity)
  
  ValidOffers = []
  
  FOR EACH offer IN system.allOffers:
    // Step 1: Score by tag match
    score = countIntersection(requestedTags, offer.tags)
    
    // Step 2: Check for time collision with candidate's existing bookings
    hasCollision = FALSE
    FOR EACH existingReservation IN candidate.reservations:
      IF existingReservation.offer.conflictsWith(offer):
        hasCollision = TRUE
        BREAK
    END FOR
    
    // Step 3: Add to valid list if no collision
    IF NOT hasCollision:
      ValidOffers.add((score, offer))
    END IF
  END FOR
  
  // Step 4: Sort by score (highest first) and pick top offer
  SORT ValidOffers BY score DESC
  
  IF ValidOffers is empty:
    RETURN NULL  ← No suitable offer found
  ELSE:
    BestOffer = ValidOffers[0].offer
    NewReservation = create(candidate, BestOffer, CONFIRMED)
    RETURN NewReservation
  END IF
```

**Java Implementation**:

```java
public Reservation autoBook(Candidate candidate, List<String> desiredTags) {
    List<Offer> validOffers = new ArrayList<>();
    
    // Filter offers: no conflicts + has matching tags
    for (Offer offer : allOffers) {
        // Skip if candidate already booked at this time
        boolean conflicts = candidate.getReservations().stream()
            .anyMatch(res -> res.getOffer().conflictsWith(offer));
        
        if (!conflicts) {
            validOffers.add(offer);
        }
    }
    
    if (validOffers.isEmpty()) {
        Logger.log(WARN, "No suitable offers found for candidate: " + candidate.getEmail());
        return null;
    }
    
    // Score each valid offer by tag intersection
    Offer bestOffer = validOffers.stream()
        .max((o1, o2) -> {
            int score1 = countTagIntersection(desiredTags, o1.getTags());
            int score2 = countTagIntersection(desiredTags, o2.getTags());
            return Integer.compare(score1, score2);
        })
        .orElse(null);
    
    if (bestOffer == null) {
        return null;
    }
    
    // Create reservation
    Reservation res = new Reservation(candidate, bestOffer, CONFIRMED);
    candidate.addReservation(res);
    Logger.log(INFO, "Auto-booking succeeded: " + candidate.getEmail() + 
               " → " + bestOffer.getTimeSlot());
    
    return res;
}

private int countTagIntersection(List<String> desired, List<String> offerTags) {
    return (int) desired.stream()
        .filter(offerTags::contains)
        .count();
}
```

**Example Scenario**:

```
Candidate: Sarah (email: sarah@uni.co.uk)
Desired Tags: ["Java", "Python", "DevOps"]
Existing Bookings: 10:00-10:30 (Google)

System Has 4 Offers (Recruiter: Microsoft):
1. Time: 09:00-09:30, Tags: ["Java", "C++"]           → Score=1
2. Time: 10:00-10:30, Tags: ["Python"]               → Score=1 BUT COLLISION!
3. Time: 11:00-11:30, Tags: ["Java", "Python"]       → Score=2
4. Time: 14:00-14:30, Tags: ["DevOps", "GoLang"]     → Score=1

Valid Offers (no collision):
  - Offer 1: Score=1
  - Offer 3: Score=2 ← HIGHEST SCORE
  - Offer 4: Score=1

RESULT: Sarah is auto-booked to Offer 3 (11:00-11:30, ["Java", "Python"])
```

---

## PART 3: SYSTEM STATE MACHINE

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│                         FAIR LIFECYCLE                             │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘

   ┌──────────────────┐
   │    DORMANT       │
   │                  │
   │ (System rests    │
   │  between fairs)  │
   └────────┬─────────┘
            │
            │ Admin: Create org/booth/recruiter, set timeline
            │ Then: "Start Fair Lifecycle"
            │
   ┌────────▼──────────┐
   │   PREPARING       │
   │                   │
   │ Recruiters set up │
   │ offers &          │
   │ availability      │
   │ blocks            │
   └────────┬──────────┘
            │
            │ Time reaches: BookingsOpenTime
            │ (System AUTO-transitions via SystemTimer)
            │
   ┌────────▼──────────────┐
   │  BOOKINGS_OPEN        │
   │                       │
   │ Candidates can browse │
   │ and reserve slots     │
   │ (Manual or Auto)      │
   └────────┬──────────────┘
            │
            │ Time reaches: BookingsCloseTime
            │
   ┌────────▼──────────────┐
   │  BOOKINGS_CLOSED      │
   │                       │
   │ No more bookings      │
   │ Recruiters prep       │
   │ Candidates review     │
   │                       │
   └────────┬──────────────┘
            │
            │ Time reaches: FairStartTime
            │
   ┌────────▼───────────────┐
   │     FAIR_LIVE          │
   │                        │
   │ Virtual meeting rooms  │
   │ open. Candidates join  │
   │ Wait in lobby until    │
   │ appointment time       │
   │ then move to session   │
   │                        │
   └────────┬───────────────┘
            │
            │ Time reaches: FairEndTime
            │
   ┌────────▼──────────────┐
   │    DORMANT            │
   │                       │
   │ (Cycle repeats)       │
   └───────────────────────┘
```

**Timeline Configuration** (Admin sets these):
```
April 1, 08:00 → DORMANT (sleeping)
April 8, 10:00 → BOOKINGS_OPEN (candidates can book)
April 8, 14:00 → BOOKINGS_CLOSED (no more bookings)
April 8, 15:00 → FAIR_LIVE (meetings start)
April 8, 20:00 → DORMANT (fair ends)
```

---

## PART 4: CLASS DEPENDENCY DIAGRAM

```
                    ┌─────────────────────────┐
                    │  CareerFairSystem       │
                    │  (Singleton Facade)     │  Listens to
                    │                         │  SystemTimer
                    └────────┬────────────────┘
                             │
                    Delegates to
                             │
                    ┌────────▼────────────────┐
                    │  CareerFair             │  State machine
                    │  (5 states)             │  Manages timeline
                    └────────────────────────┘

     ┌──────────────────────┬──────────────────────┐
     │                      │                      │
     ▼                      ▼                      ▼
┌─────────────┐      ┌─────────────┐      ┌────────────────┐
│ Models      │      │ Controllers │      │ Views          │
│             │      │             │      │                │
│ • Candidate │◄─────┤ AdminScreen │─────►│AdminScreen     │
│ • Recruiter │      │Controller   │      │(GUI)           │
│ • Offer     │      │             │      │                │
│ • Reserver  │◄─────┤ Recruiter   │─────►│RecruiterScreen │
│ • Booth     │      │Controller   │      │(GUI)           │
│ • Org       │◄─────┤ Candidate   │─────►│CandidateScreen │
│             │      │Controller   │      │(GUI)           │
└─────────────┘      └─────────────┘      └────────────────┘

Controller Logic:
  View.UserAction → Controller.businessMethod()
  → Controller validates/transforms data
  → Controller calls Model.setters and Model.getters
  → Model processes business logic
  → Controller prepares result
  → View displays result
```

---

## PART 5: DATA FLOW EXAMPLE — Booking a Meeting

```
USER CLICKS "BOOK OFFER" IN CANDIDATE GUI
          ↓
CandidateView.bookButton.onClick()
          ↓
CandidateController.makeReservation(offerId)
          ↓
[VALIDATION]
  • Is offer null? → Throw exception
  • Is candidate already booked at this time? → Throw exception
  • Is offer full (capacity reached)? → Throw exception
          ↓
[BUSINESS LOGIC]
  Candidate candidate = UserSession.getCurrentCandidate()
  Offer offer = system.getOfferById(offerId)
  
  Reservation res = new Reservation(
    candidate,      // Who?
    offer,          // When/Where?
    LocalDateTime.now(),  // When booked?
    CONFIRMED       // Status
  )
  
  candidate.addReservation(res)
  offer.incrementBookingCount()
  system.addReservation(res)
          ↓
[LOGGING & NOTIFICATIONS]
  Logger.log(INFO, "Booking confirmed for " + candidate.getEmail())
  CreateNotification("Your booking is confirmed")
          ↓
[RETURN TO VIEW]
  CandidateView.showConfirmation(res)
          ↓
GUI UPDATES TO SHOW NEW RESERVATION
```

---

## PART 6: TESTING STRATEGY (JUnit)

### Unit Test Pattern

```java
@Test
void testMethodName_Scenario_ExpectedOutcome() {
    // ARRANGE: Set up test data
    Candidate candidate = new Candidate("test@uni.co.uk", "Test", "Preference");
    Offer offer = new Offer(time1, time2, recruiter, tags);
    
    // ACT: Call the method being tested
    Reservation res = candidate.book(offer);
    
    // ASSERT: Verify the result
    assertNotNull(res);
    assertEquals(CONFIRMED, res.getStatus());
    assertTrue(candidate.hasReservation(offer));
}
```

### Tests for Booking Algorithm

```java
@Test
void testAutoBook_WithTagMatch_ReturnsHighestScoringOffer() {
    // ARRANGE
    Candidate candidate = new Candidate("sarah@uni.co.uk", "Sarah", "");
    Offer offer1 = createOffer(09:00, ["Java"]); // Score = 1
    Offer offer2 = createOffer(11:00, ["Java", "Python"]); // Score = 2
    Offer offer3 = createOffer(14:00, ["Python"]); // Score = 1
    
    List<String> desiredTags = ["Java", "Python"];
    
    // ACT
    Reservation res = system.autoBook(candidate, desiredTags);
    
    // ASSERT
    assertNotNull(res);
    assertEquals(offer2, res.getOffer()); // Highest score
    assertEquals("11:00", res.getStartTime());
}

@Test
void testAutoBook_WithTimeConflict_SkipsConflictingOffer() {
    // Candidate already booked 10:00-10:30
    // Available offers: 10:00-10:30 (conflict), 11:00-11:30 (OK)
    // Should skip conflict and pick 11:00-11:30
    
    // ARRANGE
    Candidate candidate = new Candidate("sarah@uni.co.uk", "Sarah", "");
    candidate.addReservation(createReservation(10:00, 10:30));
    
    Offer conflicting = createOffer(10:00, ["Java"]);
    Offer ok = createOffer(11:00, ["Java"]);
    
    // ACT
    Reservation res = system.autoBook(candidate, ["Java"]);
    
    // ASSERT
    assertEquals(ok, res.getOffer()); // Conflict was skipped
}
```

---

**END OF ARCHITECTURE & ALGORITHM REFERENCE**

Use this document as:
- ✅ Reference while writing JUnit tests
- ✅ Evidence of algorithm understanding in exam/demo
- ✅ Validation checklist (do my tests cover all scenarios?)
- ✅ Paste snippets into documentation if needed

