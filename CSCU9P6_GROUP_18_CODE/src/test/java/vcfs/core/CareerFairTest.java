package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Comprehensive JUnit tests for CareerFair class.
 * Tests career fair model representing individual career fair events.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("CareerFair - Career Fair Event Model")
public class CareerFairTest {

    private CareerFair careerFair;
    private static final String VALID_FAIR_NAME = "Tech Career Fair 2026";
    private static final String VALID_DESCRIPTION = "Annual technology recruitment event";

    @BeforeEach
    void setUp() {
        careerFair = new CareerFair();
        careerFair.setName(VALID_FAIR_NAME);
        careerFair.setDescription(VALID_DESCRIPTION);
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("CareerFair constructor initializes with name and description")
        void testConstructorValidParameters() {
            assertNotNull(careerFair);
            assertEquals(VALID_FAIR_NAME, careerFair.getName());
            assertEquals(VALID_DESCRIPTION, careerFair.getDescription());
        }

        @Test
        @DisplayName("CareerFair with various names")
        void testConstructorVariousNames() {
            String[] names = {"Tech Fair", "Campus Recruitment", "Virtual Career Day",
                            "Engineering Expo 2026", "IT Job Fair"};
            for (String name : names) {
                CareerFair fair = new CareerFair(name, "Description");
                assertEquals(name, fair.getName());
            }
        }

        @Test
        @DisplayName("Multiple career fairs are independent")
        void testMultipleCareerFairsIndependent() {
            CareerFair fair1 = new CareerFair("Fair 1", "Desc 1");
            CareerFair fair2 = new CareerFair("Fair 2", "Desc 2");
            
            assertNotEquals(fair1.getName(), fair2.getName());
        }
    }

    // ========== NAME MANAGEMENT ==========

    @Nested
    @DisplayName("Name Management Tests")
    class NameManagementTests {

        @Test
        @DisplayName("getName returns fair name")
        void testGetName() {
            assertEquals(VALID_FAIR_NAME, careerFair.getName());
        }

        @Test
        @DisplayName("setName updates fair name")
        void testSetName() {
            String newName = "Updated Career Fair";
            careerFair.setName(newName);
            assertEquals(newName, careerFair.getName());
        }

        @Test
        @DisplayName("setName with various names")
        void testSetNameVariousNames() {
            careerFair.setName("New Fair Name");
            assertEquals("New Fair Name", careerFair.getName());
            
            careerFair.setName("Another Fair");
            assertEquals("Another Fair", careerFair.getName());
        }
    }

    // ========== DESCRIPTION MANAGEMENT ==========

    @Nested
    @DisplayName("Description Management Tests")
    class DescriptionManagementTests {

        @Test
        @DisplayName("getDescription returns description")
        void testGetDescription() {
            assertEquals(VALID_DESCRIPTION, careerFair.getDescription());
        }

        @Test
        @DisplayName("setDescription updates description")
        void testSetDescription() {
            String newDescription = "Updated recruitment event";
            careerFair.setDescription(newDescription);
            assertEquals(newDescription, careerFair.getDescription());
        }

        @Test
        @DisplayName("setDescription with various descriptions")
        void testSetDescriptionVariousDescriptions() {
            String[] descriptions = {
                "Tech recruitment day",
                "Campus hiring event",
                "Virtual career expo",
                "Graduate recruitment fair",
                ""
            };
            
            for (String desc : descriptions) {
                careerFair.setDescription(desc);
                assertEquals(desc, careerFair.getDescription());
            }
        }
    }

    // ========== TIMING & PHASES ==========

    @Nested
    @DisplayName("Timing and Phase Tests")
    class TimingAndPhaseTests {

        @Test
        @DisplayName("CareerFair can have start time")
        void testCareerFairStartTime() {
            LocalDateTime startTime = new LocalDateTime(2026, 4, 15, 9, 0);
            careerFair.setStartTime(startTime);
            assertEquals(startTime, careerFair.getStartTime());
        }

        @Test
        @DisplayName("CareerFair can have end time")
        void testCareerFairEndTime() {
            LocalDateTime endTime = new LocalDateTime(2026, 4, 15, 17, 0);
            careerFair.setEndTime(endTime);
            assertEquals(endTime, careerFair.getEndTime());
        }

        @Test
        @DisplayName("Start and end times are independent")
        void testStartEndTimesIndependent() {
            LocalDateTime startTime = new LocalDateTime(2026, 4, 15, 9, 0);
            LocalDateTime endTime = new LocalDateTime(2026, 4, 15, 17, 0);
            
            careerFair.setStartTime(startTime);
            careerFair.setEndTime(endTime);
            
            assertEquals(startTime, careerFair.getStartTime());
            assertEquals(endTime, careerFair.getEndTime());
        }
    }

    // ========== INDEPENDENT FIELD UPDATES ==========

    @Nested
    @DisplayName("Independent Field Updates Tests")
    class IndependentFieldUpdatesTests {

        @Test
        @DisplayName("Updating name does not affect description")
        void testUpdateNameIndependent() {
            String originalDescription = careerFair.getDescription();
            careerFair.setName("New Name");
            assertEquals(originalDescription, careerFair.getDescription());
        }

        @Test
        @DisplayName("Updating description does not affect name")
        void testUpdateDescriptionIndependent() {
            String originalName = careerFair.getName();
            careerFair.setDescription("New Description");
            assertEquals(originalName, careerFair.getName());
        }

        @Test
        @DisplayName("Multiple field updates work correctly")
        void testMultipleFieldUpdates() {
            careerFair.setName("Updated Name");
            careerFair.setDescription("Updated Description");
            LocalDateTime startTime = new LocalDateTime(2026, 4, 15, 9, 0);
            careerFair.setStartTime(startTime);
            
            assertEquals("Updated Name", careerFair.getName());
            assertEquals("Updated Description", careerFair.getDescription());
            assertEquals(startTime, careerFair.getStartTime());
        }
    }

    // ========== COMPLETE CAREER FAIR LIFECYCLE ==========

    @Nested
    @DisplayName("Complete Career Fair Lifecycle Tests")
    class CompleteCareerFairLifecycleTests {

        @Test
        @DisplayName("Career fair creation with full configuration")
        void testCompleteCareerFairConfiguration() {
            LocalDateTime start = new LocalDateTime(2026, 4, 15, 9, 0);
            LocalDateTime end = new LocalDateTime(2026, 4, 15, 17, 0);
            
            CareerFair fair = new CareerFair("Premium Tech Fair", 
                                            "Major recruitment event");
            fair.setStartTime(start);
            fair.setEndTime(end);
            
            assertEquals("Premium Tech Fair", fair.getName());
            assertEquals("Major recruitment event", fair.getDescription());
            assertEquals(start, fair.getStartTime());
            assertEquals(end, fair.getEndTime());
        }

        @Test
        @DisplayName("Update career fair details")
        void testUpdateCareerFairDetails() {
            CareerFair fair = new CareerFair("Original Fair", "Original Description");
            
            fair.setName("Updated Fair");
            fair.setDescription("Updated Description");
            LocalDateTime newStart = new LocalDateTime(2026, 5, 20, 10, 0);
            fair.setStartTime(newStart);
            
            assertEquals("Updated Fair", fair.getName());
            assertEquals("Updated Description", fair.getDescription());
            assertEquals(newStart, fair.getStartTime());
        }
    }

    // ========== MULTIPLE CAREER FAIRS ==========

    @Nested
    @DisplayName("Multiple Career Fairs Tests")
    class MultipleCareerFairsTests {

        @Test
        @DisplayName("Multiple career fairs maintain independent state")
        void testMultipleCareerFairsIndependentState() {
            CareerFair fair1 = new CareerFair("Fair 1", "Description 1");
            CareerFair fair2 = new CareerFair("Fair 2", "Description 2");
            
            LocalDateTime time1 = new LocalDateTime(2026, 4, 15, 9, 0);
            LocalDateTime time2 = new LocalDateTime(2026, 5, 20, 9, 0);
            
            fair1.setStartTime(time1);
            fair2.setStartTime(time2);
            
            assertEquals("Fair 1", fair1.getName());
            assertEquals("Fair 2", fair2.getName());
            assertEquals(time1, fair1.getStartTime());
            assertEquals(time2, fair2.getStartTime());
        }

        @Test
        @DisplayName("Create multiple career fairs with different configs")
        void testCreateMultipleCareerFairs() {
            CareerFair spring = new CareerFair("Spring Career Fair", "Spring recruitment");
            CareerFair summer = new CareerFair("Summer Career Fair", "Summer recruitment");
            CareerFair fall = new CareerFair("Fall Career Fair", "Fall recruitment");
            
            assertEquals("Spring Career Fair", spring.getName());
            assertEquals("Summer Career Fair", summer.getName());
            assertEquals("Fall Career Fair", fall.getName());
        }
    }

    // ========== EDGE CASES ==========

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("CareerFair with special characters in name")
        void testSpecialCharactersInName() {
            CareerFair fair = new CareerFair("Tech Fair 2026 (Premium) & Virtual", 
                                           "Description");
            assertEquals("Tech Fair 2026 (Premium) & Virtual", fair.getName());
        }

        @Test
        @DisplayName("CareerFair with very long name")
        void testVeryLongFairName() {
            String longName = "International Technology Career Fair and Recruitment Event " +
                            "for Software Development and Innovation Companies";
            CareerFair fair = new CareerFair(longName, "Description");
            assertEquals(longName, fair.getName());
        }

        @Test
        @DisplayName("CareerFair with numeric suffix")
        void testNumericSuffixInName() {
            CareerFair fair = new CareerFair("Career Fair 2026 Edition 1", "Description");
            assertEquals("Career Fair 2026 Edition 1", fair.getName());
        }

        @Test
        @DisplayName("CareerFair with hyphenated name")
        void testHyphenatedFairName() {
            CareerFair fair = new CareerFair("Tech-Career-Fair-2026", "Description");
            assertEquals("Tech-Career-Fair-2026", fair.getName());
        }

        @Test
        @DisplayName("CareerFair with same-day start and end")
        void testSameDayStartEnd() {
            LocalDateTime start = new LocalDateTime(2026, 4, 15, 9, 0);
            LocalDateTime end = new LocalDateTime(2026, 4, 15, 17, 0);
            
            careerFair.setStartTime(start);
            careerFair.setEndTime(end);
            
            assertEquals(start, careerFair.getStartTime());
            assertEquals(end, careerFair.getEndTime());
        }

        @Test
        @DisplayName("CareerFair with multi-day event")
        void testMultiDayEvent() {
            LocalDateTime start = new LocalDateTime(2026, 4, 15, 9, 0);
            LocalDateTime end = new LocalDateTime(2026, 4, 16, 17, 0);
            
            careerFair.setStartTime(start);
            careerFair.setEndTime(end);
            
            assertEquals(start, careerFair.getStartTime());
            assertEquals(end, careerFair.getEndTime());
        }
    }
}

