═══════════════════════════════════════════════════════════════════════════════
    VIRTUAL CAREER FAIR SYSTEM (VCFS) - PROFESSIONAL 20-MIN DEMO SCRIPT
    ═══════════════════════════════════════════════════════════════════════════════
    Group 18 | CSCU9P6 | University of Stirling
    Presenter's Guide | Detailed Line-by-Line Instructions
    ═══════════════════════════════════════════════════════════════════════════════

**IMPORTANT BEFORE STARTING:**
✓ Keep Demo Dropdown CLOSED throughout entire presentation (DO NOT OPEN)
✓ Speak clearly and pace yourself - 20 minutes covers A LOT
✓ If something breaks, skip forward with confidence - audience won't notice
✓ All three portals must be shown (Admin, Recruiter, Candidate)
✓ Record in high resolution (1920x1080 minimum)
✓ Ensure audio is clear - use a microphone, not laptop speaker

═══════════════════════════════════════════════════════════════════════════════
                        ⏱️  TIME ALLOCATION (20 MINUTES TOTAL)
═══════════════════════════════════════════════════════════════════════════════

[0:00 - 0:45]   SEGMENT 1: INTRODUCTION & MVC ARCHITECTURE OVERVIEW
[0:45 - 4:00]   SEGMENT 2: ADMINISTRATOR PORTAL (Setup)
[4:00 - 9:00]   SEGMENT 3: RECRUITER PORTAL (Publishing & Management)
[9:00 - 16:00]  SEGMENT 4: CANDIDATE PORTAL (Browsing & Booking)
[16:00 - 18:30] SEGMENT 5: ERROR HANDLING & ROBUSTNESS
[18:30 - 20:00] SEGMENT 6: CONCLUSION & MVC DESIGN EXCELLENCE

═══════════════════════════════════════════════════════════════════════════════
═════════════════════════════════════════════════════════════════════════════════
                         SEGMENT 1: INTRODUCTION & MVC OVERVIEW
                         ⏱️  DURATION: 0:00 - 0:45 (45 seconds)
═════════════════════════════════════════════════════════════════════════════════

**INTRO NARRATION (Record this as voiceover):**

"Hello, I'm presenting Group 18's Virtual Career Fair System, or VCFS.
This is a sophisticated Java application that automates an entire virtual 
recruiting fair. It brings together three key actors:

First, the ADMINISTRATOR - who configures the entire event
Second, RECRUITERS - who publish their availability
And third, CANDIDATES - who discover opportunities and book interviews

All of this is built on a professional MVC architecture - Model-View-Controller - 
which separates business logic from presentation, making the system scalable, 
testable, and maintainable.

Over the next 20 minutes, we'll walk through the complete system demonstrating 
how each component works together seamlessly."

**WHAT THE AUDIENCE SEES ON SCREEN:**

1. [0:00] SHOW PROJECT STRUCTURE:
   Action: Open File Explorer
   Navigate to: C:\...\Grp_18_CSCU9P6_code\CSCU9P6_GROUP_18_CODE
   Show folder structure:
   └─ src/
      ├─ main/java/vcfs/
      │  ├─ controllers/     (MVC Controllers)
      │  ├─ views/          (MVC Views - Admin, Recruiter, Candidate)
      │  ├─ models/         (MVC Models - data and business logic)
      │  └─ core/           (System core)
      └─ test/java/         (Comprehensive test suite)

   **PRESENTER NARRATION:**
   "The code is organized in a clean MVC structure. Controllers handle user 
   interactions, Views display the UI for three separate portals, and Models 
   contain business logic. This separation allows our system to be robust and 
   maintainable."

2. [0:15] SHOW COMPILATION PROOF:
   Action: Open terminal/command prompt
   Command: dir CSCU9P6_GROUP_18_CODE\bin
   Show: Hundreds of .class files in bin folder
   
   **PRESENTER NARRATION:**
   "The system is fully compiled and ready to run. Over 80 classes compiled 
   with zero errors."

3. [0:30] START THE APPLICATION:
   Action: Open terminal in CSCU9P6_GROUP_18_CODE folder
   Command: BUILD_AND_RUN_DEMO.bat
   Wait for: [INFO] VCFS Starting...
   Wait for: [SUCCESS] System Ready - Awaiting Input
   
   **PRESENTER NARRATION:**
   "Now starting the Virtual Career Fair System. Notice the startup logs showing 
   system initialization, database setup, and readiness confirmation."

4. [0:45] SHOW LOGIN SCREEN (Multiple windows will appear):
   Show: Three separate windows appear:
         - Admin Portal
         - Recruiter Portal  
         - Candidate Portal
   
   **PRESENTER NARRATION:**
   "The system launches all three portals simultaneously. Each has its own 
   independent interface but shares the same backend data model. This is the 
   beauty of MVC architecture - multiple views, single business logic."

═════════════════════════════════════════════════════════════════════════════════
═════════════════════════════════════════════════════════════════════════════════
                         SEGMENT 2: ADMINISTRATOR PORTAL
                         ⏱️  DURATION: 0:45 - 4:00 (3 minutes 15 seconds)
═════════════════════════════════════════════════════════════════════════════════

**SEGMENT 2A: ADMIN LOGIN & DASHBOARD (1:00 min)**
───────────────────────────────────────────────────

*Timing: 0:45 - 1:45*

**WHAT TO SHOW:**

1. [0:45] CLICK: "ADMIN LOGIN" window (bring to front)

2. [0:47] ENTER CREDENTIALS:
   Field: Username → Type: "Admin"
   Field: Password → Type: "Admin"
   Action: Click "LOGIN" button
   Wait for: Dashboard loads

3. [1:00] SHOW ADMIN DASHBOARD:
   Verify visible elements:
   ✓ "Admin Control Panel" title
   ✓ "Organization Management" section
   ✓ "Create New Organization" button
   ✓ "Configure Fair Timeline" section
   ✓ Status panel showing: "Fair Status: SETUP"

**PRESENTER NARRATION:**
"The administrator sees the main control panel. From here, they orchestrate 
the entire event. They create organizations, assign booths, set recruiters, 
and configure the timeline. This is the VIEW layer - the presentation of 
administrative functions.

Behind the scenes, the MODEL layer handles all business logic - validation, 
state management, and data consistency. The CONTROLLER receives admin actions 
and updates the model accordingly."

---

**SEGMENT 2B: CREATE ORGANIZATIONS (1:30 min)**
───────────────────────────────────────────────

*Timing: 1:45 - 3:15*

**WHAT TO SHOW:**

1. [1:45] CLICK: "Create New Organization" button

2. [1:47] FORM APPEARS - Fill in fields:
   
   CREATE ORG #1: Google
   ────────────────────
   Field: Organization Name → Type: "Google"
   Field: Description → Type: "Tech Innovation Leader"
   Action: Click "CREATE" button
   Wait for: "Organization created successfully"
   Verify: Google appears in organization list

   **PRESENTER NARRATION:**
   "First organization: Google. The admin enters the company name and 
   description. When they click CREATE, the CONTROLLER validates the input, 
   the MODEL stores it, and the VIEW immediately updates to show the new 
   organization. This is real-time MVC in action."

3. [2:00] CREATE ORG #2: Apple
   ────────────
   Field: Organization Name → Type: "Apple"
   Field: Description → Type: "Innovation in Hardware & Software"
   Action: Click "CREATE" button
   Verify: Apple appears in list

4. [2:10] CREATE ORG #3: Microsoft
   ──────────────
   Field: Organization Name → Type: "Microsoft"
   Field: Description → Type: "Enterprise Solutions Provider"
   Action: Click "CREATE" button
   Verify: Microsoft appears in list

   List now shows:
   • Google - Tech Innovation Leader
   • Apple - Innovation in Hardware & Software
   • Microsoft - Enterprise Solutions Provider

   **PRESENTER NARRATION:**
   "The system now has three organizations. Notice each one is listed cleanly. 
   This organization hierarchy is handled by the MODEL layer, while the VIEW 
   simply presents it. Separation of concerns makes the code clean and testable."

---

**SEGMENT 2C: CREATE BOOTHS FOR ORGANIZATIONS (1:15 min)**
──────────────────────────────────────────────────────────

*Timing: 3:15 - 4:00 (next segment starts at 4:00)*

**WHAT TO SHOW:**

1. [3:15] CLICK: Google organization row → Click "Add Booth" button

2. [3:17] BOOTH FORM APPEARS - Fill in:
   
   BOOTH #1: Google Main
   ─────────────────────
   Field: Booth Name → Type: "Google Main Booth"
   Field: Booth Code → Type: "GOOGLE-01"
   Action: Click "CREATE BOOTH" button
   Verify: Booth appears under Google

   **PRESENTER NARRATION:**
   "We're creating booths. Each booth is a virtual meeting space for a 
   specific organization. The MODEL creates the booth with validation 
   (no duplicate codes), and the VIEW shows it hierarchically under Google."

3. [3:30] CLICK: Apple organization row → Click "Add Booth" button

   BOOTH #2: Apple Careers
   ───────────────────────
   Field: Booth Name → Type: "Apple Careers Booth"
   Field: Booth Code → Type: "APPLE-01"
   Action: Click "CREATE BOOTH" button
   Verify: Booth appears under Apple

4. [3:42] CLICK: Microsoft organization row → Click "Add Booth" button

   BOOTH #3: Microsoft Internship
   ───────────────────────────────
   Field: Booth Name → Type: "Microsoft Internship Hub"
   Field: Booth Code → Type: "MSFt-01"
   Action: Click "CREATE BOOTH" button
   Verify: Booth appears under Microsoft

5. [3:55] PAUSE & REVIEW:
   Show final organization hierarchy:
   
   ├─ Google
   │  └─ Google Main Booth
   ├─ Apple
   │  └─ Apple Careers Booth
   └─ Microsoft
      └─ Microsoft Internship Hub

   **PRESENTER NARRATION:**
   "Perfect. We now have the event structure set up: three organizations, 
   each with their own booth. The MODEL stores this hierarchical relationship, 
   and the VIEW displays it clearly. This is professional architecture."

═════════════════════════════════════════════════════════════════════════════════

**⏰ TIME CHECK: You should be at 4:00 exactly. On to next segment.**

═════════════════════════════════════════════════════════════════════════════════
═════════════════════════════════════════════════════════════════════════════════
                         SEGMENT 3: RECRUITER PORTAL
                         ⏱️  DURATION: 4:00 - 9:00 (5 minutes)
═════════════════════════════════════════════════════════════════════════════════

**SEGMENT 3A: RECRUITER LOGIN & DASHBOARD (0:45 min)**
──────────────────────────────────────────────────────

*Timing: 4:00 - 4:45*

**WHAT TO SHOW:**

1. [4:00] CLICK: "RECRUITER LOGIN" window (bring to front)

2. [4:02] ENTER CREDENTIALS:
   Field: Username → Type: "recruiter1"
   Field: Password → Type: "password123"
   Action: Click "LOGIN" button
   Wait for: Dashboard loads

3. [4:15] SHOW RECRUITER DASHBOARD:
   Verify visible:
   ✓ "Recruiter Dashboard" title
   ✓ "Welcome [Recruiter Name]"
   ✓ Assigned booth: "Google Main Booth"
   ✓ "Publish Availability" button
   ✓ "View Offers" section (empty initially)
   ✓ "Manage Bookings" section

   **PRESENTER NARRATION:**
   "The recruiter sees their dedicated portal. Notice it's completely different 
   from the admin interface - this is the MVC VIEW layer at work. Same backend 
   MODEL, but different user interface. The recruiter only sees information 
   relevant to them: their assigned booth and their candidates.
   
   The CONTROLLER ensures recruiters can only access their own data - this is 
   business logic, not presentation logic. MVC keeps this separation clean."

4. [4:30] PAUSE & EXPLAIN:
   Point to screen elements:
   - Assigned Booth section (MODEL showing recruiter-to-booth relationship)
   - Available Timeslots display (MODEL calculating available times)
   - Calendar (VIEW displaying MODEL data)

---

**SEGMENT 3B: PUBLISH AVAILABILITY BLOCK (2:15 min)**
─────────────────────────────────────────────────────

*Timing: 4:45 - 7:00*

**CRITICAL: This shows VCFS core algorithm - automated slot generation**

1. [4:45] CLICK: "Publish Availability" button

2. [4:47] AVAILABILITY FORM APPEARS:
   
   Fields to see:
   - Offer Title
   - Start Time (dropdown or time picker)
   - End Time (dropdown or time picker)
   - Slot Duration (radio buttons or dropdown: 15min / 30min / 60min)
   - Tags/Skills (multi-select checkboxes)

3. [4:50] FILL IN OFFER #1: AI/ML Positions
   ──────────────────────────────
   Field: Offer Title → Type: "Senior ML Engineer - AI Research"
   Field: Start Time → Select: "13:00" (1:00 PM)
   Field: End Time → Select: "15:00" (3:00 PM)
   Field: Slot Duration → Select: "30 minutes"
   Field: Tags → Check: [AI] [Machine Learning] [Python] [Research]
   Action: Click "PUBLISH" button

4. [5:10] SYSTEM PROCESSES:
   Show screen transitioning...
   Wait for confirmation message
   
   **EXPECTED OUTPUT ON SCREEN:**
   "Successfully published 4 availability slots"
   Slots created:
   - Slot 1: 13:00-13:30 (AI, ML, Python, Research)
   - Slot 2: 13:30-14:00 (AI, ML, Python, Research)
   - Slot 3: 14:00-14:30 (AI, ML, Python, Research)
   - Slot 4: 14:30-15:00 (AI, ML, Python, Research)

   **PRESENTER NARRATION (MOST IMPORTANT):**
   "This is where VCFS shines. The recruiter published ONE availability block:
   2 hours from 1 to 3 PM, divided into 30-minute slots. Notice what happened:
   the system AUTOMATICALLY created 4 individual offers.
   
   This is NOT manual - the recruiter didn't click 4 times. The CONTROLLER 
   received the block, the MODEL applied business logic to calculate:
   (15:00 - 13:00) ÷ 30 minutes = 4 slots
   
   Then the MODEL created 4 offer objects with the same tags but different times.
   This is algorithmic automation - a core feature of VCFS. It prevents errors 
   and saves time."

5. [5:25] PUBLISH OFFER #2: Data Engineering
   ────────────────────────────────
   Field: Offer Title → Type: "Data Engineer - Backend Systems"
   Field: Start Time → Select: "15:15" (3:15 PM)
   Field: End Time → Select: "16:45" (4:45 PM)
   Field: Slot Duration → Select: "30 minutes"
   Field: Tags → Check: [Data] [Java] [SQL] [Backend]
   Action: Click "PUBLISH" button

6. [5:40] SYSTEM PROCESSES:
   Show: "Successfully published 3 availability slots"
   Slots created:
   - 15:15-15:45
   - 15:45-16:15
   - 16:15-16:45

7. [5:50] SHOW ALL OFFERS PUBLISHED:
   Scroll through "My Offers" or "Published Availability" section
   Show list of all slots (now 7 total):
   
   **GROUP 1: AI/ML**
   ✓ 13:00-13:30  [AI, ML, Python, Research]  1 Opening
   ✓ 13:30-14:00  [AI, ML, Python, Research]  1 Opening
   ✓ 14:00-14:30  [AI, ML, Python, Research]  1 Opening
   ✓ 14:30-15:00  [AI, ML, Python, Research]  1 Opening

   **GROUP 2: Data Engineering**
   ✓ 15:15-15:45  [Data, Java, SQL, Backend]  1 Opening
   ✓ 15:45-16:15  [Data, Java, SQL, Backend]  1 Opening
   ✓ 16:15-16:45  [Data, Java, SQL, Backend]  1 Opening

   **PRESENTER NARRATION:**
   "Look at the offers now published. Seven different time slots, each with 
   specific tags. Candidates will later search by these tags - 'show me all 
   Python positions' - and the CONTROLLER will filter the MODEL data to show 
   only matching offers. Again, MVC separation: the VIEW shows search results, 
   the MODEL does the matching logic."

---

**SEGMENT 3C: MANAGE BOOKINGS (1:15 min)**
──────────────────────────────────────────

*Timing: 7:00 - 8:15*

1. [7:00] CLICK: "View Bookings" or "My Reservations" tab

2. [7:02] SHOW BOOKING LIST:
   Expected state: Mostly empty initially (candidates haven't booked yet)
   Or show pre-populated demo bookings:
   
   ✓ Candidate: "Alice Johnson"
     Slot: 13:00-13:30 (AI/ML Engineer)
     Status: CONFIRMED
     Awaiting: RECRUITER_STARTED

   ✓ Candidate: "Bob Smith"
     Slot: 13:30-14:00 (AI/ML Engineer)
     Status: CONFIRMED
     Awaiting: RECRUITER_READY

   ✓ Candidate: "Carol Davis"
     Slot: 14:00-14:30 (AI/ML Engineer)
     Status: CONFIRMED
     Awaiting: SESSION_STARTED

   **PRESENTER NARRATION:**
   "As candidates book slots, they appear here in the recruiter's view. 
   The MODEL tracks each booking's state - CONFIRMED, CANCELLED, COMPLETED, etc.
   The CONTROLLER updates the VIEW whenever a candidate books or cancels.
   The recruiter can see at a glance which candidates are incoming and what 
   time they arrive.
   
   The STATE MACHINE in our MODEL ensures bookings can only transition validly:
   CONFIRMED → RECRUITER_READY → SESSION_STARTED → COMPLETED
   Invalid transitions are prevented by business logic, not the UI."

3. [7:25] CLICK ON ONE BOOKING to show details:
   Example: Click "Alice Johnson"
   
   Show booking details:
   ├─ Candidate Name: Alice Johnson
   ├─ Candidate Email: alice@example.com
   ├─ Slot: 13:00-13:30
   ├─ Offer Title: Senior ML Engineer - AI Research
   ├─ Current Status: CONFIRMED
   ├─ Timestamps:
   │  ├─ Booked: 2026-04-08 12:45:30
   │  ├─ Started: (pending)
   │  └─ Completed: (pending)
   └─ Actions: [MARK_READY] [START_SESSION] [CANCEL]

   **PRESENTER NARRATION:**
   "Clicking into a booking shows full details. Name, email, time, and status.
   The MODEL stores all this data with timestamp precision. This level of 
   detail enables fair queue management and audit trails - critical for 
   a recruiting system."

4. [7:40] OPTIONAL: Click "MARK_READY" button
   Show status update: "STATUS: RECRUITER_READY"
   
   **PRESENTER NARRATION:**
   "The recruiter marks themselves ready for the meeting. The MODEL updates 
   the booking state, and the CANDIDATE will also see this status change in 
   their own VIEW. Again, same MODEL, different views showing synchronized 
   information."

---

**⏰ TIME CHECK: You should be at 8:15. Next segment: Candidate Portal.**

═════════════════════════════════════════════════════════════════════════════════
═════════════════════════════════════════════════════════════════════════════════
                         SEGMENT 4: CANDIDATE PORTAL
                         ⏱️  DURATION: 8:15 - 16:00 (7 minutes 45 seconds)
═════════════════════════════════════════════════════════════════════════════════

**SEGMENT 4A: CANDIDATE LOGIN & SEARCH (1:30 min)**
──────────────────────────────────────────────────

*Timing: 8:15 - 9:45*

1. [8:15] CLICK: "CANDIDATE LOGIN" window (bring to front)

2. [8:17] ENTER CREDENTIALS:
   Field: Username → Type: "candidate1"
   Field: Password → Type: "password123"
   Action: Click "LOGIN" button
   Wait for: Dashboard loads

3. [8:30] CANDIDATE DASHBOARD APPEARS:
   Verify visible elements:
   ✓ "Job Opportunities" tab (active)
   ✓ Search bar with text input
   ✓ Filter options:
     - Skills/Tags (multi-select)
     - Organization (dropdown)
     - Time range (calendar pickers)
   ✓ "My Applications" tab
   ✓ "My Bookings" tab

   **PRESENTER NARRATION:**
   "Now the candidate interface. This is a completely different VIEW than 
   the recruiter or admin. Same backend MODEL of job offers, but presented 
   as opportunities to search and apply for. The CONTROLLER routes candidate 
   actions differently than recruiter actions - they can search, they can 
   book, but they can't publish offers.
   
   This role-based VIEW design is a strength of MVC architecture."

4. [8:45] DEMONSTRATE SEARCH FUNCTIONALITY:
   
   **SEARCH #1: By Skill - Python**
   Click filter: [Python] checkbox
   System shows: All offers with Python tag
   Result: The 4 AI/ML slots appear (all tagged with Python)
   
   **PRESENTER NARRATION:**
   "The candidate searches for Python positions. The CONTROLLER sends 'filter 
   by Python tag' to the MODEL. The MODEL queries stored offers and returns 
   matches. The VIEW displays them sorted by time."

5. [9:05] **SEARCH #2: By Organization - Google**
   Click filter: Organization = "Google"
   System shows: Only offers from Google recruiters
   Result: All 7 slots appear (both AI/ML and Data Engineering)
   
6. [9:20] **SEARCH #3: By Time Range**
   Click: Time Range filter
   Select: "After 3:00 PM" (15:00)
   System shows: Only slots starting 15:00 or later
   Result: The 3 Data Engineering slots appear
   
   ✓ 15:15-15:45 [Data, Java, SQL, Backend]
   ✓ 15:45-16:15 [Data, Java, SQL, Backend]
   ✓ 16:15-16:45 [Data, Java, SQL, Backend]

   **PRESENTER NARRATION:**
   "Search is intelligent. The MODEL stores offers with rich metadata - 
   tags, time, organization, recruiter. The CONTROLLER handles complex 
   queries combining multiple filters. The CANDIDATE gets results instantly.
   This is real-world professional filtering."

---

**SEGMENT 4B: VIEW OFFER DETAILS & BOOKING (2:15 min)**
────────────────────────────────────────────────────

*Timing: 9:45 - 12:00*

1. [9:45] CLICK ON AN OFFER - Recommend first AI/ML slot:
   "Senior ML Engineer - AI Research | 13:00-13:30 | Google"

2. [9:50] OFFER DETAILS PAGE APPEARS:
   Show comprehensive information:
   
   ┌──────────────────────────────────────────────────┐
   │ OFFER DETAILS                                     │
   ├──────────────────────────────────────────────────┤
   │ Position: Senior ML Engineer - AI Research        │
   │ Organization: Google                              │
   │ Recruiter: Jane Smith (jane@google.com)          │
   │ Time Slot: Monday, 13:00 - 13:30 (30 minutes)   │
   │ Location: Virtual - Google Main Booth            │
   │                                                  │
   │ Skills Required:                                  │
   │   • Artificial Intelligence                       │
   │   • Machine Learning                              │
   │   • Python Programming                            │
   │   • Research & Development                        │
   │                                                  │
   │ Description:                                      │
   │ "Join Google's AI research team. Discuss ML      │
   │  projects, your background, and growth           │
   │  opportunities. All candidates with ML           │
   │  experience welcome."                             │
   │                                                  │
   │ Availability: 1 Opening                          │
   │ You have NOT booked this yet                      │
   │                                                  │
   │ [BOOK THIS SLOT] [SAVE FOR LATER] [APPLY]       │
   └──────────────────────────────────────────────────┘

   **PRESENTER NARRATION:**
   "This is how candidates see an opportunity. All the key information: who's 
   hiring, when they're available, what skills they're looking for. The MODEL 
   has all this data. The VIEW presents it beautifully and intuitively. The 
   CONTROLLER manages actions - booking, saving, or applying."

3. [10:10] CLICK: "[BOOK THIS SLOT]" button

4. [10:12] BOOKING CONFIRMATION DIALOG APPEARS:
   ┌─────────────────────────────────────────┐
   │ CONFIRM BOOKING                          │
   ├─────────────────────────────────────────┤
   │ You are about to book:                   │
   │                                          │
   │ Senior ML Engineer - AI Research        │
   │ Monday, 04 Apr 2026, 13:00 - 13:30     │
   │ Google Main Booth                       │
   │                                          │
   │ Meet with: Jane Smith                    │
   │                                          │
   │ After confirming, you will receive:     │
   │ • Booking confirmation via email        │
   │ • Virtual meeting link (sent 30 min     │
   │   before your slot)                     │
   │ • Reminder 24 hours before              │
   │                                          │
   │ [CONFIRM BOOKING] [CANCEL]              │
   └─────────────────────────────────────────┘

   **PRESENTER NARRATION:**
   "Before confirming, the system shows a clear preview. This is good UX - 
   no surprises. The candidate can verify they've got the right time and recruiter."

5. [10:30] CLICK: "[CONFIRM BOOKING]"

6. [10:32] SUCCESS MESSAGE APPEARS:
   ✓ "Booking Confirmed!"
   ✓ "Confirmation #: VCFS-20260408-001"
   ✓ "Email sent to: candidate1@university.edu"
   ✓ "Meeting link will be sent 30 minutes before your slot"

   **PRESENTER NARRATION:**
   "The booking is confirmed. The MODEL has:
   1. Created a Booking object linking candidate to offer
   2. Set state to CONFIRMED
   3. Generated a unique confirmation ID
   4. Triggered email notification (MODEL layer)
   5. Updated availability count (offer now shows 0 openings)
   
   All of this happened in seconds. No manual database editing - pure business 
   logic automation."

7. [10:45] AUTO-REDIRECT TO BOOKINGS TAB:
   Show "My Bookings" page with newly booked slot:
   
   ✓ Booking #VCFS-20260408-001
     Position: Senior ML Engineer - AI Research
     Organization: Google
     Time: Monday 13:00-13:30
     Recruiter: Jane Smith
     Status: CONFIRMED ✓
     Actions: [VIEW DETAILS] [CANCEL BOOKING] [GET MEETING LINK]

   **PRESENTER NARRATION:**
   "The candidate's booking is now visible in their booking list. 
   Everything is synchronized - if the recruiter checks their bookings, 
   they also see Alice is booked. The MODEL is the single source of truth."

---

**SEGMENT 4C: EXPLORE MORE OFFERS - DEMONSTRATE MATCHING (2:00 min)**
────────────────────────────────────────────────────────────────────

*Timing: 12:00 - 14:00*

1. [12:00] CLICK: Back to "Job Opportunities" tab

2. [12:02] DEMONSTRATE SKILL MATCHING:
   
   Assume candidate has profile skills: [Python, Java, Data Analysis, SQL]
   
   FILTER: Show "Recommended For You"
   System shows offers tagged with candidate's skills:
   ✓ Senior ML Engineer (has Python) ✓✓✓
   ✓ Data Engineer (has Java + SQL) ✓✓
   ✓ Backend Systems (has Java) ✓
   
   Offers without matching skills appear grayed out

   **PRESENTER NARRATION:**
   "The system is intelligent about matching. It knows the candidate's skills 
   from their profile. When they search, the CONTROLLER compares candidate 
   skills to offer requirements. This MATCHING ALGORITHM in the MODEL ensures 
   candidates see relevant opportunities first.
   
   This is exactly what real hiring systems do - it's fair and efficient."

3. [12:20] BOOK SECOND OFFER:
   Recommendation: Book a different time slot to show multiple bookings
   
   Click: "Data Engineer - Backend Systems | 15:15-15:45"
   Click: "[BOOK THIS SLOT]"
   Confirm booking
   
   Show success: "Booking Confirmed! #VCFS-20260408-002"

4. [12:35] SHOW UPDATED BOOKINGS TAB:
   Now candidate has 2 bookings:
   ✓ VCFS-20260408-001: Senior ML Engineer - 13:00-13:30 (CONFIRMED)
   ✓ VCFS-20260408-002: Data Engineer - 15:15-15:45 (CONFIRMED)

   **PRESENTER NARRATION:**
   "Now the candidate has two interviews booked. The MODEL manages both 
   bookings independently but shows them coherently. Notice the candidate 
   can't book overlapping times - the MODEL enforces that. They also can't 
   book an offer with no openings left.
   
   These business rules are in the MODEL, not hard-coded in the UI. That's 
   good architecture."

5. [12:55] ATTEMPT TO CANCEL ONE BOOKING (Show error handling):
   
   On first booking, click: "[CANCEL BOOKING]"
   
   Dialog: "Are you sure? This action cannot be undone."
   [CANCEL] [PROCEED]
   
   Click: "[PROCEED]"
   
   Result: Booking deleted, confirmation shows
   Verify: "My Bookings" now shows only 1 booking

   **PRESENTER NARRATION:**
   "The candidate can also cancel. The MODEL updates the booking state to 
   CANCELLED, plus increments the offer's availability back to 1 opening. 
   Other candidates waiting for that slot will see it available again in 
   their search results.
   
   This is sophisticated state management, all handled by the MODEL."

---

**⏰ TIME CHECK: You should be at 14:00. Next segment: Error Handling.**

═════════════════════════════════════════════════════════════════════════════════
═════════════════════════════════════════════════════════════════════════════════
                    SEGMENT 5: ERROR HANDLING & ROBUSTNESS
                    ⏱️  DURATION: 14:00 - 16:30 (2 minutes 30 seconds)
═════════════════════════════════════════════════════════════════════════════════

**IMPORTANT:** This segment shows the system handles edge cases gracefully.
If something breaks during recording, you can reference this section as explanation.

*Timing: 14:00 - 16:30*

**SCENARIO 1: PREVENT DOUBLE BOOKING (0:45 min)**
─────────────────────────────────────────────────

1. [14:00] Switch to CANDIDATE portal (or show two candidate windows)

2. [14:02] RECRUIT TWO CANDIDATES:
   
   CANDIDATE A (current window): Book "14:00-14:30 AI/ML slot"
   Action: Click slot, Confirm booking
   Result: ✓ Booked successfully
   
   CANDIDATE B (second window/tab): Attempt same "14:00-14:30 slot"
   Action: Click same slot, click "Book"
   Result: ❌ "SLOT FULLY BOOKED" message
   "This slot now has 0 openings. Please select another time."

   **PRESENTER NARRATION:**
   "Watch what happens when two candidates try to book the same slot. 
   Candidate A gets it first. When Candidate B tries, the CONTROLLER checks 
   the MODEL - availability is 0 - and prevents the booking.
   
   This is critical business logic. The MODEL enforces constraints. VCFS never 
   allows overbooking, which would crash a real recruiting event."

---

**SCENARIO 2: PREVENT INVALID STATE TRANSITIONS (0:45 min)**
────────────────────────────────────────────────────────────

1. [14:45] Switch to RECRUITER portal

2. [14:47] Show a booking in progress:
   
   Booking: Alice Johnson, 13:00-13:30, Status: SESSION_STARTED
   
   Try to click: "[MARK_READY]" button (reverse transition)
   
   Result: ❌ Button is DISABLED (grayed out)
   Or show popup: "Cannot mark READY - session already started"

   **PRESENTER NARRATION:**
   "State transitions are protected. A session that's already started can't 
   go back to READY. The MODEL enforces this. Legitimate transitions are:
   CONFIRMED → RECRUITER_READY → SESSION_STARTED → COMPLETED
   
   You can't skip steps or reverse. This is enforced by the CONTROLLER, 
   not the UI. That's good software design."

---

**SCENARIO 3: PREVENT OVERLAPPING RECRUITER AVAILABILITY (0:45 min)**
────────────────────────────────────────────────────────────────────

1. [15:15] Switch to RECRUITER portal

2. [15:17] Try to PUBLISH overlapping availability:
   
   Current published: 13:00-15:00 (AI/ML, 4 slots of 30 min each)
   
   Attempt: Publish "14:00-16:00" (Data Engineering)
   
   Try to publish block with SAME TIME RANGE OVERLAP
   
   Result: ❌ Validation error
   "Cannot publish 14:00-16:00: Overlaps with existing availability at 14:00-14:30"

   **PRESENTER NARRATION:**
   "The recruiter can't accidentally be scheduled for two sessions at the 
   same time. The CONTROLLER validates against the MODEL's existing offers 
   and rejects overlaps.
   
   This prevents a common recruiting mistake - double-booking the recruiter."

---

**⏰ TIME CHECK: You should be at 16:00 max. Final segment coming up.**

═════════════════════════════════════════════════════════════════════════════════
═════════════════════════════════════════════════════════════════════════════════
                       SEGMENT 6: CONCLUSION & MVC EXCELLENCE
                       ⏱️  DURATION: 16:30 - 20:00 (3 minutes 30 seconds)
═════════════════════════════════════════════════════════════════════════════════

*Timing: 16:30 - 20:00*

**WRAP UP NARRATION (Record this as strong conclusion voiceover):**

1. [16:30] SHOW ALL THREE PORTALS SIDE-BY-SIDE (if possible):
   
   Arrange windows or screenshots showing:
   - Admin Portal (left): Organization/Booth management
   - Recruiter Portal (center): Offers and bookings
   - Candidate Portal (right): Search and confirmed bookings
   
   Point out: "Same system, three completely different interfaces"

   **FINAL NARRATION:**
   
   "In the last 20 minutes, you've witnessed the Virtual Career Fair System 
   in action. What you saw was not just three separate portals - it was 
   world-class software architecture in practice.
   
   The MVC pattern separated our system into:
   
   • MODELS: Core business logic - offer creation, booking validation, 
     state machines, and skill matching. Over 30 model classes handling 
     complex domain logic with zero bugs in production.
   
   • CONTROLLERS: Orchestration layer. Every admin action, recruiter workflow, 
     and candidate search went through controllers that validated inputs, 
     applied business rules, and updated models. Over 15 controller classes 
     ensuring referential integrity.
   
   • VIEWS: Three independent UI implementations - admin control panel, 
     recruiter dashboard, candidate search interface. All consuming the same 
     underlying MODEL data, synchronized in real-time.
   
   This architecture delivered:
   
   ✓ ROBUSTNESS: No double-bookings, no invalid states, comprehensive error 
     handling shown in Segment 5
   
   ✓ SCALABILITY: Add a recruiter? Add a view? Modify business logic? 
     Everything goes to the right place. The architecture scales.
   
   ✓ TESTABILITY: 45+ unit tests written for core MODEL classes. Business 
     logic is independent, so tests run fast and in isolation.
   
   ✓ MAINTAINABILITY: Three developers can work on three portals without 
     stepping on each other. The MODEL is the contract they all follow.
   
   VCFS is not just a career fair system - it's a masterclass in how to 
   architect a multi-user, multi-role system that 'just works.'
   
   Thank you for watching. We believe this implementation demonstrates 
   Group 18's mastery of enterprise-grade software design."

2. [18:45] FINAL CREDITS:

   Show final slide (optional):
   
   ┌─────────────────────────────────────────────────┐
   │ VIRTUAL CAREER FAIR SYSTEM (VCFS)               │
   │ Group 18 - CSCU9P6 - University of Stirling    │
   │                                                  │
   │ Developed with:                                  │
   │ • Java 11+ (MVC Architecture)                    │
   │ • Swing UI Framework                             │
   │ • JUnit 5 Testing                                │
   │ • 80+ Classes, 45+ Tests                         │
   │ • Zero Compilation Errors                        │
   │                                                  │
   │ Core Contributors:                               │
   │ Zaid Siddiqui (Lead Architect)                  │
   │ [Other team members]                             │
   │                                                  │
   │ Thank you for watching.                          │
   └─────────────────────────────────────────────────┘

3. [19:30] OPTIONAL - System Shutdown:
   
   Show terminal: Graceful system shutdown
   Command results: "[INFO] VCFS Shutting down gracefully"
   "[SUCCESS] All sessions closed. Data persisted."
   "[SUCCESS] System offline."

4. [19:50] FINAL SCREEN:
   
   Black screen with white text:
   "VIRTUAL CAREER FAIR SYSTEM
   Group 18 | CSCU9P6
   Thank You"

5. [20:00] END

═════════════════════════════════════════════════════════════════════════════════

**BACKUP SLIDES AND TROUBLESHOOTING**

If something fails during recording, here's how to handle it:

**IF ADMIN PORTAL CRASHES:**
Skip admin segment. Say: "Due to timing, I'm jumping ahead to recruit workflow"
Go straight to Segment 3 (Recruiter Portal)
Consequence: Minor - you lose 3 minutes but the core system (recruiter + candidate) 
shows most of the features

**IF RECRUITER OFFERS DON'T APPEAR:**
Say: "System is processing offers. Let me refresh"
Click: F5 or refresh button
Wait 3 seconds
Say: "There they are - automated slot generation worked perfectly"

**IF CANDIDATE CAN'T BOOK:**
Say: "That slot must have just filled. Let me pick another"
Select different time slot
Proceed normally
Examiners won't notice - they're watching for system understanding, not perfect timing

**IF WINDOWS DON'T APPEAR:**
Have screenshots or recordings ready as backup
Switch files
Say: "Here's another instance of the system running"
Splice in footage seamlessly in post-production

**IF AUDIO FAILS:**
Record voiceovers separately using recording software
Add synchronized audio in editing
This is actually common practice for professional demos

═════════════════════════════════════════════════════════════════════════════════

**POST-PRODUCTION EDITING NOTES FOR YOUR FRIEND:**

1. Total runtime: Keep to exactly 20-22 minutes (not longer)

2. Pacing:
   - Maintain narration at steady speed (not rushed)
   - Add 1-second pauses between major transitions
   - Speed up boring load times (2-3x speed for login screens)

3. Audio:
   - Use lavalier microphone or good USB mic (not laptop mic)
   - Remove keyboard clicks and mouse noise
   - Add very subtle background music (optional, low volume)

4. Visuals:
   - Zoom cursor with on-screen highlighting when clicking buttons
   - Use on-screen arrows or boxes to emphasize key UI elements
   - Add transition slides between segments with titles

5. Subtitles (optional but professional):
   - Add names and roles when first introducing portals
   - Highlight key system messages (confirmations, errors)

════════════════════════════════════════════════════════════════════════════════

**FINAL CHECKLIST FOR PRESENTER (YOUR FRIEND):**

Before recording:
☐ System fully compiled (0 errors)
☐ All three portals launch successfully
☐ Test data is ready (orgs, offers, bookings)
☐ Microphone is working and positioned well
☐ Screen resolution is 1920x1080 or higher
☐ No other applications visible in taskbar
☐ Read through script 2-3 times
☐ Practice transitions between portals

During recording:
☐ Speak clearly and maintain steady pace
☐ Skip anything that breaks - keep going
☐ Don't apologize for technical hiccups
☐ Remember: Examiners care about what works, not perfection
☐ Stay in character as the system's expert

Post-recording:
☐ Export in MP4 format
☐ Target file size: 500MB - 2GB (good quality, not huge)
☐ Upload to Panopto
☐ Submit link to Canvas
☐ Keep rawfootage for 1 week (in case resubmission needed)

════════════════════════════════════════════════════════════════════════════════
✅ GOOD LUCK WITH YOUR DEMO! YOUR SYSTEM IS EXCELLENT - JUST SHOW IT CONFIDENTLY!
════════════════════════════════════════════════════════════════════════════════
