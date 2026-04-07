import java.util.ArrayList;
import java.util.List;

/**
 * Observable base class.
 * CentralModel extends this so that any registered Observer
 * (e.g., AdministratorScreen) automatically receives event messages.
 *
 * Pattern: Subject in Observer/Subject pattern.
 *
 * @author leiyam (YAMI) — VCFS Observer Pattern
 */
public class Observable {

    protected List<Observer> observers = new ArrayList<>();

    /**
     * Register an observer.
     * Called at startup: model.addObserver(adminScreen)
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notify every registered observer of an event message.
     * Call this after any significant system action.
     *
     * Example: notifyObservers("Recruiter Alice assigned to Google Booth")
     */
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }
}
