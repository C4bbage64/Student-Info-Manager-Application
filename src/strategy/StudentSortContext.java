package strategy;

import model.Student;
import java.util.List;

/**
 * Context class for Strategy pattern.
 * Uses a SortStrategy to sort students.
 */
public class StudentSortContext {
    private SortStrategy strategy;
    
    /**
     * Creates a context with default strategy (Sort by ID).
     */
    public StudentSortContext() {
        this.strategy = new SortByIdStrategy();
    }
    
    /**
     * Creates a context with specified strategy.
     */
    public StudentSortContext(SortStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Sets the sorting strategy.
     */
    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Gets the current strategy.
     */
    public SortStrategy getStrategy() {
        return strategy;
    }
    
    /**
     * Sorts students using the current strategy.
     */
    public List<Student> sortStudents(List<Student> students) {
        return strategy.sort(students);
    }
    
    /**
     * Factory method to get strategy by name.
     * @param name Strategy name (name, age, course, student_id)
     * @return SortStrategy instance
     */
    public static SortStrategy getStrategyByName(String name) {
        return switch (name.toLowerCase()) {
            case "name" -> new SortByNameStrategy();
            case "age" -> new SortByAgeStrategy();
            case "course" -> new SortByCourseStrategy();
            case "student_id", "studentid" -> new SortByIdStrategy();
            default -> new SortByIdStrategy();
        };
    }
}

