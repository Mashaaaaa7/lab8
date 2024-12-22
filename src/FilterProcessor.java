import java.util.List;
import java.util.stream.Collectors;
//Создание обработчиков данных

public class FilterProcessor {

    @DataProcessor
    public List<String> filterData(List<String> data) {
        System.out.println("Фильтрация данных...");
        return data.stream()
                .filter(s -> s.length() > 5)
                .collect(Collectors.toList());
    }

    @DataProcessor
    public List<String> transformData(List<String> data) {
        System.out.println("Трансформация данных...");
        return data.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    @DataProcessor
    public List<String> aggregateData(List<String> data) {
        System.out.println("Агрегация данных...");
        return data.stream()
                .map(s -> "Processed: " + s)
                .collect(Collectors.toList());
    }
}
