package cs451.communicator;

import cs451.Host;
import cs451.dataType.Packet;

public interface PacketLister {
    void send(Host dest, Packet packet);
    void deliver(Host src, Packet packet);
}
