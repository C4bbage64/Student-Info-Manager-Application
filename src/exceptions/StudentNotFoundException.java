package exceptions;

/**
 * Exception thrown when a student is not found in the system.
 */
public class StudentNotFoundException extends Exception {

    public StudentNotFoundException(String studentId) {
        super("Student with ID '" + studentId + "' not found");
    }

    public StudentNotFoundException(String studentId, String context) {
        super("Student with ID '" + studentId + "' not found" + (context != null ? " - " + context : ""));
    }
}
