package simManagement;

import Utils.NumberGenerator;
import Utils.TimeManager;
import events.ArrivingAtTheTestStation;
import events.Event;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
    private static final int[] allQueueSizes= {10,12,14,16,20};
    private static int runID;
    private static int carCounter =0;
    private static int inTestingLane =0;
    private static int carsThatCouldNotHaveBeenTested=0;
    private static final int min_Value= 300;
    private static final int max_value= 1200;
    private static Event currentEvent;
    private static boolean generatedEvents;
    private static int maxQueueSize= 10;
    public static LinkedList<ArrivingAtTheTestStation> queuedArrivals= new LinkedList<>();

    public static List<Integer> amountOfCarsInTestingLane= new LinkedList<>();




    public static void setupRun(){
        carCounter=0;
        inTestingLane =0;
        queuedArrivals=new LinkedList<>();
        eventList.clear();
        eventList.addAll(arrivalEventsForEveryRun);
        carsThatCouldNotHaveBeenTested=0;
        amountOfCarsInTestingLane= new LinkedList<>();
        maxQueueSize= allQueueSizes[runID];
        singleRunData=new LinkedList<>();

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

                eventList = currentEvent.processEvent(eventList);
                printEvent(currentEvent);

            } catch (IllegalStateException e) {

                System.out.println("There is a Problem mate, this should not have happened -> passed a Eventclass that cant be processed");
            }

        }

        TimeManager.stop();
        System.out.println("End of Sim Run "+getRunID()+", total Time is: "+ TimeManager.getElapsedTimeInScaledTime()+" seconds or "+ TimeManager.getElapsedTimeInScaledTime()/60+" minutes");
        runID++;

    }

    static double amountOfPeopleInACar=0;
    public static void generateEvents(){

        System.out.println("Generating Arriving Events");
        int i= 0;
        arrivalEventsForEveryRun= new LinkedList<>();

        while(i<=72000){   // 2 minutes in milliseconds
            int timeInterval= NumberGenerator.generateRandomNumber(max_value,min_Value);
            i+= timeInterval;
            ArrivingAtTheTestStation event= new ArrivingAtTheTestStation(i, carCounter++, NumberGenerator.generateRandomNumber(5,1));
            arrivalEventsForEveryRun.add(event);
            amountOfPeopleInACar+= event.getNumberOfPeopleInCar();

        }

        System.out.println("amountOfAllPeople"+ amountOfPeopleInACar);
        amountOfPeopleInACar= amountOfPeopleInACar/arrivalEventsForEveryRun.size();
        System.out.println("unformatted averagePeopleInCar "+ amountOfPeopleInACar);



        System.out.println("Generated a total of "+ arrivalEventsForEveryRun.size()+" events");
        setGeneratedEvents(true);
    }

    public static void decreaseQueueCounter(){
        inTestingLane--;}
    public static int getQueueCounter(){return inTestingLane;}
    public static void increaseQueueCounter(){
        inTestingLane++;}
    public static int getMaxQueueSize(){return maxQueueSize;}

    public static boolean isGeneratedEvents() {
        return generatedEvents;
    }

    public static void setGeneratedEvents(boolean generatedEvents) {
        SimulationManager.generatedEvents = generatedEvents;
    }


    private static List<String> singleRunData= new LinkedList<>();
    public static void printEvent(Event event){


        String str= runID+";"+TimeManager.formatTimeFromMilliSecondsToSeconds(event.getTimestampOfExecution())+";"+ event.getCarID()+";"+event.getEventClass().getSimpleName()+";"+ inTestingLane;
        System.out.println(str);
        singleRunData.add(str);

    }

    public static List<String> getSingleRunDataLogs() {
        return singleRunData;
    }
    public static String getSingleRunData(){
        if(runID-1>=0) {
            DecimalFormat df = new DecimalFormat("#.###");
            return getRunID() + ";" + getAllQueueSizes()[runID - 1] + ";" + df.format(amountOfPeopleInACar) + ";" + getCarsThatCouldNotHaveBeenTested();
        }
        return "Empty Result";
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


    public static int getRunID() {
        return runID;
    }



}
