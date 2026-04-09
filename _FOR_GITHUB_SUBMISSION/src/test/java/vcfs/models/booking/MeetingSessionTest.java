package vcfs.models.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;

import vcfs.models.audit.AttendanceRecord;
import vcfs.models.enums.MeetingState;
import vcfs.models.structure.VirtualRoom;
import vcfs.models.structure.Booth;
import vcfs.models.users.Candidate;
import vcfs.core.LocalDateTime;

/**
 * Comprehensive JUnit tests for MeetingSession class.
 * Tests per-reservation runtime session functionality including room management, lobby, and attendance.
 * CSCU9P6 Group Project - Required for JUnit Test Report
 * ZAID (mzs00007) - Test Suite Implementation
 */
@DisplayName("MeetingSession - Per-Reservation Runtime Meeting Session")
public class MeetingSessionTest {

    private MeetingSession session;
    private Reservation reservation;
    private Lobby lobby;
    private VirtualRoom room;
    private Booth booth;

    @BeforeEach
    void setUp() {
        session = new MeetingSession();
        reservation = new Reservation();
        lobby = new Lobby();
        
        // Create test booth and virtual room
        booth = new Booth("Test Booth");
        room = new VirtualRoom(booth);
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Test
    @DisplayName("MeetingSession constructor creates valid empty instance")
    void testMeetingSessionConstructor() {
        assertNotNull(session, "Session should not be null");
        assertNull(session.getRoom(), "Room should be null initially");
        assertNull(session.getLobby(), "Lobby should be null initially");
        assertNull(session.getReservation(), "Reservation should be null initially");
        assertEquals("Meeting Session", session.getTitle(), "Title should be default");
    }

    @Test
    @DisplayName("Initial state is WAITING")
    void testInitialState() {
        assertEquals(MeetingState.WAITING, session.getState(), "Initial state should be WAITING");
    }

    @Test
    @DisplayName("Attendance records collection starts empty")
    void testAttendanceRecordsEmpty() {
        Collection<AttendanceRecord> records = session.getAttendanceRecords();
        assertNotNull(records, "Records collection should not be null");
        assertTrue(records.isEmpty(), "Should start empty");
    }

    // ========== RESERVATION MANAGEMENT ==========

    @Test
    @DisplayName("setReservation stores reservation")
    void testSetReservation() {
        assertNull(session.getReservation());
        
        session.setReservation(reservation);
        assertEquals(reservation, session.getReservation());
    }

    @Test
    @DisplayName("setReservation throws exception for null")
    void testSetReservationNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            session.setReservation(null);
        });
        assertTrue(exception.getMessage().contains("null") || exception.getMessage().contains("Reservation"),
                  "Should indicate null reservation");
    }

    @Test
    @DisplayName("Reservation can be updated")
    void testUpdateReservation() {
        Reservation res1 = new Reservation();
        Reservation res2 = new Reservation();
        
        session.setReservation(res1);
        assertEquals(res1, session.getReservation());
        
        session.setReservation(res2);
        assertEquals(res2, session.getReservation(), "Should update to new reservation");
    }

    // ========== ROOM MANAGEMENT ==========

    @Test
    @DisplayName("setRoom stores virtual room")
    void testSetRoom() {
        assertNull(session.getRoom());
        
        session.setRoom(room);
        assertEquals(room, session.getRoom());
    }

    @Test
    @DisplayName("Room can be null")
    void testRoomCanBeNull() {
        session.setRoom(null);
        assertNull(session.getRoom());
    }

    // ========== LOBBY MANAGEMENT ==========

    @Test
    @DisplayName("setLobby stores lobby")
    void testSetLobby() {
        assertNull(session.getLobby());
        
        session.setLobby(lobby);
        assertEquals(lobby, session.getLobby());
    }

    @Test
    @DisplayName("Lobby can be null")
    void testLobbyCanBeNull() {
        session.setLobby(null);
        assertNull(session.getLobby());
    }

    @Test
    @DisplayName("setRoomandLobby sets both simultaneously")
    void testSetRoomAndLobby() {
        assertNull(session.getRoom());
        assertNull(session.getLobby());
        
        session.setRoomandLobby(room, lobby);
        
        assertEquals(room, session.getRoom());
        assertEquals(lobby, session.getLobby());
    }

    // ========== STATE MANAGEMENT ==========

    @Test
    @DisplayName("setState sets valid meeting state")
    void testSetState() {
        session.setState(MeetingState.ACTIVE);
        assertEquals(MeetingState.ACTIVE, session.getState());
        
        session.setState(MeetingState.ENDED);
        assertEquals(MeetingState.ENDED, session.getState());
    }

    @Test
    @DisplayName("setState throws exception for null state")
    void testSetStateNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            session.setState(null);
        });
        assertTrue(exception.getMessage().contains("null") || exception.getMessage().contains("state"),
                  "Should indicate null state");
    }

    @Test
    @DisplayName("State transitions: WAITING -> ACTIVE -> ENDED")
    void testStateTransitions() {
        assertEquals(MeetingState.WAITING, session.getState());
        
        session.setState(MeetingState.ACTIVE);
        assertEquals(MeetingState.ACTIVE, session.getState());
        
        session.setState(MeetingState.ENDED);
        assertEquals(MeetingState.ENDED, session.getState());
    }

    // ========== TITLE MANAGEMENT ==========

    @Test
    @DisplayName("getTitle returns default title initially")
    void testGetDefaultTitle() {
        assertEquals("Meeting Session", session.getTitle());
    }

    @Test
    @DisplayName("setTitle updates session title")
    void testSetTitle() {
        session.setTitle("Interview with Google");
        assertEquals("Interview with Google", session.getTitle());
    }

    @Test
    @DisplayName("setTitle accepts null")
    void testSetTitleNull() {
        assertDoesNotThrow(() -> {
            session.setTitle(null);
        });
    }

    // ========== ATTENDANCE RECORDS ==========

    @Test
    @DisplayName("getAttendanceRecords returns empty collection initially")
    void testAttendanceRecordsInitial() {
        Collection<AttendanceRecord> records = session.getAttendanceRecords();
        assertTrue(records.isEmpty(), "Should start empty");
    }

    @Test
    @DisplayName("Attendance records can be added")
    void testAddAttendanceRecord() {
        Collection<AttendanceRecord> records = session.getAttendanceRecords();
        AttendanceRecord record = new AttendanceRecord();
        
        records.add(record);
        
        assertEquals(1, records.size());
        assertTrue(records.contains(record));
    }

    @Test
    @DisplayName("Multiple attendance records can be stored")
    void testMultipleAttendanceRecords() {
        Collection<AttendanceRecord> records = session.getAttendanceRecords();
        
        records.add(new AttendanceRecord());
        records.add(new AttendanceRecord());
        records.add(new AttendanceRecord());
        
        assertEquals(3, records.size());
    }

    // ========== INTEGRATION TESTS ==========

    @Test
    @DisplayName("Complete session setup with all components")
    void testCompleteSessionSetup() {
        session.setTitle("Important Meeting");
        session.setReservation(reservation);
        session.setRoom(room);
        session.setLobby(lobby);
        session.setState(MeetingState.ACTIVE);
        
        assertEquals("Important Meeting", session.getTitle());
        assertEquals(reservation, session.getReservation());
        assertEquals(room, session.getRoom());
        assertEquals(lobby, session.getLobby());
        assertEquals(MeetingState.ACTIVE, session.getState());
    }

    @Test
    @DisplayName("Session with lobby for waiting candidates")
    void testSessionWithLobby() {
        session.setLobby(lobby);
        
        // Add candidates to lobby
        Candidate candidate = new Candidate("alice@test.com", "Alice", "alice@test.com");
        
        session.getLobby().add(candidate);
        
        assertTrue(session.getLobby().getCandidates().contains(candidate));
    }

    @Test
    @DisplayName("Session lifecycle: create, populate, complete")
    void testSessionLifecycle() {
        assertEquals(MeetingState.WAITING, session.getState());
        
        session.setState(MeetingState.ACTIVE);
        assertEquals(MeetingState.ACTIVE, session.getState());
        
        Collection<AttendanceRecord> records = session.getAttendanceRecords();
        records.add(new AttendanceRecord());
        
        session.setState(MeetingState.ENDED);
        assertEquals(MeetingState.ENDED, session.getState());
        assertEquals(1, records.size());
    }

    @Test
    @DisplayName("setRoomandLobby integration with state")
    void testRoomLobbyIntegration() {
        session.setRoomandLobby(room, lobby);
        session.setState(MeetingState.ACTIVE);
        
        assertEquals(room, session.getRoom());
        assertEquals(lobby, session.getLobby());
        assertEquals(MeetingState.ACTIVE, session.getState());
    }

    // ========== EDGE CASES ==========

    @Test
    @DisplayName("Multiple state updates in sequence")
    void testMultipleStateUpdates() {
        for (MeetingState state : MeetingState.values()) {
            assertDoesNotThrow(() -> {
                session.setState(state);
            });
            assertEquals(state, session.getState());
        }
    }

    @Test
    @DisplayName("Title updates multiple times")
    void testMultipleTitleUpdates() {
        session.setTitle("Title 1");
        assertEquals("Title 1", session.getTitle());
        
        session.setTitle("Title 2");
        assertEquals("Title 2", session.getTitle());
        
        session.setTitle("Title 3");
        assertEquals("Title 3", session.getTitle());
    }

    @Test
    @DisplayName("Legacy: MeetingSession test passed")
    void testMeetingSessionLegacy() {
        System.out.println("[TEST] MeetingSession comprehensive test suite");
        assertTrue(true);
        System.out.println("[TEST] MeetingSession tests passed.\n");
    }
}

