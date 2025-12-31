package chain;

import exceptions.InvalidInputException;

/**
 * Abstract handler for Chain of Responsibility pattern.
 * Each handler validates a specific aspect of input.
 */
public abstract class ValidationHandler {
    protected ValidationHandler nextHandler;
    
    /**
     * Sets the next handler in the chain.
     * Returns the next handler for method chaining.
     */
    public ValidationHandler setNext(ValidationHandler handler) {
        this.nextHandler = handler;
        return handler;
    }
    
    /**
     * Validates the given field value.
     * Subclasses should implement specific validation logic.
     * 
     * @param fieldName The name of the field being validated
     * @param value The value to validate
     * @throws InvalidInputException if validation fails
     */
    public abstract void validate(String fieldName, String value) throws InvalidInputException;
    
    /**
     * Passes validation to the next handler in the chain.
     * Called after current handler's validation succeeds.
     */
    protected void validateNext(String fieldName, String value) throws InvalidInputException {
        if (nextHandler != null) {
            nextHandler.validate(fieldName, value);
        }
    }
}

