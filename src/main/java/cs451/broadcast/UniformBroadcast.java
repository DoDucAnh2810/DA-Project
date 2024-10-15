package cs451.broadcast;

import cs451.Host;
import cs451.Message;
import cs451.communicator.Deliverable;
import cs451.communicator.MessageBroadcaster;

/**
 * UniformBroadcast
 */
public class UniformBroadcast extends MessageBroadcaster {
    private BestEffortBroadcast beb;
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
