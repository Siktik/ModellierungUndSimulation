package simManagement;

import Utils.DataCollection;
import Utils.TimeManager;
import events.ArrivingAtTheTestStation;
import events.Event;
import events.LeavingTheStation;
import events.Testing;

public class Server {

    Testing testingInformation;
    int id;
    ServerState serverState;
    public Server(int id){
        this.id= id;
        setServerState(ServerState.IDLE);
    }

    public Event getTestingInformation() {
        return testingInformation;
    }

    public int getId() {
        return id;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public void setTestingInformation(Testing testingInformation) {
        this.testingInformation = testingInformation;
    }

    public void evaluateCurrentState(){
        switch (serverState){
            case IDLE -> {
                if(!SimulationManager.queue.isEmpty()){

                    SimulationManager.printQueueTimeStamps();
                    setServerState(ServerState.TESTING);
                    ArrivingAtTheTestStation arrivingAtTheTestStation= SimulationManager.queue.poll();
                    System.out.println("Arrival Time: " + arrivingAtTheTestStation.getTimestampOfExecution());
                    System.out.println("Current Time: " + TimeManager.getElapsedTimeInMilliSeconds());
                    System.out.println("Testing Time: "+ arrivingAtTheTestStation.getTimeToSpendOnTesting());
                    DataCollection.writeLogEntry(arrivingAtTheTestStation);
                    SimulationManager.waitingTime.add(TimeManager.getElapsedTimeInMilliSeconds()-arrivingAtTheTestStation.getTimestampOfExecution());
                    testingInformation= new Testing(TimeManager.getElapsedTimeInMilliSeconds(), arrivingAtTheTestStation.getCarID(), arrivingAtTheTestStation.getNumberOfPeopleInCar(), arrivingAtTheTestStation.getTimeToSpendOnTesting(), arrivingAtTheTestStation.getTimestampOfExecution());
                    System.out.println("Server" + id+" changes to Testing, took \n"+ arrivingAtTheTestStation+" \n from queue,  testing now for "+ testingInformation.getTimeToSpentOnTesting());
                }
            }
            case TESTING -> {
                if((testingInformation.getTimeToSpentOnTesting()+ testingInformation.getTimestampOfExecution())< TimeManager.getElapsedTimeInMilliSeconds()){
                    setServerState(ServerState.IDLE);
                    System.out.println("Server" + id+" changes to IDLE, finished \n"+ testingInformation+" \n from queue,  sending home ");

                    LeavingTheStation leavingTheStation= new LeavingTheStation(TimeManager.getElapsedTimeInMilliSeconds(),
                            testingInformation.getCarID(),testingInformation.getNumberOfPeopleInCar(), true);
                    SimulationManager.dwellTime.add(leavingTheStation.getTimestampOfExecution()- testingInformation.getTimeWhenJoinedQueue());
                    leavingTheStation.process();

                }
            }
        }
    }
    public void evaluateCurrentStateMQ(){
        switch (serverState){
            case IDLE -> {


                if(!SimulationManager.multiQueue.get(this.id).isEmpty()){
                    SimulationManager.printMultiQueueTimeStamps();
                    System.err.println("SERVER " + this.id + " IS WORKING ON OWN QUEUE");

                    setServerState(ServerState.TESTING);
                    ArrivingAtTheTestStation arrivingAtTheTestStation= SimulationManager.multiQueue.get(this.id).poll();

                    DataCollection.writeLogEntry(arrivingAtTheTestStation);
                    SimulationManager.waitingTime.add(TimeManager.getElapsedTimeInMilliSeconds()-arrivingAtTheTestStation.getTimestampOfExecution());
                    testingInformation= new Testing(TimeManager.getElapsedTimeInMilliSeconds(), arrivingAtTheTestStation.getCarID(), arrivingAtTheTestStation.getNumberOfPeopleInCar(), arrivingAtTheTestStation.getTimeToSpendOnTesting(), arrivingAtTheTestStation.getTimestampOfExecution());
                    System.out.println("Server" + id+" changes to Testing, took \n"+ arrivingAtTheTestStation+" \n from queue,  testing now for "+ testingInformation.getTimeToSpentOnTesting());
                }else {
                    //System.err.println("SERVER " + this.id + " IS CHECKING OTHER QUEUES");
                    int longestQueueID = this.id;
                    for (Integer key : SimulationManager.multiQueue.keySet()) {
                        if (!SimulationManager.multiQueue.get(key).isEmpty()) {
                            if (SimulationManager.multiQueue.get(key).size() > SimulationManager.multiQueue.get(longestQueueID).size()) {
                                longestQueueID = key;
                            }
                        }
                    }

                    if (!(longestQueueID == this.id)) {
                        setServerState(ServerState.TESTING);
                        ArrivingAtTheTestStation arrivingAtTheTestStation = SimulationManager.multiQueue.get(longestQueueID).poll();
                        DataCollection.writeLogEntry(arrivingAtTheTestStation);
                        System.err.println("TAKING CAR FROM DIFFERENT QUEUE: SERVER "+ this.id + " FROM QUEUE "+longestQueueID);
                        SimulationManager.waitingTime.add(TimeManager.getElapsedTimeInMilliSeconds() - arrivingAtTheTestStation.getTimestampOfExecution());
                        testingInformation = new Testing(TimeManager.getElapsedTimeInMilliSeconds(), arrivingAtTheTestStation.getCarID(), arrivingAtTheTestStation.getNumberOfPeopleInCar(), arrivingAtTheTestStation.getTimeToSpendOnTesting(), arrivingAtTheTestStation.getTimestampOfExecution());
                        System.out.println("Server" + id + " changes to Testing, took \n" + arrivingAtTheTestStation + " \n from queue,  testing now for " + testingInformation.getTimeToSpentOnTesting());

                    }
                }
            }
            case TESTING -> {
                if((testingInformation.getTimeToSpentOnTesting()+ testingInformation.getTimestampOfExecution())< TimeManager.getElapsedTimeInMilliSeconds()){
                    setServerState(ServerState.IDLE);
                    System.out.println("Server" + id+" changes to IDLE, finished \n"+ testingInformation+" \n from queue,  sending home ");

                    LeavingTheStation leavingTheStation= new LeavingTheStation(TimeManager.getElapsedTimeInMilliSeconds(),
                            testingInformation.getCarID(),testingInformation.getNumberOfPeopleInCar(), true);
                    SimulationManager.dwellTime.add(leavingTheStation.getTimestampOfExecution()- testingInformation.getTimeWhenJoinedQueue());
                    leavingTheStation.process();

                }
            }
        }
    }
    public boolean isIdle(){
        return ServerState.IDLE == serverState;
    }
    public boolean isTesting(){
        return ServerState.TESTING == serverState;
    }
}
