# Student Info Manager Application

## Overview
The Student Info Manager Application is a desktop application built with Java Swing to manage student information. It supports CRUD (Create, Read, Update, Delete) operations on student records and handles exceptions gracefully. The application reads and writes student data from/to text files.

## Features
- Add new student records
- Edit existing student records
- Delete student records
- Sort and View all student records
- Exception handling with custom messages
- Persistent storage of student data using text files

## Class Diagram
![Class Diagram](https://github.com/C4bbage64/Student-Info-Manager-Application/assets/151762860/71c9b6c5-e481-4601-8625-47e7a965d0e4)

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- An IDE like IntelliJ IDEA, Eclipse, or NetBeans

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/student-info-manager.git
    cd student-info-manager
    ```

2. Open the project in your IDE.

3. Build the project to resolve dependencies.

## Usage
1. Run the `StudentInfoManagerApp` class to start the application.
2. Use the tabs to navigate through different functionalities:
    - **Add Student**: Fill in the details and click "Add Student".
    - **Edit Student**: Provide the Student ID of the record you want to update, fill in the new details, and click "Edit Student".
    - **Delete Student**: Provide the Student ID and click "Delete Student".
    - **View Students**: Click "Refresh" to view the list of all students.

## File Structure
- `Student.java`: Defines the `Student` class with attributes and methods.
- `StudentManager.java`: Manages the collection of students and CRUD operations.
- `FileManager.java`: Handles reading and writing student data to/from text files.
- `InvalidInputException.java`: Custom exception class for handling invalid inputs.
- `StudentInfoManagerApp.java`: Main class that initializes the GUI and integrates all functionalities.

## Exception Handling
The application includes custom exception handling to manage invalid inputs. For instance, attempting to add a student without filling in all fields will trigger an `InvalidInputException` with a relevant error message.

## Sample Data File
Ensure you have a `students.txt` file in the root directory with sample data in the following format:

- Email: [your-email@example.com](mailto:your-email@example.com)
- GitHub Issues: [https://github.com/your-username/your-repository-name/issues](https://github.com/your-username/your-repository-name/issues)

---

Thank you for visiting our repository! We hope you find this project useful and look forward to your contributions.
