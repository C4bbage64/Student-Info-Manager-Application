package state;

/**
 * Context for student enrollment state management.
 * Manages transitions between enrollment states.
 */
public class StudentEnrollmentContext {
    private StudentEnrollmentState currentState;
    
    /**
     * Creates a new context with EnrolledState as default.
     */
    public StudentEnrollmentContext() {
        this.currentState = new EnrolledState();
    }
    
    /**
     * Creates a new context with specified initial state.
     */
    public StudentEnrollmentContext(StudentEnrollmentState initialState) {
        this.currentState = initialState;
    }
    
    /**
     * Sets the current enrollment state.
     */
    public void setState(StudentEnrollmentState state) {
        this.currentState = state;
        System.out.println("Enrollment state changed to: " + state.getStatus());
    }
    
    /**
     * Gets the current enrollment state.
     */
    public StudentEnrollmentState getCurrentState() {
        return currentState;
    }
    
    /**
     * Handles enrollment operation.
     */
    public void enroll() {
        currentState.enroll();
        if (currentState instanceof SuspendedState || currentState instanceof GraduatedState) {
            setState(new EnrolledState());
        }
    }
    
    /**
     * Handles suspension operation and transitions to SuspendedState.
     */
    public void suspend() {
        currentState.suspend();
        if (currentState instanceof EnrolledState) {
            setState(new SuspendedState());
        }
    }
    
    /**
     * Handles graduation operation and transitions to GraduatedState.
     */
    public void graduate() {
        currentState.graduate();
        if (currentState instanceof EnrolledState) {
            setState(new GraduatedState());
        }
    }
    
    /**
     * Handles activation/reactivation operation and transitions to EnrolledState.
     */
    public void activate() {
        currentState.activate();
        if (currentState instanceof SuspendedState) {
            setState(new EnrolledState());
        }
    }
    
    /**
     * Gets the current enrollment status string.
     */
    public String getStatus() {
        return currentState.getStatus();
    }
    
    /**
     * Checks if student is currently enrolled (active).
     */
    public boolean isEnrolled() {
        return currentState instanceof EnrolledState;
    }
    
    /**
     * Checks if student is suspended.
     */
    public boolean isSuspended() {
        return currentState instanceof SuspendedState;
    }
    
    /**
     * Checks if student is graduated.
     */
    public boolean isGraduated() {
        return currentState instanceof GraduatedState;
    }
}

