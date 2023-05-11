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


        if(SimulationManager.getQueueCounter() < SimulationManager.getMaxQueueSize()) {

                    eventList.add(new Testing(TimeManager.getElapsedTimeInMilliSeconds(), super.getCarID(), super.getNumberOfPeopleInCar()));
                    SimulationManager.increaseQueueCounter();
        }else{

            eventList.add(new LeavingTheStation(TimeManager.getElapsedTimeInMilliSeconds(), super.getCarID(), super.getNumberOfPeopleInCar(),false));

        }
        return eventList;
    }
}
