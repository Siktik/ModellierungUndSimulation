import simManagement.SimulationManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {
    /**
     * Köhmstedt Jonas, 1351904
     * Maywald Moritz, 1358960
     * Späth Maximilian, 1456880
     */

    static List<String> dataCollectorLogs= new LinkedList<>();
    static List<String> dataCollectorSingleValues= new LinkedList<>();
    static List<String> dataCollectorDwellTime= new LinkedList<>();
    static List<String> dataCollectorAmountOfVehicleInTestingLane= new LinkedList<>();

    public static void main(String[] args) {


        SimulationManager.generateEvents();

        if(SimulationManager.isGeneratedEvents()){
            /// start
            while(SimulationManager.getRunID()< SimulationManager.getAllQueueSizes().length) {
                SimulationManager.setupRun();
                SimulationManager.run();
                dataCollectorLogs.addAll(SimulationManager.getSingleRunDataLogs());
                dataCollectorSingleValues.add(SimulationManager.getSingleRunData());
                dataCollectorDwellTime.addAll(SimulationManager.getDwellTime());
                dataCollectorAmountOfVehicleInTestingLane.addAll(SimulationManager.getTestingLaneData());
            }

            writeData();
        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }



    }

    private static void writeData(){

        try {
            File simDataTestingLane = new File("SimDataTestingLane.csv");
            FileWriter writerTestingLane = new FileWriter(simDataTestingLane, false);
            for (String string : dataCollectorAmountOfVehicleInTestingLane) {
                writerTestingLane.write(string + "\n");
            }
            writerTestingLane.close();


            File simDataDwellTime = new File("SimDataDwellTime.csv");
            FileWriter writerDwellTime = new FileWriter(simDataDwellTime, false);
            for (String string : dataCollectorDwellTime) {
                writerDwellTime.write(string + "\n");
            }
            writerDwellTime.close();

            File simDataValues = new File("SimDataValues.csv");
            FileWriter writerValues = new FileWriter(simDataValues, false);
            for (String string : dataCollectorSingleValues) {
                writerValues.write(string + "\n");
            }
            writerValues.close();

            File file = new File("SimDataLogs.csv");
            FileWriter writerLogs = new FileWriter(file, false);
            for (String string : dataCollectorLogs) {
                writerLogs.write(string + "\n");
            }
            writerLogs.close();

        }catch (IOException ignored){
            System.err.println("Could not write Data to CSV -> IOException");
        }
    }



}
