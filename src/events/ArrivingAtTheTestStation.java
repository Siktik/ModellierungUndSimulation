package events;

import Utils.TimeManager;
import simManagement.SimulationManager;

import java.util.PriorityQueue;



public class ArrivingAtTheTestStation extends Event {

    private long timeToSpentOnTesting;

    public ArrivingAtTheTestStation(long timestampOfExecution, int carID, int numberOfPeopleInCar){
        super(timestampOfExecution, carID, numberOfPeopleInCar, ArrivingAtTheTestStation.class);
        timeToSpentOnTesting= (1200L * super.getNumberOfPeopleInCar());
        SimulationManager.waitingTime.add(timeToSpentOnTesting);
    }

    public long getTimeToSpentOnTesting() {
        return timeToSpentOnTesting;
    }
}
