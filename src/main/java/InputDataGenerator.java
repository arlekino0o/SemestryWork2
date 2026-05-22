import java.io.*;
import java.util.*;

public class InputDataGenerator {
    public static void main(String[] args) throws IOException {
        int[] sizes = {
                100, 180, 260, 400, 550,
                800, 1200, 1700, 2500, 3800,
                5200, 7600, 11000, 16000, 23000,
                32000, 45000, 63000, 82000, 100000
        };

        int filesPerSize = 30;

        File randomDir = new File("data/random");
        File sortedDir = new File("data/sorted");

        randomDir.mkdirs();
        sortedDir.mkdirs();

        Random random = new Random();

        for (int size : sizes) {
            for (int test = 1; test <= filesPerSize; test++) {
                generateRandomFile("data/random/random_" + size + "_" + test + ".txt", size, random);
                generateSortedFile("data/sorted/sorted_" + size + "_" + test + ".txt", size);
            }
        }

        System.out.println("Input files generated successfully.");
    }

    private static void generateRandomFile(String fileName, int size, Random random) throws IOException {
        Set<Integer> values = new LinkedHashSet<>();

        while (values.size() < size) {
            values.add(random.nextInt(size * 10));
        }

        writeToFile(fileName, values);
    }

    private static void generateSortedFile(String fileName, int size) throws IOException {
        List<Integer> values = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            values.add(i);
        }

        writeToFile(fileName, values);
    }

    private static void writeToFile(String fileName, Collection<Integer> values) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (int value : values) {
                writer.println(value);
            }
        }
    }
}
