package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Subject for Observer pattern.
 * Manages observers and notifies them of student data changes.
 * Uses Singleton pattern to ensure single instance.
 */
public class StudentDataManager implements StudentDataSubject {
    private static StudentDataManager instance;
    private List<StudentDataObserver> observers;
    
    private StudentDataManager() {
        this.observers = new ArrayList<>();
    }
    
    /**
     * Gets the singleton instance of StudentDataManager.
     * Thread-safe implementation.
     */
    public static synchronized StudentDataManager getInstance() {
        if (instance == null) {
            instance = new StudentDataManager();
        }
        return instance;
    }
    
    @Override
    public void addObserver(StudentDataObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void removeObserver(StudentDataObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(String eventType) {
        for (StudentDataObserver observer : observers) {
            observer.onStudentDataChanged(eventType);
        }
    }
    
    /**
     * Convenience method: Notifies observers when a student is added.
     */
    public void notifyStudentAdded() {
        notifyObservers("ADD");
    }
    
    /**
     * Convenience method: Notifies observers when a student is updated.
     */
    public void notifyStudentUpdated() {
        notifyObservers("UPDATE");
    }
    
    /**
     * Convenience method: Notifies observers when a student is deleted.
     */
    public void notifyStudentDeleted() {
        notifyObservers("DELETE");
    }
}

