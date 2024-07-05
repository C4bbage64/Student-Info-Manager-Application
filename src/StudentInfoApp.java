import javax.swing.*;
import java.awt.*;

public class StudentInfoApp {
    // Instance variable to hold the implementation of the StudentManagerInterface
    private StudentManagerInterface studentManager;

    // Constructor for the StudentInfoApp class
    public StudentInfoApp() {
        // Create an instance of the StudentManager class, which likely implements the StudentManagerInterface
        studentManager = new StudentManager();
    }

    public static void main(String[] args) {
        // Schedules the application to be run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new StudentInfoApp()); {
            // Create the main application frame (window)
            JFrame frame = new JFrame("Student Info Manager Application");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits when the window is closed
            frame.setSize(800, 600); // Set the initial size of the window

            // Create an instance of your StudentManager implementation
            StudentManager studentManager = new StudentManager(); // Replace with your implementation

            // Create the base panel with a CardLayout to manage switching between different panels
            BasePanel basePanel = new BasePanel();
            basePanel.setLayout(new CardLayout());

            // Create instances of the login and main panels
            LoginPanel loginPanel = new LoginPanel();
            MainPanel mainPanel = new MainPanel(studentManager);

            // Add the login and main panels to the base panel with associated names
            basePanel.add(loginPanel, "Login");
            basePanel.add(mainPanel, "Main");

            // Add the base panel to the frame
            frame.add(basePanel);
            frame.setVisible(true); // Make the frame visible

            // Show the login panel initially by switching the CardLayout to the "Login" card
            CardLayout cardLayout = (CardLayout) basePanel.getLayout();
            cardLayout.show(basePanel, "Login");
        }
    }
}
