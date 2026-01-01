package dao;

import model.Student;
import exceptions.DuplicateStudentException;
import exceptions.StudentNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student entity.
 * Part of the DAO pattern - handles all database operations for students.
 */
public class StudentDAO {

    /**
     * Creates a new student in the database.
     */
    public void create(Student student) throws SQLException, DuplicateStudentException {
        // Check if student already exists
        if (findById(student.getStudentId()) != null) {
            throw new DuplicateStudentException(student.getStudentId());
        }

        String sql = "INSERT INTO students (student_id, name, age, course, email, enrollment_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getName());
            stmt.setInt(3, student.getAge());
            stmt.setString(4, student.getCourse());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getEnrollmentStatus());
            stmt.executeUpdate();
        }
    }

    /**
     * Reads a student by their ID.
     */
    public Student read(String studentId) throws SQLException, StudentNotFoundException {
        Student student = findById(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        return student;
    }

    /**
     * Updates an existing student.
     */
    public void update(Student student) throws SQLException, StudentNotFoundException {
        // Check if student exists
        if (findById(student.getStudentId()) == null) {
            throw new StudentNotFoundException(student.getStudentId(), "cannot update");
        }

        String sql = "UPDATE students SET name = ?, age = ?, course = ?, email = ?, enrollment_status = ? WHERE student_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setString(3, student.getCourse());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getEnrollmentStatus());
            stmt.setString(6, student.getStudentId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a student by their ID.
     */
    public void delete(String studentId) throws SQLException, StudentNotFoundException {
        // Check if student exists
        if (findById(studentId) == null) {
            throw new StudentNotFoundException(studentId, "cannot delete");
        }

        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all students from the database.
     */
    public List<Student> readAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }

    /**
     * Searches for a student by ID (returns null if not found).
     */
    public Student findById(String studentId) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        }
        return null;
    }

    /**
     * Searches for students by name (partial match).
     */
    public List<Student> searchByName(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }

    /**
     * Updates enrollment status for a student.
     */
    public void updateEnrollmentStatus(String studentId, String status) throws SQLException, StudentNotFoundException {
        // Check if student exists
        if (findById(studentId) == null) {
            throw new StudentNotFoundException(studentId, "cannot update enrollment status");
        }

        String sql = "UPDATE students SET enrollment_status = ? WHERE student_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, studentId);
            stmt.executeUpdate();
        }
    }

    /**
     * Helper method to map ResultSet to Student object.
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        String enrollmentStatus = null;
        try {
            enrollmentStatus = rs.getString("enrollment_status");
        } catch (SQLException e) {
            // Column might not exist in older databases, default to ENROLLED
            enrollmentStatus = "ENROLLED";
        }
        
        return new Student(
            rs.getString("name"),
            rs.getInt("age"),
            rs.getString("student_id"),
            rs.getString("course"),
            rs.getString("email"),
            enrollmentStatus
        );
    }
}
