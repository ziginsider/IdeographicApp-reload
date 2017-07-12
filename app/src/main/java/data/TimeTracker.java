package data;

/**
 * Created by zigin on 08.07.2017.
 *
 * He helps to work with tracking functions time-work
 */

public class TimeTracker {
    private static long tStart, tEnd;

    public static void start() {
        tStart = System.nanoTime();
    }

    public static void end() {
        tEnd = System.nanoTime();
    }

    public static long howLong() {
        return tEnd - tStart;
    }
}
