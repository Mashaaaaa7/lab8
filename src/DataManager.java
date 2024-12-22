import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class DataManager {
    private final List<Object> processors = new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private final List<String> processedData = new ArrayList<>(); // Обработанные данные
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void registerDataProcessor(Object processor) {
        processors.add(processor);
    }

    public void loadData(String source) {
        try {
            data.addAll(Files.readAllLines(Paths.get(source)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from file: " + source , e);
        }
    }

    public void processData() {
        System.out.println("Обработка данных...");
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Object processor : processors) {
            for (Method method : processor.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(DataProcessor.class)) {
                    futures.add(CompletableFuture.runAsync(() -> {
                        try {
                            List<String> result = (List<String>) method.invoke(processor, data);
                            synchronized (processedData) {
                                processedData.addAll(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, executor));
                }
            }
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        data.clear();
        data.addAll(processedData);

    }

    public void saveData(String destination) {
        try {
            Files.write(Paths.get(destination), data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + destination, e);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}