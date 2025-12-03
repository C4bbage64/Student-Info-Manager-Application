import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentInformationApp {
    private StudentManager studentManager;

    public StudentInformationApp() {
        studentManager = new StudentManager();
        initializeUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentInformationApp());
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Student Information System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Add Student", createAddStudentPane());
        tabbedPane.addTab("Edit Student", createEditStudentPane());
        tabbedPane.addTab("Delete Student", createDeleteStudentPane());
        tabbedPane.addTab("View Students", createViewStudentsPane());

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createAddStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField studentIdField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JButton addButton = new JButton("Add Student");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; panel.add(ageField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1; panel.add(courseField, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(addButton, gbc);

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

    private JPanel createEditStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField studentIdField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JButton editButton = new JButton("Edit Student");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; panel.add(ageField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1; panel.add(courseField, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(editButton, gbc);

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

    private JPanel createDeleteStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField studentIdField = new JTextField(20);
        JButton deleteButton = new JButton("Delete Student");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(deleteButton, gbc);

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

    private JPanel createViewStudentsPane() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> {
            List<Student> students = studentManager.getAllStudents();
            StringBuilder sb = new StringBuilder();
            for (Student student : students) {
                sb.append(student.getName()).append(", ").append(student.getAge()).append(", ").append(student.getStudentId()).append(", ").append(student.getCourse()).append("\n");
            }
            textArea.setText(sb.toString());
        });

        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private void showMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
