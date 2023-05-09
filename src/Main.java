public class Main {

    public static void main(String[] args) {


        SimulationManager.generateEvents();
        if(SimulationManager.generatedEvents){
            /// start
            SimulationManager.run();


        }else{
            throw new IllegalStateException("Could not generate Events for Simulation");
        }
    }
}
