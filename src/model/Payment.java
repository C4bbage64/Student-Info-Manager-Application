package model;

/**
 * Model class representing a Payment record.
 * Part of the MVC architecture - Model layer.
 */
public class Payment {
    private int id;
    private String studentId;
    private double amount;
    private String date;
    private String description;

    // Default constructor
    public Payment() {
    }

    // Constructor with all fields
    public Payment(int id, String studentId, double amount, String date, String description) {
        this.id = id;
        this.studentId = studentId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Constructor without id (for new records)
    public Payment(String studentId, double amount, String date, String description) {
        this.studentId = studentId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Student: %s | Amount: $%.2f | Date: %s | Description: %s",
                id, studentId, amount, date, description);
    }
}
