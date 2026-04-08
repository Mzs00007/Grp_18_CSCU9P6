package vcfs.models.structure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.models.users.Recruiter;


/**
 * Comprehensive JUnit tests for Organization class.
 * Tests organization model representing companies in career fair.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("Organization - Career Fair Organization/Company Model")
public class OrganizationTest {

    private Organization organization;
    private Recruiter recruiter;
    private static final String VALID_ORG_NAME = "Tech Corporation";
    private static final String VALID_DESCRIPTION = "Leading technology innovator";

    @BeforeEach
    void setUp() {
        organization = new Organization(VALID_ORG_NAME, VALID_DESCRIPTION);
        recruiter = new Recruiter("rec001", "Recruiter", "rec@test.com");
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Organization constructor initializes with name and description")
        void testConstructorValidParameters() {
            assertNotNull(organization);
            assertEquals(VALID_ORG_NAME, organization.getName());
            assertEquals(VALID_DESCRIPTION, organization.getDescription());
        }

        @Test
        @DisplayName("Organization with various names")
        void testConstructorVariousNames() {
            String[] names = {"Google", "Microsoft", "Apple", "Small Startup", 
                            "Tech Innovation Ltd"};
            for (String name : names) {
                Organization testOrg = new Organization(name, "Description");
                assertEquals(name, testOrg.getName());
            }
        }

        @Test
        @DisplayName("Organization with various descriptions")
        void testConstructorVariousDescriptions() {
            String[] descriptions = {"Enterprise software", "Cloud services", 
                                    "AI and machine learning", "Mobile apps", ""};
            for (String desc : descriptions) {
                Organization testOrg = new Organization("Company", desc);
                assertEquals(desc, testOrg.getDescription());
            }
        }
    }

    // ========== NAME MANAGEMENT ==========

    @Nested
    @DisplayName("Name Management Tests")
    class NameManagementTests {

        @Test
        @DisplayName("getName returns organization name")
        void testGetName() {
            assertEquals(VALID_ORG_NAME, organization.getName());
        }

        @Test
        @DisplayName("setName updates organization name")
        void testSetName() {
            String newName = "New Tech Company";
            organization.setName(newName);
            assertEquals(newName, organization.getName());
        }

        @Test
        @DisplayName("setName with various names")
        void testSetNameVariousNames() {
            organization.setName("Company One");
            assertEquals("Company One", organization.getName());
            
            organization.setName("Company Two");
            assertEquals("Company Two", organization.getName());
        }

        @Test
        @DisplayName("setName with empty string")
        void testSetNameEmpty() {
            organization.setName("");
            assertEquals("", organization.getName());
        }
    }

    // ========== DESCRIPTION MANAGEMENT ==========

    @Nested
    @DisplayName("Description Management Tests")
    class DescriptionManagementTests {

        @Test
        @DisplayName("getDescription returns description")
        void testGetDescription() {
            assertEquals(VALID_DESCRIPTION, organization.getDescription());
        }

        @Test
        @DisplayName("setDescription updates description")
        void testSetDescription() {
            String newDescription = "Leading digital transformation company";
            organization.setDescription(newDescription);
            assertEquals(newDescription, organization.getDescription());
        }

        @Test
        @DisplayName("setDescription with various descriptions")
        void testSetDescriptionVariousDescriptions() {
            String[] descriptions = {
                "Enterprise software provider",
                "Cloud computing pioneer",
                "AI and ML specialist",
                "Fortune 500 company",
                ""
            };
            
            for (String desc : descriptions) {
                organization.setDescription(desc);
                assertEquals(desc, organization.getDescription());
            }
        }

        @Test
        @DisplayName("setDescription with long text")
        void testSetDescriptionLongText() {
            String longDescription = "This is a comprehensive organization description that " +
                                    "includes details about the company's mission, vision, " +
                                    "and core business areas. It provides information about " +
                                    "their expertise and focus areas.";
            organization.setDescription(longDescription);
            assertEquals(longDescription, organization.getDescription());
        }
    }

    // ========== RECRUITER MANAGEMENT ==========

    @Nested
    @DisplayName("Recruiter Management Tests")
    class RecruiterManagementTests {

        @Test
        @DisplayName("setRecruiter associates recruiter with organization")
        void testSetRecruiter() {
            organization.setRecruiter(recruiter);
            assertEquals(recruiter, organization.getRecruiter());
        }

        @Test
        @DisplayName("setRecruiter can be set to null")
        void testSetRecruiterNull() {
            organization.setRecruiter(recruiter);
            organization.setRecruiter(null);
            assertNull(organization.getRecruiter());
        }

        @Test
        @DisplayName("setRecruiter with different recruiters")
        void testSetRecruiterDifferentRecruiters() {
            Recruiter recruiter1 = new Recruiter("rec1", "Recruiter 1", "rec1@test.com");
            Recruiter recruiter2 = new Recruiter("rec2", "Recruiter 2", "rec2@test.com");
            
            organization.setRecruiter(recruiter1);
            assertEquals(recruiter1, organization.getRecruiter());
            
            organization.setRecruiter(recruiter2);
            assertEquals(recruiter2, organization.getRecruiter());
        }

        @Test
        @DisplayName("getRecruiter returns associated recruiter")
        void testGetRecruiter() {
            organization.setRecruiter(recruiter);
            assertEquals(recruiter, organization.getRecruiter());
        }
    }

    // ========== INDEPENDENT FIELD UPDATES ==========

    @Nested
    @DisplayName("Independent Field Updates Tests")
    class IndependentFieldUpdatesTests {

        @Test
        @DisplayName("Updating name does not affect description or recruiter")
        void testUpdateNameIndependent() {
            organization.setRecruiter(recruiter);
            String originalDescription = organization.getDescription();
            
            organization.setName("New Name");
            
            assertEquals(originalDescription, organization.getDescription());
            assertEquals(recruiter, organization.getRecruiter());
        }

        @Test
        @DisplayName("Updating description does not affect name or recruiter")
        void testUpdateDescriptionIndependent() {
            organization.setRecruiter(recruiter);
            String originalName = organization.getName();
            
            organization.setDescription("New Description");
            
            assertEquals(originalName, organization.getName());
            assertEquals(recruiter, organization.getRecruiter());
        }

        @Test
        @DisplayName("Updating recruiter does not affect name or description")
        void testUpdateRecruiterIndependent() {
            String originalName = organization.getName();
            String originalDescription = organization.getDescription();
            
            organization.setRecruiter(recruiter);
            
            assertEquals(originalName, organization.getName());
            assertEquals(originalDescription, organization.getDescription());
        }
    }

    // ========== MULTIPLE ORGANIZATIONS ==========

    @Nested
    @DisplayName("Multiple Organizations Tests")
    class MultipleOrganizationsTests {

        @Test
        @DisplayName("Multiple organizations maintain independent state")
        void testMultipleOrganizationsIndependent() {
            Organization org1 = new Organization("Company 1", "Description 1");
            Organization org2 = new Organization("Company 2", "Description 2");
            
            Recruiter rec1 = new Recruiter("rec1", "Rec 1", "rec1@test.com");
            Recruiter rec2 = new Recruiter("rec2", "Rec 2", "rec2@test.com");
            
            org1.setRecruiter(rec1);
            org2.setRecruiter(rec2);
            
            assertEquals("Company 1", org1.getName());
            assertEquals("Company 2", org2.getName());
            assertEquals(rec1, org1.getRecruiter());
            assertEquals(rec2, org2.getRecruiter());
        }

        @Test
        @DisplayName("Create multiple organizations with different configs")
        void testCreateMultipleOrganizations() {
            Organization org1 = new Organization("Tech Corp", "Tech leader");
            Organization org2 = new Organization("Finance Co", "Finance specialist");
            Organization org3 = new Organization("Healthcare Inc", "Healthcare provider");
            
            assertEquals("Tech Corp", org1.getName());
            assertEquals("Finance Co", org2.getName());
            assertEquals("Healthcare Inc", org3.getName());
        }
    }

    // ========== COMPLETE ORGANIZATION LIFECYCLE ==========

    @Nested
    @DisplayName("Complete Organization Lifecycle Tests")
    class CompleteOrganizationLifecycleTests {

        @Test
        @DisplayName("Organization creation with full configuration")
        void testCompleteOrganizationConfiguration() {
            Organization testOrg = new Organization("Premium Tech Ltd", 
                                                    "Leading technology innovator");
            Recruiter rec = new Recruiter("rec_prem", "Premium Recruiter", "prem@test.com");
            
            testOrg.setRecruiter(rec);
            
            assertEquals("Premium Tech Ltd", testOrg.getName());
            assertEquals("Leading technology innovator", testOrg.getDescription());
            assertEquals(rec, testOrg.getRecruiter());
        }

        @Test
        @DisplayName("Update all organization fields")
        void testUpdateAllOrganizationFields() {
            Organization testOrg = new Organization("Original Name", "Original Description");
            Recruiter recruiterOld = new Recruiter("old", "Old", "old@test.com");
            Recruiter recruiterNew = new Recruiter("new", "New", "new@test.com");
            
            // Initial setup
            testOrg.setRecruiter(recruiterOld);
            
            // Update all fields
            testOrg.setName("Updated Name");
            testOrg.setDescription("Updated Description");
            testOrg.setRecruiter(recruiterNew);
            
            assertEquals("Updated Name", testOrg.getName());
            assertEquals("Updated Description", testOrg.getDescription());
            assertEquals(recruiterNew, testOrg.getRecruiter());
        }
    }

    // ========== EDGE CASES ==========

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("Organization with special characters in name")
        void testSpecialCharactersInName() {
            Organization testOrg = new Organization("Tech Corp (USA) & International", 
                                                    "Description");
            assertEquals("Tech Corp (USA) & International", testOrg.getName());
        }

        @Test
        @DisplayName("Organization with very long name")
        void testVeryLongOrganizationName() {
            String longName = "International Technology Innovation Consortium - " +
                            "Global Software Development and Cloud Services Provider";
            Organization testOrg = new Organization(longName, "Description");
            assertEquals(longName, testOrg.getName());
        }

        @Test
        @DisplayName("Organization with numeric suffix in name")
        void testNumericSuffixInName() {
            Organization testOrg = new Organization("Tech Company 2024 Inc.", "Description");
            assertEquals("Tech Company 2024 Inc.", testOrg.getName());
        }

        @Test
        @DisplayName("Organization with hyphenated name")
        void testHyphenatedOrganizationName() {
            Organization testOrg = new Organization("Tech-Innovation-Ltd", "Description");
            assertEquals("Tech-Innovation-Ltd", testOrg.getName());
        }
    }
}
