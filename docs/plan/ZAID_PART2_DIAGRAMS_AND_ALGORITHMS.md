# Zaid's Personal Implementation Blueprint — Part 2 of 2
## Tickets: VCFS-003 & VCFS-004 + ALL Diagrams
### Project Manager | Group 9 | CSCU9P6

> This document is **exclusively for Zaid**. It covers VCFS-003 and VCFS-004
> and contains ALL architecture diagrams: Class, State Machine, Sequence, and Flowcharts.

---

# SECTION A: ALL DIAGRAMS

## Diagram 1 — Full System Class Diagram (Zaid's Files + Their Relationships)

```mermaid
classDiagram
    direction TB

    class LocalDateTime {
        -java.time.LocalDateTime inner
        +LocalDateTime(year, month, day, hour, minute)
        +plusMinutes(long mins) LocalDateTime
        +isBefore(LocalDateTime) boolean
        +isAfter(LocalDateTime) boolean
        +isEqual(LocalDateTime) boolean
        +isBeforeOrEqual(LocalDateTime) boolean
        +isAfterOrEqual(LocalDateTime) boolean
        +minutesUntil(LocalDateTime) long
        +toString() String
    }

    class SystemTimer {
        -static SystemTimer instance
        -LocalDateTime now
        -PropertyChangeSupport support
        -SystemTimer()
        +static getInstance() SystemTimer
        +getNow() LocalDateTime
        +stepMinutes(int mins) void
        +jumpTo(LocalDateTime time) void
        +addPropertyChangeListener(PCL) void
    }

    class CareerFair {
        +String name
        -FairPhase currentPhase
        -LocalDateTime bookingsOpenTime
        -LocalDateTime bookingsCloseTime
        -LocalDateTime startTime
        -LocalDateTime endTime
        +setTimes(LDT, LDT, LDT, LDT) void
        +evaluatePhase(LocalDateTime now) void
        +isInPhase(FairPhase) boolean
        +canBook(LocalDateTime) boolean
        +isLive(LocalDateTime) boolean
        +getCurrentPhase() FairPhase
    }

    class CareerFairSystem {
        -static CareerFairSystem instance
        -CareerFair fair
        -CareerFairSystem()
        +static getInstance() CareerFairSystem
        +propertyChange(PropertyChangeEvent) void
        +tick() void
        +getCurrentPhase() FairPhase
        +getFair() CareerFair
        +parseAvailabilityIntoOffers(...) int
        +autoBook(Candidate, Request) Reservation
        +cancelAsCandidate(...) void
        +cancelAsRecruiter(...) void
        +joinSession(Candidate, String) void
        -getAllOffers() List~Offer~
    }

    class FairPhase {
        <<enumeration>>
        DORMANT
        PREPARING
        BOOKINGS_OPEN
        BOOKINGS_CLOSED
        FAIR_LIVE
    }

    class Offer {
        +Recruiter publisher
        +LocalDateTime startTime
        +LocalDateTime endTime
        +int durationMins
        +String topicTags
        +String title
        +int capacity
        +Collection~Reservation~ reservations
        +getDurationMins() int
        +updateDetails(...) void
    }

    class Reservation {
        +Candidate candidate
        +Offer offer
        +MeetingSession session
        +ReservationState state
        +LocalDateTime scheduledStart
        +LocalDateTime scheduledEnd
        +cancel(String reason) void
        +isActive(LocalDateTime now) boolean
    }

    class Request {
        +Candidate requester
        +String desiredTags
        +String preferredOrgs
        +updatePreferences(...) void
    }

    class Candidate {
        +String id
        +String displayName
        +String email
        +Collection~Reservation~ reservations
        +createRequest(...) Request
        +cancelMyReservation(String) void
        +viewMySchedule() String
    }

    class Recruiter {
        +String id
        +String displayName
        +String email
        +Booth booth
        +Collection~Offer~ offers
        +publishOffer(...) Offer
        +cancelReservation(...) void
        +viewSchedule() String
    }

    class Organization {
        +String name
        +Collection~Booth~ booths
        +CareerFair fair
        +addBooth(Booth) void
        +removeBooth(Booth) void
    }

    class Booth {
        +String title
        +Organization organization
        +Collection~Recruiter~ recruiters
        +VirtualRoom room
        +assignRecruiter(Recruiter) void
        +removeRecruiter(Recruiter) void
        +getRoom() VirtualRoom
    }

    %% Relationships
    SystemTimer --> LocalDateTime : "stores 'now'"
    CareerFair --> LocalDateTime : "compares boundaries"
    CareerFair --> FairPhase : "currentPhase"
    CareerFairSystem --> CareerFair : "owns + evaluates"
    CareerFairSystem --> SystemTimer : "observes (PCL)"
    CareerFairSystem --> Organization : "manages collection"
    CareerFairSystem --> Offer : "autoBook() selects"
    CareerFairSystem --> Reservation : "autoBook() creates"
    Reservation --> Candidate : "links to"
    Reservation --> Offer : "links to"
    Reservation --> LocalDateTime : "scheduledStart/End"
    Offer --> Recruiter : "published by"
    Offer --> LocalDateTime : "startTime/endTime"
    Request --> Candidate : "belongs to"
    Recruiter --> Offer : "owns collection"
    Recruiter --> Booth : "assigned to"
    Organization --> Booth : "owns collection"
    Booth --> Recruiter : "has collection"
    Candidate --> Reservation : "owns collection"
    Candidate --> Request : "creates"
```

---

## Diagram 2 — State Machine: FairPhase Transitions

```mermaid
stateDiagram-v2
    direction LR

    [*] --> DORMANT : App launched

    DORMANT --> PREPARING : Admin calls setTimes()\n(all 4 boundaries configured)

    PREPARING --> BOOKINGS_OPEN : now >= bookingsOpenTime

    BOOKINGS_OPEN --> BOOKINGS_CLOSED : now >= bookingsCloseTime
    BOOKINGS_CLOSED --> FAIR_LIVE : now >= startTime

    FAIR_LIVE --> DORMANT : now > endTime

    note right of DORMANT
      No booking allowed.
      No room entry allowed.
      Admin can configure times.
    end note

    note right of PREPARING
      Times are set.
      Recruiters can publish offers (draft).
      Candidates can register profiles.
      No bookings yet.
    end note

    note right of BOOKINGS_OPEN
      canBook() returns TRUE.
      Candidates can autoBook() or manualBook().
      Recruiters publish availability slots via parser.
    end note

    note right of BOOKINGS_CLOSED
      canBook() returns FALSE.
      Schedules are locked in.
      System prepares for live event.
    end note

    note right of FAIR_LIVE
      isLive() returns TRUE.
      Virtual rooms open.
      Lobby Gatekeeper active.
      Sessions start/end.
    end note
```

---

## Diagram 3 — State Machine: MeetingSession Lifecycle

```mermaid
stateDiagram-v2
    direction LR

    [*] --> WAITING : Reservation confirmed\nMeetingSession created

    WAITING --> IN_PROGRESS : SystemTimer.now >= scheduledStart\nCareerFairSystem calls session.start()

    IN_PROGRESS --> ENDED : SystemTimer.now >= scheduledEnd\nCareerFairSystem calls session.end()

    ENDED --> [*] : AttendanceOutcome set\n(ATTENDED / NO_SHOW / ENDED_EARLY)

    note right of WAITING
      Lobby is active.
      Candidates who arrive early
      go to Lobby.add().
    end note

    note right of IN_PROGRESS
      VirtualRoom.enter() called.
      recordJoin() creates AttendanceRecord.
      recordLeave() updates record.
    end note

    note right of ENDED
      Taha's attendance calculator runs.
      AuditEntry logged.
      Reservation state → COMPLETED.
    end note
```

---

## Diagram 4 — State Machine: VirtualRoom States

```mermaid
stateDiagram-v2
    direction LR
    [*] --> CLOSED : Booth created
    CLOSED --> OPEN : MeetingSession starts\nroom.open()
    OPEN --> IN_SESSION : First candidate enters\nroom.enter(candidate)
    IN_SESSION --> OPEN : Last candidate leaves\nroom.leave(candidate)
    OPEN --> CLOSED : MeetingSession ends\nroom.close()
    IN_SESSION --> CLOSED : Emergency close\nor session force-ended
```

---

## Diagram 5 — Sequence: Full Observer Tick Flow (VCFS-001 + VCFS-002)

```mermaid
sequenceDiagram
    participant Admin as AdminController (YAMI)
    participant Timer as SystemTimer (Zaid)
    participant CFS as CareerFairSystem (Zaid)
    participant CF as CareerFair (Zaid)
    participant TSS as SystemTimerScreen
    participant CS as CandidateScreen (MJAMishkat)

    Note over CFS,Timer: ── APP STARTUP ──
    CFS->>Timer: addPropertyChangeListener(this)
    TSS->>Timer: addPropertyChangeListener(this)
    CS->>Timer: addPropertyChangeListener(this)

    Note over Admin,Timer: ── ADMIN CLICKS "ADVANCE 30 MIN" ──
    Admin->>Timer: stepMinutes(30)
    Timer->>Timer: oldTime = now
    Timer->>Timer: now = now.plusMinutes(30)
    Timer->>CFS: firePropertyChange("time", old, new)
    Timer->>TSS: firePropertyChange("time", old, new)
    Timer->>CS: firePropertyChange("time", old, new)

    Note over CFS,CF: ── AUTOMATIC PHASE EVALUATION ──
    CFS->>CFS: propertyChange(evt) fires tick()
    CFS->>CF: evaluatePhase(now)
    CF->>CF: if now >= bookingsOpenTime → BOOKINGS_OPEN
    CF->>CFS: currentPhase updated
    CFS->>CFS: System.out "[TICK] Phase: BOOKINGS_OPEN"

    Note over TSS: ── UI CLOCK AUTO-UPDATES ──
    TSS->>TSS: clockLabel.setText(newTime.toString())

    Note over CS: ── BOOKING BUTTON ENABLES ──
    CS->>CFS: getCurrentPhase()
    CFS-->>CS: BOOKINGS_OPEN
    CS->>CS: bookBtn.setEnabled(true)
```

---

## Diagram 6 — Sequence: autoBook() MatchEngine Full Flow (VCFS-004)

```mermaid
sequenceDiagram
    participant C as CandidateScreen
    participant CC as CandidateController
    participant CFS as CareerFairSystem (Zaid)
    participant CF as CareerFair (Zaid)
    participant Timer as SystemTimer
    participant Offers as Global Offer Pool

    C->>CC: onAutoBook(candidate, request)
    CC->>CFS: autoBook(candidate, request)

    CFS->>Timer: getNow()
    Timer-->>CFS: currentTime
    CFS->>CF: canBook(currentTime)
    CF->>CF: evaluatePhase(now)
    CF-->>CFS: true / false

    alt canBook() == false
        CFS-->>CC: return null
        CC-->>C: Show "Booking window closed" dialog
    end

    CFS->>Offers: getAllOffers()
    Offers-->>CFS: List of all Offer objects

    loop For each Offer in pool
        CFS->>CFS: COLLISION CHECK\nIs offer.startTime overlapping\nany candidate.reservations?

        alt Collision detected
            CFS->>CFS: skip this offer (continue)
        else No collision
            CFS->>CFS: SCORE: count tag intersections\ndesiredTags ∩ offer.topicTags
            CFS->>CFS: scoreMap.put(offer, score)
        end
    end

    CFS->>CFS: bestOffer = max(scoreMap by value)

    alt No offers scored > 0
        CFS-->>CC: return null
        CC-->>C: Show "No matching offers found"
    else Best offer found
        CFS->>CFS: Create new Reservation object
        CFS->>CFS: reservation.candidate = candidate
        CFS->>CFS: reservation.offer = bestOffer
        CFS->>CFS: reservation.state = CONFIRMED
        CFS->>CFS: candidate.reservations.add(reservation)
        CFS->>CFS: bestOffer.reservations.add(reservation)
        CFS-->>CC: return reservation
        CC-->>C: Show "Booked: [time] at [recruiter]" dialog
    end
```

---

## Diagram 7 — Sequence: Availability Parser (VCFS-003)

```mermaid
sequenceDiagram
    participant R as RecruiterScreen (Taha)
    participant RC as RecruiterController
    participant CFS as CareerFairSystem (Zaid)
    participant CF as CareerFair

    R->>RC: onPublishOffer(recruiter, title, 30, "AI,Java", 1, 09:00, 12:00)
    RC->>CFS: parseAvailabilityIntoOffers(recruiter, "AI,Java", 30, 1, 09:00, 12:00)

    CFS->>CF: canBook(now)
    CF-->>CFS: true

    CFS->>CFS: cursor = 09:00
    loop cursor + 30min <= 12:00
        CFS->>CFS: Create Offer(start=cursor, end=cursor+30min)
        CFS->>CFS: offer.topicTags = "AI,Java"
        CFS->>CFS: offer.publisher = recruiter
        CFS->>CFS: recruiter.offers.add(offer)
        CFS->>CFS: cursor = cursor + 30min
        Note right of CFS: Slot 1: 09:00→09:30\nSlot 2: 09:30→10:00\n...\nSlot 6: 11:30→12:00
    end
    CFS-->>RC: return 6 (slots created)
    RC-->>R: Show "6 slots published successfully"
```

---

# SECTION B: TICKET VCFS-003
## Availability Parser: Continuous Block → Discrete Offer Slots

### What Problem Are We Solving?

A recruiter says: **"I'm free from 09:00 to 12:00, each session is 30 minutes."**

Without a parser:
- Recruiter manually creates 6 Offer objects → tedious, error-prone
- Times could overlap → system breaks
- Can't scale to different durations

With the parser:
- Recruiter provides one simple block (start, end, duration)
- Parser automatically generates the exact correct number of non-overlapping slots
- Fully dynamic: works for 20-min slots, 45-min slots, any duration

---

### The Parser Flowchart

```mermaid
flowchart TD
    A["📥 Input: blockStart=09:00, blockEnd=12:00, durationMins=30"]
    B["Phase Guard:\nfair.canBook(now)?"]
    C["❌ Reject:\nBooking window not open"]
    D["Validation:\nblockEnd - blockStart >= durationMins?"]
    E["❌ Reject:\nBlock too short"]
    F["cursor = blockStart = 09:00"]
    G{"cursor + durationMins\n<= blockEnd?"}
    H["Create Offer:\nstart = cursor\nend = cursor + 30min\ntags = topicTags\npublisher = recruiter"]
    I["recruiter.offers.add(slot)"]
    J["cursor = cursor + 30min"]
    K["✅ Return total slots created"]

    A --> B
    B -- No --> C
    B -- Yes --> D
    D -- No --> E
    D -- Yes --> F
    F --> G
    G -- Yes --> H --> I --> J --> G
    G -- No --> K

    style C fill:#ff6b6b
    style E fill:#ff6b6b
    style K fill:#51cf66
```

---

### Fields to Add to `Offer.java`

Open `src/main/java/vcfs/models/booking/Offer.java` and add these fields alongside the existing ones:

```java
// === ADD THESE FIELDS to Offer.java ===
// (Zaid adds these to support the Availability Parser)

/** When this specific appointment slot begins */
LocalDateTime startTime;

/** When this specific appointment slot ends */
LocalDateTime endTime;

/** Display name for this offer type */
String title;

/** Comma-separated skill tags. e.g. "AI,Java,Cloud,ML" */
String topicTags;

/** Max candidates per slot (usually 1 for 1-on-1 meetings) */
int capacity;
```

---

### Full Parser Implementation (in `CareerFairSystem.java`)

```java
/**
 * ============================================================
 * VCFS-003: Availability Parser Algorithm
 * ============================================================
 * Converts a recruiter's continuous free-time block into a
 * collection of discrete, bookable Offer slot objects.
 *
 * Example:
 *   Input:  blockStart=09:00, blockEnd=12:00, durationMins=30
 *   Output: 6 Offer objects created:
 *           09:00→09:30, 09:30→10:00, 10:00→10:30,
 *           10:30→11:00, 11:00→11:30, 11:30→12:00
 *
 * @param recruiter    The Recruiter publishing their availability
 * @param title        Session title (e.g. "Software Engineer Chat")
 * @param durationMins Length of each individual slot in minutes
 * @param topicTags    Comma-separated skill tags for MatchEngine scoring
 * @param capacity     Max candidates per slot (typically 1)
 * @param blockStart   Start of the recruiter's availability window
 * @param blockEnd     End of the recruiter's availability window
 * @return             Number of discrete slots successfully generated
 *
 * Implemented by: Zaid (VCFS-003)
 * ============================================================
 */
public int parseAvailabilityIntoOffers(
        Recruiter recruiter,
        String title,
        int durationMins,
        String topicTags,
        int capacity,
        LocalDateTime blockStart,
        LocalDateTime blockEnd) {

    // === GUARD 1: Phase check — recruiters can only publish during BOOKINGS_OPEN ===
    if (!fair.canBook(SystemTimer.getInstance().getNow())) {
        System.out.println("[PARSER] ❌ Rejected — not in BOOKINGS_OPEN phase.");
        throw new IllegalStateException(
            "Cannot publish offers outside the booking window. "
            + "Current phase: " + fair.getCurrentPhase());
    }

    // === GUARD 2: The block must be long enough for at least one slot ===
    long blockDuration = blockStart.minutesUntil(blockEnd);
    if (blockDuration < durationMins) {
        throw new IllegalArgumentException(
            "[PARSER] ❌ Block too short. Block=" + blockDuration
            + "min, Slot=" + durationMins + "min");
    }

    // === GUARD 3: Validate recruiter object ===
    if (recruiter == null || recruiter.offers == null) {
        throw new IllegalArgumentException("[PARSER] ❌ Invalid recruiter provided.");
    }

    // === MAIN PARSING LOOP ===
    LocalDateTime cursor = blockStart;
    int slotsCreated = 0;

    System.out.println("[PARSER] Starting parse for '"
                       + recruiter.displayName + "'");
    System.out.println("[PARSER] Block: " + blockStart + " → " + blockEnd
                       + " | SlotDuration: " + durationMins + "min");

    while (!cursor.plusMinutes(durationMins).isAfter(blockEnd)) {

        // === Create the discrete Offer slot ===
        Offer slot = new Offer();
        slot.title        = title;
        slot.startTime    = cursor;
        slot.endTime      = cursor.plusMinutes(durationMins);
        slot.durationMins = durationMins;
        slot.topicTags    = topicTags;
        slot.capacity     = capacity;
        slot.publisher    = recruiter;
        slot.reservations = new java.util.ArrayList<>();

        // === Register the slot with the recruiter ===
        recruiter.offers.add(slot);
        slotsCreated++;

        System.out.println("[PARSER] ✅ Slot " + slotsCreated + ": "
                           + slot.startTime + " → " + slot.endTime);

        // === Advance cursor by one slot duration ===
        cursor = cursor.plusMinutes(durationMins);
    }

    System.out.println("[PARSER] Complete: " + slotsCreated
                       + " slots published for " + recruiter.displayName);
    return slotsCreated;
}
```

---

# SECTION C: TICKET VCFS-004
## Tag-Weighted MatchEngine for Auto-Booking

### What Problem Are We Solving?

A candidate clicks "Find me the best appointment." The system must:
1. Look at every recruiter's every offer globally
2. Throw away offers that would double-book the candidate (collision detection)
3. Score remaining offers based on how well the tags match
4. Book the highest-scoring offer automatically

This is a **recommendation engine** built from scratch in pure Java.

---

### The Scoring Algorithm Visualized

```mermaid
flowchart LR
    subgraph INPUT
        A["Candidate Tags:\nJava, AI, Cloud"]
    end

    subgraph OFFER_POOL
        B["Offer A @ 09:00\nTags: Java, Python"]
        C["Offer B @ 09:00\nTags: AI, HR ⚠️ CONFLICT"]
        D["Offer C @ 10:00\nTags: AI, Cloud, Java"]
        E["Offer D @ 11:00\nTags: Marketing"]
    end

    subgraph COLLISION_FILTER
        F["Candidate has 09:00 booked"]
    end

    subgraph SCORER
        G["Offer A: Java∩Java=1\nScore = 1"]
        H["Offer C: ❌ SKIPPED\n09:00 conflicts"]
        I["Offer D: AI∩AI, Cloud∩Cloud, Java∩Java\nScore = 3"]
        J["Offer E: No intersections\nScore = 0 → ignored"]
    end

    subgraph WINNER
        K["🏆 Offer D wins!\nHighest score = 3\n→ RESERVATION CREATED"]
    end

    A --> B & C & D & E
    F --> C
    B --> G
    C --> H
    D --> I
    E --> J
    G & I --> K
    J --> K

    style H fill:#ff6b6b
    style K fill:#51cf66
```

---

### Collision Detection Deep Dive

Collision detection is the trickiest part. Two reservations conflict if their time windows **overlap**. There are 3 possible overlap cases:

```
Case 1 — Exact same start:
  Existing:  [09:00 ──────── 09:30]
  New offer: [09:00 ──────── 09:30]
  → CONFLICT ❌

Case 2 — New offer starts inside existing:
  Existing:  [09:00 ──────── 09:30]
  New offer:       [09:15 ──────── 09:45]
  → CONFLICT ❌

Case 3 — New offer ends inside existing:
  Existing:       [09:15 ──────── 09:45]
  New offer: [09:00 ──────── 09:30]
  → CONFLICT ❌

Case 4 — No overlap (safe):
  Existing:  [09:00 ──── 09:30]
  New offer:              [09:30 ──── 10:00]
  → SAFE ✅
```

The mathematical formula for overlap: Two intervals `[A_start, A_end]` and `[B_start, B_end]` overlap if and only if `A_start < B_end AND B_start < A_end`.

---

### Full MatchEngine Implementation (in `CareerFairSystem.java`)

```java
---

## Diagram 6 — MVC Architecture: Full Layer Separation

```mermaid
graph TB
    subgraph Users["👥 End Users"]
        Admin["Admin User"]
        Recruiter_User["Recruiter User"]
        Candidate_User["Candidate User"]
    end

    subgraph View_Layer["🖥️ VIEW LAYER - Java Swing GUI"]
        AdminScreen["AdminScreen.java<br/>YAMI"]
        RecruiterScreen["RecruiterScreen.java<br/>Taha"]
        CandidateScreen["CandidateScreen.java<br/>MJAMishkat"]
        LoginFrame["LoginFrame.java<br/>Anonymous"]
        SystemTimerScreen["SystemTimerScreen.java<br/>Shared"]
    end

    subgraph Controller_Layer["⚙️ CONTROLLER LAYER - Business Logic"]
        AdminController["AdminScreenController.java<br/>- createOrganization()<br/>- assignRecruiter()<br/>- setFairTimeline()"]
        RecruiterController["RecruiterController.java<br/>- publishOffer()<br/>- scheduleSession()<br/>- viewMeetingHistory()"]
        CandidateController["CandidateController.java<br/>- submitMeetingRequest()<br/>- viewAvailableLobbies()<br/>- updateProfile()"]
    end

    subgraph Model_Layer["📊 MODEL LAYER - Pure Data"]
        Users_Model["users/<br/>- User (abstract)<br/>- Candidate<br/>- Recruiter"]
        Booking_Model["booking/<br/>- Offer<br/>- Reservation<br/>- Request<br/>- Lobby<br/>- MeetingSession"]
        Structure_Model["structure/<br/>- Organization<br/>- Booth<br/>- VirtualRoom"]
        Audit_Model["audit/<br/>- AttendanceRecord<br/>- AuditEntry"]
    end

    subgraph Core_Layer["🔧 CORE SYSTEM - Orchestration & Timing"]
        CareerFairSystem["CareerFairSystem<br/>(Singleton Facade)<br/>- parseAvailabilityIntoOffers()<br/>- autoBook()<br/>- Observes SystemTimer"]
        CareerFair["CareerFair<br/>(State + Rules)<br/>- evaluatePhase()<br/>- canBook()<br/>- isLive()"]
        SystemTimer["SystemTimer<br/>(Observable)<br/>- stepMinutes()<br/>- PublishesPropertyChangeEvents"]
        Logger["Logger & LogLevel<br/>(Cross-cutting)<br/>- Log all operations"]
    end

    %% Connections View → Controller
    Admin -->|"Clicks"| AdminScreen
    Recruiter_User -->|"Clicks"| RecruiterScreen
    Candidate_User -->|"Clicks"| CandidateScreen
    
    AdminScreen -->|"Call methods"| AdminController
    RecruiterScreen -->|"Call methods"| RecruiterController
    CandidateScreen -->|"Call methods"| CandidateController
    LoginFrame -->|"Creates session"| AdminScreen

    %% Connections Controller → Model
    AdminController -->|"Create"| Users_Model
    AdminController -->|"Create"| Structure_Model
    RecruiterController -->|"Create/Update"| Booking_Model
    CandidateController -->|"Access/Filter"| Booking_Model

    %% Connections Model ↔ Core
    CareerFairSystem -->|"Manages"| Users_Model
    CareerFairSystem -->|"Manages"| Booking_Model
    CareerFairSystem -->|"Manages"| Structure_Model
    CareerFairSystem -->|"Observes"| SystemTimer
    CareerFairSystem -->|"Evaluates with"| CareerFair
    Logger -->|"Logs from"| CareerFairSystem
    Logger -->|"Logs from"| Booking_Model

    %% UI refresh loop
    SystemTimer -->|"PropertyChangeEvent:'time'"| SystemTimerScreen
    SystemTimer -->|"PropertyChangeEvent:'time'"| AdminScreen
    SystemTimer -->|"PropertyChangeEvent:'time'"| CandidateScreen

    style View_Layer fill:#e8f5e9
    style Controller_Layer fill:#fff3e0
    style Model_Layer fill:#f3e5f5
    style Core_Layer fill:#e3f2fd
```

---

## Diagram 7 — Reservation Lifecycle State Machine

```mermaid
stateDiagram-v2
    direction LR

    [*] --> PENDING : Candidate requests/autoBooks\nReservation.state = PENDING

    PENDING --> CONFIRMED : Admin approves\nor autoBook confirms\nstate = CONFIRMED

    CONFIRMED --> ACTIVE : SystemTimer.now >= scheduledStart\nMeetingSession created\nCandidate enters room

    ACTIVE --> COMPLETED : SessionTimer.now >= scheduledEnd\nAttendanceRecord finalized\nstate = COMPLETED

    COMPLETED --> [*] : Audit logged\nReservation closed

    %% Alternative paths
    PENDING --> REJECTED : Admin rejects\nstate = REJECTED
    REJECTED --> [*] : Notification sent

    CONFIRMED --> CANCELLED : Candidate cancels\nstate = CANCELLED

    CANCELLED --> [*] : Audit logged\n'Cancelled by candidate'

    ACTIVE --> ENDED_EARLY : Candidate leaves\nbefore session end

    ENDED_EARLY --> [*] : Attendance marked\n'LEFT_EARLY'

    note right of PENDING
      Created by autoBook() or manual request
      Not yet confirmed
      Lobby does not exist yet
    end note

    note right of CONFIRMED
      Secured in system
      Lobby++ for early arrivals
      VirtualRoom staged
    end note

    note right of ACTIVE
      In real-time execution
      recordJoin() called
      Attendance tracking live
    end note

    note right of COMPLETED
      Session ended naturally
      Outcome: ATTENDED or NO_SHOW
      Audit trail intact
    end note
```

---

## Diagram 8 — Controller-to-System Communication (Sequence)

```mermaid
sequenceDiagram
    participant UI as Swing UI<br/>(Screen)
    participant Ctrl as Controller
    participant CFS as CareerFairSystem<br/>(Singleton)
    participant Model as Model Objects
    participant Timer as SystemTimer

    rect rgb(200, 220, 255)
        Note over UI,CFS: Admin Creates Offer (VCFS-003 Scenario)
    end

    UI->>Ctrl: onPublishOffer(recruiterName, title,<br/>blockStart, blockEnd, durationMins)
    activate Ctrl

    Ctrl->>CFS: parseAvailabilityIntoOffers(recruiter, title,<br/>durationMins, tags, capacity,<br/>blockStart, blockEnd)
    activate CFS

    CFS->>CFS: Check currentPhase == BOOKINGS_OPEN?
    alt Phase not correct
        CFS-->>Ctrl: throw IllegalStateException
        Ctrl-->>UI: displayError("Cannot publish<br/>during this phase")
    else Phase OK
        CFS->>CFS: cursor = blockStart<br/>while (cursor + duration <= blockEnd)
        CFS->>Model: Create new Offer<br/>offer.setStartTime(cursor)<br/>offer.setEndTime(cursor + duration)
        CFS->>Model: recruiter.addOffer(offer)
        CFS->>CFS: cursor += durationMins<br/>repeat loop
        CFS-->>Ctrl: return slotsCreated
        Ctrl-->>UI: displayMessage(slotsCreated<br/>" slots created")
    end
    deactivate CFS
    deactivate Ctrl

    rect rgb(220, 255, 200)
        Note over UI,CFS: Candidate Books (VCFS-004 Scenario)
    end

    UI->>Ctrl: onAutoBook(candidateId, requestId)
    activate Ctrl

    Ctrl->>CFS: autoBook(candidate, request)
    activate CFS

    CFS->>CFS: Check BOOKINGS_OPEN?
    CFS->>CFS: desiredTags = parse(request.getDesiredTags())
    CFS->>CFS: allOffers = getAllOffers()

    loop For each offer
        CFS->>CFS: Check collision with candidate.reservations
        alt Conflict detected
            CFS->>CFS: Skip offer
        else No conflict
            CFS->>CFS: score = tagIntersection(offer.tags, desiredTags)
            CFS->>CFS: scoreMap.put(offer, score)
        end
    end

    CFS->>CFS: bestOffer = max(scoreMap)
    CFS->>Model: Create Reservation<br/>reservation.setCandidate(candidate)<br/>reservation.setOffer(bestOffer)<br/>reservation.setState(CONFIRMED)
    CFS-->>Ctrl: return reservation
    Ctrl-->>UI: displayMessage("Booking confirmed!")
    deactivate CFS
    deactivate Ctrl
```

---

## Diagram 9 — System Timer & Observer Pattern (VCFS-001/002)

```mermaid
graph TB
    subgraph SystemTimer["SystemTimer<br/>(Observable)"]
        Now["now: LocalDateTime<br/>currentValue: 2026-04-07T09:00"]
        Support["PropertyChangeSupport<br/>(holds listeners)"]
    end

    subgraph Listeners["📡 All Observers"]
        CFS["CareerFairSystem<br/>propertyChange() {<br/>  tick();<br/>  fair.evaluatePhase();<br/>}"]
        AdminScreen["AdminScreen<br/>propertyChange() {<br/>  updateTimeLabel();<br/>}"]
        CandidateScreen["CandidateScreen<br/>propertyChange() {<br/>  refreshOffers();<br/>}"]
        Logger["Logger<br/>propertyChange() {<br/>  logTimeChange();<br/>}"]
    end

    subgraph Timeline["⏰ Time Flow"]
        Step1["Admin clicks<br/>'Advance 30 min'"]
        Step2["stepMinutes(30) called"]
        Step3["now = 2026-04-07T09:30"]
        Step4["firePropertyChange('time',<br/>old, new)"]
        Step5["Listeners notified"]
    end

    SystemTimer -->|"stores"| Now
    SystemTimer -->|"manages"| Support
    
    Support -->|"notifies"| CFS
    Support -->|"notifies"| AdminScreen
    Support -->|"notifies"| CandidateScreen
    Support -->|"notifies"| Logger

    Step1 --> Step2
    Step2 --> Step3
    Step3 --> Step4
    Step4 --> Step5
    Step5 -->|"All receive update"| Listeners

    style Now fill:#fffde7
    style Support fill:#fff9c4
    style Listeners fill:#e1f5fe
```

---

## Diagram 10 — Complete Offer Creation Pipeline (VCFS-003: parseAvailabilityIntoOffers)

```mermaid
graph LR
    Input["Input:<br/>blockStart: 09:00<br/>blockEnd: 12:00<br/>durationMins: 30<br/>capacity: 2<br/>tags: 'Java, AI'"]

    Phase_Check["Check Phase<br/>== BOOKINGS_OPEN?"<br/><br/>❌ NO → Throw error<br/>✅ YES → Continue"]

    Duration_Check["Check Block Duration<br/>blockEnd - blockStart<br/>= 180 minutes<br/><br/>>= 30? ✅ YES"]

    Loop["Create Slots<br/>Cursor = 09:00<br/>While (cursor is valid)"]

    Slot1["Slot 1:<br/>09:00 - 09:30<br/>capacity: 2<br/>tags: Java, AI"]

    Slot2["Slot 2:<br/>09:30 - 10:00<br/>capacity: 2<br/>tags: Java, AI"]

    Slot3["Slot 3:<br/>10:00 - 10:30<br/>capacity: 2<br/>tags: Java, AI"]

    Slot4["Slot 4:<br/>10:30 - 11:00<br/>capacity: 2<br/>tags: Java, AI"]

    Slot5["Slot 5:<br/>11:00 - 11:30<br/>capacity: 2<br/>tags: Java, AI"]

    SlotFail["11:30 - 12:00 slot<br/>Would end at 12:00<br/>✅ VALID but LAST"]

    Add["recruiter.addOffer(slot)<br/>slot added to system"]

    Result["Result:<br/>6 Offer objects created<br/>→ returnValue = 6"]

    Input --> Phase_Check
    Phase_Check -->|"YES"| Duration_Check
    Duration_Check --> Loop
    
    Loop -->|"cursor=09:00"| Slot1
    Loop -->|"cursor=09:30"| Slot2
    Loop -->|"cursor=10:00"| Slot3
    Loop -->|"cursor=10:30"| Slot4
    Loop -->|"cursor=11:00"| Slot5
    Loop -->|"cursor=11:30"| SlotFail
    
    Slot1 --> Add
    Slot2 --> Add
    Slot3 --> Add
    Slot4 --> Add
    Slot5 --> Add
    SlotFail --> Add
    
    Add --> Result

    style Input fill:#f8bbd0
    style Phase_Check fill:#fff9c4
    style Duration_Check fill:#fff9c4
    style Result fill:#c8e6c9
```

---

## Diagram 11 — Controller Architecture & Responsibilities

```mermaid
classDiagram
    class AdminScreenController {
        -view: AdminScreen
        -system: CareerFairSystem
        +onCreateOrganization(name) void
        +onAddBooth(org, title) void
        +onRegisterRecruiter(name, email, booth) void
        +onSetFairTimes(open, close, start, end) void
        +onPublishOffers(recruiter, availability) void
        +onResetFair() void
    }

    class RecruiterController {
        -view: RecruiterView
        -system: CareerFairSystem
        -currentRecruiter: Recruiter
        +setCurrentRecruiter(recruiter) void
        +publishOffer(offer) void
        +scheduleSession(session) void
        +viewLobbySessions(lobbyId) void
        +viewMeetingHistory() void
        +updateOfferStatus(offerId, status) void
        +cancelSession(sessionId) void
        +getPublishedOffers() List
    }

    class CandidateController {
        -view: CandidateView
        -system: CareerFairSystem
        -currentCandidate: Candidate
        +setCurrentCandidate(candidate) void
        +submitMeetingRequest(request) void
        +viewAvailableLobbies() void
        +viewLobbyInfo(lobbyId) void
        +viewMeetingSchedule() void
        +cancelMeetingRequest(requestId) void
        +viewRequestHistory() void
        +updateProfile(phone, email) void
        +getAvailableSessions(lobbyId) List
    }

    AdminScreenController -->|"works with"| CareerFairSystem
    RecruiterController -->|"works with"| CareerFairSystem
    CandidateController -->|"works with"| CareerFairSystem
```

---

## Diagram 12 — Implementation Status Checklist (As of April 7, 2026)

```mermaid
graph TD
    A["✅ COMPLETED"]
    B["🔄 IN PROGRESS"]
    C["⏳ PENDING"]

    A1["✅ LocalDateTime.java<br/>(Custom Immutable DateTime)"]
    A2["✅ SystemTimer.java<br/>(Observable Timer)"]
    A3["✅ Logger.java & LogLevel.java<br/>(Logging System)"]
    A4["✅ FairPhase enum<br/>(6 lifecycle phases)"]
    A5["✅ CareerFair.java<br/>(State machine + rules)"]
    A6["✅ Model Classes<br/>(User, Candidate, Recruiter, Offer, etc.)"]
    
    B1["🔄 CareerFairSystem.java<br/>(VCFS-001,002: Singleton + Observer)<br/>(VCFS-003: parseAvailabilityIntoOffers)<br/>(VCFS-004: autoBook)"]
    B2["🔄 AdminScreenController.java<br/>(Admin operations)"]
    B3["🔄 CandidateController.java<br/>(Candidate operations)"]
    B4["🔄 RecruiterController.java<br/>(Recruiter operations)"]
    
    C1["⏳ AdminScreen.java<br/>(Admin UI - YAMI)"]
    C2["⏳ RecruiterScreen.java<br/>(Recruiter UI - Taha)"]
    C3["⏳ CandidateScreen.java<br/>(Candidate UI - MJAMishkat)"]
    C4["⏳ JUnit Tests<br/>(Mohamed - Testing)"]
    C5["⏳ Integration Testing<br/>(Full System Flow)"]
    C6["⏳ Video Demo<br/>(20 minutes)"]

    A --> A1 & A2 & A3 & A4 & A5 & A6
    B --> B1 & B2 & B3 & B4
    C --> C1 & C2 & C3 & C4 & C5 & C6

    style A fill:#c8e6c9
    style B fill:#fff9c4
    style C fill:#ffccbc
```

---

## Diagram 13 — Data Flow: Complete Booking Scenario

```mermaid
graph LR
    Start["Candidate logs in"]
    
    Step1["1. CareerFairSystem<br/>loads all Offers<br/>getAllOffers()"]
    Step2["2. CandidateScreen<br/>displays available<br/>offers by tag"]
    Step3["3. Candidate clicks<br/>'Auto Book'"]
    
    Step4["4. CandidateController<br/>.autoBook(candidate,<br/>request)"]
    Step5["5. CareerFairSystem<br/>.autoBook() handles:<br/>- Phase check<br/>- Tag parsing<br/>- Collision detection<br/>- Score calculation<br/>- Winner selection"]
    
    Step6["6. Create Reservation<br/>state = CONFIRMED"]
    Step7["7. Add to candidate<br/>reservations"]
    Step8["8. Add to offer<br/>reservations"]
    
    Step9["9. Return to view"]
    Step10["10. DisplayMessage<br/>'Booking confirmed!'"]
    
    Step11["11. SystemTimer tick"]
    Step12["12. scheduledStart reached"]
    Step13["13. MeetingSession created<br/>session.start()"]
    
    Step14["14. Candidate/Recruiter<br/>join virtual room"]
    Step15["15. recordJoin() called<br/>AttendanceRecord created"]
    
    Step16["16. Meeting happens"]
    Step17["17. scheduledEnd reached<br/>session.end()"]
    
    Step18["18. recordLeave() called<br/>Outcome = ATTENDED"]
    Step19["19. AuditEntry logged"]
    Step20["20. Reservation state<br/>=COMPLETED"]
    
    Start --> Step1 --> Step2 --> Step3
    Step3 --> Step4 --> Step5
    Step5 --> Step6 --> Step7 --> Step8
    Step8 --> Step9 --> Step10
    Step10 --> Step11 --> Step12 --> Step13
    Step13 --> Step14 --> Step15
    Step15 --> Step16 --> Step17
    Step17 --> Step18 --> Step19 --> Step20

    style Start fill:#c8e6c9
    style Step6 fill:#fff9c4
    style Step13 fill:#fff9c4
    style Step20 fill:#c8e6c9
```

---

## Summary: What's Done vs What's Left

| Component | Status | Owner | Est. Hours Left |
|-----------|--------|-------|-----------------|
| **LocalDateTime** | ✅ DONE | Zaid | 0 |
| **SystemTimer** | ✅ DONE | Zaid | 0 |
| **CareerFair + FairPhase** | ✅ DONE | Zaid | 0 |
| **Model Classes** | ✅ DONE | Team | 0 |
| **CareerFairSystem** | 🔄 70% DONE | Zaid | 2 |
| **Controllers** | 🔄 60% DONE | Zaid | 3 |
| **Observer Pattern** | ✅ DONE | Zaid | 0 |
| **UI Screens** | ⏳ NOT STARTED | YAMI/Taha/MJAMishkat | 8 |
| **JUnit Tests** | ⏳ NOT STARTED | Mohamed | 4 |
| **Integration Testing** | ⏳ NOT STARTED | Team | 2 |
| **Video Demo** | ⏳ NOT STARTED | Zaid | 3 |
| **Documentation** | ✅ DONE | Zaid | 0 |

---

## Critical Fixes Needed (Before Final Submission):

1. **Remove duplicate AdminController.java** — keep only AdminScreenController.java
2. **Fix Logger.java imports** — use vcfs.core.LocalDateTime, NOT java.time.LocalDateTime
3. **Add null checks** in all Controllers
4. **Complete Offer.getMeetingSession()** method
5. **Add Request.getId()** method
6. **Add CandidateProfile methods** (setPhoneNumber, etc.)
7. **Ensure view interfaces** are created (RecruiterView, CandidateView)
8. **Complete integration** between all controllers and screens

---

## Next Steps (Priority Order):

1. **TODAY**: Fix all 24 compilation errors
2. **TODAY**: Complete CareerFairSystem.java methods
3. **TOMORROW**: Create/update all UI screens
4. **TOMORROW**: Run full integration test
5. **SUBMISSION DAY**: Record demo video, submit all forms

/**
 * ============================================================
 * VCFS-004: Tag-Weighted MatchEngine
 * ============================================================
 * Automatically finds and confirms the highest-scoring
 * Offer for a given Candidate, preventing schedule conflicts.
 *
 * Algorithm Summary:
 *   1. Validate phase (BOOKINGS_OPEN required)
 *   2. Parse candidate's desired tags from Request
 *   3. For each global Offer:
 *      a. Run Collision Detection — skip if time conflict exists
 *      b. Calculate Tag Intersection Score
 *      c. Store in HashMap<Offer, Integer>
 *   4. Select Offer with maximum score from HashMap
 *   5. Create and return a confirmed Reservation
 *
 * @param candidate  The candidate requesting auto-booking
 * @param request    Contains desiredTags and preferredOrgs
 * @return           Confirmed Reservation, or null if no match found
 *
 * Implemented by: Zaid (VCFS-004)
 * ============================================================
 */
Reservation autoBook(Candidate candidate, Request request) {

    // === GUARD 1: Phase must be BOOKINGS_OPEN ===
    LocalDateTime now = SystemTimer.getInstance().getNow();
    if (!fair.canBook(now)) {
        System.out.println("[MATCHENGINE] ❌ Booking rejected — Phase: "
                           + fair.getCurrentPhase());
        return null;
    }

    // === GUARD 2: Validate inputs ===
    if (candidate == null || request == null || request.desiredTags == null) {
        throw new IllegalArgumentException("[MATCHENGINE] ❌ Invalid candidate or request.");
    }

    // === STEP 1: Parse desired tags into a List for comparison ===
    // Split "Java, AI, Cloud" → ["java", "ai", "cloud"] (lowercase for fair comparison)
    java.util.List<String> desiredTags = java.util.Arrays.asList(
        request.desiredTags.toLowerCase().split(",\\s*")
    );

    System.out.println("[MATCHENGINE] Auto-booking for: " + candidate.displayName);
    System.out.println("[MATCHENGINE] Desired tags: " + desiredTags);

    // === STEP 2: Initialize the score map ===
    java.util.Map<Offer, Integer> scoreMap = new java.util.HashMap<>();

    // === STEP 3: Evaluate every offer in the system ===
    for (Offer offer : getAllOffers()) {

        // Skip offers with no topic tags (shouldn't happen but defensive)
        if (offer.topicTags == null || offer.startTime == null) continue;

        // --- COLLISION DETECTION ---
        // Check if candidate already has a reservation that overlaps this offer's time
        boolean conflict = false;
        for (Reservation existing : candidate.reservations) {
            // Overlap condition: existing.start < offer.end AND offer.start < existing.end
            boolean overlaps =
                existing.scheduledStart.isBefore(offer.endTime) &&
                offer.startTime.isBefore(existing.scheduledEnd);

            if (overlaps) {
                conflict = true;
                System.out.println("[MATCHENGINE] ⚡ Collision at "
                    + offer.startTime + " — skipping.");
                break;
            }
        }
        if (conflict) continue; // Skip this offer — it conflicts

        // --- TAG INTERSECTION SCORING ---
        java.util.List<String> offerTags = java.util.Arrays.asList(
            offer.topicTags.toLowerCase().split(",\\s*")
        );

        int score = 0;
        for (String desired : desiredTags) {
            if (offerTags.contains(desired.trim())) {
                score++;
            }
        }

        // Only add to the map if there is at least 1 matching tag
        if (score > 0) {
            scoreMap.put(offer, score);
            System.out.println("[MATCHENGINE] 📊 Offer at " + offer.startTime
                + " | Tags: " + offerTags
                + " | Score: " + score + "/" + desiredTags.size());
        }
    }

    // === STEP 4: Select the winner ===
    if (scoreMap.isEmpty()) {
        System.out.println("[MATCHENGINE] ❌ No matching offers found.");
        return null;
    }

    // Collections.max returns the entry with the largest value
    Offer bestOffer = java.util.Collections.max(
        scoreMap.entrySet(),
        java.util.Map.Entry.comparingByValue()
    ).getKey();

    System.out.println("[MATCHENGINE] 🏆 Winner: " + bestOffer.startTime
        + " (score=" + scoreMap.get(bestOffer) + ")");

    // === STEP 5: Create and register the Reservation ===
    Reservation reservation = new Reservation();
    reservation.candidate      = candidate;
    reservation.offer          = bestOffer;
    reservation.scheduledStart = bestOffer.startTime;
    reservation.scheduledEnd   = bestOffer.endTime;
    reservation.state          = ReservationState.CONFIRMED;

    // Register on both sides of the relationship
    candidate.reservations.add(reservation);
    bestOffer.reservations.add(reservation);

    System.out.println("[MATCHENGINE] ✅ CONFIRMED: "
        + candidate.displayName + " → " + bestOffer.startTime
        + " with " + bestOffer.publisher.displayName);

    return reservation;
}

/**
 * Utility: Traverse the entire org → booth → recruiter → offer hierarchy
 * to collect ALL published Offer objects into a flat list.
 *
 * Used by: autoBook() MatchEngine
 * Used by: ManualBoook browsing (MJAMishkat's search)
 */
private java.util.List<Offer> getAllOffers() {
    java.util.List<Offer> allOffers = new java.util.ArrayList<>();
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

    System.out.println("[getAllOffers] Total offers in system: " + allOffers.size());
    return allOffers;
}
```

---

# SECTION D: FINAL IMPLEMENTATION ORDER (Full Summary)

| Priority | File | Methods to Implement | Ticket |
|----------|------|----------------------|--------|
| 🔴 **1st** | `LocalDateTime.java` | Full class from scratch | VCFS-001 |
| 🔴 **2nd** | `SystemTimer.java` | Singleton + Observer | VCFS-001 |
| 🟡 **3rd** | `CareerFair.java` | setTimes, evaluatePhase, canBook, isLive | VCFS-002 |
| 🟡 **4th** | `CareerFairSystem.java` | Singleton + PropertyChangeListener + tick | VCFS-002 |
| 🟢 **5th** | `Offer.java` | Add startTime, endTime, title, topicTags, capacity fields | VCFS-003 |
| 🟢 **6th** | `CareerFairSystem.java` | parseAvailabilityIntoOffers() | VCFS-003 |
| 🔵 **7th** | `CareerFairSystem.java` | autoBook() + getAllOffers() | VCFS-004 |

**Total estimated time: ~6-8 hours of focused implementation**

---

## Critical Notes for Zaid

> [!IMPORTANT]
> **Do NOT touch** `AdminScreen.java`, `CandidateScreen.java`, `RecruiterScreen.java`, `Lobby.java`, `MeetingSession.java`, `VirtualRoom.java`, `AttendanceRecord.java`, or `AuditEntry.java`.
> Those belong to YAMI, Taha, and MJAMishkat respectively.
> Your role ends at the `core/` package and the fields/methods explicitly listed above.

---

**Document Version**: 1.0  
**Last Updated**: April 6, 2026  
**Assigned to**: Zaid (Project Manager)  
**Tickets Covered**: VCFS-003, VCFS-004 (Complete Diagrams & Algorithms)
