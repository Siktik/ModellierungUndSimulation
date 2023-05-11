import Utils.CSVWriter;
import simManagement.SimulationManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {



    public static void main(String[] args) throws IOException {


       /* SimulationManager.generateEvents();     //should set generated Events to true if successful

        if(SimulationManager.isGeneratedEvents()){
            /// start
            //while(SimulationManager.getRunID()< SimulationManager.getAllQueueSizes().length) {
                SimulationManager.setupRun();
                SimulationManager.run();
           // }



        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }*/
        File file= new File("test.csv");
        FileWriter writer= new FileWriter(file, false);

        writer.write("Test");
        writer.close();
    }


}
