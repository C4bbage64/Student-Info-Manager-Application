package view;

import controller.PaymentController;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import model.Payment;
import exceptions.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for managing student finance/payments.
 * Part of MVC architecture - View layer.
 */
public class FinancePanel extends BasePanel {

    private PaymentController controller;
    private JTable paymentTable;
    private DefaultTableModel tableModel;

    public FinancePanel() {
        this.controller = new PaymentController();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Payment", createAddPaymentPane());
        tabbedPane.addTab("View Payments", createViewPaymentsPane());
        tabbedPane.addTab("Check Balance", createCheckBalancePane());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAddPaymentPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JLabel amountLabel = new JLabel("Amount ($):");
        JTextField amountField = new JTextField(20);
        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField(LocalDate.now().toString(), 20);
        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField(20);
        JButton addButton = new JButton("Add Payment");

        setComponentStyles(addButton, studentIdLabel, amountLabel, dateLabel, descLabel);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(amountLabel, gbc);
        gbc.gridx = 1; panel.add(amountField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(dateLabel, gbc);
        gbc.gridx = 1; panel.add(dateField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(descLabel, gbc);
        gbc.gridx = 1; panel.add(descField, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(addButton, gbc);

        addButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                String amountStr = amountField.getText().trim();
                String date = dateField.getText().trim();
                String description = descField.getText().trim();

                // Chain of Responsibility: Validate inputs
                ValidationHandler validator = ValidationChainBuilder.buildPaymentValidationChain();
                validator.validate("studentId", studentId);
                validator.validate("amount", amountStr);
                validator.validate("date", date);

                double amount = Double.parseDouble(amountStr);
                controller.addPayment(studentId, amount, date, description);
                showMessageDialog("Success", "Payment added successfully!");
                
                // Clear fields
                studentIdField.setText("");
                amountField.setText("");
                descField.setText("");
                
            } catch (NumberFormatException ex) {
                showWarningDialog("Validation Error", "Invalid amount format!");
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (StudentNotFoundException ex) {
                showErrorDialog("Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to add payment: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createViewPaymentsPane() {
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
        String[] columns = {"ID", "Student ID", "Amount", "Date", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        // Bottom panel for total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);

        // Action listeners
        searchButton.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()) {
                loadAllPayments(totalLabel);
            } else {
                loadStudentPayments(studentId, totalLabel);
            }
        });

        refreshButton.addActionListener(e -> loadAllPayments(totalLabel));

        return panel;
    }

    private JPanel createCheckBalancePane() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JLabel totalFeesLabel = new JLabel("Total Fees ($):");
        JTextField totalFeesField = new JTextField("5000.00", 20);
        JButton checkButton = new JButton("Check Balance");

        JLabel paidLabel = new JLabel("Total Paid: ");
        JLabel paidValueLabel = new JLabel("-");
        JLabel balanceLabel = new JLabel("Balance Due: ");
        JLabel balanceValueLabel = new JLabel("-");

        paidValueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceValueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        setComponentStyles(checkButton, studentIdLabel, totalFeesLabel, paidLabel, balanceLabel);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(totalFeesLabel, gbc);
        gbc.gridx = 1; panel.add(totalFeesField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(checkButton, gbc);

        // Separator
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 4; panel.add(paidLabel, gbc);
        gbc.gridx = 1; panel.add(paidValueLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(balanceLabel, gbc);
        gbc.gridx = 1; panel.add(balanceValueLabel, gbc);

        checkButton.addActionListener(e -> {
            try {
                String studentId = studentIdField.getText().trim();
                String totalFeesStr = totalFeesField.getText().trim();

                // Chain of Responsibility: Validate inputs
                ValidationHandler validator = ValidationChainBuilder.buildGeneralValidationChain();
                validator.validate("studentId", studentId);
                validator.validate("totalFees", totalFeesStr);

                double totalFees = Double.parseDouble(totalFeesStr);
                double totalPaid = controller.getTotalPaid(studentId);
                double balance = controller.getBalance(studentId, totalFees);

                paidValueLabel.setText(String.format("$%.2f", totalPaid));
                balanceValueLabel.setText(String.format("$%.2f", balance));

                // Color code balance
                if (balance <= 0) {
                    balanceValueLabel.setForeground(new Color(0, 128, 0)); // Green - paid in full
                    balanceValueLabel.setText("$0.00 (PAID IN FULL)");
                } else {
                    balanceValueLabel.setForeground(Color.RED);
                }
                paidValueLabel.setForeground(new Color(0, 128, 0));

            } catch (NumberFormatException ex) {
                showWarningDialog("Validation Error", "Invalid fees format!");
            } catch (InvalidInputException ex) {
                showWarningDialog("Validation Error", ex.getMessage());
            } catch (SQLException ex) {
                showErrorDialog("Database Error", "Failed to check balance: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void loadAllPayments(JLabel totalLabel) {
        try {
            List<Payment> payments = controller.getAllPayments();
            updateTable(payments);
            
            double total = payments.stream().mapToDouble(Payment::getAmount).sum();
            totalLabel.setText(String.format("Total: $%.2f", total));
            
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load payments: " + ex.getMessage());
        }
    }

    private void loadStudentPayments(String studentId, JLabel totalLabel) {
        try {
            List<Payment> payments = controller.getStudentPayments(studentId);
            updateTable(payments);
            
            double total = payments.stream().mapToDouble(Payment::getAmount).sum();
            totalLabel.setText(String.format("Total: $%.2f", total));
            
        } catch (InvalidInputException ex) {
            showWarningDialog("Validation Error", ex.getMessage());
        } catch (PaymentNotFoundException ex) {
            tableModel.setRowCount(0);
            totalLabel.setText("Total: $0.00");
            showMessageDialog("Info", ex.getMessage());
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load payments: " + ex.getMessage());
        }
    }

    private void updateTable(List<Payment> payments) {
        tableModel.setRowCount(0);
        for (Payment payment : payments) {
            tableModel.addRow(new Object[]{
                payment.getId(),
                payment.getStudentId(),
                String.format("$%.2f", payment.getAmount()),
                payment.getDate(),
                payment.getDescription()
            });
        }
    }
}
