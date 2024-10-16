package cs451.broadcast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import cs451.Host;
import cs451.communication.Deliverable;
import cs451.communication.Message;
import cs451.communication.MessageBroadcaster;

/**
 * FIFOUniformBroadcast
 */
public class FIFOUniformBroadcast extends MessageBroadcaster {
    private UniformBroadcast urb;
    private Deliverable app;
    private int myId, fifoNum;
    private HashSet<String> delivered;
    private List<Message> past; 


    public FIFOUniformBroadcast(Deliverable app, int myId) {
        this.urb = new UniformBroadcast(this, myId);
        this.app = app;
        this.myId = myId;
        this.fifoNum = 1;
        this.delivered = new HashSet<String>();
        this.past = Collections.synchronizedList(new ArrayList<Message>());
    }


    @Override
    public void broadcast(Message message) {
        past.add(message);
        urb.broadcast(new Message(myId,
                                  fifoNum++,
                                  Message.groupSerialization(past),
                                  Message.FIFOPstMes));
    }


    @Override
    public void deliver(Host src, Message fifo) {
        List<Message> pastM = Message.groupDeserialization(fifo.content());
        Message m = pastM.get(pastM.size()-1);
        if (delivered.contains(m.hash()))
            return;
        m.setSrcId(src.getId());
        for (Message n : pastM)
            if (!delivered.contains(n.hash())) {
                app.deliver(Host.idLookup(n.srcId()), n);
                delivered.add(n.hash());
                past.add(n);
            }
    }


    @Override
    public void closeConnection() {
        urb.closeConnection();
    }
}
