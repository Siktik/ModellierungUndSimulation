package Utils;

import events.ArrivingAtTheTestStation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class handling Data Collection. Contains Lists that are filled with Logs during he simulation
 *
 * Before each run, a writer is Initialized. After each Run, the Logs are written to a .csv File "LogData.csv" and the Lists are reset.
 * After all runs are concluded, the writer is closed, resulting in a single file containing the average time informmation for all queueing strategies.
 *
 * The Aggregated data is copied into another file and the proces repeated to allow for more significant data.
 */

public class DataCollection {

    public static List<String> waitTimeLogs = new ArrayList<>();
    public static List<String> processingTimeLogs = new ArrayList<>();
    public static List<String> dwellTimeLogs = new ArrayList<>();
    public static File logFile = new File("LogData.csv");
    public static FileWriter logWriter;

    static {
        try {
            logWriter = new FileWriter(logFile, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initWriter(){
        try {

            logWriter.write("runID;QueueType;Avg WaitTime;Avg ProcessingTime;Avg. DwellTime\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void closeWriter(){
        try{

            logWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Iterate through all Log Lists and calculate the corresponding average.
     * Write it to the file along with a runId and the used Queueing Strategy.
     */

    public static void writeData(String runId, String queueType){

        try {
            long avgWaitTime = 0;
            long avgProcessingTime = 0;
            long avgDwellTime = 0;

            for (int i = 0; i < waitTimeLogs.size(); i++) {
                avgWaitTime += Long.parseLong(waitTimeLogs.get(i));
                avgProcessingTime += Long.parseLong(processingTimeLogs.get(i));
                avgDwellTime += Long.parseLong(dwellTimeLogs.get(i));
            }
            avgWaitTime = avgWaitTime/waitTimeLogs.size();
            avgProcessingTime = avgProcessingTime/processingTimeLogs.size();
            avgDwellTime = avgDwellTime/dwellTimeLogs.size();

            logWriter.write(runId + ";" + queueType + ";"+  avgWaitTime + ";"+ avgProcessingTime + ";"  + avgDwellTime+"\n");

            clearLogCollection();

        }catch (IOException ignored){
            ignored.printStackTrace();

        }
    }


    /**
     * Takes an ArrivingAtTheStation event and deduces all relevant Logs. As this method is only called when an event
     * is removed from the queue, we can infer the waiting time, processing time and resulting dwell time from that alone.
     *
     * @param arrivalEvent Instance of an event containing among other thing the arrival time and processing time.
     *                     WaitTime = current simulation time - arrival time
     *                     ProcessingTime = ArrivalEvent:ProcessingTime
     *                     DwellTime = WaitTime + ProcessingTime
     */
    public static void writeLogEntry(ArrivingAtTheTestStation arrivalEvent){

        String waitTime = String.valueOf(TimeManager.getElapsedTimeInMilliSeconds() - arrivalEvent.getTimestampOfExecution());
        String processingTime = String.valueOf(arrivalEvent.getTimeToSpendOnTesting());
        String dwellTime = String.valueOf(Long.parseLong(waitTime)+ Long.parseLong(processingTime));

        waitTimeLogs.add(waitTime);
        processingTimeLogs.add(processingTime);
        dwellTimeLogs.add(dwellTime);

    }

    public static void clearLogCollection () {
        waitTimeLogs.clear();
        processingTimeLogs.clear();
        dwellTimeLogs.clear();
    }
}
