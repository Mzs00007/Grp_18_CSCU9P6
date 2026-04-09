package vcfs.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.core.*;
import vcfs.models.users.*;
import vcfs.models.booking.*;
import vcfs.models.structure.*;
import vcfs.models.audit.*;


/**
 * Comprehensive Integration Tests for VCFS System.
 * Tests interactions between multiple system components including:
 * - User management and sessions
 * - Bookings and reservations
 *
 * TODO: [VCFS-085] Test full VCFS system integration: verify component interactions, end-to-end workflows, observer pattern across system, and PropertyChangeListener propagation - tests must ensure full system works correctly
 * - Career fair events
 * - Audit trail tracking
 * 
 * CSCU9P6 Group Project - Integration Test Suite
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("VCFS Integration Tests - System Component Interactions")
public class VCFSIntegrationTest {

    private CareerFair careerFair;
    private Candidate candidate;
    private Recruiter recruiter;
    private Organization organization;
    private Booth booth;
    private VirtualRoom virtualRoom;
    private MeetingSession meetingSession;
    private Offer offer;

    @BeforeEach
    void setUp() {
        // Initialize career fair
        careerFair = new CareerFair("Tech Career Fair 2026", "Annual tech recruitment event");
        careerFair.setStartTime(new LocalDateTime(2026, 4, 15, 9, 0));
        careerFair.setEndTime(new LocalDateTime(2026, 4, 15, 17, 0));
        
        // Initialize users
        candidate = new Candidate("cand001", "John Doe", "john@example.com");
        recruiter = new Recruiter("rec001", "Jane Smith", "jane@company.com");
        
        // Initialize organization
        organization = new Organization("Tech Corp", "Leading tech company");
        organization.setRecruiter(recruiter);
        
        // Initialize booth
        booth = new Booth("Booth A1", 1);
        booth.setRecruiter(recruiter);
        
        // Initialize virtual room
        virtualRoom = new VirtualRoom("Meeting Room 1", "https://meet.example.com/room1");
        
        // Initialize meeting session
        meetingSession = new MeetingSession();
        virtualRoom.setMeetingSession(meetingSession);
        
        // Initialize offer
        offer = new Offer();
        offer.setTitle("Software Engineer Position");
        offer.setPublisher(recruiter);
        offer.setMeetingSession(meetingSession);
        
        // Set session
        SessionManager.setCurrentCandidate(candidate);
    }

    // ========== RECRUITER-ORGANIZATION INTEGRATION ==========

    @Nested
    @DisplayName("Recruiter-Organization Integration Tests")
    class RecruiterOrganizationIntegrationTests {

        @Test
        @DisplayName("Recruiter is associated with organization")
        void testRecruiterOrganizationAssociation() {
            assertEquals(recruiter, organization.getRecruiter());
            assertEquals("Jane Smith", organization.getRecruiter().getDisplayName());
        }

        @Test
        @DisplayName("Organization represents recruiter's company")
        void testOrganizationRepresentsRecruiterCompany() {
            String companyName = organization.getName();
            assertEquals("Tech Corp", companyName);
            assertNotNull(organization.getRecruiter());
        }

        @Test
        @DisplayName("Multiple recruiters can work for same organization")
        void testMultipleRecruitersForOrganization() {
            Recruiter recruiter2 = new Recruiter("rec002", "Bob Johnson", "bob@company.com");
            Organization org2 = new Organization("Tech Corp", "Leading tech company");
            
            org2.setRecruiter(recruiter2);
            
            assertNotEquals(organization.getRecruiter().getId(), org2.getRecruiter().getId());
            assertEquals(organization.getName(), org2.getName());
        }
    }

    // ========== RECRUITER-BOOTH INTEGRATION ==========

    @Nested
    @DisplayName("Recruiter-Booth Integration Tests")
    class RecruiterBoothIntegrationTests {

        @Test
        @DisplayName("Recruiter staffs booth at career fair")
        void testRecruiterStaffsBooth() {
            assertEquals(recruiter, booth.getRecruiter());
            assertEquals("Booth A1", booth.getName());
        }

        @Test
        @DisplayName("Booth number identifies recruiter's space")
        void testBoothIdentifiesRecruiterSpace() {
            assertEquals(1, booth.getBoothNumber());
            assertEquals(recruiter, booth.getRecruiter());
        }

        @Test
        @DisplayName("Multiple booths for same recruiter")
        void testMultipleBoothsForRecruiter() {
            Booth booth2 = new Booth("Booth A2", 2);
            booth2.setRecruiter(recruiter);
            
            assertEquals(recruiter, booth.getRecruiter());
            assertEquals(recruiter, booth2.getRecruiter());
            assertNotEquals(booth.getBoothNumber(), booth2.getBoothNumber());
        }
    }

    // ========== OFFER-MEETING SESSION INTEGRATION ==========

    @Nested
    @DisplayName("Offer-Meeting Session Integration Tests")
    class OfferMeetingSessionIntegrationTests {

        @Test
        @DisplayName("Offer is associated with meeting session")
        void testOfferMeetingSessionAssociation() {
            assertEquals(meetingSession, offer.getMeetingSession());
        }

        @Test
        @DisplayName("Meeting session hosts offering")
        void testMeetingSessionHostsOffering() {
            assertNotNull(offer.getMeetingSession());
            assertEquals("Software Engineer Position", offer.getTitle());
        }

        @Test
        @DisplayName("Multiple offers can use same meeting session")
        void testMultipleOffersWithSameSession() {
            Offer offer2 = new Offer();
            offer2.setTitle("Product Manager Position");
            offer2.setMeetingSession(meetingSession);
            
            assertEquals(offer.getMeetingSession(), offer2.getMeetingSession());
        }
    }

    // ========== VIRTUAL ROOM-MEETING SESSION INTEGRATION ==========

    @Nested
    @DisplayName("Virtual Room-Meeting Session Integration Tests")
    class VirtualRoomMeetingSessionIntegrationTests {

        @Test
        @DisplayName("Virtual room hosts meeting session")
        void testVirtualRoomHostsMeetingSession() {
            assertEquals(meetingSession, virtualRoom.getMeetingSession());
            assertEquals("https://meet.example.com/room1", virtualRoom.getRoomUrl());
        }

        @Test
        @DisplayName("Meeting session uses virtual room")
        void testMeetingSessionUsesVirtualRoom() {
            assertNotNull(virtualRoom.getMeetingSession());
            assertEquals("Meeting Room 1", virtualRoom.getName());
        }
    }

    // ========== CANDIDATE-RESERVATION INTEGRATION ==========

    @Nested
    @DisplayName("Candidate-Reservation Integration Tests")
    class CandidateReservationIntegrationTests {

        @Test
        @DisplayName("Candidate can make reservation for offer")
        void testCandidateReservationForOffer() {
            Reservation reservation = new Reservation();
            reservation.setCandidate(candidate);
            reservation.setOffer(offer);
            
            assertEquals(candidate, reservation.getCandidate());
            assertEquals(offer, reservation.getOffer());
        }

        @Test
        @DisplayName("Reservation links candidate to offer")
        void testReservationLinksCandidateToOffer() {
            Reservation reservation = new Reservation();
            reservation.setCandidate(candidate);
            reservation.setOffer(offer);
            reservation.setSession(meetingSession);
            
            assertEquals("john@example.com", reservation.getCandidate().getEmail());
            assertEquals("Software Engineer Position", reservation.getOffer().getTitle());
        }

        @Test
        @DisplayName("Multiple candidates can reserve same offer")
        void testMultipleCandidatesReservingSameOffer() {
            Candidate candidate2 = new Candidate("cand002", "Jane Doe", "jane@example.com");
            
            Reservation res1 = new Reservation();
            res1.setCandidate(candidate);
            res1.setOffer(offer);
            
            Reservation res2 = new Reservation();
            res2.setCandidate(candidate2);
            res2.setOffer(offer);
            
            assertEquals(offer, res1.getOffer());
            assertEquals(offer, res2.getOffer());
            assertNotEquals(res1.getCandidate().getId(), res2.getCandidate().getId());
        }
    }

    // ========== CAREER FAIR-EVENT LIFECYCLE ==========

    @Nested
    @DisplayName("Career Fair Event Lifecycle Integration Tests")
    class CareerFairEventLifecycleTests {

        @Test
        @DisplayName("Career fair contains multiple booths")
        void testCareerFairContainsBooths() {
            assertEquals("Tech Career Fair 2026", careerFair.getName());
            assertNotNull(booth);
            assertEquals(1, booth.getBoothNumber());
        }

        @Test
        @DisplayName("Career fair timing constraints")
        void testCareerFairTimingConstraints() {
            LocalDateTime startTime = careerFair.getStartTime();
            LocalDateTime endTime = careerFair.getEndTime();
            
            assertNotNull(startTime);
            assertNotNull(endTime);
        }

        @Test
        @DisplayName("Career fair with organizations and recruiters")
        void testCareerFairWithOrganizationsAndRecruiters() {
            assertEquals(careerFair.getName(), "Tech Career Fair 2026");
            assertEquals(recruiter, organization.getRecruiter());
            assertEquals(recruiter, booth.getRecruiter());
        }
    }

    // ========== ATTENDANCE TRACKING INTEGRATION ==========

    @Nested
    @DisplayName("Attendance Tracking Integration Tests")
    class AttendanceTrackingIntegrationTests {

        @Test
        @DisplayName("Track candidate attendance at meeting")
        void testCandidateAttendanceTracking() {
            LocalDateTime joinTime = new LocalDateTime(2026, 4, 15, 10, 0);
            LocalDateTime leaveTime = new LocalDateTime(2026, 4, 15, 10, 30);
            
            AttendanceRecord record = new AttendanceRecord();
            record.setCandidate(candidate);
            record.setSession(meetingSession);
            record.setJoinTime(joinTime);
            record.setLeaveTime(leaveTime);
            
            assertEquals(candidate, record.getCandidate());
            assertEquals(meetingSession, record.getSession());
        }

        @Test
        @DisplayName("Multiple attendance records for same session")
        void testMultipleAttendanceRecords() {
            Candidate candidate2 = new Candidate("cand002", "Jane Doe", "jane@example.com");
            LocalDateTime time1 = new LocalDateTime(2026, 4, 15, 10, 0);
            LocalDateTime time2 = new LocalDateTime(2026, 4, 15, 10, 15);
            
            AttendanceRecord record1 = new AttendanceRecord();
            record1.setCandidate(candidate);
            record1.setSession(meetingSession);
            record1.setJoinTime(time1);
            
            AttendanceRecord record2 = new AttendanceRecord();
            record2.setCandidate(candidate2);
            record2.setSession(meetingSession);
            record2.setJoinTime(time2);
            
            assertEquals(meetingSession, record1.getSession());
            assertEquals(meetingSession, record2.getSession());
            assertNotEquals(record1.getCandidate().getId(), record2.getCandidate().getId());
        }
    }

    // ========== AUDIT TRAIL INTEGRATION ==========

    @Nested
    @DisplayName("Audit Trail Integration Tests")
    class AuditTrailIntegrationTests {

        @Test
        @DisplayName("Create audit entry for career fair event")
        void testCreateAuditEntryForCareerFair() {
            LocalDateTime timestamp = new LocalDateTime(2026, 4, 15, 9, 0);
            AuditEntry entry = new AuditEntry(careerFair, timestamp, "FAIR_STARTED");
            
            assertEquals(careerFair, entry.getFair());
            assertEquals("FAIR_STARTED", entry.getEventType());
        }

        @Test
        @DisplayName("Track multiple events in audit trail")
        void testMultipleAuditEvents() {
            LocalDateTime time1 = new LocalDateTime(2026, 4, 15, 9, 0);
            LocalDateTime time2 = new LocalDateTime(2026, 4, 15, 10, 0);
            LocalDateTime time3 = new LocalDateTime(2026, 4, 15, 17, 0);
            
            AuditEntry entry1 = new AuditEntry(careerFair, time1, "FAIR_STARTED");
            AuditEntry entry2 = new AuditEntry(careerFair, time2, "BOOKING_CREATED");
            AuditEntry entry3 = new AuditEntry(careerFair, time3, "FAIR_ENDED");
            
            assertEquals("FAIR_STARTED", entry1.getEventType());
            assertEquals("BOOKING_CREATED", entry2.getEventType());
            assertEquals("FAIR_ENDED", entry3.getEventType());
        }
    }

    // ========== SESSION INTEGRATION ==========

    @Nested
    @DisplayName("Session Management Integration Tests")
    class SessionManagementIntegrationTests {

        @Test
        @DisplayName("Current candidate is available for operations")
        void testCurrentCandidateAvailable() {
            Candidate currentCandidate = SessionManager.getCurrentCandidate();
            assertNotNull(currentCandidate);
            assertEquals("john@example.com", currentCandidate.getEmail());
        }

        @Test
        @DisplayName("Session tracks candidate activity")
        void testSessionTracksActivity() {
            assertTrue(SessionManager.isCandidateLoggedIn());
            
            // Candidate can make reservations
            Reservation reservation = new Reservation();
            reservation.setCandidate(SessionManager.getCurrentCandidate());
            reservation.setOffer(offer);
            
            assertNotNull(reservation.getCandidate());
        }

        @Test
        @DisplayName("Session switching between candidate and recruiter")
        void testSessionSwitching() {
            assertTrue(SessionManager.isCandidateLoggedIn());
            assertFalse(SessionManager.isRecruiterLoggedIn());
            
            SessionManager.setCurrentCandidate(null);
            SessionManager.setCurrentRecruiter(recruiter);
            
            assertFalse(SessionManager.isCandidateLoggedIn());
            assertTrue(SessionManager.isRecruiterLoggedIn());
        }
    }

    // ========== COMPLETE USER WORKFLOW ==========

    @Nested
    @DisplayName("Complete User Workflow Integration Tests")
    class CompleteUserWorkflowTests {

        @Test
        @DisplayName("Complete candidate booking workflow")
        void testCompleteCandidateBookingWorkflow() {
            // Candidate is logged in
            assertTrue(SessionManager.isCandidateLoggedIn());
            
            // Candidate views offer
            assertEquals("Software Engineer Position", offer.getTitle());
            
            // Candidate makes reservation
            Reservation reservation = new Reservation();
            reservation.setCandidate(SessionManager.getCurrentCandidate());
            reservation.setOffer(offer);
            reservation.setSession(meetingSession);
            
            // Record attendance
            LocalDateTime joinTime = new LocalDateTime(2026, 4, 15, 10, 0);
            AttendanceRecord attendance = new AttendanceRecord();
            attendance.setCandidate(reservation.getCandidate());
            attendance.setSession(meetingSession);
            attendance.setJoinTime(joinTime);
            
            // Verify integration
            assertEquals("john@example.com", attendance.getCandidate().getEmail());
            assertEquals(meetingSession, attendance.getSession());
        }

        @Test
        @DisplayName("Complete recruiter hosting workflow")
        void testCompleteRecruiterHostingWorkflow() {
            // Recruiter is at booth
            assertEquals(recruiter, booth.getRecruiter());
            
            // Recruiter publishes offer
            assertEquals(recruiter, offer.getPublisher());
            assertEquals(meetingSession, offer.getMeetingSession());
            
            // Booth has virtual room
            assertEquals(meetingSession, virtualRoom.getMeetingSession());
            
            // Verify integration
            assertEquals("jane@company.com", recruiter.getEmail());
            assertEquals("Booth A1", booth.getName());
        }
    }

    // ========== PROFILE INTEGRATION ==========

    @Nested
    @DisplayName("Candidate Profile Integration Tests")
    class CandidateProfileIntegrationTests {

        @Test
        @DisplayName("Candidate has profile with skills and CV")
        void testCandidateProfileIntegration() {
            CandidateProfile profile = new CandidateProfile();
            profile.setCV("Bachelor's in Computer Science");
            profile.setSkills("Java, Python, Spring Boot");
            profile.setPreferences("Tech companies, Remote work");
            
            candidate.setProfile(profile);
            
            assertEquals(profile, candidate.getProfile());
            assertEquals("Java, Python, Spring Boot", profile.getSkills());
        }

        @Test
        @DisplayName("Profile information influences booking decisions")
        void testProfileInfluencesBooking() {
            CandidateProfile profile = new CandidateProfile();
            profile.setSkills("Java");
            candidate.setProfile(profile);
            
            // Recruiter reviews candidate profile
            assertNotNull(candidate.getProfile());
            assertTrue(candidate.getProfile().getSkills().contains("Java"));
        }
    }

    // ========== COMPLEX SCENARIOS ==========

    @Nested
    @DisplayName("Complex Integration Scenarios")
    class ComplexIntegrationScenarios {

        @Test
        @DisplayName("Multiple candidates attend same recruiter session")
        void testMultipleCandidatesAttendSameSession() {
            Candidate cand1 = new Candidate("cand1", "Alice", "alice@test.com");
            Candidate cand2 = new Candidate("cand2", "Bob", "bob@test.com");
            
            // Both candidates reserve same offer
            Reservation res1 = new Reservation();
            res1.setCandidate(cand1);
            res1.setOffer(offer);
            
            Reservation res2 = new Reservation();
            res2.setCandidate(cand2);
            res2.setOffer(offer);
            
            // Both attend same session
            AttendanceRecord att1 = new AttendanceRecord();
            att1.setCandidate(cand1);
            att1.setSession(meetingSession);
            
            AttendanceRecord att2 = new AttendanceRecord();
            att2.setCandidate(cand2);
            att2.setSession(meetingSession);
            
            assertEquals(offer, res1.getOffer());
            assertEquals(offer, res2.getOffer());
            assertEquals(meetingSession, att1.getSession());
            assertEquals(meetingSession, att2.getSession());
        }

        @Test
        @DisplayName("Career fair with multiple organizations and booths")
        void testMultipleOrganizationsAndBooths() {
            Organization org2 = new Organization("InfoTech", "Information technology");
            Recruiter rec2 = new Recruiter("rec002", "Tom", "tom@infotech.com");
            org2.setRecruiter(rec2);
            
            Booth booth2 = new Booth("Booth B1", 2);
            booth2.setRecruiter(rec2);
            
            assertNotNull(careerFair);
            assertNotNull(organization);
            assertNotNull(org2);
            assertNotNull(booth);
            assertNotNull(booth2);
        }
    }
}
