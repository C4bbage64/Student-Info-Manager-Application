package model;

/**
 * Model class representing an Attendance record.
 * Part of the MVC architecture - Model layer.
 */
public class Attendance {
    private int id;
    private String studentId;
    private String date;
    private String status; // PRESENT or ABSENT

    // Default constructor
    public Attendance() {
    }

    // Constructor with all fields
    public Attendance(int id, String studentId, String date, String status) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    // Constructor without id (for new records)
    public Attendance(String studentId, String date, String status) {
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Student: %s | Date: %s | Status: %s",
                id, studentId, date, status);
    }
}
