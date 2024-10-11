package cs451.p2pLink;

import cs451.Host;
import cs451.Message;

public interface MessagePacketer {
    void send(Host dest, Message message);
    void deliver(Host src, Packet packet);
}
