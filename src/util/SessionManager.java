package util;

/**
 * Singleton class for managing user session state.
 * Tracks logged-in user information across the application.
 */
public class SessionManager {
    private static SessionManager instance;
    private String currentUser;

    // Private constructor - prevents external instantiation (Singleton pattern)
    private SessionManager() {
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
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Gets the current logged-in username.
     * @return Username if logged in, null otherwise
     */
    public String getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently logged in.
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
