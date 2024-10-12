package cs451.communicator;

import cs451.Host;
import cs451.dataType.Message;
import cs451.dataType.Packet;

public interface MessagePacketer {
    void send(Host dest, Message message);
    void deliver(Host src, Packet packet);
}
