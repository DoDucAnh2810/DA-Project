package cs451;

import java.io.FileWriter;
import java.io.PrintWriter;

import cs451.p2pLink.GroupedLink;

import java.io.IOException;

public class App implements MessageListener {
    GroupedLink gp2p;
    PrintWriter writer;

    App(int myId, String output) {
        gp2p = new GroupedLink(this, myId);
        try {
            writer = new PrintWriter(new FileWriter(output));
        } catch (IOException e) {
            System.err.println("App: Cannot start");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void send(Host dest, Message message) {
        gp2p.send(dest, message);
        writer.println("b " + message.sequenceNum());
        writer.flush();
    }

    @Override
    public void deliver(Host src, Message message) {
        writer.println("d " + message.processId() + " " + message.sequenceNum());
        writer.flush();
    }

    public void closeSocket() {
        gp2p.closeSocket();
    }
}