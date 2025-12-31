package state;

/**
 * Concrete state: Graduated (completed).
 */
public class GraduatedState implements StudentEnrollmentState {
    @Override
    public void enroll() {
        System.out.println("Cannot enroll graduated student.");
    }
    
    @Override
    public void suspend() {
        System.out.println("Cannot suspend graduated student.");
    }
    
    @Override
    public void graduate() {
        System.out.println("Student is already graduated.");
    }
    
    @Override
    public void activate() {
        System.out.println("Cannot activate graduated student.");
    }
    
    @Override
    public String getStatus() {
        return "GRADUATED";
    }
}

