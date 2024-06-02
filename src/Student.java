// Define a class named "Student"
public class Student {

    // Declare private instance variables for storing student information
    private String name;
    private int age;
    private String studentId;
    private String course;

    // Constructor method for initializing a Student object with provided information
    public Student(String name, int age, String studentId, String course) {
        // Assign values passed to the constructor to the corresponding instance variables
        this.name = name;
        this.age = age;
        this.studentId = studentId;
        this.course = course;
    }

    // Getter method for retrieving the name of the student
    public String getName() {
        return name;
    }

    // Setter method for updating the name of the student
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for retrieving the age of the student
    public int getAge() {
        return age;
    }

    // Setter method for updating the age of the student
    public void setAge(int age) {
        this.age = age;
    }

    // Getter method for retrieving the student ID
    public String getStudentId() {
        return studentId;
    }

    // Setter method for updating the student ID
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    // Getter method for retrieving the course of the student
    public String getCourse() {
        return course;
    }

    // Setter method for updating the course of the student
    public void setCourse(String course) {
        this.course = course;
    }
}
