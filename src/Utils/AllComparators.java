package Utils;

import events.ArrivingAtTheTestStation;

import java.util.Comparator;
import java.util.List;

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
            if(o1.getTimeToSpentOnTesting()<o2.getTimeToSpentOnTesting())
                return -1;
            if(o1.getTimeToSpentOnTesting()>o2.getTimeToSpentOnTesting()){
                return 1;
            }
            return 0;
        }
    };
    private static Comparator<ArrivingAtTheTestStation> LPT= new Comparator<ArrivingAtTheTestStation>() {
        @Override
        public int compare(ArrivingAtTheTestStation o1, ArrivingAtTheTestStation o2) {
            if(o1.getTimeToSpentOnTesting()<o2.getTimeToSpentOnTesting())
                return 1;
            if(o1.getTimeToSpentOnTesting()>o2.getTimeToSpentOnTesting()){
                return -1;
            }
            return 0;
        }
    };
    private static List<Comparator> allComparators= List.of(FIFO,LIFO,SPT,LPT);


    public static List<Comparator> getAllComparators() {
        return allComparators;
    }

    public static Comparator<ArrivingAtTheTestStation> getFIFO() {
        return FIFO;
    }
    public static Comparator<ArrivingAtTheTestStation> getLIFO() {
        return LIFO;
    }
    public static Comparator<ArrivingAtTheTestStation> getSPT() {
        return SPT;
    }
    public static Comparator<ArrivingAtTheTestStation> getLPT() {
        return LPT;
    }

}
