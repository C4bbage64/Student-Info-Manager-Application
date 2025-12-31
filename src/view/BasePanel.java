package view;

import javax.swing.*;
import java.awt.*;

/**
 * Base panel class providing common UI functionality.
 * Part of MVC architecture - View layer base class.
 */
public class BasePanel extends JPanel {

    // Constructor for BasePanel
    public BasePanel() {
        // Look and Feel is set globally in StudentInfoApp.main()
    }

    /**
     * Sets the styles for the provided components.
     */
    protected void setComponentStyles(JComponent... components) {
        for (JComponent component : components) {
            component.setFont(new Font("Arial", Font.PLAIN, 14));
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setFont(new Font("Arial", Font.BOLD, 14));
                button.setBackground(new Color(70, 130, 180));
                button.setForeground(Color.WHITE);
            }
        }
    }

    /**
     * Adds a label and corresponding text field to a panel using GridBagLayout.
     */
    protected void addLabelAndField(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    /**
     * Displays a message dialog with the specified title and message.
     */
    protected void showMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error dialog with the specified title and message.
     */
    protected void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a warning dialog with the specified title and message.
     */
    protected void showWarningDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
