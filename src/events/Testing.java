package events;

import Utils.NumberGenerator;
import Utils.TimeManager;
import simManagement.SimulationManager;

import java.sql.Time;
import java.util.PriorityQueue;

public class Testing extends Event {


    private final int max_TimeHandingOutNotification= 1200;
    private final int min_TimeHandingOutNotification= 600;
    private long timeToSpentOnTesting;


    public Testing(long timestampOfExecution, int carID, int numberOfPeopleInCar, long timeToSpentOnTesting){

        super(timestampOfExecution, carID, numberOfPeopleInCar, Testing.class);
        this.timeToSpentOnTesting=timeToSpentOnTesting;

    }


    public long getTimeToSpentOnTesting() {
        return timeToSpentOnTesting;
    }
}
