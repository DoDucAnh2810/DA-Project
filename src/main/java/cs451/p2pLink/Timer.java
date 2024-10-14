package cs451.p2pLink;

class Timer {
    private long startTime;

    // Constructor initializes the timer by resetting it
    Timer() {
        reset();
    }

    // Reset the timer by setting the start time to the current time
    void reset() {
        startTime = System.nanoTime(); // Using nanoTime for more precision
    }

    // Get the elapsed time in milliseconds since the last reset
    long getElapsedTimeMillis() {
        return (System.nanoTime() - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
    }

    // Get the elapsed time in seconds since the last reset
    double getElapsedTimeSeconds() {
        return (System.nanoTime() - startTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
    }
}
