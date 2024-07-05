public class Student {
    // Instance variables
    private String name;     // Stores the name of the student
    private int age;         // Stores the age of the student
    private String studentId; // Stores the student ID
    private String course;   // Stores the course the student is enrolled in

    // Constructor method for Student
    public Student(String name, int age, String studentId, String course) {
        this.name = name;
        this.age = age;
        this.studentId = studentId;
        this.course = course;
    }

    // Getter method for getName
    public String getName() {
        return name;
    }

    // Getter method for getAge
    public int getAge() {
        return age;
    }

    // Getter method for getStudentId
    public String getStudentId() {
        return studentId;
    }

    // Getter method for getCourse
    public String getCourse() {
        return course;
    }

    // Setter method for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter method for age
    public void setAge(int age) {
        this.age = age;
    }

    // Setter method for studentId
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    // Setter method for course
    public void setCourse(String course) {
        this.course = course;
    }

    // Overridden toString method
    @Override
    public String toString() {
        return String.format("Name: %s\nAge: %d\nStudent ID: %s\nCourse: %s\n", name, age, studentId, course);
    }
}
