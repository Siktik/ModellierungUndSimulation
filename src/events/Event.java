package events;

import Utils.TimeManager;

import java.util.PriorityQueue;

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


    /*
    Relikt aus Aufgabe 1
    nichtmehr benötigt für Aufgabe 2, da keine eventList verwendet wird wie in Aufgabe 1
    queue verwaltet Arriving Events -> Server pullt arriving Events -> erstellt Leaving The Station und führt aus wenn fertig
    getestet
    public PriorityQueue<Event> processEvent(PriorityQueue<Event> eventList) throws IllegalStateException{

        if(eventClass.isAssignableFrom(ArrivingAtTheTestStation.class)){

            ArrivingAtTheTestStation arrivingAtTheTestStation= (ArrivingAtTheTestStation) this;
            return arrivingAtTheTestStation.process(eventList);

        }else if(eventClass.isAssignableFrom(Testing.class)){


            Testing testing= (Testing) this;
            return testing.process(eventList);

        }else if(eventClass.isAssignableFrom(LeavingTheStation.class)){

            LeavingTheStation leavingTheStation= (LeavingTheStation) this;
            return leavingTheStation.process(eventList);

        }else{
            throw new IllegalStateException("The EventClass you want to process is not defined");
        }

    }
    */


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
