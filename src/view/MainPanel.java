package view;

import facade.StudentManagementFacade;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import model.Student;
import exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Main panel containing all student management functionality.
 * Part of MVC architecture - View layer.
 * Uses Facade pattern to interact with controllers.
 */
public class MainPanel extends BasePanel {

    private StudentManagementFacade facade;

    public MainPanel() {
        this.facade = StudentManagementFacade.getInstance();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Creating a tabbed pane to hold different panels
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Student Management tabs
        tabbedPane.addTab("Dashboard", new StudentDashboardPanel());
        tabbedPane.addTab("Manage Students", new StudentManagementPanel());
        tabbedPane.addTab("Search Student", createSearchStudentPane());
        
        // Attendance tab (embedded panel)
        tabbedPane.addTab("Attendance", new AttendancePanel());
        
        // Finance tab (embedded panel)
        tabbedPane.addTab("Finance", new FinancePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }


    private JPanel createSearchStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search type selection
        JLabel searchTypeLabel = new JLabel("Search by:");
        ButtonGroup searchTypeGroup = new ButtonGroup();
        JRadioButton searchByIdRadio = new JRadioButton("Student ID", true);
        JRadioButton searchByNameRadio = new JRadioButton("Name");
        searchTypeGroup.add(searchByIdRadio);
        searchTypeGroup.add(searchByNameRadio);
        
        JPanel searchTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchTypePanel.add(searchTypeLabel);
        searchTypePanel.add(searchByIdRadio);
        searchTypePanel.add(searchByNameRadio);

        JLabel searchLabel = new JLabel("Search term:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        setComponentStyles(searchButton, searchLabel);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(searchTypePanel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(searchLabel, gbc);
        gbc.gridx = 1; panel.add(searchField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(searchButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        panel.add(new JScrollPane(resultArea), gbc);

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            
            if (searchTerm.isEmpty()) {
                showWarningDialog("Validation Error", "Please enter a search term");
                return;
            }
            
            try {
                // Chain of Responsibility: Validate input
                ValidationHandler validator = ValidationChainBuilder.buildGeneralValidationChain();
                validator.validate("studentId", searchTerm);
                
                if (searchByIdRadio.isSelected()) {
                    // Search by Student ID
                    try {
                        Student student = facade.searchStudent(searchTerm);
                        if (student != null) {
                            resultArea.setText(student.toString());
                        } else {
                            resultArea.setText("Student not found: " + searchTerm);
                        }
                    } catch (SQLException ex) {
                        showErrorDialog("Database Error", "Failed to search student: " + ex.getMessage());
                    }
                } else {
                    // Search by Name
                    try {
                        List<Student> students = facade.searchStudentsByName(searchTerm);
                        if (students.isEmpty()) {
                            resultArea.setText("No students found with name containing: " + searchTerm);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Found ").append(students.size()).append(" student(s):\n\n");
                            for (Student student : students) {
                                sb.append(student.toString()).append("\n---\n");
                            }
                            resultArea.setText(sb.toString());
                        }
                    } catch (SQLException ex) {
                        showErrorDialog("Database Error", "Failed to search students: " + ex.getMessage());
                    }
                }
                
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            }
        });
        
        // Enter key support
        searchField.addActionListener(e -> searchButton.doClick());

        return panel;
    }
    
}
