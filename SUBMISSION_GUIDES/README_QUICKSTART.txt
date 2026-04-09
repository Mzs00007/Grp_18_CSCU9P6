================================================================================
ASSIGNMENT SUBMISSION GUIDE - COMPLETE INDEX & STRATEGY
================================================================================
CSCU9P6 GROUP ASSIGNMENT - Spring 2026 | Total Points: 100
VCFS (Virtual Career Fair System) | Group 9
================================================================================

⚠️ CRITICAL DATES ⚠️
┌─ 7 APRIL (Wednesday) 23:59        CODE DUE (triggers -3 pts/day penalty)
├─ 8-9 APRIL (Thu-Fri)              DEMONSTRATION (live, 20 min)
└─ 9 APRIL (Friday) 23:59 MAX       ALL items must be submitted

CONSEQUENCES OF MISSING DEADLINES:
  • Code 1 day late: -3 (Screencast) - 3 (Demo) = -6 points
  • Code 3 days late: -9 (Screencast) - 9 (Demo) = -18 points
  • Screencast never submitted: Lose 50% of total grade
  
STRATEGY: Submit code by 6 April (24-hour safety margin)

================================================================================
SUBMISSION COMPONENTS - DETAILED BREAKDOWN
================================================================================

Your VCFS system is graded on SIX distinct components:

1️⃣  JUnit TEST REPORT (15 points) - GROUP SUBMISSION
    ├─ Based on 4 model classes: Lobby, MeetingSession, Reservation, VirtualRoom
    ├─ Requires: 120+ comprehensive test cases (30-35 per class)
    ├─ Include: Test rationale, test code, execution results
    ├─ Location: SUBMISSION_GUIDES/01_JUnit_Test_Report/GUIDE_JUnit_Report.txt
    ├─ KEY INSIGHT: Tests are verified 100% passing during demo
    └─ STRATEGY: Focus on coverage (initialization, boundaries, state transitions, 
                 integration), not just code paths

2️⃣  INDIVIDUAL REFLECTIVE DIARY (20 points) - INDIVIDUAL
    ├─ Format: 5-8 weekly entries × 300-500 words = 1,500-2,500 total words
    ├─ Framework: STAR-L (Situation, Task, Action, Results, Learning)
    ├─ Requirements: GitHub links, metrics (hours, commits, tests), OneDrive track 
                     changes enabled
    ├─ Location: SUBMISSION_GUIDES/02_Individual_Reflective_Diary/
                           GUIDE_Reflective_Diary.txt
    ├─ GRADING CRITERIA: 
    │   - Demonstrates learning (not just recounting work)
    │   - Honest reflection on challenges faced
    │   - Shows growth and understanding
    ├─ RED FLAGS: Copied from teammates', generic statements, no reflection
    └─ STRATEGY: Write honestly about what surprised you, what was hard, 
                 what you learned. Examiners want to see growth.

3️⃣  SCREENCAST VIDEO (50 points) - GROUP SUBMISSION *** LARGEST COMPONENT ***
    ├─ Duration: 15-30 minutes (optimal: 20-22 min)
    ├─ Format: MP4, clear audio, 720p+ video
    ├─ Content: Demonstrate ALL use cases from specification
    │   - Admin: Create org → booth → assign recruiter → set timeline
    │   - Recruiter: Publish availability (auto-generates 6×30 min slots) 
    │               → manage bookings → conduct meeting
    │   - Candidate: Browse → book → attend meeting with FIFO queue shown
    │   - Error handling: Double-booking prevention, capacity limits, timeline 
    │                     enforcement
    ├─ Location: SUBMISSION_GUIDES/03_Screencast_Video/GUIDE_Screencast_Video.txt
    ├─ INCLUDES: Complete script with exact on-screen actions
    ├─ CRITICAL: This is 50% of grade—worth all effort to get right
    └─ STRATEGY: Record when system is stable, test data populated, no rushing.
                 Rough cuts are OK if content complete. Upload 48 hours before 
                 deadline for buffering.

4️⃣  CODE SUBMISSION (0 points assessed, but affects other components!) 
    ├─ What: .zip file with all src/main/java and src/test/java
    ├─ When: 7 April 23:59 on Canvas
    ├─ Late Penalty: -3 pts Screencast/day, -3 pts Demo/day
    ├─ Location: SUBMISSION_GUIDES/04_Code_Submission/GUIDE_Code_Submission.txt
    ├─ Contents: ONLY .java files, NO .class, NO build/, NO .idea/
    ├─ VERIFICATION: Open zip after creating—confirm src/ structure present
    └─ STRATEGY: Create zip by 6 April, test on different machine to ensure 
                 portable

5️⃣  LIVE DEMONSTRATION (15 points) - GROUP SUBMISSION
    ├─ When: 8-9 April, 2-hour examination window
    ├─ Duration: 20 minutes total (15 min demo + 5 min Q&A)
    ├─ Attendees: ENTIRE GROUP MUST BE PRESENT (marked in system)
    ├─ Location: SUBMISSION_GUIDES/05_Demonstration_Prep/GUIDE_Demonstration_Prep.txt
    ├─ Format: Live system walkthrough + examiners ask design/code questions
    ├─ GRADING:
    │   - Use case coverage (4 pts): All workflows shown without crashes
    │   - System stability (3 pts): No crashes, smooth transitions
    │   - Presentation (3 pts): Clear narration, confident delivery
    │   - Design Q&A (2 pts): Understand architecture choices (MVC, patterns)
    │   - Code Q&A (2 pts): Explain implementation details
    │   - Teamwork (1 pt): Group coordination evident, GitHub used well
    ├─ SAMPLE QUESTIONS:
    │   "Why MVC architecture?" → Examiners want to hear about separation of 
    │                               concerns, testing, maintainability
    │   "How prevent double-booking?" → Show validation in model, controller, 
    │                                    response handling
    │   "What part did you implement?" → Point to GitHub commits, show code 
    │                                     you understand
    └─ STRATEGY: Practice 20-min demo 3 times before demo day. Assign roles. 
                 Prepare for Q&A by reviewing your actual code (not just talking 
                 points).

6️⃣  INDIVIDUAL CONTRIBUTIONS FORM (0 points graded, critical for fairness!)
    ├─ What: One form per group member documenting individual work
    ├─ When: Print and bring to demonstration (8-9 April)
    ├─ Format: Word document, Microsoft template provided
    ├─ Location: SUBMISSION_GUIDES/06_Individual_Contributions_Form/
                           GUIDE_Contributions_Form.txt
    ├─ KEY SECTIONS:
    │   - Name, ID, group number
    │   - Contribution percentage (you, not group): ~18-20% expected
    │   - Detailed breakdown (components you owned)
    │   - GitHub evidence (links to your commits)
    │   - Challenges and how you solved them
    │   - Self-assessment (1-5 rating on skills)
    ├─ VERIFICATION: Examiners compare:
    │   - Your claimed % vs your commit count
    │   - Your claimed components vs your GitHub history
    │   - Your claimed % + all teammates' % should ≈ 100%
    └─ STRATEGY: Fill out honestly. Examiners know GitHub can't lie.
                 Better to claim 18% and show 20 commits than claim 25% and 
                 show 5 commits.

================================================================================
YOUR VCFS SYSTEM ARCHITECTURE (What makes it "professional")
================================================================================

VCFS uses proper MVC (Model-View-Controller) design:

MODELS (Pure data + business logic):
  vcfs.models.booking: Reservation, MeetingSession, Lobby, Offer, Request
  vcfs.models.users: Candidate, Recruiter, User
  vcfs.models.structure: VirtualRoom, Booth, Organization
  vcfs.models.enums: ReservationState, MeetingState, FairPhase, RoomState
  vcfs.models.audit: AttendanceRecord, AuditEntry

CONTROLLERS (Handle user actions):
  vcfs.controllers: AdminScreenController, CandidateController, 
                   RecruiterController

VIEWS (Java Swing GUI):
  vcfs.views.admin: AdminScreen
  vcfs.views.candidate: CandidateScreen
  vcfs.views.recruiter: RecruiterScreen
  vcfs.views.shared: Shared components

CORE SYSTEM:
  vcfs.core: CareerFairSystem (orchestrator), CareerFair, SystemTimer, 
             Logger, LocalDateTime

CRITICAL CLASSES FOR TESTING (4 model classes):
  1. Lobby (Queue management, FIFO ordering)
  2. Reservation (Booking with state transitions: CONFIRMED→IN_PROGRESS→CANCELLED)
  3. MeetingSession (Runtime session linking reservation to room+lobby)
  4. VirtualRoom (Persistent room with occupancy management)

================================================================================
100-POINT GRADE BREAKDOWN - WHERE YOUR MARKS COME FROM
================================================================================

SUBMISSION          POINTS    FORMAT           DEADLINE    SUBMISSION

1. JUnit Tests      15 pts    Include in code  7 Apr 23:59 Canvas + Demo
2. Diary            20 pts    Individual Word  9 Apr 23:59 OneDrive + Demo
3. Screencast       50 pts    MP4 video        ASAP        Panopto + Canvas
4. Code             0 pts     .zip file        7 Apr 23:59 Canvas
5. Demo             15 pts    Live group       8-9 Apr     In-person
6. Contributions    0 pts     Individual form  9 Apr       Print at demo

───────────────────────────────────────────────────────────────────────────
TOTAL               100 pts

BREAKDOWN BY CATEGORY:

GROUP GRADE (most members get same):
  JUnit (15) + Screencast (50) + Demo (15) = 80/100

INDIVIDUAL MODIFICATION (varies by person):
  Diary (20 pts): Each person's diary scored individually
  Contributions form (0 pts direct, but modifies group grade if unequal)
  
REALISTIC SCENARIOS:

Scenario A: Excellent submission
  JUnit: 12-15 (comprehensive, all tests passing)
  Screencast: 45-50 (all use cases, professional quality)
  Demo: 12-15 (smooth, good Q&A answers)
  Diary: 18-20 (detailed learning, GitHub evidence)
  GROUP GRADE: 69-80 → TYPICAL FINAL: 85-90

Scenario B: Good submission
  JUnit: 10-12 (comprehensive, minor gaps)
  Screencast: 35-40 (good coverage, some rough edges)
  Demo: 10-12 (few crashes, Q&A partly shaky)
  Diary: 14-16 (decent but not deep reflection)
  GROUP GRADE: 55-64 → TYPICAL FINAL: 70-78

Scenario C: Minimum acceptable
  JUnit: 8-10 (covers basic cases, some edge cases missing)
  Screencast: 25-30 (missing some use cases)
  Demo: 8-10 (crashes handled, Q&A weak)
  Diary: 10-12 (generic, little personal reflection)
  GROUP GRADE: 41-50 → TYPICAL FINAL: 45-60 (at risk of failing)

================================================================================
HOW TO USE THESE GUIDES - STEP BY STEP
================================================================================

THIS WEEK (3-7 April):

Step 1: Read relevant guides for YOUR role
  If Zaid (lead): Read 01_JUnit + 05_Demo + 06_Contributions
  If YAMI (admin): Read 01_JUnit + 03_Video + 06_Contributions
  If Taha (recruiter): Read 01_JUnit + 03_Video + 06_Contributions
  If testing: Read 01_JUnit (YOU ARE STAR HERE!)
  Everyone: Read your specific component guide

Step 2: Implement what's described (if not already done)
  Example: Follow 01_JUnit_Report guide to create/verify test reports
  Example: Follow 03_Screencast guide to create video script
  Example: Ensure code compiles per 04_Code_Submission guide

Step 3: Prepare for demo
  Follow 05_Demonstration_Prep for role assignment, Q&A prep, script timing
  Practice 3-4 times, time it, refine transitions

Step 4: Complete individual documents
  Diary: Write reflective journal entries (02_Individual_Reflective_Diary)
  Contributions: Fill individual contributions form (06_Individual_Contributions_Form)

BEFORE DEMONSTRATION (8-9 April):

Step 5: Create code .zip file
  Follow 04_Code_Submission instructions
  Create .zip, verify contents, test on different machine
  Submit to Canvas by 7 Apr 23:59

Step 6: Submit/finalize Screencast
  Upload to Panopto (follow 03_Screencast guide step-by-step)
  Post link to Canvas
  Do this EARLY (don't wait until demonstration day)

Step 7: Print contributions forms
  Print one form per group member
  Have ready to hand to examination staff

Step 8: Group practice run
  Do full 20-minute demo with roles assigned
  Practice Q&A responses
  Verify all members understand their code sections

DEMONSTRATION DAY:

Step 9: Arrive 10 minutes early
  Bring: Printed contributions forms, code compiled on demo machine
  Setup: Plug in laptop, test screen display, verify system starts

Step 10: Deliver demonstration
  Follow assigned roles from 05_Demonstration_Prep
  Demonstrate all use cases from specification
  Handle Q&A confidently (answers prepared in advance)

Step 11: Submit contributions forms
  Hand printed forms to examination staff
  Get timestamp/receipt
  Celebrate! (you're done with assessment)

AFTER DEMONSTRATION:

Step 12: Final submissions (if not done)
  Diary: Finalize reflective journal on OneDrive
  Code: Ensure .zip submitted to Canvas
  Video: Confirm Panopto link active and Canvas updated

================================================================================
COMMON MISTAKES - AVOID THESE!
================================================================================

❌ JUNIT TESTS
  ✗ Too few tests (< 15 per class) → Incomplete coverage
  ✗ Tests that don't actually test anything (just constructors)
  ✗ Tests that don't handle exceptions (error cases missing)
  ✗ Test names like "test1, test2" (not descriptive)
  ✓ CORRECT: 30+ tests per class, descriptive names, edge cases included

❌ REFLECTIVE DIARY
  ✗ Copied from teammates' diaries (plagiarism alert!)
  ✗ Just lists work ("did X, then did Y") with no reflection
  ✗ No GitHub evidence (difficult to verify)
  ✗ Written all at once day before (shows in writing quality)
  ✓ CORRECT: Written weekly, genuine personal insights, GitHub evidence

❌ SCREENCAST VIDEO
  ✗ Missing use cases (only shows admin setup, not candidate booking)
  ✗ Crashes without recovery (system looks unstable)
  ✗ Inaudible narration (too quiet, background noise)
  ✗ Rushed transitions (jumps between screens too fast)
  ✓ CORRECT: All use cases demonstrated, clear voice, smooth pacing

❌ DEMONSTRATION
  ✗ Not all group members present (marked as absent)
  ✗ Can't answer basic questions about code (looks unprepared)
  ✗ Demonstrated system crashes repeatedly (stability concern)
  ✗ Can't explain why MVC pattern was chosen
  ✓ CORRECT: All present, confident answers, prepared Q&A, stable demo

❌ CONTRIBUTIONS FORM
  ✗ Claiming 45% when group has 5 people (obviously false)
  ✗ Can't explain what you actually did for 8 weeks
  ✗ GitHub shows no commits in your name but you claim work
  ✗ Accusative tone ("person X didn't do anything")
  ✓ CORRECT: Realistic %, specific components, GitHub evidence, honest

================================================================================
FINAL REMINDERS
================================================================================

🎯 STRATEGY PRIORITY:
  1. SCREENCAST is worth 50 points → Invest most effort here
  2. Demo must not crash → Test thoroughly beforehand
  3. Code must compile → Verify on demo machine day before
  4. JUnit coverage must be comprehensive → 30+ tests per class minimum
  5. Diary must show learning → Write honestly about challenges
  6. Contributions honest → Examiners verify with GitHub anyway

📅 DEADLINE DISCIPLINE:
  Code by 6 Apr (safety margin for 7 Apr midnight deadline)
  Screencast uploaded 48 hours before demo (Panopto buffering)
  Contributions forms printed by morning of demo day
  Demo rehearsed at least 3× before actual evaluation

🔐 ACADEMIC INTEGRITY:
  • AI usage limited to planning/research (must disclose on coversheet)
  • No plagiarism from other groups' code
  • Diary entries your own unique perspective
  • Contributions form honestly reflects YOUR work
  • GitHub commit history must match claimed contributions

✅ SUCCESS INDICATORS:
  You're ready if you can answer:
  1. "Explain your system's architecture" (MVC, packages, design patterns)
  2. "Show us your code—explain this method" (You wrote/understand it)
  3. "What did you personally implement?" (Point to GitHub commits, code)
  4. "What test cases verify?" (Can explain test rationale)
  5. "How did your team work together?" (GitHub workflow, code reviews)

================================================================================
END OF SUBMISSION GUIDE INDEX
================================================================================

Last Updated: 8 April 2026
Project: Virtual Career Fair System (VCFS), Group 9, CSCU9P6
All guides: SUBMISSION_GUIDES/ folder (6 detailed guides + this index)

================================================================================
POINT BREAKDOWN - WHERE YOUR MARK COMES FROM (100 pts total)
================================================================================

┌─ SUBMISSION 1: JUnit Test Report                    15 pts  │ GROUP
│  Deadline: 7 April (submit with code)
│  What: 4 model class test suites (Lobby, MeetingSession, 
│        Reservation, VirtualRoom)
│  Required: 120+ comprehensive JUnit tests covering:
│    ✓ All public methods
│    ✓ Exception handling
│    ✓ Edge cases and boundary conditions
│    ✓ State transitions
│    ✓ Integration scenarios
│
├─ SUBMISSION 2: Individual Reflective Diary         20 pts  │ INDIVIDUAL
│  Deadline: 9 April (submit with demonstration)
│  What: Your personal weekly journal (5-8 weeks)
│  Format: STAR-L framework (Situation, Task, Action, Results, Learning)
│  Length: 300-500 words per week
│  Include: GitHub links, metrics (hours, commits, tests)
│           Personal reflection on learning
│
├─ SUBMISSION 3: Screencast Video                     50 pts  │ GROUP
│  Deadline: ASAP (submit Panopto link to Canvas)
│  What: 15-30 minute recorded demo of all use cases
│  Format: MP4, recorded in Panopto
│  Content: Demonstrate every user journey:
│    • Candidate: browse → book → attend meeting
│    • Recruiter: publish offer → manage bookings → conduct session
│    • Admin: oversee fair → manage queues → view audit logs
│    • Error handling and edge cases
│
├─ SUBMISSION 4: Code Submission                      0 pts   │ GROUP
│  Deadline: 7 April (via Canvas)
│  What: Single .zip file with all source code
│  Contents: src/main/java + src/test/java + README
│  Impact: No points, but late penalties affect Screencast & Demo!
│           Each day late: -3 pts from Screencast, -3 pts from Demo
│
├─ SUBMISSION 5: Demonstration (Live)                15 pts   │ GROUP
│  Deadline: 8-9 April (attend scheduled time)
│  What: 20-minute group demo to examiners
│  Assessment: Use case coverage, system stability, design knowledge,
│              code understanding, teamwork shown
│  Impact: All members must attend (marked in system)
│
└─ SUBMISSION 6: Individual Contributions Form        0 pts   │ INDIVIDUAL
   Deadline: 9 April (bring printed to demonstration)
   What: Form documenting your personal contribution %
   Impact: No points, but used for fairness assessment
           May adjust individual marks if contributions very unequal

───────────────────────────────────────────────────────────────────────
TOTAL GROUP GRADE:  100 points (distribution among 4-6 members)
TOTAL INDIVIDUAL:   Marked components (Diary) + moderated via contributions

GRADING STRATEGY:
  • Most group members typically get same grade (if equal contribution)
  • Individual diary contribution can differentiate within group
  • Contributions form used to verify fairness
  • No individual mark changes post-assessment

================================================================================
FOLDER STRUCTURE - WHERE YOUR GUIDES ARE LOCATED
================================================================================

SUBMISSION_GUIDES/
│
├── 01_JUnit_Test_Report/
│   └── GUIDE_JUnit_Report.txt (4,500 words)
│       ├─ Overview of requirements
│       ├─ Test structure (arrangement, payload, assertion)
│       ├─ Test data strategy (realistic vs edge cases)
│       ├─ Grading rubric breakdown (15 pts detail)
│       ├─ Week 3 detailed example (testing Lobby)
│       ├─ Common mistakes to avoid
│       └─ Sample test report template
│
├── 02_Individual_Reflective_Diary/
│   └── GUIDE_Reflective_Diary.txt (5,000 words)
│       ├─ STAR-L framework explained
│       ├─ Week-by-week template structure
│       ├─ Complete Week 3 example entry (900 words)
│       ├─ Grading criteria breakdown (20 pts detail)
│       ├─ Common mistakes and timing
│       ├─ AI compliance guidance
│       └─ What to track (hours, commits, tests)
│
├── 03_Screencast_Video/
│   └── GUIDE_Screencast_Video.txt (4,000 words)
│       ├─ Pre-recording preparation checklist
│       ├─ 16 use case coverage from specification
│       ├─ Video script template (chapter-by-chapter)
│       ├─ Recording tips and common issues
│       ├─ Panopto upload step-by-step
│       ├─ Grading rubric (50 pts detail)
│       └─ What not to include
│
├── 04_Code_Submission/
│   └── GUIDE_Code_Submission.txt (2,500 words)
│       ├─ ZIP file structure expectations
│       ├─ What to include/exclude
│       ├─ How to create ZIP (3 methods)
│       ├─ Verification checklist
│       ├─ Canvas submission steps
│       ├─ Late penalty explanation (-3 pts/day!)
│       ├─ Code quality expectations
│       └─ AI compliance notes
│
├── 05_Demonstration_Prep/
│   └── GUIDE_Demonstration_Prep.txt (5,000+ words)
│       ├─ Grading rubric (15 pts detail)
│       ├─ How examiners will question you
│       ├─ Preparation timeline (week before)
│       ├─ Demo script structure (20 minutes)
│       ├─ Role assignments (who does what)
│       ├─ Q&A preparation (design, code, process)
│       ├─ Technical setup checklist
│       ├─ Common mistakes
│       └─ Day-of logistics
│
├── 06_Individual_Contributions_Form/
│   └── GUIDE_Contributions_Form.txt (3,500 words)
│       ├─ Form sections explained
│       ├─ How to estimate contribution %
│       ├─ Detailed breakdown template
│       ├─ GitHub evidence requirements
│       ├─ Challenges & learning section
│       ├─ Fairness guidelines
│       ├─ Completed example form
│       └─ Common mistakes
│
└── README_QUICKSTART.txt (This file)
    └─ Overview of all submissions, key dates, strategy

================================================================================
HIGH-LEVEL SUBMISSION STRATEGY - MAXIMIZE YOUR MARK
================================================================================

STRATEGY 1: GUARANTEE 65 POINTS (Test Report + Code + Demo)
  Requirements:
    ✓ Submit working code by 7 April 23:59 (0 points, but required)
    ✓ Include 120+ JUnit tests covering 4 model classes (15 points)
    ✓ Demonstrate system on 8-9 April without crashes (15 points)
    ✓ Answer basic design questions correctly (5 points)
    
  Result: 35 points minimum, plus 30 for Screencast demo aspect
  Typical outcome: 60-70 points overall

STRATEGY 2: AIM FOR 85+ POINTS (Add Screencast + Professional Demo)
  Additional requirements:
    ✓ Record comprehensive 20-minute Screencast (all 16 use cases shown)
    ✓ Professional narration and smooth transitions
    ✓ Show error handling and edge cases
    ✓ Upload to Panopto by submission date
    
  Result: +40-50 points for excellent Screencast video
  Typical outcome: 80-90 points overall

STRATEGY 3: MAXIMIZE 90+ POINTS (Everything above + Diary)
  Additional requirements:
    ✓ Complete detailed Individual Reflective Diary (20 points)
    ✓ 5-8 complete weekly entries (300-500 words each)
    ✓ Link to specific GitHub commits
    ✓ Show personal learning progression
    
  Result: +15-20 points for detailed, thoughtful diary
  Typical outcome: 90-100 points overall

KEY INSIGHT:
  • 15 pts from JUnit tests (required for code submission)
  • 50 pts from Screencast video (largest single component!)
  • 15 pts from live demonstration (if code works)
  • 20 pts from individual diary (personal component)
  • 0 pts from code & contributions form (but they matter indirectly!)

BEST APPROACH:
  Priority 1: Get code working and tested (JUnit + demo)
  Priority 2: Record comprehensive Screencast (biggest points)
  Priority 3: Write detailed reflective diary (individual mark)
  Priority 4: Prepare for demo questions (polish delivery)

================================================================================
COMMON QUESTIONS
================================================================================

Q: What if we're late on code submission?
A: Each day late: -3 pts from Screencast + -3 pts from Demo = -6 pts/day
   CRITICAL: Submit code by 7 April to protect your 65+ points!

Q: Do we have to write JUnit tests for all 4 classes?
A: YES. Assignment explicitly requires: Lobby, MeetingSession, 
   Reservation, VirtualRoom. All 4 must be included in code submission.

Q: Is the Screencast required?
A: Not explicitly required (50 points available but no "must have").
   However, it represents 50% of total points - not doing it = max 50 pts.
   Strongly recommended for competitive grades.

Q: Do all members get the same grade?
A: Typically YES - it's group work. BUT:
   • Reflective diary is individual (different mark for each person)
   • Contributions form can trigger moderation if very unequal work
   • Individual achievement shown in demo questions

Q: What if someone didn't contribute fairly?
A: Contributions form and GitHub history reveal this.
   Examiners may adjust individual marks down.
   This is why honest contributions form is important.

Q: When is the Reflective Diary due?
A: Should be completed by 9 April (with demo submission).
   Can be submitted in OneDrive with track changes enabled.
   Show weekly entries from weeks 1-8 of project.

Q: Can we use AI to help write code?
A: Limited use OK for research/planning.
   NOT OK for generating test code or implementation code directly.
   Must include AI coversheet if you used AI tools.

Q: How do we submit the contributions form?
A: Print one copy per group member.
   Bring to demonstration on 8-9 April.
   Hand to examination staff during demo slot.
   Not submitted on Canvas.

Q: What if we find a bug in code during demo?
A: Acknowledge it professionally: "In this edge case, the system would need..."
   Don't say "that's a bug" - frame as learning opportunity.
   Show you understand the issue even if not fixed.

Q: What if application crashes during demo?
A: Restart it calmly, explain what might have caused it.
   Show you know how to recover and continue demo.
   Doesn't automatically fail you - recovery matters too.

Q: How long should the Screencast be?
A: 15-30 minutes recommended.
   Less than 15 min: won't cover all use cases (points lost).
   More than 30 min: too long, show quality over quantity.
   Sweet spot: 20-22 minutes (covers everything, professional pace).

Q: Do all group members need to speak in the Screencast?
A: Recommended to have 2-3 people narrate (shows teamwork).
   But one person narrating is OK if they cover everything clearly.
   Quality of demonstration more important than speaker count.

Q: What if we disagree on the contributions percentage?
A: Be honest and independent. Don't coordinate with teammates.
   Small variations are normal (±5% acceptable).
   Large discrepancies trigger examiner investigation.
   Honesty better than exact matching.

================================================================================
POINTS DISTRIBUTION - VISUAL BREAKDOWN
================================================================================

TOTAL 100 POINTS:

│ 50 │███████████████████████████████          SCREENCAST VIDEO
│    │
│ 20 │████████████                              REFLECTIVE DIARY
│    │
│ 15 │█████████                                 DEMONSTRATION (Live)
│    │
│ 15 │█████████                                 JUnit Test Report
│    │
│  0 │ ──────────                              CODE + CONTRIBUTIONS
│    │  (0 pts but critical internally)

MARK DISTRIBUTION BY COMPONENT:

≥ 85 pts:  Excellent
  • JUnit (15) + Diary (20) + Demo (15) + Screencast (40+)
  • Shows comprehensive work and strong understanding

70-84 pts: Good
  • JUnit (15) + Diary (15) + Demo (12) + Screencast (30+)
  • Solid system with some gaps in presentation/diary

60-69 pts: Acceptable
  • JUnit (15) + Diary (10) + Demo (10) + Screencast (25+)
  • Working system but weak on individual reflection/quality

< 60 pts:  Below Standard
  • Missing Screencast or Demo or Diary content
  • Code issues or incomplete testing

================================================================================
WEEKLY ACTION PLAN - NEXT 2 WEEKS
================================================================================

THIS WEEK (Week of 31 March - 7 April):
  Day 1-2 (Mon-Tue):
    • Make code fully compiles (ensure all 4 test classes included)
    • Verify no runtime crashes in demo scenarios
    • Clean up any leftover test data
  
  Day 3-4 (Wed-Thu):
    • Finalize JUnit test report (130+ tests documented)
    • Group meeting: assign demo roles
    • Start Screencast recording (or schedule recording session)
  
  Day 5-6 (Fri-Sat):
    • Submit code to Canvas (by Friday 23:59 - no penalties!)
    • Do group demo run-through (time it: should be ≤ 20 min)
    • Review code for Q&A preparation
  
  Day 7 (Sun):
    • Individual: Complete contributions form
    • Individual: Start or finish reflective diary entries
    • Rest and prepare mentally for demo Monday/Tuesday

NEXT WEEK (Week of 7 April - 13 April):
  Day 1-2 (Mon-Tue):
    • Attend demonstration (8-9 April)
    • Hand in printed contributions forms
    • Celebrate! (you made it!)
  
  Day 3-4 (Wed-Thu):
    • Continue working on Screencast if not yet submitted
    • Finalize individual reflective diary if not done
    • Submit any final items to Canvas
  
  Day 5-7 (Fri-Sun):
    • Reflection on project
    • Document lessons learned
    • Prepare for any feedback from examiners

IDEAL STATUS BEFORE DEMO:
  ✅ Code submitted (7 April 23:59)
  ✅ Demo rehearsed and scripted
  ✅ All group members prepared for questions
  ✅ Screencast recorded (or near completion)
  ✅ Individual contributions forms printed
  ✅ Reflective diary entries mostly complete

================================================================================
DELIVERABLES CHECKLIST - BEFORE APRIL 9
================================================================================

GROUP SUBMISSIONS (All members submit same items):
  □ Code (.zip file submitted to Canvas by 7 April)
    └─ Includes: src/main/java, src/test/java, 130+ JUnit tests
  □ JUnit test report (included with code submission)
    └─ Includes: Test coverage document, test rationale, results
  □ Attended demonstration on 8-9 April
    └─ All group members present and on-time
  □ Screencast video (submitted to Panopto + Canvas link)
    └─ 15-30 min, all 16 use cases, professional quality

INDIVIDUAL SUBMISSIONS (Each member independently submits):
  □ Reflective Diary (5-8 weeks of entries, 20 points)
    └─ OneDrive with track changes, STAR-L framework
    └─ 300-500 words per week, includes metrics and GitHub links
  □ Contributions Form (printed, bring to demo)
    └─ Honest % assessment, detailed breakdown, GitHub evidence
  □ AI Use Statement (if applicable)
    └─ If AI was used for planning/research

FINAL VERIFICATION:
  □ Code compiles without errors
  □ All JUnit tests pass locally (130+ tests)
  □ Demo practiced and timed (<20 min)
  □ Screencast recorded and uploaded
  □ Each person has printed contributions form
  □ Each person has finalized diary entries
  □ Everyone knows which questions they'll answer
  □ Transport arranged if in-person demo

ALL ITEMS COMPLETED? → You're ready for the demonstration!

================================================================================
CONTACT & SUPPORT
================================================================================

ACADEMIC SUPPORT:
  • Course Coordinator: [Check Canvas/email]
  • Marking Rubric: See CSCU9P6 course page (Canvas)
  • Assignment Brief: CSCU9P6_Assignment_2026.pdf (Canvas)
  • Submission Portal: Canvas Course Page

TECHNICAL ISSUES:
  • Code compilation: Check Java version, classpath, imports
  • Screencast upload: Use Panopto web interface or desktop recorder
  • Canvas submission: Use "Submit Assignment" button, not comments
  • GitHub issues: Check branch protection, merge conflicts

EXTENSION POLICY:
  • Extensions NOT typically granted for group work
  • Request must be submitted before deadline with justification
  • Same lateness penalties apply (code submission especially critical!)

GROUP CONFLICT:
  • If disagreement on work distribution, document in contributions form
  • Honest assessment is better than perfect alignment
  • GitHub history shows actual work (cannot hide!)

ACADEMIC INTEGRITY:
  • All code must be your group's original work
  • Limited AI use for planning (must document)
  • No copy-pasting from other groups or internet
  • Plagiarism detection software monitors submissions

================================================================================
FINAL NOTES
================================================================================

TONE OF SUBMISSION:
  • Professional but human (be yourself, show personality in diary)
  • Demonstrate your learning journey, not just technical perfection
  • Show effort and resilience when facing challenges
  • Reflect on what you'd do differently (honesty valued)

COMMON SUCCESS FACTORS:
  1. Submit code ON TIME (protects your multi-component grade)
  2. Record thorough Screencast (50 points worth effort!)
  3. Write thoughtful diary (shows individual understanding)
  4. Prepare demo answers (design + code + process Q&A)
  5. Come prepared to demonstration (punctual, confident)

COMMON FAILURE FACTORS:
  1. Late code submission (-3 pts/day cascades to demo/screencast)
  2. Incomplete Screencast (missing use cases = significant points lost)
  3. Shallow diary (copying specifications vs reflecting on learning)
  4. Unprepared demo answers (looks like only one person did work)
  5. Poor code quality (works but messy = demo questions harder)

REMEMBER:
  • Examiners WANT you to succeed
  • Your system will have minor imperfections (that's normal)
  • Recovery from mistakes matters (professionalism counts)
  • Individual learning and reflection valued (not just marks!)
  • Group work means teamwork credit but individual honesty too

YOU'VE GOT THIS!
  ✓ Your JUnit tests are comprehensive and professional
  ✓ Your code works and you understand it deeply
  ✓ Your group has collaborated well
  ✓ You're prepared for the assignment components
  ✓ This guide has given you the roadmap to success

Now execute the plan, submit on time, and demonstrate with confidence!

================================================================================
END OF SUBMISSION GUIDE INDEX
================================================================================

Last Updated: 7 April 2024
Guide Author: Group Assignment Support Team
This document: SUBMISSION_GUIDES/README_QUICKSTART.txt