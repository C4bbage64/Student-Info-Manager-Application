package dao;

import model.Payment;
import exceptions.PaymentNotFoundException;
import exceptions.StudentNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Payment entity.
 * Part of the DAO pattern - handles all database operations for payment records.
 */
public class PaymentDAO {

    private StudentDAO studentDAO;

    public PaymentDAO() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * Adds a new payment record.
     */
    public void addPayment(String studentId, double amount, String date, String description) 
            throws SQLException, StudentNotFoundException {
        
        // Verify student exists
        if (!studentDAO.exists(studentId)) {
            throw new StudentNotFoundException(studentId);
        }

        String sql = "INSERT INTO payments (student_id, amount, date, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setDouble(2, amount);
            stmt.setString(3, date);
            stmt.setString(4, description);
            stmt.executeUpdate();
        }
    }

    /**
     * Gets payment records for a specific student.
     */
    public List<Payment> getPaymentsByStudent(String studentId) 
            throws SQLException, PaymentNotFoundException {
        
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE student_id = ? ORDER BY date DESC";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }

        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(studentId);
        }
        return payments;
    }

    /**
     * Gets all payment records.
     */
    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY date DESC";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    /**
     * Calculates total amount paid by a student.
     */
    public double calculateTotalPaid(String studentId) throws SQLException {
        String sql = "SELECT SUM(amount) as total FROM payments WHERE student_id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    /**
     * Calculates balance (total fees - total paid) for a student.
     * Assumes a fixed total fee amount (can be customized).
     */
    public double calculateBalance(String studentId, double totalFees) throws SQLException {
        double totalPaid = calculateTotalPaid(studentId);
        return totalFees - totalPaid;
    }

    /**
     * Deletes a payment record by ID.
     */
    public void deletePayment(int id) throws SQLException {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Updates a payment record.
     */
    public void updatePayment(int id, double amount, String description) throws SQLException {
        String sql = "UPDATE payments SET amount = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, description);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Gets payment by ID.
     */
    public Payment getPaymentById(int id) throws SQLException, PaymentNotFoundException {
        String sql = "SELECT * FROM payments WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        }
        throw new PaymentNotFoundException(id);
    }

    /**
     * Helper method to map ResultSet to Payment object.
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("id"),
            rs.getString("student_id"),
            rs.getDouble("amount"),
            rs.getString("date"),
            rs.getString("description")
        );
    }
}
