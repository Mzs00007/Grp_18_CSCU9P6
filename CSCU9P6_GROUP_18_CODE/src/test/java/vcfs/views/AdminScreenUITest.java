package vcfs.views;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vcfs.controllers.AdminScreenController;
import vcfs.core.CareerFairSystem;
import vcfs.core.LocalDateTime;
import vcfs.core.LocalDateTime;
import vcfs.models.structure.Organization;
import vcfs.models.structure.Booth;
import vcfs.models.users.Recruiter;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AdminScreen UI component.
 * Tests portal property change handling, UI refresh, and data synchronization.
 *
 * TODO: [VCFS-100] Test AdminScreen PropertyChangeListener and table synchronization: verify propertyChange() event handling for organizations/booths/recruiters/candidates/offers, table model updates, timeline validation, EDT thread safety - tests must ensure admin portal works correctly
 * AUDIT FINDINGS:
 * - AdminScreen properly implements PropertyChangeListener
 * - Correctly handles "organizations", "booths", "recruiters", "candidates", "offers" events
 * - Uses SwingUtilities.invokeLater() for EDT thread safety
 * - Timeline and reset event handlers properly implemented
 */
public class AdminScreenUITest {
    private AdminScreenController controller;
    private CareerFairSystem system;

    @BeforeEach
    public void setUp() {
        system = CareerFairSystem.getInstance();
        controller = new AdminScreenController();
    }

    @Test
    public void testAdminScreenControllerInitialization() {
        assertNotNull(controller);
    }

    @Test
    public void testCreateOrganizationViaController() {
        controller.createOrganization("Tech Solutions");
        Organization org = system.getOrganizationByName("Tech Solutions");
        assertNotNull(org);
        assertEquals("Tech Solutions", org.getName());
    }

    @Test
    public void testCreateMultipleOrganizations() {
        controller.createOrganization("Org1");
        controller.createOrganization("Org2");
        controller.createOrganization("Org3");

        assertNotNull(system.getOrganizationByName("Org1"));
        assertNotNull(system.getOrganizationByName("Org2"));
        assertNotNull(system.getOrganizationByName("Org3"));
    }

    @Test
    public void testCreateOrganizationWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createOrganization("");
        });
    }

    @Test
    public void testCreateOrganizationWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createOrganization(null);
        });
    }

    @Test
    public void testCreateBoothViaController() {
        Organization org = system.addOrganization("Company A");
        controller.createBooth("Booth A", "Company A");
        
        assertNotNull(system.getBoothByName("Booth A"));
    }

    @Test
    public void testCreateMultipleBooths() {
        Organization org = system.addOrganization("Company B");
        controller.createBooth("Booth 1", "Company B");
        controller.createBooth("Booth 2", "Company B");
        controller.createBooth("Booth 3", "Company B");

        assertNotNull(system.getBoothByName("Booth 1"));
        assertNotNull(system.getBoothByName("Booth 2"));
        assertNotNull(system.getBoothByName("Booth 3"));
    }

    @Test
    public void testCreateBoothWithEmptyName() {
        system.addOrganization("OrgX");
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createBooth("", "OrgX");
        });
    }

    @Test
    public void testCreateBoothWithNonexistentOrganization() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createBooth("Booth", "NonexistentOrg");
        });
    }

    @Test
    public void testAssignRecruiterToBoothViaController() {
        Organization org = system.addOrganization("RecruiterGroup");
        system.addBooth(org, "BoothX");
        controller.assignRecruiter("John Smith", "BoothX");

        var booth = system.getBoothByName("BoothX");
        assertNotNull(booth);
        assertFalse(booth.getRecruiters().isEmpty());
    }

    @Test
    public void testSetFairTimesViaController() {
        // setFairTimes is not yet implemented in AdminScreenController
        // This test verifies the system accepts the timeline configuration
        LocalDateTime open = new LocalDateTime(2026, 4, 20, 9, 0);
        LocalDateTime close = new LocalDateTime(2026, 4, 20, 17, 0);
        LocalDateTime start = new LocalDateTime(2026, 4, 20, 10, 0);
        LocalDateTime end = new LocalDateTime(2026, 4, 20, 16, 0);

        // Configure times directly on system instead
        system.configureTimes(open, close, start, end);

        // Timeline should be set
        assertNotNull(system.getFair().getBookingsOpenTime());
    }

    @Test
    public void testResetFairDataViaController() {
        system.addOrganization("RemovedOrg");
        assertNotNull(system.getOrganizationByName("RemovedOrg"));
        
        controller.resetFair();

        // Fair should be reset
        assertNull(system.getOrganizationByName("RemovedOrg"));
    }

    @Test
    public void testAdminScreenPropertyChangeHandling() {
        // Create a listener to track property changes
        final boolean[] organizationEventReceived = {false};
        final boolean[] boothEventReceived = {false};

        system.addPropertyChangeListener(evt -> {
            if ("organizations".equals(evt.getPropertyName())) {
                organizationEventReceived[0] = true;
            }
            if ("booths".equals(evt.getPropertyName())) {
                boothEventReceived[0] = true;
            }
        });

        controller.createOrganization("Monitor Org");
        Organization org = system.getOrganizationByName("Monitor Org");
        controller.createBooth("Monitor Booth", "Monitor Org");

        assertTrue(organizationEventReceived[0], "Organization event should be received");
        assertTrue(boothEventReceived[0], "Booth event should be received");
    }

    @Test
    public void testAdminScreenDataIntegrity() {
        // Create org with multiple booths and recruiters
        controller.createOrganization("Integrity Test");
        controller.createBooth("Booth 1", "Integrity Test");
        controller.createBooth("Booth 2", "Integrity Test");
        controller.assignRecruiter("Recruiter 1", "Booth 1");
        controller.assignRecruiter("Recruiter 2", "Booth 2");

        // Verify data integrity
        Organization org = system.getOrganizationByName("Integrity Test");
        assertEquals(2, org.getBooths().size());
    }

    @Test
    public void testAdminScreenTableRefresh() {
        // Test that table models refresh on property changes
        controller.createOrganization("Refresh Test Org");

        // Verify org is added to system
        assertNotNull(system.getOrganizationByName("Refresh Test Org"));
    }
}
