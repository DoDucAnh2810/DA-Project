package cs451.p2pLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs451.Host;
import cs451.Main;
import cs451.Message;
import cs451.MessageListener;
import cs451.Timer;

public class GroupedLink implements MessageListener {
    PerfectLink pp2p;
    MessageListener app;
    int myId, groupNum;
    HashMap<Integer, List<Message>> waiting;
    HashMap<Integer, Timer> lastModTime;


    public GroupedLink(MessageListener app, int myId) {
        this.pp2p = new PerfectLink(this, myId);
        this.app = app;
        this.myId = myId;
        this.groupNum = -1;
        this.waiting = new HashMap<>();
        for (int id : Host.idSet())
            this.waiting.put(id, new ArrayList<>());
        this.lastModTime = new HashMap<>();
        for (int id : Host.idSet())
            this.lastModTime.put(id, new Timer());

        Thread periodicSend = new Thread(() -> {
            while (Main.running.get()) {
                for (Integer destId : waiting.keySet())
                    if (lastModTime.get(destId).getElapsedTimeMillis() > 100)
                        sendGroup(destId);
            }
            try {
                Thread.sleep(100); // Sleep to prevent high CPU usage
            } catch (InterruptedException e) {
                System.exit(1);
            }
        });
        periodicSend.start();
    }

    public synchronized void sendGroup(int destId) {
        if (waiting.get(destId).isEmpty())
            return;
        pp2p.send(Host.idLookup(destId), 
                  new Message(myId, groupNum--, 
                              Message.groupSerialization(waiting.get(destId))));
        waiting.get(destId).clear();
        lastModTime.get(destId).reset();
    }

    @Override
    public void send(Host dest, Message message) {
        int destId = dest.getId();
        waiting.get(destId).add(message);
        if (waiting.get(destId).size() == 8)
            sendGroup(destId);
    }

    @Override
    public void deliver(Host src, Message message) {
        for (Message m : Message.groupDeserialization(message.content()))
            app.deliver(src, m);
    }

    public void closeSocket() {
        pp2p.closeSocket();
    }
}