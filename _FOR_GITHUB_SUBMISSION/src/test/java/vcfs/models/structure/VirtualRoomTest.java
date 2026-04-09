package vcfs.models.structure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.models.booking.MeetingSession;


/**
 * Comprehensive JUnit tests for VirtualRoom class.
 * Tests virtual room model for candidate-recruiter meetings.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("VirtualRoom - Virtual Meeting Room Model")
public class VirtualRoomTest {

    private VirtualRoom virtualRoom;
    private MeetingSession meetingSession;
    private static final String VALID_ROOM_NAME = "Virtual Room A";
    private static final String VALID_ROOM_URL = "https://meet.example.com/room1";

    @BeforeEach
    void setUp() {
        virtualRoom = new VirtualRoom(VALID_ROOM_NAME, VALID_ROOM_URL);
        meetingSession = new MeetingSession();
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("VirtualRoom constructor initializes with name and URL")
        void testConstructorValidParameters() {
            assertNotNull(virtualRoom);
            assertEquals(VALID_ROOM_NAME, virtualRoom.getName());
            assertEquals(VALID_ROOM_URL, virtualRoom.getRoomUrl());
        }

        @Test
        @DisplayName("VirtualRoom with various names")
        void testConstructorVariousNames() {
            String[] names = {"Room 1", "Meeting Room A", "Interview Room", 
                            "Conference Hall", "Private Session"};
            for (String name : names) {
                VirtualRoom testRoom = new VirtualRoom(name, "https://example.com");
                assertEquals(name, testRoom.getName());
            }
        }

        @Test
        @DisplayName("VirtualRoom with various URLs")
        void testConstructorVariousURLs() {
            String[] urls = {
                "https://zoom.us/meeting123",
                "https://teams.microsoft.com/meetingid123",
                "https://meet.google.com/xyz-abc-def",
                "https://jitsi.example.com/room1"
            };
            for (String url : urls) {
                VirtualRoom testRoom = new VirtualRoom("Room", url);
                assertEquals(url, testRoom.getRoomUrl());
            }
        }
    }

    // ========== ROOM NAME MANAGEMENT ==========

    @Nested
    @DisplayName("Room Name Management Tests")
    class RoomNameManagementTests {

        @Test
        @DisplayName("getName returns room name")
        void testGetName() {
            assertEquals(VALID_ROOM_NAME, virtualRoom.getName());
        }

        @Test
        @DisplayName("setName updates room name")
        void testSetName() {
            String newName = "Updated Room Name";
            virtualRoom.setName(newName);
            assertEquals(newName, virtualRoom.getName());
        }

        @Test
        @DisplayName("setName with various names")
        void testSetNameVariousNames() {
            virtualRoom.setName("Conference Room 1");
            assertEquals("Conference Room 1", virtualRoom.getName());
            
            virtualRoom.setName("Interview Space 2");
            assertEquals("Interview Space 2", virtualRoom.getName());
        }

        @Test
        @DisplayName("setName with empty string")
        void testSetNameEmpty() {
            virtualRoom.setName("");
            assertEquals("", virtualRoom.getName());
        }
    }

    // ========== ROOM URL MANAGEMENT ==========

    @Nested
    @DisplayName("Room URL Management Tests")
    class RoomURLManagementTests {

        @Test
        @DisplayName("getRoomUrl returns correct URL")
        void testGetRoomUrl() {
            assertEquals(VALID_ROOM_URL, virtualRoom.getRoomUrl());
        }

        @Test
        @DisplayName("setRoomUrl updates room URL")
        void testSetRoomUrl() {
            String newUrl = "https://meet.example.com/room2";
            virtualRoom.setRoomUrl(newUrl);
            assertEquals(newUrl, virtualRoom.getRoomUrl());
        }

        @Test
        @DisplayName("setRoomUrl with various URLs")
        void testSetRoomUrlVariousURLs() {
            virtualRoom.setRoomUrl("https://zoom.us/newmeeting");
            assertEquals("https://zoom.us/newmeeting", virtualRoom.getRoomUrl());
            
            virtualRoom.setRoomUrl("https://teams.microsoft.com/newcall");
            assertEquals("https://teams.microsoft.com/newcall", virtualRoom.getRoomUrl());
        }

        @Test
        @DisplayName("setRoomUrl with complex URLs")
        void testSetRoomUrlComplex() {
            String complexUrl = "https://example.com:8080/meeting?id=12345&token=xyz&lang=en";
            virtualRoom.setRoomUrl(complexUrl);
            assertEquals(complexUrl, virtualRoom.getRoomUrl());
        }

        @Test
        @DisplayName("setRoomUrl with http protocol")
        void testSetRoomUrlHttp() {
            String httpUrl = "http://example.com/room";
            virtualRoom.setRoomUrl(httpUrl);
            assertEquals(httpUrl, virtualRoom.getRoomUrl());
        }
    }

    // ========== MEETING SESSION MANAGEMENT ==========

    @Nested
    @DisplayName("Meeting Session Management Tests")
    class MeetingSessionManagementTests {

        @Test
        @DisplayName("setMeetingSession associates meeting session")
        void testSetMeetingSession() {
            virtualRoom.setMeetingSession(meetingSession);
            assertEquals(meetingSession, virtualRoom.getMeetingSession());
        }

        @Test
        @DisplayName("setMeetingSession can be set to null")
        void testSetMeetingSessionNull() {
            virtualRoom.setMeetingSession(meetingSession);
            virtualRoom.setMeetingSession(null);
            assertNull(virtualRoom.getMeetingSession());
        }

        @Test
        @DisplayName("setMeetingSession with different sessions")
        void testSetMeetingSessionDifferentSessions() {
            MeetingSession session1 = new MeetingSession();
            MeetingSession session2 = new MeetingSession();
            
            virtualRoom.setMeetingSession(session1);
            assertEquals(session1, virtualRoom.getMeetingSession());
            
            virtualRoom.setMeetingSession(session2);
            assertEquals(session2, virtualRoom.getMeetingSession());
        }

        @Test
        @DisplayName("getMeetingSession returns associated session")
        void testGetMeetingSession() {
            virtualRoom.setMeetingSession(meetingSession);
            assertEquals(meetingSession, virtualRoom.getMeetingSession());
        }
    }

    // ========== INDEPENDENT FIELD UPDATES ==========

    @Nested
    @DisplayName("Independent Field Updates Tests")
    class IndependentFieldUpdatesTests {

        @Test
        @DisplayName("Updating name does not affect URL or session")
        void testUpdateNameIndependent() {
            virtualRoom.setMeetingSession(meetingSession);
            String originalUrl = virtualRoom.getRoomUrl();
            
            virtualRoom.setName("New Name");
            
            assertEquals(originalUrl, virtualRoom.getRoomUrl());
            assertEquals(meetingSession, virtualRoom.getMeetingSession());
        }

        @Test
        @DisplayName("Updating URL does not affect name or session")
        void testUpdateUrlIndependent() {
            virtualRoom.setMeetingSession(meetingSession);
            String originalName = virtualRoom.getName();
            
            virtualRoom.setRoomUrl("https://newurl.com/room");
            
            assertEquals(originalName, virtualRoom.getName());
            assertEquals(meetingSession, virtualRoom.getMeetingSession());
        }

        @Test
        @DisplayName("Updating session does not affect name or URL")
        void testUpdateSessionIndependent() {
            String originalName = virtualRoom.getName();
            String originalUrl = virtualRoom.getRoomUrl();
            
            virtualRoom.setMeetingSession(meetingSession);
            
            assertEquals(originalName, virtualRoom.getName());
            assertEquals(originalUrl, virtualRoom.getRoomUrl());
        }
    }

    // ========== MULTIPLE VIRTUAL ROOMS ==========

    @Nested
    @DisplayName("Multiple Virtual Rooms Tests")
    class MultipleVirtualRoomsTests {

        @Test
        @DisplayName("Multiple virtual rooms maintain independent state")
        void testMultipleRoomsIndependent() {
            VirtualRoom room1 = new VirtualRoom("Room 1", "https://example.com/room1");
            VirtualRoom room2 = new VirtualRoom("Room 2", "https://example.com/room2");
            
            MeetingSession session1 = new MeetingSession();
            MeetingSession session2 = new MeetingSession();
            
            room1.setMeetingSession(session1);
            room2.setMeetingSession(session2);
            
            assertEquals("Room 1", room1.getName());
            assertEquals("Room 2", room2.getName());
            assertEquals(session1, room1.getMeetingSession());
            assertEquals(session2, room2.getMeetingSession());
        }

        @Test
        @DisplayName("Create multiple virtual rooms in series")
        void testCreateMultipleVirtualRooms() {
            VirtualRoom room1 = new VirtualRoom("Room A", "https://example.com/a");
            VirtualRoom room2 = new VirtualRoom("Room B", "https://example.com/b");
            VirtualRoom room3 = new VirtualRoom("Room C", "https://example.com/c");
            
            assertEquals("Room A", room1.getName());
            assertEquals("Room B", room2.getName());
            assertEquals("Room C", room3.getName());
        }
    }

    // ========== COMPLETE VIRTUAL ROOM LIFECYCLE ==========

    @Nested
    @DisplayName("Complete Virtual Room Lifecycle Tests")
    class VirtualRoomLifecycleTests {

        @Test
        @DisplayName("Virtual room creation with full configuration")
        void testCompleteVirtualRoomConfiguration() {
            VirtualRoom testRoom = new VirtualRoom("Premium Room", 
                                                  "https://premium.example.com/room123");
            MeetingSession session = new MeetingSession();
            
            testRoom.setMeetingSession(session);
            
            assertEquals("Premium Room", testRoom.getName());
            assertEquals("https://premium.example.com/room123", testRoom.getRoomUrl());
            assertEquals(session, testRoom.getMeetingSession());
        }

        @Test
        @DisplayName("Update all virtual room fields")
        void testUpdateAllVirtualRoomFields() {
            VirtualRoom testRoom = new VirtualRoom("Original Room", 
                                                  "https://original.com/room");
            MeetingSession sessionOld = new MeetingSession();
            MeetingSession sessionNew = new MeetingSession();
            
            // Initial setup
            testRoom.setMeetingSession(sessionOld);
            
            // Update all fields
            testRoom.setName("Updated Room");
            testRoom.setRoomUrl("https://updated.com/room");
            testRoom.setMeetingSession(sessionNew);
            
            assertEquals("Updated Room", testRoom.getName());
            assertEquals("https://updated.com/room", testRoom.getRoomUrl());
            assertEquals(sessionNew, testRoom.getMeetingSession());
        }
    }

    // ========== EDGE CASES ==========

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("VirtualRoom with special characters in name")
        void testSpecialCharactersInName() {
            VirtualRoom testRoom = new VirtualRoom("Room A-1 (Premium) & Meeting", 
                                                  "https://example.com");
            assertEquals("Room A-1 (Premium) & Meeting", testRoom.getName());
        }

        @Test
        @DisplayName("VirtualRoom with very long name")
        void testVeryLongRoomName() {
            String longName = "Premium Virtual Conference Room for Technology Innovation " +
                            "and Strategic Business Development Meetings";
            VirtualRoom testRoom = new VirtualRoom(longName, "https://example.com");
            assertEquals(longName, testRoom.getName());
        }

        @Test
        @DisplayName("VirtualRoom with URL containing parameters")
        void testURLWithParameters() {
            String urlWithParams = "https://meet.example.com/room?id=123&auth=token123&lang=en";
            VirtualRoom testRoom = new VirtualRoom("Room", urlWithParams);
            assertEquals(urlWithParams, testRoom.getRoomUrl());
        }

        @Test
        @DisplayName("VirtualRoom with URL containing port")
        void testURLWithPort() {
            String urlWithPort = "https://example.com:8443/meeting/room123";
            VirtualRoom testRoom = new VirtualRoom("Room", urlWithPort);
            assertEquals(urlWithPort, testRoom.getRoomUrl());
        }

        @Test
        @DisplayName("VirtualRoom with numeric identifier in name")
        void testNumericIdentifierInName() {
            VirtualRoom testRoom = new VirtualRoom("Room 2026 Session 001", 
                                                  "https://example.com");
            assertEquals("Room 2026 Session 001", testRoom.getName());
        }

        @Test
        @DisplayName("VirtualRoom with empty URL")
        void testEmptyURL() {
            VirtualRoom testRoom = new VirtualRoom("Room", "");
            assertEquals("", testRoom.getRoomUrl());
        }
    }
}

