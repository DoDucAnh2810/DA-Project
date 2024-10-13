package cs451;

/**
 * MessageListener
 */
public interface MessageListener {
    void send(Host dest, Message message);
    void deliver(Host src, Message message);
}