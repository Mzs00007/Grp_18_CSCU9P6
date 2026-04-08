# 🎬 VCFS SCREENCAST PREPARATION & EXECUTION SCRIPT

**Purpose**: Complete 20-25 minute video demonstration of VCFS system  
**Grade Impact**: 20% of total grade  
**Time Required**: 3-4 hours (recording + editing)  
**Recording Date**: April 7-8, 2026  
**Video Output**: VCFS_Screencast.mp4 (< 500MB, .mp4 format)

---

## PRE-RECORDING CHECKLIST (30 min setup)

### Environment Setup
- [ ] Close all applications except terminal/IDE
- [ ] Set desktop background to plain/light color
- [ ] Increase terminal font size to readable (18-20pt)
- [ ] Arrange windows for clear visibility
- [ ] Test microphone/audio input
- [ ] Check available disk space (> 2GB free)
- [ ] Disable notifications (OS + Slack + email)
- [ ] Enable Do Not Disturb mode

### Software Setup
- [ ] Download & install OBS Studio (obs-project.com) OR
  - [ ] Use Windows Game Bar (Win+G) OR
  - [ ] Use built-in screen recording
- [ ] Set recording quality: 1080p or 720p (HD recommended)
- [ ] Set frame rate: 30 fps (balanced)
- [ ] Test recording 30 seconds (save test file)
- [ ] Verify audio levels (50-75% volume)

### System Verification (Do this RIGHT BEFORE recording)
```powershell
cd "c:\Users\ZAID SIDDIQUI\OneDrive - University of Stirling\stir uni\SEMESTERS\sem6 2026\CSCU9P6\ASSIGNMENTS\Grp_9_CSCU9P6"

# Verify compilation
$files = Get-ChildItem -Path src\main\java -Recurse -Filter "*.java"
javac -d bin -sourcepath src\main\java $files
if ($LASTEXITCODE -eq 0) { Write-Host "✅ System compiles cleanly" }

# Launch system (starts in background)
java -cp bin vcfs.App &

# Give it 3-5 seconds to fully load
Start-Sleep -Seconds 5
```

---

## SCREENCAST SCRIPT (20-25 minutes total)

### **SEGMENT 1: INTRODUCTION & SYSTEM OVERVIEW (1:00 - 1:30 min)**

**Audio Narration**:
> "Welcome to the Virtual Career Fair System (VCFS) demonstration. This is Group 9's implementation of a complete recruiting platform where administrators configure events, recruiters publish availability, and candidates book one-on-one meetings using automated tag-based matching. Over the next 20 minutes, I'll walk you through the complete workflow."

**On-Screen Actions**:
- [ ] Show desktop with terminal window open
- [ ] Display workspace directory structure (highlight src/main/java/vcfs/)
- [ ] Show compilation output: "40 .class files generated, 0 errors"
- [ ] Display system startup logs in terminal

**What to Show**:
```
C:\...\ASSIGNMENTS\Grp_9_CSCU9P6> java -cp bin vcfs.App
[SystemTimer] Initialised at: 2026-04-01 08:00
[2026-04-01 08:00:00] [INFO] [App.java:27] - Starting up Virtual Career Fair System...
[2026-04-01 08:00:00] [INFO] [App.java:31] - Initializing UI threads...
========================================
 Virtual Career Fair System (VCFS)
 Group 9 - CSCU9P6
 Project Manager: Zaid
========================================
```

---

### **SEGMENT 2: ADMIN CONFIGURATION (1:30 - 5:00 min, 3:30 duration)**

**Narrator**:
> "First, we have the Administrator. Admins configure the event by creating organizations, booths, and assigning recruiters. They also set critical timeline parameters."

#### 2A: Create Organization (1:00 min)
- [ ] Open AdminScreen window
- [ ] Navigate to "Organization" tab
- [ ] Click "Create Organization"
- [ ] Enter name: "Google"
- [ ] Enter description: "Technology recruiting"
- [ ] Click "Create"
- [ ] **Verify**: Organization appears in list
- [ ] Audio: "Created organization: Google"

**Expected Output**: Org appears in admin dashboard

#### 2B: Create Booth (1:00 min)
- [ ] Click "Create Booth"
- [ ] Select organization: "Google"
- [ ] Enter booth name: "Google Main Booth"
- [ ] Enter booth code: "GOOGLE-01"
- [ ] Click "Create"
- [ ] **Verify**: Booth appears under org
- [ ] Audio: "Created booth: Google Main Booth"

#### 2C: Assign Recruiter (1:00 min)
- [ ] Click "Assign Recruiter"
- [ ] Enter name: "Jane Smith"
- [ ] Enter email: "jane.smith@google.com"
- [ ] Select booth: "Google Main Booth"
- [ ] Click "Assign"
- [ ] **Verify**: Recruiter assigned to booth
- [ ] Audio: "Assigned recruiter Jane Smith to booth"

#### 2D: Set Timeline (1:30 min)
- [ ] Click "Timeline Configuration"
- [ ] Set times:
  - [ ] Bookings Open: 10:00 (April 8)
  - [ ] Bookings Close: 12:00 (April 8)
  - [ ] Fair Start: 13:00 (April 8)
  - [ ] Fair End: 17:00 (April 8)
- [ ] Click "Apply Timeline"
- [ ] Audio: "Set timeline: bookings 10-12, fair 13-17"
- [ ] **Verify**: Timeline stored, phase shows "READY"

---

### **SEGMENT 3: RECRUITER WORKFLOW (5:00 - 10:00 min, 5:00 duration)**

**Narrator**:
> "Next, the Recruiter publishes their availability. They specify a 3-hour block from 13:00 to 16:00, and the system automatically creates six 30-minute offer slots. This is VCFS-003 in action."

#### 3A: Open Recruiter Screen (0:30 min)
- [ ] Switch to RecruiterScreen window
- [ ] Recruiter automatically logged in: "Jane Smith"
- [ ] Display recruiter dashboard
- [ ] Audio: "Recruiter dashboard for Jane Smith"

#### 3B: Publish Offer Block (2:00 min)
- [ ] Click "Publish Availability"
- [ ] Enter offer title: "AI/ML Career Consultation"
- [ ] Enter start time: 13:00
- [ ] Enter end time: 16:00
- [ ] Enter duration: 30 minutes
- [ ] Enter tags: "AI,ML,Data"
- [ ] Click "Publish"
- [ ] **CRITICAL**: Count visible offer slots
- [ ] Audio: "Published 3-hour availability block. System generates 6 discrete 30-minute slots from 13:00 to 16:00. Each slot is tagged with AI, ML, Data."

**Expected Output in System Logs**:
```
[2026-04-08 08:00:00] [INFO] Recruiter Jane Smith published 6 offers
Slot 1: 13:00-13:30 (AI,ML,Data)
Slot 2: 13:30-14:00 (AI,ML,Data)
Slot 3: 14:00-14:30 (AI,ML,Data)
Slot 4: 14:30-15:00 (AI,ML,Data)
Slot 5: 15:00-15:30 (AI,ML,Data)
Slot 6: 15:30-16:00 (AI,ML,Data)
```

#### 3C: View Published Offers (1:00 min)
- [ ] Click "My Offers"
- [ ] Display list showing all 6 offers
- [ ] Scroll through to show all
- [ ] Highlight offer details (time, tags, capacity)
- [ ] Audio: "Here are all published offers, ready for candidates to book"

#### 3D: View Booking Status (1:30 min)
- [ ] Explain what happens next
- [ ] Audio: "As candidates book these slots, they will transition from AVAILABLE to BOOKED, and we'll see them populate in the Recruiter's calendar."

---

### **SEGMENT 4: SYSTEM TIME ADVANCEMENT (10:00 - 12:30 min, 2:30 duration)**

**Narrator**:
> "Now watch what happens as the system time advances through the phases. This demonstrates VCFS-002: Observer pattern + tick mechanism."

#### 4A: Advance to Bookings Open (1:00 min)
- [ ] Open SystemTimer controller (or show time in UI)
- [ ] Display current time: 08:00
- [ ] Advance time to: 10:00
- [ ] **Verify**: Phase changes to "BOOKINGS_OPEN"
- [ ] Audio: "Advancing system time to 10:00. The fair automatically transitions to BOOKINGS_OPEN phase."
- [ ] Show UI update reflecting new phase

#### 4B: Advance to Bookings Closed (1:00 min)
- [ ] Advance time to: 12:00
- [ ] **Verify**: Phase changes to "BOOKINGS_CLOSED"
- [ ] Audio: "Now 12:00. Bookings closed. No new reservations can be made."
- [ ] Show system logs: Phase transition

#### 4C: Advance to Fair Live (0:30 min)
- [ ] Advance time to: 13:00
- [ ] **Verify**: Phase changes to "FAIR_LIVE"
- [ ] Audio: "13:00. The fair is now LIVE. Meetings begin."
- [ ] Show UI updates

---

### **SEGMENT 5: CANDIDATE WORKFLOW (12:30 - 18:00 min, 5:30 duration)**

**Narrator**:
> "Now let's switch to a Candidate's experience. The candidate wants to meet with a recruiter interested in AI and ML. They submit a booking request."

#### 5A: Open Candidate Screen (0:30 min)
- [ ] Switch to CandidateScreen window (or login as candidate)
- [ ] Candidate name: "John Developer"
- [ ] Display candidate dashboard
- [ ] Audio: "John Developer, a candidate, logs in to search for recruiter meetings"

#### 5B: Browse Available Offers (1:00 min) 
- [ ] Navigate to "Browse Offers"
- [ ] Show filter/search: "AI,ML"
- [ ] Display matching offers
- [ ] Show Jane Smith's 6 offers
- [ ] Audio: "John searches for AI and ML meetings. He sees Jane Smith's offers, each tagged with AI, ML, and Data."

#### 5C: Submit Booking Request (1:30 min - **CRITICAL MOMENT**)
- [ ] Select one of Jane's offers (e.g., first 30-min slot, 13:00-13:30)
- [ ] Click "Request Meeting"
- [ ] System triggers **VCFS-004 Auto-Booking Algorithm**:
  - John's desired tags: AI, ML, Python
  - Offer tags: AI, ML, Data
  - Tag intersection score: 2 (AI, ML match)
  - Check collision: No conflict with John's other meetings
  - Result: MATCH FOUND
- [ ] Audio: "John submits a booking request with tags AI, ML, Python. The system runs tag-weighted matching..."
- [ ] **Wait 2-3 seconds** to show algorithm processing
- [ ] Audio: "...and automatically finds Jane's AI/ML offer. Tag intersection: 2 matches. No time conflicts. BOOKING CONFIRMED!"
- [ ] Show confirmation popup: "Booking successful for 13:00-13:30"

#### 5D: View Booking Confirmation (1:00 min)
- [ ] Show booking in John's "My Schedule"
- [ ] Display details:
  - Recruiter: Jane Smith
  - Time: 13:00-13:30
  - Date: April 8, 2026
  - Topic: AI/ML Career Consultation
  - Status: CONFIRMED
- [ ] Audio: "John's booking is confirmed. He now has the meeting in his calendar."

#### 5E: Verify Recruiter Side (1:00 min)
- [ ] Switch back to RecruiterScreen (Jane Smith's view)
- [ ] Show that the booked slot now shows "BOOKED"
- [ ] Display "John Developer - 13:00-13:30"
- [ ] Audio: "On Jane's side, she can see that this slot is now booked with John Developer."

---

### **SEGMENT 6: MEETING EXECUTION (18:00 - 22:00 min, 4:00 duration)**

**Narrator**:
> "Now the meeting time arrives. Let's see how the meeting progresses through its lifecycle."

#### 6A: Meeting Starts (1:00 min)
- [ ] Advance system time to 13:00 (exact meeting start)
- [ ] **Verify**: MeetingSession state changes to "IN_PROGRESS"
- [ ] Show virtual room opening
- [ ] Display lobby with waiting area
- [ ] Participants: Jane (Recruiter), John (Candidate)
- [ ] Audio: "It's now 13:00. The meeting starts. Both Jane and John join the virtual room."

#### 6B: Meeting in Progress (1:30 min)
- [ ] Show participants in virtual room
- [ ] Display timer: "Time remaining: 30 minutes"
- [ ] Show chat/collaboration tools (if implemented)
- [ ] Display meeting status: "IN_PROGRESS"
- [ ] Audio: "Participants are in the virtual meeting room. This 30-minute session is where they discuss the AI/ML career opportunity."
- [ ] Show real-time attendance tracking
- [ ] Display: "Jane: online, John: online"

#### 6C: Attendance Recording (1:00 min)
- [ ] Show system recording attendance:
  - [ ] Jane joined at 13:00
  - [ ] John joined at 13:01
  - [ ] Both present for duration
- [ ] Display AttendanceRecord in system logs
- [ ] Audio: "The system records who attended and when they joined. This data is important for fair metrics."

#### 6D: Meeting Ends (0:30 min)
- [ ] Advance time to 13:30 (meeting end time)
- [ ] **Verify**: MeetingSession state changes to "ENDED"
- [ ] Show final attendance summary
- [ ] Display: "Meeting Duration: 30 minutes, Attendance: 100%"
- [ ] Audio: "The meeting ends. Both participants attended for the full 30 minutes."

---

### **SEGMENT 7: POST-MEETING SUMMARY (22:00 - 25:00 min, 3:00 duration)**

**Narrator**:
> "Let's see what data the system has collected and how the full workflow integrates."

#### 7A: Reservation Summary (1:00 min)
- [ ] Show completed reservation record
- [ ] Display all fields:
  - [ ] Candidate: John Developer
  - [ ] Recruiter: Jane Smith (via Offer)
  - [ ] Time: 13:00-13:30
  - [ ] Status: ENDED
  - [ ] Tags matched: AI, ML
- [ ] Audio: "Here's the complete reservation record showing the match was successful."

#### 7B: Recruiter Reporting (1:00 min)
- [ ] Switch to Jane's dashboard
- [ ] Show "Meeting History"
- [ ] Display: "1 meeting completed today"
- [ ] Show John's attendance record
- [ ] Duration: 30 minutes
- [ ] Audio: "From Jane's perspective, she can see her meeting history and attendance records for quality assurance."

#### 7C: System Architecture Overview (1:00 min)
- [ ] Display system diagram (or describe):
  - [ ] Observer Pattern: SystemTimer → CareerFairSystem
  - [ ] Singleton: Only one CareerFairSystem instance
  - [ ] State Machine: Fair phases DORMANT → BOOKINGS_OPEN → FAIR_LIVE
  - [ ] Auto-Booking: Tag-weighted matching + collision detection
- [ ] Audio: "The system is built on solid architecture: Singleton factory, Observer pattern for time-driven events, state machines for phase management, and intelligent matching algorithms for booking."
- [ ] Show code snippet (if possible):
  ```java
  autoBook(candidate, request) {
    best_offer = findBestMatch(candidate.desired_tags);
    if (no_collision(candidate, best_offer)) {
      create_reservation(candidate, best_offer);
      return CONFIRMED;
    }
  }
  ```

---

## POST-RECORDING CHECKLIST (15 min)

- [ ] Stop recording
- [ ] Save video file: `VCFS_Screencast.mp4`
- [ ] Test video playback (verify audio + video sync)
- [ ] Check file size (target: < 500MB)
- [ ] Verify duration: 20-25 minutes
- [ ] Export as MP4 with quality settings:
  - [ ] Resolution: 1080p or 720p
  - [ ] Bitrate: 5-8 Mbps
  - [ ] Audio: 128 kbps
- [ ] Save to project root: `VCFS_Screencast.mp4`

---

## AUDIO SCRIPT SUMMARY (For Reference)

| Segment | Script | Duration |
|---------|--------|----------|
| 1. Intro | "Welcome to VCFS. Demonstrating complete recruiting workflow..." | 1:30 |
| 2a. Admin - Org | "Created organization: Google" | 1:00 |
| 2b. Admin - Booth | "Created booth: Google Main Booth" | 1:00 |
| 2c. Admin - Recruiter | "Assigned recruiter Jane Smith" | 1:00 |
| 2d. Admin - Timeline | "Set timeline: bookings 10-12, fair 13-17" | 1:30 |
| 3a. Recruiter - Open | "Recruiter dashboard for Jane Smith" | 0:30 |
| 3b. Recruiter - Publish | "Published 6 discrete 30-min slots, VCFS-003 algorithm" | 2:00 |
| 3c. Recruiter - View | "All 6 offers ready for candidates" | 1:00 |
| 3d. Recruiter - Status | "As candidates book, offers transition to BOOKED" | 1:30 |
| 4a. Time - BookingsOpen | "Phase transitions to BOOKINGS_OPEN, VCFS-002 Observer pattern" | 1:00 |
| 4b. Time - BookingsClosed | "Phase transitions to BOOKINGS_CLOSED" | 1:00 |
| 4c. Time - FairLive | "Phase transitions to FAIR_LIVE" | 0:30 |
| 5a. Candidate - Open | "John Developer logs in to search for meetings" | 0:30 |
| 5b. Candidate - Browse | "Searches for AI/ML meetings" | 1:00 |
| 5c. Candidate - request | "VCFS-004 Auto-booking triggered. Tag matching LIVE!" | 1:30 |
| 5d. Candidate - Confirm | "Booking confirmed, added to calendar" | 1:00 |
| 5e. Candidate - Verify | "Jane sees the booked slot on her side" | 1:00 |
| 6a. Meeting - Start | "Meeting starts at 13:00" | 1:00 |
| 6b. Meeting - Progress | "Meeting in progress, participants online" | 1:30 |
| 6c. Meeting - Attendance | "System records attendance and duration" | 1:00 |
| 6d. Meeting - End | "Meeting ended, 30 minutes completed" | 0:30 |
| 7a. Summary - Reservation | "Complete reservation record showing successful match" | 1:00 |
| 7b. Summary - Reporting | "Recruiter can view history and attendance" | 1:00 |
| 7c. Summary - Architecture | "System architecture: Observer, Singleton, State Machine, Auto-booking" | 1:00 |
| **TOTAL** | | **25:00** |

---

## TROUBLESHOOTING DURING RECORDING

| Issue | Solution |
|-------|----------|
| System crashes | Pre-compile and test. Keep backup of code. Restart and resume recording. |
| UI not visible | Increase window size. Use F11 for fullscreen. Adjust zoom. |
| Offers don't appear | Verify recruiter is logged in. Check system time is in BOOKINGS_OPEN phase. |
| Audio too quiet/loud | Adjust microphone gain before next segment. |
| Time won't advance | Verify SystemTimer is running (check logs). Reload system. |
| Video corruption | Record in shorter segments. Save intermediate versions. |
| File too large | Reduce bitrate (use 4 Mbps instead of 8). Export at 720p instead of 1080p. |

---

## SUBMISSION VERIFICATION

Before uploading, confirm:
- [ ] File named: `VCFS_Screencast.mp4`
- [ ] Location: Project root directory
- [ ] Size: < 500MB
- [ ] Duration: 20-25 minutes
- [ ] Format: MP4 (H.264 codec)
- [ ] Audio: Clear and synchronized
- [ ] No debugging/errors visible
- [ ] All 7 segments complete
- [ ] All 4 VCFS specs demonstrated (001, 002, 003, 004)

---

**⏱️ TIMELINE**: Record this immediately - it's the most time-sensitive deliverable!

**✅ GRADE IMPACT**: 20% of total grade depends on this video!

