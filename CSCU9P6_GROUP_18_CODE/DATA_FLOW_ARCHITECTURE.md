# Virtual Career Fair System - Data Flow Architecture

**Reference:** For understanding how booking data flows from creation to UI display

---

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    VIRTUAL CAREER FAIR SYSTEM                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  CANDIDATE PORTAL              RECRUITER PORTAL    ADMIN        │
│  ├─ Step 1: Browse              ├─ Step 1: Publish            │
│  ├─ Step 2: My Schedule          ├─ Step 2: Manage Bookings    │
│  ├─ Step 3: Virtual Room         └─ Step 3: Virtual Room       │
│  └─ Step 4: Outcomes             └─ Step 4: Results            │
│                                                                 │
│  ↓                ↓                    ↓                        │
│  
│  └──────────────→ CONTROLLER ←────────────────────────────────┘
│                       ↓
│  ┌──────────────→ USERSSESSION ←──────────────────────────────┐
│  │                    │                                        │
│  │  currentCandidate  │  currentRecruiter  Admin               │
│  │  ├─ profile        └─ profile          Active Session       │
│  │  ├─ offers         ├─ offers                               │
│  │  ├─ reservations   └─ reservations                         │
│  │  └─ ...                                                    │
│  │                                                            │
│  └────────────────────┬────────────────────────────────────────┘
│                       ↓
└─ REFRESH METHODS (refreshScheduleTable, refreshVirtualRoomTable)
                       ↓
            ┌──────────────────────┐
            │   TABLE MODELS       │
            │ ├─ scheduleTableModel│
            │ ├─ roomTableModel    │
            │ └─ ...               │
            └──────────────────────┘
                       ↓
            ┌──────────────────────┐
            │    UI DISPLAY        │
            │ (JTable in Tabs)     │
            └──────────────────────┘
```

---

## Booking Flow: Step-by-Step

### 1. Offer Published (Recruiter Step 1)

**Flow:**
```
PublishOfferPanel (View)
    ↓
RecruiterController.publishOffer()
    ↓
CareerFairSystem.addOffer()
    ↓
Offer stored in system
    ↓
Broadcast: "offers" property changed
    ↓
CandidateScreen receives update
    ↓
Step 1: Browse Offers displays new offer
```

---

### 2. Candidate Books Interview (Candidate Step 1)

**Flow:**
```
CandidateScreen.Browse Tab
    ↓
User clicks "Book Interview" button
    ↓
CandidateController.bookOffer(offer)
    ↓
├─ Create Reservation object
├─ Save to database
├─ Add reservation to offer.reservations
└─ Add reservation to candidate.reservations
    ↓
UserSession.setCurrentCandidate(updatedCandidate)
    ↓
[CRITICAL FIX] Controller calls THREE refresh methods:
    ├─ viewMeetingSchedule()
    ├─ view.refreshScheduleTable()  ← STEP 2 TAB UPDATES HERE
    └─ view.refreshVirtualRoomTable() ← STEP 3 TAB UPDATES HERE
```

**Code Location:** CandidateController.java lines 310-326

---

### 3. Refresh Schedule Table (Candidate Step 2)

**Flow:**
```
CandidateController.bookOffer() calls:
    view.refreshScheduleTable()
    
CandidateScreen.refreshScheduleTable() (lines 1359-1401)
    ├─ Get fresh candidate from UserSession
    ├─ scheduleTableModel.setRowCount(0)  [Clear old data]
    ├─ for each reservation in candidate.reservations:
    │   └─ Create row: [Title+Recruiter, Time, Duration, Status]
    ├─ Add row to scheduleTableModel
    └─ Swing automatically updates JTable with new model
    
Result: Step 2 "My Schedule" tab now shows booking
```

**Code Location:** CandidateScreen.java lines 1359-1401

**Data Retrieved:**
- `candidate.getReservations()` → List of all bookings
- For each reservation:
  - `reservation.getOffer().getTitle()` → Interview name
  - `reservation.getOffer().getPublisher().getDisplayName()` → Recruiter name
  - `reservation.getScheduledStart()` → When interview is
  - `reservation.getStatus()` → CONFIRMED/PENDING
  - `reservation.getOffer().getDurationMins()` → How long

---

### 4. Refresh Virtual Room Table (Candidate Step 3)

**Flow:**
```
CandidateController.bookOffer() calls:
    view.refreshVirtualRoomTable()
    
CandidateScreen.refreshVirtualRoomTable() (lines 1408-1500)
    ├─ Get fresh candidate from UserSession
    ├─ roomTableModel.setRowCount(0)  [Clear old data]
    ├─ int roomNumber = 1
    ├─ for each reservation in candidate.reservations:
    │   ├─ Get offer from reservation
    │   ├─ Get recruiter details:
    │   │   ├─ Title: offer.getTitle()
    │   │   ├─ Recruiter: offer.getPublisher().getDisplayName()
    │   │   ├─ Email: offer.getPublisher().getEmail()
    │   │   ├─ Duration: offer.getDurationMins()
    │   │   ├─ Company: offer.getPublisher().getBooth().getOrganization().getName()
    │   │   ├─ Time: reservation.getScheduledStart()
    │   │   ├─ Status: reservation.getStatus()
    │   │   └─ Button: "🎥 Join Room " + roomNumber
    │   ├─ ADD ALL 8 COLUMNS TO ROW
    │   └─ roomNumber++
    └─ Swing updates JTable with new model
    
Result: Step 3 "Virtual Room" tab shows booking with full details
```

**Code Location:** CandidateScreen.java lines 1408-1500

**8-Column Output:**
1. Interview → offer.getTitle()
2. Recruiter → offer.getPublisher().getDisplayName()
3. Email → offer.getPublisher().getEmail()
4. Duration → offer.getDurationMins()
5. Company → offer.getPublisher().getBooth().getOrganization().getName()
6. Scheduled Time → reservation.getScheduledStart()
7. Status → reservation.getStatus()
8. Join → "🎥 Join Room " + roomNumber

---

### 5. Join Button Click (User Interaction)

**Flow:**
```
User clicks on "Join Room X" cell in Virtual Room table
    ↓
Mouse listener fires (CandidateScreen.java lines 689-705)
    ├─ Get row clicked
    ├─ Verify column is 7 (Join column)
    ├─ Read cell values from correct columns:
    │   ├─ Column 0: interviewTitle
    │   ├─ Column 1: recruiterName
    │   ├─ Column 5: scheduledTime  [NOT 2 - THIS WAS THE BUG]
    │   └─ Column 6: status         [NOT 3 - THIS WAS THE BUG]
    ├─ Show dialog with details
    └─ If user clicks "Yes": Open virtual room
    
Result: Dialog shows correct interview details
```

**Code Location:** CandidateScreen.java lines 689-730

**Critical Fix (Phase 8):**
- Line 694: `scheduledTime = roomTableModel.getValueAt(row, 5)` (was 2)
- Line 695: `status = roomTableModel.getValueAt(row, 6)` (was 3)

---

## Recruiter Schedule Display (Recruiter Step 2)

**Flow:**
```
RecruiterScreen → Step 2: Manage Schedule
    ↓
SchedulePanel.loadSchedule() runs on init
    ↓
SchedulePanel.refreshScheduleTable()
    ├─ Get recruiter from controller
    ├─ Get all offers: recruiter.getOffers()
    ├─ for each offer:
    │   ├─ Get reservations: offer.getReservations()
    │   ├─ for each reservation:
    │   │   ├─ Get candidate: reservation.getCandidate()
    │   │   └─ Add row: [CandidateName, Email, OfferTitle, Status, Time, View]
    │   └─ Schedule table model updated
    └─ Swing displays table
    
Result: Recruiter sees all incoming bookings for their offers
```

**Code Location:** SchedulePanel.java lines 195-245

**Table Shows:**
1. Candidate Name
2. Candidate Email
3. Interview Title
4. Booking Status
5. Scheduled Time
6. View Details Link

---

## Data Consistency Rules

### Rule 1: Always Get Fresh Data from UserSession
```java
// CORRECT - Gets latest state
Candidate current = UserSession.getInstance().getCurrentCandidate();

// WRONG - Uses old reference
Candidate old = this.cachedCandidate;
```

### Rule 2: Clear Table Before Refresh
```java
// MUST DO THIS FIRST
tableModel.setRowCount(0);

// Then populate with fresh data
```

### Rule 3: Call Refresh Methods Explicitly
```java
// After booking is complete:
view.refreshScheduleTable();
view.refreshVirtualRoomTable();

// Not automatic - must be called intentionally
```

---

## Error Scenarios & Recovery

### Scenario 1: Step 2 Shows Empty After Booking

**Root Causes:**
1. `refreshScheduleTable()` not called
   - Check: CandidateController.java line 321
   - Fix: Add `view.refreshScheduleTable();`

2. `UserSession` not updated with new candidate
   - Check: CandidateController.java line 315
   - Fix: Add `UserSession.getInstance().setCurrentCandidate(candidate);`

3. Reservation not added to candidate
   - Check: Database/model logic
   - Fix: Verify `candidate.getReservations().add(newReservation)`

**Recovery Steps:**
1. Check logs for: `[CandidateScreen] refreshScheduleTable() CALLED`
2. If missing → Refresh method not called
3. If called but table empty → Check: `Processing 0 reservation(s)`
4. If 0 reservations → Issue is in booking/UserSession

---

### Scenario 2: Virtual Room Shows Wrong Data in Join Dialog

**Root Cause:** Wrong column indices in mouse listener

**Symptoms:**
- Time field shows email, phone, or number → Column 2 or 3 being read
- Status field shows company name or other data → Wrong column

**Code Issue:**
```java
// WRONG (reads columns 2, 3)
String scheduledTime = (String) roomTableModel.getValueAt(row, 2);
String status = (String) roomTableModel.getValueAt(row, 3);

// CORRECT (reads columns 5, 6)
String scheduledTime = (String) roomTableModel.getValueAt(row, 5);
String status = (String) roomTableModel.getValueAt(row, 6);
```

**Fix File:** [CandidateScreen.java](src/main/java/vcfs/views/candidate/CandidateScreen.java#L694-L697)

---

### Scenario 3: Company Name Shows as "Company" (Fallback)

**Root Cause:** Publisher.getBooth().getOrganization() is null

**Recovery:**
1. Check: `offer.getPublisher()` not null ✓
2. Check: `getPublisher().getBooth()` not null ✓
3. Check: `getBooth().getOrganization()` not null ✗ (problem)

**Fix:**
- Verify booth has organization assigned
- Check organization creation in admin console
- Verify recruiter linked to correct booth

---

## Testing the Data Flow

### Test 1: Verify UserSession Sync
```
1. Book interview as candidate
2. Console should show:
   [CandidateController] UserSession.setCurrentCandidate() called
   [CandidateScreen] Got candidate from UserSession: <name>
```

### Test 2: Verify Refresh Sequence
```
Console should show in order:
[CandidateController] [BOOKING COMPLETE] About to refresh UI...
[CandidateController] [BOOKING COMPLETE] Calling refreshScheduleTable()...
[CandidateScreen] refreshScheduleTable() CALLED
[CandidateScreen] Processing X reservation(s)...
[CandidateScreen] Schedule table now has X rows
[CandidateController] [BOOKING COMPLETE] About to refresh virtual room...
[CandidateScreen] refreshVirtualRoomTable() CALLED
[CandidateScreen] FINAL: Virtual room table now has X rows
```

### Test 3: Verify Column Indices
```
1. Click Join button
2. Dialog appears with:
   ✓ Interview title (column 0)
   ✓ Recruiter name (column 1)
   ✓ Time showing timestamp (column 5 - NOT email)
   ✓ Status showing CONFIRMED (column 6 - NOT duration)
```

---

## Performance Notes

- **SchedulePanel Refresh:** O(offers × reservations) complexity
- **VirtualRoomTable Refresh:** O(reservations) complexity
- **Table Updates:** Automatic via Swing JTable model
- **UserSession Lookup:** O(1) access to current objects

**Optimization Potential:**
- Cache offer list instead of querying each refresh
- Implement incremental updates instead of full table clear
- Use PropertyChangeListener for auto-refresh

---

## Related Code Files

| File | Responsibility | Lines |
|------|---|---|
| [CandidateController.java](src/main/java/vcfs/controllers/CandidateController.java) | Booking logic + refresh trigger | 310-326 |
| [CandidateScreen.java](src/main/java/vcfs/views/candidate/CandidateScreen.java) | Table display + refresh methods | 1359-1500 |
| [RecruiterController.java](src/main/java/vcfs/controllers/RecruiterController.java) | Recruiter operations | 82+ |
| [SchedulePanel.java](src/main/java/vcfs/views/recruiter/SchedulePanel.java) | Recruiter schedule display | 195-245 |
| [UserSession.java](src/main/java/vcfs/core/UserSession.java) | Session state management | - |

---

## Summary

**Key Insight:** The system uses a **refresh-on-demand** pattern:
1. Create/modify data in controller
2. Update UserSession with new state
3. Explicitly call refresh methods
4. Refresh methods pull from UserSession
5. UI updates automatically via Swing model

**This ensures:**
- ✅ Consistency between tabs
- ✅ Fresh data on every refresh
- ✅ Explicit control over when updates happen
- ✅ Clear debugging visibility (step-by-step logging)

**Phase 8 Achievement:** Fixed column index bug that prevented join dialog from showing correct interview time and status.
