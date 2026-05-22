import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TreapExperiment {
    private static class Stats {
        private double totalTimeMs;
        private long totalSteps;
        private int runs;

        void add(double timeMs, long steps) {
            totalTimeMs += timeMs;
            totalSteps += steps;
            runs++;
        }

        double getAverageTimeMs() {
            return runs == 0 ? 0 : totalTimeMs / runs;
        }

        double getAverageSteps() {
            return runs == 0 ? 0 : (double) totalSteps / runs;
        }
    }

    public static void main(String[] args) throws IOException {
        String outputFile = "resultsDT.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("type,size,operation,avg_time_ms,avg_steps,runs");

            runExperiments("data/random", "random", writer);
            runExperiments("data/sorted", "sorted", writer);
        }

        System.out.println("Experiments completed. Results saved to results.csv");
    }

    private static void runExperiments(String directoryPath, String type, PrintWriter writer) throws IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) {
            return;
        }

        Arrays.sort(files);

        Map<Integer, Stats> insertStatsBySize = new TreeMap<>();
        Map<Integer, Stats> searchStatsBySize = new TreeMap<>();
        Map<Integer, Stats> deleteStatsBySize = new TreeMap<>();

        for (File file : files) {
            List<Integer> data = readData(file);
            int size = data.size();

            addStats(insertStatsBySize, size, measureInsert(data));
            addStats(searchStatsBySize, size, measureSearch(data));
            addStats(deleteStatsBySize, size, measureDelete(data));
        }

        writeAverages(writer, type, "insert", insertStatsBySize);
        writeAverages(writer, type, "search", searchStatsBySize);
        writeAverages(writer, type, "delete", deleteStatsBySize);
    }

    private static List<Integer> readData(File file) throws IOException {
        List<Integer> data = new ArrayList<>();

        List<String> lines = Files.readAllLines(file.toPath());

        for (String line : lines) {
            if (!line.isBlank()) {
                data.add(Integer.parseInt(line.trim()));
            }
        }

        return data;
    }

    private static Stats measureInsert(List<Integer> data) {
        if (data.isEmpty()) {
            return new Stats();
        }

        Treap treap = new Treap();

        for (int i = 0; i < data.size() - 1; i++) {
            treap.insert(data.get(i));
        }

        SimpleStopwatch stopwatch = new SimpleStopwatch();
        treap.resetSteps();
        stopwatch.start();
        treap.insert(data.get(data.size() - 1));
        stopwatch.stop();

        Stats stats = new Stats();
        stats.add(stopwatch.getElapsedMilliseconds(), treap.getSteps());
        return stats;
    }

    private static Stats measureSearch(List<Integer> data) {
        if (data.isEmpty()) {
            return new Stats();
        }

        Treap treap = new Treap();

        for (int value : data) {
            treap.insert(value);
        }

        int keyToSearch = data.get(data.size() / 2);
        SimpleStopwatch stopwatch = new SimpleStopwatch();
        treap.resetSteps();
        stopwatch.start();
        treap.search(keyToSearch);
        stopwatch.stop();

        Stats stats = new Stats();
        stats.add(stopwatch.getElapsedMilliseconds(), treap.getSteps());
        return stats;
    }

    private static Stats measureDelete(List<Integer> data) {
        if (data.isEmpty()) {
            return new Stats();
        }

        Treap treap = new Treap();

        for (int value : data) {
            treap.insert(value);
        }

        int keyToDelete = data.get(data.size() / 2);
        SimpleStopwatch stopwatch = new SimpleStopwatch();
        treap.resetSteps();
        stopwatch.start();
        treap.delete(keyToDelete);
        stopwatch.stop();

        Stats stats = new Stats();
        stats.add(stopwatch.getElapsedMilliseconds(), treap.getSteps());
        return stats;
    }

    private static void addStats(Map<Integer, Stats> statsBySize, int size, Stats measurement) {
        Stats stats = statsBySize.computeIfAbsent(size, ignored -> new Stats());
        stats.add(measurement.getAverageTimeMs(), Math.round(measurement.getAverageSteps()));
    }

    private static void writeAverages(PrintWriter writer, String type, String operation, Map<Integer, Stats> statsBySize) {
        for (Map.Entry<Integer, Stats> entry : statsBySize.entrySet()) {
            int size = entry.getKey();
            Stats stats = entry.getValue();

            writer.println(type + "," + size + "," + operation + "," +
                    stats.getAverageTimeMs() + "," + stats.getAverageSteps() + "," + stats.runs);
        }
    }
}
