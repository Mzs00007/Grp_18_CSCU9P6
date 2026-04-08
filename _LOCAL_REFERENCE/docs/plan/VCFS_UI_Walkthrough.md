# VCFS UI Demonstration Walkthrough

Follow these literal step-by-step instructions when demonstrating the application for your screencast or final testing.

## System Startup
1. Run the application using `.\run_vcfs.bat`.
2. Notice the clean terminal startup output indicating the exact time the system is initialized and the GUI screens opening.
3. You will see 4 windows open:
   - **Admin Dashboard**
   - **Recruiter Dashboard**
   - **Candidate Dashboard**
   - **System Timer Control**

---

## Part 1: Admin Configurations (Admin Dashboard)
Navigate to the **Admin Dashboard** window. This window configures the backbone of the fair.

### 1. Configure the Timeline
*   Go to the **Configure Fair Timeline** tab.
*   **Bookings Open:** `2026-04-01T08:00`
*   **Bookings Close:** `2026-04-05T17:00`
*   **Fair Start Time:** `2026-04-10T09:00`
*   **Fair End Time:**   `2026-04-10T17:00`
*   **Action:** Click **"Set Timeline"**. 
*   *Observation:* Watch the terminal log output confirm the timeline changes and the phase set to `BEFORE_BOOKING`.

### 2. Create an Organization & Booth
*   Go to the **Organizations & Booths** tab.
*   **Create Organization:** Enter `TechNova Solutions` in the field. Click **"Create Organization"**.
*   **Create Booth:** Enter `Booth A1` and assign it to Organization `TechNova Solutions`. Click **"Create Booth"**.

### 3. Assign a Recruiter
*   **Recruiter Configuration:** Enter `Alice Jenkins` as the Recruiter Name and assign her to `Booth A1`. Click **"Assign Recruiter"**.

---

## Part 2: Recruiter Actions (Recruiter Dashboard)
Navigate to the **Recruiter Dashboard** window. *Note: Since actual login/sessions weren't deeply mocked for the UI inputs, the UI operates in a shared sandbox.*

### 1. Publish Meeting Offers
*   Go to the **Publish Offers** panel on the primary tab.
*   **Offer Details:**
    *   Title: `Software Engineering Role - Intro`
    *   Tags: `Java, Software Engineering, Backend`
    *   Max Appointments: `3`
*   **Action:** Click **"Publish Offer"**.

---

## Part 3: Candidate Actions (Candidate Dashboard)
Navigate to the **Candidate Dashboard** window. We will now submit an auto-booking request based on the tags published by the Recruiter.

Wait! The Candidate can only book meetings if the system phase is **Booking Phase**.

### 1. Advance the System Time
*   Navigate to the **System Timer Control** window.
*   Enter `1440` (1 day in minutes) and click **"Step Forward"**.
*   Look at the terminal. You should see `[SystemTimer] +1440min -> ...`. You may need to click it a couple of times until the current time crosses `2026-04-01T08:00`. The system will automatically shift its phase to `BOOKING_OPEN`.

### 2. Submit Auto-Book Request
*   Go back to the **Candidate Dashboard**, in the **Search Offers** tab.
*   **Desired Tags:** `Java, Backend`
*   **Max Appointments:** `1`
*   **Action:** Click **"Submit Auto-Book Request"**.
*   *Observation:* Behind the scenes, the `MatchEngine` automatically matches this candidate to the `Software Engineering Role - Intro` offer because of the intersecting tags!

### 3. View Candidate Schedule
*   Switch to the **My Schedule** tab in the Candidate Dashboard.
*   **Action:** Click **"Refresh Schedule"**.
*   *Observation:* The system will fetch the confirmed reservations and populate the table with the matched session.

---

## Part 4: Managing Lobbies and Live Sessions
### 1. Enter Live Fair Phase
*   Navigate back to the **System Timer Control** window.
*   Jump the time forward drastically, or type in a jump time past `2026-04-10T09:00`.
*   The phase dynamically changes to `FAIR_LIVE`. Lobbies are now open.

### 2. Accessing the Lobby
*   In the **Candidate Dashboard**, go to the **Lobby** tab.
*   **Action:** Click **Check Available Lobbies**. The available sessions configured by the Recruiter earlier are now active and ready for joining.

---

## Part 5: Console Logs & Shutdown
*   Demonstrate your command prompt. It will be full of detailed, timestamped colored `Logger` outputs detailing exactly how the MatchEngine compared the Candidate Request tags with the Recruiter Offer tags.
*   Finally, close any of the GUI windows to shut down the application cleanly.

You have successfully validated all the core OOP interactions, chronological phases, UI listeners, and the matching algorithms!
