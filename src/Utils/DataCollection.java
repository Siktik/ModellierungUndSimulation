package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DataCollection {
    public static List<String> dataCollectorLogs= new LinkedList<>();
    public static List<String> dataCollectorSingleValues= new LinkedList<>();
    public static List<String> dataCollectorDwellTime= new LinkedList<>();
    public static List<String> dataCollectorAmountOfVehicleInTestingLane= new LinkedList<>();



    public static void writeData(){

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
