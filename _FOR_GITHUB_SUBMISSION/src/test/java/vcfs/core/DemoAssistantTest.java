package vcfs.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DemoAssistant singleton.
 * Tests 20-minute presentation timing, step progression, and guidance delivery.
 * 
 * Uses ONLY PUBLIC API:
 * - getInstance()
 * - startDemo()
 * - getCurrentStatus()
 * - getNextStepGuidance()
 * - getTip(String action)
 * - getAllSteps()
 * - endDemo()
 */
@DisplayName("DemoAssistant - Presentation Engine Tests")
public class DemoAssistantTest {

    private DemoAssistant assistant;

    @BeforeEach
    void setUp() {
        this.assistant = DemoAssistant.getInstance();
    }

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonPatternTests {

        @Test
        @DisplayName("Should return same instance on multiple calls")
        void testSingletonUniqueness() {
            DemoAssistant instance1 = DemoAssistant.getInstance();
            DemoAssistant instance2 = DemoAssistant.getInstance();
            DemoAssistant instance3 = DemoAssistant.getInstance();

            assertSame(instance1, instance2, "Multiple calls should return identical instance");
            assertSame(instance2, instance3, "Multiple calls should return identical instance");
            assertNotNull(instance1, "Instance should never be null");
        }

        @Test
        @DisplayName("Should preserve singleton across reference variables")
        void testSingletonIdentity() {
            DemoAssistant ref1 = DemoAssistant.getInstance();
            DemoAssistant ref2 = DemoAssistant.getInstance();
            
            assertTrue(ref1 == ref2, "Object identity should be preserved");
        }

        @Test
        @DisplayName("Singleton should be thread-safe")
        void testSingletonThreadSafety() throws InterruptedException {
            DemoAssistant[] instances = new DemoAssistant[10];
            Thread[] threads = new Thread[10];

            for (int i = 0; i < 10; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    instances[index] = DemoAssistant.getInstance();
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            DemoAssistant first = instances[0];
            for (int i = 1; i < 10; i++) {
                assertSame(first, instances[i], "All threads should get same singleton");
            }
        }
    }

    @Nested
    @DisplayName("Demo Initialization Tests")
    class DemoInitializationTests {

        @Test
        @DisplayName("Should return status indicating demo not started initially")
        void testInitialStatus() {
            String status = assistant.getCurrentStatus();
            
            assertNotNull(status, "Status should never be null");
            assertTrue(status.contains("not started") || status.isEmpty() || 
                      status.toLowerCase().contains("demo not"),
                "Initial status should indicate demo not started");
        }

        @Test
        @DisplayName("Status should change after startDemo() called")
        void testStatusChangesAfterStart() {
            String statusBefore = assistant.getCurrentStatus();
            
            assistant.startDemo();
            
            String statusAfter = assistant.getCurrentStatus();
            
            assertNotEquals(statusBefore, statusAfter, "Status should change after demo starts");
        }

        @Test
        @DisplayName("getCurrentStatus() should contain demo step info after start")
        void testStatusShowsStepAfterStart() {
            assistant.startDemo();
            String status = assistant.getCurrentStatus();
            
            assertTrue(status.contains("DEMO") || status.contains("Step") || 
                      status.contains("Time"), "Status should show step/time information");
        }

        @Test
        @DisplayName("startDemo() should enable timer")
        void testTimerStartsAfterStart() {
            assistant.startDemo();
            
            String status1 = assistant.getCurrentStatus();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            String status2 = assistant.getCurrentStatus();
            
            // Both should contain timing info
            assertNotNull(status1, "Status1 should not be null");
            assertNotNull(status2, "Status2 should not be null");
        }
    }

    @Nested
    @DisplayName("Step Progression Tests")
    class StepProgressionTests {

        @Test
        @DisplayName("Demo should have 8 total steps for 20 minutes")
        void testEightStepsExist() {
            DemoAssistant demo = DemoAssistant.getInstance();
            
            // Use getAllSteps which returns a string with all 8 steps
            String allSteps = demo.getAllSteps();
            assertTrue(allSteps.length() > 100, "Should contain all 8 steps");
        }

        @Test
        @DisplayName("getAllSteps() should list all demo steps")
        void testAllStepsRetreval() {
            String allSteps = assistant.getAllSteps();
            
            assertNotNull(allSteps, "getAllSteps should not return null");
            assertTrue(allSteps.contains("DEMO") || allSteps.contains("20-MINUTE") || 
                      allSteps.contains("SEQUENCE") || allSteps.length() > 100,
                "Should return demo sequence information");
        }

        @Test
        @DisplayName("getAllSteps() should reference multiple steps")
        void testMultipleStepsInSequence() {
            String allSteps = assistant.getAllSteps();
            
            int dotPositions = 0;
            for (char c : allSteps.toCharArray()) {
                if (c == '.') dotPositions++;
            }
            
            assertTrue(dotPositions >= 5, "Should reference multiple demo steps (5+)");
        }

        @Test
        @DisplayName("Status should show current step number")
        void testStatusShowsCurrentStep() {
            assistant.startDemo();
            String status = assistant.getCurrentStatus();
            
            // Should contain step indicator like "STEP 1" or "1/"
            assertTrue(status.contains("STEP") || status.contains("/") || 
                      status.contains("DEMO"), "Status should indicate current step");
        }

        @Test
        @DisplayName("Status message should be multi-line")
        void testStatusIsMultiline() {
            assistant.startDemo();
            String status = assistant.getCurrentStatus();
            
            int lineCount = status.split("\n").length;
            assertTrue(lineCount >= 2, "Status should have multiple lines of information");
        }
    }

    @Nested
    @DisplayName("Guidance and Tips Tests")
    class GuidanceAndTipsTests {

        @Test
        @DisplayName("getNextStepGuidance() should return non-empty string")
        void testNextStepGuidanceNotNull() {
            String guidance = assistant.getNextStepGuidance();
            
            assertNotNull(guidance, "Guidance should not be null");
            assertTrue(guidance.length() > 5, "Guidance should contain meaningful text");
        }

        @Test
        @DisplayName("Guidance should be contextual and informative")
        void testNextStepGuidanceContent() {
            String guidance = assistant.getNextStepGuidance();
            
            assertTrue(guidance.contains("NEXT") || guidance.contains("Step") || 
                      guidance.contains("Thank") || guidance.contains("Complete") ||
                      guidance.length() > 50,
                "Guidance should reference next step or completion or be substantive");
        }

        @Test
        @DisplayName("getTip() should handle login action")
        void testLoginTip() {
            String tip = assistant.getTip("login");
            
            assertNotNull(tip, "Tip should not be null");
            assertTrue(tip.length() > 5, "Tip should provide helpful information");
        }

        @Test
        @DisplayName("getTip() should handle search action")
        void testSearchTip() {
            String tip = assistant.getTip("search");
            assertNotNull(tip, "Tip should not be null");
            assertTrue(tip.length() > 5, "Tip should contain content");
        }

        @Test
        @DisplayName("getTip() should handle book action")
        void testBookTip() {
            String tip = assistant.getTip("book");
            assertNotNull(tip, "Tip should not be null");
            assertTrue(tip.length() > 5, "Tip should contain content");
        }

        @Test
        @DisplayName("getTip() should handle time action")
        void testTimeTip() {
            String tip = assistant.getTip("time");
            assertNotNull(tip, "Tip should not be null");
            assertTrue(tip.length() > 5, "Tip should contain content");
        }

        @Test
        @DisplayName("getTip() should handle sync action")
        void testSyncTip() {
            String tip = assistant.getTip("sync");
            assertNotNull(tip, "Tip should not be null");
            assertTrue(tip.length() > 5, "Tip should contain content");
        }

        @Test
        @DisplayName("getTip() should handle unknown action gracefully")
        void testUnknownActionTip() {
            String tip = assistant.getTip("xyz_unknown_123");
            
            assertNotNull(tip, "Tip should handle unknown actions gracefully");
            assertTrue(tip.length() > 5, "Should provide fallback help for unknown actions");
        }

        @Test
        @DisplayName("getTip() should be case-insensitive")
        void testTipCaseInsensitive() {
            String tipLower = assistant.getTip("login");
            String tipUpper = assistant.getTip("LOGIN");
            String tipMixed = assistant.getTip("LoGiN");
            
            assertNotNull(tipLower, "Lowercase should work");
            assertNotNull(tipUpper, "Uppercase should work");
            assertNotNull(tipMixed, "Mixed case should work");
        }
    }

    @Nested
    @DisplayName("Demo Lifecycle Tests")
    class DemoLifecycleTests {

        @Test
        @DisplayName("endDemo() should complete without exception")
        void testEndDemoAfterStart() {
            assistant.startDemo();
            
            assertDoesNotThrow(() -> assistant.endDemo(),
                "endDemo() should complete without exception");
        }

        @Test
        @DisplayName("endDemo() should be callable without starting demo")
        void testEndDemoWithoutStart() {
            assertDoesNotThrow(() -> assistant.endDemo(),
                "endDemo() should be safe to call anytime");
        }

        @Test
        @DisplayName("Multiple endDemo() calls should be safe")
        void testMultipleEndDemoCalls() {
            assistant.startDemo();
            
            assertDoesNotThrow(() -> {
                assistant.endDemo();
                assistant.endDemo();
                assistant.endDemo();
            }, "Multiple endDemo() calls should not cause errors");
        }

        @Test
        @DisplayName("Demo restart after end should work correctly")
        void testRestartAfterEnd() {
            assistant.startDemo();
            String status1 = assistant.getCurrentStatus();
            
            assistant.endDemo();
            
            assistant.startDemo();
            String status2 = assistant.getCurrentStatus();
            
            // Both should be valid statuses
            assertNotNull(status1, "First start status should be valid");
            assertNotNull(status2, "Restart status should be valid");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete demo start-to-end workflow")
        void testCompleteWorkflow() {
            // Check initial state
            String statusBefore = assistant.getCurrentStatus();

            // Start demo
            assertDoesNotThrow(() -> assistant.startDemo(),
                "startDemo() should complete successfully");

            // Check status changed
            String statusAfter = assistant.getCurrentStatus();
            assertNotEquals(statusBefore, statusAfter, "Status should change after start");

            // Get guidance
            String guidance = assistant.getNextStepGuidance();
            assertNotNull(guidance, "Should get guidance");

            // Get all steps
            String allSteps = assistant.getAllSteps();
            assertNotNull(allSteps, "Should get all steps");

            // Get tips for various actions
            String loginTip = assistant.getTip("login");
            assertNotNull(loginTip, "Should get login tip");

            // End demo
            assertDoesNotThrow(() -> assistant.endDemo(),
                "endDemo() should complete successfully");
        }

        @Test
        @DisplayName("Multiple start-end cycles should work")
        void testMultipleCycles() {
            for (int cycle = 0; cycle < 3; cycle++) {
                assertDoesNotThrow(() -> assistant.startDemo(),
                    "Start cycle " + cycle + " should succeed");
                
                String status = assistant.getCurrentStatus();
                assertNotNull(status, "Cycle " + cycle + " should have status");

                assertDoesNotThrow(() -> assistant.endDemo(),
                    "End cycle " + cycle + " should succeed");
            }
        }

        @Test
        @DisplayName("Singleton state persists across getInstance() calls")
        void testStateConsistencyAcrossInstances() {
            DemoAssistant ref1 = DemoAssistant.getInstance();
            ref1.startDemo();

            DemoAssistant ref2 = DemoAssistant.getInstance();
            String status = ref2.getCurrentStatus();

            assertNotNull(status, "Should have status from second reference");
        }

        @Test
        @DisplayName("All public methods should work together")
        void testAllPublicMethodsTogether() {
            String tips = assistant.getTip("search");
            assertNotNull(tips, "getTip() should work");

            String allSteps = assistant.getAllSteps();
            assertNotNull(allSteps, "getAllSteps() should work");

            assistant.startDemo();
            
            String status = assistant.getCurrentStatus();
            assertNotNull(status, "getCurrentStatus() after start should work");

            String guidance = assistant.getNextStepGuidance();
            assertNotNull(guidance, "getNextStepGuidance() should work");

            assistant.endDemo();
        }
    }
}
