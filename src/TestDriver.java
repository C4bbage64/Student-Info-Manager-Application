import facade.StudentManagementFacade;
import observer.StudentDataObserver;
import observer.StudentDataManager;
import model.Student;
import model.Attendance;
import model.Payment;
import java.util.List;

public class TestDriver implements StudentDataObserver {
    public static void main(String[] args) {
        new TestDriver().run();
    }

    public void run() {
        System.out.println("Starting Backend Verification...");

        StudentManagementFacade facade = StudentManagementFacade.getInstance();
        StudentDataManager.getInstance().addObserver(this);

        String studId = "STU_TEST_" + (System.currentTimeMillis() % 10000);

        try {
            // 1. Add Student
            System.out.println("\n[Action] Adding Student: " + studId);
            facade.addStudent(studId, "Test Student", 20, "CS", "test@test.com");

            // 2. Mark Attendance
            System.out.println("\n[Action] Marking Attendance...");
            facade.markAttendance(studId, "2023-10-01", "PRESENT");

            // Verify Attendance Added
            List<Attendance> atts = facade.getStudentAttendance(studId);
            System.out.println("Attendance records found: " + atts.size());

            // 3. Update Attendance
            if (!atts.isEmpty()) {
                System.out.println("\n[Action] Updating Attendance...");
                facade.updateAttendance(atts.get(0).getId(), "ABSENT");
            }

            // 4. Add Payment
            System.out.println("\n[Action] Adding Payment...");
            facade.addPayment(studId, 50.0, "2023-10-02", "Fee 1");

            // Verify Payment Added
            List<Payment> payments = facade.getStudentPayments(studId);
            System.out.println("Payment records found: " + payments.size());

            // 5. Delete Payment & Attendance (Cleanup)
            System.out.println("\n[Action] Cleaning up records...");
            for (Payment p : payments)
                facade.deletePayment(p.getId());
            for (Attendance a : atts)
                facade.deleteAttendance(a.getId()); // Note: this list might be stale after update, but IDs persist

            // 6. Delete Student
            System.out.println("\n[Action] Deleting Student...");
            facade.deleteStudent(studId);

            System.out.println("\nVerification Completed Successfully.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void onStudentDataChanged(String eventType) {
        System.out.println("[OBSERVER] Notification Received: " + eventType);
    }
}
