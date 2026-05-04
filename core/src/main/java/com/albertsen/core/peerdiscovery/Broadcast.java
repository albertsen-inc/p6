package com.albertsen.core.peerdiscovery;

import com.albertsen.core.dataObjs.Peer;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.UUID;

public class Broadcast {

    public void sendBroadcast(Peer profile) throws IOException {

        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        //some is temp but need to be created in profile
        String type = "DISCOVER";
        String name = sanitize(profile.getName());
        String id = profile.getID().toString();
        long timestamp = System.currentTimeMillis();
        String nonce = UUID.randomUUID().toString();

        String msg = type + "|" + name + "|" + id + "|" + timestamp + "|" + nonce;
        byte[] buffer = msg.getBytes();



        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            if (!ni.isUp() || ni.isLoopback()) continue;

            for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                InetAddress broadcast = ia.getBroadcast();
                if (broadcast == null) continue;

                DatagramPacket packet = new DatagramPacket(
                        buffer,
                        buffer.length,
                        broadcast,
                        8888
                );

                socket.send(packet);
            }
        }

        socket.close();
    }

    private String sanitize(String input) {
        return input.replace("|", "_");
    }



}
