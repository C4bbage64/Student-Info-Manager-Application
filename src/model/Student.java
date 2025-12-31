package model;

/**
 * Model class representing a Student entity.
 * Part of the MVC architecture - Model layer.
 */
public class Student {
    private String studentId;
    private String name;
    private int age;
    private String course;
    private String email;
    private String enrollmentStatus; // ENROLLED, SUSPENDED, GRADUATED

    // Default constructor
    public Student() {
    }

    // Constructor with all fields
    public Student(String name, int age, String studentId, String course) {
        this.name = name;
        this.age = age;
        this.studentId = studentId;
        this.course = course;
    }

    // Constructor with email
    public Student(String name, int age, String studentId, String course, String email) {
        this.name = name;
        this.age = age;
        this.studentId = studentId;
        this.course = course;
        this.email = email;
        this.enrollmentStatus = "ENROLLED"; // Default status
    }
    
    // Constructor with all fields including enrollment status
    public Student(String name, int age, String studentId, String course, String email, String enrollmentStatus) {
        this.name = name;
        this.age = age;
        this.studentId = studentId;
        this.course = course;
        this.email = email;
        this.enrollmentStatus = enrollmentStatus != null ? enrollmentStatus : "ENROLLED";
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCourse() {
        return course;
    }

    public String getEmail() {
        return email;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus != null ? enrollmentStatus : "ENROLLED";
    }

    // Setters
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus != null ? enrollmentStatus : "ENROLLED";
    }

    @Override
    public String toString() {
        return String.format("Student ID: %s\nName: %s\nAge: %d\nCourse: %s\nEmail: %s\nStatus: %s\n",
                studentId, name, age, course, email != null ? email : "N/A", getEnrollmentStatus());
    }
}
