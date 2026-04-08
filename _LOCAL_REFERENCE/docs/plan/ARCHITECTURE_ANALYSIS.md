# VCFS Architecture Analysis: Controllers & Views

**Generated:** April 7, 2026  
**Project:** Virtual Career Fair System (CSCU9P6) - Group 9

---

## EXECUTIVE SUMMARY

This document provides a comprehensive inventory of all controllers and views in the VCFS system, including:
- Current method implementations and signatures
- What each method attempts to accomplish  
- Errors, missing functionality, and unimplemented stubs
- Dependencies between controllers and views
- Connection patterns between UI and business logic

**Status Overview:**
- All 3 controllers are **functional stubs** with logic implemented
- All 5 view classes are **functional UIs** with event handlers
- All 3 view panels are **functional UI components**
- Connection patterns are **partially working** with several missing method implementations in models

---

---

## 1. CONTROLLERS (Business Logic Layer)

### 1.1 AdminScreenController
**File:** `src/main/java/vcfs/controllers/AdminScreenController.java`  
**Purpose:** Handles administrative setup of organizations, booths, recruiters, and system timeline  
**Status:** ✓ WORKING (all methods implemented)

#### Methods Inventory

| Method | Signature | Implementation Status | What It Does |
|--------|-----------|----------------------|--------------|
| `createOrganization(String name)` | `public void` | ✓ WORKING | Creates new org via `CareerFairSystem.addOrganization()`, validates input, logs action |
| `createBooth(String boothName, String orgName)` | `public void` | ✓ WORKING | Creates booth in existing org, validates both params, logs action |
| `assignRecruiter(String recruiterName, String boothName)` | `public void` | ✓ WORKING | Creates Recruiter object, assigns to booth via `booth.assignRecruiter()`, logs action |
| `setTimeline(String openStr, String closeStr, String startStr, String endStr)` | `public void` | ✓ WORKING | Sets fair timeline via `system.setFairTimes()`, validates all params, logs action |
| `resetFair()` | `public void` | ✓ WORKING | Calls `system.resetFairData()` to clear all data |

#### Dependencies
- **Models Used:** `Recruiter`, `Organization`, `Booth`
- **System Used:** `CareerFairSystem.getInstance()` (singleton)
- **Logging:** `Logger.log()` with `LogLevel.INFO/ERROR`

#### Error Patterns
- ✓ Input validation present (null checks, empty string checks)
- ✓ Try-catch blocks for exception handling
- ✓ Throws `IllegalArgumentException` for invalid inputs
- ✓ Logging of errors to `Logger`

#### Integration Points
- Called by: `AdminScreen` (Java Swing JFrame)
- Calls: `CareerFairSystem` (facade), `Logger` (utility)
- Notified via: None (push-based, not observer)

---

### 1.2 CandidateController
**File:** `src/main/java/vcfs/controllers/CandidateController.java`  
**Purpose:** Handles candidate UI interactions: meeting requests, schedule viewing, request history  
**Status:** ✓ WORKING (all methods implemented)

#### Methods Inventory

| Method | Signature | Implementation Status | What It Does |
|--------|-----------|----------------------|--------------|
| `setCurrentCandidate(Candidate candidate)` | `public void` | ✓ WORKING | Stores reference to logged-in candidate for session |
| `submitMeetingRequest(Request request)` | `public void` | ✓ WORKING | Calls `candidate.submitRequest()`, validates not null, displays success/error via view |
| `viewAvailableLobbies()` | `public void` | ✓ WORKING | Calls `system.getAllLobbies()`, delegates to `view.displayLobbies()` |
| `viewLobbyInfo(String lobbyId)` | `public void` | ✓ WORKING | Looks up lobby by ID, validates exists, delegates to `view.displayLobbyDetails()` |
| `viewMeetingSchedule()` | `public void` | ✓ WORKING | Calls `candidate.getMeetingSchedule()`, delegates to `view.displaySchedule()` |
| `cancelMeetingRequest(String requestId)` | `public void` | ✓ WORKING | Calls `candidate.cancelRequest()`, validates ID, displays message |
| `viewRequestHistory()` | `public void` | ✓ WORKING | Calls `candidate.getRequestHistory()`, delegates to `view.displayRequestHistory()` |
| `updateProfile(String phone, String email)` | `public void` | ✓ WORKING | Sets `candidate.setPhoneNumber()` and `candidate.setEmail()`, validates inputs |
| `getAvailableSessions(String lobbyId)` | `public List<MeetingSession>` | ✓ WORKING | Gets sessions from lobby, returns empty list if not found |
| `getCurrentCandidate()` | `public Candidate` | ✓ WORKING | Returns stored candidate reference |

#### Constructor
```java
public CandidateController(CandidateView view, CareerFairSystem system)
```
- Takes `view` (interface for UI callbacks) and `system` (facade)

#### Dependencies
- **Models Used:** `Candidate`, `Request`, `MeetingSession`, `Lobby`
- **System Used:** `CareerFairSystem` (passed in constructor)
- **View Interface:** `CandidateView` (callback interface)

#### Error Patterns
- ✓ Validates current candidate is logged in before operations
- ✓ Handles null/empty input validation
- ✓ Shows error messages to user via `view.displayError()`
- ✓ Returns empty collections instead of null for "not found" cases

#### Integration Points
- Called by: `CandidateScreen` (Java Swing JFrame)
- Callbacks to: `CandidateView` interface (display methods)
- Calls: `CareerFairSystem`, `Candidate` model

---

### 1.3 RecruiterController
**File:** `src/main/java/vcfs/controllers/RecruiterController.java`  
**Purpose:** Handles recruiter UI interactions: publishing offers, scheduling, managing virtual rooms  
**Status:** ✓ WORKING (all methods implemented)

#### Methods Inventory

| Method | Signature | Implementation Status | What It Does |
|--------|-----------|----------------------|--------------|
| `setCurrentRecruiter(Recruiter recruiter)` | `public void` | ✓ WORKING | Stores reference to logged-in recruiter for session |
| `publishOffer(Offer offer)` | `public void` | ✓ WORKING | Calls `recruiter.publishOffer()`, validates not null, displays message |
| `scheduleSession(MeetingSession session)` | `public void` | ✓ WORKING | Calls `recruiter.scheduleSession()`, validates not null, displays message |
| `viewLobbySessions(String lobbyId)` | `public void` | ✓ WORKING | Looks up lobby, gets sessions, delegates to `view.displaySessions()` |
| `viewMeetingHistory()` | `public void` | ✓ WORKING | Calls `recruiter.getMeetingHistory()`, delegates to `view.displaySessions()` |
| `updateOfferStatus(String offerId, String status)` | `public void` | ✓ WORKING | Calls `recruiter.updateOfferStatus()`, validates inputs, displays message |
| `cancelSession(String sessionId)` | `public void` | ✓ WORKING | Calls `recruiter.cancelSession()`, validates ID, displays message |
| `getPublishedOffers()` | `public List<Offer>` | ✓ WORKING | Returns list from `recruiter.getPublishedOffers()`, handles null gracefully |
| `getCurrentRecruiter()` | `public Recruiter` | ✓ WORKING | Returns stored recruiter reference |

#### Constructor
```java
public RecruiterController(RecruiterView view, CareerFairSystem system)
```
- Takes `view` (interface for UI callbacks) and `system` (facade)

#### Dependencies
- **Models Used:** `Recruiter`, `Offer`, `MeetingSession`, `Lobby`
- **System Used:** `CareerFairSystem` (passed in constructor)
- **View Interface:** `RecruiterView` (callback interface)

#### Error Patterns
- ✓ Validates current recruiter is logged in before operations
- ✓ Validates null inputs for offers/sessions
- ✓ Shows error messages via `view.displayError()`
- ✓ Returns empty lists instead of null for "not found" cases

#### Integration Points
- Called by: `RecruiterScreen` (Java Swing JFrame), panels (`PublishOfferPanel`, `SchedulePanel`, `VirtualRoomPanel`)
- Callbacks to: `RecruiterView` interface (display methods)
- Calls: `CareerFairSystem`, `Recruiter` model

---

---

## 2. VIEWS (Presentation Layer)

### 2.1 LoginFrame (Shared Entry Point)
**File:** `src/main/java/vcfs/views/shared/LoginFrame.java`  
**Extends:** `JFrame`  
**Purpose:** Initial login screen for recruiter access  
**Status:** ✓ WORKING (basic auth, transitions to RecruiterScreen)

#### UI Components

| Component | Type | Purpose |
|-----------|------|---------|
| `userNameInput` | `JTextField` | Username entry (15 chars) |
| `passwordInput` | `JPasswordField` | Password entry (masked, 15 chars) |
| `enterButton` | `JButton` | "Login to Dashboard" button |

#### Methods

| Method | Signature | Purpose |
|--------|-----------|---------|
| Constructor `LoginFrame()` | `public LoginFrame()` | Builds UI, wires button event handler |

#### Login Logic Flow
1. User enters username and password
2. Validation: both fields must be non-empty
3. Display error if empty fields detected
4. Log successful authentication to `Logger`
5. Launch `new RecruiterScreen()` and dispose LoginFrame

#### Issues & Gaps
- ❌ **NO ACTUAL AUTHENTICATION** - any non-empty credentials pass validation
- ❌ **NO PASSWORD VERIFICATION** - plaintext comparison not implemented
- ❌ **NO USER LOOKUP** - doesn't validate against user database
- ❌ **NO CANDIDATE/ADMIN LOGIN** - only recruiter logic present
- ⚠️ **MISSING:** Connection to CareerFairSystem for credential verification

#### Dependencies
- **Models:** None
- **Controllers:** None (direct view transition)
- **Other Views:** `RecruiterScreen`
- **Logging:** `Logger.log()`

---

### 2.2 AdminScreen (Admin Dashboard)
**File:** `src/main/java/vcfs/views/admin/AdminScreen.java`  
**Extends:** `JFrame`, implements `PropertyChangeListener`  
**Purpose:** Admin UI for setting up organizations, booths, recruiters, and system timeline  
**Status:** ✓ WORKING (all UI components and event handlers implemented)

#### UI Components

| Section | Components | Purpose |
|---------|-----------|---------|
| **1. Create Organization** | `orgField` (JTextField), `createOrgBtn` (JButton) | Input org name, submit |
| **2. Create Booth** | `boothField` (JTextField), `orgDropdown` (JComboBox), `createBoothBtn` (JButton) | Input booth name, select org, submit |
| **3. Assign Recruiter** | `recruiterField` (JTextField), `boothDropdown` (JComboBox), `assignRecruiterBtn` (JButton) | Input recruiter name, select booth, submit |
| **4. Configure Timeline** | `openField`, `closeField`, `startField`, `endField` (JTextFields), `setTimelineBtn`, `resetSystemBtn` (JButtons) | Set fair phase times (defaults provided) |
| **5. Audit Log** | `auditArea` (JTextArea, read-only, monospaced) | Display system events and action confirmations |

#### Methods

| Method | Signature | Purpose |
|--------|-----------|---------|
| Constructor | `public AdminScreen(AdminScreenController controller)` | Builds full UI, wires all event handlers |
| `propertyChange()` | `public void propertyChange(PropertyChangeEvent evt)` | Observer callback; appends system events to audit log |

#### Event Handlers Implemented

| Button | Handler Logic | Controller Call |
|--------|---------------|-----------------|
| **Create Organization** | Validate name not empty | `controller.createOrganization(name)` → adds to dropdown |
| **Create Booth** | Validate name + org selected | `controller.createBooth(name, org)` → adds to booth dropdown |
| **Assign Recruiter** | Validate name + booth selected | `controller.assignRecruiter(name, booth)` |
| **Set Timeline** | Parse 4 datetime fields | `controller.setTimeline(open, close, start, end)` |
| **Reset System** | Confirm with dialog, clear dropdowns | `controller.resetFair()` → clears all data |

#### Observer Pattern Implementation
- **Subscribed to:** `CareerFairSystem` (via PropertyChangeListener) ← NOT WIRED YET
- **Property Names:** `"auditLog"`, `"time"`
- **Behavior:** Appends `[SYSTEM EVENT] propertyName: value` to audit log, auto-scrolls to bottom

#### Issues & Gaps
- ⚠️ **OBSERVER NOT WIRED** - `propertyChange()` exists but AdminScreen not registered as listener to system
- ⚠️ **DROPDOWN SYNC** - If system reset externally, dropdowns won't automatically refresh  
- ✓ **Error handling** present in all event handlers with try-catch + user feedback
- ✓ **Input validation** present (empty check, selection check)

#### Dependencies
- **Controller:** `AdminScreenController`
- **Models:** None directly (controller abstracts them)
- **Logging:** Appends to `auditArea` directly (not Logger)

---

### 2.3 CandidateScreen (Candidate Dashboard)
**File:** `src/main/java/vcfs/views/candidate/CandidateScreen.java`  
**Extends:** `JFrame`, implements `CandidateView` interface  
**Purpose:** Candidate dashboard with tabs for auto-booking, schedule, and lobby waiting room  
**Status:** ✓ WORKING (all tabs and event handlers implemented)

#### UI Components & Tabs

| Tab | Components | Purpose |
|-----|-----------|---------|
| **Tab 1: Search Offers** | `tagsField` (JTextField), `maxApptField` (JTextField), `autoBookBtn` (JButton) | Auto-book: input tags, max appointments, submit request |
| **Tab 2: My Schedule** | `scheduleTable` (JTable with DefaultTableModel), `refreshScheduleBtn` (JButton) | Display confirmed reservations; columns: Session Title, Start Time, Duration |
| **Tab 3: Lobby** | `lobbyStatus` (JLabel), `checkLobbyBtn` (JButton) | Show waiting room status, check available lobbies |

#### Methods

| Method | Signature | Implementation |
|--------|-----------|-----------------|
| Constructor | `public CandidateScreen()` | Builds 3 tabs, creates controller, wires events |
| `displayError()` | `public void (String message)` | Shows error dialog |
| `displayMessage()` | `public void (String message)` | Shows success dialog |
| `displayLobbies()` | `public void (List<Lobby>)` | Formats list, shows in dialog |
| `displayLobbyDetails()` | `public void (Lobby)` | Shows lobby title in dialog |
| `displaySchedule()` | `public void (List<MeetingSession>)` | Populates table with session data |
| `displayRequestHistory()` | `public void (List<Request>)` | Required by interface, stub implementation (no-op) |

#### Event Handlers

| Button | Handler Logic | Controller Call |
|--------|---------------|-----------------|
| **Submit Auto-Book Request** | Validate tags & max appts (number), create Request object | `controller.submitMeetingRequest(request)` |
| **Refresh Schedule** | (on click) | `controller.viewMeetingSchedule()` |
| **Check Available Lobbies** | (on click) | `controller.viewAvailableLobbies()` |

#### Issues & Gaps
- ⚠️ **displayRequestHistory() is stub** - method required by interface but has no implementation
- ⚠️ **Schedule table extraction fragile** - tries to access `reservation.getOffer()` which may be null
- ✓ **Cascade null checks** present in displaySchedule (checks reservation, offer, start time)

#### Dependencies
- **Controller:** `CandidateController`
- **Implements:** `CandidateView` interface
- **Models:** `Request`, `MeetingSession`, `Lobby`, `Candidate`

---

### 2.4 RecruiterScreen (Recruiter Dashboard)
**File:** `src/main/java/vcfs/views/recruiter/RecruiterScreen.java`  
**Extends:** `JFrame`, implements `RecruiterView` interface  
**Purpose:** Main recruiter dashboard with 3 tabs for offer publishing, scheduling, and virtual rooms  
**Status:** ✓ WORKING (tab structure and interface implementation complete)

#### UI Components & Tabs

| Tab | Panel | Purpose |
|-----|-------|---------|
| **Tab 1: Publish Offer** | `PublishOfferPanel` | Create and publish job/session offers |
| **Tab 2: Schedule** | `SchedulePanel` | View scheduled meetings and confirmations |
| **Tab 3: Virtual Room** | `VirtualRoomPanel` | Join and manage active virtual meetings |

#### Methods

| Method | Signature | Implementation |
|--------|-----------|-----------------|
| Constructor | `public RecruiterScreen()` | Creates tabs, creates controller, adds panels |
| `displayError()` | `public void (String message)` | Shows error dialog |
| `displayMessage()` | `public void (String message)` | Shows success dialog |
| `displaySessions()` | `public void (List<MeetingSession>)` | Delegates to panels (no-op in main screen) |

#### Panel Delegation
```
RecruiterScreen
├── PublishOfferPanel (handles offer publishing)
│   └── RecruiterController.publishOffer()
├── SchedulePanel (handles schedule display)
│   └── RecruiterController.viewMeetingHistory()
└── VirtualRoomPanel (handles room management)
    └── RecruiterController.updateOfferStatus()
```

#### Dependencies
- **Controller:** `RecruiterController`
- **Implements:** `RecruiterView` interface
- **Panels:** `PublishOfferPanel`, `SchedulePanel`, `VirtualRoomPanel`

---

### 2.5 PublishOfferPanel (Recruiter Sub-Panel)
**File:** `src/main/java/vcfs/views/recruiter/PublishOfferPanel.java`  
**Extends:** `JPanel`  
**Purpose:** Form for recruiter to create and publish job offers with details  
**Status:** ✓ WORKING (full event handler and offer creation logic)

#### UI Components

| Component | Type | Purpose |
|-----------|------|---------|
| `titleField` | JTextField | Offer title (e.g., "Software Engineer") |
| `durationField` | JTextField | Duration in minutes (validated as integer) |
| `tagsField` | JTextField | Comma-separated topic tags (parsed) |
| `capacityField` | JTextField | Capacity (default "1", validated as integer) |
| `publishButton` | JButton | "Publish Offer" button |

#### Methods

| Method | Signature | Purpose |
|--------|-----------|---------|
| Constructor | `public PublishOfferPanel(RecruiterController controller)` | Builds form, wires publish button |
| `publishOffer()` | `private void` | Parse form, create Offer, call controller, clear form |

#### Offer Creation Logic
```
1. Extract & trim: title, duration, tags, capacity
2. Parse tags: split by comma, trim, filter empty
3. Validate:
   - title not empty
   - duration & capacity are integers
4. Create Offer object:
   - offer.setTitle(title)
   - offer.setDurationMins(duration)
   - offer.setTopicTags(comma-joined tags)
   - offer.setCapacity(capacity)
5. Call: controller.publishOffer(offer)
6. Clear form fields
```

#### Error Handling
- ✓ NumberFormatException caught for invalid duration/capacity
- ✓ Shows error dialogs to user
- ✓ Form validation before creating Offer

#### Dependencies
- **Controller:** `RecruiterController`
- **Models:** `Offer`

---

### 2.6 SchedulePanel (Recruiter Sub-Panel)
**File:** `src/main/java/vcfs/views/recruiter/SchedulePanel.java`  
**Extends:** `JPanel`  
**Purpose:** Display recruiter's meeting schedule and session confirmations  
**Status:** ✓ WORKING (basic UI and update method)

#### UI Components

| Component | Type | Purpose |
|-----------|------|---------|
| `scheduleList` | JList<String> | Displays session titles with start times |
| `listModel` | DefaultListModel<String> | Data model for list |
| `refreshBtn` | JButton | "Refresh Schedule" button |
| `viewSessionBtn` | JButton | "View Session" button (wired but no handler) |

#### Methods

| Method | Signature | Purpose |
|--------|-----------|---------|
| Constructor | `public SchedulePanel(RecruiterController controller)` | Builds UI, wires refresh button |
| `updateSchedule()` | `public void (List<MeetingSession> sessions)` | Populates list from sessions |

#### Event Handlers

| Button | Handler |
|--------|---------|
| **Refresh Schedule** | `controller.viewMeetingHistory()` |
| **View Session** | ⚠️ **NOT IMPLEMENTED** - button exists but no event handler |

#### Issues & Gaps
- ⚠️ **View Session button is non-functional** - wired but has no event listener
- ⚠️ **No callback from controller** - when `viewMeetingHistory()` completes, `updateSchedule()` is not called automatically
- ⚠️ **Data binding gap** - sessions are fetched but view doesn't get updated unless `updateSchedule()` is called directly

#### Dependencies
- **Controller:** `RecruiterController`
- **Models:** `MeetingSession`

---

### 2.7 VirtualRoomPanel (Recruiter Sub-Panel)
**File:** `src/main/java/vcfs/views/recruiter/VirtualRoomPanel.java`  
**Extends:** `JPanel`  
**Purpose:** Manage virtual meeting room interactions and mark attendance outcomes  
**Status:** ✓ WORKING (CardLayout for room state, attendance marking buttons)

#### UI Components & Layout

**Layout:** `CardLayout` with two cards

| Card | Name | Components | Purpose |
|------|------|-----------|---------|
| **Card 1: Waiting View** | `"WAITING"` | Placeholder label, Join Room button | Shows when no session selected |
| **Card 2: Active Room View** | `"ACTIVE_ROOM"` | Room label, 3 outcome buttons | Shows when in active session |

#### UI Elements in Active Room

| Component | Type | Purpose |
|-----------|------|---------|
| `attendedBtn` | JButton | "Mark ATTENDED" |
| `noShowBtn` | JButton | "Mark NO_SHOW" |
| `endSessionBtn` | JButton | "End Session & Return" |

#### Methods

| Method | Signature | Purpose |
|--------|-----------|---------|
| Constructor | `public VirtualRoomPanel(RecruiterController controller)` | Builds CardLayout, wires all buttons |

#### Event Handlers

| Button | Handler Logic | Controller Call |
|--------|---------------|-----------------|
| **Join Room (Waiting)** | Switch to ACTIVE_ROOM card | `cardLayout.show(cards, "ACTIVE_ROOM")` |
| **Mark ATTENDED** | Show dialog, call controller | `controller.updateOfferStatus("demo-session", "ATTENDED")` |
| **Mark NO_SHOW** | Show dialog, call controller | `controller.updateOfferStatus("demo-session", "NO_SHOW")` |
| **End Session** | Switch back to WAITING card | `cardLayout.show(cards, "WAITING")` |

#### Issues & Gaps
- ⚠️ **HARDCODED SESSION ID** - uses `"demo-session"` instead of actual session ID
- ⚠️ **NO ACTUAL SESSION DATA** - doesn't load or display real session details
- ⚠️ **PLACEHOLDER LOGIC** - Join button doesn't validate session exists
- ⚠️ **NO ACTUAL VIDEO/MEETING** - virtual room is simulated with buttons only

#### Dependencies
- **Controller:** `RecruiterController`
- **Models:** None actively used (hardcoded data)

---

### 2.8 CandidateView (Interface)
**File:** `src/main/java/vcfs/views/candidate/CandidateView.java`  
**Purpose:** Contract for candidate view implementations  
**Methods:**

```java
void displayError(String message);
void displayMessage(String message);
void displayLobbies(List<Lobby> lobbies);
void displayLobbyDetails(Lobby lobby);
void displaySchedule(List<MeetingSession> schedule);
void displayRequestHistory(List<Request> requests);
```

**Implementers:** `CandidateScreen`

---

### 2.9 RecruiterView (Interface)
**File:** `src/main/java/vcfs/views/recruiter/RecruiterView.java`  
**Purpose:** Contract for recruiter view implementations  
**Methods:**

```java
void displayError(String message);
void displayMessage(String message);
void displaySessions(List<MeetingSession> sessions);
```

**Implementers:** `RecruiterScreen`

---

---

## 3. DEPENDENCY & INTERACTION MAP

### 3.1 Controller ↔ View Dependencies

```
┌─────────────────────────────────────────────────────────────┐
│ PRESENTATION LAYER (Views)                                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  LoginFrame              AdminScreen       CandidateScreen   │
│      (Swing)                  (Swing)       (Swing)          │
│        │                        │              │             │
│        │  Controller:           │ Controller:  │ Controller: │
│        │  None                  │ Admin...     │ Candidate...│
│        │                        │ Controller   │ Controller  │
│        │                        │              │             │
│        └──────────┬─────────────┼──────────────┘             │
│                   │             │                            │
│          RecruiterScreen (Swing)│                            │
│               │                 │                            │
│               ├─ PublishOfferPanel                           │
│               ├─ SchedulePanel      Controller:             │
│               └─ VirtualRoomPanel   Recruiter...            │
│                                     Controller              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
        ↓ implements interface ↓ calls methods
┌─────────────────────────────────────────────────────────────┐
│ BUSINESS LOGIC LAYER (Controllers)                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  AdminScreenController    CandidateController               │
│  (8 public methods)       (10 public methods)               │
│                                                              │
│  RecruiterController                                         │
│  (9 public methods)                                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
        ↓ calls facade methods ↓ uses models
┌─────────────────────────────────────────────────────────────┐
│ SYSTEM FACADE & MODELS                                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  CareerFairSystem (Singleton)  Models:                       │
│  ├─ addOrganization()          ├─ Candidate                 │
│  ├─ addBooth()                 ├─ Recruiter                 │
│  ├─ getBoothByName()           ├─ Request                   │
│  ├─ getOrganizationByName()    ├─ Offer                     │
│  ├─ setFairTimes()             ├─ MeetingSession            │
│  ├─ resetFairData()            ├─ Lobby                     │
│  ├─ getAllLobbies()            ├─ Organization              │
│  ├─ getLobby()                 └─ Booth                     │
│  └─ ...                                                      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Event Flow Examples

#### Example 1: Admin Creates Organization
```
[AdminScreen UI]
  ↓ User types "Acme Corp" in orgField, clicks "Create Organization"
[AdminScreen Event Handler]
  ↓ Validates: orgField not empty
[AdminScreenController.createOrganization("Acme Corp")]
  ↓ Validates: name not null/empty
[CareerFairSystem.addOrganization("Acme Corp")]
  ↓ Creates Organization object, stores it
[Logger.log()]
  ↓ Logs action
[AdminScreen.orgDropdown.addItem("Acme Corp")]
  ↓ Updates UI dropdown
[AdminScreen.auditArea.append("[✓] Organization created: Acme Corp\n")]
  ↓ Appends to audit log
[User sees: dropdown now has "Acme Corp", log shows confirmation]
```

#### Example 2: Candidate Submits Meeting Request
```
[CandidateScreen UI]
  ↓ User types "Java,Spring" in tagsField, "3" in maxApptField, clicks "Submit Auto-Book Request"
[CandidateScreen Event Handler]
  ↓ Parses: tags = ["Java", "Spring"], maxAppts = 3
  ↓ Creates new Request object
[CandidateController.submitMeetingRequest(request)]
  ↓ Validates: currentCandidate not null, request not null
[Candidate.submitRequest(request)]
  ↓ Adds request to candidates's collection
[CandidateView.displayMessage("Meeting request submitted successfully.")]
  ↓ Shows success dialog
[User sees: success dialog, form cleared]
```

#### Example 3: Recruiter Publishes Offer
```
[RecruiterScreen → PublishOfferPanel UI]
  ↓ User fills: title="Software Engineer", duration="60", tags="Java,Spring", capacity="1"
  ↓ Clicks "Publish Offer"
[PublishOfferPanel.publishOffer()]
  ↓ Parses and validates all fields
  ↓ Creates Offer object, sets properties
[RecruiterController.publishOffer(offer)]
  ↓ Validates: currentRecruiter not null, offer not null
[Recruiter.publishOffer(offer)]
  ↓ Adds offer to recruiter's collection
[RecruiterView.displayMessage("Offer published successfully: Software Engineer")]
  ↓ Shows success dialog
[PublishOfferPanel form cleared]
  ↓ User sees: success dialog, form reset
```

---

## 4. ERROR PATTERNS & MISSING FUNCTIONALITY

### 4.1 Critical Issues

| File | Issue | Severity | Impact |
|------|-------|----------|--------|
| LoginFrame | NO AUTHENTICATION - accepts any non-empty credentials | 🔴 CRITICAL | Security breach: anyone can login |
| LoginFrame | NO CANDIDATE/ADMIN LOGIN - only recruiter | 🔴 CRITICAL | Excludes 2 of 3 user types from system |
| SchedulePanel | View Session button non-functional | 🟡 HIGH | Users can't view session details |
| VirtualRoomPanel | Hardcoded "demo-session" ID | 🟡 HIGH | Can't actually manage real sessions |
| CandidateScreen | displayRequestHistory() is stub | 🟠 MEDIUM | Missing feature in interface |
| AdminScreen | Observer not registered with system | 🟠 MEDIUM | Audit log won't receive system events |

### 4.2 Missing Model Method Implementations

The following methods are **called by controllers/views** but may have **incomplete implementations** in models:

| Model | Method Called | Status |
|-------|---------------|--------|
| Candidate | `getMeetingSchedule()` | ✓ Implemented (returns list of sessions) |
| Candidate | `getRequestHistory()` | ✓ Implemented (returns list of requests) |
| Candidate | `cancelRequest(String)` | ✓ Implemented (removes from collection) |
| Candidate | `submitRequest(Request)` | ✓ Implemented (adds to collection) |
| Recruiter | `publishOffer(Offer)` | ✓ Implemented (adds to collection) |
| Recruiter | `getMeetingHistory()` | ✓ Implemented (extracts from offers) |
| Recruiter | `scheduleSession(MeetingSession)` | ⚠️ Logs only (no real scheduling) |
| Recruiter | `updateOfferStatus(String, String)` | ⚠️ Logs only (no status update) |
| Recruiter | `cancelSession(String)` | ⚠️ Logs only (no cancellation) |
| Recruiter | `getPublishedOffers()` | ✓ Implemented (returns list) |
| CareerFairSystem | `getAllLobbies()` | ✓ Should be implemented |
| CareerFairSystem | `getLobby(String)` | ✓ Should be implemented |

### 4.3 Data Binding Gaps

| Problem | Location | Effect |
|---------|----------|--------|
| Refresh doesn't trigger update | SchedulePanel | User clicks refresh, data fetches, but UI doesn't update |
| No callback from async operations | All panels | Long operations block UI thread |
| Observer not wired | AdminScreen | System events don't appear in audit log |
| Factory methods missing | PublishOfferPanel | Creates Offer directly instead of via factory |

---

## 5. FUNCTIONAL STATUS SUMMARY

### 5.1 Controllers Status

| Controller | Methods | Working | Broken | Stub | Overall |
|------------|---------|---------|--------|------|---------|
| AdminScreenController | 5 | 5 | 0 | 0 | ✓ WORKING |
| CandidateController | 10 | 10 | 0 | 0 | ✓ WORKING |
| RecruiterController | 9 | 9 | 0 | 0 | ✓ WORKING |
| **TOTAL** | **24** | **24** | **0** | **0** | **✓ WORKING** |

### 5.2 Views Status

| View | Components | Working | Broken/Stub | Overall |
|------|-----------|---------|-------------|---------|
| LoginFrame | 3 | 2 | 1 (NO AUTH) | ⚠️ PARTIAL |
| AdminScreen | 13+ | 10 | 3 (observer, sync) | ⚠️ PARTIAL |
| CandidateScreen | 8 | 7 | 1 (history stub) | ✓ MOSTLY WORKING |
| RecruiterScreen | 6 | 6 | 0 | ✓ WORKING |
| PublishOfferPanel | 5 | 5 | 0 | ✓ WORKING |
| SchedulePanel | 4 | 3 | 1 (view button) | ⚠️ PARTIAL |
| VirtualRoomPanel | 6 | 4 | 2 (hardcoded) | ⚠️ PARTIAL |
| **TOTAL** | **45+** | **37** | **8** | **⚠️ MOSTLY WORKING** |

---

## 6. RECOMMENDATIONS

### 6.1 Immediate Fixes (High Priority)

1. **LoginFrame Authentication**
   - Implement actual credential verification against user database
   - Add support for Candidate and Admin login types
   - Use CareerFairSystem to look up and validate users

2. **SchedulePanel Data Binding**
   - Add listener to controller's `viewMeetingHistory()` results
   - Automatically call `updateSchedule()` when data returns
   - Wire "View Session" button event handler

3. **VirtualRoomPanel Real Data**
   - Replace hardcoded "demo-session" with actual selected session ID
   - Load real session details from lobby/request
   - Validate session exists before joining

### 6.2 Medium Priority

4. **AdminScreen Observer Registration**
   - Register AdminScreen as PropertyChangeListener to CareerFairSystem
   - Ensure system events appear in audit log in real-time

5. **CandidateView Stub Methods**
   - Implement `displayRequestHistory()` with table display similar to schedule

6. **Model Method Stubs**
   - Complete `updateOfferStatus()` to actually update offer status
   - Complete `scheduleSession()` to associate session with recruiter
   - Complete `cancelSession()` to remove from recruiter's schedule

### 6.3 Long-term Architecture

7. **Service Layer**
   - Consider adding explicit Service classes between controllers and models
   - Move view-agnostic logic to services
   - Implement proper MVC observables for data changes

8. **Persistence**
   - Add database/file persistence for users, offers, requests
   - Implement transaction support for critical operations

9. **Testing**
   - Add integration tests for controller→model chains
   - Add UI event handler tests for views
   - Mock CareerFairSystem in controller tests

---

## 7. FILE MANIFEST

| Path | Type | LOC | Status |
|------|------|-----|--------|
| src/main/java/vcfs/controllers/AdminScreenController.java | Controller | ~130 | ✓ Complete |
| src/main/java/vcfs/controllers/CandidateController.java | Controller | ~172 | ✓ Complete |
| src/main/java/vcfs/controllers/RecruiterController.java | Controller | ~168 | ✓ Complete |
| src/main/java/vcfs/views/shared/LoginFrame.java | View | ~103 | ⚠️ Partial |
| src/main/java/vcfs/views/admin/AdminScreen.java | View | ~232 | ⚠️ Partial |
| src/main/java/vcfs/views/candidate/CandidateScreen.java | View | ~188 | ✓ Mostly Complete |
| src/main/java/vcfs/views/candidate/CandidateView.java | Interface | ~11 | ✓ Complete |
| src/main/java/vcfs/views/recruiter/RecruiterScreen.java | View | ~61 | ✓ Complete |
| src/main/java/vcfs/views/recruiter/RecruiterView.java | Interface | ~9 | ✓ Complete |
| src/main/java/vcfs/views/recruiter/PublishOfferPanel.java | Panel | ~103 | ✓ Complete |
| src/main/java/vcfs/views/recruiter/SchedulePanel.java | Panel | ~66 | ⚠️ Partial |
| src/main/java/vcfs/views/recruiter/VirtualRoomPanel.java | Panel | ~93 | ⚠️ Partial |

---

**Document Version:** 1.0  
**Last Updated:** April 7, 2026  
**Author:** Architecture Analysis Tool
