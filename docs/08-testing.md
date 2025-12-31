# Testing Guide

## Test Cases

### Authentication Tests

| Test ID | Input | Expected Output | Status |
|---------|-------|----------------|--------|
| AUTH-001 | Valid username and password | Login successful, main window displayed | ✅ Pass |
| AUTH-002 | Invalid username | Error: "Invalid username or password" | ✅ Pass |
| AUTH-003 | Invalid password | Error: "Invalid username or password" | ✅ Pass |
| AUTH-004 | Empty username | Error: "Invalid username or password" | ✅ Pass |
| AUTH-005 | Empty password | Error: "Invalid username or password" | ✅ Pass |

### Student Management Tests

| Test ID | Input | Expected Output | Status |
|---------|-------|----------------|--------|
| STU-001 | Valid student data (all fields) | Student added successfully | ✅ Pass |
| STU-002 | Student ID: "S001", Name: "John", Age: 20, Course: "CS" | Student added, table refreshes | ✅ Pass |
| STU-003 | Duplicate Student ID | Error: "Student already exists" | ✅ Pass |
| STU-004 | Empty Student ID | Validation Error: "Student ID cannot be empty" | ✅ Pass |
| STU-005 | Age: 0 | Validation Error: "Age must be between 1 and 150" | ✅ Pass |
| STU-006 | Age: 200 | Validation Error: "Age must be between 1 and 150" | ✅ Pass |
| STU-007 | Invalid email format | Validation Error: "Invalid email format" | ✅ Pass |
| STU-008 | Edit existing student | Student updated successfully | ✅ Pass |
| STU-009 | Edit non-existent student | Error: "Student not found" | ✅ Pass |
| STU-010 | Delete existing student | Student deleted, confirmation dialog | ✅ Pass |
| STU-011 | Delete non-existent student | Error: "Student not found" | ✅ Pass |

### Attendance Tests

| Test ID | Input | Expected Output | Status |
|---------|-------|----------------|--------|
| ATT-001 | Valid attendance (PRESENT) | Attendance marked successfully | ✅ Pass |
| ATT-002 | Valid attendance (ABSENT) | Attendance marked successfully | ✅ Pass |
| ATT-003 | Invalid status | Validation Error: "Status must be PRESENT or ABSENT" | ✅ Pass |
| ATT-004 | Non-existent student ID | Error: "Student not found" | ✅ Pass |
| ATT-005 | Calculate attendance rate | Rate displayed (0-100%) | ✅ Pass |
| ATT-006 | View attendance for student | Attendance records displayed | ✅ Pass |

### Payment Tests

| Test ID | Input | Expected Output | Status |
|---------|-------|----------------|--------|
| PAY-001 | Valid payment (amount > 0) | Payment added successfully | ✅ Pass |
| PAY-002 | Amount: 0 | Validation Error: "Amount must be greater than 0" | ✅ Pass |
| PAY-003 | Amount: -100 | Validation Error: "Amount must be greater than 0" | ✅ Pass |
| PAY-004 | Non-existent student ID | Error: "Student not found" | ✅ Pass |
| PAY-005 | Check balance | Balance calculated correctly | ✅ Pass |
| PAY-006 | View payments | Payment history displayed | ✅ Pass |

### Sorting Tests (Strategy Pattern)

| Test ID | Input | Expected Output | Status |
|---------|-------|----------------|--------|
| SORT-001 | Sort by Student ID | Students sorted by ID | ✅ Pass |
| SORT-002 | Sort by Name | Students sorted alphabetically by name | ✅ Pass |
| SORT-003 | Sort by Age | Students sorted by age (ascending) | ✅ Pass |
| SORT-004 | Sort by Course | Students sorted by course | ✅ Pass |

### Observer Pattern Tests

| Test ID | Action | Expected Output | Status |
|---------|--------|----------------|--------|
| OBS-001 | Add student | Student table auto-refreshes | ✅ Pass |
| OBS-002 | Update student | Student table auto-refreshes | ✅ Pass |
| OBS-003 | Delete student | Student table auto-refreshes | ✅ Pass |

### State Pattern Tests

| Test ID | Action | Expected Output | Status |
|---------|--------|----------------|--------|
| STATE-001 | Login | State changes to LoggedInState | ✅ Pass |
| STATE-002 | Perform operation without login | Access denied message | ✅ Pass |
| STATE-003 | After login | Operations allowed | ✅ Pass |

## Test Data

### Sample Students

```
Student ID: S001
Name: John Doe
Age: 20
Course: Computer Science
Email: john.doe@example.com

Student ID: S002
Name: Jane Smith
Age: 21
Course: Information Technology
Email: jane.smith@example.com

Student ID: S003
Name: Bob Johnson
Age: 19
Course: Computer Science
Email: bob.johnson@example.com
```

### Sample Attendance

```
Student ID: S001
Date: 2024-01-15
Status: PRESENT

Student ID: S001
Date: 2024-01-16
Status: PRESENT

Student ID: S001
Date: 2024-01-17
Status: ABSENT
```

### Sample Payments

```
Student ID: S001
Amount: 1000.00
Date: 2024-01-10
Description: Tuition Fee - First Installment

Student ID: S001
Amount: 500.00
Date: 2024-02-10
Description: Tuition Fee - Second Installment
```

## Testing Procedures

### 1. Unit Testing (Manual)

Test individual components:

```java
// Example: Test validation handler
ValidationHandler validator = ValidationChainBuilder.buildStudentValidationChain();
try {
    validator.validate("age", "25");
    // Should pass
} catch (InvalidInputException e) {
    // Test failed
}
```

### 2. Integration Testing

Test component interactions:

1. Login → Add Student → View Students
2. Add Student → Mark Attendance → View Attendance
3. Add Payment → Check Balance

### 3. System Testing

Test complete workflows:

1. **Student Management Workflow**
   - Login
   - Add student
   - Edit student
   - View students
   - Delete student

2. **Attendance Workflow**
   - Mark attendance
   - View attendance
   - Calculate attendance rate

3. **Payment Workflow**
   - Add payment
   - View payments
   - Check balance

## Test Coverage

### Design Pattern Testing

- ✅ Singleton: Verify single instance
- ✅ Facade: Test unified interface
- ✅ Chain of Responsibility: Test validation chain
- ✅ State: Test state transitions
- ✅ Observer: Test notification system
- ✅ Strategy: Test sorting strategies
- ✅ MVC: Test separation of concerns
- ✅ DAO: Test database operations

## Bug Reports

When reporting bugs, include:

1. Test case ID
2. Steps to reproduce
3. Expected behavior
4. Actual behavior
5. Screenshots (if applicable)
6. Error messages

