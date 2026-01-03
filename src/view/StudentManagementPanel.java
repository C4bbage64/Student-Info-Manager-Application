package view;

import facade.StudentManagementFacade;
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
 * Unified Student Management Panel consolidating Add, Edit, Delete, and View operations.
 * Part of MVC architecture - View layer.
 * Uses Facade pattern to interact with controllers.
 * Implements Observer pattern to auto-refresh when data changes.
 * Uses Strategy pattern for sorting students.
 */
public class StudentManagementPanel extends BasePanel implements StudentDataObserver {
    
    private StudentManagementFacade facade;
    private StudentSortContext sortContext;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    
    // Toolbar buttons
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    // Search/filter
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JLabel countLabel;
    
    public StudentManagementPanel() {
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
        SwingUtilities.invokeLater(() -> {
            try {
                refreshStudentTable();
            } catch (SQLException e) {
                // Silently handle - table will refresh on next manual refresh
            }
        });
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel - Search and Sort
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);
        
        topPanel.add(new JLabel("Sort by:"));
        String[] criteria = {"student_id", "name", "age", "course"};
        sortComboBox = new JComboBox<>(criteria);
        topPanel.add(sortComboBox);
        
        refreshButton = new JButton("Refresh");
        setComponentStyles(refreshButton);
        topPanel.add(refreshButton);
        
        // Table
        String[] columns = {"Student ID", "Name", "Age", "Course", "Email", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(25);
        
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
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Bottom panel - Action buttons and count
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add New");
        editButton = new JButton("Edit Selected");
        deleteButton = new JButton("Delete Selected");
        
        setComponentStyles(addButton, editButton, deleteButton);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Count label
        countLabel = new JLabel("Total Students: 0");
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        countPanel.add(countLabel);
        
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(countPanel, BorderLayout.EAST);
        
        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Action listeners
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> {
            try {
                refreshStudentTable();
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to refresh: " + ex.getMessage());
            }
        });
        
        // Search functionality
        searchField.addActionListener(e -> handleSearch());
        JButton searchButton = new JButton("Search");
        setComponentStyles(searchButton);
        topPanel.add(searchButton);
        searchButton.addActionListener(e -> handleSearch());
        
        // Initial load
        try {
            refreshStudentTable();
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load students: " + ex.getMessage());
        }
    }
    
    private void handleAdd() {
        StudentFormDialog dialog = new StudentFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            StudentFormDialog.Mode.ADD,
            null
        );
        
        if (dialog.showDialog()) {
            showMessageDialog("Success", "Student added successfully!");
            try {
                refreshStudentTable();
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to refresh table: " + ex.getMessage());
            }
        }
    }
    
    private void handleEdit() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog("Selection Required", "Please select a student to edit.");
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        
        try {
            Student student = facade.getStudent(studentId);
            
            StudentFormDialog dialog = new StudentFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                StudentFormDialog.Mode.EDIT,
                student
            );
            
            if (dialog.showDialog()) {
                showMessageDialog("Success", "Student updated successfully!");
                try {
                    refreshStudentTable();
                } catch (SQLException ex) {
                    showErrorDialog("Database Error", "Failed to refresh table: " + ex.getMessage());
                }
            }
            
        } catch (InvalidInputException ex) {
            showWarningDialog("Validation Error", ex.getMessage());
        } catch (StudentNotFoundException ex) {
            showErrorDialog("Error", ex.getMessage());
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load student: " + ex.getMessage());
        }
    }
    
    private void handleDelete() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog("Selection Required", "Please select a student to delete.");
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete student:\n" +
            "ID: " + studentId + "\n" +
            "Name: " + studentName + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                facade.deleteStudent(studentId);
                showMessageDialog("Success", "Student deleted successfully!");
                refreshStudentTable();
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (StudentNotFoundException ex) {
                showErrorDialog("Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to delete student: " + ex.getMessage());
            }
        }
    }
    
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            try {
                refreshStudentTable();
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to refresh: " + ex.getMessage());
            }
            return;
        }
        
        try {
            List<Student> students = facade.searchStudentsByName(searchTerm);
            
            // Apply sorting
            String selectedCriteria = (String) sortComboBox.getSelectedItem();
            SortStrategy strategy = StudentSortContext.getStrategyByName(selectedCriteria);
            sortContext.setStrategy(strategy);
            students = sortContext.sortStudents(students);
            
            // Update table
            tableModel.setRowCount(0);
            for (Student student : students) {
                tableModel.addRow(new Object[]{
                    student.getStudentId(),
                    student.getName(),
                    student.getAge(),
                    student.getCourse(),
                    student.getEmail() != null ? student.getEmail() : "",
                    student.getEnrollmentStatus()
                });
            }
            countLabel.setText("Found: " + students.size() + " student(s)");
            
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to search students: " + ex.getMessage());
        }
    }
    
    /**
     * Refreshes the student table with all students, applying current sort.
     */
    private void refreshStudentTable() throws SQLException {
        String selectedCriteria = (String) sortComboBox.getSelectedItem();
        
        // Strategy Pattern: Use strategy to sort students
        List<Student> students = facade.getAllStudents();
        SortStrategy strategy = StudentSortContext.getStrategyByName(selectedCriteria);
        sortContext.setStrategy(strategy);
        students = sortContext.sortStudents(students);
        
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getStudentId(),
                student.getName(),
                student.getAge(),
                student.getCourse(),
                student.getEmail() != null ? student.getEmail() : "",
                student.getEnrollmentStatus()
            });
        }
        countLabel.setText("Total Students: " + students.size());
    }
}

