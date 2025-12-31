package chain;

/**
 * Builder for creating validation chains.
 * Provides convenient methods to build different validation chains.
 */
public class ValidationChainBuilder {
    
    /**
     * Builds a validation chain for student input.
     * Validates: empty values, age range, email format.
     */
    public static ValidationHandler buildStudentValidationChain() {
        ValidationHandler chain = new EmptyValidationHandler();
        chain.setNext(new AgeValidationHandler())
             .setNext(new EmailValidationHandler());
        return chain;
    }
    
    /**
     * Builds a validation chain for payment input.
     * Validates: empty values, positive amounts.
     */
    public static ValidationHandler buildPaymentValidationChain() {
        ValidationHandler chain = new EmptyValidationHandler();
        chain.setNext(new AmountValidationHandler());
        return chain;
    }
    
    /**
     * Builds a validation chain for attendance input.
     * Validates: empty values, status (PRESENT/ABSENT).
     */
    public static ValidationHandler buildAttendanceValidationChain() {
        ValidationHandler chain = new EmptyValidationHandler();
        chain.setNext(new StatusValidationHandler());
        return chain;
    }
    
    /**
     * Builds a general validation chain (empty check only).
     */
    public static ValidationHandler buildGeneralValidationChain() {
        return new EmptyValidationHandler();
    }
}

