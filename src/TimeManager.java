public class TimeManager {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTimeInSeconds() {
        return (endTime - startTime) / 1000;
    }
}
