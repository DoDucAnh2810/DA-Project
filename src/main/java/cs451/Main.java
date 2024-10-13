package cs451;

import java.util.concurrent.atomic.AtomicBoolean;

import cs451.dataType.Message;
import cs451.format.Parser;

public class Main {
    public static final AtomicBoolean running = new AtomicBoolean(true);
    static App app;

    private static void handleSignal() {
        running.set(false);
        app.closeSocket();
    }

    private static void initSignalHandlers() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                handleSignal();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        Parser parser = new Parser(args);
        parser.parse();
        initSignalHandlers();
        Host.initLookup(parser.hosts());
        app = new App(parser.myId(), parser.output());
        Host receiver = Host.idLookup(parser.receiverId());

        if (parser.myId() != parser.receiverId())
            for (int i = 1; i <= parser.nbMes(); i++)
                app.send(receiver, new Message(parser.myId(), i, null));

        while (true)
            Thread.sleep(60 * 60 * 1000);
    }
}
