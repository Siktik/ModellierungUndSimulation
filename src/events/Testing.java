package events;

import Utils.NumberGenerator;
import Utils.TimeManager;
import simManagement.SimulationManager;

import java.util.PriorityQueue;

public class Testing extends Event {


    private final int max_TimeHandingOutNotification= 1200;
    private final int min_TimeHandingOutNotification= 600;
    long timeToSpentOnTesting;
    long timeForHandingOutTestNotification;
    long timeToTestAllPerson;

    public Testing(long timestampOfExecution, int carID, int numberOfPeopleInCar){

        super(timestampOfExecution, carID, numberOfPeopleInCar, Testing.class);

    }

    public PriorityQueue<Event> process(PriorityQueue<Event> eventList){


        eventList.add(new LeavingTheStation(calculateTestingTime(), super.getCarID(), super.getNumberOfPeopleInCar(), true));

        return eventList;


    }
    private long calculateTestingTime(){
        //can be simplified when debugging is done
        this.timeForHandingOutTestNotification = NumberGenerator.generateRandomNumber(max_TimeHandingOutNotification, min_TimeHandingOutNotification);
        this.timeToTestAllPerson = (2400L * super.getNumberOfPeopleInCar());
        this.timeToSpentOnTesting = timeForHandingOutTestNotification + timeToTestAllPerson;

        return timeToSpentOnTesting+ TimeManager.getElapsedTimeInMilliSeconds();
    }




}
