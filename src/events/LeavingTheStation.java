public class LeavingTheStation extends Event{

    private int arrivedAt;
    private int timeOfLeaving;
    private boolean hasBeenTested;
    public LeavingTheStation (int timestampOfExecution, int carID, int numberOfPeopleInCar, boolean hasBeenTested, int arrivedAt, int timeOfLeaving){

        super(timestampOfExecution,carID,numberOfPeopleInCar, LeavingTheStation.class);
        this.hasBeenTested= hasBeenTested;
        this.arrivedAt= arrivedAt;
        this.timeOfLeaving= timeOfLeaving;
    }

    public boolean process(){
        return true;
    }

    public boolean isHasBeenTested() {
        return hasBeenTested;
    }

    public void setHasBeenTested(boolean hasBeenTested) {
        this.hasBeenTested = hasBeenTested;
    }

    public int getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(int arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public int getTimeOfLeaving() {
        return timeOfLeaving;
    }

    public void setTimeOfLeaving(int timeOfLeaving) {
        this.timeOfLeaving = timeOfLeaving;
    }
}
