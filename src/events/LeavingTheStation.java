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

    public void process(){

            System.out.println("Car " + super.getCarID() + " is leaving at " + super.getTimestampOfExecution() + " and has been tested");

    }

    public boolean isHasBeenTested() {
        return hasBeenTested;
    }

    public void setHasBeenTested(boolean hasBeenTested) {
        this.hasBeenTested = hasBeenTested;
    }


}
