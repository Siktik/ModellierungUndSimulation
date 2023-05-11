public class TimeManager {
    private static long startTime;
    private static long endTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static void stop() {
        endTime = System.currentTimeMillis();
    }

    public static long getElapsedTimeInSeconds() {
        return getElapsedTimeInMilliSeconds() / 1000;
    }
    public static long getElapsedTimeInMilliSeconds() {
        return (System.currentTimeMillis() - startTime);
    }
}
