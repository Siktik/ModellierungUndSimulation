package simManagement;

import events.Event;

public class Server {

    Event testingInformation;
    int id;
    ServerState serverState;
    public Server(int id, Event testingInformation){
        this.id= id;
        this.testingInformation=testingInformation;
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

    public void setTestingInformation(Event testingInformation) {
        this.testingInformation = testingInformation;
    }
}
