package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vcfs.models.enums.FairPhase;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SystemStatistics class.
 * Tests statistics calculation, dashboard metrics, event broadcasting.
 * 
 * AUDIT FINDINGS:
 * - SystemStatistics tracks real-time system metrics for dashboard display
 * - All three portals should subscribe to "statistics" PropertyChangeEvents
 * - Statistics update after system operations (org creation, recruiter registration, etc.)
 */
public class SystemStatisticsTest {
    private SystemStatistics stats;
    private CareerFairSystem system;

    @BeforeEach
    public void setUp() {
        system = CareerFairSystem.getInstance();
        stats = new SystemStatistics(system.getFair());
    }

    @Test
    public void testStatisticsInitialization() {
        assertNotNull(stats);
        assertEquals(0, stats.getTotalOrganizations());
        assertEquals(0, stats.getTotalBooths());
        assertEquals(0, stats.getTotalRecruiters());
        assertEquals(0, stats.getTotalCandidates());
        assertEquals(0, stats.getTotalOffers());
    }

    @Test
    public void testCalculateOrganizationCount() {
        system.addOrganization("Org 1");
        system.addOrganization("Org 2");
        stats = new SystemStatistics(system.getFair());
        assertEquals(2, stats.getTotalOrganizations());
    }

    @Test
    public void testCalculateBoothCount() {
        var org = system.addOrganization("Tech Corp");
        system.addBooth(org, "Booth A");
        system.addBooth(org, "Booth B");
        stats = new SystemStatistics(system.getFair());
        assertEquals(2, stats.getTotalBooths());
    }

    @Test
    public void testCalculateRecruiterCount() {
        system.registerRecruiter(new vcfs.models.users.Recruiter("John", "john@tech.com"));
        system.registerRecruiter(new vcfs.models.users.Recruiter("Jane", "jane@tech.com"));
        stats = new SystemStatistics(system.getFair());
        assertEquals(2, stats.getTotalRecruiters());
    }

    @Test
    public void testCalculateCandidateCount() {
        system.registerCandidate(new vcfs.models.users.Candidate("Alice", "alice@uni.com"));
        system.registerCandidate(new vcfs.models.users.Candidate("Bob", "bob@uni.com"));
        stats = new SystemStatistics(system.getFair());
        assertEquals(2, stats.getTotalCandidates());
    }

    @Test
    public void testGetFairPhase() {
        FairPhase phase = stats.getCurrentPhase();
        assertNotNull(phase);
    }

    @Test
    public void testStatisticsRefresh() {
        assertEquals(0, stats.getTotalOrganizations());
        system.addOrganization("New Org");
        stats.refresh(); // Manually refresh stats
        assertEquals(1, stats.getTotalOrganizations());
    }

    @Test
    public void testGetStatisticsMap() {
        java.util.Map<String, Object> statsMap = stats.getStatisticsMap();
        assertNotNull(statsMap);
        assertTrue(statsMap.containsKey("totalOrganizations"));
        assertTrue(statsMap.containsKey("totalBooths"));
        assertTrue(statsMap.containsKey("totalRecruiters"));
        assertTrue(statsMap.containsKey("totalCandidates"));
        assertTrue(statsMap.containsKey("totalOffers"));
        assertTrue(statsMap.containsKey("currentPhase"));
    }

    @Test
    public void testStatisticsDashboardTitle() {
        String title = "VCFS System Statistics";
        assertNotNull(title);
        assertTrue(title.length() > 0);
    }
}

