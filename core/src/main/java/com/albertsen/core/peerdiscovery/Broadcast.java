package com.albertsen.core.peerdiscovery;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Broadcast {

    //todo fix datagram packets (username, identifier)
    //todo way to activate broadcast
    public void sendMsgBroadcast(String msg) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = msg.getBytes();

        //get all interfaces (wifi,eathernet ...)
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();


        while (interfaces.hasMoreElements()) {
            NetworkInterface temp = interfaces.nextElement();
            //skip useless
            if (!temp.isUp() || temp.isLoopback()) continue;

            //get adress for interface and check if broadcast is allowed ipv6 dont have for example.
            for (InterfaceAddress ia : temp.getInterfaceAddresses()) {
                InetAddress broadcast = ia.getBroadcast(); //ip with 255 at end
                if (broadcast == null) continue;

                //create UDP packet
                DatagramPacket packet = new DatagramPacket(
                        buffer,
                        buffer.length,
                        broadcast,
                        8888
                );

                socket.send(packet);
                System.out.println("Sent to: " + broadcast.getHostAddress());
            }
        }
        socket.close();
    }



}
