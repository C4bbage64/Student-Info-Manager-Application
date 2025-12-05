import view.LoginPanel;
import view.MainPanel;
import dao.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main application entry point.
 * Initializes the database and launches the GUI.
 */
public class StudentInfoApp {

    public static void main(String[] args) {
        // Initialize database connection
        try {
            DatabaseConnection.getInstance();
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to database: " + e.getMessage() + 
                "\n\nMake sure sqlite-jdbc driver is in your classpath.",
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

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
