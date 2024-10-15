package cs451.communicator;

import cs451.Host;
import cs451.Message;

/**
 * MessageBroadcaster
 */
public abstract class MessageBroadcaster implements Deliverable {
    abstract public void broadcast(Message message);
    abstract public void deliver(Host src, Message message);
    abstract public void closeConnection();
}