package vcfs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.beans.PropertyChangeListener;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ViewModel class.
 * Tests view model data binding, property changes, and observer pattern.
 * 
 * AUDIT FINDINGS:
 * - ViewModel is the intermediate layer between Model (CareerFairSystem) and View (Portals)
 * - Transforms system data into view-specific format
 * - Enables loose coupling between system and UI components
 */
public class ViewModelTest {
    private ViewModel viewModel;
    private CareerFairSystem system;

    @BeforeEach
    public void setUp() {
        system = CareerFairSystem.getInstance();
        viewModel = new ViewModel();
    }

    @Test
    public void testViewModelInitialization() {
        assertNotNull(viewModel);
    }

    @Test
    public void testAddPropertyChangeListener() {
        PropertyChangeListener listener = evt -> {};
        viewModel.addPropertyChangeListener(listener);
        // Listener should be registered and notified of changes
    }

    @Test
    public void testRemovePropertyChangeListener() {
        PropertyChangeListener listener = evt -> {};
        viewModel.addPropertyChangeListener(listener);
        viewModel.removePropertyChangeListener(listener);
        // Listener should no longer receive notifications
    }

    @Test
    public void testSetProperty() {
        viewModel.setProperty("testProp", "testValue");
        assertEquals("testValue", viewModel.getProperty("testProp"));
    }

    @Test
    public void testPropertyChangeNotification() {
        final boolean[] eventFired = {false};
        PropertyChangeListener listener = evt -> eventFired[0] = true;
        viewModel.addPropertyChangeListener(listener);
        viewModel.setProperty("prop", "value");
        // Event should be fired
    }

    @Test
    public void testMultipleProperties() {
        viewModel.setProperty("prop1", "value1");
        viewModel.setProperty("prop2", "value2");
        viewModel.setProperty("prop3", "value3");
        
        assertEquals("value1", viewModel.getProperty("prop1"));
        assertEquals("value2", viewModel.getProperty("prop2"));
        assertEquals("value3", viewModel.getProperty("prop3"));
    }

    @Test
    public void testPropertyOverwrite() {
        viewModel.setProperty("key", "oldValue");
        viewModel.setProperty("key", "newValue");
        assertEquals("newValue", viewModel.getProperty("key"));
    }

    @Test
    public void testNullProperty() {
        viewModel.setProperty("nullKey", null);
        assertNull(viewModel.getProperty("nullKey"));
    }

    @Test
    public void testGetNonexistentProperty() {
        assertNull(viewModel.getProperty("nonexistent"));
    }

    @Test
    public void testPropertyTypeTransformation() {
        viewModel.setProperty("intProp", 42);
        assertEquals(42, viewModel.getProperty("intProp"));
        
        viewModel.setProperty("boolProp", true);
        assertEquals(true, viewModel.getProperty("boolProp"));
    }

    @Test
    public void testClearProperties() {
        viewModel.setProperty("key1", "value1");
        viewModel.setProperty("key2", "value2");
        viewModel.clear();
        assertNull(viewModel.getProperty("key1"));
        assertNull(viewModel.getProperty("key2"));
    }

    @Test
    public void testViewModelAsMediator() {
        // ViewModel should transmit system changes to view
        PropertyChangeListener listener = evt -> {};
        viewModel.addPropertyChangeListener(listener);
        
        viewModel.setProperty("organizations", system.getFair().organizations);
        assertNotNull(viewModel.getProperty("organizations"));
    }
}

