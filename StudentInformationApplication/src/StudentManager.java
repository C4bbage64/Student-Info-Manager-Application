import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private List<Student> students;

    public StudentManager() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void editStudent(String studentId, Student updatedStudent) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                student.setName(updatedStudent.getName());
                student.setAge(updatedStudent.getAge());
                student.setCourse(updatedStudent.getCourse());
                break;
            }
        }
    }

    public void deleteStudent(String studentId) {
        students.removeIf(student -> student.getStudentId().equals(studentId));
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
}