package cs451.communication;

import cs451.Host;

/**
 * MessageBroadcaster
 */
public abstract class MessageBroadcaster implements Deliverable {
    abstract public void broadcast(Message message);
    abstract public void deliver(Host src, Message message);
    abstract public void closeConnection();
}