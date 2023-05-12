package simManagement;

import Utils.NumberGenerator;
import Utils.TimeManager;
import events.ArrivingAtTheTestStation;
import events.Event;

import java.text.DecimalFormat;
import java.util.*;

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
     * eventList verwaltet alle Events die abgearbeitet werden sollen. Hierbei steht das event mit dem kleinsten
     * Zeitstempel immer an erster Stelle und wird in der run Methode in der entsprechenden while Schleife aus der Queue entfernt wenn
     * der Zeitstempel laut Simulationszeit überschritten wurde
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
    /**
     * da wir mehrere Runs machen mit verschiedenen Testing Lane Kapazitäten aber vergleichbarkeit zwischen den Runs haben wollen werden die Arriving events
     * nur einmal initalisiert und in dieser Liste gespeichert. Zu Beginn eines Runs werden alle ihre Elemente in die eventList geschrieben
     */
    private static List<Event> arrivalEventsForEveryRun;
    private static final int[] allQueueSizes= {10,12,14,16,20};
    private static int runID;
    private static int carCounter =0;   //static car ID Counter
    private static int maxQueueSize;    //maximale Kapazität der Testing Lane, zu beginn eines Runs festgelegt über allQueueSizes
    private static boolean generatedEvents;     //Hilfsvariable wird in der main abgefragt ob das generieren der Events geklappt hat
    private static final int min_Value= 300;    //minimaler Wert für Abstand zwischen Arriving Event -> 300ms -> 30 sek
    private static final int max_value= 1200;   //maximaler Wert für Abstand zwischen Arriving Event -> 1200ms -> 120 sek

    /**
     * wird benutzt um zu speichern wie viele Fahrzeuge gerade getestet werden, dieser Wert ist daher immer <= maxQueueSize
     */
    private static int inTestingLane =0;


    /**
     * Vars and Lists for Analysis
     */
    private static double amountOfPeopleInACar=0;       //wird einmalig bei generateEvents() berechnet, da für alle Runs die gleichen Arriving Events benutzt werden
    private static int carsThatCouldNotHaveBeenTested=0;    //..
    private static List<Long> dwellTime=new LinkedList<>(); //alle Zeiten die während eines Durchlaufs in der testingLane verbracht werden
    private static long timestampOfLastUpdateOnTestingLaneAmount=0; //benötigt um die Verteilung der Wartezeit in der Testing Lane zu berechnen
    private static HashMap<Integer, Long> amountOfVehiclesInSystemOverTime= new HashMap<>();    //speichert Wartezeit: Key-> inTestingLane, Value: akkumulierte Wartezeit
    public static List<Integer> amountOfCarsInTestingLane= new LinkedList<>();  //benötigt um den Durschnitt zu berechnen, wird immer dann geprüft wenn QueueGröße sich verändert


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
        while (!eventList.isEmpty()) {


            Event currentEvent;
            if (eventList.peek().getTimestampOfExecution() < TimeManager.getElapsedTimeInMilliSeconds())
                currentEvent = eventList.poll();        //Zeitstempel des Events mit dem frühesten Zeitpunkt ist kleiner als die Simulationszeit und wird daher verarbeitet
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

    /**
     * wird von der main aus vor jedem Simulationsdurchlauf aufgerufen
     * Reset/Setup Funktion
     */
    public static void setupRun(){
        carCounter=0;
        inTestingLane =0;
        eventList.clear();
        eventList.addAll(arrivalEventsForEveryRun);     //wie bei Variablen beschrieben
        maxQueueSize= allQueueSizes[runID];             //runID startet bei null wird am ende eines Durchlaufs in der run Methode inkrementiert



        //used for Data Analysis
        carsThatCouldNotHaveBeenTested=0;
        singleRunData=new LinkedList<>();
        dwellTime= new LinkedList<>();
        amountOfVehiclesInSystemOverTime= new HashMap<>();
        timestampOfLastUpdateOnTestingLaneAmount=0;
        amountOfCarsInTestingLane= new LinkedList<>();
    }


    /**
     * generiert Arriving Events
     * ein Event alle 300-1200ms bis 72000ms erreicht sind
     * da bei diesem Maßstab 10ms simZeit = 1sek echtZeit gilt
     * 1min * 120 = 60 sek * 120 = 600ms *120 = 72000ms
     */


    public static void generateEvents(){

        System.out.println("Generating Arriving Events");
        int i= 0;
        arrivalEventsForEveryRun= new LinkedList<>();

        while(i<=72000){   // 2 minutes in milliseconds     //ein Event alle 30-120 sek (Echtzeit), alle 300-1200 ms (SimZeit)
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

    public static boolean isGeneratedEvents() {
        return generatedEvents;
    }

    public static void setGeneratedEvents(boolean generatedEvents) {
        SimulationManager.generatedEvents = generatedEvents;
    }

    public static void decreaseQueueCounter(){
        updateTimeOfTestingLaneData();      //für das Histogramm der Wartezeiten

        inTestingLane--;
        SimulationManager.addCarsInTestingLane();
    }

    public static int getQueueCounter(){return inTestingLane;}

    public static void increaseQueueCounter(){
        updateTimeOfTestingLaneData();
        inTestingLane++;
        SimulationManager.addCarsInTestingLane();
    }

    public static int getMaxQueueSize(){return maxQueueSize;}


    private static List<String> singleRunData= new LinkedList<>();
    /**
     * wird immer ausgeführt wenn ein Event abgearbeitet wurde (Logging)
     * log wird in singleRunData geschrieben welches am Ende in einer .csv landet
     * @param event
     */
    public static void printEvent(Event event){

        String str= runID+";"+TimeManager.formatTimeFromMilliSecondsToSeconds(event.getTimestampOfExecution())+";"+ event.getCarID()+";"+event.getEventClass().getSimpleName()+";"+ inTestingLane;
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

    public static List<String> getTestingLaneData(){
        List<String> testingLaneData= new LinkedList<>();
        for(int i=0; i<= maxQueueSize; i++){
            testingLaneData.add((runID-1)+";"+i+";"+ amountOfVehiclesInSystemOverTime.get(i));
        }
        return testingLaneData;
    }

    public static String getSingleRunData(){
        if(runID-1>=0) {
            double inTestingLaneOnAverage= 0;
            for(int i=0; i< amountOfCarsInTestingLane.size(); i++){
                inTestingLaneOnAverage+= amountOfCarsInTestingLane.get(i);
            }
            inTestingLaneOnAverage= inTestingLaneOnAverage/ amountOfCarsInTestingLane.size();

            DecimalFormat df = new DecimalFormat("#.###");
            return (runID-1) + ";" + getAllQueueSizes()[runID - 1] + ";" + df.format(amountOfPeopleInACar) + ";" + getCarsThatCouldNotHaveBeenTested()+ ";"+df.format(inTestingLaneOnAverage);
        }
        return "Empty Result";
    }
    public static void updateTimeOfTestingLaneData(){
        long currentTime= TimeManager.getElapsedTimeInMilliSeconds();
        long timeSinceLastUpdate= currentTime-timestampOfLastUpdateOnTestingLaneAmount;
        timestampOfLastUpdateOnTestingLaneAmount= currentTime;
        if(amountOfVehiclesInSystemOverTime.containsKey(inTestingLane)){
            long time= amountOfVehiclesInSystemOverTime.get(inTestingLane);
            amountOfVehiclesInSystemOverTime.replace(inTestingLane, time+timeSinceLastUpdate);
        }else{
            amountOfVehiclesInSystemOverTime.put(inTestingLane, timeSinceLastUpdate);
        }
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
    public static List<String> getSingleRunDataLogs() {
        return singleRunData;
    }

    public static int getRunID() {
        return runID;
    }
    public static void addDwellTime(long time){
        dwellTime.add(time);
    }
    public static void addCarsInTestingLane(){
        amountOfCarsInTestingLane.add(inTestingLane);
    }


}
