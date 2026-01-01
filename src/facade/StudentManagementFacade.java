package facade;

import controller.AttendanceController;
import controller.PaymentController;
import controller.StudentController;
import exceptions.*;
import java.sql.SQLException;
import java.util.List;
import model.Attendance;
import model.Payment;
import model.Student;
import observer.StudentDataManager;
import state.EnrolledState;
import state.GraduatedState;
import state.StudentEnrollmentContext;
import state.SuspendedState;

/**
 * Facade pattern implementation.
 * Provides a unified interface to the subsystem of controllers.
 * Simplifies client interaction with Student, Attendance, and Payment operations.
 * Uses Singleton pattern to ensure single instance.
 */
public class StudentManagementFacade {
    private StudentController studentController;
    private AttendanceController attendanceController;
    private PaymentController paymentController;
    
    // Singleton instance
    private static StudentManagementFacade instance;
    
    private StudentManagementFacade() {
        this.studentController = new StudentController();
        this.attendanceController = new AttendanceController();
        this.paymentController = new PaymentController();
    }
    
    /**
     * Gets the singleton instance of StudentManagementFacade.
     * Thread-safe implementation.
     */
    public static synchronized StudentManagementFacade getInstance() {
        if (instance == null) {
            instance = new StudentManagementFacade();
        }
        return instance;
    }
    
    // ========== Student Operations ==========
    
    /**
     * Adds a new student with validation.
     * Observer Pattern: Notifies observers after successful addition.
     */
    public void addStudent(String studentId, String name, int age, String course, String email) 
            throws InvalidInputException, DuplicateStudentException, SQLException {
        studentController.addStudent(studentId, name, age, course, email);
        // Observer Pattern: Notify observers of data change
        StudentDataManager.getInstance().notifyStudentAdded();
    }
    
    /**
     * Gets a student by ID.
     */
    public Student getStudent(String studentId) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        return studentController.getStudent(studentId);
    }
    
    /**
     * Updates an existing student.
     * Observer Pattern: Notifies observers after successful update.
     */
    public void updateStudent(String studentId, String name, int age, String course, String email) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        studentController.updateStudent(studentId, name, age, course, email);
        // Observer Pattern: Notify observers of data change
        StudentDataManager.getInstance().notifyStudentUpdated();
    }
    
    /**
     * Deletes a student.
     * Observer Pattern: Notifies observers after successful deletion.
     */
    public void deleteStudent(String studentId) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        studentController.deleteStudent(studentId);
        // Observer Pattern: Notify observers of data change
        StudentDataManager.getInstance().notifyStudentDeleted();
    }
    
    /**
     * Gets all students.
     */
    public List<Student> getAllStudents() throws SQLException {
        return studentController.getAllStudents();
    }
    
    /**
     * Searches for a student by ID.
     */
    public Student searchStudent(String studentId) throws SQLException {
        return studentController.searchStudent(studentId);
    }
    
    /**
     * Searches students by name.
     */
    public List<Student> searchStudentsByName(String name) throws SQLException {
        return studentController.searchStudentsByName(name);
    }
    
    /**
     * Updates enrollment status for a student.
     * Uses State Pattern to manage enrollment state transitions.
     * Observer Pattern: Notifies observers after successful update.
     */
    public void updateEnrollmentStatus(String studentId, String status) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        // Get current student to determine current state
        Student student = studentController.getStudent(studentId);
        StudentEnrollmentContext context = new StudentEnrollmentContext();
        
        // Set initial state based on current enrollment status
        String currentStatus = student.getEnrollmentStatus();
        if ("SUSPENDED".equals(currentStatus)) {
            context.setState(new SuspendedState());
        } else if ("GRADUATED".equals(currentStatus)) {
            context.setState(new GraduatedState());
        } else {
            context.setState(new EnrolledState());
        }
        
        // Perform state transition based on new status
        switch (status) {
            case "ENROLLED":
                if (context.isSuspended()) {
                    context.activate();
                } else if (context.isGraduated()) {
                    context.enroll();
                }
                break;
            case "SUSPENDED":
                if (context.isEnrolled()) {
                    context.suspend();
                }
                break;
            case "GRADUATED":
                if (context.isEnrolled()) {
                    context.graduate();
                }
                break;
        }
        
        // Update database
        studentController.updateEnrollmentStatus(studentId, status);
        
        // Observer Pattern: Notify observers of data change
        StudentDataManager.getInstance().notifyStudentUpdated();
    }

    // ========== Attendance Operations ==========
    
    /**
     * Marks attendance for a student (today's date).
     */
    public void markAttendance(String studentId, String status) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        attendanceController.markAttendance(studentId, status);
    }
    
    /**
     * Marks attendance for a student on a specific date.
     */
    public void markAttendance(String studentId, String date, String status) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        attendanceController.markAttendance(studentId, date, status);
    }
    
    /**
     * Gets attendance records for a student.
     */
    public List<Attendance> getStudentAttendance(String studentId) 
            throws InvalidInputException, AttendanceRecordNotFoundException, SQLException {
        return attendanceController.getStudentAttendance(studentId);
    }
    
    /**
     * Gets all attendance records.
     */
    public List<Attendance> getAllAttendance() throws SQLException {
        return attendanceController.getAllAttendance();
    }
    
    /**
     * Gets attendance records for a specific date.
     */
    public List<Attendance> getAttendanceByDate(String date) 
            throws InvalidInputException, SQLException {
        return attendanceController.getAttendanceByDate(date);
    }
    
    /**
     * Calculates attendance rate for a student.
     */
    public double getAttendanceRate(String studentId) 
            throws InvalidInputException, SQLException {
        return attendanceController.getAttendanceRate(studentId);
    }
    
    /**
     * Deletes an attendance record.
     */
    public void deleteAttendance(int id) throws SQLException {
        attendanceController.deleteAttendance(id);
    }
    
    /**
     * Updates an attendance record status.
     */
    public void updateAttendance(int id, String status) 
            throws InvalidInputException, SQLException {
        attendanceController.updateAttendance(id, status);
    }
    
    // ========== Payment Operations ==========
    
    /**
     * Adds a new payment (today's date).
     */
    public void addPayment(String studentId, double amount, String description) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        paymentController.addPayment(studentId, amount, description);
    }
    
    /**
     * Adds a new payment on a specific date.
     */
    public void addPayment(String studentId, double amount, String date, String description) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        paymentController.addPayment(studentId, amount, date, description);
    }
    
    /**
     * Gets payment records for a student.
     */
    public List<Payment> getStudentPayments(String studentId) 
            throws InvalidInputException, PaymentNotFoundException, SQLException {
        return paymentController.getStudentPayments(studentId);
    }
    
    /**
     * Gets all payment records.
     */
    public List<Payment> getAllPayments() throws SQLException {
        return paymentController.getAllPayments();
    }
    
    /**
     * Calculates total amount paid by a student.
     */
    public double getTotalPaid(String studentId) 
            throws InvalidInputException, SQLException {
        return paymentController.getTotalPaid(studentId);
    }
    
    /**
     * Calculates outstanding balance for a student (default fees).
     */
    public double getBalance(String studentId) 
            throws InvalidInputException, SQLException {
        return paymentController.getBalance(studentId);
    }
    
    /**
     * Calculates outstanding balance with custom total fees.
     */
    public double getBalance(String studentId, double totalFees) 
            throws InvalidInputException, SQLException {
        return paymentController.getBalance(studentId, totalFees);
    }
    
    /**
     * Gets a payment by ID.
     */
    public Payment getPayment(int id) 
            throws PaymentNotFoundException, SQLException {
        return paymentController.getPayment(id);
    }
    
    /**
     * Deletes a payment record.
     */
    public void deletePayment(int id) throws SQLException {
        paymentController.deletePayment(id);
    }
    
    /**
     * Updates a payment record.
     */
    public void updatePayment(int id, double amount, String description) 
            throws InvalidInputException, SQLException {
        paymentController.updatePayment(id, amount, description);
    }
    
    // ========== Combined Operations (Facade Benefits) ==========
    
    /**
     * Gets complete student information including attendance rate and payment details.
     * This demonstrates the facade pattern benefit - combining multiple operations.
     */
    public StudentInfo getCompleteStudentInfo(String studentId) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        Student student = studentController.getStudent(studentId);
        double attendanceRate = 0.0;
        double totalPaid = 0.0;
        double balance = 0.0;
        
        try {
            attendanceRate = attendanceController.getAttendanceRate(studentId);
        } catch (Exception e) {
            // If no attendance records, rate remains 0.0
        }
        
        try {
            totalPaid = paymentController.getTotalPaid(studentId);
            balance = paymentController.getBalance(studentId);
        } catch (Exception e) {
            // If no payment records, amounts remain 0.0
        }
        
        return new StudentInfo(student, attendanceRate, totalPaid, balance);
    }
    
    /**
     * Inner class to hold combined student information.
     * Part of the facade pattern - provides aggregated data structure.
     */
    public static class StudentInfo {
        private Student student;
        private double attendanceRate;
        private double totalPaid;
        private double balance;
        
        public StudentInfo(Student student, double attendanceRate, double totalPaid, double balance) {
            this.student = student;
            this.attendanceRate = attendanceRate;
            this.totalPaid = totalPaid;
            this.balance = balance;
        }
        
        // Getters
        public Student getStudent() {
            return student;
        }
        
        public double getAttendanceRate() {
            return attendanceRate;
        }
        
        public double getTotalPaid() {
            return totalPaid;
        }
        
        public double getBalance() {
            return balance;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Student: %s\nAttendance Rate: %.2f%%\nTotal Paid: $%.2f\nBalance: $%.2f",
                student.toString(), attendanceRate, totalPaid, balance
            );
        }
    }
}

