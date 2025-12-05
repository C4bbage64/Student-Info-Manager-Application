package exceptions;

/**
 * Exception thrown when a payment record is not found.
 */
public class PaymentNotFoundException extends Exception {

    public PaymentNotFoundException(String studentId) {
        super("No payment record found for student: " + studentId);
    }

    public PaymentNotFoundException(int paymentId) {
        super("Payment with ID '" + paymentId + "' not found");
    }
}
