# 🧪 VCFS JUnit Testing Strategy & Implementation Guide

**Project**: Virtual Career Fair System (VCFS)  
**Testing Framework**: JUnit 5 (Jupiter)  
**Test Coverage Target**: 85%+ code coverage  
**Execution Time**: ~8-10 hours (writing + execution + debugging)  
**Priority**: ALL TESTS MUST PASS before submission

---

## PHASE 5.5: COMPREHENSIVE JUNIT TEST SUITE

### 📋 Test Structure Overview

```
src/test/java/vcfs/
├── core/                          ← System core functionality
│   ├── LocalDateTimeTest.java     ← Date/time operations (2 hours)
│   ├── SystemTimerTest.java       ← Observer pattern + timing (2 hours)
│   ├── CareerFairTest.java        ← Phase state machine (2 hours)
│   └── CareerFairSystemTest.java  ← VCFS-003, 004, core logic (3 hours)
│
├── models/                         ← Data model integrity
│   ├── booking/
│   │   ├── OfferTest.java         ← Offer slot creation (1 hour)
│   │   ├── RequestTest.java       ← Request submission (1 hour)
│   │   └── ReservationTest.java   ← Reservation state machine (1.5 hours)
│   ├── users/
│   │   ├── CandidateTest.java     ← Candidate profile operations (1 hour)
│   │   └── RecruiterTest.java     ← Recruiter offer management (1 hour)
│   └── structure/
│       └── BoothTest.java         ← Booth/Organization setup (1 hour)
│
├── controllers/                    ← Business logic integration
│   ├── AdminScreenControllerTest.java        ← Admin operations (1 hour)
│   ├── RecruiterControllerTest.java         ← Recruiter workflows (2 hours)
│   └── CandidateControllerTest.java         ← Candidate workflows (2 hours)
│
└── integration/                    ← Full system workflows
    └── EndToEndWorkflowTest.java   ← Complete fair lifecycle (2 hours)
```

**Total Test Count**: ~80 test methods across 15 test classes  
**Estimated Time**: 25 hours (writing) + 1 hour (execution) = 26 hours

---

## TEST CLASS 1: LocalDateTimeTest (2 hours)

**Purpose**: Verify custom date/time wrapper works correctly

**Dependencies**: LocalDateTime.java only (no external dependencies)

**Test Methods** (8 tests):

```java
package vcfs.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocalDateTimeTest {
    
    @Test
    void testConstructor_ValidInputs() {
        // Arrange: Create with valid year, month, day, hour, minute
        // Act: LocalDateTime ldt = new LocalDateTime(2026, 4, 8, 14, 30);
        // Assert: Year, month, day, hour, minute all match
    }
    
    @Test
    void testConstructor_InvalidMonth() {
        // Should throw exception for month > 12
        assertThrows(IllegalArgumentException.class, 
            () -> new LocalDateTime(2026, 13, 8, 14, 30));
    }
    
    @Test
    void testIsBefore() {
        // Create two datetime objects
        // Earlier datetime.isBefore(later) should be true
        // Later datetime.isBefore(earlier) should be false
        // Same datetime.isBefore(same) should be false
    }
    
    @Test
    void testIsAfter() {
        // Verify isAfter() works inverse to isBefore()
    }
    
    @Test
    void testPlusMinutes() {
        // Create 09:00
        // Add 30 minutes
        // Verify result is 09:30
        // Test: 23:45 + 30 min = 00:15 next day (midnight rollover)
    }
    
    @Test
    void testMinutesUntil() {
        // From 09:00 to 09:30 = 30 minutes
        // From 09:00 to 10:00 = 60 minutes  
        // From 09:00 to 09:00 = 0 minutes
    }
    
    @Test
    void testEquality() {
        // Same date/time should be equal
        // Different date/time should not be equal
    }
    
    @Test
    void testComparability() {
        // Earlier datetime < later datetime
        // Same datetime == same datetime
    }
}
```

**Setup**: Create LocalDateTime.java test fixtures

---

## TEST CLASS 2: SystemTimerTest (2 hours)

**Purpose**: Verify singleton timer + observer pattern

**Dependencies**: LocalDateTime, Logger

**Test Methods** (8 tests):

```java
package vcfs.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SystemTimerTest {
    private SystemTimer timer;
    private PropertyChangeListener mockListener;
    
    @BeforeEach
    void setUp() {
        timer = SystemTimer.getInstance();
        mockListener = mock(PropertyChangeListener.class);
    }
    
    @Test
    void testSingleton() {
        // SystemTimer.getInstance() always returns same instance
        SystemTimer timer1 = SystemTimer.getInstance();
        SystemTimer timer2 = SystemTimer.getInstance();
        assertSame(timer1, timer2);
    }
    
    @Test
    void testAddPropertyChangeListener() {
        // Add listener to observer list
        timer.addPropertyChangeListener(mockListener);
        // Firing change should notify the listener
        timer.tick();
        verify(mockListener, atLeastOnce()).propertyChange(any());
    }
    
    @Test
    void testRemovePropertyChangeListener() {
        // Add, then remove listener
        timer.addPropertyChangeListener(mockListener);
        timer.removePropertyChangeListener(mockListener);
        // Should not be called after removal
        verify(mockListener, never()).propertyChange(any());
    }
    
    @Test
    void testTickIncrementsTime() {
        // Get current time
        LocalDateTime before = timer.getCurrentTime();
        // Tick (advance time)
        timer.tick();
        LocalDateTime after = timer.getCurrentTime();
        // After > before
        assertTrue(after.isAfter(before));
    }
    
    @Test
    void testNotifiesManyListeners() {
        // Add 5 listeners
        PropertyChangeListener[] listeners = new PropertyChangeListener[5];
        for (int i = 0; i < 5; i++) {
            listeners[i] = mock(PropertyChangeListener.class);
            timer.addPropertyChangeListener(listeners[i]);
        }
        // On tick, all should be notified
        timer.tick();
        for (PropertyChangeListener listener : listeners) {
            verify(listener).propertyChange(any());
        }
    }
    
    @Test
    void testPropertyChangeEventDetails() {
        // Capture the event passed to listener
        ArgumentCaptor<PropertyChangeEvent> eventCaptor = 
            ArgumentCaptor.forClass(PropertyChangeEvent.class);
        timer.addPropertyChangeListener(mockListener);
        timer.tick();
        verify(mockListener).propertyChange(eventCaptor.capture());
        
        PropertyChangeEvent event = eventCaptor.getValue();
        assertEquals("currentTime", event.getPropertyName());
        assertNotNull(event.getOldValue());
        assertNotNull(event.getNewValue());
    }
    
    @Test
    void testSetCurrentTime() {
        // Manually set time
        LocalDateTime newTime = new LocalDateTime(2026, 4, 8, 15, 30);
        timer.setCurrentTime(newTime);
        assertEquals(newTime, timer.getCurrentTime());
    }
}
```

---

## TEST CLASS 3: CareerFairTest (2 hours)

**Purpose**: Test state machine (DORMANT → PREPARING → BOOKINGS_OPEN → ...)

**Dependencies**: LocalDateTime, CareerFair

**Test Methods** (10 tests):

```java
package vcfs.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CareerFairTest {
    private CareerFair fair;
    
    @BeforeEach
    void setUp() {
        fair = new CareerFair();
    }
    
    @Test
    void testInitialState_DORMANT() {
        // Fair starts in DORMANT state
        assertEquals(FairPhase.DORMANT, fair.getCurrentPhase());
    }
    
    @Test
    void testSetTimes() {
        // Set the 4 critical times
        LocalDateTime bookOpen = new LocalDateTime(2026, 4, 8, 10, 0);
        LocalDateTime bookClose = new LocalDateTime(2026, 4, 8, 12, 0);
        LocalDateTime fairStart = new LocalDateTime(2026, 4, 8, 13, 0);
        LocalDateTime fairEnd = new LocalDateTime(2026, 4, 8, 17, 0);
        
        fair.setTimes(bookOpen, bookClose, fairStart, fairEnd);
        // Verify times stored
        assert fair.getBookingsOpenTime() == bookOpen;
    }
    
    @Test
    void testEvaluatePhase_TransitionsToPREPARING() {
        // Before bookings open, should be PREPARING
        // (or stays DORMANT if not activated)
        // This depends on implementation
    }
    
    @Test
    void testEvaluatePhase_TransitionsToBookingsOpen() {
        // Set times
        LocalDateTime bookOpen = new LocalDateTime(2026, 4, 8, 10, 0);
        LocalDateTime bookClose = new LocalDateTime(2026, 4, 8, 12, 0);
        LocalDateTime fairStart = new LocalDateTime(2026, 4, 8, 13, 0);
        LocalDateTime fairEnd = new LocalDateTime(2026, 4, 8, 17, 0);
        fair.setTimes(bookOpen, bookClose, fairStart, fairEnd);
        
        // Set SystemTimer to 10:30 (during bookings)
        SystemTimer timer = SystemTimer.getInstance();
        timer.setCurrentTime(new LocalDateTime(2026, 4, 8, 10, 30));
        
        fair.evaluatePhase();
        assertEquals(FairPhase.BOOKINGS_OPEN, fair.getCurrentPhase());
    }
    
    @Test
    void testCanBook_True_DuringBookingsOpen() {
        // Set times and move to BOOKINGS_OPEN phase
        // fair.canBook() should return true
    }
    
    @Test
    void testCanBook_False_DuringFairLive() {
        // During FAIR_LIVE phase, canBook() should return false
    }
    
    @Test
    void testIsLive_False_BeforeFair() {
        // Before fair start time, isLive() = false
    }
    
    @Test
    void testIsLive_True_DuringFair() {
        // During fair time window, isLive() = true
    }
    
    @Test
    void testFullPhaseProgression() {
        // Go through all phases in sequence:
        // DORMANT → PREPARING → BOOKINGS_OPEN → BOOKINGS_CLOSED → FAIR_LIVE
        // Verify each transition at the right time
    }
}
```

---

## TEST CLASS 4: CareerFairSystemTest (3 hours)

**Purpose**: Test VCFS-001, 002, 003, 004 algorithms + core logic

**Dependencies**: All models, CareerFairSystem, SystemTimer

**Key Tests for VCFS Tickets**:

### VCFS-001: Singleton Pattern (1 test)
```java
@Test
void testSingleton_VCFS001() {
    CareerFairSystem sys1 = CareerFairSystem.getInstance();
    CareerFairSystem sys2 = CareerFairSystem.getInstance();
    assertSame(sys1, sys2);
}
```

### VCFS-002: Observer Pattern (3 tests)
```java
@Test
void testTick_CallsEvaluatePhase_VCFS002() {
    // SystemTimer.tick() 
    //   → fires PropertyChangeEvent 
    //   → CareerFairSystem receives event 
    //   → calls fair.evaluatePhase()
    // Verify phase changes at correct times
}

@Test
void testObserverRegistration_VCFS002() {
    // System registers as observer of SystemTimer
    SystemTimer timer = SystemTimer.getInstance();
    // Verify CareerFairSystem is in listener list
}

@Test
void testPhaseTransitionsViaTimer_VCFS002() {
    // Simulate timer ticks
    // Verify phase transitions happen
}
```

### VCFS-003: Offer Slot Generation (3 tests)
```java
@Test
void testParseAvailabilityIntoOffers_3HourBlock_VCFS003() {
    // Input: 3-hour block (09:00-12:00), 30-min duration
    // Expected: 6 discrete Offer slots
    Recruiter recruiter = new Recruiter("R1", "John", "john@example.com");
    LocalDateTime blockStart = new LocalDateTime(2026, 4, 8, 9, 0);
    LocalDateTime blockEnd = new LocalDateTime(2026, 4, 8, 12, 0);
    
    int slotsCreated = system.parseAvailabilityIntoOffers(
        recruiter, "1-on-1", blockStart, blockEnd, 30, "AI,ML"
    );
    
    assertEquals(6, slotsCreated);
    // Verify 6 Offer objects created
    List<Offer> offers = recruiter.getOffers();
    assertEquals(6, offers.size());
}

@Test
void testParseAvailability_SlotTimes_VCFS003() {
    // Verify slots have correct times:
    // 09:00-09:30, 09:30-10:00, ..., 11:30-12:00
}

@Test
void testParseAvailability_NonDivisibleBlock_VCFS003() {
    // 2.5 hours with 30-min slots should give 5 slots
    // (last slot: 11:00-11:30, not 11:30-12:00)
}
```

### VCFS-004: Auto-Booking Algorithm (5 tests)
```java
@Test
void testAutoBook_TagMatching_VCFS004() {
    // Offer tags: "AI", "ML", "Data"
    // Request tags: "ML", "Data", "Python"
    // Intersection: 2 tags
    // Should be matched with good score
}

@Test
void testAutoBook_NoCollision_VCFS004() {
    // Candidate has no conflicts
    // Booking should succeed
}

@Test
void testAutoBook_WithCollision_VCFS004() {
    // Candidate already has meeting 10:00-10:30
    // Offer is 10:15-10:45 (overlaps)
    // Should NOT be booked (collision detection)
    // Formula: A_start < B_end AND B_start < A_end
}

@Test
void testAutoBook_BestMatch_VCFS004() {
    // 3 available offers
    // Tags: Offer1={AI,ML}, Offer2={ML,Data}, Offer3={Java,Python}
    // Request={ML,Data}
    // Scores: Offer1=1, Offer2=2, Offer3=0
    // Should match Offer2 (highest score)
}

@Test
void testAutoBook_ReturnsReservation_VCFS004() {
    // Successful booking returns Reservation object
    // With correct state, candidate, offer, scheduled times
}
```

---

## TEST CLASS 5-10: Model Tests (5 hours)

Quick tests for data integrity:

```java
// OfferTest.java (1 hour)
- testConstructor
- testSettersGetters
- testRemoveReservation
- testIsAvailable

// RequestTest.java (1 hour)
- testSetCandidate
- testSetDesiredTags
- testSetMaxAppointments
- testGetters

// ReservationTest.java (1.5 hours)
- testStateTransitions (PENDING → CONFIRMED → IN_PROGRESS → ENDED/CANCELLED)
- testGettersSetters
- testValidateState

// CandidateTest.java (1 hour)
- testSubmitRequest
- testCancelRequest
- testGetMeetingSchedule
- testGetRequestHistory

// RecruiterTest.java (1 hour)
- testPublishOffer
- testCancelReservation
- testGetPublishedOffers
- testMeetingHistory

// BoothTest.java (1 hour)
- testAddRecruiter
- testGetRecruiters
- testOrganizationRelationship
```

---

## TEST CLASS 11-13: Controller Tests (5 hours)

Integration tests between Controllers and Models:

```java
// AdminScreenControllerTest.java (1 hour)
- testCreateOrganization
- testCreateBooth
- testAssignRecruiter
- testSetTimeline (validates all 4 times)

// RecruiterControllerTest.java (2 hours)
- testPublishOffer_Success
- testPublishOffer_ValidationErrors
- testScheduleSession
- testCancelSession
- testViewMeetingHistory
- testGetPublishedOffers

// CandidateControllerTest.java (2 hours)
- testSubmitMeetingRequest
- testSubmitRequest_NoMatches
- testViewAvailableLobbies
- testViewMeetingSchedule
- testCancelMeetingRequest
- testUpdateProfile
```

---

## TEST CLASS 14: Integration Test (2 hours)

**EndToEndWorkflowTest.java** - Full system workflow

```java
package vcfs.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class EndToEndWorkflowTest {
    private CareerFairSystem system;
    private Organization org;
    private Booth booth;
    private Recruiter recruiter;
    private Candidate candidate;
    
    @BeforeEach
    void setUp() {
        system = CareerFairSystem.getInstance();
        // Initialize: org → booth → recruiter → candidate
    }
    
    @Test
    void testCompleteWorkflow_RegistrationToMeeting() {
        // STEP 1: Admin configures fair
        LocalDateTime bookOpen = new LocalDateTime(2026, 4, 8, 10, 0);
        LocalDateTime bookClose = new LocalDateTime(2026, 4, 8, 12, 0);
        LocalDateTime fairStart = new LocalDateTime(2026, 4, 8, 13, 0);
        LocalDateTime fairEnd = new LocalDateTime(2026, 4, 8, 17, 0);
        system.setFairTimes(bookOpen, bookClose, fairStart, fairEnd);
        
        // STEP 2: Recruiter publishes offer
        int slots = system.parseAvailabilityIntoOffers(
            recruiter, "1-on-1", 
            new LocalDateTime(2026, 4, 8, 13, 0),
            new LocalDateTime(2026, 4, 8, 15, 0),
            30, "AI,ML,Data"
        );
        assertEquals(4, slots);
        
        // STEP 3: Candidate submits request
        Request request = new Request();
        request.setCandidate(candidate);
        request.setDesiredTags("ML,Data,Python");
        Reservation reservation = system.autoBook(candidate, request);
        
        // STEP 4: Verify reservation created
        assertNotNull(reservation);
        assertEquals("CONFIRMED", reservation.getState());
        assertEquals(candidate, reservation.getCandidate());
        
        // STEP 5: Verify offer removed from availability
        List<Offer> available = recruiter.getPublishedOffers();
        // One less offer available
        
        // STEP 6: Recruiter schedules meeting
        system.scheduleSession(reservation.getId());
        assertEquals("WAITING", reservation.getMeetingSession().getState());
        
        // STEP 7: Candidate joins lobby
        MeetingSession session = reservation.getMeetingSession();
        Lobby lobby = session.getLobby();
        assertNotNull(lobby);
        assertTrue(lobby.isOpen());
        
        // STEP 8: Meeting progress
        session.setState("IN_PROGRESS");
        session.recordJoin(candidate);
        assertEquals(1, session.getAttendanceRecords().size());
        
        // STEP 9: Meeting ends
        session.setState("ENDED");
        session.recordLeave(candidate);
        assertEquals("ENDED", reservation.getState());
    }
    
    @Test
    void testWorkflow_MultipleReservations() {
        // Same candidate books multiple meetings
        // Verify collision detection prevents overlap
    }
    
    @Test
    void testWorkflow_CancellationAndReaction() {
        // Candidate books, then cancels
        // Verify offer becomes available again
    }
}
```

---

## MOCK & TESTING UTILITIES

### Dependencies (Add to pom.xml or build.gradle)

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.2.0</version>
    <scope>test</scope>
</dependency>
```

### Test Fixtures (Reusable Objects)

```java
// TestDataBuilder.java
public class TestDataBuilder {
    public static Candidate createCandidate() {
        return new Candidate("C1", "John Doe", "john@example.com");
    }
    
    public static Recruiter createRecruiter() {
        return new Recruiter("R1", "Jane Smith", "jane@example.com");
    }
    
    public static Organization createOrganization() {
        return new Organization("Google", "Tech");
    }
    
    public static Booth createBooth(Organization org) {
        Booth booth = new Booth("Booth1", "Google Main");
        org.addBooth(booth);
        return booth;
    }
}
```

---

## TEST EXECUTION STRATEGY

### Phase 1: Unit Tests (Core)
Priority: LocalDateTimeTest → SystemTimerTest → CareerFairTest → CareerFairSystemTest
Command: `mvn test -Dtest=vcfs.core.*Test`

### Phase 2: Model Tests  
Priority: Reservation → Offer → Request → Users
Command: `mvn test -Dtest=vcfs.models.**.*Test`

### Phase 3: Controller Tests
Command: `mvn test -Dtest=vcfs.controllers.**.*Test`

### Phase 4: Integration Tests
Command: `mvn test -Dtest=vcfs.integration.**.*Test`

### Phase 5: Full Coverage Report
Command: `mvn test jacoco:report`

**Pass Criteria**: 
- ✅ All 80 tests pass
- ✅ No skipped tests
- ✅ 0 failures
- ✅ Code coverage >= 85%

---

## TIME ALLOCATION BREAKDOWN

| Category | Hours | Tests |
|----------|-------|-------|
| Core (LocalDateTime, SystemTimer, CareerFair, CareerFairSystem) | 9 | 30 |
| Models (Offer, Request, Reservation, Users, Booth) | 6 | 25 |
| Controllers (Admin, Recruiter, Candidate) | 5 | 18 |
| Integration (End-to-end workflows) | 2 | 7 |
| Debugging & Fixes | 3 | — |
| **TOTAL** | **25** | **80** |

---

## SUCCESS CRITERIA

| Metric | Target | Status |
|--------|--------|--------|
| Total Tests | 80+ | 🔄 In Progress |
| Pass Rate | 100% | 🔴 Pending |
| Code Coverage | 85%+ | 🔴 Pending |
| Compilation | 0 errors | ✅ Complete |
| Execution Time | < 5 min | 🔄 To measure |

---

## NEXT STEPS

1. ✅ Review this test blueprint
2. ⏳ Implement test classes in priority order
3. ⏳ Execute tests and fix any failures
4. ⏳ Generate coverage report
5. ⏳ Document test results
