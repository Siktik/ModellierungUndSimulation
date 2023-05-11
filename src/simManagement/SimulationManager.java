package simManagement;

import Utils.NumberGenerator;
import Utils.TimeManager;
import events.ArrivingAtTheTestStation;
import events.Event;
import events.LeavingTheStation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class SimulationManager {



    /**
     About the SimTime
     !!!!!  1 sec  ==    10 ms
     !!!!! 10 sec  ==   100 ms
     !!!!!  1 min  ==   600 ms
     !!!!!  1   h  == 36000 ms
     */



    static PriorityQueue<Event> eventList= new PriorityQueue<>(new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            if(o1.getTimestampOfExecution()<o2.getTimestampOfExecution())
                return -1;
            else if(o1.getTimestampOfExecution()>o2.getTimestampOfExecution())
                return 1;
            return 0;
        }
    });
    private static List<Event> arrivalEventsForEveryRun;
    private static int[] allQueueSizes= {10,12,14,16,18,20,30};
    private static int runID;
    private static int carCounter =0;
    private static int queue=0;
    private static int carsThatCouldNotHaveBeenTested=0;
    private static final int min_Value= 300;
    private static final int max_value= 1200;
    private static Event currentEvent;
    private static boolean generatedEvents;
    private static int maxQueueSize= 10;
    public static LinkedList<ArrivingAtTheTestStation> queuedArrivals= new LinkedList<>();
    public static List<Integer> amountOfPeopleInACar= new LinkedList<>();
    public static List<Integer> amountOfCarsInTestingLane= new LinkedList<>();




    public static void setupRun(){
        carCounter=0;
        queue=0;
        queuedArrivals=new LinkedList<>();
        eventList.clear();
        carsThatCouldNotHaveBeenTested=0;
        amountOfPeopleInACar= new LinkedList<>();
        amountOfCarsInTestingLane= new LinkedList<>();
        maxQueueSize= allQueueSizes[runID];
        runID++;
        eventList.addAll(arrivalEventsForEveryRun);

    }



    public static void run(){
        System.out.println("____________________________________\nTimeStamp___CarID___EventType___#CarsInTheSystem\n____________________________________");

        TimeManager.start();
        while (!eventList.isEmpty()) {

            if (eventList.peek().getTimestampOfExecution() < TimeManager.getElapsedTimeInMilliSeconds())
                currentEvent = eventList.poll();
            else continue;


            try {

                assert currentEvent != null;
                printEvent(currentEvent);
                eventList = currentEvent.processEvent(eventList);

            } catch (IllegalStateException e) {

                System.out.println("There is a Problem mate, this should not have happened -> passed a Eventclass that cant be processed");
            }

        }

        TimeManager.stop();
        System.out.println("End of Sim Run, total Time is: "+ TimeManager.getElapsedTimeInScaledTime()+" seconds or "+ TimeManager.getElapsedTimeInScaledTime()/60+" minutes");


    }


    public static void generateEvents(){

        System.out.println("Generating Arriving Events");
        int i= 0;
        arrivalEventsForEveryRun= new LinkedList<>();

        while(i<=72000){   // 2 minutes in milliseconds
            int timeInterval= NumberGenerator.generateRandomNumber(max_value,min_Value);
            i+= timeInterval;
            ArrivingAtTheTestStation event= new ArrivingAtTheTestStation(i, carCounter++, NumberGenerator.generateRandomNumber(5,1));
            arrivalEventsForEveryRun.add(event);
            amountOfPeopleInACar.add(event.getNumberOfPeopleInCar());

        }

        System.out.println("Generated a total of "+ arrivalEventsForEveryRun.size()+" events");
        setGeneratedEvents(true);
    }

    public static void decreaseQueueCounter(){queue--;}
    public static int getQueueCounter(){return queue;}
    public static void increaseQueueCounter(){queue++;}
    public static int getMaxQueueSize(){return maxQueueSize;}

    public static boolean isGeneratedEvents() {
        return generatedEvents;
    }

    public static void setGeneratedEvents(boolean generatedEvents) {
        SimulationManager.generatedEvents = generatedEvents;
    }



    public static void printEvent(Event event){



        System.out.println(TimeManager.formatTimeFromMilliSecondsToSeconds(event.getTimestampOfExecution())+"___"+ event.getCarID()+"___"+event.getEventClass().getSimpleName()+"___"+ queue);
    }

    public static int getCarsThatCouldNotHaveBeenTested() {
        return carsThatCouldNotHaveBeenTested;
    }

    public static void addCarThatCouldNotHaveBeenTested() {
        carsThatCouldNotHaveBeenTested++;
    }
    public static int[] getAllQueueSizes() {
        return allQueueSizes;
    }

    public static void setAllQueueSizes(int[] allQueueSizes) {
        SimulationManager.allQueueSizes = allQueueSizes;
    }

    public static int getRunID() {
        return runID;
    }

    public static void setRunID(int runID) {
        SimulationManager.runID = runID;
    }
}
