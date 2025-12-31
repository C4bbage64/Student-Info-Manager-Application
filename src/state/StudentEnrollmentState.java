package state;

/**
 * State interface for student enrollment status.
 * Represents different states of student enrollment (Enrolled/Suspended/Graduated).
 */
public interface StudentEnrollmentState {
    /**
     * Handles enrollment operation.
     */
    void enroll();
    
    /**
     * Handles suspension operation.
     */
    void suspend();
    
    /**
     * Handles graduation operation.
     */
    void graduate();
    
    /**
     * Handles activation/reactivation operation.
     */
    void activate();
    
    /**
     * Gets the current enrollment status.
     */
    String getStatus();
}

