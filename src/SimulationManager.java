import events.ArrivingAtTheTestStation;
import events.Event;

import java.sql.Time;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class SimulationManager {


    static boolean generatedEvents;


    /*
         nach jeder add und remove operation wird passend sortiert
         wo werden Testing und Leaving gespeichert? müssen die überhaupt gespeichert werden
     */
    private static List<Event> allArrivingEvents;
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


    private static final int min_Value=300;      //milliseconds
    private static final int max_value=1200;
    public static void generateEvents(){

      /*
        ka wie 10er Warteschlange gemeint ist, ob das implizit gegeben ist durch die zeit die für testen etc. benötigt wird und da fahrzeuge ja nur alle 30 sek - 120 sek ankommen
        */
        System.out.println("Generating Arriving Events");
        int i= 0;
        allArrivingEvents= new LinkedList<>();


        while(i<=120000){   // 2 minutes in milliseconds
            int timeInterval= NumberGenerator.generateRandomNumber(max_value,min_Value);
            i+= timeInterval;
            allArrivingEvents.add(new ArrivingAtTheTestStation(i, carCounter++, NumberGenerator.generateRandomNumber(4,1)));
        }


        System.out.println("Generated a total of "+ allArrivingEvents.size()+" events");

        checkQueue();
        generatedEvents=true;


    }

    /**
     * should check if queue is smaller 10 and if there are Arriving Events that are need to be added to the queue because of timestamp Of Execution
     */
    public static void checkQueue(){

        int waitingCarsInQueue= (int)eventList
                .stream()
                .filter(
                        e-> e.getEventClass().isAssignableFrom(ArrivingAtTheTestStation.class)
                )
                .count();

        List<Event> readyToArriveAtQueue= allArrivingEvents
                .stream()
                .filter(
                        e-> e.getTimestampOfExecution()<TimeManager.getElapsedTimeInMilliSeconds()
                )
                .sorted(new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        if(o1.getTimestampOfExecution()<o2.getTimestampOfExecution())
                            return -1;
                        else if(o1.getTimestampOfExecution()>o2.getTimestampOfExecution())
                            return 1;
                        return 0;
                    }
                })
                .collect(Collectors.toList());


        int i=0;
        System.out.println("Currently Waiting Cars number is "+ waitingCarsInQueue);
        System.out.println("Therefore adding up to "+ (10-waitingCarsInQueue)+" cars");

        try {
            while (waitingCarsInQueue <= 10) {
                Event e = readyToArriveAtQueue.remove(i);
                System.out.println("Adding " + e.toString());
                eventList.add(e);
                i++;
                waitingCarsInQueue++;
            }

            System.out.println("Size of Waiting Cars is now " + (int) eventList
                    .stream()
                    .filter(
                            e -> e.getEventClass().isAssignableFrom(ArrivingAtTheTestStation.class)
                    )
                    .count());

        }catch(IndexOutOfBoundsException ignored){

        }
    }

    static Event currentEvent;

    public static void run(){

       TimeManager.start();

       while(!allArrivingEvents.isEmpty()){

           checkQueue();
           currentEvent= eventList.poll();
           if(currentEvent==null){
               continue;
           }


           System.out.println("Current Event is \n"+ currentEvent.toString());
           currentEvent.processEvent();






       }
        System.out.println("Finished Simulation, all Events are processed");

        TimeManager.stop();
    }
}
