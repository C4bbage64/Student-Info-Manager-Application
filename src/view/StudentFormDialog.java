package view;

import facade.StudentManagementFacade;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import model.Student;
import util.StudentIdGenerator;
import exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Modal dialog for adding or editing students.
 * Used by StudentManagementPanel for both Add and Edit operations.
 */
public class StudentFormDialog extends JDialog {
    public enum Mode { ADD, EDIT }
    
    private Mode mode;
    private Student student;        // null for ADD, populated for EDIT
    private boolean confirmed = false;
    private Student resultStudent;   // Student object created/updated
    
    // Form fields
    private JLabel studentIdLabel;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField courseField;
    private JTextField emailField;
    
    // Buttons
    private JButton saveButton;
    private JButton cancelButton;
    
    private StudentManagementFacade facade;
    
    /**
     * Creates a new StudentFormDialog.
     * 
     * @param parent Parent frame for modal behavior
     * @param mode ADD or EDIT mode
     * @param student Student to edit (null for ADD mode)
     */
    public StudentFormDialog(JFrame parent, Mode mode, Student student) {
        super(parent, true); // Modal dialog
        this.mode = mode;
        this.student = student;
        this.facade = StudentManagementFacade.getInstance();
        
        setTitle(mode == Mode.ADD ? "Add New Student" : "Edit Student");
        setupUI();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void setupUI() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Student ID (display-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        studentIdLabel = new JLabel(mode == Mode.ADD ? "(Auto-generated)" : student.getStudentId());
        studentIdLabel.setFont(studentIdLabel.getFont().deriveFont(Font.BOLD));
        formPanel.add(studentIdLabel, gbc);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(25);
        if (mode == Mode.EDIT && student != null) {
            nameField.setText(student.getName());
        }
        formPanel.add(nameField, gbc);
        
        // Age field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(25);
        if (mode == Mode.EDIT && student != null) {
            ageField.setText(String.valueOf(student.getAge()));
        }
        formPanel.add(ageField, gbc);
        
        // Course field
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        courseField = new JTextField(25);
        if (mode == Mode.EDIT && student != null) {
            courseField.setText(student.getCourse());
        }
        formPanel.add(courseField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(25);
        if (mode == Mode.EDIT && student != null) {
            emailField.setText(student.getEmail() != null ? student.getEmail() : "");
        }
        formPanel.add(emailField, gbc);
        
        contentPane.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton(mode == Mode.ADD ? "Add Student" : "Save Changes");
        cancelButton = new JButton("Cancel");
        
        // Style buttons
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        // Action listeners
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        // Enter key support
        getRootPane().setDefaultButton(saveButton);
    }
    
    private void handleSave() {
        try {
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String course = courseField.getText().trim();
            String email = emailField.getText().trim();
            
            // Chain of Responsibility: Validate inputs
            ValidationHandler validator = ValidationChainBuilder.buildStudentValidationChain();
            validator.validate("name", name);
            validator.validate("age", ageStr);
            validator.validate("course", course);
            validator.validate("email", email);
            
            int age = Integer.parseInt(ageStr);
            String studentId;
            
            if (mode == Mode.ADD) {
                // Generate new ID
                studentId = StudentIdGenerator.generateNextId();
                facade.addStudent(studentId, name, age, course, email);
                // Create result student object
                resultStudent = new Student(name, age, studentId, course, email, "ENROLLED");
            } else {
                // Use existing ID
                studentId = student.getStudentId();
                facade.updateStudent(studentId, name, age, course, email);
                // Create result student object with updated data
                resultStudent = new Student(name, age, studentId, course, email, student.getEnrollmentStatus());
            }
            
            confirmed = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid age input!", "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
        } catch (DuplicateStudentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (StudentNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                    "Database Error: " + ex.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows the dialog and returns whether the user confirmed the action.
     * 
     * @return true if user clicked Save/Add, false if cancelled
     */
    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }
    
    /**
     * Gets the Student object created/updated (only valid if confirmed is true).
     * 
     * @return Student object, or null if not confirmed
     */
    public Student getResultStudent() {
        return confirmed ? resultStudent : null;
    }
}

