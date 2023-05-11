import java.util.PriorityQueue;

public class ArrivingAtTheTestStation extends Event {

    public ArrivingAtTheTestStation(int timestampOfExecution, int carID, int numberOfPeopleInCar){
        super(timestampOfExecution, carID, numberOfPeopleInCar, ArrivingAtTheTestStation.class);
    }

    //processEvent
    public PriorityQueue<Event> process(PriorityQueue<Event> eventList){

        eventList.remove(this);
        eventList.add( new Testing( 0, super.getCarID(), super.getNumberOfPeopleInCar(),System.currentTimeMillis(), super.getTimestampOfExecution()));
        SimulationManager.decreaseQueueCounter();
        return eventList;
    }
}
