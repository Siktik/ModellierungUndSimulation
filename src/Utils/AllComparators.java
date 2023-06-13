package Utils;

import events.ArrivingAtTheTestStation;

import java.util.Comparator;
import java.util.Map;

/**
 * All Comparators used in the PriorityQueues. Queues are initialized by passing a Comparator, the Simulation gets the
 * current Comparator according the the simulation configuration from here.
 */
public class AllComparators {


    private static Comparator<ArrivingAtTheTestStation> FIFO= new Comparator<ArrivingAtTheTestStation>() {
        @Override
        public int compare(ArrivingAtTheTestStation o1, ArrivingAtTheTestStation o2) {
            if(o1.getTimestampOfExecution()<o2.getTimestampOfExecution())
                return -1;
            if(o1.getTimestampOfExecution()>o2.getTimestampOfExecution())
                return 1;
            return 0;
        }
    };
    private static Comparator<ArrivingAtTheTestStation> LIFO= new Comparator<ArrivingAtTheTestStation>() {
        @Override
        public int compare(ArrivingAtTheTestStation o1, ArrivingAtTheTestStation o2) {
            if(o1.getTimestampOfExecution()<o2.getTimestampOfExecution())
                return 1;
            if(o1.getTimestampOfExecution()>o2.getTimestampOfExecution())
                return -1;
            return 0;
        }
    };
    private static Comparator<ArrivingAtTheTestStation> SPT= new Comparator<ArrivingAtTheTestStation>() {
        @Override
        public int compare(ArrivingAtTheTestStation o1, ArrivingAtTheTestStation o2) {
            if(o1.getTimeToSpendOnTesting()<o2.getTimeToSpendOnTesting())
                return -1;
            if(o1.getTimeToSpendOnTesting()>o2.getTimeToSpendOnTesting()){
                return 1;
            }
            return 0;
        }
    };
    private static Comparator<ArrivingAtTheTestStation> LPT= new Comparator<ArrivingAtTheTestStation>() {
        @Override
        public int compare(ArrivingAtTheTestStation o1, ArrivingAtTheTestStation o2) {
            if(o1.getTimeToSpendOnTesting()<o2.getTimeToSpendOnTesting())
                return 1;
            if(o1.getTimeToSpendOnTesting()>o2.getTimeToSpendOnTesting()){
                return -1;
            }
            return 0;
        }
    };
    private static final Map<QueueType,Comparator<ArrivingAtTheTestStation>> allComparators= Map.of(QueueType.FIFO,getFIFO(),QueueType.LIFO,getLIFO(),QueueType.LPT,getLPT(),QueueType.SPT, getSPT());


    public static Map<QueueType,Comparator<ArrivingAtTheTestStation>> getAllComparators() {
        return allComparators;
    }

    private static Comparator<ArrivingAtTheTestStation> getFIFO() {
        return FIFO;
    }
    private static Comparator<ArrivingAtTheTestStation> getLIFO() {
        return LIFO;
    }
    private static Comparator<ArrivingAtTheTestStation> getSPT() {
        return SPT;
    }
    private static Comparator<ArrivingAtTheTestStation> getLPT() {
        return LPT;
    }

    public enum QueueType{
        FIFO,
        LIFO,
        SPT,
        LPT
    }
}
