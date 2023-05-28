import Utils.AllComparators;
import Utils.DataCollection;
import events.ArrivingAtTheTestStation;
import simManagement.SimulationManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {
    /**
     * Köhmstedt Jonas, 1351904
     * Maywald Moritz, 1358960
     * Späth Maximilian, 1456880
     */
/*
    static List<String> dataCollectorLogs= new LinkedList<>();
    static List<String> dataCollectorSingleValues= new LinkedList<>();
    static List<String> dataCollectorDwellTime= new LinkedList<>();
    static List<String> dataCollectorAmountOfVehicleInTestingLane= new LinkedList<>();

 */

    public static void main(String[] args) {


        SimulationManager.generateEvents();

        if(SimulationManager.isGeneratedEvents()){
            /// start
                Map<AllComparators.QueueType, Comparator<ArrivingAtTheTestStation>> allComparators= AllComparators.getAllComparators();
                for(AllComparators.QueueType type:allComparators.keySet()) {
                    System.out.println("Starting new Run \n" +
                            "Queue Type is "+ type.name());
                    SimulationManager.setupSingleQueueRun(allComparators.get(type), type);
                    SimulationManager.run();
                    DataCollection.dataCollectorLogs.addAll(SimulationManager.getSingleRunDataLogs());
                    DataCollection.dataCollectorSingleValues.add(SimulationManager.getSingleRunData()+";"+ type.name());
                }

            //DataCollection.writeData();
            //writeData();
        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }



    }





}
