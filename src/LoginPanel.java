import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginPanel extends BasePanel {

    private String storedUsername;
    private String storedPassword;

    private static final String CREDENTIALS_FILE = "C:/Users/faiz/IdeaProjects/StudentInformationApplication/src/credentials.txt";

    // Constructor for LoginPanel
    public LoginPanel() {
        LoadCredentials(); // Load the credentials from a file
        setupUI(); // Set up the user interface
    }

    // Method to load credentials from the specified file
    private void LoadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            // Read the username and password from the file
            storedUsername = reader.readLine();
            storedPassword = reader.readLine();
        } catch (IOException e) {
            // Show an error dialog if there's an issue reading the file
            showMessageDialog("Error", "Error reading credentials file");
            System.exit(1); // Exit the application
        }
    }

    // Method to set up the user interface components
    private void setupUI() {
        // Set the layout and border for the panel
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // GridBagConstraints for arranging components in the GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Components fill horizontally

        // Create labels, text fields, and a button
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // Set styles for components
        setComponentStyles(loginButton, usernameLabel, passwordLabel);

        // Add the labels and fields to the panel
        addLabelAndField(this, gbc, usernameLabel, usernameField, 0);
        addLabelAndField(this, gbc, passwordLabel, passwordField, 1);

        // Add the login button to the panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(loginButton, gbc);

        // Add action listener to the login button
        loginButton.addActionListener(e -> {
            // Get the entered username and password
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check if the entered credentials match the stored credentials
            if (storedUsername.equals(username) && storedPassword.equals(password)) {
                // Get the parent container (BasePanel)
                Container parent = getParent();
                if (parent instanceof JPanel) {
                    // Get the CardLayout from the parent panel
                    JPanel basePanel = (JPanel) parent;
                    CardLayout cardLayout = (CardLayout) basePanel.getLayout();

                    // Show the "Main" panel
                    cardLayout.show(basePanel, "Main");
                }
            } else {
                // Show an error dialog if the credentials are invalid
                showMessageDialog("Error", "Invalid username or password!");
            }
        });
    }
}
