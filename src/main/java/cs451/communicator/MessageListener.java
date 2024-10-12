package cs451.communicator;

import cs451.Host;

/**
 * MessageListener
 */
public interface MessageListener {
    void send(Host dest, String message);
    void deliver(Host src, String packet);
}