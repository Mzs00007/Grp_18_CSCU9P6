# 🎬 VCFS DEMO READINESS GUIDE

**Status:** ✅ **READY FOR DEMO** | Compilation: **ZERO ERRORS** | Architecture: **MVC COMPLIANT**

---

## 📋 DEMO SCRIPT - WHAT TO SHOW

### **PART 1: SYSTEM LAUNCH (30 seconds)**
```
1. Open Command Prompt
2. Navigate to project folder
3. Run: java -cp bin vcfs.Main
4. Wait for MainMenuFrame to appear
   - Shows 3 role buttons (Admin, Recruiter, Candidate)
   - Title: "VCFS - Virtual Career Fair System"
   - Color-coded (Red=Admin, Blue=Recruiter, Green=Candidate)
```

---

## 🎭 DEMO FLOW - COMPLETE SCENARIO

### **SCENARIO: Admin Setup Fair, Recruiter Creates Offer, Candidate Books Appointment**

---

## 🏗️ PART A: ADMIN FLOW (3-5 minutes)

### **Admin Login**
1. Click "Administrator" button on Main Menu
2. You'll see **AdminLoginFrame**:
   - Title: "VCFS Administrator Login"
   - Has login fields for username/password
   - Default credentials: admin / admin123

### **Admin Dashboard (AdminScreen)**
After successful login, you'll see the **Admin Dashboard** with:

#### **Section 1: Organization Management**
- **List of Organizations** (left panel with JList)
- **Add Organization Button**
  - Click → TextArea asks for organization name
  - Input: "Tech Innovations Corp"
  - Shows success: ✓ Organization added

#### **Section 2: Booth Management**
- **List of Booths** (middle panel)
- **Add Booth Button**
  - Select organization: "Tech Innovations Corp"
  - Input booth name: "Main Booth"
  - Shows success: ✓ Booth created

#### **Section 3: Recruiter Management**
- **Add Recruiter Button**
  - Input recruiter name: "John Smith"
  - Select booth: "Main Booth"
  - Shows success: ✓ Recruiter assigned

#### **Section 4: Fair Timeline Configuration**
- **Set Fair Times Button**
  - Bookings Open: Select date/time (30 mins from now)
  - Bookings Close: Select date/time (1 hour from now)
  - Fair Start: Select date/time (2 hours from now)
  - Fair End: Select date/time (6 hours from now)
  - Shows: ✓ Timeline configured

#### **Section 5: System Control**
- **SystemTimer Button**
  - Opens SystemTimer window
  - Shows current system time
  - Has buttons to move time forward
  - Use to advance system time to fair operating windows

---

## 📊 PART B: RECRUITER FLOW (3-5 minutes)

### **Recruiter Login**
1. Click "Recruiter" button on Main Menu
2. **LoginFrame** appears:
   - Email field: recruiter@company.com (or any email)
   - Password field: any password
   - Click "Login"

### **Recruiter Dashboard (RecruiterScreen)**
After login, you'll see the **Recruiter Dashboard** with 3 main panels using **CardLayout**:

#### **Panel 1: Publish Offers (PublishOfferPanel)**
- **Form with fields:**
  - Offer Title: "Graduate Scheme 2026"
  - Duration (minutes): "30"
  - Capacity (number of candidates): "5"
  - Tags (comma-separated): "graduate, tech, london"
  - Description: "An exciting opportunity..."
  
- **Action:**
  - Click "Publish Offer" button
  - Shows success message: ✓ Offer published
  - Offer appears in schedule panel

#### **Panel 2: Schedule & Availability (SchedulePanel)**
- **List of published offers**
  - Shows: "Graduate Scheme 2026" (30 min, 5 capacity)
  
- **Add Availability Button**
  - Select offer: "Graduate Scheme 2026"
  - Start time: 10:00 AM (in fair window)
  - End time: 12:00 PM (in fair window)
  - Shows: ✓ Availability block added

- **View Schedule**
  - Shows generated appointment slots
  - Example: 10:00-10:30, 10:30-11:00, 11:00-11:30, etc.

#### **Panel 3: Virtual Room & Meetings (VirtualRoomPanel)**
- **Dual-view interface:**
  - View 1: Waiting Room (before fair starts)
    - Shows candidates in lobby
    - List: [waiting list of candidates]
  
  - View 2: Active Session (during fair)
    - Shows current meeting
    - Recruiter name + Candidate name
    - "End Session" button
    - Attendance tracking

- **Attendance Recording:**
  - After session ends, records:
    - ATTENDED ✓
    - NO_SHOW ✗
    - ENDED_EARLY ⏱

---

## 👤 PART C: CANDIDATE FLOW (3-5 minutes)

### **Candidate Registration & Login**
1. Click "Candidate" button on Main Menu
2. **CandidateLoginFrame** appears:
   - Email field: "candidate@email.com"
   - Display Name: "Alice Johnson"
   - Click "Register & Login"

### **Candidate Dashboard (CandidateScreen)**
After login, you'll see the **Candidate Dashboard** with multiple sections:

#### **Section 1: Browse Organizations & Booths**
- **Left Panel: Organizations**
  - List: "Tech Innovations Corp", "Finance Ltd", etc.
  - Click organization → shows booths
  
- **Middle Panel: Booths**
  - Shows selected org's booths
  - Click booth → shows offers

#### **Section 2: View Available Offers**
- **Right Panel: Offers**
  - Title: "Graduate Scheme 2026"
  - Duration: 30 minutes
  - Capacity: 5
  - Tags: graduate, tech, london
  - Recruiter: "John Smith"

#### **Section 3: Browse Available Appointments**
- **Appointment Slots**
  - Shows available times generated from recruiter's availability blocks
  - Example: 10:00-10:30, 10:30-11:00, 11:00-11:30, etc.
  - Color-coded: Green=Available, Red=Booked, Gray=Passed

#### **Section 4: Make Reservation**
- **Option 1: Manual Booking**
  - Select offer: "Graduate Scheme 2026"
  - Select time slot: "10:00-10:30"
  - Click "Reserve Appointment"
  - Shows: ✓ Reservation confirmed
  - Appears in "My Reservations" list

- **Option 2: Automatic Booking (Future Enhancement)**
  - Create Request: "Looking for graduate schemes in London"
  - Tags: graduate, tech
  - Max appointments: 3
  - System auto-generates bookings
  - Candidate accepts or rejects

#### **Section 5: MyReservations**
- Shows all candidate's bookings:
  - Offer: "Graduate Scheme 2026"
  - Recruiter: "John Smith"
  - Date/Time: "10:00-10:30"
  - Status: CONFIRMED
  - Buttons: "View Details", "Cancel Reservation"

#### **Section 6: Join VirtualRoom**
- When fair goes LIVE (using SystemTimer)
- Shows: "Join Meeting" button
- Candidate can:
  - Join before appointment time → waits in LOBBY
  - Join during appointment time → enters ACTIVE session
  - Join after appointment time → cannot join (meeting ended)
- Shows session with recruiter name
- "Leave Session" button

---

## ⏱️ PART D: SYSTEM TIMER (1-2 minutes)

### **Opening SystemTimer Window**
- Click "Timer Controls" button (appears in admin or main window)
- Shows current system time

### **Time Navigation Buttons**
- **"Next Day"** - Move 24 hours forward
- **"Next 5 Minutes"** - Move 5 minutes forward
- **"Go to BookingsOpen"** - Jump to bookings open time
- **"Go to FairLive"** - Jump to fair start time

### **Demo Sequence:**
```
1. Start at setup time → Admin configures everything
2. Click "Go to BookingsOpen" → Candidate window updates
   - Now shows "Bookings Now Open!" message
   - Appointment slots become available
   - "Make Reservation" button becomes enabled
3. Candidate makes reservations
4. Click "Go to FairLive" → System transitions to FairLive state
   - Recruiter sees "Fair is LIVE" indicator
   - Virtual rooms open
   - Candidates can join meetings
5. Recruiter joins virtual room
6. Candidate joins virtual room
7. Both see each other in session view
```

---

## 🎨 UI LAYOUT DETAILS

### **Main Menu Frame**
```
┌─────────────────────────────────────┐
│   VCFS - Virtual Career Fair System  │
│    Select Your Role to Login        │
├─────────────────────────────────────┤
│  [Administrator - Red] (500×80px)   │
│  "Access admin console..."          │
│                                     │
│  [Recruiter - Blue] (500×80px)      │
│  "Publish offers..."                │
│                                     │
│  [Candidate - Green] (500×80px)     │
│  "Search offers..."                 │
├─────────────────────────────────────┤
│         Version 1.0 - 2026          │
└─────────────────────────────────────┘
```

### **Admin Dashboard**
```
┌─────────────────────────────────────────────────────────┐
│        VCFS Administrator Dashboard                     │
├─────────────────────────────────────────────────────────┤
│ [Fair Timeline] [System Control] [View Audit Log]      │
├────────────────┬────────────────┬────────────────┐     │
│ Organizations  │ Booths         │ Recruiters     │     │
├────────────────┼────────────────┼────────────────┤     │
│ Tech Corp      │ Main Booth     │ John Smith     │     │
│ Finance Ltd    │ Booth 2        │ Jane Doe       │     │
│ [+ Add]        │ [+ Add]        │ [+ Add]        │     │
│                │                │                │     │
└────────────────┴────────────────┴────────────────┘     │
```

### **Recruiter Dashboard (CardLayout - 3 Views)**
```
┌──────────────────────────────────────┐
│  Recruiter: John Smith @Tech Corp    │
├──────────────────────────────────────┤
│ [Publish] [Schedule] [Virtual Room]  │ ← Tab buttons
├──────────────────────────────────────┤
│                                      │
│  VIEW 1: PUBLISH OFFER FORM         │
│  ┌────────────────────────────────┐ │
│  │ Offer Title: [________]        │ │
│  │ Duration (min): [30]           │ │
│  │ Capacity: [5]                  │ │
│  │ Tags: [________]               │ │
│  │ Description: [_____] ✓ Publish │ │
│  └────────────────────────────────┘ │
│                                      │
└──────────────────────────────────────┘
```

### **Candidate Dashboard (Tabbed)**
```
┌────────────────────────────────────────────────────────┐
│  Candidate: Alice Johnson                              │
├────────────────────────────────────────────────────────┤
│ [Browse] [My Reservations] [Notifications]             │
├────────────────────────────────────────────────────────┤
│ Organizations │ Booths      │ Offers                   │
│               │             │                          │
│ Tech Corp   → │ Main Booth→ │ Graduate Scheme 2026    │
│ Finance Ltd   │ Booth 2     │ 30 min, Capacity: 5     │
│               │             │                          │
│               │ Available Times:                       │
│               │ ☐ 10:00-10:30 ✓                       │
│               │ ☐ 10:30-11:00 ✓                       │
│               │ ☐ 11:00-11:30 ✓                       │
│               │ [Reserve Appointment]                 │
│               │                                       │
└────────────────────────────────────────────────────────┘
```

---

## ✅ DEMO CHECKLIST

### **Pre-Demo Verification**
- [ ] System compiles with zero errors
- [ ] All .class files present in bin/vcfs/
- [ ] MainMenuFrame launches on java -cp bin vcfs.Main
- [ ] All three role buttons are visible and color-coded

### **Admin Section**
- [ ] AdminLoginFrame appears on Admin button click
- [ ] Admin login successful with credentials
- [ ] Can create organization
- [ ] Can create booth under organization
- [ ] Can assign recruiter to booth
- [ ] Can set fair timeline
- [ ] SystemTimer button opens and shows current time

### **Recruiter Section**
- [ ] LoginFrame appears on Recruiter button click
- [ ] Login successful
- [ ] RecruiterScreen dashboard loads
- [ ] Can publish offers with proper validation
- [ ] Offers appear in schedule panel
- [ ] Can add availability blocks
- [ ] Appointment slots generated correctly
- [ ] VirtualRoomPanel shows lobby (waiting room)

### **Candidate Section**
- [ ] CandidateLoginFrame appears
- [ ] Can register with email and name
- [ ] CandidateScreen dashboard loads
- [ ] Can browse organizations
- [ ] Can browse booths
- [ ] Can view offers
- [ ] Can see available appointment slots
- [ ] Can make reservations
- [ ] Reservations appear in "My Reservations"
- [ ] Can join virtual room (after FairLive state)

### **System Integration**
- [ ] Time flows correctly (use SystemTimer)
- [ ] States transition: Dormant → Preparing → BookingsOpen → BookingsClosed → FairLive
- [ ] Candidates can only book during BookingsOpen state
- [ ] Candidates can only join meetings during FairLive state
- [ ] Attendance is recorded correctly

---

## 🚀 LAUNCH COMMANDS

### **Compile System**
```bash
cd "C:\Users\ZAID SIDDIQUI\OneDrive - University of Stirling\stir uni\SEMESTERS\sem6 2026\CSCU9P6\ASSIGNMENTS\Grp_9_CSCU9P6"
javac -d bin -sourcepath src/main/java src/main/java/vcfs/Main.java
```

### **Run System**
```bash
java -cp bin vcfs.Main
```

### **Expected Output**
- MainMenuFrame window appears (500×400px)
- Centered on screen
- Three colored buttons ready for interaction

---

## 🔧 TROUBLESHOOTING

### **Issue: "Class not found" errors**
**Solution:** Ensure all files are compiled to bin/ folder
```bash
javac -d bin -sourcepath src/main/java src/main/java/vcfs/**/*.java
```

### **Issue: GUI not appearing**
**Solution:** Ensure EDT (Event Dispatch Thread) is used. Check Main.java:
```java
SwingUtilities.invokeLater(() -> new MainMenuFrame());
```

### **Issue: Controllers not responding**
**Solution:** Verify controller classes have proper Logger and CareerFairSystem references

### **Issue: Time not advancing**
**Solution:** SystemTimer must directly call CareerFairSystem.setCurrentTime()

---

## 📝 KEY FEATURES TO DEMONSTRATE

✅ **MVC Architecture**
- Models: User, Offer, Reservation, MeetingSession classes
- Views: AdminScreen, RecruiterScreen, CandidateScreen
- Controllers: AdminScreenController, RecruiterController, CandidateController

✅ **State Management**
- Five system states managed by CareerFairSystem
- Smooth transitions: Dormant → Preparing → BookingsOpen → BookingsClosed → FairLive

✅ **Logging**
- All operations logged with Logger.log()
- Four levels: INFO, WARN, ERROR, DEBUG
- Timestamps and context shown

✅ **Data Flow**
- Admin creates organizations/booths/recruiters
- Recruiters publish offers and availability
- System auto-generates appointment slots
- Candidates browse and make reservations
- Virtual meetings conducted with attendance tracking

✅ **Input Validation**
- All text fields validated (non-empty, correct format)
- Inappropriate values rejected with error messages
- User sees clear feedback: ✓ Success or ✗ Error

✅ **Error Handling**
- Try-catch blocks throughout
- Graceful error messages to users
- Errors logged with context

---

## 🎯 EXPECTED DEMO DURATION

- **Total Demo Time:** 10-15 minutes
- Admin Setup: 3-5 minutes
- Recruiter Offering: 2-3 minutes
- Candidate Booking: 2-3 minutes
- System Timer & State Transitions: 2 minutes
- Q&A on Architecture & Code: 3-5 minutes

---

## 💡 PRO TIPS FOR SMOOTH DEMO

1. **Pre-setup data:** Have organizations/recruiters created before demo
2. **Time management:** Use SystemTimer to jump between states quickly
3. **Clear narration:** Explain what's happening at each step
4. **Show both sides:** Open candidate and recruiter windows side-by-side
5. **Mention validation:** Point out input validation when entering data
6. **Highlight logging:** Show console logging in background

---

**Last Updated:** April 7, 2026
**Status:** ✅ READY FOR DEMONSTRATION
**Compilation:** ✅ ZERO ERRORS
**Architecture:** ✅ MVC COMPLIANT
