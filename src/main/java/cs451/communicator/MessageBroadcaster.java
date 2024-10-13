package cs451.communicator;

import cs451.Host;
import cs451.Message;

public interface MessageBroadcaster {
    void broadcast(Host dest, Message message);
    void deliver(Host src, Message message);
}
