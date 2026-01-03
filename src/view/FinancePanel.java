package view;

import facade.StudentManagementFacade;
import observer.StudentDataObserver;
import observer.StudentDataManager;
import chain.ValidationChainBuilder;
import chain.ValidationHandler;
import model.Payment;
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
 * Unified Finance Panel consolidating Add, View, and Balance operations.
 * Part of MVC architecture - View layer.
 * Uses Facade pattern to interact with controllers.
 * Implements Observer pattern to auto-refresh when payment data changes.
 */
public class FinancePanel extends BasePanel implements StudentDataObserver {

    private StudentManagementFacade facade;
    private DefaultTableModel tableModel;
    private JTable paymentTable;

    // Cache for student names: StudentID -> Name
    private Map<String, String> studentNameMap;
    // Store all records for filtering
    private List<Payment> allRecords;

    // Toolbar buttons
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton clearButton;

    // Search/filter
    private JTextField searchField;
    private JTextField dateFromField;
    private JTextField dateToField;
    private JLabel totalLabel;

    public FinancePanel() {
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
        if ("PAYMENT".equals(eventType) || "STUDENT_UPDATE".equals(eventType)) {
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
        searchField.setToolTipText("Search by Student ID, Name, or Description");
        topPanel.add(searchField);

        clearButton = new JButton("Clear");
        setComponentStyles(clearButton);
        topPanel.add(clearButton);

        topPanel.add(new JLabel("From:"));
        dateFromField = new JTextField(8);
        topPanel.add(dateFromField);

        topPanel.add(new JLabel("To:"));
        dateToField = new JTextField(8);
        topPanel.add(dateToField);

        refreshButton = new JButton("Refresh");
        setComponentStyles(refreshButton);
        topPanel.add(refreshButton);

        // Table
        String[] columns = { "ID", "Student ID", "Student Name", "Amount", "Date", "Description" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        paymentTable = new JTable(tableModel);
        paymentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentTable.setRowHeight(25);

        // Color coding for Amount
        paymentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected && column == 3) { // Amount column
                    c.setForeground(new Color(0, 128, 0)); // Dark green text
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(Color.BLACK);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        // Bottom panel - Actions and Total
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Payment");
        editButton = new JButton("Edit Selected");
        deleteButton = new JButton("Delete Selected");

        setComponentStyles(addButton, editButton, deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalLabel);

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(totalPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> handleAddPayment());
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
            dateFromField.setText("");
            dateToField.setText("");
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
        dateFromField.getDocument().addDocumentListener(filterListener);
        dateToField.getDocument().addDocumentListener(filterListener);

        // Initial Load
        try {
            refreshData();
        } catch (SQLException ex) {
            showErrorDialog("Database Error", "Failed to load data: " + ex.getMessage());
        }
    }

    private void handleAddPayment() {
        try {
            List<Student> students = facade.getAllStudents();
            PaymentFormDialog dialog = new PaymentFormDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    students);

            if (dialog.showDialog()) {
                String studentId = dialog.getSelectedStudentId();
                String amountStr = dialog.getAmount();
                String date = dialog.getDate();
                String description = dialog.getDescription();

                // Validate
                ValidationHandler validator = ValidationChainBuilder.buildPaymentValidationChain();
                validator.validate("studentId", studentId);
                validator.validate("amount", amountStr);
                validator.validate("date", date);

                double amount = Double.parseDouble(amountStr);

                facade.addPayment(studentId, amount, date, description);
                showMessageDialog("Success", "Payment added!");
                refreshData();
            }
        } catch (Exception ex) {
            if (ex instanceof NumberFormatException) {
                showWarningDialog("Validation Error", "Invalid amount format!");
            } else {
                showErrorDialog("Error", ex.getMessage());
            }
        }
    }

    private void handleEdit() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog("Selection Required", "Please select a record to edit.");
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String currentAmount = ((String) tableModel.getValueAt(selectedRow, 3)).replace("$", "").trim();
        String currentDesc = (String) tableModel.getValueAt(selectedRow, 5);

        // Simple dialog for editing amount and description
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Payment", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Amount ($):"), gbc);
        JTextField amountField = new JTextField(currentAmount, 15);
        gbc.gridx = 1;
        dialog.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Description:"), gbc);
        JTextField descField = new JTextField(currentDesc, 15);
        gbc.gridx = 1;
        dialog.add(descField, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        saveBtn.addActionListener(e -> {
            try {
                String amtStr = amountField.getText().trim();
                String desc = descField.getText().trim();

                ValidationHandler validator = ValidationChainBuilder.buildPaymentValidationChain();
                validator.validate("amount", amtStr);

                double amount = Double.parseDouble(amtStr);

                facade.updatePayment(id, amount, desc);
                showMessageDialog("Success", "Payment updated!");
                dialog.dispose();
                refreshData();
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException) {
                    showWarningDialog("Validation Error", "Invalid amount format!");
                } else {
                    showErrorDialog("Error", ex.getMessage());
                }
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void handleDelete() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog("Selection Required", "Please select a record to delete.");
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 2);
        String amount = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete payment of " + amount + " for " + name + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                facade.deletePayment(id);
                showMessageDialog("Success", "Record deleted!");
                refreshData();
            } catch (SQLException ex) {
                showErrorDialog("Database Error", ex.getMessage());
            }
        }
    }

    /**
     * Refreshes all data: Student Map and Payment Records.
     */
    private void refreshData() throws SQLException {
        // 1. Refresh Student Map (ID -> Name)
        studentNameMap.clear();
        List<Student> students = facade.getAllStudents();
        for (Student s : students) {
            studentNameMap.put(s.getStudentId(), s.getName());
        }

        // 2. Load Payment Records
        allRecords = facade.getAllPayments();

        // 3. Apply Filters
        filterRecords();
    }

    private void filterRecords() {
        String search = searchField.getText().trim().toLowerCase();
        String from = dateFromField.getText().trim();
        String to = dateToField.getText().trim();

        if (allRecords == null)
            return;

        List<Payment> filtered = new ArrayList<>();
        double totalAmount = 0.0;

        for (Payment r : allRecords) {
            String sName = studentNameMap.getOrDefault(r.getStudentId(), "Unknown");

            boolean matchesSearch = search.isEmpty() ||
                    r.getStudentId().toLowerCase().contains(search) ||
                    sName.toLowerCase().contains(search) ||
                    r.getDescription().toLowerCase().contains(search);

            boolean matchesDate = (from.isEmpty() || r.getDate().compareTo(from) >= 0) &&
                    (to.isEmpty() || r.getDate().compareTo(to) <= 0);

            if (matchesSearch && matchesDate) {
                filtered.add(r);
                totalAmount += r.getAmount();
            }
        }

        // Update Table
        tableModel.setRowCount(0);
        for (Payment r : filtered) {
            tableModel.addRow(new Object[] {
                    r.getId(),
                    r.getStudentId(),
                    studentNameMap.getOrDefault(r.getStudentId(), "Unknown"),
                    String.format("$%.2f", r.getAmount()),
                    r.getDate(),
                    r.getDescription()
            });
        }

        // Update Total
        totalLabel.setText(String.format("Total: $%.2f", totalAmount));
    }
}
