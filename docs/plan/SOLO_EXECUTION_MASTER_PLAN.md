# 🚨 EMERGENCY SOLO EXECUTION PLAN
**Project:** Virtual Career Fair System (VCFS) - Group 9  
**Sole Developer / PM:** Zaid  
**Objective:** Since all team members (YAMI, Taha, MJAMishkat, Mohamed) are offline due to network issues, Zaid will assume 100% ownership of all remaining technical tickets to ensure the highest quality submission.

---

## 📊 1. CURRENT SYSTEM STATE
The **Core Architecture** and **Backend Logic** are 100% complete and compiling flawlessly:
- `CareerFairSystem.java` fully functional (algorithms, singletons, observer pattern).
- `JUnit` test suite contains 68 tests that all PASS.
- The `App.java` properly instantiates the MVC layers.

**What is Missing?**
The frontend Java Swing Graphical User Interfaces (GUIs) assigned to the team are currently just stubs or incomplete panels. They must be fully built out and wired to the controllers.

---

## 📋 2. SOLO TAKEOVER TICKETS (The Work Zaid Must Do)

### A. YAMI's Tickets (Admin UI & Logging)
- [ ] **VCFS-005**: Construct `AdministratorScreen` using Java Swing `GridBagLayout`. Needs proper text fields for Org/Booth creation.
- [ ] **VCFS-006**: Wire up the "Reset System" data purging routine to the UI button.
- [ ] **VCFS-007**: Develop the System Time Boundary Injector (the 4 date/time fields to configure Fair phases).
- [ ] **VCFS-008**: Implement a live, auto-scrolling `JTextArea` Audit Log in the Admin UI that listens to the Observer.

### B. Taha's Tickets (Recruiter UI & Virtual Room)
- [ ] **VCFS-009**: Build the dynamic "Publish Offer" creation form for recruiters (Inputs: title, duration, tags, capacity).
- [ ] **VCFS-010**: Implement a `JList` with `DefaultListModel` for the dynamic Schedule View.
- [ ] **VCFS-011**: Implement `CardLayout` swapping between the Schedule View and the live Virtual Room View.
- [ ] **VCFS-012**: Add post-session "Attendance Outcome" buttons (ATTENDED, NO_SHOW) in the Virtual Room UI.

### C. MJAMishkat's Tickets (Candidate UI & Lobby)
- [ ] **VCFS-013**: Build the Candidate Dashboard with a tabbed pane (Search Offers, My Schedule, Lobby).
- [ ] **VCFS-014**: Create the "Auto-Book" request form (Inputs: desired tags, max appointments) and wire it to `CandidateController.submitMeetingRequest`.
- [ ] **VCFS-015**: Display a dynamic table of Confirmed Reservations.
- [ ] **VCFS-016**: Build the "Lobby Waiting Room" view showing candidates currently queued for a session.

### D. Mohamed's Tickets (Testing Integration)
- [ ] **VCFS-017**: Since core JUnit tests are complete, create a final `run_vcfs.bat` (COMPLETED) to seamlessly compile and launch the GUI for the final demonstration video.

---

## 🚀 3. IMMEDIATE EXECUTION STRATEGY

1. **Phase 1: Controller Wiring (1 Hour)**
   - Add targeted `TODO` markers in `AdminScreenController`, `RecruiterController`, and `CandidateController` where the Swing UI actions must trigger the `CareerFairSystem` backend methods.
   
2. **Phase 2: UI Construction (3 Hours)**
   - Use `JFrame`, `JPanel`, `JButton`, and `JTextField` to rapidly build the visual forms. 
   - *Tip:* Keep layouts simple. Use `BorderLayout` and `GridLayout` to save time instead of complex `GridBagLayout` where possible.
   
3. **Phase 3: Event Listeners (2 Hours)**
   - Attach `ActionListener` to every button to read the text fields and pass the strings/integers to the respective Controller.
   - Show `JOptionPane.showMessageDialog` on success or error (using `try/catch` blocks).

4. **Phase 4: Final Demonstration (1 Hour)**
   - Run `run_vcfs.bat`.
   - Record the 25-minute screencast demonstrating the full, end-to-end user journey across all three screens.

---

## ⚠️ 4. CRITICAL SUCCESS FACTORS
- **Do not alter the Core (`src/main/java/vcfs/core/`)**. It works perfectly. Only touch `views/` and `controllers/`.
- Ensure all GUI updates run on the Event Dispatch Thread (handled in `App.java`).
- When UI throws an exception (e.g. invalid date format), catch it in the View and display an error popup rather than crashing the system.

*Zaid, you built the foundation. You have the `run_vcfs.bat` ready. Just build the Swing forms and you will secure the A grade.*