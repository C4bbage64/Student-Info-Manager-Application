package view;

import javax.swing.*;
import java.awt.*;

/**
 * Main panel containing all student management functionality.
 * Part of MVC architecture - View layer.
 * Uses Facade pattern to interact with controllers.
 */
public class MainPanel extends BasePanel {

    public MainPanel() {
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

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
    
}
