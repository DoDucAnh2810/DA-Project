package cs451;

public class Timer {
    private long startTime;

    // Constructor initializes the timer by resetting it
    public Timer() {
        reset();
    }

    // Reset the timer by setting the start time to the current time
    public void reset() {
        startTime = System.nanoTime(); // Using nanoTime for more precision
    }

    // Get the elapsed time in milliseconds since the last reset
    public long getElapsedTimeMillis() {
        return (System.nanoTime() - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
    }

    // Get the elapsed time in seconds since the last reset
    public double getElapsedTimeSeconds() {
        return (System.nanoTime() - startTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
    }
}
