package util;

/**
 * Singleton class for managing user session state.
 * Tracks logged-in user information across the application.
 */
public class SessionManager {
    private static SessionManager instance;
    private String currentUser;
    private boolean loggedIn;

    // Private constructor - prevents external instantiation (Singleton pattern)
    private SessionManager() {
        this.loggedIn = false;
        this.currentUser = null;
    }

    /**
     * Gets the singleton instance of SessionManager.
     * Thread-safe implementation.
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Logs in a user.
     */
    public void login(String username) {
        this.currentUser = username;
        this.loggedIn = true;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        this.currentUser = null;
        this.loggedIn = false;
    }

    /**
     * Gets the current logged-in username.
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
