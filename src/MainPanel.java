import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

// MainPanel class which extends BasePanel and includes various functionalities for managing students
public class MainPanel extends BasePanel {

    private StudentManagerInterface studentManager;

    // Constructor initializing the StudentManager and setting up the UI
    public MainPanel(StudentManager studentManager) {
        this.studentManager = studentManager;
        setupUI();
    }

    // Method to set up the UI components
    private void setupUI() {
        setLayout(new BorderLayout());

        // Creating a tabbed pane to hold different panels
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Student", createAddStudentPane());
        tabbedPane.addTab("Edit Student", createEditStudentPane());
        tabbedPane.addTab("Delete Student", createDeleteStudentPane());
        tabbedPane.addTab("Sort & View Students", createSortAndViewStudentsPane());
        tabbedPane.addTab("Search Student", createSearchStudentPane());
        tabbedPane.addTab("Export Data", createExportDataPane());
        tabbedPane.addTab("Import Data", createImportDataPane());

        // Adding the tabbed pane to the center of the layout
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Method to create the panel for adding students
    private JPanel createAddStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Defining labels and text fields for student attributes
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel courseLabel = new JLabel("Course:");
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField studentIdField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JButton addButton = new JButton("Add Student");

        // Applying styles to the components
        setComponentStyles(addButton, nameLabel, ageLabel, studentIdLabel, courseLabel);

        // Adding components to the panel
        addLabelAndField(panel, gbc, nameLabel, nameField, 0);
        addLabelAndField(panel, gbc, ageLabel, ageField, 1);
        addLabelAndField(panel, gbc, studentIdLabel, studentIdField, 2);
        addLabelAndField(panel, gbc, courseLabel, courseField, 3);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(addButton, gbc);

        // Adding action listener to the add button to handle adding a new student
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

    // Method to create the panel for editing student information
    private JPanel createEditStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Defining labels and text fields for student attributes
        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel courseLabel = new JLabel("Course:");
        JTextField studentIdField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JButton editButton = new JButton("Edit Student");

        // Applying styles to the components
        setComponentStyles(editButton, studentIdLabel, nameLabel, ageLabel, courseLabel);

        // Adding components to the panel
        addLabelAndField(panel, gbc, studentIdLabel, studentIdField, 0);
        addLabelAndField(panel, gbc, nameLabel, nameField, 1);
        addLabelAndField(panel, gbc, ageLabel, ageField, 2);
        addLabelAndField(panel, gbc, courseLabel, courseField, 3);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(editButton, gbc);

        // Adding action listener to the edit button to handle editing a student's information
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

    // Method to create the panel for deleting a student
    private JPanel createDeleteStudentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Defining labels and text fields for student attributes
        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JButton deleteButton = new JButton("Delete Student");

        // Applying styles to the components
        setComponentStyles(deleteButton, studentIdLabel);

        // Adding components to the panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(deleteButton, gbc);

        // Adding action listener to the delete button to handle deleting a student
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

    /**
     * Creates a panel for sorting and viewing students. This panel includes a combo box
     * for selecting the sorting criteria, a button to perform the sort, and a text area
     * to display the sorted list of students. It also includes a refresh button to display
     * the current list of students without sorting.
     *
     * @return A JPanel containing the sort and view students functionality.
     */
    private JPanel createSortAndViewStudentsPane() {
        // Create a new JPanel with a GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        // Set an empty border around the panel with 10-pixel padding on all sides
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a new GridBagConstraints object to manage the layout of components
        GridBagConstraints gbc = new GridBagConstraints();
        // Set the padding (insets) around components to 5 pixels on all sides
        gbc.insets = new Insets(5, 5, 5, 5);
        // Set the fill behavior to horizontal, so components will expand to fill their horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a new label for the sort criteria
        JLabel sortByLabel = new JLabel("Sort by:");
        // Define the sorting criteria options
        String[] criteria = {"name", "age", "studentId", "course"};
        // Create a combo box with the sorting criteria options
        JComboBox<String> criteriaComboBox = new JComboBox<>(criteria);
        // Create a new button labeled "Sort"
        JButton sortButton = new JButton("Sort");
        // Create a text area for displaying sorted student data with 20 rows and 40 columns
        JTextArea resultArea = new JTextArea(20, 40);
        // Set the text area to be non-editable
        resultArea.setEditable(false);
        // Wrap the result area in a JScrollPane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Apply custom styles to the sort button and sort by label using a helper method
        setComponentStyles(sortButton, sortByLabel);

        // Set the grid position to the first row and first column and add the sort by label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(sortByLabel, gbc);
        // Set the grid position to the first row and second column and add the criteria combo box
        gbc.gridx = 1;
        panel.add(criteriaComboBox, gbc);
        // Set the grid position to the second row and second column and add the sort button
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(sortButton, gbc);

        // Set the grid position to the third row spanning two columns and add the scroll pane
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Ensure the scroll pane fills the available space
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        // Add an action listener to the sort button to handle click events
        sortButton.addActionListener(e -> {
            // Get the selected sorting criteria from the combo box
            String selectedCriteria = (String) criteriaComboBox.getSelectedItem();
            // Sort the students using the studentManager's sortStudents method
            java.util.List<Student> sortedStudents = studentManager.sortStudents(selectedCriteria);
            // Build a string with the sorted student data
            StringBuilder sb = new StringBuilder();
            for (Student student : sortedStudents) {
                sb.append(student.toString()).append("\n");
            }
            // Set the result area's text to the sorted student data
            resultArea.setText(sb.toString());
        });

        // Create a new button labeled "Refresh"
        JButton refreshButton = new JButton("Refresh");
        // Set the grid position to the fourth row and first column and add the refresh button
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panel.add(refreshButton, gbc);

        // Add an action listener to the refresh button to handle click events
        refreshButton.addActionListener(e -> {
            // Get the current list of all students using the studentManager's getAllStudents method
            List<Student> students = studentManager.getAllStudents();
            // Build a string with the current student data
            StringBuilder sb = new StringBuilder();
            for (Student student : students) {
                sb.append(student.toString()).append("\n");
            }
            // Set the result area's text to the current student data
            resultArea.setText(sb.toString());
        });

        // Return the completed panel
        return panel;
    }

    /**
     * Creates a panel for searching student data by student ID. This panel includes
     * a label and text field for entering a student ID, a button to initiate the search,
     * and a text area to display the search results.
     *
     * @return A JPanel containing the search student functionality.
     */
    private JPanel createSearchStudentPane() {
        // Create a new JPanel with a GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        // Set an empty border around the panel with 10-pixel padding on all sides
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a new GridBagConstraints object to manage the layout of components
        GridBagConstraints gbc = new GridBagConstraints();
        // Set the padding (insets) around components to 5 pixels on all sides
        gbc.insets = new Insets(5, 5, 5, 5);
        // Set the fill behavior to horizontal, so components will expand to fill their horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a new label for the student ID
        JLabel studentIdLabel = new JLabel("Student ID:");
        // Create a text field for entering the student ID with a width of 20 columns
        JTextField studentIdField = new JTextField(20);
        // Create a new button labeled "Search"
        JButton searchButton = new JButton("Search");
        // Create a text area for displaying search results with 5 rows and 20 columns
        JTextArea resultArea = new JTextArea(5, 20);
        // Set the text area to be non-editable
        resultArea.setEditable(false);

        // Apply custom styles to the search button and student ID label using a helper method
        setComponentStyles(searchButton, studentIdLabel);

        // Set the grid position to the first row and first column and add the student ID label
        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        // Set the grid position to the first row and second column and add the student ID field
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        // Set the grid position to the second row and second column and add the search button
        gbc.gridx = 1; gbc.gridy = 1; panel.add(searchButton, gbc);
        // Set the grid position to the third row spanning two columns and add the result area wrapped in a JScrollPane
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(new JScrollPane(resultArea), gbc);

        // Add an action listener to the search button to handle click events
        searchButton.addActionListener(e -> {
            // Get the student ID entered in the text field
            String studentId = studentIdField.getText();
            // Check if the student ID field is empty
            if (studentId.isEmpty()) {
                // Show an error message dialog if the student ID field is empty
                showMessageDialog("Error", "Student ID must be provided!");
                return;
            }

            // Search for the student using the studentManager's searchStudent method
            Student student = studentManager.searchStudent(studentId);
            // If the student is found, display the student's information in the result area
            if (student != null) {
                resultArea.setText(student.toString());
            } else {
                // If the student is not found, display "Student not found." in the result area
                resultArea.setText("Student not found.");
            }
        });

        // Return the completed panel
        return panel;
    }


    /**
     * Creates a panel for exporting student data. This panel includes a button
     * that allows the user to choose a file location to export student data to.
     *
     * @return A JPanel containing the export data functionality.
     */
    private JPanel createExportDataPane() {
        // Create a new JPanel with a GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        // Set an empty border around the panel with 10-pixel padding on all sides
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a new GridBagConstraints object to manage the layout of components
        GridBagConstraints gbc = new GridBagConstraints();
        // Set the padding (insets) around components to 5 pixels on all sides
        gbc.insets = new Insets(5, 5, 5, 5);
        // Set the fill behavior to horizontal, so components will expand to fill their horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a new button labeled "Export Data"
        JButton exportButton = new JButton("Export Data");

        // Apply custom styles to the export button using a helper method
        setComponentStyles(exportButton);

        // Set the grid position to the first row and first column
        gbc.gridx = 0;
        gbc.gridy = 0;
        // Add the export button to the panel using the specified GridBagConstraints
        panel.add(exportButton, gbc);

        // Add an action listener to the export button to handle click events
        exportButton.addActionListener(e -> {
            // Create a new JFileChooser instance to open a file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            // Set the dialog title to "Specify a file to save"
            fileChooser.setDialogTitle("Specify a file to save");

            // Show the save dialog and store the result (Approve or Cancel)
            int userSelection = fileChooser.showSaveDialog(null);
            // If the user approved the file selection
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Get the absolute path of the selected file
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    // Try to export the data to the specified file
                    studentManager.exportData(filename);
                    // Show a success message dialog if data export is successful
                    showMessageDialog("Success", "Data exported successfully!");
                } catch (IOException ex) {
                    // Show an error message dialog if an IOException occurs
                    showMessageDialog("Error", "Failed to export data: " + ex.getMessage());
                }
            }
        });

        // Return the completed panel
        return panel;
    }

    /**
     * Creates a panel for importing student data. This panel includes a button
     * that allows the user to choose a file to import student data from.
     *
     * @return A JPanel containing the import data functionality.
     */
    private JPanel createImportDataPane() {
        // Create a new JPanel with a GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        // Set an empty border around the panel with 10-pixel padding on all sides
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a new GridBagConstraints object to manage the layout of components
        GridBagConstraints gbc = new GridBagConstraints();
        // Set the padding (insets) around components to 5 pixels on all sides
        gbc.insets = new Insets(5, 5, 5, 5);
        // Set the fill behavior to horizontal, so components will expand to fill their horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a new button labeled "Import Data"
        JButton importButton = new JButton("Import Data");

        // Apply custom styles to the import button using a helper method
        setComponentStyles(importButton);

        // Set the grid position to the first row and first column
        gbc.gridx = 0;
        gbc.gridy = 0;
        // Add the import button to the panel using the specified GridBagConstraints
        panel.add(importButton, gbc);

        // Add an action listener to the import button to handle click events
        importButton.addActionListener(e -> {
            // Create a new JFileChooser instance to open a file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            // Show the open dialog and store the result (Approve or Cancel)
            int result = fileChooser.showOpenDialog(null);
            // If the user approved the file selection
            if (result == JFileChooser.APPROVE_OPTION) {
                // Get the absolute path of the selected file
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    // Try to import the data from the selected file
                    studentManager.importData(filename);
                    // Show a success message dialog if data import is successful
                    showMessageDialog("Success", "Data imported successfully!");
                } catch (IOException ex) {
                    // Show an error message dialog if an IOException occurs
                    showMessageDialog("Error", "Failed to import data: " + ex.getMessage());
                }
            }
        });

        // Return the completed panel
        return panel;
    }

    protected void showMessageDialog(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(MainPanel.this, message, title, JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
