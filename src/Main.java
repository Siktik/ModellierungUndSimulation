import simManagement.SimulationManager;

public class Main {

    public static void main(String[] args) {


        SimulationManager.generateEvents();     //should set generated Events to true if successful

        if(SimulationManager.isGeneratedEvents()){
            /// start
            System.out.println("Starting Sim Run");
            SimulationManager.run();


        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }
    }
}
