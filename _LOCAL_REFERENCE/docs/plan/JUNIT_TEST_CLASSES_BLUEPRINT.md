# JUnit Test Classes Blueprint for VCFS
## Mohamed's QA & Testing Responsibility

**Document Version**: 1.0  
**Author**: Group 9 QA Lead (Mohamed)  
**Date**: April 7, 2026  
**Status**: Ready for Implementation

---

## 📋 COMPLETE TEST CLASS INVENTORY

**Total Test Classes to Create**: 32 JUnit test classes  
**Total Test Methods**: ~250+ test cases  
**Coverage Target**: >80% code coverage

---

# SECTION 1: CORE PACKAGE TESTS
## `vcfs.core` - Testing System Foundation

### 1️⃣ **LocalDateTimeTest.java**

**Location**: `src/test/java/vcfs/core/LocalDateTimeTest.java`

**Purpose**: 
Validate immutable time wrapper behavior. LocalDateTime is critical because it ensures time values never accidentally mutate, preventing subtle bugs in scheduling and booking algorithms.

**Relates To**:
- `vcfs.core.LocalDateTime.java` (primary)
- SystemTimer (uses LocalDateTime)
- CareerFairSystem (uses LocalDateTime for scheduling)

**Class Responsibility**: IMMUTABLE TIME WRAPPER
- Immutable means once created, values cannot change
- Essential for thread-safe time operations
- Used throughout system for date/time handling

**Test Methods** (15 test cases):
```
✓ testConstructor_ValidInputs()
  - Tests creation with valid year, month, day, hour, minute
  - Verifies fields are correctly stored

✓ testPlusMinutes_AddsCorrectly()
  - Test: plus(30) to 08:30 = 09:00
  - Verifies minute arithmetic without mutating original
  - Original should remain unchanged (immutability check)

✓ testPlusMinutes_CarriesOverToHours()
  - Test: 08:45 plus(30) = 09:15
  - Verifies hour boundary condition

✓ testPlusMinutes_CarriesOverToDays()
  - Test: 23:45 plus(30) = 00:15 next day
  - Verifies day boundary condition

✓ testPlusMinutes_NegativeMinutes()
  - Test: plus(-30) going backward
  - Verifies negative minute handling

✓ testIsBefore_CorrectComparison()
  - Tests 08:00 is before 09:00
  - Verifies comparison logic

✓ testIsBefore_SameTime()
  - Tests 08:00 is NOT before 08:00
  - Edge case: same time

✓ testIsAfter_CorrectComparison()
  - Tests 09:00 is after 08:00
  - Verifies comparison logic

✓ testIsEqual_CorrectComparison()
  - Tests 08:00 equals 08:00
  - Verifies equality logic

✓ testMinutesUntil_CorrectCalculation()
  - Tests 08:00 to 08:30 = 30 minutes
  - Verifies time delta calculation

✓ testMinutesUntil_Across24Hours()
  - Tests 23:00 to 01:00 = 120 minutes next day
  - Verifies cross-day calculation

✓ testToString_FormattedCorrectly()
  - Tests "2026-04-07 08:30:00" format
  - Verifies human-readable output

✓ testEquals_SameTimes()
  - Tests LocalDateTime(2026, 4, 7, 8, 30).equals(LocalDateTime(2026, 4, 7, 8, 30))
  - Verifies equals contract

✓ testEquals_DifferentTimes()
  - Tests LocalDateTime objects with different times are not equal
  - Verifies inequality

✓ testHashCode_ConsistentWithEquals()
  - If two LocalDateTimes are equal, hashCode must be equal
  - Allows use in HashMap/HashSet
```

**Test Data**:
```java
LocalDateTime testTime1 = new LocalDateTime(2026, 4, 7, 8, 30, 0);
LocalDateTime testTime2 = new LocalDateTime(2026, 4, 7, 9, 30, 0);
LocalDateTime testTime3 = new LocalDateTime(2026, 4, 8, 8, 30, 0);
```

**Key Assertions**:
- `assertTrue(time1.isBefore(time2))`
- `assertFalse(time1.equals(time2))`
- `assertEquals(30, time1.minutesUntil(time2))`
- `assertEquals(new LocalDateTime(...), time1.plusMinutes(30))`

---

### 2️⃣ **SystemTimerTest.java**

**Location**: `src/test/java/vcfs/core/SystemTimerTest.java`

**Purpose**: 
Verify singleton pattern, observer notification, and timing logic. SystemTimer is the "heartbeat" of the fair - every action depends on knowing the current time.

**Relates To**:
- `vcfs.core.SystemTimer.java` (primary)
- `vcfs.core.LocalDateTime.java` (uses for time storage)
- AdminScreen (listens to timer updates)
- CareerFair (lifecycle depends on timer)

**Class Responsibility**: SINGLETON OBSERVER CLOCK
- Only one instance exists (singleton pattern)
- Notifies listeners when time changes
- Provides jump/step time operations

**Test Methods** (18 test cases):
```
✓ testGetInstance_ReturnsSameInstance()
  - Call getInstance() twice
  - Verify both return the EXACT same object (reference equality)
  - Assert object1 == object2 (not just equals, but same reference)

✓ testGetInstance_ThreadSafe()
  - Create 100 threads calling getInstance() simultaneously
  - Verify all threads get the same singleton instance
  - Tests thread safety of synchronized getInstance()

✓ testStepMinute_AdvancesTimeCorrectly()
  - Set timer to 08:00
  - Call stepMinute()
  - Verify timer now reads 08:01

✓ testStepMinute_NotifiesListeners()
  - Create PropertyChangeListener and add to timer
  - Call stepMinute()
  - Verify listener.propertyChange() was called
  - Verify event contains property name "time"

✓ testStepMinute_MultipleSteps()
  - Start at 08:00
  - Step 60 times
  - Verify time is now 09:00
  - Verify listener called 60 times

✓ testJumpTo_SetsTimeDirectly()
  - Call jumpTo(new LocalDateTime(2026, 4, 7, 14, 30, 0))
  - Verify timer time is exactly 14:30

✓ testJumpTo_NotifiesListeners()
  - Create listener, add to timer
  - Call jumpTo()
  - Verify listener notified

✓ testJumpTo_LargeTimeJump()
  - Jump from 08:00 to 22:00 (14 hours forward)
  - Verify time is correct
  - Verify no off-by-one errors

✓ testCurrentTime_ReturnsCorrectValue()
  - Set time to 08:30
  - Call getCurrentTime()
  - Verify returns 08:30

✓ testAddPropertyChangeListener_ListenerAdded()
  - Create listener
  - Call addPropertyChangeListener(listener)
  - Verify listener is in internal list

✓ testRemovePropertyChangeListener_ListenerRemoved()
  - Add listener, then remove it
  - Call stepMinute()
  - Verify listener did NOT receive notification

✓ testPropertyChangeEvent_ContainsCorrectData()
  - Step minute and capture event
  - Verify event.oldValue = previous time
  - Verify event.newValue = new time
  - Verify event.propertyName = "time"

✓ testMultipleListeners_AllNotified()
  - Add 5 listeners
  - Step minute
  - Verify all 5 listeners called

✓ testListener_ReceivesTimeValues()
  - Listener captures newValue from event
  - Verify newValue is LocalDateTime instance
  - Verify newValue has correct time

✓ testStepMinute_24HourWraparound()
  - Set time to 23:59
  - Step minute
  - Verify time is 00:00 next day (if 24-hour wrapping implemented)

✓ testConcurrentStepAndJump_Consistent()
  - Thread A steps minute
  - Thread B jumps to different time
  - Verify final state is consistent (no race conditions)

✓ testListenerException_DoesNotBlockOthers()
  - Add listener1 that throws exception
  - Add listener2 that works fine
  - Step minute
  - Verify listener2 still called (exception in listener1 doesn't block others)

✓ testResetNotImplemented_OrThrowsException()
  - Verify system behavior for reset (if supported)
  - Or verify no unintended reset behavior
```

**Test Data**:
```java
SystemTimer timer = SystemTimer.getInstance();
LocalDateTime baseTime = new LocalDateTime(2026, 4, 7, 8, 0, 0);
timer.jumpTo(baseTime);
```

**Mock Objects**:
```java
// PropertyChangeListener mock
PropertyChangeListener mockListener = new PropertyChangeListener() {
    int callCount = 0;
    PropertyChangeEvent lastEvent = null;
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        callCount++;
        lastEvent = evt;
    }
};
```

**Key Assertions**:
- `assertSame(SystemTimer.getInstance(), SystemTimer.getInstance())`
- `assertEquals(expectedTime, timer.getCurrentTime())`
- `assertEquals(1, mockListener.callCount)`
- `assertEquals("time", mockListener.lastEvent.getPropertyName())`

---

### 3️⃣ **CareerFairTest.java**

**Location**: `src/test/java/vcfs/core/CareerFairTest.java`

**Purpose**: 
Test the 5-phase state machine that controls the entire fair lifecycle. This is CRITICAL because the entire system behavior changes based on what phase we're in.

**Relates To**:
- `vcfs.core.CareerFair.java` (primary)
- `vcfs.models.enums.FairPhase.java` (DORMANT, PREPARING, BOOKINGS_OPEN, BOOKINGS_CLOSED, FAIR_LIVE)
- `vcfs.core.SystemTimer` (transitions based on time)
- All booking/offering logic (depends on phase)

**Class Responsibility**: 5-PHASE STATE MACHINE
```
DORMANT 
  ↓ (setupTime arrives)
PREPARING 
  ↓ (bookingsOpenTime arrives)
BOOKINGS_OPEN 
  ↓ (bookingsCloseTime arrives)
BOOKINGS_CLOSED 
  ↓ (fairStartTime arrives)
FAIR_LIVE
```

**Test Methods** (20 test cases):
```
✓ testConstructor_InitializesDormant()
  - Create new CareerFair
  - Verify phase is DORMANT
  - Verify all times are set

✓ testSetup_TransitionsToPreparing()
  - Create CareerFair in DORMANT
  - Call setup()
  - Verify phase changed to PREPARING
  - Verify setupTime stored correctly

✓ testOpenBookings_TransitionsToBookingsOpen()
  - State: PREPARING
  - Call openBookings()
  - Verify phase is BOOKINGS_OPEN
  - Verify bookingsOpenTime stored

✓ testCloseBookings_TransitionsToBookingsClosed()
  - State: BOOKINGS_OPEN
  - Call closeBookings()
  - Verify phase is BOOKINGS_CLOSED
  - Verify bookingsCloseTime stored

✓ testStartFair_TransitionsToFairLive()
  - State: BOOKINGS_CLOSED
  - Call startFair()
  - Verify phase is FAIR_LIVE
  - Verify fairStartTime stored

✓ testInvalidTransition_DormantToBookingsOpen()
  - State: DORMANT
  - Call closeBookings() (invalid - must go through PREPARING first)
  - Verify phase remains DORMANT (rejected)
  - Verify exception thrown or no-op behavior

✓ testCannotGoBackward_BookingsOpenToDormant()
  - State: BOOKINGS_OPEN
  - Attempt to set phase back to DORMANT (invalid)
  - Verify phase unchanged
  - Verify business rule enforced

✓ testGetCurrentPhase_ReturnsCorrectPhase()
  - Set phase to BOOKINGS_OPEN
  - Call getCurrentPhase()
  - Verify returns BOOKINGS_OPEN

✓ testIsPhase_ChecksCurrentPhase()
  - Phase is BOOKINGS_OPEN
  - Verify isPhase(FairPhase.BOOKINGS_OPEN) = true
  - Verify isPhase(FairPhase.FAIR_LIVE) = false

✓ testSetupTimesValid_AllTimesStored()
  - Call setup with times:
    - Setup: 08:00
    - BookingsOpen: 09:00
    - BookingsClose: 10:00
    - FairStart: 11:00
  - Verify all times correctly stored

✓ testSetupTimes_NotInChronological()
  - Try to set bookingsCloseTime BEFORE bookingsOpenTime (invalid)
  - Verify exception or rejection
  - Verify state unchanged

✓ testTransitionSequence_FullLifecycle()
  - DORMANT → setup() → PREPARING
  - → openBookings() → BOOKINGS_OPEN
  - → closeBookings() → BOOKINGS_CLOSED
  - → startFair() → FAIR_LIVE
  - Verify each transition succeeds

✓ testGetSetupTime_ReturnsStoredTime()
  - Set setupTime to 08:00
  - Get setupTime
  - Verify 08:00 returned

✓ testGetBookingsOpenTime_ReturnsStoredTime()
  - Set bookingsOpenTime to 09:00
  - Verify retrieval

✓ testGetBookingsCloseTime_ReturnsStoredTime()
  - Set bookingsCloseTime to 10:00
  - Verify retrieval

✓ testGetFairStartTime_ReturnsStoredTime()
  - Set fairStartTime to 11:00
  - Verify retrieval

✓ testEarlyTransition_BookingsOpenBeforeSetup()
  - In DORMANT phase
  - Call openBookings() without calling setup() first
  - Verify exception or state guard prevents transition

✓ testSetupPhase_CanBeCalledOnce()
  - In DORMANT
  - Call setup() twice
  - Verify second call rejected (already set up)
  - Or allow re-setup and verify state correct

✓ testToString_FormattedCorrectly()
  - ToString should show: "Fair [PHASE: BOOKINGS_OPEN, setupTime: ..., ...]"
  - Verify human-readable output

✓ testIsDuring_ChecksTimeRange()
  - Fair PREPARING from 08:00 to 09:00
  - Call isDuring(08:30)?
  - Verify returns true
  - Call isDuring(10:00)?
  - Verify returns false
```

**Test Data**:
```java
CareerFair fair = new CareerFair();
LocalDateTime setup = new LocalDateTime(2026, 4, 7, 8, 0, 0);
LocalDateTime bookOpen = new LocalDateTime(2026, 4, 7, 9, 0, 0);
LocalDateTime bookClose = new LocalDateTime(2026, 4, 7, 10, 0, 0);
LocalDateTime fairStart = new LocalDateTime(2026, 4, 7, 11, 0, 0);
```

**Key Assertions**:
- `assertEquals(FairPhase.DORMANT, fair.getCurrentPhase())`
- `assertTrue(fair.isPhase(FairPhase.PREPARING))`
- `assertEquals(setup, fair.getSetupTime())`
- `assertTrue(fair.isDuring(testTime))`

---

### 4️⃣ **CareerFairSystemTest.java**

**Location**: `src/test/java/vcfs/core/CareerFairSystemTest.java`

**Purpose**: 
Test the main system facade and two critical algorithms:
- VCFS-003: parseAvailabilityIntoOffers (parses recruiter availability into structured offers)
- VCFS-004: autoBook (automatically books candidates into available slots)

**Relates To**:
- `vcfs.core.CareerFairSystem.java` (primary)
- `vcfs.core.CareerFair.java` (manages fair state)
- `vcfs.core.SystemTimer` (provides current time)
- All model classes (Offer, Reservation, Candidate, Recruiter, etc.)

**Class Responsibility**: SYSTEM ORCHESTRATION + BOOKING ALGORITHMS
- Central point for all system operations
- Parses availability strings into Offer objects
- Automatically matches candidates to available recruiter slots
- Maintains consistency between all system components

**Test Methods** (25 test cases):

```
=== INITIALIZATION TESTS ===

✓ testConstructor_InitializesCorrectly()
  - Create CareerFairSystem()
  - Verify careerFair initialized
  - Verify systemTimer loaded
  - Verify internal collections exist

✓ testGetCareerFair_ReturnsInitializedFair()
  - Call getCareerFair()
  - Verify returns non-null CareerFair instance

✓ testGetSystemTimer_ReturnsSingletonTimer()
  - Call getSystemTimer()
  - Verify returns SystemTimer.getInstance()

=== VCFS-003: PARSE AVAILABILITY TESTS ===

✓ testParseAvailability_SingleSlot()
  - Input: "09:00-10:00"
  - Parse with recruiter "John", role "Software Engineer"
  - Verify 1 Offer created
  - Verify Offer: startTime=09:00, endTime=10:00, recruiter="John"

✓ testParseAvailability_MultipleSlots()
  - Input: "09:00-10:00,10:00-11:00,11:00-12:00"
  - Parse availability string
  - Verify 3 Offers created
  - Verify each Offer has correct time slots

✓ testParseAvailability_InvalidFormat_ThrowsException()
  - Input: "09:00" (missing end time)
  - Verify parseAvailabilityIntoOffers throws ParseException
  - Or returns empty list

✓ testParseAvailability_MalformedTime_ThrowsException()
  - Input: "25:00-26:00" (invalid hours)
  - Verify exception thrown with clear message

✓ testParseAvailability_OverlappingSlots()
  - Input: "09:00-10:00,09:30-10:30" (overlapping)
  - Verify offers created
  - Or verify validation prevents overlaps

✓ testParseAvailability_EndTimeBeforeStartTime()
  - Input: "10:00-09:00" (end before start)
  - Verify exception thrown
  - Or verify rejected

✓ testParseAvailability_ZeroMinuteDuration()
  - Input: "09:00-09:00" (same start and end)
  - Verify handled correctly (1-minute slot? or rejected?)

✓ testParseAvailability_EmptyString()
  - Input: ""
  - Verify returns empty list
  - No exception thrown

✓ testParseAvailability_WhitespaceHandling()
  - Input: " 09:00 - 10:00 , 10:00 - 11:00 " (extra spaces)
  - Verify correctly parsed
  - Whitespace should not block parsing

✓ testParseAvailability_DayBoundaries()
  - Input: "23:00-01:00" (crosses midnight)
  - Verify handles correctly
  - Test expectation: either fails or handles 24-hour wrap

✓ testParseAvailability_AssignToRecruiter()
  - Parse "09:00-10:00" with recruiter "Alice"
  - Verify created Offer is assigned to recruiter "Alice"

✓ testParseAvailability_AssignRole()
  - Parse with role "Product Manager"
  - Verify Offer.role = "Product Manager"

=== VCFS-004: AUTO-BOOKING TESTS ===

✓ testAutoBook_SingleCandidateToSingleSlot()
  - Setup:
    - Recruiter "John" available 09:00-10:00
    - Candidate "Alice" requests meeting
  - Call autoBook()
  - Verify:
    - Reservation created
    - Reservation.state = CONFIRMED
    - Reservation.candidate = "Alice"
    - Reservation.recruiter = "John"
    - Time slot is now unavailable

✓ testAutoBook_MultipleCandidatesToMultipleSlots()
  - Setup:
    - John available: 09:00-10:00, 10:00-11:00 (2 slots)
    - Alice requests meeting
    - Bob requests meeting
  - Call autoBook()
  - Verify:
    - Both candidates booked
    - One in 09:00-10:00 slot, other in 10:00-11:00 slot
    - No slot assigned twice

✓ testAutoBook_NotEnoughSlots()
  - Setup:
    - 1 available slot with John
    - 3 candidates requesting (Alice, Bob, Charlie)
  - Call autoBook()
  - Verify:
    - 1 candidate booked (e.g., Alice)
    - 2 candidates remain in waiting queue (Bob, Charlie)

✓ testAutoBook_RespectsCandidatePreferences()
  - Setup:
    - Alice prefers "Product Manager"
    - Bob prefers "Software Engineer"
    - John (Software Engineer) available
    - Mary (Product Manager) available
  - Call autoBook()
  - Verify:
    - Alice booked with Mary (match preference)
    - Bob booked with John (match preference)

✓ testAutoBook_DurationMatches()
  - Setup:
    - Candidate wants 1-hour meeting
    - Recruiter available in 30-minute slots
  - Call autoBook()
  - Verify:
    - Does NOT book candidate to 30-minute slot (duration mismatch)
    - Candidate remains in queue

✓ testAutoBook_TimeConflicts_Prevented()
  - Setup:
    - Alice booked with John 09:00-10:00
    - Bob requests 09:30-10:30
  - Call autoBook()
  - Verify:
    - Bob NOT booked in conflicting time
    - Bob remains in queue

✓ testAutoBook_WorksOutsideBookingPhase()
  - Fair phase: FAIR_LIVE (not BOOKINGS_OPEN)
  - Call autoBook()
  - Verify:
    - Returns false or throws exception
    - Cannot book outside designated booking phase

✓ testAutoBook_EmptyQueue()
  - Setup: No candidates requesting booking
  - Call autoBook()
  - Verify:
    - Returns 0 bookings made
    - No errors thrown

✓ testAutoBook_FiresPropertyChangeNotification()
  - Add PropertyChangeListener to system
  - Call autoBook()
  - Verify:
    - Listener notified of "booking" event
    - Event contains new Reservation details

✓ testAutoBook_AuditLogged()
  - Call autoBook()
  - Query audit log
  - Verify:
    - Entry: "AUTO_BOOKING attempted, N slots available, M candidates booked"
    - Timestamp correct
    - Result recorded

=== SYSTEM STATE TESTS ===

✓ testGetAllOffers_ReturnsAllCreatedOffers()
  - Parse availability 3 times (3 offers)
  - Call getAllOffers()
  - Verify returns all 3 offers

✓ testGetAllReservations_ReturnsAllBooking()
  - Create 5 reservations
  - Call getAllReservations()
  - Verify returns list of 5

✓ testAddOrganization_StoresOrganization()
  - Call addOrganization(new Organization("Acme Corp"))
  - Call getOrganizations()
  - Verify "Acme Corp" in list

✓ testAddBooth_StoresBooth()
  - Call addBooth(new Booth("Tech Booth", org))
  - Call getBooths()
  - Verify booth stored
```

**Test Data**:
```java
CareerFairSystem system = new CareerFairSystem();
String availabilityStr = "09:00-10:00,10:00-11:00,11:00-12:00";
Recruiter recruiter = new Recruiter("John Smith", "john@acme.com");
Candidate candidate = new Candidate("Alice Johnson", "alice@university.edu");
```

**Key Assertions**:
- `assertEquals(3, offers.size())`
- `assertEquals(LocalDateTime(...), offers.get(0).getStartTime())`
- `assertTrue(reservation.isConfirmed())`
- `assertEquals(recruiter, reservation.getRecruiter())`
- `assertEquals(candidate, reservation.getCandidate())`

---

### 5️⃣ **LoggerTest.java**

**Location**: `src/test/java/vcfs/core/LoggerTest.java`

**Purpose**: 
Verify logging utility correctly records system events at appropriate severity levels.

**Relates To**:
- `vcfs.core.Logger.java` (primary)
- `vcfs.core.LogLevel.java` (INFO, WARNING, ERROR, DEBUG)

**Class Responsibility**: LOGGING UTILITY
- Records system events
- Supports multiple log levels
- Outputs to console/file

**Test Methods** (10 test cases):
```
✓ testDebugLog_RecordsDebugLevel()
✓ testInfoLog_RecordsInfoLevel()
✓ testWarningLog_RecordsWarningLevel()
✓ testErrorLog_RecordsErrorLevel()
✓ testLog_IncludesTimestamp()
✓ testLog_IncludesLevelLabel()
✓ testLog_IncludesMessage()
✓ testFilterByLevel_OnlyShowsHigher()
✓ testLogFormat_IsReadable()
✓ testLogFileOutput_FileCreated()
```

---

---

# SECTION 2: MODELS PACKAGE TESTS
## `vcfs.models.*` - Testing Business Data Layer

### 6️⃣ **UserTest.java**

**Location**: `src/test/java/vcfs/models/users/UserTest.java`

**Purpose**: 
Test abstract User base class that both Candidate and Recruiter inherit from.

**Relates To**:
- `vcfs.models.users.User.java` (primary - abstract)
- `vcfs.models.users.Candidate.java` (extends User)
- `vcfs.models.users.Recruiter.java` (extends User)

**Class Responsibility**: ABSTRACT BASE USER
- Stores common user properties (id, name, email)
- Cannot be instantiated directly (abstract)
- Provides foundation for Candidate and Recruiter

**Test Methods** (8 test cases):
```
✓ testUserIsAbstract_CannotInstantiate()
  - Verify new User() throws compilation error
  - User is abstract class

✓ testCandidateExtendsUser_Inherits()
  - Create Candidate
  - Verify instanceof User = true
  - Can call User methods

✓ testRecruiterExtendsUser_Inherits()
  - Create Recruiter
  - Verify instanceof User = true
  - Can call User methods

✓ testGetId_ReturnsStoredId()
  - Create user with id=123
  - Verify getId() returns 123

✓ testGetName_ReturnsStoredName()
  - Create user with name="Alice"
  - Verify getName() returns "Alice"

✓ testGetEmail_ReturnsStoredEmail()
  - Create user with email="alice@example.com"
  - Verify getEmail() returns "alice@example.com"

✓ testSetEmail_UpdatesEmail()
  - Create user with email="alice@example.com"
  - Call setEmail("newemail@example.com")
  - Verify getEmail() returns "newemail@example.com"

✓ testEquals_BasedOnId()
  - Create User1 with id=1, User2 with id=1
  - Verify User1.equals(User2) = true
  - Create User3 with id=2
  - Verify User1.equals(User3) = false
```

---

### 7️⃣ **CandidateTest.java**

**Location**: `src/test/java/vcfs/models/users/CandidateTest.java`

**Purpose**: 
Test Candidate class with all 3 implemented methods:
- createRequest()
- cancelMyReservation()
- viewMySchedule()

**Relates To**:
- `vcfs.models.users.Candidate.java` (primary)
- `vcfs.models.booking.Request.java` (created by createRequest())
- `vcfs.models.booking.Reservation.java` (cancelled by cancelMyReservation())
- `vcfs.models.users.User.java` (extends User)

**Class Responsibility**: JOB CANDIDATE ACTOR
- Creates meeting requests with recruiters
- Cancels reservations
- Views their meeting schedule

**Test Methods** (20 test cases):
```
✓ testConstructor_InitializesCandidate()
  - Create Candidate("Alice", "alice@uni.edu")
  - Verify name="Alice"
  - Verify email="alice@uni.edu"
  - Verify reservations list exists

✓ testCreateRequest_CreatesNewRequest()
  - Call createRequest("Software Engineer", "Product Manager")
  - Verify Request created with correct role preferences
  - Verify Request added to candidate's requests collection

✓ testCreateRequest_SingleRole()
  - Call createRequest("Software Engineer")
  - Verify Request has "Software Engineer" as preference

✓ testCreateRequest_MultipleRoles()
  - Call createRequest("Software Engineer", "Data Scientist")
  - Verify Request stored internally
  - Can create multiple separate requests

✓ testCreateRequest_EmptyRole()
  - Call createRequest("")
  - Verify no request created or exception thrown

✓ testCreateRequest_NullRole()
  - Call createRequest(null)
  - Verify exception thrown or invalid input rejected

✓ testCancelMyReservation_CancelsActiveReservation()
  - Create and confirm reservation for candidate
  - Call cancelMyReservation(reservation)
  - Verify reservation.state = CANCELLED

✓ testCancelMyReservation_CannotCancelAlreadyCancelled()
  - Reservation already cancelled
  - Call cancelMyReservation()
  - Verify exception thrown or no-op

✓ testCancelMyReservation_RemovesFromActive()
  - Add reservation to active list
  - Cancel it
  - Verify no longer in active list

✓ testCancelMyReservation_InvalidReservation()
  - Try to cancel reservation not belonging to candidate
  - Verify exception thrown

✓ testViewMySchedule_ReturnsAllReservations()
  - Create 3 confirmed reservations for candidate
  - Call viewMySchedule()
  - Verify list contains all 3 reservations

✓ testViewMySchedule_UnmodifiableList()
  - Get schedule with viewMySchedule()
  - Try to call list.add() on returned list
  - Verify exception thrown (cannot modify returned list)

✓ testViewMySchedule_SortedByTime()
  - Create reservations: 11:00, 09:00, 10:00
  - Call viewMySchedule()
  - Verify returned in order: 09:00, 10:00, 11:00

✓ testViewMySchedule_EmptySchedule()
  - Candidate with no reservations
  - Call viewMySchedule()
  - Verify returns empty list (not null)

✓ testViewMySchedule_ExcludesCancelled()
  - Create 3 reservations: 2 CONFIRMED, 1 CANCELLED
  - Call viewMySchedule()
  - Verify returns only 2 CONFIRMED

✓ testViewMySchedule_WithDetails()
  - Get schedule item
  - Verify contains: recruiter, time, role, booth

✓ testGetCandidateProfile_ReturnsProfile()
  - Candidate has CandidateProfile
  - Call getCandidateProfile()
  - Verify returns profile with candidate's info

✓ testGetAllRequests_ReturnsCreatedRequests()
  - Create 2 requests
  - Call getAllRequests()
  - Verify returns both

✓ testGetReservationCount_CountsActive()
  - Create 3 reservations, cancel 1
  - Call getReservationCount()
  - Verify returns 2

✓ testToString_FormattedCorrectly()
  - ToString should show: "Candidate[Alice, alice@uni.edu, 2 reservations]"
```

---

### 8️⃣ **RecruiterTest.java**

**Location**: `src/test/java/vcfs/models/users/RecruiterTest.java`

**Purpose**: 
Test Recruiter class with all 3 implemented methods:
- publishOffer()
- cancelReservation()
- viewSchedule()

**Relates To**:
- `vcfs.models.users.Recruiter.java` (primary)
- `vcfs.models.booking.Offer.java` (created by publishOffer())
- `vcfs.models.booking.Reservation.java` (cancelled by cancelReservation())
- `vcfs.models.structure.Booth.java` (recruiter assigned to booth)

**Class Responsibility**: JOB RECRUITER ACTOR
- Publishes job offers with availability
- Cancels reservations with candidates
- Views their interview schedule

**Test Methods** (20 test cases):
```
✓ testConstructor_InitializesRecruiter()
  - Create Recruiter("John Smith", "john@acme.com")
  - Verify name="John Smith"
  - Verify email="john@acme.com"
  - Verify offers list exists

✓ testPublishOffer_CreatesNewOffer()
  - Call publishOffer("Software Engineer", "09:00-10:00")
  - Verify Offer created
  - Verify published offers list contains new offer

✓ testPublishOffer_WithDetails()
  - Call publishOffer("Senior Developer", "10:00-11:00", "Beijing office", "Competitive salary")
  - Verify Offer has all details stored

✓ testPublishOffer_InvalidTimeFormat()
  - Call publishOffer("Role", "invalid-time")
  - Verify exception thrown or rejected

✓ testPublishOffer_EndTimeBeforeStart()
  - Call publishOffer("Role", "11:00-10:00")
  - Verify rejected

✓ testPublishOffer_MultipleOffers()
  - Publish 3 different offers
  - Verify all 3 stored in offers list
  - Each has unique role and time

✓ testPublishOffer_UpdateExisting()
  - Publish offer
  - Publish another with same role/time (update?)
  - Verify behavior (replace or duplicate?)

✓ testCancelReservation_CancelsActiveReservation()
  - Create confirmed reservation with recruiter
  - Call cancelReservation(reservation)
  - Verify reservation.state = CANCELLED

✓ testCancelReservation_CannotCancelAlreadyCancelled()
  - Reservation already cancelled
  - Call cancelReservation()
  - Verify no double-cancellation

✓ testCancelReservation_InvalidReservation()
  - Try to cancel reservation not assigned to this recruiter
  - Verify exception thrown

✓ testCancelReservation_ReleaseTimeSlot()
  - Cancel reservation for 10:00-11:00
  - Verify time slot is available again for other candidates

✓ testViewSchedule_ReturnsAllReservations()
  - Create 4 confirmed reservations
  - Call viewSchedule()
  - Verify list contains all 4

✓ testViewSchedule_UnmodifiableList()
  - Get schedule
  - Try to list.add()
  - Verify exception thrown

✓ testViewSchedule_SortedByTime()
  - Create reservations: 11:00, 09:00, 10:00
  - Call viewSchedule()
  - Verify returned in order

✓ testViewSchedule_EmptySchedule()
  - No reservations
  - Call viewSchedule()
  - Verify returns empty list (not null)

✓ testViewSchedule_ExcludesCancelled()
  - 2 CONFIRMED, 1 CANCELLED reservations
  - Call viewSchedule()
  - Verify returns only 2 CONFIRMED

✓ testViewSchedule_WithCandidateDetails()
  - Get schedule item
  - Verify contains: candidate name, booth, location, time

✓ testGetAllPublishedOffers_ReturnsAllOffers()
  - Publish 3 offers
  - Call getAllPublishedOffers()
  - Verify returns all 3

✓ testGetAvailableSlots_CountsAvailable()
  - Publish offer 09:00-10:00
  - Book 1 candidate
  - Call getAvailableSlots()
  - Verify returns remaining slots

✓ testToString_FormattedCorrectly()
  - ToString shows: "Recruiter[John Smith, john@acme.com, 3 offers, 5 reservations]"
```

---

### 9️⃣ **CandidateProfileTest.java**

**Location**: `src/test/java/vcfs/models/users/CandidateProfileTest.java`

**Purpose**: 
Test candidate profile information storage.

**Relates To**:
- `vcfs.models.users.CandidateProfile.java` (primary)
- `vcfs.models.users.Candidate.java` (has profile)

**Class Responsibility**: CANDIDATE PROFILE DATA
- Stores resume, skills, preferences
- Linked to specific candidate
- Read/update profile information

**Test Methods** (12 test cases):
```
✓ testConstructor_StoresCandidate()
✓ testGetCandidate_ReturnsAssignedCandidate()
✓ testSetResume_StoresResume()
✓ testGetResume_ReturnsResume()
✓ testAddSkill_AddsToSkillsList()
✓ testGetAllSkills_ReturnsList()
✓ testRemoveSkill_RemovesFromList()
✓ testSetDesiredRole_StoresRole()
✓ testGetDesiredRole_ReturnsRole()
✓ testAddPreference_StoresPreference()
✓ testGetAllPreferences_ReturnsUnmodifiableList()
✓ testToString_FormattedCorrectly()
```

---

### 🔟 **OrganizationTest.java**

**Location**: `src/test/java/vcfs/models/structure/OrganizationTest.java`

**Purpose**: 
Test Organization entity (company/employer at career fair).

**Relates To**:
- `vcfs.models.structure.Organization.java` (primary)
- `vcfs.models.structure.Booth.java` (organization has booths)
- `vcfs.models.users.Recruiter.java` (recruiters work for organizations)

**Class Responsibility**: ORGANIZATION DATA
- Stores company information
- Manages collection of booths
- Tracks recruiters

**Test Methods** (14 test cases):
```
✓ testConstructor_StoresOrganizationName()
✓ testGetName_ReturnsName()
✓ testSetName_UpdatesName()
✓ testAddBooth_AddsToBooth()
✓ testRemoveBooth_RemovesBooth()
✓ testGetAllBooths_ReturnsBooths()
✓ testAddRecruiter_AddsRecruiter()
✓ testRemoveRecruiter_RemovesRecruiter()
✓ testGetAllRecruiters_ReturnsRecruiters()
✓ testGetBoothCount_CountsBooths()
✓ testGetRecruiterCount_CountsRecruiters()
✓ testEquals_BasedOnName()
✓ testHashCode_ConsistentWithEquals()
✓ testToString_FormattedCorrectly()
```

---

### 1️⃣1️⃣ **BoothTest.java**

**Location**: `src/test/java/vcfs/models/structure/BoothTest.java`

**Purpose**: 
Test Booth class with 2 implemented methods:
- assignRecruiter()
- removeRecruiter()

**Relates To**:
- `vcfs.models.structure.Booth.java` (primary)
- `vcfs.models.structure.Organization.java` (booth belongs to org)
- `vcfs.models.users.Recruiter.java` (recruiter assigned to booth)

**Class Responsibility**: CAREER BOOTH ENTITY
- Represents physical booth at fair
- Manages assigned recruiters
- Links organization to physical location

**Test Methods** (18 test cases):
```
✓ testConstructor_StoresBoothInfo()
  - Create Booth("Tech Booth", org, "Hall A, Table 1")
  - Verify name, organization, location stored

✓ testAssignRecruiter_AssignsRecruiter()
  - Create booth with no recruiters
  - Call assignRecruiter(recruiter)
  - Verify recruiter added to booth's recruiter list

✓ testAssignRecruiter_MultipeRecruiters()
  - Add 3 recruiters to booth
  - Verify all 3 stored
  - Booth can have multiple recruiters

✓ testAssignRecruiter_SameRecruiterTwice()
  - Assign recruiter once, assign again
  - Verify only 1 instance (no duplicate)
  - Or verify exception thrown

✓ testRemoveRecruiter_RemovesRecruiter()
  - Assign recruiter, then remove
  - Verify recruiter no longer in booth's list

✓ testRemoveRecruiter_NonexistentRecruiter()
  - Try to remove recruiter not in booth
  - Verify exception thrown or no-op

✓ testRemoveRecruiter_EmptyBooth()
  - Remove recruiter from booth with no recruiters
  - Verify exception thrown or handled gracefully

✓ testGetAllRecruiters_ReturnsUnmodifiableList()
  - Get recruiters list
  - Try to add to returned list
  - Verify exception thrown (cannot modify)

✓ testGetRecruiterCount_CountsAssignedRecruiters()
  - Add 3 recruiters
  - Call getRecruiterCount()
  - Verify returns 3

✓ testGetName_ReturnsBoothName()
  - Booth name = "Tech Booth"
  - Verify getName() returns "Tech Booth"

✓ testGetOrganization_ReturnsAssignedOrg()
  - Booth assigned to Acme Corp
  - Verify getOrganization() returns Acme Corp

✓ testGetLocation_ReturnsLocation()
  - Location = "Hall A, Table 1"
  - Verify getLocation() returns correct location

✓ testSetLocation_UpdatesLocation()
  - Change location to "Hall B, Table 2"
  - Verify getLocation() returns new location

✓ testGetAvailableRecruiters_FiltersAvailable()
  - Add 3 recruiters, 1 is on break
  - Call getAvailableRecruiters()
  - Verify returns only 2 available

✓ testEquals_BasedOnNameAndOrg()
  - Two booths with same org and name are equal
  - Verify equals() returns true
  - Different org or name = false

✓ testHashCode_ConsistentWithEquals()

✓ testToString_FormattedCorrectly()
  - "Booth[Tech Booth, Acme Corp, Hall A Table 1, 3 recruiters]"

✓ testValidateBoothConstraints()
  - Name cannot be empty
  - Organization cannot be null
  - Location can be null (virtual booth?)
```

---

### 1️⃣2️⃣ **VirtualRoomTest.java**

**Location**: `src/test/java/vcfs/models/structure/VirtualRoomTest.java`

**Purpose**: 
Test virtual meeting room for remote interviews.

**Relates To**:
- `vcfs.models.structure.VirtualRoom.java` (primary)
- `vcfs.models.booking.MeetingSession.java` (room hosts session)
- `vcfs.models.enums.RoomState.java` (AVAILABLE, OCCUPIED, MAINTENANCE)

**Class Responsibility**: VIRTUAL ROOM ENTITY
- Represents virtual meeting space
- Manages room availability/state
- Tracks active sessions

**Test Methods** (16 test cases):
```
✓ testConstructor_CreatesRoom()
✓ testGetRoomId_ReturnsSavedId()
✓ testGetRoomName_ReturnsSavedName()
✓ testGetRoomState_ReturnsState()
✓ testSetRoomState_UpdatesState()
  - Change state from AVAILABLE to OCCUPIED
  - Verify getState() returns OCCUPIED
  
✓ testSetRoomState_InvalidStateTransition()
  - Try invalid transition
  - Verify rejected or exception thrown

✓ testIsAvailable_ChecksState()
  - State = AVAILABLE
  - Verify isAvailable() = true
  - State = OCCUPIED
  - Verify isAvailable() = false

✓ testStartSession_SetsOccupied()
  - Room state = AVAILABLE
  - Call startSession()
  - Verify state = OCCUPIED
  - Verify currentSession set

✓ testEndSession_SetsAvailable()
  - Room state = OCCUPIED
  - Call endSession()
  - Verify state = AVAILABLE
  - Verify currentSession cleared

✓ testGetCapacity_ReturnsCapacity()
✓ testSetCapacity_UpdatesCapacity()
✓ testCurrentSession_ReturnsActiveSession()
✓ testAddLog_TracksRoomActivity()
✓ testEquals_BasedOnRoomId()
✓ testHashCode_ConsistentWithEquals()
✓ testToString_FormattedCorrectly()
```

---

### 1️⃣3️⃣ **OfferTest.java**

**Location**: `src/test/java/vcfs/models/booking/OfferTest.java`

**Purpose**: 
Test Offer class with 1 implemented method:
- updateDetails()

**Relates To**:
- `vcfs.models.booking.Offer.java` (primary)
- `vcfs.models.users.Recruiter.java` (recruiter publishes offer)
- `vcfs.models.structure.Booth.java` (booth hosts hiring for offer)

**Class Responsibility**: JOB OFFER ENTITY
- Represents job opening recruiter is advertising
- Stores role, location, time availability
- Can be updated with new details

**Test Methods** (16 test cases):
```
✓ testConstructor_StoresOfferDetails()
  - Create Offer("Software Engineer", recruiter, "09:00-10:00", booth)
  - Verify all fields stored

✓ testGetRole_ReturnsRole()
✓ testGetRecruiter_ReturnsRecruiter()
✓ testGetStartTime_ReturnsStartTime()
✓ testGetEndTime_ReturnsEndTime()
✓ testGetBooth_ReturnsBooth()

✓ testUpdateDetails_ChangesRole()
  - Original: "Software Engineer"
  - Call updateDetails("Senior Developer", ...)
  - Verify getRole() = "Senior Developer"

✓ testUpdateDetails_ChangesTime()
  - Original: 09:00-10:00
  - Call updateDetails(..., "10:00-11:00")
  - Verify new time stored

✓ testUpdateDetails_ChangesDescription()
  - Call updateDetails(..., description="New description")
  - Verify description updated

✓ testUpdateDetails_PartialUpdate()
  - Update only role, not time
  - Verify role changed, time unchanged

✓ testUpdateDetails_InvalidRole()
  - Call updateDetails("", ...)
  - Verify exception thrown or rejected

✓ testGetDescription_ReturnsDescription()
✓ testGetCreatedTime_ReturnsCreatedTime()
✓ testEquals_BasedOnRoleAndTime()
  - Two offers with same role/time = equal
  - Same role, different time = not equal

✓ testToString_FormattedCorrectly()
  - "Offer[Software Engineer, recruiter=John, 09:00-10:00, booth=Tech]"

✓ testIsAvailable_ChecksTimeFrame()
  - Current time = 09:15, offer = 09:00-10:00
  - Verify isAvailable() = true
  - Current time = 11:00
  - Verify isAvailable() = false

✓ testGetAttendeeCount_CountsAttendees()
  - 0 attendees initially
  - Add 2 candidates
  - Verify count = 2
```

---

### 1️⃣4️⃣ **RequestTest.java**

**Location**: `src/test/java/vcfs/models/booking/RequestTest.java`

**Purpose**: 
Test Request class with 1 implemented method:
- updatePreferences()

**Relates To**:
- `vcfs.models.booking.Request.java` (primary)
- `vcfs.models.users.Candidate.java` (candidate creates request)
- `vcfs.models.booking.Reservation.java` (request leads to reservation)

**Class Responsibility**: MEETING REQUEST ENTITY
- Candidate expresses interest in meeting
- Stores preferred roles
- Can update preferences before booking

**Test Methods** (16 test cases):
```
✓ testConstructor_StoresRequestDetails()
  - Create Request(candidate, "Software Engineer", "09:00-11:00")
  - Verify candidate, role, timeframe stored

✓ testGetCandidate_ReturnsCandidate()
✓ testGetPreferredRole_ReturnsRole()
✓ testGetTimeframe_ReturnsTimeframe()
✓ testGetRequestStatus_ReturnsStatus()
  - Status: PENDING, MATCHED, FULFILLED, EXPIRED

✓ testUpdatePreferences_ChangesRole()
  - Original: "Software Engineer"
  - Call updatePreferences("Product Manager")
  - Verify getPreferredRole() = "Product Manager"

✓ testUpdatePreferences_ChangesTimeframe()
  - Original: 09:00-11:00
  - Call updatePreferences("", "10:00-12:00")
  - Verify new timeframe

✓ testUpdatePreferences_MultipleRoles()
  - Call updatePreferences("Software Engineer", "Data Scientist")
  - Verify both preferences stored

✓ testUpdatePreferences_CannotUpdateFulfilled()
  - Request status = FULFILLED
  - Call updatePreferences()
  - Verify exception thrown (too late)

✓ testUpdatePreferences_CanOnlyUpdatePending()
  - Only PENDING requests can update
  - MATCHED, FULFILLED, EXPIRED cannot update

✓ testGetCreatedTime_ReturnsCreatedTime()
✓ testGetExpiryTime_ReturnsExpiryTime()
  - Request expires if not matched within time window

✓ testIsExpired_ChecksExpiryTime()
  - Current time > expiry time
  - Verify isExpired() = true

✓ testGetAssignedReservation_ReturnsReservation()
  - After matching, reservation assigned to request
  - Verify getAssignedReservation() returns it

✓ testEquals_BasedOnCandidate()
  - Same candidate = equal request
  - Different candidate = not equal

✓ testToString_FormattedCorrectly()
```

---

### 1️⃣5️⃣ **ReservationTest.java**

**Location**: `src/test/java/vcfs/models/booking/ReservationTest.java`

**Purpose**: 
Test Reservation class with 2 implemented methods:
- cancel()
- isActive()

**Relates To**:
- `vcfs.models.booking.Reservation.java` (primary)
- `vcfs.models.users.Candidate.java` (candidate has reservations)
- `vcfs.models.users.Recruiter.java` (recruiter has reservations)
- `vcfs.models.enums.ReservationState.java` (PENDING, CONFIRMED, IN_PROGRESS, CANCELLED, COMPLETED)

**Class Responsibility**: MEETING RESERVATION ENTITY
- Represents confirmed meeting between candidate and recruiter
- State machine: PENDING → CONFIRMED → IN_PROGRESS → COMPLETED
- Can be cancelled at any point before completion

**Test Methods** (24 test cases):
```
✓ testConstructor_CreatesReservation()
  - Create Reservation(candidate, recruiter, time, booth)
  - Verify all fields stored

✓ testGetCandidate_ReturnsCandidate()
✓ testGetRecruiter_ReturnsRecruiter()
✓ testGetStartTime_ReturnsStartTime()
✓ testGetEndTime_ReturnsEndTime()
✓ testGetBooth_ReturnsBooth()

✓ testGetState_ReturnsState()
  - New reservation state = PENDING

✓ testSetState_UpdatesState()
  - Change from PENDING to CONFIRMED
  - Verify getState() = CONFIRMED

✓ testCancel_SetsStateToCancelled()
  - State = PENDING
  - Call cancel()
  - Verify state = CANCELLED

✓ testCancel_FromConfirmed()
  - State = CONFIRMED
  - Call cancel()
  - Verify state = CANCELLED

✓ testCancel_AlreadyCancelled()
  - State = CANCELLED
  - Call cancel()
  - Verify no double-cancellation
  - Verify exception thrown or no-op

✓ testCancel_CompletedReservation()
  - State = COMPLETED
  - Try to call cancel()
  - Verify exception thrown (cannot cancel completed)

✓ testCancel_NotifiesParties()
  - Cancel reservation
  - Verify candidate notified
  - Verify recruiter notified

✓ testIsActive_Pending()
  - State = PENDING
  - Verify isActive() = true

✓ testIsActive_Confirmed()
  - State = CONFIRMED
  - Verify isActive() = true

✓ testIsActive_InProgress()
  - State = IN_PROGRESS
  - Verify isActive() = true

✓ testIsActive_Cancelled()
  - State = CANCELLED
  - Verify isActive() = false

✓ testIsActive_Completed()
  - State = COMPLETED
  - Verify isActive() = false

✓ testIsActive_OnlyCountsSpecificStates()
  - Only PENDING, CONFIRMED, IN_PROGRESS count as active
  - CANCELLED, COMPLETED = not active

✓ testConfirm_TransitionsPendingToConfirmed()
  - State = PENDING
  - Call confirm()
  - Verify state = CONFIRMED

✓ testStart_TransitionsConfirmedToInProgress()
  - State = CONFIRMED
  - Call startMeeting()
  - Verify state = IN_PROGRESS

✓ testComplete_TransitionsInProgressToCompleted()
  - State = IN_PROGRESS
  - Call completeMeeting()
  - Verify state = COMPLETED

✓ testEquals_BasedOnParticipants()
  - Same candidate + recruiter = equal
  - Different participants = not equal

✓ testHashCode_ConsistentWithEquals()

✓ testToString_FormattedCorrectly()
  - "Reservation[Alice-John, Pending, 09:00-10:00, Tech Booth]"

✓ testGetCancellationReason_ReturnsReason()
  - Call cancel("Double booked")
  - Verify getCancellationReason() returns reason

✓ testGetCreatedTime_ReturnsCreatedTime()
✓ testGetStartTime_ReturnsStartTime()
```

---

### 1️⃣6️⃣ **LobbyTest.java**

**Location**: `src/test/java/vcfs/models/booking/LobbyTest.java`

**Purpose**: 
Test Lobby (waiting queue) class with 3 implemented methods:
- add()
- remove()
- listWaiting()

**Relates To**:
- `vcfs.models.booking.Lobby.java` (primary)
- `vcfs.models.users.Candidate.java` (candidates in lobby)
- `vcfs.models.structure.Booth.java` (booth has lobby)

**Class Responsibility**: WAITING QUEUE MANAGER
- Candidates wait in lobby before booking
- First-in-first-out queue
- Can remove candidates (if cancelled)

**Test Methods** (18 test cases):
```
✓ testConstructor_CreatesLobbly()
  - Create Lobby()
  - Verify queue initialized

✓ testAdd_AddsCandidateToQueue()
  - Create lobby (empty)
  - Call add(candidate1)
  - Verify candidate1 in queue

✓ testAdd_MultipleCandidate()
  - Add candidate1, candidate2, candidate3
  - Verify all 3 in queue
  - Verify order preserved (FIFO)

✓ testAdd_SameCandidateTwice()
  - Try to add candidate twice
  - Verify either:
    - Duplicate allowed (2 entries)
    - Or duplicate rejected (1 entry)

✓ testAdd_NullCandidate()
  - Try to add(null)
  - Verify exception thrown

✓ testRemove_RemovesCandidateFromQueue()
  - 3 candidates in queue
  - Call remove(candidate2)
  - Verify candidate2 no longer in queue
  - Verify candidate1 and candidate3 still there

✓ testRemove_RemoveFirstCandidate()
  - 3 candidates: 1, 2, 3
  - Call remove(candidate1)
  - Verify removed

✓ testRemove_RemoveLastCandidate()
  - 3 candidates: 1, 2, 3
  - Call remove(candidate3)
  - Verify removed

✓ testRemove_NonexistentCandidate()
  - Try to remove candidate not in queue
  - Verify exception thrown or no-op

✓ testRemove_EmptyQueue()
  - Call remove() on empty lobby
  - Verify exception thrown or handled

✓ testListWaiting_ReturnsUnmodifiableList()
  - Get list from listWaiting()
  - Try to call list.add()
  - Verify exception thrown (immutable)

✓ testListWaiting_ReturnsInOrder()
  - Add: candidate1, candidate2, candidate3
  - Get list from listWaiting()
  - Verify order: [1, 2, 3]

✓ testListWaiting_DoesNotReturnRemoved()
  - Add 3 candidates
  - Remove candidate2
  - Call listWaiting()
  - Verify returns only [1, 3]

✓ testListWaiting_EmptyQueue()
  - No candidates
  - Call listWaiting()
  - Verify returns empty list (not null)

✓ testGetWaitingCount_CountsCandidates()
  - Add 5 candidates
  - Call getWaitingCount()
  - Verify returns 5
  - Remove 2
  - Verify returns 3

✓ testGetFirstInQueue_ReturnsFirst()
  - Queue: [1, 2, 3]
  - Call getFirst()
  - Verify returns candidate1 (not removed)

✓ testPoll_RemovesAndReturnsFIrst()
  - Queue: [1, 2, 3]
  - Call poll()
  - Verify returns candidate1
  - Verify candidate1 removed from queue

✓ testToString_FormattedCorrectly()
  - "Lobby[5 waiting: Alice, Bob, Charlie, ...]"
```

---

### 1️⃣7️⃣ **MeetingSessionTest.java**

**Location**: `src/test/java/vcfs/models/booking/MeetingSessionTest.java`

**Purpose**: 
Test virtual meeting session between candidate and recruiter.

**Relates To**:
- `vcfs.models.booking.MeetingSession.java` (primary)
- `vcfs.models.booking.Reservation.java` (session fulfills reservation)
- `vcfs.models.structure.VirtualRoom.java` (session uses room)
- `vcfs.models.enums.MeetingState.java` (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED)

**Class Responsibility**: VIRTUAL MEETING SESSION
- Represents active/completed meeting
- Tracks start/end times
- Records outcome/notes

**Test Methods** (14 test cases):
```
✓ testConstructor_CreatesSession()
✓ testGetReservation_ReturnsReservation()
✓ testGetVirtualRoom_ReturnsRoom()
✓ testGetStartTime_ReturnsStartTime()
✓ testGetEndTime_ReturnsEndTime()
✓ testGetState_ReturnsState()
  - Initial state = NOT_STARTED

✓ testStart_TransitionsToInProgress()
  - Call startSession()
  - Verify state = IN_PROGRESS

✓ testEnd_TransitionsToCompleted()
  - state = IN_PROGRESS
  - Call endSession()
  - Verify state = COMPLETED

✓ testFail_TransitionsToFailed()
  - Call failSession()
  - Verify state = FAILED

✓ testAddNote_RecordsNote()
  - Call addNote("Candidate was late")
  - Verify note stored

✓ testGetNotes_ReturnsNotes()

✓ testGeDuration_CalculatesDuration()
  - Start: 09:00, End: 09:30
  - Verify getDuration() = 30 minutes

✓ testToString_FormattedCorrectly()
```

---

### 1️⃣8️⃣ **AuditEntryTest.java**

**Location**: `src/test/java/vcfs/models/audit/AuditEntryTest.java`

**Purpose**: 
Test audit log entry with 1 implemented method:
- toString()

**Relates To**:
- `vcfs.models.audit.AuditEntry.java` (primary)
- `vcfs.core.CareerFairSystem` (creates audit entries)

**Class Responsibility**: AUDIT LOG ENTRY
- Records system events
- Immutable once created
- Formatted for display

**Test Methods** (12 test cases):
```
✓ testConstructor_StoresEventDetails()
  - Create AuditEntry(time, "RESERVATION_CREATED", "Alice booked with John")
  - Verify all fields stored

✓ testGetTime_ReturnsTime()
✓ testGetEventType_ReturnsEventType()
✓ testGetDescription_ReturnsDescription()

✓ testToString_FormattedCorrectly()
  - Format: "[2026-04-07 09:00:00] RESERVATION_CREATED: Alice booked with John"
  - Verify timestamp, event type, description all present
  - Verify human-readable

✓ testToString_WithNullDescription()
  - Description = null
  - Verify toString() handles gracefully

✓ testToString_Consistency()
  - Call toString() twice
  - Verify returns same string (deterministic)

✓ testImmutable_CannotChangeTime()
  - Time is set in constructor
  - No setTime() method
  - Verify immutable

✓ testImmutable_CannotChangeDescription()
  - No setDescription() method
  - Verify immutable

✓ testEquals_BasedOnTimeAndType()
  - Same time + type = equal
  - Different time or type = not equal

✓ testHashCode_ConsistentWithEquals()

✓ testCompareTo_SortsByTime()
  - Entry1: 09:00, Entry2: 10:00
  - Verify Entry1 < Entry2 (for sorting)
```

---

### 1️⃣9️⃣ **AttendanceRecordTest.java**

**Location**: `src/test/java/vcfs/models/audit/AttendanceRecordTest.java`

**Purpose**: 
Test attendance tracking with 1 implemented method:
- close()

**Relates To**:
- `vcfs.models.audit.AttendanceRecord.java` (primary)
- `vcfs.models.enums.AttendanceOutcome.java` (PRESENT, ABSENT, NO_SHOW, EXCUSED)
- `vcfs.models.booking.Reservation.java` (reservation generates record)

**Class Responsibility**: ATTENDANCE TRACKING
- Records whether candidate attended meeting
- Immutable outcome once closed
- Used for fair analytics

**Test Methods** (14 test cases):
```
✓ testConstructor_CreatesOpenRecord()
  - Create AttendanceRecord(candidate, recruiter, reservation)
  - Verify record is OPEN

✓ testGetCandidate_ReturnsCandidate()
✓ testGetRecruiter_ReturnsRecruiter()
✓ testGetReservation_ReturnsReservation()

✓ testClose_WithPresent()
  - Call close(AttendanceOutcome.PRESENT)
  - Verify outcome = PRESENT
  - Verify record no longer open

✓ testClose_WithAbsent()
  - Call close(AttendanceOutcome.ABSENT)
  - Verify outcome = ABSENT

✓ testClose_WithNoShow()
  - Call close(AttendanceOutcome.NO_SHOW)
  - Verify outcome = NO_SHOW

✓ testClose_WithExcused()
  - Call close(AttendanceOutcome.EXCUSED)
  - Verify outcome = EXCUSED

✓ testClose_CannotCloseAlreadyClosed()
  - Close record
  - Try to close again
  - Verify exception thrown (idempotency or rejection)

✓ testIsClosed_ChecksStatus()
  - Open = isClosed() = false
  - After close() = isClosed() = true

✓ testGetOutcome_ReturnsOutcome()
  - After closing with PRESENT
  - Verify getOutcome() = PRESENT

✓ testAddNote_RecordsNote()
  - Call addNote("Candidate was 5 minutes late")
  - Verify note stored

✓ testGetNotes_ReturnsNotes()

✓ testToString_FormattedCorrectly()
  - "AttendanceRecord[Alice-John, PRESENT, notes=...]"
```

---

### 2️⃣0️⃣ **Enum Tests** (5 classes - brief)

#### **FairPhaseTest.java**
```
✓ testEnumValues_AllPhasesDefined()
✓ testOrderOfPhases()
  - DORMANT < PREPARING < BOOKINGS_OPEN < BOOKINGS_CLOSED < FAIR_LIVE
✓ testToString_FormattedNicely()
```

#### **MeetingStateTest.java**
```
✓ testEnumValues_AllStatesDefined()
✓ testValidStates()
  - NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED, POSTPONED
```

#### **ReservationStateTest.java**
```
✓ testEnumValues_AllStatesDefined()
✓ testValidTransitions()
  - PENDING → CONFIRMED → IN_PROGRESS → COMPLETED
  - Or CANCELLED from any state
```

#### **RoomStateTest.java**
```
✓ testEnumValues_AllStatesDefined()
✓ testValidStates()
  - AVAILABLE, OCCUPIED, MAINTENANCE, OFFLINE
```

#### **AttendanceOutcomeTest.java**
```
✓ testEnumValues_AllOutcomesDefined()
✓ testIsPresent_ChecksOutcome()
  - PRESENT = true, others = false
✓ testIsAbsent_ChecksOutcome()
```

---

---

# SECTION 3: CONTROLLER TESTS
## `vcfs.controllers` - Testing Business Logic Orchestration

### 2️⃣1️⃣ **AdminScreenControllerTest.java**

**Location**: `src/test/java/vcfs/controllers/AdminScreenControllerTest.java`

**Purpose**: 
Test controller bridge between AdminScreen UI and system logic.

**Relates To**:
- `vcfs.controllers.AdminScreenController.java` (primary)
- `vcfs.views.admin.AdminScreen.java` (UI that calls controller)
- `vcfs.core.CareerFairSystem` (system operations)

**Class Responsibility**: ADMIN UI CONTROLLER
- Handles organization creation
- Handles booth creation
- Handles recruiter assignment
- Handles timeline configuration

**Test Methods** (16 test cases):
```
✓ testCreateOrganization_StoresOrganization()
✓ testCreateOrganization_InvalidName()
✓ testCreateBooth_CreatesBooth()
✓ testCreateBooth_AssignToOrganization()
✓ testAssignRecruiter_StoresAssignment()
✓ testAssignRecruiter_InvalidBooth()
✓ testSetTimeline_StoresTimes()
✓ testSetTimeline_ValidatesChronological()
```

---

### 2️⃣2️⃣ **CandidateControllerTest.java**

**Location**: `src/test/java/vcfs/controllers/CandidateControllerTest.java`

**Purpose**: 
Test candidate operations controller.

**Test Methods** (14 test cases):
```
✓ testCreateRequest_CallsCandidateMethod()
✓ testViewSchedule_ReturnsSchedule()
✓ testCancelReservation_CancelsReservation()
✓ testProcessBooking_MatchesWithOffers()
```

---

### 2️⃣3️⃣ **RecruiterControllerTest.java**

**Location**: `src/test/java/vcfs/controllers/RecruiterControllerTest.java`

**Purpose**: 
Test recruiter operations controller.

**Test Methods** (14 test cases):
```
✓ testPublishOffer_CreatesOffer()
✓ testViewSchedule_ReturnsSchedule()
✓ testCancelReservation_CancelsReservation()
✓ testViewApplications_ReturnsApplications()
```

---

---

# SECTION 4: INTEGRATION TESTS
## Full System Workflow Testing

### 2️⃣4️⃣ **CareerFairSystemIntegrationTest.java**

**Location**: `src/test/java/vcfs/integration/CareerFairSystemIntegrationTest.java`

**Purpose**: 
Test complete workflow: Fair setup → Admin creates booths/org → Recruiters publish offers → Candidates request meetings → Auto-booking → Meetings happen

**Test Methods** (12 test cases):
```
✓ testFullWorkflow_FromSetupToCompletion()
  - Setup fair timeline
  - Create organization and booths
  - Add recruiters to booths
  - Recruiters publish offers
  - Candidates request meetings
  - System auto-books
  - Meetings complete
  - Verify all state transitions correct

✓ testMultipleCandidatesAndRecruiters()
  - 3 organizations
  - 5 booths
  - 10 recruiters
  - 50 candidates
  - Verify system handles scale

✓ testConcurrentBooking()
  - Multiple threads booking simultaneously
  - Verify no double-bookings
  - Verify thread-safe

✓ testAuditTrail_AllEventsLogged()
  - Verify every major event has audit entry
  - Correct timestamps
  - Correct descriptions

✓ testPhaseTransitions_Correct()
  - Fair progresses through all 5 phases
  - Verify phase-dependent behavior works

✓ testBookingOutsidePhase_Rejected()
  - Try to book outside BOOKINGS_OPEN phase
  - Verify rejected

✓ testTimeProgression_Correct()
  - Simulate time passing minute-by-minute
  - Verify fair phase transitions at right times

✓ testCancelAndRebook_Possible()
  - Candidate cancels reservation
  - Time slot becomes available
  - Another candidate books slot
  - Verify process works

✓ testQueueManagement_FIFO()
  - Multiple candidates waiting
  - Verify spots allocated in order (FIFO)

✓ testOfferMatching_PreferencesHonored()
  - Candidates with preferences
  - Verify matched to preferred roles when available

✓ testAttendanceTracking_Recorded()
  - Meetings complete
  - Verify attendancerecords created
  - Verify outcomes recorded

✓ testReporting_CanGenerateStats()
  - Query: total candidates, total recruiters, total bookings
  - Verify correct counts
```

---

### 2️⃣5️⃣ **BookingWorkflowTest.java**

**Location**: `src/test/java/vcfs/integration/BookingWorkflowTest.java`

**Purpose**: 
Test booking pipeline in detail.

**Test Methods** (10 test cases):
```
✓ testRequestToReservation_Pipeline()
✓ testMultipleOfferMatching_PicksBest()
✓ testConflictResolution_NoDoubleBooking()
✓ testWaitlistToConfirmed_WhenSlotFrees()
```

---

### 2️⃣6️⃣ **AuditLoggingTest.java**

**Location**: `src/test/java/vcfs/integration/AuditLoggingTest.java`

**Purpose**: 
Verify comprehensive audit trail of all system actions.

**Test Methods** (8 test cases):
```
✓ testAllMajorEventsLogged()
✓ testAuditLogTimestamps_Accurate()
✓ testAuditLogCanBeQueried_ByEventType()
✓ testAuditLogCanBeQueried_ByTimeRange()
```

---

---

# SECTION 5: VIEW/UI TESTS
## Java Swing Component Testing

### 2️⃣7️⃣ **AdminScreenTest.java**

**Location**: `src/test/java/vcfs/views/admin/AdminScreenTest.java`

**Purpose**: 
Test AdminScreen UI rendering and user interactions.

**Test Methods** (12 test cases):
```
✓ testScreenCreated_WithAllComponents()
✓ testCreateOrgButton_Calls Controller()
✓ testCreateBoothButton_Calls Controller()
✓ testTimeInputFields_AcceptInput()
✓ testAuditLogDisplay_ShowsEntries()
✓ testPropertyChangeListener_Receives Notifications()
```

---

### 2️⃣8️⃣ **LoginFrameTest.java**

**Location**: `src/test/java/vcfs/views/shared/LoginFrameTest.java`

**Purpose**: 
Test login screen UI.

**Test Methods** (8 test cases):
```
✓ testFrame Created_WithInputFields()
✓ testLoginButton_Validates Input()
✓ testInvalidCredentials_ShowsError()
✓ testValidLogin_LaunchesNext Screen()
```

---

### 2️⃣9️⃣ **SystemTimerScreenTest.java**

**Location**: `src/test/java/vcfs/views/shared/SystemTimerScreenTest.java`

**Purpose**: 
Test timer display panel.

**Test Methods** (8 test cases):
```
✓ testTimerDisplay_Updates()
✓ testStepButton_AdvancesTime()
✓ testJumpButton_JumpsTime()
✓ testPhaseDisplay_UpdatesWithFairPhase()
```

---

### 3️⃣0️⃣ **RecruiterScreenTest.java**

**Location**: `src/test/java/vcfs/views/recruiter/RecruiterScreenTest.java`

**Purpose**: 
Test recruiter UI.

**Test Methods** (12 test cases):
```
✓ testScreenCreated_WithTabs()
✓ testPublishOfferTab_Works()
✓ testScheduleTab_DisplaysSchedule()
```

---

---

# SECTION 6: EDGE CASE & STRESS TESTS

### 3️⃣1️⃣ **EdgeCaseTest.java**

**Location**: `src/test/java/vcfs/EdgeCaseTest.java`

**Purpose**: 
Test unusual but valid scenarios.

**Test Cases** (20+ cases):
```
✓ testNull Input_Handled Gracefully()
✓ testEmpty Collections_Work Correctly()
✓ testBoundaryValues_TimeWrap at Midnight()
✓ testLarge Numbers_1000 Candidates()
✓ testSpecialCharacters_InNames()
✓ testVeryLongStrings_Notification Truncated()
```

---

### 3️⃣2️⃣ **ConcurrencyTest.java**

**Location**: `src/test/java/vcfs/ConcurrencyTest.java`

**Purpose**: 
Test thread safety.

**Test Cases** (15+ cases):
```
✓ testConcurrentBooking_NoRaceConditions()
✓ testConcurrentTimerUpdates_AllListenersNotified()
✓ testConcurrentCollectionModification_Thread Safe()
✓ testSingletonTimer_ReallySingleton_UnderConcurrency()
```

---

---

# 📊 TEST COVERAGE SUMMARY

```
CORE PACKAGE:
  ✓ LocalDateTimeTest: 15 tests
  ✓ SystemTimerTest: 18 tests
  ✓ CareerFairTest: 20 tests
  ✓ CareerFairSystemTest: 25 tests
  ✓ LoggerTest: 10 tests
  Subtotal: 88 tests

MODELS PACKAGE:
  ✓ UserTest: 8 tests
  ✓ CandidateTest: 20 tests
  ✓ RecruiterTest: 20 tests
  ✓ CandidateProfileTest: 12 tests
  ✓ OrganizationTest: 14 tests
  ✓ BoothTest: 18 tests
  ✓ VirtualRoomTest: 16 tests
  ✓ OfferTest: 16 tests
  ✓ RequestTest: 16 tests
  ✓ ReservationTest: 24 tests
  ✓ LobbyTest: 18 tests
  ✓ MeetingSessionTest: 14 tests
  ✓ AuditEntryTest: 12 tests
  ✓ AttendanceRecordTest: 14 tests
  ✓ 5 Enum Tests: 15+ tests
  Subtotal: 247 tests

CONTROLLERS PACKAGE:
  ✓ AdminScreenControllerTest: 16 tests
  ✓ CandidateControllerTest: 14 tests
  ✓ RecruiterControllerTest: 14 tests
  Subtotal: 44 tests

INTEGRATION TESTS:
  ✓ CareerFairSystemIntegrationTest: 12 tests
  ✓ BookingWorkflowTest: 10 tests
  ✓ AuditLoggingTest: 8 tests
  Subtotal: 30 tests

VIEW TESTS:
  ✓ AdminScreenTest: 12 tests
  ✓ LoginFrameTest: 8 tests
  ✓ SystemTimerScreenTest: 8 tests
  ✓ RecruiterScreenTest: 12 tests
  Subtotal: 40 tests

SPECIAL TESTS:
  ✓ EdgeCaseTest: 20+ tests
  ✓ ConcurrencyTest: 15+ tests
  Subtotal: 35+ tests

─────────────────────────────────
GRAND TOTAL: 560+ TEST CASES
TARGET COVERAGE: >85%
─────────────────────────────────
```

---

# 📋 IMPLEMENTATION ROADMAP (For Mohamed)

**Phase 1: Core Tests** (Week 1)
- [ ] LocalDateTimeTest
- [ ] SystemTimerTest
- [ ] LoggerTest
- [x] CareerFairTest
- [ ] CareerFairSystemTest

**Phase 2: Model Tests** (Week 2-3)
- [ ] User + Candidate + Recruiter + Profile tests
- [ ] Organization + Booth + VirtualRoom tests
- [ ] Offer + Request + Reservation + Lobby + MeetingSession tests
- [ ] Audit + Attendance tests
- [ ] Enum tests

**Phase 3: Controller & Integration Tests** (Week 4)
- [ ] Controller tests
- [ ] Integration workflow tests
- [ ] Edge case & concurrency tests

**Phase 4: View Tests** (Week 4)
- [ ] UI component tests
- [ ] User interaction tests

**Phase 5: Final Validation** (Week 5)
- [ ] Run full test suite
- [ ] Calculate code coverage
- [ ] Document uncovered paths
- [ ] Generate test report

---

**Document Version**: 1.0  
**Status**: READY FOR IMPLEMENTATION  
**Estimated Total Time**: 40-50 hours for all test classes  
**Target Completion**: Before final submission

src/test/java/vcfs/
├── 📁 core/
│   ├── LocalDateTimeTest.java (15 tests)
│   ├── SystemTimerTest.java (18 tests)
│   ├── CareerFairTest.java (20 tests)
│   ├── CareerFairSystemTest.java (25 tests)
│   └── LoggerTest.java (10 tests)
│
├── 📁 models/
│   ├── 📁 users/
│   │   ├── UserTest.java (8 tests)
│   │   ├── CandidateTest.java (20 tests)
│   │   ├── RecruiterTest.java (20 tests)
│   │   └── CandidateProfileTest.java (12 tests)
│   │
│   ├── 📁 structure/
│   │   ├── OrganizationTest.java (14 tests)
│   │   ├── BoothTest.java (18 tests)
│   │   └── VirtualRoomTest.java (16 tests)
│   │
│   ├── 📁 booking/
│   │   ├── OfferTest.java (16 tests)
│   │   ├── RequestTest.java (16 tests)
│   │   ├── ReservationTest.java (24 tests)
│   │   ├── LobbyTest.java (18 tests)
│   │   └── MeetingSessionTest.java (14 tests)
│   │
│   ├── 📁 audit/
│   │   ├── AuditEntryTest.java (12 tests)
│   │   └── AttendanceRecordTest.java (14 tests)
│   │
│   └── 📁 enums/
│       ├── FairPhaseTest.java (5 tests)
│       ├── MeetingStateTest.java (5 tests)
│       ├── ReservationStateTest.java (5 tests)
│       ├── RoomStateTest.java (5 tests)
│       └── AttendanceOutcomeTest.java (5 tests)
│
├── 📁 controllers/
│   ├── AdminScreenControllerTest.java (16 tests)
│   ├── CandidateControllerTest.java (14 tests)
│   └── RecruiterControllerTest.java (14 tests)
│
├── 📁 views/
│   ├── 📁 admin/
│   │   └── AdminScreenTest.java (12 tests)
│   │
│   ├── 📁 candidate/
│   │   └── CandidateScreenTest.java (8 tests)
│   │
│   ├── 📁 recruiter/
│   │   └── RecruiterScreenTest.java (12 tests)
│   │
│   └── 📁 shared/
│       ├── LoginFrameTest.java (8 tests)
│       └── SystemTimerScreenTest.java (8 tests)
│
└── 📁 integration/
    ├── CareerFairSystemIntegrationTest.java (12 tests)
    ├── BookingWorkflowTest.java (10 tests)
    ├── AuditLoggingTest.java (8 tests)
    ├── EdgeCaseTest.java (20+ tests)
    └── ConcurrencyTest.java (15+ tests)

════════════════════════════════════════════════════════
TOTAL: 32 TEST CLASSES | 560+ TEST CASES | >85% COVERAGE
════════════════════════════════════════════════════════

src/main/java/vcfs/              ←→  src/test/java/vcfs/
├── core/                               ├── core/
├── models/                            ├── models/
│   ├── users/                        │   ├── users/
│   ├── structure/                    │   ├── structure/
│   ├── booking/                      │   ├── booking/
│   ├── audit/                        │   ├── audit/
│   └── enums/                        │   └── enums/
├── controllers/                       ├── controllers/
└── views/                             └── views/
    ├── admin/                            ├── admin/
    ├── candidate/                        ├── candidate/
    ├── recruiter/                        ├── recruiter/
    └── shared/                           └── shared/

    
NAMING CONVENTION
✅ All Test Classes Follow Pattern: [ClassName]Test.java

Examples:

LocalDateTime.java → LocalDateTimeTest.java
CareerFair.java → CareerFairTest.java
Candidate.java → CandidateTest.java
AdminScreen.java → AdminScreenTest.java

javac -d bin -cp src/main/java:src/test/java -sourcepath src/test/java src/test/java/vcfs/**/*.java


FINAL PROJECT STRUCTURE (After Cleanup)

Grp_9_CSCU9P6/
├── 📄 README.md                              ← PROJECT OVERVIEW
├── 📄 INDIVIDUAL_CONTRIBUTION_FORM.md        ← SUBMISSION ARTIFACT
├── 📄 REFLECTIVE_DIARY_ENTRIES.md            ← SUBMISSION ARTIFACT
├── 📁 src/
│   ├── main/java/vcfs/
│   │   ├── App.java
│   │   ├── Main.java                         ✓ PROPER VERSION (not root version)
│   │   ├── core/
│   │   ├── models/
│   │   ├── controllers/
│   │   └── views/
│   └── test/java/vcfs/
│       ├── core/
│       ├── models/
│       ├── controllers/
│       ├── views/
│       └── integration/
├── 📁 docs/
│   ├── plan/
│   │   ├── ARCHITECTURE.md
│   │   ├── MASTER_IMPLEMENTATION_PLAN.md
│   │   ├── JUNIT_TEST_CLASSES_BLUEPRINT.md
│   │   ├── PHASE_1_COMPLETION_SUMMARY.md     ✓ MOVED HERE
│   │   └── PHASE_1_SUMMARY_REPORT.txt        ✓ MOVED HERE
│   └── ...
├── 📁 logs/
│   ├── compile_errors.txt                    ✓ MOVED HERE
│   └── ...
└── 📁 GitHubVersion/
    └── (Original GitHub files for reference)

    SUMMARY: WHAT TO DO
✅ DELETE (14 files):
