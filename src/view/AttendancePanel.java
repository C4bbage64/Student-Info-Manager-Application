package view;

import facade.StudentManagementFacade;
import observer.StudentDataObserver;
import observer.StudentDataManager;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import model.Attendance;
import model.Student;
import exceptions.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unified Attendance Panel consolidating Mark, View, and Rate operations.
 * Part of MVC architecture - View layer.
 * Uses Facade pattern to interact with controllers.
 * Implements Observer pattern to auto-refresh when attendance data changes.
 */
public class AttendancePanel extends BasePanel implements StudentDataObserver {

    private StudentManagementFacade facade;
    private DefaultTableModel tableModel;
    private JTable attendanceTable;

    // Cache for student names: StudentID -> Name
    private Map<String, String> studentNameMap;
    // Store all records for filtering
    private List<Attendance> allRecords;

    // Toolbar buttons
    private JButton markButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton clearButton;

    // Search/filter
    private JTextField searchField;
    private JTextField dateFilterField;
    private JLabel statsLabel;

    public AttendancePanel() {
        this.facade = StudentManagementFacade.getInstance();
        this.studentNameMap = new HashMap<>();
        setupUI();

        // Observer Pattern: Register this panel as an observer
        StudentDataManager.getInstance().addObserver(this);
    }

    /**
     * Observer Pattern: Called when data changes.
     */
    @Override
    public void onStudentDataChanged(String eventType) {
        // Refresh on any relevant change
        if ("ATTENDANCE".equals(eventType) || "STUDENT_UPDATE".equals(eventType)) {
            SwingUtilities.invokeLater(() -> {
                try {
                    refreshData();
                } catch (SQLException e) {
                    // Silently handle
                }
            });
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Search and Filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchField.setToolTipText("Search by Student ID, Name, or Status");
        topPanel.add(searchField);

        clearButton = new JButton("Clear");
        setComponentStyles(clearButton);
        topPanel.add(clearButton);

        topPanel.add(new JLabel("Filter Date:"));
        dateFilterField = new JTextField(10);
        dateFilterField.setToolTipText("YYYY-MM-DD");
        topPanel.add(dateFilterField);

        refreshButton = new JButton("Refresh");
        setComponentStyles(refreshButton);
        topPanel.add(refreshButton);

        // Table
        String[] columns = { "ID", "Student ID", "Student Name", "Date", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attendanceTable.setRowHeight(25);

        // Color coding for Status
        attendanceTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    String status = (String) table.getValueAt(row, 4); // Status column
                    if ("PRESENT".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(220, 255, 220)); // Light green
                    } else if ("ABSENT".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(255, 220, 220)); // Light red
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        // Bottom panel - Actions and Stats
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        markButton = new JButton("Mark Attendance");
        editButton = new JButton("Edit Selected");
        deleteButton = new JButton("Delete Selected");

        setComponentStyles(markButton, editButton, deleteButton);

        buttonPanel.add(markButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        statsLabel = new JLabel("Today: 0 Present, 0 Absent");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.add(statsLabel);

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(statsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        markButton.addActionListener(e -> handleMarkAttendance());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());

        refreshButton.addActionListener(e -> {
            try {
                refreshData();
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to refresh: " + ex.getMessage());
            }
        });

        clearButton.addActionListener(e -> {
            searchField.setText("");
            dateFilterField.setText("");
            try {
                refreshData();
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to refresh: " + ex.getMessage());
            }
        });

        // Real-time search/filter
        DocumentListener filterListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterRecords();
            }

            public void removeUpdate(DocumentEvent e) {
                filterRecords();
            }

            public void changedUpdate(DocumentEvent e) {
                filterRecords();
            }
        };
        searchField.getDocument().addDocumentListener(filterListener);
        dateFilterField.getDocument().addDocumentListener(filterListener);

        // Initial Load
        try {
            refreshData();
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load data: " + ex.getMessage());
        }
    }

    private void handleMarkAttendance() {
        try {
            List<Student> students = facade.getAllStudents();
            AttendanceFormDialog dialog = new AttendanceFormDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    students);

            if (dialog.showDialog()) {
                String studentId = dialog.getSelectedStudentId();
                String date = dialog.getDate();
                String status = dialog.getStatus();

                // Validate
                ValidationHandler validator = ValidationChainBuilder.buildAttendanceValidationChain();
                validator.validate("studentId", studentId);
                validator.validate("date", date);
                validator.validate("status", status);

                facade.markAttendance(studentId, date, status);
                showMessageDialog("Success", "Attendance marked!");
                refreshData();
            }
        } catch (Exception ex) {
            showErrorDialog("Error", ex.getMessage());
        }
    }

    private void handleEdit() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog("Selection Required", "Please select a record to edit.");
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

        // Simple dialog for status update since that's the main editable field
        String[] options = { "PRESENT", "ABSENT" };
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Update Status:",
                "Edit Attendance",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                currentStatus);

        if (newStatus != null) {
            try {
                facade.updateAttendance(id, newStatus);
                showMessageDialog("Success", "Attendance updated!");
                refreshData();
            } catch (Exception ex) {
                showErrorDialog("Error", ex.getMessage());
            }
        }
    }

    private void handleDelete() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog("Selection Required", "Please select a record to delete.");
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 2);
        String date = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete attendance record for " + name + " on " + date + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                facade.deleteAttendance(id);
                showMessageDialog("Success", "Record deleted!");
                refreshData();
            } catch (SQLException ex) {
                showErrorDialog("Database Error", ex.getMessage());
            }
        }
    }

    /**
     * Refreshes all data: Student Map and Attendance Records.
     */
    private void refreshData() throws SQLException {
        // 1. Refresh Student Map (ID -> Name)
        studentNameMap.clear();
        List<Student> students = facade.getAllStudents();
        for (Student s : students) {
            studentNameMap.put(s.getStudentId(), s.getName());
        }

        // 2. Load Attendance Records
        allRecords = facade.getAllAttendance();

        // 3. Apply Filters
        filterRecords();
    }

    private void filterRecords() {
        String search = searchField.getText().trim().toLowerCase();
        String dateFilter = dateFilterField.getText().trim();

        if (allRecords == null)
            return;

        List<Attendance> filtered = new ArrayList<>();
        int presentCount = 0;
        int absentCount = 0;

        for (Attendance r : allRecords) {
            String sName = studentNameMap.getOrDefault(r.getStudentId(), "Unknown");

            boolean matchesSearch = search.isEmpty() ||
                    r.getStudentId().toLowerCase().contains(search) ||
                    sName.toLowerCase().contains(search) ||
                    r.getStatus().toLowerCase().contains(search);

            boolean matchesDate = dateFilter.isEmpty() ||
                    r.getDate().contains(dateFilter);

            if (matchesSearch && matchesDate) {
                filtered.add(r);
                if ("PRESENT".equalsIgnoreCase(r.getStatus()))
                    presentCount++;
                if ("ABSENT".equalsIgnoreCase(r.getStatus()))
                    absentCount++;
            }
        }

        // Update Table
        tableModel.setRowCount(0);
        for (Attendance r : filtered) {
            tableModel.addRow(new Object[] {
                    r.getId(),
                    r.getStudentId(),
                    studentNameMap.getOrDefault(r.getStudentId(), "Unknown"),
                    r.getDate(),
                    r.getStatus()
            });
        }

        // Update Stats
        statsLabel.setText(String.format("Shown: %d (P: %d, A: %d)",
                filtered.size(), presentCount, absentCount));
    }
}
