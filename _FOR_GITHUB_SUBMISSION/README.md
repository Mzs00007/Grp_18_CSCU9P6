# Virtual Career Fair System (VCFS) 🎓

**Advanced Object-Oriented Design • CSCU9P6 • University of Stirling**

![Status](https://img.shields.io/badge/Status-Production%20Ready-brightgreen)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)
![Tests](https://img.shields.io/badge/Tests-112%2F112%20Passing-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![License](https://img.shields.io/badge/License-Academic-blue)

---

## 📖 Table of Contents

1. [Project Overview](#project-overview)
2. [Key Features](#key-features)
3. [Architecture & Design Patterns](#architecture--design-patterns)
4. [Quick Start Guide](#quick-start-guide)
5. [Project Structure](#project-structure)
6. [System Components](#system-components)
7. [How to Use](#how-to-use)
8. [Technologies Used](#technologies-used)
9. [Testing & Quality](#testing--quality)
10. [Team & Contributions](#team--contributions)
11. [Demonstration Guide](#demonstration-guide)

---

## 🎯 Project Overview

The **Virtual Career Fair System (VCFS)** is a comprehensive Java-based platform that enables organizations to conduct **virtual career fairs** seamlessly. It connects **three distinct portals** (Administrator, Recruiter, Candidate) in a unified system where users can:

- **Administrators** coordinate multiple organizations, set up booths, manage timelines
- **Recruiters** publish interview offers, manage meeting schedules
- **Candidates** search offers, book interviews, join virtual meeting rooms

### Core Vision
Create an **enterprise-grade, scalable, and user-friendly** platform that simulates real-world career fair operations with real-time data synchronization across all portals.

### Submission Details
- **Module**: CSCU9P6 - Advanced Object-Oriented Design
- **Semester**: 2025-2026 (Sem 6)
- **Group**: 18
- **Submission Date**: April 8, 2026
- **Project Manager & Lead Developer**: Zaid Siddiqui (mzs00007)

---

## ✨ Key Features

### 🏢 **Multi-Portal Architecture**
- **Admin Console** (1200×800px)
  - Create and manage organizations
  - Configure system timeline (phases, dates/times)
  - Monitor all recruiters, candidates, offers in real-time
  - Access complete audit log of system events

- **Recruiter Dashboard** (900×700px)
  - Browse organizations to assign to
  - Publish interview offer slots with duration & capacity
  - View meeting schedule for booked interviews
  - Access virtual meeting rooms

- **Candidate Dashboard** (900×700px)
  - Search available interview offers by skill tags
  - Auto-match with recruiter offers
  - View confirmed booking schedule
  - Join virtual waiting rooms for interviews

### 🔄 **Real-Time Data Synchronization**
- **PropertyChangeListener Pattern** - All portals instantly see updates
- **Dual Scrollbars** - Full responsive UI with vertical + horizontal scrolling
- **Auto-Refresh Tables** - Tables update automatically when data changes

### 📊 **Advanced Features**
- ✅ State machine for fair phases (DORMANT → PREPARING → BOOKINGS_OPEN → FAIR_LIVE)
- ✅ Tag-based offer matching algorithm
- ✅ Thread-safe candidate/recruiter tracking
- ✅ Complete audit logging of all system events
- ✅ Virtual room system for live interviews
- ✅ Capacity management with booking counts

### 🎮 **Demo Mode**
- Pre-loaded demo data (3 organizations, 3 recruiters, 3 candidates)
- 9 pre-created interview offers
- Side-by-side portal view for live demonstrations
- Full functionality without manual setup

---

## 🏛️ Architecture & Design Patterns

### **MVC (Model-View-Controller)**
```
┌─────────────────────────────────────────┐
│         UI LAYER (Swing)                 │
│  AdminScreen | RecruiterScreen | CandidateScreen
├─────────────────────────────────────────┤
│      CONTROLLER LAYER                    │
│  AdminController | RecruiterController | CandidateController
├─────────────────────────────────────────┤
│      BUSINESS LOGIC LAYER                │
│  CareerFairSystem (Singleton)            │
├─────────────────────────────────────────┤
│      MODEL LAYER                         │
│  Organization | Recruiter | Candidate | Offer | Reservation
└─────────────────────────────────────────┘
```

### **Design Patterns Implemented**

#### 1. **Singleton Pattern**
```
CareerFairSystem.getInstance() → Returns single system instance
```
- Ensures one unified state across entire application
- Manages all organizations, recruiters, candidates, offers

#### 2. **Observer Pattern**
```
PropertyChangeListener chain:
CareerFairSystem (data changes)
    ↓
AdminScreen listens to: "organizations", "recruiters", "candidates", "offers"
RecruiterScreen listens to: "offers"
CandidateScreen listens to: "offers"
    ↓
Auto-refresh tables when events broadcast
```

#### 3. **State Pattern**
```
Fair Phases:
DORMANT → PREPARING → BOOKINGS_OPEN → FAIR_LIVE → BOOKINGS_CLOSED
```
- Controls what actions are available at each phase

#### 4. **MVC Pattern**
- **Models**: Separate classes for each entity (User, Offer, Reservation)
- **Views**: GUI screens (AdminScreen, RecruiterScreen, CandidateScreen)
- **Controllers**: Business logic delegation (AdminController, etc.)

#### 5. **Strategy Pattern**
- Tag-based matching algorithm for offer-candidate pairing

---

## 🚀 Quick Start Guide

### **Prerequisites**
- Java 17 or higher
- Windows/Mac/Linux with GUI support

### **Installation**

```bash
# Step 1: Clone or download repository
cd Grp_18_CSCU9P6_code/_FOR_GITHUB_SUBMISSION

# Step 2: Compile (if needed)
javac -d build/bin -encoding UTF-8 src/main/java/vcfs/**/*.java

# Step 3: Run the application
java -cp build/bin vcfs.App
```

### **Quick Launch - Windows**
```batch
# Double-click:
BUILD_AND_RUN_DEMO.bat
```

### **First Time Setup**

1. **Application starts** → MainMenuFrame opens
2. **Click "⚡ DEMO MODE"** → All 3 portals open side-by-side
3. **OR Select Role**:
   - Admin: user=`admin`, password=`admin123`
   - Recruiter: Any username (demo mode)
   - Candidate: Any email from demo (David Lee, Elena Rodriguez, Frank Williams)

---

## 📁 Project Structure

```
_FOR_GITHUB_SUBMISSION/
├── src/main/java/vcfs/
│   ├── App.java                          # Entry point
│   │
│   ├── core/
│   │   ├── CareerFairSystem.java        # ✨ Singleton system core
│   │   ├── SystemTimer.java             # Timeline management
│   │   ├── UserSession.java             # User context tracking
│   │   ├── Logger.java                  # Audit logging
│   │   ├── UIHelpers.java               # UI utilities
│   │   ├── LocalDateTime.java           # Custom datetime
│   │   └── LogLevel.enum                # Log severity levels
│   │
│   ├── models/
│   │   ├── users/
│   │   │   ├── User.java               # Base user class
│   │   │   ├── Administrator.java      # Admin user
│   │   │   ├── Recruiter.java          # Recruiter profile
│   │   │   └── Candidate.java          # Candidate profile
│   │   │
│   │   ├── structure/
│   │   │   ├── Organization.java       # Fair organization
│   │   │   └── Booth.java              # Recruiter booth
│   │   │
│   │   ├── booking/
│   │   │   ├── Offer.java              # Interview offer slot
│   │   │   ├── Reservation.java        # Booking confirmation
│   │   │   ├── MeetingSession.java     # Meeting details
│   │   │   ├── Lobby.java              # Virtual room
│   │   │   └── Request.java            # Booking request
│   │   │
│   │   ├── enums/
│   │   │   └── FairPhase.java          # Phase enumeration
│   │   │
│   │   └── profiles/
│   │       └── CandidateProfile.java   # Extended candidate info
│   │
│   ├── views/
│   │   ├── shared/
│   │   │   ├── MainMenuFrame.java      # Role selection UI
│   │   │   ├── AdminLoginFrame.java    # Admin login
│   │   │   ├── LoginFrame.java         # Recruiter login
│   │   │   ├── CandidateLoginFrame.java # Candidate login
│   │   │   ├── MultiPortalDemoWindow.java # Demo mode window
│   │   │   └── SystemTimerScreen.java  # Timeline control
│   │   │
│   │   ├── admin/
│   │   │   └── AdminScreen.java        # Admin dashboard (6 tabs)
│   │   │
│   │   ├── recruiter/
│   │   │   ├── RecruiterScreen.java    # Recruiter dashboard
│   │   │   ├── PublishOfferPanel.java  # Offer publishing tab
│   │   │   ├── SchedulePanel.java      # Schedule management tab
│   │   │   ├── VirtualRoomPanel.java   # Meeting room tab
│   │   │   └── RecruiterView.java      # View interface
│   │   │
│   │   └── candidate/
│   │       └── CandidateScreen.java    # Candidate dashboard (4 tabs)
│   │
│   ├── controllers/
│   │   ├── AdminScreenController.java
│   │   ├── RecruiterController.java
│   │   └── CandidateController.java
│   │
│   └── integration/
│       └── [Integration tests]
│
├── build/bin/                           # 60 compiled .class files
├── logs/                                # System log files
├── JUNIT_TEST_REPORT.txt               # Test results (112/112 passing)
└── README.md                            # This file
```

---

## 🔧 System Components

### **1. CareerFairSystem (vcfs/core/CareerFairSystem.java)**
**Type**: Singleton | **Lines**: ~850 | **Responsibility**: Core business logic

**Key Methods**:
| Method | Purpose | Returns |
|--------|---------|---------|
| `getInstance()` | Get singleton instance | CareerFairSystem |
| `addOrganization(name)` | Create organization | void |
| `registerRecruiter(name, email, booth)` | Add recruiter | Recruiter |
| `registerCandidate(name, email, bio, tags)` | Add candidate | Candidate |
| `publishOffer(recruiter, title, capacity, tags, startTime, endTime)` | Create interview offer | Offer |
| `autoBook(candidate, tags, maxAppointments)` | Auto-match offers | List<Reservation> |
| `getAllOffers()` | Get all offers | List<Offer> |
| `firePropertyChange(event, oldVal, newVal)` | Broadcast event | void |

### **2. AdminScreen (vcfs/views/admin/AdminScreen.java)**
**Type**: JFrame | **Lines**: ~1000 | **Components**: 6 Tabs

**Tabs**:
1. **Setup & Configuration** - Organization setup, booth management, timeline config
2. **Organizations** - Table of all organizations with booth counts
3. **Recruiters** - Table of recruiters assigned to offers
4. **Candidates** - Table of registered candidates with skills
5. **Offers** - Table of published offers with booking status
6. **Audit Log & System Events** - Real-time event stream

**Auto-Refresh**: Each table updates when:
- New organization added
- Recruiter registered
- Candidate registers
- Offer published
- Interview booked

### **3. RecruiterScreen (vcfs/views/recruiter/RecruiterScreen.java)**
**Type**: JFrame | **Lines**: ~500 | **Components**: 3 Tabs

**Tabs**:
1. **Publish Offer** - Create and manage interview offers
2. **Schedule** - View booked interviews and meeting times
3. **Virtual Room** - Join virtual meeting rooms

**Auto-Refresh**: Updates when:
- New offers published system-wide
- Candidate books an interview

### **4. CandidateScreen (vcfs/views/candidate/CandidateScreen.java)**
**Type**: JFrame | **Lines**: ~500 | **Components**: 4 Tabs

**Tabs**:
1. **Browse Offers** - See all available interview slots
2. **Search & Book** - Smart tag-based offer matching
3. **My Schedule** - Confirmed interview bookings
4. **Virtual Room** - Enter video meeting Lobby

**Auto-Refresh**: Updates when:
- New offers published
- Other candidates book slots (availability changes)

---

## 🎮 How to Use

### **Administrator Workflow**

```
1. Login: admin / admin123
   ↓
2. Setup & Configuration Tab
   → Create Organizations (Google, Microsoft, etc.)
   → Set Fair Timeline (Start/End dates)
   ↓
3. Monitor in Real-Time
   → Organizations Tab - See created orgs
   → Recruiters Tab - See recruiters from offers
   → Candidates Tab - See registered candidates
   → Offers Tab - See published interview offers
   → Audit Log - See all system events
```

### **Recruiter Workflow**

```
1. Login: [Name of registered recruiter]
   → Alice Thompson, Bob Chen, or Carol Singh (in demo mode)
   ↓
2. Publish Offer Tab
   → Fill interview title, duration, tags, capacity
   → Click "Publish Offer"
   ↓
3. Schedule Tab
   → View booked interview times
   ↓
4. Virtual Room Tab
   → View waiting candidates
   → Start virtual meeting
```

### **Candidate Workflow**

```
1. Login: [Email of registered candidate]
   → david.lee@example.com
   → elena.rodriguez@example.com
   → frank.williams@example.com (in demo mode)
   ↓
2. Browse Offers Tab
   → See all available interview slots
   ↓
3. Search & Book Tab
   → Enter skill tags (Java, Python, AWS, etc.)
   → Set max appointments (1-5)
   → Click "Search & Book"
   → System auto-matches and confirms booking
   ↓
4. My Schedule Tab
   → View confirmed interview times
   ↓
5. Virtual Room Tab
   → When meeting time arrives, join lobby
   → Connect with recruiter
```

### **Demo Mode (Side-by-Side)**

```
1. Click "⚡ DEMO MODE - All 3 Portals"
   ↓
2. Three windows open:
   - AdminScreen (1200×800) - Monitor all activity
   - RecruiterScreen (900×700) - Publish offers
   - CandidateScreen (900×700) - Book interviews
   ↓
3. Test Live Synchronization:
   - Admin creates organization → See instantly in dropdowns
   - Recruiter publishes offer → See instantly in Candidate browse
   - Candidate books interview → See booking count update in Admin
```

---

## 💻 Technologies Used

| Category | Technology | Version | Usage |
|----------|-----------|---------|-------|
| **Language** | Java | 17+ | Core application |
| **GUI Framework** | Swing | Built-in | Multi-window UI |
| **Testing** | JUnit 5 | 5.x | 112+ unit tests |
| **Architecture** | MVC | N/A | Model-View-Controller |
| **Design Patterns** | Multiple | N/A | Singleton, Observer, State, Strategy |
| **Build Tool** | javac | Built-in | Compilation |
| **IDE Compatible** | VS Code, IntelliJ | N/A | Development |

### **Thread Safety**
- `Collections.synchronizedList()` for thread-safe collections
- `volatile` keywords for shared variables
- `PropertyChangeSupport` for event broadcasting

### **Coding Standards**
- **Naming**: Camel-case variables, PascalCase classes
- **Comments**: Comprehensive Javadoc on all public methods
- **Validation**: Null checks and empty string validation
- **Logging**: Multi-level logging (INFO, WARNING, ERROR, CRITICAL)

---

## ✅ Testing & Quality

### **Test Coverage: 112/112 Tests Passing**

```
Total Tests: 112
- Unit Tests: 95
- Integration Tests: 17
- Pass Rate: 100% ✅
```

### **Test Categories**

1. **Model Tests** - Data integrity, state validation
2. **Business Logic Tests** - Offer matching, booking algorithms
3. **Controller Tests** - UI command delegation
4. **Integration Tests** - Cross-component communication
5. **Event Tests** - PropertyChangeListener functionality

### **Code Quality Metrics**

- ✅ Zero compilation warnings
- ✅ All methods have Javadoc comments
- ✅ Input validation on all public methods
- ✅ Consistent naming conventions throughout
- ✅ No code duplication (DRY principle)
- ✅ Proper exception handling

### **Run Tests**

```bash
# Using JUnit 5
java -cp build/bin:lib/junit-jupiter-api-5.x.jar org.junit.runner.JUnitCore [TestClass]

# Or using IDE test runners (VS Code, IntelliJ, Eclipse)
```

**Test Report**: See `JUNIT_TEST_REPORT.txt` for detailed results.

---

## 👥 Team & Contributions

### **Project Team**

| Role | Name | ID | Contributions |
|------|------|----|----|
| **Project Manager & Lead Developer** | Zaid Siddiqui | mzs00007 | Architecture, Core Systems, UI Integration |
| **Recruiter Portal** | Taha | CodeByTaha18 | RecruiterScreen, PublishOfferPanel |
| **UI/UX** | YAMI | leiyam | AdminScreen design, UI helpers |
| **Candidate Portal** | MJAMishkat | - | CandidateScreen, booking logic |
| **Testing & QA** | Mohamed | - | JUnit tests, quality assurance |

### **Key Development Phases**

**Phase 1: Architecture & Design** (Week 1-2)
- MVC structure planning
- Design pattern selection
- Component identification

**Phase 2: Core Implementation** (Week 3-4)
- CareerFairSystem singleton
- Data models
- Business logic

**Phase 3: UI Development** (Week 5-6)
- AdminScreen tabs
- RecruiterScreen components
- CandidateScreen features

**Phase 4: Integration & Testing** (Week 7)
- PropertyChangeListener wiring
- JUnit test suite
- Multi-portal synchronization

**Phase 5: Refinement** (Week 8)
- Responsive UI upgrades
- Login consistency fixes
- Performance optimization

---

## 📊 Demonstration Guide

### **Live Demo Script (15 minutes)**

#### **Part 1: System Overview (2 min)**
```
"This is VCFS - Virtual Career Fair System. It has three portals:
 - Admin manages organizations and timeline
 - Recruiters publish interview offers
 - Candidates search and book interviews"
 
Show: MainMenuFrame with 3 role buttons
```

#### **Part 2: Demo Mode (5 min)**
```
Click: "⚡ DEMO MODE"
→ AdminScreen opens (left)
→ RecruiterScreen opens (middle)
→ CandidateScreen opens (right)

"Three portals running simultaneously in demo mode"
"Watch data sync in real-time across all three"
```

#### **Part 3: Admin Portal (3 min)**
```
Show AdminScreen tabs:
- Setup: Organization creation ✅
- Organizations: All 3 created orgs ✅
- Recruiters: 3 recruiters from offers ✅
- Candidates: 3 demo candidates ✅
- Offers: 9 published interview slots ✅  
- Audit Log: Real-time event stream ✅

"All tables auto-refresh when data changes"
```

#### **Part 4: Recruiter Portal (3 min)**
```
Show RecruiterScreen (PublishOfferPanel):
- "My Published Offers" tab with 3 offers ✅
- Show: Title, Duration, Capacity, Booking count
- Demonstrate: Click refresh → instant update

"Each recruiter sees only their own offers"
"Real-time booking count updates"
```

#### **Part 5: Candidate Portal (2 min)**
```
Show CandidateScreen tabs:
- Browse Offers: All 9 offers visible ✅
- Search & Book: Tag-based matching ✅
- My Schedule: Confirmed bookings ✅
- Virtual Room: Meeting lobby ✅

"Candidates can instantly search by skills"
"System auto-matches with available offers"
```

#### **Part 6: Live Synchronization Demo (1 min)**
```
Action: In Recruiter portal, publish new offer
Watch: 
→ AdminScreen Offers table updates instantly ✅
→ CandidateScreen Browse Offers updates ✅

"All three portals sync in real-time"
"PropertyChangeListener pattern enables this"
```

---

## 🔍 Troubleshooting

### **Common Issues & Solutions**

| Issue | Cause | Solution |
|-------|-------|----------|
| **"Cannot find vcfs.App"** | Classpath incorrect | Run from `_FOR_GITHUB_SUBMISSION` directory |
| **"No offers showing"** | Demo data not initialized | Clean `build/` folder and recompile |
| **"Login loops infinitely"** | Recruiter already registered | Use demo names: Alice Thompson, Bob Chen, Carol Singh |
| **"Tables not scrolling"** | Window too small | Resize to 900×700 minimum |
| **"Port already in use"** | Another instance running | Check Windows Task Manager, close previous Java instance |

### **Performance Tips**

- Compile to `build/bin` directory before running
- Use `-Xmx512m` flag for more RAM if needed:
  ```bash
  java -Xmx512m -cp build/bin vcfs.App
  ```
- Clean logs periodically:
  ```bash
  del logs\*.txt
  ```

---

## 📄 Submission Checklist

- ✅ All 47 Java source files included
- ✅ 60 compiled .class files in build/bin/
- ✅ Comprehensive README (this file)
- ✅ JUnit test report (112/112 passing)
- ✅ Demo data pre-loaded
- ✅ BUILD_AND_RUN_DEMO.bat for Windows users
- ✅ Zero compilation errors
- ✅ Full JavaDoc comments
- ✅ MVC architecture verified
- ✅ All design patterns implemented
- ✅ Multi-portal synchronization working
- ✅ Responsive UI with scrollbars
- ✅ Production-ready code

---

## 📞 Support & Questions

For issues or questions regarding this submission:

1. Check `JUNIT_TEST_REPORT.txt` for test details
2. Review system logs in `logs/` directory
3. Check `logs/system.log` for error messages
4. Verify demo data loading in application console

---

## 📝 License

This project is developed for academic purposes as part of CSCU9P6 module at the University of Stirling.

---

**Last Updated**: April 8, 2026  
**Build Status**: ✅ Production Ready  
**Compilation**: ✅ Zero Errors  
**Tests**: ✅ 112/112 Passing

---

