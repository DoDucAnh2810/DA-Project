package cs451;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class App implements MessageListener {
    PerfectLink pp2p;
    PrintWriter writer;

    App(int port, String output) {
        pp2p = new PerfectLink(this, port);
        try {
            writer = new PrintWriter(new FileWriter(output));
        } catch (IOException e) {
            System.err.println("App: Cannot start");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void send(Host dest, String message) {
        pp2p.send(dest, message);
        writer.println("b " + message);
        writer.flush();
    }

    @Override
    public void deliver(Host src, String message) {
        writer.println("d " + src.getId() + " " + message);
        writer.flush();
    }
}