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

    public enum currentStateEnum{
        IDLE,
        TESTING
    }

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
    static int carCounter =0;
    private static int queue=0;
    private static final int min_Value= 300;
    private static final int max_value= 1200;
    private static Event currentEvent;
    private static boolean generatedEvents;
    private static final int maxQueueSize= 10;
    private static currentStateEnum currentState= currentStateEnum.IDLE;
    public static LinkedList<ArrivingAtTheTestStation> queuedArrivals= new LinkedList<>();



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

        while(i<=72000){   // 2 minutes in milliseconds
            int timeInterval= NumberGenerator.generateRandomNumber(max_value,min_Value);
            i+= timeInterval;
            eventList.add(new ArrivingAtTheTestStation(i, carCounter++, NumberGenerator.generateRandomNumber(5,1)));
        }

        System.out.println("Generated a total of "+ eventList.size()+" events");
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

    public static currentStateEnum getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(currentStateEnum currentState) {
        SimulationManager.currentState = currentState;
    }

    public static void printEvent(Event event){

        int carsInTheSystem= queuedArrivals.size();
        if(currentState==currentStateEnum.TESTING)
            carsInTheSystem++;

        System.out.println(TimeManager.formatTimeFromMilliSecondsToSeconds(event.getTimestampOfExecution())+"___"+ event.getCarID()+"___"+event.getEventClass().getSimpleName()+"___"+ carsInTheSystem);
    }
}
