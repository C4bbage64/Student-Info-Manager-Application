package state;

/**
 * State interface for State pattern.
 * Represents different states of the application (Login/LoggedIn).
 */
public interface ApplicationState {
    /**
     * Handles login operation.
     */
    void handleLogin();
    
    /**
     * Handles logout operation.
     */
    void handleLogout();
    
    /**
     * Handles student operations.
     */
    void handleStudentOperation();
    
    /**
     * Gets the name of the current state.
     */
    String getStateName();
}

