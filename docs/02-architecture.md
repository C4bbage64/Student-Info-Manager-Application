# System Architecture

## Architecture Overview

The Student Information Application follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│         (View - Swing Panels)          │
└───────────────┬─────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────┐
│            Facade Layer                 │
│    (StudentManagementFacade)           │
└───────────────┬─────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────┐
│         Business Logic Layer            │
│         (Controllers)                    │
└───────────────┬─────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────┐
│         Data Access Layer                │
│         (DAO Classes)                    │
└───────────────┬─────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────┐
│         Database Layer                   │
│         (SQLite Database)                │
└─────────────────────────────────────────┘
```

## MVC Architecture

The application implements the **Model-View-Controller (MVC)** pattern:

### Model Layer (`model/`)
- **Student.java**: Student entity with attributes (ID, name, age, course, email)
- **Attendance.java**: Attendance records (ID, student ID, date, status)
- **Payment.java**: Payment records (ID, student ID, amount, date, description)

### View Layer (`view/`)
- **LoginPanel.java**: User authentication interface
- **MainPanel.java**: Main application interface with tabs
- **AttendancePanel.java**: Attendance management interface
- **FinancePanel.java**: Payment management interface
- **BasePanel.java**: Base class with common UI functionality

### Controller Layer (`controller/`)
- **StudentController.java**: Business logic for student operations
- **AttendanceController.java**: Business logic for attendance operations
- **PaymentController.java**: Business logic for payment operations

## Package Structure

```
src/
├── model/          # Entity classes (MVC Model)
├── view/           # UI components (MVC View)
├── controller/     # Business logic (MVC Controller)
├── dao/            # Data Access Objects
├── facade/         # Facade pattern implementation
├── chain/          # Chain of Responsibility pattern
├── state/          # State pattern implementations
├── observer/       # Observer pattern implementation
├── strategy/      # Strategy pattern implementation
├── exceptions/     # Custom exception classes
└── util/           # Utility classes
```

## Data Flow

1. **User Input** → View layer receives user actions
2. **Validation** → Chain of Responsibility validates input
3. **State Check** → State pattern checks application state
4. **Facade** → Unified interface routes request to appropriate controller
5. **Controller** → Business logic processing
6. **DAO** → Database operations
7. **Observer** → Notifies views of data changes
8. **View Update** → UI automatically refreshes

## Design Principles

- **Separation of Concerns**: Each layer has a specific responsibility
- **Single Responsibility**: Each class has one reason to change
- **Dependency Inversion**: High-level modules depend on abstractions
- **Open/Closed**: Open for extension, closed for modification
- **DRY (Don't Repeat Yourself)**: Code reusability through patterns

