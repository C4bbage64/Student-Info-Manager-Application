package view;

import controller.AttendanceController;
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
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public AttendancePanel() {
        this.controller = new AttendanceController();
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

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

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
}
