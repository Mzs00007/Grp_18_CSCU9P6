═══════════════════════════════════════════════════════════════════════════════
    VCFS DEMO - DETAILED STEP-BY-STEP INSTRUCTION MANUAL
    For Presenter (Friend) - Every click, every pause, every word
═══════════════════════════════════════════════════════════════════════════════

READ THIS LINE BY LINE WHILE RECORDING

═══════════════════════════════════════════════════════════════════════════════
SEGMENT 1: INTRODUCTION (0:00 - 0:45)
═══════════════════════════════════════════════════════════════════════════════

[0:00] START RECORDING

[0:01] OPEN FILE EXPLORER
   • Windows key + E
   • Or click File Explorer icon on taskbar
   
[0:03] NAVIGATE TO PROJECT FOLDER
   • Path: C:\Users\ZAID SIDDIQUI\OneDrive - University of Stirling\...
   • Or simpler: Type in address bar:
     C:\...\Grp_18_CSCU9P6_code
   • Press Enter
   • You should see:
     - CSCU9P6_GROUP_18_CODE folder
     - Grp_9_CSCU9P6.iml file
     - README.md file

[0:05] DOUBLE-CLICK: CSCU9P6_GROUP_18_CODE
   Open the code folder
   
[0:07] YOU NOW SEE:
   • src/ folder
   • bin/ folder
   • lib/ folder
   • BUILD_AND_RUN_DEMO.bat
   • README.md

[0:08] NARRATION (READ SLOWLY - 15 seconds):
   "Welcome to the Virtual Career Fair System, or VCFS. This is a complete 
   Java application that automates virtual recruiting events. Over the next 
   20 minutes, we'll see how three independent user interfaces - for 
   Administrators, Recruiters, and Candidates - are all powered by a single, 
   robust data model. This is the Model-View-Controller architectural pattern 
   in action."

[0:23] RIGHT-CLICK: Empty space in folder
   Select: "Open Terminal Here" or "Open Command Prompt Here"

[0:27] TERMINAL APPEARS
   Current directory: ...Grp_18_CSCU9P6_code\CSCU9P6_GROUP_18_CODE

[0:28] TYPE COMMAND:
   BUILD_AND_RUN_DEMO.bat
   
[0:30] PRESS: Enter
   System starts loading...
   
[0:31] WAIT: 3-5 seconds for startup
   You should see output like:
   [INFO] VCFS Initializing...
   [INFO] Loading configurations...
   [INFO] Starting user interfaces...

[0:35] FIRST WINDOW APPEARS: ADMIN LOGIN
   Second window appears: RECRUITER LOGIN
   Third window appears: CANDIDATE LOGIN

[0:38] NARRATION (READ SLOWLY - 7 seconds):
   "Notice: three completely separate windows, each with their own login.
   They're independent portals but they share the same backend system.
   Let's start with the Administrator portal."

[0:45] STOP NARRATION
   Position: Admin window is in focus (bring to front if needed)
   
SEGMENT 1 COMPLETE - Proceed to Segment 2 immediately

═══════════════════════════════════════════════════════════════════════════════
SEGMENT 2: ADMIN PORTAL (0:45 - 4:00)
═══════════════════════════════════════════════════════════════════════════════

[0:45] CLICK ON: Admin Login Window
   Make sure it's in front

[0:47] YOU SEE: Admin login form with fields
   Username: [_________]
   Password: [_________]
   [Login Button]

[0:48] CLICK in Username field
   Type: Admin
   (exactly "Admin" - case sensitive)

[0:50] CLICK in Password field
   Type: Admin
   (exactly "Admin")

[0:52] CLICK: [Login] button

[0:55] WAIT: 2-3 seconds
   Admin Dashboard loads
   Shows:
   - Admin Control Panel (title)
   - Organizations section
   - Booths section
   - Timeline configuration
   - Status: "Fair Status: SETUP"

[1:00] NARRATION (10 seconds):
   "The admin sees the control center. From here, they orchestrate the 
   entire event. First step: create the organizations that will be recruiting."

[1:10] LOOK FOR: "Create Organization" or "Add Organization" button
   Click it

[1:12] FORM APPEARS:
   Organization Name: [_________]
   Description: [_________________]
   [Create Button]

[1:13] CREATE ORGANIZATION #1: Google
   CLICK in Organization Name field
   Type: Google
   
   CLICK in Description field
   Type: Tech Innovation Leader
   
   CLICK: [Create] button

[1:18] WAIT: 1 second
   Success message appears
   Organization list now shows:
   • Google - Tech Innovation Leader

[1:20] NARRATION (5 seconds):
   "First organization added: Google. The system stores this in the MODEL.
   Now let's add two more."

[1:25] CLICK: "Add Organization" button again (or "Create New Org")

[1:27] CREATE ORGANIZATION #2: Apple
   CLICK in Organization Name field
   Type: Apple
   
   CLICK in Description field
   Type: Innovation in Hardware & Software
   
   CLICK: [Create] button

[1:33] WAIT: 1 second reaction
   Organization list now shows:
   • Google - Tech Innovation Leader
   • Apple - Innovation in Hardware & Software

[1:35] CLICK: "Add Organization" button again

[1:37] CREATE ORGANIZATION #3: Microsoft
   CLICK in Organization Name field
   Type: Microsoft
   
   CLICK in Description field
   Type: Enterprise Solutions Provider
   
   CLICK: [Create] button

[1:43] WAIT: 2 seconds
   Final list shows all 3:
   • Google - Tech Innovation Leader
   • Apple - Innovation in Hardware & Software
   • Microsoft - Enterprise Solutions Provider

[1:45] NARRATION (8 seconds):
   "Perfect. Three organizations set up. Now I need to create booths - 
   virtual meeting spaces - for each organization. One booth per org."

[1:53] CLICK ON: Google organization row
   (Click the "Google" line in the list)
   
   Or look for: "+" button or "Add Booth" button next to Google

[1:55] BOOTH CREATION FORM APPEARS:
   Booth Name: [______________]
   Booth Code: [______________]
   [Create Booth Button]

[1:57] CREATE BOOTH #1: Google Main Booth
   CLICK in Booth Name field
   Type: Google Main Booth
   
   CLICK in Booth Code field
   Type: GOOGLE-01
   
   CLICK: [Create Booth] button

[2:03] WAIT: 1 second
   Hierarchical view updates:
   • Google - Tech Innovation Leader
     └─ Google Main Booth (GOOGLE-01)

[2:05] CLICK ON: Apple organization row
   Find "Add Booth" for Apple

[2:07] CREATE BOOTH #2: Apple Careers Booth
   CLICK in Booth Name field
   Type: Apple Careers Booth
   
   CLICK in Booth Code field
   Type: APPLE-01
   
   CLICK: [Create Booth] button

[2:13] WAIT: 1 second
   Hierarchical view updates:
   • Google - Tech Innovation Leader
     └─ Google Main Booth (GOOGLE-01)
   • Apple - Innovation in Hardware & Software
     └─ Apple Careers Booth (APPLE-01)

[2:15] CLICK ON: Microsoft organization row
   Find "Add Booth" for Microsoft

[2:17] CREATE BOOTH #3: Microsoft Internship Hub
   CLICK in Booth Name field
   Type: Microsoft Internship Hub
   
   CLICK in Booth Code field
   Type: MSFt-01
   
   CLICK: [Create Booth] button

[2:23] WAIT: 2 seconds
   Final hierarchical structure:
   • Google - Tech Innovation Leader
     └─ Google Main Booth (GOOGLE-01)
   • Apple - Innovation in Hardware & Software
     └─ Apple Careers Booth (APPLE-01)
   • Microsoft - Enterprise Solutions Provider
     └─ Microsoft Internship Hub (MSFt-01)

[2:25] PAUSE: 3 seconds
   Let audience see the complete structure

[2:28] NARRATION (10 seconds):
   "The event structure is now set: three organizations, each with their 
   own booth. This hierarchical organization is managed by the MODEL layer. 
   The VIEW simply displays it cleanly. The CONTROLLER coordinated the 
   admin's actions with the model.
   
   Now let's move to the Recruiter portal, where they'll publish their 
   availability."

[2:38] MINIMIZE or MOVE ADMIN WINDOW aside
   Bring Recruiter window to front

[2:40] TIME CHECK: Should be between 3:50 - 4:00
   (Allow 20-40 seconds buffer)
   If ahead: Show the admin panel 10 more seconds
   If behind: Move forward to Recruiter now

SEGMENT 2 COMPLETE - Proceed to Segment 3

═══════════════════════════════════════════════════════════════════════════════
SEGMENT 3: RECRUITER PORTAL (4:00 - 9:00)
═══════════════════════════════════════════════════════════════════════════════

[4:00] RECRUITER WINDOW NOW IN FOCUS
   
   You see: Recruiter Login form
   Username: [_________]
   Password: [_________]
   [Login Button]

[4:02] CLICK in Username field
   Type: recruiter1

[4:04] CLICK in Password field
   Type: password123

[4:06] CLICK: [Login] button

[4:09] WAIT: 2-3 seconds
   Recruiter Dashboard loads
   Shows:
   - "Welcome [Recruiter Name]"
   - Assigned Booth: "Google Main Booth"
   - "Publish Availability" button
   - "View Offers" section
   - "My Bookings" section

[4:15] NARRATION (15 seconds):
   "The recruiter sees their portal. It's completely different from the 
   admin interface - same system backend, but a different VIEW tailored 
   to the recruiter's needs. They can only see what's relevant to them: 
   their assigned booth and their candidates.
   
   The real magic happens next. Watch what happens when they publish 
   availability."

[4:30] CLICK: "Publish Availability" button

[4:33] WINDOW OPENS: Availability Publication Form
   Title: [_____________________]
   Start Time: [Select/Dropdown]
   End Time: [Select/Dropdown]
   Slot Duration: [Radio buttons: 15min/30min/60min]
   Tags/Skills: [Checkboxes: AI, ML, Python, Data, Java, SQL, etc.]
   [Publish Button]

[4:38] FILL IN OFFER #1
   CLICK in Title field
   Type: Senior ML Engineer - AI Research
   
   CLICK Start Time dropdown
   Select: 13:00 (or 1:00 PM)
   
   CLICK End Time dropdown
   Select: 15:00 (or 3:00 PM)
   
   SELECT Slot Duration:
   Click: [30 minutes] radio button
   
   CHECK Tags:
   ☑ AI
   ☑ Machine Learning (or ML)
   ☑ Python
   ☑ Research

[4:55] CLICK: [Publish] button

[4:58] SYSTEM PROCESSES - Wait for response
   Message appears: "Successfully published 4 availability slots"
   OR slots become visible:
   Slot 1: 13:00-13:30 (AI, ML, Python, Research)
   Slot 2: 13:30-14:00 (AI, ML, Python, Research)
   Slot 3: 14:00-14:30 (AI, ML, Python, Research)
   Slot 4: 14:30-15:00 (AI, ML, Python, Research)

[5:05] NARRATION (15 seconds - THIS IS KEY):
   "This is crucial. Notice: The recruiter published ONE availability block
   from 1 to 3 PM, divided into 30-minute slots. The system AUTOMATICALLY 
   created 4 individual offers.
   
   Why? Because the MODEL applied business logic: 
   (15:00 - 13:00) ÷ 30 minutes = 4 slots.
   
   This is algorithmic slot generation - a core VCFS feature. No manual 
   clicking 4 times. The CONTROLLER received the block, the MODEL generated 
   slots, the VIEW displays them. This is MVC excellence."

[5:20] WAIT: 3 seconds (let audience absorb)

[5:23] CLICK: "Publish Availability" button again

[5:25] FILL IN OFFER #2
   CLICK in Title field
   Type: Data Engineer - Backend Systems
   
   CLICK Start Time dropdown
   Select: 15:15 (or 3:15 PM)
   
   CLICK End Time dropdown
   Select: 16:45 (or 4:45 PM)
   
   SELECT Slot Duration:
   Click: [30 minutes] radio button
   
   CHECK Tags:
   ☑ Data
   ☑ Java
   ☑ SQL
   ☑ Backend

[5:40] CLICK: [Publish] button

[5:43] SYSTEM PROCESSES
   Message appears: "Successfully published 3 availability slots"
   OR slots become visible:
   Slot 5: 15:15-15:45 (Data, Java, SQL, Backend)
   Slot 6: 15:45-16:15 (Data, Java, SQL, Backend)
   Slot 7: 16:15-16:45 (Data, Java, SQL, Backend)

[5:50] NARRATION (8 seconds):
   "Second block published. Again, 1 hour 30 minutes divided into 30-min 
   slots equals 3 new offers. Now the recruiter has 7 time slots ready 
   for candidates to book."

[5:58] SCROLL or VIEW: Complete offer list
   All 7 slots should be visible:
   Group 1 - AI/ML:
   ✓ 13:00-13:30 1 opening
   ✓ 13:30-14:00 1 opening
   ✓ 14:00-14:30 1 opening
   ✓ 14:30-15:00 1 opening
   Group 2 - Data:
   ✓ 15:15-15:45 1 opening
   ✓ 15:45-16:15 1 opening
   ✓ 16:15-16:45 1 opening

[6:08] NARRATION (8 seconds):
   "Seven offers published. Each with its own time and tags. Candidates 
   will later search by these tags. The MODEL stores this data. The 
   RECRUITER sees an organized view. When a candidate books a slot, 
   this count drops to 0 and the slot disappears from other candidates' 
   search results. Real-time MODEL updates reflected across all VIEWS."

[6:16] CLICK: "View Bookings" or "My Reservations" tab

[6:19] BOOKINGS LIST APPEARS
   Expected states:
   - Empty (if no candidates booked yet)
   - Or pre-populated with demo bookings:
   
   ✓ Alice Johnson | 13:00-13:30 | Status: CONFIRMED
   ✓ Bob Smith | 13:30-14:00 | Status: CONFIRMED
   ✓ Carol Davis | 14:00-14:30 | Status: CONFIRMED

[6:25] NARRATION (10 seconds):
   "Bookings appear here in real-time as candidates make reservations. 
   Each booking has a state: CONFIRMED, RECRUITER_READY, SESSION_STARTED, 
   COMPLETED. The MODEL enforces valid state transitions. For example, 
   a recruiter can't skip SESSION_STARTED and go straight to COMPLETED."

[6:35] OPTIONAL - CLICK on one booking to show details
   Example: Click "Alice Johnson"
   
   Details appear:
   Name: Alice Johnson
   Email: alice@example.com
   Slot: 13:00-13:30
   Status: CONFIRMED
   Booked: 2026-04-08 12:45:30
   Actions: [MARK READY] [START SESSION] [CANCEL]

[6:42] NARRATION (8 seconds):
   "Full booking details shown. The recruiter can see exactly who's coming, 
   when, and what their status is. They can mark themselves ready for the 
   interview, start the session, or cancel if needed."

[6:50] OPTIONAL - CLICK: [MARK_READY] button
   Status updates: "RECRUITER_READY"
   
   NARRATION (5 seconds):
   "Status updated. The candidate will also see this change in their 
   interface. Same MODEL, synchronized across both VIEWS."

[6:55] CLOSE booking details
   Return to bookings list

[6:57] TIME CHECK: Should be around 7:00-7:15
   (Allow 10-15 second buffer)
   If behind: Skip to Candidate portal now
   If ahead: Show recruiter details 10 more seconds

SEGMENT 3 COMPLETE - Proceed to Segment 4

═══════════════════════════════════════════════════════════════════════════════
SEGMENT 4: CANDIDATE PORTAL (7:00 - 16:00) - LONGEST SEGMENT
═══════════════════════════════════════════════════════════════════════════════

[7:00] CANDIDATE WINDOW NOW IN FOCUS
   
   You see: Candidate Login form
   Username: [_________]
   Password: [_________]
   [Login Button]

[7:02] CLICK in Username field
   Type: candidate1

[7:04] CLICK in Password field
   Type: password123

[7:06] CLICK: [Login] button

[7:09] WAIT: 2-3 seconds
   Candidate Dashboard loads
   Shows:
   - "Job Opportunities" tab (active)
   - Search bar
   - Filter options (Skills, Organization, Time)
   - Job listing area (showing available offers)
   - "My Applications" tab
   - "My Bookings" tab

[7:15] NARRATION (12 seconds):
   "The candidate sees the job search interface. This is their VIEW of 
   the exact same offer data that the recruiter published. But instead of 
   'manage these offers,' the candidate sees 'search and book these 
   opportunities.' MVC allows the same MODEL to be presented through 
   completely different user interfaces."

[7:27] DEMONSTRATE SEARCH #1 - By Skill
   LOOK FOR: Filter checkbox section
   Find: [Python] checkbox
   CLICK: [Python] checkbox
   
   List filters instantly
   Now shows only: 4 AI/ML slots (all tagged Python)

[7:35] NARRATION (8 seconds):
   "Search by skill: Python. The MODEL filters offers matching the Python 
   tag. The VIEW instantly updates. This filtering logic is in the CONTROLLER 
   and MODEL, not hard-coded in the UI."

[7:43] RESET filter
   CLICK: [Python] checkbox again to uncheck
   OR CLICK: "Clear filters" button
   
   All 7 offers reappear

[7:48] DEMONSTRATE SEARCH #2 - By Organization
   FIND: Organization dropdown
   SELECT: "Google"
   
   List shows: All 7 Google offers
   (Both AI/ML and Data Engineering slots)

[7:55] NARRATION (8 seconds):
   "Search by organization: Google. Now showing all opportunities from 
   Google. The algorithm is intelligent - multiple filter layers, real-time 
   results. This is professional search functionality."

[8:03] DEMONSTRATE SEARCH #3 - By Time Range
   LOOK FOR: Time filter (calendar or time range selector)
   SELECT: "After 3:00 PM" or "After 15:00"
   
   List shows: Only 3 Data Engineering slots
   ✓ 15:15-15:45
   ✓ 15:45-16:15
   ✓ 16:15-16:45

[8:12] NARRATION (8 seconds):
   "Time-based filtering. Candidate searches for afternoon slots only. 
   The MODEL applies time comparison logic. The candidate gets relevant 
   results instantly. This is exactly how LinkedIn, Indeed, and Google 
   do job searching."

[8:20] CLEAR FILTERS
   Reset to show all 7 offers again

[8:23] CLICK ON FIRST OFFER TO VIEW DETAILS
   Recommend: "Senior ML Engineer - AI Research | 13:00-13:30"
   
   CLICK on that offer in the list

[8:27] OFFER DETAILS PAGE APPEARS:
   Shows full information:
   Position: Senior ML Engineer - AI Research
   Organization: Google
   Recruiter: Jane Smith
   Time: 13:00-13:30 (30 minutes)
   Location: Google Main Booth
   Skills Required: AI, ML, Python, Research
   Description: [Offer text]
   Availability: 1 opening (or "1 spot available")
   Buttons: [BOOK THIS SLOT] [SAVE FOR LATER] [APPLY]

[8:40] NARRATION (10 seconds):
   "Offer details. Candidate sees exactly what they're applying for: 
   position, recruiter, time, required skills. The details are pulled 
   from the MODEL and formatted in the VIEW."

[8:50] CLICK: [BOOK THIS SLOT] button

[8:53] CONFIRMATION DIALOG APPEARS:
   Confirm: Senior ML Engineer - AI Research
   Monday, 04 Apr 2026, 13:00-13:30
   Google Main Booth
   Recruiter: Jane Smith
   
   Message: "After confirming, you will receive booking confirmation 
   and meeting link"
   
   [CONFIRM BOOKING] [CANCEL]

[9:05] NARRATION (8 seconds):
   "Confirmation preview. Good UX - no surprises. The candidate reviews 
   the exact time and recruiter before committing."

[9:13] CLICK: [CONFIRM BOOKING]

[9:16] SUCCESS MESSAGE APPEARS:
   ✓ "Booking Confirmed!"
   ✓ "Confirmation #: VCFS-20260408-001"
   ✓ "Email sent to: candidate1@university.edu"
   ✓ "Meeting link will be sent 30 minutes before your slot"

[9:25] NARRATION (10 seconds):
   "Booking confirmed. Behind the scenes: the MODEL created a Booking 
   object, set state to CONFIRMED, assigned a unique ID, decremented the 
   offer's availability from 1 to 0, triggered an email notification, 
   and updated both the recruiter and candidate VIEWS."

[9:35] AUTO-REDIRECT or CLICK: "My Bookings" tab

[9:38] BOOKINGS PAGE APPEARS:
   Shows: 1 booking
   ✓ Confirmation #VCFS-20260408-001
   Position: Senior ML Engineer - AI Research
   Organization: Google
   Time: Monday 13:00-13:30
   Recruiter: Jane Smith
   Status: CONFIRMED
   Actions: [VIEW DETAILS] [CANCEL] [GET MEETING LINK]

[9:50] NARRATION (10 seconds):
   "The booking is now in the candidate's list. If we switch to the 
   recruiter view, they'd also see Alice booked in their 13:00 slot. 
   The MODEL is the single source of truth. All VIEWS stay synchronized."

[10:00] CLICK: "Job Opportunities" tab
   Return to job search

[10:03] SCROLL through available jobs
   Notice: The 13:00-13:30 slot might now show 0 openings (because Alice 
   just booked it) OR it might still show as "FULLY BOOKED"

[10:08] NARRATION (8 seconds):
   "Notice the availability updated instantly. The slot Alice just booked 
   now shows 0 openings. This is MODEL layer efficiency - the system 
   prevents double bookings by design."

[10:16] BOOK SECOND OFFER
   Recommendation: Choose a different time slot
   Example: "Data Engineer - Backend Systems | 15:15-15:45"
   
   CLICK on offer

[10:20] OFFER DETAILS appear

[10:22] CLICK: [BOOK THIS SLOT]

[10:25] CONFIRMATION appears
   Confirm details

[10:27] CLICK: [CONFIRM BOOKING]

[10:30] SUCCESS MESSAGE
   New confirmation: "VCFS-20260408-002"

[10:33] NARRATION (8 seconds):
   "Second booking confirmed. Candidate now has two interviews scheduled 
   at different times. The MODEL prevents conflicts."

[10:41] CLICK: "My Bookings" tab

[10:44] BOOKINGS PAGE SHOWS TWO:
   ✓ VCFS-20260408-001: Senior ML (13:00-13:30) CONFIRMED
   ✓ VCFS-20260408-002: Data Engineer (15:15-15:45) CONFIRMED

[10:53] NARRATION (8 seconds):
   "Two bookings, no time conflicts. The candidate is ready for their 
   interviews. The recruiter can see both candidates on their calendar."

[11:01] OPTIONAL - CANCEL A BOOKING (Show state management):
   CLICK on first booking action: [CANCEL BOOKING]

[11:04] CONFIRMATION appears:
   "Are you sure? This action cannot be undone."
   [CANCEL] [PROCEED]

[11:06] CLICK: [PROCEED]

[11:09] SUCCESS MESSAGE
   "Booking cancelled"

[11:10] BOOKINGS PAGE updates:
   Now shows only 1 booking
   (The first one is gone)

[11:13] NARRATION (8 seconds):
   "Booking cancelled. The slot is now available again for other candidates. 
   The recruiter will also see their 13:00-13:30 slot open up again. 
   MODEL updates propagate across all VIEWS instantly."

[11:21] TIME CHECK: Should be around 11:00-12:00
   (Allow 10-20 second buffer)

SEGMENT 4 COMPLETE if on time - Proceed to Segment 5

═══════════════════════════════════════════════════════════════════════════════
SEGMENT 5: ERROR HANDLING & ROBUSTNESS (11:00 - 16:30 or remaining time)
═════════════════════════════════════════════════════════════════════════════════

[11:21] DEMONSTRATE ERROR #1 - Prevent Double Booking

   OPTION A (Two candidate windows):
   → Window 1: Candidate A already booked 14:00-14:30
   → Window 2: Show Candidate B attempting same slot
   
   OPTION B (Single window, pre-recorded):
   → Show screenshot or recorded footage of error message

   WHAT SHOULD HAPPEN:
   When Candidate B tries to book the filled slot:
   Message appears: "This slot has no openings"
   Or: "Cannot book - SLOT FULLY BOOKED"
   Button: [BOOK THIS SLOT] is DISABLED

[11:33] NARRATION (8 seconds):
   "The system prevents overbooking. This is core business logic in the 
   MODEL layer, not a UI suggestion. VCFS guarantees no double bookings."

[11:41] MOVE TO RECRUITER WINDOW

[11:44] DEMONSTRATE ERROR #2 - Invalid State Transitions
   FIND: A booking with Status: SESSION_STARTED
   (Or find one with Status: CONFIRMED and mark ready first)
   
   TRY: Click a button that would reverse the state
   Example: Try to click [MARK_READY] when status is SESSION_STARTED
   
   WHAT SHOULD HAPPEN:
   Button is DISABLED (grayed out)
   OR error message: "Cannot transition from SESSION_STARTED to READY"

[11:56] NARRATION (10 seconds):
   "State transitions are protected. The MODEL enforces a strict state 
   machine. Valid transitions: CONFIRMED → READY → STARTED → COMPLETED.
   Backwards transitions are impossible. This prevents operational errors."

[12:06] DEMONSTRATE ERROR #3 - Recruiter Overlap
   
   TRY: Publish second availability block that OVERLAPS first
   Current: 13:00-15:00 (4 slots)
   Attempt: 14:00-16:00 (would overlap 14:00-14:30 and 14:30-15:00)
   
   WHAT SHOULD HAPPEN:
   Validation error message appears:
   "Cannot publish 14:00-16:00: Time slot overlaps existing availability 
   at 14:00-14:30"
   Button: [Publish] is disabled or throws error

[12:20] NARRATION (8 seconds):
   "The recruiter can't accidentally double-book themselves. The CONTROLLER 
   validates against existing offers before publishing. Business logic 
   prevents recruiter conflicts."

[12:28] PAUSE: 2 seconds

[12:30] NARRATION (15 seconds - Final ERROR handling summary):
   "You've seen three critical error handlers:
   1. No overbooking for candidates
   2. No invalid state transitions for bookings
   3. No recruiter scheduling conflicts
   
   These aren't UI bandages - they're REAL business logic in the MODEL. 
   VCFS is robust because the MODEL enforces rules, not because the UI 
   tries to prevent mistakes."

[12:45] TIME CHECK: Should be around 12:45-14:00
   (Might be ahead or behind - adjust next segment timing)

SEGMENT 5 COMPLETE - Proceed to Segment 6

═════════════════════════════════════════════════════════════════════════════════
SEGMENT 6: CONCLUSION & MVC EXCELLENCE (Final 2-4 minutes)
═════════════════════════════════════════════════════════════════════════════════

[12:45 or later - adjust to remain close to final 20 min] ARRANGE ALL 3 WINDOWS
   
   Minimize/arrange so all 3 portals visible on screen:
   - Left: Admin Dashboard
   - Center: Recruiter Dashboard
   - Right: Candidate interface
   
   OR show three side-by-side screenshots

[13:00] NARRATION (30 seconds - READ SLOWLY AND CONFIDENTLY):
   
   "Let me summarize what you've witnessed in this demonstration.
   
   You saw three distinct user interfaces:
   • An administrator configuring an entire event
   • A recruiter publishing availability and managing bookings
   • A candidate searching for opportunities and confirming interviews
   
   All three interfaces operated on the SAME backend data model.
   
   When the admin created organizations, the MODEL stored them.
   When the recruiter published offers, the MODEL generated slots.
   When the candidate booked, the MODEL enforced constraints.
   
   This is the MVC architectural pattern - Model, View, Controller - 
   at its finest."

[13:30] NARRATION CONTINUED (45 seconds):
   
   "The MODEL handles all business logic:
   → Offer creation and validation
   → Booking state machines
   → Skill-based matching algorithms
   → Conflict detection
   → Real-time availability updates
   
   The CONTROLLER coordinates user actions:
   → Receives admin commands
   → Validates input
   → Updates the model
   → Notifies views
   
   The VIEW presents information:
   → Three completely independent interfaces
   → Each tailored to one user role
   → All consuming synchronized data
   
   This architecture delivered:
   • ROBUSTNESS: Business rules enforced at the model layer
   • SCALABILITY: Easy to add new features or portals
   • TESTABILITY: 45+ unit tests for core logic
   • MAINTAINABILITY: Clear separation of concerns"

[14:15] FINAL NARRATION (45 seconds):
   
   "VCFS represents professional, enterprise-grade software design.
   It's the kind of architecture used by LinkedIn, Indeed, and Greenhouse - 
   the real recruitment software companies.
   
   Group 18 built this system from scratch in Java using Swing for the UI, JUnit 
   for testing, and rigorous software engineering principles.
   
   Over 80 classes, over 45 tests, zero compilation errors, and complex 
   business logic all working in harmony.
   
   Thank you for watching Virtual Career Fair System. This concludes 
   Group 18's demonstration of enterprise-grade recruitment automation."

[15:00] OPTIONAL - SHOW CREDITS SLIDE (if you want professional touch)
   
   Slide text:
   VIRTUAL CAREER FAIR SYSTEM
   Group 18 - CSCU9P6
   University of Stirling
   
   Built with:
   • Java 11+
   • Swing UI
   • JUnit 5 Testing
   • MVC Architecture
   
   Lead Architect: Zaid Siddiqui
   [Other team members]

[15:20] FADE TO BLACK

[15:22] END RECORDING

TOTAL TIME: Approximately 15:22

═══════════════════════════════════════════════════════════════════════════════

✅ POST-RECORDING CHECKLIST

□ Playback recording
□ Check audio quality (clear, no background noise)
□ Check video quality (sharp, not blurry)
□ Check timing (should be 15-22 minutes)
□ Check all 3 portals shown
□ Export as MP4
□ File size reasonable (500MB - 2GB)

READY TO SUBMIT TO PANOPTO

═══════════════════════════════════════════════════════════════════════════════
END OF DETAILED INSTRUCTIONS
═══════════════════════════════════════════════════════════════════════════════
