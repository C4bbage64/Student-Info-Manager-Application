# API Reference

## Facade API

### StudentManagementFacade

Main entry point for all operations.

#### Student Operations

```java
// Add student
void addStudent(String studentId, String name, int age, String course, String email)
    throws InvalidInputException, DuplicateStudentException, SQLException

// Get student
Student getStudent(String studentId)
    throws InvalidInputException, StudentNotFoundException, SQLException

// Update student
void updateStudent(String studentId, String name, int age, String course, String email)
    throws InvalidInputException, StudentNotFoundException, SQLException

// Delete student
void deleteStudent(String studentId)
    throws InvalidInputException, StudentNotFoundException, SQLException

// Get all students
List<Student> getAllStudents() throws SQLException

// Get sorted students
List<Student> getStudentsSorted(String criteria) throws SQLException

// Search student
Student searchStudent(String studentId) throws SQLException

// Check if student exists
boolean studentExists(String studentId) throws SQLException
```

#### Attendance Operations

```java
// Mark attendance (today)
void markAttendance(String studentId, String status)
    throws InvalidInputException, StudentNotFoundException, SQLException

// Mark attendance (specific date)
void markAttendance(String studentId, String date, String status)
    throws InvalidInputException, StudentNotFoundException, SQLException

// Get student attendance
List<Attendance> getStudentAttendance(String studentId)
    throws InvalidInputException, AttendanceRecordNotFoundException, SQLException

// Get all attendance
List<Attendance> getAllAttendance() throws SQLException

// Get attendance rate
double getAttendanceRate(String studentId)
    throws InvalidInputException, SQLException
```

#### Payment Operations

```java
// Add payment
void addPayment(String studentId, double amount, String date, String description)
    throws InvalidInputException, StudentNotFoundException, SQLException

// Get student payments
List<Payment> getStudentPayments(String studentId)
    throws InvalidInputException, PaymentNotFoundException, SQLException

// Get total paid
double getTotalPaid(String studentId)
    throws InvalidInputException, SQLException

// Get balance
double getBalance(String studentId, double totalFees)
    throws InvalidInputException, SQLException
```

## Validation Chain API

### ValidationChainBuilder

```java
// Build student validation chain
static ValidationHandler buildStudentValidationChain()

// Build payment validation chain
static ValidationHandler buildPaymentValidationChain()

// Build attendance validation chain
static ValidationHandler buildAttendanceValidationChain()

// Build general validation chain
static ValidationHandler buildGeneralValidationChain()
```

### ValidationHandler

```java
// Set next handler
ValidationHandler setNext(ValidationHandler handler)

// Validate value
void validate(String fieldName, String value) throws InvalidInputException
```

## State Pattern API

### ApplicationStateContext

```java
// Get instance
static ApplicationStateContext getInstance()

// Set state
void setState(ApplicationState state)

// Get current state
ApplicationState getCurrentState()

// Check if logged in
boolean isLoggedIn()
```

### StudentEnrollmentContext

```java
// Create context
StudentEnrollmentContext()

// Set state
void setState(StudentEnrollmentState state)

// Get status
String getStatus()

// State operations
void enroll()
void suspend()
void graduate()
void activate()
```

## Strategy Pattern API

### StudentSortContext

```java
// Create context
StudentSortContext()

// Set strategy
void setStrategy(SortStrategy strategy)

// Sort students
List<Student> sortStudents(List<Student> students)

// Get strategy by name
static SortStrategy getStrategyByName(String name)
```

### SortStrategy

```java
// Sort students
List<Student> sort(List<Student> students)

// Get strategy name
String getStrategyName()
```

## Observer Pattern API

### StudentDataManager

```java
// Get instance
static StudentDataManager getInstance()

// Add observer
void addObserver(StudentDataObserver observer)

// Remove observer
void removeObserver(StudentDataObserver observer)

// Notify observers
void notifyObservers(String eventType)

// Convenience methods
void notifyStudentAdded()
void notifyStudentUpdated()
void notifyStudentDeleted()
```

### StudentDataObserver

```java
// Handle data change
void onStudentDataChanged(String eventType)
```

## Model Classes

### Student

```java
// Constructors
Student()
Student(String name, int age, String studentId, String course)
Student(String name, int age, String studentId, String course, String email)

// Getters
String getStudentId()
String getName()
int getAge()
String getCourse()
String getEmail()

// Setters
void setStudentId(String studentId)
void setName(String name)
void setAge(int age)
void setCourse(String course)
void setEmail(String email)
```

### Attendance

```java
// Constructors
Attendance()
Attendance(int id, String studentId, String date, String status)
Attendance(String studentId, String date, String status)

// Getters/Setters
int getId()
String getStudentId()
String getDate()
String getStatus()
```

### Payment

```java
// Constructors
Payment()
Payment(int id, String studentId, double amount, String date, String description)
Payment(String studentId, double amount, String date, String description)

// Getters/Setters
int getId()
String getStudentId()
double getAmount()
String getDate()
String getDescription()
```

