package simManagement;

import Utils.AllComparators;
import Utils.NumberGenerator;
import Utils.TimeManager;
import events.ArrivingAtTheTestStation;
import events.Event;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SimulationManager {



    /**
     About the SimTime
     !!!!!  1 sec  ==    10 ms
     !!!!! 10 sec  ==   100 ms
     !!!!!  1 min  ==   600 ms
     !!!!!  1   h  == 36000 ms
     */
    /**
     * ###########################################################
     * ###########################################################
     * ##############VARIABLEN LISTEN QUEUES######################
     * ###########################################################
     * ###########################################################
     *
     */

    /**
     * da wir mehrere Runs machen mit verschiedenen Queuing Disziplinen aber vergleichbarkeit zwischen den Runs haben wollen werden die Arriving events
     * nur einmal initalisiert und in dieser Liste gespeichert. Zu Beginn eines Runs werden alle ihre Elemente in die allArrivalEvents geschrieben
     */
    private static List<ArrivingAtTheTestStation> arrivalEventsForEveryRun;
    private static List<ArrivingAtTheTestStation> allArrivalEvents;
    private static int runID=0;
    private static int carCounter =0;   //static car ID Counter
    private static boolean generatedEvents;     //Hilfsvariable wird in der main abgefragt ob das generieren der Events geklappt hat
    private static final int min_Value= 300;    //minimaler Wert für Abstand zwischen Arriving Event -> 300ms -> 30 sek
    private static final int max_value= 1800;   //maximaler Wert für Abstand zwischen Arriving Event -> 1200ms -> 120 sek
    private static List<Server> allServers= new LinkedList();

    public static PriorityQueue<ArrivingAtTheTestStation> queue= new PriorityQueue<>();
    /**
     * Vars and Lists for Analysis
     */
    private static double amountOfPeopleInACar=0;       //wird einmalig bei generateEvents() berechnet, da für alle Runs die gleichen Arriving Events benutzt werden
    public static List<Long> dwellTime=new LinkedList<>(); //Zeit die Fahrzeug x in Simulation verbringt
    public static List<Long> waitingTime= new LinkedList<>(); //Zeit die Fahrzeug x in Warteschlange verbringt
    public static List<Long> processTime= new LinkedList<>(); //Zeit die Fahrzeug x beim Testen benötigt


    /**
     * ###########################################################
     * ###########################################################
     * ##############_______METHODEN_______######################
     * ###########################################################
     * ###########################################################
     *
     */

    /**
     * Hauptmethode
     * wird von der main aus gestartet -> läuft bis alle Events der eventList abgearbeitet wurden
     *
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

    private static void checkForNewArrivingEvents(){
        if(!allArrivalEvents.isEmpty()) {
            if(allArrivalEvents.get(0).getTimestampOfExecution()< TimeManager.getElapsedTimeInMilliSeconds())
                queue.add(allArrivalEvents.remove(0));
        }
    }
    private static AllComparators.QueueType currentQueueType;
    /**
     * wird von der main aus vor jedem Simulationsdurchlauf aufgerufen
     * Reset/Setup Funktion
     */
    public static void setupRun(Comparator<ArrivingAtTheTestStation> comparator, AllComparators.QueueType type){
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
        dwellTime= new LinkedList<>();
        processTime= new LinkedList<>();
        waitingTime= new LinkedList<>();
        }


    /**
     * generiert Arriving Events
     * ein Event alle 1200-1800ms bis 72000ms erreicht sind
     * da bei diesem Maßstab 10ms simZeit = 1sek echtZeit gilt
     * 1min * 120 = 60 sek * 120 = 600ms *120 = 72000ms
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

    public static boolean isGeneratedEvents() {
        return generatedEvents;
    }

    public static void setGeneratedEvents(boolean generatedEvents) {
        SimulationManager.generatedEvents = generatedEvents;
    }


    private static List<String> singleRunData= new LinkedList<>();
    /**
     * wird immer ausgeführt wenn ein Event abgearbeitet wurde (Logging)
     * log wird in singleRunData geschrieben welches am Ende in einer .csv landet
     * @param event
     */
    public static void printEvent(Event event){

        String str= runID+";"+TimeManager.formatTimeFromMilliSecondsToSeconds(event.getTimestampOfExecution())+";"+ event.getCarID()+";"+event.getEventClass().getSimpleName();
        System.out.println(str);
        singleRunData.add(str);

    }
    /**
     * Methoden die mit der Datenanalyse zusammenhängen
     *
     * @return
     */

    public static List<String> getDwellTime() {
        List<String> dwellTimeData= new LinkedList<>();
        for(Long l: dwellTime){

            dwellTimeData.add((runID-1)+";"+ l)   ;
        }
        return dwellTimeData;
    }

    public static int getRunID() {
        return runID;
    }

    public static List<String> getSingleRunDataLogs() {
        return singleRunData;
    }
    public static String getSingleRunData() {

        return runID+";"+calcAvgOfLongList(dwellTime)+";"+ calcAvgOfLongList(processTime)+";"+ calcAvgOfLongList(waitingTime);


    }
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
                System.out.println(event.getTimeToSpentOnTesting()+":");
            }
        }


    }


}
