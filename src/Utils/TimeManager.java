package Utils;

public class TimeManager {
    private static long startTime;
    private static long endTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static void stop() {
        endTime = System.currentTimeMillis();
    }

    public static long getElapsedTimeInMilliSeconds() {
        return (System.currentTimeMillis() - startTime);
    }
    public static int getElapsedTimeInScaledTime(){
        return (int)((System.currentTimeMillis()- startTime)/10);
    }
    public static long formatTimeFromMilliSecondsToSeconds (long milliseconds){
        return milliseconds/10;
    }
}
