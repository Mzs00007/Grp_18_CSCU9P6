package vcfs.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SystemTimer Unit Tests - Testing Singleton + Observer pattern
 * FIXED VERSION: Uses correct method signatures and jumpTo() instead of setCurrentTime()
 */
public class SystemTimerTest {
    
    private SystemTimer timer;
    
    @BeforeEach
    void setUp() {
        timer = SystemTimer.getInstance();
    }
    
    @Test
    void testSingleton_GetInstanceReturnsNotNull() {
        assertNotNull(timer);
    }
    
    @Test
    void testSingleton_GetInstanceReturnsSameInstance() {
        SystemTimer timer1 = SystemTimer.getInstance();
        SystemTimer timer2 = SystemTimer.getInstance();
        assertSame(timer1, timer2);
    }
    
    @Test
    void testGetNow_ReturnsCurrentTime() {
        LocalDateTime now = timer.getNow();
        assertNotNull(now);
        // Verify it's a valid LocalDateTime by checking it has a string representation
        String timeStr = now.toString();
        assertNotNull(timeStr);
        assertTrue(timeStr.length() > 0);
    }
    
    @Test
    void testStepMinutes_AdvancesTime() {
        LocalDateTime before = timer.getNow();
        timer.stepMinutes(30);
        LocalDateTime after = timer.getNow();
        
        long minutesDifference = before.minutesUntil(after);
        assertEquals(30L, minutesDifference);
    }
    
    @Test
    void testStepMinutes_NegativeValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            timer.stepMinutes(-10);
        });
    }
    
    @Test
    void testStepMinutes_ZeroValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            timer.stepMinutes(0);
        });
    }
    
    @Test
    void testJumpTo_SetsTimeDirectly() {
        LocalDateTime targetTime = new LocalDateTime(2026, 4, 8, 14, 30);
        timer.jumpTo(targetTime);
        
        LocalDateTime now = timer.getNow();
        assertTrue(now.isEqual(targetTime), "Time should be set to target time after jumpTo");
    }
    
    @Test
    void testJumpTo_NullValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            timer.jumpTo(null);
        });
    }
    
    @Test
    void testPropertyChangeListener_ReceivesEventOnStepMinutes() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        
        PropertyChangeListener testListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("time".equals(evt.getPropertyName())) {
                    listenerCalled.set(true);
                }
            }
        };
        
        timer.addPropertyChangeListener(testListener);
        timer.stepMinutes(1);
        
        assertTrue(listenerCalled.get(), "PropertyChangeListener should have been called");
    }
    
    @Test
    void testPropertyChangeListener_ReceivesEventOnJumpTo() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        
        PropertyChangeListener testListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("time".equals(evt.getPropertyName())) {
                    listenerCalled.set(true);
                }
            }
        };
        
        timer.addPropertyChangeListener(testListener);
        timer.jumpTo(new LocalDateTime(2026, 4, 8, 18, 30));
        
        assertTrue(listenerCalled.get(), "PropertyChangeListener should have been called on jumpTo()");
    }

    // ===================================================================
    // CONCURRENT ACCESS TESTS - Thread-safety of Singleton + Observer
    // ===================================================================

    @Test
    void testConcurrentAccess_SingletonReturnsConsistentInstance() throws InterruptedException {
        java.util.Set<SystemTimer> instances = java.util.Collections.synchronizedSet(new java.util.HashSet<>());
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(10);
        
        // Launch 10 threads, each getting the singleton
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                instances.add(SystemTimer.getInstance());
                latch.countDown();
            }).start();
        }
        
        latch.await(); // Wait for all threads to complete
        
        // All threads should have retrieved the same instance
        assertEquals(1, instances.size(), 
            "All concurrent getInstance() calls should return same instance");
    }

    @Test
    void testConcurrentAccess_MultipleListenersRegistration() throws InterruptedException {
        java.util.concurrent.atomic.AtomicInteger eventCount = new java.util.concurrent.atomic.AtomicInteger(0);
        int listenerCount = 5;
        java.util.concurrent.CountDownLatch registrationLatch = 
            new java.util.concurrent.CountDownLatch(listenerCount);
        
        // Register 5 listeners concurrently
        for (int i = 0; i < listenerCount; i++) {
            new Thread(() -> {
                timer.addPropertyChangeListener(evt -> {
                    if ("time".equals(evt.getPropertyName())) {
                        eventCount.incrementAndGet();
                    }
                });
                registrationLatch.countDown();
            }).start();
        }
        
        registrationLatch.await(); // Wait for all listeners registered
        
        // Trigger a time change
        LocalDateTime before = timer.getNow();
        timer.stepMinutes(1);
        LocalDateTime after = timer.getNow();
        
        // Ensure before and after are different to use the variables
        assertTrue(before.minutesUntil(after) > 0, "Time should advance after stepMinutes");
        
        // Each listener should have fired once (5 total)
        assertEquals(listenerCount, eventCount.get(), 
            "All " + listenerCount + " listeners should have received the event");
    }

    @Test
    void testConcurrentAccess_ConcurrentStepMinutes() throws InterruptedException {
        LocalDateTime startTime = timer.getNow();
        int threadCount = 4;
        int minutesPerThread = 10;
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(threadCount);
        
        // Launch 4 threads, each stepping 10 minutes
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                timer.stepMinutes(minutesPerThread);
                latch.countDown();
            }).start();
        }
        
        latch.await(); // Wait for all threads
        
        LocalDateTime endTime = timer.getNow();
        long totalMinutes = startTime.minutesUntil(endTime);
        
        // Should have advanced by 40 minutes total
        assertEquals(threadCount * minutesPerThread, totalMinutes,
            "Concurrent stepMinutes calls should accumulate correctly");
    }

    @Test
    void testConcurrentAccess_JumpToIsThreadSafe() throws InterruptedException {
        LocalDateTime target1 = new LocalDateTime(2026, 5, 1, 10, 0);
        LocalDateTime target2 = new LocalDateTime(2026, 5, 2, 10, 0);
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(2);
        
        // Thread 1 jumps to target1
        new Thread(() -> {
            timer.jumpTo(target1);
            latch.countDown();
        }).start();
        
        // Thread 2 jumps to target2
        new Thread(() -> {
            timer.jumpTo(target2);
            latch.countDown();
        }).start();
        
        latch.await(); // Wait for both threads
        
        LocalDateTime finalTime = timer.getNow();
        
        // Final time should be one of the two targets (whichever completed last)
        boolean isTarget1 = finalTime.isEqual(target1);
        boolean isTarget2 = finalTime.isEqual(target2);
        
        assertTrue(isTarget1 || isTarget2,
            "Final time should be one of the two target times");
    }

    @Test
    void testConcurrentAccess_StepAndListenConcurrently() throws InterruptedException {
        java.util.concurrent.atomic.AtomicInteger eventsFired = 
            new java.util.concurrent.atomic.AtomicInteger(0);
        
        // Register listener in one thread
        PropertyChangeListener listener = evt -> {
            if ("time".equals(evt.getPropertyName())) {
                eventsFired.incrementAndGet();
            }
        };
        timer.addPropertyChangeListener(listener);
        
        // Step time in multiple threads concurrently
        int threadCount = 3;
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                timer.stepMinutes(5);
                latch.countDown();
            }).start();
        }
        
        latch.await();
        
        // Each stepMinutes should have fired the listener
        assertEquals(threadCount, eventsFired.get(),
            "Each concurrent stepMinutes should fire property change event");
    }
}
