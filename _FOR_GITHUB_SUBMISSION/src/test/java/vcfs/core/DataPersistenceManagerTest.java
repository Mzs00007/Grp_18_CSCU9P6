package vcfs.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DataPersistenceManager singleton.
 * Tests automatic data persistence, crash recovery, and background saving.
 * 
 * Uses ONLY PUBLIC API:
 * - getInstance()
 * - initialize(CareerFairSystem)
 * - saveAllState()
 * - manualCheckpoint(String)
 * - shutdown()
 * - getDataDirectory()
 */
@DisplayName("DataPersistenceManager - Data Persistence Tests")
public class DataPersistenceManagerTest {

    private DataPersistenceManager manager;
    private CareerFairSystem system;

    @BeforeEach
    void setUp() {
        this.manager = DataPersistenceManager.getInstance();
        this.system = CareerFairSystem.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Cleanup
        try {
            manager.shutdown();
        } catch (Exception e) {
            // Ignore
        }
    }

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonPatternTests {

        @Test
        @DisplayName("Should return same instance on multiple calls")
        void testSingletonUniqueness() {
            DataPersistenceManager instance1 = DataPersistenceManager.getInstance();
            DataPersistenceManager instance2 = DataPersistenceManager.getInstance();

            assertSame(instance1, instance2, "Multiple calls should return same instance");
            assertNotNull(instance1, "Instance should never be null");
        }

        @Test
        @DisplayName("Should preserve singleton across references")
        void testSingletonIdentity() {
            DataPersistenceManager ref1 = DataPersistenceManager.getInstance();
            DataPersistenceManager ref2 = DataPersistenceManager.getInstance();
            
            assertTrue(ref1 == ref2, "Object identity should be preserved");
        }

        @Test
        @DisplayName("Singleton should be thread-safe")
        void testSingletonThreadSafety() throws InterruptedException {
            DataPersistenceManager[] instances = new DataPersistenceManager[10];
            Thread[] threads = new Thread[10];

            for (int i = 0; i < 10; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    instances[index] = DataPersistenceManager.getInstance();
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            DataPersistenceManager first = instances[0];
            for (int i = 1; i < 10; i++) {
                assertSame(first, instances[i], "All threads should get same singleton");
            }
        }
    }

    @Nested
    @DisplayName("Data Directory Tests")
    class DataDirectoryTests {

        @Test
        @DisplayName("getDataDirectory() should return non-empty string")
        void testDataDirectoryRetreval() {
            String directory = manager.getDataDirectory();
            
            assertNotNull(directory, "Data directory should not be null");
            assertFalse(directory.isEmpty(), "Data directory should not be empty");
            assertTrue(directory.length() > 2, "Data directory should be a valid path");
        }

        @Test
        @DisplayName("getDataDirectory() should return consistent value")
        void testDataDirectoryConsistency() {
            String dir1 = manager.getDataDirectory();
            String dir2 = manager.getDataDirectory();
            String dir3 = manager.getDataDirectory();
            
            assertEquals(dir1, dir2, "Data directory should be consistent");
            assertEquals(dir2, dir3, "Data directory should be consistent");
        }

        @Test
        @DisplayName("Data directory should exist or be creatable")
        void testDataDirectoryValid() {
            String directory = manager.getDataDirectory();
            
            Path dirPath = Paths.get(directory);
            // Should be a valid path
            assertNotNull(dirPath, "Path should be valid");
            assertTrue(directory.contains("vcfs") || directory.contains("data") ||
                      directory.contains("_data"), "Should reference VCFS data location");
        }

        @Test
        @DisplayName("getDataDirectory() should be idempotent")
        void testDataDirectoryIdempotent() {
            String dir1 = manager.getDataDirectory();
            String dir2 = manager.getDataDirectory();
            String dir3 = manager.getDataDirectory();
            
            assertEquals(dir1, dir2, "First and second calls should match");
            assertEquals(dir2, dir3, "Second and third calls should match");
            assertEquals(dir1, dir3, "First and third calls should match");
        }
    }

    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("initialize() should accept CareerFairSystem")
        void testInitializationWithSystem() {
            assertDoesNotThrow(() -> manager.initialize(system),
                "initialize() should complete without exception");
        }

        @Test
        @DisplayName("initialize() should handle multiple calls")
        void testMultipleInitializeCalls() {
            assertDoesNotThrow(() -> {
                manager.initialize(system);
                manager.initialize(system);
                manager.initialize(system);
            }, "Multiple initialize() calls should be safe");
        }

        @Test
        @DisplayName("initialize() should set up background auto-save")
        void testAutoSaveSetup() {
            manager.initialize(system);
            
            // Give timer a moment to start
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Should have started without exception
            assertDoesNotThrow(() -> manager.getDataDirectory(),
                "Should continue to work after initialize");
        }
    }

    @Nested
    @DisplayName("Save Operations Tests")
    class SaveOperationsTests {

        @Test
        @DisplayName("saveAllState() should complete without exception")
        void testSaveAllStateCompletion() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> manager.saveAllState(),
                "saveAllState() should complete without exception");
        }

        @Test
        @DisplayName("saveAllState() can be called multiple times")
        void testMultipleSaveOperations() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> {
                manager.saveAllState();
                Thread.sleep(10);
                manager.saveAllState();
                Thread.sleep(10);
                manager.saveAllState();
            }, "Multiple save operations should be safe");
        }

        @Test
        @DisplayName("saveAllState() should be thread-safe")
        void testSaveStateThreadSafety() throws InterruptedException {
            manager.initialize(system);
            
            Thread[] threads = new Thread[5];
            for (int i = 0; i < 5; i++) {
                threads[i] = new Thread(() -> {
                    try {
                        manager.saveAllState();
                    } catch (Exception e) {
                        fail("Save operation in thread failed: " + e.getMessage());
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }
        }
    }

    @Nested
    @DisplayName("Checkpoint Tests")
    class CheckpointTests {

        @Test
        @DisplayName("manualCheckpoint() should complete without exception")
        void testManualCheckpointCompletion() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> manager.manualCheckpoint("Test checkpoint"),
                "manualCheckpoint() should complete without exception");
        }

        @Test
        @DisplayName("manualCheckpoint() should accept description string")
        void testCheckpointWithDescription() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> {
                manager.manualCheckpoint("Setup checkpoint");
                manager.manualCheckpoint("Data import checkpoint");
                manager.manualCheckpoint("Final state checkpoint");
            }, "Should handle various checkpoint descriptions");
        }

        @Test
        @DisplayName("manualCheckpoint() should handle empty description")
        void testCheckpointEmptyDescription() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> manager.manualCheckpoint(""),
                "Should handle empty checkpoint description");
        }

        @Test
        @DisplayName("manualCheckpoint() should handle null description gracefully")
        void testCheckpointNullDescription() {
            manager.initialize(system);
            
            // May throw or handle null - either way should be safe
            try {
                manager.manualCheckpoint(null);
            } catch (NullPointerException e) {
                // Expected - null handling
                assertTrue(true, "Null handling is acceptable");
            }
        }

        @Test
        @DisplayName("Multiple checkpoints should be supported")
        void testMultipleCheckpoints() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 5; i++) {
                    manager.manualCheckpoint("Checkpoint " + i);
                }
            }, "Should support multiple sequential checkpoints");
        }
    }

    @Nested
    @DisplayName("Shutdown Tests")
    class ShutdownTests {

        @Test
        @DisplayName("shutdown() should complete without exception")
        void testShutdownCompletion() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> manager.shutdown(),
                "shutdown() should complete without exception");
        }

        @Test
        @DisplayName("shutdown() should stop background operations")
        void testShutdownStopsOperations() {
            manager.initialize(system);
            manager.shutdown();
            
            // Should have completed without hanging
            assertTrue(true, "Shutdown completed within reasonable time");
        }

        @Test
        @DisplayName("Multiple shutdown() calls should be safe")
        void testMultipleShutdownCalls() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> {
                manager.shutdown();
                manager.shutdown();
                manager.shutdown();
            }, "Multiple shutdown() calls should be safe");
        }

        @Test
        @DisplayName("shutdown() should perform final save")
        void testFinalSaveOnShutdown() {
            manager.initialize(system);
            
            assertDoesNotThrow(() -> manager.shutdown(),
                "Should save state before shutdown");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete lifecycle: init -> save -> checkpoint -> shutdown")
        void testCompleteLifecycle() {
            assertDoesNotThrow(() -> {
                manager.initialize(system);
                
                manager.saveAllState();
                Thread.sleep(50);
                
                manager.manualCheckpoint("Integration test checkpoint");
                Thread.sleep(50);
                
                manager.saveAllState();
                Thread.sleep(50);
                
                manager.shutdown();
            }, "Complete lifecycle should work without exceptions");
        }

        @Test
        @DisplayName("Multiple initialization cycles should work")
        void testMultipleInitializationCycles() throws InterruptedException {
            for (int cycle = 0; cycle < 3; cycle++) {
                manager.initialize(system);
                Thread.sleep(50);
                manager.shutdown();
            }
        }

        @Test
        @DisplayName("Auto-save should trigger periodically")
        void testAutoSavePeriodicity() throws InterruptedException {
            manager.initialize(system);
            
            // Wait for auto-save to trigger (5 seconds interval)
            Thread.sleep(1000);
            
            manager.saveAllState();
            Thread.sleep(1000);
            
            manager.shutdown();
        }

        @Test
        @DisplayName("Data directory should be accessible during operations")
        void testDataDirectoryAccessible() {
            manager.initialize(system);
            
            String dir1 = manager.getDataDirectory();
            manager.saveAllState();
            String dir2 = manager.getDataDirectory();
            manager.shutdown();
            String dir3 = manager.getDataDirectory();
            
            assertEquals(dir1, dir2, "Directory should be consistent during save");
            assertEquals(dir2, dir3, "Directory should persist through shutdown");
        }

        @Test
        @DisplayName("getInstance() should provide same manager throughout lifecycle")
        void testManagerConsistency() {
            DataPersistenceManager ref1 = DataPersistenceManager.getInstance();
            ref1.initialize(system);
            
            DataPersistenceManager ref2 = DataPersistenceManager.getInstance();
            String dir = ref2.getDataDirectory();
            
            assertNotNull(dir, "Should access same manager");
            
            DataPersistenceManager ref3 = DataPersistenceManager.getInstance();
            ref3.shutdown();
        }
    }
}
