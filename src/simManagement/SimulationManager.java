import events.ArrivingAtTheTestStation;
import events.Event;
import events.LeavingTheStation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class SimulationManager {


    static boolean generatedEvents;



    /*
         nach jeder add und remove operation wird passend sortiert
         wo werden events.Testing und Leaving gespeichert? müssen die überhaupt gespeichert werden
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
    private static int queue=0;
    private static final int min_Value;      //milliseconds
    private static final int max_value;
    private static Event currentEvent;
    private static currentStateEnum currentState= currentStateEnum.IDLE;

    public enum currentStateEnum{
        IDLE,
        TESTING
    }



    static {

        min_Value=300;
        max_value=1200;
    }

    public static void run(){

        TimeManager.start();
        while(true){

            SimulationManager.checkQueue();

            if(currentState==currentStateEnum.IDLE)
            currentEvent= eventList.peek();
            if(currentEvent==null)
                continue;
            if(allArrivingEvents.isEmpty()&&eventList.isEmpty())
                break;

            try {

                eventList = currentEvent.processEvent(eventList);

            }catch(IllegalStateException e){

                System.out.println("There is a Problem mate, this should not have happened -> passed a Eventclass that cant be processed");
            }



            List<Event> leavingTheStationEvents= eventList.stream().filter(e-> e.getEventClass().isAssignableFrom(LeavingTheStation.class)).collect(Collectors.toList());
            if(!leavingTheStationEvents.isEmpty()){

                for(Event e: leavingTheStationEvents){
                    LeavingTheStation leavingTheStation= (LeavingTheStation) e;
                    if(leavingTheStation.isHasBeenTested())
                        System.out.println("Car "+ e.getCarID() +" arrived at "+ e.getTimestampOfExecution()+ " is leaving and has been tested");
                    else
                        System.out.println("Car "+ e.getCarID() +" arrived at "+ e.getTimestampOfExecution()+ " is leaving and has not been tested");

                }
                eventList.removeAll(leavingTheStationEvents);

            }
      }
        System.out.println("End of Sim Run");
        TimeManager.stop();


    }
    public static void checkQueue(){

        List<Event> newArrivals= allArrivingEvents.stream().filter(e-> e.getTimestampOfExecution()< TimeManager.getElapsedTimeInMilliSeconds()).collect(Collectors.toList());
        if(queue<10) {
            while (queue < 10) {
                if (newArrivals.isEmpty())
                    break;
                Event e = newArrivals.remove(0);
                allArrivingEvents.remove(e);
                eventList.add(e);
                queue++;
                for (Event f : eventList) {
                    System.out.println(f);
                }
            }
            if(!newArrivals.isEmpty())
                releaseCarsNotFittingInQueue(newArrivals);
        }else{
            releaseCarsNotFittingInQueue(newArrivals);
        }
    }
    public static void releaseCarsNotFittingInQueue(List<Event> newArrivals){
        allArrivingEvents.removeAll(newArrivals);
        for(Event e: newArrivals){
            eventList.add(new LeavingTheStation(Integer.MAX_VALUE, e.getCarID(), e.getNumberOfPeopleInCar(),false, e.getTimestampOfExecution(), (int)TimeManager.getElapsedTimeInSeconds()));
        }
    }



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

        generatedEvents=true;


    }
    public static void decreaseQueueCounter(){queue--;}
    public static void setCurrentState(currentStateEnum stateToSet){currentState=stateToSet;}
}
