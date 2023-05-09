package events;

public class LeavingTheStation extends Event{

    public LeavingTheStation (int timestampOfExecution, int carID, int numberOfPeopleInCar){

        super(timestampOfExecution,carID,numberOfPeopleInCar, LeavingTheStation.class);

    }

    public boolean process(){
        return true;
    }
}
