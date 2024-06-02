import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class StudentInfoApp {
    private StudentManager studentManager;

    public StudentInfoApp() {
        studentManager = new StudentManager();
        initializeUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentInfoApp());
    }

    // Initialize the UI with JFrame and JTabbedPane
    private void initializeUI() {
        // Set Nimbus Look and Feel for a modern look
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Student Info Manager Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Add Student", createAddStudentPane());
        tabbedPane.addTab("Edit Student", createEditStudentPane());
        tabbedPane.addTab("Delete Student", createDeleteStudentPane());
        tabbedPane.addTab("View Students", createViewStudentsPane());

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Create panel for adding students
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
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField studentIdField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JButton addButton = new JButton("Add Student");

        // Set colors and fonts
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);

        // Add components to the panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(ageLabel, gbc);
        gbc.gridx = 1; panel.add(ageField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(courseLabel, gbc);
        gbc.gridx = 1; panel.add(courseField, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(addButton, gbc);

        // Action listener for the add button
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String studentId = studentIdField.getText();
                String course = courseField.getText();

                if (name.isEmpty() || studentId.isEmpty() || course.isEmpty()) {
                    throw new InvalidInputException("All fields must be filled!");
                }

                Student student = new Student(name, age, studentId, course);
                studentManager.addStudent(student);
                showMessageDialog("Success", "Student added successfully!");
                nameField.setText("");
                ageField.setText("");
                studentIdField.setText("");
                courseField.setText("");
            } catch (NumberFormatException ex) {
                showMessageDialog("Error", "Invalid age input!");
            } catch (InvalidInputException ex) {
                showMessageDialog("Error", ex.getMessage());
            }
        });

        return panel;
    }

    // Create panel for editing students
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
        JTextField studentIdField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JButton editButton = new JButton("Edit Student");

        // Set colors and fonts
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBackground(new Color(70, 130, 180));
        editButton.setForeground(Color.WHITE);

        // Add components to the panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(nameLabel, gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(ageLabel, gbc);
        gbc.gridx = 1; panel.add(ageField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(courseLabel, gbc);
        gbc.gridx = 1; panel.add(courseField, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(editButton, gbc);

        // Action listener for the edit button
        editButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText();
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String course = courseField.getText();

                if (name.isEmpty() || studentId.isEmpty() || course.isEmpty()) {
                    throw new InvalidInputException("All fields must be filled!");
                }

                Student updatedStudent = new Student(name, age, studentId, course);
                studentManager.editStudent(studentId, updatedStudent);
                showMessageDialog("Success", "Student edited successfully!");
                studentIdField.setText("");
                nameField.setText("");
                ageField.setText("");
                courseField.setText("");
            } catch (NumberFormatException ex) {
                showMessageDialog("Error", "Invalid age input!");
            } catch (InvalidInputException ex) {
                showMessageDialog("Error", ex.getMessage());
            }
        });

        return panel;
    }

    // Create panel for deleting students
    private JPanel createDeleteStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JButton deleteButton = new JButton("Delete Student");

        // Set colors and fonts
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(70, 130, 180));
        deleteButton.setForeground(Color.WHITE);

        // Add components to the panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(deleteButton, gbc);

        // Action listener for the delete button
        deleteButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText();
                if (studentId.isEmpty()) {
                    throw new InvalidInputException("Student ID must be provided!");
                }

                studentManager.deleteStudent(studentId);
                showMessageDialog("Success", "Student deleted successfully!");
                studentIdField.setText("");
            } catch (InvalidInputException ex) {
                showMessageDialog("Error", ex.getMessage());
            }
        });

        return panel;
    }

    // Create panel for viewing all students
    private JPanel createViewStudentsPane() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        JButton refreshButton = new JButton("Refresh");

        // Set colors and fonts
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);

        // Action listener for the refresh button
        refreshButton.addActionListener(e -> {
            List<Student> students = studentManager.getAllStudents();
            StringBuilder sb = new StringBuilder();
            for (Student student : students) {
                sb.append(student.getName()).append(", ").append(student.getAge()).append(", ")
                        .append(student.getStudentId()).append(", ").append(student.getCourse()).append("\n");
            }
            textArea.setText(sb.toString());
        });

        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    // Show message dialog for displaying information to the user
    private void showMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
