package controller;

import dao.PaymentDAO;
import model.Payment;
import exceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for Payment/Finance operations.
 * Part of MVC architecture - handles business logic between View and DAO.
 */
public class PaymentController {
    private final PaymentDAO paymentDAO;
    private static final double DEFAULT_TOTAL_FEES = 5000.00; // Default total fees

    public PaymentController() {
        this.paymentDAO = new PaymentDAO();
    }

    /**
     * Adds a new payment with validation.
     */
    public void addPayment(String studentId, double amount, String description) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        // Validation
        validatePaymentInput(studentId, amount);

        String today = LocalDate.now().toString();
        paymentDAO.addPayment(studentId, amount, today, description);
    }

    /**
     * Adds a new payment on a specific date.
     */
    public void addPayment(String studentId, double amount, String date, String description) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        // Validation
        validatePaymentInput(studentId, amount);
        if (date == null || date.trim().isEmpty()) {
            throw new InvalidInputException("Date cannot be empty");
        }

        paymentDAO.addPayment(studentId, amount, date, description);
    }

    /**
     * Gets payment records for a student.
     */
    public List<Payment> getStudentPayments(String studentId) 
            throws InvalidInputException, PaymentNotFoundException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        return paymentDAO.getPaymentsByStudent(studentId);
    }

    /**
     * Gets all payment records.
     */
    public List<Payment> getAllPayments() throws SQLException {
        return paymentDAO.getAllPayments();
    }

    /**
     * Calculates total amount paid by a student.
     */
    public double getTotalPaid(String studentId) 
            throws InvalidInputException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        return paymentDAO.calculateTotalPaid(studentId);
    }

    /**
     * Calculates outstanding balance for a student.
     */
    public double getBalance(String studentId) 
            throws InvalidInputException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        return paymentDAO.calculateBalance(studentId, DEFAULT_TOTAL_FEES);
    }

    /**
     * Calculates outstanding balance with custom total fees.
     */
    public double getBalance(String studentId, double totalFees) 
            throws InvalidInputException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        if (totalFees < 0) {
            throw new InvalidInputException("Total fees cannot be negative");
        }
        return paymentDAO.calculateBalance(studentId, totalFees);
    }

    /**
     * Gets a payment by ID.
     */
    public Payment getPayment(int id) 
            throws PaymentNotFoundException, SQLException {
        return paymentDAO.getPaymentById(id);
    }

    /**
     * Deletes a payment record.
     */
    public void deletePayment(int id) throws SQLException {
        paymentDAO.deletePayment(id);
    }

    /**
     * Updates a payment record.
     */
    public void updatePayment(int id, double amount, String description) 
            throws InvalidInputException, SQLException {
        
        if (amount <= 0) {
            throw new InvalidInputException("Amount must be greater than 0");
        }
        paymentDAO.updatePayment(id, amount, description);
    }

    /**
     * Validates payment input fields.
     */
    private void validatePaymentInput(String studentId, double amount) 
            throws InvalidInputException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        if (amount <= 0) {
            throw new InvalidInputException("Amount must be greater than 0");
        }
    }
}
