import Utils.CSVWriter;
import simManagement.SimulationManager;

import java.io.IOException;

public class Main {



    public static void main(String[] args) {


        SimulationManager.generateEvents();     //should set generated Events to true if successful

        if(SimulationManager.isGeneratedEvents()){
            /// start
            //while(SimulationManager.getRunID()< SimulationManager.getAllQueueSizes().length) {
                SimulationManager.setupRun();
                SimulationManager.run();
           // }
            CSVWriter writer= new CSVWriter("Test.csv");
            if(writer.fileExists()) {
                try {
                    writer.write("test");
                }catch(IOException ignored){

                }
            }


        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }
    }


}
