package simManagement;

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
                    setServerState(ServerState.TESTING);
                    ArrivingAtTheTestStation arrivingAtTheTestStation= SimulationManager.multiQueue.get(this.id).poll();
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
    public boolean isIdle(){
        return ServerState.IDLE == serverState;
    }
    public boolean isTesting(){
        return ServerState.TESTING == serverState;
    }
}
