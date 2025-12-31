# Student Information Application

## Overview

The Student Information Application is a comprehensive desktop application built with Java Swing to manage student information, attendance, and financial records. It supports complete CRUD (Create, Read, Update, Delete) operations on student records with robust exception handling and data validation. The application uses SQLite database for persistent storage and implements 8 design patterns to ensure maintainability, scalability, and code reusability.

## Features

### Student Management
- Add new student records with validation
- Edit existing student records
- Delete student records with confirmation
- Search students by ID or name
- View all students in a sortable table
- Real-time table updates (Observer Pattern)

### Attendance Management
- Mark student attendance (PRESENT/ABSENT)
- View attendance records by student or date
- Calculate attendance rate percentage
- Color-coded attendance rate indicators

### Financial Management
- Record student payments
- View payment history
- Calculate total amount paid
- Calculate outstanding balance
- Track payment descriptions and dates

### Security & Validation
- User authentication system
- Comprehensive input validation (Chain of Responsibility)
- Custom exception handling with helpful error messages
- State-based access control

## Design Patterns Implemented

This application implements **8 design patterns**:

1. **Singleton Pattern** - DatabaseConnection, SessionManager, StudentManagementFacade, ApplicationStateContext
2. **Facade Pattern** - Unified interface to controller subsystem
3. **Chain of Responsibility Pattern** - Input validation chain
4. **State Pattern** - Application state management & Student enrollment states
5. **Observer Pattern** - Automatic UI updates on data changes
6. **Strategy Pattern** - Flexible sorting algorithms
7. **MVC Pattern** - Model-View-Controller architecture
8. **DAO Pattern** - Data Access Object for database abstraction

For detailed documentation on design patterns, see [Design Patterns Documentation](docs/03-design-patterns.md).

## Technology Stack

- **Programming Language**: Java (JDK 8+)
- **GUI Framework**: Java Swing (Nimbus Look and Feel)
- **Database**: SQLite
- **JDBC Driver**: sqlite-jdbc-3.45.1.0.jar
- **Logging**: SLF4J (API + Simple)
- **Architecture**: MVC with DAO pattern

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- SQLite JDBC driver (included in `lib/` folder)
- An IDE like IntelliJ IDEA, Eclipse, or NetBeans (optional)

## Installation

### Quick Start

1. **Clone or download the repository**
   ```bash
   git clone <repository-url>
   cd StudentInformationApplication/StudentInformationApplication
   ```

2. **Verify Java installation**
   ```bash
   java -version
   ```

3. **Run the application**
   
   **Windows:**
   ```bash
   run.bat
   ```
   
   **Linux/macOS:**
   ```bash
   java -cp "out:lib/*" StudentInfoApp
   ```

### Detailed Installation

See [Installation Guide](docs/07-installation.md) for detailed instructions including:
- Compiling from source
- IDE setup (IntelliJ IDEA)
- Docker installation
- Troubleshooting

## Usage

1. **Launch the application**
   - Run `StudentInfoApp.java` or use `run.bat`
   - Login screen will appear

2. **Login**
   - Default credentials are in `src/credentials.txt`
   - Enter username and password
   - Click "Login" or press Enter

3. **Navigate Features**
   - **Add Student**: Fill in student details and click "Add Student"
   - **Edit Student**: Load student by ID, modify fields, click "Save Changes"
   - **Delete Student**: Enter Student ID, confirm deletion
   - **View Students**: Browse all students, use sorting options
   - **Search Student**: Search by Student ID
   - **Attendance**: Mark attendance, view records, calculate rates
   - **Finance**: Add payments, view history, check balances

For detailed user instructions, see [User Guide](docs/04-user-guide.md).

## Project Structure

```
StudentInformationApplication/
├── src/
│   ├── model/          # Entity classes (Student, Attendance, Payment)
│   ├── view/           # UI components (Panels)
│   ├── controller/     # Business logic (Controllers)
│   ├── dao/            # Data Access Objects
│   ├── facade/         # Facade pattern
│   ├── chain/          # Chain of Responsibility pattern
│   ├── state/          # State pattern
│   ├── observer/       # Observer pattern
│   ├── strategy/       # Strategy pattern
│   ├── exceptions/     # Custom exceptions
│   └── util/           # Utilities (SessionManager)
├── lib/                # External JAR dependencies
├── docs/               # Comprehensive documentation
├── out/                # Compiled classes
├── student_app.db      # SQLite database (auto-created)
├── Dockerfile          # Docker configuration
└── run.bat             # Windows run script
```

## Documentation

Comprehensive documentation is available in the `docs/` folder:

- [Overview](docs/01-overview.md) - Project introduction and domain
- [Architecture](docs/02-architecture.md) - System architecture and design
- [Design Patterns](docs/03-design-patterns.md) - All 8 design patterns explained
- [User Guide](docs/04-user-guide.md) - Step-by-step user instructions
- [Developer Guide](docs/05-developer-guide.md) - Technical documentation
- [API Reference](docs/06-api-reference.md) - API documentation
- [Installation Guide](docs/07-installation.md) - Setup instructions
- [Testing Guide](docs/08-testing.md) - Test cases and procedures
- [Database Schema](docs/09-database-schema.md) - Database structure

## Exception Handling

The application includes comprehensive exception handling:

- **InvalidInputException**: Invalid user input validation
- **StudentNotFoundException**: Student record not found
- **DuplicateStudentException**: Duplicate student ID
- **AttendanceRecordNotFoundException**: Attendance record not found
- **PaymentNotFoundException**: Payment record not found
- **FileOperationException**: File operation errors

All exceptions provide helpful error messages displayed to users via dialog boxes.

## Database

- **Type**: SQLite
- **File**: `student_app.db` (auto-created on first run)
- **Tables**: 
  - `students` - Student information
  - `attendance` - Attendance records
  - `payments` - Payment records

See [Database Schema](docs/09-database-schema.md) for detailed structure.

## Key Features Highlights

### Design Pattern Benefits

- **Facade**: Simplified API for all operations
- **Chain of Responsibility**: Modular validation system
- **State Pattern**: Controlled access based on application state
- **Observer Pattern**: Automatic UI synchronization
- **Strategy Pattern**: Flexible sorting without code duplication
- **Singleton**: Efficient resource management
- **MVC**: Clean separation of concerns
- **DAO**: Database abstraction layer

### User Experience

- Modern Nimbus Look and Feel
- Real-time data updates
- Comprehensive input validation
- Helpful error messages
- Intuitive tabbed interface
- Color-coded status indicators

## Contributing

This is an educational project demonstrating Object-Oriented Programming principles and design patterns. Contributions and suggestions are welcome!

## License

This project is created for educational purposes as part of the Software Design and Integration course.

## Acknowledgments

- Built with Java Swing
- Uses SQLite for data persistence
- Implements industry-standard design patterns
- Follows MVC architectural pattern

---

For more information, please refer to the [Documentation Index](docs/README.md).
