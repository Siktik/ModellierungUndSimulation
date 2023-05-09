package events;

public class Testing extends Event{

    public Testing(int timestampOfExecution, int carID, int numberOfPeopleInCar){

        super(timestampOfExecution, carID, numberOfPeopleInCar, Testing.class);

    }

    public boolean process(){
        return true;
    }
}
