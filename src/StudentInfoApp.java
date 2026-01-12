import view.LoginPanel;
import view.MainPanel;
import dao.DatabaseConnection;
import state.ApplicationStateContext;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main application entry point.
 * Initializes the database and launches the GUI.
 */
public class StudentInfoApp {

    public static void main(String[] args) {
        // Check for headless mode
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("ERROR: Cannot run GUI application in headless mode.");
            System.err.println("DISPLAY variable: " + System.getenv("DISPLAY"));
            System.err.println("\nPlease ensure:");
            System.err.println("1. DISPLAY environment variable is set (e.g., export DISPLAY=:0)");
            System.err.println("2. X11 server is running");
            System.err.println("3. You have permission to access the display (try: xhost +local:)");
            System.exit(1);
        }
        
        // Initialize database connection
        try {
            DatabaseConnection.getInstance();
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            System.err.println("\nMake sure sqlite-jdbc driver is in your classpath.");
            System.exit(1);
        }

        // State Pattern: Initialize application state context (starts in LoginState)
        ApplicationStateContext.getInstance();
        System.out.println("Application state initialized.");

        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Nimbus Look and Feel
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create main frame
            JFrame frame = new JFrame("Student Info Manager Application");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null); // Center on screen

            // Create base panel with CardLayout for view switching
            JPanel cardPanel = new JPanel(new CardLayout());

            // Create panels
            LoginPanel loginPanel = new LoginPanel();
            MainPanel mainPanel = new MainPanel();

            // Add panels to card layout
            cardPanel.add(loginPanel, "Login");
            cardPanel.add(mainPanel, "Main");

            // Add to frame
            frame.add(cardPanel);
            frame.setVisible(true);

            // Show login panel initially
            CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
            cardLayout.show(cardPanel, "Login");

            // Add shutdown hook to close database connection
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    DatabaseConnection.getInstance().closeConnection();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }));
        });
    }
}
