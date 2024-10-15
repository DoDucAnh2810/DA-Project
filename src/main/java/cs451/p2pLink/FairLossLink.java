package cs451.p2pLink;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import cs451.Host;
import cs451.Message;
import cs451.MessageListener;

public class FairLossLink implements MessageListener {
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private MessageListener app;
    private DatagramSocket socket;


    public FairLossLink(MessageListener app, int myId){
        this.app = app;
        try {
            this.socket = new DatagramSocket(Host.idLookup(myId).getPort());
        } catch (SocketException e) {
            System.err.println("FairLossLink: Unable to create socket");
            e.printStackTrace();
        }

        Thread UDPreceiver = new Thread(() -> {UDPreceive();});
        UDPreceiver.start();
    }


    private void UDPsend(Message message, String ip, int port) {
        byte[] buffer = message.getBytes();
        try {
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            try {socket.send(packet);}
            catch (SocketException e) {System.exit(1);}
        } catch (IOException e) {
            System.err.println("FairLossLink: Unable to send a message");
            e.printStackTrace();
        }
    }


    private void UDPreceive() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            while (running.get()) {
                try {socket.receive(packet);}
                catch (SocketException e) {System.exit(1);}

                InetAddress srcAddress = packet.getAddress(); 
                int srcPort = packet.getPort();        
                String srcHash = srcAddress.toString() + srcPort;
                Host src = Host.hashLookup(srcHash);

                Message message = new Message(packet.getData());
                
                deliver(src, message);
            }
        } catch (IOException e) {
            System.err.println("FairLossLink: Unable to receive a message");
            e.printStackTrace();
        }
    }


    @Override
    public void broadcast(Host dest, Message message) {
        UDPsend(message, dest.getIp(), dest.getPort());
    }


    @Override
    public void deliver(Host src, Message message) {
        app.deliver(src, message);
    }


    @Override
    public void closeConnection() {
        running.set(false);
        socket.close();
    }
}