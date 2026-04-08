package vcfs.models.structure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import vcfs.models.users.Recruiter;


/**
 * Comprehensive JUnit tests for Booth class.
 * Tests booth model representing physical/virtual booth spaces in career fair.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("Booth - Career Fair Booth Model")
public class BoothTest {

    private Booth booth;
    private Recruiter recruiter;
    private static final String VALID_BOOTH_NAME = "Booth A1";
    private static final int VALID_BOOTH_NUMBER = 1;

    @BeforeEach
    void setUp() {
        booth = new Booth(VALID_BOOTH_NAME, VALID_BOOTH_NUMBER);
        recruiter = new Recruiter("rec001", "Recruiter Name", "rec@test.com");
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Booth constructor initializes with name and number")
        void testConstructorValidParameters() {
            assertNotNull(booth);
            assertEquals(VALID_BOOTH_NAME, booth.getName());
            assertEquals(VALID_BOOTH_NUMBER, booth.getBoothNumber());
        }

        @Test
        @DisplayName("Booth with different booth numbers")
        void testConstructorDifferentBoothNumbers() {
            for (int i = 1; i <= 50; i++) {
                Booth testBooth = new Booth("Booth " + i, i);
                assertEquals(i, testBooth.getBoothNumber());
            }
        }

        @Test
        @DisplayName("Booth with various names")
        void testConstructorVariousNames() {
            String[] names = {"Booth A", "Booth-B", "Booth_1", "Premium Booth", "Tech Pavillion"};
            for (String name : names) {
                Booth testBooth = new Booth(name, 1);
                assertEquals(name, testBooth.getName());
            }
        }
    }

    // ========== NAME MANAGEMENT ==========

    @Nested
    @DisplayName("Name Management Tests")
    class NameManagementTests {

        @Test
        @DisplayName("getName returns booth name")
        void testGetName() {
            assertEquals(VALID_BOOTH_NAME, booth.getName());
        }

        @Test
        @DisplayName("setName updates booth name")
        void testSetName() {
            String newName = "Booth B1";
            booth.setName(newName);
            assertEquals(newName, booth.getName());
        }

        @Test
        @DisplayName("setName with different names")
        void testSetNameVariousNames() {
            booth.setName("New Booth Name");
            assertEquals("New Booth Name", booth.getName());
            
            booth.setName("Another Booth");
            assertEquals("Another Booth", booth.getName());
        }
    }

    // ========== BOOTH NUMBER MANAGEMENT ==========

    @Nested
    @DisplayName("Booth Number Management Tests")
    class BoothNumberManagementTests {

        @Test
        @DisplayName("getBoothNumber returns booth number")
        void testGetBoothNumber() {
            assertEquals(VALID_BOOTH_NUMBER, booth.getBoothNumber());
        }

        @Test
        @DisplayName("setBoothNumber updates booth number")
        void testSetBoothNumber() {
            int newNumber = 5;
            booth.setBoothNumber(newNumber);
            assertEquals(newNumber, booth.getBoothNumber());
        }

        @Test
        @DisplayName("setBoothNumber with various numbers")
        void testSetBoothNumberVariousNumbers() {
            booth.setBoothNumber(10);
            assertEquals(10, booth.getBoothNumber());
            
            booth.setBoothNumber(100);
            assertEquals(100, booth.getBoothNumber());
        }

        @Test
        @DisplayName("setBoothNumber with zero")
        void testSetBoothNumberZero() {
            booth.setBoothNumber(0);
            assertEquals(0, booth.getBoothNumber());
        }

        @Test
        @DisplayName("setBoothNumber with negative number")
        void testSetBoothNumberNegative() {
            booth.setBoothNumber(-1);
            assertEquals(-1, booth.getBoothNumber());
        }
    }

    // ========== RECRUITER MANAGEMENT ==========

    @Nested
    @DisplayName("Recruiter Management Tests")
    class RecruiterManagementTests {

        @Test
        @DisplayName("setRecruiter associates recruiter with booth")
        void testSetRecruiter() {
            booth.setRecruiter(recruiter);
            assertEquals(recruiter, booth.getRecruiter());
        }

        @Test
        @DisplayName("setRecruiter can be set to null")
        void testSetRecruiterNull() {
            booth.setRecruiter(recruiter);
            booth.setRecruiter(null);
            assertNull(booth.getRecruiter());
        }

        @Test
        @DisplayName("setRecruiter with different recruiters")
        void testSetRecruiterDifferentRecruiters() {
            Recruiter recruiter1 = new Recruiter("rec1", "Recruiter 1", "rec1@test.com");
            Recruiter recruiter2 = new Recruiter("rec2", "Recruiter 2", "rec2@test.com");
            
            booth.setRecruiter(recruiter1);
            assertEquals(recruiter1, booth.getRecruiter());
            
            booth.setRecruiter(recruiter2);
            assertEquals(recruiter2, booth.getRecruiter());
        }

        @Test
        @DisplayName("getRecruiter returns associated recruiter")
        void testGetRecruiter() {
            booth.setRecruiter(recruiter);
            assertEquals(recruiter, booth.getRecruiter());
        }
    }

    // ========== INDEPENDENT FIELD UPDATES ==========

    @Nested
    @DisplayName("Independent Field Updates Tests")
    class IndependentFieldUpdatesTests {

        @Test
        @DisplayName("Updating name does not affect number or recruiter")
        void testUpdateNameIndependent() {
            booth.setBoothNumber(5);
            booth.setRecruiter(recruiter);
            
            booth.setName("New Name");
            
            assertEquals(5, booth.getBoothNumber());
            assertEquals(recruiter, booth.getRecruiter());
        }

        @Test
        @DisplayName("Updating booth number does not affect name or recruiter")
        void testUpdateNumberIndependent() {
            booth.setName("Test Name");
            booth.setRecruiter(recruiter);
            
            booth.setBoothNumber(10);
            
            assertEquals("Test Name", booth.getName());
            assertEquals(recruiter, booth.getRecruiter());
        }

        @Test
        @DisplayName("Updating recruiter does not affect name or number")
        void testUpdateRecruiterIndependent() {
            String originalName = booth.getName();
            int originalNumber = booth.getBoothNumber();
            
            booth.setRecruiter(recruiter);
            
            assertEquals(originalName, booth.getName());
            assertEquals(originalNumber, booth.getBoothNumber());
        }
    }

    // ========== MULTIPLE BOOTHS ==========

    @Nested
    @DisplayName("Multiple Booths Tests")
    class MultipleBoothsTests {

        @Test
        @DisplayName("Multiple booths maintain independent state")
        void testMultipleBoothsIndependent() {
            Booth booth1 = new Booth("Booth 1", 1);
            Booth booth2 = new Booth("Booth 2", 2);
            
            Recruiter rec1 = new Recruiter("rec1", "Rec 1", "rec1@test.com");
            Recruiter rec2 = new Recruiter("rec2", "Rec 2", "rec2@test.com");
            
            booth1.setRecruiter(rec1);
            booth2.setRecruiter(rec2);
            
            assertEquals(rec1, booth1.getRecruiter());
            assertEquals(rec2, booth2.getRecruiter());
            assertNotEquals(booth1.getRecruiter(), booth2.getRecruiter());
        }

        @Test
        @DisplayName("Create multiple booths in series")
        void testCreateMultipleBooths() {
            Booth booth1 = new Booth("Booth A", 1);
            Booth booth2 = new Booth("Booth B", 2);
            Booth booth3 = new Booth("Booth C", 3);
            
            assertEquals("Booth A", booth1.getName());
            assertEquals("Booth B", booth2.getName());
            assertEquals("Booth C", booth3.getName());
            
            assertEquals(1, booth1.getBoothNumber());
            assertEquals(2, booth2.getBoothNumber());
            assertEquals(3, booth3.getBoothNumber());
        }
    }

    // ========== COMPLETE BOOTH LIFECYCLE ==========

    @Nested
    @DisplayName("Complete Booth Lifecycle Tests")
    class CompleteBoothLifecycleTests {

        @Test
        @DisplayName("Booth creation with full configuration")
        void testCompleteBoothConfiguration() {
            Booth testBooth = new Booth("Premium Booth A", 15);
            Recruiter rec = new Recruiter("rec_premium", "Premium Recruiter", "premium@test.com");
            
            testBooth.setRecruiter(rec);
            
            assertEquals("Premium Booth A", testBooth.getName());
            assertEquals(15, testBooth.getBoothNumber());
            assertEquals(rec, testBooth.getRecruiter());
        }

        @Test
        @DisplayName("Update all booth fields")
        void testUpdateAllBoothFields() {
            Booth testBooth = new Booth("Initial Name", 1);
            Recruiter recruiterOld = new Recruiter("old", "Old", "old@test.com");
            Recruiter recruiterNew = new Recruiter("new", "New", "new@test.com");
            
            // Initial setup
            testBooth.setRecruiter(recruiterOld);
            
            // Update all fields
            testBooth.setName("Updated Name");
            testBooth.setBoothNumber(20);
            testBooth.setRecruiter(recruiterNew);
            
            assertEquals("Updated Name", testBooth.getName());
            assertEquals(20, testBooth.getBoothNumber());
            assertEquals(recruiterNew, testBooth.getRecruiter());
        }
    }

    // ========== EDGE CASES ==========

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("Booth with special characters in name")
        void testSpecialCharactersInName() {
            Booth testBooth = new Booth("Booth A-1 (Premium) & Tech", 1);
            assertEquals("Booth A-1 (Premium) & Tech", testBooth.getName());
        }

        @Test
        @DisplayName("Booth with very long name")
        void testVeryLongBoothName() {
            String longName = "Premium Technology Innovation Pavilion - Booth A1 " +
                            "for Advanced Software Development Companies";
            Booth testBooth = new Booth(longName, 1);
            assertEquals(longName, testBooth.getName());
        }

        @Test
        @DisplayName("Booth with large booth number")
        void testLargeBoothNumber() {
            Booth testBooth = new Booth("Booth", 99999);
            assertEquals(99999, testBooth.getBoothNumber());
        }
    }
}
