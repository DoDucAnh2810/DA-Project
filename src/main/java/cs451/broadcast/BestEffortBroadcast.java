package cs451.broadcast;

import cs451.Host;
import cs451.Message;
import cs451.communicator.Deliverable;
import cs451.communicator.MessageBroadcaster;
import cs451.p2pLink.GroupedLink;

/**
 * BestEffortBroadcast
 */
public class BestEffortBroadcast extends MessageBroadcaster {
    private GroupedLink gp2p;
    private Deliverable app;


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
        gp2p.closeConnection();
    }
}