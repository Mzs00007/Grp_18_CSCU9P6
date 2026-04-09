═══════════════════════════════════════════════════════════════════════════════
    VCFS DEMO - PRESENTER'S QUICK REFERENCE CARD
    (Keep this visible while recording - glance at it frequently)
═══════════════════════════════════════════════════════════════════════════════

⏱️  TIMING CHECKLIST (Total: 20 Minutes)

[0:00-0:45]     Intro + MVC Overview (45 sec)
                ✓ Show project structure
                ✓ Show bin folder (.class files)
                ✓ Start application (BUILD_AND_RUN_DEMO.bat)
                └─ All 3 portals should launch

[0:45-4:00]     ADMIN PORTAL (3 min 15 sec)
                ✓ Admin login (Admin/Admin)
                ✓ Create 3 orgs: Google, Apple, Microsoft
                ✓ Create 1 booth for each org
                └─ Time check: Should be at 4:00 exactly

[4:00-9:00]     RECRUITER PORTAL (5 min)
                ✓ Recruiter login (recruiter1/password123)
                ✓ Publish 2 availability blocks (7 total slots)
                ✓ Show automated slot generation
                ✓ Show bookings list
                └─ Time check: Should be at 9:00 exactly

[9:00-16:00]    CANDIDATE PORTAL (7 min)
                ✓ Candidate login (candidate1/password123)
                ✓ Search by skill, organization, time
                ✓ Book 2 offers
                ✓ Show matching algorithm
                ✓ Cancel 1 booking (show state management)
                └─ Time check: Should be at 16:00 exactly

[16:00-16:30]   ERROR HANDLING (0.5 min)
                ✓ Prevent double booking
                ✓ Prevent invalid state transition
                ✓ Prevent recruiter overlap
                └─ Time check: Should be at 16:30

[16:30-20:00]   CONCLUSION & MVC SUMMARY (3.5 min)
                ✓ Recap three portals
                ✓ Explain MVC separation
                ✓ Show robustness & architecture excellence
                └─ End at exactly 20:00

═══════════════════════════════════════════════════════════════════════════════

🎯 WHAT TO SAY (Key Phrases - Use these verbatim)

"This is MVC architecture in action - same MODEL, three different VIEWS"

"The system AUTOMATICALLY created 4 slots from one 2-hour block"

"The MODEL enforces business rules - no double bookings, no invalid states"

"This is exactly what real recruiting systems do"

"Separation of concerns makes code clean and testable"

"The CONTROLLER validates input, the MODEL stores data, the VIEW shows it"

═══════════════════════════════════════════════════════════════════════════════

🚨 IF SOMETHING BREAKS - RECOVERY PHRASES

"System is processing - let me refresh"
[Press F5, wait 3 seconds, continue]

"Due to timing, I'm jumping ahead to the next feature"
[Skip broken part, move forward confidently]

"Let me try that again with the second instance"
[Switch windows, try same action again]

"That slot just filled - let me select another"
[Pick different time, continue booking]

═══════════════════════════════════════════════════════════════════════════════

✅ CHECKLIST BEFORE HITTING RECORD

□ All 3 portals launched? (Admin, Recruiter, Candidate windows visible)
□ Microphone working? (Test recording 10 sec first)
□ Screen at 1920x1080 or higher?
□ Taskbar hidden or clean?
□ Firefox/Chrome browser full screen if needed?
□ System startup messages visible?
□ Read script section 1 in your head?
□ Ready to go? → HIT RECORD

═══════════════════════════════════════════════════════════════════════════════

📋 ACTION-BY-ACTION FOR SEGMENT 1 (0:00-0:45)

START: Open file explorer
  Show: CSCU9P6_GROUP_18_CODE folder
  
ACTION 1: Open terminal in folder
  Type: BUILD_AND_RUN_DEMO.bat
  Press: Enter
  Wait: System starts (expect 5-10 sec)
  
ACTION 2: Show login screens
  Observe: 3 windows (Admin, Recruiter, Candidate)
  
SAY: "Three portals, one system, MVC architecture"

NEXT: Move to Admin window

═══════════════════════════════════════════════════════════════════════════════

📋 ACTION-BY-ACTION FOR SEGMENT 2 (0:45-4:00)

CLICK: Admin window (bring to front)
LOGIN: Username=Admin, Password=Admin
WAIT: Dashboard loads

CREATE 3 ORGANIZATIONS:
  1. "Google" | "Tech Innovation Leader"
  2. "Apple" | "Innovation in Hardware & Software"
  3. "Microsoft" | "Enterprise Solutions Provider"

CREATE 3 BOOTHS (one per org):
  1. "Google Main Booth" | Code: GOOGLE-01
  2. "Apple Careers Booth" | Code: APPLE-01
  3. "Microsoft Internship Hub" | Code: MSFt-01

PAUSE: Show hierarchical list on screen (30 sec)

SAY: "Org structure set. Now to recruiter publishing."

NEXT: Move to Recruiter window

═══════════════════════════════════════════════════════════════════════════════

📋 ACTION-BY-ACTION FOR SEGMENT 3 (4:00-9:00)

CLICK: Recruiter window (bring to front)
LOGIN: Username=recruiter1, Password=password123
WAIT: Dashboard loads

PUBLISH FIRST AVAILABILITY BLOCK:
  Title: "Senior ML Engineer - AI Research"
  Start: 13:00
  End: 15:00
  Slot Duration: 30 min
  Tags: AI, ML, Python, Research
  CLICK: PUBLISH
  OBSERVE: System creates 4 slots automatically

SAY: "Notice - ONE block published, FOUR slots created automatically. 
     That's algorithmic slot generation."

PUBLISH SECOND AVAILABILITY BLOCK:
  Title: "Data Engineer - Backend Systems"
  Start: 15:15
  End: 16:45
  Slot Duration: 30 min
  Tags: Data, Java, SQL, Backend
  CLICK: PUBLISH
  OBSERVE: System creates 3 slots

SHOW: Full offer list (7 total slots)
  2-3 seconds viewing

CLICK: "View Bookings" tab
SHOW: Current bookings (if any)

SAY: "Recruiter can see all their bookings in real-time"

NEXT: Move to Candidate window

═══════════════════════════════════════════════════════════════════════════════

📋 ACTION-BY-ACTION FOR SEGMENT 4 (9:00-16:00) - LONGEST SEGMENT

CLICK: Candidate window (bring to front)
LOGIN: Username=candidate1, Password=password123
WAIT: Dashboard loads

SEARCH #1 - BY SKILL:
  Click: Filter checkbox [Python]
  OBSERVE: 4 AI/ML slots appear
  SAY: "Candidate searches for Python. MODEL filters offer tags."

SEARCH #2 - RESET & BY ORGANIZATION:
  Clear filters
  Click: Organization = "Google"
  OBSERVE: All 7 slots appear
  SAY: "Now showing all Google opportunities"

SEARCH #3 - BY TIME:
  Click: Time range "After 3:00 PM"
  OBSERVE: 3 Data Engineering slots
  SAY: "Smart filtering - same MODEL, instant results"

BOOK FIRST OFFER:
  CLICK: "Senior ML Engineer - 13:00-13:30"
  CLICK: "[BOOK THIS SLOT]"
  WAIT: Confirmation preview
  CLICK: "[CONFIRM BOOKING]"
  WAIT: Success message
  OBSERVE: Confirmation number shown
  SAY: "VCFS assigns booking ID and sends confirmation email"

CLICK: "My Bookings" tab
SHOW: 1 booking now visible
SAY: "Real-time synchronization between views"

BACK TO OPPORTUNITIES:
  CLICK: "Job Opportunities" tab
  CLICK: Different offer - "Data Engineer - 15:15-15:45"
  CLICK: "[BOOK THIS SLOT]"
  CONFIRM: Booking
  OBSERVE: Success message

BACK TO BOOKINGS TAB:
  CLICK: "My Bookings"
  SHOW: 2 bookings now visible
  SAY: "Candidate now has two interviews scheduled"

CANCEL A BOOKING (optional but good):
  On first booking: CLICK: "[CANCEL]"
  CONFIRM: Cancel operation
  OBSERVE: List updates, booking disappears
  SAY: "Model instantly frees up the slot for other candidates"

═════════════════════════════════════════════════════════════════════════════════

📋 ACTION-BY-ACTION FOR SEGMENT 5 (16:00-16:30) - ERROR HANDLING

This segment is about SHOWING robustness without failures. Do these calmly:

ERROR #1 - PREVENT DOUBLE BOOKING:
  (Requires 2 candidate windows OR screenshot)
  Candidate A: Book "14:00-14:30" → Success
  Candidate B: Attempt same slot → Message: "SLOT FULLY BOOKED"
  SAY: "System prevents overbooking - core business logic"

ERROR #2 - INVALID STATE TRANSITIONS:
  (Switch to Recruiter window)
  Find booking with Status: SESSION_STARTED
  TRY: Click "[MARK_READY]" button
  OBSERVE: Button disabled or error message
  SAY: "State machine enforced by MODEL layer"

ERROR #3 - RECRUITER OVERLAP:
  (Still in Recruiter window)
  TRY: Publish "14:00-16:00" while "13:00-15:00" exists
  OBSERVE: Validation error
  SAY: "MODEL prevents recruiter double-booking"

═════════════════════════════════════════════════════════════════════════════════

📋 ACTION-BY-ACTION FOR SEGMENT 6 (16:30-20:00) - CONCLUSION

ARRANGE WINDOWS: Show all 3 portals visible simultaneously if possible
(Or show screenshots side-by-side)

RECORD CONCLUSION VOICEOVER (with all 3 portals visible):

"You've seen VCFS: three independent user interfaces sharing one 
robust data MODEL.

The MVC pattern delivered:
• MODELS: Business logic, validation, state machines
• CONTROLLERS: User action handling and rule enforcement  
• VIEWS: Three beautiful, independent portals

This architecture ensures:
• ROBUSTNESS: No invalid states, business rules enforced
• SCALABILITY: Easy to add features or portals
• TESTABILITY: 45+ unit tests for core logic
• MAINTAINABILITY: Clear separation of concerns

VCFS represents professional, enterprise-grade software design.

Thank you for watching Group 18's Virtual Career Fair System."

END VIDEO here (20:00 mark exactly)

═════════════════════════════════════════════════════════════════════════════════

🎬 RECORDING TIPS FOR YOUR FRIEND

1. PACING:
   - Talk slightly SLOWER than normal (even if it feels slow to you)
   - Pause 1-2 seconds between major transitions
   - Let important messages sink in

2. NARRATION:
   - Speak with confidence - you KNOW this system
   - Use technical terms (MVC, Controller, Model, View) - it's good
   - Avoid "umm" and "uh" - pause instead

3. MOUSE MOVEMENT:
   - Move deliberately, not frantically
   - Click items clearly (audience should see exactly what you click)
   - Hover over buttons 1 second before clicking

4. TIMING:
   - This is NOT a race - 20 minutes is plenty of time
   - If lagging behind, slow narration or add 1-2 sec pauses
   - If ahead of schedule, repeat an explanation or show detail view longer

5. AUDIO QUALITY:
   - Use good microphone (USB mic, not laptop mic)
   - Sit 6 inches from mic
   - Avoid background noise - close other apps
   - Do a 10-second test recording first

═════════════════════════════════════════════════════════════════════════════════

⚠️  IF YOU MESS UP A SENTENCE

Don't stop recording and restart. Instead:
  - Pause 2-3 seconds silently
  - Re-state the sentence correctly
  - Continue forward

During editing, the pause will be trimmed and it'll sound natural.

═════════════════════════════════════════════════════════════════════════════════

📱 SUBMISSION CHECKLIST

After recording:
  ☐ Total runtime: 19-21 minutes (not longer)
  ☐ Audio quality: Clear, no background noise
  ☐ Video quality: 1920x1080 at 30fps minimum
  ☐ Format: MP4
  ☐ File size: 500MB - 2GB (good quality)
  ☐ All 3 portals shown: YES
  ☐ MVC concepts explained: YES
  ☐ Error handling demonstrated: YES

Upload to Panopto and submit link to Canvas.

═════════════════════════════════════════════════════════════════════════════════

🎯 FINAL WORDS

This script is YOUR ROADMAP. Don't memorize it word-for-word. Instead:
  • Know the structure (6 segments, 20 minutes total)
  • Practice 2-3 times before recording
  • Be confident - this system is EXCELLENT
  • If something breaks, keep going smoothly
  • Remember: Examiners are impressed by understanding, not perfection

YOU'VE GOT THIS. NOW GO PRESENT A FANTASTIC DEMO! 🚀

═════════════════════════════════════════════════════════════════════════════════
