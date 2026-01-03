package util;

import dao.StudentDAO;
import java.sql.SQLException;

/**
 * Utility class for auto-generating Student IDs.
 * Generates IDs in format STU001, STU002, etc.
 */
public class StudentIdGenerator {
    private static final String PREFIX = "STU";
    
    /**
     * Generates the next available Student ID in format: STU001, STU002, etc.
     * Thread-safe implementation to prevent duplicate IDs.
     * 
     * @return Next available Student ID (e.g., "STU001")
     * @throws SQLException if database query fails
     */
    public static synchronized String generateNextId() throws SQLException {
        StudentDAO dao = new StudentDAO();
        int maxNumber = dao.getMaxStudentIdNumber();
        int nextNumber = maxNumber + 1;
        return String.format("%s%03d", PREFIX, nextNumber);
    }
}

