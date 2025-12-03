import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public List<Student> readStudentsFromFile(String fileName) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                Student student = new Student(details[0], Integer.parseInt(details[1]), details[2], details[3]);
                students.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void writeStudentsToFile(String fileName, List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Student student : students) {
                bw.write(student.getName() + "," + student.getAge() + "," + student.getStudentId() + "," + student.getCourse());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}