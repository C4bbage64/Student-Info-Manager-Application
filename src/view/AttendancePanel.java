package view;

import controller.AttendanceController;
import facade.StudentManagementFacade;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import model.Attendance;
import exceptions.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for managing student attendance.
 * Part of MVC architecture - View layer.
 */
public class AttendancePanel extends BasePanel {

    private AttendanceController controller;
    private StudentManagementFacade facade;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public AttendancePanel() {
        this.controller = new AttendanceController();
        this.facade = StudentManagementFacade.getInstance();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mark Attendance", createMarkAttendancePane());
        tabbedPane.addTab("View Attendance", createViewAttendancePane());
        tabbedPane.addTab("Attendance Rate", createAttendanceRatePane());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createMarkAttendancePane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField(LocalDate.now().toString(), 20);
        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"PRESENT", "ABSENT"});
        JButton markButton = new JButton("Mark Attendance");

        setComponentStyles(markButton, studentIdLabel, dateLabel, statusLabel);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(dateLabel, gbc);
        gbc.gridx = 1; panel.add(dateField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(statusLabel, gbc);
        gbc.gridx = 1; panel.add(statusBox, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(markButton, gbc);

        markButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                String date = dateField.getText().trim();
                String status = (String) statusBox.getSelectedItem();

                // Chain of Responsibility: Validate inputs
                ValidationHandler validator = ValidationChainBuilder.buildAttendanceValidationChain();
                validator.validate("studentId", studentId);
                validator.validate("date", date);
                validator.validate("status", status);

                controller.markAttendance(studentId, date, status);
                showMessageDialog("Success", "Attendance marked successfully!");
                studentIdField.setText("");
                
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (StudentNotFoundException ex) {
                showErrorDialog("Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to mark attendance: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createViewAttendancePane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel studentIdLabel = new JLabel("Student ID (leave empty for all):");
        JTextField studentIdField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh All");

        setComponentStyles(searchButton, refreshButton, studentIdLabel);

        searchPanel.add(studentIdLabel);
        searchPanel.add(studentIdField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // Table
        String[] columns = {"ID", "Student ID", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);

        // Bottom panel for Edit/Delete buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        setComponentStyles(editButton, deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Enable buttons when row is selected
        attendanceTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = attendanceTable.getSelectedRow() >= 0;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });

        // Edit button action
        editButton.addActionListener(e -> editAttendance());

        // Delete button action
        deleteButton.addActionListener(e -> deleteAttendance());

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        searchButton.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()) {
                loadAllAttendance();
            } else {
                loadStudentAttendance(studentId);
            }
        });

        refreshButton.addActionListener(e -> loadAllAttendance());

        return panel;
    }

    private JPanel createAttendanceRatePane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JButton calculateButton = new JButton("Calculate Rate");
        JLabel resultLabel = new JLabel("Attendance Rate: ");
        JLabel rateLabel = new JLabel("-");
        rateLabel.setFont(new Font("Arial", Font.BOLD, 18));

        setComponentStyles(calculateButton, studentIdLabel, resultLabel);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(calculateButton, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(resultLabel, gbc);
        gbc.gridx = 1; panel.add(rateLabel, gbc);

        calculateButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                double rate = controller.getAttendanceRate(studentId);
                rateLabel.setText(String.format("%.2f%%", rate));
                
                // Color code the result
                if (rate >= 80) {
                    rateLabel.setForeground(new Color(0, 128, 0)); // Green
                } else if (rate >= 60) {
                    rateLabel.setForeground(new Color(255, 165, 0)); // Orange
                } else {
                    rateLabel.setForeground(Color.RED);
                }
                
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to calculate rate: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void loadAllAttendance() {
        try {
            List<Attendance> records = controller.getAllAttendance();
            updateTable(records);
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load attendance: " + ex.getMessage());
        }
    }

    private void loadStudentAttendance(String studentId) {
        try {
            List<Attendance> records = controller.getStudentAttendance(studentId);
            updateTable(records);
        } catch (InvalidInputException ex) {
            showWarningDialog("Validation Error", ex.getMessage());
        } catch (AttendanceRecordNotFoundException ex) {
            tableModel.setRowCount(0);
            showMessageDialog("Info", ex.getMessage());
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load attendance: " + ex.getMessage());
        }
    }

    private void updateTable(List<Attendance> records) {
        tableModel.setRowCount(0);
        for (Attendance record : records) {
            tableModel.addRow(new Object[]{
                record.getId(),
                record.getStudentId(),
                record.getDate(),
                record.getStatus()
            });
        }
    }

    private void editAttendance() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);

        // Create dialog for editing
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Attendance", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"PRESENT", "ABSENT"});
        statusCombo.setSelectedItem(currentStatus);
        dialog.add(statusCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, gbc);

        saveButton.addActionListener(e -> {
            try {
                String newStatus = (String) statusCombo.getSelectedItem();
                
                // Chain of Responsibility: Validate input
                ValidationHandler validator = ValidationChainBuilder.buildAttendanceValidationChain();
                validator.validate("status", newStatus);

                facade.updateAttendance(id, newStatus);
                showMessageDialog("Success", "Attendance updated successfully!");
                dialog.dispose();
                
                // Refresh table
                int studentIdCol = 1;
                String studentId = (String) tableModel.getValueAt(selectedRow, studentIdCol);
                if (studentId != null && !studentId.isEmpty()) {
                    loadStudentAttendance(studentId);
                } else {
                    loadAllAttendance();
                }
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to update attendance: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteAttendance() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String studentId = (String) tableModel.getValueAt(selectedRow, 1);
        String date = (String) tableModel.getValueAt(selectedRow, 2);
        String status = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this attendance record?\n" +
            "Student ID: " + studentId + "\n" +
            "Date: " + date + "\n" +
            "Status: " + status,
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                facade.deleteAttendance(id);
                showMessageDialog("Success", "Attendance record deleted successfully!");
                
                // Refresh table
                if (studentId != null && !studentId.isEmpty()) {
                    loadStudentAttendance(studentId);
                } else {
                    loadAllAttendance();
                }
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to delete attendance: " + ex.getMessage());
            }
        }
    }
}
