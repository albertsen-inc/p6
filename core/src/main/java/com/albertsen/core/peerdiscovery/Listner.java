package com.albertsen.core.peerdiscovery;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Listner {


    public void startListner() throws IOException {
        DatagramSocket socket = new DatagramSocket(8888);

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        System.out.println("Listening...");
        socket.receive(packet);
        String msg = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Received: " + msg);

        socket.close();
    }

}
