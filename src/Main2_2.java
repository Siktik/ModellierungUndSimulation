import Utils.AllComparators;
import Utils.DataCollection;
import events.ArrivingAtTheTestStation;
import simManagement.SimulationManager;

import java.util.Comparator;
import java.util.Map;

public class Main2_2 {
    /**
     * Köhmstedt Jonas, 1351904
     * Maywald Moritz, 1358960
     * Späth Maximilian, 1456880
     */

    public static void main(String[] args) {
        int runId=0;
        DataCollection.initWriter();
        SimulationManager.generateEvents();
        if(SimulationManager.isGeneratedEvents()){
            /// start
            Map<AllComparators.QueueType, Comparator<ArrivingAtTheTestStation>> allComparators= AllComparators.getAllComparators();
            for(AllComparators.QueueType type:allComparators.keySet()) {
                System.out.println("Starting new Run \n" +
                        "Queue Type is "+ type.name());
                SimulationManager.setupMultiQueueRun(allComparators.get(type), type);
                SimulationManager.runMultiQueue();

                DataCollection.writeData(""+ (++runId), ""+type);
            }
        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }
        DataCollection.closeWriter();
    }
}
