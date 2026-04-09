# End-to-End Testing Guide - Virtual Career Fair System

**Status:** ✅ System Ready - 100 classes compiled
**Last Update:** Phase 8 - Column Index Bug Fix Applied
**Focus:** Verify Candidate booking flow updates Schedule & Virtual Room tabs

---

## Quick System Check

### Before Starting Tests
```
✓ All 100 classes compiled
✓ CandidateScreen.java line 694-697: Column indices fixed (2,3 → 5,6)
✓ CandidateController: refresh methods called after booking
✓ SchedulePanel: Recruiter schedule display ready
✓ UserSession: Data syncing working
```

---

## Test Suite 1: Candidate Booking Flow (CRITICAL)

### Objective
Verify that when a candidate books an interview:
- Step 2 (My Schedule) updates immediately with the booking
- Step 3 (Virtual Room) displays booking with all 8 columns
- Join button shows correct interview details

### Prerequisites
- Admin account created with organization and booth
- Recruiter account created with published offers
- Candidate account ready for testing

### Test 1.1: Browse and Book Interview

**Steps:**
1. Launch system → Select "Candidate Portal"
2. Login with test candidate account
3. Navigate to "Step 1: Browse Offers"
4. **VERIFY:** Offers from recruiter appear in table
5. Find an offer and click "Book Interview"
6. **VERIFY:** Success message appears: "✓ You have successfully booked this interview!"

**Expected Output:**
- Success dialog shows:
  - Interview title
  - Recruiter name
  - Duration in minutes
  - "Check 'My Schedule' for details"

**Evidence:** Screenshot of success message

---

### Test 1.2: Schedule Tab Updates After Booking

**Steps:**
1. (Continue from Test 1.1)
2. Click on "**Step 2: My Schedule**" tab
3. Wait for table to load (should auto-refresh)
4. **CHECK:** New booking appears in table

**Expected Output:**
Table should contain row with:
- Interview title + recruiter name
- Scheduled start time
- Duration (in minutes)
- Status: "CONFIRMED"

**If Empty:**
- Check browser console for errors
- Verify refreshScheduleTable() was called (check system.log)
- Confirm UserSession updated with new candidate

**Evidence:** Screenshot of populated schedule table

---

### Test 1.3: Virtual Room Tab Shows Complete Interview Details

**Steps:**
1. (Continue from Test 1.2)
2. Click on "**Step 3: Join Virtual Room**" tab
3. Wait for table to load
4. **VERIFY:** Booking appears with ALL columns

**Expected Columns (8 total):**
1. **Interview** - Offer title
2. **Recruiter** - Recruiter display name
3. **Email** - Recruiter email address
4. **Duration** - Interview duration (e.g., "30")
5. **Company** - Organization name
6. **Scheduled Time** - Full timestamp
7. **Status** - "CONFIRMED"
8. **Join** - "🎥 Join Room 1"

**Verification Checklist:**
- [ ] Interview column shows correct offer title
- [ ] Recruiter column shows recruiter name
- [ ] Email column shows recruiter@email.com (not blank)
- [ ] Duration shows number (e.g., 30)
- [ ] Company shows organization name (not "Company" or blank)
- [ ] Time shows correct timestamp
- [ ] Status shows "CONFIRMED"
- [ ] Join button shows "🎥 Join Room 1"

**If Column Data Missing:**
- Email shows "N/A" → Recruiter object missing email field
- Company shows "Company" → Publisher.getBooth().getOrganization() is null
- Time shows "TBD" → Reservation.getScheduledStart() is null

**Evidence:** Screenshot of virtual room table with all 8 columns

---

### Test 1.4: Join Button Shows Correct Details

**Steps:**
1. (Continue from Test 1.3)
2. Click on the "🎥 Join Room 1" cell in the table
3. **CRITICAL:** Verify dialog contents match booking details

**Expected Dialog Shows:**
```
Join '<offer title>' interview with <recruiter name>?

Time: <scheduled_start_timestamp>
Status: CONFIRMED
```

**Column Index Verification:**
- **Time field should show** the value from **column 5** (Scheduled Time)
  - If it shows an email or number → wrong column (bug not fixed)
  - If it shows "TBD" → Scheduled start is null
- **Status field should show** the value from **column 6** (Status)
  - If it shows a number or different text → wrong column (bug not fixed)

**If Bug Still Exists:**
- Time showing email address → Code still reads column 2
- Time showing duration number → Code still reads column 3
- **FIX:** CandidateScreen.java lines 694-697 need:
  - Column 5 for scheduledTime (not 2)
  - Column 6 for status (not 3)

**Evidence:** Screenshot of join dialog (must show correct time and status)

---

## Test Suite 2: Recruiter Schedule Display

### Objective
Verify that recruiter can see incoming booking requests in Step 2

### Test 2.1: Recruiter Portal Access

**Steps:**
1. (Keep candidate logged in from previous test)
2. Launch new browser window/tab
3. System → Login as recruiter (who published the offer)
4. Navigate to "Step 2: Manage Schedule"
5. Click "Refresh Schedule" button

**Expected Output:**
- Table shows the candidate booking:
  - Candidate Name: <candidate display name>
  - Email: <candidate email>
  - Interview: <offer title>
  - Status: CONFIRMED
  - Scheduled Time: <timestamp>

**Verification Checklist:**
- [ ] Candidate name appears
- [ ] Candidate email appears
- [ ] Interview title matches offer
- [ ] Status shows "CONFIRMED"
- [ ] Time shows same timestamp as candidate's booking

**Evidence:** Screenshot of recruiter schedule table

---

## Test Suite 3: Auto-Booking Feature

### Objective
Verify that "Submit Auto-Book Requests" also triggers table refresh

### Test 3.1: Auto-Book Multiple Offers

**Steps:**
1. Login as candidate (fresh or existing)
2. Go to Step 1: Browse Offers
3. Click "Submit Auto-Book Requests" button
4. Select all available offers
5. Click "Auto-Book Selected"
6. **VERIFY:** Success message appears

**Expected Output:**
- Success message: "✓ Successfully booked X/Y interviews"
- "Check 'My Schedule' tab to see your confirmed bookings"

**Evidence:** Screenshot of success message

---

### Test 3.2: Auto-Book Refreshes Schedule Tab

**Steps:**
1. (Continue from Test 3.1)
2. Click "Step 2: My Schedule" tab
3. **VERIFY:** All booked interviews appear

**Expected Output:**
- Multiple rows in schedule table
- Each row shows correct:
  - Interview title
  - Recruiter name
  - Start time
  - Duration
  - "CONFIRMED" status

**Evidence:** Screenshot of schedule with multiple bookings

---

## Test Suite 4: Data Consistency

### Objective
Verify that the same booking appears correctly in all three tabs

### Test 4.1: Three-Tab Consistency

**Steps:**
1. Book one interview (Test 1.1-1.2)
2. Navigate to Step 1: Browse Offers
3. **VERIFY:** Offer still shows (may show as booked if implemented)
4. Navigate to Step 2: My Schedule
5. **VERIFY:** Booking appears here
6. Navigate to Step 3: Virtual Room
7. **VERIFY:** Same booking appears here with full details

**Expected:**
- Same offer appears in all three places
- Virtual Room has the most complete information
- Schedule shows simplified version
- All timestamps match

**Evidence:** Screenshots of all three tabs showing same booking

---

## Test Suite 5: Error Handling

### Test 5.1: Check System Logs

**How to Find Logs:**
- Look for console output or system log file
- Search for: `[CandidateScreen]`, `[CandidateController]`

**Key Log Messages (Should See):**
```
[CandidateController] [BOOKING COMPLETE] About to refresh UI...
[CandidateController] [BOOKING COMPLETE] Calling refreshScheduleTable()...
[CandidateScreen] refreshScheduleTable() CALLED
[CandidateScreen] Cleared schedule table
[CandidateScreen] Processing X reservation(s)...
[CandidateScreen] Added reservation: <offer title>
[CandidateScreen] Schedule table now has X rows
[CandidateScreen] refreshVirtualRoomTable() CALLED
[CandidateScreen] FINAL: Virtual room table now has X rows
```

**If Logs Missing:**
- Methods not being called
- Check CandidateController.java lines 218-323
- Verify view.refreshScheduleTable() and view.refreshVirtualRoomTable() are called

**Evidence:** Console/log screenshot showing refresh sequence

---

## Debugging Checklist

### If Step 2 (My Schedule) Shows Empty:

1. **Check Booking Created:**
   - Verify database/UserSession has reservation
   - Try clicking "Refresh Schedule" manually

2. **Check refreshScheduleTable() Called:**
   - Look for log: "refreshScheduleTable() CALLED"
   - Check CandidateController line ~321

3. **Check Data Query:**
   - Log should show: "Processing X reservation(s)"
   - If shows 0, issue is in UserSession

4. **Check Table Update:**
   - Log should show: "Schedule table now has X rows"
   - If still 0, data query issue

---

### If Join Button Shows Wrong Data:

1. **Expected (CORRECT):**
   - Time shows: `2024-01-15 14:30:00` (Scheduled Time)
   - Status shows: `CONFIRMED` (Status)

2. **Bug Signs (INCORRECT):**
   - Time shows: `recruiter@email.com` → Reading column 2 (Email)
   - Time shows: `30` → Reading column 3 (Duration)
   - Status shows: `Amazon` → Reading column 4 (Company)

3. **Fix Location:**
   - File: [CandidateScreen.java](src/main/java/vcfs/views/candidate/CandidateScreen.java#L694-L697)
   - Lines 694-697 must read columns: 0, 1, **5**, **6** (not 0, 1, 2, 3)

---

### If Virtual Room Shows Only 5 Columns:

1. **Problem:** Table initialization not updated
2. **Check:** CandidateScreen.java line ~620
3. **Fix:** Must have column headers:
   ```
   "Interview", "Recruiter", "Email", "Duration", "Company", "Scheduled Time", "Status", "Join"
   ```

---

## Test Results Template

```
TEST DATE: ___________
TESTER: ___________________

CANDIDATE PORTAL
[ ] Test 1.1: Browse and Book Interview ✓/✗ Notes: _______________
[ ] Test 1.2: Schedule Tab Updates ✓/✗ Notes: _______________
[ ] Test 1.3: Virtual Room Shows All Columns ✓/✗ Notes: _______________
[ ] Test 1.4: Join Button Shows Correct Details ✓/✗ Notes: _______________

RECRUITER PORTAL
[ ] Test 2.1: Recruiter Schedule Display ✓/✗ Notes: _______________

AUTO-BOOKING
[ ] Test 3.1: Auto-Book Multiple Offers ✓/✗ Notes: _______________
[ ] Test 3.2: Schedule Updates with Auto-Book ✓/✗ Notes: _______________

DATA CONSISTENCY
[ ] Test 4.1: Three-Tab Consistency ✓/✗ Notes: _______________

LOGS & ERRORS
[ ] Test 5.1: System Logs Show Refresh Sequence ✓/✗ Notes: _______________

OVERALL STATUS: ✓ PASS / ✗ FAIL
BLOCKING ISSUES: _______________________________________________________
NOTES: ___________________________________________________________________
```

---

## Quick Fix Commands

### Recompile After Changes
```bash
cd "path\to\CSCU9P6_GROUP_18_CODE"
javac -d bin -cp "lib/*" (Get-ChildItem -Path 'src\main\java' -Recurse -Filter "*.java").FullName
```

### Verify Compilation
```bash
(Get-ChildItem -Path bin\vcfs -Recurse -Filter "*.class" | Measure-Object).Count
# Should show: 100
```

---

## Expected Test Duration
- Full test suite: ~30-45 minutes
- Critical path (1.1-1.4): ~10-15 minutes
- Recruiter test: ~10 minutes

## Success Criteria
- ✅ Step 2 shows booking after booking made
- ✅ Step 3 shows all 8 columns with correct data
- ✅ Join button shows correct time and status
- ✅ Recruiter portal shows incoming bookings
- ✅ No SQL/compilation errors in logs
