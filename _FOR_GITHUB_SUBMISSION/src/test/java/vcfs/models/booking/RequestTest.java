package vcfs.models.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import vcfs.models.users.Candidate;
import java.util.Collection;

@DisplayName("Request - Candidate Booking Request Model")
public class RequestTest {

    private Request request;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        request = new Request();
        candidate = new Candidate("candidate@test.com", "Test Candidate", "pass123");
    }

    @Test
    @DisplayName("Request constructor creates valid instance")
    void testRequest_Constructor() {
        assertNotNull(request);
    }

    @Test
    @DisplayName("setCandidate and getCandidate work correctly")
    void testCandidate() {
        assertNull(request.getCandidate());
        
        request.setCandidate(candidate);
        assertEquals(candidate, request.getCandidate());
    }

    @Test
    @DisplayName("setDesiredTags and getDesiredTags work correctly")
    void testDesiredTags() {
        String tags = "Java,Kotlin,Spring";
        request.setDesiredTags(tags);
        assertEquals(tags, request.getDesiredTags());
    }

    @Test
    @DisplayName("setDesiredTags with empty string is handled")
    void testDesiredTags_Empty() {
        request.setDesiredTags("");
        assertEquals("", request.getDesiredTags());
    }

    @Test
    @DisplayName("setDesiredTags with null is handled")
    void testDesiredTags_Null() {
        request.setDesiredTags(null);
        assertNull(request.getDesiredTags());
    }

    @Test
    @DisplayName("setPreferredOrganizations and getPreferredOrganizations work correctly")
    void testPreferredOrganizations() {
        String orgs = "Google,Microsoft,Apple";
        request.setPreferredOrganizations(orgs);
        assertEquals(orgs, request.getPreferredOrganizations());
    }

    @Test
    @DisplayName("setPreferredOrganizations with empty string is handled")
    void testPreferredOrganizations_Empty() {
        request.setPreferredOrganizations("");
        assertEquals("", request.getPreferredOrganizations());
    }

    @Test
    @DisplayName("setPreferredOrganizations with null is handled")
    void testPreferredOrganizations_Null() {
        request.setPreferredOrganizations(null);
        assertNull(request.getPreferredOrganizations());
    }

    @Test
    @DisplayName("setMaxAppointments and getMaxAppointments work correctly")
    void testMaxAppointments() {
        request.setMaxAppointments(5);
        assertEquals(5, request.getMaxAppointments());
        
        request.setMaxAppointments(1);
        assertEquals(1, request.getMaxAppointments());
        
        request.setMaxAppointments(10);
        assertEquals(10, request.getMaxAppointments());
    }

    @Test
    @DisplayName("Multiple properties can be set together")
    void testMultipleProperties() {
        request.setCandidate(candidate);
        request.setDesiredTags("Python,C++,Rust");
        request.setPreferredOrganizations("Tesla,SpaceX");
        request.setMaxAppointments(3);
        
        assertEquals(candidate, request.getCandidate());
        assertEquals("Python,C++,Rust", request.getDesiredTags());
        assertEquals("Tesla,SpaceX", request.getPreferredOrganizations());
        assertEquals(3, request.getMaxAppointments());
    }

    @Test
    @DisplayName("Request with null candidate is handled")
    void testRequest_NullCandidate() {
        request.setCandidate(null);
        assertNull(request.getCandidate());
    }

    @Test
    @DisplayName("Request maintains state across multiple calls")
    void testRequest_StateConsistency() {
        request.setDesiredTags("Initial Tags");
        assertEquals("Initial Tags", request.getDesiredTags());
        
        request.setDesiredTags("Updated Tags");
        assertEquals("Updated Tags", request.getDesiredTags());
    }

    @Test
    @DisplayName("Request with combination of tags and organizations")
    void testRequest_TagsAndOrganizations() {
        request.setDesiredTags("Full Stack,Cloud");
        request.setPreferredOrganizations("Amazon,Google");
        request.setMaxAppointments(4);
        
        assertEquals("Full Stack,Cloud", request.getDesiredTags());
        assertEquals("Amazon,Google", request.getPreferredOrganizations());
        assertEquals(4, request.getMaxAppointments());
    }

    @Test
    @DisplayName("Request can be created for multiple candidates without interference")
    void testRequest_MultipleInstances() {
        Request request1 = new Request();
        Request request2 = new Request();
        Candidate candidate1 = new Candidate("cand1@test.com", "Candidate 1", "pass");
        Candidate candidate2 = new Candidate("cand2@test.com", "Candidate 2", "pass");
        
        request1.setCandidate(candidate1);
        request2.setCandidate(candidate2);
        
        assertEquals(candidate1, request1.getCandidate());
        assertEquals(candidate2, request2.getCandidate());
        assertNotEquals(request1.getCandidate(), request2.getCandidate());
    }

    @Test
    @DisplayName("Request with very long tag strings")
    void testRequest_LongTags() {
        String longTags = "Java,Python,C++,JavaScript,TypeScript,Go,Rust,Kotlin,Swift,C#,VB.NET,PHP,Ruby,Perl,Scala";
        request.setDesiredTags(longTags);
        assertEquals(longTags, request.getDesiredTags());
    }

    @Test
    @DisplayName("Request with special characters in tags")
    void testRequest_SpecialCharacters() {
        String tagsWithSpecial = "Java/Spring,Python-Django,C++/STL";
        request.setDesiredTags(tagsWithSpecial);
        assertEquals(tagsWithSpecial, request.getDesiredTags());
    }
}

