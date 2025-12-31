package chain;

import exceptions.InvalidInputException;
import java.util.regex.Pattern;

/**
 * Concrete handler: Validates email format using regex.
 */
public class EmailValidationHandler extends ValidationHandler {
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    @Override
    public void validate(String fieldName, String value) throws InvalidInputException {
        if (fieldName.equalsIgnoreCase("email") && value != null && !value.trim().isEmpty()) {
            if (!pattern.matcher(value).matches()) {
                throw new InvalidInputException("Invalid email format");
            }
        }
        validateNext(fieldName, value);
    }
}

