package chain;

import exceptions.InvalidInputException;

/**
 * Concrete handler: Validates age range (1-150).
 */
public class AgeValidationHandler extends ValidationHandler {
    @Override
    public void validate(String fieldName, String value) throws InvalidInputException {
        if (fieldName.equalsIgnoreCase("age")) {
            try {
                int age = Integer.parseInt(value);
                if (age < 1 || age > 150) {
                    throw new InvalidInputException("Age must be between 1 and 150");
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Age must be a valid number");
            }
        }
        validateNext(fieldName, value);
    }
}

