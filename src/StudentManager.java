import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// Implementation of StudentManagerInterface to manage student records
public class StudentManager implements StudentManagerInterface {
    private List<Student> students; // List to store Student objects

    // Constructor initializing the list of students
    public StudentManager() {
        students = new ArrayList<>(); // Initialize the list
    }

    // Method to add a new student to the list
    @Override
    public void addStudent(Student student) throws InvalidInputException {
        students.add(student); // Add a new Student object to the list
    }

    // Method to edit an existing student's information
    @Override
    public void editStudent(String studentId, Student updatedStudent) throws InvalidInputException {
        Student student = searchStudent(studentId); // Search for the student by ID
        if (student != null) {
            students.remove(student); // Remove the existing student
            students.add(updatedStudent); // Add the updated student
        } else {
            throw new InvalidInputException("Student not found!"); // Throw an exception if the student is not found
        }
    }

    // Method to delete a student by their ID
    @Override
    public void deleteStudent(String studentId) throws InvalidInputException {
        Student student = searchStudent(studentId); // Search for the student by ID
        if (student != null) {
            students.remove(student); // Remove the student from the list
        } else {
            throw new InvalidInputException("Student not found!"); // Throw an exception if the student is not found
        }
    }

    // Method to retrieve all students
    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Return a copy of the student list
    }

    // Method to search for a student by their ID
    @Override
    public Student searchStudent(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student; // Return the student if found
            }
        }
        return null; // Return null if the student is not found
    }

    // Method to export student data to a file
    @Override
    public void exportData(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student student : students) {
                writer.write(student.getName() + "," +
                        student.getAge() + "," +
                        student.getStudentId() + "," +
                        student.getCourse());
                writer.newLine(); // Move to the next line
            }
        }
    }

    // Method to import student data from a file
    @Override
    public void importData(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split each line by commas
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    int age = Integer.parseInt(parts[1].trim());
                    String studentId = parts[2].trim();
                    String course = parts[3].trim();
                    students.add(new Student(name, age, studentId, course)); // Create a new Student object and add it to the list
                }
            }
        }
    }

    // Method to sort students based on specified criteria
    @Override
    public List<Student> sortStudents(String criteria) {
        return students.stream()
                .sorted((s1, s2) -> {
                    switch (criteria) {
                        case "name":
                            return s1.getName().compareToIgnoreCase(s2.getName()); // Sort by name (case-insensitive)
                        case "age":
                            return Integer.compare(s1.getAge(), s2.getAge()); // Sort by age
                        case "studentId":
                            return s1.getStudentId().compareToIgnoreCase(s2.getStudentId()); // Sort by student ID (case-insensitive)
                        case "course":
                            return s1.getCourse().compareToIgnoreCase(s2.getCourse()); // Sort by course (case-insensitive)
                        default:
                            return 0; // Return 0 for any other criteria (no sorting)
                    }
                })
                .collect(Collectors.toList()); // Collect the sorted stream into a new list
    }
}
