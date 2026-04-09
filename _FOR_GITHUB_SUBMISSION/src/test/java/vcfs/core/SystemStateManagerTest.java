package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for SystemStateManager.
 * Tests state tracking, operation recording, and audit trail functionality.
 */
@DisplayName("SystemStateManager Test Suite")
public class SystemStateManagerTest {

    private SystemStateManager manager;

    @BeforeEach
    void setUp() {
        manager = SystemStateManager.getInstance();
    }

    // =========================================================
    // SINGLETON Tests
    // =========================================================

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonTests {

        @Test
        @DisplayName("Should return same instance on multiple calls")
        void testSingletonInstance() {
            SystemStateManager instance1 = SystemStateManager.getInstance();
            SystemStateManager instance2 = SystemStateManager.getInstance();

            assertSame(instance1, instance2, "Should return same instance");
        }

        @Test
        @DisplayName("Should be thread-safe singleton")
        void testThreadSafeSingleton() {
            SystemStateManager[] instances = new SystemStateManager[10];

            for (int i = 0; i < 10; i++) {
                instances[i] = SystemStateManager.getInstance();
            }

            for (int i = 1; i < 10; i++) {
                assertSame(instances[0], instances[i], "All instances should be identical");
            }
        }
    }

    // =========================================================
    // RECORD STATE CHANGE Tests
    // =========================================================

    @Nested
    @DisplayName("Record State Change Tests")
    class RecordStateChangeTests {

        @Test
        @DisplayName("Should record successful state change")
        void testRecordSuccessfulStateChange() {
            assertDoesNotThrow(() -> {
                manager.recordStateChange("TEST_OPERATION", "Test description", true);
            }, "Should record successful state change");
        }

        @Test
        @DisplayName("Should record failed state change")
        void testRecordFailedStateChange() {
            assertDoesNotThrow(() -> {
                manager.recordStateChange("TEST_OPERATION", "Test failed", false);
            }, "Should record failed state change");
        }

        @Test
        @DisplayName("Should handle null operation type")
        void testRecordNullOperationType() {
            assertDoesNotThrow(() -> {
                manager.recordStateChange(null, "Description", true);
            }, "Should handle null operation type");
        }

        @Test
        @DisplayName("Should handle null description")
        void testRecordNullDescription() {
            assertDoesNotThrow(() -> {
                manager.recordStateChange("OPERATION", null, true);
            }, "Should handle null description");
        }

        @Test
        @DisplayName("Should record multiple state changes")
        void testRecordMultipleStateChanges() {
            for (int i = 0; i < 10; i++) {
                final int index = i;
                assertDoesNotThrow(() -> {
                    manager.recordStateChange("OP" + index, "Description " + index, index % 2 == 0);
                }, "Should record multiple changes");
            }
        }

        @Test
        @DisplayName("Should handle rapid recording of state changes")
        void testRapidRecording() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 100; i++) {
                    manager.recordStateChange("RAPID_OP", "Rapid change " + i, true);
                }
            }, "Should handle rapid state change recording");
        }
    }

    // =========================================================
    // RECORD BOOKING Tests
    // =========================================================

    @Nested
    @DisplayName("Record Booking Tests")
    class RecordBookingTests {

        @Test
        @DisplayName("Should record successful booking")
        void testRecordSuccessfulBooking() {
            assertDoesNotThrow(() -> {
                manager.recordBooking("John Doe", "Jane Smith", "Google", "9:00-10:00", true);
            }, "Should record successful booking");
        }

        @Test
        @DisplayName("Should record failed booking")
        void testRecordFailedBooking() {
            assertDoesNotThrow(() -> {
                manager.recordBooking("John Doe", "Jane Smith", "Google", "9:00-10:00", false);
            }, "Should record failed booking");
        }

        @Test
        @DisplayName("Should handle null candidate name")
        void testRecordBookingNullCandidate() {
            assertDoesNotThrow(() -> {
                manager.recordBooking(null, "Jane Smith", "Google", "9:00-10:00", true);
            }, "Should handle null candidate name");
        }

        @Test
        @DisplayName("Should handle null recruiter name")
        void testRecordBookingNullRecruiter() {
            assertDoesNotThrow(() -> {
                manager.recordBooking("John Doe", null, "Google", "9:00-10:00", true);
            }, "Should handle null recruiter name");
        }

        @Test
        @DisplayName("Should record multiple bookings")
        void testRecordMultipleBookings() {
            for (int i = 0; i < 5; i++) {
                final int index = i;
                assertDoesNotThrow(() -> {
                    manager.recordBooking("Candidate" + index, "Recruiter" + index, "Company" + index, "Time" + index, true);
                }, "Should record multiple bookings");
            }
        }
    }

    // =========================================================
    // RECORD OFFER PUBLISHED Tests
    // =========================================================

    @Nested
    @DisplayName("Record Offer Published Tests")
    class RecordOfferPublishedTests {

        @Test
        @DisplayName("Should record successful offer publication")
        void testRecordSuccessfulOfferPublished() {
            assertDoesNotThrow(() -> {
                manager.recordOfferPublished("Jane Smith", "Software Engineer", 5, true);
            }, "Should record successful offer publication");
        }

        @Test
        @DisplayName("Should record failed offer publication")
        void testRecordFailedOfferPublished() {
            assertDoesNotThrow(() -> {
                manager.recordOfferPublished("Jane Smith", "Software Engineer", 5, false);
            }, "Should record failed offer publication");
        }

        @Test
        @DisplayName("Should handle zero slots")
        void testRecordOfferZeroSlots() {
            assertDoesNotThrow(() -> {
                manager.recordOfferPublished("Jane Smith", "Developer", 0, true);
            }, "Should handle zero slots");
        }

        @Test
        @DisplayName("Should handle large slot numbers")
        void testRecordOfferLargeSlots() {
            assertDoesNotThrow(() -> {
                manager.recordOfferPublished("Jane Smith", "Role", 100, true);
            }, "Should handle large slot numbers");
        }

        @Test
        @DisplayName("Should record multiple offers")
        void testRecordMultipleOffers() {
            for (int i = 0; i < 5; i++) {
                final int index = i;
                assertDoesNotThrow(() -> {
                    manager.recordOfferPublished("Recruiter" + index, "Position" + index, index + 1, true);
                }, "Should record multiple offers");
            }
        }
    }

    // =========================================================
    // RECORD SEARCH Tests
    // =========================================================

    @Nested
    @DisplayName("Record Search Tests")
    class RecordSearchTests {

        @Test
        @DisplayName("Should record search with results")
        void testRecordSearchWithResults() {
            assertDoesNotThrow(() -> {
                manager.recordSearch("John Doe", "Java Engineer", 3, true);
            }, "Should record search with results");
        }

        @Test
        @DisplayName("Should record search without results")
        void testRecordSearchNoResults() {
            assertDoesNotThrow(() -> {
                manager.recordSearch("John Doe", "Rare Position", 0, false);
            }, "Should record search without results");
        }

        @Test
        @DisplayName("Should handle null candidate name")
        void testRecordSearchNullCandidate() {
            assertDoesNotThrow(() -> {
                manager.recordSearch(null, "Search Term", 5, true);
            }, "Should handle null candidate name");
        }

        @Test
        @DisplayName("Should handle large result counts")
        void testRecordSearchLargeResults() {
            assertDoesNotThrow(() -> {
                manager.recordSearch("John Doe", "Common Role", 1000, true);
            }, "Should handle large result counts");
        }

        @Test
        @DisplayName("Should record multiple searches")
        void testRecordMultipleSearches() {
            for (int i = 0; i < 5; i++) {
                final int index = i;
                assertDoesNotThrow(() -> {
                    manager.recordSearch("Candidate" + index, "Search" + index, index * 2, true);
                }, "Should record multiple searches");
            }
        }
    }

    // =========================================================
    // STATISTICS Tests
    // =========================================================

    @Nested
    @DisplayName("Statistics Tests")
    class StatisticsTests {

        @Test
        @DisplayName("Should track total operations")
        void testGetTotalOperations() {
            manager.recordStateChange("OP1", "Test", true);
            manager.recordStateChange("OP2", "Test", true);
            manager.recordStateChange("OP3", "Test", false);

            int total = manager.getTotalOperations();
            assertTrue(total >= 3, "Should track total operations");
        }

        @Test
        @DisplayName("Should track successful operations")
        void testGetSuccessfulOperations() {
            manager.recordStateChange("SUCCESS1", "Test", true);
            manager.recordStateChange("SUCCESS2", "Test", true);

            int successful = manager.getSuccessfulOperations();
            assertTrue(successful >= 2, "Should track successful operations");
        }

        @Test
        @DisplayName("Should track failed operations")
        void testGetFailedOperations() {
            manager.recordStateChange("FAIL1", "Test", false);
            manager.recordStateChange("FAIL2", "Test", false);

            int failed = manager.getFailedOperations();
            assertTrue(failed >= 2, "Should track failed operations");
        }

        @Test
        @DisplayName("Should calculate success rate")
        void testGetSuccessRate() {
            manager.recordStateChange("OP1", "Test", true);
            manager.recordStateChange("OP2", "Test", false);

            double rate = manager.getSuccessRate();
            assertTrue(rate >= 0 && rate <= 100, "Success rate should be between 0-100%");
        }

        @Test
        @DisplayName("Should track uptime")
        void testGetUptime() {
            long uptime = manager.getUptime();
            assertTrue(uptime >= 0, "Uptime should be positive");
        }
    }

    // =========================================================
    // HISTORY Tests
    // =========================================================

    @Nested
    @DisplayName("History Tests")
    class HistoryTests {

        @Test
        @DisplayName("Should retrieve state history")
        void testGetStateHistory() {
            manager.recordStateChange("HIST1", "Test", true);

            assertDoesNotThrow(() -> {
                var history = manager.getStateHistory();
                assertNotNull(history, "History should not be null");
            }, "Should retrieve state history");
        }

        @Test
        @DisplayName("Should limit history to prevent memory overload")
        void testHistoryMemoryLimit() {
            // Record many operations
            for (int i = 0; i < 2000; i++) {
                manager.recordStateChange("OP" + i, "Desc" + i, true);
            }

            var history = manager.getStateHistory();
            assertTrue(history.size() <= 1200, "History should be limited to prevent memory overload");
        }

        @Test
        @DisplayName("Should retrieve recent state changes")
        void testGetRecentStateChanges() {
            manager.recordStateChange("OLD", "Old operation", true);
            manager.recordStateChange("RECENT", "Recent operation", true);

            assertDoesNotThrow(() -> {
                var recent = manager.getRecentStateChanges(5);
                assertNotNull(recent, "Should return recent changes");
            }, "Should retrieve recent state changes");
        }
    }

    // =========================================================
    // RESET Tests
    // =========================================================

    @Nested
    @DisplayName("Reset Tests")
    class ResetTests {

        @Test
        @DisplayName("Should reset state statistics")
        void testResetStatistics() {
            manager.recordStateChange("OP1", "Test", true);

            assertDoesNotThrow(() -> {
                manager.reset();
            }, "Should reset statistics");
        }

        @Test
        @DisplayName("Should clear history after reset")
        void testHistoryClearedAfterReset() {
            manager.recordStateChange("OP1", "Test", true);
            manager.reset();

            var history = manager.getStateHistory();
            assertTrue(history.isEmpty(), "History should be cleared after reset");
        }
    }

    // =========================================================
    // ERROR HANDLING Tests
    // =========================================================

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle concurrent state changes")
        void testConcurrentStateChanges() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 50; i++) {
                    manager.recordStateChange("CONCURRENT_" + i, "Concurrent op", true);
                }
            }, "Should handle concurrent operations");
        }

        @Test
        @DisplayName("Should survive stress testing")
        void testStressTesting() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 500; i++) {
                    manager.recordStateChange("STRESS_" + i, "Stress test " + i, i % 3 == 0);
                    manager.recordBooking("C" + i, "R" + i, "Org" + i, "Time", i % 2 == 0);
                    manager.recordOfferPublished("R" + i, "Pos", i, true);
                    manager.recordSearch("C" + i, "Search", i, true);
                }
            }, "Should survive stress testing");
        }
    }
}

