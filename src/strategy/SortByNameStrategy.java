package strategy;

import model.Student;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete strategy: Sort students by name (alphabetically).
 */
public class SortByNameStrategy implements SortStrategy {
    @Override
    public List<Student> sort(List<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Sort by Name";
    }
}

