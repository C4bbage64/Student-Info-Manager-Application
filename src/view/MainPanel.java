package view;

import facade.StudentManagementFacade;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import state.ApplicationStateContext;
import observer.StudentDataObserver;
import observer.StudentDataManager;
import strategy.StudentSortContext;
import strategy.SortStrategy;
import model.Student;
import exceptions.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Main panel containing all student management functionality.
 * Part of MVC architecture - View layer.
 * Uses Facade pattern to interact with controllers.
 * Implements Observer pattern to auto-refresh when data changes.
 * Uses Strategy pattern for sorting students.
 */
public class MainPanel extends BasePanel implements StudentDataObserver {

    private StudentManagementFacade facade;
    private StudentSortContext sortContext;
    private DefaultTableModel studentTableModel;
    private JTable studentTable;

    public MainPanel() {
        this.facade = StudentManagementFacade.getInstance();
        this.sortContext = new StudentSortContext();
        setupUI();
        
        // Observer Pattern: Register this panel as an observer
        StudentDataManager.getInstance().addObserver(this);
    }
    
    /**
     * Observer Pattern: Called when student data changes.
     * Auto-refreshes the student table.
     */
    @Override
    public void onStudentDataChanged(String eventType) {
        // Refresh the student table when data changes
        if (studentTableModel != null) {
            SwingUtilities.invokeLater(() -> {
                try {
                    refreshStudentTable();
                } catch (SQLException e) {
                    // Silently handle - table will refresh on next manual refresh
                }
            });
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Creating a tabbed pane to hold different panels
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Student Management tabs
        tabbedPane.addTab("Dashboard", new StudentDashboardPanel());
        tabbedPane.addTab("Add Student", createAddStudentPane());
        tabbedPane.addTab("Edit Student", createEditStudentPane());
        tabbedPane.addTab("Delete Student", createDeleteStudentPane());
        tabbedPane.addTab("View Students", createViewStudentsPane());
        tabbedPane.addTab("Search Student", createSearchStudentPane());
        
        // Attendance tab (embedded panel)
        tabbedPane.addTab("Attendance", new AttendancePanel());
        
        // Finance tab (embedded panel)
        tabbedPane.addTab("Finance", new FinancePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAddStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel emailLabel = new JLabel("Email:");
        
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField studentIdField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JButton addButton = new JButton("Add Student");

        setComponentStyles(addButton, nameLabel, ageLabel, studentIdLabel, courseLabel, emailLabel);

        addLabelAndField(panel, gbc, studentIdLabel, studentIdField, 0);
        addLabelAndField(panel, gbc, nameLabel, nameField, 1);
        addLabelAndField(panel, gbc, ageLabel, ageField, 2);
        addLabelAndField(panel, gbc, courseLabel, courseField, 3);
        addLabelAndField(panel, gbc, emailLabel, emailField, 4);
        
        gbc.gridx = 1; gbc.gridy = 5; 
        panel.add(addButton, gbc);

        addButton.addActionListener(e -> {
            try {
                // State Pattern: Check if logged in
                ApplicationStateContext stateContext = ApplicationStateContext.getInstance();
                if (!stateContext.isLoggedIn()) {
                    showErrorDialog("Access Denied", "Please login first to perform student operations.");
                    return;
                }
                stateContext.handleStudentOperation();

                String studentId = studentIdField.getText().trim();
                String name = nameField.getText().trim();
                String ageStr = ageField.getText().trim();
                String course = courseField.getText().trim();
                String email = emailField.getText().trim();

                // Chain of Responsibility: Validate inputs
                ValidationHandler validator = ValidationChainBuilder.buildStudentValidationChain();
                validator.validate("studentId", studentId);
                validator.validate("name", name);
                validator.validate("age", ageStr);
                validator.validate("course", course);
                validator.validate("email", email);

                int age = Integer.parseInt(ageStr);
                facade.addStudent(studentId, name, age, course, email);
                showMessageDialog("Success", "Student added successfully!");
                
                // Clear fields
                studentIdField.setText("");
                nameField.setText("");
                ageField.setText("");
                courseField.setText("");
                emailField.setText("");
                
            } catch (NumberFormatException ex) {
                showWarningDialog("Validation Error", "Invalid age input!");
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (DuplicateStudentException ex) {
                showErrorDialog("Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to add student: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createEditStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel emailLabel = new JLabel("Email:");
        
        JTextField studentIdField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        
        JButton loadButton = new JButton("Load Student");
        JButton editButton = new JButton("Save Changes");

        setComponentStyles(loadButton, editButton, studentIdLabel, nameLabel, ageLabel, courseLabel, emailLabel);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 2; panel.add(loadButton, gbc);
        
        addLabelAndField(panel, gbc, nameLabel, nameField, 1);
        addLabelAndField(panel, gbc, ageLabel, ageField, 2);
        addLabelAndField(panel, gbc, courseLabel, courseField, 3);
        addLabelAndField(panel, gbc, emailLabel, emailField, 4);
        
        gbc.gridx = 1; gbc.gridy = 5; 
        panel.add(editButton, gbc);

        // Load student data
        loadButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                Student student = facade.getStudent(studentId);
                
                nameField.setText(student.getName());
                ageField.setText(String.valueOf(student.getAge()));
                courseField.setText(student.getCourse());
                emailField.setText(student.getEmail() != null ? student.getEmail() : "");
                
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (StudentNotFoundException ex) {
                showErrorDialog("Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to load student: " + ex.getMessage());
            }
        });

        // Save changes
        editButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                String name = nameField.getText().trim();
                String ageStr = ageField.getText().trim();
                String course = courseField.getText().trim();
                String email = emailField.getText().trim();

                // Chain of Responsibility: Validate inputs
                ValidationHandler validator = ValidationChainBuilder.buildStudentValidationChain();
                validator.validate("studentId", studentId);
                validator.validate("name", name);
                validator.validate("age", ageStr);
                validator.validate("course", course);
                validator.validate("email", email);

                int age = Integer.parseInt(ageStr);
                facade.updateStudent(studentId, name, age, course, email);
                showMessageDialog("Success", "Student updated successfully!");
                
            } catch (NumberFormatException ex) {
                showWarningDialog("Validation Error", "Invalid age input!");
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (StudentNotFoundException ex) {
                showErrorDialog("Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to update student: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createDeleteStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JButton deleteButton = new JButton("Delete Student");

        setComponentStyles(deleteButton, studentIdLabel);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(deleteButton, gbc);

        deleteButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                
                // Chain of Responsibility: Validate input
                ValidationHandler validator = ValidationChainBuilder.buildGeneralValidationChain();
                validator.validate("studentId", studentId);
                
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete student: " + studentId + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    facade.deleteStudent(studentId);
                    showMessageDialog("Success", "Student deleted successfully!");
                    studentIdField.setText("");
                }
                
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (StudentNotFoundException ex) {
                showErrorDialog("Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to delete student: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createViewStudentsPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for sorting
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sortByLabel = new JLabel("Sort by:");
        String[] criteria = {"student_id", "name", "age", "course"};
        JComboBox<String> criteriaComboBox = new JComboBox<>(criteria);
        JButton sortButton = new JButton("Sort");
        JButton refreshButton = new JButton("Refresh");

        setComponentStyles(sortButton, refreshButton, sortByLabel);

        sortPanel.add(sortByLabel);
        sortPanel.add(criteriaComboBox);
        sortPanel.add(sortButton);
        sortPanel.add(refreshButton);

        // Table
        String[] columns = {"Student ID", "Name", "Age", "Course", "Email", "Status"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(studentTableModel);
        
        // Color-code rows based on enrollment status
        studentTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    // Get status from the last column (index 5)
                    String status = (String) table.getValueAt(row, 5);
                    if (status != null) {
                        switch (status) {
                            case "ENROLLED":
                                c.setBackground(new Color(220, 255, 220)); // Light green
                                break;
                            case "SUSPENDED":
                                c.setBackground(new Color(255, 255, 200)); // Light yellow
                                break;
                            case "GRADUATED":
                                c.setBackground(new Color(240, 240, 240)); // Light gray
                                break;
                            default:
                                c.setBackground(Color.WHITE);
                        }
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Count label
        JLabel countLabel = new JLabel("Total Students: 0");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(countLabel);

        panel.add(sortPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        Runnable loadStudents = () -> {
            try {
                String selectedCriteria = (String) criteriaComboBox.getSelectedItem();
                
                // Strategy Pattern: Use strategy to sort students
                List<Student> students = facade.getAllStudents();
                SortStrategy strategy = StudentSortContext.getStrategyByName(selectedCriteria);
                sortContext.setStrategy(strategy);
                students = sortContext.sortStudents(students);
                
                studentTableModel.setRowCount(0);
                for (Student student : students) {
                    studentTableModel.addRow(new Object[]{
                        student.getStudentId(),
                        student.getName(),
                        student.getAge(),
                        student.getCourse(),
                        student.getEmail() != null ? student.getEmail() : "",
                        student.getEnrollmentStatus()
                    });
                }
                countLabel.setText("Total Students: " + students.size());
                
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to load students: " + ex.getMessage());
            }
        };

        sortButton.addActionListener(e -> loadStudents.run());
        refreshButton.addActionListener(e -> loadStudents.run());

        return panel;
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
    
    /**
     * Helper method to refresh student table.
     * Used by Observer pattern to auto-refresh when data changes.
     */
    private void refreshStudentTable() throws SQLException {
        if (studentTableModel == null) {
            return;
        }
        
        // Get all students and sort using current strategy
        List<Student> students = facade.getAllStudents();
        students = sortContext.sortStudents(students);
        
        studentTableModel.setRowCount(0);
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                student.getStudentId(),
                student.getName(),
                student.getAge(),
                student.getCourse(),
                student.getEmail() != null ? student.getEmail() : "",
                student.getEnrollmentStatus()
            });
        }
    }
}
