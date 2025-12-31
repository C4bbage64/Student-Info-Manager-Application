package state;

/**
 * Context class that maintains current state and delegates requests to state objects.
 * Uses Singleton pattern to ensure single instance across the application.
 */
public class ApplicationStateContext {
    private ApplicationState currentState;
    private static ApplicationStateContext instance;
    
    private ApplicationStateContext() {
        this.currentState = new LoginState();
    }
    
    /**
     * Gets the singleton instance of ApplicationStateContext.
     * Thread-safe implementation.
     */
    public static synchronized ApplicationStateContext getInstance() {
        if (instance == null) {
            instance = new ApplicationStateContext();
        }
        return instance;
    }
    
    /**
     * Sets the current state and logs the transition.
     */
    public void setState(ApplicationState state) {
        this.currentState = state;
        System.out.println("State changed to: " + state.getStateName());
    }
    
    /**
     * Gets the current state.
     */
    public ApplicationState getCurrentState() {
        return currentState;
    }
    
    /**
     * Handles login operation through current state.
     */
    public void handleLogin() {
        currentState.handleLogin();
    }
    
    /**
     * Handles logout operation and transitions to LoginState.
     */
    public void handleLogout() {
        currentState.handleLogout();
        setState(new LoginState());
    }
    
    /**
     * Handles student operation through current state.
     */
    public void handleStudentOperation() {
        currentState.handleStudentOperation();
    }
    
    /**
     * Checks if the application is in logged in state.
     */
    public boolean isLoggedIn() {
        return currentState instanceof LoggedInState;
    }
}

