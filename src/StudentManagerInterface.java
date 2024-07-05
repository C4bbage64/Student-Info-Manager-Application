import java.io.IOException;
import java.util.List;

public interface StudentManagerInterface {
    // Adds a new student to the system
    // Throws InvalidInputException if the input data is invalid
    void addStudent(Student student) throws InvalidInputException;

    // Edits an existing student's information
    // Throws InvalidInputException if the input data is invalid
    void editStudent(String studentId, Student updatedStudent) throws InvalidInputException;

    // Deletes a student from the system
    // Throws InvalidInputException if the input data is invalid
    void deleteStudent(String studentId) throws InvalidInputException;

    // Searches for a student by their student ID
    // Returns the Student object if found, otherwise returns null
    Student searchStudent(String studentId);

    // Returns a list of all students in the system
    List<Student> getAllStudents();

    // Sorts the list of students based on the provided criteria
    // The criteria can be "name", "id", "grade", etc.
    List<Student> sortStudents(String criteria);

    // Exports the student data to a file with the specified filename
    // Throws IOException if there is an error during file operations
    void exportData(String filename) throws IOException;

    // Imports student data from a file with the specified filename
    // Throws IOException if there is an error during file operations
    void importData(String filename) throws IOException;
}
