package view;

import model.Student;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PaymentFormDialog extends JDialog {
    private JComboBox<String> studentCombo;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField descField;
    private boolean submitted = false;
    private List<Student> students;

    public PaymentFormDialog(Frame owner, List<Student> students) {
        super(owner, "Add Payment", true);
        this.students = students;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Student:"), gbc);

        gbc.gridx = 1;
        studentCombo = new JComboBox<>();
        for (Student s : students) {
            studentCombo.addItem(s.getStudentId() + " - " + s.getName());
        }
        add(studentCombo, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Amount ($):"), gbc);

        gbc.gridx = 1;
        amountField = new JTextField(15);
        add(amountField, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        dateField = new JTextField(LocalDate.now().toString(), 15);
        add(dateField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        descField = new JTextField(15);
        add(descField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            submitted = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(getOwner());
    }

    public boolean showDialog() {
        setVisible(true);
        return submitted;
    }

    public String getSelectedStudentId() {
        String selected = (String) studentCombo.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            return selected.split(" - ")[0];
        }
        return null;
    }

    public String getAmount() {
        return amountField.getText().trim();
    }

    public String getDate() {
        return dateField.getText().trim();
    }

    public String getDescription() {
        return descField.getText().trim();
    }
}
