package cs451.broadcast;

import cs451.Host;
import cs451.Message;
import cs451.communicator.Deliverable;
import cs451.communicator.MessageBroadcaster;
import cs451.communicator.MessageListener;

/**
 * FIFOUniformBroadcast
 */
public class FIFOUniformBroadcast extends MessageBroadcaster {
    private UniformBroadcast urb;
    private Deliverable app;


    @Override
    public void broadcast(Message message) {
        throw new UnsupportedOperationException("Unimplemented method 'broadcast'");
    }


    @Override
    public void deliver(Host src, Message message) {
        throw new UnsupportedOperationException("Unimplemented method 'deliver'");
    }


    @Override
    public void closeConnection() {
        throw new UnsupportedOperationException("Unimplemented method 'closeConnection'");
    }
}
