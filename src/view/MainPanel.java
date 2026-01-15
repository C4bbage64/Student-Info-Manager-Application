package view;

import util.SessionManager;
import state.ApplicationStateContext;
import state.LoginState;

import javax.swing.*;
import java.awt.*;

/**
 * Main panel containing all student management functionality.
 * Part of MVC architecture - View layer.
 * Uses Facade pattern to interact with controllers.
 */
public class MainPanel extends BasePanel {

    private JLabel welcomeLabel;

    public MainPanel() {
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Header panel with welcome message and logout button
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Creating a tabbed pane to hold different panels
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Student Management tabs
        tabbedPane.addTab("Dashboard", new StudentDashboardPanel());
        tabbedPane.addTab("Manage Students", new StudentManagementPanel());
        
        // Attendance tab (embedded panel)
        tabbedPane.addTab("Attendance", new AttendancePanel());
        
        // Finance tab (embedded panel)
        tabbedPane.addTab("Finance", new FinancePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        headerPanel.setBackground(new Color(245, 245, 245));

        // Welcome label on the left
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // Logout button on the right
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> performLogout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Clear session
            SessionManager.getInstance().logout();

            // Update state pattern to LoginState
            ApplicationStateContext.getInstance().setState(new LoginState());

            // Switch to login panel
            Container parent = getParent();
            if (parent instanceof JPanel) {
                JPanel basePanel = (JPanel) parent;
                CardLayout cardLayout = (CardLayout) basePanel.getLayout();
                
                // Clear login fields if LoginPanel is accessible
                for (Component comp : basePanel.getComponents()) {
                    if (comp instanceof LoginPanel) {
                        ((LoginPanel) comp).clearFields();
                    }
                }
                
                cardLayout.show(basePanel, "Login");
            }
        }
    }

    /**
     * Refreshes the welcome label with the current user.
     * Call this method when navigating to the main panel after login.
     */
    public void refreshWelcomeLabel() {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + SessionManager.getInstance().getCurrentUser());
        }
    }
    
}
