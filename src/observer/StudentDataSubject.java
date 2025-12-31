package observer;

/**
 * Subject interface for Observer design pattern.
 * Implemented by classes that notify observers of changes.
 */
public interface StudentDataSubject {
    /**
     * Adds an observer to the notification list.
     */
    void addObserver(StudentDataObserver observer);
    
    /**
     * Removes an observer from the notification list.
     */
    void removeObserver(StudentDataObserver observer);
    
    /**
     * Notifies all observers of a data change.
     * @param eventType Type of change (ADD, UPDATE, DELETE)
     */
    void notifyObservers(String eventType);
}

