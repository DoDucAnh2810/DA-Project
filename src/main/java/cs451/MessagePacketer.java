package cs451;

public interface MessagePacketer {
    void send(Host dest, Message message);
    void deliver(Host src, Packet packet);
}
