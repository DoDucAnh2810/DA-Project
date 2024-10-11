package cs451.p2pLink;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import cs451.Host;
import cs451.Main;

public class FairLossLink implements MessageListener {
    MessageListener app;
    public DatagramSocket socket;

    FairLossLink(MessageListener app, int port){
        this.app = app;
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println("FairLossLink: Unable to create socket");
            e.printStackTrace();
        }

        Thread UDPreceiver = new Thread(() -> {UDPreceive();});
        UDPreceiver.start();
    }

    public void UDPsend(String message, String ip, int port) {
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

    public void UDPreceive() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            while (Main.running.get()) {
                try {socket.receive(packet);}
                catch (SocketException e) {System.exit(1);}
                String message = new String(packet.getData(), 0, packet.getLength());

                InetAddress srcAddress = packet.getAddress(); 
                int srcPort = packet.getPort();        
                String srcHash = srcAddress.toString() + srcPort;
                Host src = Host.hashLookup(srcHash);

                deliver(src, message);
            }
        } catch (IOException e) {
            System.err.println("FairLossLink: Unable to receive a message");
            e.printStackTrace();
        }
    }

    @Override
    public void send(Host dest, String message) {
        UDPsend(message, dest.getIp(), dest.getPort());
    }

    @Override
    public void deliver(Host src, String packet) {
        app.deliver(src, packet);
    }
}