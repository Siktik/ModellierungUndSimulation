package events;

import Utils.TimeManager;
import simManagement.SimulationManager;

import java.util.PriorityQueue;

public class LeavingTheStation extends Event {


    private boolean hasBeenTested;
    public LeavingTheStation (long timestampOfExecution, int carID, int numberOfPeopleInCar, boolean hasBeenTested){

        super(timestampOfExecution,carID,numberOfPeopleInCar, LeavingTheStation.class);
        this.hasBeenTested= hasBeenTested;

    }

    public PriorityQueue<Event> process(PriorityQueue<Event> eventList){

        if(this.isHasBeenTested()) {        //wurde getestet? dann decrease queue sonst nichts (außer data value)

            SimulationManager.decreaseQueueCounter();
            System.out.println("Car " + super.getCarID() + " is leaving at " + super.getTimestampOfExecution() + " and has been tested");


        }else {
            SimulationManager.addCarThatCouldNotHaveBeenTested();
            System.out.println("Car " + super.getCarID() + " is leaving at " + super.getTimestampOfExecution() + " and has not been tested");
        }
        return eventList;
    }

    public boolean isHasBeenTested() {
        return hasBeenTested;
    }

    public void setHasBeenTested(boolean hasBeenTested) {
        this.hasBeenTested = hasBeenTested;
    }


}
