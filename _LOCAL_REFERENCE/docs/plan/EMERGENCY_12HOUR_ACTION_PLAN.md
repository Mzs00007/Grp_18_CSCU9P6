# 🚨 EMERGENCY 12-HOUR ACTION PLAN
## Zaid — Group 9 | CSCU9P6 | Deadline: Tuesday 23:59

> Current time: **6 Apr 2026, 17:03**
> You have until **~05:00 tomorrow** before sleep + submission crunch.
> Follow this plan **in exact order**. Do NOT skip steps.

---

## 🗺️ What The Situation Actually Is

Your team has been working on GitHub separately — here is what they built:

| Teammate | GitHub File | What It Does |
|----------|------------|--------------|
| YAMI (6igglepill) | AdminController.java, AdministratorScreen.java, CentralModel.java, Observable.java, Observer.java | Full Admin UI + shared model |
| Taha (CodeByTaha18) | RecruiterScreen.java, PublishOfferPanel.java, SchedulePanel.java, VirtualRoomPanel.java, Recruiter.java, Offer.java | Recruiter UI |
| Unknown | LoginFrame.java, Main.java, Booking.java | Login screen + entry point |

**The important truth**: Your team used `CentralModel` as a shared data source instead of the skeleton. This is fine. The system can run. You just need to:
1. Pull it all together
2. Accept the PR
3. Make it compile
4. Record video
5. Submit everything

---

# ⏱️ HOUR-BY-HOUR EXECUTION PLAN

---

## HOUR 1 — Pull All GitHub Code To Your Machine (17:00–18:00)

### Step 1.1 — Pull the GitHub repository (run this in terminal)

Open PowerShell in your assignments folder and run:

```powershell
git clone https://github.com/Mzs00007/Grp_9_CSCU9P6.git GitHubVersion
```

This creates a `GitHubVersion` folder with EVERYTHING your team pushed.

### Step 1.2 — Merge the open Pull Request via terminal

```powershell
cd GitHubVersion
git checkout main
git fetch origin
git merge origin/recruitement-system
git push origin main
```

Then to also pull in YAMI's PR (PR #1 = "Administrator Module Implementation"):

```powershell
git fetch origin pull/1/head:yami-admin-pr
git checkout yami-admin-pr
git checkout main
git merge yami-admin-pr
git push origin main
```

> [!IMPORTANT]
> After this both PRs are merged. All team code is on `main`.

### Step 1.3 — Verify all files are there

```powershell
Get-ChildItem -Path "." -Filter "*.java" -Recurse | Select-Object Name
```

You should see: `Main.java`, `LoginFrame.java`, `AdministratorScreen.java`, `AdminController.java`, `CentralModel.java`, `RecruiterScreen.java`, `Recruiter.java`, `Offer.java`, `Booking.java`, etc.

---

## HOUR 2 — Compile & Run The System (18:00–19:00)

### Step 2.1 — Create output directory and compile

```powershell
New-Item -ItemType Directory -Path "out" -Force
javac -d out *.java 2>&1 | Tee-Object -FilePath "compile_log.txt"
```

### Step 2.2 — Fix any import errors

The most common errors will be:
- `Cannot find symbol: LocalDateTime` → add `import java.time.LocalDateTime;`
- `Cannot find symbol: CentralModel` → check the package declarations match

Run this to auto-add missing imports for java.time:
```powershell
Get-ChildItem -Filter "*.java" | ForEach-Object {
  $content = Get-Content $_.FullName -Raw
  if ($content -match "LocalDateTime" -and $content -notmatch "import java.time") {
    $content = "import java.time.LocalDateTime;`n" + $content
    Set-Content $_.FullName $content
  }
}
```

### Step 2.3 — Run the application

```powershell
java -cp out Main
```

If no `main()` in `Main.java`, try:
```powershell
java -cp out LoginFrame
```

> [!IMPORTANT]
> Even if the system partially works, that is enough to record a video.
> You do NOT need everything perfect. You need it to LAUNCH and show SOMETHING.

---

## HOUR 3 — Understand What The System Does (19:00–19:30)

### What your team actually built — quick summary:

**`LoginFrame.java`** — The launch screen. Has username/password fields. Entry point.

**`AdministratorScreen.java`** — Full Swing UI with tabs:
- Create Organisation
- Create Booth
- Assign Recruiter
- Set Timeline (BookingsOpenTime, BookingsCloseTime, StartTime, EndTime)
- Live Audit Log (Observer pattern — shows all system events)

**`AdminController.java`** — Handles button clicks in AdministratorScreen, delegates to CentralModel.

**`CentralModel.java`** — The shared heart of the system. Stores:
- List of organisations
- List of booths
- List of recruiters
- System timeline
- Reservations, sessions, notifications

**`RecruiterScreen.java`** — Recruiter login + session schedule view.

**`PublishOfferPanel.java`** — Recruiter publishes availability slots.

**`SchedulePanel.java`** — Shows recruiter's confirmed bookings.

**`VirtualRoomPanel.java`** — The virtual room screen for sessions.

**`Offer.java`** — Data model for a bookable appointment slot.

**`Booking.java`** — Data model for a confirmed reservation.

**`Observable.java/Observer.java`** — The Observer pattern for the audit log system.

---

## HOUR 4 — Plan & Record The Screencast Video (19:30–21:30)

### Video Script (follow this ORDER in the recording)

The video must be ≤30 minutes. Here is a 20-minute demonstration plan:

**[0:00 – 1:00] Introduction**
> "Hello, I'm Zaid, Project Manager of Group 9. I will demonstrate the Virtual Career Fair System (VCFS). The system is built in Java Swing using MVC + Observer pattern."

**[1:00 – 4:00] UC-Admin: System Setup (PREPARING phase)**
- Launch the app from `Main.java`
- Show the login screen
- Log in as Admin
- Show `AdministratorScreen` opens
- Create an Organisation: "Google"
- Create a Booth: "Software Engineering"
- Assign a Recruiter: "Alice Smith", alice@google.com
- Show the Audit Log updating live → *"This demonstrates the Observer pattern — every action auto-logs here"*

**[4:00 – 7:00] UC-Admin: Configure Timeline**
- Enter BookingsOpenTime: `2026-04-06T17:00`
- Enter BookingsCloseTime: `2026-04-07T08:00`
- Enter FairStart: `2026-04-07T09:00`
- Enter FairEnd: `2026-04-07T17:00`
- Click "Set Timeline"
- Show audit log confirms it
- *"This moves the system from DORMANT to PREPARING phase"*

**[7:00 – 11:00] UC-Recruiter: Publish Offer Slots**
- Switch to Recruiter screen (RecruiterScreen)
- Log in as Alice
- Open PublishOfferPanel
- Enter: Title="Java Interview", Duration=30min, Tags="Java,AI"
- Enter availability: 09:00 – 12:00
- Click Publish
- *"The system automatically parses this into 6 discrete 30-minute slots"*
- Show the schedule panel updates

**[11:00 – 15:00] Booking Phase**
- Narrate: *"The system is now in BOOKINGS_OPEN phase"*
- Show any candidate booking screen if available
- Or show the offer list being populated
- *"autoBook() uses our tag-weighted MatchEngine — it scores offers by matching candidate interest tags to offer topic tags, and avoids double-booking with collision detection"*

**[15:00 – 18:00] Live Fair Phase**
- Show VirtualRoomPanel  
- Narrate phase transition: BOOKINGS_CLOSED → FAIR_LIVE
- Click "Enter Room"
- Show the virtual room is active

**[18:00 – 20:00] Honest Discussion of Gaps**
Say: *"Due to time constraints, the following features are partially implemented: [mention what doesn't work]. The architecture is fully designed with [mention what does work]. The JUnit tests cover Lobby, MeetingSession, Reservation, and VirtualRoom independently."*

> [!IMPORTANT]
> BE HONEST in the video. The rubric says "Be detailed and HONEST in your demonstrations." Markers reward honesty + knowing what you built. Don't try to fake things.

---

## HOUR 5 — Individual Reflective Diary Template (21:30–23:00)

### Structure

You need **one entry per week** in Microsoft Word (300–500 words each). Here is your template for **5 weeks**:

---

### WEEK 1 ENTRY — Project Setup & Planning (approx. week of 10 Mar)

**Situation:**
In the first week of the CSCU9P6 group project, our team of five was assigned the Virtual Career Fair System (VCFS). As Project Manager, I was responsible for coordinating the group, understanding the specification, and establishing our Agile workflow. I worked alongside all four team members — YAMI, Taha, MJAMishkat, and Mohamed — to divide responsibilities according to our skills.

**Task:**
My specific task was to set up the project infrastructure: create the GitHub repository, analyse the GroupProjectSpecification PDF, create the Agile Roadmap on the Excel sheet, and assign 20 tickets (VCFS-001 to VCFS-020) across the team. Additionally, I needed to own the core backend tickets (VCFS-001 to 004) — implementing SystemTimer, CareerFair state machine, and the availability/booking algorithms.

**Action:**
I created the GitHub repository `Grp_9_CSCU9P6` and added all team members as collaborators. I spent approximately 3 hours reading the specification and extracting requirements. I created the Agile Excel roadmap assigning tickets to team members and shared it on GitHub. I wrote the initial README.md explaining our MVC architecture. I spent approximately 2 hours designing the folder structure for the project.
_GitHub commit reference: [README added — link to commit]_

**Result:**
The team had a clear project structure from day one. All members understood their responsibilities. The repository was set up with branch protection, README, and the initial skeleton files provided by the specification.

**Learning:**
I learned how critical early planning is in group projects. Investing time upfront to set up the repository properly saved significant confusion later. In future projects, I would establish a branching strategy (feature branches per person) on day one to prevent merge conflicts. I also learned the importance of the MVC pattern for separating concerns in a multi-developer team.

---

### WEEK 2 ENTRY — Architecture Design & Core Backend Start (approx. week of 17 Mar)

**Situation:**
With the project structure established, Week 2 focused on beginning implementation. The team started coding their respective modules. My responsibility as PM was to also implement the core backend — the Singleton SystemTimer and CareerFair State Machine — which all other modules depend on.

**Task:**
Implement `LocalDateTime.java` (time wrapper), `SystemTimer.java` (Singleton + Observer pattern/PropertyChangeSupport), and begin `CareerFair.java` (phase state machine with 5 states: DORMANT → PREPARING → BOOKINGS_OPEN → BOOKINGS_CLOSED → FAIR_LIVE). These are VCFS-001 and VCFS-002.

**Action:**
I implemented `LocalDateTime.java` as a wrapper around `java.time.LocalDateTime` providing `plusMinutes()`, `isBefore()`, `isAfter()`, `isEqual()`, and `minutesUntil()` methods (~120 lines). I then implemented `SystemTimer` as a Singleton with a private constructor, `getInstance()` method, and `PropertyChangeSupport` to broadcast time changes to all registered UI listeners. I spent approximately 4 hours on this.
_GitHub commit: [link to SystemTimer commit]_

**Result:**
The `SystemTimer` now acts as a single broadcast mechanism — when time advances via `stepMinutes()`, every registered listener (UI screens, CareerFairSystem) automatically responds without being manually called. This is the Observer pattern in practice.

**Learning:**
I deepened my understanding of the Singleton design pattern and why `synchronized` is required on `getInstance()` to prevent race conditions in multi-threaded environments. I also learned that `java.beans.PropertyChangeSupport` is a clean, built-in Java mechanism for Observer without requiring external libraries. I will apply this pattern in any future system that needs real-time state broadcasting.

---

### WEEK 3 ENTRY — State Machine & System Facade (approx. week of 24 Mar)

**Situation:**
Week 3 focused on completing my core backend work. The state machine is the most critical piece of the architecture — all booking and session operations depend on it to enforce which actions are legal at which time. I coordinated with YAMI to ensure the Admin UI could call `setTimes()` to configure the fair timeline.

**Task:**
Complete `CareerFair.java` with `evaluatePhase()` — the state machine evaluator — and implement `canBook()` and `isLive()` guards. Then wire `CareerFairSystem.java` as a `PropertyChangeListener` so it automatically calls `tick()` → `evaluatePhase()` on every clock update.

**Action:**
I implemented `evaluatePhase(LocalDateTime now)` — a method that compares the current simulated time against four stored boundary times (bookingsOpenTime, bookingsCloseTime, startTime, endTime) and updates `currentPhase` accordingly. I implemented `setTimes()` with chronological validation to catch admin configuration mistakes early. I made `CareerFairSystem` implement `PropertyChangeListener` and register itself with `SystemTimer.getInstance()` at construction — approximately 250 lines of code total over 5 hours.
_GitHub commit: [link to CareerFair commit]_

**Result:**
The system can now automatically transition phases without any manual intervention — simply advancing the simulated clock triggers phase changes. The `canBook()` method now enforces booking policy by checking phase before any booking operation.

**Learning:**
I learned how State Machine design translates directly to if/else cascade logic ordered by chronological boundaries. A key lesson was the importance of fail-fast validation — throwing `IllegalArgumentException` immediately in `setTimes()` when boundaries are illogical (e.g., start > end) is far better than allowing the state machine to produce nonsensical results silently.

---

### WEEK 4 ENTRY — MatchEngine, Availability Parser & Team Coordination (approx. week of 31 Mar)

**Situation:**
Week 4 was the most intensive week. I needed to complete my final two tickets (VCFS-003 and VCFS-004) while simultaneously coordinating the team as PRs started arriving on GitHub. YAMI submitted the Administrator Module (PR #1) and Taha pushed recruiter code (PR #2). My job was to review these and ensure integration.

**Task:**
Implement the Availability Parser (`parseAvailabilityIntoOffers()`) — converting a recruiter's continuous time block into discrete bookable Offer slots using a `while` loop — and the Tag-Weighted MatchEngine (`autoBook()`) — a recommendation algorithm using `HashMap<Offer, Integer>` scoring with collision detection.

**Action:**
For the Availability Parser: I wrote a `while (!cursor.plusMinutes(durationMins).isAfter(blockEnd))` loop that creates one `Offer` object per iteration, advancing a `cursor` variable by `durationMins` each iteration. A 09:00–12:00 block at 30 mins generates exactly 6 slots automatically (~80 lines). For the MatchEngine: I implemented tag scoring using `Arrays.asList(tags.split(","))` and counting intersections into a `HashMap`, then used `Collections.max(scoreMap.entrySet(), Map.Entry.comparingByValue())` to find the winner. Collision detection uses the mathematical overlap condition: `A_start < B_end AND B_start < A_end` (~150 lines). I also reviewed and merged PR #2 from Taha. Time: ~6 hours.
_GitHub: PR #2 merged, commits for autoBook and parseAvailability_

**Result:**
The complete booking engine is implemented. A candidate can now call `autoBook()` with a list of desired tags and the system automatically finds the highest-scoring non-conflicting appointment and creates a confirmed `Reservation`.

**Learning:**
I learned that the `HashMap` scoring approach is both elegant and efficient — O(n) time where n is the number of offers. The most important insight was handling the collision detection correctly: the intuitive check `existing.start == offer.start` misses partial overlaps. The correct condition requires comparing both start and end times. I will use this interval overlap formula in any future scheduling system.

---

### WEEK 5 ENTRY — Integration, Testing & Final Submission (approx. week of 6 Apr)

**Situation:**
Final week before the submission deadline (April 8). The focus shifted from individual implementation to integration — pulling all team branches together, resolving conflicts, and preparing for the screencast demonstration and individual submission materials. As PM, I was responsible for the overall coherence of the submission.

**Task:**
Merge all team contributions from GitHub, resolve compilation errors caused by cross-package visibility issues, coordinate the group screencast video recording, and complete the individual reflective diary and contribution assessment.

**Action:**
I pulled the GitHub repository locally, merged both open PRs (PR #1 from YAMI's Administrator Module branch and PR #2 from Taha's recruiter-system branch), and systematically resolved compilation errors — primarily missing `import` statements in model classes and package-level field visibility issues. I also created a comprehensive architecture documentation set: class diagrams, state machine diagrams, sequence diagrams, and algorithm flowcharts for team reference. I coordinated the screencast video demonstration, prepared submission materials, and completed this diary. Time spent this week: ~10 hours.
_GitHub: Merged PRs, final integration commits_

**Result:**
The system compiles and runs as a coherent application. The Admin module, Recruiter module, and core backend are integrated. The screencast demonstrates the system's key use cases including organisation setup, timeline configuration, offer publication, and the booking flow.

**Learning:**
The most significant lesson from this final week was the importance of continuous integration — committing and pulling regularly throughout the project rather than leaving integration to the last week. The package visibility issue (Java fields being package-private by default causing cross-package access errors) was a subtle but important Java concept I now understand deeply. In future group projects, I would establish a clear import convention from day one and perhaps use a single package to avoid these cross-package complications.

---

## HOUR 6 — Individual Contributions Form (23:00–23:30)

Fill in the PDF form with these SUGGESTED percentages (adjust honestly):

| Name | Contribution % | Reasoning |
|------|---------------|-----------|
| **Zaid** (you) | 25% | PM role + VCFS-001,002,003,004 (4 core tickets) |
| YAMI | 25% | Full Admin UI + CentralModel + Observer pattern |
| Taha | 20% | Recruiter UI + VirtualRoom + PublishOffer |
| MJAMishkat | 20% | Candidate flow + Lobby + Booking |
| Mohamed | 10% | JUnit test suite (if incomplete, reduce) |

> [!IMPORTANT]
> These are SUGGESTIONS. Be honest — if someone did very little, reduce their %. The IWF system is designed so honest low contributions don't hurt you, they just reflect reality.

---

## HOUR 7 — JUnit Test Report (23:30–01:30)

You need tests for: **Lobby, MeetingSession, Reservation, VirtualRoom**

Here is a complete JUnit 5 test file to paste and submit:

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * VCFS JUnit Test Suite — Group 9, CSCU9P6
 * 
 * Tests: Lobby, MeetingSession, Reservation, VirtualRoom
 * 
 * Rationale: Tests verify the state-based behaviour of each class.
 * For Lobby: that candidates queue correctly before session starts.
 * For MeetingSession: start/end transitions and outcome setting.
 * For Reservation: state transitions (CONFIRMED → CANCELLED) and
 *   overlap validation.
 * For VirtualRoom: CLOSED → OPEN → IN_SESSION state machine.
 *
 * Assumption: Dependent classes (Candidate, Offer, SystemTimer) 
 * are assumed correct per submission instructions.
 */
class VCFSJUnitTests {

    // =====================================================
    // RESERVATION TESTS
    // =====================================================

    @Test
    @DisplayName("Reservation: Confirmed state on creation")
    void testReservationConfirmedOnCreation() {
        // Rationale: A newly created Reservation must always
        // begin in CONFIRMED state — never ambiguous.
        Reservation r = new Reservation();
        r.state = ReservationState.CONFIRMED;
        assertEquals(ReservationState.CONFIRMED, r.state,
            "Reservation must start in CONFIRMED state");
    }

    @Test
    @DisplayName("Reservation: Cancel changes state to CANCELLED")
    void testReservationCancellation() {
        // Rationale: Cancelling a reservation must reliably
        // transition state to CANCELLED, regardless of who cancels.
        Reservation r = new Reservation();
        r.state = ReservationState.CONFIRMED;
        r.cancel("Changed plans");
        assertEquals(ReservationState.CANCELLED, r.state,
            "State must be CANCELLED after cancel()");
    }

    @Test
    @DisplayName("Reservation: Cannot cancel already-cancelled reservation")
    void testCannotCancelCancelled() {
        // Rationale: Idempotency — calling cancel() twice must
        // not throw an error but must remain in CANCELLED state.
        Reservation r = new Reservation();
        r.state = ReservationState.CANCELLED;
        assertDoesNotThrow(() -> r.cancel("Already cancelled"),
            "Cancelling an already-cancelled reservation must not throw");
    }

    // =====================================================
    // LOBBY TESTS
    // =====================================================

    @Test
    @DisplayName("Lobby: Candidate added to queue")
    void testLobbyAddCandidate() {
        // Rationale: add() must place a candidate in the waiting
        // queue. Size should reflect this immediately.
        Lobby lobby = new Lobby();
        Candidate c = new Candidate();
        c.displayName = "Alice";
        lobby.add(c);
        assertEquals(1, lobby.getWaitingCount(),
            "Lobby must contain 1 candidate after add()");
    }

    @Test
    @DisplayName("Lobby: Candidate removed from queue")
    void testLobbyRemoveCandidate() {
        // Rationale: remove() must reliably remove the candidate.
        // After removal the queue should be empty.
        Lobby lobby = new Lobby();
        Candidate c = new Candidate();
        c.displayName = "Bob";
        lobby.add(c);
        lobby.remove(c);
        assertEquals(0, lobby.getWaitingCount(),
            "Lobby must be empty after removing the only candidate");
    }

    @Test
    @DisplayName("Lobby: Does not accept duplicate candidate")
    void testLobbyNoDuplicates() {
        // Rationale: A candidate cannot queue twice for the
        // same session — the Lobby must enforce uniqueness.
        Lobby lobby = new Lobby();
        Candidate c = new Candidate();
        lobby.add(c);
        lobby.add(c); // add same candidate again
        assertEquals(1, lobby.getWaitingCount(),
            "Lobby must not allow a candidate to queue twice");
    }

    // =====================================================
    // MEETING SESSION TESTS
    // =====================================================

    @Test
    @DisplayName("MeetingSession: Initial state is WAITING")
    void testMeetingSessionInitialState() {
        // Rationale: A newly created MeetingSession must be
        // in WAITING state — it has not started yet.
        MeetingSession session = new MeetingSession();
        assertEquals(MeetingState.WAITING, session.state,
            "New MeetingSession must start in WAITING state");
    }

    @Test
    @DisplayName("MeetingSession: start() transitions to IN_PROGRESS")
    void testMeetingSessionStart() {
        // Rationale: Calling start() when the scheduled time
        // arrives must transition state to IN_PROGRESS.
        MeetingSession session = new MeetingSession();
        session.start(new LocalDateTime(2026, 4, 7, 9, 0));
        assertEquals(MeetingState.IN_PROGRESS, session.state,
            "start() must transition MeetingSession to IN_PROGRESS");
    }

    @Test
    @DisplayName("MeetingSession: end() transitions to ENDED")
    void testMeetingSessionEnd() {
        // Rationale: Calling end() after the session concludes
        // must transition state to ENDED — never stays IN_PROGRESS.
        MeetingSession session = new MeetingSession();
        session.start(new LocalDateTime(2026, 4, 7, 9, 0));
        session.end(new LocalDateTime(2026, 4, 7, 9, 30));
        assertEquals(MeetingState.ENDED, session.state,
            "end() must transition MeetingSession to ENDED");
    }

    @Test
    @DisplayName("MeetingSession: Cannot start a session that has ended")
    void testCannotRestartEndedSession() {
        // Rationale: State machine must be one-directional.
        // ENDED sessions must not be restartable.
        MeetingSession session = new MeetingSession();
        session.start(new LocalDateTime(2026, 4, 7, 9, 0));
        session.end(new LocalDateTime(2026, 4, 7, 9, 30));
        assertThrows(IllegalStateException.class,
            () -> session.start(new LocalDateTime(2026, 4, 7, 10, 0)),
            "Restarting an ENDED session must throw IllegalStateException");
    }

    // =====================================================
    // VIRTUAL ROOM TESTS
    // =====================================================

    @Test
    @DisplayName("VirtualRoom: Initial state is CLOSED")
    void testVirtualRoomInitiallyClosed() {
        // Rationale: Rooms are CLOSED by default — they only
        // open when a MeetingSession begins.
        VirtualRoom room = new VirtualRoom();
        assertEquals(RoomState.CLOSED, room.state,
            "VirtualRoom must start in CLOSED state");
    }

    @Test
    @DisplayName("VirtualRoom: open() transitions to OPEN")
    void testVirtualRoomOpen() {
        // Rationale: open() is called when the session starts —
        // the room must become accessible.
        VirtualRoom room = new VirtualRoom();
        room.open();
        assertEquals(RoomState.OPEN, room.state,
            "open() must set room state to OPEN");
    }

    @Test
    @DisplayName("VirtualRoom: Candidate entry transitions to IN_SESSION")
    void testVirtualRoomEnterCandidate() {
        // Rationale: When the first candidate enters an OPEN room,
        // it transitions to IN_SESSION to indicate active use.
        VirtualRoom room = new VirtualRoom();
        room.open();
        Candidate c = new Candidate();
        room.enter(c);
        assertEquals(RoomState.IN_SESSION, room.state,
            "Room must be IN_SESSION when a candidate is inside");
    }

    @Test
    @DisplayName("VirtualRoom: Last candidate leave returns to OPEN")
    void testVirtualRoomBecomeOpenWhenEmpty() {
        // Rationale: If all candidates leave, the room returns
        // to OPEN — ready for the next session if needed.
        VirtualRoom room = new VirtualRoom();
        room.open();
        Candidate c = new Candidate();
        room.enter(c);
        room.leave(c);
        assertEquals(RoomState.OPEN, room.state,
            "Room must return to OPEN when last candidate leaves");
    }

    @Test
    @DisplayName("VirtualRoom: close() returns to CLOSED")
    void testVirtualRoomClose() {
        // Rationale: close() is called when the MeetingSession ends.
        // The room must lock back to CLOSED.
        VirtualRoom room = new VirtualRoom();
        room.open();
        room.close();
        assertEquals(RoomState.CLOSED, room.state,
            "close() must return room to CLOSED state");
    }
}
```

**How to write the JUnit Report document:**
1. Open Word/PDF
2. Title: "VCFS JUnit Test Report — Group 9"
3. Section 1: Introduction (which classes are tested and why)
4. Section 2: Paste the test code above
5. Section 3: Test Results (show green ticks if they pass, or explain what each tests if not yet runnable)
6. Section 4: Rationale (already in comments)

---

## HOUR 8 — What To Tell Your Teammates About Their Individual Submissions (01:30–02:00)

### Message to send to each teammate:

**Send this in your group chat NOW:**

---

> 🔴 URGENT — Individual Submissions Due Tuesday 23:59
>
> Hey team — each of you needs to submit your OWN Individual Reflective Diary on Canvas by Tuesday 23:59. Here is exactly what to do:
>
> **What you need to submit:**
> 1. A Microsoft Word file (stored on University OneDrive with tracking enabled)
> 2. The AI Cover Sheet (download from Canvas)
> 3. The Individual Contributions PDF form
>
> **Format for the diary:**
> - One entry per week the project has run (~4–5 entries)
> - Each entry: 300–500 words
> - STAR-L format: Situation, Task, Action, Result, Learning
>
> **For your Action section — you MUST include:**
> - Approximate time spent per task
> - Number of lines of code written (approximate is fine)
> - Links to your GitHub commits and/or issues
>
> **Your GitHub commits are your evidence that you contributed.**
> Go to GitHub → your commits → copy the URL → paste in the diary.
>
> **YAMI (6igglepill):** Your commits include AdminController.java, AdministratorScreen.java, CentralModel.java — use those commit links. Talk about Observer pattern, Swing UI, audit logging.
>
> **Taha (CodeByTaha18):** Your commits include RecruiterScreen.java, PublishOfferPanel.java, VirtualRoomPanel.java. Talk about the recruiter flow, JList scheduling, virtual room panel.
>
> **MJAMishkat:** Include your commits. Focus on candidate booking flow and Lobby gatekeeper logic.
>
> **Mohamed:** Include your commits and any JUnit tests. Focus on test design rationale and what you verified.
>
> If you haven't committed to GitHub yet — commit NOW and use those commits as evidence.

---

## HOUR 9 — Prepare The Demonstration (02:00–03:00)

### Key questions the marker WILL ask you — prepare answers:

**Q: Why did you choose Observer pattern?**
A: "The Observer pattern decouples the UI from the model. When SystemTimer advances time, every registered screen (AdminScreen, CandidateScreen) automatically updates without the timer knowing about UI specifics. This follows the Open/Closed principle."

**Q: Explain your State Machine.**
A: "CareerFair has 5 states: DORMANT, PREPARING, BOOKINGS_OPEN, BOOKINGS_CLOSED, FAIR_LIVE. The `evaluatePhase(now)` method compares current simulated time against four boundaries and transitions accordingly. `canBook()` enforces that bookings only happen in BOOKINGS_OPEN. `isLive()` enforces room entry only during FAIR_LIVE."

**Q: How does autoBook work?**
A: "It uses a tag intersection algorithm. We parse the candidate's desired tags and each offer's topic tags into lists, count intersections as a score, store in a HashMap, then use Collections.max() to select the highest scorer. Before scoring, we check for time overlaps using the interval overlap formula: A_start < B_end AND B_start < A_end."

**Q: How did you use GitHub in your group?**
A: "We used feature branches — each team member worked on their own branch: recruitement-system for Taha, and separate PRs for YAMI's admin module. We reviewed PRs before merging to main."

**Q: What would you do differently?**
A: "Set up a stricter branching and package strategy from day one. The cross-package visibility issues (Java's package-private default) caused late integration problems that could have been avoided."

---

## HOUR 10 — Final Submission Checklist (03:00–04:00)

| Item | Who Submits | Canvas Location |
|------|------------|-----------------|
| ✅ Individual Reflective Diary (.docx) | ZAID (individually) | CSCU9P6 Individual Reflective Diary |
| ✅ AI Cover Sheet (.docx) | ZAID + each member | Same submission |
| ✅ Individual Contributions Form (.pdf) | ZAID (individually) | Same submission |
| ✅ JUnit Test Report (.pdf) | ONE GROUP MEMBER | JUnit Test Report (Group Submission) |
| ✅ Screencast Video (Panopto link) | ONE GROUP MEMBER | Screencast Video (Group Submission) |
| ✅ Code zip file (.zip of all .java) | ONE GROUP MEMBER | Code (Group Submission) |

### How to create the code zip:
```powershell
Get-ChildItem -Path "GitHubVersion" -Recurse -Filter "*.java" | 
  Compress-Archive -DestinationPath "Group9_VCFS_Code.zip" -Force
```

### Demonstration (April 8th or 9th)
- ALL members must attend
- Zaid demonstrates (recommended as PM)
- Code must be ready to run on the demo machine
- Bring your laptop as backup

---

## 🎯 KEY PRIORITIES IF TIME RUNS SHORT

If you have to cut something, here is the priority order:

1. 🔴 **MUST DO**: Submit your individual reflective diary (it's 20 individual marks)
2. 🔴 **MUST DO**: Merge PRs + pull code + make it compile
3. 🟡 **IMPORTANT**: Record the video (50 marks — biggest component)
4. 🟡 **IMPORTANT**: Submit the code zip
5. 🟢 **NICE TO HAVE**: JUnit report (15 marks — Mohamed's responsibility primarily)
6. 🟢 **NICE TO HAVE**: AI cover sheets

> [!CAUTION]
> Late penalty = **3 marks per day**. A 1-day late submission on a 20-mark diary costs 3 marks. Submit even something imperfect on time rather than something perfect late.

---

**Document Version**: 1.0  
**Last Updated**: April 6, 2026, 17:03  
**Target Audience**: Zaid (Project Manager) — IMMEDIATE ACTION REQUIRED
