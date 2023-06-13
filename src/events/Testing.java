package events;


/**
 * Event models the testing of an entire car. Time to Spend on Testing is passed in the constructor by the
 * ArrinvingAtTheStation instance of this Car.
 */

public class Testing extends Event {

    private long timeToSpentOnTesting;
    private long timeWhenJoinedQueue;

    public Testing(long timestampOfExecution, int carID, int numberOfPeopleInCar, long timeToSpentOnTesting, long timeWhenJoinedQueue){
        super(timestampOfExecution, carID, numberOfPeopleInCar, Testing.class);
        this.timeToSpentOnTesting=timeToSpentOnTesting;
        this.timeWhenJoinedQueue= timeWhenJoinedQueue;
    }


    public long getTimeToSpentOnTesting() {
        return timeToSpentOnTesting;
    }

    public long getTimeWhenJoinedQueue() {
        return timeWhenJoinedQueue;
    }
}
