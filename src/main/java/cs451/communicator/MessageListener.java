package cs451.communicator;

import cs451.Host;
import cs451.Message;

/**
 * MessageListener
 */
public interface MessageListener {
    void send(Host dest, Message message);
    void deliver(Host src, Message message);
    void closeSocket();
}