package cs451.p2pLink;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

import cs451.Host;
import cs451.communication.Deliverable;
import cs451.communication.Message;
import cs451.communication.MessageListener;

public class GroupedLink extends MessageListener {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private PerfectLink pp2p;
    private Deliverable app;
    private int myId, groupNum;
    private ConcurrentHashMap<Integer, List<Message>> waiting;
    private ConcurrentHashMap<Integer, Timer> lastModTime;


    public GroupedLink(Deliverable app, int myId) {
        this.pp2p = new PerfectLink(this, myId);
        this.app = app;
        this.myId = myId;
        this.groupNum = -1;
        this.waiting = new ConcurrentHashMap<>();
        for (int id : Host.idSet())
            this.waiting.put(id, new ArrayList<>());
        this.lastModTime = new ConcurrentHashMap<>();
        for (int id : Host.idSet())
            this.lastModTime.put(id, new Timer());

        executor.scheduleAtFixedRate(() -> {
            for (Integer destId : waiting.keySet()) {
                if (!waiting.get(destId).isEmpty() &&
                    lastModTime.get(destId).getElapsedTimeMillis() > 1000) {
                    sendGroup(destId);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }


    public synchronized void sendGroup(int destId) {
        if (waiting.get(destId).isEmpty())
            return;
        pp2p.send(Host.idLookup(destId), 
                  new Message(myId, 
                              groupNum++, 
                              Message.groupSerialization(waiting.get(destId)),
                              Message.GroupedMes));
        waiting.get(destId).clear();
        lastModTime.get(destId).reset();
    }


    @Override
    public synchronized void send(Host dest, Message message) {
        int destId = dest.getId();
        waiting.get(destId).add(message);
        if (waiting.get(destId).size() == 8)
            sendGroup(destId);
    }


    @Override
    public void deliver(Host src, Message message) {
        for (Message m : Message.groupDeserialization(message.content())) {
            m.setSrcId(message.srcId());
            app.deliver(src, m);
        }
    }


    @Override
    public void closeConnection() {
        executor.shutdownNow();
        pp2p.closeConnection();
    }
}