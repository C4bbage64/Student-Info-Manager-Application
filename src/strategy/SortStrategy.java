package strategy;

import model.Student;
import java.util.List;

/**
 * Strategy interface for sorting students.
 * Part of Strategy design pattern.
 */
public interface SortStrategy {
    /**
     * Sorts the list of students according to the strategy.
     * @param students List of students to sort
     * @return Sorted list of students
     */
    List<Student> sort(List<Student> students);
    
    /**
     * Gets the name of this sorting strategy.
     */
    String getStrategyName();
}

