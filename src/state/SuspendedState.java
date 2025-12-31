package state;

/**
 * Concrete state: Suspended (temporarily inactive).
 */
public class SuspendedState implements StudentEnrollmentState {
    @Override
    public void enroll() {
        System.out.println("Cannot enroll suspended student.");
    }
    
    @Override
    public void suspend() {
        System.out.println("Student is already suspended.");
    }
    
    @Override
    public void graduate() {
        System.out.println("Cannot graduate suspended student.");
    }
    
    @Override
    public void activate() {
        System.out.println("Student reactivated.");
    }
    
    @Override
    public String getStatus() {
        return "SUSPENDED";
    }
}

