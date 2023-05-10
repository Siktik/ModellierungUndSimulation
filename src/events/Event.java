package events;

public class Event {

    private int timestampOfExecution;
    private int carID;
    private int numberOfPeopleInCar;
    private Class<?extends Event> eventClass;

    public Event(int timestampOfExecution, int carID, int numberOfPeopleInCar, Class<?extends Event> eventClass){
        this.timestampOfExecution= timestampOfExecution;
        this.carID=carID;
        this.numberOfPeopleInCar=numberOfPeopleInCar;
        this.eventClass=eventClass;
        System.out.println(this);
    }

    public void processEvent(){
        System.out.println("Processed " + this);
        /*
        if(eventClass.isAssignableFrom(ArrivingAtTheTestStation.class)){

            ArrivingAtTheTestStation arrivingAtTheTestStation= (ArrivingAtTheTestStation) this;

        }else if(eventClass.isAssignableFrom(Testing.class)){

            Testing testing= (Testing) this;

        }else if(eventClass.isAssignableFrom(LeavingTheStation.class)){

            LeavingTheStation leavingTheStation= (LeavingTheStation) this;

        }else{
            throw new IllegalStateException("The Eventclass you want to process is not defined");
        }
        */

    }

    @Override
    public String toString() {
        return "Event{" +
                "timestampOfExecution=" + timestampOfExecution +
                ", carID=" + carID +
                ", numberOfPeopleInCar=" + numberOfPeopleInCar +
                ", eventClass=" + eventClass +
                '}';
    }

    public int getTimestampOfExecution() {
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
