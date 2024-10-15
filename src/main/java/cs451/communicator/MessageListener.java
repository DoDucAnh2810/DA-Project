package cs451.communicator;

import cs451.Host;
import cs451.Message;

/**
 * MessageListener
 */
public abstract class MessageListener implements Deliverable {
    abstract public void send(Host dest, Message message);
    abstract public void deliver(Host src, Message message);
    abstract public void closeConnection();
}