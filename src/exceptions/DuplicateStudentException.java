package exceptions;

/**
 * Exception thrown when attempting to add a student with an ID that already exists.
 */
public class DuplicateStudentException extends Exception {

    public DuplicateStudentException(String studentId) {
        super("Student with ID '" + studentId + "' already exists");
    }
}
