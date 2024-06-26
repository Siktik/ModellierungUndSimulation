package simManagement;

import Utils.DataCollection;
import Utils.TimeManager;
import events.ArrivingAtTheTestStation;
import events.Event;
import events.LeavingTheStation;
import events.Testing;

/**
 * Class for the servers performing the tests at the station. The same Server class is used for Single- and Multi-Queue.
 */
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

    /**
     * Main functionality of the servers. Servers are either idle or testing. If idle, check queue for waiting cars and remove
     * the first if present. Set server state to TESTING, prepare logdata and proces the event.
     * If a server is Testing, he will continue to do so until the events processing time has passed.
     */
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

                    // ArrivingAtTheStation event is all that is needed to write the entore log including waiting,
                    // processing and dwell time
                    DataCollection.writeLogEntry(arrivingAtTheTestStation);

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
                    leavingTheStation.process();

                }
            }
        }
    }

    /**
     * Same Method as above but for the MultiQueue implementation.
     */
    public void evaluateCurrentStateMQ(){
        switch (serverState){
            case IDLE -> {
                if(!SimulationManager.multiQueue.get(this.id).isEmpty()){

                    SimulationManager.printMultiQueueTimeStamps();
                    setServerState(ServerState.TESTING);
                    ArrivingAtTheTestStation arrivingAtTheTestStation= SimulationManager.multiQueue.get(this.id).poll();

                    DataCollection.writeLogEntry(arrivingAtTheTestStation);

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
