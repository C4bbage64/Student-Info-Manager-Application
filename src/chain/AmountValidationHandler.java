package chain;

import exceptions.InvalidInputException;

/**
 * Concrete handler: Validates amount (must be positive).
 */
public class AmountValidationHandler extends ValidationHandler {
    @Override
    public void validate(String fieldName, String value) throws InvalidInputException {
        if (fieldName.equalsIgnoreCase("amount")) {
            try {
                double amount = Double.parseDouble(value);
                if (amount <= 0) {
                    throw new InvalidInputException("Amount must be greater than 0");
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Amount must be a valid number");
            }
        }
        validateNext(fieldName, value);
    }
}

