package cs451.broadcast;

import java.util.HashSet;
import java.util.List;

import cs451.Host;
import cs451.communication.Deliverable;
import cs451.communication.Message;
import cs451.communication.MessageBroadcaster;

/**
 * FIFOUniformBroadcast
 */
public class FIFOUniformBroadcast extends MessageBroadcaster {
    private UniformBroadcast urb;
    private Deliverable app;
    private int myId;
    private HashSet<String> delivered;
    private List<Message> past; 


    public FIFOUniformBroadcast(Deliverable app, int myId) {
        this.urb = new UniformBroadcast(this, myId);
        this.app = app;
        this.myId = myId;
    }


    @Override
    public void broadcast(Message message) {
        throw new UnsupportedOperationException("Unimplemented method 'broadcast'");
    }


    @Override
    public void deliver(Host src, Message message) {
        app.deliver(src, message);
        throw new UnsupportedOperationException("Unimplemented method 'deliver'");
    }


    @Override
    public void closeConnection() {
        urb.closeConnection();
    }
}
