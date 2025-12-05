package view;

import controller.StudentController;
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
 */
public class MainPanel extends BasePanel {

    private StudentController studentController;

    public MainPanel() {
        this.studentController = new StudentController();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Creating a tabbed pane to hold different panels
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Student Management tabs
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
                String studentId = studentIdField.getText().trim();
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String course = courseField.getText().trim();
                String email = emailField.getText().trim();

                studentController.addStudent(studentId, name, age, course, email);
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
                Student student = studentController.getStudent(studentId);
                
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
                int age = Integer.parseInt(ageField.getText().trim());
                String course = courseField.getText().trim();
                String email = emailField.getText().trim();

                studentController.updateStudent(studentId, name, age, course, email);
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
                
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete student: " + studentId + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    studentController.deleteStudent(studentId);
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
        String[] columns = {"Student ID", "Name", "Age", "Course", "Email"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable studentTable = new JTable(tableModel);
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
                List<Student> students = studentController.getStudentsSorted(selectedCriteria);
                
                tableModel.setRowCount(0);
                for (Student student : students) {
                    tableModel.addRow(new Object[]{
                        student.getStudentId(),
                        student.getName(),
                        student.getAge(),
                        student.getCourse(),
                        student.getEmail() != null ? student.getEmail() : ""
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

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        setComponentStyles(searchButton, studentIdLabel);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(searchButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        panel.add(new JScrollPane(resultArea), gbc);

        searchButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                
                if (studentId.isEmpty()) {
                    showWarningDialog("Validation Error", "Student ID cannot be empty");
                    return;
                }
                
                Student student = studentController.searchStudent(studentId);
                
                if (student != null) {
                    resultArea.setText(student.toString());
                } else {
                    resultArea.setText("Student not found.");
                }
                
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to search: " + ex.getMessage());
            }
        });

        return panel;
    }
}
