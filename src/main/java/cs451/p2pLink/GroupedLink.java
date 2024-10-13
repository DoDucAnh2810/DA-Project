package cs451.p2pLink;

import java.util.List;

import cs451.Host;
import cs451.Message;
import cs451.MessageListener;

public class GroupedLink implements MessageListener {
    PerfectLink pp2p;
    List<Message> waiting;

    @Override
    public void send(Host dest, Message message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'send'");
    }

    @Override
    public void deliver(Host src, Message message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deliver'");
    }

    
}