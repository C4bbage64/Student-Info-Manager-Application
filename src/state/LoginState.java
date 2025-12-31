package state;

/**
 * Concrete state: Login state (initial state - not logged in).
 */
public class LoginState implements ApplicationState {
    @Override
    public void handleLogin() {
        System.out.println("Processing login...");
        // Login logic handled by LoginPanel
    }
    
    @Override
    public void handleLogout() {
        System.out.println("Already logged out.");
    }
    
    @Override
    public void handleStudentOperation() {
        System.out.println("Please login first to perform student operations.");
    }
    
    @Override
    public String getStateName() {
        return "LOGIN";
    }
}

