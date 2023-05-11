import java.util.PriorityQueue;

public class Testing extends Event{

    private long startOfTesting;
    private int arrivedAt;
    private final int max_TimeHandingOutNotification= 1200;
    private final int min_TimeHandingOutNotification= 600;


    public Testing(int timestampOfExecution, int carID, int numberOfPeopleInCar, long startOfTesting, int arrivedAt){

        super(timestampOfExecution, carID, numberOfPeopleInCar, Testing.class);
        this.startOfTesting=startOfTesting;
        this.arrivedAt=arrivedAt;

    }

    public PriorityQueue<Event> process(PriorityQueue<Event> eventList){
        SimulationManager.setCurrentState(SimulationManager.currentStateEnum.TESTING);


        long timeForHandingOutTestNotification= NumberGenerator.generateRandomNumber(max_TimeHandingOutNotification,min_TimeHandingOutNotification);
        long timeToTestAllPerson= (240L * super.getNumberOfPeopleInCar());
        long timeToSpentOnTesting = timeForHandingOutTestNotification +timeToTestAllPerson;
        long timeElapsed= System.currentTimeMillis()- this.getStartOfTesting();

        if(timeToSpentOnTesting< timeElapsed){

            System.out.println("Done Testing For \n"+ this);
            System.out.println("timeToSpentOnTesting: "+ timeToSpentOnTesting+ "("+timeForHandingOutTestNotification+","+timeToTestAllPerson+")"+" | time Elapsed "+ timeElapsed);
            eventList.remove(this);
            eventList.add(new LeavingTheStation(Integer.MAX_VALUE, super.getCarID(), super.getNumberOfPeopleInCar(), true, this.getArrivedAt(), (int) TimeManager.getElapsedTimeInSeconds()));
            SimulationManager.setCurrentState(SimulationManager.currentStateEnum.IDLE);

        }
        return eventList;
    }

    public long getStartOfTesting() {
        return startOfTesting;
    }

    public void setStartOfTesting(long startOfTesting) {
        this.startOfTesting = startOfTesting;
    }

    public int getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(int arrivedAt) {
        this.arrivedAt = arrivedAt;
    }
}
