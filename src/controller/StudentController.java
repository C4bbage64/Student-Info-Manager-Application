package controller;

import dao.StudentDAO;
import model.Student;
import exceptions.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Student operations.
 * Part of MVC architecture - handles business logic between View and DAO.
 */
public class StudentController {
    private final StudentDAO studentDAO;

    public StudentController() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * Adds a new student with validation.
     */
    public void addStudent(String studentId, String name, int age, String course, String email) 
            throws InvalidInputException, DuplicateStudentException, SQLException {
        
        // Validation
        validateStudentInput(studentId, name, age, course);

        Student student = new Student(name, age, studentId, course, email, "ENROLLED");
        studentDAO.create(student);
    }

    /**
     * Gets a student by ID.
     */
    public Student getStudent(String studentId) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        return studentDAO.read(studentId);
    }

    /**
     * Updates an existing student.
     */
    public void updateStudent(String studentId, String name, int age, String course, String email) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        // Validation
        validateStudentInput(studentId, name, age, course);

        // Get existing student to preserve enrollment status
        Student existingStudent = studentDAO.findById(studentId);
        String enrollmentStatus = existingStudent != null ? existingStudent.getEnrollmentStatus() : "ENROLLED";
        Student student = new Student(name, age, studentId, course, email, enrollmentStatus);
        studentDAO.update(student);
    }

    /**
     * Deletes a student.
     */
    public void deleteStudent(String studentId) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        studentDAO.delete(studentId);
    }

    /**
     * Gets all students.
     */
    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.readAll();
    }

    /**
     * Gets all students sorted by criteria.
     */
    public List<Student> getStudentsSorted(String criteria) throws SQLException {
        return studentDAO.readAllSorted(criteria);
    }

    /**
     * Searches for a student by ID.
     */
    public Student searchStudent(String studentId) throws SQLException {
        return studentDAO.findById(studentId);
    }

    /**
     * Searches students by name.
     */
    public List<Student> searchStudentsByName(String name) throws SQLException {
        return studentDAO.searchByName(name);
    }

    /**
     * Checks if a student exists.
     */
    public boolean studentExists(String studentId) throws SQLException {
        return studentDAO.exists(studentId);
    }

    /**
     * Updates enrollment status for a student.
     */
    public void updateEnrollmentStatus(String studentId, String status) 
            throws InvalidInputException, StudentNotFoundException, SQLException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new InvalidInputException("Enrollment status cannot be empty");
        }
        if (!status.equals("ENROLLED") && !status.equals("SUSPENDED") && !status.equals("GRADUATED")) {
            throw new InvalidInputException("Enrollment status must be ENROLLED, SUSPENDED, or GRADUATED");
        }
        
        studentDAO.updateEnrollmentStatus(studentId, status);
    }

    /**
     * Validates student input fields.
     */
    private void validateStudentInput(String studentId, String name, int age, String course) 
            throws InvalidInputException {
        
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }
        if (age < 1 || age > 150) {
            throw new InvalidInputException("Age must be between 1 and 150");
        }
        if (course == null || course.trim().isEmpty()) {
            throw new InvalidInputException("Course cannot be empty");
        }
    }
}
