package exceptions;

/**
 * Exception thrown when an attendance record is not found.
 */
public class AttendanceRecordNotFoundException extends Exception {

    public AttendanceRecordNotFoundException(String studentId) {
        super("No attendance record found for student: " + studentId);
    }

    public AttendanceRecordNotFoundException(String studentId, String date) {
        super("No attendance record found for student '" + studentId + "' on " + date);
    }
}
