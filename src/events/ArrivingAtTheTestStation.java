package events;

/**
 * Event models arrival of a car at the testing station.
 * Time to spend on testing is calculated as it is predetermined, waiting time entry is added to the corresponding log list.
 */
public class ArrivingAtTheTestStation extends Event {

    private final long timeToSpendOnTesting;

    public ArrivingAtTheTestStation(long timestampOfExecution, int carID, int numberOfPeopleInCar){
        super(timestampOfExecution, carID, numberOfPeopleInCar, ArrivingAtTheTestStation.class);
        timeToSpendOnTesting= (1200L * super.getNumberOfPeopleInCar());
    }

    public long getTimeToSpendOnTesting() {
        return timeToSpendOnTesting;
    }
}
