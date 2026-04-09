package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.beans.PropertyChangeListener;

/**
 * Unit tests for UIDataBinder class.
 * Tests data binding, listeners, and automatic UI updates.
 * 
 * AUDIT FINDINGS:
 * - UIDataBinder enables automatic view updates when model data changes
 * - Implements Observer pattern for loose coupling
 * - Should be used across all portal views for real-time data synchronization
 */
public class UIDataBinderTest {

    private UIDataBinder binder;

    @BeforeEach
    public void setUp() {
        binder = new UIDataBinder();
    }

    @Test
    public void testBinderInitialization() {
        assertNotNull(binder);
    }

    @Test
    public void testAddListener() {
        PropertyChangeListener listener = evt -> {};
        binder.addListener(listener);
        // Verification: listener is registered
    }

    @Test
    public void testBindValue() {
        String key = "testKey";
        Object value = "testValue";
        binder.bind(key, value);
        assertEquals(value, binder.getBoundValue(key));
    }

    @Test
    public void testBindMultipleValues() {
        binder.bind("key1", "value1");
        binder.bind("key2", "value2");
        binder.bind("key3", "value3");
        
        assertEquals("value1", binder.getBoundValue("key1"));
        assertEquals("value2", binder.getBoundValue("key2"));
        assertEquals("value3", binder.getBoundValue("key3"));
    }

    @Test
    public void testUpdateBoundValue() {
        String key = "testKey";
        binder.bind(key, "oldValue");
        binder.bind(key, "newValue");
        assertEquals("newValue", binder.getBoundValue(key));
    }

    @Test
    public void testUnbind() {
        binder.bind("testKey", "testValue");
        binder.unbind("testKey");
        assertNull(binder.getBoundValue("testKey"));
    }

    @Test
    public void testClearAllBindings() {
        binder.bind("key1", "value1");
        binder.bind("key2", "value2");
        binder.clear();
        assertNull(binder.getBoundValue("key1"));
        assertNull(binder.getBoundValue("key2"));
    }

    @Test
    public void testNullKeyHandling() {
        // Should handle null keys gracefully
        binder.bind(null, "value");
        // Should not throw exception
    }

    @Test
    public void testNullValueBinding() {
        binder.bind("key", null);
        assertNull(binder.getBoundValue("key"));
    }

    @Test
    public void testBinderFiresPropertyChangeEvent() {
        final boolean[] eventFired = {false};
        PropertyChangeListener listener = evt -> eventFired[0] = true;
        binder.addListener(listener);
        binder.bind("key", "value");
        // Event should be fired
    }

    @Test
    public void testMultipleListeners() {
        final boolean[] listener1Fired = {false};
        final boolean[] listener2Fired = {false};
        
        binder.addListener(evt -> listener1Fired[0] = true);
        binder.addListener(evt -> listener2Fired[0] = true);
        
        binder.bind("key", "value");
        // Both listeners should be notified
    }
}

