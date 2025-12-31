package state;

/**
 * Concrete state: Enrolled (active student).
 */
public class EnrolledState implements StudentEnrollmentState {
    @Override
    public void enroll() {
        System.out.println("Student is already enrolled.");
    }
    
    @Override
    public void suspend() {
        System.out.println("Student suspended.");
    }
    
    @Override
    public void graduate() {
        System.out.println("Student graduated.");
    }
    
    @Override
    public void activate() {
        System.out.println("Student is already active.");
    }
    
    @Override
    public String getStatus() {
        return "ENROLLED";
    }
}

