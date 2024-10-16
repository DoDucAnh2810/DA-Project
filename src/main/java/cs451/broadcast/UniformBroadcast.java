package cs451.broadcast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
    private HashMap<String, Set<Integer>> ack;
    private HashSet<String> delivered;


    public UniformBroadcast(Deliverable app, int myId) {
        this.gp2p = new GroupedLink(this, myId);
        this.app = app;
        this.myId = myId;
        this.ack = new HashMap<String, Set<Integer>>();
    }


    private void acknowledge(Host src, Message message) {
        String hash = message.hash();
        if (ack.get(hash) == null) {
            ack.put(hash, new HashSet<Integer>());
            ack.get(hash).add(myId);
        }
        ack.get(hash).add(src.getId());
        if (ack.get(hash).size() > Host.count()/2) {
            app.deliver(src, message);
            delivered.add(hash);
            ack.remove(hash);
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
        if (delivered.contains(message.hash()))
            return;
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
