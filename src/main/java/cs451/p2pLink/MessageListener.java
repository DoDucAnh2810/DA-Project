package cs451.p2pLink;

import cs451.Host;

/**
 * MessageListener
 */
public interface MessageListener {
    void send(Host dest, String message);
    void deliver(Host src, String packet);
}