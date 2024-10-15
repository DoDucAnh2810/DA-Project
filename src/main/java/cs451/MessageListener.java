package cs451;

/**
 * MessageListener
 */
public interface MessageListener {
    void broadcast(Host dest, Message message);
    void deliver(Host src, Message message);
    void closeConnection();
}