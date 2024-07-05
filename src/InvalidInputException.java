// Custom exception class to handle invalid input scenarios
public class InvalidInputException extends Exception {

    // Constructor that accepts a String message
    public InvalidInputException(String message) {
        // Calls the constructor of the parent class (Exception) and passes the message to it
        super(message);
    }
}
