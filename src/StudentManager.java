import java.util.ArrayList;
import java.util.List;

// Define a class named "StudentManager"
public class StudentManager {

    // Declare a private list to store Student objects
    private List<Student> students;

    // Constructor method for initializing a StudentManager object
    public StudentManager() {
        // Initialize the list of students as an ArrayList
        this.students = new ArrayList<>();
    }

    // Method to add a new student to the list
    public void addStudent(Student student) {
        // Add the provided student to the list of students
        students.add(student);
    }

    // Method to edit an existing student's information
    public void editStudent(String studentId, Student updatedStudent) {
        // Iterate through the list of students
        for (Student student : students) {
            // Check if the student's ID matches the provided ID
            if (student.getStudentId().equals(studentId)) {
                // Update the student's information with the provided updatedStudent object
                student.setName(updatedStudent.getName());
                student.setAge(updatedStudent.getAge());
                student.setCourse(updatedStudent.getCourse());
                // Exit the loop once the student is found and updated
                break;
            }
        }
    }

    // Method to delete a student from the list by student ID
    public void deleteStudent(String studentId) {
        // Remove the student from the list if their ID matches the provided studentId
        students.removeIf(student -> student.getStudentId().equals(studentId));
    }

    // Method to retrieve a copy of the list of all students
    public List<Student> getAllStudents() {
        // Return a new ArrayList containing all the students
        return new ArrayList<>(students);
    }
}
