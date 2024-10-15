package cs451.p2pLink;

class Timer {
    private long startTime;


    Timer() {
        reset();
    }


    void reset() {
        startTime = System.nanoTime();
    }


    long getElapsedTimeMillis() {
        return (System.nanoTime() - startTime) / 1_000_000;
    }

    
    double getElapsedTimeSeconds() {
        return (System.nanoTime() - startTime) / 1_000_000_000.0;
    }
}
