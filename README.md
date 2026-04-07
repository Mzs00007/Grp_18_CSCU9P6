# Virtual Career Fair System (VCFS)
**Group 9 - CSCU9P6**

## Project Leadership & Architecture
**Project Manager & Lead Developer:** Zaid Siddiqui  
**Collaborators:** Taha, YAMI, MJAMishkat, Mohamed

---

## 🏗️ System Architecture Overview

The VCFS application is built upon an **Enterprise-Grade Java Architecture** utilizing strict **Model-View-Controller (MVC)** separation, the **Observer Pattern**, and a robust **State Machine**.

### 1. High-Level MVC Flow
```mermaid
graph TD
    subgraph View Layer [Java Swing UI]
        A[AdminScreen]
        R[RecruiterScreen]
        C[CandidateScreen]
    end

    subgraph Controller Layer [Business Logic]
        AC[AdminController]
        RC[RecruiterController]
        CC[CandidateController]
    end

    subgraph Core Layer [VCFS Engine]
        CFS[CareerFairSystem Facade]
        CF[CareerFair State Machine]
        ST[SystemTimer Observer]
    end

    subgraph Model Layer [Data Entities]
        M1[Users: Candidate, Recruiter]
        M2[Structure: Org, Booth, Room]
        M3[Booking: Offer, Request, Reservation]
    end

    A --> AC
    R --> RC
    C --> CC

    AC --> CFS
    RC --> CFS
    CC --> CFS

    CFS --> CF
    CFS --> ST
    CFS --> M1
    CFS --> M2
    CFS --> M3
    
    ST -.->|Notifies| CFS
```

### 2. The Core State Machine (VCFS-002)
The CareerFair class enforces a strict, chronological state machine to control what actions are permitted at any given time.

```mermaid
stateDiagram-v2
    [*] --> DORMANT : App Launch
    DORMANT --> PREPARING : Admin configures timeline
    PREPARING --> BOOKINGS_OPEN : Clock hits BookingsOpenTime
    BOOKINGS_OPEN --> BOOKINGS_CLOSED : Clock hits BookingsCloseTime
    BOOKINGS_CLOSED --> FAIR_LIVE : Clock hits FairStartTime
    FAIR_LIVE --> DORMANT : Clock hits FairEndTime

    note right of PREPARING
      Admin creates Orgs, Booths, Recruiters
      Recruiters publish Offers
    end note

    note right of BOOKINGS_OPEN
      Candidates browse and book Offers
    end note

    note right of FAIR_LIVE
      Users join Virtual Rooms
    end note
```

### 3. VCFS-001: SystemTimer (Observer Pattern)
A centralized, simulated clock that dictates the flow of time for the entire system, allowing for rigorous testing of time-based constraints without waiting for real-world time to pass.

```mermaid
sequenceDiagram
    participant UI as SystemTimerScreen
    participant ST as SystemTimer (Observable)
    participant CFS as CareerFairSystem (Observer)
    participant CF as CareerFair (State Machine)

    UI->>ST: stepMinutes(30)
    activate ST
    ST->>ST: now = now.plusMinutes(30)
    ST-->>CFS: propertyChange("time", old, new)
    activate CFS
    CFS->>CF: evaluatePhase(now)
    activate CF
    CF-->>CFS: phase updated
    deactivate CF
    CFS-->>ST: Handled
    deactivate CFS
    deactivate ST
```

### 4. VCFS-003 & 004: Booking Algorithms
The system implements complex algorithms to parse recruiter availability into discrete booking slots, and a tag-weighted matching engine to automatically pair candidates with the best available offers.

```mermaid
graph LR
    subgraph VCFS-003 Parsing
        A[3-Hour Block: 09:00-12:00] --> B[parseAvailabilityIntoOffers]
        B --> O1[Offer: 09:00-09:30]
        B --> O2[Offer: 09:30-10:00]
        B --> O3[...]
        B --> O6[Offer: 11:30-12:00]
    end

    subgraph VCFS-004 Matching
        C[Candidate Tags: Java, Spring] --> M[Match Engine]
        O1 --> M
        O2 --> M
        M -->|Score: 2/2| R[Reservation Created]
    end
```

---

## 📁 Directory Structure & Team Responsibilities

To prevent merge conflicts and ensure code quality, the project is strictly divided by domain:

| Team Member | Primary Responsibility | Isolated Folder Path |
|-------------|----------------------|----------------------|
| **Zaid** | Project Management, Core System | `src/main/java/vcfs/core/` |
| **YAMI** | Admin UI & Lifecycle | `src/main/java/vcfs/views/admin/` |
| **Taha** | Recruiter UI & Virtual Room | `src/main/java/vcfs/views/recruiter/` |
| **MJAMishkat** | Candidate UI & Booking | `src/main/java/vcfs/views/candidate/` |
| **Mohamed** | Architecture & QA | `src/test/java/vcfs/` |

---

## 🚀 How to Run

A batch script is provided to compile and run the entire system cleanly:

1. Double click `run_vcfs.bat`
2. Or from command line:
```cmd
.\run_vcfs.bat
```

Alternatively, manually compile and run:
```cmd
mkdir bin
javac -d bin (Get-ChildItem -Path src\main\java -Filter *.java -Recurse)
java -cp bin vcfs.App
```

---

## 🧪 Running Tests
The project features a comprehensive JUnit test suite (>80 tests) covering all core logic, boundary conditions, and state transitions.

*Note: JUnit platform console standalone jar is required in `lib/` directory.*

```cmd
javac -cp "lib\*;bin" -d bin (Get-ChildItem -Path src\test\java -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
java -jar lib\junit-platform-console-standalone-1.9.2.jar --class-path bin --scan-classpath
```