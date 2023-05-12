import simManagement.SimulationManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {



    public static void main(String[] args) throws IOException {

        List<String> dataCollectorLogs= new LinkedList<>();
        List<String> dataCollectorSingleValues= new LinkedList<>();

        SimulationManager.generateEvents();     //should set generated Events to true if successful

        if(SimulationManager.isGeneratedEvents()){
            /// start
            while(SimulationManager.getRunID()< SimulationManager.getAllQueueSizes().length) {
                SimulationManager.setupRun();
                SimulationManager.run();
                dataCollectorLogs.addAll(SimulationManager.getSingleRunDataLogs());
                dataCollectorSingleValues.add(SimulationManager.getSingleRunData());
            }



        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }


        File simDataValues= new File("SimDataValues.csv");
        FileWriter writerValues= new FileWriter(simDataValues, false);
        for(String string: dataCollectorSingleValues){
            writerValues.write(string+"\n");
        }
        writerValues.close();

        File file= new File("SimDataLogs.csv");
        FileWriter writerLogs= new FileWriter(file, false);
        for(String string: dataCollectorLogs){
            writerLogs.write(string+"\n");
        }
        writerLogs.close();


    }
    public static void setData(){

    }


}
