package cs451.broadcast;

import java.util.HashMap;

import cs451.Host;
import cs451.communication.Deliverable;
import cs451.communication.Message;
import cs451.communication.MessageBroadcaster;
import cs451.p2pLink.GroupedLink;

/**
 * UniformBroadcast
 */
public class UniformBroadcast extends MessageBroadcaster {
    private GroupedLink gp2p;
    private Deliverable app;
    private int myId;
    private HashMap<String, Integer> ack;


    public UniformBroadcast(Deliverable app, int myId) {
        this.gp2p = new GroupedLink(this, myId);
        this.app = app;
        this.myId = myId;
        this.ack = new HashMap<String, Integer>();
    }

    private void acknowledge(Host src, Message message) {
        String hash = message.toString();
        Integer value = ack.get(hash); 
        if (value == null)
            ack.put(hash, 1);
        else
            ack.put(hash, ++value);
        if (value > Host.count()/2) {
            ack.remove(hash);
            app.deliver(src, message);
        }
    }

    @Override
    public void broadcast(Message message) {
        for (Host host : Host.hostList())
            if (host.getId() != myId)
                gp2p.send(host, message);
        acknowledge(Host.idLookup(myId), message);
    }


    @Override
    public void deliver(Host src, Message message) {
        for (Host host : Host.hostList())
            if (host.getId() != myId && 
                host.getId() != src.getId())
                gp2p.send(host, message);
        acknowledge(src, message);
    }


    @Override
    public void closeConnection() {
        gp2p.closeConnection();
    }
}
