package state;

import util.SessionManager;

/**
 * Concrete state: Logged in state (active state).
 */
public class LoggedInState implements ApplicationState {
    @Override
    public void handleLogin() {
        String currentUser = SessionManager.getInstance().getCurrentUser();
        System.out.println("Already logged in as: " + currentUser);
    }
    
    @Override
    public void handleLogout() {
        System.out.println("Logging out...");
        SessionManager.getInstance().logout();
    }
    
    @Override
    public void handleStudentOperation() {
        System.out.println("Student operations available.");
    }
    
    @Override
    public String getStateName() {
        return "LOGGED_IN";
    }
}

