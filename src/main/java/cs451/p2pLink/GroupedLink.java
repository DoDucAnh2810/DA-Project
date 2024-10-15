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


    public GroupedLink(MessageListener app, int myId) {
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
                       new Message(myId, groupNum--, 
                                   groupSerialization(waiting.get(destId))));
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
        for (Message m : groupDeserialization(message.content()))
            app.deliver(src, m);
    }


    @Override
    public void closeConnection() {
        executor.shutdownNow();
        pp2p.closeConnection();
    }


    public static byte[] groupSerialization(List<Message> messages) {
        int totalLength = 0;
        for (Message message : messages)
            totalLength += 12+message.length();
        byte[] buffer = new byte[totalLength];

        int start = 0;
        for (Message message : messages) {
            int processId = message.processId();
            int sequenceNum = message.sequenceNum();
            int length = message.length();
            int i = 0, j = 0;
            while (i < 4) {
                buffer[start+(i++)] = (byte) (processId & 0xFF);
                processId >>= 8;
            }
            while (i < 8) {
                buffer[start+(i++)] = (byte) (sequenceNum & 0xFF);
                sequenceNum >>= 8;
            }
            while (i < 12) {
                buffer[start+(i++)] = (byte) (length & 0xFF);
                length >>= 8;
            }
            while (i < 12 + message.length())
                buffer[start+(i++)] = message.content()[j++];
            start += 12+message.length();
        }

        return buffer;
    }

    
    public static List<Message> groupDeserialization(byte[] serialization) {
        List<Message> messages = new ArrayList<>();
        int start = 0;
        while (start < serialization.length) {
            int processId = 0, sequenceNum = 0, length = 0;

            for (int i = start+3; i >= start; i--)
                processId = (processId << 8) | (serialization[i] & 0xFF);
            for (int i = start+7; i >= start+4; i--)
                sequenceNum = (sequenceNum << 8) | (serialization[i] & 0xFF);
            for (int i = start+11; i >= start+8; i--)
                length = (length << 8) | (serialization[i] & 0xFF);

            byte[] content = new byte[length];
            int i = 0, j = start+12;
            while (i < length)
                content[i++] = serialization[j++];

            start += 12+length;
            messages.add(new Message(processId, sequenceNum, content));
        }

        return messages;
    }
}