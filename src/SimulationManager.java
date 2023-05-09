import events.ArrivingAtTheTestStation;
import events.Event;

import java.sql.Time;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class SimulationManager {


    static boolean generatedEvents;


    /*
         nach jeder add und remove operation wird passend sortiert
         wo werden Testing und Leaving gespeichert? müssen die überhaupt gespeichert werden
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



    public static void generateEvents(){

      /*
        generate all Arriving Events for Sim
        alles in die eventList
        ka wie 10er Warteschlange gemeint ist, ob das implizit gegeben ist durch die zeit die für testen etc. benötigt wird und da fahrzeuge ja nur alle 30 sek - 120 sek ankommen
        statt 120 min 120 sek?

       */

    }

    static Event currentEvent;

    public static void run(){

        while(true){

            currentEvent= eventList.poll(); //wenn Liste leer dann hier null -> abbruch da alle ArrivingEvents bearbeitet;
            if(currentEvent!=null){

                currentEvent.processEvent();

            }else{
                break;
            }
            
        }

        System.out.println("Finished Simulation, all Events are processed");


    }
}
