package events;

import Utils.TimeManager;
import simManagement.SimulationManager;

import java.util.PriorityQueue;



public class ArrivingAtTheTestStation extends Event {

    public ArrivingAtTheTestStation(long timestampOfExecution, int carID, int numberOfPeopleInCar){
        super(timestampOfExecution, carID, numberOfPeopleInCar, ArrivingAtTheTestStation.class);
    }

    //processEvent
    public PriorityQueue<Event> process(PriorityQueue<Event> eventList){



        return eventList;
    }
}
