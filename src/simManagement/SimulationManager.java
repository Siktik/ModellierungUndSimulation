package simManagement;

import Utils.AllComparators;
import Utils.NumberGenerator;
import Utils.TimeManager;
import events.ArrivingAtTheTestStation;
import events.Event;

import java.util.*;

public class SimulationManager {

    /**
     SimZeit ist um Faktor 100 schneller
     1s Simulationszeit = 100s Echtzeit.
     1000ms Simulationszeit = 100 000ms Echtzeit
     1min Simulationszeit = 6 000s Echtzeit
     36s Simulationszeit = 3600s Echtzeit = 1h Echtzeit
     */


    /**
     * da wir mehrere Runs machen mit verschiedenen Queuing Disziplinen aber vergleichbarkeit zwischen den Runs haben wollen werden die Arriving events
     * nur einmal initalisiert und in dieser Liste gespeichert. Zu Beginn eines Runs werden alle ihre Elemente in die allArrivalEvents geschrieben
     */
    private static List<ArrivingAtTheTestStation> arrivalEventsForEveryRun;
    private static List<ArrivingAtTheTestStation> allArrivalEvents;
    private static int runID=0;
    private static int carCounter =1;   //static car ID Counter
    private static boolean generatedEvents;     //Hilfsvariable wird in der main abgefragt ob das generieren der Events geklappt hat
    private static final int min_Value= 300;    //minimaler Wert für Abstand zwischen Arriving Event -> 300ms -> 30 sek
    private static final int max_value= 1800;   //maximaler Wert für Abstand zwischen Arriving Event -> 1200ms -> 120 sek
    private static List<Server> allServers= new LinkedList();
    public static PriorityQueue<ArrivingAtTheTestStation> queue= new PriorityQueue<>();

    //Multi Queue
    public static HashMap<Integer, PriorityQueue<ArrivingAtTheTestStation>> multiQueue = new HashMap<>();
    private static int numOfEvents=0;
    /**
     * Vars and Lists for Analysis
     */
    private static double amountOfPeopleInACar=0;       //wird einmalig bei generateEvents() berechnet, da für alle Runs die gleichen Arriving Events benutzt werden

    /**
     * Method providing main simulation functionality. Runs until all events are processed.
     * In each iteration, checks if new events are available based on their arrival time and the current simulation time.
     */
    public static void run(){
        System.out.println("____________________________________\nTimeStamp___CarID___EventType___#CarsInTheSystem\n____________________________________");
        TimeManager.start();

        while(true){
            if(allArrivalEvents.isEmpty()&&allServers.stream().allMatch(Server::isIdle))
                break;
            checkForNewArrivingEvents();
            allServers.forEach(Server::evaluateCurrentState);
        }

        TimeManager.stop();
        System.out.println("End of Sim Run "+getRunID()+", total Time is: "+ TimeManager.getElapsedTimeInScaledTime()+" seconds or "+ TimeManager.getElapsedTimeInScaledTime()/60+" minutes");
        runID++;
    }

    /**
     * Same as method above, except for the call "checkForNewArrivingEventMQ(), as events are sorted into the Multi queue differently.
     */
    public static void runMultiQueue(){
        System.out.println("____________________________________\nTimeStamp___CarID___EventType___#CarsInTheSystem\n____________________________________");
        TimeManager.start();
        while(true){
            if(allArrivalEvents.isEmpty()&&allServers.stream().allMatch(Server::isIdle))
                break;
            checkForNewArrivingEventsMQ();
            allServers.forEach(Server::evaluateCurrentStateMQ);
        }
        TimeManager.stop();
        System.out.println("End of Sim Run "+getRunID()+"("+ currentQueueType + "), total Time is: "+ TimeManager.getElapsedTimeInScaledTime()+" seconds or "+ TimeManager.getElapsedTimeInScaledTime()/60+" minutes");
        runID++;
    }

    /**
     * Search through List of all predetermined events, check if the simulation time is greater that their start time. If so,
     * the event is treated as a car having arrived at the station and added to the queue with all currently active events.
     */
    private static void checkForNewArrivingEvents(){
        if(!allArrivalEvents.isEmpty()) {
            if(allArrivalEvents.get(0).getTimestampOfExecution()< TimeManager.getElapsedTimeInMilliSeconds())
                queue.add(allArrivalEvents.remove(0));
        }
    }

    private static void checkForNewArrivingEventsMQ(){
        if(!allArrivalEvents.isEmpty()) {
            if(allArrivalEvents.get(0).getTimestampOfExecution()< TimeManager.getElapsedTimeInMilliSeconds()){



                // Instead of finding next queue in predetermined order, find the smallest queue.
                int targetQueue = 0;
                for (Integer key : multiQueue.keySet()){
                    if(multiQueue.get(key).size() < multiQueue.get(targetQueue).size()){
                        targetQueue = key;
                        }
                }
                System.out.println("targetQueue: " + targetQueue  + " carID: "+ allArrivalEvents.get(0).getCarID() + ": target server: " + targetQueue);
                multiQueue.get(targetQueue).add(allArrivalEvents.remove(0));
            }
        }
    }

    private static AllComparators.QueueType currentQueueType;
     /**
     * wird von der main aus vor jedem Simulationsdurchlauf aufgerufen
     * Reset/Setup Funktion
     * Set up a Simulation run using a single queue:
     *      1. Collect the predetermined arrival events
     *      2. Reinstantiate Servers and Collections
     *      3. Reset Log Collections
     */
    public static void setupSingleQueueRun(Comparator<ArrivingAtTheTestStation> comparator, AllComparators.QueueType type){
        currentQueueType=type;
        carCounter= 0;
        allArrivalEvents=new LinkedList<>(arrivalEventsForEveryRun);
        int serverCouter=0;
        int numberOfServers=3;
        allServers= new LinkedList<>();
        while(serverCouter< numberOfServers){
            allServers.add(new Server(serverCouter++));
        }
        queue= new PriorityQueue<>(comparator);

        //used for Data Analysis
        singleRunData=new LinkedList<>();
    }

        /**
         * Method to setup simulation with multi queue. Reinstantiate Servers and Event queues with pre-gerenerated
         * arrival events. Initialise Log Collections.
         *
         */
    public static void setupMultiQueueRun(Comparator<ArrivingAtTheTestStation> comparator, AllComparators.QueueType type){
        allServers.clear();
        numOfEvents =0;
        currentQueueType=type;
        allArrivalEvents=new LinkedList<>(arrivalEventsForEveryRun);
        int serverCounter = 3;
        for (int i = 0; i < serverCounter; i++) {
            allServers.add(new Server(i));
            multiQueue.put(i, new PriorityQueue<>(comparator));
        }
        singleRunData=new LinkedList<>();
    }


    /**
     * generiert Arriving Events vor Simlationsstart, speichert sie in einer Liste.
     *
     */
    public static void generateEvents(){
        System.out.println("Generating Arriving Events");
        int i= 0;
        arrivalEventsForEveryRun= new LinkedList<>();

        while(i<=72000){   // 2 minutes in milliseconds     //ein Event alle 30-120 sek (Echtzeit), alle 1200-1800 ms (SimZeit)
            int timeInterval= NumberGenerator.generateRandomNumber(max_value,min_Value);
            i+= timeInterval;
            ArrivingAtTheTestStation event= new ArrivingAtTheTestStation(i, carCounter++, NumberGenerator.generateRandomNumber(4,1));
            arrivalEventsForEveryRun.add(event);
            amountOfPeopleInACar+= event.getNumberOfPeopleInCar();

        }

        System.out.println("amountOfAllPeople"+ amountOfPeopleInACar);
        amountOfPeopleInACar= amountOfPeopleInACar/arrivalEventsForEveryRun.size();
        System.out.println("unformatted averagePeopleInCar "+ amountOfPeopleInACar);

        System.out.println("Generated a total of "+ arrivalEventsForEveryRun.size()+" events");
        setGeneratedEvents(true);
    }

    /**
     * Utility Methods
     *
     */
    public static boolean isGeneratedEvents() {
        return generatedEvents;
    }

    public static void setGeneratedEvents(boolean generatedEvents) {
        SimulationManager.generatedEvents = generatedEvents;
    }

    private static List<String> singleRunData= new LinkedList<>();

    public static int getRunID() {
        return runID;
    }

    // Hilfsmethoden für Logging und Debugging.
    private static double calcAvgOfLongList(List<Long> values){
        double mean= 0;
        for (Long value : values) mean += value;
        return mean/(double)values.size();
    }

    public static void printQueueTimeStamps(){
        if(currentQueueType== AllComparators.QueueType.FIFO||currentQueueType== AllComparators.QueueType.LIFO){
            System.out.println("current queue");
            for(ArrivingAtTheTestStation event: queue){
                System.out.println(event.getTimestampOfExecution()+":");
            }
        }else{
            System.out.println("current queue");
            for(ArrivingAtTheTestStation event: queue){
                System.out.println(event.getTimeToSpendOnTesting()+":");
            }
        }
    }

    public static void printMultiQueueTimeStamps(){
        if(currentQueueType== AllComparators.QueueType.FIFO||currentQueueType== AllComparators.QueueType.LIFO){
            System.out.println("current multi queue: <CarID:timestampOfExecution");
            for (Integer key: multiQueue.keySet()) {
                System.out.print("Server "+key + ": ");
                for (ArrivingAtTheTestStation event : multiQueue.get(key)) {
                    System.out.print("<"+ event.getCarID() + ":" + event.getTimestampOfExecution() + "> ;");
                }
                System.out.println();
            }

        }else{
            System.out.println("current multi queue: <CarID:TimeToSpendOnTesting>");
            for (Integer key: multiQueue.keySet()) {
                System.out.print("Server "+key + ": ");
                for (ArrivingAtTheTestStation event : multiQueue.get(key)) {
                    System.out.print("<"+ event.getCarID() + ":" + event.getTimeToSpendOnTesting() + "> ;");
                }
                System.out.println();
            }
        }
    }
}
