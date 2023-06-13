package events;


/**
 * Basic Event parent class. All Events consist of an ID, an arrival timeStamp (i.e. when the car is supposed to arrive,
 * not when this event is created), and the amount of People in the Car.
 */
public class Event {

    private long timestampOfExecution;
    private int carID;
    private int numberOfPeopleInCar;
    private Class<?extends Event> eventClass;

    public Event(long timestampOfExecution, int carID, int numberOfPeopleInCar, Class<?extends Event> eventClass){
        this.timestampOfExecution= timestampOfExecution;
        this.carID=carID;
        this.numberOfPeopleInCar=numberOfPeopleInCar;
        this.eventClass=eventClass;
        //System.out.println(this);
    }

    @Override
    public String toString() {
        return "events.Event{" +
                "timestampOfExecution=" + timestampOfExecution +
                ", carID=" + carID +
                ", numberOfPeopleInCar=" + numberOfPeopleInCar +
                ", eventClass=" + eventClass +
                '}';
    }

    public long getTimestampOfExecution() {
        return timestampOfExecution;
    }

    public void setTimestampOfExecution(int timestampOfExecution) {
        this.timestampOfExecution = timestampOfExecution;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getNumberOfPeopleInCar() {
        return numberOfPeopleInCar;
    }

    public void setNumberOfPeopleInCar(int numberOfPeopleInCar) {
        this.numberOfPeopleInCar = numberOfPeopleInCar;
    }

    public Class<? extends Event> getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class<? extends Event> eventClass) {
        this.eventClass = eventClass;
    }
}
