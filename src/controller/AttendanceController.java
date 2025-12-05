package controller;

import dao.AttendanceDAO;
import model.Attendance;
import exceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for Attendance operations.
 * Part of MVC architecture - handles business logic between View and DAO.
 */
public class AttendanceController {
    private final AttendanceDAO attendanceDAO;

    public AttendanceController() {
        this.attendanceDAO = new AttendanceDAO();
    }

    /**
     * Marks attendance for a student with validation.
     */
    public void markAttendance(String studentId, String status) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        // Validation
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        if (!status.equals("PRESENT") && !status.equals("ABSENT")) {
            throw new InvalidInputException("Status must be PRESENT or ABSENT");
        }

        String today = LocalDate.now().toString();
        attendanceDAO.markAttendance(studentId, today, status);
    }

    /**
     * Marks attendance for a student on a specific date.
     */
    public void markAttendance(String studentId, String date, String status) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        // Validation
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        if (date == null || date.trim().isEmpty()) {
            throw new InvalidInputException("Date cannot be empty");
        }
        if (!status.equals("PRESENT") && !status.equals("ABSENT")) {
            throw new InvalidInputException("Status must be PRESENT or ABSENT");
        }

        attendanceDAO.markAttendance(studentId, date, status);
    }

    /**
     * Gets attendance records for a student.
     */
    public List<Attendance> getStudentAttendance(String studentId) 
            throws InvalidInputException, AttendanceRecordNotFoundException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        return attendanceDAO.getAttendanceByStudent(studentId);
    }

    /**
     * Gets all attendance records.
     */
    public List<Attendance> getAllAttendance() throws SQLException {
        return attendanceDAO.getAllAttendance();
    }

    /**
     * Gets attendance records for a specific date.
     */
    public List<Attendance> getAttendanceByDate(String date) 
            throws InvalidInputException, SQLException {
        
        if (date == null || date.trim().isEmpty()) {
            throw new InvalidInputException("Date cannot be empty");
        }
        return attendanceDAO.getAttendanceByDate(date);
    }

    /**
     * Calculates attendance rate for a student.
     */
    public double getAttendanceRate(String studentId) 
            throws InvalidInputException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        return attendanceDAO.calculateAttendanceRate(studentId);
    }

    /**
     * Deletes an attendance record.
     */
    public void deleteAttendance(int id) throws SQLException {
        attendanceDAO.deleteAttendance(id);
    }

    /**
     * Updates an attendance record status.
     */
    public void updateAttendance(int id, String status) 
            throws InvalidInputException, SQLException {
        
        if (!status.equals("PRESENT") && !status.equals("ABSENT")) {
            throw new InvalidInputException("Status must be PRESENT or ABSENT");
        }
        attendanceDAO.updateAttendance(id, status);
    }
}
