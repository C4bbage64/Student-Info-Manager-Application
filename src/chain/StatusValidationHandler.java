package chain;

import exceptions.InvalidInputException;

/**
 * Concrete handler: Validates attendance status (PRESENT or ABSENT).
 */
public class StatusValidationHandler extends ValidationHandler {
    @Override
    public void validate(String fieldName, String value) throws InvalidInputException {
        if (fieldName.equalsIgnoreCase("status")) {
            if (!value.equals("PRESENT") && !value.equals("ABSENT")) {
                throw new InvalidInputException("Status must be PRESENT or ABSENT");
            }
        }
        validateNext(fieldName, value);
    }
}

