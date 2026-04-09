package vcfs.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ViewModel implements Serializable {
    private static final long serialVersionUID = 1L;
    protected transient PropertyChangeSupport propertyChangeSupport;
    protected Map<String, Object> properties;
    
    public ViewModel() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.properties = new HashMap<>();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Set a property value and fire property change event.
     *
     * @param propertyName The name of the property
     * @param value The new value
     */
    public void setProperty(String propertyName, Object value) {
        Object oldValue = properties.get(propertyName);
        properties.put(propertyName, value);
        firePropertyChange(propertyName, oldValue, value);
    }

    /**
     * Get a property value.
     *
     * @param propertyName The name of the property
     * @return The property value, or null if not set
     */
    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    /**
     * Clear all properties.
     */
    public void clear() {
        properties.clear();
    }
}
