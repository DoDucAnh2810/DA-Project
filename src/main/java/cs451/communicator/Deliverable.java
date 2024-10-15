package cs451.communicator;

import cs451.Host;
import cs451.Message;

/**
 * Deliverable
 */
public interface Deliverable {
    public void deliver(Host src, Message message);
}