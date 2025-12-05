package view;

import util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Login panel for user authentication.
 * Part of MVC architecture - View layer.
 */
public class LoginPanel extends BasePanel {

    private String storedUsername;
    private String storedPassword;

    private static final String CREDENTIALS_FILE = "StudentInformationApplication/src/credentials.txt";
    private static final String CREDENTIALS_FILE_ALT = "src/credentials.txt";

    public LoginPanel() {
        loadCredentials();
        setupUI();
    }

    private void loadCredentials() {
        // Try primary path first, then alternative
        String filePath = new java.io.File(CREDENTIALS_FILE).exists() ? CREDENTIALS_FILE : CREDENTIALS_FILE_ALT;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            storedUsername = reader.readLine();
            storedPassword = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Error", "Error reading credentials file: " + e.getMessage() + 
                    "\nPath: " + new java.io.File(CREDENTIALS_FILE).getAbsolutePath());
            System.exit(1);
        }
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Student Info Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        
        // Login button
        JButton loginButton = new JButton("Login");

        setComponentStyles(loginButton, usernameLabel, passwordLabel);

        addLabelAndField(this, gbc, usernameLabel, usernameField, 1);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(loginButton, gbc);

        // Action listener for login
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (storedUsername.equals(username) && storedPassword.equals(password)) {
                // Use Singleton to track session
                SessionManager.getInstance().login(username);

                Container parent = getParent();
                if (parent instanceof JPanel) {
                    JPanel basePanel = (JPanel) parent;
                    CardLayout cardLayout = (CardLayout) basePanel.getLayout();
                    cardLayout.show(basePanel, "Main");
                }
            } else {
                showErrorDialog("Error", "Invalid username or password!");
            }
        });

        // Allow Enter key to trigger login
        passwordField.addActionListener(e -> loginButton.doClick());
    }
}
