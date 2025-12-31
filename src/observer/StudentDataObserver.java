package observer;

/**
 * Observer interface for Observer design pattern.
 * Implemented by classes that want to be notified of data changes.
 */
public interface StudentDataObserver {
    /**
     * Called when student data changes.
     * @param eventType Type of change (ADD, UPDATE, DELETE)
     */
    void onStudentDataChanged(String eventType);
}

