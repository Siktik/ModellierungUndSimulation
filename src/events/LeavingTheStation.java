package events;

import Utils.TimeManager;
import simManagement.SimulationManager;

import java.sql.Time;
import java.util.PriorityQueue;

public class LeavingTheStation extends Event {


    private boolean hasBeenTested;
    public LeavingTheStation (long timestampOfExecution, int carID, int numberOfPeopleInCar, boolean hasBeenTested){

        super(timestampOfExecution,carID,numberOfPeopleInCar, LeavingTheStation.class);
        this.hasBeenTested= hasBeenTested;

    }

    public PriorityQueue<Event> process(PriorityQueue<Event> eventList){

        if(this.isHasBeenTested()) {
            SimulationManager.setCurrentState(SimulationManager.currentStateEnum.IDLE);
            System.out.println("Car " + super.getCarID() + " is leaving at " + super.getTimestampOfExecution() + " and has been tested");

            if(!SimulationManager.queuedArrivals.isEmpty()){
                ArrivingAtTheTestStation arrivingAtTheTestStation= SimulationManager.queuedArrivals.remove(0);
                eventList.add(new Testing(TimeManager.getElapsedTimeInMilliSeconds(), arrivingAtTheTestStation.getCarID(), arrivingAtTheTestStation.getNumberOfPeopleInCar()));
                System.out.println("Car "+ arrivingAtTheTestStation.getCarID()+" should be tested next");
            }
        }else {
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
