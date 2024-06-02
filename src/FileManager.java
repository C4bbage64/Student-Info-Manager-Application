import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Define a class named "FileManager"
public class FileManager {

    // Method to read student details from a file and return a list of Student objects
    public List<Student> readStudentsFromFile(String fileName) {
        // Create a list to store Student objects
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                // Split the line by comma to extract student details
                String[] details = line.split(",");
                // Create a new Student object using the extracted details and add it to the list
                Student student = new Student(details[0], Integer.parseInt(details[1]), details[2], details[3]);
                students.add(student);
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs
            e.printStackTrace();
        }
        // Return the list of Student objects
        return students;
    }

    // Method to write student details to a file
    public void writeStudentsToFile(String fileName, List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // Iterate through the list of students
            for (Student student : students) {
                // Write student details to the file separated by commas
                bw.write(student.getName() + "," + student.getAge() + "," + student.getStudentId() + "," + student.getCourse());
                // Move to the next line
                bw.newLine();
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs
            e.printStackTrace();
        }
    }
}
