# Design Patterns Documentation

This application implements **8 design patterns** to ensure maintainability, scalability, and code reusability.

## 1. Singleton Pattern

**Type**: Creational Pattern  
**Purpose**: Ensure only one instance of a class exists throughout the application.

### Implementations

#### 1.1 DatabaseConnection
- **Location**: `dao/DatabaseConnection.java`
- **Purpose**: Manages SQLite database connection
- **Thread Safety**: Yes (synchronized getInstance method)

```java
public static synchronized DatabaseConnection getInstance() throws SQLException {
    if (instance == null || instance.connection == null || instance.connection.isClosed()) {
        instance = new DatabaseConnection();
    }
    return instance;
}
```

#### 1.2 SessionManager
- **Location**: `util/SessionManager.java`
- **Purpose**: Tracks logged-in user session
- **Thread Safety**: Yes

#### 1.3 StudentManagementFacade
- **Location**: `facade/StudentManagementFacade.java`
- **Purpose**: Single facade instance for controller access
- **Thread Safety**: Yes

#### 1.4 ApplicationStateContext
- **Location**: `state/ApplicationStateContext.java`
- **Purpose**: Manages application state transitions
- **Thread Safety**: Yes

### Benefits
- Controlled access to shared resources
- Global point of access
- Lazy initialization
- Thread-safe implementation

---

## 2. Facade Pattern

**Type**: Structural Pattern  
**Purpose**: Provide a unified interface to a complex subsystem of controllers.

### Implementation

- **Location**: `facade/StudentManagementFacade.java`
- **Wraps**: StudentController, AttendanceController, PaymentController

### Key Methods

```java
// Unified student operations
addStudent(), getStudent(), updateStudent(), deleteStudent()

// Unified attendance operations
markAttendance(), getStudentAttendance(), getAttendanceRate()

// Unified payment operations
addPayment(), getStudentPayments(), getBalance()

// Combined operations
getCompleteStudentInfo() // Aggregates data from multiple controllers
```

### Benefits
- Simplifies client interaction
- Reduces coupling between subsystems
- Provides a single entry point
- Hides complexity of underlying system

### Usage Example

```java
StudentManagementFacade facade = StudentManagementFacade.getInstance();
facade.addStudent("S001", "John Doe", 20, "CS", "john@example.com");
```

---

## 3. Chain of Responsibility Pattern

**Type**: Behavioral Pattern  
**Purpose**: Pass validation requests along a chain of handlers.

### Implementation

- **Base Handler**: `chain/ValidationHandler.java`
- **Concrete Handlers**:
  - `EmptyValidationHandler.java` - Validates empty/null values
  - `AgeValidationHandler.java` - Validates age range (1-150)
  - `EmailValidationHandler.java` - Validates email format
  - `AmountValidationHandler.java` - Validates positive amounts
  - `StatusValidationHandler.java` - Validates attendance status
- **Builder**: `ValidationChainBuilder.java`

### Chain Structure

```
EmptyValidationHandler → AgeValidationHandler → EmailValidationHandler
```

### Usage Example

```java
ValidationHandler validator = ValidationChainBuilder.buildStudentValidationChain();
validator.validate("studentId", studentId);
validator.validate("age", ageStr);
validator.validate("email", email);
```

### Benefits
- Decouples sender and receiver
- Dynamic chain composition
- Easy to add/remove validators
- Stops at first failure

---

## 4. State Pattern

**Type**: Behavioral Pattern  
**Purpose**: Allow an object to change behavior when its internal state changes.

### Implementation A: Application State

- **Interface**: `state/ApplicationState.java`
- **States**: 
  - `LoginState.java` - Initial state (not logged in)
  - `LoggedInState.java` - Active state (logged in)
- **Context**: `state/ApplicationStateContext.java`

### State Transitions

```
LoginState → (login) → LoggedInState
LoggedInState → (logout) → LoginState
```

### Implementation B: Student Enrollment State

- **Interface**: `state/StudentEnrollmentState.java`
- **States**:
  - `EnrolledState.java` - Active enrollment
  - `SuspendedState.java` - Suspended
  - `GraduatedState.java` - Graduated
- **Context**: `state/StudentEnrollmentContext.java`

### Benefits
- Encapsulates state-specific behavior
- Makes state transitions explicit
- Easy to add new states
- Eliminates conditional logic

---

## 5. Observer Pattern

**Type**: Behavioral Pattern  
**Purpose**: Define a one-to-many dependency between objects so that when one object changes state, all dependents are notified.

### Implementation

- **Observer Interface**: `observer/StudentDataObserver.java`
- **Subject Interface**: `observer/StudentDataSubject.java`
- **Concrete Subject**: `observer/StudentDataManager.java` (Singleton)

### Flow

1. Views register as observers
2. Facade notifies StudentDataManager on data changes
3. StudentDataManager notifies all observers
4. Views automatically refresh

### Usage Example

```java
// Register observer
StudentDataManager.getInstance().addObserver(mainPanel);

// Notify observers
StudentDataManager.getInstance().notifyStudentAdded();
```

### Benefits
- Loose coupling between subject and observers
- Dynamic subscription/unsubscription
- Automatic UI updates
- Supports multiple observers

---

## 6. Strategy Pattern

**Type**: Behavioral Pattern  
**Purpose**: Define a family of algorithms, encapsulate each one, and make them interchangeable.

### Implementation

- **Strategy Interface**: `strategy/SortStrategy.java`
- **Concrete Strategies**:
  - `SortByNameStrategy.java`
  - `SortByAgeStrategy.java`
  - `SortByIdStrategy.java`
  - `SortByCourseStrategy.java`
- **Context**: `strategy/StudentSortContext.java`

### Usage Example

```java
StudentSortContext context = new StudentSortContext();
context.setStrategy(new SortByNameStrategy());
List<Student> sorted = context.sortStudents(students);
```

### Benefits
- Runtime algorithm selection
- Easy to add new sorting strategies
- Eliminates conditional statements
- Promotes code reuse

---

## 7. MVC Pattern

**Type**: Architectural Pattern  
**Purpose**: Separate application logic into three interconnected components.

### Implementation

- **Model**: `model/Student.java`, `model/Attendance.java`, `model/Payment.java`
- **View**: `view/LoginPanel.java`, `view/MainPanel.java`, etc.
- **Controller**: `controller/StudentController.java`, etc.

### Benefits
- Separation of concerns
- Easier maintenance
- Reusable components
- Testability

---

## 8. DAO Pattern

**Type**: Data Access Pattern  
**Purpose**: Abstract database operations from business logic.

### Implementation

- **StudentDAO**: `dao/StudentDAO.java`
- **AttendanceDAO**: `dao/AttendanceDAO.java`
- **PaymentDAO**: `dao/PaymentDAO.java`
- **DatabaseConnection**: `dao/DatabaseConnection.java`

### Benefits
- Database independence
- Centralized data access logic
- Easier testing
- Clean separation of concerns

---

## Pattern Interaction

The patterns work together:

1. **Facade** provides unified interface
2. **Chain of Responsibility** validates before operations
3. **State** controls access based on application state
4. **Observer** notifies views of changes
5. **Strategy** provides flexible sorting
6. **Singleton** ensures single instances
7. **MVC** structures the application
8. **DAO** abstracts database operations

