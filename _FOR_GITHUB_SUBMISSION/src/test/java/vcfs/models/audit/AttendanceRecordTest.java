package vcfs.models.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.core.LocalDateTime;
import vcfs.models.booking.MeetingSession;
import vcfs.models.users.Candidate;


/**
 * Comprehensive JUnit tests for AttendanceRecord class.
 * Tests candidate attendance tracking with join and leave times.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("AttendanceRecord - Candidate Attendance Tracking")
public class AttendanceRecordTest {

    private AttendanceRecord attendanceRecord;
    private Candidate candidate;
    private MeetingSession session;
    private LocalDateTime joinTime;
    private LocalDateTime leaveTime;

    @BeforeEach
    void setUp() {
        attendanceRecord = new AttendanceRecord();
        candidate = new Candidate("cand001", "Test Candidate", "test@example.com");
        session = new MeetingSession();
        joinTime = new LocalDateTime(2026, 4, 10, 10, 0);
        leaveTime = new LocalDateTime(2026, 4, 10, 10, 30);
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("AttendanceRecord constructor creates empty instance")
        void testConstructorCreatesEmpty() {
            assertNotNull(attendanceRecord);
            assertNull(attendanceRecord.getCandidate());
            assertNull(attendanceRecord.getSession());
            assertNull(attendanceRecord.getJoinTime());
            assertNull(attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Multiple attendance records are independent")
        void testMultipleRecordsIndependent() {
            AttendanceRecord record1 = new AttendanceRecord();
            AttendanceRecord record2 = new AttendanceRecord();
            
            assertNotSame(record1, record2);
        }
    }

    // ========== CANDIDATE MANAGEMENT ==========

    @Nested
    @DisplayName("Candidate Management Tests")
    class CandidateManagementTests {

        @Test
        @DisplayName("getCandidate returns null initially")
        void testGetCandidateInitiallyNull() {
            assertNull(attendanceRecord.getCandidate());
        }

        @Test
        @DisplayName("setCandidate stores candidate")
        void testSetCandidate() {
            attendanceRecord.setCandidate(candidate);
            assertEquals(candidate, attendanceRecord.getCandidate());
        }

        @Test
        @DisplayName("setCandidate can be set to null")
        void testSetCandidateNull() {
            attendanceRecord.setCandidate(candidate);
            attendanceRecord.setCandidate(null);
            assertNull(attendanceRecord.getCandidate());
        }

        @Test
        @DisplayName("setCandidate with different candidates")
        void testSetCandidateDifferentCandidates() {
            Candidate candidate1 = new Candidate("cand001", "Candidate 1", "cand1@test.com");
            Candidate candidate2 = new Candidate("cand002", "Candidate 2", "cand2@test.com");
            
            attendanceRecord.setCandidate(candidate1);
            assertEquals(candidate1, attendanceRecord.getCandidate());
            
            attendanceRecord.setCandidate(candidate2);
            assertEquals(candidate2, attendanceRecord.getCandidate());
        }
    }

    // ========== SESSION MANAGEMENT ==========

    @Nested
    @DisplayName("Session Management Tests")
    class SessionManagementTests {

        @Test
        @DisplayName("getSession returns null initially")
        void testGetSessionInitiallyNull() {
            assertNull(attendanceRecord.getSession());
        }

        @Test
        @DisplayName("setSession stores meeting session")
        void testSetSession() {
            attendanceRecord.setSession(session);
            assertEquals(session, attendanceRecord.getSession());
        }

        @Test
        @DisplayName("setSession can be set to null")
        void testSetSessionNull() {
            attendanceRecord.setSession(session);
            attendanceRecord.setSession(null);
            assertNull(attendanceRecord.getSession());
        }

        @Test
        @DisplayName("setSession with different sessions")
        void testSetSessionDifferentSessions() {
            MeetingSession session1 = new MeetingSession();
            MeetingSession session2 = new MeetingSession();
            
            attendanceRecord.setSession(session1);
            assertEquals(session1, attendanceRecord.getSession());
            
            attendanceRecord.setSession(session2);
            assertEquals(session2, attendanceRecord.getSession());
        }
    }

    // ========== JOIN TIME MANAGEMENT ==========

    @Nested
    @DisplayName("Join Time Management Tests")
    class JoinTimeManagementTests {

        @Test
        @DisplayName("getJoinTime returns null initially")
        void testGetJoinTimeInitiallyNull() {
            assertNull(attendanceRecord.getJoinTime());
        }

        @Test
        @DisplayName("setJoinTime stores join time")
        void testSetJoinTime() {
            attendanceRecord.setJoinTime(joinTime);
            assertEquals(joinTime, attendanceRecord.getJoinTime());
        }

        @Test
        @DisplayName("setJoinTime can be set to null")
        void testSetJoinTimeNull() {
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setJoinTime(null);
            assertNull(attendanceRecord.getJoinTime());
        }

        @Test
        @DisplayName("setJoinTime with different times")
        void testSetJoinTimeDifferentTimes() {
            LocalDateTime time1 = new LocalDateTime(2026, 4, 10, 9, 0);
            LocalDateTime time2 = new LocalDateTime(2026, 4, 10, 10, 0);
            LocalDateTime time3 = new LocalDateTime(2026, 4, 10, 11, 0);
            
            attendanceRecord.setJoinTime(time1);
            assertEquals(time1, attendanceRecord.getJoinTime());
            
            attendanceRecord.setJoinTime(time2);
            assertEquals(time2, attendanceRecord.getJoinTime());
            
            attendanceRecord.setJoinTime(time3);
            assertEquals(time3, attendanceRecord.getJoinTime());
        }

        @Test
        @DisplayName("setJoinTime can be updated multiple times")
        void testUpdateJoinTime() {
            LocalDateTime originalTime = new LocalDateTime(2026, 4, 10, 10, 0);
            LocalDateTime updatedTime = new LocalDateTime(2026, 4, 10, 10, 5);
            
            attendanceRecord.setJoinTime(originalTime);
            assertEquals(originalTime, attendanceRecord.getJoinTime());
            
            attendanceRecord.setJoinTime(updatedTime);
            assertEquals(updatedTime, attendanceRecord.getJoinTime());
        }
    }

    // ========== LEAVE TIME MANAGEMENT ==========

    @Nested
    @DisplayName("Leave Time Management Tests")
    class LeaveTimeManagementTests {

        @Test
        @DisplayName("getLeaveTime returns null initially")
        void testGetLeaveTimeInitiallyNull() {
            assertNull(attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("setLeaveTime stores leave time")
        void testSetLeaveTime() {
            attendanceRecord.setLeaveTime(leaveTime);
            assertEquals(leaveTime, attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("setLeaveTime can be set to null")
        void testSetLeaveTimeNull() {
            attendanceRecord.setLeaveTime(leaveTime);
            attendanceRecord.setLeaveTime(null);
            assertNull(attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("setLeaveTime with different times")
        void testSetLeaveTimeDifferentTimes() {
            LocalDateTime time1 = new LocalDateTime(2026, 4, 10, 10, 30);
            LocalDateTime time2 = new LocalDateTime(2026, 4, 10, 11, 0);
            LocalDateTime time3 = new LocalDateTime(2026, 4, 10, 12, 0);
            
            attendanceRecord.setLeaveTime(time1);
            assertEquals(time1, attendanceRecord.getLeaveTime());
            
            attendanceRecord.setLeaveTime(time2);
            assertEquals(time2, attendanceRecord.getLeaveTime());
            
            attendanceRecord.setLeaveTime(time3);
            assertEquals(time3, attendanceRecord.getLeaveTime());
        }
    }

    // ========== INDEPENDENT FIELD UPDATES ==========

    @Nested
    @DisplayName("Independent Field Updates Tests")
    class IndependentFieldUpdatesTests {

        @Test
        @DisplayName("Setting candidate does not affect session or times")
        void testSetCandidateIndependent() {
            attendanceRecord.setSession(session);
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setLeaveTime(leaveTime);
            
            attendanceRecord.setCandidate(candidate);
            
            assertEquals(session, attendanceRecord.getSession());
            assertEquals(joinTime, attendanceRecord.getJoinTime());
            assertEquals(leaveTime, attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Setting session does not affect candidate or times")
        void testSetSessionIndependent() {
            attendanceRecord.setCandidate(candidate);
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setLeaveTime(leaveTime);
            
            attendanceRecord.setSession(session);
            
            assertEquals(candidate, attendanceRecord.getCandidate());
            assertEquals(joinTime, attendanceRecord.getJoinTime());
            assertEquals(leaveTime, attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Setting times does not affect candidate or session")
        void testSetTimesIndependent() {
            attendanceRecord.setCandidate(candidate);
            attendanceRecord.setSession(session);
            
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setLeaveTime(leaveTime);
            
            assertEquals(candidate, attendanceRecord.getCandidate());
            assertEquals(session, attendanceRecord.getSession());
        }
    }

    // ========== COMPLETE ATTENDANCE RECORD LIFECYCLE ==========

    @Nested
    @DisplayName("Complete Attendance Record Lifecycle Tests")
    class AttendanceRecordLifecycleTests {

        @Test
        @DisplayName("Complete attendance record with all fields")
        void testCompleteAttendanceRecord() {
            attendanceRecord.setCandidate(candidate);
            attendanceRecord.setSession(session);
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setLeaveTime(leaveTime);
            
            assertEquals(candidate, attendanceRecord.getCandidate());
            assertEquals(session, attendanceRecord.getSession());
            assertEquals(joinTime, attendanceRecord.getJoinTime());
            assertEquals(leaveTime, attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Attendance record with join but no leave")
        void testAttendanceRecordJoinNoLeave() {
            attendanceRecord.setCandidate(candidate);
            attendanceRecord.setSession(session);
            attendanceRecord.setJoinTime(joinTime);
            
            assertEquals(candidate, attendanceRecord.getCandidate());
            assertEquals(session, attendanceRecord.getSession());
            assertEquals(joinTime, attendanceRecord.getJoinTime());
            assertNull(attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Update attendance record times")
        void testUpdateAttendanceRecordTimes() {
            LocalDateTime newJoinTime = new LocalDateTime(2026, 4, 10, 9, 30);
            LocalDateTime newLeaveTime = new LocalDateTime(2026, 4, 10, 11, 0);
            
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setLeaveTime(leaveTime);
            
            attendanceRecord.setJoinTime(newJoinTime);
            attendanceRecord.setLeaveTime(newLeaveTime);
            
            assertEquals(newJoinTime, attendanceRecord.getJoinTime());
            assertEquals(newLeaveTime, attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Multiple attendance records for same session")
        void testMultipleAttendanceRecordsSameSession() {
            Candidate cand1 = new Candidate("cand1", "Candidate 1", "cand1@test.com");
            Candidate cand2 = new Candidate("cand2", "Candidate 2", "cand2@test.com");
            
            AttendanceRecord record1 = new AttendanceRecord();
            AttendanceRecord record2 = new AttendanceRecord();
            
            record1.setCandidate(cand1);
            record1.setSession(session);
            record1.setJoinTime(new LocalDateTime(2026, 4, 10, 10, 0));
            record1.setLeaveTime(new LocalDateTime(2026, 4, 10, 10, 30));
            
            record2.setCandidate(cand2);
            record2.setSession(session);
            record2.setJoinTime(new LocalDateTime(2026, 4, 10, 10, 5));
            record2.setLeaveTime(new LocalDateTime(2026, 4, 10, 10, 35));
            
            assertEquals(cand1, record1.getCandidate());
            assertEquals(cand2, record2.getCandidate());
            assertEquals(session, record1.getSession());
            assertEquals(session, record2.getSession());
        }
    }

    // ========== TIME SEQUENCE SCENARIOS ==========

    @Nested
    @DisplayName("Time Sequence Scenarios")
    class TimeSequencesScenarios {

        @Test
        @DisplayName("Join time before leave time (normal scenario)")
        void testNormalTimeSequence() {
            LocalDateTime joinTime = new LocalDateTime(2026, 4, 10, 10, 0);
            LocalDateTime leaveTime = new LocalDateTime(2026, 4, 10, 10, 30);
            
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setLeaveTime(leaveTime);
            
            assertEquals(joinTime, attendanceRecord.getJoinTime());
            assertEquals(leaveTime, attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Same join and leave time (instantaneous)")
        void testSameJoinAndLeaveTime() {
            LocalDateTime sameTime = new LocalDateTime(2026, 4, 10, 10, 0);
            
            attendanceRecord.setJoinTime(sameTime);
            attendanceRecord.setLeaveTime(sameTime);
            
            assertEquals(sameTime, attendanceRecord.getJoinTime());
            assertEquals(sameTime, attendanceRecord.getLeaveTime());
        }

        @Test
        @DisplayName("Leave time before join time (edge case)")
        void testReverseTimeSequence() {
            LocalDateTime joinTime = new LocalDateTime(2026, 4, 10, 10, 30);
            LocalDateTime leaveTime = new LocalDateTime(2026, 4, 10, 10, 0);
            
            attendanceRecord.setJoinTime(joinTime);
            attendanceRecord.setLeaveTime(leaveTime);
            
            // System should allow this (business logic validation is separate)
            assertEquals(joinTime, attendanceRecord.getJoinTime());
            assertEquals(leaveTime, attendanceRecord.getLeaveTime());
        }
    }
}

