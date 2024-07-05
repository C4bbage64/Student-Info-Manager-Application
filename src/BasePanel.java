import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel {
    private JFrame frame; // JFrame instance variable for the application window

    // Constructor for BasePanel
    public BasePanel() {
        initializeUI(); // Initialize the user interface
    }

    // Method to initialize the user interface
    private void initializeUI() {
        try {
            // Set the look and feel of the UI to Nimbus
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace if setting look and feel fails
        }

        // Create and set up the JFrame
        frame = new JFrame("Student Info Manager Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // Set the size of the JFrame
    }

    /**
     * Sets the styles for the provided components. This method sets the font for all
     * JComponents passed in, and if a component is a JButton, it also sets the font to bold,
     * background color, and foreground color.
     *
     * @param components Varargs parameter allowing multiple JComponent objects to be styled.
     */
    protected void setComponentStyles(JComponent... components) {
        // Iterate over each component provided
        for (JComponent component : components) {
            // Set the font for the component to Arial, plain, size 14
            component.setFont(new Font("Arial", Font.PLAIN, 14));
            // If the component is an instance of JButton, apply additional styles
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                // Set the font for the button to Arial, bold, size 14
                button.setFont(new Font("Arial", Font.BOLD, 14));
                // Set the background color of the button to a specific shade of blue
                button.setBackground(new Color(70, 130, 180));
                // Set the foreground color (text color) of the button to white
                button.setForeground(Color.WHITE);
            }
        }
    }

    /**
     * Adds a label and corresponding text field to a panel using GridBagLayout. This method
     * positions the label and text field in the specified row of the panel.
     *
     * @param panel The JPanel to which the label and field are added.
     * @param gbc   The GridBagConstraints used to layout the components.
     * @param label The JLabel to be added to the panel.
     * @param field The JTextField to be added to the panel.
     * @param row   The row index in the GridBagLayout where the label and field will be placed.
     */
    protected void addLabelAndField(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField field, int row) {
        // Set the GridBagConstraints for the label to be in the first column (0) of the specified row
        gbc.gridx = 0;
        gbc.gridy = row;
        // Add the label to the panel with the specified GridBagConstraints
        panel.add(label, gbc);
        // Set the GridBagConstraints for the field to be in the second column (1) of the specified row
        gbc.gridx = 1;
        // Add the text field to the panel with the specified GridBagConstraints
        panel.add(field, gbc);
    }

    /**
     * Displays a message dialog with the specified title and message. This method
     * uses JOptionPane to show a dialog box with an information message type.
     *
     * @param title   The title of the dialog window.
     * @param message The message to be displayed in the dialog.
     */
    protected void showMessageDialog(String title, String message) {
        // Display a message dialog with the specified title and message
        // The parent component is null, meaning the dialog is not attached to any specific component
        // The message type is INFORMATION_MESSAGE, which typically displays an information icon
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
