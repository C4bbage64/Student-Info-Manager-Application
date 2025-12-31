package view;

import facade.StudentManagementFacade;
import facade.StudentManagementFacade.StudentInfo;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import model.Student;
import exceptions.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Dashboard panel showing comprehensive student information.
 * Displays student details, enrollment status, attendance summary, and payment summary.
 * Uses Facade pattern to get complete student information.
 */
public class StudentDashboardPanel extends BasePanel {

    private StudentManagementFacade facade;
    private JTextField studentIdField;
    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel courseLabel;
    private JLabel emailLabel;
    private JComboBox<String> statusComboBox;
    private JButton changeStatusButton;
    private JLabel attendanceRateLabel;
    private JProgressBar attendanceProgressBar;
    private JLabel attendanceStatusLabel;
    private JLabel presentCountLabel;
    private JLabel absentCountLabel;
    private JLabel totalPaidLabel;
    private JLabel balanceLabel;
    private JLabel totalFeesLabel;
    private JButton loadButton;
    
    private StudentInfo currentStudentInfo;

    public StudentDashboardPanel() {
        this.facade = StudentManagementFacade.getInstance();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Student ID input
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField(15);
        topPanel.add(studentIdField);
        loadButton = new JButton("Load Student");
        topPanel.add(loadButton);

        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Left panel - Student Info
        JPanel studentInfoPanel = createStudentInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        contentPanel.add(studentInfoPanel, gbc);

        // Right panel - Enrollment Status
        JPanel enrollmentPanel = createEnrollmentPanel();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        contentPanel.add(enrollmentPanel, gbc);

        // Bottom left - Attendance Summary
        JPanel attendancePanel = createAttendancePanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        contentPanel.add(attendancePanel, gbc);

        // Bottom right - Payment Summary
        JPanel paymentPanel = createPaymentPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(paymentPanel, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Load button action
        loadButton.addActionListener(e -> loadStudent());
        
        // Enter key support
        studentIdField.addActionListener(e -> loadStudent());
    }

    private JPanel createStudentInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Student Information", 
            TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameLabel = new JLabel("-");
        panel.add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageLabel = new JLabel("-");
        panel.add(ageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        courseLabel = new JLabel("-");
        panel.add(courseLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailLabel = new JLabel("-");
        panel.add(emailLabel, gbc);

        return panel;
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Enrollment Status", 
            TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(new String[]{"ENROLLED", "SUSPENDED", "GRADUATED"});
        statusComboBox.setEnabled(false);
        panel.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        changeStatusButton = new JButton("Change Status");
        changeStatusButton.setEnabled(false);
        panel.add(changeStatusButton, gbc);

        changeStatusButton.addActionListener(e -> changeEnrollmentStatus());

        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Attendance Summary", 
            TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Rate:"), gbc);
        gbc.gridx = 1;
        attendanceRateLabel = new JLabel("-");
        attendanceRateLabel.setFont(attendanceRateLabel.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(attendanceRateLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        attendanceProgressBar = new JProgressBar(0, 100);
        attendanceProgressBar.setStringPainted(true);
        panel.add(attendanceProgressBar, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        attendanceStatusLabel = new JLabel("-");
        panel.add(attendanceStatusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        presentCountLabel = new JLabel("Present: -");
        panel.add(presentCountLabel, gbc);

        gbc.gridx = 1;
        absentCountLabel = new JLabel("Absent: -");
        panel.add(absentCountLabel, gbc);

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Payment Summary", 
            TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Total Paid:"), gbc);
        gbc.gridx = 1;
        totalPaidLabel = new JLabel("-");
        totalPaidLabel.setFont(totalPaidLabel.getFont().deriveFont(Font.BOLD));
        totalPaidLabel.setForeground(new Color(0, 128, 0)); // Green
        panel.add(totalPaidLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Total Fees:"), gbc);
        gbc.gridx = 1;
        totalFeesLabel = new JLabel("-");
        panel.add(totalFeesLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        balanceLabel = new JLabel("-");
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD));
        panel.add(balanceLabel, gbc);

        return panel;
    }

    private void loadStudent() {
        String studentId = studentIdField.getText().trim();
        
        if (studentId.isEmpty()) {
            showWarningDialog("Validation Error", "Please enter a Student ID");
            return;
        }

        try {
            // Chain of Responsibility: Validate input
            ValidationHandler validator = ValidationChainBuilder.buildGeneralValidationChain();
            validator.validate("studentId", studentId);

            // Get complete student info using Facade pattern
            currentStudentInfo = facade.getCompleteStudentInfo(studentId);
            Student student = currentStudentInfo.getStudent();

            // Update student info panel
            nameLabel.setText(student.getName());
            ageLabel.setText(String.valueOf(student.getAge()));
            courseLabel.setText(student.getCourse());
            emailLabel.setText(student.getEmail() != null ? student.getEmail() : "N/A");

            // Update enrollment status
            String status = student.getEnrollmentStatus();
            statusComboBox.setSelectedItem(status);
            statusComboBox.setEnabled(true);
            changeStatusButton.setEnabled(true);

            // Update attendance summary
            double attendanceRate = currentStudentInfo.getAttendanceRate();
            attendanceRateLabel.setText(String.format("%.1f%%", attendanceRate));
            attendanceProgressBar.setValue((int) attendanceRate);
            
            // Color code attendance
            if (attendanceRate >= 80) {
                attendanceProgressBar.setForeground(new Color(0, 128, 0)); // Green
                attendanceStatusLabel.setText("Good");
                attendanceStatusLabel.setForeground(new Color(0, 128, 0));
            } else if (attendanceRate >= 60) {
                attendanceProgressBar.setForeground(new Color(255, 165, 0)); // Orange
                attendanceStatusLabel.setText("Fair");
                attendanceStatusLabel.setForeground(new Color(255, 165, 0));
            } else {
                attendanceProgressBar.setForeground(Color.RED);
                attendanceStatusLabel.setText("Poor");
                attendanceStatusLabel.setForeground(Color.RED);
            }

            // Calculate present/absent counts (approximate from rate)
            // Note: This is an approximation. For exact counts, we'd need to query attendance records
            int totalRecords = (int) (attendanceRate > 0 ? (100.0 / attendanceRate) : 0);
            int present = totalRecords > 0 ? (int) (totalRecords * attendanceRate / 100.0) : 0;
            int absent = totalRecords - present;
            presentCountLabel.setText("Present: " + present);
            absentCountLabel.setText("Absent: " + absent);

            // Update payment summary
            double totalPaid = currentStudentInfo.getTotalPaid();
            double balance = currentStudentInfo.getBalance();
            double totalFees = totalPaid + balance;

            totalPaidLabel.setText(String.format("$%.2f", totalPaid));
            totalFeesLabel.setText(String.format("$%.2f", totalFees));
            
            if (balance <= 0) {
                balanceLabel.setText(String.format("$%.2f (PAID IN FULL)", balance));
                balanceLabel.setForeground(new Color(0, 128, 0)); // Green
            } else {
                balanceLabel.setText(String.format("$%.2f", balance));
                balanceLabel.setForeground(Color.RED);
            }

        } catch (InvalidInputException ex) {
            showWarningDialog("Validation Error", ex.getMessage());
        } catch (StudentNotFoundException ex) {
            showErrorDialog("Error", ex.getMessage());
            clearDashboard();
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load student: " + ex.getMessage());
            clearDashboard();
        }
    }

    private void changeEnrollmentStatus() {
        if (currentStudentInfo == null) {
            return;
        }

        String newStatus = (String) statusComboBox.getSelectedItem();
        String studentId = currentStudentInfo.getStudent().getStudentId();

        try {
            facade.updateEnrollmentStatus(studentId, newStatus);
            showMessageDialog("Success", "Enrollment status updated successfully!");
            
            // Reload student to refresh display
            loadStudent();
        } catch (InvalidInputException ex) {
            showWarningDialog("Validation Error", ex.getMessage());
        } catch (StudentNotFoundException ex) {
            showErrorDialog("Error", ex.getMessage());
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to update enrollment status: " + ex.getMessage());
        }
    }

    private void clearDashboard() {
        nameLabel.setText("-");
        ageLabel.setText("-");
        courseLabel.setText("-");
        emailLabel.setText("-");
        statusComboBox.setSelectedItem("ENROLLED");
        statusComboBox.setEnabled(false);
        changeStatusButton.setEnabled(false);
        attendanceRateLabel.setText("-");
        attendanceProgressBar.setValue(0);
        attendanceStatusLabel.setText("-");
        presentCountLabel.setText("Present: -");
        absentCountLabel.setText("Absent: -");
        totalPaidLabel.setText("-");
        totalFeesLabel.setText("-");
        balanceLabel.setText("-");
        currentStudentInfo = null;
    }
}

