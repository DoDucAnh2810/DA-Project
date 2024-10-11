package cs451.p2pLink;

import java.util.HashMap;
import java.util.HashSet;

import cs451.Constants;
import cs451.Host;

public class PerfectLink implements MessageListener {
    public FairLossLink flp2p;
    MessageListener app;
    HashSet<String> delivered;
    HashMap<Integer, HashSet<String>> received; 

    public PerfectLink(MessageListener app, int port) {
        this.flp2p = new FairLossLink(this, port);
        this.app = app;
        this.delivered = new HashSet<String>();
        this.received = new HashMap<Integer, HashSet<String>>();
        for (int id : Host.idSet())
            this.received.put(id, new HashSet<String>());
    }

    @Override
    public void send(Host dest, String message) {
        while (!received.get(dest.getId()).contains(message))
            flp2p.send(dest, Constants.MES + message);
    }

    @Override   
    public void deliver(Host src, String packet) {
        if (packet.isEmpty())
            return;

        char mesType = packet.charAt(0);
        String message = packet.substring(1);

        switch (mesType) {
            case Constants.MES:
                if (!delivered.contains(message)) {
                    app.deliver(src, message);
                    delivered.add(message);
                }
                flp2p.send(src, Constants.ACK + message);
                break;
            case Constants.ACK:
                received.get(src.getId()).add(message);
                break;
            default:
                System.err.println("PerfectLink: Invalid message type specified");  
        }
    }
}