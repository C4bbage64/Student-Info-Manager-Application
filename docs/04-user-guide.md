# User Guide

## Getting Started

### Launching the Application

1. Ensure Java is installed (JDK 8 or higher)
2. Navigate to the project directory
3. Run `StudentInfoApp.java` or use the provided `run.bat` script
4. The login screen will appear

### Login

1. Enter your username and password
2. Credentials are stored in `src/credentials.txt`
3. Click "Login" or press Enter
4. Upon successful login, you'll see the main application window

## Main Features

### 1. Add Student

**Location**: "Add Student" tab

**Steps**:
1. Fill in all required fields:
   - Student ID (unique identifier)
   - Name
   - Age (must be between 1-150)
   - Course
   - Email (optional, validated format)
2. Click "Add Student"
3. Success message will appear
4. Student table automatically refreshes (Observer Pattern)

**Validation**:
- All fields except email are required
- Age must be a valid number between 1-150
- Email must be in valid format if provided
- Student ID must be unique

### 2. Edit Student

**Location**: "Edit Student" tab

**Steps**:
1. Enter the Student ID
2. Click "Load Student" to populate fields
3. Modify the desired fields
4. Click "Save Changes"
5. Success message will appear
6. Student table automatically refreshes

### 3. Delete Student

**Location**: "Delete Student" tab

**Steps**:
1. Enter the Student ID
2. Click "Delete Student"
3. Confirm deletion in the dialog
4. Student will be deleted
5. Student table automatically refreshes

### 4. View Students

**Location**: "View Students" tab

**Features**:
- View all students in a table
- Sort by: Student ID, Name, Age, or Course (Strategy Pattern)
- See total student count
- Click "Refresh" to reload data
- Table auto-refreshes when data changes (Observer Pattern)

**Sorting**:
1. Select sort criteria from dropdown
2. Click "Sort" button
3. Table updates with sorted data

### 5. Search Student

**Location**: "Search Student" tab

**Steps**:
1. Enter Student ID
2. Click "Search"
3. Student details appear in the text area below

### 6. Attendance Management

**Location**: "Attendance" tab

#### Mark Attendance
1. Enter Student ID
2. Select date (defaults to today)
3. Select status: PRESENT or ABSENT
4. Click "Mark Attendance"

#### View Attendance
1. Enter Student ID (or leave empty for all)
2. Click "Search"
3. View attendance records in table

#### Attendance Rate
1. Enter Student ID
2. Click "Calculate Rate"
3. View attendance percentage
   - Green: ≥80%
   - Orange: 60-79%
   - Red: <60%

### 7. Finance Management

**Location**: "Finance" tab

#### Add Payment
1. Enter Student ID
2. Enter amount (must be > 0)
3. Enter date (defaults to today)
4. Enter description
5. Click "Add Payment"

#### View Payments
1. Enter Student ID (or leave empty for all)
2. Click "Search"
3. View payment history
4. See total amount paid

#### Check Balance
1. Enter Student ID
2. Enter total fees (default: $5000.00)
3. Click "Check Balance"
4. View:
   - Total Paid
   - Balance Due
   - Status (PAID IN FULL if balance ≤ 0)

## Error Handling

The application provides helpful error messages:

- **Validation Errors**: Red warning dialogs for invalid input
- **Database Errors**: Error dialogs with detailed messages
- **Not Found Errors**: Clear messages when records don't exist
- **Duplicate Errors**: Warnings when trying to add duplicate students

## Tips

1. **Auto-refresh**: The student table automatically updates when you add/edit/delete students
2. **Validation**: All inputs are validated before processing
3. **State Management**: You must be logged in to perform operations
4. **Sorting**: Use the Strategy pattern sorting for different views
5. **Search**: Use Student ID for quick searches

## Keyboard Shortcuts

- **Enter**: Triggers login when in password field
- **Tab**: Navigate between fields
- **Escape**: Close dialogs

## Logout

Currently, logout functionality is available through the State pattern but not exposed in UI. The application maintains session state throughout use.

