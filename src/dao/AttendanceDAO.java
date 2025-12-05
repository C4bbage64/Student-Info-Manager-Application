package dao;

import model.Attendance;
import exceptions.AttendanceRecordNotFoundException;
import exceptions.StudentNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Attendance entity.
 * Part of the DAO pattern - handles all database operations for attendance records.
 */
public class AttendanceDAO {

    private StudentDAO studentDAO;

    public AttendanceDAO() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * Marks attendance for a student.
     */
    public void markAttendance(String studentId, String date, String status) 
            throws SQLException, StudentNotFoundException {
        
        // Verify student exists
        if (!studentDAO.exists(studentId)) {
            throw new StudentNotFoundException(studentId);
        }

        String sql = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, date);
            stmt.setString(3, status);
            stmt.executeUpdate();
        }
    }

    /**
     * Gets attendance records for a specific student.
     */
    public List<Attendance> getAttendanceByStudent(String studentId) 
            throws SQLException, AttendanceRecordNotFoundException {
        
        List<Attendance> records = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ? ORDER BY date DESC";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(mapResultSetToAttendance(rs));
            }
        }

        if (records.isEmpty()) {
            throw new AttendanceRecordNotFoundException(studentId);
        }
        return records;
    }

    /**
     * Gets all attendance records.
     */
    public List<Attendance> getAllAttendance() throws SQLException {
        List<Attendance> records = new ArrayList<>();
        String sql = "SELECT * FROM attendance ORDER BY date DESC";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(mapResultSetToAttendance(rs));
            }
        }
        return records;
    }

    /**
     * Gets attendance records for a specific date.
     */
    public List<Attendance> getAttendanceByDate(String date) throws SQLException {
        List<Attendance> records = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE date = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, date);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(mapResultSetToAttendance(rs));
            }
        }
        return records;
    }

    /**
     * Calculates attendance rate (percentage) for a student.
     */
    public double calculateAttendanceRate(String studentId) throws SQLException {
        String sql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) as present
            FROM attendance WHERE student_id = ?
            """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                return total > 0 ? (present * 100.0 / total) : 0.0;
            }
        }
        return 0.0;
    }

    /**
     * Deletes an attendance record by ID.
     */
    public void deleteAttendance(int id) throws SQLException {
        String sql = "DELETE FROM attendance WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Updates an attendance record.
     */
    public void updateAttendance(int id, String status) throws SQLException {
        String sql = "UPDATE attendance SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Helper method to map ResultSet to Attendance object.
     */
    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        return new Attendance(
            rs.getInt("id"),
            rs.getString("student_id"),
            rs.getString("date"),
            rs.getString("status")
        );
    }
}
