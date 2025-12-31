package strategy;

import model.Student;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete strategy: Sort students by Student ID.
 */
public class SortByIdStrategy implements SortStrategy {
    @Override
    public List<Student> sort(List<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getStudentId))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Sort by Student ID";
    }
}

