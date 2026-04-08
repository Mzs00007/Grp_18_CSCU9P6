# VCFS Directory Architecture Blueprint (Phase 0)

## Overview
> [!IMPORTANT]
> This document outlines an **Enterprise-Grade Java Architecture** designed to prevent merge conflicts, ensure code clarity, and demonstrate advanced Software Engineering practices aligned with university marking standards.

This architecture uses standard professional nesting patterns (similar to Maven/Gradle builds) and implements strict Model-View-Controller (MVC) separation of concerns.

---

## The Complete Folder Structure

```
Grp_9_CSCU9P6/
├── 📁 src/                                 (All Source Code)
│   ├── 📁 main/
│   │   ├── 📁 java/vcfs/                  (The Root Package: 'vcfs')
│   │   │   │
│   │   │   ├── 📄 App.java                (Entry Point: main method)
│   │   │   │
│   │   │   ├── 📁 core/                   (System Core - Business Logic Engine)
│   │   │   │   ├── CareerFairSystem.java  (Orchestrates all system operations)
│   │   │   │   ├── CareerFair.java        (Career fair entity & logic)
│   │   │   │   ├── SystemTimer.java       (Timing & scheduling)
│   │   │   │   ├── Logger.java            (Logging facility)
│   │   │   │   ├── LogLevel.java          (Log level enumeration)
│   │   │   │   └── LocalDateTime.java     (Custom datetime handling)
│   │   │   │
│   │   │   ├── 📁 models/                 (The 'Model' in MVC - Pure Data & Entities)
│   │   │   │   │
│   │   │   │   ├── 📁 users/              (System Actors)
│   │   │   │   │   ├── User.java          (Abstract base user class)
│   │   │   │   │   ├── Candidate.java     (Job candidate)
│   │   │   │   │   ├── CandidateProfile.java
│   │   │   │   │   └── Recruiter.java     (Recruiter/HR representative)
│   │   │   │   │
│   │   │   │   ├── 📁 structure/          (Physical/Virtual Entities)
│   │   │   │   │   ├── Organization.java  (Company/Organization)
│   │   │   │   │   ├── Booth.java         (Career booth at fair)
│   │   │   │   │   └── VirtualRoom.java   (Virtual meeting room)
│   │   │   │   │
│   │   │   │   ├── 📁 booking/            (Transactional Entities)
│   │   │   │   │   ├── Offer.java         (Job offer from recruiter)
│   │   │   │   │   ├── Request.java       (Meeting request)
│   │   │   │   │   ├── Reservation.java   (Meeting reservation)
│   │   │   │   │   ├── Lobby.java         (Lobby management)
│   │   │   │   │   └── MeetingSession.java (Virtual meeting session)
│   │   │   │   │
│   │   │   │   ├── 📁 audit/              (Audit & Tracking)
│   │   │   │   │   ├── AttendanceRecord.java (Attendance tracking)
│   │   │   │   │   └── AuditEntry.java    (Audit log entry)
│   │   │   │   │
│   │   │   │   └── 📁 enums/              (System State Definitions)
│   │   │   │       ├── AttendanceOutcome.java
│   │   │   │       ├── FairPhase.java     (Fair lifecycle phases)
│   │   │   │       ├── MeetingState.java  (Meeting states)
│   │   │   │       ├── ReservationState.java
│   │   │   │       └── RoomState.java     (Virtual room states)
│   │   │   │
│   │   │   ├── 📁 controllers/            (The 'Controller' in MVC)
│   │   │   │   ├── AdminController.java   (Admin operations handler)
│   │   │   │   ├── CandidateController.java (Candidate operations handler)
│   │   │   │   └── RecruiterController.java (Recruiter operations handler)
│   │   │   │
│   │   │   └── 📁 views/                  (The 'View' in MVC - Java Swing GUI)
│   │   │       ├── 📁 admin/              (YAMI: Admin Dashboard)
│   │   │       │   └── AdminScreen.java
│   │   │       ├── 📁 candidate/          (MJAMishkat: Candidate Interface)
│   │   │       │   └── CandidateScreen.java
│   │   │       ├── 📁 recruiter/          (Taha: Recruiter Interface)
│   │   │       │   └── RecruiterScreen.java
│   │   │       └── 📁 shared/             (Common/Shared Components)
│   │   │           └── SystemTimerScreen.java
│   │   │
│   │   └── 📁 resources/                  (Assets & Configuration)
│   │       └── (Images, icons, config files)
│   │
│   └── 📁 test/                           (Mohamed: QA Testing)
│       └── 📁 java/vcfs/
│           ├── VirtualRoomTest.java
│           └── MeetingSessionTest.java
│
├── 📁 docs/                               (Documentation & Assignment Files)
│   ├── ARCHITECTURE.md                    (This file)
│   ├── GroupProjectSpecification.pdf      (Assignment brief)
│   ├── excel_content.txt                  (Reference data)
│   └── CSCU9P6_VCFS_Agile_Roadmap.xlsx   (Agile sprint plan)
│
├── 📁 .github/
│   └── 📁 workflows/                      (CI/CD configuration)
│
├── .gitignore                             (Git ignore rules)
├── README.md                              (Project overview)
└── compile_errors.txt                     (Build issue log)
```

---

## Architecture Principles

### 1. **Model-View-Controller (MVC) Separation**
- **Models** (`models/`): Pure data objects and entities
  - No UI logic
  - No business orchestration
  - Contains only fields, getters, setters, and simple validation
  
- **Views** (`views/`): JavaSwing GUI components only
  - No business logic
  - No database operations
  - Delegates all operations to Controllers
  
- **Controllers** (`controllers/`): Business logic orchestration
  - Handles user interactions from Views
  - Calls appropriate Model operations
  - Updates Views with results

### 2. **Package Organization**
- **Core Package** (`core/`): System's beating heart
  - `CareerFairSystem`: Main orchestrator
  - `SystemTimer`: Global timing mechanism
  - `Logger`: Centralized logging
  
- **Models Subpackages**:
  - `users/`: All actor classes (Candidate, Recruiter, User)
  - `structure/`: Physical/virtual entities (Organization, Booth, VirtualRoom)
  - `booking/`: Transactional entities (Offer, Request, Reservation)
  - `audit/`: Audit & tracking (AttendanceRecord, AuditEntry)
  - `enums/`: All enumeration types for system states
  
- **Controllers**: One per major user type
  - `AdminController`
  - `CandidateController`
  - `RecruiterController`
  
- **Views Subpackages**: One folder per user type
  - `admin/`: All admin UI screens
  - `candidate/`: All candidate UI screens
  - `recruiter/`: All recruiter UI screens
  - `shared/`: Reusable UI components

### 3. **Team Collaboration**
| Team Member | Primary Responsibility | Isolated Folder |
|-------------|----------------------|-----------------|
| **Zaid** | Project Management, Core System | `core/` |
| **YAMI** | Admin UI & Lifecycle | `views/admin/` |
| **Taha** | Recruiter UI & Virtual Room | `views/recruiter/` + `models/structure/` |
| **MJAMishkat** | Candidate UI & Booking | `views/candidate/` + `models/booking/` |
| **Mohamed** | Architecture & QA | `src/test/` |

---

## Key Benefits of This Architecture

### ✅ No Merge Conflicts
Since each developer works in isolated package hierarchies, Git merges are seamless:
- YAMI modifies `views/admin/` while Zaid modifies `core/`
- Taha edits `views/recruiter/` while MJAMishkat edits `views/candidate/`
- No overlapping file edits = no conflicts

### ✅ Instant Code Locatability
Need to change how Candidates book a meeting?
→ Look in `models/booking/` for Offer, Request, Reservation
→ Look in `controllers/CandidateController.java` for booking logic
→ Look in `views/candidate/CandidateScreen.java` for the UI

### ✅ Enterprise-Grade Quality
University markers recognize professional software architecture:
- **High Cohesion**: Related classes grouped together
- **Low Coupling**: Dependencies between packages are minimal
- **Single Responsibility Principle**: Each class has one reason to change
- **Open/Closed Principle**: Easy to extend without modifying existing code

### ✅ Scalability
Adding new features is trivial:
- New user type? Add to `models/users/`
- New virtual room feature? Extend `models/structure/VirtualRoom.java`
- New meeting state? Update `models/enums/MeetingState.java`

### ✅ Testability
Each layer can be tested independently:
- Unit test Models without GUI
- Mock Controllers to test Views
- Integration test entire workflows

---

## Package Declarations
All Java files follow the standard package declaration pattern:

```java
package vcfs;                    // App.java
package vcfs.core;              // SystemTimer, CareerFair, etc.
package vcfs.models.users;       // User, Candidate, Recruiter
package vcfs.models.structure;   // Organization, Booth, VirtualRoom
package vcfs.models.booking;     // Offer, Request, Reservation
package vcfs.models.audit;       // AttendanceRecord, AuditEntry
package vcfs.models.enums;       // FairPhase, MeetingState, etc.
package vcfs.controllers;        // AdminController, CandidateController, etc.
package vcfs.views.admin;        // AdminScreen
package vcfs.views.candidate;    // CandidateScreen
package vcfs.views.recruiter;    // RecruiterScreen
package vcfs.views.shared;       // SystemTimerScreen
```

---

## Migration Checklist

### Phase 0: Planning ✓ (Current)
- [x] Define architecture blueprint
- [x] Document package structure
- [x] Plan file reorganization

### Phase 1: Folder Creation
- [ ] Create all nested package directories
- [ ] Verify directory structure

### Phase 2: File Migration
- [ ] Move all `.java` files to correct package folders
- [ ] Update all `package` declarations
- [ ] Update all `import` statements
- [ ] Verify no compilation errors

### Phase 3: Documentation Migration
- [ ] Move PDF/Excel documents to `docs/` folder
- [ ] Update `README.md` with new structure
- [ ] Commit to Git

### Phase 4: CI/CD & Validation
- [ ] Test full compilation
- [ ] Run all unit tests
- [ ] Push to GitHub

---

## Compilation Command (After Migration)

```bash
# From project root
javac -cp src/main/java -d out src/main/java/vcfs/**/*.java

# Run the application
java -cp out vcfs.App
```

---

## Git Strategy

To avoid conflicts during simultaneous development:

```bash
# Before starting work
git pull origin main

# Create a feature branch (optional)
git checkout -b feature/vcfs-xxx

# Work in your isolated folder
# (YAMI: views/admin/, Taha: views/recruiter/, etc.)

# Commit frequently
git add src/main/java/vcfs/your_package/
git commit -m "VCFS-XXX: Your feature description"

# Push to remote
git push origin feature/vcfs-xxx

# Create Pull Request for review
```

---

## Next Steps

This architecture is ready for implementation. Once approved:
1. System will auto-generate all nested folders
2. All 27 Java files will be reorganized automatically
3. All package declarations and imports will be corrected
4. Documentation will be moved to `docs/`
5. Full compilation will be verified
6. Changes will be committed to Git

**Status**: Awaiting approval to proceed with Phase 1 (Folder Creation)

---

**Document Version**: 1.0  
**Last Updated**: April 6, 2026  
**Architect**: Group 9 Team Lead
