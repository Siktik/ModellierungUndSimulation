package Utils;

import events.ArrivingAtTheTestStation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataCollection {
    public static List<String> dataCollectorLogs= new LinkedList<>();
    public static List<String> dataCollectorSingleValues= new LinkedList<>();
    public static List<String> dataCollectorDwellTime= new LinkedList<>();
    public static List<String> dataCollectorAmountOfVehicleInTestingLane= new LinkedList<>();
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

            logWriter.write("runID;QueueType;Avg WaitTime;Avg ProcessingTime;Avg. DwellTime;\n");
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

    public static void writeData(String runId, String queueType){
        try {
            System.out.println("Log list sizes");
            System.out.println(waitTimeLogs.size());
            System.out.println(processingTimeLogs.size());
            System.out.println(dwellTimeLogs.size());
            String fileName = "LogFile_"+runId+"_"+queueType+".csv";


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
            //System.err.println("Could not write Data to CSV -> IOException");
        }
    }


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
