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
        if(SimulationManager.queuedArrivals.size() < SimulationManager.getMaxQueueSize()) {

            SimulationManager.queuedArrivals.add(this);
                if(SimulationManager.getCurrentState() == SimulationManager.currentStateEnum.IDLE){

                    ArrivingAtTheTestStation arrivingAtTheTestStation= SimulationManager.queuedArrivals.remove(0);
                    eventList.add(new Testing(TimeManager.getElapsedTimeInMilliSeconds(), arrivingAtTheTestStation.getCarID(), arrivingAtTheTestStation.getNumberOfPeopleInCar()));

                }

        }else{

            eventList.add(new LeavingTheStation(TimeManager.getElapsedTimeInMilliSeconds(), super.getCarID(), super.getNumberOfPeopleInCar(),false));

        }
        return eventList;
    }
}
