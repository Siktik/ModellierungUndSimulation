package events;

import simManagement.SimulationManager;


public class ArrivingAtTheTestStation extends Event {

    private long timeToSpendOnTesting;

    public ArrivingAtTheTestStation(long timestampOfExecution, int carID, int numberOfPeopleInCar){
        super(timestampOfExecution, carID, numberOfPeopleInCar, ArrivingAtTheTestStation.class);
        timeToSpendOnTesting= (1200L * super.getNumberOfPeopleInCar());
        SimulationManager.waitingTime.add(timeToSpendOnTesting);
    }

    public long getTimeToSpendOnTesting() {
        return timeToSpendOnTesting;
    }
}
