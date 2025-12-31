package strategy;

import model.Student;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete strategy: Sort students by age.
 */
public class SortByAgeStrategy implements SortStrategy {
    @Override
    public List<Student> sort(List<Student> students) {
        return students.stream()
                .sorted(Comparator.comparingInt(Student::getAge))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Sort by Age";
    }
}

