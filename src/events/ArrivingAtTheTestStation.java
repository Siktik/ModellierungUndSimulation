package events;



public class ArrivingAtTheTestStation extends Event {

    public ArrivingAtTheTestStation(int timestampOfExecution, int carID, int numberOfPeopleInCar){
        super(timestampOfExecution, carID, numberOfPeopleInCar, ArrivingAtTheTestStation.class);
    }

    //processEvent
    public boolean process(){
        return true;
    }
}
