public class Main {
    public static void main(String[] args) {
        DataManager manager = new DataManager();
        FilterProcessor filters = new FilterProcessor();

        manager.registerDataProcessor(filters);

        manager.loadData("src/input.txt");
        manager.processData();
        manager.saveData("src/output.txt");

        manager.shutdown();
    }
}

//система обработки данных
