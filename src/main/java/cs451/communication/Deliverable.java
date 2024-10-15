package cs451.communication;

import cs451.Host;

/**
 * Deliverable
 */
public interface Deliverable {
    public void deliver(Host src, Message message);
}