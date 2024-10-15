package cs451.communication;

import cs451.Host;

/**
 * MessageListener
 */
public abstract class MessageListener implements Deliverable {
    abstract public void send(Host dest, Message message);
    abstract public void deliver(Host src, Message message);
    abstract public void closeConnection();
}