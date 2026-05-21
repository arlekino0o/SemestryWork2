import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TreapExperiment {
    public static void main(String[] args) throws IOException {
        String outputFile = "resultsDT.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("type,size,operation,time_ms,steps");

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

        for (File file : files) {
            List<Integer> data = readData(file);
            int size = data.size();

            measureInsert(type, size, data, writer);
            measureSearch(type, size, data, writer);
            measureDelete(type, size, data, writer);
        }
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

    private static void measureInsert(String type, int size, List<Integer> data, PrintWriter writer) {
        Treap treap = new Treap();
        SimpleStopwatch stopwatch = new SimpleStopwatch();

        treap.resetSteps();

        stopwatch.start();

        for (int value : data) {
            treap.insert(value);
        }

        stopwatch.stop();

        writer.println(type + "," + size + ",insert," +
                stopwatch.getElapsedMilliseconds() + "," + treap.getSteps());
    }

    private static void measureSearch(String type, int size, List<Integer> data, PrintWriter writer) {
        Treap treap = new Treap();

        for (int value : data) {
            treap.insert(value);
        }

        SimpleStopwatch stopwatch = new SimpleStopwatch();
        treap.resetSteps();

        stopwatch.start();

        for (int value : data) {
            treap.search(value);
        }

        stopwatch.stop();

        writer.println(type + "," + size + ",search," +
                stopwatch.getElapsedMilliseconds() + "," + treap.getSteps());
    }

    private static void measureDelete(String type, int size, List<Integer> data, PrintWriter writer) {
        Treap treap = new Treap();

        for (int value : data) {
            treap.insert(value);
        }

        SimpleStopwatch stopwatch = new SimpleStopwatch();
        treap.resetSteps();

        stopwatch.start();

        for (int value : data) {
            treap.delete(value);
        }

        stopwatch.stop();

        writer.println(type + "," + size + ",delete," + stopwatch.getElapsedMilliseconds() + "," + treap.getSteps());
    }
}