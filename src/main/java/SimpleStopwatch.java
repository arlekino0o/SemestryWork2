public class SimpleStopwatch {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public long getElapsedNanoseconds() {
        return endTime - startTime;
    }

    public double getElapsedMilliseconds() {
        return (endTime - startTime) / 1_000_000.0;
    }
}