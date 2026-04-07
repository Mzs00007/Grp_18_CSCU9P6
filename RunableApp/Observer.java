/**
 * Observer interface.
 * Any class that wants to receive system event notifications
 * must implement this interface.
 *
 * Implemented by: AdministratorScreen (displays events in audit log)
 *
 * @author leiyam (YAMI) — VCFS Observer Pattern
 */
public interface Observer {
    void update(String message);
}
