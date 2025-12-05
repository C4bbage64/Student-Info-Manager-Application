package exceptions;

/**
 * Exception thrown when a file operation fails.
 */
public class FileOperationException extends Exception {

    public FileOperationException(String operation, String filename) {
        super("Failed to " + operation + " file: " + filename);
    }

    public FileOperationException(String operation, String filename, Throwable cause) {
        super("Failed to " + operation + " file: " + filename, cause);
    }
}
