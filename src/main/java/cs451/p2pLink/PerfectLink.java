package cs451.p2pLink;

import java.util.HashMap;
import java.util.HashSet;

import cs451.Host;
import cs451.communication.Deliverable;
import cs451.communication.Message;
import cs451.communication.MessageListener;

public class PerfectLink extends MessageListener {
    int myId;
    FairLossLink flp2p;
    Deliverable app;
    HashSet<String> delivered;
    HashMap<Integer, HashSet<String>> received; 


    public PerfectLink(Deliverable app, int myId) {
        this.myId = myId;
        this.flp2p = new FairLossLink(this, myId);
        this.app = app;
        this.delivered = new HashSet<String>();
        this.received = new HashMap<Integer, HashSet<String>>();
        for (int id : Host.idSet())
            this.received.put(id, new HashSet<String>());
    }


    @Override
    public void send(Host dest, Message message) {
        while (!received.get(dest.getId()).contains(message.hash()))
            flp2p.send(dest, message);
    }


    @Override   
    public void deliver(Host src, Message message) {
        String hash = message.hash();
        if (!message.isPerfectAck()) {
            if (!delivered.contains(hash)) {
                app.deliver(src, message);
                delivered.add(hash);
            }
            flp2p.send(src, new Message(message, Message.PerfectAck));
        } else
            received.get(src.getId()).add(hash);
    }

    
    @Override
    public void closeConnection() {
        flp2p.closeConnection();
    }
}