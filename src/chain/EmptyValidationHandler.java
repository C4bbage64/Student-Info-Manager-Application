package chain;

import exceptions.InvalidInputException;

/**
 * Concrete handler: Validates empty/null values.
 * First handler in most validation chains.
 */
public class EmptyValidationHandler extends ValidationHandler {
    @Override
    public void validate(String fieldName, String value) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
        validateNext(fieldName, value);
    }
}

