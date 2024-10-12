package cs451.communicator;

import cs451.Host;
import cs451.Message;
import cs451.p2pLink.Packet;

public interface MessagePacketer {
    void send(Host dest, Message message);
    void deliver(Host src, Packet packet);
}
