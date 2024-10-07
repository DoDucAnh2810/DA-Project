package cs451;

/**
 * MessageListener
 */
public interface MessageListener {
    void send(Host dest, String message);
    void deliver(Host src, String packet);
}