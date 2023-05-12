package events;

import Utils.NumberGenerator;
import Utils.TimeManager;
import simManagement.SimulationManager;

import java.sql.Time;
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

        long dwellTime= calculateTestingTime(); //verweildauer
        SimulationManager.addDwellTime(dwellTime);  //data analysis
        eventList.add(new LeavingTheStation(dwellTime+ TimeManager.getElapsedTimeInMilliSeconds(), super.getCarID(), super.getNumberOfPeopleInCar(), true));
        //Zeitpunkt für LeavingTheStation ist jetzige Simzeit + Verweildauer

        return eventList;


    }

    //berechnet die zeit die das Auto beim testen verbringt
    private long calculateTestingTime(){

        this.timeForHandingOutTestNotification = NumberGenerator.generateRandomNumber(max_TimeHandingOutNotification, min_TimeHandingOutNotification);
        this.timeToTestAllPerson = (2400L * super.getNumberOfPeopleInCar());
        this.timeToSpentOnTesting = timeForHandingOutTestNotification + timeToTestAllPerson;

        return timeToSpentOnTesting;
    }




}
