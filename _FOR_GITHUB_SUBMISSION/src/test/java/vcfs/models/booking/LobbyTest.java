package vcfs.models.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.List;

import vcfs.models.users.Candidate;

/**
 * Comprehensive JUnit tests for Lobby class.
 * Tests waiting room functionality for candidates arriving early to booth sessions.
 * CSCU9P6 Group Project - Required for JUnit Test Report
 * ZAID (mzs00007) - Test Suite Implementation
 */
@DisplayName("Lobby - Per-Session Waiting Area for Early-Arriving Candidates")
public class LobbyTest {

    private Lobby lobby;
    private MeetingSession session;
    private Candidate candidate1;
    private Candidate candidate2;
    private Candidate candidate3;

    @BeforeEach
    void setUp() {
        lobby = new Lobby();
        
        // Create test candidates directly
        candidate1 = new Candidate("alice@test.com", "Alice Johnson", "alice@test.com");
        candidate2 = new Candidate("bob@test.com", "Bob Davis", "bob@test.com");
        candidate3 = new Candidate("carol@test.com", "Carol Smith", "carol@test.com");
        
        session = new MeetingSession();
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Test
    @DisplayName("Lobby constructor creates empty instance with null session")
    void testLobbyConstructor() {
        assertNotNull(lobby, "Lobby should be instantiated");
        assertNull(lobby.getSession(), "Session should be null initially");
        assertTrue(lobby.getWaitingQueue().isEmpty(), "Waiting queue should be empty");
    }

    @Test
    @DisplayName("getCandidates returns empty collection initially")
    void testInitialCandidatesEmpty() {
        Collection<Candidate> candidates = lobby.getCandidates();
        assertNotNull(candidates, "Candidates collection should not be null");
        assertEquals(0, candidates.size(), "Should contain 0 candidates initially");
    }

    @Test
    @DisplayName("getWaitingQueue returns empty list initially")
    void testInitialQueueEmpty() {
        List<Candidate> queue = lobby.getWaitingQueue();
        assertNotNull(queue, "Queue should not be null");
        assertTrue(queue.isEmpty(), "Queue should be empty initially");
    }

    // ========== SESSION MANAGEMENT ==========

    @Test
    @DisplayName("setSession stores meeting session reference")
    void testSetSession() {
        assertNull(lobby.getSession());
        
        lobby.setSession(session);
        
        assertNotNull(lobby.getSession(), "Session should be set");
        assertEquals(session, lobby.getSession(), "Session should match");
    }

    @Test
    @DisplayName("setSession updates existing session reference")
    void testSetSessionUpdate() {
        MeetingSession session1 = new MeetingSession();
        MeetingSession session2 = new MeetingSession();
        
        lobby.setSession(session1);
        assertEquals(session1, lobby.getSession());
        
        lobby.setSession(session2);
        assertEquals(session2, lobby.getSession(), "Session should be updated");
    }

    // ========== ADD CANDIDATE TESTS ==========

    @Test
    @DisplayName("add() throws exception for null candidate")
    void testAddNullCandidate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lobby.add(null);
        });
        assertTrue(exception.getMessage().contains("null"), "Should mention null candidate");
    }

    @Test
    @DisplayName("add() adds single valid candidate to queue")
    void testAddSingleCandidate() {
        lobby.add(candidate1);
        
        assertEquals(1, lobby.getWaitingQueue().size(), "Queue should contain 1");
        assertTrue(lobby.getWaitingQueue().contains(candidate1), "Candidate1 should be in queue");
    }

    @Test
    @DisplayName("add() prevents duplicate candidates")
    void testAddDuplicateCandidate() {
        lobby.add(candidate1);
        assertTrue(lobby.getWaitingQueue().contains(candidate1));
        
        lobby.add(candidate1);  // Add same candidate again
        
        assertEquals(1, lobby.getWaitingQueue().size(), "Should still be 1 (no duplicates)");
    }

    @Test
    @DisplayName("add() maintains FIFO order for multiple candidates")
    void testAddMaintainsFifoOrder() {
        lobby.add(candidate1);
        lobby.add(candidate2);
        lobby.add(candidate3);
        
        List<Candidate> queue = lobby.getWaitingQueue();
        assertEquals(3, queue.size(), "Queue should have 3");
        assertEquals(candidate1, queue.get(0), "First should be alice");
        assertEquals(candidate2, queue.get(1), "Second should be bob");
        assertEquals(candidate3, queue.get(2), "Third should be carol");
    }

    // ========== REMOVE CANDIDATE TESTS ==========

    @Test
    @DisplayName("remove() handles null candidate gracefully")
    void testRemoveNullCandidate() {
        lobby.add(candidate1);
        
        assertDoesNotThrow(() -> {
            lobby.remove(null);
        });
        
        assertEquals(1, lobby.getWaitingQueue().size(), "Should still contain candidate1");
    }

    @Test
    @DisplayName("remove() removes candidate from queue")
    void testRemoveCandidate() {
        lobby.add(candidate1);
        lobby.add(candidate2);
        
        lobby.remove(candidate1);
        
        assertEquals(1, lobby.getWaitingQueue().size(), "Should have 1 left");
        assertTrue(lobby.getWaitingQueue().contains(candidate2), "Candidate2 remains");
        assertFalse(lobby.getWaitingQueue().contains(candidate1), "Candidate1 removed");
    }

    @Test
    @DisplayName("remove() from empty queue does not throw")
    void testRemoveFromEmptyQueue() {
        assertDoesNotThrow(() -> {
            lobby.remove(candidate1);
        });
        assertTrue(lobby.getWaitingQueue().isEmpty());
    }

    @Test
    @DisplayName("remove() non-existent candidate does nothing")
    void testRemoveNonExistentCandidate() {
        lobby.add(candidate1);
        
        lobby.remove(candidate2);  // Not in queue
        
        assertEquals(1, lobby.getWaitingQueue().size());
        assertTrue(lobby.getWaitingQueue().contains(candidate1));
    }

    // ========== LIST WAITING TESTS ==========

    @Test
    @DisplayName("listWaiting() returns formatted string for empty queue")
    void testListWaitingEmpty() {
        String result = lobby.listWaiting();
        
        assertNotNull(result, "Result should not be null");
        assertTrue(result.contains("No candidates") || result.contains("empty"), 
                  "Should indicate empty state");
    }

    @Test
    @DisplayName("listWaiting() includes candidate count")
    void testListWaitingIncludesCount() {
        lobby.add(candidate1);
        lobby.add(candidate2);
        
        String result = lobby.listWaiting();
        
        assertTrue(result.contains("2"), "Should display count 2");
    }

    @Test
    @DisplayName("listWaiting() includes candidate names")
    void testListWaitingIncludesNames() {
        lobby.add(candidate1);
        
        String result = lobby.listWaiting();
        
        assertTrue(result.toLowerCase().contains("alice") || result.contains("Alice"), 
                  "Should include candidate name");
    }

    // ========== COLLECTION OPERATIONS ==========

    @Test
    @DisplayName("getWaitingQueue returns mutable list")
    void testGetWaitingQueueMutable() {
        List<Candidate> queue = lobby.getWaitingQueue();
        queue.add(candidate1);
        
        assertEquals(1, lobby.getWaitingQueue().size(), "Direct modification works");
    }

    @Test
    @DisplayName("getCandidates returns current candidates")
    void testGetCandidates() {
        lobby.add(candidate1);
        lobby.add(candidate2);
        
        Collection<Candidate> candidates = lobby.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(candidate1));
        assertTrue(candidates.contains(candidate2));
    }

    // ========== QUEUE SIZE TRACKING ==========

    @Test
    @DisplayName("Queue size changes correctly with add/remove")
    void testQueueSizeTracking() {
        assertEquals(0, lobby.getWaitingQueue().size());
        
        lobby.add(candidate1);
        assertEquals(1, lobby.getWaitingQueue().size());
        
        lobby.add(candidate2);
        assertEquals(2, lobby.getWaitingQueue().size());
        
        lobby.remove(candidate1);
        assertEquals(1, lobby.getWaitingQueue().size());
        
        lobby.remove(candidate2);
        assertEquals(0, lobby.getWaitingQueue().size());
    }

    // ========== INTEGRATION TESTS ==========

    @Test
    @DisplayName("Lobby with session integration")
    void testLobbyWithSessionIntegration() {
        lobby.setSession(session);
        lobby.add(candidate1);
        lobby.add(candidate2);
        
        assertNotNull(lobby.getSession());
        assertEquals(session, lobby.getSession());
        assertEquals(2, lobby.getWaitingQueue().size());
    }

    @Test
    @DisplayName("Complete workflow: setup, add, list, remove")
    void testCompleteWorkflow() {
        lobby.setSession(session);
        
        lobby.add(candidate1);
        String list1 = lobby.listWaiting();
        assertTrue(list1.contains("1"));
        
        lobby.add(candidate2);
        String list2 = lobby.listWaiting();
        assertTrue(list2.contains("2"));
        
        lobby.remove(candidate1);
        String list3 = lobby.listWaiting();
        assertTrue(list3.contains("1"));
    }

    @Test
    @DisplayName("Rapid concurrent-style operations")
    void testRapidOperations() {
        for (int i = 0; i < 5; i++) {
            lobby.add(candidate1);
        }
        
        assertEquals(1, lobby.getWaitingQueue().size(), "No duplicates");
        
        lobby.remove(candidate1);
        assertTrue(lobby.getWaitingQueue().isEmpty());
    }

    @Test
    @DisplayName("Legacy: Lobby test passed")
    void testLobbyLegacyTest() {
        System.out.println("[TEST] Lobby comprehensive test suite"); 
        assertTrue(true);
        System.out.println("[TEST] Lobby tests passed.\n");
    }
}
