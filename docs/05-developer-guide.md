# Developer Guide

## Project Structure

```
StudentInformationApplication/
├── src/
│   ├── model/          # Entity classes
│   ├── view/           # UI components
│   ├── controller/     # Business logic
│   ├── dao/            # Data access objects
│   ├── facade/         # Facade pattern
│   ├── chain/          # Chain of Responsibility
│   ├── state/          # State pattern
│   ├── observer/       # Observer pattern
│   ├── strategy/       # Strategy pattern
│   ├── exceptions/     # Custom exceptions
│   └── util/           # Utilities
├── lib/                # External JARs
├── docs/               # Documentation
└── student_app.db      # SQLite database
```

## Setting Up Development Environment

### Prerequisites

1. **JDK 8 or higher**
   ```bash
   java -version
   ```

2. **IDE** (IntelliJ IDEA recommended)
   - Import project as existing project
   - Configure JDK

3. **Dependencies**
   - sqlite-jdbc-3.45.1.0.jar (included)
   - slf4j-api-2.0.9.jar (included)
   - slf4j-simple-2.0.9.jar (included)

### Building the Project

```bash
# Compile all Java files
javac -cp "lib/*" -d out src/**/*.java

# Run the application
java -cp "out:lib/*" StudentInfoApp
```

## Code Conventions

### Naming Conventions

- **Classes**: PascalCase (e.g., `StudentController`)
- **Methods**: camelCase (e.g., `addStudent()`)
- **Variables**: camelCase (e.g., `studentId`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `DB_URL`)
- **Packages**: lowercase (e.g., `dao`, `model`)

### Package Organization

- Each design pattern has its own package
- Related classes grouped together
- Clear separation of concerns

## Adding New Features

### Adding a New Validation Handler

1. Create new handler class extending `ValidationHandler`
2. Implement `validate()` method
3. Add to `ValidationChainBuilder`

```java
public class CustomValidationHandler extends ValidationHandler {
    @Override
    public void validate(String fieldName, String value) throws InvalidInputException {
        // Validation logic
        validateNext(fieldName, value);
    }
}
```

### Adding a New Sort Strategy

1. Implement `SortStrategy` interface
2. Add to `StudentSortContext.getStrategyByName()`

```java
public class CustomSortStrategy implements SortStrategy {
    @Override
    public List<Student> sort(List<Student> students) {
        // Sorting logic
    }
}
```

### Adding a New Observer

1. Implement `StudentDataObserver` interface
2. Register with `StudentDataManager`

```java
public class CustomObserver implements StudentDataObserver {
    @Override
    public void onStudentDataChanged(String eventType) {
        // Handle notification
    }
}
```

## Database Schema

See [Database Schema Documentation](09-database-schema.md) for details.

## Exception Handling

### Custom Exceptions

- `InvalidInputException`: Invalid user input
- `StudentNotFoundException`: Student not found
- `DuplicateStudentException`: Duplicate student ID
- `AttendanceRecordNotFoundException`: Attendance record not found
- `PaymentNotFoundException`: Payment record not found
- `FileOperationException`: File operation errors

### Best Practices

1. Always validate input before processing
2. Use specific exceptions, not generic ones
3. Provide helpful error messages
4. Log errors appropriately

## Testing

See [Testing Guide](08-testing.md) for testing procedures.

## Debugging

### Common Issues

1. **Database Connection Errors**
   - Check SQLite JDBC driver in classpath
   - Verify database file exists
   - Check file permissions

2. **State Pattern Issues**
   - Verify ApplicationStateContext is initialized
   - Check state transitions

3. **Observer Not Updating**
   - Verify observer is registered
   - Check notification calls

## Performance Considerations

1. **Database Queries**: Use prepared statements
2. **UI Updates**: Use SwingUtilities.invokeLater() for thread safety
3. **Memory**: Close database connections properly
4. **Validation**: Chain stops at first failure

## Future Enhancements

1. Add more sorting strategies
2. Implement report generation
3. Add data export functionality
4. Implement user roles and permissions
5. Add audit logging
6. Implement backup/restore functionality

